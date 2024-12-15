package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AreaAbastecimentoPostoCombustivel;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.ClasseNbrPosto;
import br.gov.ba.seia.entity.DistribuidoraPosto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FaseEmpreendimento;
import br.gov.ba.seia.entity.LacLegislacao;
import br.gov.ba.seia.entity.LacPostoCombustivel;
import br.gov.ba.seia.entity.Legislacao;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.PostoCombustivelProdutoComercializado;
import br.gov.ba.seia.entity.PostoCombustivelTanque;
import br.gov.ba.seia.entity.PostoCombustivelTanqueProduto;
import br.gov.ba.seia.entity.PostoCombustivelTipoServico;
import br.gov.ba.seia.entity.Produto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SistemaControlePosto;
import br.gov.ba.seia.entity.TipoAreaAbastecimento;
import br.gov.ba.seia.entity.TipoBandeiraPostoCombustivel;
import br.gov.ba.seia.entity.TipoEquipamentoEntornoPosto;
import br.gov.ba.seia.entity.TipoEstruturaTanque;
import br.gov.ba.seia.entity.TipoParedeTanque;
import br.gov.ba.seia.entity.TipoPermeabilidade;
import br.gov.ba.seia.entity.TipoServicoPosto;
import br.gov.ba.seia.entity.TipoSistemaControlePosto;
import br.gov.ba.seia.entity.TipoTanquePosto;
import br.gov.ba.seia.enumerator.DistribuidoraPostoEnum;
import br.gov.ba.seia.enumerator.FaseEmpreendimentoEnum;
import br.gov.ba.seia.enumerator.TipoBandeiraPostoEnum;
import br.gov.ba.seia.enumerator.TipoServicoPostoEnum;
import br.gov.ba.seia.facade.LacPostoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

@Named("lacPostoController")
@ViewScoped
public class LacPostoController extends Lac {

	private final int ABA_DADOS = 0;
	private final int ABA_ABASTECIMENTO = 1;
	private final int ABA_CONTROLE = 2;
	private final int ABA_MANUTENCAO = 3;

	@EJB
	private LacPostoServiceFacade lacPostoServiceFacade;

	private LacPostoCombustivel lacPostoCombustivel;

	private PostoCombustivelProdutoComercializado produtoComercializado;

	private PostoCombustivelTanque postoCombustivelTanque;

	private Object objetoARemover;

	private TipoEquipamentoEntornoPosto tipoEquipamentoEntornoPosto;

	private SistemaControlePosto sistemaControlePosto;

	private List<TipoSistemaControlePosto> tiposSistemaControleASalvar;

	private AreaAbastecimentoPostoCombustivel areaAbastecimentoPostoCombustivel;

	private String dscOutrosServicos;

	private FaseEmpreendimento faseEmpreendimento;

	private Collection<TipoBandeiraPostoCombustivel> tiposBandeiraPosto;

	private Collection<TipoEquipamentoEntornoPosto> tiposEquipamentoEntornoPosto;

	private Collection<TipoSistemaControlePosto> tiposSistemaControlePosto;

	private Collection<ClasseNbrPosto> classesNbrPosto;

	private Collection<Produto> produtos;

	private Collection<TipoEstruturaTanque> tiposEstruturaTanque;

	private Collection<TipoAreaAbastecimento> tiposAreaAbastecimento;

	private Collection<TipoServicoPosto> tiposServicoPosto;

	// Divisao da lista para exibição organizada dos checks
	private Collection<TipoServicoPosto> listaTipoServicoPostoTemp1;
	private Collection<TipoServicoPosto> listaTipoServicoPostoTemp2;

	private Collection<LacLegislacao> legislacoesAceitasLac;

	private Collection<TipoParedeTanque> tiposParedeTanque;

	private Collection<TipoTanquePosto> tiposTanque;

	private Collection<TipoPermeabilidade> tiposPermeabilidade;

	private Collection<DistribuidoraPosto> distribuidorasPosto;

	private boolean exibeAbaGeral, exibeAbaAbastecimento, exibeAbaControle, exibeAbaManutencao, exibeOutrosDistribuidoras;

	private boolean exibeDscOutrosServicos, exibeDistribuidoras, exibeObsAcidente, exibeFormularioTanque, exibeDataInicioOperacao;

	private boolean editMode, viewMode;

	private Collection<PostoCombustivelTanque> listPostoCombustivelTanqueARemover;
	private Collection<AreaAbastecimentoPostoCombustivel> listAreaAbastecimentoARemover;
	private Collection<PostoCombustivelProdutoComercializado> postoCombustivelProdutoComercializados;

	public LacPostoController() {

	}

	public void novo() {
		
		Exception erro = null;
		try {
			this.carregarListas();
			Integer ideRequerimento = this.lacPostoCombustivel.getIdeRequerimento().getIdeRequerimento();
			Integer ideDocumentoObrigatorio = this.lacPostoCombustivel.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio();
			this.carregarDados();

			if (Util.isNull(this.lacPostoCombustivel)) {
				this.clear();
				this.exibeAbaGeral = true;
				this.desabilitarAbas();
				this.lacPostoCombustivel.getIdeRequerimento().setIdeRequerimento(ideRequerimento);
				this.lacPostoCombustivel.getIdeDocumentoObrigatorio().setIdeDocumentoObrigatorio(ideDocumentoObrigatorio);
				this.carregarFaseEmpreendimento(ideRequerimento);
			} else {
				this.editar();
			}

		} catch (Exception e) {
			erro = null;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void visualizar() {
		Exception erro = null;
		try {
			this.carregarListas();
			this.habilitarAbas();
			this.carregarDados();
			this.carregarDadosLacPosto();
			this.viewMode = true;
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	private void carregarDados() throws Exception {
		Integer ideRequerimento = this.lacPostoCombustivel.getIdeRequerimento().getIdeRequerimento();

		this.carregarFaseEmpreendimento(ideRequerimento);
		lacPostoCombustivel = this.lacPostoServiceFacade.carregarLacPostoByIdeRequerimento(ideRequerimento);
	}

	private void carregarFaseEmpreendimento(Integer ideRequerimento) throws Exception {
		this.faseEmpreendimento = this.lacPostoServiceFacade.buscarFaseEmpreendimentoByIdeRequerimento(ideRequerimento);
		boolean isLocalizacao = isLocalizacao();
		boolean isImplantacao = isImplantacao();
		if (!Util.isNull(this.faseEmpreendimento.getIdeFaseEmpreendimento()) && (isLocalizacao || isImplantacao)) {
			this.exibeDataInicioOperacao = true;
		} else {
			this.exibeDataInicioOperacao = false;
			if (!this.isOperacao()) {
				this.lacPostoCombustivel.setDtcInicioOperacao(null);
			}
		}
	}

	public boolean isLocalizacao() {
		return FaseEmpreendimentoEnum.LOCALIZACAO.getId().equals(this.faseEmpreendimento.getIdeFaseEmpreendimento());

	}

	public boolean isImplantacao() {
		return FaseEmpreendimentoEnum.IMPLANTACAO.getId().equals(this.faseEmpreendimento.getIdeFaseEmpreendimento());
	}

	public boolean isOperacao() {
		return FaseEmpreendimentoEnum.OPERACAO.getId().equals(this.faseEmpreendimento.getIdeFaseEmpreendimento());
	}

	public void editar() throws Exception {
		this.editMode = true;
		this.verificarTermos();
		this.habilitarAbas();
		this.carregarDadosLacPosto();

		Integer ideRequerimento = this.lacPostoCombustivel.getIdeRequerimento().getIdeRequerimento();
		this.carregarFaseEmpreendimento(ideRequerimento);

		this.exibeObsAcidente = this.lacPostoCombustivel.getIndAcidente();
		this.verificarTipoBandeira(this.lacPostoCombustivel.getIdeTipoBandeiraPostoCombustivel());
		if (!Util.isNull(this.lacPostoCombustivel.getIdeDistribuidoraPosto())) {
			this.verificarTipoDistribuidora(this.lacPostoCombustivel.getIdeDistribuidoraPosto().getIdeDistribuidoraPosto());
		}
		if (Util.isNull(lacPostoCombustivel.getIdeClasseNbrPosto())) {
			this.lacPostoCombustivel.setIdeClasseNbrPosto(new ClasseNbrPosto());
		}

		if (Util.isNull(lacPostoCombustivel.getIdeDistribuidoraPosto())) {
			this.lacPostoCombustivel.setIdeDistribuidoraPosto(new DistribuidoraPosto());
		}

	}

	private void carregarDadosLacPosto() throws Exception {
		Integer ideLac = this.lacPostoCombustivel.getIdeLac();
		if (Util.isNull(ideLac)) {
			return;
		}
		this.carregarProdutosComercializados(ideLac);
		this.carregarTipoServicoPosto(ideLac);
		this.carregarTanquesPosto(ideLac);
		this.carregarEquipamentosEntornoPosto(ideLac);
		this.carregarSistemasDeControle(ideLac);
		this.carregarAreaAbastecimento(ideLac);
	}

	private void carregarProdutosComercializados(Integer ideLac) throws Exception {
		Collection<PostoCombustivelProdutoComercializado> produtosComercializados = this.lacPostoServiceFacade.carregarProdutosComercializadosByIdeLac(ideLac);
		this.lacPostoCombustivel.setPostoCombustivelProdutosComercializadosCollection(produtosComercializados);
	}

	private void carregarTipoServicoPosto(Integer ideLac) throws Exception {
		Collection<PostoCombustivelTipoServico> postoCombustivelTipoServicos = this.lacPostoServiceFacade.carregarPostoTipoServicoByIdeLac(ideLac);
		Collection<TipoServicoPosto> tiposServicoPostoParte1 = getTiposServicoPostoParte1();
		Collection<TipoServicoPosto> tiposServicoPostoParte2 = getTiposServicoPostoParte2();
		for (PostoCombustivelTipoServico postoCombustivelTipoServico : postoCombustivelTipoServicos) {
			if (!Util.isNullOuVazio(postoCombustivelTipoServico.getDscOutros())) {
				this.dscOutrosServicos = postoCombustivelTipoServico.getDscOutros();
			}
			if (tiposServicoPostoParte1.contains(postoCombustivelTipoServico.getTipoServicoPosto())) {
				this.listaTipoServicoPostoTemp1.add(postoCombustivelTipoServico.getTipoServicoPosto());
			}
			if (tiposServicoPostoParte2.contains(postoCombustivelTipoServico.getTipoServicoPosto())) {
				this.listaTipoServicoPostoTemp2.add(postoCombustivelTipoServico.getTipoServicoPosto());
				this.verificarLista2tipoServico((List<TipoServicoPosto>) listaTipoServicoPostoTemp2);
			}

		}
	}

	private void carregarTanquesPosto(Integer ideLac) throws Exception {
		Collection<PostoCombustivelTanque> tanquesPosto = this.lacPostoServiceFacade.carregarTanquesPostoByIdeLac(ideLac);
		for (PostoCombustivelTanque postoCombustivelTanque : tanquesPosto) {
			Collection<PostoCombustivelTanqueProduto> listaProdutoTanque = this.lacPostoServiceFacade.carregarProdutoTanqueByIdeTanque(postoCombustivelTanque
					.getIdeTanque());
			postoCombustivelTanque.setProdutoCollection(listaProdutoTanque);
		}
		this.lacPostoCombustivel.setPostoCombustivelTanqueCollection(tanquesPosto);
	}

	private void carregarEquipamentosEntornoPosto(Integer ideLac) throws Exception {
		Collection<TipoEquipamentoEntornoPosto> equipamentosEntornoPosto = this.lacPostoServiceFacade.carregarEquipamentosEntornoPostoByIdeLac(ideLac);
		this.lacPostoCombustivel.setTipoEquipamentoEntornoPostoCollection(equipamentosEntornoPosto);
	}

	private void carregarSistemasDeControle(Integer ideLac) throws Exception {
		Collection<SistemaControlePosto> sistemasDeControle = this.lacPostoServiceFacade.carregarSistemasDeControleByIdeLac(ideLac);
		this.lacPostoCombustivel.setSistemaControlePostoCollection(sistemasDeControle);
	}

	private void carregarAreaAbastecimento(Integer ideLac) throws Exception {
		Collection<AreaAbastecimentoPostoCombustivel> areasAbastecimento = this.lacPostoServiceFacade.carregarAreaAbastecimentoByIdeLac(ideLac);
		this.lacPostoCombustivel.setAreaAbastecimentoPostoCombustivelCollection(areasAbastecimento);
	}

	public void editarPostoCombustivelTanque() {
		this.exibeFormularioTanque = true;
	}

	@Override
	public void controlarAbas(TabChangeEvent tabChangeEvent) {
		// MÀGICA
	}

	@PostConstruct
	public void init() {
		this.clear();
	}

	public void clear() {
		super.activeTab = 0;
		this.dscOutrosServicos = StringUtils.EMPTY;
		this.exibeObsAcidente = false;
		this.exibeDataInicioOperacao = false;
		this.exibeDistribuidoras = false;
		this.exibeOutrosDistribuidoras = false;
		this.exibeFormularioTanque = false;
		super.resetarTermos();
		this.viewMode = false;
		this.editMode = false;
		this.desabilitarAbas();
		this.initLacPostoCombustivel();
		this.listaTipoServicoPostoTemp1 = new ArrayList<TipoServicoPosto>();
		this.listaTipoServicoPostoTemp2 = new ArrayList<TipoServicoPosto>();
		this.listPostoCombustivelTanqueARemover = new ArrayList<PostoCombustivelTanque>();
		this.tiposSistemaControleASalvar = new ArrayList<TipoSistemaControlePosto>();
		this.listAreaAbastecimentoARemover = new ArrayList<AreaAbastecimentoPostoCombustivel>();
		if (Util.isNull(faseEmpreendimento)) {
			this.faseEmpreendimento = new FaseEmpreendimento();
		}
		this.legislacoesAceitasLac = new ArrayList<LacLegislacao>();
		this.initDistribuidoraPosto();
		this.initPostoCombustiveTanque();
		this.initTipoEquipamentoEntorno();
		this.initSistemaControlePosto();
		this.initAreaAbastecimentoPosto();
		this.initProdutoComercializado();
	}

	public void initLacPostoCombustivel() {
		this.lacPostoCombustivel = new LacPostoCombustivel();
		this.lacPostoCombustivel.setIdeRequerimento(new Requerimento());
		this.lacPostoCombustivel.setIdeDocumentoObrigatorio(new DocumentoObrigatorio());
	}

	private void desabilitarAbas() {
		this.exibeAbaAbastecimento = false;
		this.exibeAbaControle = false;
		this.exibeAbaManutencao = false;
	}

	private void habilitarAbas() {
		this.exibeAbaGeral = true;
		this.exibeAbaAbastecimento = true;
		this.exibeAbaControle = true;
		this.exibeAbaManutencao = true;
	}

	public void validarDadosGerais() {
		boolean valido = validarDadosAbaGeral();

		if (valido) {
			this.activeTab = ABA_ABASTECIMENTO;
			this.exibeAbaAbastecimento = true;
		} else {
			this.activeTab = ABA_DADOS;
		}

	}

	private boolean validarDadosAbaGeral() {
		boolean valido = validarDadosPosto();

		if (Util.isNullOuVazio(this.lacPostoCombustivel.getPostoCombustivelProdutosComercializadosCollection())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo 'Produtos comercializados ou a serem comercializados' é de preenchimento obrigatório!");
		}
		return valido;
	}

	private boolean validarDadosPosto() {
		boolean valido = true;

		if (exibeDataInicioOperacao && Util.isNullOuVazio(this.lacPostoCombustivel.getDtcInicioOperacao())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo 'Data Início Operação' é de preenchimento obrigatório!");
		}

		if (Util.isNullOuVazio(this.lacPostoCombustivel.getIdeTipoBandeiraPostoCombustivel())
				|| Util.isNullOuVazio(this.lacPostoCombustivel.getIdeTipoBandeiraPostoCombustivel().getIdeTipoBandeiraPostoCombustivel())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo Bandeira do Posto é de preenchimento obrigatório.");
		}

		if (this.exibeDistribuidoras && Util.isNullOuVazio(this.lacPostoCombustivel.getIdeDistribuidoraPosto().getIdeDistribuidoraPosto())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo Distribuidora é de preenchimento obrigatório.");
		}

		if (this.exibeDistribuidoras && Util.isNullOuVazio(this.lacPostoCombustivel.getDtcValidadeContratoDistribuidora())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo Vigência do contrato é de preenchimento obrigatório.");
		}

		if (exibeOutrosDistribuidoras && Util.isNullOuVazio(this.lacPostoCombustivel.getDscOutrosDistribuidora())) {
			valido = false;
			JsfUtil.addErrorMessage("A descrição da Distribuidora é de preenchimento obrigatório!");
		}

		if (this.exibeDistribuidoras && !Util.isNullOuVazio(this.lacPostoCombustivel.getDtcValidadeContratoDistribuidora())
				&& this.lacPostoCombustivel.getDtcValidadeContratoDistribuidora().compareTo(new Date()) == -1) {
			valido = false;
			JsfUtil.addErrorMessage("A Vigência do contrato deve ser maior que a data atual!");
		}

		if (Util.isNullOuVazio(this.lacPostoCombustivel.getIndFlutuante())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo Trata-se de Posto Flutuante é de preenchimento obrigatório.");
		}

		if (this.listaTipoServicoPostoTemp1.isEmpty() && this.listaTipoServicoPostoTemp2.isEmpty()) {
			valido = false;
			JsfUtil.addErrorMessage("O campo Serviços Prestados ou a serem Prestados é de preenchimentos obrigatórios.");
		}

		if (this.exibeDscOutrosServicos && Util.isNullOuVazio(this.dscOutrosServicos)) {
			valido = false;
			JsfUtil.addErrorMessage("A descrição do serviço Outros é de preenchimento obrigatório.");
		}

		if (Util.isNullOuVazio(this.lacPostoCombustivel.getQtdAreaTotal())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo Área Total é de preenchimento obrigatório.");
		}

		if (Util.isNullOuVazio(this.lacPostoCombustivel.getQtdAreaConstruida())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo Área Construída é de preenchimento obrigatório.");
		}
		if (Util.isNullOuVazio(this.lacPostoCombustivel.getQtdAreaAmpliacao())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo Área para Ampliação é de preenchimento obrigatório.");
		}

		if (!Util.isNullOuVazio(this.lacPostoCombustivel.getQtdAreaAmpliacao())
				&& !Util.isNullOuVazio(this.lacPostoCombustivel.getQtdAreaConstruida())
				&& (this.lacPostoCombustivel.getQtdAreaConstruida().add(this.lacPostoCombustivel.getQtdAreaAmpliacao()).doubleValue()) > this.lacPostoCombustivel
				.getQtdAreaTotal().doubleValue()) {
			valido = false;
			JsfUtil.addErrorMessage("O resultado das somas das áreas internas do empreendimento informadas, não pode ser maior que o empreendimento.");
		}

		if (valido) {
			this.activeTab = ABA_ABASTECIMENTO;
			this.exibeAbaAbastecimento = true;
		} else {
			this.activeTab = ABA_DADOS;
		}

		return valido;
	}

	public void validarDadosAbastecimento() {
		boolean valido = validarDadosAbaAbastecimento();

		if (valido) {
			this.activeTab = ABA_CONTROLE;
			this.exibeAbaControle = true;
		} else {
			this.activeTab = ABA_ABASTECIMENTO;
		}

	}

	private boolean validarDadosAbaAbastecimento() {
		boolean valido = true;
		if (Util.isNullOuVazio(this.lacPostoCombustivel.getPostoCombustivelTanqueCollection())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo 'Tanques' é de preenchimento obrigatório.");
		}
		return valido;
	}

	public void validarDadosControle() {
		this.validarDadosAbaControle();
	}

	private boolean validarDadosAbaControle() {
		boolean valido = validarOutrosDadosPosto();

		if (Util.isNull(this.lacPostoCombustivel.getIndSistemaControleAutomatico())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo 'Sistema de Controle' é de preenchimento obrigatório.");
		}


		if (Util.isNullOuVazio(this.lacPostoCombustivel.getSistemaControlePostoCollection())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo 'Tipos de Sistema de controle' é de preenchimento obrigatório.");
		}

		if (valido) {
			this.activeTab = ABA_MANUTENCAO;
			this.exibeAbaManutencao = true;
		} else {
			this.activeTab = ABA_CONTROLE;
		}

		return valido;
	}

	private boolean validarOutrosDadosPosto() {
		boolean valido = true;
		if (Util.isNull(this.lacPostoCombustivel.getIndAreaIndigena())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo 'O empreendimento está localizado em área indígena ?' é de preenchimento obrigatório.");
		}

		if (Util.isNull(this.lacPostoCombustivel.getIndSitioArqueologico())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo 'O empreendimento está localizado em sítio arqueológico ?' é de preenchimento obrigatório.");
		}

		if (Util.isNullOuVazio(this.lacPostoCombustivel.getTipoEquipamentoEntornoPostoCollection())) {
			valido = false;
			JsfUtil.addErrorMessage("O campo 'Equipamentos no Entorno do Ambiente' é de preenchimento obrigatório.");
		}

		if (Util.isNull(this.lacPostoCombustivel.getIdeClasseNbrPosto()) || Util.isNull(this.lacPostoCombustivel.getIdeClasseNbrPosto().getIdeClasseNbrPosto()) ||
				( !Util.isNull(this.lacPostoCombustivel.getIdeClasseNbrPosto().getIdeClasseNbrPosto()) && this.lacPostoCombustivel.getIdeClasseNbrPosto().getIdeClasseNbrPosto().equals(0))) {
			valido = false;
			JsfUtil.addErrorMessage("O campo Classificação Conforme NBR 13.786 é de preenchimento obrigatório.");
		}

		if (valido) {
			this.activeTab = ABA_MANUTENCAO;
			this.exibeAbaManutencao = true;
		} else {
			this.activeTab = ABA_CONTROLE;
		}

		return valido;
	}

	public void salvarOuAtualizarDadosManutencao() {
		if (this.validarDadosManutencao()) {
			Exception erro = null;
			try {
				salvarPostoCombustivel();
				JsfUtil.addSuccessMessage("Dados da manutenção inseridos com sucesso.");
			} catch (Exception e) {
				erro = e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
			
			this.activeTab = 3;
		}
	}

	public void validarLacEFinalizar() {
		boolean valido = validarDadosAbaGeral();
		if (!valido) {
			this.activeTab = ABA_DADOS;
		}
		valido = valido && validarDadosAbaAbastecimento();
		if (!valido) {
			this.activeTab = ABA_ABASTECIMENTO;
		}
		valido = valido && validarDadosAbaControle();
		this.activeTab = ABA_CONTROLE;

		if (isOperacao() && !Util.isNull(this.lacPostoCombustivel.getIndReformado()) && this.lacPostoCombustivel.getIndReformado()) {
			this.activeTab = ABA_MANUTENCAO;
			valido = valido && this.validarDadosManutencao();
		}else{
			this.removerAreasAbastecimento(this.lacPostoCombustivel.getAreaAbastecimentoPostoCombustivelCollection());
			this.lacPostoCombustivel.setAreaAbastecimentoPostoCombustivelCollection(new ArrayList<AreaAbastecimentoPostoCombustivel>());
			this.lacPostoCombustivel.setIndAcidente(false);
			this.lacPostoCombustivel.setDscOcorrenciaAcidente(null);
			this.lacPostoCombustivel.setQtdTrocaOleo(null);
		}

		if (valido) {
			try {
				if (!exibeDistribuidoras) {
					this.lacPostoCombustivel.setIdeDistribuidoraPosto(null);
				}
				this.salvarPostoCombustivel();
				this.getLegislacoes();

				this.verificarTermos();

				// VERIFICAR ACEITE
				if (!Util.isNull(this.legislacoesAceitasLac) && this.legislacoesAceitasLac.isEmpty()) {
					RequestContext.getCurrentInstance().execute("lac_posto_termos.show();");
				} else {
					JsfUtil.addSuccessMessage("Lac de Posto de Combustível concluída com sucesso!");
				}

				if (!exibeDistribuidoras) {
					this.lacPostoCombustivel.setIdeDistribuidoraPosto(new DistribuidoraPosto());
				}
			} 
			catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
			}
			
		}

	}

	public void finalizar() {
		try {
			this.lacPostoServiceFacade.salvarLacLegislacao(this.lacPostoCombustivel, legislacoes);
			RequestContext.getCurrentInstance().execute("updateDocumento();");
			JsfUtil.addSuccessMessage("Lac de Posto de Combustível concluída com sucesso!");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		

	}

	private void salvarPostoCombustivel() throws Exception {

		this.gerarTipoServicoPosto();

		if (!Util.isNull(this.lacPostoCombustivel.getIdeClasseNbrPosto())
				&& Util.isNull(this.lacPostoCombustivel.getIdeClasseNbrPosto().getIdeClasseNbrPosto())) {
			this.lacPostoCombustivel.setIdeClasseNbrPosto(null);
		}

		if (!Util.isNull(this.lacPostoCombustivel.getIdeDistribuidoraPosto())
				&& Util.isNull(this.lacPostoCombustivel.getIdeDistribuidoraPosto().getIdeDistribuidoraPosto())) {
			this.lacPostoCombustivel.setIdeDistribuidoraPosto(null);
		}

		this.lacPostoServiceFacade.salvarOuAtualizarLacPosto(this.lacPostoCombustivel);

		if (!Util.isNull(this.lacPostoCombustivel.getIdeDistribuidoraPosto())
				&& Util.isNull(this.lacPostoCombustivel.getIdeDistribuidoraPosto().getIdeDistribuidoraPosto())) {
			this.lacPostoCombustivel.setIdeDistribuidoraPosto(new DistribuidoraPosto());
		}

		if (!Util.isNull(this.lacPostoCombustivel.getIdeClasseNbrPosto())
				&& Util.isNull(this.lacPostoCombustivel.getIdeClasseNbrPosto().getIdeClasseNbrPosto())) {
			this.lacPostoCombustivel.setIdeClasseNbrPosto(new ClasseNbrPosto());
		}

	}

	public boolean validarDadosManutencao() {
		boolean valido = true;
		if (isOperacao()) {
			if (Util.isNullOuVazio(this.lacPostoCombustivel.getAreaAbastecimentoPostoCombustivelCollection())) {
				valido = false;
				JsfUtil.addErrorMessage("As Áreas de Abastecimento são de preenchimento obrigatório!");
			}

			if (Util.isNull(this.lacPostoCombustivel.getIndAcidente())) {
				valido = false;
				JsfUtil.addErrorMessage("O campo Houve Acidente e/ou Vazamento desde o seu funcionamento é de preenchimento obrigatório.");
			}

			if (!Util.isNull(this.lacPostoCombustivel.getIndAcidente()) && this.lacPostoCombustivel.getIndAcidente()
					&& Util.isNullOuVazio(this.lacPostoCombustivel.getDscOcorrenciaAcidente())) {
				valido = false;
				JsfUtil.addErrorMessage("O campo Observação da ocorrência do acidente é de preenchimento obrigatório.");
			}
		}
		return valido;

	}

	private void initDistribuidoraPosto() {
		this.lacPostoCombustivel.setIdeDistribuidoraPosto(new DistribuidoraPosto());
	}

	public void initPostoCombustiveTanque() {
		this.postoCombustivelTanque = new PostoCombustivelTanque();
		// this.postoCombustivelTanque.setIdeProduto(new Produto());
		this.postoCombustivelTanque.setIdeTipoParedeTanque(new TipoParedeTanque());
		this.postoCombustivelTanque.setIdeTipoEstruturaTanque(new TipoEstruturaTanque());
		this.postoCombustivelTanque.setIdeTipoTanquePosto(new TipoTanquePosto());
	}

	private void initSistemaControlePosto() {
		this.sistemaControlePosto = new SistemaControlePosto();
		this.tiposSistemaControleASalvar = new ArrayList<TipoSistemaControlePosto>();
		this.sistemaControlePosto.setIdeTipoSistemaControlePosto(new TipoSistemaControlePosto());
	}

	private void initAreaAbastecimentoPosto() {
		this.areaAbastecimentoPostoCombustivel = new AreaAbastecimentoPostoCombustivel();
		this.areaAbastecimentoPostoCombustivel.setTipoAreaAbastecimento(new TipoAreaAbastecimento());
		this.areaAbastecimentoPostoCombustivel.setTipoPermeabilidadeDepoisReforma(new TipoPermeabilidade());
		this.areaAbastecimentoPostoCombustivel.setTipoPermeabilidadeAntesReforma(new TipoPermeabilidade());
	}

	private void initTipoEquipamentoEntorno() {
		this.tipoEquipamentoEntornoPosto = new TipoEquipamentoEntornoPosto();
	}

	private void initProdutoComercializado() {
		this.produtoComercializado = new PostoCombustivelProdutoComercializado();
		this.produtoComercializado.setProduto(new Produto());
	}

	private void carregarListas() {
		try {
			this.carregarListaTiposBandeiraPosto();
			this.carregarListaClassesNbrPosto();
			this.carregarListaServicosPosto();
			this.carregarListaProdutos();
			this.carregarListaTiposEquipamentoEntornoPosto();
			this.carregarListaTiposSistemaControlePosto();
			this.carregarListaTiposAreaAbastecimento();
			this.carregarListaTiposPermeabilidade();
			this.carregarListaTiposEstruturaTanque();
			this.carregarListaTiposTanque();
			this.carregarListaTiposParede();
			this.carregarListaDistribuidoraPosto();
			this.carregarLegislacoesPosto();
		} catch (Exception ex) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, ex);
		}
	}

	@Override
	public Collection<LacLegislacao> getLegislacoes() {
		Collection<Legislacao> legislacoesLacPosto = new HashSet<Legislacao>();

		if (!Util.isNull(this.lacPostoCombustivel) && !Util.isNull(this.lacPostoCombustivel.getIdeLac())) {
			if (!this.lacPostoCombustivel.getIndFlutuante()) {
				removeLegislacao(getLegislacaoCapitania());
			} else {
				Legislacao legislacaoCapitania = getLegislacaoCapitania();
				legislacoesLacPosto.add(legislacaoCapitania);
			}
			if (!Util.isNull(this.lacPostoCombustivel.getIndAreaIndigena()) && !this.lacPostoCombustivel.getIndAreaIndigena()) {
				removeLegislacao(getLegislacaoFunai());
			} else {

				Legislacao legislacaoFunai = getLegislacaoFunai();
				legislacoesLacPosto.add(legislacaoFunai);

			}
			if (!Util.isNull(this.lacPostoCombustivel.getIndSitioArqueologico()) && !this.lacPostoCombustivel.getIndSitioArqueologico()) {
				removeLegislacao(getLegislacaoIphan());
			} else {

				Legislacao legislacaoIPhan = getLegislacaoIphan();
				legislacoesLacPosto.add(legislacaoIPhan);
			}
		}
		super.carregarLegislacoes(legislacoesLacPosto);
		return super.getLegislacoes();
	}

	private void removeLegislacao(Legislacao leg) {
		for (LacLegislacao legislacao : legislacoes) {
			if (legislacao.getLegislacao().equals(leg)) {
				legislacoes.remove(legislacao);
				break;
			}
		}
	}

	private void carregarLegislacoesPosto() throws Exception {
		Collection<Legislacao> legislacoesLacPosto = this.lacPostoServiceFacade.carregarLegislacoesLacPosto();

		Legislacao legislacaoFunai = getLegislacaoFunai();
		legislacoesLacPosto.add(legislacaoFunai);

		Legislacao legislacaoIPhan = getLegislacaoIphan();
		legislacoesLacPosto.add(legislacaoIPhan);

		Legislacao legislacaoCapitania = getLegislacaoCapitania();
		legislacoesLacPosto.add(legislacaoCapitania);

		super.carregarLegislacoes(legislacoesLacPosto);
	}

	private Legislacao getLegislacaoFunai() {
		Legislacao legislacaoFunai = new Legislacao();
		legislacaoFunai.setCodLegilacao("FUNAI");
		legislacaoFunai.setDscLegislacao("'Estou ciente que devo manter no empreendimento a autorização prévia da Fundação Nacional do Índio (FUNAI)'");
		return legislacaoFunai;
	}

	private Legislacao getLegislacaoIphan() {
		Legislacao legislacaoIPhan = new Legislacao();
		legislacaoIPhan.setCodLegilacao("IPHAN");
		legislacaoIPhan
		.setDscLegislacao("'Estou ciente que devo manter no empreendimento a autorização do Instituto do Património"
				+ " Histórico e Artístico Nacional (IPHAN) ou do Instituto do Patrimônio Artístico Cultural (IPAC), para apresentação no caso de fiscalização ambiental ao empreendimento;'");
		return legislacaoIPhan;
	}

	private Legislacao getLegislacaoCapitania() {
		Legislacao legislacaoCapitania = new Legislacao();
		legislacaoCapitania.setCodLegilacao("CAPITANIA");
		legislacaoCapitania.setDscLegislacao("'Estou ciente que devo manter no empreedimento cópia autenticada do documento expedido pela Capitania dos"
				+ " Portos autorizando sua localização e funcionamento e contendo a localização geogr´afica do posto no respectivo curso d'água'");
		return legislacaoCapitania;
	}

	public void atualizar() {
		Exception erro = null;
		try {
			this.salvarLacPosto();
			JsfUtil.addSuccessMessage("Alteração efetuada com sucesso.");
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public synchronized void salvarLacPosto() {
		if (!this.validarDadosPosto()) {
			return;
		}
		this.prepararDadosLac();
		Exception erro = null;
		try {
			salvarPostoCombustivel();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		this.lacPostoCombustivel.setIdeClasseNbrPosto(new ClasseNbrPosto());
		JsfUtil.addSuccessMessage("Posto de Combustível inserido com sucesso.");
	}

	public void salvarDadosDoPosto() {
		if (!this.validarOutrosDadosPosto()) {
			return;
		}

		Exception erro = null;
		try {
			salvarPostoCombustivel();
			JsfUtil.addSuccessMessage("Dados do Posto de Combustível inseridos com sucesso.");
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		

	}

	public void salvarDadosDoSistemaDeControle() {
		if (Util.isNull(this.lacPostoCombustivel.getIndSistemaControleAutomatico())) {
			JsfUtil.addErrorMessage("O campo 'Sistema de Controle' é de preenchimento obrigatório!");
			return;
		}
		Exception erro = null;
		try {
			salvarPostoCombustivel();
			JsfUtil.addSuccessMessage("Dados do Sistema de Controle inseridos com sucesso.");
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		

	}

	public void prepararDadosLac() {
		this.lacPostoCombustivel.setIdeClasseNbrPosto(null);
		if (Util.isNull(this.lacPostoCombustivel.getDtcCriacao())) {
			this.lacPostoCombustivel.setDtcCriacao(new Date());
		}
		if (!exibeDistribuidoras) {
			this.lacPostoCombustivel.setIdeDistribuidoraPosto(null);
		}
	}

	private void gerarTipoServicoPosto() throws Exception {
		List<PostoCombustivelTipoServico> tiposServicosPosto = new ArrayList<PostoCombustivelTipoServico>();
		this.gerarPostoServicoCombustivel(tiposServicosPosto, this.listaTipoServicoPostoTemp1);
		this.gerarPostoServicoCombustivel(tiposServicosPosto, this.listaTipoServicoPostoTemp2);
		this.lacPostoCombustivel.setPostoCombustivelTipoServicoCollection(tiposServicosPosto);
	}

	private void gerarPostoServicoCombustivel(List<PostoCombustivelTipoServico> tiposServicosPosto, Collection<TipoServicoPosto> listaTipoServicoPosto) {
		for (TipoServicoPosto tipoServico : listaTipoServicoPosto) {
			PostoCombustivelTipoServico postoCombustivelTipoServico = gerarPostoCombustivelServico(tipoServico);
			tiposServicosPosto.add(postoCombustivelTipoServico);
		}
	}

	private PostoCombustivelTipoServico gerarPostoCombustivelServico(TipoServicoPosto tipoServico) {
		PostoCombustivelTipoServico postoCombustivelTipoServico = new PostoCombustivelTipoServico();
		postoCombustivelTipoServico.setTipoServicoPosto(tipoServico);
		postoCombustivelTipoServico.setPostoCombustivel(this.lacPostoCombustivel);
		if (tipoServico.getIdeTipoServicoPosto().equals(TipoServicoPostoEnum.OUTROS.getId())) {
			postoCombustivelTipoServico.setDscOutros(this.dscOutrosServicos);
		}
		return postoCombustivelTipoServico;
	}

	public void fecharDialogs() {
		RequestContext.getCurrentInstance().execute("lac_posto_termos.hide()");
		RequestContext.getCurrentInstance().execute("lac_posto.hide()");
	}

	@Override
	public void exibirTermos() {
		RequestContext.getCurrentInstance().execute("lac_posto_termos.show()");
	}

	public void verificarTermos() throws Exception {
		Integer ideLac = this.lacPostoCombustivel.getIdeLac();
		legislacoesAceitasLac = this.lacPostoServiceFacade.buscarLegislacaoByIdeLac(ideLac);
	}



	public StreamedContent getImprimirRelatorio() throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();

		lParametros.put("ide_lac", this.lacPostoCombustivel.getIdeLac());
		lParametros.put("legislacao", this.getCondicionantesFormularioLAC());
		return new RelatorioUtil("lac_posto.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	public StreamedContent getImprimirRelatorioFinal() throws Exception {
		this.lacPostoCombustivel = this.lacPostoServiceFacade.carregarLacPostoByIdeRequerimento(this.lacPostoCombustivel.getIdeRequerimento().getIdeRequerimento());
		return getImprimirRelatorio();
	}

	public StreamedContent getImprimirCertificado() {
		Exception erro = null;
		try {
			Integer ideRequerimento = this.lacPostoCombustivel.getIdeRequerimento().getIdeRequerimento();
			this.lacPostoCombustivel = this.lacPostoServiceFacade.carregarLacPostoByIdeRequerimento(ideRequerimento);

			if (!this.lacPostoServiceFacade.hasCertificado(ideRequerimento)) {
				Certificado certificado = this.gerarCertificadoLac();
				this.lacPostoServiceFacade.salvarCertificadoLac(certificado);
			}else if(!this.lacPostoServiceFacade.hasToken(ideRequerimento)){
				Certificado certificado = this.lacPostoServiceFacade.carregarCertificado(ideRequerimento);
				this.lacPostoServiceFacade.atualizarTokenCertificadoLac(certificado);
			}


			return this.imprimirCertificadoLacPosto();
		} catch (Exception e) {
			erro = e;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	private Certificado gerarCertificadoLac() {
		AtoAmbiental atoAmbiental = getAtoAmbientalLac();
		Requerimento requerimento = this.lacPostoCombustivel.getIdeRequerimento();
		return super.gerarCertificado(atoAmbiental, requerimento);
	}

	private StreamedContent imprimirCertificadoLacPosto() throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();

		lParametros.put("ide_requerimento", this.lacPostoCombustivel.getIdeRequerimento().getIdeRequerimento());
		return new RelatorioUtil("certificado_lac_tcra.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	public StreamedContent getImprimirCertificadoTcraLac(Integer ideRequerimento) {
		Exception  erro = null;
		try {
			if (!this.lacPostoServiceFacade.hasCertificado(ideRequerimento)) {
				this.lacPostoCombustivel.getIdeRequerimento().setIdeOrgao(new Orgao(1,1));
				Certificado certificado = this.gerarCertificadoLac();
				this.lacPostoServiceFacade.salvarCertificadoLac(certificado);
			}else if(!this.lacPostoServiceFacade.hasToken(ideRequerimento)){
				Certificado certificado = this.lacPostoServiceFacade.carregarCertificado(ideRequerimento);
				this.lacPostoServiceFacade.atualizarTokenCertificadoLac(certificado);
			}

			return this.imprimirCertificadoTcraLac(ideRequerimento);
		} catch (Exception e) {
			erro =null;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	private StreamedContent imprimirCertificadoTcraLac(Integer ideRequerimento) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();

		lParametros.put("ide_requerimento", ideRequerimento);
		return new RelatorioUtil("certificado_lac_tcra.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	private void carregarListaTiposBandeiraPosto() throws Exception {
		this.tiposBandeiraPosto = this.lacPostoServiceFacade.listarTiposBandeiraPosto();
	}

	private void carregarListaClassesNbrPosto() throws Exception {
		this.classesNbrPosto = this.lacPostoServiceFacade.listarClassesNbr();
	}

	private void carregarListaServicosPosto() throws Exception {
		this.tiposServicoPosto = this.lacPostoServiceFacade.listarServicosPosto();
	}

	private void carregarListaProdutos() throws Exception {
		this.produtos = this.lacPostoServiceFacade.listarProdutos();
	}

	private void carregarListaTiposEquipamentoEntornoPosto() throws Exception {
		this.tiposEquipamentoEntornoPosto = this.lacPostoServiceFacade.listarTiposEquipamentoEntornoPosto();
	}

	private void carregarListaTiposSistemaControlePosto() throws Exception {
		this.tiposSistemaControlePosto = this.lacPostoServiceFacade.listarTiposSistemaControle();
	}

	private void carregarListaTiposAreaAbastecimento() throws Exception {
		this.tiposAreaAbastecimento = this.lacPostoServiceFacade.listarTiposAreaAbastecimento();
	}

	private void carregarListaTiposPermeabilidade() throws Exception {
		this.tiposPermeabilidade = this.lacPostoServiceFacade.listarTiposPermeabilidade();
	}

	private void carregarListaTiposEstruturaTanque() throws Exception {
		this.tiposEstruturaTanque = this.lacPostoServiceFacade.listarTiposEstruturaTanque();
	}

	private void carregarListaTiposParede() throws Exception {
		this.tiposParedeTanque = this.lacPostoServiceFacade.listarTiposParedeTanque();
	}

	private void carregarListaTiposTanque() throws Exception {
		this.tiposTanque = this.lacPostoServiceFacade.listarTiposTanque();
	}

	private void carregarListaDistribuidoraPosto() throws Exception {
		this.distribuidorasPosto = this.lacPostoServiceFacade.listarDistribuidorasPosto();
	}

	public void removerProdutoComercializado() {
		this.lacPostoCombustivel.getPostoCombustivelProdutosComercializadosCollection().remove(this.objetoARemover);
		Integer ideProduto = ((PostoCombustivelProdutoComercializado) this.objetoARemover).getProduto().getIdeProduto();
		Exception erro = null;
		try {
			this.lacPostoServiceFacade.removerProdutoComercializado(ideProduto, this.lacPostoCombustivel.getIdeLac());
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void adicionarProdutoComercializado() {
		if (verificarDuplicidadeProduto()) {
			JsfUtil.addErrorMessage("O Produto já foi adicionado");
		} else {
			Integer ideProduto = this.produtoComercializado.getProduto().getIdeProduto();
			Produto produto = this.obterTipoProduto(ideProduto);
			this.produtoComercializado.setProduto(produto);
			this.produtoComercializado.setPostoCombustivel(this.lacPostoCombustivel);
			this.lacPostoCombustivel.getPostoCombustivelProdutosComercializadosCollection().add(this.produtoComercializado);
			this.salvarOuAtualizarProdutosComercializados();
			this.initProdutoComercializado();
		}
	}

	private boolean verificarDuplicidadeProduto() {

		for (PostoCombustivelProdutoComercializado produto : this.lacPostoCombustivel.getPostoCombustivelProdutosComercializadosCollection()) {
			if (this.produtoComercializado.getProduto().equals(produto.getProduto())) {
				return true;
			}
		}

		return false;
	}

	private Produto obterTipoProduto(String dscProduto) {
		Exception erro = null;
		try {
			return this.lacPostoServiceFacade.carregarProdutoByDescricao(dscProduto);
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return null;
	}

	private Produto obterTipoProduto(Integer ideProduto) {
		Exception erro = null;
		try {
			return this.lacPostoServiceFacade.carregarProdutoByIde(ideProduto);
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return null;
	}

	private void salvarOuAtualizarProdutosComercializados() {
		Exception erro = null;
		try {
			this.lacPostoServiceFacade.salvarOuAtualizarProdutosComercializados(this.produtoComercializado);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void removerEquipamentoEntorno() {
		this.lacPostoCombustivel.getTipoEquipamentoEntornoPostoCollection().remove(this.objetoARemover);
		
		Exception erro = null;
		try {
			
			this.lacPostoServiceFacade.removerEquipamentoEntorno(this.lacPostoCombustivel, (TipoEquipamentoEntornoPosto) this.objetoARemover);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void adicionarEquipamentoEntorno() {
		if (verificarDuplicidadeTipoEquipamentoEntorno()) {
			JsfUtil.addErrorMessage("O Tipo de Equipamento já foi adicionado");
		} else {
			this.obterTipoEquipamentoEntorno();
			this.tipoEquipamentoEntornoPosto.setPostoCombustivelCollection(new ArrayList<LacPostoCombustivel>());
			this.tipoEquipamentoEntornoPosto.getPostoCombustivelCollection().add(this.lacPostoCombustivel);
			this.lacPostoCombustivel.getTipoEquipamentoEntornoPostoCollection().add(this.tipoEquipamentoEntornoPosto);
			Exception erro = null;
			try {
				this.lacPostoServiceFacade.salvarOuAtualizarEquipamentosEntorno(this.tipoEquipamentoEntornoPosto);
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
			
			this.initTipoEquipamentoEntorno();
		}
	}

	private boolean verificarDuplicidadeTipoEquipamentoEntorno() {
		for (TipoEquipamentoEntornoPosto tipoEquipamento : this.lacPostoCombustivel.getTipoEquipamentoEntornoPostoCollection()) {
			if (this.tipoEquipamentoEntornoPosto.getIdeTipoEquipamentoEntornoPosto().equals(tipoEquipamento.getIdeTipoEquipamentoEntornoPosto())) {
				return true;
			}
		}

		return false;
	}

	private void obterTipoEquipamentoEntorno() {
		Exception erro = null;
		try {
			Integer ideTipoEquipamentoEntorno = this.tipoEquipamentoEntornoPosto.getIdeTipoEquipamentoEntornoPosto();
			this.tipoEquipamentoEntornoPosto = this.lacPostoServiceFacade.carregarTipoEquipamentoByIde(ideTipoEquipamentoEntorno);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void removerSistemaControle() {
		this.lacPostoCombustivel.getSistemaControlePostoCollection().remove(this.objetoARemover);
		Exception erro =null;
		try {
			this.lacPostoServiceFacade.removerSistemaControle((SistemaControlePosto) this.objetoARemover);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void adicionarSistemaControle() {
		if (verificarDuplicidadeSistemaControle()) {
			JsfUtil.addErrorMessage("O sistema de controle já foi adicionado.");
		} else {

			for (TipoSistemaControlePosto tipoSistema : this.tiposSistemaControleASalvar) {
				this.sistemaControlePosto.setIdeSistemaControlePosto(null);
				this.sistemaControlePosto.setIdeTipoSistemaControlePosto(null);
				this.obterTipoSistemaControle(tipoSistema.getIdeTipoSistemaControlePosto());
				this.sistemaControlePosto.setIdePostoCombustivel(this.lacPostoCombustivel);
				
				Exception erro =null;
				try {
					this.lacPostoServiceFacade.salvarOuAtualizarSistemaControle(this.sistemaControlePosto);
					this.lacPostoCombustivel.getSistemaControlePostoCollection().add(this.sistemaControlePosto.clone());
				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
				
			}

			this.initSistemaControlePosto();
		}
	}

	private boolean verificarDuplicidadeSistemaControle() {
		for (SistemaControlePosto sistema : this.lacPostoCombustivel.getSistemaControlePostoCollection()) {
			for (TipoSistemaControlePosto tipoSistemaControle : this.tiposSistemaControleASalvar) {
				if (tipoSistemaControle.equals(sistema.getIdeTipoSistemaControlePosto())) {
					return true;
				}
			}
		}

		return false;
	}

	private void obterTipoSistemaControle(Integer ideTipoSistemaControle) {
		Exception erro = null;
		try {
			TipoSistemaControlePosto tipoSistemaControle = this.lacPostoServiceFacade.carregarTipoSistemaControleByIde(ideTipoSistemaControle);
			this.sistemaControlePosto.setIdeTipoSistemaControlePosto(tipoSistemaControle);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void removerAreaAbastecimento() {
		this.listAreaAbastecimentoARemover.add((AreaAbastecimentoPostoCombustivel) this.objetoARemover);
		this.lacPostoCombustivel.getAreaAbastecimentoPostoCombustivelCollection().remove(this.objetoARemover);
		this.removerAreasAbastecimento(this.listAreaAbastecimentoARemover);
	}

	private void removerAreasAbastecimento(Collection<AreaAbastecimentoPostoCombustivel> listAreaAbastecimentoARemover) {
		Exception erro = null;
		try {
			this.lacPostoServiceFacade.removerAreasAbastecimento(listAreaAbastecimentoARemover);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void adicionarAreaAbastecimento() {
		if (verificarDuplicidadeAreaAbastecimento()) {
			JsfUtil.addErrorMessage("A área de abastecimento já foi adicionada.");
		} else {
			this.obterTipoAreaAbastecimento();
			this.obterTipoPermeabilidadeAntesReforma();
			this.obterTipoPermeabilidadeDepoisReforma();
			this.areaAbastecimentoPostoCombustivel.setPostoCombustivel(this.lacPostoCombustivel);
			this.lacPostoCombustivel.getAreaAbastecimentoPostoCombustivelCollection().add(this.areaAbastecimentoPostoCombustivel);
			Exception erro =null;
			try {
				this.lacPostoServiceFacade.salvarOuAtualizarAreaAbastecimento(this.areaAbastecimentoPostoCombustivel);
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
			
			this.initAreaAbastecimentoPosto();
		}
	}

	private boolean verificarDuplicidadeAreaAbastecimento() {
		for (AreaAbastecimentoPostoCombustivel area : this.lacPostoCombustivel.getAreaAbastecimentoPostoCombustivelCollection()) {
			if (this.areaAbastecimentoPostoCombustivel.getTipoAreaAbastecimento().equals(area.getTipoAreaAbastecimento())) {
				return true;
			}
		}

		return false;
	}

	private void obterTipoAreaAbastecimento() {
		Exception erro = null;
		try {
			Integer ideTipoSistemaControle = this.areaAbastecimentoPostoCombustivel.getTipoAreaAbastecimento().getIdeTipoAreaAbastecimento();
			TipoAreaAbastecimento tipoAreaAbastecimento = this.lacPostoServiceFacade.carregarTipoAreaAbastecimentoByIde(ideTipoSistemaControle);
			this.areaAbastecimentoPostoCombustivel.setTipoAreaAbastecimento(tipoAreaAbastecimento);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	private void obterTipoPermeabilidadeAntesReforma() {
		Exception erro = null;
		try {
			Integer ideTipoPermeabilidade = this.areaAbastecimentoPostoCombustivel.getTipoPermeabilidadeAntesReforma().getIdeTipoPermeabilidade();
			TipoPermeabilidade tipoPermeabilidade = obterTipoPermeabilidade(ideTipoPermeabilidade);
			this.areaAbastecimentoPostoCombustivel.setTipoPermeabilidadeAntesReforma(tipoPermeabilidade);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	private void obterTipoPermeabilidadeDepoisReforma() {
		Exception erro = null;
		try {
			Integer ideTipoPermeabilidade = this.areaAbastecimentoPostoCombustivel.getTipoPermeabilidadeDepoisReforma().getIdeTipoPermeabilidade();
			TipoPermeabilidade tipoPermeabilidade = obterTipoPermeabilidade(ideTipoPermeabilidade);
			this.areaAbastecimentoPostoCombustivel.setTipoPermeabilidadeDepoisReforma(tipoPermeabilidade);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public TipoPermeabilidade obterTipoPermeabilidade(Integer ideTipoPermeabilidade) throws Exception {
		TipoPermeabilidade tipoPermeabilidade = this.lacPostoServiceFacade.carregarTipoPermeabilidadeByIde(ideTipoPermeabilidade);
		return tipoPermeabilidade;
	}

	public void removerPostoCombustivelTanque() {
		this.listPostoCombustivelTanqueARemover.add((PostoCombustivelTanque) this.objetoARemover);
		this.lacPostoCombustivel.getPostoCombustivelTanqueCollection().remove(this.objetoARemover);
		Exception erro = null;
		try {
			this.lacPostoServiceFacade.removerPostosTanque(this.listPostoCombustivelTanqueARemover);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void adicionarPostoCombustivelTanque() {

		if (!this.validarPostoCombustivelTanque()) {
			return;
		}

		for (PostoCombustivelTanqueProduto produtoTanque : postoCombustivelTanque.getProdutoCollection()) {
			String dscProduto = produtoTanque.getIdeProduto().getDscProduto();
			Produto produto = this.obterTipoProduto(dscProduto);
			produtoTanque.setIdeProduto(produto);
		}
		this.postoCombustivelTanque.setIdePostoCombustivel(this.lacPostoCombustivel);
		this.obterTipoTanque();
		if (this.lacPostoCombustivel.getPostoCombustivelTanqueCollection().contains(this.postoCombustivelTanque)) {
			List<PostoCombustivelTanque> tanques = (List<PostoCombustivelTanque>) this.lacPostoCombustivel.getPostoCombustivelTanqueCollection();
			int index = tanques.indexOf(this.postoCombustivelTanque);
			tanques.set(index, this.postoCombustivelTanque);
		} else {
			this.lacPostoCombustivel.getPostoCombustivelTanqueCollection().add(this.postoCombustivelTanque);
		}
		this.exibeFormularioTanque = false;
		Exception erro = null;
		try {
			this.lacPostoServiceFacade.salvarOuAtualizarPostoCombustivelTanque(this.postoCombustivelTanque);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		this.initPostoCombustiveTanque();
	}

	private boolean validarPostoCombustivelTanque() {
		boolean valido = true;
		if (Util.isNullOuVazio(this.postoCombustivelTanque.getNomeTanque())) {
			valido = false;
			JsfUtil.addErrorMessage("O Campo Nome do Tanque é de preenchimento obrigatório.");
		}

		if (Util.isNull(this.postoCombustivelTanque.getIndInstalado())) {
			valido = false;
			JsfUtil.addErrorMessage("O Campo 'Instalado' é de preenchimento obrigatório.");
		}

		if (!Util.isNull(this.postoCombustivelTanque.getIndInstalado()) && !this.postoCombustivelTanque.getIndInstalado()
				&& Util.isNullOuVazio(this.postoCombustivelTanque.getDtcInstalacao())) {
			valido = false;
			JsfUtil.addErrorMessage("O Campo 'Data da instalação ou previsão da instalação' é de preenchimento obrigatório.");
		}

		if (!Util.isNull(this.postoCombustivelTanque.getIndInstalado()) && this.postoCombustivelTanque.getIndInstalado()) {
			if (Util.isNullOuVazio(this.postoCombustivelTanque.getDtcEstanqueidade())) {
				valido = false;
				JsfUtil.addErrorMessage("O Campo 'Data do último ensaio de estanqueidade realizados nos tanques' é de preenchimento obrigatório.");
			}

			if (Util.isNullOuVazio(this.postoCombustivelTanque.getDtcUltimaInspecao())) {
				valido = false;
				JsfUtil.addErrorMessage("O Campo 'Data da última inspeção técnica' é de preenchimento obrigatório.");
			}

		}

		if (Util.isNull(this.postoCombustivelTanque.getIdeTipoParedeTanque().getIdeTipoParedeTanque())) {
			valido = false;
			JsfUtil.addErrorMessage("O Campo 'Tipo de Parede' é de preenchimento obrigatório.");
		}

		if (Util.isNull(this.postoCombustivelTanque.getIdeTipoEstruturaTanque().getIdeTipoEstruturaTanque())) {
			valido = false;
			JsfUtil.addErrorMessage("O Campo 'Tipo da Estrutura' é de preenchimento obrigatório.");
		}

		if (Util.isNull(this.postoCombustivelTanque.getIdeTipoTanquePosto().getIdeTipoTanquePosto())) {
			valido = false;
			JsfUtil.addErrorMessage("O Campo 'Tipo da Tanque' é de preenchimento obrigatório.");
		}

		if (Util.isNull(this.postoCombustivelTanque.getIndTipoDescargaLocal())) {
			valido = false;
			JsfUtil.addErrorMessage("O Campo 'Tipo de descarga' é de preenchimento obrigatório.");
		}

		for (PostoCombustivelTanqueProduto produtoTanque : postoCombustivelTanque.getProdutoCollection()) {
			if (Util.isNullOuVazio(produtoTanque.getIdeProduto().getDscProduto())) {
				valido = false;
				JsfUtil.addErrorMessage("O Campo 'Produto Armazenado' é de preenchimento obrigatório.");
			}

			if (Util.isNullOuVazio(produtoTanque.getValCapacidade())) {
				valido = false;
				JsfUtil.addErrorMessage("O Campo 'Capacidade' é de preenchimento obrigatório.");
			}

			if (!valido) {
				break;
			}

		}

		return valido;
	}

	private void obterTipoTanque() {
		Exception erro = null;
		try {
			Integer ideTipoTanque = this.postoCombustivelTanque.getIdeTipoTanquePosto().getIdeTipoTanquePosto();
			TipoTanquePosto tipoTanquePosto = this.lacPostoServiceFacade.carregarTipoTanquePostoByIde(ideTipoTanque);
			this.postoCombustivelTanque.setIdeTipoTanquePosto(tipoTanquePosto);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public LacPostoCombustivel getLacPostoCombustivel() {
		return lacPostoCombustivel;
	}

	public void setLacPostoCombustivel(LacPostoCombustivel lacPostoCombustivel) {
		this.lacPostoCombustivel = lacPostoCombustivel;
	}

	public TipoEquipamentoEntornoPosto getTipoEquipamentoEntornoPosto() {
		return tipoEquipamentoEntornoPosto;
	}

	public void setTipoEquipamentoEntornoPosto(TipoEquipamentoEntornoPosto tipoEquipamentoEntornoPosto) {
		this.tipoEquipamentoEntornoPosto = tipoEquipamentoEntornoPosto;
	}

	public Collection<TipoBandeiraPostoCombustivel> getTiposBandeiraPosto() {
		return tiposBandeiraPosto;
	}

	public void setTiposBandeiraPosto(Collection<TipoBandeiraPostoCombustivel> tiposBandeiraPosto) {
		this.tiposBandeiraPosto = tiposBandeiraPosto;
	}

	public Collection<TipoEquipamentoEntornoPosto> getTiposEquipamentoEntornoPosto() {
		return tiposEquipamentoEntornoPosto;
	}

	public void setTiposEquipamentoEntornoPosto(Collection<TipoEquipamentoEntornoPosto> tiposEquipamentoEntornoPosto) {
		this.tiposEquipamentoEntornoPosto = tiposEquipamentoEntornoPosto;
	}

	public Collection<TipoServicoPosto> getTiposServicoPostoParte1() {
		List<TipoServicoPosto> listaDividida = (List<TipoServicoPosto>) tiposServicoPosto;
		List<TipoServicoPosto> subList = listaDividida.subList(0, listaDividida.size() / 2);
		return subList;
	}

	public Collection<TipoServicoPosto> getTiposServicoPostoParte2() {
		List<TipoServicoPosto> listaDividida = (List<TipoServicoPosto>) tiposServicoPosto;
		List<TipoServicoPosto> subList = listaDividida.subList(listaDividida.size() / 2, listaDividida.size());
		return subList;
	}

	public void verificarLista1TipoServico(ValueChangeEvent event) {

	}

	@SuppressWarnings("unchecked")
	public void verificarLista2TipoServico(ValueChangeEvent event) {
		List<TipoServicoPosto> listaTipoServicoPosto = (List<TipoServicoPosto>) event.getNewValue();
		this.verificarLista2tipoServico(listaTipoServicoPosto);
	}

	public void verificarLista2tipoServico(List<TipoServicoPosto> listaTipoServicoPosto) {
		exibeDscOutrosServicos = false;
		for (TipoServicoPosto tipoServicoPosto : listaTipoServicoPosto) {
			if (tipoServicoPosto.getIdeTipoServicoPosto().equals(TipoServicoPostoEnum.OUTROS.getId())) {
				exibeDscOutrosServicos = true;
			}
		}
	}

	public void verificarTipoParede(ValueChangeEvent event) {
		Integer quantidade = (Integer) event.getNewValue();
		this.postoCombustivelTanque.getProdutoCollection().clear();
		for (int i = 0; i < quantidade; i++) {
			PostoCombustivelTanqueProduto produto = new PostoCombustivelTanqueProduto();
			produto.setIdeProduto(new Produto());
			produto.setPostoCombustivelTanque(this.postoCombustivelTanque);
			this.postoCombustivelTanque.getProdutoCollection().add(produto);
		}
	}

	public void verificarTipoBandeira(ValueChangeEvent event) {
		TipoBandeiraPostoCombustivel tipoBandeiraPostoCombustivel = (TipoBandeiraPostoCombustivel) event.getNewValue();
		this.verificarTipoBandeira(tipoBandeiraPostoCombustivel);
	}

	public void verificarTipoBandeira(TipoBandeiraPostoCombustivel tipoBandeiraPostoCombustivel) {
		exibeDistribuidoras = false;
		if (tipoBandeiraPostoCombustivel.getIdeTipoBandeiraPostoCombustivel().equals(TipoBandeiraPostoEnum.DISTRIBUIDORA.getId())) {
			exibeDistribuidoras = true;
		}
	}

	public void verificarTipoDistribuidora(ValueChangeEvent event) {
		Integer idDistribuidora = (Integer) event.getNewValue();
		this.verificarTipoDistribuidora(idDistribuidora);
	}

	private void verificarTipoDistribuidora(Integer idDistribuidora) {
		this.exibeOutrosDistribuidoras = DistribuidoraPostoEnum.OUTROS.getId().equals(idDistribuidora);

	}

	public void verificarAcidente(ValueChangeEvent event) {
		Boolean houveAcidente = (Boolean) event.getNewValue();
		exibeObsAcidente = houveAcidente;
		if (!houveAcidente) {
			this.lacPostoCombustivel.setDscOcorrenciaAcidente(null);
		}
	}

	public void setTiposServicoPosto(Collection<TipoServicoPosto> tiposServicoPosto) {
		this.tiposServicoPosto = tiposServicoPosto;
	}

	public Collection<ClasseNbrPosto> getClassesNbrPosto() {
		return classesNbrPosto;
	}

	public void setClassesNbrPosto(Collection<ClasseNbrPosto> classesNbrPosto) {
		this.classesNbrPosto = classesNbrPosto;
	}

	public Collection<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(Collection<Produto> produtos) {
		this.produtos = produtos;
	}

	public Collection<TipoParedeTanque> getTiposParedeTanque() {
		return tiposParedeTanque;
	}

	public void setTiposParedeTanque(Collection<TipoParedeTanque> tiposParedeTanque) {
		this.tiposParedeTanque = tiposParedeTanque;
	}

	public Collection<TipoTanquePosto> getTiposTanque() {
		return tiposTanque;
	}

	public void setTiposTanque(Collection<TipoTanquePosto> tiposTanque) {
		this.tiposTanque = tiposTanque;
	}

	public PostoCombustivelProdutoComercializado getProdutoComercializado() {
		return produtoComercializado;
	}

	public void setProdutoComercializado(PostoCombustivelProdutoComercializado produtoComercializado) {
		this.produtoComercializado = produtoComercializado;
	}

	public SistemaControlePosto getSistemaControlePosto() {
		return sistemaControlePosto;
	}

	public void setSistemaControlePosto(SistemaControlePosto sistemaControlePosto) {
		this.sistemaControlePosto = sistemaControlePosto;
	}

	public Collection<TipoSistemaControlePosto> getTiposSistemaControlePosto() {
		return tiposSistemaControlePosto;
	}

	public void setTiposSistemaControlePosto(Collection<TipoSistemaControlePosto> tiposSistemaControlePosto) {
		this.tiposSistemaControlePosto = tiposSistemaControlePosto;
	}

	public AreaAbastecimentoPostoCombustivel getAreaAbastecimentoPostoCombustivel() {
		return areaAbastecimentoPostoCombustivel;
	}

	public void setAreaAbastecimentoPostoCombustivel(AreaAbastecimentoPostoCombustivel areaAbastecimentoPostoCombustivel) {
		this.areaAbastecimentoPostoCombustivel = areaAbastecimentoPostoCombustivel;
	}

	public Collection<TipoAreaAbastecimento> getTiposAreaAbastecimento() {
		return tiposAreaAbastecimento;
	}

	public void setTiposAreaAbastecimento(Collection<TipoAreaAbastecimento> tiposAreaAbastecimento) {
		this.tiposAreaAbastecimento = tiposAreaAbastecimento;
	}

	public PostoCombustivelTanque getPostoCombustivelTanque() {
		return postoCombustivelTanque;
	}

	public void setPostoCombustivelTanque(PostoCombustivelTanque postoCombustivelTanque) {
		this.postoCombustivelTanque = postoCombustivelTanque;
	}

	public Collection<TipoPermeabilidade> getTiposPermeabilidade() {
		return tiposPermeabilidade;
	}

	public void setTiposPermeabilidade(Collection<TipoPermeabilidade> tiposPermeabilidade) {
		this.tiposPermeabilidade = tiposPermeabilidade;
	}

	public boolean isExibeAbaGeral() {
		return exibeAbaGeral;
	}

	public void setExibeAbaGeral(boolean exibeAbaGeral) {
		this.exibeAbaGeral = exibeAbaGeral;
	}

	public boolean isExibeAbaAbastecimento() {
		return exibeAbaAbastecimento;
	}

	public void setExibeAbaAbastecimento(boolean exibeAbaAbastecimento) {
		this.exibeAbaAbastecimento = exibeAbaAbastecimento;
	}

	public boolean isExibeAbaControle() {
		return exibeAbaControle;
	}

	public void setExibeAbaControle(boolean exibeAbaControle) {
		this.exibeAbaControle = exibeAbaControle;
	}

	public boolean isExibeFormularioTanque() {
		return exibeFormularioTanque;
	}

	public void setExibeFormularioTanque(boolean exibeFormularioTanque) {
		this.exibeFormularioTanque = exibeFormularioTanque;
	}

	public boolean isExibeAbaManutencao() {
		return exibeAbaManutencao;
	}

	public void setExibeAbaManutencao(boolean exibeAbaManutencao) {
		this.exibeAbaManutencao = exibeAbaManutencao;
	}

	public Collection<TipoEstruturaTanque> getTiposEstruturaTanque() {
		return tiposEstruturaTanque;
	}

	public void setTiposEstruturaTanque(Collection<TipoEstruturaTanque> tiposEstruturaTanque) {
		this.tiposEstruturaTanque = tiposEstruturaTanque;
	}

	public Collection<DistribuidoraPosto> getDistribuidorasPosto() {
		return distribuidorasPosto;
	}

	public void setDistribuidorasPosto(Collection<DistribuidoraPosto> distribuidorasPosto) {
		this.distribuidorasPosto = distribuidorasPosto;
	}

	public Collection<TipoServicoPosto> getTiposServicoPosto() {
		return tiposServicoPosto;
	}

	public boolean isExibeDataInicioOperacao() {
		return exibeDataInicioOperacao;
	}

	public void setExibeDataInicioOperacao(boolean exibeDataInicioOperacao) {
		this.exibeDataInicioOperacao = exibeDataInicioOperacao;
	}

	public void setFaseEmpreendimento(FaseEmpreendimento faseEmpreendimento) {
		this.faseEmpreendimento = faseEmpreendimento;
	}

	public FaseEmpreendimento getFaseEmpreendimento() {
		return faseEmpreendimento;
	}

	public String getDscOutrosServicos() {
		return dscOutrosServicos;
	}

	public void setDscOutrosServicos(String dscOutrosServicos) {
		this.dscOutrosServicos = dscOutrosServicos;
	}

	public boolean isExibeDscOutrosServicos() {
		return exibeDscOutrosServicos;
	}

	public void setExibeDscOutrosServicos(boolean exibeDscOutrosServicos) {
		this.exibeDscOutrosServicos = exibeDscOutrosServicos;
	}

	public Collection<TipoServicoPosto> getListaTipoServicoPostoTemp1() {
		return listaTipoServicoPostoTemp1;
	}

	public void setListaTipoServicoPostoTemp1(Collection<TipoServicoPosto> listaTipoServicoPostoTemp1) {
		this.listaTipoServicoPostoTemp1 = listaTipoServicoPostoTemp1;
	}

	public Collection<TipoServicoPosto> getListaTipoServicoPostoTemp2() {
		return listaTipoServicoPostoTemp2;
	}

	public void setListaTipoServicoPostoTemp2(Collection<TipoServicoPosto> listaTipoServicoPostoTemp2) {
		this.listaTipoServicoPostoTemp2 = listaTipoServicoPostoTemp2;
	}

	public boolean isExibeDistribuidoras() {
		return exibeDistribuidoras;
	}

	public void setExibeDistribuidoras(boolean exibeDistribuidoras) {
		this.exibeDistribuidoras = exibeDistribuidoras;
	}

	public boolean isExibeObsAcidente() {
		return exibeObsAcidente;
	}

	public void setExibeObsAcidente(boolean exibeObsAcidente) {
		this.exibeObsAcidente = exibeObsAcidente;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public Object getObjetoARemover() {
		return objetoARemover;
	}

	public void setObjetoARemover(Object objetoARemover) {
		this.objetoARemover = objetoARemover;
	}

	public boolean isExibeOutrosDistribuidoras() {
		return exibeOutrosDistribuidoras;
	}

	public void setExibeOutrosDistribuidoras(boolean exibeOutrosDistribuidoras) {
		this.exibeOutrosDistribuidoras = exibeOutrosDistribuidoras;
	}

	public Collection<PostoCombustivelTanque> getListPostoCombustivelTanqueARemover() {
		return listPostoCombustivelTanqueARemover;
	}

	public void setListPostoCombustivelTanqueARemover(Collection<PostoCombustivelTanque> listPostoCombustivelTanqueARemover) {
		this.listPostoCombustivelTanqueARemover = listPostoCombustivelTanqueARemover;
	}

	public Collection<AreaAbastecimentoPostoCombustivel> getListAreaAbastecimentoARemover() {
		return listAreaAbastecimentoARemover;
	}

	public Collection<LacLegislacao> getLegislacoesAceitasLac() {
		return legislacoesAceitasLac;
	}

	public void setLegislacoesAceitasLac(Collection<LacLegislacao> legislacoesAceitasLac) {
		this.legislacoesAceitasLac = legislacoesAceitasLac;
	}

	public void setListAreaAbastecimentoARemover(Collection<AreaAbastecimentoPostoCombustivel> listAreaAbastecimentoARemover) {
		this.listAreaAbastecimentoARemover = listAreaAbastecimentoARemover;
	}

	public Collection<PostoCombustivelProdutoComercializado> getPostoCombustivelProdutoComercializados() {
		return postoCombustivelProdutoComercializados;
	}

	public void setPostoCombustivelProdutoComercializados(Collection<PostoCombustivelProdutoComercializado> postoCombustivelProdutoComercializados) {
		this.postoCombustivelProdutoComercializados = postoCombustivelProdutoComercializados;
	}

	public List<TipoSistemaControlePosto> getTiposSistemaControleASalvar() {
		return tiposSistemaControleASalvar;
	}

	public void setTiposSistemaControleASalvar(List<TipoSistemaControlePosto> tiposSistemaControleASalvar) {
		this.tiposSistemaControleASalvar = tiposSistemaControleASalvar;
	}

	public Date getDataAtual() {
		return new Date();
	}

	@Override
	protected String getCondicionantesFormularioLAC() {
		return new LacPostoCombustivel().getCondicionantesFormularioLAC();
	}

}