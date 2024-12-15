package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.ProcessoAtoDAOImpl;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TransferenciaAmbiental;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoAtoService {
	
	@Inject
	private IDAO<ProcessoAto> processoAtoDAO;
	
	@Inject 
	private IDAO<ControleProcessoAto> controleProcessoAtoDAO;
	
	@Inject 
	private IDAO<StatusProcessoAto> statusProcessoAto;
	
	@Inject
	private IDAO<TransferenciaAmbiental> transferenciaAmbientalDAO;
	
	@Inject
	private ProcessoAtoDAOImpl processoAtoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ControleProcessoAto> maxControleProcessoAto(int ideProcessoAto)  {		 
		List<ControleProcessoAto> controleProcessoAtoList = new ArrayList<ControleProcessoAto>();
		ControleProcessoAto  controleProcessoAto = new ControleProcessoAto();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleProcessoAto.class);
	
		 try{ 
			 criteria
			 	.add(Property.forName("ideControleProcessoAto").eq(
			 			
			 			DetachedCriteria.forClass(ControleProcessoAto.class)
			 			.createAlias("ideProcessoAto", "ideProcessoAto")
			 			.createAlias("ideStatusProcessoAto", "ideStatusProcessoAto")
			 			.setProjection(Projections.max("ideControleProcessoAto"))
			 			.add(Restrictions.eq("ideProcessoAto.ideProcessoAto", ideProcessoAto))
			 			.add(Restrictions.eq("ideProcessoAto.indExcluido", false))
			 			))

			 		.setProjection(Projections.projectionList()
						.add(Projections.property("ideControleProcessoAto"),"ideControleProcessoAto")
						.add(Projections.property("ideProcessoAto"),"ideProcessoAto")
						.add(Projections.property("ideStatusProcessoAto"),"ideStatusProcessoAto")
						
				).setResultTransformer(new AliasToNestedBeanResultTransformer(ControleProcessoAto.class));
					 
			controleProcessoAto = controleProcessoAtoDAO.buscarPorCriteria(criteria);
			
			if(!Util.isNullOuVazio(controleProcessoAto)){
				controleProcessoAtoList.add(controleProcessoAto);
			}
			
		 }
		 catch(Exception e){
			 Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		 }
		 
		 return controleProcessoAtoList;
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoAto getProcessoAtoByProcessoByAtoAmbiental(Processo processo, AtoAmbiental atoAmbiental, Tipologia ideTipologia) {
		try {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProcessoAto.class)
				.createAlias("processo", "processo", JoinType.INNER_JOIN)
				.createAlias("atoAmbiental", "ideAtoAmbiental" ,JoinType.INNER_JOIN)
				.createAlias("tipologia", "ideTipologia",JoinType.LEFT_OUTER_JOIN)
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideProcessoAto"),"ideProcessoAto")
					.add(Projections.property("indExcluido"),"indExcluido")
				)
				
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class))
				
				.add(Restrictions.eq("ideAtoAmbiental.ideAtoAmbiental", atoAmbiental.getIdeAtoAmbiental()));
			
				if(!Util.isNullOuVazio(processo.getNumProcesso())){
					detachedCriteria.add(Restrictions.eq("processo.numProcesso", processo.getNumProcesso().toUpperCase()));
					detachedCriteria.add(Restrictions.eq("indExcluido", false));
				}else{
					detachedCriteria.add(Restrictions.eq("processo.ideProcesso", processo.getIdeProcesso()));
				}
			
			
				if(!Util.isNullOuVazio(ideTipologia) && !Util.isNullOuVazio(ideTipologia.getIdeTipologia())){
					detachedCriteria.add(Restrictions.eq("ideTipologia.ideTipologia", ideTipologia.getIdeTipologia()));
				}
			
			return processoAtoDAO.buscarPorCriteriaMaxResult(detachedCriteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusProcessoAto getStatusProcessoAto(int ideStatusProcessoAto){
		DetachedCriteria criteria = DetachedCriteria.forClass(StatusProcessoAto.class)
				.add(Restrictions.eq("ideStatusProcessoAto", ideStatusProcessoAto));
		return statusProcessoAto.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ProcessoAto pProcessoAto)  {
		processoAtoDAO.salvar(pProcessoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ProcessoAto pProcessoAto)  {
		processoAtoDAO.salvarOuAtualizar(pProcessoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(Collection<ProcessoAto> pProcessosAtos)  {
		processoAtoDAO.salvarEmLote((List<ProcessoAto>) pProcessosAtos);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ProcessoAto> filtrarListaAtosAmbientalPorProcessos(List<VwConsultaProcesso> listaProcessos)  {
		 Integer[] idsDosProcessosIntegers = new Integer[listaProcessos.size()];
		 for (int i = 0; i < listaProcessos.size(); i++) {
			idsDosProcessosIntegers[i] = listaProcessos.get(i).getIdeProcesso(); 
		}
		 if (idsDosProcessosIntegers.length > 0) {
			 DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAto.class);
			 criteria.createAlias("atoAmbiental", "atoAmbiental");
			 criteria.createAlias("processo", "processo");
			 criteria.add(Restrictions.in("processo.ideProcesso", idsDosProcessosIntegers));
			 criteria.add(Restrictions.eq("indExcluido", false));
			 
			 return processoAtoDAO.listarPorCriteria(criteria);
		 } else{
			 return new ArrayList<ProcessoAto>();
		 }
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> listarAtosPorProcesso(Integer ideProcesso)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProcessoAto.class)
				.createAlias("processo", "processo", JoinType.INNER_JOIN)
				.createAlias("tipologia", "tipologia", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipologia.tipologiaGrupo", "tipologiaGrupo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("atoAmbiental", "ato", JoinType.INNER_JOIN)
				.createAlias("ato.ideTipoAto", "tipoAto", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipoAto.ideGrupoProcesso", "grupoProcesso", JoinType.LEFT_OUTER_JOIN);
				
		ProjectionList projecao = Projections.projectionList()
				.add(Property.forName("ideProcessoAto"), "ideProcessoAto")
				.add(Property.forName("indExcluido"), "indExcluido")
				.add(Property.forName("dtcExclusao"), "dtcExclusao")
				
				.add(Property.forName("processo.ideProcesso"), "processo.ideProcesso")
				
				.add(Property.forName("ato.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
				.add(Property.forName("ato.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
				
				.add(Property.forName("tipologia.ideTipologia"), "tipologia.ideTipologia")
				.add(Property.forName("tipologia.desTipologia"), "tipologia.desTipologia")
				
				.add(Property.forName("tipoAto.ideTipoAto"), "atoAmbiental.ideTipoAto.ideTipoAto")
				
				.add(Property.forName("tipoAto.ideGrupoProcesso"), "atoAmbiental.ideTipoAto.ideGrupoProcesso");
		
		detachedCriteria
				.setProjection(projecao)
				.setProjection(Projections.distinct(projecao))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));
		
		detachedCriteria
				.add(Restrictions.eq("processo.ideProcesso", ideProcesso))
				.add(Restrictions.eq("indExcluido", false))
				//Processo reenquadrados não estão sendo listados e impede que o precesso seja deferido
				//.add(Restrictions.isNull("ideProcessoReenquadramento.ideProcessoReenquadramento"))
				;
		
		return processoAtoDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> listarAtosPorProcessoReenquadramento(Integer ideProcesso, Integer ideProcessoReenquadramento)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProcessoAto.class)
				.createAlias("processo", "processo", JoinType.INNER_JOIN)
				.createAlias("tipologia", "tipologia", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipologia.tipologiaGrupo", "tipologiaGrupo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("atoAmbiental", "ato", JoinType.INNER_JOIN)
				.createAlias("ato.ideTipoAto", "tipoAto", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipoAto.ideGrupoProcesso", "grupoProcesso", JoinType.LEFT_OUTER_JOIN);
				
		ProjectionList projecao = Projections.projectionList()
				.add(Property.forName("ideProcessoAto"), "ideProcessoAto")
				.add(Property.forName("indExcluido"), "indExcluido")
				.add(Property.forName("dtcExclusao"), "dtcExclusao")
				
				.add(Property.forName("processo.ideProcesso"), "processo.ideProcesso")
				
				.add(Property.forName("ato.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
				.add(Property.forName("ato.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
				
				.add(Property.forName("tipologia.ideTipologia"), "tipologia.ideTipologia")
				.add(Property.forName("tipologia.desTipologia"), "tipologia.desTipologia")
				
				.add(Property.forName("tipoAto.ideTipoAto"), "atoAmbiental.ideTipoAto.ideTipoAto")
				
				.add(Property.forName("tipoAto.ideGrupoProcesso"), "atoAmbiental.ideTipoAto.ideGrupoProcesso");
		
		detachedCriteria
				.setProjection(projecao)
				.setProjection(Projections.distinct(projecao))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));
		
		detachedCriteria
		.add(Restrictions.eq("processo.ideProcesso", ideProcesso))
				.add(Restrictions.le("ideProcessoReenquadramento.ideProcessoReenquadramento", ideProcessoReenquadramento))
				.add(Restrictions.eq("indExcluido", false));
		
		return processoAtoDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> listarAtosPorProcessoReequadramento(Integer ideProcesso)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProcessoAto.class);
		
		detachedCriteria
			.createAlias("ideProcessoReenquadramento", "pr", JoinType.INNER_JOIN)
			.createAlias("pr.ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("tipologia", "t", JoinType.LEFT_OUTER_JOIN)
			.createAlias("t.tipologiaGrupo", "tg", JoinType.LEFT_OUTER_JOIN)
			.createAlias("atoAmbiental", "aa", JoinType.INNER_JOIN)
			.createAlias("aa.ideTipoAto", "ta", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ta.ideGrupoProcesso", "grupoProcesso", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			.add(Restrictions.eq("indExcluido", false))
			
			.setProjection(Projections.projectionList()
				.add(Property.forName("ideProcessoAto"), "ideProcessoAto")
				.add(Property.forName("indExcluido"), "indExcluido")
				.add(Property.forName("dtcExclusao"), "dtcExclusao")
				.add(Property.forName("p.ideProcesso"), "processo.ideProcesso")
				.add(Property.forName("aa.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
				.add(Property.forName("aa.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
				.add(Property.forName("t.ideTipologia"), "tipologia.ideTipologia")
				.add(Property.forName("t.desTipologia"), "tipologia.desTipologia")
				.add(Property.forName("ta.ideTipoAto"), "atoAmbiental.ideTipoAto.ideTipoAto")
				.add(Property.forName("pr.ideProcessoReenquadramento"), "ideProcessoReenquadramento.ideProcessoReenquadramento")
			)
		
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));
		
		return processoAtoDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> listarAtosTlaByProcesso(Integer ideProcesso, boolean indVisivelTla)  {
	
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProcessoAto.class)
				.createAlias("processo", "processo", JoinType.INNER_JOIN)
				.createAlias("tipologia", "tipologia", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipologia.tipologiaGrupo", "tipologiaGrupo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("atoAmbiental", "ato", JoinType.INNER_JOIN)
				.createAlias("ato.ideTipoAto", "tipoAto", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipoAto.ideGrupoProcesso", "grupoProcesso", JoinType.LEFT_OUTER_JOIN);
				
		ProjectionList projecao = Projections.projectionList()
				.add(Property.forName("ideProcessoAto"), "ideProcessoAto")
				.add(Property.forName("indExcluido"), "indExcluido")
				.add(Property.forName("dtcExclusao"), "dtcExclusao")
				.add(Property.forName("processo.ideProcesso"), "processo.ideProcesso")
				.add(Property.forName("ato.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
				.add(Property.forName("ato.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
				.add(Property.forName("ato.indVisivelSolicitacaoTla"), "atoAmbiental.indVisivelSolicitacaoTla")
				.add(Property.forName("tipologia.ideTipologia"), "tipologia.ideTipologia")
				.add(Property.forName("tipologia.desTipologia"), "tipologia.desTipologia")
				.add(Property.forName("tipoAto.ideTipoAto"), "atoAmbiental.ideTipoAto.ideTipoAto")
				.add(Property.forName("tipoAto.ideGrupoProcesso"), "atoAmbiental.ideTipoAto.ideGrupoProcesso");
		detachedCriteria
				.setProjection(projecao)
				.setProjection(Projections.distinct(projecao))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));
		
		detachedCriteria.add(Restrictions.eq("processo.ideProcesso", ideProcesso));
		detachedCriteria.add(Restrictions.eq("ato.indVisivelSolicitacaoTla", indVisivelTla));
		detachedCriteria.add(Restrictions.eq("indExcluido", false));
		
		return processoAtoDAO.listarPorCriteria(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean possuiTransferenciaAmbiental(Integer ideProcessoAto)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransferenciaAmbiental.class)
				.add(Restrictions.eq("ideProcessoAto.ideProcessoAto", ideProcessoAto));
		return !Util.isNullOuVazio(transferenciaAmbientalDAO.listarPorCriteria(detachedCriteria));
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)	
	public List<ProcessoAto> getProcessoAtoProcessoPai (Processo processo) {
		return processoAtoDAOImpl.getProcessoAtoByProcessoPai(processo);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)	
	public boolean isProcessoTla(Processo processo) {
		return processoAtoDAOImpl.isProcessoTla(processo);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)	
	public List<ProcessoAto> listarAtosPorProcessoComOuSemStatus(Integer ideProcesso, boolean comStatus, StatusProcessoAtoEnum spaEnum) {
		return processoAtoDAOImpl.listarAtosPorProcessoComOuSemStatus(ideProcesso, comStatus, spaEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> listarAtosPorProcesso(Processo ideProcesso) {
		return processoAtoDAOImpl.listarAtosPorProcesso(ideProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoAto getProcessoAtoByProcessoByAtoAmbientalReenquadramento(Processo processo, AtoAmbiental atoAmbiental, Tipologia ideTipologia, Integer ideProcessoReenquadramento) {
		DetachedCriteria detachedCriteria =DetachedCriteria.forClass(ProcessoAto.class)
			.createAlias("processo", "processo", JoinType.INNER_JOIN)
			.createAlias("atoAmbiental", "ideAtoAmbiental" ,JoinType.INNER_JOIN)
			.createAlias("tipologia", "ideTipologia",JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("ideAtoAmbiental.ideAtoAmbiental", atoAmbiental.getIdeAtoAmbiental()))
			.add(Restrictions.eq("processo.numProcesso", processo.getNumProcesso().toUpperCase()))
			.add(Restrictions.eq("indExcluido", false))
			.add(Restrictions.eq("ideProcessoReenquadramento.ideProcessoReenquadramento", ideProcessoReenquadramento));
			
			if(!Util.isNullOuVazio(ideTipologia) && !Util.isNullOuVazio(ideTipologia.getIdeTipologia())){
				detachedCriteria.add(Restrictions.eq("ideTipologia.ideTipologia", ideTipologia.getIdeTipologia()));
			}
			detachedCriteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcessoAto"),"ideProcessoAto")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));
		
		return processoAtoDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Object[]> listarFinalidadeReenquadramentoProcessoPorProceso(Integer ideProcesso){
		return processoAtoDAOImpl.listarFinalidadeReenquadramentoProcessoPorProceso(ideProcesso);
	}
}