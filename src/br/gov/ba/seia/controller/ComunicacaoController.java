package br.gov.ba.seia.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.Comunicacao;
import br.gov.ba.seia.entity.ComunicacaoPerfil;
import br.gov.ba.seia.entity.ComunicacaoStatus;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.enumerator.ComunicacaoStatusEnum;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ComunicacaoService;
import br.gov.ba.seia.service.ComunicacaoStatusService;
import br.gov.ba.seia.service.PerfilService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.DataUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;

@Named("comunicacaoController")
@ViewScoped
public class ComunicacaoController {

	private List<Perfil> perfilList;
	private DualListModel<Perfil> perfilDualList;
	private Perfil perfilSelecionado;
	private String descTitulo;
	private Date dtPeriodoInicial;
	private Date dtPeriodoFinal;
	private String tpComunicacao;
	private String situacao;
	private Comunicacao comunicacao;
	private boolean disabledField;
	private Date dtAtual;

	private List<Comunicacao> comunicacaoList;
	private List<ComunicacaoStatus> comunicacaoStatusList;
	private ComunicacaoStatus comunicacaoStatusSelecionado;
	private LazyDataModel<Comunicacao> comunicacaoLDM;
	private Integer textSize;
	@EJB
	private PerfilService perfilService;
	@EJB
	private ComunicacaoService comunicacaoService;
	@EJB
	private ComunicacaoStatusService comunicacaoStatusService;

	public void limparTela() {
		dtAtual = DataUtil.getDataAtual();
		tpComunicacao = "";
		comunicacao = new Comunicacao();
		comunicacao.setDtcCriacao(DataUtil.getDataAtual());
		comunicacao.setComunicacaoPerfilCollection(new ArrayList<ComunicacaoPerfil>());
		situacao = "";
		perfilList = (List<Perfil>) perfilService.filtrarListaPerfis(null);
		comunicacaoStatusList = comunicacaoStatusService.findAll();
		comunicacaoStatusSelecionado = null;
		perfilDualList = new DualListModel<Perfil>();
		perfilDualList.setSource(perfilList);
		perfilDualList.setTarget(new ArrayList<Perfil>());
		dtPeriodoFinal = null;
		dtPeriodoInicial = null;
		textSize = 1000;
		descTitulo = "";
		perfilSelecionado = null;
	}

	public void onChangeTpComunicacao() {
		textSize = 500;
		if (comunicacao.getTpComunicacao().equals("N")) {
			textSize = 1000;
		}
	}

	public String salvar() {
		try {
			comunicacaoService.verificarCampos(comunicacao, perfilDualList.getTarget());
			if (comunicacao.getTpComunicacao().equals("T") && comunicacao.isIndAtiva()
					&& comunicacaoService.verificarComunicacaoTemporaria(comunicacao, perfilDualList.getTarget())) {
				JsfUtil.addErrorMessage("Já existe uma Comunicação Temporária para esta Data");
				return "";
			}
			comunicacao.setComunicacaoPerfilCollection(new ArrayList<ComunicacaoPerfil>());
			for (Perfil pf : perfilDualList.getTarget()) {
				ComunicacaoPerfil comP = new ComunicacaoPerfil(pf);
				comunicacao.getComunicacaoPerfilCollection().add(comP);
			}

			comunicacaoService.salvarOuAtualizar(comunicacao);
			JsfUtil.addSuccessMessage("O registro foi salvo com sucesso!");

		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			return "";
		}
		return "/paginas/comunicacao/consulta.xhtml?faces-redirect=true";

	}

	@PostConstruct
	public void init() {

		limparTela();
		final Object obj = ContextoUtil.getContexto().getObject();
		if (!Util.isNullOuVazio(obj) && obj instanceof Comunicacao) {
			comunicacao = (Comunicacao) obj;
			ContextoUtil.getContexto().setObject(null);
			carregarPerfilDualList(comunicacao);
		}
		consultar();

	}

	public String novo() {
		return "/paginas/comunicacao/manter-comunicacao.xhtml?faces-redirect=true";
	}

	public String editarComunicacao(Comunicacao comunicacaoEditar) {
		comunicacao = comunicacaoService.find(comunicacaoEditar.getIdeComunicacao());

		if (!Util.isNullOuVazio(comunicacao)) {

			return "/paginas/comunicacao/manter-comunicacao.xhtml?faces-redirect=true";

		}
		JsfUtil.addErrorMessage("Registro não existe");
		return "";
	}

	private void carregarPerfilDualList(Comunicacao com) {
		perfilList = (List<Perfil>) perfilService.filtrarListaPerfis(null);
		List<Perfil> perfilExistente = new ArrayList<Perfil>();
		for (ComunicacaoPerfil comP : com.getComunicacaoPerfilCollection()) {
			perfilExistente.add(comP.getIdePerfil());
		}
		perfilList.removeAll(perfilExistente);
		perfilDualList.setSource(perfilList);
		perfilDualList.setTarget(perfilExistente);
	}

	public void carregarComunicacao(Comunicacao com) {
		if(!Util.isNullOuVazio(com.getIdeComunicacao())) {
			com = comunicacaoService.find(com.getIdeComunicacao());
		}
		this.comunicacao = com;
		carregarPerfilDualList(this.comunicacao);

	}

	public void confirmarExclusao() {
		try {

			if (comunicacaoService.excluir(comunicacao)) {

				JsfUtil.addSuccessMessage("Registro excluído com sucesso.");
			}
			consultar();
		} catch (Exception e) {

			JsfUtil.addErrorMessage("Houve um erro na solicitação");
		}
	}

	public void confirmarCancelamento() {
		try {
			if (comunicacaoService.excluir(comunicacao)) {

				JsfUtil.addSuccessMessage("Registro cancelado com sucesso.");
			}
			consultar();

		} catch (Exception e) {

			JsfUtil.addErrorMessage("Houve um erro na solicitação");
		}
	}

	public void consultar() {
		comunicacaoLDM = new LazyDataModel<Comunicacao>() {

			private static final long serialVersionUID = -549249300009769836L;

			@Override
			public List<Comunicacao> load(int first, int pageSize, String sortField, SortOrder sortOrderder,
					Map<String, String> fields) {
				return comunicacaoService.listByFilterPaginator(descTitulo, perfilSelecionado,
						comunicacaoStatusSelecionado, getBooleanIndAtiva(), tpComunicacao, dtPeriodoInicial,
						dtPeriodoFinal, first, pageSize);

			}
		};
		comunicacaoLDM.setRowCount(count());
		resetPageDatatable();
	}

	private Boolean getBooleanIndAtiva() {
		Boolean retorno =null;
		if ("A".equalsIgnoreCase(situacao)) {
			retorno =  true;
		}else if("I".equalsIgnoreCase(situacao)){
			retorno =  false;
		}
		return retorno;

	}

	private Integer count() {
		return comunicacaoService.count(descTitulo, perfilSelecionado, comunicacaoStatusSelecionado,
				getBooleanIndAtiva(), tpComunicacao, dtPeriodoInicial, dtPeriodoFinal);
	}

	public void consultarComUsuario() {
		comunicacaoList = comunicacaoService.listarByFiltroConsultarUsuario(descTitulo,
				ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil(), dtPeriodoInicial);
	}

	public boolean renderedEditar(Comunicacao com) {
		boolean rendered = false;
		if (com.getIdeComunicacaoStatus().equals(ComunicacaoStatusEnum.NOVO.getIdComunicacaoStatus())) {
			rendered = true;
		}
		return rendered;

	}

	public boolean renderedExcluir(Comunicacao com) {
		boolean rendered = false;
		if (com.getIdeComunicacaoStatus().equals(ComunicacaoStatusEnum.NOVO.getIdComunicacaoStatus())) {
			rendered = true;
		}
		return rendered;

	}

	public boolean renderedCancelar(Comunicacao com) {
		boolean rendered = false;
		if (com.getIdeComunicacaoStatus().equals(ComunicacaoStatusEnum.ENVIADO.getIdComunicacaoStatus())) {
			rendered = true;
		}
		return rendered;

	}

	private void resetPageDatatable() {
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("formConsultarComunicacao:dataTableComunicacoes");
		if (!Util.isNull(dataTable)) {
			dataTable.reset();
		}
	}

	public String verificarMensagensUsuario() {
		return PaginaEnum.EXIBIR_COMUNICACAO_USUARIO.getUrl();
	}

	public String getDescTitulo() {
		return descTitulo;
	}

	public void setDescTitulo(String descTitulo) {
		this.descTitulo = descTitulo;
	}

	public Date getDtPeriodoInicial() {
		return dtPeriodoInicial;
	}

	public void setDtPeriodoInicial(Date dtPeriodoInicial) {
		this.dtPeriodoInicial = dtPeriodoInicial;
	}

	public Date getDtPeriodoFinal() {
		return dtPeriodoFinal;
	}

	public void setDtPeriodoFinal(Date dtPeriodoFinal) {
		this.dtPeriodoFinal = dtPeriodoFinal;
	}

	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}

	public Integer getTextSize() {
		return textSize;
	}

	public void setTextSize(Integer textSize) {
		this.textSize = textSize;
	}

	public DualListModel<Perfil> getPerfilDualList() {
		return perfilDualList;
	}

	public void setPerfilDualList(DualListModel<Perfil> perfilDualList) {
		this.perfilDualList = perfilDualList;
	}

	public Perfil getPerfilSelecionado() {
		return perfilSelecionado;
	}

	public void setPerfilSelecionado(Perfil perfilSelecionado) {
		this.perfilSelecionado = perfilSelecionado;
	}

	public List<Perfil> getPerfilList() {
		return perfilList;
	}

	public void setPerfilList(List<Perfil> perfilList) {
		this.perfilList = perfilList;
	}

	public List<Comunicacao> getComunicacaoList() {
		return comunicacaoList;
	}

	public void setComunicacaoList(List<Comunicacao> comunicacaoList) {
		this.comunicacaoList = comunicacaoList;
	}

	public boolean isDisabledField() {
		return disabledField;
	}

	public void setDisabledField(boolean disabledField) {
		this.disabledField = disabledField;
	}

	public String getTpComunicacao() {
		return tpComunicacao;
	}

	public void setTpComunicacao(String tpComunicacao) {
		this.tpComunicacao = tpComunicacao;
	}

	public Date getDtAtual() {
		return dtAtual;
	}

	public void setDtAtual(Date dtAtual) {
		this.dtAtual = dtAtual;
	}

	public void exibirComunicacao(Comunicacao cm) {

		this.comunicacao = cm;
		ContextoUtil.getContexto().setVisualizado(true);
		Html.exibir("dlgVisualizarNotificacaoUsuario2");
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public LazyDataModel<Comunicacao> getComunicacaoLDM() {
		return comunicacaoLDM;
	}

	public void setComunicacaoLDM(LazyDataModel<Comunicacao> comunicacaoLDM) {
		this.comunicacaoLDM = comunicacaoLDM;
	}

	public List<ComunicacaoStatus> getComunicacaoStatusList() {
		return comunicacaoStatusList;
	}

	public void setComunicacaoStatusList(List<ComunicacaoStatus> comunicacaoStatusList) {
		this.comunicacaoStatusList = comunicacaoStatusList;
	}

	public ComunicacaoStatus getComunicacaoStatusSelecionado() {
		return comunicacaoStatusSelecionado;
	}

	public void setComunicacaoStatusSelecionado(ComunicacaoStatus comunicacaoStatusSelecionado) {
		this.comunicacaoStatusSelecionado = comunicacaoStatusSelecionado;
	}
}
