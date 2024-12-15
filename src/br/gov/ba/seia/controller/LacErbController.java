package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.ErbEquipamento;
import br.gov.ba.seia.entity.LacErb;
import br.gov.ba.seia.entity.LacErbEquipamento;
import br.gov.ba.seia.entity.Legislacao;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoCanalErb;
import br.gov.ba.seia.entity.TipoDelimitacaoTerreno;
import br.gov.ba.seia.entity.TipoModalidadeErb;
import br.gov.ba.seia.enumerator.TipoDelimitacaoEnum;
import br.gov.ba.seia.enumerator.TipoModalidadeEnum;
import br.gov.ba.seia.facade.LacErbServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

/***
 * 
 * @author luis
 * 
 */
@Named("lacErbController")
@ViewScoped
public class LacErbController extends Lac {

	@EJB
	private LacErbServiceFacade lacErbServiceFacade;

	private LacErb lacErb;
	private ErbEquipamento erbEquipamento;
	private ErbEquipamento erbEquipamentoADeletar;

	private List<ErbEquipamento> equipamentosACadastrar, equipamentosAComparar;
	private Collection<LacErbEquipamento> lacErbsEquipamento;

	private Collection<TipoDelimitacaoTerreno> tiposDelimitacaoTerreno;
	private Collection<TipoModalidadeErb> tiposModalidade;
	private Collection<TipoCanalErb> tiposCanalErb;

	private int indiceListaEquipamento;

	private Boolean mostraTelaTorre, mostraTelaEdf, mostraTelaInd, mostraOpcaoOutros, exibirTermos,
	mostraAbaEquipamento, editMode, viewMode, exibeMsg;

	public LacErbController() {
	}

	@PostConstruct
	public void init() {
		this.lacErb = new LacErb();
		this.lacErb.setIdeTipoModalidadeErb(new TipoModalidadeErb());
		this.lacErb.setIdeRequerimento(new Requerimento());
		this.lacErb.setIdeDocumentoObrigatorio(new DocumentoObrigatorio());
		this.erbEquipamento = new ErbEquipamento();
		this.equipamentosACadastrar = new ArrayList<ErbEquipamento>();
		this.equipamentosAComparar = new ArrayList<ErbEquipamento>();
		this.editMode = false;
		this.viewMode = false;
		this.indiceListaEquipamento = -1;// Gerenciar lista de equipamentos não
		// salvos
		this.loadTiposCanalErb();
		this.initTiposDelimitacaoTerreno();
		this.loadTiposModalidade();
		this.carregarLegislacoesLacErb();
		this.carregaLacErbsEquipamento();
		this.clear();
	}

	public void novo() {

		Exception erro = null;
		try {
			LacErb lacErb = this.lacErbServiceFacade.carregarLacErbByIdeRequerimento(this.lacErb.getIdeRequerimento()
					.getIdeRequerimento());
			if (!Util.isNull(lacErb)) {
				this.editar();
			}
		} catch (Exception e) {
			erro = null;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

	}

	public void editar() {
		Exception erro = null;
		try {
			this.editMode = true;
			this.carregarDadosLicenciamentoAmbiental();
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

	}

	public void visualizar() {
		Exception erro = null;
		try {
			this.viewMode = true;
			this.carregarDadosLicenciamentoAmbiental();
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

	}

	private void carregarDadosLicenciamentoAmbiental() throws Exception {
		this.resetarDados();
		this.carregarDadosLacErb();
		this.carregarEquipamentosLacErb();
	}

	private void resetarDados() {
		this.equipamentosACadastrar = new ArrayList<ErbEquipamento>();
		this.mostraAbaEquipamento = this.viewMode || this.editMode;
	}

	private void carregarDadosLacErb() throws Exception {
		this.lacErb = this.lacErbServiceFacade.carregarLacErbByIdeRequerimento(this.lacErb.getIdeRequerimento()
				.getIdeRequerimento());
		this.lacErb.setTipoDelimitacaoTerrenoCollection(this.lacErbServiceFacade
				.carregarTipoDelimitacaoTerrenoByIdeLacErb(this.lacErb.getIdeLac()));
		this.trocarTipoTela(this.lacErb.getIdeTipoModalidadeErb().getIdeTipoModalidadeErb());
		this.marcarTipoDelimitacao((List<TipoDelimitacaoTerreno>) this.lacErb.getTipoDelimitacaoTerrenoCollection());
	}

	private void carregarGridEquipamentos(Set<ErbEquipamento> listaEquipamentos) {
		for (ErbEquipamento erbEquipamento : this.equipamentosACadastrar) {
			for (LacErbEquipamento lacErbEquipamento : this.lacErb.getLacErbEquipamentoCollection()) {
				if (lacErbEquipamento.getLacErbEquipamentoPK().getIdeErbEquipamento()
						.equals(erbEquipamento.getIdeErbEquipamento())) {
					erbEquipamento.getLacErbEquipamentos().add(lacErbEquipamento);
				}
			}
			listaEquipamentos.add(erbEquipamento);
		}

		this.equipamentosAComparar.addAll(listaEquipamentos);
		this.setEquipamentosACadastrar(new ArrayList<ErbEquipamento>(listaEquipamentos));
	}

	private Set<ErbEquipamento> carregarEquipamentosLacErb() throws Exception {
		this.lacErb.setLacErbEquipamentoCollection(this.lacErbServiceFacade
				.carregarLacErbEquipamentoByIdeLacErb(this.lacErb.getIdeLac()));
		Set<ErbEquipamento> listaEquipamentos = new HashSet<ErbEquipamento>();

		for (LacErbEquipamento lacErbEquipamento : this.lacErb.getLacErbEquipamentoCollection()) {
			ErbEquipamento erbEquipamento = this.lacErbServiceFacade.carregarErbEquipamentoById(lacErbEquipamento
					.getLacErbEquipamentoPK().getIdeErbEquipamento());
			erbEquipamento.setLacErbEquipamentos(new ArrayList<LacErbEquipamento>());
			this.equipamentosACadastrar.add(erbEquipamento);
		}

		this.carregarGridEquipamentos(listaEquipamentos);

		return listaEquipamentos;
	}

	public void gerenciarTipoModalidade(ValueChangeEvent event) {
		this.trocarTipoTela(Integer.valueOf(event.getNewValue().toString()));
		this.lacErb.getTipoDelimitacaoTerrenoCollection().clear();
		this.mostraOpcaoOutros = false;
	}

	private void trocarTipoTela(Integer tipo) {
		this.mostraTelaTorre = TipoModalidadeEnum.TORRE.getIde().equals(tipo);
		this.mostraTelaEdf = TipoModalidadeEnum.EDIFICACAO.getIde().equals(tipo);
		this.mostraTelaInd = TipoModalidadeEnum.INDOOR.getIde().equals(tipo);
	}

	@Override
	public void exibirTermos() {
		if (this.isDadosValidados() && !editMode) {
			RequestContext.getCurrentInstance().execute("termos.show()");
		}
	}

	public void salvar() {

		if (this.isDadosValidados() & super.isTermosAceites()) {
			try {
				if (this.hasLac()) {
					JsfUtil.addSuccessMessage("Já existe uma lac registrada pra esse requerimento.");
					return;
				}
				this.preencherDadosLac();
				this.lacErbServiceFacade.salvarLacErb(this.lacErb, this.equipamentosACadastrar, super.legislacoes);
				this.gerenciarModals();
				JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro ao cadastrar Licenciamento Ambiental.");
				this.lacErb.setIdeLac(null);
				return;
			}
		}
	}

	private Boolean hasLac() {
		Exception erro = null;
		try {
			return this.lacErbServiceFacade.hasLac(this.lacErb.getIdeRequerimento().getIdeRequerimento());
		} catch (Exception e) {
			erro = null;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

		return false;
	}

	private void preencherDadosLac() {
		this.lacErb.setDtcCriacao(new Date());
	}

	private void gerenciarModals() {
		RequestContext.getCurrentInstance().execute("termos.hide();");
		RequestContext.getCurrentInstance().execute("rel.show();");
		RequestContext.getCurrentInstance().execute("updateDocumento()");
	}

	public void atualizar() {
		if (this.isDadosValidados()) {
			Exception erro = null;
			try {
				this.lacErbServiceFacade.atualizarLacErb(this.lacErb, this.equipamentosACadastrar,
						this.equipamentosAComparar);
				RequestContext.getCurrentInstance().execute("rel.show();");
				JsfUtil.addSuccessMessage("Licenciamento Ambiental atualizado com sucesso.");
			} catch (Exception e) {
				erro = null;
				JsfUtil.addErrorMessage("Erro ao atualizar Licenciamento Ambiental.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				this.lacErb.setIdeLac(null);
				return;
			}finally{
				if(erro != null) {
					throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
				}
			}

		}
	}

	public StreamedContent getImprimir() {
		try {
			return imprimirRelatorioLac();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			return null;
		}
	}

	public StreamedContent getImprimirCertificado() {
		Exception erro = null;
		try {
			Integer ideRequerimento = this.lacErb.getIdeRequerimento().getIdeRequerimento();

			if (!this.lacErbServiceFacade.hasCertificado(ideRequerimento)) {
				this.lacErb.getIdeRequerimento().setIdeOrgao(new Orgao(1, 1));
				Certificado certificado = this.gerarCertificadoLacErb();
				this.lacErbServiceFacade.salvarCertificadoLacErb(certificado);
				
			} else if (!this.lacErbServiceFacade.hasToken(ideRequerimento)) {
				Certificado certificado = this.lacErbServiceFacade.carregarCertificado(ideRequerimento);
				this.lacErbServiceFacade.atualizarTokenCertificadoLac(certificado);
			}

			return this.imprimirCertificadoLacErb();
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}

	private Certificado gerarCertificadoLacErb() {
		AtoAmbiental atoAmbiental = getAtoAmbientalLac();
		Requerimento requerimento = this.lacErb.getIdeRequerimento();
		return super.gerarCertificado(atoAmbiental, requerimento);
	}

	private StreamedContent imprimirCertificadoLacErb() throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();

		lParametros.put("ide_requerimento", this.lacErb.getIdeRequerimento().getIdeRequerimento());
		return new RelatorioUtil("certificado_lac_tcra.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	public StreamedContent getImprimirRelatorioFinalLac() {
		try {
			this.lacErb = this.lacErbServiceFacade.carregarLacErbByIdeRequerimento(this.lacErb.getIdeRequerimento().getIdeRequerimento());
			return this.imprimirRelatorioLac();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			return null;
		}
	}

	private StreamedContent imprimirRelatorioLac() throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();

		lParametros.put("ide_lac", this.lacErb.getIdeLac());
		lParametros.put("legislacao", this.getCondicionantesFormularioLAC());
		return new RelatorioUtil("lac_erb.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	public boolean isDadosValidados() {
		boolean valido = true;

		if (this.mostraOpcaoOutros && Util.isNullOuVazio(this.lacErb.getDscOutrosTipoDelimitacao())) {
			JsfUtil.addErrorMessage("O campo Descricao do tipo de delimitação é de preenchimento obrigatório.");
			valido = false;
		}

		return valido;
	}

	public void excluirEquipamento() {
		this.equipamentosACadastrar.remove(indiceListaEquipamento);
	}

	@SuppressWarnings("unchecked")
	public void verificarTipoDelimitacao(ValueChangeEvent event) {
		this.marcarTipoDelimitacao((List<TipoDelimitacaoTerreno>) event.getNewValue());
	}

	private void marcarTipoDelimitacao(List<TipoDelimitacaoTerreno> tiposDelimitacaoSelecionados) {
		if (tiposDelimitacaoSelecionados.contains(new TipoDelimitacaoTerreno(TipoDelimitacaoEnum.OUTROS.getIde()))) {
			mostraOpcaoOutros = true;
		} else {
			mostraOpcaoOutros = false;
		}
	}

	public void controlarAbas() {
		if (lacErb.getTipoDelimitacaoTerrenoCollection().isEmpty() && !this.viewMode && !mostraTelaInd) {
			JsfUtil.addErrorMessage("Tipo de delimitação é obrigatório.");
			this.activeTab = 0;
			this.mostraAbaEquipamento = false;
		} else if (!this.viewMode && this.mostraOpcaoOutros && this.lacErb.getDscOutrosTipoDelimitacao().isEmpty()) {
			JsfUtil.addErrorMessage("Descricão do Tipo de delimitação é obrigatório.");
			this.activeTab = 0;
			this.mostraAbaEquipamento = false;
		}

	}

	public void incluirEquipamento() {
		this.getCanaisMarcados();
		if (this.validaEquipamento()) {
			this.erbEquipamento.setIdeErbEquipamento(indiceListaEquipamento--);
			this.equipamentosACadastrar.add(this.erbEquipamento.clone());
			this.erbEquipamento = new ErbEquipamento();
			this.carregaLacErbsEquipamento();
		}
	}

	public void removeEquipamento() {
		this.equipamentosACadastrar.remove(this.erbEquipamentoADeletar);
	}

	private boolean validaEquipamento() {
		boolean valido = true;
		// VALIDAR DADOS

		if (this.erbEquipamento.getLacErbEquipamentos().isEmpty()) {
			JsfUtil.addErrorMessage("Pelo menos um canal é obrigatório.");
			valido = false;
		}

		return valido;
	}

	private void getCanaisMarcados() {
		this.erbEquipamento.setLacErbEquipamentos(new ArrayList<LacErbEquipamento>());
		for (LacErbEquipamento lee : this.lacErbsEquipamento) {
			if (!Util.isNull(lee.getQtdCanais()) && lee.getQtdCanais() > 0) {
				this.erbEquipamento.getLacErbEquipamentos().add(lee.clone());
			}
		}
	}

	public void initTiposDelimitacaoTerreno() {
		try {
			this.tiposDelimitacaoTerreno = this.lacErbServiceFacade.listarTiposDelimitacaoTerreno();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void loadTiposModalidade() {
		try {
			this.tiposModalidade = this.lacErbServiceFacade.listarTipoModalidadeErb();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void loadTiposCanalErb() {
		try {
			this.tiposCanalErb = this.lacErbServiceFacade.listarTipoCanalErb();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void carregarLegislacoesLacErb() {
		Exception erro = null;
		try {
			Collection<Legislacao> legislacoesLacErb = this.lacErbServiceFacade.listarLegislacao();
			super.carregarLegislacoes(legislacoesLacErb);
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

	}

	private void clear() {
		this.mostraTelaTorre = false;
		this.mostraTelaEdf = false;
		this.mostraTelaInd = false;
		this.mostraAbaEquipamento = false;
		this.mostraOpcaoOutros = false;
		this.exibirTermos = false;
		this.exibeMsg = false;
		this.activeTab = 0;
	}

	public void carregaLacErbsEquipamento() {
		try {
			Collection<TipoCanalErb> listarTipoCanalErb = this.lacErbServiceFacade.listarTipoCanalErb();
			Collection<LacErbEquipamento> lista = new ArrayList<LacErbEquipamento>();
			for (TipoCanalErb tp : listarTipoCanalErb) {
				LacErbEquipamento equip = new LacErbEquipamento();
				equip.setTipoCanalErb(tp);
				lista.add(equip);
			}
			this.lacErbsEquipamento = lista;
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	@Override
	protected String getCondicionantesFormularioLAC() {
		return new LacErb().getCondicionantesFormularioLAC();
	}

	@Override
	public int getActiveTab() {
		return activeTab;
	}

	@Override
	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public int getIndiceListaEquipamento() {
		return indiceListaEquipamento;
	}

	public void setIndiceListaEquipamento(int indiceListaEquipamento) {
		this.indiceListaEquipamento = indiceListaEquipamento;
	}

	public LacErb getLacErb() {
		return Util.isNull(lacErb) ? lacErb = new LacErb() : lacErb;
	}

	public void setLacErb(LacErb lacErb) {
		this.lacErb = lacErb;
	}

	public ErbEquipamento getErbEquipamento() {
		return Util.isNull(erbEquipamento) ? erbEquipamento = new ErbEquipamento() : erbEquipamento;
	}

	public void setErbEquipamento(ErbEquipamento erbEquipamento) {
		this.erbEquipamento = erbEquipamento;
	}

	public List<ErbEquipamento> getEquipamentosACadastrar() {
		return Util.isNull(equipamentosACadastrar) ? equipamentosACadastrar = new ArrayList<ErbEquipamento>()
				: equipamentosACadastrar;
	}

	public void setEquipamentosACadastrar(List<ErbEquipamento> equipamentosACadastrar) {
		this.equipamentosACadastrar = equipamentosACadastrar;
	}

	public LacErbServiceFacade getLicenciamentoAmbientalServiceFacade() {
		return lacErbServiceFacade;
	}

	public void setLicenciamentoAmbientalServiceFacade(LacErbServiceFacade licenciamentoAmbientalServiceFacade) {
		this.lacErbServiceFacade = licenciamentoAmbientalServiceFacade;
	}

	public List<ErbEquipamento> getEquipamentosAComparar() {
		return equipamentosAComparar;
	}

	public void setEquipamentosAComparar(List<ErbEquipamento> equipamentosAComparar) {
		this.equipamentosAComparar = equipamentosAComparar;
	}

	public Collection<LacErbEquipamento> getLacErbsEquipamento() {
		return lacErbsEquipamento;
	}

	public void setLacErbsEquipamento(Collection<LacErbEquipamento> lacErbsEquipamento) {
		this.lacErbsEquipamento = lacErbsEquipamento;
	}

	public Collection<TipoDelimitacaoTerreno> getTiposDelimitacaoTerreno() {
		return tiposDelimitacaoTerreno;
	}

	public void setTiposDelimitacaoTerreno(Collection<TipoDelimitacaoTerreno> tiposDelimitacaoTerreno) {
		this.tiposDelimitacaoTerreno = tiposDelimitacaoTerreno;
	}

	public Collection<TipoModalidadeErb> getTiposModalidade() {
		return tiposModalidade;
	}

	public void setTiposModalidade(Collection<TipoModalidadeErb> tiposModalidade) {
		this.tiposModalidade = tiposModalidade;
	}

	public Collection<TipoCanalErb> getTiposCanalErb() {
		return tiposCanalErb;
	}

	public void setTiposCanalErb(Collection<TipoCanalErb> tiposCanalErb) {
		this.tiposCanalErb = tiposCanalErb;
	}

	public Boolean getViewMode() {
		return viewMode;
	}

	public void setViewMode(Boolean viewMode) {
		this.viewMode = viewMode;
	}

	public Boolean getMostraTelaTorre() {
		return mostraTelaTorre;
	}

	public Boolean getExibeMsg() {
		return exibeMsg;
	}

	public void setExibeMsg(Boolean exibeMsg) {
		this.exibeMsg = exibeMsg;
	}

	public void setMostraTelaTorre(Boolean mostraTelaTorre) {
		this.mostraTelaTorre = mostraTelaTorre;
	}

	public Boolean getMostraTelaEdf() {
		return mostraTelaEdf;
	}

	public void setMostraTelaEdf(Boolean mostraTelaEdf) {
		this.mostraTelaEdf = mostraTelaEdf;
	}

	public Boolean getMostraTelaInd() {
		return mostraTelaInd;
	}

	public void setMostraTelaInd(Boolean mostraTelaInd) {
		this.mostraTelaInd = mostraTelaInd;
	}

	public Boolean getMostraAbaEquipamento() {
		return mostraAbaEquipamento;
	}

	public void setMostraAbaEquipamento(Boolean mostraAbaEquipamento) {
		this.mostraAbaEquipamento = mostraAbaEquipamento;
	}

	public Boolean getMostraOpcaoOutros() {
		return mostraOpcaoOutros;
	}

	public void setMostraOpcaoOutros(Boolean mostraOpcaoOutros) {
		this.mostraOpcaoOutros = mostraOpcaoOutros;
	}

	public Boolean getExibirTermos() {
		return exibirTermos;
	}

	public void setExibirTermos(Boolean exibirTermos) {
		this.exibirTermos = exibirTermos;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public ErbEquipamento getErbEquipamentoADeletar() {
		return erbEquipamentoADeletar;
	}

	public void setErbEquipamentoADeletar(ErbEquipamento erbEquipamentoADeletar) {
		this.erbEquipamentoADeletar = erbEquipamentoADeletar;
	}

}
