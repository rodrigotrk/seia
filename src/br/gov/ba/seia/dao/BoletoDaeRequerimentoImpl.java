package br.gov.ba.seia.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.ParametroTaxaCertificado;
import br.gov.ba.seia.entity.ParametroTaxaVistoria;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class BoletoDaeRequerimentoImpl {
	
	@Inject
	IDAO<BoletoDaeRequerimento> boletoDaeRequerimentoDAO;
	
	@Inject
	IDAO<ParametroTaxaCertificado> parametroTaxaCertificadoDAO;
	
	@Inject
	IDAO<ParametroTaxaVistoria> parametroTaxaVistoriaDAO;
	
	
	public void salvarOuAtualizar(BoletoDaeRequerimento boletoDaeRequerimento) {
		boletoDaeRequerimentoDAO.salvarOuAtualizar(boletoDaeRequerimento);
	}
	
	public ParametroTaxaCertificado getParametroTaxaCertificado(){
		return parametroTaxaCertificadoDAO.carregarGet(1);
	}
	
	public List<ParametroTaxaVistoria> getAllParametroTaxaVistoria(){
		return parametroTaxaVistoriaDAO.listarTodos();
	}
	
	public ParametroTaxaVistoria getParametroTaxaVistoria(BoletoDaeRequerimento boletoDaeRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ParametroTaxaVistoria.class);
		criteria.add(Restrictions.le("valAreaMinima", boletoDaeRequerimento.getVlrAreaVistoria()));
		criteria.add(Restrictions.ge("valAreaMaximo", boletoDaeRequerimento.getVlrAreaVistoria()));
		return parametroTaxaVistoriaDAO.buscarPorCriteria(criteria);
	}
	

	public BoletoDaeRequerimento carregarVistoriaByRequerimento(Integer ideRequerimento) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoDaeRequerimento.class);
		detachedCriteria.createAlias("ideRequerimento", "req");
		
		detachedCriteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideBoletoDaeRequerimento"),"ideBoletoDaeRequerimento")
				.add(Projections.property("vlrAreaVistoria"),"vlrAreaVistoria")
				.add(Projections.property("vlrTotalVistoria"),"vlrTotalVistoria")
		);
		
		detachedCriteria.add(Restrictions.and(Restrictions.isNotNull("vlrTotalVistoria"), Restrictions.ne("vlrTotalVistoria", BigDecimal.ZERO)));
		detachedCriteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		
		detachedCriteria.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoDaeRequerimento.class));
		
		return boletoDaeRequerimentoDAO.buscarPorCriteria(detachedCriteria);
	}

	public BoletoDaeRequerimento carregarCertificadoByRequerimento(Integer ideRequerimento) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoDaeRequerimento.class);
		detachedCriteria.createAlias("ideRequerimento", "req");
		
		detachedCriteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideBoletoDaeRequerimento"),"ideBoletoDaeRequerimento")
				.add(Projections.property("indIsento"),"indIsento")
				.add(Projections.property("vlrTotalCertificado"),"vlrTotalCertificado")
		);
		
		detachedCriteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		detachedCriteria.add(Restrictions.isNotNull("vlrTotalCertificado"));
		
		detachedCriteria.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoDaeRequerimento.class));
		List<BoletoDaeRequerimento> listboletoDaeReq = boletoDaeRequerimentoDAO.listarPorCriteria(detachedCriteria, Order.desc("ideBoletoDaeRequerimento"));
		if(Util.isNullOuVazio(listboletoDaeReq))
			return null;
		else
			return listboletoDaeReq.get(0);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoDaeRequerimento> getBoletosPorIdeRequerimento(Integer ideRequerimento) {
		
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimento", ideRequerimento);
		
		return boletoDaeRequerimentoDAO.buscarPorNamedQuery("BoletoDaeRequerimento.findByIdeRequerimento", parametros);
	}
	
	public BoletoDaeRequerimento carregarCertificadoByProcessoRequerimento(Integer ideProcessoReenquadramento) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoDaeRequerimento.class);
		detachedCriteria.createAlias("ideProcesso", "pro");
		detachedCriteria.createAlias("ideProcesso.ideRequerimento", "req");
		detachedCriteria.createAlias("ideProcessoReenquadramento", "pre");
		
		detachedCriteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideBoletoDaeRequerimento"),"ideBoletoDaeRequerimento")
				.add(Projections.property("vlrTotalCertificado"),"vlrTotalCertificado")
				.add(Projections.property("vlrTotalVistoria"),"vlrTotalVistoria")
		);
		
		detachedCriteria.add(Restrictions.eq("pre.ideProcessoReenquadramento", ideProcessoReenquadramento));
		detachedCriteria.add(Restrictions.isNotNull("vlrTotalCertificado"));
		
		detachedCriteria.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoDaeRequerimento.class));
		List<BoletoDaeRequerimento> listboletoDaeReq = boletoDaeRequerimentoDAO.listarPorCriteria(detachedCriteria, Order.desc("ideBoletoDaeRequerimento"));
		if(Util.isNullOuVazio(listboletoDaeReq))
			return null;
		else
			return listboletoDaeReq.get(0);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoDaeRequerimento> getBoletosPorIdeProcessoReenquadramento(Integer ideProcesso) {
			
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideProcesso", ideProcesso);
		
		return boletoDaeRequerimentoDAO.buscarPorNamedQuery("BoletoDaeRequerimento.findByIdeProcesso", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoDaeRequerimento> listarPorIdeProcesso(Integer ideProcesso) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoDaeRequerimento.class);
		
		detachedCriteria.createAlias("ideProcesso", "processo", JoinType.INNER_JOIN);
		detachedCriteria.createAlias("ideProcesso.ideRequerimento", "requerimento", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideTipoBoletoPagamento", "tipoBoletoPagamento", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("boletoDaeHistorico", "boletoDaeHistorico", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("boletoDaeHistorico.ideMotivoCancelamentoBoleto", "motivo", JoinType.LEFT_OUTER_JOIN);
		
		detachedCriteria.add(Restrictions.eq("processo.ideProcesso", ideProcesso));
		
		return boletoDaeRequerimentoDAO.listarPorCriteria(detachedCriteria);
	}
	
}