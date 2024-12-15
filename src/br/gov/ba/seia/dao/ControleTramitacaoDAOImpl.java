package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ControleTramitacaoDAOImpl {
	
	@Inject
	private IDAO<ControleTramitacao> controleTramitacaoDAO;
	@EJB
	private PautaDAOImpl pautaDAOImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ControleTramitacao controleTramitacao) {
		controleTramitacaoDAO.salvar(controleTramitacao);
		controleTramitacaoDAO.sessionFlush();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ControleTramitacao controleTramitacao) {
		controleTramitacaoDAO.salvarOuAtualizar(controleTramitacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(List<ControleTramitacao> lista) {
		controleTramitacaoDAO.salvarEmLote(lista);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ControleTramitacao controleTramitacao) {
		controleTramitacaoDAO.atualizar(controleTramitacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void resetarTramitacaoPorProcesso(Integer ideProcesso) {
		String sqL = "update ControleTramitacao ct set ct.indFimDaFila = false where ct.ideProcesso.ideProcesso = :ideProcesso";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideProcesso", ideProcesso);
		
		controleTramitacaoDAO.executarQuery(sqL, params);
		controleTramitacaoDAO.sessionFlush();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void resetarTramitacaoPor(Integer ideProcesso, Integer idePessoaFisica){
		
		StringBuilder sql = new StringBuilder();
		
		sql
			.append("update ControleTramitacao ct1 set ct1.indFimDaFila = false ")
			.append("where ct1.ideControleTramitacao = (select ct2.ideControleTramitacao ")
			.append("									from ControleTramitacao ct2 ")
			.append("											inner join ct2.ideProcesso p ")
			.append("											inner join ct2.idePauta pa ")
			.append("											inner join pa.idePessoaFisica fun ")
			.append("									where p.ideProcesso = :ideProcesso ")
			.append("											and fun.idePessoaFisica = :idePessoaFisica) ")
		;
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideProcesso", ideProcesso);
		params.put("idePessoaFisica", idePessoaFisica);
		
		controleTramitacaoDAO.executarQuery(sql.toString(), params);
		controleTramitacaoDAO.sessionFlush();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarStatusFluxoProcessoConcluido(Integer ideRequerimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class)
			.createAlias("ideProcesso", "proc", JoinType.INNER_JOIN)
			.createAlias("proc.ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideControleTramitacao"),"ideControleTramitacao")
				.add(Projections.property("dtcTramitacao"),"dtcTramitacao")
				
				.add(Projections.property("proc.ideProcesso"),"ideProcesso.ideProcesso")
			)
			
			.add(Restrictions.eq("st.ideStatusFluxo", StatusFluxoEnum.CONCLUIDO.getStatus()))
			.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
			.add(Restrictions.eq("indFimDaFila", true))
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleTramitacao.class));
		
		return controleTramitacaoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer numeroControleTramitacaoEmAnaliseTecnicaAtivo(Integer ideProcesso) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("indFimDaFila", true))
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			.add(Restrictions.eq("st.ideStatusFluxo", StatusFluxoEnum.ANALISE_TECNICA.getStatus()))
			
			.setProjection(Projections.rowCount())
		;
		
		Object o = controleTramitacaoDAO.buscarPorCriteria(criteria);
		
		return Integer.valueOf(o.toString());
		
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarUltimoPorProcesso(Integer ideProcesso) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
			.createAlias("idePauta", "pt", JoinType.INNER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("a.idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("a.ideAreaPai", "ap", JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			
			.add(Property.forName("ideControleTramitacao").eq(
					DetachedCriteria.forClass(ControleTramitacao.class)
						.createAlias("ideProcesso", "proc", JoinType.INNER_JOIN)
						.add(Restrictions.eq("proc.ideProcesso",ideProcesso))
						.setProjection(Projections.max("ideControleTramitacao"))
			))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideControleTramitacao"),"ideControleTramitacao")
				.add(Projections.property("dtcTramitacao"),"dtcTramitacao")
				.add(Projections.property("indFimDaFila"),"indFimDaFila")
				.add(Projections.property("dscComentarioExterno"),"dscComentarioExterno")
				.add(Projections.property("dscComentarioInterno"),"dscComentarioInterno")
				.add(Projections.property("indResponsavel"),"indResponsavel")
				.add(Projections.property("indAreaSecundaria"),"indAreaSecundaria")
				.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("a.nomArea"),"ideArea.nomArea")
				.add(Projections.property("fun.idePessoaFisica"),"ideArea.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("ap.ideArea"),"ideArea.ideAreaPai.ideArea")
				.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pt.idePauta"),"idePauta.idePauta")
				.add(Projections.property("st.ideStatusFluxo"),"ideStatusFluxo.ideStatusFluxo")
				.add(Projections.property("st.dscStatusFluxo"),"ideStatusFluxo.dscStatusFluxo")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleTramitacao.class))		
		;
		
		return controleTramitacaoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarUltimoPorProcessoComTresStatus(Integer ideProcesso) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
			.createAlias("idePauta", "pt", JoinType.INNER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("a.idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("a.ideAreaPai", "ap", JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			
			.add(Property.forName("ideControleTramitacao").eq(
					DetachedCriteria.forClass(ControleTramitacao.class)
						.createAlias("ideProcesso", "proc", JoinType.INNER_JOIN)
						.add(Restrictions.eq("proc.ideProcesso",ideProcesso))
						.add(Restrictions.in("ideStatusFluxo.ideStatusFluxo", new Object[] {StatusFluxoEnum.FORMADO.getStatus(), StatusFluxoEnum.ENCAMINHADO_PARA_AREA.getStatus(), StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus()}))
						.setProjection(Projections.max("ideControleTramitacao"))
			))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideControleTramitacao"),"ideControleTramitacao")
				.add(Projections.property("dtcTramitacao"),"dtcTramitacao")
				.add(Projections.property("indFimDaFila"),"indFimDaFila")
				.add(Projections.property("dscComentarioExterno"),"dscComentarioExterno")
				.add(Projections.property("dscComentarioInterno"),"dscComentarioInterno")
				.add(Projections.property("indResponsavel"),"indResponsavel")
				.add(Projections.property("indAreaSecundaria"),"indAreaSecundaria")
				.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("a.nomArea"),"ideArea.nomArea")
				.add(Projections.property("fun.idePessoaFisica"),"ideArea.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("ap.ideArea"),"ideArea.ideAreaPai.ideArea")
				.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pt.idePauta"),"idePauta.idePauta")
				.add(Projections.property("st.ideStatusFluxo"),"ideStatusFluxo.ideStatusFluxo")
				.add(Projections.property("st.dscStatusFluxo"),"ideStatusFluxo.dscStatusFluxo")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleTramitacao.class))		
		;
		
		return controleTramitacaoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer numeroControleTramitacaoLiderPorTecnico(Integer idePessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		
		criteria
		.createAlias("idePauta", "pta", JoinType.INNER_JOIN)
		.createAlias("pta.idePessoaFisica", "fun", JoinType.INNER_JOIN)
		.createAlias("pta.ideTipoPauta", "t", JoinType.INNER_JOIN)
		
		.add(Restrictions.eq("indFimDaFila", true))
		.add(Restrictions.eq("indResponsavel", true))
		.add(Restrictions.eq("t.ideTipoPauta", TipoPautaEnum.PAUTA_TECNICA.getTipo()))
		.add(Restrictions.eq("fun.idePessoaFisica", idePessoaFisica))
		
		.setProjection(Projections.rowCount())
		;
		
		Object o = controleTramitacaoDAO.buscarPorCriteria(criteria);
		
		return Integer.valueOf(o.toString());
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Integer numeroControleTramitacaoPorTecnico(Integer idePessoaFisica)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		
		criteria
			.createAlias("idePauta", "pta", JoinType.INNER_JOIN)
			.createAlias("pta.idePessoaFisica", "fun", JoinType.INNER_JOIN)
			.createAlias("pta.ideTipoPauta", "t", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("indFimDaFila", true))
			.add(Restrictions.eq("t.ideTipoPauta", TipoPautaEnum.PAUTA_TECNICA.getTipo()))
			.add(Restrictions.eq("fun.idePessoaFisica", idePessoaFisica))
			
			.setProjection(Projections.rowCount())
		;
		
		Object o = controleTramitacaoDAO.buscarPorCriteria(criteria);
		
		return Integer.valueOf(o.toString());
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarPenultimoPorProcesso(Processo processo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ct ");
		sql.append("from ControleTramitacao ct ");
		sql.append("     inner join fetch ct.ideStatusFluxo ");
		sql.append("where ct = (select max(ct) ");
		sql.append("            from ControleTramitacao ct ");
		sql.append("            where ct.indFimDaFila = :indFimDaFila ");
		sql.append("                  and ct.ideProcesso = :ideProcesso) ");
		
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("ideProcesso", processo);
		params.put("indFimDaFila", false);
		
		return controleTramitacaoDAO.buscarPorQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<ControleTramitacao> getControlesTramitacao(ControleTramitacao pControleTramitacao) {
    	
    	DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
    	criteria
    		.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
    		.createAlias("ideArea", "a", JoinType.INNER_JOIN)
    		.createAlias("idePauta", "pta", JoinType.INNER_JOIN)
    		.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
    		.createAlias("idePessoaFisica", "pf", JoinType.INNER_JOIN)
    		.createAlias("pta.idePessoaFisica", "fun", JoinType.INNER_JOIN)
    	;
    	
    	if (!Util.isNull(pControleTramitacao.getIdeProcesso()) 
    		&& !Util.isNull(pControleTramitacao.getIdeProcesso().getIdeProcesso())) {
    		criteria.add(Restrictions.eq("p.ideProcesso", pControleTramitacao.getIdeProcesso().getIdeProcesso()));
    	}
    	
    	if (!Util.isNull(pControleTramitacao.getIdePauta()) 
    		&& !Util.isNull(pControleTramitacao.getIdePauta().getIdePauta())) {
    		criteria.add(Restrictions.eq("pta.idePauta", pControleTramitacao.getIdePauta().getIdePauta()));
    	}
    	
    	if (!Util.isNull(pControleTramitacao.getIdePauta()) 
    		&& !Util.isNull(pControleTramitacao.getIdePauta().getIdePessoaFisica()) 
    		&& !Util.isNull(pControleTramitacao.getIdePauta().getIdePessoaFisica().getIdePessoaFisica())) {
    		criteria.add(Restrictions.eq("fun.idePessoaFisica", pControleTramitacao.getIdePauta().getIdePessoaFisica().getIdePessoaFisica()));
    	}
    	
    	if (!Util.isNull(pControleTramitacao.getIdePauta()) 
    			&& !Util.isNull(pControleTramitacao.getIdePauta().getIdePessoaFisica()) 
    			&& !Util.isNull(pControleTramitacao.getIdePauta().getIdePessoaFisica().getIdePessoaFisica())) {
    		criteria.add(Restrictions.eq("fun.idePessoaFisica", pControleTramitacao.getIdePauta().getIdePessoaFisica().getIdePessoaFisica()));
    	}
    	
    	if (!Util.isNull(pControleTramitacao.getIndFimDaFila())) {
    		criteria.add(Restrictions.eq("indFimDaFila", pControleTramitacao.getIndFimDaFila()));
    	}
    	
    	criteria
    		.setProjection(Projections.projectionList()
    			.add(Projections.property("ideControleTramitacao"),"ideControleTramitacao")
    			.add(Projections.property("dtcTramitacao"),"dtcTramitacao")
    			.add(Projections.property("indFimDaFila"),"indFimDaFila")
    			.add(Projections.property("dscComentarioExterno"),"dscComentarioExterno")
    			.add(Projections.property("dscComentarioInterno"),"dscComentarioInterno")
    			.add(Projections.property("indResponsavel"),"indResponsavel")
    			.add(Projections.property("indAreaSecundaria"),"indAreaSecundaria")
    			.add(Projections.property("a.ideArea"),"ideArea.ideArea")
    			.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
    			.add(Projections.property("pta.idePauta"),"idePauta.idePauta")
    			.add(Projections.property("st.ideStatusFluxo"),"ideStatusFluxo.ideStatusFluxo")
    			.add(Projections.property("st.dscStatusFluxo"),"ideStatusFluxo.dscStatusFluxo")
    			.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
    		)
    		.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleTramitacao.class))
    		.addOrder(Order.asc("ideControleTramitacao"))
    	;
    	
    	try {
			return controleTramitacaoDAO.listarPorCriteria(criteria);
		}
    	catch (Exception e) {
    		Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null; 
		}
    }
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ControleTramitacao> listarControleTramitacaoTecnicos(Integer ideProcesso) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
			.createAlias("idePauta", "pt", JoinType.INNER_JOIN)
			.createAlias("idePessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("pt.ideTipoPauta", "t", JoinType.INNER_JOIN)
			.createAlias("pt.ideArea", "apt", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pt.idePessoaFisica", "funp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("a.idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("fun.pessoaFisica", "pfa", JoinType.LEFT_OUTER_JOIN)
			
			.add(Property.forName("ideControleTramitacao").in(
				DetachedCriteria.forClass(ControleTramitacao.class)
					
					.createAlias("idePauta", "pta", JoinType.INNER_JOIN)
					.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
					.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
					
					.add(Restrictions.eq("p.ideProcesso", ideProcesso))
					.add(Restrictions.eq("st.ideStatusFluxo", StatusFluxoEnum.ANALISE_TECNICA.getStatus()))
					.add(Restrictions.in("idePauta", pautaDAOImpl.listarPautaIntegraEquipe(ideProcesso)))
					
					.setProjection(Projections.projectionList()
						.add(Projections.sqlGroupProjection("max({alias}.ide_controle_tramitacao)", "pta1_.ide_pauta", new String[] {"ide_controle_tramitacao","ide_pauta"}, new Type[] {StandardBasicTypes.INTEGER,StandardBasicTypes.INTEGER}))
					)
			))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideControleTramitacao"),"ideControleTramitacao")
				.add(Projections.property("dtcTramitacao"),"dtcTramitacao")
				.add(Projections.property("indFimDaFila"),"indFimDaFila")
				.add(Projections.property("dscComentarioExterno"),"dscComentarioExterno")
				.add(Projections.property("dscComentarioInterno"),"dscComentarioInterno")
				.add(Projections.property("indResponsavel"),"indResponsavel")
				.add(Projections.property("indAreaSecundaria"),"indAreaSecundaria")
				.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("a.nomArea"),"ideArea.nomArea")
				.add(Projections.property("fun.idePessoaFisica"),"ideArea.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pfa.idePessoaFisica"),"ideArea.idePessoaFisica.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pfa.nomPessoa"),"ideArea.idePessoaFisica.pessoaFisica.nomPessoa")
				.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"),"idePessoaFisica.nomPessoa")
				.add(Projections.property("pt.idePauta"),"idePauta.idePauta")
				.add(Projections.property("apt.ideArea"),"idePauta.ideArea.ideArea")
				.add(Projections.property("apt.nomArea"),"idePauta.ideArea.nomArea")
				.add(Projections.property("funp.idePessoaFisica"),"idePauta.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("t.ideTipoPauta"),"idePauta.ideTipoPauta.ideTipoPauta")
				.add(Projections.property("st.ideStatusFluxo"),"ideStatusFluxo.ideStatusFluxo")
				.add(Projections.property("st.dscStatusFluxo"),"ideStatusFluxo.dscStatusFluxo")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleTramitacao.class))
			.addOrder(Order.asc("dtcTramitacao"))
		;
		
		return controleTramitacaoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ControleTramitacao> listarControleTramitacaoPorIdeProcesso(Integer ideProcesso) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
			.createAlias("idePauta", "pt", JoinType.INNER_JOIN)
			.createAlias("idePessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("a.ideAreaPai", "ap", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pt.ideTipoPauta", "t", JoinType.INNER_JOIN)
			.createAlias("pt.ideArea", "apt", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pt.idePessoaFisica", "funp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("a.idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("fun.pessoaFisica", "pfa", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideControleTramitacao"),"ideControleTramitacao")
				.add(Projections.property("dtcTramitacao"),"dtcTramitacao")
				.add(Projections.property("indFimDaFila"),"indFimDaFila")
				.add(Projections.property("dscComentarioExterno"),"dscComentarioExterno")
				.add(Projections.property("dscComentarioInterno"),"dscComentarioInterno")
				.add(Projections.property("indResponsavel"),"indResponsavel")
				.add(Projections.property("indAreaSecundaria"),"indAreaSecundaria")
				.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("a.nomArea"),"ideArea.nomArea")
				.add(Projections.property("ap.ideArea"),"ideArea.ideAreaPai.ideArea")
				.add(Projections.property("fun.idePessoaFisica"),"ideArea.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pfa.idePessoaFisica"),"ideArea.idePessoaFisica.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pfa.nomPessoa"),"ideArea.idePessoaFisica.pessoaFisica.nomPessoa")
				.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"),"idePessoaFisica.nomPessoa")
				.add(Projections.property("pt.idePauta"),"idePauta.idePauta")
				.add(Projections.property("apt.ideArea"),"idePauta.ideArea.ideArea")
				.add(Projections.property("apt.nomArea"),"idePauta.ideArea.nomArea")
				.add(Projections.property("funp.idePessoaFisica"),"idePauta.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("t.ideTipoPauta"),"idePauta.ideTipoPauta.ideTipoPauta")
				.add(Projections.property("st.ideStatusFluxo"),"ideStatusFluxo.ideStatusFluxo")
				.add(Projections.property("st.dscStatusFluxo"),"ideStatusFluxo.dscStatusFluxo")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleTramitacao.class))
			.addOrder(Order.asc("dtcTramitacao"))
		;
		
		return controleTramitacaoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarUltimaTramitacaoPorProcessoCoordenador(Integer ideProcesso, Integer idePessoaFisica, boolean areaSegundaria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);		
		criteria
			.createAlias("ideProcesso","proc",JoinType.INNER_JOIN)
			.createAlias("idePauta","pauta",JoinType.INNER_JOIN)
			.createAlias("pauta.idePessoaFisica","pFunc",JoinType.LEFT_OUTER_JOIN)
			.createAlias("pauta.ideArea","area",JoinType.LEFT_OUTER_JOIN)
			.createAlias("area.idePessoaFisica","aFunc",JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("indFimDaFila", true))
			.add(Restrictions.eq("proc.ideProcesso", ideProcesso));
			
			if(areaSegundaria){
				criteria.add(Restrictions.eq("indAreaSecundaria", areaSegundaria));
			}else{
				criteria.add(Restrictions.or(Restrictions.eq("indAreaSecundaria", areaSegundaria),Restrictions.isNull("indAreaSecundaria")));
			}
			
			criteria.add(Restrictions.or(
					Restrictions.eq("aFunc.idePessoaFisica", idePessoaFisica),
					Restrictions.eq("pFunc.idePessoaFisica", idePessoaFisica)
				)
			)
			
			.setProjection(
				Projections.projectionList()
				.add(Projections.property("ideControleTramitacao"),"ideControleTramitacao")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleTramitacao.class));		
		
		
		return controleTramitacaoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarUltimaTramitacaPorProcessoCoordenadorIndAreaSecundariaTrue(Integer ideProcesso, Integer idePessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);		
		criteria
			.createAlias("ideProcesso","proc",JoinType.INNER_JOIN)
			.createAlias("idePauta","pauta",JoinType.INNER_JOIN)
			.createAlias("pauta.idePessoaFisica","pFunc",JoinType.LEFT_OUTER_JOIN)
			.createAlias("pauta.ideArea","area",JoinType.LEFT_OUTER_JOIN)
			.createAlias("area.idePessoaFisica","aFunc",JoinType.LEFT_OUTER_JOIN)
			
			
			.add(Restrictions.eq("indFimDaFila", true))
			.add(Restrictions.eq("proc.ideProcesso", ideProcesso))
			.add(Restrictions.or(
					Restrictions.eq("indAreaSecundaria", true),
					Restrictions.isNull("indAreaSecundaria")
				)
			)
			.add(Restrictions.or(
					Restrictions.eq("aFunc.idePessoaFisica", idePessoaFisica),
					Restrictions.eq("pFunc.idePessoaFisica", idePessoaFisica)
				)
			)
			
			.setProjection(
				Projections.projectionList()
				.add(Projections.property("ideControleTramitacao"),"ideControleTramitacao")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleTramitacao.class));		
		;
		
		return controleTramitacaoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarControleTramitacaoConcluidoPor(Integer ideProcesso){
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		criteria.add(Restrictions.eq("ideProcesso.ideProcesso", ideProcesso));
		criteria.add(Restrictions.eq("ideStatusFluxo.ideStatusFluxo", StatusFluxoEnum.CONCLUIDO.getStatus()));
		
		return controleTramitacaoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ControleTramitacao> retornarFinaisDeFilaPorProcessoPorArea(Processo pProcesso, Area pArea){
		
		StringBuilder sql = new StringBuilder();		
		Map<String,Object> parametros = new HashMap<String, Object>();
		parametros.put("ideProcesso", pProcesso);
		parametros.put("ideArea", pArea);
		
		sql.append("select ct ");
		sql.append("from ControleTramitacao ct ");
		sql.append("where ct.ideProcesso = :ideProcesso ");
		sql.append("      and ct.ideArea = :ideArea ");
		sql.append("      and ct.indFimDaFila = true ");
		
		return controleTramitacaoDAO.listarPorQuery(sql.toString(), parametros);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarControleTramitacaoAguardandoAprovacaoNotificacaoUltimoPorProcesso(Integer processo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ct ");
		sql.append("from ControleTramitacao ct ");
		sql.append("     inner join fetch ct.ideStatusFluxo ");
		sql.append("where ct = (select max(ct) ");
		sql.append("            from ControleTramitacao ct ");
		sql.append("            where ct.indFimDaFila = :indFimDaFila ");
		sql.append("                  and ct.ideProcesso.ideProcesso = :ideProcesso) ");
		sql.append("                  and ct.ideStatusFluxo.ideStatusFluxo = :ideStatusFluxo) ");
		
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("ideProcesso", processo);
		params.put("indFimDaFila", false);
		params.put("ideStatusFluxo", StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO.getStatus());
		
		return controleTramitacaoDAO.buscarPorQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarUltimoPorProcessoPautaCoordenador(Integer ideProcesso){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
			.createAlias("idePauta", "pt", JoinType.INNER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("a.idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("a.ideAreaPai", "ap", JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN);
		
		
		criteria
			.add(Property.forName("ideControleTramitacao").eq(DetachedCriteria.forClass(ControleTramitacao.class)
					.createAlias("ideProcesso", "proc", JoinType.INNER_JOIN)
					.createAlias("ideStatusFluxo", "stf", JoinType.INNER_JOIN)
					.createAlias("idePauta", "pt", JoinType.INNER_JOIN)
					.createAlias("pt.ideTipoPauta", "tp", JoinType.INNER_JOIN)
					.add(Restrictions.eq("tp.ideTipoPauta", TipoPautaEnum.PAUTA_COORDENADOR_AREA.getTipo()))
					.add(Restrictions.eq("proc.ideProcesso",ideProcesso))
					.add(Restrictions.eq("stf.ideStatusFluxo", StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO.getStatus()))
					.setProjection(Projections.max("ideControleTramitacao"))
			))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideControleTramitacao"),"ideControleTramitacao")
				.add(Projections.property("dtcTramitacao"),"dtcTramitacao")
				.add(Projections.property("indFimDaFila"),"indFimDaFila")
				.add(Projections.property("dscComentarioExterno"),"dscComentarioExterno")
				.add(Projections.property("dscComentarioInterno"),"dscComentarioInterno")
				.add(Projections.property("indResponsavel"),"indResponsavel")
				.add(Projections.property("indAreaSecundaria"),"indAreaSecundaria")
				.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("a.nomArea"),"ideArea.nomArea")
				.add(Projections.property("fun.idePessoaFisica"),"ideArea.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("ap.ideArea"),"ideArea.ideAreaPai.ideArea")
				.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pt.idePauta"),"idePauta.idePauta")
				.add(Projections.property("st.ideStatusFluxo"),"ideStatusFluxo.ideStatusFluxo")
				.add(Projections.property("st.dscStatusFluxo"),"ideStatusFluxo.dscStatusFluxo")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleTramitacao.class))		
		;
		
		return controleTramitacaoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ControleTramitacao> listarControleTramitacaoTecnicosArea(Integer ideProcesso, Integer ideArea) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
			.createAlias("idePauta", "pt", JoinType.INNER_JOIN)
			.createAlias("idePessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("pt.ideTipoPauta", "t", JoinType.INNER_JOIN)
			.createAlias("pt.ideArea", "apt", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pt.idePessoaFisica", "funp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("a.idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("fun.pessoaFisica", "pfa", JoinType.LEFT_OUTER_JOIN)
			
			.add(Property.forName("ideControleTramitacao").in(
				DetachedCriteria.forClass(ControleTramitacao.class)
					
					.createAlias("idePauta", "pta", JoinType.INNER_JOIN)
					.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
					.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
					
					.add(Restrictions.eq("p.ideProcesso", ideProcesso))
					.add(Restrictions.eq("st.ideStatusFluxo", StatusFluxoEnum.ANALISE_TECNICA.getStatus()))
					 //.add(Restrictions.in("idePauta", pautaDAOImpl.listarPautaIntegraEquipeArea(ideProcesso, ideArea)))

					.setProjection(Projections.projectionList()
						.add(Projections.sqlGroupProjection("max({alias}.ide_controle_tramitacao)", "pta1_.ide_pauta", new String[] {"ide_controle_tramitacao","ide_pauta"}, new Type[] {StandardBasicTypes.INTEGER,StandardBasicTypes.INTEGER}))
					)
			))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideControleTramitacao"),"ideControleTramitacao")
				.add(Projections.property("dtcTramitacao"),"dtcTramitacao")
				.add(Projections.property("indFimDaFila"),"indFimDaFila")
				.add(Projections.property("dscComentarioExterno"),"dscComentarioExterno")
				.add(Projections.property("dscComentarioInterno"),"dscComentarioInterno")
				.add(Projections.property("indResponsavel"),"indResponsavel")
				.add(Projections.property("indAreaSecundaria"),"indAreaSecundaria")
				.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("a.ideArea"),"ideArea.ideArea")
				.add(Projections.property("a.nomArea"),"ideArea.nomArea")
				.add(Projections.property("fun.idePessoaFisica"),"ideArea.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pfa.idePessoaFisica"),"ideArea.idePessoaFisica.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pfa.nomPessoa"),"ideArea.idePessoaFisica.pessoaFisica.nomPessoa")
				.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"),"idePessoaFisica.nomPessoa")
				.add(Projections.property("pt.idePauta"),"idePauta.idePauta")
				.add(Projections.property("apt.ideArea"),"idePauta.ideArea.ideArea")
				.add(Projections.property("apt.nomArea"),"idePauta.ideArea.nomArea")
				.add(Projections.property("funp.idePessoaFisica"),"idePauta.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("t.ideTipoPauta"),"idePauta.ideTipoPauta.ideTipoPauta")
				.add(Projections.property("st.ideStatusFluxo"),"ideStatusFluxo.ideStatusFluxo")
				.add(Projections.property("st.dscStatusFluxo"),"ideStatusFluxo.dscStatusFluxo")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ControleTramitacao.class))
			.addOrder(Order.asc("dtcTramitacao"))
		;
		
		return controleTramitacaoDAO.listarPorCriteria(criteria);
	}

}