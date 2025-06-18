package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.DeclaracaoIntervencaoAppController;
import br.gov.ba.seia.dto.CaracteristcaAtividadeIntervencaoAppDTO;
import br.gov.ba.seia.entity.AtividadeIntervencaoApp;
import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.CaracteristicaAtividadeIntervencaoApp;
import br.gov.ba.seia.entity.CaracteristicaIntervencaoApp;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoApp;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoAppCaracteristca;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.service.AtividadeIntervencaoAppService;
import br.gov.ba.seia.service.CaracteristicaAtividadeIntervencaoAppService;
import br.gov.ba.seia.service.CaracteristicaIntervencaoAppService;
import br.gov.ba.seia.service.DeclaracaoIntervencaoAppCaracteristcaService;
import br.gov.ba.seia.service.DeclaracaoIntervencaoAppService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.RequerimentoPessoaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 11/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoIntervencaoAppFacade {

	@EJB
	private DeclaracaoIntervencaoAppService declaracaoIntervencaoAppService;
	
	@EJB
	private DeclaracaoIntervencaoAppCaracteristcaService declaracaoIntervencaoAppCaracteristcaService;
	
	@EJB
	private CaracteristicaAtividadeIntervencaoAppService caracteristicaAtividadeIntervencaoAppService;
	
	@EJB
	private CaracteristicaIntervencaoAppService caracteristicaIntervencaoAppService;
	
	@EJB
	private AtividadeIntervencaoAppService atividadeIntervencaoAppService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private AtoDeclaratorioFacade atosDeclaratorioFacade;

	@EJB
	private RequerimentoPessoaService requerimentoPessoaService;

	@EJB
	private PessoaService pessoaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaIntervencaoApp> listarCaracteristicaIntervencaoApp() throws Exception{
		return caracteristicaIntervencaoAppService.listarAtivos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtividadeIntervencaoApp> listarAtividadeByCaracteristica(CaracteristicaIntervencaoApp caracteristicaIntervencaoApp) throws Exception{
		return atividadeIntervencaoAppService.listarBy(caracteristicaIntervencaoApp);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isLocalizacaoGeograficaSalva(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		if(!Util.isNullOuVazio(localizacaoGeografica)){
			return !Util.isNullOuVazio(atosDeclaratorioFacade.retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica));
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaLocalizacaoGeografica(List<LocalizacaoGeografica> lista) throws Exception {
		atosDeclaratorioFacade.excluirListaLocalizacaoGeografica(lista);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		atosDeclaratorioFacade.excluirLocalizacaoGeografica(localizacaoGeografica);
	}

	/**
	 * Método para persistir a {@link DeclaracaoIntervencaoApp}
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param declaracaoIntervencaoApp
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DeclaracaoIntervencaoAppController controller) {
		try {
			salvarDIAP(controller.getDeclaracaoIntervencaoApp());
			desmontarDTO(controller.getDeclaracaoIntervencaoApp(), controller.getListaCaracteristicaDTO());
			if(!Util.isNullOuVazio(controller.getDeclaracaoIntervencaoApp().getDeclaracaoIntervencaoAppCaracteristcas())){
				declaracaoIntervencaoAppCaracteristcaService.salvar(controller.getDeclaracaoIntervencaoApp(), controller.getDeclaracaoIntervencaoApp().getDeclaracaoIntervencaoAppCaracteristcas());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar a DIAP.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	/**
	 * Método para salvar ou atulizar a {@link DeclaracaoIntervencaoApp}. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 08/02/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8501">#8501</a>
	 * @param diap
	 * @throws Exception
	 */
	public void salvarDIAP(DeclaracaoIntervencaoApp diap) throws Exception{
		atosDeclaratorioFacade.salvarAtoDeclaratorio(diap.getIdeAtoDeclaratorio());
		declaracaoIntervencaoAppService.salvar(diap);
	}
	
	/**
	 * Método para concluir a {@link DeclaracaoIntervencaoApp}
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param declaracaoIntervencaoAppController
	 */
	public void concluir(DeclaracaoIntervencaoAppController controller) {
		try {
			atosDeclaratorioFacade.finalizarAtoDeclaratorio(controller.getDeclaracaoIntervencaoApp().getIdeAtoDeclaratorio());
			declaracaoIntervencaoAppService.salvar(controller.getDeclaracaoIntervencaoApp());
			desmontarDTO(controller.getDeclaracaoIntervencaoApp(), controller.getListaCaracteristicaDTO());
			declaracaoIntervencaoAppCaracteristcaService.salvar(controller.getDeclaracaoIntervencaoApp(), controller.getDeclaracaoIntervencaoApp().getDeclaracaoIntervencaoAppCaracteristcas());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao concluir a DIAP.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes 
	 * @since 12/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param diap 
	 * @param listaCaracteristicaDTO
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void desmontarDTO(DeclaracaoIntervencaoApp diap, List<CaracteristcaAtividadeIntervencaoAppDTO> listaCaracteristicaDTO) throws Exception {
		for (CaracteristcaAtividadeIntervencaoAppDTO dto : listaCaracteristicaDTO) {
			if(dto.isSelecionado()){
				for(DeclaracaoIntervencaoAppCaracteristca diac : dto.getListaDeclaracaoIntervencaoAppCaracteristca()){
					if(!Util.isNullOuVazio(diac.getIdeDeclaracaoIntervencaoAppCaracteristica())){
						diac.setIdeDeclaracaoIntervencaoAppCaracteristica(null);
					}
					if(diac.getCaracteristicaAtividadeIntervencaoApp().isOutrasAtividades()){
						diac.setDesCaminhoArquivoDecreto(dto.getDesCaminhoArquivoDecreto());
					}
					diac.setCaracteristicaAtividadeIntervencaoApp(buscar(diac.getCaracteristicaAtividadeIntervencaoApp()));
					diac.getCaracteristicaAtividadeIntervencaoApp().getAtividadeIntervencaoApp().setSelecionado(true);
					diap.getDeclaracaoIntervencaoAppCaracteristcas().add(diac);
				}
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private CaracteristicaAtividadeIntervencaoApp buscar(CaracteristicaAtividadeIntervencaoApp caracteristicaAtividadeIntervencaoApp) throws Exception{
		return buscar(caracteristicaAtividadeIntervencaoApp.getCaracteristicaIntervencaoApp(), caracteristicaAtividadeIntervencaoApp.getAtividadeIntervencaoApp());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CaracteristicaAtividadeIntervencaoApp buscar(CaracteristicaIntervencaoApp caracteristicaIntervencaoApp, AtividadeIntervencaoApp atividadeIntervencaoApp) throws Exception{
		return caracteristicaAtividadeIntervencaoAppService.buscar(caracteristicaIntervencaoApp, atividadeIntervencaoApp);
	}
	
	/**
	 * ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristcaAtividadeIntervencaoAppDTO> prepararDTO(DeclaracaoIntervencaoApp declaracaoIntervencaoApp) {
		try {
			List<CaracteristcaAtividadeIntervencaoAppDTO> listaDTO = new ArrayList<CaracteristcaAtividadeIntervencaoAppDTO>();
			for(CaracteristicaIntervencaoApp caracteristicaIntervencaoApp : listarCaracteristicaIntervencaoApp()){
				CaracteristcaAtividadeIntervencaoAppDTO dto = new CaracteristcaAtividadeIntervencaoAppDTO(declaracaoIntervencaoApp);
				dto.setCaracteristica(caracteristicaIntervencaoApp);
				dto.setListaAtividade(listarAtividadeByCaracteristica(caracteristicaIntervencaoApp));
				listaDTO.add(dto);
			}
			return listaDTO;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent getDocumentoAdicionalUpado(String dscCaminhoArquivo) throws Exception {
		return gerenciaArquivoService.getContentFile(dscCaminhoArquivo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarArquivo(String dscCaminhoArquivo){
		gerenciaArquivoService.deletarArquivo(dscCaminhoArquivo);
	}

	/**
	 * Método para buscar a {@link DeclaracaoIntervencaoApp} de acordo com o parâmetro.
	 * 
	 * @author eduardo.fernandes 
	 * @since 12/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param requerimento
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoIntervencaoApp buscarDiap(Requerimento requerimento) throws Exception {
		return declaracaoIntervencaoAppService.buscar(requerimento);
	}
	
	public String montarTextoRepresentadoPor(Requerimento requerimento) throws Exception {
		Integer ideRequerimento = requerimento.getIdeRequerimento();

		requerimento.setRequerimentoPessoaCollection(
				requerimentoPessoaService.listarEnvolvidosRequerimento(ideRequerimento));

		boolean isPessoaFisica = false;
		String representantes = "";
		
		
		for (RequerimentoPessoa rp : requerimento.getRequerimentoPessoaCollection()) {
			if(!Util.isNullOuVazio(rp.getPessoa().getPessoaFisica())) {
				isPessoaFisica = true;
			}
			break;
		}
		
		if(!isPessoaFisica) {
			representantes = ", representado por ";
		}
		
		for (RequerimentoPessoa rp : requerimento.getRequerimentoPessoaCollection()) {
			if (!rp.getIdeTipoPessoaRequerimento()
					.equals(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId()))
					&& !rp.getIdeTipoPessoaRequerimento()
							.equals(new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.ATENDENTE.getId()))) {

				PessoaFisica procuradorOuRepresentante = rp.getPessoa().getPessoaFisica();

				Pessoa p = pessoaService.carregarDadosTipoPessoaRequerimento(ideRequerimento,
						rp.getIdeTipoPessoaRequerimento(), rp.getPessoa().getPessoaFisica());
				if (Util.isNullOuVazio(p)) {
					JsfUtil.addErrorMessage(
							"Falha ao gerar o certificado. Verifique se os dados básicos e endereço do responsável /representante legal estão preenchidos.");
				}else {
					if (Util.isNullOuVazio(p.getEndereco())) {
						JsfUtil.addWarnMessage(
								"Será necessário preencher o endereço do representante legal para que o certificado seja emitido.");
						throw Util.capturarException(new Exception(), Util.SEIA_EXCEPTION);
					}
					representantes += procuradorOuRepresentante.getNomPessoa();
					representantes += ", residente à " + p.getEndereco().getEnderecoCompletoSemPais();
					representantes += ", nacionalidade " + procuradorOuRepresentante.getIdePais().getNomPais();
					if (!Util.isNullOuVazio(procuradorOuRepresentante.getIdeOcupacao())) {
						representantes += ", profissão " + procuradorOuRepresentante.getIdeOcupacao().getNomOcupacao();
					}
					representantes += ", CPF " + procuradorOuRepresentante.getNumCpfFormatado() + ", ";

				}

			}

		}
		List<AtividadeIntervencaoApp> listAtividadeIntervencaoApp = atividadeIntervencaoAppService
				.listarPor(requerimento);
		
		StringBuilder msg = new StringBuilder();
		
		msg.append(representantes);
		
		int count = 0;
		
		if (!Util.isNullOuVazio(listAtividadeIntervencaoApp)) {
			
			
			if(!isPessoaFisica) {
				msg.append( " declara que realizará a INTERVENÇÃO EM ÁREA DE PRESERVAÇÃO PERMANENTE: ");
			}else {
				msg.append( ": ");
			}	
			
			for (AtividadeIntervencaoApp aia : listAtividadeIntervencaoApp ) {
				 count ++;
				 if (count >1 && count == listAtividadeIntervencaoApp.size()  ){				
				 msg.append("e");	
				 
				}
				msg.append(" ");
				msg.append(aia.getDesAtividadeIntervencaoApp());
				msg.append("; ");
				
			}
		}

		return msg.toString();
	}
	
}
