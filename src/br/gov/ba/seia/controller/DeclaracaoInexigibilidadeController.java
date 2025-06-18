package br.gov.ba.seia.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.entity.AtividadeInexigivel;
import br.gov.ba.seia.entity.AtividadeInexigivelModeloCertificadoInexigibilidade;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoAbastecimento;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoBueiro;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoGeral;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoPonte;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoProjeto;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoUnidade;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RecomendacaoAtividadeInexigivel;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoAtividadeInexigivel;
import br.gov.ba.seia.entity.TipoLocalAtividadeInexigivel;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.TipoRioIntervencao;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.OrgaoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeInexigivelEnum;
import br.gov.ba.seia.enumerator.TipoLocalAtividadeInexigivelEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoRioIntervencaoEnum;
import br.gov.ba.seia.facade.DeclaracaoInexigibilidadeServiceFacade;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AtividadeInexigivelService;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.BairroService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeInfoAbastecimentoService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeInfoBueiroService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeInfoGeralService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeInfoPonteService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeInfoProjetoService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeInfoUnidadeService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.LogradouroService;
import br.gov.ba.seia.service.ModeloCertificadoInexigibilidadeService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.ProcuradorPessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorRepresentanteService;
import br.gov.ba.seia.service.RecomendacaoAtividadeInexigivelService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.RequerimentoPessoaService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PessoaUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Uri;
import br.gov.ba.seia.util.Util;

@ViewScoped
@Named("declaracaoInexigibilidadeController")
public class DeclaracaoInexigibilidadeController {

	private static final int ABA_ATIVIDADE = 0;
	private static final int ABA_INFO_ADICIONAL = 1;
	private static final int ABA_RECOMENDACAO = 2;
	
	private int activeIndex = 0;
	private int localRealizacao = 0;
	
	private Integer numeroBueiro;
	
	private boolean ponteQuantidadeQualidadeAgua;
	
	private boolean permiteSalvar;
	
	private boolean inexigivelOutorga;
	private boolean inexigivelLicenca;
	private boolean permiteSelecionar = true;
	private boolean luzParaTodos = false;
	private boolean supressaoVegetacao = false;
	private boolean sistemaSimplificadoAbastecimentoAgua = false;
	
	private Boolean desabilitaTab01;
	private Boolean desabilitaTab02;
	private Boolean desabilitaTab03;
	private boolean permiteEditar = true;
	private boolean permiteEditarPonte = true;
	
	private String descricaoAtividade;
	
	private String descricaoTrajetoVia;
	
	private List<AtividadeInexigivel> listaAtividade;
	private List<AtividadeInexigivel> listaAtividadeSelecionada;
	
//	private List<DeclaracaoInexigibilidadeInfoGeral> listaEnderecoRealizacaoAtividade;
	private List<Endereco> listaEnderecoRealizacaoAtividade;
	private List<DeclaracaoInexigibilidadeInfoUnidade> listaUnidade;
	private List<DeclaracaoInexigibilidadeInfoProjeto> listaProjeto;
	private List<DeclaracaoInexigibilidadeInfoAbastecimento> listaSistema;
	private List<RecomendacaoAtividadeInexigivel> listaRecomendacao;
	private List<DeclaracaoInexigibilidadeInfoPonte> listaPonte;
	private List<SelectItem> listaTipoLocalAtividade;
	private List<SelectItem> listaTipoRioIntervencao;
	
	private AtividadeInexigivel atividadeSelecionada;
	private AtividadeInexigivel atividadeConsulta;
	
	private Requerimento requerimento;
	
	private Requerimento requerimentoConsulta;
	
	private DeclaracaoInexigibilidade declaracaoInexigibilidade;
	
	private DeclaracaoInexigibilidadeInfoGeral declaracaoInexigibilidadeInfoGeral;
	
	private DeclaracaoInexigibilidadeInfoUnidade declaracaoInfoUnidade;
	
	private DeclaracaoInexigibilidadeInfoProjeto declaracaoInfoProjeto;
	
	private DeclaracaoInexigibilidadeInfoAbastecimento declaracaoInfoAbastecimento;
	
	private DeclaracaoInexigibilidadeInfoPonte declaracaoInexigibilidadeInfoPonte;
	
	private DeclaracaoInexigibilidadeInfoBueiro declaracaoInexigibilidadeInfoBueiro;
	
	private Pessoa solicitante;
	
	private RequerimentoDTO requerimentoDTO;
	
	private TipoRioIntervencao tipoRioIntervencao;
	
	private RequerimentoPessoa reqPessoaRequerente;
	private RequerimentoPessoa reqPessoaSolicitante;
	
	@EJB
	private DeclaracaoInexigibilidadeServiceFacade declaracaoInexigibilidadeServiceFacade;
	
	@EJB
	private AtividadeInexigivelService atividadeInexigivelService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private DeclaracaoInexigibilidadeService declaracaoInexigibilidadeService; 
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private RequerimentoPessoaService requerimentoPessoaService; 
	
	@EJB
	private BairroService bairroService;
	
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private LogradouroService logradouroService;
	
	@EJB
	private DeclaracaoInexigibilidadeInfoGeralService declaracaoInexigibilidadeInfoGeralService;
	
	@EJB
	private DeclaracaoInexigibilidadeInfoUnidadeService declaracaoInexigibilidadeInfoUnidadeService;
	
	@EJB
	private DeclaracaoInexigibilidadeInfoProjetoService declaracaoInexigibilidadeInfoProjetoService;
	
	@EJB
	private DeclaracaoInexigibilidadeInfoAbastecimentoService declaracaoInexigibilidadeInfoAbastecimentoService;
	
	@EJB
	private DeclaracaoInexigibilidadeInfoPonteService declaracaoInexigibilidadeInfoPonteService;
	
	@EJB
	private DeclaracaoInexigibilidadeInfoBueiroService declaracaoInexigibilidadeInfoBueiroService;
	
	@EJB
	private RecomendacaoAtividadeInexigivelService recomendacaoAtividadeInexigivelService; 
	
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	
	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private EnquadramentoService enquadramentoService;
	
	@EJB
	private PessoaService pessoaService;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private ProcuradorRepresentanteService procuradorRepresentanteService;
	
	@EJB
	private ModeloCertificadoInexigibilidadeService modeloCertificadoInexigibilidadeService;
	
	@Inject
	private EnderecoRealizacaoAtividadeController enderecoRealizacaoAtividadeController;
	
	@Inject
	private UnidadeEnderecoController unidadeEnderecoController;
	
	@Inject
	private ProjetoEnderecoController projetoEnderecoController;
	
	@Inject
	private SistemaEnderecoController sistemaEnderecoController;
	
	@Inject
	private BoletoAutomatizadoController boletoAutomatizadoController;
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	@Inject
	private CertificadoUtil certificadoUtil;
	
	private boolean camposEnderecoValidos = false;
	
	private boolean camposEnderecoUnidadeValidos = false;
	
	private boolean camposEnderecoProjetoValidos = false;
	
	private boolean  camposEnderecoPonteValidos = false;
	
	private boolean  camposEnderecoSistemaValidos = false;
	
	
	@PostConstruct
	public void init() {
		this.desabilitaTab01 = Boolean.FALSE;
		this.desabilitaTab02 = Boolean.TRUE;
		this.desabilitaTab03 = Boolean.TRUE;
		
		final Object temp = ContextoUtil.getContexto().getObject();
		// Caso o usuário clique em VISUALIZAR ou EDITAR na tela de CONSULTA.
		if (temp != null && temp instanceof RequerimentoDTO) {
			requerimentoDTO = ((RequerimentoDTO) temp);
			
			this.requerimento = requerimentoDTO.getRequerimento();
			
			if (requerimentoDTO.isVisualizar()) {
				permiteEditar = false;
			} else {
				permiteEditar = true;
			}
		}
		Pessoa solicitante = ContextoUtil.getContexto().getSolicitanteRequerimento();
		reqPessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente();
		reqPessoaSolicitante = ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante();
		try {
			if(!Util.isNull(this.requerimento)) {
				this.declaracaoInexigibilidade = declaracaoInexigibilidadeService.obterDeclaracaoPorRequerimento(this.requerimento);
				
				this.requerimento = requerimentoService.carregar(this.requerimento);
				
				if(!Util.isNull(this.declaracaoInexigibilidade)) {
					carregarAtividadeInexigivel();
					
					filtrar();
					
					if(!Util.isNullOuVazio(this.listaAtividadeSelecionada)) {
						this.desabilitaTab01 = Boolean.FALSE;
						this.desabilitaTab02 = Boolean.FALSE;
						this.desabilitaTab03 = Boolean.FALSE;
					}
					
					if(!Util.isNullOuVazio(getDeclaracaoInexigibilidade().getIdeDeclaracaoInexigibilidade())) {
						this.declaracaoInexigibilidadeInfoGeral = declaracaoInexigibilidadeInfoGeralService.obterDeclaracaoInfoGeralPor(getDeclaracaoInexigibilidade());
						
						if(Util.isNull(this.declaracaoInexigibilidadeInfoGeral)) {
							this.declaracaoInexigibilidadeInfoGeral = new DeclaracaoInexigibilidadeInfoGeral();
							this.declaracaoInexigibilidadeInfoGeral.setIndAsvAtosAutorizativos(Boolean.FALSE);
							this.declaracaoInexigibilidadeInfoGeral.setIndLuzParaTodos(Boolean.FALSE);
							this.declaracaoInexigibilidadeInfoGeral.setIndSistemaSimplificadoAbastecimento(Boolean.FALSE);
							this.declaracaoInexigibilidadeInfoGeral.setTipoLocalAtividadeInexigivel(new TipoLocalAtividadeInexigivel(TipoLocalAtividadeInexigivelEnum.LOCAL_ESPECIFICO.getId()));
							
							tipoRioIntervencao = declaracaoInexigibilidadeService.obterTipoRioIntervencaoPor(TipoRioIntervencaoEnum.ESTADUAL.getId());
							
							this.declaracaoInexigibilidadeInfoGeral.setDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
							this.declaracaoInexigibilidadeInfoGeral.setIndLuzParaTodos(getDeclaracaoInexigibilidade().getAtividadeInexigivel().getPermiteLuzParaTodos());
							this.declaracaoInexigibilidadeInfoGeral.setIndSistemaSimplificadoAbastecimento(getDeclaracaoInexigibilidade().getAtividadeInexigivel().getPermiteAbastecimento());
						}else{
							if(Util.isNull(this.declaracaoInexigibilidadeInfoGeral.getTipoRioIntervencao())) {
								tipoRioIntervencao = declaracaoInexigibilidadeService.obterTipoRioIntervencaoPor(TipoRioIntervencaoEnum.ESTADUAL.getId());
							}else{
								tipoRioIntervencao = this.declaracaoInexigibilidadeInfoGeral.getTipoRioIntervencao();
							}
						}
					}
					
					this.declaracaoInexigibilidadeInfoBueiro = this.declaracaoInexigibilidadeInfoBueiroService.obterDeclaracaoInfoBueiroPor(declaracaoInexigibilidade);
					if(declaracaoInexigibilidadeInfoBueiro != null){
						numeroBueiro = declaracaoInexigibilidadeInfoBueiro.getNumBueiro();
						descricaoTrajetoVia = declaracaoInexigibilidadeInfoBueiro.getDesTrajetoVia();
					}
				}
			}else{
				
				if(!Util.isNull(solicitante)) {
					this.declaracaoInexigibilidade = requerimentoService.buscarRequerimentoDeclaracaoInexigibilidadePorSolicitante(reqPessoaRequerente.getPessoa());
					
					this.solicitante = solicitante;
					
					if(!Util.isNull(this.declaracaoInexigibilidade)) {
						this.requerimento = this.declaracaoInexigibilidade.getRequerimento();
						
						carregarAtividadeInexigivel();
						
						filtrar();
						
						if(!Util.isNullOuVazio(this.listaAtividadeSelecionada)) {
							this.desabilitaTab01 = Boolean.FALSE;
							this.desabilitaTab02 = Boolean.FALSE;
							this.desabilitaTab03 = Boolean.FALSE;
						}else{
							this.desabilitaTab01 = Boolean.FALSE;
							this.desabilitaTab02 = Boolean.FALSE;
							this.desabilitaTab03 = Boolean.FALSE;
						}
					}else{
						salvarNovoRequerimento(reqPessoaRequerente.getPessoa());
						
						iniciarNovaDeclaracaoInexigibilidade(this.requerimento);
						
						filtrar();
						
						this.desabilitaTab01 = Boolean.FALSE;
						this.desabilitaTab02 = Boolean.TRUE;
						this.desabilitaTab03 = Boolean.TRUE;
					}
					
					if(!Util.isNull(this.declaracaoInexigibilidade) && Util.isNull(this.declaracaoInexigibilidade.getIndCienteRecomendacao())) {
						declaracaoInexigibilidade.setIndCienteRecomendacao(Boolean.FALSE);
					}
					
					if(!Util.isNullOuVazio(getDeclaracaoInexigibilidade().getIdeDeclaracaoInexigibilidade())) {
						this.declaracaoInexigibilidadeInfoGeral = declaracaoInexigibilidadeInfoGeralService.obterDeclaracaoInfoGeralPor(getDeclaracaoInexigibilidade());
						
						if(Util.isNull(this.declaracaoInexigibilidadeInfoGeral)) {
							this.declaracaoInexigibilidadeInfoGeral = new DeclaracaoInexigibilidadeInfoGeral();
							this.declaracaoInexigibilidadeInfoGeral.setIndAsvAtosAutorizativos(Boolean.FALSE);
							this.declaracaoInexigibilidadeInfoGeral.setIndLuzParaTodos(Boolean.FALSE);
							this.declaracaoInexigibilidadeInfoGeral.setIndSistemaSimplificadoAbastecimento(Boolean.FALSE);
							
							if(this.atividadeSelecionada != null && (this.atividadeSelecionada.getPermiteEndereco() || this.atividadeSelecionada.getPermiteLocalRealizacao())) {
								this.declaracaoInexigibilidadeInfoGeral.setTipoLocalAtividadeInexigivel(new TipoLocalAtividadeInexigivel(TipoLocalAtividadeInexigivelEnum.LOCAL_ESPECIFICO.getId()));
							}
							
							if(this.atividadeSelecionada != null && this.atividadeSelecionada.isPermiteInformarRioIntervencao()) {
								tipoRioIntervencao = declaracaoInexigibilidadeService.obterTipoRioIntervencaoPor(TipoRioIntervencaoEnum.ESTADUAL.getId());
							}
							
							this.declaracaoInexigibilidadeInfoGeral.setDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
						}else{
							if(this.atividadeSelecionada != null && this.atividadeSelecionada.isPermiteInformarRioIntervencao()) {
								if(Util.isNull(this.declaracaoInexigibilidadeInfoGeral.getTipoRioIntervencao())) {
									tipoRioIntervencao = declaracaoInexigibilidadeService.obterTipoRioIntervencaoPor(TipoRioIntervencaoEnum.ESTADUAL.getId());
								}else{
									tipoRioIntervencao = this.declaracaoInexigibilidadeInfoGeral.getTipoRioIntervencao();
								}
							}
						}
					}
				}
			}
			requerimentoConsulta = new Requerimento();
			ContextoUtil.getContexto().setObject(null);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void carregarAtividadeInexigivel() {
		this.listaAtividadeSelecionada = new ArrayList<AtividadeInexigivel>();
		
		if(this.declaracaoInexigibilidade != null && this.declaracaoInexigibilidade.getAtividadeInexigivel() != null){
			AtividadeInexigivel atividade = atividadeInexigivelService.buscarAtividadeInexigivelPorIde(this.declaracaoInexigibilidade.getAtividadeInexigivel().getIdeAtividadeInexigivel());
			
			this.atividadeSelecionada = atividade;
		}
		
	}
	
	private void salvarNovoRequerimento(Pessoa solicitante) throws Exception {
		this.requerimento = new Requerimento();
		this.requerimento.setDtcCriacao(new Date());
		this.requerimento.setIdeTipoRequerimento(new TipoRequerimento(TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO));
		//INEMA
		this.requerimento.setIdeOrgao(new Orgao(OrgaoEnum.INEMA.getId()));
		this.requerimento.setIndExcluido(false);
		
		//dados do contato para o requerimento
		List<Telefone> listaTel = null;
		
		try {
			listaTel = telefoneService.filtraTelefonePorPessoa(solicitante);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		if (!Util.isNullOuVazio(solicitante.getPessoaFisica()) && !Util.isNullOuVazio(solicitante.getPessoaFisica().getNomPessoa()) && Util.isNullOuVazio(requerimento.getNomContato())) {
			this.requerimento.setNomContato(solicitante.getPessoaFisica().getNomPessoa());

			if (!Util.isNullOuVazio(solicitante.getPessoaFisica().getPessoa().getDesEmail()) && Util.isNullOuVazio(this.requerimento.getDesEmail())) {
				this.requerimento.setDesEmail(solicitante.getPessoaFisica().getPessoa().getDesEmail());
			}
		} else if (!Util.isNullOuVazio(solicitante.getPessoaJuridica()) && Util.isNullOuVazio(this.requerimento.getNomContato())) {

			this.requerimento.setNomContato(solicitante.getPessoaJuridica().getNomRazaoSocial());

			Integer tipoSolicit = ContextoUtil.getContexto().getTipoSolicitante();
			ContextoUtil.getContexto().setTipoSolicitante(null);

			if (Util.isNullOuVazio(tipoSolicit) || tipoSolicit.equals(1)) {
				PessoaJuridica pJuridica = ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaJuridica();

				if(!Util.isNullOuVazio(pJuridica) && !Util.isNullOuVazio(pJuridica.getPessoa()) && !Util.isNullOuVazio(pJuridica.getPessoa().getDesEmail())) {
					this.requerimento.setDesEmail(pJuridica.getPessoa().getDesEmail());
				}
			} else if (tipoSolicit.equals(4) || tipoSolicit.equals(2)) {

				if (!Util.isNullOuVazio(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica())) {
					this.requerimento.setDesEmail(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica().getPessoa().getDesEmail());
				} else {
					this.requerimento.setDesEmail(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa().getDesEmail());
				}
			}
		}
		
		if (!listaTel.isEmpty() && Util.isNullOuVazio(this.requerimento.getNumTelefone())) {
			try {
				if (listaTel.size() > 1) {
					this.requerimento.setNumTelefone(PessoaUtil.getTelefoneParaRequerimento(listaTel).getNumTelefone());
				} else {
					this.requerimento.setNumTelefone(listaTel.get(0).getNumTelefone());
				}
			} catch (Exception e) {
				this.requerimento.setNumTelefone(listaTel.get(0).getNumTelefone());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		
		pessoasRequerimento();
		
		adicionarTramitacao(StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO, solicitante);
		
	}
	
	private void adicionarTramitacao(StatusRequerimentoEnum status, Pessoa requerente) {
		TramitacaoRequerimento tramitacaoRequerimento = new TramitacaoRequerimento();
		tramitacaoRequerimento.setIdePessoa(requerente);
		StatusRequerimento statusRequeri = new StatusRequerimento();
		statusRequeri.setIdeStatusRequerimento(status.getStatus());
		tramitacaoRequerimento.setIdeStatusRequerimento(statusRequeri);
		requerimento.setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		requerimento.getTramitacaoRequerimentoCollection().add(tramitacaoRequerimento);

	}
	
	private void pessoasRequerimento() throws Exception {
		try {

			List<RequerimentoPessoa> pessoasRequerimentos = new ArrayList<RequerimentoPessoa>();
			RequerimentoPessoa reqPessoaAtendente = ContextoUtil.getContexto().getReqPapeisDTO().getAtendente();
			RequerimentoPessoa reqPessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente();
			RequerimentoPessoa reqPessoaSolicitante = ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante();

			pessoasRequerimentos.add(reqPessoaRequerente);

			if (!reqPessoaRequerente.isIndSolicitante()) {
				pessoasRequerimentos.add(reqPessoaSolicitante);
			}

			if (!reqPessoaRequerente.isIndUsuarioLogado() && !reqPessoaSolicitante.isIndUsuarioLogado()) {
				pessoasRequerimentos.add(reqPessoaAtendente);
			}

			
			if(!Util.isNull(reqPessoaRequerente.getPessoa().getPessoaFisica()) && !Util.isNull(reqPessoaRequerente.getPessoa().getPessoaFisica().getIdePessoaFisica())) {
				
				ProcuradorPessoaFisica donoProcurador = new ProcuradorPessoaFisica();
				donoProcurador.setIdePessoaFisica(reqPessoaRequerente.getPessoa().getPessoaFisica());
			
				List<ProcuradorPessoaFisica> listaProcuradorPessoaFisica = (List<ProcuradorPessoaFisica>) procuradorPessoaFisicaService.listarProcuradorPessoaFisica(donoProcurador);
				for (ProcuradorPessoaFisica procuradorPessoaFisica : listaProcuradorPessoaFisica) {
					RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
					requerimentoPessoa.setPessoa(procuradorPessoaFisica.getIdeProcurador().getPessoa());
					TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
					tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
					requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);
					pessoasRequerimentos.add(requerimentoPessoa);
				}
			}
			
			ProcuradorRepresentante procuradoRepresentante = new ProcuradorRepresentante();
			procuradoRepresentante.setIdePessoaJuridica(new PessoaJuridica());
			procuradoRepresentante.getIdePessoaJuridica().setIdePessoaJuridica(reqPessoaRequerente.getPessoa().getIdePessoa());
			
			List<ProcuradorRepresentante> listaProcuradorRepresentante = (List<ProcuradorRepresentante>) procuradorRepresentanteService.getListaProcuradorRepresentante(procuradoRepresentante);
			for (ProcuradorRepresentante procuradorRepresentante : listaProcuradorRepresentante) {
				RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
				requerimentoPessoa.setPessoa(procuradorRepresentante.getIdeProcurador().getPessoa());
				TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
				tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
				requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);

				boolean temPessoaRequerimento = false;

				for (RequerimentoPessoa reqPes : pessoasRequerimentos) {
					if (reqPes.getIdeTipoPessoaRequerimento().equals(requerimentoPessoa.getIdeTipoPessoaRequerimento())
							&& reqPes.getPessoa().equals(requerimentoPessoa.getPessoa())) {
						temPessoaRequerimento = true;
						break;
					}
				}

				if (!temPessoaRequerimento) {
					pessoasRequerimentos.add(requerimentoPessoa);
				}
			}

			PessoaJuridica pessoaJuridica = new PessoaJuridica(reqPessoaRequerente.getPessoa().getIdePessoa());
			
			List<RepresentanteLegal> collRepresentanteLegal = representanteLegalService.getListaRepresentanteLegalByPessoa(pessoaJuridica);
			
			for (RepresentanteLegal representantelegal : collRepresentanteLegal) {

				RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
				requerimentoPessoa.setPessoa(representantelegal.getIdePessoaFisica().getPessoa());
				TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
				tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId());
				requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);

				boolean temPessoaRequerimento = false;

				for (RequerimentoPessoa reqPes : pessoasRequerimentos) {
					if (reqPes.getIdeTipoPessoaRequerimento().equals(requerimentoPessoa.getIdeTipoPessoaRequerimento())
							&& reqPes.getPessoa().equals(requerimentoPessoa.getPessoa())) {

						temPessoaRequerimento = true;
						break;
					}
				}

				if (!temPessoaRequerimento) {
					pessoasRequerimentos.add(requerimentoPessoa);
				}
			}

			for (RequerimentoPessoa reqPess : pessoasRequerimentos) {
				reqPess.setRequerimento(requerimento);
			}

			requerimento.setRequerimentoPessoaCollection(pessoasRequerimentos);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao associar a pessoa ao requerimento.");
			throw e;
		}
	}
	
	private void iniciarNovaDeclaracaoInexigibilidade(Requerimento requerimento)  {
		this.declaracaoInexigibilidade = new DeclaracaoInexigibilidade();
		this.declaracaoInexigibilidade.setIndCienteRecomendacao(Boolean.FALSE);
		this.declaracaoInexigibilidade.setRequerimento(requerimento);
	}
	
	public void filtrar() {
		if(Util.isNull(this.declaracaoInexigibilidade)) {
			JsfUtil.addErrorMessage("Ocorreu um erro com a declaração e não foi possível selecionar atividades.");
			return;
		}
		
		AtividadeInexigivel atividadeInexigivel = new AtividadeInexigivel();
		TipoAtividadeInexigivel tipo = null;
		
		if(inexigivelLicenca) {
			tipo = new TipoAtividadeInexigivel(TipoAtividadeInexigivelEnum.LICENCA.getIdeTipoAtividadeInexigivel());
		}else if (inexigivelOutorga){
			tipo = new TipoAtividadeInexigivel(TipoAtividadeInexigivelEnum.OUTORGA.getIdeTipoAtividadeInexigivel());
		}else{
			tipo = null;
		}
		
		atividadeInexigivel.setTipoAtividadeInexigivel(tipo);
		
		if(!Util.isNullOuVazio(descricaoAtividade)) {
			atividadeInexigivel.setNomAtividadeInexigivel(descricaoAtividade.trim());
		}
		
		try {
			this.listaAtividade = atividadeInexigivelService.buscarPorDescricaoTipoAtividade(atividadeInexigivel);
			
			AtividadeInexigivel atividadeOutros = new AtividadeInexigivel();
			atividadeOutros.setIdeAtividadeInexigivel( -1);
			atividadeOutros.setNomAtividadeInexigivel("Outros");
			
			this.listaAtividade.add(atividadeOutros);
			
			this.permiteSelecionar = true;
			
			if(Util.isNullOuVazio(declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade())) {
				this.listaAtividadeSelecionada = null;
			}else{
				if(Util.isNullOuVazio(this.listaAtividadeSelecionada)) {
					this.listaAtividadeSelecionada = new ArrayList<AtividadeInexigivel>();
				}
				
				if(!Util.isNull(this.getDeclaracaoInexigibilidade().getAtividadeInexigivel())) {
					if(Util.isNullOuVazio(this.listaAtividadeSelecionada)) {
						this.listaAtividadeSelecionada.add(this.declaracaoInexigibilidade.getAtividadeInexigivel());
						this.listaAtividade.remove(getDeclaracaoInexigibilidade().getAtividadeInexigivel());
					}
					this.permiteSelecionar = false;
				}
				
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro na consulta.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void selecionarAtividade(AtividadeInexigivel atividadeInexigivel) {
		if(Util.isNull(this.declaracaoInexigibilidade)) {
			JsfUtil.addErrorMessage("Não foi possível selecionar atividade.");
			return;
		}
		
		if(!Util.isNullOuVazio(this.listaAtividadeSelecionada)) { 
			JsfUtil.addErrorMessage(BUNDLE.getString("di-atividade-inexigivel-unico"));
			return;
		}
		
		if(Util.isNull(atividadeInexigivel) || atividadeInexigivel.getIdeAtividadeInexigivel() == -1) {
			JsfUtil.addWarnMessage("Não será possível continuar com a declaração com atividade Outros selecionada. Favor entrar em contato com o atend@inema.ba.gov.br!");
			this.atividadeConsulta = new AtividadeInexigivel();
			
			try {
				carregarAtividadeInexigivel();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage("Ocorreu um erro ao carregar atividade inexigivel.");
			}
			
			filtrar();
			Html.atualizar("tabViewDI:formDeclaracao");
			return;
		}
		
		if(!Util.isNull(atividadeInexigivel) && !Util.isNullOuVazio(atividadeInexigivel.getIdeAtividadeInexigivel())) {
			this.listaAtividadeSelecionada = new ArrayList<AtividadeInexigivel>();
			this.listaAtividadeSelecionada.add(atividadeInexigivel);
			
			this.listaAtividade.remove(atividadeInexigivel);
			
			this.permiteSelecionar = false;
			
			try {
				this.declaracaoInexigibilidade.setAtividadeInexigivel(atividadeInexigivel);
				this.atividadeSelecionada = atividadeInexigivel;
				
				if(!Util.isNullOuVazio(getDeclaracaoInexigibilidade().getIdeDeclaracaoInexigibilidade())) {
					refazerDeclaracao();
					
					this.declaracaoInexigibilidadeService.salvarDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
					
					this.desabilitaTab02 = false;
					this.desabilitaTab03 = false;
				}
				
				Html.atualizar("tabViewDI");
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}
	
	public void removerAtividade() {
		this.listaAtividadeSelecionada = null;
		this.permiteSelecionar = true;
		
		if(!Util.isNull(this.getDeclaracaoInexigibilidade().getAtividadeInexigivel())) {
			this.getDeclaracaoInexigibilidade().setAtividadeInexigivel(null);
		}
		
		filtrar();
		this.atividadeSelecionada = null;
		this.desabilitaTab02 = true;
		this.desabilitaTab03 = true;
		
		JsfUtil.addWarnMessage("Adicione novamente uma atividade inexigível.");
		Html.atualizar("tabViewDI");
	}
	
	public void prepararParaRefazerDeclaracao(){
	
		try {
			this.declaracaoInexigibilidadeInfoGeralService.removerDeclaracaoInfoGeral(getDeclaracaoInexigibilidadeInfoGeral());
			this.declaracaoInexigibilidadeInfoGeral = new DeclaracaoInexigibilidadeInfoGeral();
/*			carregarDeclaracaoInfoGeral();
			this.getDeclaracaoInexigibilidadeInfoGeral().setIdeDeclaracaoInexigibilidadeInfoGeral(null);*/
			
			this.declaracaoInexigibilidadeInfoBueiro = this.declaracaoInexigibilidadeInfoBueiroService.obterDeclaracaoInfoBueiroPor(getDeclaracaoInexigibilidade());
			if(declaracaoInexigibilidadeInfoBueiro != null){
				numeroBueiro = declaracaoInexigibilidadeInfoBueiro.getNumBueiro();
				descricaoTrajetoVia = declaracaoInexigibilidadeInfoBueiro.getDesTrajetoVia();
			}
		
			this.declaracaoInexigibilidadeInfoProjetoService.removerDeclaracaoInfoProjeto(getDeclaracaoInexigibilidade());
		
			this.declaracaoInexigibilidadeInfoUnidadeService.removerDeclaracaoInfoUnidade(getDeclaracaoInexigibilidade());
			
			this.declaracaoInexigibilidadeInfoAbastecimentoService.removerDeclaracaoInfoAbastecimento(getDeclaracaoInexigibilidade());
			
			if(!Util.isNull(declaracaoInexigibilidadeInfoBueiro)) {
				this.declaracaoInexigibilidadeInfoBueiroService.removerDeclaracaoInfoBueiro(getDeclaracaoInexigibilidadeInfoBueiro());
			}
			
			this.declaracaoInexigibilidadeInfoPonteService.removerDeclaracaoInfoPonte(getDeclaracaoInexigibilidade());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void refazerDeclaracao(){
		
		try {
			declaracaoInexigibilidadeServiceFacade.refazerDeclaracao(this);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
	
	public void prepararParaSalvarDeclaracaoInexigibilidade(){
		
		if(permiteSalvar && !validarSalvar()){
			return;
		}
		
		try {
			if(this.requerimento.getIdeRequerimento() == null) {
				declaracaoInexigibilidadeService.gerarRequerimento(getRequerimento());
			}
			
			this.declaracaoInexigibilidadeService.salvarDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
			
			salvarDeclaracaoInexigibilidadeInfoBueiro();
			
			if(this.declaracaoInexigibilidadeInfoGeral != null) {
				if(Util.isNull(this.declaracaoInexigibilidadeInfoGeral.getDeclaracaoInexigibilidade())) {
					this.declaracaoInexigibilidadeInfoGeral.setDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
				}
				
				if(this.atividadeSelecionada != null && this.atividadeSelecionada.isPermiteInformarRioIntervencao()) {
					if(tipoRioIntervencao != null) {
						this.declaracaoInexigibilidadeInfoGeral.setTipoRioIntervencao(tipoRioIntervencao);
					}
				}
				this.declaracaoInexigibilidadeInfoGeralService.salvarOuAtualizar(getDeclaracaoInexigibilidadeInfoGeral());
			}
			
			JsfUtil.addSuccessMessage(BUNDLE.getString("di-geral-salvo-sucesso"));
			
			this.desabilitaTab02 = false;
			this.desabilitaTab03 = false;
			
			Html.atualizar("tabViewDI");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	}
	
	public void salvar() {
		
		try {
			
			declaracaoInexigibilidadeServiceFacade.salvarDeclaracaoInexigibilidade(this);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		Html.atualizar("dialogEnderecoUnidade");
	}

	private boolean validarSalvar() {
		if(Util.isNullOuVazio(this.listaAtividadeSelecionada)) {
			JsfUtil.addErrorMessage("Selecione uma Atividade Inexigível!");
			return false;
		}

		//RIO FEDERAL - não é permitido.
		if(tipoRioIntervencao != null && tipoRioIntervencao.getIdeTipoRioIntervencao() != null && tipoRioIntervencao.getIdeTipoRioIntervencao() == 2) {
			JsfUtil.addErrorMessage(BUNDLE.getString("di-inf-0036"));
			return false;
		}
		
		if(ponteQuantidadeQualidadeAgua) {
			JsfUtil.addErrorMessage(BUNDLE.getString("di-inf-0039"));
			return false;
		}
		
		if(!Util.isNullOuVazio(this.listaAtividadeSelecionada)) {
			for(AtividadeInexigivel ativ : this.listaAtividadeSelecionada) {
				if(Integer.valueOf(-1).equals(ativ.getIdeAtividadeInexigivel())) {
					JsfUtil.addWarnMessage("Não será possível continuar com a declaração com atividade Outros selecionada. Favor entrar em contato com o atendimento@inema.ba.gov.br!");
					return false;
				}
			}
		}
		
		if(this.atividadeSelecionada.getPermiteQtdBueiros() != null && this.atividadeSelecionada.getPermiteQtdBueiros() && activeIndex != ABA_ATIVIDADE) {
			
			if(tipoRioIntervencao == null || tipoRioIntervencao.getIdeTipoRioIntervencao() == null){
				JsfUtil.addErrorMessage("É obrigatório informar o o rio que sofrerá intervenção!");
				return false;
			}
			
			if(numeroBueiro == null) {
				JsfUtil.addErrorMessage("É obrigatório informar o número de bueiros!");
				return false;
			}
			
			if(StringUtils.isBlank(descricaoTrajetoVia)) {
				JsfUtil.addErrorMessage("É obrigatório informar o trajeto da via!");
				return false;
			}
		}
		
		return true;
	}
	
	public void changeLocalRealizacao() {
		if(TipoLocalAtividadeInexigivelEnum.DIVERSOS_MUNICIPIOS_NA_BAHIA.getId().equals(this.declaracaoInexigibilidadeInfoGeral.getTipoLocalAtividadeInexigivel().getIdeTipoLocalAtividadeInexigivel())) {
			this.listaEnderecoRealizacaoAtividade = new ArrayList<Endereco>();
			
			this.declaracaoInexigibilidadeInfoGeral.setEndereco(null);
			this.camposEnderecoValidos = Boolean.TRUE;
			
			try {
				this.declaracaoInexigibilidadeService.salvarEnderecoRealizacaoAtividade(getDeclaracaoInexigibilidadeInfoGeral());
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			
			Html.atualizar("tabViewDI:formInfoAdicional:pnlEndereco");
		}
	}
	
	public void salvarEnderecoRealizacaoAtividade() {
		try {
			declaracaoInexigibilidadeServiceFacade.salvarAtividadeEndereco(this);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(BUNDLE.getString("di-endereco-erro-salvar"));
		}
	}
	
	public void prepararParaSalvarEnderecoRealizacaoAtividade() {
		try {
			
			this.enderecoRealizacaoAtividadeController.carregarDados();
			
			Endereco endereco = this.enderecoRealizacaoAtividadeController.getEndereco();
			Bairro bairro = this.enderecoRealizacaoAtividadeController.getBairro();
			Bairro bairroOutro = this.enderecoRealizacaoAtividadeController.getBairroOutro();
			Logradouro logradouro = this.enderecoRealizacaoAtividadeController.getLogradouro();
			TipoLogradouro tipoLogradouro = this.enderecoRealizacaoAtividadeController.getTipoLogradouro();
			Municipio municipio = this.enderecoRealizacaoAtividadeController.getMunicipio();
			Integer numCep = this.enderecoRealizacaoAtividadeController.getLogradouroPesquisa().getNumCep();
			
			
			if(!enderecoRealizacaoAtividadeController.getIsEstadoBahia()) {
				JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
				return;
			}
			
			if(validarEndereco(endereco, numCep, bairro, logradouro, tipoLogradouro, municipio, null)) {
				if(Util.isNullOuVazio(bairro.getIdeBairro()) || bairro.getIdeBairro() <= 0) {
					persistirBairro(bairroOutro, municipio);
				}
				
				if(Util.isNull(logradouro.getIdeLogradouro()) || logradouro.getIdeLogradouro() <= 0) {
					if(!Util.isNullOuVazio(bairro.getIdeBairro()) && bairro.getIdeBairro() > 0) {
						persistirLogradouro(logradouro, municipio, tipoLogradouro, bairro, numCep);
					}else{
						persistirLogradouro(logradouro, municipio, tipoLogradouro, bairro, numCep);
					}
				}
				
				if(!Util.isNullOuVazio(bairro.getIdeBairro()) && bairro.getIdeBairro() > 0) {
					enderecoFacade.salvarEnderecoCompletoDeclaracaoInexigibilidade(endereco, bairro, logradouro);
				}else{
					bairro.setIdeBairro(null);
					enderecoFacade.salvarEnderecoCompletoDeclaracaoInexigibilidade(endereco, bairro, logradouro);
				}
				
				endereco = this.enderecoFacade.carregar(endereco.getIdeEndereco());
				
				if(endereco != null) {
					this.declaracaoInexigibilidadeInfoGeral.setEndereco(endereco);
					
					if(Util.isNull(this.declaracaoInexigibilidadeInfoGeral.getDeclaracaoInexigibilidade())) {
						this.declaracaoInexigibilidadeInfoGeral.setDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
					}
					
					this.declaracaoInexigibilidadeService.salvarEnderecoRealizacaoAtividade(this.declaracaoInexigibilidadeInfoGeral);
					
					carregarListaEnderecoRealizacaoAtividade();
					
					JsfUtil.addSuccessMessage(BUNDLE.getString("di-geral-salvo-sucesso"));
					camposEnderecoValidos = true;
				}else{
					throw new Exception();
				}
				Html.atualizar("tabViewDI:formInfoAdicional:pnlEndereco");
				Html.esconder("dialogEnderecoRealizacaoAtividade");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(BUNDLE.getString("di-endereco-erro-salvar"));
		}finally{
			Html.atualizar("tabViewDI:formInfoAdicional");
		}
	}
	
	private boolean validarEndereco(Endereco endereco, Integer numCep,Bairro bairroResiduo, Logradouro logradouroResiduo,
			TipoLogradouro tipoLogradouro, Municipio municipioResiduo, Bairro bairroOutro) {
		
		boolean sucesso = true;
		
		if(Util.isNullOuVazio(numCep)) {
			JsfUtil.addErrorMessage("O campo CEP é de preenchimento obrigatório");
			sucesso = false;
		}
			
		if(bairroResiduo == null) {
			JsfUtil.addErrorMessage("O campo Bairro é de preenchimento obrigatório.");
			sucesso = false;
		}else{
			if((bairroResiduo.isOutroBairro() || bairroResiduo.isBairroNaoSelecionado()) && StringUtils.isBlank(bairroResiduo.getNomBairro())) {
				JsfUtil.addErrorMessage("O campo Bairro é de preenchimento obrigatório.");
				sucesso = false;
			}
		}
		
		if(tipoLogradouro == null) {
			JsfUtil.addErrorMessage("O campo Tipo de Logradouro é de preenchimento obrigatório.");
			sucesso = false;
		}
		
		if(logradouroResiduo == null) {
			JsfUtil.addErrorMessage("O campo Logradouro é de preenchimento obrigatório.");
			sucesso = false;
		}else{
			if((logradouroResiduo.isOutroLogradouro() || logradouroResiduo.isLagradouroNaoSelecionado()) && StringUtils.isBlank(logradouroResiduo.getNomLogradouro())) {
				JsfUtil.addErrorMessage("O campo Logradouro é de preenchimento obrigatório.");
				sucesso = false;
			}
		}
		
		if(Util.isNullOuVazio(endereco.getNumEndereco())) {
			JsfUtil.addErrorMessage("O campo Número é de preenchimento obrigatório.");
			sucesso = false;
		}
		
		if(!Util.isNull(municipioResiduo.getIdeEstado()) && Util.isNullOuVazio(municipioResiduo.getIdeEstado().getIdeEstado())) {
			JsfUtil.addErrorMessage("O campo UF é de preenchimento obrigatório.");
			sucesso = false;
		}
		
		if(!Util.isNull(municipioResiduo) && Util.isNullOuVazio(municipioResiduo.getIdeMunicipio())) {
			JsfUtil.addErrorMessage("O campo Localidade é de preenchimento obrigatório.");
			sucesso = false;
		}
		
		return sucesso;
	}
	
	private void persistirLogradouro(Logradouro logradouro, Municipio municipio, TipoLogradouro tipoLogradouro, Bairro bairro, Integer numCep) {
		try {
			logradouro.setIdeLogradouro(null);
			logradouro.setIdeMunicipio(municipio);
			logradouro.setIndOrigemCorreio(false);
			logradouro.setIndOrigemApi(false);
			logradouro.setIdeBairro(bairro);
			logradouro.setIdeTipoLogradouro(tipoLogradouro);
			logradouro.setNumCep(numCep);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void persistirBairro(Bairro bairro, Municipio municipio) {
		try {
			bairro.setIdeBairro(null);
			bairro.setIdeMunicipio(municipio);
			bairro.setIndOrigemCorreio(false);
			bairro.setIndOrigemApi(false);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
	        	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarListaEnderecoRealizacaoAtividade() throws Exception {
		this.listaEnderecoRealizacaoAtividade = new ArrayList<Endereco>();

		if(!Util.isNull(this.declaracaoInexigibilidadeInfoGeral) && !Util.isNull(this.declaracaoInexigibilidadeInfoGeral.getEndereco())) {
			this.listaEnderecoRealizacaoAtividade.add(this.enderecoFacade.carregar(this.declaracaoInexigibilidadeInfoGeral.getEndereco().getIdeEndereco()));
		}
		
		if(!Util.isNullOuVazio(listaEnderecoRealizacaoAtividade)){
			camposEnderecoValidos = true;
		}
	}
	
	public void onTabChange(TabChangeEvent event) {
		if(Util.isNull(getDeclaracaoInexigibilidade().getAtividadeInexigivel())) {
			JsfUtil.addErrorMessage(BUNDLE.getString("di-atividade-inexigivel-vazio"));
			activeIndex = ABA_ATIVIDADE;
			Html.atualizar("tabViewDI");
			return;
		}
		if(!Util.isNull(event.getTab())) {
			if("tab01".equals(event.getTab().getId())) {
				activeIndex = ABA_ATIVIDADE;
				Html.atualizar("tabViewDI");
			}else if("tab02".equals(event.getTab().getId())) {
				carregarAbaInfoAdicional();
			}else {
				carregarAbaInfoAdicional();
				carregarAbaRecomendacoes();
			}
		}
	}

	private void carregarAbaRecomendacoes() {
		activeIndex = ABA_RECOMENDACAO;
		
		try {
			carregarListaRecomendacao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		Html.atualizar("tabViewDI");
		Html.atualizar("tabViewDI:tab03");
		Html.atualizar("tabViewDI:formRecomendacao");
		Html.atualizar("tabViewDI:formRecomendacao:pnlRecomendacao");
		Html.atualizar("tabViewDI:formRecomendacao:dtRecomendacao");
	}

	private void carregarAbaInfoAdicional() {
		try {
			
			carregarListaEnderecoUnidade();
			
			carregarListaEnderecoProjeto();
			
			carregarListaEnderecoSistema();
			
			carregarListaPonte();
			
			carregarListaTipoLocalAtividade();
			
			carregarListaTipoRioIntervencao();
			
			carregarDeclaracaoInfoGeral();
			
			this.declaracaoInexigibilidadeInfoBueiro = this.declaracaoInexigibilidadeInfoBueiroService.obterDeclaracaoInfoBueiroPor(getDeclaracaoInexigibilidade());
			if(declaracaoInexigibilidadeInfoBueiro != null){
				numeroBueiro = declaracaoInexigibilidadeInfoBueiro.getNumBueiro();
				descricaoTrajetoVia = declaracaoInexigibilidadeInfoBueiro.getDesTrajetoVia();
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		activeIndex = ABA_INFO_ADICIONAL;
		Html.atualizar("tabViewDI");
		Html.atualizar("tabViewDI:tab02");
		Html.atualizar("tabViewDI:tab02:formInfoAdicional");
	}

	private void carregarDeclaracaoInfoGeral() throws Exception {
		if(Util.isNull(this.declaracaoInexigibilidadeInfoGeral)) {
			this.declaracaoInexigibilidadeInfoGeral = declaracaoInexigibilidadeInfoGeralService.obterDeclaracaoInfoGeralPor(getDeclaracaoInexigibilidade());
		}
		
		carregarListaEnderecoRealizacaoAtividade();
		
		if(Util.isNull(this.declaracaoInexigibilidadeInfoGeral)) {
			this.declaracaoInexigibilidadeInfoGeral = new DeclaracaoInexigibilidadeInfoGeral();
			this.declaracaoInexigibilidadeInfoGeral.setIndAsvAtosAutorizativos(Boolean.FALSE);
			this.declaracaoInexigibilidadeInfoGeral.setIndLuzParaTodos(Boolean.FALSE);
			this.declaracaoInexigibilidadeInfoGeral.setIndSistemaSimplificadoAbastecimento(Boolean.FALSE);
			this.declaracaoInexigibilidadeInfoGeral.setDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
			
			if(this.atividadeSelecionada.getPermiteEndereco() || this.atividadeSelecionada.getPermiteLocalRealizacao()) {
				this.declaracaoInexigibilidadeInfoGeral.setTipoLocalAtividadeInexigivel(this.declaracaoInexigibilidadeService.obterTipoLocalAtividadePor(TipoLocalAtividadeInexigivelEnum.LOCAL_ESPECIFICO.getId()));
			}
			
			if(this.atividadeSelecionada.isPermiteInformarRioIntervencao() && Util.isNull(this.declaracaoInexigibilidadeInfoGeral.getTipoRioIntervencao())) {
				this.declaracaoInexigibilidadeInfoGeral.setTipoRioIntervencao(this.declaracaoInexigibilidadeService.obterTipoRioIntervencaoPor(TipoRioIntervencaoEnum.ESTADUAL.getId()));
			}
		}
	}
	
	private void carregarListaPonte() throws Exception {
		this.listaPonte = declaracaoInexigibilidadeInfoPonteService.obterListaEnderecoPonte(getDeclaracaoInexigibilidade());
	}

	public void excluirEnderecoRealizacaoAtividade() {
		if(!Util.isNull(this.declaracaoInexigibilidadeInfoGeral) && !Util.isNull(this.declaracaoInexigibilidadeInfoGeral.getIdeDeclaracaoInexigibilidadeInfoGeral())) {
			try {
				this.declaracaoInexigibilidadeInfoGeral.setEndereco(null);
				this.declaracaoInexigibilidadeService.salvarEnderecoRealizacaoAtividade(getDeclaracaoInexigibilidadeInfoGeral());
				carregarListaEnderecoRealizacaoAtividade();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				
				JsfUtil.addErrorMessage("Não foi possível excluir a Endereço de Realização da Atividade.");
			}
			
			Html.atualizar("tabViewDI:formInfoAdicional:dtEnderecoRealizacaoAtividade");
		}
	}
	
	public void excluirEnderecoUnidade() {
		if(!Util.isNull(this.declaracaoInfoUnidade) && !Util.isNull(this.declaracaoInfoUnidade.getIdeDeclaracaoInexigibilidadeInfoUnidade())) {
			try {
				this.declaracaoInexigibilidadeInfoUnidadeService.excluirEnderecoUnidade(this.declaracaoInfoUnidade);
				
				this.declaracaoInfoUnidade = new DeclaracaoInexigibilidadeInfoUnidade();
				
				carregarListaEnderecoUnidade();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				
				JsfUtil.addErrorMessage("Não foi possível excluir a Unidade.");
			}
			
			Html.atualizar("tabViewDI:formInfoAdicional:dtUnidade");
		}
	}
	
	public void excluirEnderecoProjeto() {
		if(!Util.isNull(this.declaracaoInfoProjeto) && !Util.isNull(this.declaracaoInfoProjeto.getIdeDeclaracaoInexigibilidadeInfoProjeto())) {
			try {
				this.declaracaoInexigibilidadeInfoProjetoService.excluirEnderecoProjeto(this.declaracaoInfoProjeto);
				
				this.declaracaoInfoProjeto = new DeclaracaoInexigibilidadeInfoProjeto();
				
				carregarListaEnderecoProjeto();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				
				JsfUtil.addErrorMessage("Não foi possível excluir o Projeto.");
			}
			
			Html.atualizar("tabViewDI:formInfoAdicional:dtProjeto");
		}
	}
	
	public void salvarEnderecoUnidade() {

		try {
			declaracaoInexigibilidadeServiceFacade.salvarUnidadeEndereco(this);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(BUNDLE.getString("di-endereco-erro-salvar"));
		}finally{
			Html.atualizar("tabViewDI:formInfoAdicional");
		}
		
	}
	
	public void prepararParaSalvarEnderecoUnidade(){
		
			camposEnderecoUnidadeValidos = false;
		try {
			DeclaracaoInexigibilidadeInfoUnidade enderecoExistente = this.unidadeEnderecoController.getDeclaracaoInfoUnidade();
			
			
			if(!isAtividadeRecuperacaoEstrada()) {
				this.unidadeEnderecoController.carregarDados();
				Endereco endereco = this.unidadeEnderecoController.getEndereco();
				Bairro bairro = this.unidadeEnderecoController.getBairro();
				Logradouro logradouro = this.unidadeEnderecoController.getLogradouro();
				TipoLogradouro tipoLogradouro = this.unidadeEnderecoController.getTipoLogradouro();
				Municipio municipio = this.unidadeEnderecoController.getMunicipio();
				Integer numCep = this.unidadeEnderecoController.getLogradouroPesquisa().getNumCep();
				
				if(!unidadeEnderecoController.getIsEstadoBahia()) {
					JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
					return;
				}
				
				if(validarEndereco(endereco, numCep, bairro, logradouro, tipoLogradouro, municipio,null)) {
					if(Util.isNullOuVazio(bairro) || bairro.getIdeBairro() <= 0) {
						persistirBairro(bairro, municipio);
					}
					
					if(Util.isNullOuVazio(logradouro.getIdeLogradouro()) || logradouro.getIdeLogradouro() <= 0) {
						persistirLogradouro(logradouro, municipio, tipoLogradouro, bairro, numCep);
					}
					
					enderecoFacade.salvarEnderecoCompletoDeclaracaoInexigibilidade(endereco, bairro, logradouro);
					
					endereco = this.enderecoFacade.carregar(endereco.getIdeEndereco());
					
					enderecoExistente.setEndereco(endereco);
					if(Util.isNull(enderecoExistente.getDeclaracaoInexigibilidade())) {
						enderecoExistente.setDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
					}
					
					this.declaracaoInexigibilidadeInfoUnidadeService.salvarEnderecoUnidade(enderecoExistente);
					
					carregarListaEnderecoUnidade();
					JsfUtil.addSuccessMessage(BUNDLE.getString("di-geral-salvo-sucesso"));
					
					Html.atualizar("tabViewDI:formInfoAdicional:dtUnidade");
					Html.esconder("dialogEnderecoUnidade");
				}
			}else{
				if(Util.isNull(enderecoExistente.getDeclaracaoInexigibilidade())) {
					enderecoExistente.setDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
				}
				enderecoExistente.setEndereco(null);
				this.declaracaoInexigibilidadeInfoUnidadeService.salvarEnderecoUnidade(enderecoExistente);
				
				carregarListaEnderecoUnidade();
				JsfUtil.addSuccessMessage(BUNDLE.getString("di-geral-salvo-sucesso"));
				
				Html.atualizar("tabViewDI:formInfoAdicional:dtUnidade");
				Html.esconder("dialogEnderecoUnidade");
			}
			camposEnderecoUnidadeValidos = true;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(BUNDLE.getString("di-endereco-erro-salvar"));
		}finally{
			Html.atualizar("tabViewDI:formInfoAdicional");
		}
		
	}
	
	public void salvarEnderecoProjeto() {
		try {
			declaracaoInexigibilidadeServiceFacade.salvarEnderecoProjeto(this);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(BUNDLE.getString("di-endereco-erro-salvar"));
		}
	}
	
	public void prepararParaSalvarEnderecoProjeto() throws Exception {
		camposEnderecoProjetoValidos = false;
		DeclaracaoInexigibilidadeInfoProjeto enderecoExistente = this.projetoEnderecoController.getDeclaracaoInfoProjeto();
		
		this.projetoEnderecoController.carregarDados();
		
		Endereco endereco = this.projetoEnderecoController.getEndereco();
		Bairro bairro = this.projetoEnderecoController.getBairro();
		Logradouro logradouro = this.projetoEnderecoController.getLogradouro();
		TipoLogradouro tipoLogradouro = this.projetoEnderecoController.getTipoLogradouro();
		Municipio municipio = this.projetoEnderecoController.getMunicipio();
		Integer numCep = this.projetoEnderecoController.getLogradouroPesquisa().getNumCep();
		
		if(validarEndereco(endereco, numCep, bairro, logradouro, tipoLogradouro, municipio, null)) {
			if(bairro == null || bairro.getIdeBairro() <= 0) {
				persistirBairro(bairro, municipio);
			}
			
			if(logradouro == null || logradouro.getIdeLogradouro() <= 0) {
				persistirLogradouro(logradouro, municipio, tipoLogradouro, bairro, numCep);
			}
			
			enderecoFacade.salvarEnderecoCompletoDeclaracaoInexigibilidade(endereco, bairro, logradouro);
			
			endereco = this.enderecoFacade.carregar(endereco.getIdeEndereco());
			
			enderecoExistente.setEndereco(endereco);
			
			if(Util.isNull(enderecoExistente.getDeclaracaoInexigibilidade())) {
				enderecoExistente.setDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
			}
			
			if(!projetoEnderecoController.getIsEstadoBahia()) {
				JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
				return;
			}
			
			this.declaracaoInexigibilidadeInfoProjetoService.salvarEnderecoProjeto(enderecoExistente);
			
			carregarListaEnderecoProjeto();
			JsfUtil.addSuccessMessage(BUNDLE.getString("di-geral-salvo-sucesso"));
			Html.atualizar("tabViewDI:formInfoAdicional:dtProjeto");
			Html.esconder("dialogProjeto");
			camposEnderecoProjetoValidos = true;
		}
	}
	
	public void salvarPonte() {
		try {
			declaracaoInexigibilidadeServiceFacade.salvarPonte(this);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível salvar a ponte.");
		}
		finally{
			Html.atualizar("tabViewDI:formInfoAdicional");
		}
	}
	
	public void prepararParaSalvarPonte() throws Exception{
		camposEnderecoPonteValidos = false;
		if(Util.isNullOuVazio(this.declaracaoInexigibilidadeInfoPonte.getNomPonte())) {
			JsfUtil.addErrorMessage("É obrigatório o preenchimento do campo Nome da ponte!");
			return;
		}
		
		this.declaracaoInexigibilidadeInfoPonte.setDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
		this.declaracaoInexigibilidadeInfoPonteService.salvar(getDeclaracaoInexigibilidadeInfoPonte());
		
		carregarListaPonte();
		JsfUtil.addSuccessMessage(BUNDLE.getString("di-geral-salvo-sucesso"));
		Html.atualizar("tabViewDI:formInfoAdicional:dtPonte");
		Html.esconder("dialogPonte");
		camposEnderecoPonteValidos = true;
	}
	
	public void excluirPonte() {
		if(!Util.isNull(this.declaracaoInexigibilidadeInfoPonte) && !Util.isNull(this.declaracaoInexigibilidadeInfoPonte.getIdeDeclaracaoInexigibilidadeInfoPonte())) {
			try {
				this.declaracaoInexigibilidadeInfoPonteService.excluirPonte(this.declaracaoInexigibilidadeInfoPonte);
				
				this.declaracaoInexigibilidadeInfoPonte = new DeclaracaoInexigibilidadeInfoPonte();
				
				carregarListaPonte();
				
				JsfUtil.addSuccessMessage("Excluído com sucesso.");
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				
				JsfUtil.addErrorMessage("Não foi possível excluir a Ponte.");
			}
			
			Html.atualizar("tabViewDTRP:formInfoAdicional:dtPonte");
		}
	}
	
	private boolean validarSistema(DeclaracaoInexigibilidadeInfoAbastecimento enderecoExistente) {
		boolean valido = true;
		
		if(Util.isNullOuVazio(enderecoExistente.getNomSistema())) {
			JsfUtil.addErrorMessage("Nome do Sistema é obrigatório.");
			valido = false;
		}
		
		if(Util.isNullOuVazio(enderecoExistente.getLocalizacaoGeografica())) {
			JsfUtil.addErrorMessage("Localização Geográfica é obrigatório.");
			valido = false;
		}
		
		if(!Util.isNullOuVazio(enderecoExistente.getValVazao()) && enderecoExistente.getValVazao().compareTo(new BigDecimal(100)) > 0) {
			JsfUtil.addErrorMessage(BUNDLE.getString("di-inf-0037"));
			valido = false;
		}
		
		return valido;
	}
	
	public void salvarSistema() {
		try {
			declaracaoInexigibilidadeServiceFacade.salvarSistema(this);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(BUNDLE.getString("di-endereco-erro-salvar"));
		}
		finally{
			Html.atualizar("tabViewDI:formInfoAdicional");
		}
	}
	
	public void prepararParaSalvarSistema() throws Exception {
		camposEnderecoSistemaValidos = false;
		DeclaracaoInexigibilidadeInfoAbastecimento enderecoExistente = this.sistemaEnderecoController.getDeclaracaoInfoAbastecimento();
		
		if(!validarSistema(enderecoExistente)) {
			return;
		}
		
			this.sistemaEnderecoController.carregarDados();
			
			Endereco endereco = this.sistemaEnderecoController.getEndereco();
			Bairro bairro = this.sistemaEnderecoController.getBairro();
			Logradouro logradouro = this.sistemaEnderecoController.getLogradouro();
			TipoLogradouro tipoLogradouro = this.sistemaEnderecoController.getTipoLogradouro();
			Municipio municipio = this.sistemaEnderecoController.getMunicipio();
			Integer numCep = this.sistemaEnderecoController.getLogradouroPesquisa().getNumCep();
			
			if(municipio.getIndEstadoEmergencia() == null || !municipio.getIndEstadoEmergencia()) {
				JsfUtil.addErrorMessage(BUNDLE.getString("di-municipio-estado-emergencia"));
				return;
			}
					
			if(!sistemaEnderecoController.getIsEstadoBahia()) {
				JsfUtil.addErrorMessage("Apenas o estado da Bahia é permitido para esta funcionalidade.");
				return;
			}
		
			if(validarEndereco(endereco, numCep, bairro, logradouro, tipoLogradouro, municipio, null)) {
				if(Util.isNullOuVazio(bairro.getIdeBairro()) || bairro.getIdeBairro() <= 0) {
					persistirBairro(bairro, municipio);
				}
				
				if(Util.isNull(logradouro.getIdeLogradouro()) || logradouro.getIdeLogradouro() <= 0) {
					persistirLogradouro(logradouro, municipio, tipoLogradouro, bairro, numCep);
				}
				
				enderecoFacade.salvarEnderecoCompletoDeclaracaoInexigibilidade(endereco, bairro, logradouro);
				
				endereco = this.enderecoFacade.carregar(endereco.getIdeEndereco());
				
				if(endereco != null){
				enderecoExistente.setEndereco(endereco);
				
				if(Util.isNull(enderecoExistente.getDeclaracaoInexigibilidade())) {
					enderecoExistente.setDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
				}
				
				this.declaracaoInexigibilidadeInfoAbastecimentoService.salvarEnderecoAbastecimento(sistemaEnderecoController.getDeclaracaoInfoAbastecimento());
				
				carregarListaEnderecoSistema();
				JsfUtil.addSuccessMessage(BUNDLE.getString("di-geral-salvo-sucesso"));
				camposEnderecoSistemaValidos = true;
			} else{
				throw new Exception();
			}
			
			Html.atualizar("tabViewDI:formInfoAdicional:dtSistema");
			Html.esconder("dialogSistema");
				
		}else{
			return;
		}		
	}
	
	public void excluirSistema() {
		if(!Util.isNull(this.declaracaoInfoAbastecimento) && !Util.isNull(this.declaracaoInfoAbastecimento.getIdeDeclaracaoInexigibilidadeInfoAbastecimento())) {
			try {
				this.declaracaoInexigibilidadeInfoAbastecimentoService.excluirEnderecoAbastecimento(this.declaracaoInfoAbastecimento);
				
				this.declaracaoInfoAbastecimento = new DeclaracaoInexigibilidadeInfoAbastecimento();
				
				carregarListaEnderecoSistema();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				
				JsfUtil.addErrorMessage("Não foi possível excluir o Sitema.");
			}
			
			Html.atualizar("tabViewDTRP:formInfoAdicional:dtSistema");
		}
	}
	
	public void finalizar(){
		
		String mensagem = "";
		
		if(!camposEnderecoPonteValidos && !camposEnderecoProjetoValidos && !camposEnderecoSistemaValidos && !camposEnderecoUnidadeValidos && !camposEnderecoValidos){
			JsfUtil.addErrorMessage("Preencha todos os campos obrigatórios da aba 'Informações adicionais'.");
			return;
		}
		
		try {
			if((this.atividadeSelecionada.getPermiteEndereco() || this.atividadeSelecionada.getPermiteLocalRealizacao())) {
				
				if(this.declaracaoInexigibilidadeInfoGeral != null &&
						this.declaracaoInexigibilidadeInfoGeral.getTipoLocalAtividadeInexigivel() != null &&
						this.declaracaoInexigibilidadeInfoGeral.getTipoLocalAtividadeInexigivel().getIdeTipoLocalAtividadeInexigivel() != null &&
						TipoLocalAtividadeInexigivelEnum.LOCAL_ESPECIFICO.getId().equals(this.declaracaoInexigibilidadeInfoGeral.getTipoLocalAtividadeInexigivel().getIdeTipoLocalAtividadeInexigivel()) &&
						Util.isNullOuVazio(this.listaEnderecoRealizacaoAtividade)) {
					JsfUtil.addErrorMessage(BUNDLE.getString("di-local-realizacao-obrigatorio"));
					return;
				}
			}
			
			if(Boolean.FALSE.equals(this.getDeclaracaoInexigibilidade().getIndCienteRecomendacao())) {
				JsfUtil.addErrorMessage("É obrigatório declarar estar Ciente.");
				return;
			}
			
			if(validarAtividade()) {
				requerimento.setIdeOrgao(requerimentoUnicoService.recuperarOrgao(new Orgao(OrgaoEnum.INEMA.getId())));
				
				if (Util.isNullOuVazio(getRequerimento().getNumRequerimento())) {
					declaracaoInexigibilidadeService.geraNumeroRequerimento(getRequerimento());
				}
				
				RequerimentoPessoa requerente = requerimentoPessoaService.buscarRequerimentoPessoa(requerimento.getIdeRequerimento(), TipoPessoaRequerimentoEnum.REQUERENTE.getId());
				
				if(requerente != null) {
					
					salvar();
					
					this.tramitacaoRequerimentoService.tramitarAutomaticamente(requerimento, StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO, requerente.getPessoa());
					
					Enquadramento enquadramento = declaracaoInexigibilidadeService.finalizar(getRequerimento(), getDeclaracaoInexigibilidade());
					
					this.salvarDeclaracaoInexigibilidadeInfoBueiro();
					
					if(!Util.isNull(enquadramento)) {
						if(!isIsentoBoleto()) {
							this.boletoAutomatizadoController.loadDtrp(getRequerimento());
							
							this.boletoAutomatizadoController.salvarBoletos();
							
							 mensagem = new String("O requerimento foi aberto com sucesso. O número para acompanhamento é: "  
									+ " <br />" 
									+ requerimento.getNumRequerimento()
									+ " <br />");
							 enquadramento.setIndInexigibilidadeIsenta(Boolean.FALSE);
						}else{
							
							this.tramitacaoRequerimentoService.tramitarAutomaticamente(requerimento, StatusRequerimentoEnum.DECLARACAO_EMITIDA);
							
							// mensagem criada sem a informação do boleto pois o requerente está isento
							 mensagem = new String("O requerimento foi aberto com sucesso. O número para acompanhamento é: "  
										+ " <br />" 
										+ requerimento.getNumRequerimento()
										+ " <br />"
										+ "------------------------------------"
										+ " <br />" 
							 			+ "Certificado de Inexigibilidade já disponível para impressão.");
							 
							 enquadramento.setIndInexigibilidadeIsenta(Boolean.TRUE);
						}
						//emitir certificados.
						
						this.enquadramentoService.salvarComunicacao(enquadramento);
					}
					
					ContextoUtil.getContexto().setSolicitanteRequerimento(null);
					
					carregaMsgAberturaRequerimento(mensagem);
					
				}else{
					JsfUtil.addErrorMessage("Não foi possível finalizar, pois não foi encontrado o requerente do requerimento.");
				}
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
	
	public void prepararFinalizar() {
		
		try {
				declaracaoInexigibilidadeServiceFacade.finalizar(this);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}	
			
	}
	
	private void salvarDeclaracaoInexigibilidadeInfoBueiro() throws Exception {
		if(declaracaoInexigibilidadeInfoBueiro == null){
			this.declaracaoInexigibilidadeInfoBueiro = new DeclaracaoInexigibilidadeInfoBueiro();
		}
		this.declaracaoInexigibilidadeInfoBueiro.setDeclaracaoInexigibilidade(getDeclaracaoInexigibilidade());
		this.declaracaoInexigibilidadeInfoBueiro.setNumBueiro(numeroBueiro);
		this.declaracaoInexigibilidadeInfoBueiro.setDesTrajetoVia(descricaoTrajetoVia);
		
		if(this.declaracaoInexigibilidadeInfoBueiro != null && StringUtils.isNotBlank(this.declaracaoInexigibilidadeInfoBueiro.getDesTrajetoVia()) 
				&& this.declaracaoInexigibilidadeInfoBueiro.getNumBueiro() != null){
			this.declaracaoInexigibilidadeInfoBueiroService.salvar(this.declaracaoInexigibilidadeInfoBueiro);
		}
	}
	
	private boolean validarAtividade() {
		boolean valido = true;
		
		/* Se a atividade for obrigatório ter local de realização */
		if(!Util.isNull(this.atividadeSelecionada.getPermiteLocalRealizacao()) && this.atividadeSelecionada.getPermiteLocalRealizacao()) {
			if(this.localRealizacao == 0) { //um local especifico no estado da bahia
				if(!Util.isNullOuVazio(this.listaEnderecoRealizacaoAtividade)) {
					if(this.listaEnderecoRealizacaoAtividade.size() > 1) {
						JsfUtil.addErrorMessage(BUNDLE.getString("di-endereco-realizacao-local-especifico"));
						return false;
					}
				}
			}
			
			if(this.localRealizacao == 1) { //um vários locais no estado da bahia
				if(!Util.isNullOuVazio(this.listaEnderecoRealizacaoAtividade)) {
					JsfUtil.addErrorMessage(BUNDLE.getString("di-endereco-realizacao-varios-locais"));
					return false;
				}
			}
		}
		
		/* Se a atividade for obrigatório ter endereço */
		if(!Util.isNull(this.atividadeSelecionada.getPermiteEndereco()) && this.atividadeSelecionada.getPermiteEndereco()) {
			if(Util.isNullOuVazio(this.listaEnderecoRealizacaoAtividade)) {
				JsfUtil.addErrorMessage("É obrigatório informar um ou mais endereços.");
				return false;
			}
		}
		
		if(this.atividadeSelecionada.getPermiteAbastecimento() != null  && this.atividadeSelecionada.getPermiteAbastecimento()) {
			if(!Util.isNullOuVazio(this.listaSistema)) {
				for (DeclaracaoInexigibilidadeInfoAbastecimento abastecimento : listaSistema) {
					try {
						Endereco endereco = enderecoFacade.carregar(abastecimento.getEndereco().getIdeEndereco());
						
						if(endereco.getIdeLogradouro() != null && 
								endereco.getIdeLogradouro().getMunicipio() != null &&
										endereco.getIdeLogradouro().getMunicipio().getIndEstadoEmergencia() != null &&
										!endereco.getIdeLogradouro().getMunicipio().getIndEstadoEmergencia()) {
							JsfUtil.addErrorMessage(BUNDLE.getString("di-municipio-estado-emergencia"));
							return false;
						}
					} catch (Exception e) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					}
				}
			}
		}
		
		if(this.atividadeSelecionada.getPermiteUnidade() != null && this.atividadeSelecionada.getPermiteUnidade()) {
			if(Util.isNullOuVazio(this.listaUnidade)) {
				JsfUtil.addErrorMessage("É obrigatório informar um ou mais unidades.");
				return false;
			}
			
			if(!Util.isNullOuVazio(this.listaUnidade) && this.listaUnidade.size() > 1) {
				for(DeclaracaoInexigibilidadeInfoUnidade unid : this.listaUnidade) {
					if(Util.isNullOuVazio(unid.getNomUnidade())) {
						JsfUtil.addErrorMessage("É obrigatório informar o Nome da unidade!");
						return false;
					}
					if(Util.isNullOuVazio(unid.getValAreaInundada()) && !unid.getDeclaracaoInexigibilidade().getAtividadeInexigivel().getPermiteUnidade()) {
						JsfUtil.addErrorMessage("É obrigatório informar o valor da área inundada (ha)!");
						return false;
					}
					
					if(Util.isNullOuVazio(unid.getValArea()) && !unid.getDeclaracaoInexigibilidade().getAtividadeInexigivel().getPermiteUnidade()) {
						JsfUtil.addErrorMessage("É obrigatório informar o valor da área (m²)!");
						return false;
					}
					
					if(!isAtividadeRecuperacaoEstrada() && Util.isNull(unid.getEndereco())) {
						JsfUtil.addErrorMessage("É obrigatório informar o endereço de todas as unidades!");
						return false;
					}
				}
			}
		}
		
		if(this.atividadeSelecionada.getPermiteProjeto() != null && this.atividadeSelecionada.getPermiteProjeto()) {
			if(Util.isNullOuVazio(this.listaProjeto)) {
				JsfUtil.addErrorMessage("É obrigatório informar um ou mais projetos!");
				return false;
			}
		}
		
		if(this.atividadeSelecionada.getPermitePonte() != null && this.atividadeSelecionada.getPermitePonte()) {
			if(Util.isNullOuVazio(this.listaPonte)) {
				JsfUtil.addErrorMessage("É obrigatório informar um ou mais pontes!");
				return false;
			}
		}
		
		if(this.atividadeSelecionada.getPermiteQtdBueiros() != null && this.atividadeSelecionada.getPermiteQtdBueiros()) {
			
			if(tipoRioIntervencao == null || tipoRioIntervencao.getIdeTipoRioIntervencao() == null){
				JsfUtil.addErrorMessage("É obrigatório informar o o rio que sofrerá intervenção!");
				return false;
			}
			
			if(Util.isNullOuVazio(numeroBueiro)) {
				JsfUtil.addErrorMessage("É obrigatório informar o número de bueiros!");
				return false;
			}
			
			if(StringUtils.isBlank(descricaoTrajetoVia)) {
				JsfUtil.addErrorMessage("É obrigatório informar o trajeto da via!");
				return false;
			}
		}
		
		return valido;
	}
	
	private boolean isIsentoBoleto() {
		boolean retorno = false;
		
		if(this.atividadeSelecionada.getNomAtividadeInexigivel().contains("Construção e ampliação de sistemas de abastecimento de água com vazão máxima de 100 l/s") &&
				this.sistemaSimplificadoAbastecimentoAgua) {
			retorno = true;
		}
		
		if(this.declaracaoInexigibilidadeInfoGeral != null &&
				this.declaracaoInexigibilidadeInfoGeral.getIndLuzParaTodos() != null && this.declaracaoInexigibilidadeInfoGeral.getIndLuzParaTodos()) {
			retorno = true;
		}
		
		if(this.declaracaoInexigibilidadeInfoGeral != null &&
				this.declaracaoInexigibilidadeInfoGeral.getIndSistemaSimplificadoAbastecimento() != null && this.declaracaoInexigibilidadeInfoGeral.getIndSistemaSimplificadoAbastecimento()) {
			retorno = true;
		}
		
		return retorno;
	}
	
	private void carregaMsgAberturaRequerimento(String mensagem) {
		try {

			ContextoUtil.getContexto().setObject(mensagem);
			System.out.println(ContextoUtil.getContexto().getObject());
			FacesContext.getCurrentInstance().getExternalContext().redirect(prepararParaConsultar());
		} catch (IOException e) {
			JsfUtil.addErrorMessage("Erro ao redirecionar para página de consulta");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void visualizarPonte(DeclaracaoInexigibilidadeInfoPonte declaracaoInexigibilidadeInfoPonte) {
		try {
			this.declaracaoInexigibilidadeInfoPonte = this.declaracaoInexigibilidadeInfoPonteService.obterDeclaracaoInfoPontePorId(declaracaoInexigibilidadeInfoPonte.getIdeDeclaracaoInexigibilidadeInfoPonte());
			permiteEditarPonte = false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível carregar os dados da Ponte.");
		}finally{
			Html.atualizar("tabViewDI:formInfoAdicional");
		}
	}
	
	public void editarPonte(DeclaracaoInexigibilidadeInfoPonte declaracaoInexigibilidadeInfoPonte) {
		if(!Util.isNull(declaracaoInexigibilidadeInfoPonte)) {
			limparPonte();
			
			try {
				this.declaracaoInexigibilidadeInfoPonte = declaracaoInexigibilidadeInfoPonte;
			
				permiteEditarPonte = true;
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage("Não foi possível carregar os dados do Local de Realização.");
			}
			
		}
	}
	
	public void limparPonte() {
		this.declaracaoInexigibilidadeInfoPonte = new DeclaracaoInexigibilidadeInfoPonte();
	}
	
	public String prepararParaConsultar() {
		return "/paginas/novo-requerimento/consulta.xhtml";
	}
	
	private void carregarListaEnderecoUnidade() throws Exception {
		this.listaUnidade = declaracaoInexigibilidadeInfoUnidadeService.obterListaEnderecoUnidade(getDeclaracaoInexigibilidade());
		if(!Util.isNullOuVazio(listaUnidade)){
			camposEnderecoUnidadeValidos = true;
		}
	}
	
	private void carregarListaEnderecoProjeto() throws Exception {
		this.listaProjeto = declaracaoInexigibilidadeInfoProjetoService.obterListaEnderecoProjeto(getDeclaracaoInexigibilidade());
		if(!Util.isNullOuVazio(listaProjeto)){
			camposEnderecoProjetoValidos = true;
		}
	}
	
	private void carregarListaEnderecoSistema() throws Exception {
		this.listaSistema = declaracaoInexigibilidadeInfoAbastecimentoService.obterListaEnderecoSistema(getDeclaracaoInexigibilidade());
		
		if(!Util.isNullOuVazio(listaSistema)){
			camposEnderecoSistemaValidos = true;
		}
	}
	
	private void carregarListaRecomendacao() throws Exception {
		if(!Util.isNull(this.declaracaoInexigibilidade.getAtividadeInexigivel()) && !Util.isNull(this.declaracaoInexigibilidade.getAtividadeInexigivel().getIdeAtividadeInexigivel())) {
			this.listaRecomendacao = recomendacaoAtividadeInexigivelService.obterRecomendacaoPor(this.declaracaoInexigibilidade.getAtividadeInexigivel());
		}
	}
	
	public void avancar() {
		if(!validarSalvar()) {
			return;
		}
		 
		if(permiteEditar) {
			salvar();
		}
		
		if(activeIndex == ABA_ATIVIDADE) {
			carregarAbaInfoAdicional();
		}else if(activeIndex == ABA_INFO_ADICIONAL) {
			carregarAbaRecomendacoes();
		}
		Html.atualizar("tabViewDI");
		Html.atualizar("tabViewDI:formInfoAdicional");
	}
	
	public void voltar() {
		if(permiteEditar) {
			salvar();
		}
		
		if(activeIndex == ABA_INFO_ADICIONAL) {
			try {
				carregarAtividadeInexigivel();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			
			filtrar();
			
			activeIndex = ABA_ATIVIDADE;
			
			Html.atualizar("tabViewDI:formDeclaracao");
		}else if(activeIndex == ABA_RECOMENDACAO) {
			carregarAbaInfoAdicional();
			activeIndex = ABA_INFO_ADICIONAL;
			Html.atualizar("tabViewDI:formInfoAdicional");
		}
		Html.atualizar("tabViewDI");
	}
	
	private void carregarListaTipoLocalAtividade() throws Exception {
		List<TipoLocalAtividadeInexigivel> lista = this.declaracaoInexigibilidadeService.obterListaTipoLocalAtividadeInexigivel();
		
		this.listaTipoLocalAtividade = new ArrayList<SelectItem>();
		
		for (TipoLocalAtividadeInexigivel tipoLocal : lista) {
			this.listaTipoLocalAtividade.add(new SelectItem(tipoLocal, tipoLocal.getDesTipoLocalAtividadeInexigivel()));
		}
	}
	
	private void carregarListaTipoRioIntervencao() throws Exception {
		List<TipoRioIntervencao> lista = this.declaracaoInexigibilidadeService.obterTodosTipoRioIntervencao();
		
		this.listaTipoRioIntervencao = new ArrayList<SelectItem>();
		
		for (TipoRioIntervencao tipoRioIntervencao : lista) {
			this.listaTipoRioIntervencao.add(new SelectItem(tipoRioIntervencao, tipoRioIntervencao.getDesTipoRioIntervencao()));
		}
	}
	
	public StreamedContent getImprimirRelatorio() throws Exception {
	    Map<String, Object> lParametros = new HashMap<String, Object>();
	    lParametros.put("SUBREPORT_DIR", retornaCaminhoRelatorioInexigibilidade());
	    lParametros.put("NOME_RELATORIO", "relatorio.jasper");
	    lParametros.put("ide_requerimento", getRequerimentoConsulta().getIdeRequerimento());
	    
	    Requerimento requerimento = requerimentoService.buscarEntidadePorId(getRequerimentoConsulta());
	    lParametros.put("num_requerimento", requerimento.getNumRequerimento());
	    lParametros.put("dtc_criacao", requerimento.getDtcCriacao());
	    
	    return new RelatorioUtil(lParametros).gerar();
	}
	
	public static String retornaCaminhoRelatorioInexigibilidade() {	
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		return sessao.getServletContext().getRealPath(File.separator + Uri.URL_RELATORIOS_INEXIGIBILIDADE) + File.separator;
	}
	
	public StreamedContent getImprimirCertificado(Integer ideRequerimento) {
		Exception  erro = null;
		try {
			if (!this.declaracaoInexigibilidadeService.hasCertificado(ideRequerimento)) {
				Certificado certificado = this.gerarCertificadoInexigibilidade(ideRequerimento);
				this.declaracaoInexigibilidadeService.salvarCertificadoInexigibilidade(certificado);
			}
			

			return this.imprimirCertificado(ideRequerimento);
		} catch (Exception e) {
			erro =null;
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	private StreamedContent imprimirCertificado(Integer ideRequerimento) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("SUBREPORT_DIR", retornaCaminhoRelatorioInexigibilidade());
		lParametros.put("NOME_RELATORIO", obterModeloCertificado(ideRequerimento));
		lParametros.put("IDE_REQUERIMENTO", ideRequerimento);
		return new RelatorioUtil(lParametros).gerar();
	}
	
	private String obterModeloCertificado(Integer ideRequerimento) throws Exception {
		String modelo = null;
		
		DeclaracaoInexigibilidade declaracao = declaracaoInexigibilidadeService.obterDeclaracaoPorRequerimento(new Requerimento(ideRequerimento));
		
		if(!Util.isNull(declaracao)) {
			AtividadeInexigivel atividade = declaracao.getAtividadeInexigivel();
			
			if(!Util.isNull(atividade)) {
				AtividadeInexigivelModeloCertificadoInexigibilidade modeloCertificado = modeloCertificadoInexigibilidadeService.obterModeloCertificadoPorAtividadeInexigivel(atividade);
				
				if(!Util.isNull(modeloCertificado)) {
					modelo = modeloCertificado.getModeloCertificadoInexigibilidade().getDescricao();
				}
				
				if(Util.isNull(modelo)) {
					DeclaracaoInexigibilidadeInfoGeral declaracaoInexigibilidadeInfoGeral = declaracaoInexigibilidadeInfoGeralService.obterDeclaracaoInfoGeralPor(declaracao);
					
					if(declaracaoInexigibilidadeInfoGeral != null) {
						if(declaracaoInexigibilidadeInfoGeral.getTipoLocalAtividadeInexigivel() != null){
							if(TipoLocalAtividadeInexigivelEnum.LOCAL_ESPECIFICO.getId().equals(declaracaoInexigibilidadeInfoGeral.getTipoLocalAtividadeInexigivel().getIdeTipoLocalAtividadeInexigivel())) {
								modelo = "certificado_inexigibilidade_modelo01";
							}else if(TipoLocalAtividadeInexigivelEnum.DIVERSOS_MUNICIPIOS_NA_BAHIA.getId().equals(declaracaoInexigibilidadeInfoGeral.getTipoLocalAtividadeInexigivel().getIdeTipoLocalAtividadeInexigivel())) {
								modelo = "certificado_inexigibilidade_modelo03";
							}
						}
					}
				}
			}
		}
		
		if(modelo != null) {
			modelo = modelo + ".jasper";
		}
		
		return modelo;
	}
	
	private Certificado gerarCertificadoInexigibilidade(Integer ideRequerimento) throws Exception {
		AtoAmbiental atoAmbiental = getAtoAmbientalInexigibilidade();
		Requerimento requerimento = requerimentoService.buscarEntidadePorId(new Requerimento(ideRequerimento));
		
		return gerarCertificado(atoAmbiental, requerimento);
	}
	
	private Certificado gerarCertificado(AtoAmbiental atoAmbiental, Requerimento requerimento) {
		try {
			return certificadoUtil.gerarCertificadoInexigibilidade(atoAmbiental, requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
	}
	
	protected AtoAmbiental getAtoAmbientalInexigibilidade() {
		AtoAmbiental atoAmbiental = new AtoAmbiental(AtoAmbientalEnum.INEXIGIBILIDADE.getId(), AtoAmbientalEnum.INEXIGIBILIDADE.getSigla());
		return atoAmbiental;
	}
	
	public void changeTipoRioIntervencao() {
		Html.atualizar("tabViewDI:formInfoAdicional:pnlRioIntervencao");
	}
	
	public boolean isInexigivelOutorga() {
		return inexigivelOutorga;
	}

	public boolean isInexigivelLicenca() {
		return inexigivelLicenca;
	}

	public void setInexigivelOutorga(boolean inexigivelOutorga) {
		this.inexigivelOutorga = inexigivelOutorga;
	}

	public void setInexigivelLicenca(boolean inexigivelLicenca) {
		this.inexigivelLicenca = inexigivelLicenca;
	}

	public AtividadeInexigivel getAtividadeSelecionada() {
		return atividadeSelecionada;
	}

	public void setAtividadeSelecionada(AtividadeInexigivel atividadeSelecionada) {
		this.atividadeSelecionada = atividadeSelecionada;
	}

	public AtividadeInexigivel getAtividadeConsulta() {
		return atividadeConsulta;
	}

	public void setAtividadeConsulta(AtividadeInexigivel atividadeConsulta) {
		this.atividadeConsulta = atividadeConsulta;
	}

	public String getDescricaoAtividade() {
		return descricaoAtividade;
	}

	public void setDescricaoAtividade(String descricaoAtividade) {
		this.descricaoAtividade = descricaoAtividade;
	}

	public List<AtividadeInexigivel> getListaAtividadeSelecionada() {
		return listaAtividadeSelecionada;
	}

	public void setListaAtividadeSelecionada(
			List<AtividadeInexigivel> listaAtividadeSelecionada) {
		this.listaAtividadeSelecionada = listaAtividadeSelecionada;
	}

	public List<AtividadeInexigivel> getListaAtividade() {
		return listaAtividade;
	}

	public void setListaAtividade(List<AtividadeInexigivel> listaAtividade) {
		this.listaAtividade = listaAtividade;
	}

	public boolean isPermiteSelecionar() {
		return permiteSelecionar;
	}

	public void setPermiteSelecionar(boolean permiteSelecionar) {
		this.permiteSelecionar = permiteSelecionar;
	}

	public DeclaracaoInexigibilidade getDeclaracaoInexigibilidade() {
		return declaracaoInexigibilidade;
	}

	public void setDeclaracaoInexigibilidade(
			DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		this.declaracaoInexigibilidade = declaracaoInexigibilidade;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public Boolean getDesabilitaTab01() {
		return desabilitaTab01;
	}

	public Boolean getDesabilitaTab02() {
		return desabilitaTab02;
	}

	public Boolean getDesabilitaTab03() {
		return desabilitaTab03;
	}

	public void setDesabilitaTab01(Boolean desabilitaTab01) {
		this.desabilitaTab01 = desabilitaTab01;
	}

	public void setDesabilitaTab02(Boolean desabilitaTab02) {
		this.desabilitaTab02 = desabilitaTab02;
	}

	public void setDesabilitaTab03(Boolean desabilitaTab03) {
		this.desabilitaTab03 = desabilitaTab03;
	}

	public int getLocalRealizacao() {
		return localRealizacao;
	}

	public void setLocalRealizacao(int localRealizacao) {
		this.localRealizacao = localRealizacao;
	}

	public DeclaracaoInexigibilidadeInfoGeral getDeclaracaoInexigibilidadeInfoGeral() {
		return declaracaoInexigibilidadeInfoGeral;
	}

	public void setDeclaracaoInexigibilidadeInfoGeral(
			DeclaracaoInexigibilidadeInfoGeral declaracaoInexigibilidadeInfoGeral) {
		this.declaracaoInexigibilidadeInfoGeral = declaracaoInexigibilidadeInfoGeral;
	}

	public boolean isLuzParaTodos() {
		return luzParaTodos;
	}

	public boolean isSupressaoVegetacao() {
		return supressaoVegetacao;
	}

	public void setLuzParaTodos(boolean luzParaTodos) {
		this.luzParaTodos = luzParaTodos;
	}

	public void setSupressaoVegetacao(boolean supressaoVegetacao) {
		this.supressaoVegetacao = supressaoVegetacao;
	}

	public Integer getNumeroBueiro() {
		return numeroBueiro;
	}

	public void setNumeroBueiro(Integer numeroBueiro) {
		this.numeroBueiro = numeroBueiro;
	}

	public String getDescricaoTrajetoVia() {
		return descricaoTrajetoVia;
	}

	public void setDescricaoTrajetoVia(String descricaoTrajetoVia) {
		this.descricaoTrajetoVia = descricaoTrajetoVia;
	}

	public List<DeclaracaoInexigibilidadeInfoUnidade> getListaUnidade() {
		return listaUnidade;
	}

	public void setListaUnidade(
			List<DeclaracaoInexigibilidadeInfoUnidade> listaUnidade) {
		this.listaUnidade = listaUnidade;
	}

	public DeclaracaoInexigibilidadeInfoUnidade getDeclaracaoInfoUnidade() {
		return declaracaoInfoUnidade;
	}

	public void setDeclaracaoInfoUnidade(
			DeclaracaoInexigibilidadeInfoUnidade declaracaoInfoUnidade) {
		this.declaracaoInfoUnidade = declaracaoInfoUnidade;
	}

	public DeclaracaoInexigibilidadeInfoProjeto getDeclaracaoInfoProjeto() {
		return declaracaoInfoProjeto;
	}

	public void setDeclaracaoInfoProjeto(
			DeclaracaoInexigibilidadeInfoProjeto declaracaoInfoProjeto) {
		this.declaracaoInfoProjeto = declaracaoInfoProjeto;
	}

	public List<DeclaracaoInexigibilidadeInfoProjeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(
			List<DeclaracaoInexigibilidadeInfoProjeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public DeclaracaoInexigibilidadeInfoAbastecimento getDeclaracaoInfoAbastecimento() {
		return declaracaoInfoAbastecimento;
	}

	public void setDeclaracaoInfoAbastecimento(
			DeclaracaoInexigibilidadeInfoAbastecimento declaracaoInfoAbastecimento) {
		this.declaracaoInfoAbastecimento = declaracaoInfoAbastecimento;
	}

	public DeclaracaoInexigibilidadeInfoPonte getDeclaracaoInexigibilidadeInfoPonte() {
		return declaracaoInexigibilidadeInfoPonte;
	}

	public DeclaracaoInexigibilidadeInfoBueiro getDeclaracaoInexigibilidadeInfoBueiro() {
		return declaracaoInexigibilidadeInfoBueiro;
	}

	public void setDeclaracaoInexigibilidadeInfoPonte(
			DeclaracaoInexigibilidadeInfoPonte declaracaoInexigibilidadeInfoPonte) {
		this.declaracaoInexigibilidadeInfoPonte = declaracaoInexigibilidadeInfoPonte;
	}

	public void setDeclaracaoInexigibilidadeInfoBueiro(
			DeclaracaoInexigibilidadeInfoBueiro declaracaoInexigibilidadeInfoBueiro) {
		this.declaracaoInexigibilidadeInfoBueiro = declaracaoInexigibilidadeInfoBueiro;
	}

	public List<DeclaracaoInexigibilidadeInfoAbastecimento> getListaSistema() {
		return listaSistema;
	}

	public void setListaSistema(
			List<DeclaracaoInexigibilidadeInfoAbastecimento> listaSistema) {
		this.listaSistema = listaSistema;
	}

	public boolean isSistemaSimplificadoAbastecimentoAgua() {
		return sistemaSimplificadoAbastecimentoAgua;
	}

	public void setSistemaSimplificadoAbastecimentoAgua(
			boolean sistemaSimplificadoAbastecimentoAgua) {
		this.sistemaSimplificadoAbastecimentoAgua = sistemaSimplificadoAbastecimentoAgua;
	}

	public List<RecomendacaoAtividadeInexigivel> getListaRecomendacao() {
		return listaRecomendacao;
	}

	public void setListaRecomendacao(
			List<RecomendacaoAtividadeInexigivel> listaRecomendacao) {
		this.listaRecomendacao = listaRecomendacao;
	}

	public Pessoa getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Pessoa solicitante) {
		this.solicitante = solicitante;
	}

	public boolean isPermiteEditar() {
		return permiteEditar;
	}

	public void setPermiteEditar(boolean permiteEditar) {
		this.permiteEditar = permiteEditar;
	}

	public List<DeclaracaoInexigibilidadeInfoPonte> getListaPonte() {
		return listaPonte;
	}

	public void setListaPonte(List<DeclaracaoInexigibilidadeInfoPonte> listaPonte) {
		this.listaPonte = listaPonte;
	}

	public boolean isPonteQuantidadeQualidadeAgua() {
		return ponteQuantidadeQualidadeAgua;
	}

	public void setPonteQuantidadeQualidadeAgua(boolean ponteQuantidadeQualidadeAgua) {
		this.ponteQuantidadeQualidadeAgua = ponteQuantidadeQualidadeAgua;
	}

	public boolean isPermiteEditarPonte() {
		return permiteEditarPonte;
	}

	public void setPermiteEditarPonte(boolean permiteEditarPonte) {
		this.permiteEditarPonte = permiteEditarPonte;
	}

	public List<SelectItem> getListaTipoLocalAtividade() {
		return listaTipoLocalAtividade;
	}

	public void setListaTipoLocalAtividade(
			List<SelectItem> listaTipoLocalAtividade) {
		this.listaTipoLocalAtividade = listaTipoLocalAtividade;
	}

	public List<Endereco> getListaEnderecoRealizacaoAtividade() {
		return listaEnderecoRealizacaoAtividade;
	}

	public void setListaEnderecoRealizacaoAtividade(
			List<Endereco> listaEnderecoRealizacaoAtividade) {
		this.listaEnderecoRealizacaoAtividade = listaEnderecoRealizacaoAtividade;
	}

	public boolean isAtividadeRecuperacaoEstrada() {
		if(atividadeSelecionada != null && atividadeSelecionada.getIdeAtividadeInexigivel().equals(new Integer(76))) {
			return true;
		}
		
		return false;
	}
	
	public boolean isAtividadeAreaInundada() {
		if(atividadeSelecionada != null && atividadeSelecionada.getIdeAtividadeInexigivel().equals(new Integer(72)) || 
				atividadeSelecionada.getIdeAtividadeInexigivel().equals(new Integer(78))) {
			return true;
		}
		
		return false;
	}
	
	public int getValorAlturaModalUnidade() {
		if(isAtividadeRecuperacaoEstrada()) {
			return 200;
		}
		
		return 470;
	}
	
	public List<SelectItem> getListaTipoRioIntervencao() {
		return listaTipoRioIntervencao;
	}

	public void setListaTipoRioIntervencao(List<SelectItem> listaTipoRioIntervencao) {
		this.listaTipoRioIntervencao = listaTipoRioIntervencao;
	}

	public TipoRioIntervencao getTipoRioIntervencao() {
		return tipoRioIntervencao;
	}

	public void setTipoRioIntervencao(TipoRioIntervencao tipoRioIntervencao) {
		this.tipoRioIntervencao = tipoRioIntervencao;
	}

	public RequerimentoPessoa getReqPessoaRequerente() {
		return reqPessoaRequerente;
	}

	public void setReqPessoaRequerente(RequerimentoPessoa reqPessoaRequerente) {
		this.reqPessoaRequerente = reqPessoaRequerente;
	}

	public RequerimentoPessoa getReqPessoaSolicitante() {
		return reqPessoaSolicitante;
	}

	public void setReqPessoaSolicitante(RequerimentoPessoa reqPessoaSolicitante) {
		this.reqPessoaSolicitante = reqPessoaSolicitante;
	}

	public boolean isPermiteSalvar() {
		return permiteSalvar;
	}

	public void setPermiteSalvar(boolean permiteSalvar) {
		this.permiteSalvar = permiteSalvar;
	}

	public Requerimento getRequerimentoConsulta() {
		return requerimentoConsulta;
	}

	public void setRequerimentoConsulta(Requerimento requerimentoConsulta) {
		this.requerimentoConsulta = requerimentoConsulta;
	}

	public boolean isCamposEnderecoValidos() {
		return camposEnderecoValidos;
	}

	public void setCamposEnderecoValidos(boolean camposEnderecoValidos) {
		this.camposEnderecoValidos = camposEnderecoValidos;
	}

	public boolean isCamposEnderecoUnidadeValidos() {
		return camposEnderecoUnidadeValidos;
	}

	public void setCamposEnderecoUnidadeValidos(boolean camposEnderecoUnidadeValidos) {
		this.camposEnderecoUnidadeValidos = camposEnderecoUnidadeValidos;
	}

	public boolean isCamposEnderecoProjetoValidos() {
		return camposEnderecoProjetoValidos;
	}

	public void setCamposEnderecoProjetoValidos(boolean camposEnderecoProjetoValidos) {
		this.camposEnderecoProjetoValidos = camposEnderecoProjetoValidos;
	}

	public boolean isCamposEnderecoPonteValidos() {
		return camposEnderecoPonteValidos;
	}

	public void setCamposEnderecoPonteValidos(boolean camposEnderecoPonteValidos) {
		this.camposEnderecoPonteValidos = camposEnderecoPonteValidos;
	}

	public boolean isCamposEnderecoSistemaValidos() {
		return camposEnderecoSistemaValidos;
	}

	public void setCamposEnderecoSistemaValidos(boolean camposEnderecoSistemaValidos) {
		this.camposEnderecoSistemaValidos = camposEnderecoSistemaValidos;
	}
}