package br.gov.ba.seia.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.dao.EmpreendimentoDAOImpl;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoTipologia;
import br.gov.ba.seia.entity.EmpreendimentoVeiculo;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelUrbano;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TipoImovel;
import br.gov.ba.seia.entity.TipoVeiculo;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.VwConsultaEmpreendimento;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoEmpreendimentoEnum;
import br.gov.ba.seia.enumerator.TipoImovelEnum;
import br.gov.ba.seia.enumerator.TipoPessoaEnum;
import br.gov.ba.seia.enumerator.ValidacaoShapeEnum;
import br.gov.ba.seia.facade.CerhDadosGeraisServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EmpreendimentoVeiculoService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("empreendimentoController")
@ViewScoped
public class EmpreendimentoController extends SeiaControllerAb implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer abaImoveis = 1;
	
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	@EJB
	private CerhDadosGeraisServiceFacade cerhDadosGeraisFacade;
	
	@Inject
	private NovoRequerimentoController novoRequerimentoController;
	
	@Inject
	private LocalizacaoGeograficaController localizacaoGeograficaController;
	
	@Inject
	private EmpreendimentoDAOImpl empreendimentoDAOImpl;
	
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	
	private Imovel imovel;
	private Tipologia tipologia;
	private Empreendimento empreendimento;
	private EmpreendimentoTipologia empreendimentoTipologia;
	private LazyDataModel<PessoaFisica> pessoasFisicaModel;
	private LazyDataModel<PessoaJuridica> pessoasJuridicaModel;
	private DataTable pessoasFisicaDataTable;
	private DataTable pessoasJuridicaDataTable;
	private LazyDataModel<EnderecoEmpreendimento> empreedimentosModel;
	private Municipio municipio;
	private Collection<Municipio> listaMunicipio;
	private Collection<DocumentoIdentificacao> modelIdentificacao;
	private String tipoPessoa;
	List<SelectItem> itens;
	private String lblNome;
	private String lblDoc; //cpf/cnpj
	private String mascara;
	private String nome; //Nome ou Razão
	private String documento; // CPF ou CNPJ
	private Pessoa requerente;
	private String nomeEmpreendimento;
	/** Controla se a grid para visualizar deve ficar visível ou não. **/
	private boolean renderizaFisica;
	private boolean renderizaJuridica;
	private boolean desabilitaEnderecoCorrespondencia;
	private boolean desabilitaTabImoveisResponsavelTecnico = true;
	private boolean statusProcessoFormado;
	
	
	private boolean flagRequerenteDestino;
	private String formaVinculo;
	
	private String urlOrigem;
	private Boolean visualizarEmpreendimento;
	private VwConsultaEmpreendimento vwEmpreendimento;
	
	private TipoEmpreendimentoEnum tipoEmpreendimento;
	
	@EJB
	private PessoaService pessoaService;
	@EJB
	private TipologiaService tipologiaService;
	@EJB
	private ImovelService imovelService;
	@EJB
	private MunicipioService municipioService;
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;
	
	private TreeNode root;
	private TreeNode selectedNode;
	//Verificar se os veiculos podem ser editados de acrodo com o status do requerimento
	//Consultar método buscarRequerimentoAndTramitacaoPorEmpreendimento()  - RN 28 de SEIA - LAC - FCE
	private boolean disableEdicaoVeiculoByStatusRequerimento;
	
	
	@EJB
	private EmpreendimentoVeiculoService empreendimentoVeiculoService;
	private boolean edicaoTipoVeiculo;
	private TipoVeiculo tipoVeiculo;
	private List<TipoVeiculo> listaTipoVeiculo;
	private EmpreendimentoVeiculo empreendimentoVeiculo;
	private List<EmpreendimentoVeiculo> listaEmpreendimentoVeiculo;
	
	private final String ABAIMOVEL = "ABAIMOVEL";
	private final String HABILITAABAS  = "HABILITAABAS";
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	public EmpreendimentoController() {
	}

	@PostConstruct
	public void init() {
		empreendimentoVeiculo = new EmpreendimentoVeiculo();
		tipoPessoa = TipoPessoaEnum.FISICA.getId().toString();
		desabilitaAbaImovel(false);
		addAtributoSessao(HABILITAABAS,false);
		inicializar();		
	}

	private void inicializar() {
		try {

			inicializarVariaveis();
			carregarItens();
			
			carregarUrlParaRedirecionar();

			empreendimento = (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO");
				
			carregarRequerenteQuandoUsuarioExterno();
			
			if (!Util.isNullOuVazio(empreendimento)) {
				TipoImovel tipoImovel = null;
				if(!Util.isNullOuVazio(empreendimento.getImovelCollection())){
					setImovel(empreendimento.getImovelCollection().iterator().next());
					tipoImovel = getImovel().getIdeTipoImovel();
				}
				setRequerente(empreendimento.getIdePessoa());
				addAtributoSessao("REQUERENTE", empreendimento.getIdePessoa());
				
				Collection<EnderecoEmpreendimento> lColecaoEnderecoEmpreendimento = null;
				
				try {
					lColecaoEnderecoEmpreendimento = empreendimentoService.listarEnderecoByEmpreendimento(new EnderecoEmpreendimento(empreendimento));
				} catch (Exception e) {
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
				
				System.out.println();
				if (!Util.isNullOuVazio(lColecaoEnderecoEmpreendimento)) {
					if (!Util.isNullOuVazio(empreendimento.getIndEnderecoCorrespondencia()) && !empreendimento.getIndEnderecoCorrespondencia()){
					    setDesabilitaTabImoveisResponsavelTecnico(false);
					}
					else{
						desabilitaTabImoveisResponsavelTecnico = false;
						mudaStatusAbaNaSessao(true);
						desabilitaAbaImovel(false);
					}
				}
				
				//desabilitar a aba de imóvel caso seja Cessionario ou lac
				if(!Util.isNullOuVazio(empreendimento.getIndCessionario()) && empreendimento.getIndCessionario()){
					desabilitaAbaImovel(true);
					this.tipoEmpreendimento = TipoEmpreendimentoEnum.CESSIONARIO;
					if (!Util.isNullOuVazio(getImovel().getImovelUrbano()) && !Util.isNullOuVazio(getImovel().getImovelUrbano().getNumInscricaoIptu()) ) {
						abaImoveis = 0;
					}
				}
				
				if(!Util.isNullOuVazio(empreendimento.getIndConversaoTcraLac()) && empreendimento.getIndConversaoTcraLac()){
					desabilitaAbaImovel(true);
					this.tipoEmpreendimento = TipoEmpreendimentoEnum.LAC;
					if(!Util.isNullOuVazio(tipoImovel)){
						setAbaImoveis((!tipoImovel.getIdeTipoImovel().equals(TipoImovelEnum.RURAL.getId())?0:1));
					}
				}
				
				if(Util.isNull(this.tipoEmpreendimento)){
					this.tipoEmpreendimento = TipoEmpreendimentoEnum.PROPRIETARIO;
					if(!Util.isNullOuVazio(tipoImovel)){
					//	setAbaImoveis((!tipoImovel.getIdeTipoImovel().equals(TipoImovelEnum.RURAL.getId())?0:1));
					}
				}
				
				if (!Util.isNullOuVazio(empreendimento.getImovelCollection())) {
					for (Imovel lImovel : empreendimento.getImovelCollection()) {
						ImovelUrbano lImovelUrbano = lImovel.getImovelUrbano();
						if (!Util.isNullOuVazio(lImovelUrbano)) {
							imovel = lImovel;
							if (empreendimento.getIndCessionario()){
								abaImoveis = 0;
							}
							desabilitaAbaImovel(true);
						}
					}
				}
				
				carregarEmpreendimentoVeiculo();
				disableEdicaoVeiculoByStatusRequerimento = desabilitarCadastrarVeiculo(statusRequerimentoService.getMaxStatusRequerimentosByEmpreendimento(empreendimento));
				
				addAtributoSessao("EMPREENDIMENTO_SESSAO", empreendimento);
				
				statusProcessoFormado = empreendimentoDAOImpl.isEmpreendimentoProcessoFormado(empreendimento.getIdeEmpreendimento());
			
			}
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Houve um erro ao tentar carregar o empreendimento.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void inicializarVariaveis() {
		requerente = null;
		lblNome = "Nome";
		lblDoc = "CPF";
		mascara = "999.999.999-99";
		
		visualizarEmpreendimento = Boolean.FALSE;
		carregarMunicipios();
	}

	private void carregarRequerenteQuandoUsuarioExterno() {
		if(ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno()) {
			try {
				if(!Util.isNullOuVazio(urlOrigem) && urlOrigem.equalsIgnoreCase("/paginas/novo-requerimento/incluirNovoRequerimento.xhtml")) {
					requerente = novoRequerimentoController.getPessoa();
				}
				else if(urlOrigem.equalsIgnoreCase(PaginaEnum.PESQUISA_MINERAL_SEM_GUIA.getUrl()) || urlOrigem.equalsIgnoreCase(PaginaEnum.CADASTRO_CERH.getUrl())){
					requerente = ContextoUtil.getContexto().getPessoa();
				}
				else {
					requerente = pessoaService.carregarGet(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
				}
			} catch (Exception e) {
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
	}

	private void carregarUrlParaRedirecionar() {
		String lUrl = (String) getAtributoSession("URL_EMPREENDIMENTO_ORIGEM");

		if (Util.isNullOuVazio(lUrl)) { 
			urlOrigem = PaginaEnum.LISTAR_EMPREENDIMENTO.getUrl();
		} else {
			
			if(lUrl == PaginaEnum.CADASTRO_CERH.getUrl()){
				addAtributoSessao("ORIGEM_CERH", true);
				
				if(SessaoUtil.recuperarObjetoSessao("ORIGEM_CERH")!=null){
					
				}
				
			}
			
			urlOrigem = lUrl;
		}
		
	}
	
	/**
	 * Método para gerar uma requisição AJAX.
	 * 
	 * @author eduardo.fernandes 
	 */
	public void poll() {
		Html.executarJS("empreendimentoPoll.stop();");
		
		if(!Util.isNull(SessaoUtil.recuperarObjetoSessao("URL_EMPREENDIMENTO_ORIGEM")) 
				&& !Util.isNullOuVazio(urlOrigem) && urlOrigem.equals(PaginaEnum.CADASTRO_CERH.getUrl())) {
			
			Html.exibir("dlgMensagemIncluirEmpreendimento");
			SessaoUtil.removerObjetoSessao("URL_EMPREENDIMENTO_ORIGEM");
		}
	}
	
	/**
	 * O método verifica se na lista de {@link StatusRequerimento} existe algum dos staus abaixo:
	 * 	<ul>
	 *  <li>Enquadrado</li>
	 * 	<li>Em Validação Prévia</li> 
	 * 	<li>Validado</li>
	 * 	<li>Pagamento Liberado</li>
	 * 	<li>Comprovante Enviado</li>
	 *  <li>Boleto Validado</li>
	 * 	<li>Pendência Envio Documentação</li>
	 * 	<li>Pendência Validação Comprovante</li>
	 * </ul>
	 * Caso exista pelo menos um desses status não será possível editar a frota de veículos desete {@link Empreendimento}.
	 * <br>
	 * @param listaStatus - lista com os últimos status dos {@link Requerimento} associados ao {@link Empreendimento}
	 * @return true - impede edição da frota de veículos | false - permite edição da frota de veículos
	 */
	public boolean desabilitarCadastrarVeiculo(List<StatusRequerimento> listaStatus){
		for(StatusRequerimento statusRequerimento : listaStatus) {
			if(statusRequerimento.getIdeStatusRequerimento() == StatusRequerimentoEnum.ENQUADRADO.getStatus() 
				||statusRequerimento.getIdeStatusRequerimento() == StatusRequerimentoEnum.EM_VALIDACAO_PREVIA.getStatus() 
				||statusRequerimento.getIdeStatusRequerimento() == StatusRequerimentoEnum.VALIDADO.getStatus() 
				||statusRequerimento.getIdeStatusRequerimento() == StatusRequerimentoEnum.PAGAMENTO_LIBERADO.getStatus() 
				||statusRequerimento.getIdeStatusRequerimento() == StatusRequerimentoEnum.COMPROVANTE_ENVIADO.getStatus()
				||statusRequerimento.getIdeStatusRequerimento() == StatusRequerimentoEnum.BOLETO_VALIDADO.getStatus()
				||statusRequerimento.getIdeStatusRequerimento() == StatusRequerimentoEnum.PENDENCIA_ENVIO_DOCUMENTACAO.getStatus() 
				||statusRequerimento.getIdeStatusRequerimento() == StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getStatus() 
				){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @INFO Carrega a lista de tipo de veiculo utilizados para transporte
	 */
	public void carregarTipoVeiculo(){
		try {
			if(Util.isNullOuVazio(listaTipoVeiculo)){
				this.listaTipoVeiculo = empreendimentoVeiculoService.listarTipoVeiculo();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		
	}
	public void carregarEmpreendimentoVeiculo(){
		try {
			listaEmpreendimentoVeiculo = empreendimentoVeiculoService.listarEmpreendimentoVeiculoByEmpreedimento(getEmpreendimento());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	/**
	 * @INFO Salva empreendimento veículo
	 */
	public void salvarAtualizarEmpreendimentoVeiculo(){
		if(validarEmpreendimentoVeiculo()){
			if(!edicaoTipoVeiculo){
				empreendimentoVeiculo.setDtcCadastroVeiculo(new Date());
			}
			empreendimentoVeiculo.setIdeEmpreendimento(empreendimento);
			
			Exception erro = null;
			try {
				empreendimentoVeiculo.setDscPlacaCarroceria(empreendimentoVeiculo.getDscPlacaCarroceria().toUpperCase());
				empreendimentoVeiculo.setDscPlacaCavaloMecanico(empreendimentoVeiculo.getDscPlacaCavaloMecanico().toUpperCase());
				if(Util.isNullOuVazio(empreendimentoVeiculo.getDtcCadastroVeiculo())) {
					empreendimentoVeiculo.setDtcCadastroVeiculo(new Date());
				}
				empreendimentoVeiculoService.salvarEmpreendimentoVeiculo(empreendimentoVeiculo);
				if(!edicaoTipoVeiculo){
					if(Util.isNullOuVazio(listaEmpreendimentoVeiculo)){
						listaEmpreendimentoVeiculo = new ArrayList<EmpreendimentoVeiculo>();
					}
					listaEmpreendimentoVeiculo.add(empreendimentoVeiculo);
					JsfUtil.addSuccessMessage("Inclusão realizada com sucesso!");
				}else{
					JsfUtil.addSuccessMessage("Alteração realizada com sucesso!");
				}
				empreendimentoVeiculo = new EmpreendimentoVeiculo();
				edicaoTipoVeiculo = false;
			} catch (Exception e) {
				erro = e;
				JsfUtil.addErrorMessage("Erro ao salvar veículo utilizado para transporte.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
			
		}
	}
	/**
	 * @INFO Valida a dialog de incluir Veiculo
	 * @return true se válido.
	 */
	public boolean validarEmpreendimentoVeiculo(){
		boolean valido = true;
		if(Util.isNullOuVazio(empreendimentoVeiculo.getIdeTipoVeiculo())){
			valido = false;
			campoObrigatorio("Tipo de Veículo utilizado para transportes");
		}
		if(Util.isNullOuVazio(empreendimentoVeiculo.getDscPlacaCarroceria())){
			valido = false;
			campoObrigatorio("Placas de carroceria");
		}
		return valido;
	}
	
	public void campoObrigatorio(String str){
		JsfUtil.addErrorMessage(str+" é uma informação obrigatória.");
	}
	
	/**
	 * @INFO Garante que o EmpreendimentoVeiculo não será atualizado com uma nova data. 
	 */
	public void prepararEditarEmpreendimentoVeiculo(){
		carregarTipoVeiculo();
		edicaoTipoVeiculo = true;
	}
	/**
	 * @INFO Exclui o EmpreendimentoVeiculo
	 */
	public void excluirEmpreendimentoVeiculo(){
		
		Exception erro = null;
		try {
			empreendimentoVeiculoService.excluirEmpreendimentoVeiculo(empreendimentoVeiculo);
			listaEmpreendimentoVeiculo.remove(empreendimentoVeiculo);
			JsfUtil.addSuccessMessage("Veículo excluído com sucesso.");
		} catch (Exception e) {
			erro = e;
			JsfUtil.addErrorMessage("Erro ao excluir veículo utilizado para transporte.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	public void limparDialogEmpreendimentoVeiculo(){
		empreendimentoVeiculo = new EmpreendimentoVeiculo();
	}

	public void novoEmpreendimento() {
		setEmpreendimento(null);
	}

	public boolean isDesabilitaComponentesEmpreendimentoAbaIdentificacao() {
		if (Util.isNullOuVazio(getRequerente()) || Util.isNullOuVazio(getRequerente().getIdePessoa()))
			return true;
		else
			return false;
	}

	public boolean isDesabilitaComponentesTipologiaTelefoneAbaIdentificacao() {
		if (Util.isNullOuVazio(getEmpreendimento()) || Util.isNullOuVazio(getEmpreendimento().getIdeEmpreendimento()))
			return true;
		else
			return false;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public void novaTipologia() {
		setRoot(null);
		setTipologia(null);
		setSelectedNode(null);
	}

	public void salvarAtualizarEmpreendimento() {

		if(getEmpreendimento().getNomEmpreendimento().trim().equals("")){
			JsfUtil.addErrorMessage("Preencha o campo * Nome do empreendimento");
		}

		try {
			getEmpreendimento().setIdePessoa(getRequerente());
			if (Util.isNull(getEmpreendimento().getIdeEmpreendimento())) {

				if (getAbaImoveis().equals(Integer.valueOf(0))) {//Imóvel Urbano
					getEmpreendimento().setImovelCollection(new ArrayList<Imovel>());
					getImovel().setIdeTipoImovel(new TipoImovel(TipoImovelEnum.URBANO.getId()));
					getImovel().setDtcCriacao(new Date());
					getEmpreendimento().getImovelCollection().add(getImovel());
				}
				if (getEmpreendimento().getIndCessionario()) {
					getEmpreendimento().setImovelCollection(new ArrayList<Imovel>());
					getImovel().setIdeTipoImovel(new TipoImovel(TipoImovelEnum.CESSIONARIO.getId()));
					getImovel().setDtcCriacao(new Date());
					if (!getAbaImoveis().equals(Integer.valueOf(0))) {
						setAbaImoveis(1);
						getImovel().setImovelUrbano(null);
					}
					getEmpreendimento().getImovelCollection().add(getImovel());
				}
				empreendimentoService.salvarEmpreendimento(getEmpreendimento());
				JsfUtil.addSuccessMessage("Inclusão efetuada com Sucesso!");
			} 
			else {
				if (getAbaImoveis().equals(Integer.valueOf(0))) {//Imóvel Urbano
					getEmpreendimento().setImovelCollection(new ArrayList<Imovel>());
					getEmpreendimento().getImovelCollection().add(getImovel());
				}
				if (getEmpreendimento().getIndCessionario()) {
					getEmpreendimento().setImovelCollection(new ArrayList<Imovel>());
					Imovel i = new Imovel();
					i.setDtcCriacao(new Date());
					i.setIdeTipoImovel(new TipoImovel(TipoImovelEnum.CESSIONARIO.getId()));
					i.setDtcExclusao(null);
					getEmpreendimento().getImovelCollection().add(i);
					if (!Util.isNullOuVazio(getImovel().getIdeTipoImovel().getIdeTipoImovel()) && getImovel().getIdeTipoImovel().getIdeTipoImovel().equals(0)) {
						setAbaImoveis(1);
					}
				}
				empreendimentoService.atualizarEmpreendimento(getEmpreendimento());
				JsfUtil.addSuccessMessage("Alteração efetuada com Sucesso!");
			}
			this.addAtributoSessao("EMPREENDIMENTO_SESSAO", empreendimento);
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public Collection<Tipologia> getColTipologias() {
		return tipologiaService.filtrarListaTipologias(new Tipologia(new Tipologia()));
	}

	private void carregarMunicipios() {
		
		Exception erro = null;
		try {
			listaMunicipio = municipioService.filtrarListaMunicipiosPorEstado(new Estado(5));
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}


	public void trocaTipoPessoa(ValueChangeEvent event) {
		//Seta o tipoPessoa selecionado, para se basear nas buscas
		 Integer vlo = Integer.parseInt((String) event.getNewValue().toString());
		 setDocumento("");
		 setNome("");
		 limpaDataTableRequerentePFouPJ();
		if (vlo == TipoPessoaEnum.FISICA.getId() ) {
			lblNome = "Nome";
			lblDoc = "CPF";
			tipoPessoa = TipoPessoaEnum.FISICA.toString();
			mascara = "999.999.999-99";
		} else if(vlo == TipoPessoaEnum.JURIDICA.getId()){
			lblNome = "Razão Social";
			lblDoc = "CNPJ";
			tipoPessoa = TipoPessoaEnum.JURIDICA.toString();
			mascara = "99.999.999/9999-99";
		}
		setRenderizaFisica(false);
		setRenderizaJuridica(false);
	}

	private void limpaDataTableRequerentePFouPJ() {
		pessoasFisicaModel = null;
		pessoasJuridicaModel = null;
	}
	
	public boolean isPF(){
		return tipoPessoa.equals(TipoPessoaEnum.FISICA.getId().toString());
	}
	
	public boolean isPJ(){
		return tipoPessoa.equals(TipoPessoaEnum.JURIDICA.getId().toString());
	}

	/** Popular Radio **/
	public void carregarItens() {
		itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem(TipoPessoaEnum.FISICA.getId(), "Pessoa Física"));
		itens.add(new SelectItem(TipoPessoaEnum.JURIDICA.getId(), "Pessoa Jurídica"));
	}

	public void consultarRequerente() {
		if (isPF() || tipoPessoa.equals(TipoPessoaEnum.FISICA.name())){
			
			if(this.documento.isEmpty()){
				consultarPessoaFisica();
				setRenderizaFisica(true);
			}else if(validarDocumento(this.documento.trim())){
				consultarPessoaFisica();
				setRenderizaFisica(true);
			}else {
				JsfUtil.addErrorMessage("CPF inválido.");
			}
			
		} else if (isPJ() || tipoPessoa.equals(TipoPessoaEnum.JURIDICA.name())){
			
			if(this.documento.isEmpty()) {
				consultarPessoaJuridica();
				setRenderizaJuridica(true);
			}else if(validarDocumento(this.documento.trim())) {
				consultarPessoaJuridica();
				setRenderizaJuridica(true);
			}else {
				JsfUtil.addErrorMessage("CNPJ inválido.");
			}
		}
	}
	
	public boolean validarDocumento(String documento) {
		if(isPF()) return Util.isCPF(this.documento.trim());
		
		return Util.isCNPJ(this.documento.trim());
	}
	

	private void consultarPessoaFisica() {
		PessoaFisica lPessoa = new PessoaFisica();
		lPessoa.setNomPessoa(this.nome.trim());
		lPessoa.setNumCpf(this.documento.trim());
		carregarLazyModelPessoaFisica(lPessoa);		
	}

	private void carregarLazyModelPessoaFisica(final PessoaFisica pessoaSelecionada) {
		
		limpaDataTableRequerentePFouPJ();
		
		pessoasFisicaModel = new LazyDataModel<PessoaFisica>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<PessoaFisica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<PessoaFisica> pessoasFisicas = null;
				Exception erro = null;
				try {
					setPageSize(pageSize);
					pessoasFisicas = populateList(pessoaSelecionada, first, pageSize);
				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					JsfUtil.addErrorMessage(e.getMessage());
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
				
				return pessoasFisicas;
			}
		};
		pessoasFisicaModel.setRowCount(getRowCount(pessoaSelecionada));
	}

	private void consultarPessoaJuridica() {
		Exception erro = null;
		try {
			if ((Util.isNull(nome) || Util.isEmptyString(nome)) && (Util.isNull(documento) || Util.isEmptyString(documento))) {
				carregarLazyModelPessoaJuridica(new PessoaJuridica());
			} else {
				PessoaJuridica lPessoa = new PessoaJuridica();
				lPessoa.setNomRazaoSocial(this.nome.trim());
				lPessoa.setNumCnpj(this.documento.trim());
				carregarLazyModelPessoaJuridica(lPessoa);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	private void carregarLazyModelPessoaJuridica(final PessoaJuridica pessoaSelecionada) {
		
		limpaDataTableRequerentePFouPJ();
		
		pessoasJuridicaModel = new LazyDataModel<PessoaJuridica>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<PessoaJuridica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<PessoaJuridica> pessoasJuridicas = null;
				Exception erro = null;
				try {
					setPageSize(pageSize);
					pessoasJuridicas = populateListJuridica(pessoaSelecionada, first, pageSize);
				} catch (Exception e) {
					erro = e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					JsfUtil.addErrorMessage(e.getMessage());
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
				
				return pessoasJuridicas;
			}
		};
		pessoasJuridicaModel.setRowCount(getRowCountJuridica(pessoaSelecionada));
	}

	public void consultarEmpreendimentos() {
		Exception erro = null;
		try {
			LocalizacaoGeograficaController locGeoController = 
					(LocalizacaoGeograficaController) SessaoUtil.recuperarManagedBean("#{localizacaoGeograficaController}", LocalizacaoGeograficaController.class);
			locGeoController.setExisteTheGeom(false);
			
			LocalizacaoGeograficaImovelController locGeoImovController = 
					(LocalizacaoGeograficaImovelController) SessaoUtil.recuperarManagedBean("#{localizacaoGeograficaImovelController}", LocalizacaoGeograficaImovelController.class);
			locGeoImovController.setExisteTheGeom(false);
			
			
			carregarLazyModelEmpreendimento();
		
			Html.atualizar("filtroEmpreendimento");
		} catch (Exception e) {
			erro =e;	
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void excluirEmpreendimento() {
	 	try {
			if(!requerimentoUnicoService.verificarRequerimentoAssociadoEmpreendimento(empreendimento)){
				empreendimentoService.excluirEmpreendimento(empreendimento);
				consultarEmpreendimentos();
				JsfUtil.addSuccessMessage("Exclusão efetuada com sucesso.");
			}else{
				JsfUtil.addWarnMessage("Não é possível excluir este Empreendimento, pois existe um Requerimento relacionado a ele.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao excluir Empreendimento");
		}
	}

	public void limparTela() {
		this.requerente = null;
		this.nomeEmpreendimento = "";
		this.setRenderizaFisica(false);
		this.setRenderizaJuridica(false);
		this.municipio = null;
		this.setEmpreendimento(null);
		this.addAtributoSessao("EMPREENDIMENTO_SESSAO", null);
		limparTabela();
		empreendimentoModel = null;
	}

	private void limparTabela() {
		carregarLazyModelEmpreendimento();
	}

	public String finalizar() {
		Boolean origemCerh = false;
		String urlSessao = null;
		
		if(SessaoUtil.recuperarObjetoSessao("URL_EMPREENDIMENTO_ORIGEM")!=null){
			urlSessao = (String) SessaoUtil.recuperarObjetoSessao("URL_EMPREENDIMENTO_ORIGEM");
		}
		
		if(SessaoUtil.recuperarObjetoSessao("ORIGEM_CERH")!=null){
			origemCerh = (Boolean) SessaoUtil.recuperarObjetoSessao("ORIGEM_CERH");
		}
		
		if(origemCerh){
			this.addAtributoSessao("ORIGEM_CERH", null);
			urlOrigem = PaginaEnum.CADASTRO_CERH.getUrl();
		}

		if(!Util.isNullOuVazio(urlSessao) 
				&& !urlSessao.equals(PaginaEnum.PESQUISA_MINERAL_SEM_GUIA.getUrl()) 
				&& !urlOrigem.equals(PaginaEnum.CADASTRO_CERH.getUrl())) {
			
			this.addAtributoSessao("EMPREENDIMENTO_SESSAO", null);
		}
		
		
		return urlOrigem;
	}
	

	public String irParaEmpreendimento() {
		this.addAtributoSessao("EMPREENDIMENTO_SESSAO", null);
		return "/paginas/manter-empreendimento/empreendimentoLst.xhtml";
	}

	public String incluirEmpreendimento() {
		this.addAtributoSessao("EMPREENDIMENTO_SESSAO", null);
		return "/paginas/manter-empreendimento/empreendimento.xhtml";
	}
	
	
	public void mudaStatusAbaNaSessao(Boolean b) {
		addAtributoSessao(HABILITAABAS, b);
	}
	
	public void desabilitaAbaImovel(Boolean b){
		addAtributoSessao(ABAIMOVEL, b);
	}
	
	private boolean isUsuarioSemValidacao(){
		try {
			return !ContextoUtil.getContexto().isUsuarioExterno() || cerhDadosGeraisFacade.isProcuradorConveniado();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}  
	}
	
	public boolean isNecessitaValidacao(){
		if(isUsuarioSemValidacao()){
			String urlSessao = (String) SessaoUtil.recuperarObjetoSessao("URL_EMPREENDIMENTO_ORIGEM");
			if((!Util.isNullOuVazio(urlSessao) && urlSessao.equals(PaginaEnum.CADASTRO_CERH.getUrl()))
					|| (!Util.isNullOuVazio(urlOrigem) &&  urlOrigem.equals(PaginaEnum.CADASTRO_CERH.getUrl()))){
				return false;
			}
		}
		return true;
	}

	/** GETTERS AND SETTERS **/
	public Tipologia getTipologia() {
		if (Util.isNull(tipologia))
			tipologia = new Tipologia();
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public Empreendimento getEmpreendimento() {
		if (Util.isNull(empreendimento)) {
			empreendimento = (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO");
			if (Util.isNull(empreendimento))
				empreendimento = new Empreendimento();
				empreendimento.setIndEnderecoCorrespondencia(true);
		}
		return empreendimento;
	}
	
	public void setEmpreendimento(Empreendimento pEmpreendimento) {
		try {
			if (!Util.isNullOuVazio(pEmpreendimento)) {
				this.empreendimento = empreendimentoService.filtrarEmpreendimento(pEmpreendimento);
				this.addAtributoSessao("EMPREENDIMENTO_SESSAO", empreendimento);
			} else {
				this.empreendimento = pEmpreendimento;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Houve um erro ao tentar carregar o empreendimento.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<Municipio> getListaMunicipio() {
		return listaMunicipio;
	}

	public EmpreendimentoTipologia getEmpreendimentoTipologia() {
		return empreendimentoTipologia;
	}

	public void setEmpreendimentoTipologia(EmpreendimentoTipologia empreendimentoTipologia) {
		this.empreendimentoTipologia = empreendimentoTipologia;
	}

	public void setListaMunicipio(Collection<Municipio> listaMunicipio) {
		this.listaMunicipio = listaMunicipio;
	}

	public LazyDataModel<PessoaFisica> getPessoasFisicaModel() {
		return pessoasFisicaModel;
	}

	public void setPessoasFisicaModel(LazyDataModel<PessoaFisica> pessoasFisicaModel) {
		this.pessoasFisicaModel = pessoasFisicaModel;
	}

	public LazyDataModel<PessoaJuridica> getPessoasJuridicaModel() {
		return pessoasJuridicaModel;
	}

	public void setPessoasJuridicaModel(LazyDataModel<PessoaJuridica> pessoasJuridicaModel) {
		this.pessoasJuridicaModel = pessoasJuridicaModel;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getLblNome() {
		return lblNome;
	}

	public void setLblNome(String lblNome) {
		this.lblNome = lblNome;
	}

	public String getLblDoc() {
		return lblDoc;
	}

	public void setLblDoc(String lblDoc) {
		this.lblDoc = lblDoc;
	}

	public String getMascara() {
		if(!Util.isNullOuVazio(getTipoPessoa()) && getTipoPessoa().equals(TipoPessoaEnum.FISICA.getId().toString())){
			mascara = "999.999.999-99";	
		}
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}

	public List<SelectItem> getItens() {
		return itens;
	}

	public void setItens(List<SelectItem> itens) {
		this.itens = itens;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public boolean isRenderizaFisica() {
		return renderizaFisica;
	}

	public void setRenderizaFisica(boolean pRenderizaFisica) {
		this.renderizaFisica = pRenderizaFisica;
		if (pRenderizaFisica) {
			this.setRenderizaJuridica(false);
		}
	}

	public boolean isRenderizaJuridica() {
		return renderizaJuridica;
	}

	public void setRenderizaJuridica(boolean pRenderizaJuridica) {
		this.renderizaJuridica = pRenderizaJuridica;
		if (pRenderizaJuridica) {
			this.renderizaFisica = false;
		}
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public LazyDataModel<EnderecoEmpreendimento> getEmpreedimentosModel() {
		return empreedimentosModel;
	}

	public void setEmpreedimentosModel(LazyDataModel<EnderecoEmpreendimento> empreedimentosModel) {
		this.empreedimentosModel = empreedimentosModel;
	}

	public Pessoa getRequerente() {
		if (Util.isNullOuVazio(requerente))
			requerente = new Pessoa();
		return requerente;
	}

	public void setRequerente(Pessoa pRequerente) {
		this.requerente = pRequerente;
		if (!Util.isNullOuVazio(pRequerente)) {
			addAtributoSessao("REQUERENTE", this.requerente);
			if (tipoPessoa.equals(TipoPessoaEnum.FISICA.toString()) && !Util.isNullOuVazio(pRequerente.getPessoaFisica())) {

			} else {
				if (!Util.isNullOuVazio(pRequerente.getPessoaJuridica())) {
					
				}
			}
		}
	}

	public String getDescnome() {
		if(!Util.isNull(this.requerente)){
			if (!Util.isNull(requerente.getPessoaFisica())) {
					return this.requerente.getPessoaFisica().getNomPessoa();
			}
			else if(!Util.isNull(this.requerente.getPessoaJuridica())) {
				return this.requerente.getPessoaJuridica().getNomRazaoSocial();
			}
		}
		else {
			return "";
		}
		
		return "";
	}

	public String getNomeEmpreendimento() {
		return nomeEmpreendimento;
	}

	public void setNomeEmpreendimento(String nomeEmpreendimento) {
		this.nomeEmpreendimento = nomeEmpreendimento;
	}

	public Integer getAbaImoveis() {
		return abaImoveis;
	}

	public void setAbaImoveis(Integer abaImoveis) {
		this.abaImoveis = abaImoveis;
	}

	public Collection<DocumentoIdentificacao> getModelIdentificacao() {
		return modelIdentificacao;
	}

	public void setModelIdentificacao(Collection<DocumentoIdentificacao> modelIdentificacao) {
		this.modelIdentificacao = modelIdentificacao;
	}

	public boolean isFlagAbaImovel() {
		return getAbaImoveis() != null && getAbaImoveis().intValue() == 1;
	}

	public boolean isFlagPanelEnderecoCorrespondencia() {
		return !Util.isNull(empreendimento.getIndEnderecoCorrespondencia()) && !empreendimento.getIndEnderecoCorrespondencia();
	}

	public void changeListenerTipoImovel(ValueChangeEvent pValueChangeEvent) {
		if (pValueChangeEvent.getNewValue() instanceof Integer) {
			Integer vlo = (Integer) pValueChangeEvent.getNewValue();
			if(vlo == 1 || (!Util.isNullOuVazio(empreendimento.getEnderecoEmpreendimentoCollection()) && empreendimento.getEnderecoEmpreendimentoCollection().size() == 2 )){
				setAbaImoveis(1);
				desabilitaAbaImovel(false);
			}
			else { 
				setAbaImoveis(0);
				desabilitaAbaImovel(true);
			}
		}
	}

	public void changeListenerEnderecoCorrespondencia(ValueChangeEvent pValueChangeEvent) {
		Boolean lNovoValor = (Boolean) pValueChangeEvent.getNewValue();
		if (lNovoValor) {
			this.setDesabilitaTabImoveisResponsavelTecnico(true);
		} 
		else {
			this.setDesabilitaTabImoveisResponsavelTecnico(false);
		}
		JsfUtil.addErrorMessage("Para salvar essa alteração clique no botão 'Salvar Empreendimento'.");
	}

	public void changeListenerTipologia() {
		try {
			setRoot(empreendimentoService.montarArvoreTipologia(this.tipologia));
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void changeVinculoImovel(ValueChangeEvent event){
		Boolean vinculoImovel = (Boolean) event.getNewValue();

		if(statusProcessoFormado) {
			JsfUtil.addWarnMessage(BUNDLE.getString("empreendimento_msg_possui_processo_formado"));
		}
		
		if(this.abaImoveis == 1){
			if(!Util.isNullOuVazio(vinculoImovel) && vinculoImovel ){
				desabilitaAbaImovel(true);
			}
			else {
				desabilitaAbaImovel(false);
			}
		}else{
			desabilitaAbaImovel(true);
		}
	}
	
	public void changeBaseOperacional(ValueChangeEvent event){
		empreendimento.setIndBaseOperacional((Boolean) event.getNewValue());
	}
	
	public void changeVinculoImovelTcra(ValueChangeEvent event){
		TipoEmpreendimentoEnum tipoEmpreendimento = (TipoEmpreendimentoEnum) event.getNewValue();
		if(this.abaImoveis == 1){
			switch (tipoEmpreendimento) {
			case CESSIONARIO:
				this.empreendimento.setIndCessionario(true);
				this.empreendimento.setIndConversaoTcraLac(false);
				desabilitaAbaImovel(true);
				break;
			case PROPRIETARIO:
				this.empreendimento.setIndCessionario(false);
				this.empreendimento.setIndConversaoTcraLac(false);
				desabilitaAbaImovel(false);
				break;
			case LAC:
				this.empreendimento.setIndConversaoTcraLac(true);
				this.empreendimento.setIndCessionario(false);
				desabilitaAbaImovel(true);
				break;	
			default:
				desabilitaAbaImovel(false);
				break;
			}
		}else{
			switch (tipoEmpreendimento) {
			case CESSIONARIO:
				this.empreendimento.setIndCessionario(true);
				this.empreendimento.setIndConversaoTcraLac(false);
				break;
			case PROPRIETARIO:
				this.empreendimento.setIndCessionario(false);
				this.empreendimento.setIndConversaoTcraLac(false);
				break;
			case LAC:
				this.empreendimento.setIndConversaoTcraLac(true);
				this.empreendimento.setIndCessionario(false);
				break;	
			default:
				desabilitaAbaImovel(true);
				break;
			}
			desabilitaAbaImovel(true);
		}
		 
	}

	public String getFormaVinculo() {
		return formaVinculo;
	}

	public void setFormaVinculo(String formaVinculo) {
		this.formaVinculo = formaVinculo;
	}

	private EmpreendimentoTipologia montarEmpreendimentoTipologia(Tipologia tipologia){
		EmpreendimentoTipologia empTipologia = new EmpreendimentoTipologia(); 
		empTipologia.setTipologiaGrupo(tipologia.getTipologiaGrupo());
		empTipologia.setEmpreendimento(getEmpreendimento());
		return empTipologia;
	}

	public void adicionaGrupoTipologia(boolean fecharDialog) {
		try {
			if (!Util.isNullOuVazio(getSelectedNode())) {
				Tipologia lTipologiaSelecionada = (Tipologia) getSelectedNode().getData();
				if (lTipologiaSelecionada.getIndPossuiFilhos()) {
					JsfUtil.addWarnMessage("Esta Tipologia não é uma Atividade.");
				} 
				else {
					EmpreendimentoTipologia empreendimentoTipologia = montarEmpreendimentoTipologia(lTipologiaSelecionada);
					empreendimentoService.salvarEmpreendimentoTipologia(empreendimentoTipologia);
					if(Util.isNullOuVazio(empreendimento.getEmpreendimentoTipologiaCollection())){
						empreendimento.setEmpreendimentoTipologiaCollection(new ArrayList<EmpreendimentoTipologia>());
					}
					empreendimento.getEmpreendimentoTipologiaCollection().add(empreendimentoTipologia);
					novaTipologia();
					JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
					if(fecharDialog){
						RequestContext.getCurrentInstance().execute("dlgTipologia.hide();");						
					}
				}
			} 
			else {
				if (!Util.isNullOuVazio(getTipologia()) && !Util.isNullOuVazio(getTipologia().getIdeTipologia())) {
					JsfUtil.addWarnMessage("Erro de Tipologia: A tipologia não foi selecionada na árvore.");
				}
				else {
					JsfUtil.addWarnMessage("O campo Divisão é de preenchimento obrigatório.");
				}
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}		
	}

	public void excluirEmpreendimentoTipologia() {
		Exception erro = null;
		try {
			empreendimento.getEmpreendimentoTipologiaCollection().remove(empreendimentoTipologia);
			empreendimentoService.excluirEmpreendimentoTipologia(empreendimentoTipologia);
			if(empreendimentoTipologia.getTipologiaGrupo().getIdeTipologia().isTipologiaTransportadoraResiduosPerigosos()){
				empreendimentoVeiculoService.excluirEmpreendimentoVeiculoByEmpreedimento(listaEmpreendimentoVeiculo);
				listaEmpreendimentoVeiculo.clear();
			}
			requerimentoTipologiaService.removerByEmpreendimentoByTipologiaGrupo(empreendimentoTipologia.getEmpreendimento().getIdeEmpreendimento(), empreendimentoTipologia.getTipologiaGrupo().getIdeTipologiaGrupo());
			JsfUtil.addSuccessMessage("Tipologia excluída com sucesso!");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	/**
	 * Metódo para salvar o indicador de base operacional.
	 */
	public void salvarDadosEmpreendimento(){ 
		Exception erro = null;
		
		if(validarVeiculo()){
			try {
				empreendimentoService.salvarDadosEmpreendimento(getEmpreendimento());
				JsfUtil.addSuccessMessage("Salvo com sucesso.");
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				JsfUtil.addErrorMessage("Erro ao salvar a base.");
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		
	}

	private boolean validarVeiculo() {
		if(Util.isNullOuVazio(listaEmpreendimentoVeiculo)){
			JsfUtil.addErrorMessage("O campo Incluir Veículo " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			return false;
		}
		return true;
	}

	private boolean visualiza;
	private UIForm formularioASerLimpoDialogTelefone;

	public void limpar() {
		setVisualiza(false);
	}

	public boolean isVisualiza() {
		return visualiza;
	}

	public void setVisualiza(boolean visualiza) {
		this.visualiza = visualiza;
	}

	public UIForm getFormularioASerLimpoDialogTelefone() {
		return formularioASerLimpoDialogTelefone;
	}

	public void setFormularioASerLimpoDialogTelefone(UIForm formularioASerLimpoDialogTelefone) {
		this.formularioASerLimpoDialogTelefone = formularioASerLimpoDialogTelefone;
	}

	public boolean isDesabilitaEnderecoCorrespondencia() {
		return desabilitaEnderecoCorrespondencia;
	}

	public void setDesabilitaEnderecoCorrespondencia(boolean desabilitaEnderecoCorrespondencia) {
		this.desabilitaEnderecoCorrespondencia = desabilitaEnderecoCorrespondencia;
	}

	public boolean isDesabilitaTabImoveisResponsavelTecnico() {
		return desabilitaTabImoveisResponsavelTecnico;
	}

	public void setDesabilitaTabImoveisResponsavelTecnico(boolean desabilitaTabImoveisResponsavelTecnico) {
		this.desabilitaTabImoveisResponsavelTecnico = desabilitaTabImoveisResponsavelTecnico;
		addAtributoSessao(HABILITAABAS, !desabilitaTabImoveisResponsavelTecnico);
	}

	public boolean isEmpreendimentoTipologiaAdicionado() {
		return !Util.isNullOuVazio(empreendimento) && !Util.isNullOuVazio(empreendimento.getEmpreendimentoTipologiaCollection());
	}

	public boolean isPossuiEmpreendimento() {
		if (!Util.isNullOuVazio(getEmpreendimento()) && !Util.isNullOuVazio(getEmpreendimento().getIdeEmpreendimento()))
			return true;
		return false;
	}

	public String getUrlOrigem() {
		return urlOrigem;
	}

	public void setUrlOrigem(String urlOrigem) {
		addAtributoSessao("URL_EMPREENDIMENTO_ORIGEM", urlOrigem);
		this.urlOrigem = urlOrigem;
	}

	public Imovel getImovel() {
		if (Util.isNullOuVazio(imovel)) {
			imovel = new Imovel();
			imovel.setImovelUrbano(new ImovelUrbano());
		}
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	protected List<PessoaFisica> populateList(PessoaFisica pessoaSelecionada, int first, int pageSize) {
		List<PessoaFisica> pessoasFisicas = new ArrayList<PessoaFisica>();
		Exception erro = null;
		try {
			pessoasFisicas = pessoaFisicaService.listarPorCriteriaDemanda(pessoaSelecionada, first, pageSize);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return pessoasFisicas;
	}

	protected int getRowCount(PessoaFisica pessoaSelecionada) {
		int totalRowCount = 0;
		Exception erro = null;
		try {
			totalRowCount = pessoaFisicaService.count(pessoaSelecionada);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return totalRowCount;
	}

	protected List<PessoaJuridica> populateListJuridica(PessoaJuridica pessoaJuridicaSelecionada, int first, int pageSize) {
		List<PessoaJuridica> pessoasJuridicas = new ArrayList<PessoaJuridica>();
		Exception erro = null;
		try {
			if(flagRequerenteDestino) {
				pessoasJuridicas = pessoaJuridicaService.listarPorCriteriaDemandaSemRestricao(pessoaJuridicaSelecionada, first, pageSize);
			} else {
				pessoasJuridicas = pessoaJuridicaService.listarPorCriteriaDemanda(pessoaJuridicaSelecionada, first, pageSize);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return pessoasJuridicas;
	}

	protected int getRowCountJuridica(PessoaJuridica pessoaJuridicaSelecionada) {
		int totalRowCount = 0;
		Exception erro = null;
		try {
			if(flagRequerenteDestino) {
				totalRowCount = pessoaJuridicaService.countSemRestricao(pessoaJuridicaSelecionada);
			} else {
				totalRowCount = pessoaJuridicaService.count(pessoaJuridicaSelecionada);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return totalRowCount;
	}

	/*** .... Teste .... ***/
	private LazyDataModel<VwConsultaEmpreendimento> empreendimentoModel;

	public LazyDataModel<VwConsultaEmpreendimento> getEmpreendimentoModel() {
		return empreendimentoModel;
	}

	public void setEmpreendimentoModel(LazyDataModel<VwConsultaEmpreendimento> empreendimentoModel) {
		this.empreendimentoModel = empreendimentoModel;
	}

	public void carregarLazyModelEmpreendimento() {
		empreendimentoModel = new LazyDataModel<VwConsultaEmpreendimento>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<VwConsultaEmpreendimento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<VwConsultaEmpreendimento> empreendimentos = null;
				
				try {
					setPageSize(pageSize);
					empreendimentos = populateListEmpreendimento(first, pageSize);
				} catch (Exception e) {
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
				
				return empreendimentos;
			}
		};
		
		empreendimentoModel.setRowCount(getRowCountEmpreendimento());
	}

	protected List<VwConsultaEmpreendimento> populateListEmpreendimento(int first, int pageSize) {
		List<VwConsultaEmpreendimento> empreedimentos = new ArrayList<VwConsultaEmpreendimento>();

		try {
			empreedimentos = empreendimentoService.listarPorCriteriaDemanda(this.municipio, this.requerente, this.nomeEmpreendimento, first, pageSize);
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return empreedimentos;
	}

	protected int getRowCountEmpreendimento() {
		int totalRowCount = 0;

		try {
			totalRowCount = empreendimentoService.count(this.municipio, this.requerente, this.nomeEmpreendimento);
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return totalRowCount;
	}

	public void limparFiltroRequerente(){
		nome = null;
		documento = null;
		requerente = null;
		limpaDataTableRequerentePFouPJ();
		renderizaFisica = false;
		renderizaJuridica = false;
		
		tipoPessoa = TipoPessoaEnum.FISICA.getId().toString();
		lblNome = "Nome";
		lblDoc = "CPF";
		mascara = "999.999.999-99";
		carregarItens();
	}
	
	public void exibeSomentePessoaJuridicaFiltroRequerente() {
		nome = null;
		documento = null;
		requerente = null;
		limpaDataTableRequerentePFouPJ();
		renderizaFisica = false;
		renderizaJuridica = false;
		
		tipoPessoa = TipoPessoaEnum.JURIDICA.getId().toString();
		lblNome = "Razão Social";
		lblDoc = "CNPJ";
		mascara = "99.999.999/9999-99";
		
		itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem(TipoPessoaEnum.JURIDICA.getId(), "Pessoa Jurídica"));
	}

	public void trataArquivo(FileUploadEvent event) {
		localizacaoGeograficaController.setTipoSecaoGeometrica(ValidacaoShapeEnum.POLIGONO.getId());
		localizacaoGeograficaController.trataArquivo(event);
	}
	
	public void atualizarMunicipiosAdicionais(){
		Html.atualizar("tabAbas:frmMunicipiosAdicionais");
		
	}
	
	public VwConsultaEmpreendimento getVwEmpreendimento() {
		return vwEmpreendimento;
	}

	public void setVwEmpreendimento(VwConsultaEmpreendimento vwEmpreendimento) {
		this.vwEmpreendimento = vwEmpreendimento;
		setEmpreendimento(new Empreendimento(vwEmpreendimento.getIdeEmpreendimento()));
	}

	public Boolean getVisualizarEmpreendimento() {
		if (!Util.isNull(ContextoUtil.getContexto().getVisualizarEmpreendimento())) {
			visualizarEmpreendimento = ContextoUtil.getContexto().getVisualizarEmpreendimento();
		}
		return visualizarEmpreendimento;
	}

	public void setVisualizarEmpreendimento(Boolean visualizarEmpreendimento) {
		ContextoUtil.getContexto().setVisualizarEmpreendimento(visualizarEmpreendimento);
		this.visualizarEmpreendimento = visualizarEmpreendimento;
	}
	
	public boolean isExternalUser(){
		return (ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil() == 2);
	}

	public boolean isAtende(){
		return (ContextoUtil.getContexto().getUsuarioLogado().isAtende());
	}
	
	public TipoEmpreendimentoEnum getTipoEmpreendimento() {
		return tipoEmpreendimento;
	}

	public void setTipoEmpreendimento(TipoEmpreendimentoEnum tipoEmpreendimento) {
		this.tipoEmpreendimento = tipoEmpreendimento;
	}

	public List<TipoVeiculo> getListaTipoVeiculo() {
		return listaTipoVeiculo;
	}

	public void setListaTipoVeiculo(List<TipoVeiculo> listaTipoVeiculo) {
		this.listaTipoVeiculo = listaTipoVeiculo;
	}

	public TipoVeiculo getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	public boolean isRenderizarCadastroTipoVeiculo() {
		if(isEmpreendimentoTipologiaAdicionado()){
			for(EmpreendimentoTipologia empreendimentoTipologia : getEmpreendimento().getEmpreendimentoTipologiaCollection()){
				if(empreendimentoTipologia.getTipologiaGrupo().getIdeTipologia().isTipologiaTransportadoraResiduosPerigosos()){
					return true;
				}
			}
		}
		return false;
	}

	public EmpreendimentoVeiculo getEmpreendimentoVeiculo() {
		return empreendimentoVeiculo;
	}

	public void setEmpreendimentoVeiculo(EmpreendimentoVeiculo empreendimentoVeiculo) {
		this.empreendimentoVeiculo = empreendimentoVeiculo;
	}

	public List<EmpreendimentoVeiculo> getListaEmpreendimentoVeiculo() {
		return listaEmpreendimentoVeiculo;
	}

	public void setListaEmpreendimentoVeiculo(List<EmpreendimentoVeiculo> listaEmpreendimentoVeiculo) {
		this.listaEmpreendimentoVeiculo = listaEmpreendimentoVeiculo;
	}

	public boolean isEdicaoTipoVeiculo() {
		return edicaoTipoVeiculo;
	}

	public void setEdicaoTipoVeiculo(boolean edicaoTipoVeiculo) {
		this.edicaoTipoVeiculo = edicaoTipoVeiculo;
	}

	public boolean isDisableEdicaoVeiculoByStatusRequerimento() {
		return disableEdicaoVeiculoByStatusRequerimento;
	}

	public void setDisableEdicaoVeiculoByStatusRequerimento(boolean disableEdicaoVeiculoByStatusRequerimento) {
		this.disableEdicaoVeiculoByStatusRequerimento = disableEdicaoVeiculoByStatusRequerimento;
	}

	public boolean isFlagRequerenteDestino() {
		return flagRequerenteDestino;
	}

	public void setFlagRequerenteDestino(boolean flagRequerenteDestino) {
		this.flagRequerenteDestino = flagRequerenteDestino;
	}

	public DataTable getPessoasFisicaDataTable() {
		return pessoasFisicaDataTable;
	}

	public void setPessoasFisicaDataTable(DataTable pessoasFisicaDataTable) {
		this.pessoasFisicaDataTable = pessoasFisicaDataTable;
	}

	public DataTable getPessoasJuridicaDataTable() {
		return pessoasJuridicaDataTable;
	}

	public void setPessoasJuridicaDataTable(DataTable pessoasJuridicaDataTable) {
		this.pessoasJuridicaDataTable = pessoasJuridicaDataTable;
	}

	
}