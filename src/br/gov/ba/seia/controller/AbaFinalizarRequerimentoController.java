package br.gov.ba.seia.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Classe;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.FaseEmpreendimento;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.ObjetoAlteracao;
import br.gov.ba.seia.entity.ParametroCalculo;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.PotencialPoluicao;
import br.gov.ba.seia.entity.ProgramaGoverno;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.RequerimentoTipologiaNR;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.ObjetoAlteracaoEnum;
import br.gov.ba.seia.enumerator.OpcaoEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.PorteEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.FinalizarRequerimentoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BiomaRequerimentoService;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.ClasseService;
import br.gov.ba.seia.service.EmpreendimentoRequerimentoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.FaseEmpreendimentoService;
import br.gov.ba.seia.service.LicencaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.NovoRequerimentoService;
import br.gov.ba.seia.service.ObjetoAlteracaoService;
import br.gov.ba.seia.service.ParametroCalculoService;
import br.gov.ba.seia.service.PorteService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.TipologiaGrupoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.service.UnidadeMedidaTipologiaGrupoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("abaFinalizarRequerimentoController")
@ViewScoped
public class AbaFinalizarRequerimentoController {

	@Inject
	private NovoRequerimentoController novoRequerimentoController;
	
	@Inject
	private TipologiaController tipologiaController;

	@EJB
	private RequerimentoService requerimentoService;

	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;

	@EJB
	private FaseEmpreendimentoService faseEmpreendimentoService;

	@EJB
	private ClasseService classePotencialService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@EJB
	private PorteService porteService;

	@EJB
	private NovoRequerimentoService novoRequerimentoService;

	@EJB
	private TipologiaService tipologiaService;

	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;

	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private CertificadoService certificadoService;

	@EJB
	private TipologiaGrupoService tipologiaGrupoService;
	
	@EJB
	private UnidadeMedidaTipologiaGrupoService unidadeMedidaTipologiaGrupoService;
	
	@EJB
	private EmpreendimentoRequerimentoService empreendimentoRequerimentoService;
	
	@EJB
	private ParametroCalculoService parametroCalculoService;
	
	@EJB
	private LicencaService licencaService;
	
	@EJB
	private ObjetoAlteracaoService objetoAlteracaoService;
	
	@EJB
	private FinalizarRequerimentoServiceFacade finalizarRequerimentoServiceFacade;
	
	@EJB
	private BiomaRequerimentoService biomaRequerimentoService;
	
	@EJB
	private MunicipioService municipioService;
	
	/**
	 * OBJETOS
	 */
	
	private DataModel<RequerimentoTipologiaNR> requerimentoTipologiaData;

	private Boolean tipologiaPrincipal;

	private Tipologia atividadePrincipal;
	
	private Tipologia atividadeAExcluir;

	private Porte porteIdentificado;

	private FaseEmpreendimento faseEmpreendimentoSelecionado;
	private List<FaseEmpreendimento> listaFaseEmpreendimento;

	private EmpreendimentoRequerimento empreendimentoRequerimento;

	private Date dtcPrevistaFaseEmpreendimento;

	private Collection<Tipologia> atividadesLicenca;  
	
	private Collection<Tipologia> atividadesAutorizacao;
	
	private Collection<Tipologia> tipologiasPrincipais;
	
	private Collection<Tipologia> atividadesLicencaEmprendSelected;
	
	private Collection<Tipologia> atividadesEmpreendimento;
	
	private Collection<Tipologia> atividadesLicencaParaCalculo = new ArrayList<Tipologia>();
	
	private Tipologia tipologiaEmpreendSelecionada;

	private boolean escolherTipologiaPrincipal;
	
	private boolean isAutorizacaoEspecial;

	private boolean isEmpreend;

	private Classe classe;

	private boolean temLicencaComValorVazio;

	private boolean isPorteCalculado = false;

	@PostConstruct
	public void init() {

		try {
			escolherTipologiaPrincipal = false;

			tipologiasPrincipais = new ArrayList<Tipologia>();

			atividadesAutorizacao = new ArrayList<Tipologia>();

			if (Util.isNullOuVazio(empreendimentoRequerimento)) {
				
				empreendimentoRequerimento = new EmpreendimentoRequerimento();
				
				if (isModoVisualizacao() || isModoEdicao()) {
					empreendimentoRequerimento = novoRequerimentoController.getEmpreendimentoRequerimento();
					faseEmpreendimentoSelecionado = empreendimentoRequerimento.getIdeFaseEmpreendimento();
					dtcPrevistaFaseEmpreendimento = empreendimentoRequerimento.getDtcFaseEmpreendimento();
				}
			}

			carregarAtividades();
			
			calcularPorteInit();
			
			vincularAtividadePrincipal(atividadesLicencaParaCalculo);
			
			if(Util.isNullOuVazio(atividadePrincipal) && atividadesLicencaParaCalculo != null){
				verificarTipologiaPrincipal(atividadesLicencaParaCalculo);
			}
			
			listarFaseEmpreendimento();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	public boolean isRenderedPnlAutorizacaoEspecial() {
		return isAutorizacaoEspecial;
	}
	


	private void carregarAtividadesDeAutorizacao() {
		atividadesAutorizacao = requerimentoTipologiaService.buscarTipologiasAutorizacao(novoRequerimentoController.getRequerimento());
	}

	private void vincularAtividadePrincipal(Collection<Tipologia> atividadesDoRequerimento) {
		this.atividadePrincipal = new Tipologia();
		for (Tipologia licenca : atividadesDoRequerimento) {
			if(licenca.isIndPrincipal()){
				this.atividadePrincipal = licenca;
			}
		}
	}
	
	private Collection<Tipologia> montarListaAuxiliarByPrincipal(Collection<Tipologia> listaTemporaria, Collection<Tipologia> listaOriginal){
		if(Util.isNull(listaTemporaria)){
			listaTemporaria = new ArrayList<Tipologia>();
		} 
		else if(!Util.isNullOuVazio(listaTemporaria)){
			listaTemporaria.clear();
		}
		listaTemporaria.addAll(listaOriginal);
		return listaTemporaria;
	}

	private void carregarAtividades()  {
		Requerimento requerimento = novoRequerimentoController.getRequerimento();
		Empreendimento empreendimento = novoRequerimentoController.getEmpreendimento();
		TramitacaoRequerimento ultimaTramitacao = finalizarRequerimentoServiceFacade.buscarUltimaTramitacaoPorRequerimento(requerimento.getIdeRequerimento());
		
		atividadesLicenca = new ArrayList<Tipologia>();
		atividadesLicencaEmprendSelected = new ArrayList<Tipologia>();
		Collection<Tipologia> listaTipologiaTemporaria = new ArrayList<Tipologia>();
		
		if(isModoVisualizacao() && !Util.isNullOuVazio(ultimaTramitacao) && !Util.isNullOuVazio(ultimaTramitacao.getIdeStatusRequerimento())
				&& ultimaTramitacao.getIdeStatusRequerimento().getIdeStatusRequerimento() != StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO.getStatus()
				&& ultimaTramitacao.getIdeStatusRequerimento().getIdeStatusRequerimento() > StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus()) {
			
			atividadesEmpreendimento = empreendimentoService.buscarTipologias(empreendimento, false, false);
		} else {
			atividadesEmpreendimento = empreendimentoService.buscarTipologias(empreendimento, true, true);
		}
		
		for(Tipologia t : atividadesEmpreendimento){
			if(t.isTipologiaEspecial()) {
				isAutorizacaoEspecial = true 
				&& !Util.isNull(novoRequerimentoController.getAbaLicencaAutorizacaoController().getPerguntaNR_A3_P11().getIndResposta())
				&& novoRequerimentoController.getAbaLicencaAutorizacaoController().getPerguntaNR_A3_P11().getIndResposta();
				
			} else if(!t.isTipologiaEspecial()) {
				listaTipologiaTemporaria.add(t);
			}
		}
		
		if(!isAutorizacaoEspecial) atividadesEmpreendimento = listaTipologiaTemporaria;
		
		atividadesAutorizacao = requerimentoTipologiaService.buscarTipologiasAutorizacao(requerimento);
		
		listaTipologiaTemporaria = null;
		
		if(!Util.isNullOuVazio(atividadesEmpreendimento)){
			if(isModoVisualizacao()){ // Se não é um requerimento NOVO, carregar as Tipologias de RequerimentoTipologia.
				if(!Util.isNullOuVazio(ultimaTramitacao) && !Util.isNullOuVazio(ultimaTramitacao.getIdeStatusRequerimento())
						&& ultimaTramitacao.getIdeStatusRequerimento().getIdeStatusRequerimento() != StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO.getStatus()
						&& ultimaTramitacao.getIdeStatusRequerimento().getIdeStatusRequerimento() > StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus()) {
				
					atividadesLicenca = requerimentoTipologiaService.buscarTipologiasLicenca(requerimento, false, false);
				} else {
					atividadesLicenca = requerimentoTipologiaService.buscarTipologiasLicenca(requerimento, false, false);
				}
			} else if(isModoEdicao()){ // Se for EDIÇÃO, é preciso verificar se houve alteração no cadastro das Atividades (Tipologias) do Empreendimento.
				atividadesLicenca = requerimentoTipologiaService.buscarTipologiasLicenca(requerimento, true, true);
				
				boolean isExistemTipologiasDiferentes = !atividadesEmpreendimento.containsAll(atividadesLicenca) || !atividadesLicenca.containsAll(atividadesEmpreendimento);
				
				if(isExistemTipologiasDiferentes){
					// Caso exista mais Atividade no Empreendimento
					if(atividadesEmpreendimento.size() > atividadesLicenca.size()){
						for(Tipologia tipologia : atividadesEmpreendimento){
							if(!atividadesLicenca.contains(tipologia)){
								atividadesLicenca.add(tipologia);
							}
						}
					} 
					// Caso exista a mesma quantidade de Tipologias
					else if(atividadesEmpreendimento.size() == atividadesLicenca.size()){
						atividadesLicenca = montarListaAuxiliarByPrincipal(atividadesLicenca, atividadesEmpreendimento);
					} 
					// Caso exista mais Atividade no Requerimento
					else {
						listaTipologiaTemporaria = montarListaAuxiliarByPrincipal(listaTipologiaTemporaria, atividadesLicenca);
						for(Tipologia tipologia : listaTipologiaTemporaria){
							if(!atividadesEmpreendimento.contains(tipologia)){
								atividadesLicenca.remove(tipologia);
							}
						}
					}
				}
			} else { // Para Requerimento NOVO, a lista de Tipologias deve ser preenchida conforme cadastrado no Empreendimento.
				atividadesLicenca.addAll(atividadesEmpreendimento);
			}
			
			// Caso o requerente solicite uma Alteração de Licença, o sistema deverá montar uma segunda lista de Tipologia.
			if(isAlteracaoLicenca()){
				listaTipologiaTemporaria = montarListaAuxiliarByPrincipal(listaTipologiaTemporaria, atividadesLicenca);
				for (Tipologia atividade : listaTipologiaTemporaria) {
					if(atividadesEmpreendimento.contains(atividade)){
						if(!isModoVisualizacao()){
							atividadesEmpreendimento.remove(atividade);
						}
						atividadesLicenca.remove(atividade);
						atividadesLicencaEmprendSelected.add(atividade);
					}
				}				
			}
		}
		
		//ajusta a APE
		if (isAutorizacaoEspecial) {
			for (Iterator iterator = atividadesLicenca.iterator(); iterator.hasNext();) {
				Tipologia tipologia = (Tipologia) iterator.next();
				
				if(!tipologia.isTipologiaEspecial()) {
					iterator.remove();
				}
			}
		}
		
		this.carregarAtividadesDeAutorizacao();

	}

	private boolean isModoEdicao() {
		return novoRequerimentoController.isModoEdicao();
	}

	private boolean isModoVisualizacao() {
		return !Util.isNull(novoRequerimentoController.getRequerimentoDTO()) && novoRequerimentoController.getRequerimentoDTO().isVisualizar();
	}

	private void salvarRequerimentoTipologia(Tipologia tipologia) throws Exception {
		
		RequerimentoTipologia requerimentoTipologia = gerarRequerimentoTipologia(tipologia);
		
		if(!Util.isNullOuVazio(requerimentoTipologia.getIdeRequerimento()) && 
				!Util.isNullOuVazio(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo()) 
				&& !Util.isNullOuVazio(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeUnidadeMedidaTipologiaGrupo())) {
			
			this.requerimentoTipologiaService.salvarOuAtualizar(requerimentoTipologia);
			
		} else {
			throw new RuntimeException("Não foi possível salvar a tipologia, favor consultar a central de  atendimento.");
		}
	}
	
	public void excluirAtividade(){
		try {
			
			RequerimentoTipologia requerimentoTipologia = gerarRequerimentoTipologia(this.atividadeAExcluir);
			
			if(!Util.isNullOuVazio(requerimentoTipologia) && Util.isNull(requerimentoTipologia.getIdeRequerimentoTipologia())) {
				//provavelmente esta faltando tipologia grupo ou unidade medida tipologi grupo 
				JsfUtil.addErrorMessage("Não foi possível excluir a tipologia, favor consultar a central de  atendimento.");
	
			}
			else {
				this.requerimentoTipologiaService.remover(requerimentoTipologia);
				this.atividadesAutorizacao.remove(this.atividadeAExcluir);
				
				if(tipologiasPrincipais.contains(this.atividadeAExcluir)) {
					tipologiasPrincipais.remove(this.atividadeAExcluir);
				}
				
				porteIdentificado = null;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private RequerimentoTipologia gerarRequerimentoTipologia(Tipologia tipologia) throws Exception {
		
		RequerimentoTipologia requerimentoTipologia = new RequerimentoTipologia();
		requerimentoTipologia.setIdeRequerimento(this.novoRequerimentoController.getRequerimento());
		
		if(Util.isNullOuVazio(tipologia.getTipologiaGrupo())){
			tipologia.setTipologiaGrupo(tipologiaGrupoService.carregarTipologiaGrupoTipologiaAtivo(tipologia));
		}
		
		if (!Util.isNullOuVazio(tipologia) && 
			!Util.isNullOuVazio(tipologia.getTipologiaGrupo()) &&
			!Util.isNullOuVazio(tipologia.getTipologiaGrupo().getUnidadeMedidaTipologiaGrupo())) {
			requerimentoTipologia.setIdeUnidadeMedidaTipologiaGrupo(tipologia.getTipologiaGrupo().getUnidadeMedidaTipologiaGrupo());
		} else if(Util.isNullOuVazio(tipologia.getTipologiaGrupo())
				|| Util.isNullOuVazio(tipologia.getTipologiaGrupo().getUnidadeMedidaTipologiaGrupo())){
			requerimentoTipologia.setIdeUnidadeMedidaTipologiaGrupo(unidadeMedidaTipologiaGrupoService.buscarPorTipologia(tipologia));
		}
		
		if (!Util.isNullOuVazio(tipologia.getValAtividade()) && !tipologia.getValorAtividade().equals(0.00)) {
			requerimentoTipologia.setValAtividade(tipologia.getValorAtividade());
		}
		
		requerimentoTipologia.setIndTipologiaPrincipal(tipologia.isIndPrincipal());
		
		if(tipologia.getOpcao()!= null){
			requerimentoTipologia.setIndAcaoTipologia(tipologia.getOpcao().getId());
		}else{
			requerimentoTipologia.setIndAcaoTipologia(null);
		}
		

		this.carregarId(requerimentoTipologia);
		return requerimentoTipologia;
	}

	private void carregarId(RequerimentoTipologia requerimentoTipologia) {
		
		RequerimentoTipologia requerimentoTipologiaSalvo = null;
		
		if(!Util.isNullOuVazio(requerimentoTipologia.getIdeRequerimento()) 
				&& !Util.isNullOuVazio(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo()) 
				&& !Util.isNullOuVazio(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeUnidadeMedidaTipologiaGrupo())) {
			
			requerimentoTipologiaSalvo = this.requerimentoTipologiaService.carregarRequerimentoTipologia(requerimentoTipologia);
		}
			
		if(!Util.isNull(requerimentoTipologiaSalvo)){
			requerimentoTipologia.setIdeRequerimentoTipologia(requerimentoTipologiaSalvo.getIdeRequerimentoTipologia());
		}
	}
	
	/**
	 * @author eduardo.fernandes
	 * @return listaFaseEmpreendimento
	 */
	public List<FaseEmpreendimento> listarFaseEmpreendimento() {
		try {
			if (Util.isNullOuVazio(listaFaseEmpreendimento)) {
				listaFaseEmpreendimento = new ArrayList<FaseEmpreendimento>();
			}
			listaFaseEmpreendimento = faseEmpreendimentoService.listarFaseEmpreendimento();
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao carrega a lista de Fases do Empreendimento:" + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return listaFaseEmpreendimento;
	}

	public void changeEmpreendimentos(ValueChangeEvent vEvent) {
		faseEmpreendimentoSelecionado = (FaseEmpreendimento) vEvent.getNewValue();
	}

	/**
	 * Método chamado para finalizarNovoRequerimento
	 * 
	 * @author eduardo.fernandes
	 */
	public void finalizarNovoRequerimento() {
		try {
			
			boolean existeSolicitacao;
			boolean podeFinalizar = false;
			
			if (novoRequerimentoController.validarAbas()) {
				
				existeSolicitacao = novoRequerimentoController.existeSolicitacao();
								
				for (Tipologia atividade : atividadesLicenca) {
					if(!Util.isNullOuVazio(atividade.getValAtividadeFormatada()) && !this.isPorteCalculado && 
							novoRequerimentoController.getAbaLicencaAutorizacaoController().getPerguntaNR_A3_P1().getIndResposta()){
						JsfUtil.addWarnMessage("O cálculo do porte é obrigatório.");
						return;
					}
				}
				
				if(isAutorizacaoEspecial) {
					if (validarAPE()) {
						Requerimento requerimento = carregarRequerimento();
						finalizarRequerimentoServiceFacade.finalizarRequerimentoAutorizacaoEspecial(requerimento,atividadesLicenca);
					}
				}
				else if(isProjetoLicenciadoComImpactoAdicional()) {
					
					if (validarFinalizarRequerimentoQuandoNaoHaObrigatoriedade() && existeSolicitacao) {
						podeFinalizar = true;
					}
					
				}
				else if(this.getAutorizacaoOuLicenca()) {
					if(naoPreencheuNadaDasLicencasDoEmpreendimento()){
						if(!atividadesAutorizacao.isEmpty()){
							if(existeSolicitacao && validacaoMinimaRequerimentoParaAutoriOuLic()) {
								podeFinalizar = true;
							}
						}
						else {
							JsfUtil.addWarnMessage("Informe a caracterização do empreendimento.");
							return;		
						}						
					}
					else if(existeSolicitacao && !validacaoMinimaRequerimentoParaAutoriOuLic()) {
						return;
					} 
					else if(isEnquadramentoAutomaticoDIAP()){
						salvarLicencas(atividadesLicencaParaCalculo);
						finalizarRequerimentoServiceFacade.finalizarRequerimentoAtoDeclaratorioDIAP(novoRequerimentoController.getRequerimento(), atividadesLicenca);
					}
					else {
						podeFinalizar = true;
					}
				}else if(isEnquadramentoAutomaticoAMC()){
					salvarLicencas(atividadesLicencaParaCalculo);
					finalizarRequerimentoServiceFacade.finalizarRequerimentoAtoDeclaratorioAMC(novoRequerimentoController.getRequerimento(), atividadesLicenca);
				}
				else if (validarFinalizarRequerimento() && existeSolicitacao) {
					podeFinalizar = true;
					
				}
				else if(!existeSolicitacao){
					JsfUtil.addErrorMessage("Não foi possível formar o requerimento, pois não foi solicitado nenhum ato autorizativo.");
				}
			}			
			
			
			if(podeFinalizar){
				acoesFinalizarNovoRequerimento();
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao Finalizar o Requerimento.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean finalizarEdicaoReenquadramento() {
		try {
			
			boolean existeSolicitacao;
			boolean podeFinalizar = false;
			
			if (novoRequerimentoController.validarAbas()) {
				
				existeSolicitacao = novoRequerimentoController.existeSolicitacao();
				
				for (Tipologia atividade : atividadesLicenca) {
					if(!Util.isNullOuVazio(atividade.getValAtividadeFormatada()) && !this.isPorteCalculado && 
							novoRequerimentoController.getAbaLicencaAutorizacaoController().getPerguntaNR_A3_P1().getIndResposta()){
						JsfUtil.addWarnMessage("O cálculo do porte é obrigatório.");
						//MensagemUtil.alerta("O cálculo do porte é obrigatório.");
						return podeFinalizar;
					}
				}
				if(!novoRequerimentoController.isDeclaracao()) {
					MensagemUtil.alerta("Para finalização, é necessário marcar o aceite da declaração.");
					return podeFinalizar;
				}
				if(isAutorizacaoEspecial) {
					MensagemUtil.alerta("Para o processo de reenquadramento não é permitido solicitar uma autorização por procedimento especial.");
				}
				else if(isProjetoLicenciadoComImpactoAdicional()) {
					
					if (validarFinalizarRequerimentoQuandoNaoHaObrigatoriedade() && existeSolicitacao) {
						podeFinalizar = true;
					}
					
				}
				else if(this.getAutorizacaoOuLicenca()) {
					if(naoPreencheuNadaDasLicencasDoEmpreendimento()){
						if(!atividadesAutorizacao.isEmpty()){
							if(existeSolicitacao && validacaoMinimaRequerimentoParaAutoriOuLic()) {
								podeFinalizar = true;
							}
						}
						else {
							MensagemUtil.alerta("Informe a caracterização do empreendimento.");
							return podeFinalizar;		
						}						
					} 
					else if(isEnquadramentoAutomaticoDIAP()){
						MensagemUtil.alerta("Para o processo de reenquadramento não é permitido solicitar uma autorização por procedimento especial.");
					} 
					else {
						podeFinalizar = true;
					}
				}else if(isEnquadramentoAutomaticoAMC()){
					salvarLicencas(atividadesLicencaParaCalculo);
					finalizarRequerimentoServiceFacade.finalizarReenquadramentoAtoDeclaratorioAMC(novoRequerimentoController.getRequerimento(), atividadesLicenca);
				}
				else if (validarFinalizarRequerimento() && existeSolicitacao) {
					podeFinalizar = true;
					
				}
				else if(!existeSolicitacao){
					MensagemUtil.alerta("Não foi possível finalizar a edição do requerimento, pois não foi solicitado nenhum ato autorizativo.");
				}
			}			
			
			if(podeFinalizar){
				acoesFinalizarEdicaoReenquadramento();	
			}
			return podeFinalizar;
		} 
		catch (Exception e) {
			e.printStackTrace();
			JsfUtil.addErrorMessage("Erro ao Finalizar o Requerimento.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean validarAPE() {
		
		Boolean retorno = true;
		
		if(!Util.isNullOuVazio(this.atividadesLicenca)){
			for (Tipologia tipologia : this.atividadesLicenca) {
				if(Util.isNullOuVazio(tipologia.getValAtividade()) || (!Util.isNullOuVazio(tipologia.getValAtividade()) && tipologia.getValAtividade().equals("0,00"))) {
					JsfUtil.addWarnMessage("Por favor, informar o valor de todas as atividades de licença.");
					retorno = false;
					break;
				}
				
				if (TipologiaEnum.SILVICULTURA_PSS_ATE_200_HA.getId() == tipologia.getIdeTipologia()) {
					if (tipologia.getValorAtividade().compareTo(new BigDecimal("200", new MathContext(0))) == 1) {
						JsfUtil.addWarnMessage("Não é possível cadastrar área superior a 200ha para atividade silvicultura vinculada a PSS.");
						retorno = false;
						break;
					}
				}
			}
		}
		
		if (!novoRequerimentoController.isDeclaracao() || !novoRequerimentoController.isDeclaracaoAutorizacaoEspecial()) {
			JsfUtil.addWarnMessage("O campo Declaração é de preenchimento obrigatório.");
			retorno = false;
		}
		
		return retorno;
	}
	
	public void validarCaracterizacaoAtividadesEmpreendimento() {
		if(naoPreencheuNadaDasLicencasDoEmpreendimento()) {
			try {
				this.porteIdentificado = porteService.buscarPortePorId(PorteEnum.NAO_IDENTIFICADO.getId());
				this.empreendimentoRequerimento.setIdePorte(new Porte(PorteEnum.NAO_IDENTIFICADO.getId()));
				this.empreendimentoRequerimento.setIndDla(false);
				this.empreendimentoRequerimento.setIdeClasse(new Classe(1));
				this.atividadePrincipal = new Tipologia();
			} 
			catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
		
		classe = null;
		empreendimentoRequerimento.setIdeClasse(null);
		this.isPorteCalculado = false;
	}
	
	private boolean naoPreencheuNadaDasLicencasDoEmpreendimento(){
		
		boolean nadaPreenchido = true;
		
		if(!Util.isNullOuVazio(this.atividadesLicenca)){
			
			for (Tipologia tipologia : this.atividadesLicenca) {
				
				if(!Util.isNull(tipologia.getOpcao())){
					nadaPreenchido = false;
					break;
				}
				
				if(!Util.isNullOuVazio(tipologia.getValAtividade()) && !tipologia.getValAtividade().equals("0,00")){
					nadaPreenchido = false;
					break;
				}
				
				if(!Util.isNullOuVazio(tipologia.getValAtividade()) && tipologia.getValAtividade().equals("0,00")){
					temLicencaComValorVazio = true;
					break;
				}
				
			}
		}
		
		return nadaPreenchido;
	}
	
	private boolean isTipologiaDoEmpreendimentoXorY(){
		try {
			return requerimentoUnicoService.isTipologiaDoEmpreendimentoXouY(novoRequerimentoController.getEmpreendimento());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}
	
	private boolean validacaoMinimaRequerimentoParaAutoriOuLic(){
		boolean valido = true;
		
		if(!novoRequerimentoController.isDeclaracao()){
			JsfUtil.addWarnMessage("O campo Declaração é de preenchimento obrigatório.");
			valido = false;
		}
		if(!naoPreencheuNadaDasLicencasDoEmpreendimento() && temLicencaComValorVazio){
			JsfUtil.addWarnMessage("Por favor, informar o valor de todas as atividades de licença.");
			valido = false;
		}else{
			if(Util.isNullOuVazio(classe) && !isTipologiaDoEmpreendimentoXorY() && necessarioCalcularPorte()){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, realize o cálculo do porte.");
			} else if (!Util.isNullOuVazio(classe)){
				if(Util.isNullOuVazio(faseEmpreendimentoSelecionado)) {
					valido = false;
					JsfUtil.addWarnMessage("Por favor, marque uma opção no campo 3.");
				} else {
					if (Util.isNullOuVazio(dtcPrevistaFaseEmpreendimento)) {
						valido = false;
						JsfUtil.addWarnMessage("Por favor, selecione uma data no campo 3.1.");
					}
				} 	
			}
		}
		return valido;
	}
	
	public void acoesFinalizarNovoRequerimento() throws Exception, IOException {
		
		Requerimento requerimento = carregarRequerimento();
				
		if(this.isAlteracaoLicenca() || this.getAutorizacaoOuLicenca() || this.isRenovacaoLicenca()){
			
			boolean indDla = false;
			
			if(!Util.isNull(this.porteIdentificado)) {
				
				indDla = this.porteIdentificado.isNi();
				
				// Solicitação de suspensão de DLA's no município de Maraú - ticket 36388
				Municipio isMarau = municipioService.getMunicipioByEmpreendimento(empreendimentoRequerimento.getIdeEmpreendimento().clone());
				if (isMarau.getIdeMunicipio().equals(552)) {
					indDla = false;
				}
				
			}
			
			if(this.isAlteracaoLicenca() || this.isRenovacaoLicenca() || isEmpreendimentoOutorgaOuAtosFlorestais()) {
				indDla = false;
			}
			
			if(temLicencaComValorVazio && this.getAutorizacaoOuLicenca()){
				this.empreendimentoRequerimento.setIdePorte(new Porte(PorteEnum.NAO_IDENTIFICADO.getId()));
				this.empreendimentoRequerimento.setIndDla(false);
				this.empreendimentoRequerimento.setIdeClasse(new Classe(1));
				this.atividadePrincipal = new Tipologia();
				
			} else{
				this.empreendimentoRequerimento.setIdePorte(this.porteIdentificado);
				this.empreendimentoRequerimento.setIdeClasse(this.classe);
				if(naoPreencheuNadaDasLicencasDoEmpreendimento()) {	
					indDla = false;
				}
				this.empreendimentoRequerimento.setIndDla(indDla);
			}

			salvarLicencas(atividadesLicencaParaCalculo);
	
			if(isAlteracaoLicenca()) {
				
				Licenca lic = licencaService.getLicencaByIdeRequerimentoTipoAlteracao(requerimento);
				List<ObjetoAlteracao> loa = (List<ObjetoAlteracao>) objetoAlteracaoService.ListarObjetoAlteracaoByLicenca(lic);
				
				if(!Util.isNullOuVazio(loa) && loa.size() == 1){
					if(loa.get(0).getIdeObjetoAlteracao().equals(ObjetoAlteracaoEnum.SUBSTITUICAO.getId())) {
						List<ParametroCalculo> listParametro = parametroCalculoService.obterParametroCalculoPorAto(AtoAmbientalEnum.LA.getId());
						
						if(!Util.isNullOuVazio(listParametro)) {
							classe = listParametro.get(0).getIdeClasse();
							empreendimentoRequerimento.setIdeClasse(classe);
						}
					}
				}
			} else {
				definirClasseDoEmpreendimento();
				defineClasseQuandoPorteNaoIdentificado();
			}
		} else{
			this.empreendimentoRequerimento.setIdePorte(null);
			this.empreendimentoRequerimento.setIndDla(false);
			this.empreendimentoRequerimento.setIdeClasse(new Classe(1));
		}
		
		boolean todasAsTipologiasComValorZero = true;
		if(!atividadesAutorizacao.isEmpty() &&  !atividadesLicenca.isEmpty()){
			for (Tipologia atividade : atividadesLicenca) {
				if(!Util.isNullOuVazio(atividade.getValorAtividade()) ){
					todasAsTipologiasComValorZero = false;
					break;
				}
			}
			if(todasAsTipologiasComValorZero){
				empreendimentoRequerimento.setIsDeveSalvarClasseNulo(Boolean.TRUE);
			}
		}
		
		
		if (tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(
				requerimento.getIdeRequerimento()).getIdeStatusRequerimento().getIdeStatusRequerimento() 
					==	StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO.getStatus()) {
			
			if(Util.isNullOuVazio(requerimento.getNumRequerimento())) requerimentoUnicoService.geraNumeroRequerimento(requerimento);
			
			requerimento.setDtcFinalizacao(new Date());
			
			requerimentoService.inserirRequerimento(requerimento);
			
			this.tramitacaoRequerimentoService.tramitar(requerimento, StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO , this.getOperador());
			
			ContextoUtil.getContexto().setObject( "O requerimento foi aberto com sucesso. O número para acompanhamento é: "
							+ requerimento.getNumRequerimento() + "." + " ------------------------------------ "
							+ "Você receberá por e-mail a relação de documentos para abertura do processo. Após o envio da documentação você receberá "
							+ "o boleto para pagamento da taxa ambiental.");
		}else{
			
			if(	tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(requerimento.getIdeRequerimento()).getIdeStatusRequerimento().getIdeStatusRequerimento()
					==	StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO.getStatus()){
			
				this.tramitacaoRequerimentoService.tramitar(requerimento, StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO , this.getOperador());
			}
						
			ContextoUtil.getContexto().setObject("Requerimento nº "+ requerimento.getNumRequerimento()+ ", salvo com sucesso!");
		}
		
		empreendimentoRequerimento.setIdeFaseEmpreendimento(faseEmpreendimentoSelecionado);
		empreendimentoRequerimento.setDtcFaseEmpreendimento(dtcPrevistaFaseEmpreendimento);
		
		requerimentoService.atualizarEmpreendRequerimento(empreendimentoRequerimento);
		biomaRequerimentoService.gerarListaBiomaRequerimentoPelaEtapa7(requerimento);
			
		FacesContext.getCurrentInstance().getExternalContext().redirect(novoRequerimentoController.prepararParaConsultar());
	}
	
	public void acoesFinalizarEdicaoReenquadramento() throws Exception {
		
		Requerimento requerimento = carregarRequerimento();
		
		if(this.isAlteracaoLicenca() || this.getAutorizacaoOuLicenca() || this.isRenovacaoLicenca()){
			
			boolean indDla = false;
			
			
			if(!Util.isNull(this.porteIdentificado)) {
				indDla = this.porteIdentificado.isNi();
			}
			
			if(this.isAlteracaoLicenca() || this.isRenovacaoLicenca() || isEmpreendimentoOutorgaOuAtosFlorestais()) {
				indDla = false;
			}
			
			if(temLicencaComValorVazio && this.getAutorizacaoOuLicenca()){
				this.empreendimentoRequerimento.setIdePorte(new Porte(PorteEnum.NAO_IDENTIFICADO.getId()));
				this.empreendimentoRequerimento.setIndDla(false);
				this.empreendimentoRequerimento.setIdeClasse(new Classe(1));
				this.atividadePrincipal = new Tipologia();
				
			} else{
				this.empreendimentoRequerimento.setIdePorte(this.porteIdentificado);
				this.empreendimentoRequerimento.setIdeClasse(this.classe);
				if(naoPreencheuNadaDasLicencasDoEmpreendimento()) {	
					indDla = false;
				}
				this.empreendimentoRequerimento.setIndDla(indDla);
			}
			
			salvarLicencas(atividadesLicencaParaCalculo);
			
			if(isAlteracaoLicenca()) {
				
				Licenca lic = licencaService.getLicencaByIdeRequerimentoTipoAlteracao(requerimento);
				List<ObjetoAlteracao> loa = (List<ObjetoAlteracao>) objetoAlteracaoService.ListarObjetoAlteracaoByLicenca(lic);
				
				if(!Util.isNullOuVazio(loa) && loa.size() == 1){
					if(loa.get(0).getIdeObjetoAlteracao().equals(ObjetoAlteracaoEnum.SUBSTITUICAO.getId())) {
						List<ParametroCalculo> listParametro = parametroCalculoService.obterParametroCalculoPorAto(AtoAmbientalEnum.LA.getId());
						
						if(!Util.isNullOuVazio(listParametro)) {
							classe = listParametro.get(0).getIdeClasse();
							empreendimentoRequerimento.setIdeClasse(classe);
						}
					}
				}
			} else {
				definirClasseDoEmpreendimento();
				defineClasseQuandoPorteNaoIdentificado();
			}
		} else{
			this.empreendimentoRequerimento.setIdePorte(null);
			this.empreendimentoRequerimento.setIndDla(false);
			this.empreendimentoRequerimento.setIdeClasse(new Classe(1));
		}
		
		boolean todasAsTipologiasComValorZero = true;
		if(!atividadesAutorizacao.isEmpty() &&  !atividadesLicenca.isEmpty()){
			for (Tipologia atividade : atividadesLicenca) {
				if(!Util.isNullOuVazio(atividade.getValorAtividade()) ){
					todasAsTipologiasComValorZero = false;
					break;
				}
			}
			if(todasAsTipologiasComValorZero){
				empreendimentoRequerimento.setIsDeveSalvarClasseNulo(Boolean.TRUE);
			}
		}
		
		empreendimentoRequerimento.setIdeFaseEmpreendimento(faseEmpreendimentoSelecionado);
		empreendimentoRequerimento.setDtcFaseEmpreendimento(dtcPrevistaFaseEmpreendimento);
		
		requerimentoService.atualizarEmpreendRequerimento(empreendimentoRequerimento);
		
	}


	private Requerimento carregarRequerimento() {
		Requerimento requerimento = novoRequerimentoController.getRequerimento();		
		requerimentoTipologiaService.removerTipologiasNaoContidasEm(requerimento, atividadesLicencaParaCalculo);
		
		if (Util.isNullOuVazio(requerimento.getIdeTipoRequerimento())) {
			requerimento.setIdeTipoRequerimento(new TipoRequerimento(TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getIde()));
		}
		
		if (Util.isNullOuVazio(requerimento.getIdeOrgao().getDscSiglaOrgao())) {
			requerimento.setIdeOrgao(requerimentoUnicoService.recuperarOrgao(requerimento.getIdeOrgao()));
		}
		
		Endereco enderecoContato = ContextoUtil.getContexto().getRequerimentoEndereco();
		if(!Util.isNullOuVazio(enderecoContato)){
			requerimento.setIdeEnderecoContato(enderecoContato);
		}
		
		EmpreendimentoRequerimento empreendimentoSalvo = empreendimentoService.buscarEmpreendimentoRequerimento(novoRequerimentoController.getRequerimento(), novoRequerimentoController.getEmpreendimento());

		if (Util.isNullOuVazio(empreendimentoSalvo.getProgramaGoverno())){
			ProgramaGoverno programaGoverno = empreendimentoRequerimento.getProgramaGoverno();
			empreendimentoRequerimento = empreendimentoSalvo;
			empreendimentoRequerimento.setProgramaGoverno(programaGoverno);
		}
		else{
			empreendimentoRequerimento = empreendimentoSalvo;
		}
		
		if (Util.isNullOuVazio(empreendimentoRequerimento) && !novoRequerimentoController.isModoEdicao()) {
			empreendimentoRequerimento.setIdeEmpreendimento(novoRequerimentoController.getEmpreendimento());
			empreendimentoRequerimento.setIdeRequerimento(requerimento);
		}
		return requerimento;
	}

	private void definirClasseDoEmpreendimento() throws Exception {
		classe = null;
		for (Tipologia tipologia : atividadesLicencaParaCalculo) {
			Tipologia tipologiaPP = this.tipologiaService.carregarTipologia(tipologia.getIdeTipologia());
			
			Classe maiorClasse = null;
			PotencialPoluicao potencialPoluicao = null;
			Porte porte = null;
			
			if(!Util.isNullOuVazio(tipologiaPP)) {
				if(!Util.isNullOuVazio(tipologiaPP.getTipologiaGrupo()) && !Util.isNullOuVazio(tipologiaPP.getTipologiaGrupo().getIdePotencialPoluicao())) {
					potencialPoluicao = tipologiaPP.getTipologiaGrupo().getIdePotencialPoluicao();
				}
				
				if(!Util.isNullOuVazio(tipologia.getPorte())) {
					porte = tipologia.getPorte();
				}
			}
			
			if(!Util.isNullOuVazio(potencialPoluicao) && !Util.isNullOuVazio(porte)) {
				maiorClasse = this.classePotencialService.buscarClasseAtividade(porte, potencialPoluicao);
				
				if(!Util.isNullOuVazio(maiorClasse)) {
					if(Util.isNullOuVazio(classe) || (maiorClasse.getIdeClasse() > classe.getIdeClasse())) {
						classe = maiorClasse;
						porteIdentificado = porte;
					}
					else if(!Util.isNullOuVazio(classe) && (maiorClasse.equals(classe) && porte.getIdePorte() > porteIdentificado.getIdePorte())) {
						porteIdentificado = porte;
					}
				} 
				else if(Util.isNullOuVazio(classe) && Util.isNull(maiorClasse)){
					porteIdentificado = porteService.carregarPorteDLA();
				}
				else if(Util.isNullOuVazio(classe)){
					classe = new Classe(1, "Classe 1");
				}
			}
		}
		
		empreendimentoRequerimento.setIdeClasse(classe);
	
	}
	
	private Pessoa getOperador() {
		Usuario usuario = ContextoUtil.getContexto().getUsuarioLogado();
		return new Pessoa(usuario.getIdePessoaFisica());
	}

	/**
	 * Método chamado no finalizarNovoRequerimento()
	 * 
	 * @return valido
	 * @author eduardo.fernandes
	 * @throws Exception 
	 */
	public boolean validarFinalizarRequerimento() throws Exception {
		
		boolean valido = true;
		
		if(((this.isAlteracaoLicenca() 
				|| this.getAutorizacaoOuLicenca() 
				|| this.isRenovacaoLicenca()) && !isEmpreendimentoOutorgaOuAtosFlorestais()) && !this.isImpactoAdicional()){
			
			if(isAlteracaoLicenca() && Util.isNullOuVazio(this.atividadesLicenca) && Util.isNullOuVazio(this.atividadesLicencaEmprendSelected)){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar ao menos uma tipologia para alteração.");
			}
			
			if((porteIdentificado == null && necessarioCalcularPorte()) 
					|| (Util.isNullOuVazio(classe) && !isTipologiaDoEmpreendimentoXorY() && necessarioCalcularPorte())) {
				valido = false;
				JsfUtil.addWarnMessage("Por favor, verifique as atividades do empreendimento e realize o cálculo do porte.");
				
			} else if (!Util.isNullOuVazio(classe) && Util.isNullOuVazio(faseEmpreendimentoSelecionado)) {
				valido = false;
				JsfUtil.addWarnMessage("Por favor, marque uma opção no campo 3.");
				
			} else if(!Util.isNullOuVazio(faseEmpreendimentoSelecionado) && Util.isNullOuVazio(dtcPrevistaFaseEmpreendimento)){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, selecione uma data no campo 3.1.");
			}
			
			if(Util.isNullOuVazio(this.atividadesLicenca) && this.getAutorizacaoOuLicenca() && !isAlteracaoLicenca()){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar as atividades de licença.");
			}
			
			if(valido) {
				valido = validarAtividades(valido);
			}
		}
		
		if(!novoRequerimentoController.isDeclaracao()){
			JsfUtil.addWarnMessage("O campo Declaração é de preenchimento obrigatório.");
			valido = false;
		}
		
		return valido;
	}

	/**
	 * @param valido
	 * @return
	 * @throws Exception
	 */
	private boolean validarAtividades(boolean valido)  {
		for (Tipologia tipologia : this.atividadesLicenca) {
			if(Util.isNullOuVazio(tipologia.getValAtividade())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar o valor de todas as atividades de licença.");
				break;
			}
			
			if(isAlteracaoLicenca() && Util.isNull(tipologia.getOpcao())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar a opção para todas as atividades de licença.");
				break;
			}
			
			if(Util.isNullOuVazio(tipologia.getPorte()) && necessarioCalcularPorte()){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, realize o cálculo do porte.");
				break;
			}
			
			if(!Util.isNullOuVazio(tipologia.getValAtividade()) && tipologia.getValAtividade().equals("0,00")){
				valido = false;
				JsfUtil.addWarnMessage("As atividades de licença não podem ter valor 0, por favor informar um valor válido.");
				break;
			}
			
		}
		
		for (Tipologia tipologia : this.atividadesLicencaEmprendSelected) {
			if(Util.isNullOuVazio(tipologia.getValAtividade())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar o valor de todas as atividades de licença.");
				break;
			}
			
			if(Util.isNullOuVazio(tipologia.getPorte())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar o valor de todas as atividades de licença.");
				break;
			}
			
			if(isAlteracaoLicenca() && Util.isNull(tipologia.getOpcao())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar a opção para todas as atividades de licença.");
				break;
			}
			
			
			if(!Util.isNullOuVazio(tipologia.getValAtividade()) && tipologia.getValAtividade().equals("0,00")){
				valido = false;
				JsfUtil.addWarnMessage("As atividades de licença não podem ter valor 0, por favor informar um valor válido.");
				break;
			}
			
		}
		return valido;
	}
	
	/**
	 * @param valido
	 * @return
	 * @throws Exception
	 */
	private boolean validarAtividadesParaCalculo(boolean valido)  {
		
		for (Tipologia tipologia : this.atividadesLicenca) {
			if(Util.isNullOuVazio(tipologia.getValAtividade())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar o valor de todas as atividades de licença.");
				break;
			}
			if(isAlteracaoLicenca() && Util.isNull(tipologia.getOpcao())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar a opção para todas as atividades de licença.");
				break;
			}
			if(!Util.isNullOuVazio(tipologia.getValAtividade()) && tipologia.getValAtividade().equals("0,00")){
				valido = false;
				JsfUtil.addWarnMessage("As atividades de licença não podem ter valor 0, por favor informar um valor válido.");
				break;
			}
		}
		for (Tipologia tipologia : this.atividadesLicencaEmprendSelected) {
			if(Util.isNullOuVazio(tipologia.getValAtividade())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar o valor de todas as atividades de licença.");
				break;
			}
			if(isAlteracaoLicenca() && Util.isNull(tipologia.getOpcao())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar a opção para todas as atividades de licença.");
				break;
			}
			if(!Util.isNullOuVazio(tipologia.getValAtividade()) && tipologia.getValAtividade().equals("0,00")){
				valido = false;
				JsfUtil.addWarnMessage("As atividades de licença não podem ter valor 0, por favor informar um valor válido.");
				break;
			}
		}
		
		return valido;
	}
	
	public boolean validarFinalizarRequerimentoQuandoNaoHaObrigatoriedade() throws Exception {
		boolean valido = true;
		
		if(isSubstituicaoOuInstalacao()) {
			return valido;
		}
		
		for (Tipologia tipologia : this.atividadesLicenca) {
			
			
			if(Util.isNullOuVazio(tipologia.getPorte())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar o valor de todas as atividades de licença.");
				break;
			}
			
			if(isAlteracaoLicenca() && Util.isNull(tipologia.getOpcao())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar a opção para todas as atividades de licença.");
				break;
			}
			
			if(Util.isNullOuVazio(tipologia.getValAtividade())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar o valor de todas as atividades de licença.");
				break;
			}
			
			if(!Util.isNullOuVazio(tipologia.getValAtividade()) && tipologia.getValAtividade().equals("0,00")){
				valido = false;
				JsfUtil.addWarnMessage("As atividades de licença não podem ter valor 0, por favor informar um valor válido.");
				break;
			}
			
		}
		
		for (Tipologia tipologia : this.atividadesLicencaEmprendSelected) {
			if(Util.isNullOuVazio(tipologia.getPorte())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar o valor de todas as atividades de licença.");
				break;
			}
			
			if(isAlteracaoLicenca() && Util.isNull(tipologia.getOpcao())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar a opção para todas as atividades de licença.");
				break;
			}
			
			if(Util.isNullOuVazio(tipologia.getValAtividade())){
				valido = false;
				JsfUtil.addWarnMessage("Por favor, informar o valor de todas as atividades de licença.");
				break;
			}
			
			if(!Util.isNullOuVazio(tipologia.getValAtividade()) && tipologia.getValAtividade().equals("0,00")){
				valido = false;
				JsfUtil.addWarnMessage("As atividades de licença não podem ter valor 0, por favor informar um valor válido.");
				break;
			}
			
		}
		
		if (!Util.isNullOuVazio(this.atividadesLicencaEmprendSelected) || !Util.isNullOuVazio(this.atividadesLicenca)){
			
			if(Util.isNullOuVazio(faseEmpreendimentoSelecionado)) {
				valido = false;
				JsfUtil.addWarnMessage("Por favor, marque uma opção no campo 3.");
			
			} else {
				if (Util.isNullOuVazio(dtcPrevistaFaseEmpreendimento)) {
					valido = false;
					JsfUtil.addWarnMessage("Por favor, selecione uma data no campo 3.1.");
				}
			}
		}
		
		if (Util.isNullOuVazio(this.atividadesLicencaEmprendSelected) && Util.isNullOuVazio(this.atividadesLicenca)){
			JsfUtil.addWarnMessage("Informe a tipologia para caracterizar o empreendimento.");
			valido = false;
		}
		
		if(!novoRequerimentoController.isDeclaracao()){
			JsfUtil.addWarnMessage("O campo Declaração é de preenchimento obrigatório.");
			valido = false;
		}
		
		return valido;
	}
	
	public void adicionarAtividade() {
		try {
			Tipologia tipologia = this.tipologiaController.getTipologiaSelecionada();
			addTipologiaSelecionada(tipologia);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	public void adicionarAtividadeDoEmpreendimento() {
		try {
			carregarTipologiaDoEmpreendSelecionada(tipologiaEmpreendSelecionada);
			tipologiaEmpreendSelecionada.setDoEmpreendimento(true);
			addTipologiaSelecionada(this.tipologiaEmpreendSelecionada);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * @param tipologia
	 * @throws Exception
	 */
	public void addTipologiaSelecionada(Tipologia tipologia) throws Exception {
		if(atividadesLicenca.contains(tipologia) || atividadesAutorizacao.contains(tipologia) || atividadesLicencaEmprendSelected.contains(tipologia)){
			JsfUtil.addWarnMessage("A Atividade já se encontra cadastrada");
			return;
		}
		
		if (!Util.isNullOuVazio(tipologia.getIndAutorizacao()) && tipologia.getIndAutorizacao())
			this.atividadesAutorizacao.add(tipologia);
		
		else if(!Util.isNullOuVazio(tipologia.isDoEmpreendimento()) && tipologia.isDoEmpreendimento()){
			if(tipologia.getTipologiaGrupo().getUnidadeMedidaTipologiaGrupo() != null && tipologia.getTipologiaGrupo().getUnidadeMedidaTipologiaGrupo().getIdeUnidadeMedidaTipologiaGrupo() != null){
				this.atividadesLicencaEmprendSelected.add(tipologia);
				this.atividadesEmpreendimento.remove(tipologia);
			}else{
				JsfUtil.addWarnMessage("Não é possível incluir atividades desse tipo.");
				return;
			}
		}
		else{
			if(isAlteracaoLicenca())
				tipologia.setOpcao(OpcaoEnum.INCLUSAO);
			this.atividadesLicenca.add(tipologia);
		}
		
		this.salvarRequerimentoTipologia(tipologia);
		
		RequestContext.getCurrentInstance().execute("dlgTipologia.hide()");
		JsfUtil.addSuccessMessage("Tipologia adicionada com sucesso");
	}

	public void calcularPorte() {
		isPorteCalculado = true;
		try {
			if(validarAtividadesParaCalculo(true)){
				
				carregaAtividadesParaCalculo();
				tipologiaService.enquadrarPorteRequerimento(atividadesLicencaParaCalculo);
 				definirClasseDoEmpreendimento();
				empreendimentoRequerimento.setIdePorte(porteIdentificado);
				verificarTipologiaPrincipal(atividadesLicencaParaCalculo);
				
				vincularAtividadePrincipal(atividadesLicencaParaCalculo);
				defineClasseQuandoPorteNaoIdentificado();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void carregaAtividadesParaCalculo() {
		atividadesLicencaParaCalculo = new ArrayList<Tipologia>();
		
		try {
			if(isAlteracaoLicenca()) {
				for (Tipologia atividadeLic : atividadesLicenca) {
					atividadeLic.setOpcao(OpcaoEnum.INCLUSAO);
				}
			}

			if(!Util.isNullOuVazio(atividadesLicenca)){
				atividadesLicencaParaCalculo.addAll(atividadesLicenca);
			}	
			if(!Util.isNullOuVazio(atividadesLicencaEmprendSelected)){
				atividadesLicencaParaCalculo.addAll(atividadesLicencaEmprendSelected);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível identificar o tipo de licença.");
		}
	}
	
	public void calcularPorteInit() {
		try {
			carregaAtividadesParaCalculo();
			
			if(isModoVisualizacao() 
					&& !Util.isNullOuVazio(empreendimentoRequerimento)
					&& !Util.isNullOuVazio(empreendimentoRequerimento.getIdePorte())) {
				
				classe = empreendimentoRequerimento.getIdeClasse();
				porteIdentificado = empreendimentoRequerimento.getIdePorte();
			} else {
				porteIdentificado = this.tipologiaService.enquadrarPorteRequerimentoInit(atividadesLicencaParaCalculo);
			}
			
			this.verificarTipologiaPrincipal(atividadesLicencaParaCalculo);
			
			if(this.novoRequerimentoController.isDesabilitarTudo()){
				
				defineClasseQuandoPorteNaoIdentificado();
				
				if(empreendimentoRequerimento != null)
					this.classe = empreendimentoRequerimento.getIdeClasse();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void defineClasseQuandoPorteNaoIdentificado() {
		if(!Util.isNullOuVazio(porteIdentificado) && porteIdentificado.getIdePorte().equals(6)){

			if(Util.isNullOuVazio(classe) || classe.getIdeClasse() == 1) {
				
				classe = new Classe(1, "Classe 1");
				empreendimentoRequerimento.setIdeClasse(classe);
			}
		}
	}

	private void salvarLicencas(Collection<Tipologia> atividadesDoRequerimento) throws Exception {
		if(!Util.isNullOuVazio(atividadesDoRequerimento)) {
			for (Tipologia licenca : atividadesDoRequerimento) {
				this.salvarRequerimentoTipologia(licenca);
			}
		}
	}

	private void verificarTipologiaPrincipal(Collection<Tipologia> atividadesDoRequerimento) {
		int qtdPorte = 0;
		
		if(atividadePrincipal==null) {
			atividadePrincipal = new Tipologia();
		}
		
		this.tipologiasPrincipais = new ArrayList<Tipologia>();
		
		for (Tipologia licenca : atividadesDoRequerimento) {
			
			atividadePrincipal.setIndPrincipal(false);
			
			if(Util.isNull(licenca.getPorte())) {
				continue;
			}
			
			if (licenca.getPorte().equals(porteIdentificado)) {
				
				this.tipologiasPrincipais.add(licenca);
				
				qtdPorte++;
				
				atividadePrincipal = licenca;
				
				if (qtdPorte > 1) {
					this.escolherTipologiaPrincipal = true;
				} else{
					this.escolherTipologiaPrincipal = false;
				}
			}
		}

		if (!this.escolherTipologiaPrincipal && !Util.isNullOuVazio(atividadePrincipal)) {
			atividadePrincipal.setIndPrincipal(true);
		}
	}

	public void changeTipologiaPrincipal() throws Exception {
		if (!Util.isNull(atividadePrincipal)) {
			Tipologia tipologia = new Tipologia(atividadePrincipal.getIdeTipologia());
			for (Tipologia licenca : this.atividadesLicencaParaCalculo) {
				if (tipologia.equals(licenca)) {
					licenca.setIndPrincipal(true);
					this.atividadePrincipal = licenca;
					definirClasseDoEmpreendimento();
				} else {
					licenca.setIndPrincipal(false);
				}
				this.salvarRequerimentoTipologia(licenca);
			}
		}
		else {
			this.classe=null;
			JsfUtil.addWarnMessage("Não foi possível obter a classe desta tipologia.");
		}
	}
	
	public void changeTipologiaEmpreendimentoSelecionada(ValueChangeEvent vEvent) {

		if (!Util.isNull(vEvent.getNewValue())) {
			Tipologia tipologia = (Tipologia) vEvent.getNewValue();
			carregarTipologiaDoEmpreendSelecionada(tipologia);
		}

	}

	/**
	 * @param tipologia
	 */
	public Tipologia carregarTipologiaDoEmpreendSelecionada(Tipologia tipologia) {
		for (Tipologia ativid : atividadesEmpreendimento) {
			if(ativid.getIdeTipologia().equals(tipologia.getIdeTipologia()))
				tipologiaEmpreendSelecionada = ativid.getClone();
		}
		return tipologiaEmpreendSelecionada;
	}
	
	public void excluirAtividadeLicEmpreendimento(){
		try {

			if(isEmpreend){
				atividadesLicencaEmprendSelected.remove(atividadeAExcluir);
				atividadesEmpreendimento.add(atividadeAExcluir.getClone());
			}else{
				atividadesLicenca.remove(atividadeAExcluir);
			}
			
			if(tipologiasPrincipais.contains(this.atividadeAExcluir))
				tipologiasPrincipais.remove(this.atividadeAExcluir);
			
			porteIdentificado = null;
			faseEmpreendimentoSelecionado = null;
			tipologiaEmpreendSelecionada = null;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private boolean necessarioCalcularPorte() {
		//alternativa para reclamação do sonar 2 returns
		boolean retorno=false;
		
		if(!atividadesAutorizacao.isEmpty() && !isValorAtividadeLicencaPreenchido()) {
			return retorno;		
		}
		
		return true;
		
	}
	
	private boolean isValorAtividadeLicencaPreenchido() {
		
		if(!Util.isNullOuVazio(this.atividadesLicenca)){
			for (Tipologia tipologia : this.atividadesLicenca) {
				if(!Util.isNullOuVazio(tipologia.getValAtividade()) && !tipologia.getValAtividade().equals("0,00")){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void salvarEmpreendimentoRequerimentoPorBoleto(EmpreendimentoRequerimento empreendimentoReq){
		try {
			empreendimentoReq.setIdeClasse(empreendimentoRequerimento.getIdeClasse());
			empreendimentoReq.setIdePorte(empreendimentoRequerimento.getIdePorte());
			empreendimentoService.salvarEmpreendimentoRequerimento(empreendimentoReq);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public Boolean getTipologiaPrincipal() {
		return tipologiaPrincipal;
	}

	public void setTipologiaPrincipal(Boolean tipologiaPrincipal) {
		this.tipologiaPrincipal = tipologiaPrincipal;
	}

	public Porte getPorteIdentificado() {
		return porteIdentificado;
	}

	public void setPorteIdentificado(Porte porteIdentificado) {
		this.porteIdentificado = porteIdentificado;
	}

	public FaseEmpreendimento getFaseEmpreendimentoSelecionado() {
		return faseEmpreendimentoSelecionado;
	}

	public void setFaseEmpreendimentoSelecionado(FaseEmpreendimento faseEmpreendimentoSelecionado) {
		this.faseEmpreendimentoSelecionado = faseEmpreendimentoSelecionado;
	}

	public List<FaseEmpreendimento> getListaFaseEmpreendimento() {
		return listaFaseEmpreendimento;
	}

	public void setListaFaseEmpreendimento(List<FaseEmpreendimento> listaFaseEmpreendimento) {
		this.listaFaseEmpreendimento = listaFaseEmpreendimento;
	}

	public EmpreendimentoRequerimento getEmpreendimentoRequerimento() {
		return empreendimentoRequerimento;
	}

	public void setEmpreendimentoRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento) {
		this.empreendimentoRequerimento = empreendimentoRequerimento;
	}

	public Date getDtcPrevistaFaseEmpreendimento() {
		return dtcPrevistaFaseEmpreendimento;
	}

	public void setDtcPrevistaFaseEmpreendimento(Date dtcPrevistaFaseEmpreendimento) {
		this.dtcPrevistaFaseEmpreendimento = dtcPrevistaFaseEmpreendimento;
	}

	public DataModel<RequerimentoTipologiaNR> getRequerimentoTipologiaData() {
		return requerimentoTipologiaData;
	}

	public void setRequerimentoTipologiaData(DataModel<RequerimentoTipologiaNR> requerimentoTipologiaData) {
		this.requerimentoTipologiaData = requerimentoTipologiaData;
	}
	
	public boolean isRenderPorteEFase() throws Exception {
		boolean retorno= false;
		if (isAutorizacaoEspecial) {
			return false;
		}
		
		if(!isModoEdicao()
				&& !isAlteracaoLicencaSomenteDeSubstituicao()
				&& (isRenovacaoLicenca() || (isAlteracaoLicenca()) || (getAutorizacaoOuLicenca()))
				&& !isEmpreendimentoOutorgaOuAtosFlorestais()) {
			return true;
		} else{
			
			if((isRenovacaoLicenca() || isAlteracaoLicenca() || (!isAlteracaoLicenca() && getAutorizacaoOuLicenca())) 
				&& !isImpactoAdicional()
				&& !Util.isNullOuVazio(atividadesLicencaParaCalculo)
				&& !isEmpreendimentoOutorgaOuAtosFlorestais()) {
				retorno= true;
				return retorno;
			} else {
				return false;
			}
			
		}
	}
	public boolean isRenderAbaFinalizarRequerimentoPerguntaTres(){
		boolean isRender = false;
		AbaRenovacaoAlteracaoProrrogacaoController abaRenovacaoProrrogacao = (AbaRenovacaoAlteracaoProrrogacaoController) SessaoUtil.recuperarManagedBean("#{abaRenovacaoAlteracaoProrrogacaoController}", AbaRenovacaoAlteracaoProrrogacaoController.class);		
		
		try {
			isRender =  (isAlteracaoLicenca() || abaRenovacaoProrrogacao.isRenovacaoLicenca() || getAutorizacaoOuLicenca()) && !isImpactoAdicional() ;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}			
		return isRender;
	}
	
	public boolean isRenderAbaFinalizarRequerimentoPerguntaTresPontoUm(){
		boolean isRender = false;		
		AbaRenovacaoAlteracaoProrrogacaoController abaRenovacaoProrrogacao = (AbaRenovacaoAlteracaoProrrogacaoController) SessaoUtil.recuperarManagedBean("#{abaRenovacaoAlteracaoProrrogacaoController}", AbaRenovacaoAlteracaoProrrogacaoController.class);
		try {
			isRender = abaRenovacaoProrrogacao.isRenovacaoLicenca() || isAlteracaoLicenca() || getAutorizacaoOuLicenca();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return isRender;
	}
	
	
	
	
	public boolean isAlteracaoLicenca()  {
		return this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().isAlteracaoLicenca();
	}
	
	public boolean isAlteracaoLicencaSomenteDeSubstituicao()  {
		return this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().isAlteracaoLicencaSomenteDeSubstituicao();
	}
	
	public boolean isImpactoAdicional() {
		return this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().isImpactoAdicional();
	}
	
	public boolean isProjetoLicenciadoComImpactoAdicional() {
		return this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().isProjetoLicenciadoComImpactoAdicional();
	}
	
	public boolean isProjetoLicenciadoComImpactoAdicionalNDA() throws Exception {
		return this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().isProjetoLicenciadoComImpactoAdicionalNDA();
	}
	
	public boolean isSubstituicaoOuInstalacao() throws Exception {
		return this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().isProjetoLicenciadoComImpactoAdicionalSubstituicaoOuInstalacao();
	}
	
	public boolean isRenovacaoLicenca() throws Exception {
		return this.novoRequerimentoController.getAbaRenovacaoAlteracaoProrrogacaoController().isRenovacaoLicenca();
	}
	

	
	public boolean isRenderedAutorizacaoOuLicenca()  {
		
		if(isAutorizacaoEspecial) {
			return false;
		}
		
		return getAutorizacaoOuLicenca();
	}
	
	public boolean isRenderedCaracterizacaoAtividadesEmpreendimento()  {
		
		if(isAutorizacaoEspecial) {
			return false;
		}
		
		if(!isModoEdicao()) {
			return true;
		}
		return !isEmpreendimentoOutorgaOuAtosFlorestais();
	}
	
	public boolean isEmpreendimentoOutorgaOuAtosFlorestais() {
		boolean teste = true;

		if(!Util.isNullOuVazio(this.atividadesEmpreendimento)){
			for (Tipologia ativEmpreend : this.atividadesEmpreendimento) {
				if(!ativEmpreend.getIdeTipologia().equals(TipologiaEnum.ATOS_FLORESTAIS.getId()) && !ativEmpreend.getIdeTipologia().equals(TipologiaEnum.OUTORGA.getId())){
					teste = false;
					break;
				}	
			}			
		}
		else if(!getAutorizacaoOuLicenca())	{
			teste = false;
		}
		
		return teste;
	}
	
	public Collection<Tipologia> getAtividadesLicenca() {
		return atividadesLicenca;
	}

	public void setAtividadesLicenca(Collection<Tipologia> atividadesLicenca) {
		this.atividadesLicenca = atividadesLicenca;
	}

	public Collection<Tipologia> getAtividadesAutorizacao() {
		return Util.isNull(atividadesAutorizacao) ? (atividadesAutorizacao = new ArrayList<Tipologia>()) : atividadesAutorizacao;
	}

	public void setAtividadesAutorizacao(Collection<Tipologia> atividadesAutorizacao) {
		this.atividadesAutorizacao = atividadesAutorizacao;
	}

	public boolean isEscolherTipologiaPrincipal() {
		return escolherTipologiaPrincipal;
	}

	public void setEscolherTipologiaPrincipal(boolean escolherTipologiaPrincipal) {
		this.escolherTipologiaPrincipal = escolherTipologiaPrincipal;
	}

	public Collection<Tipologia> getTipologiasPrincipais() {
		return tipologiasPrincipais;
	}
	
	public Boolean getTipologiasMaiorQueUm() {
		if(!Util.isNullOuVazio(tipologiasPrincipais))
			return (tipologiasPrincipais.size()>1);
		else
			return false;
	}

	public void setTipologiasPrincipais(Collection<Tipologia> tipologiasPrincipais) {
		this.tipologiasPrincipais = tipologiasPrincipais;
	}

	public Tipologia getAtividadePrincipal() {
		return atividadePrincipal;
	}

	public void setAtividadePrincipal(Tipologia atividadePrincipal) {
		this.atividadePrincipal = atividadePrincipal;
	}

	public List<OpcaoEnum> getOpcoes(){
		List<OpcaoEnum> items = new ArrayList<OpcaoEnum>();
		Exception erro = null;
		try {
			for (OpcaoEnum opcao : OpcaoEnum.values()) {
					if(!opcao.equals(OpcaoEnum.INCLUSAO) || !isAlteracaoLicenca())
						items.add(opcao);
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
		}
		return items;
	}

	public Tipologia getAtividadeAExcluir() {
		return atividadeAExcluir;
	}

	public void setAtividadeAExcluir(Tipologia atividadeAExcluir) {
		this.atividadeAExcluir = atividadeAExcluir;
	}
	
	public Boolean getAutorizacaoOuLicenca(){
		return this.novoRequerimentoController.getAbaLicencaAutorizacaoController().getAutorizacaoOuLicenca();
	}
	
	public boolean isEnquadramentoAutomaticoDIAP(){
		return this.novoRequerimentoController.isExisteUnicaSolicitacaoParaRequerimento(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA3_1) && this.novoRequerimentoController.getAbaLicencaAutorizacaoController().isTipoAreaProtegidaApenasApp() && !Util.isNullOuVazio(this.porteIdentificado) && this.porteIdentificado.getIdePorte().equals(PorteEnum.NAO_IDENTIFICADO.getId());
	}

	public boolean isEnquadramentoAutomaticoAMC(){
		return this.novoRequerimentoController.isExisteUnicaSolicitacaoParaRequerimento(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p11);
	}
	/**
	 * @return the atividadesEmpreendimento
	 */
	public Collection<Tipologia> getAtividadesEmpreendimento() {
		return atividadesEmpreendimento;
	}

	/**
	 * @param atividadesEmpreendimento the atividadesEmpreendimento to set
	 */
	public void setAtividadesEmpreendimento(Collection<Tipologia> atividadesEmpreendimento) {
		this.atividadesEmpreendimento = atividadesEmpreendimento;
	}

	/**
	 * @return the atividadesLicencaEmprendSelected
	 */
	public Collection<Tipologia> getAtividadesLicencaEmprendSelected() {
		return atividadesLicencaEmprendSelected;
	}

	/**
	 * @param atividadesLicencaEmprendSelected the atividadesLicencaEmprendSelected to set
	 */
	public void setAtividadesLicencaEmprendSelected(Collection<Tipologia> atividadesLicencaEmprendSelected) {
		this.atividadesLicencaEmprendSelected = atividadesLicencaEmprendSelected;
	}

	/**
	 * @return the tipologiaEmpreendSelecionada
	 */
	public Tipologia getTipologiaEmpreendSelecionada() {
		return tipologiaEmpreendSelecionada;
	}

	/**
	 * @param tipologiaEmpreendSelecionada the tipologiaEmpreendSelecionada to set
	 */
	public void setTipologiaEmpreendSelecionada(Tipologia tipologiaEmpreendSelecionada) {
		this.tipologiaEmpreendSelecionada = tipologiaEmpreendSelecionada;
	}

	/**
	 * @return the atividadesLicencaParaCalculo
	 */
	public Collection<Tipologia> getAtividadesLicencaParaCalculo() {
		return atividadesLicencaParaCalculo;
	}

	/**
	 * @param atividadesLicencaParaCalculo the atividadesLicencaParaCalculo to set
	 */
	public void setAtividadesLicencaParaCalculo(Collection<Tipologia> atividadesLicencaParaCalculo) {
		this.atividadesLicencaParaCalculo = atividadesLicencaParaCalculo;
	}

	/**
	 * @return the isEmpreend
	 */
	public boolean isEmpreend() {
		return isEmpreend;
	}

	/**
	 * @param isEmpreend the isEmpreend to set
	 */
	public void setEmpreend(boolean isEmpreend) {
		this.isEmpreend = isEmpreend;
	}

	/**
	 * @return the classe
	 */
	public Classe getClasse() {
		return classe;
	}

	/**
	 * @param classe the classe to set
	 */
	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	/**
	 * @return the temLicencaComValorVazio
	 */
	public boolean isTemLicencaComValorVazio() {
		return temLicencaComValorVazio;
	}

	/**
	 * @param temLicencaComValorVazio the temLicencaComValorVazio to set
	 */
	public void setTemLicencaComValorVazio(boolean temLicencaComValorVazio) {
		this.temLicencaComValorVazio = temLicencaComValorVazio;
	}

}