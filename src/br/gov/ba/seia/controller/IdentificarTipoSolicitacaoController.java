package br.gov.ba.seia.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.dto.RequerimentoUnicoDTO;
import br.gov.ba.seia.dto.TipoSolicitacaoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.CumprimentoReposicaoFlorestal;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.DeclaracaoTransporte;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.EnquadramentoDocumentoAto;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Pergunta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoExterno;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.Sistema;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.SistemaEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.enumerator.TipoTelefoneEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.NovoRequerimentoServiceFacade;
import br.gov.ba.seia.facade.TipoSolicitacaoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.CumprimentoReposicaoFlorestalService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeService;
import br.gov.ba.seia.service.DeclaracaoTransporteService;
import br.gov.ba.seia.service.DocumentoAtoAmbientalService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.EmailService;
import br.gov.ba.seia.service.EnderecoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.NovoRequerimentoService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.ProcessoExternoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("identificarTipoSolicitacaoController")
@ViewScoped
public class IdentificarTipoSolicitacaoController {

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	private int indiceTela = 0;
//	private int indiceTela = 1; // TEMPORARIO, TIRAR QUANDO SUBIR DTRP
	
	@EJB
	private PessoaService pessoaService;
	
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioReqService;
		
	@EJB
	private EnquadramentoService enquadramentoService;
	
	@EJB
	private DocumentoAtoAmbientalService documentoAtoAmbientalService;
	
	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private ProcessoExternoService processoExternoService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private EnderecoService enderecoService;
	
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	
	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;
	
	@EJB
	private EmailService emailService;
	
	@EJB
	private NovoRequerimentoService novoRequerimentoService;
	
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private PerguntaService perguntaService;
	
	@EJB
	private TipoSolicitacaoServiceFacade tipoSolicitacaoServiceFacade;
	
	@EJB
	private DeclaracaoTransporteService declaracaoTransporteService;
	
	@EJB
	private NovoRequerimentoServiceFacade novoRequerimentoServiceFacade;
	
	@EJB
	private DeclaracaoInexigibilidadeService declaracaoInexigibilidadeService;
	
	@EJB
	private CumprimentoReposicaoFlorestalService cumprimentoReposicaoFlorestalService;
	
	@Inject
	private IdentificarPapelController identificarPapelController;
	@Inject
	private EnquadramentoController enquadramentoController;
	
	private Pessoa pessoa;
	private TipoRequerimento tipoRequerimento;
	private Requerimento requerimento;
	private Requerimento requerimentoSelecionado;
	private Empreendimento empreendimento;
	private SolicitacaoAdministrativo solicitacaoAdministrativo;
	
	private boolean dadosContato;
	private boolean desabilitarTudo;
	private boolean modoEdicao = false;
	private Boolean enderecoContato;
//	private Boolean visualizaSelectEmpreendimento = true;
	private Boolean mostraQuestionarioAposSalvar;
	private boolean declaracao = false;
	private boolean declaracaoAutorizacaoEspecial = false;
	private boolean modoEnquadramento;
	
	private String novaRazaoSocial;
	private String numProcesso;
	
	private RequerimentoDTO requerimentoDTO;
	private RequerimentoUnicoDTO requerimentoUnicoDTO;
	
//	private PerguntaRequerimento pergunta0_1;
	private boolean perguntasAba0Carregadas;
	private List<SolicitacaoAdministrativo> listaSolicitacaoAdministrativo;
	
	private List<Pergunta> listPerguntas;
	private List<PerguntaRequerimento> listPerguntaRequerimentoAba0;
	private PerguntaRequerimento pergunta0_1;
	
	private EmpreendimentoRequerimento empreendimentoRequerimento;
	
	private List<TipoSolicitacaoDTO> listaTipoSolicitacao;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		carregaPerguntas();
		
		mostraQuestionarioAposSalvar = Boolean.FALSE;
		
		// Depois de selecionar AVANÇAR na tela de seleção
		if (Util.isNullOuVazio(this.pessoa)) {
			pessoa = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa();
		} else {
			ContextoUtil.getContexto().setRequerenteRequerimento(this.pessoa);
		}
		
		montarListaTipoSolicitacao();
		
		if (Util.isNullOuVazio(empreendimento)) {
			this.empreendimento = new Empreendimento();
		}

		if (Util.isNullOuVazio(empreendimentoRequerimento)) {
			empreendimentoRequerimento = new EmpreendimentoRequerimento();
			empreendimentoRequerimento.setIdeEmpreendimento(empreendimento);
		}
		
		final Object temp = ContextoUtil.getContexto().getObject();
		// Caso o usuário clique em VISUALIZAR ou EDITAR na tela de CONSULTA.
		if (temp != null && temp instanceof RequerimentoDTO) {
			requerimentoDTO = ((RequerimentoDTO) temp);
			if (requerimentoDTO.isVisualizar()) {
				desabilitarTudo = true;
				declaracao = declaracaoAutorizacaoEspecial = true;
				this.indiceTela = requerimentoDTO.getIndiceTela();
			} else {
				modoEdicao = true;
			}
			requerimento = requerimentoDTO.getRequerimento();
			empreendimentoRequerimento.setIdeRequerimento(requerimento);

			empreendimento = requerimentoDTO.getEmpreendimento();

			if(!Util.isNullOuVazio(empreendimento) &&!Util.isNullOuVazio(empreendimento.getIdeEmpreendimento())){

				empreendimentoRequerimento.setIdeRequerimento(requerimento);
				empreendimentoRequerimento.setIdeEmpreendimento(empreendimento);
			}

			try {
				empreendimentoRequerimento = requerimentoService.carregarEmpreendimentoRequerimento(requerimento.getIdeRequerimento());
			} catch (Exception e1) {
				System.out.println("Erro ao CARREGAR EMPREENDIMENTO REQUERIMENTO!!");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
				throw Util.capturarException(e1, Util.SEIA_EXCEPTION);
			}

			ContextoUtil.getContexto().setEmpreendimento(empreendimento);
			this.modoEnquadramento = requerimentoDTO.isModoEnquadramento();

			this.carregarEnderecoContato();


			try {
				PerguntaRequerimento pr = perguntaRequerimentoService.consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimento(pergunta0_1.getIdePergunta().getCodPergunta(), this.requerimento.getIdeRequerimento());

				if(pr != null) {
					pergunta0_1 = pr;
				}

					if(solicitacaoAdministrativo == null) {
						
						solicitacaoAdministrativo = new SolicitacaoAdministrativo();
						solicitacaoAdministrativo = solicitacaoAdministrativoService.obterSolicitacaoAdministrativa(requerimento, TipoSolicitacaoEnum.ALTERACAO_RAZAO_SOCIAL);
						
						listaSolicitacaoAdministrativo = new ArrayList<SolicitacaoAdministrativo>();
						listaSolicitacaoAdministrativo.add(solicitacaoAdministrativo);
					}

					if(!Util.isNull(solicitacaoAdministrativo) && !Util.isNull(solicitacaoAdministrativo.getNomRazaoSocialNova())) {
						novaRazaoSocial = solicitacaoAdministrativo.getNomRazaoSocialNova();
						mostraQuestionarioAposSalvar = Boolean.FALSE;
					}

				pessoa = ((RequerimentoDTO) temp).getPessoa();
				
				montarListaTipoSolicitacao();
				
				requerimentoSelecionado = requerimento;
			} catch (Exception e1) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
				throw Util.capturarException(e1, Util.SEIA_EXCEPTION);
			}
		} else {
			requerimento = new Requerimento();
			requerimentoSelecionado = requerimento;
		}
		
		if (pessoa != null) {

			List<Telefone> listaTel = null;

			try {
				listaTel = telefoneService.buscarTelefonesPorPessoa(pessoa);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}

			if (!Util.isNullOuVazio(pessoa.getPessoaFisica()) && !Util.isNullOuVazio(pessoa.getPessoaFisica().getNomPessoa()) && Util.isNullOuVazio(requerimento.getNomContato())) {
				requerimento.setNomContato(pessoa.getPessoaFisica().getNomPessoa());

				if (!Util.isNullOuVazio(pessoa.getPessoaFisica().getPessoa().getDesEmail()) && Util.isNullOuVazio(requerimento.getDesEmail())) {
					requerimento.setDesEmail(pessoa.getPessoaFisica().getPessoa().getDesEmail());
				}
			} else if (!Util.isNullOuVazio(pessoa.getPessoaJuridica()) && Util.isNullOuVazio(requerimento.getNomContato())) {

				requerimento.setNomContato(pessoa.getPessoaJuridica().getNomRazaoSocial());

				Integer tipoSolicit = ContextoUtil.getContexto().getTipoSolicitante();
				ContextoUtil.getContexto().setTipoSolicitante(null);

				if (Util.isNullOuVazio(tipoSolicit) || tipoSolicit.equals(1)) {
					PessoaJuridica pJuridica = null;
					if(ContextoUtil.getContexto().getSolicitanteRequerimento() != null){
						pJuridica = ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaJuridica();
					}

					if(!Util.isNullOuVazio(pJuridica) && !Util.isNullOuVazio(pJuridica.getPessoa()) && !Util.isNullOuVazio(pJuridica.getPessoa().getDesEmail())) {
						requerimento.setDesEmail(pJuridica.getPessoa().getDesEmail());
					}
				} else if (tipoSolicit.equals(4) || tipoSolicit.equals(2)) {

					if (!Util.isNullOuVazio(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica())) {
						requerimento.setDesEmail(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica().getPessoa().getDesEmail());
					} else {
						requerimento.setDesEmail(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa().getDesEmail());
					}
				}
			}

			if (!listaTel.isEmpty() && Util.isNullOuVazio(requerimento.getNumTelefone())) {
				try {
					if (listaTel.size() > 1) {
						requerimento.setNumTelefone(getTelefoneParaRequerimento(listaTel).getNumTelefone());
					} else {
						requerimento.setNumTelefone(listaTel.get(0).getNumTelefone());
					}
				} catch (Exception e) {
					requerimento.setNumTelefone(listaTel.get(0).getNumTelefone());
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}
		}
	}
	
	public void carregaPerguntas() {

		if (!perguntasAba0Carregadas) {
			try {
				listPerguntas = new ArrayList<Pergunta>();
				listPerguntas = perguntaService.carregarPerguntasNovoRequerimento(0);

				listPerguntaRequerimentoAba0 = new ArrayList<PerguntaRequerimento>();

				for (Pergunta pergunta : listPerguntas) {
					if (pergunta.getCodPergunta().equals(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA0_1.getCod())) {
						pergunta0_1 = new PerguntaRequerimento(pergunta);
						listPerguntaRequerimentoAba0.add(getPergunta0_1());
					}
				}

				perguntasAba0Carregadas = true;
			} catch (Exception e) {
				perguntasAba0Carregadas = false;
				JsfUtil.addErrorMessage("Ocorreu um erro ao carregar as perguntas " + e.getMessage());
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public String redirecionarVisualizacao(RequerimentoDTO dto) {
		try {
			
			RequerimentoUnico requerimentoUnico = novoRequerimentoService.obterRequerimentoUnico(dto.getRequerimento());
			
			if (Util.isNullOuVazio(requerimentoUnico)) {
				
				for(AtoAmbiental ato : dto.getRequerimento().getAtosAmbientais()) {
					
					//Se for igual a Alteração de razão social.
					if(ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.ARLS.getId())) {
						return "/paginas/identificar-tipo-solicitacao/identificar-tipo-solicitacao.xhtml";
					}
				}
				
				String numRequerimento = dto.getRequerimento().getNumRequerimento();
				
				if(!Util.isNullOuVazio(numRequerimento)) {
					if(numRequerimento.contains("INEXIG")) {
						DeclaracaoInexigibilidade declaracaoInexigibilidade = declaracaoInexigibilidadeService.obterDeclaracaoPorRequerimento(dto.getRequerimento());
						if(!Util.isNull(declaracaoInexigibilidade)) {
							return "/paginas/manter-declaracao-inexigibilidade/declaracaoInexigibilidade.xhtml";
						}
					} else if(numRequerimento.contains("REPFLOR")) {
						CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal = cumprimentoReposicaoFlorestalService.obterCumprimentoReposicaoFlorestalPorRequerimento(dto.getRequerimento());
						if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestal)) {
							return "/paginas/manter-CRF/cumprimentoReposicaoFlorestal.xhtml";
						}
					} else if(numRequerimento.contains("DTRP")) {
						DeclaracaoTransporte declaracaoTransporte = declaracaoTransporteService.obterDeclaracaoPorRequerimento(dto.getRequerimento());
						if(!Util.isNull(declaracaoTransporte)) {
							return "/paginas/manter-dtrp/declaracao-transporte-residuo-perigoso-incluir.xhtml";
						}
					}
				}
				else{
					DeclaracaoInexigibilidade declaracaoInexigibilidade = declaracaoInexigibilidadeService.obterDeclaracaoPorRequerimento(dto.getRequerimento());
					if(!Util.isNull(declaracaoInexigibilidade)) {
						return "/paginas/manter-declaracao-inexigibilidade/declaracaoInexigibilidade.xhtml";
					}
					CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal = cumprimentoReposicaoFlorestalService.obterCumprimentoReposicaoFlorestalPorRequerimento(dto.getRequerimento());
					if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestal)) {
						return "/paginas/manter-CRF/cumprimentoReposicaoFlorestal.xhtml";
					}
					DeclaracaoTransporte declaracaoTransporte = declaracaoTransporteService.obterDeclaracaoPorRequerimento(dto.getRequerimento());
					if(!Util.isNull(declaracaoTransporte)) {
						return "/paginas/manter-dtrp/declaracao-transporte-residuo-perigoso-incluir.xhtml";
					}
				}
				
				return "/paginas/novo-requerimento/incluirNovoRequerimento.xhtml";
				
			} else {
				pessoa = pessoaService.carregarDadosRequerente(requerimentoUnico.getIdeRequerimentoUnico(), null);
				requerimentoUnicoDTO = new RequerimentoUnicoDTO(requerimentoUnico, pessoa, empreendimento, dto.getStatusRequerimento(), null);

				if (dto.isVisualizar()) {
					requerimentoUnicoDTO.setVisualizar(true);
				} else {
					requerimentoUnicoDTO.setVisualizar(false);
				}
				
				ContextoUtil.getContexto().setObject(requerimentoUnicoDTO);
				
				return "/paginas/requerimentos/incluirRequerimentoUnico.xhtml";
			}
		} catch (Exception e) {
			JsfUtil.addWarnMessage("Erro no redirecionamento de página!");
			return null;
		}
	}
	
	public String avancar() {
		if(indiceTela == 0){
			JsfUtil.addWarnMessage(BUNDLE.getString("requerimentoUnicoLabelSelecioneTipoSolicitacao")); 
		}else if(indiceTela == 1) {
			return "/paginas/novo-requerimento/incluirNovoRequerimento.xhtml";
		}else if (indiceTela == 2) {
			return "/paginas/manter-dtrp/declaracao-transporte-residuo-perigoso-incluir.xhtml";
		}else if (indiceTela == 3) {
			return "/paginas/manter-declaracao-inexigibilidade/declaracaoInexigibilidade.xhtml";
		}else if (indiceTela == 5) {
			return "/paginas/manter-CRF/cumprimentoReposicaoFlorestal.xhtml";
		}
		return "";
	}
	
	/**
	 * Retona o telefone segundo a regra: primeiramente o celular, se não tiver
	 * celular, retorna o comercial e por último o residencial.
	 *
	 * @param collectionTel
	 * @return Telefone
	 */
	public Telefone getTelefoneParaRequerimento(List<Telefone> collectionTel) {
		Telefone tel = null;
		Telefone telRequerimento = null;
		Boolean isComercial = Boolean.FALSE;
		// Loop para pegar primeiramente o celular, se não tiver cel, pega o
		// comercial e por ultimo o residencial.
		for (Telefone telefone : collectionTel) {
			tel = telefone;
			if (tel.getIdeTipoTelefone().getIdeTipoTelefone() == TipoTelefoneEnum.CELULAR.getId()) {
				telRequerimento = tel;
				break;
			}
			if (!isComercial && tel.getIdeTipoTelefone().getIdeTipoTelefone() == TipoTelefoneEnum.RESIDENCIAL.getId()) {
				telRequerimento = tel;
			}
			if (tel.getIdeTipoTelefone().getIdeTipoTelefone() == TipoTelefoneEnum.COMERCIAL.getId()) {
				telRequerimento = tel;
				isComercial = true;
			}
		}
		return telRequerimento;
	}
	
	public void limparSolicitacaoAdministrativo() {
		numProcesso = "";
		listaSolicitacaoAdministrativo = new ArrayList<SolicitacaoAdministrativo>();
	}
	
	public String getCaminhoIdentificarPapel(){
		if(isRequerimentoDeRegulacaoAmbiental()){
			return "/paginas/identificar-papel/solicitante.xhtml";
		} 
		else if(isRequerimentoDeCadastroDeImovel()){
			return "/paginas/manter-imoveis/identificar-papel/solicitantepj.xhtml";
		}
		return "";
	}
	
	public void adicionarSolicitacaoAdministrativo() {
		try {
			
			Processo processo = processoService.buscarProcessoPorCriteria(numProcesso);
			ProcessoExterno processoExterno = processoExternoService.buscarProcessoExternoByNumeroProcesso(numProcesso);
			
			SolicitacaoAdministrativo solicitacaoAdministrativo = new SolicitacaoAdministrativo();
			solicitacaoAdministrativo.setIdeTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.ALTERACAO_RAZAO_SOCIAL.getId()));
			
			if(!Util.isNull(processo)) {
				
				boolean isProcessoConluido = processoService.isProcessoNoStatus(processo.getIdeProcesso(), StatusFluxoEnum.CONCLUIDO.getStatus());
				
				if(isProcessoConluido) {
					solicitacaoAdministrativo.setNumProcesso(processo.getNumProcesso());
					solicitacaoAdministrativo.setIdeSistema(getSistema(SistemaEnum.SEIA.getNomSistema()));
				}
				else {
					throw new SeiaValidacaoRuntimeException("O processo informado ainda não está concluído.");
				}
			}
			else if(!Util.isNull(processoExterno)) {
				solicitacaoAdministrativo.setNumProcesso(processoExterno.getProcesso());
				solicitacaoAdministrativo.setIdeSistema(getSistema(processoExterno.getSistema()));
			}
			else {
				solicitacaoAdministrativo.setNumProcesso(numProcesso);
				solicitacaoAdministrativo.setIdeSistema(getSistema(SistemaEnum.DESCONHECIDO.getNomSistema()));
			}
			
			listaSolicitacaoAdministrativo = new ArrayList<SolicitacaoAdministrativo>();
			listaSolicitacaoAdministrativo.add(solicitacaoAdministrativo);
			RequestContext.getCurrentInstance().execute("dlgInformaProcessoConcluido.hide()");
			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");
			
		}
		catch (SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private Sistema getSistema(String sistema) {
		for (SistemaEnum sistemaEnum : SistemaEnum.values()) {
			if(sistemaEnum.getNomSistema().equalsIgnoreCase(sistema)){
				return  new Sistema(sistemaEnum.getId(), sistemaEnum.getNomSistema());
			}
		}
		
		return new Sistema(SistemaEnum.DESCONHECIDO.getId(), SistemaEnum.DESCONHECIDO.getNomSistema());
	}
	
	private void carregarEnderecoContato() {

		try {

			if (!Util.isNullOuVazio(requerimento.getIdeEnderecoContato()) && !Util.isNullOuVazio(requerimento.getIdeEnderecoContato().getIdeEndereco())) {

				this.enderecoContato = true;
				Endereco endereco = this.enderecoService.carregar(requerimento.getIdeEnderecoContato().getIdeEndereco());
				ContextoUtil.getContexto().setRequerimentoEndereco(endereco);
			} else {
				requerimento.setIdeEnderecoContato(null);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void finalizarRequerimentoRazaoSocial() {

		try {

			if(isRequerentePJ()){
				identificarPapelController.setPessoaJuridica(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa().getPessoaJuridica());
			}

			if(isRequerentePF() || (isRequerentePJ() && identificarPapelController.validarPJ())){

				if (validarRazaoSocial()) {

					List<String> emails = emailService.listarEmailsRequerente(this.requerimento);

					if(Util.isNullOuVazio(emails)) {
						JsfUtil.addErrorMessage("É necessário informar pelo menos um endereço de email para que o email seja enviado.");
						return;
					}

					if (Util.isNullOuVazio(requerimento.getIdeOrgao())) {
						// ideOrga = 1 (INEMA)
						requerimento.setIdeOrgao(new Orgao(1));
					}

					RequerimentoPessoa reqPessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente();

					if (this.novoRequerimentoService.verificarExistenciaARLS(reqPessoaRequerente)) {
						JsfUtil.addErrorMessage("Já existe uma solicitação de alteração de razão social");
						return;
					}

					requerimento.setIdeOrgao(requerimentoUnicoService.recuperarOrgao(requerimento.getIdeOrgao()));

					if (!modoEdicao) {

						
						for(SolicitacaoAdministrativo sa : listaSolicitacaoAdministrativo) {
							solicitacaoAdministrativo = sa;
						}

						// ideTipoRequerimento = 1 (Regulação Ambiental do
						// Empreendimento)
						TipoRequerimento tipoRequerimento = new TipoRequerimento(1);
						requerimento.setIdeTipoRequerimento(tipoRequerimento);
						requerimento.setDtcCriacao(new Date());
						requerimento.setIndDeclaracao(true);
						requerimento.setIndExcluido(false);
						requerimento.setDtcFinalizacao(new Date());

						adicionarTramitacao(StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO);

						pessoasRequerimento();

						solicitacaoAdministrativo.setIdeRequerimento(requerimento);
						solicitacaoAdministrativo.setIdeTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.ALTERACAO_RAZAO_SOCIAL.getId()));
					}

					if (Util.isNullOuVazio(getRequerimento().getNumRequerimento())) {
						requerimentoUnicoService.geraNumeroRequerimento(requerimento);
						novoRequerimentoService.inserirRequerimento(requerimento);
					}

					solicitacaoAdministrativo.setNomRazaoSocialNova(novaRazaoSocial);
					solicitacaoAdministrativoService.salvarOuAtualizarSolicitacaoAdministrativa(solicitacaoAdministrativo);

					this.efetuarEnquadramentoAutomatico(AtoAmbientalEnum.ARLS);

					this.carregaMsgAberturaRequerimento();
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar o requerimento " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public String prepararParaConsultar() {
		return "/paginas/novo-requerimento/consulta.xhtml";
	}
	
	private void carregaMsgAberturaRequerimento() {
		try {

			String mensagem = new String("O requerimento foi aberto com sucesso. O número para acompanhamento é: "  + requerimento.getNumRequerimento()
					+ ". ------------------------------------ "
					+ "Você receberá por e-mail a relação de documentos para abertura do processo. Após o envio da documentação você receberá "
					+ "o boleto para pagamento da taxa ambiental.");

			ContextoUtil.getContexto().setObject(mensagem);
			FacesContext.getCurrentInstance().getExternalContext().redirect(prepararParaConsultar());
		} catch (IOException e) {
			JsfUtil.addErrorMessage("Erro ao redirecionar para página de consulta");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private Enquadramento gerarEnquadramento(AtoAmbiental atoAmbiental) throws Exception {
		Enquadramento enquadramento = new Enquadramento();
		enquadramento.setIdeRequerimento(this.requerimento);
		enquadramento.setIndEnquadramentoAprovado(true);
		enquadramento.setIndPassivelEiarima(false);
		enquadramento.setIdePessoa(this.pessoaService.carregarUsuarioSEIA());
		this.gerarEnquadramentoAtoAmbiental(atoAmbiental, enquadramento);
		this.gerarEnquadramentoDocumentoAto(atoAmbiental, enquadramento);

		return enquadramento;
	}
	
	private void gerarEnquadramentoDocumentoAto(AtoAmbiental atoAmbiental, Enquadramento enquadramento)
			throws Exception {
		enquadramento.setEnquadramentoDocumentoAtoCollection(new ArrayList<EnquadramentoDocumentoAto>());
		Collection<DocumentoAto> documentos = this.documentoAtoAmbientalService
				.listarDocumentoAtoByIdeAtoAmbiental(atoAmbiental.getIdeAtoAmbiental());
		for (DocumentoAto documentoAto : documentos) {
			documentoAto.setChecked(true);
			EnquadramentoDocumentoAto enquadramentoDocumentoAto = new EnquadramentoDocumentoAto();
			enquadramentoDocumentoAto.setEnquadramento(enquadramento);
			enquadramentoDocumentoAto.setDocumentoAto(documentoAto);
			enquadramento.getEnquadramentoDocumentoAtoCollection().add(enquadramentoDocumentoAto);
		}
	}
	
	private void gerarEnquadramentoAtoAmbiental(AtoAmbiental atoAmbiental, Enquadramento enquadramento) {
		enquadramento.setEnquadramentoAtoAmbientalCollection(new ArrayList<EnquadramentoAtoAmbiental>());

		EnquadramentoAtoAmbiental enquadramentoAtoAmbiental = new EnquadramentoAtoAmbiental(enquadramento,
				atoAmbiental, null);

		enquadramento.getEnquadramentoAtoAmbientalCollection().add(enquadramentoAtoAmbiental);
	}
	
	private void efetuarEnquadramentoAutomatico(AtoAmbientalEnum atoAmbientalEnum) throws Exception {
		this.tramitacaoRequerimentoService.tramitarAutomaticamente(requerimento, StatusRequerimentoEnum.SENDO_ENQUADRADO);

		AtoAmbiental atoAmbiental = this.atoAmbientalService.carregarById(atoAmbientalEnum.getId());

		Enquadramento enquadramento = gerarEnquadramento(atoAmbiental);

		this.enquadramentoService.salvar(enquadramento);

		if(!Util.isNullOuVazio(enquadramento.getEnquadramentoAtoAmbientalCollection()) && enquadramento.getEnquadramentoAtoAmbientalCollection().size() == 1) {
			documentoObrigatorioReqService.atualizarEnquadramentoDosDocumentosByRequerimento(enquadramento.getIdeRequerimento(), enquadramento.getEnquadramentoAtoAmbientalCollection().iterator().next());
		}

		this.enquadramentoService.salvarComunicacao(enquadramento);
	}
	
	private void adicionarTramitacao(StatusRequerimentoEnum status) {
		TramitacaoRequerimento tramitacaoRequerimento = new TramitacaoRequerimento();
		tramitacaoRequerimento.setIdePessoa(pessoa);
		StatusRequerimento statusRequeri = new StatusRequerimento();
		statusRequeri.setIdeStatusRequerimento(status.getStatus());
		tramitacaoRequerimento.setIdeStatusRequerimento(statusRequeri);
		requerimento.setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		requerimento.getTramitacaoRequerimentoCollection().add(tramitacaoRequerimento);

	}
	
	private boolean validarRazaoSocial() throws Exception {

		boolean valido = true;

		if(indiceTela == 4) {

			if (Util.isNullOuVazio(this.novaRazaoSocial)) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo com a Nova razão social.");
				valido = false;
			} else {
				if (!validaTamanhoString(this.novaRazaoSocial, 200)) {
					JsfUtil.addWarnMessage("A nova razão social só aceita 200 caracteres");
					valido = false;
				}
			}
		}

		return valido;
	}
	
	private void pessoasRequerimento() {
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

			List<ProcuradorPessoaFisica> collProcuradorPessoaFisica = requerimentoUnicoService.recuperarProcuradorPessoaFisicaAssinaturaObrigatoriaPorRequerenteEmpreendimento(pessoa, empreendimento);
			for (ProcuradorPessoaFisica procuradorPessoaFisica : collProcuradorPessoaFisica) {
				RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
				requerimentoPessoa.setPessoa(procuradorPessoaFisica.getIdeProcurador().getPessoa());
				TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
				tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
				requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);
				pessoasRequerimentos.add(requerimentoPessoa);
			}

			List<ProcuradorRepresentante> collProcuradorRepresentante = requerimentoUnicoService.recuperarProcuradorRepresentanteAssinaturaObrigatoriaPorRequerenteEmpreendimento(pessoa, empreendimento);
			for (ProcuradorRepresentante procuradorRepresentante : collProcuradorRepresentante) {
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

			List<RepresentanteLegal> collRepresentanteLegal = requerimentoUnicoService.recuperarRepresentanteLegalAssinaturaObrigatoriaPorRequerente(pessoa);
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
		}
	}
	
	/**
	 * @return boolean
	 * @author eduardo.fernandes
	 * @Commentarios Retorna false se o tamanho da variavel (String) exceder o
	 *               tamanho especificado
	 */
	public boolean validaTamanhoString(String string, int tamanho) {
		boolean valido = true;
		if (string.trim().length() > tamanho) {
			valido = false;
		}
		return valido;
	}
	
	private void montarListaTipoSolicitacao() {
		this.listaTipoSolicitacao = new ArrayList<TipoSolicitacaoDTO>();
		
		TipoSolicitacaoDTO tipo = new TipoSolicitacaoDTO();
		tipo.setCodigo(1L);
		tipo.setDescricao("Regularização Ambiental do empreendimento");
		
		this.listaTipoSolicitacao.add(tipo);
		
		tipo = new TipoSolicitacaoDTO();
		tipo.setCodigo(2L);
		tipo.setDescricao("Declaração de transporte de Resíduos Perigosos(DTRP)");
		
		this.listaTipoSolicitacao.add(tipo);
		
		tipo = new TipoSolicitacaoDTO();
		tipo.setCodigo(3L);
		tipo.setDescricao("Declaração de Inexigibilidade");
		
		this.listaTipoSolicitacao.add(tipo);
		
		if (isRequerentePJ()) {
			tipo = new TipoSolicitacaoDTO();
			tipo.setCodigo(4L);
			tipo.setDescricao("Alteração de Razão Social (ALRS)");
		
			this.listaTipoSolicitacao.add(tipo);
		}
		
		tipo = new TipoSolicitacaoDTO();
		tipo.setCodigo(5L);
		tipo.setDescricao("Cumprimento de reposição florestal");
		
		this.listaTipoSolicitacao.add(tipo);
	}
	
	public boolean isRequerentePJ(){
		return (!Util.isNull(getPessoa()) && !Util.isNull(getPessoa().getPessoaJuridica()));
	}
	
	public boolean isRequerentePF(){
		return (!Util.isNullOuVazio(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa().getPessoaFisica()));
	}
	
	public void salvarRequerimentoAposEndereco(ActionEvent event) {
		tipoSolicitacaoServiceFacade.salvarRequerimentoAposEndereco(event, this, identificarPapelController);
	}
	
	private boolean isRequerimentoDeRegulacaoAmbiental() {
		return isTipoRequerimentoNotNull() && tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getIde());
	}
	
	private boolean isRequerimentoDeCadastroDeImovel(){
		return isTipoRequerimentoNotNull() && tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REQUERIMENTO_DE_CADASTRO_DE_IMOVEL_RURAL.getIde());
	}
	
	public Boolean getSalvoParcialmente() {
		return !Util.isNull(requerimento) && !Util.isNull(requerimento.getIdeRequerimento()) && Util.isNull(requerimento.getNumRequerimento());
	}
	
	private boolean isTipoRequerimentoNotNull(){
		return !Util.isNull(tipoRequerimento);
	}
	
	public int getIndiceTela() {
		return indiceTela;
	}

	public void setIndiceTela(int indiceTela) {
		this.indiceTela = indiceTela;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public TipoRequerimento getTipoRequerimento() {
		return tipoRequerimento;
	}

	public void setTipoRequerimento(TipoRequerimento tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
	}

	public boolean isDadosContato() {
		return dadosContato;
	}

	public void setDadosContato(boolean dadosContato) {
		this.dadosContato = dadosContato;
	}

	public boolean isDesabilitarTudo() {
		return desabilitarTudo;
	}

	public void setDesabilitarTudo(boolean desabilitarTudo) {
		this.desabilitarTudo = desabilitarTudo;
	}

	public boolean isModoEdicao() {
		return modoEdicao;
	}

	public void setModoEdicao(boolean modoEdicao) {
		this.modoEdicao = modoEdicao;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public Requerimento getRequerimentoSelecionado() {
		return requerimentoSelecionado;
	}

	public void setRequerimentoSelecionado(Requerimento requerimentoSelecionado) {
		this.requerimentoSelecionado = requerimentoSelecionado;
	}

	public String getNovaRazaoSocial() {
		return novaRazaoSocial;
	}

	public void setNovaRazaoSocial(String novaRazaoSocial) {
		this.novaRazaoSocial = novaRazaoSocial;
	}

	public List<SolicitacaoAdministrativo> getListaSolicitacaoAdministrativo() {
		return listaSolicitacaoAdministrativo;
	}

	public void setListaSolicitacaoAdministrativo(
			List<SolicitacaoAdministrativo> listaSolicitacaoAdministrativo) {
		this.listaSolicitacaoAdministrativo = listaSolicitacaoAdministrativo;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public Boolean getEnderecoContato() {
		return enderecoContato;
	}

	public void setEnderecoContato(Boolean enderecoContato) {
		this.enderecoContato = enderecoContato;
	}

	public TipoSolicitacaoServiceFacade getTipoSolicitacaoServiceFacade() {
		return tipoSolicitacaoServiceFacade;
	}

	public void setTipoSolicitacaoServiceFacade(
			TipoSolicitacaoServiceFacade tipoSolicitacaoServiceFacade) {
		this.tipoSolicitacaoServiceFacade = tipoSolicitacaoServiceFacade;
	}

	public Boolean getMostraQuestionarioAposSalvar() {
		return mostraQuestionarioAposSalvar;
	}

	public void setMostraQuestionarioAposSalvar(Boolean mostraQuestionarioAposSalvar) {
		this.mostraQuestionarioAposSalvar = mostraQuestionarioAposSalvar;
	}

	public EmpreendimentoRequerimento getEmpreendimentoRequerimento() {
		return empreendimentoRequerimento;
	}

	public void setEmpreendimentoRequerimento(
			EmpreendimentoRequerimento empreendimentoRequerimento) {
		this.empreendimentoRequerimento = empreendimentoRequerimento;
	}

	public SolicitacaoAdministrativo getSolicitacaoAdministrativo() {
		return solicitacaoAdministrativo;
	}

	public void setSolicitacaoAdministrativo(
			SolicitacaoAdministrativo solicitacaoAdministrativo) {
		this.solicitacaoAdministrativo = solicitacaoAdministrativo;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public RequerimentoUnicoDTO getRequerimentoUnicoDTO() {
		return requerimentoUnicoDTO;
	}

	public void setRequerimentoUnicoDTO(RequerimentoUnicoDTO requerimentoUnicoDTO) {
		this.requerimentoUnicoDTO = requerimentoUnicoDTO;
	}

	public PerguntaRequerimento getPergunta0_1() {
		return pergunta0_1;
	}

	public void setPergunta0_1(PerguntaRequerimento pergunta0_1) {
		this.pergunta0_1 = pergunta0_1;
	}

	public List<TipoSolicitacaoDTO> getListaTipoSolicitacao() {
		return listaTipoSolicitacao;
	}

	public void setListaTipoSolicitacao(
			List<TipoSolicitacaoDTO> listaTipoSolicitacao) {
		this.listaTipoSolicitacao = listaTipoSolicitacao;
	}
	
	
}
