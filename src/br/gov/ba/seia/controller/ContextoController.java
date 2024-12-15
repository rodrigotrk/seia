package br.gov.ba.seia.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.MenuModel;

import br.gov.ba.seia.dto.PessoaRequerimentoDTO;
import br.gov.ba.seia.dto.identificarPapel.SolicitacaoDTO;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.Comunicacao;
import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ObjetivoRequerimentoLimpezaArea;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.Pergunta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.entity.TipoVinculoPCT;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.ComunicacaoStatusEnum;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.service.ComunicacaoService;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.service.UsuarioService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.DataUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityService;

@Named("contextoController")
@SessionScoped
public class ContextoController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Usuario usuarioLogado;
	private Pessoa pessoa;
	private PessoaFisica pessoaFisica;
	private PessoaJuridica pessoaJuridica;
	private Empreendimento empreendimento;
	private Endereco requerimentoEndereco;
	private Requerimento requerimentoDasLocGeoDoReqUnico;
	private Object object;
	private Object objectToLocGeo;
	private PessoaRequerimentoDTO reqPapeisDTO = new PessoaRequerimentoDTO();
	private Boolean visualizarPessoaFisica;
	private Boolean visualizarPessoaJuridica;
	private boolean visualizarDLA;
	private Boolean visualizarEmpreendimento;
	private Pessoa solicitanteRequerimento;
	private Pessoa requerentePesquisa;
	private Boolean bloquearLocGeoImovelDepoisDeImovelSalvo;
	private Boolean bloquearEnderecoImovelDepoisDeImovelSalvo;
	//VARIAVEIS PARA IMOVEL RURAL
	private TipoRequerimento tipoRequerimento;
	private ImovelRural imovelRural;
	private Boolean visualizandoImovel;
	private Boolean imprimindoImovel;
	//INDICA SE O USUARIO LOGADO É PROCPF OU PROCPJ OU REPRESENT. LEGAL DA SOLICITACAO.
	private Boolean isProcPfPjOuRepLegal;
	private boolean enderecoSalvo;
	private boolean localizacaoSalva;
	private Boolean disableUploadDoc;
	private String labelUpload;
	private DocumentoImovelRural arquivoEmEdicao;
	private DocumentoImovelRural procuracaoEmEdicao;
	private TipoVinculoImovel tipoVinculoImovel;
	private boolean executarValidacaoRegistroIncompleto =false;
	private Comunicacao comunicacao;
	private TipoVinculoPCT tipoVinculoPCT;
	private Pessoa usuarioCadastrante;
	
	private List<ObjetivoRequerimentoLimpezaArea> listaObjetReqLimpArea;
	private String quaisSecGeometricas;
	private ObjetivoRequerimentoLimpezaArea objReqLimpArea;

	private Boolean cadastroInCompleto;

	private Tipologia tipologia;
	private TipologiaGrupo tipologiaGrupo;
	private Integer tipoSolicitante;
	private PerguntaRequerimento perguntaReq;
	private Pergunta pergunta;
	private LocalizacaoGeografica localizacaoSalvaNoReq;
	private List<LocalizacaoGeografica> listaPontoPesquisa = null;
	private List<Imovel> imoveisDuplicados = null;
	private List<Pauta> listaPautaAreaComAcessoConcedido;
	private List<Pauta> listaPautaGestorComAcessoConcedido;
	
	private List<Comunicacao> comunicacaoList;
	private Comunicacao comunicacaoTemporaria;
	private Integer qtNovaComunicacao;
	private boolean visualizado;
	private MenuModel model;
	private boolean painelVisivel;
	
	private Caepog caepog;
	
	private Tcca tcca;
	private TccaProjeto projeto;
	
	private CadastroAtividadeNaoSujeitaLic cadastro;
	private Boolean sucessMessage;
	private String updateMessage;
	
	private Boolean usuarioVinculadoBndes;
	
	@EJB
	private UsuarioService usuarioService;
	@EJB
	private PautaService pautaService;
	@EJB
	private ComunicacaoService comunicacaoService;
	private String labelTitutoRequerimento;
	
	private boolean existeImovelCadastroPendente;
	
	private PaginaEnum telaParaRedirecionar;

	
	private boolean isPCT;
	
	private ContratoConvenio contratoConvenio;
	
	/*
	 Solicitação para a tela de identificarPapel 
	 * */
	private SolicitacaoDTO solicitacao;
	
	
	@PostConstruct
	public void init() {
		qtNovaComunicacao = 0;
		comunicacaoList = new ArrayList<Comunicacao>();
		disableUploadDoc = false;
		this.pessoa = new Pessoa();
		this.bloquearLocGeoImovelDepoisDeImovelSalvo = false;
		this.bloquearEnderecoImovelDepoisDeImovelSalvo = false;
		painelVisivel = true;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Empreendimento getEmpreendimento() {
		return this.empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public Usuario getUsuarioLogado() {
		if (usuarioLogado == null) {
			// Obtem o usu�rio logado do servi�o de seguran�a.
			usuarioLogado = SecurityService.getUser();
			try {
				// Carrega as informações do usuário da base de dados.
				usuarioLogado = usuarioService.carregar(usuarioLogado);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return usuarioLogado;
	}
	
	public boolean isUsuarioExterno() {
		
		if(Util.isNullOuVazio(this.getUsuarioLogado())||
		   Util.isNullOuVazio(this.getUsuarioLogado().getIndTipoUsuario())){
			
			return true;
		}
		return !this.getUsuarioLogado().getIndTipoUsuario();
	}
	
	
	public Boolean getIsRegulacaoAmbientalEmpreendimento(){
		if(this.tipoRequerimento.getIdeTipoRequerimento().equals(1))
			return true;
		else
			return false;
	}
	
	public String redirecionarParaRegulacaoAmbietalEmpreend(){
		setTelaParaRedirecionar(null);
		this.tipoRequerimento = new TipoRequerimento(TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getIde());
		this.labelTitutoRequerimento = "Requerimento Único";
		ContextoUtil.getContexto().setPCT(false);
		return "/paginas/identificar-papel/identificar-papel.xhtml";
	}

	public Endereco getRequerimentoEndereco() {
		return requerimentoEndereco;
	}

	public void setRequerimentoEndereco(Endereco requerimentoEndereco) {
		this.requerimentoEndereco = requerimentoEndereco;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Boolean getVisualizarPessoaFisica() {
		return visualizarPessoaFisica;
	}

	public void setVisualizarPessoaFisica(Boolean visualizarPessoaFisica) {
		this.visualizarPessoaFisica = visualizarPessoaFisica;
	}

	public Boolean getVisualizarPessoaJuridica() {
		return visualizarPessoaJuridica;
	}

	public void setVisualizarPessoaJuridica(Boolean visualizarPessoaJuridica) {
		this.visualizarPessoaJuridica = visualizarPessoaJuridica;
	}

	public PessoaRequerimentoDTO getReqPapeisDTO() {
		return reqPapeisDTO;
	}

	public void setReqPapeisDTO(PessoaRequerimentoDTO reqPapeisDTO) {
		this.reqPapeisDTO = reqPapeisDTO;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public TipologiaGrupo getTipologiaGrupo() {
		return tipologiaGrupo;
	}

	public void setTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		this.tipologiaGrupo = tipologiaGrupo;
	}

	public Boolean usuarioLogadoIsAtend() {
		if (getUsuarioLogado().getIdePerfil().getIdePerfil().intValue() == PerfilEnum.ATENDENTE.getId().intValue()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public Boolean getVisualizarEmpreendimento() {
		return visualizarEmpreendimento;
	}

	public void setVisualizarEmpreendimento(Boolean visualizarEmpreendimento) {
		this.visualizarEmpreendimento = visualizarEmpreendimento;
	}

	public boolean isVisualizarDLA() {
		return visualizarDLA;
	}

	public void setVisualizarDLA(boolean visualizarDLA) {
		this.visualizarDLA = visualizarDLA;
	}

	public Boolean getCadastroInCompleto() {
		return cadastroInCompleto;
	}

	public void setCadastroInCompleto(Boolean cadastroInCompleto) {
		this.cadastroInCompleto = cadastroInCompleto;
	}

	/**
	 * @return the bloquearLocGeoImovelDepoisDeImovelSalvo
	 */
	public Boolean getBloquearLocGeoImovelDepoisDeImovelSalvo() {
		return bloquearLocGeoImovelDepoisDeImovelSalvo;
	}

	/**
	 * @param bloquearLocGeoImovelDepoisDeImovelSalvo the bloquearLocGeoImovelDepoisDeImovelSalvo to set
	 */
	public void setBloquearLocGeoImovelDepoisDeImovelSalvo(
			Boolean bloquearLocGeoImovelDepoisDeImovelSalvo) {
		this.bloquearLocGeoImovelDepoisDeImovelSalvo = bloquearLocGeoImovelDepoisDeImovelSalvo;
	}

	/**
	 * @return the bloquearEnderecoImovelDepoisDeImovelSalvo
	 */
	public Boolean getBloquearEnderecoImovelDepoisDeImovelSalvo() {
		return bloquearEnderecoImovelDepoisDeImovelSalvo;
	}

	/**
	 * @param bloquearEnderecoImovelDepoisDeImovelSalvo the bloquearEnderecoImovelDepoisDeImovelSalvo to set
	 */
	public void setBloquearEnderecoImovelDepoisDeImovelSalvo(
			Boolean bloquearEnderecoImovelDepoisDeImovelSalvo) {
		this.bloquearEnderecoImovelDepoisDeImovelSalvo = bloquearEnderecoImovelDepoisDeImovelSalvo;
	}

	public Pessoa getSolicitanteRequerimento() {
		return solicitanteRequerimento;
	}

	public void setSolicitanteRequerimento(Pessoa solicitanteRequerimento) {
		this.solicitanteRequerimento = solicitanteRequerimento;
	}

	public TipoRequerimento getTipoRequerimento() {
		return tipoRequerimento;
	}

	public void setTipoRequerimento(TipoRequerimento tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
	}

	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}
	
	public Boolean getVisualizandoImovel() {
		return visualizandoImovel;
	}

	public void setVisualizandoImovel(Boolean visualizandoImovel) {
		this.visualizandoImovel = visualizandoImovel;
	}
	
	public Boolean getImprimindoImovel() {
		return imprimindoImovel;
	}

	public void setImprimindoImovel(Boolean imprimindoImovel) {
		this.imprimindoImovel = imprimindoImovel;
	}

	public boolean isEnderecoSalvo() {
		return enderecoSalvo;
	}

	public void setEnderecoSalvo(boolean enderecoSalvo) {
		this.enderecoSalvo = enderecoSalvo;
	}

	public boolean isLocalizacaoSalva() {
		return localizacaoSalva;
	}

	public void setLocalizacaoSalva(boolean localizacaoSalva) {
		this.localizacaoSalva = localizacaoSalva;
	}

	public Boolean getDisableUploadDoc() {
		return disableUploadDoc;
	}

	public void setDisableUploadDoc(Boolean disableUploadDoc) {
		this.disableUploadDoc = disableUploadDoc;
	}

	public String getLabelUpload() {
		return labelUpload;
	}

	public void setLabelUpload(String labelUpload) {
		this.labelUpload = labelUpload;
	}

	public DocumentoImovelRural getArquivoEmEdicao() {
		return arquivoEmEdicao;
	}

	public void setArquivoEmEdicao(DocumentoImovelRural arquivoEmEdicao) {
		this.arquivoEmEdicao = arquivoEmEdicao;
	}

	public TipoVinculoImovel getTipoVinculoImovel() {
		return tipoVinculoImovel;
	}

	public void setTipoVinculoImovel(TipoVinculoImovel tipoVinculoImovel) {
		this.tipoVinculoImovel = tipoVinculoImovel;
	}

	public Integer getTipoSolicitante() {
		return tipoSolicitante;
	}

	public void setTipoSolicitante(Integer tipoSolicitante) {
		this.tipoSolicitante = tipoSolicitante;
	}

	public Boolean getIsProcPfPjOuRepLegal() {
		if(Util.isNullOuVazio(isProcPfPjOuRepLegal))
			return false;
		else
			return isProcPfPjOuRepLegal;
	}

	public void setIsProcPfPjOuRepLegal(Boolean isProcPfPjOuRepLegal) {
		this.isProcPfPjOuRepLegal = isProcPfPjOuRepLegal;
	}

	public DocumentoImovelRural getProcuracaoEmEdicao() {
		return procuracaoEmEdicao;
	}

	public void setProcuracaoEmEdicao(DocumentoImovelRural procuracaoEmEdicao) {
		this.procuracaoEmEdicao = procuracaoEmEdicao;
	}

	public Pessoa getRequerenteRequerimento() {
		return getSolicitanteRequerimento();
	}

	public void setRequerenteRequerimento(Pessoa requerenteRequerimento) {
		this.setSolicitanteRequerimento(requerenteRequerimento);
	}
	
	public Pessoa getRequerentePesquisa() {
		return requerentePesquisa;
	}

	public void setRequerentePesquisa(Pessoa requerentePesquisa) {
		this.requerentePesquisa = requerentePesquisa;
	}
	public String getLabelTitutoRequerimento() {
		return labelTitutoRequerimento;
	}

	public void setLabelTitutoRequerimento(String labelTitutoRequerimento) {
		this.labelTitutoRequerimento = labelTitutoRequerimento;
	}

	public Object getObjectToLocGeo() {
		return objectToLocGeo;
	}

	public void setObjectToLocGeo(Object objectToLocGeo) {
		this.objectToLocGeo = objectToLocGeo;
	}

	public List<ObjetivoRequerimentoLimpezaArea> getListaObjetReqLimpArea() {
		return listaObjetReqLimpArea;
	}

	public void setListaObjetReqLimpArea(List<ObjetivoRequerimentoLimpezaArea> objetReqLimpArea) {
		this.listaObjetReqLimpArea = objetReqLimpArea;
	}

	public String getQuaisSecGeometricas() {
		return quaisSecGeometricas;
	}

	public void setQuaisSecGeometricas(String quaisSecGeometricas) {
		this.quaisSecGeometricas = quaisSecGeometricas;
	}

	public ObjetivoRequerimentoLimpezaArea getObjReqLimpArea() {
		return objReqLimpArea;
	}

	public void setObjReqLimpArea(ObjetivoRequerimentoLimpezaArea objLimpArea) {
		this.objReqLimpArea = objLimpArea;
	}

	public PerguntaRequerimento getPerguntaReq() {
		return perguntaReq;
	}

	public void setPerguntaReq(PerguntaRequerimento perguntaReq) {
		this.perguntaReq = perguntaReq;
	}

	public LocalizacaoGeografica getLocalizacaoSalvaNoReq() {
		return localizacaoSalvaNoReq;
	}

	public void setLocalizacaoSalvaNoReq(LocalizacaoGeografica localizacaoSalvaNoReq) {
		this.localizacaoSalvaNoReq = localizacaoSalvaNoReq;
	}

	public Pergunta getPergunta() {
		return pergunta;
	}

	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}

	public Requerimento getRequerimentoDasLocGeoDoReqUnico() {
		return requerimentoDasLocGeoDoReqUnico;
	}

	public void setRequerimentoDasLocGeoDoReqUnico(Requerimento requerimentoDasLocGeoDoReqUnico) {
		this.requerimentoDasLocGeoDoReqUnico = requerimentoDasLocGeoDoReqUnico;
	}
	
	public List<LocalizacaoGeografica> getListaPontoPesquisa() {
		return listaPontoPesquisa;
	}

	public void setListaPontoPesquisa(List<LocalizacaoGeografica> listaPontoPesquisa) {
		this.listaPontoPesquisa = listaPontoPesquisa;
	}

	public boolean getExisteImovelCadastroPendente() {
		return existeImovelCadastroPendente;
	}

	public void setExisteImovelCadastroPendente(boolean existeImovelCadastroPendente) {
		this.existeImovelCadastroPendente = existeImovelCadastroPendente;
	}

	public List<Imovel> getImoveisDuplicados() {
		return imoveisDuplicados;
	}

	public void setImoveisDuplicados(List<Imovel> imoveisDuplicados) {
		this.imoveisDuplicados = imoveisDuplicados;
	}

	public List<Pauta> getListaPautaGestorComAcessoConcedido() {
		Usuario usuario = getUsuarioLogado();
		if(!Util.isNull(usuario)) {
			if(Util.isNullOuVazio(listaPautaGestorComAcessoConcedido)) {
				try {
					listaPautaGestorComAcessoConcedido = pautaService.listarPautasGestorComAcessoPermitido(usuario.getPessoaFisica());
				}
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
			}
		}
		return listaPautaGestorComAcessoConcedido;
	}

	public List<Pauta> getListaPautaAreaComAcessoConcedido() {
		Usuario usuario = getUsuarioLogado();
		if(!Util.isNull(usuario)) {
			if(Util.isNullOuVazio(listaPautaAreaComAcessoConcedido)) {
				try {
					listaPautaAreaComAcessoConcedido = pautaService.listarPautasAreaComAcessoPermitido(usuario.getPessoaFisica());
				}
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
			}
		}
		return listaPautaAreaComAcessoConcedido;
	}
	
	public void verificarComunicacoes() {
		qtNovaComunicacao = 0;
		if (!Util.isNullOuVazio(usuarioLogado) && Util.isNullOuVazio(comunicacaoList)) {
			comunicacaoList = new ArrayList<Comunicacao>();
			comunicacaoTemporaria = comunicacaoService.findTemporaria(usuarioLogado.getIdePerfil());
			comunicacaoList.addAll(comunicacaoService.findByFilterLast(usuarioLogado.getIdePerfil(),
					ComunicacaoStatusEnum.ENVIADO.getIdComunicacaoStatus(),
					true,
					"N"));
		}
	}
	
	public String getUrlIcoComunicacao() {

		for(Comunicacao cm:comunicacaoList) {
			if(DataUtil.getDataAtual().getTime() == cm.getDtcPeriodoInicio().getTime()) {
				qtNovaComunicacao++;
				
			}
		}
		if(qtNovaComunicacao>0) {
			return "/resources/img/ico-nova-comunicacao.png";
		}
		return "/resources/img/ico-comunicacao.png";
	}
	
	public boolean isRenderedComunicacaoTemporaria() {
		boolean rendered = false;
		if(!Util.isNullOuVazio(comunicacaoTemporaria)) {
			rendered=true;
		}
		return rendered;
	}
	
	
	public void mensagemExibida() {
		painelVisivel = false;
	}
	
	public boolean isTecnico(){
		return getUsuarioLogado().getIdePerfil().getIdePerfil().intValue() == PerfilEnum.TECNICO.getId().intValue(); 
	}

	public Caepog getCaepog() {
		return caepog;
	}

	public void setCaepog(Caepog caepog) {
		this.caepog = caepog;
	}

	public Tcca getTcca() {
		return tcca;
	}

	public void setTcca(Tcca tcca) {
		this.tcca = tcca;
	}

	public TccaProjeto getProjeto() {
		return projeto;
	}

	public void setProjeto(TccaProjeto projeto) {
		this.projeto = projeto;
	}
	
	public CadastroAtividadeNaoSujeitaLic getCadastro() {
		return cadastro;
	}

	public void setCadastro(CadastroAtividadeNaoSujeitaLic cadastro) {
		this.cadastro = cadastro;
	}

	public PaginaEnum getTelaParaRedirecionar() {
		return telaParaRedirecionar;
	}

	public void setTelaParaRedirecionar(PaginaEnum telaParaRedirecionar) {
		this.telaParaRedirecionar = telaParaRedirecionar;
	}

	public Boolean getSucessMessage() {
		return sucessMessage;
	}

	public void setSucessMessage(Boolean sucessMessage) {
		this.sucessMessage = sucessMessage;
	}

	public String getUpdateMessage() {
		return updateMessage;
	}

	public void setUpdateMessage(String updateMessage) {
		this.updateMessage = updateMessage;
	}

	public Boolean getUsuarioVinculadoBndes() {
		return usuarioVinculadoBndes;
	}

	public void setUsuarioVinculadoBndes(Boolean usuarioVinculadoBndes) {
		this.usuarioVinculadoBndes = usuarioVinculadoBndes;
	}

	public SolicitacaoDTO getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(SolicitacaoDTO solicitacao) {
		this.solicitacao = solicitacao;
	}

	public List<Comunicacao> getComunicacaoList() {
		return comunicacaoList;
	}

	public void setComunicacaoList(List<Comunicacao> comunicacaoList) {
		this.comunicacaoList = comunicacaoList;
	}

	public MenuModel getModel() {
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

	public Comunicacao getComunicacaoTemporaria() {
		return comunicacaoTemporaria;
	}

	public void setComunicacaoTemporaria(Comunicacao comunicacaoTemporaria) {
		this.comunicacaoTemporaria = comunicacaoTemporaria;
	}

	public boolean isPainelVisivel() {
		return painelVisivel;
	}

	public void setPainelVisivel(boolean painelVisivel) {
		this.painelVisivel = painelVisivel;
	}

	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}

	public Integer getQtNovaComunicacao() {
		return qtNovaComunicacao;
	}

	public void setQtNovaComunicacao(Integer qtNovaComunicacao) {
		this.qtNovaComunicacao = qtNovaComunicacao;
	}

	public boolean isVisualizado() {
		return visualizado;
	}

	public void setVisualizado(boolean visualizado) {
		this.visualizado = visualizado;
	}

	public boolean isExecutarValidacaoRegistroIncompleto() {
		return executarValidacaoRegistroIncompleto;
	}

	public void setExecutarValidacaoRegistroIncompleto(boolean executarValidacaoRegistroIncompleto) {
		this.executarValidacaoRegistroIncompleto = executarValidacaoRegistroIncompleto;
	}
	
	public TipoVinculoPCT getTipoVinculoPCT() {
		return tipoVinculoPCT;
	}

	public void setTipoVinculoPCT(TipoVinculoPCT tipoVinculoPCT) {
		this.tipoVinculoPCT = tipoVinculoPCT;
	}

	public Pessoa getUsuarioCadastrante() {
		return usuarioCadastrante;
	}

	public void setUsuarioCadastrante(Pessoa usuarioCadastrante) {
		this.usuarioCadastrante = usuarioCadastrante;
	}

	public boolean isPCT() {
		return isPCT;
	}

	public void setPCT(boolean isPCT) {
		this.isPCT = isPCT;
	}

	public ContratoConvenio getContratoConvenio() {
		return contratoConvenio;
	}

	public void setContratoConvenio(ContratoConvenio contratoConvenio) {
		this.contratoConvenio = contratoConvenio;
	}
	
}