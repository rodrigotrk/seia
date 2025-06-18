package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.AnaliseTecnicaDAOImpl;
import br.gov.ba.seia.dao.ControleProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.ControleTramitacaoDAOImpl;
import br.gov.ba.seia.dao.EquipeDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.IntegranteEquipeDAOImpl;
import br.gov.ba.seia.dao.PautaDAOImpl;
import br.gov.ba.seia.dao.PessoaFisicaDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.StatusFluxoDAOImpl;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleCronograma;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Cronograma;
import br.gov.ba.seia.entity.Equipe;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.IntegranteEquipe;
import br.gov.ba.seia.entity.Ocupacao;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoIntegranteEquipe;
import br.gov.ba.seia.entity.ProcessoAtoIntegranteEquipePK;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.ControleCronogramaService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FormarEquipeServiceFacade {

	@Inject
	private IDAO<Funcionario> funcionarioDAO;
	
	@Inject
	private IDAO<Ocupacao> ocupacaoDAO;
	
	@Inject
	private IDAO<ProcessoAtoIntegranteEquipe> processoAtoIntegranteEquipeDAO;
	
	@EJB
	private AnaliseTecnicaDAOImpl analiseTecnicaDAOImpl;
	
	@EJB
	private IntegranteEquipeDAOImpl integranteEquipeDAOImpl;
	
	@EJB
	private ControleTramitacaoDAOImpl controleTramitacaoDAOImpl;
	
	@EJB
	private ControleProcessoAtoDAOImpl controleProcessoAtoDAOImpl;
	
	@EJB
	private EquipeDAOImpl equipeDAOImpl;
	
	@EJB
	private ProcessoAtoDAOImpl processoAtoDAOImpl;
	
	@EJB
	private PautaDAOImpl pautaDAOImpl;
	
	@EJB
	private AreaService areaService;

	@EJB
	private ParametroService parametroService;
	
	@EJB
	private ControleCronogramaService controlCronogramaService;
	
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	
	@EJB
	private StatusFluxoDAOImpl statusFluxoDAOImpl;
	
	@EJB
	private PessoaFisicaDAOImpl pessoaFisicaDAOImpl;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Equipe carregarOuCriarNovaEquipe(Processo ideProcesso, Area ideArea) throws Exception {
		Equipe equipe = equipeDAOImpl.buscarEquipe(ideProcesso.getIdeProcesso(), ideArea.getIdeArea());
		if(Util.isNull(equipe)) {
			equipe = new Equipe();
			equipe.setIdeArea(ideArea);
			equipe.setIdeProcesso(ideProcesso);
			equipe.setDtcFormacaoEquipe(new Date());
			equipeDAOImpl.salvarEquipe(equipe);
		}
		return equipe;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarIntegranteEquipe(IntegranteEquipe pIntegranteEquipe) throws Exception {
		
		List<IntegranteEquipe> listIntegranteEquipe =  (List<IntegranteEquipe>) listarIntegranteEquipe(pIntegranteEquipe.getIdeEquipe().getIdeEquipe());
		List<Funcionario> listFuncionario = new ArrayList<Funcionario>();
		
		for (IntegranteEquipe integranteEquipe : listIntegranteEquipe) {
			if (pIntegranteEquipe.getIndLiderEquipe() && integranteEquipe.getIndLiderEquipe()) throw new Exception("Não é possível ter dois líderes na mesma equipe.");
		}
		
		for (IntegranteEquipe integranteEquipe : listIntegranteEquipe) {
			listFuncionario.add(integranteEquipe.getIdePessoaFisica());
		}
		
		if(!Util.isNullOuVazio(listFuncionario) && listFuncionario.contains(pIntegranteEquipe.getIdePessoaFisica())) {
			throw new Exception("Técnico(a) selecionado já foi adicionado a equipe. Para atribuir novos atos remova-o e adicione novamente.");
		}
		
		integranteEquipeDAOImpl.salvar(pIntegranteEquipe);
		for(ProcessoAtoIntegranteEquipe processoAtoIntegranteEquipe : pIntegranteEquipe.getProcessoAtoIntegranteEquipeCollection()) {
			salvarProcessoAtoIntegrateEquipe(pIntegranteEquipe,processoAtoIntegranteEquipe);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarProcessoAtoIntegrateEquipe(IntegranteEquipe pIntegranteEquipe, ProcessoAtoIntegranteEquipe processoAtoIntegranteEquipe) {
		try{
			processoAtoIntegranteEquipe.setIdeIntegranteEquipe(pIntegranteEquipe);
			ProcessoAtoIntegranteEquipePK processoAtoIntegranteEquipePK = 
				new ProcessoAtoIntegranteEquipePK(
					processoAtoIntegranteEquipe.getIdeProcessoAto().getIdeProcessoAto(), 
					processoAtoIntegranteEquipe.getIdeIntegranteEquipe().getIdeIntegranteEquipe()
				)
			;
			processoAtoIntegranteEquipe.setProcessoAtoIntegranteEquipePK(processoAtoIntegranteEquipePK);
			processoAtoIntegranteEquipeDAO.salvar(processoAtoIntegranteEquipe);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarControleProcessoAtoEmAnalise(Collection<ProcessoAtoIntegranteEquipe> listaProcessoAtoIntegranteEquipe) {
		try{
			for(ProcessoAtoIntegranteEquipe processoAtoIntegranteEquipe : listaProcessoAtoIntegranteEquipe) {
				ControleProcessoAto controleProcessoAto = new ControleProcessoAto();
				controleProcessoAto.setDtcControleProcessoAto(new Date());
				controleProcessoAto.setIndAprovado(true);
				controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(StatusProcessoAtoEnum.EM_ANALISE.getId()));
				controleProcessoAto.setIdeProcessoAto(processoAtoIntegranteEquipe.getIdeProcessoAto());
				controleProcessoAto.setIdePessoaFisica(processoAtoIntegranteEquipe.getIdeIntegranteEquipe().getIdePessoaFisica());
				controleProcessoAtoDAOImpl.salvar(controleProcessoAto);
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIntegranteEquipe(IntegranteEquipe integranteEquipe) throws Exception {
		integranteEquipeDAOImpl.remover(integranteEquipe);
	}

	public boolean desabilitaDistribuicaoEquipeProcesso(Processo pProcesso) {

		boolean lDesabilitaDistribuicaoEquipeProcesso = true;
		/*try {

			for (EquipeProcesso equipeProcesso : this.filtrarListaEquipeProcesso(new EquipeProcesso(pProcesso))) {

				Pauta lPauta = this.filtrarPauta(new Pauta(equipeProcesso.getIdePessoaFisica()));

				ControleTramitacao lControleTramitacao = new ControleTramitacaoDAOImpl().getControleTramitacao(new ControleTramitacao(pProcesso, lPauta, Boolean.TRUE));

				if (Util.isNullOuVazio(lControleTramitacao) || !Integer.valueOf(6).equals(lControleTramitacao.getIdeStatusFluxo().getIdeStatusFluxo())) {

					lDesabilitaDistribuicaoEquipeProcesso = false;
					break;
				}
			}
		}
		catch (Exception e) {
			lDesabilitaDistribuicaoEquipeProcesso = false;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}*/

		return lDesabilitaDistribuicaoEquipeProcesso;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void distribuirProcesso(Integer ideProcesso, Collection<IntegranteEquipe> listaIntegranteEquipe, Area areaCoordenador, String observacao) throws Exception {

		Date lDataDistribuicaoProcesso = new Date();
		Processo processo = new Processo(ideProcesso);

		validar(ideProcesso, listaIntegranteEquipe, areaCoordenador, processo);
		controleTramitacaoService.resetarTramitacoes(ideProcesso);

		if (Util.isNullOuVazio(areaCoordenador)) {
			throw new Exception("Não foi possível obter a área do processo.");
		}
		else {
			for (IntegranteEquipe integrante : listaIntegranteEquipe) {
				
				Pauta lPauta = pautaDAOImpl.getPauta(new Pauta(integrante.getIdePessoaFisica()));
				PessoaFisica pessoaFisicaLogada = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
				ControleTramitacao controleTramitacao = new ControleTramitacao(processo, new StatusFluxo(StatusFluxoEnum.ANALISE_TECNICA.getStatus()), areaCoordenador, lDataDistribuicaoProcesso, true, lPauta, integrante.getIndLiderEquipe(),observacao,pessoaFisicaLogada);
				controleTramitacaoService.salvar(controleTramitacao);
				salvarControleProcessoAtoEmAnalise(integrante.getProcessoAtoIntegranteEquipeCollection());
			}
		}
	}

	private void validar(Integer ideProcesso,Collection<IntegranteEquipe> listaIntegranteEquipe, Area areaCoordenador, Processo processo) throws Exception {
		verificarExistenciaLider(listaIntegranteEquipe);
		verificarLimitePautaTecnica(listaIntegranteEquipe);
		verificarConclusaoParecer(ideProcesso, areaCoordenador);
		verificarStatusAtualDoProcesso(processo);
	}

	private void verificarStatusAtualDoProcesso(Processo processo) throws Exception {
		ControleTramitacao ultimoControleTramitacao = controleTramitacaoService.buscarUltimoPorProcesso(processo);
		if(!podeDistribuirNoStatusAtual(ultimoControleTramitacao.getIdeStatusFluxo())) {
			throw new SeiaValidacaoRuntimeException("Não é possível alterar a formação da equipe do processo no status que o mesmo se encontra.");
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void verificarConclusaoParecer(Integer ideProcesso,	Area areaCoordenador) throws Exception {
		ControleCronograma lControleCronograma = controlCronogramaService.getControleCronogramaConclusaoParecerByCronograma(new Cronograma(new Processo(ideProcesso)), areaCoordenador);
		if (Util.isNullOuVazio(lControleCronograma) || Util.isNullOuVazio(lControleCronograma.getDtcItemPrevista())) {
			throw new SeiaValidacaoRuntimeException("Não é permitido distribuir o processo antes que a data de conclusão do parecer esteja definida.");
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void verificarLimitePautaTecnica(Collection<IntegranteEquipe> listaIntegranteEquipe) throws Exception {
		
		Integer MAX_PROCESSOS_PAUTA_TECNICA = 
				Integer.valueOf(parametroService.obterPorId(ParametroEnum.MAX_PROCESSOS_PAUTA_TECNICA.getIdeParametro()).getDscValor());
		Integer MAX_PROCESSOS_PAUTA_TECNICA_LIDER = 
				Integer.valueOf(parametroService.obterPorId(ParametroEnum.MAX_PROCESSOS_PAUTA_TECNICA_LIDER.getIdeParametro()).getDscValor());

		for (IntegranteEquipe integrante : listaIntegranteEquipe) {
			
			Integer numeroLideranca = controleTramitacaoDAOImpl.numeroControleTramitacaoLiderPorTecnico(integrante.getIdePessoaFisica().getIdePessoaFisica());
			Integer numeroProcesso = controleTramitacaoDAOImpl.numeroControleTramitacaoPorTecnico(integrante.getIdePessoaFisica().getIdePessoaFisica());
			
			if (integrante.getIndLiderEquipe() && numeroLideranca >= MAX_PROCESSOS_PAUTA_TECNICA_LIDER) {

				throw new SeiaValidacaoRuntimeException("Não é permitido que o Funcionário seja líder de mais de "+MAX_PROCESSOS_PAUTA_TECNICA_LIDER+" equipes.");
			}
			
			if (numeroProcesso >= MAX_PROCESSOS_PAUTA_TECNICA) {

				throw new SeiaValidacaoRuntimeException("Não é permitido que o Funcionário esteja presente em mais de "+MAX_PROCESSOS_PAUTA_TECNICA+" equipes.");
			}
		}
	}

	private void verificarExistenciaLider(Collection<IntegranteEquipe> listaIntegranteEquipe) throws Exception {
		boolean existeLider = false;
		for (IntegranteEquipe integrante : listaIntegranteEquipe) {
			if (integrante.getIndLiderEquipe()) {

				existeLider = true;
				break;
			}
		}
		if (!existeLider) throw new SeiaValidacaoRuntimeException("Não é possível formar uma equipe sem líder.");
	}
	
	private boolean podeDistribuirNoStatusAtual(StatusFluxo statusFluxo) {
		return Integer.valueOf(StatusFluxoEnum.FORMADO.getStatus()).equals(statusFluxo.getIdeStatusFluxo())
				|| Integer.valueOf(StatusFluxoEnum.ENCAMINHADO_PARA_AREA.getStatus()).equals(statusFluxo.getIdeStatusFluxo())
				|| Integer.valueOf(StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus()).equals(statusFluxo.getIdeStatusFluxo())
				|| Integer.valueOf(StatusFluxoEnum.NOTIFICACAO_EXPEDIDA.getStatus()).equals(statusFluxo.getIdeStatusFluxo())
				|| Integer.valueOf(StatusFluxoEnum.NOTIFICACAO_CANCELADA.getStatus()).equals(statusFluxo.getIdeStatusFluxo())
				|| Integer.valueOf(StatusFluxoEnum.NOTIFICACAO_NAO_RESPONDIDA.getStatus()).equals(statusFluxo.getIdeStatusFluxo())
				|| Integer.valueOf(StatusFluxoEnum.NOTIFICACAO_RESPONDIDA.getStatus()).equals(statusFluxo.getIdeStatusFluxo());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcessoAto> filtrarListaProcessoAto(ProcessoAto pProcessoAto) {
		
		Collection<ProcessoAto> listaProcessoAto = null;
 		
		try {
			/*
			listaProcessoAto = processoAtoDAOImpl.getProcessoAtoReenquadramento(pProcessoAto, true);
			
			if (Util.isNullOuVazio(listaProcessoAto)){
				listaProcessoAto = processoAtoDAOImpl.getProcessoAtoReenquadramento(pProcessoAto, false);
			}*/
			
			listaProcessoAto =  ajustarProcessoAto(processoAtoDAOImpl.getProcessoAto(pProcessoAto));
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		return listaProcessoAto;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcessoAto> ajustarProcessoAto(Collection<ProcessoAto> listaProcessoAto){
		
		ProcessoAto processoAtoAnterior = new ProcessoAto();
		ProcessoAto processoAtoExcluido = new ProcessoAto();
		for (Iterator<ProcessoAto> iterator = listaProcessoAto.iterator(); iterator.hasNext();) {
			ProcessoAto processoAto = (ProcessoAto) iterator.next();
			
			if (processoAto.getAtoAmbiental().equals(processoAtoAnterior.getAtoAmbiental())){
				if(!Util.isNullOuVazio(processoAto.getTipologia())){
					if(processoAto.getTipologia().equals(processoAtoAnterior.getTipologia())){
						processoAtoExcluido = processoAtoAnterior;
					}
				}
			}
			processoAtoAnterior = processoAto;
		}
		
		listaProcessoAto.remove(processoAtoExcluido);
		
		return listaProcessoAto;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Ocupacao> filtrarListaOcupacoes(Ocupacao pOcupacao) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Ocupacao.class);
		
		if (!Util.isNull(pOcupacao)) {
			if (!Util.isNull(pOcupacao.getIdeOcupacao())) {
				criteria.add(Restrictions.eq("ideOcupacao", pOcupacao.getIdeOcupacao()));
			}
			if (!Util.isNull(pOcupacao.getNomOcupacao())) {
				criteria.add(Restrictions.ilike("nomOcupacao", pOcupacao.getNomOcupacao(), MatchMode.END));
			}
		}

		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideOcupacao"), "ideOcupacao")
				.add(Projections.property("codOcupacao"), "codOcupacao")
				.add(Projections.property("nomOcupacao"), "nomOcupacao")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Ocupacao.class))
		;
		
		try {
			return ocupacaoDAO.listarPorCriteria(criteria, Order.asc("nomOcupacao"));
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> filtrarListaAreas(Area pArea) {
		return areaService.filtrarListaAreas(pArea);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<StatusFluxo> filtrarListaStatusFluxo(StatusFluxo pStatusFluxo) {

		try {
			return statusFluxoDAOImpl.listarStatusFluxo(pStatusFluxo);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Funcionario> listarFuncionarios(PessoaFisica pessoaFisica) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
		
		criteria
			.createAlias("pessoaFisica", "pf", JoinType.INNER_JOIN)
			.createAlias("pf.ideOcupacao", "o", JoinType.INNER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("pf.usuario", "u", JoinType.INNER_JOIN)
			.createAlias("u.idePerfil", "p", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("a.ideArea", pessoaFisica.getFuncionario().getIdeArea().getIdeArea()))
			.add(Restrictions.not(Restrictions.in("p.idePerfil", perfisNaoExibir())))
			.add(Restrictions.eq("u.indTipoUsuario", true))
			.add(Restrictions.eq("u.indExcluido", false))
			.add(Restrictions.eq("indExcluido", false))
			
		;
		
		if(!Util.isNullOuVazio(pessoaFisica.getIdeOcupacao())) {
			criteria.add(Restrictions.eq("o.ideOcupacao", pessoaFisica.getIdeOcupacao().getIdeOcupacao()));
		}
		
		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.property("idePessoaFisica"),"idePessoaFisica")
				.add(Projections.property("pf.idePessoaFisica"),"pessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"),"pessoaFisica.nomPessoa")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Funcionario.class))
			.addOrder(Order.asc("pf.nomPessoa"));
			
		;
		
		return funcionarioDAO.listarPorCriteria(criteria);
	}
	
	private Integer[] perfisNaoExibir() {
		return new Integer[] {
			PerfilEnum.COORDENADOR.getId(),
			PerfilEnum.COORD_CTGA.getId()
		};
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<IntegranteEquipe> listarIntegranteEquipe(Integer ideEquipe) throws Exception {
		return integranteEquipeDAOImpl.listarIntegranteEquipe(ideEquipe);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<IntegranteEquipe> listarIntegranteEquipeComAtos(Integer ideEquipe) throws Exception {
		return integranteEquipeDAOImpl.listarIntegranteEquipeComListaAtos(ideEquipe);
	}
	
	public PessoaFisica filtrarPessoaFisica(PessoaFisica pPessoaFisica) {

		return pessoaFisicaDAOImpl.getPessoaFisica(pPessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Equipe> listarEquipe(Integer ideProcesso) throws Exception {
		return equipeDAOImpl.listarEquipe(ideProcesso);
	}
}