package br.gov.ba.seia.controller;

import static br.gov.ba.seia.util.fce.FceUtil.duplicarFce;

import static br.gov.ba.seia.util.fce.FceUtil.isFceLicenciamentoAquicultura;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.AnaliseAtoDTO;
import br.gov.ba.seia.dto.DadoConcedidoFceImpl;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.AnaliseTecnicaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.factory.FceFlorestalControllerFactory;
import br.gov.ba.seia.interfaces.DadoConcedidoInterface;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceUtil;

@Named("processoAnaliseTecnicaController")
@ViewScoped
public class ProcessoAnaliseTecnicaController extends SuperProcessoAnaliseTecnicaController {

	protected Collection<AnaliseAtoDTO> listaAnaliseAto;

	@EJB
	private AnaliseTecnicaServiceFacade analiseTecnicaServiceFacade;

	@EJB
	private FceFlorestalControllerFactory fceFlorestalControllerFactory;

	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;

	private Boolean indAvancar;

	@Override
	public void load(VwConsultaProcesso vwProcesso) {
		inicializar(vwProcesso);
	}

	public void load() {
		inicializar(vwProcesso);
	}

	private void inicializar(VwConsultaProcesso vwProcesso) {
		try {
			super.load(vwProcesso);
			if (!super.isExisteAnaliseTecnica()) {
				/*
				 * #7470 Os registros de analise técnica deverão ser gerados ao clicar na
				 * funcionalidade
				 */
				super.analiseTecnica = new AnaliseTecnica(vwProcesso.getIdeProcesso());
				super.analiseTecnicaServiceFacade.salvarAnaliseTecnica(super.analiseTecnica);
			}

			if (isNaoExisteFceParaAnaliseTecnica()) {
				criarFceParaAnliseTecnica();
			}

			super.ordenarListaFce();

			Integer idePessoaFisica = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
			listaAnaliseAto = super.analiseTecnicaServiceFacade.listarAnaliseAtoDTO(vwProcesso.getIdeProcesso(),
					idePessoaFisica);
			listaOutorgasConcluidos = new ArrayList<Fce>(listaFce);

			removerFceDeOutroTecnico();
			ajustarOutorgasConcluidos();

			rc().addPartialUpdateTarget("pnlAnaliseTecnica");
			rc().execute("dlgAnaliseTecnica.show();");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void ajustarOutorgasConcluidos() {

		if (!Util.isNullOuVazio(listaOutorgasConcluidos) && Util.isNullOuVazio(listaDadoConcedido)) {
			listaDadoConcedido = null;

		} else if (Util.isNullOuVazio(listaOutorgasConcluidos) && !Util.isNullOuVazio(listaDadoConcedido)) {
			return;

		} else if (!Util.isNullOuVazio(listaOutorgasConcluidos) && !Util.isNullOuVazio(listaDadoConcedido)) {
			for (Fce fce : listaOutorgasConcluidos) {
				for (DadoConcedidoInterface dadoConcedido : listaDadoConcedido) {

					if (dadoConcedido instanceof DadoConcedidoFceImpl) {

						DadoConcedidoFceImpl pDadoConcedido = (DadoConcedidoFceImpl) dadoConcedido;

						if (fce.equals(pDadoConcedido.getFce())) {
							listaDadoConcedido.remove(dadoConcedido);
							break;
						}
					}
				}
			}
		}

		super.preparaListaDadoConcedidoFce();
	}

	/**
	 * Método que remove os {@link Fce} que não devem ser analisados pelo Técnico.
	 * 
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @throws Exception
	 * @since 14/03/2016
	 */
	private void removerFceDeOutroTecnico() throws Exception {
		if (!Util.isNullOuVazio(listaAnaliseAto)) {
			List<DocumentoObrigatorio> docsToAnalisar = obterDocumentosObrigatoriosByProcessoAto();
			List<Fce> listaTemp = new ArrayList<Fce>();
			listaTemp.addAll(super.listaFce);
			for (Fce f : listaTemp) {
				if (isNotFceParaAnaliseDoTecnico(docsToAnalisar, f.getIdeDocumentoObrigatorio())) {
					super.listaFce.remove(f);
				}
			}
		}
	}

	/**
	 * Método que verifica se aquele {@link DocumentoObrigatorio} está na lista para
	 * análise.
	 * 
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @param ideDocumentoObrigatorio
	 * @param listaDocsParaAnalise
	 * @return
	 * @since 14/03/2016
	 */
	private boolean isNotFceParaAnaliseDoTecnico(List<DocumentoObrigatorio> listaDocsParaAnalise,
			DocumentoObrigatorio ideDocumentoObrigatorio) {
		return !Util.isNullOuVazio(listaDocsParaAnalise) && !listaDocsParaAnalise.contains(ideDocumentoObrigatorio);
	}

	/**
	 * Método para obter os {@link DocumentoObrigatorio} de acotdo com a lista de
	 * {@link AnaliseAtoDTO}.
	 * 
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @throws Exception
	 * @since 14/03/2016
	 */
	public List<DocumentoObrigatorio> obterDocumentosObrigatoriosByProcessoAto() throws Exception {
		return super.analiseTecnicaServiceFacade.listarDocumentoObrigatorioByProcessoAto(listaAnaliseAto);
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @throws Exception
	 * @since 02/03/2016
	 */
	private boolean isNaoExisteFceParaAnaliseTecnica() throws Exception {

		Collection<Fce> fcesTecnicos = analiseTecnicaServiceFacade.listarFceComExcluido(analiseTecnica);

		if (Util.isNullOuVazio(fcesTecnicos)) {
			return true;

		} else {
			Collection<Fce> fces = removerFces(listarFceDoProcesso());

			if (fces.size() >= fcesTecnicos.size()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param vwProcesso
	 * @throws Exception
	 * @since 17/02/2016
	 */
	private void criarFceParaAnliseTecnica() throws Exception {
		analiseTecnicaServiceFacade.criarFceParaAnliseTecnica(this);
	}

	public void duplicarFceParaAnliseTecnica() throws Exception {

		boolean podeDuplicar = true;
		
		if (Util.isNullOuVazio(super.listaFce)) {
			super.listaFce = analiseTecnicaServiceFacade.listarFceComExcluido(analiseTecnica);
		}

		Collection<Fce> fces = removerFces(listarFceDoProcesso());
		
		ArrayList<Fce> fcesDuplicadas = new ArrayList();

		if (fces.size() > super.listaFce.size() && listaFce.size() > 0) {
			fces = removerFces(listarFceDoProcessoReenquadramento());
		}

		for (ProcessoAto processoAto : this.vwProcesso.getListProcessoAto()) {
			for (Fce fce : fces) {
				if (documentoObrigatorioService.isDocumentoObrigatorioAto(fce.getIdeDocumentoObrigatorio(),
						processoAto.getAtoAmbiental(), processoAto.getTipologia())) {
					if(fcesDuplicadas.size() > 0) {
						for(Fce fceDuplicada: fcesDuplicadas) {
							if(fce.getIdeFce() == fceDuplicada.getIdeFce()) {
								podeDuplicar = false;
							}
						}
					}
					
					if(podeDuplicar) {
						duplicarFce(fce, super.analiseTecnica);
						fcesDuplicadas.add(fce);
					}
					listaFce = analiseTecnicaServiceFacade.listarFce(analiseTecnica);
					podeDuplicar = true;
				}
			}
		}

		removerFceDeOutroTecnico();

		super.preparaListaDadoConcedidoFce();
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @throws Exception
	 * @since 02/03/2016
	 */
	private Collection<Fce> listarFceDoProcesso() throws Exception {
		return super.analiseTecnicaServiceFacade.listarFce(super.vwProcesso.getIdeProcesso(),
				DadoOrigemEnum.REQUERIMENTO, DadoOrigemEnum.REENQUADRAMENTO);
	}

	private Collection<Fce> listarFceDoProcessoReenquadramento() throws Exception {
		return super.analiseTecnicaServiceFacade.listarFceDoProcessoReenquadramento(super.vwProcesso.getIdeProcesso());
	}

	/**
	 * O <b>FCE - Licenciamento para Aquicultura</b> ainda não poderá ser Analisado
	 * como os demais. Remover este método após implementação da duplicação do
	 * referido FCE.
	 * 
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fces
	 * @return
	 * @throws Exception
	 * @since 03/02/2016
	 */
	private Collection<Fce> removerFces(Collection<Fce> fces) throws Exception {
		List<Fce> fcesToRemove = new ArrayList<Fce>();
		for (Fce f : fces) {
			if (isNotFceDeOutorga(f.getIdeDocumentoObrigatorio())) {
				fcesToRemove.add(f);
			}
		}
		if (!Util.isNullOuVazio(fcesToRemove)) {
			for (Fce fToRemove : fcesToRemove) {
				fces.remove(fToRemove);
			}
		}

		Collection<Fce> listaFce = analiseTecnicaServiceFacade.listarFceComExcluido(analiseTecnica);
		for (Iterator<?> iterator = fces.iterator(); iterator.hasNext();) {
			Fce fce = (Fce) iterator.next();

			for (Fce fceRemover : listaFce) {
				if (fce.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()
						.equals(fceRemover.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())) {
					iterator.remove();
				}
			}
		}

		return fces;
	}

	/**
	 * 2. Não listar os FCEs que não são de outorga (Licenciamento para Aquicultura,
	 * Industria, Turismo e ASV)
	 * 
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @param documentoObrigatorio
	 * @return
	 * @since 07/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7470">#7470</a>
	 */
	private boolean isNotFceDeOutorga(DocumentoObrigatorio documentoObrigatorio) {
		return isFceLicenciamentoAquicultura(documentoObrigatorio);

		/*
		 * 06/12/2017 || isFceAsv(documentoObrigatorio)
		 * 
		 * 19/06/2017 || isFceTurismo(documentoObrigatorio)
		 *
		 * 20/02/2017 || isFceIndustria(documentoObrigatorio)
		 */
	}

	public void abrirFce(DadoConcedidoInterface dadoConcedido) throws Exception {
		if (isLicenca(dadoConcedido)) {
			if (!isOutorgasConcluidos()) {
				JsfUtil.addWarnMessage(Util.getString("msg_aviso_analise_outorga_pendente"));
			} else {
				FceUtil.abrirFce((DadoConcedidoFceImpl) dadoConcedido);
			}
		} else {
			FceUtil.abrirFce((DadoConcedidoFceImpl) dadoConcedido);
		}
	}

	public void abrirDadoConcedido(DadoConcedidoInterface dadoConcedido) {
		super.abrirDadoConcedido(dadoConcedido, false);
	}

	public void salvar() {
		try {
			analiseTecnicaServiceFacade.salvarAnaliseTecnica(listaAnaliseAto);
			JsfUtil.addSuccessMessage(Util.getString("analise_tecnica_msg_salvar"));
		} catch (SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("analise_tecnica_msg_erro_salvar"));
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean validarAtosFlorestais() {

		if (!Util.isNullOuVazio(listaAnaliseAto)) {

			for (AnaliseAtoDTO analiseAto : listaAnaliseAto) {

				if (analiseAto.getProcessoAto().getAtoAmbiental().getIdeTipoAto().getIdeTipoAto().equals(3)) {
					return true;
				}
			}
		}

		return false;
	}

	public void confirmar() {
		if (!Util.isNullOuVazio(this.indAvancar)) {
			super.analiseTecnica.setIndDevidamentePreenchido(indAvancar);
			super.analiseTecnicaServiceFacade.atualizarAnaliseTecnica(super.analiseTecnica);
			if (this.indAvancar) {
				confirmarEnviarModal();
			}
			rc().execute("dlgConfirmarFimAnaliseTecnicaModal.hide();");
		}
	}

	public void enviar() {

		if (validarAtosFlorestais()) {
			rc().execute("dlgConfirmarFimAnaliseTecnicaModal.show();");
			rc().execute("dlgConfirmarFimAnaliseTecnica.hide();");
			this.indAvancar = null;
			Html.atualizar("idDlgConfirmarFimAnaliseTecnicaModal");
		} else {
			confirmarEnviar();
		}

	}

	public void confirmarEnviarModal() {
		boolean ok = false;
		try {
			if (validarStatusAnaliseConcluido()) {

				super.analiseTecnicaServiceFacade.salvarAnaliseTecnica(listaAnaliseAto);
				super.analiseTecnicaServiceFacade.enviarAnaliseTecnica(super.vwProcesso.getIdeProcesso(),
						listaAnaliseAto);
				JsfUtil.addSuccessMessage(Util.getString("analise_tecnica_msg_enviar"));
				rc().execute("atualizarPauta();");
				ok = true;
			} else {
				throw new SeiaValidacaoRuntimeException(
						"Para enviar o processo é necessário que todos os dados concedidos tenham a análise concluída!");
			}
		} catch (SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("analise_tecnica_msg_erro_enviar"));
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		rc().execute("dlgConfirmarFimAnaliseTecnica.hide();");
		if (ok) {
			rc().execute("dlgAnaliseTecnica.hide();");
		}
	}

	public void confirmarEnviar() {
		boolean ok = false;
		try {
			if (validarStatusAnaliseConcluido()) {
				super.analiseTecnicaServiceFacade.salvarAnaliseTecnica(listaAnaliseAto);
				super.analiseTecnicaServiceFacade.enviarAnaliseTecnica(super.vwProcesso.getIdeProcesso(),
						listaAnaliseAto);
				JsfUtil.addSuccessMessage(Util.getString("analise_tecnica_msg_enviar"));
				rc().execute("atualizarPauta();");
				ok = true;
			} else {
				throw new SeiaValidacaoRuntimeException(
						"Para enviar o processo é necessário que todos os dados concedidos tenham a análise concluída!");
			}
		} catch (SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("analise_tecnica_msg_erro_enviar"));
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		rc().execute("dlgConfirmarFimAnaliseTecnica.hide();");
		if (ok) {
			rc().execute("dlgAnaliseTecnica.hide();");
		}
	}

	public boolean validarStatusAnaliseConcluido() {

		if (!Util.isNullOuVazio(listaDadoConcedido)) {

			for (DadoConcedidoInterface dadoConcedido : listaDadoConcedido) {

				if (!"Concluído".equalsIgnoreCase(dadoConcedido.getStatus()) && dadoConcedido.isVisivel()) {
					return false;
				}
			}
		}

		return true;
	}

	public Collection<AnaliseAtoDTO> getListaAnaliseAto() {
		return listaAnaliseAto;
	}

	public void setListaAnaliseAto(Collection<AnaliseAtoDTO> listaAnaliseAto) {
		this.listaAnaliseAto = listaAnaliseAto;
	}

	public boolean isLicenca(DadoConcedidoInterface dadoConcedido) {
		Fce fce = ((DadoConcedidoFceImpl) dadoConcedido).getFce();
		if (Util.isNullOuVazio(fce.getDocumentoAto())) {
			return false;
		} else {
			return fce.getDocumentoAto().getIdeAtoAmbiental().getIdeTipoAto().getIdeTipoAto()
					.equals(TipoAtoEnum.LICENCA.getId());
		}
	}

	public boolean isOutorgasConcluidos() {
		boolean concluido = true;

		for (Fce f : listaOutorgasConcluidos) {
			// outorga
			if (AtoAmbientalEnum.OUTORGA.getId()
					.equals(f.getDocumentoAto().getIdeAtoAmbiental().getIdeAtoAmbiental())) {
				if (Util.isNull(f.getIndConcluido()) || !f.getIndConcluido()) {
					concluido = false;
					break;
				}
			}
		}

		return concluido;
	}

	public Boolean getIndAvancar() {
		return indAvancar;
	}

	public void setIndAvancar(Boolean indAvancar) {
		this.indAvancar = indAvancar;
	}

}
