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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReenquadramentoProcessoAtoDAOImpl {
	
	@EJB
	private ReenquadramentoTipoFinalidadeUsoAguaDAOImpl reenquadramentoTipoFinalidadeUsoAguaDAOImpl;
	
	@EJB
	private ReenquadramentoProcessoAtoObjetivoAtividadeManejoDAOImpl reenquadramentoProcessoAtoObjetivoAtividadeManejoDAOImpl;
	
	@EJB
	private ReenquadramentoProcessoAtoTipoAtividadeFaunaDAOImpl reenquadramentoProcessoAtoTipoAtividadeFaunaDAOImpl;
	
	@EJB
	private ReenquadramentoTipologiaEmpreendimentoDAOImpl reenquadramentoTipologiaEmpreendimentoDAOImpl;
	
	@Inject
	private IDAO<ReenquadramentoProcessoAto> reenquadramentoProcessoAtoDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReenquadramentoProcessoAto reenquadramentoProcessoAto)  {
		reenquadramentoProcessoAtoDAO.salvar(reenquadramentoProcessoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ReenquadramentoProcessoAto reenquadramentoProcessoAto)  {
		reenquadramentoProcessoAtoDAO.remover(reenquadramentoProcessoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPor(ReenquadramentoProcesso reenquadramentoProcesso)  {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ReenquadramentoProcessoAto rpa where rpa.ideReenquadramentoProcesso = :ideReenquadramentoProcesso");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideReenquadramentoProcesso", reenquadramentoProcesso);
		
		reenquadramentoProcessoAtoDAO.executarQuery(sql.toString(),params);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ReenquadramentoProcessoAto> listarReenquadramentoProcessoAtoPor(ReenquadramentoProcesso reenquadramentoProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReenquadramentoProcessoAto.class);
		criteria
			.add(Restrictions.eq("ideReenquadramentoProcesso", reenquadramentoProcesso))
		;
		
		return reenquadramentoProcessoAtoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ReenquadramentoProcessoAto> listarReenquadramentoProcessoAto(ReenquadramentoProcesso reenquadramentoProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReenquadramentoProcessoAto.class);
		criteria
			.createAlias("ideNovaTipologia","tN", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideNovoAtoAmbiental","aaN",JoinType.LEFT_OUTER_JOIN)
			.createAlias("aaN.ideTipoAto","taN",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideProcessoAto","pa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pa.atoAmbiental","aa",JoinType.LEFT_OUTER_JOIN)
			.createAlias("pa.processo","p", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pa.tipologia","t", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("ideReenquadramentoProcesso", reenquadramentoProcesso))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideReenquadramentoProcessoAto"),"ideReenquadramentoProcessoAto")
				.add(Projections.property("pa.ideProcessoAto"),"ideProcessoAto.ideProcessoAto")
				.add(Projections.property("aa.ideAtoAmbiental"),"ideProcessoAto.atoAmbiental.ideAtoAmbiental")
				.add(Projections.property("aa.sglAtoAmbiental"),"ideProcessoAto.atoAmbiental.sglAtoAmbiental")
				.add(Projections.property("aa.nomAtoAmbiental"),"ideProcessoAto.atoAmbiental.nomAtoAmbiental")
				.add(Projections.property("p.ideProcesso"),"ideProcessoAto.processo.ideProcesso")
				.add(Projections.property("t.ideTipologia"),"ideProcessoAto.tipologia.ideTipologia")
				.add(Projections.property("t.codTipologia"),"ideProcessoAto.tipologia.codTipologia")
				.add(Projections.property("t.desTipologia"),"ideProcessoAto.tipologia.desTipologia")
				
				.add(Projections.property("tN.ideTipologia"),"ideNovaTipologia.ideTipologia")
				.add(Projections.property("tN.codTipologia"),"ideNovaTipologia.codTipologia")
				.add(Projections.property("tN.desTipologia"),"ideNovaTipologia.desTipologia")
				.add(Projections.property("aaN.ideAtoAmbiental"),"ideNovoAtoAmbiental.ideAtoAmbiental")
				.add(Projections.property("aaN.sglAtoAmbiental"),"ideNovoAtoAmbiental.sglAtoAmbiental")
				.add(Projections.property("aaN.nomAtoAmbiental"),"ideNovoAtoAmbiental.nomAtoAmbiental")
				.add(Projections.property("taN.ideTipoAto"),"ideNovoAtoAmbiental.ideTipoAto.ideTipoAto")
				.add(Projections.property("taN.nomTipoAto"),"ideNovoAtoAmbiental.ideTipoAto.nomTipoAto")
				
				.add(Projections.property("ideReenquadramentoProcesso.ideReenquadramentoProcesso"),"ideReenquadramentoProcesso.ideReenquadramentoProcesso")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReenquadramentoProcessoAto.class))
		;
		
		List<ReenquadramentoProcessoAto> listaReenquadramentoProcessoAto = reenquadramentoProcessoAtoDAO.listarPorCriteria(criteria);
		if(!Util.isNullOuVazio(listaReenquadramentoProcessoAto)) {
			for (ReenquadramentoProcessoAto rpa : listaReenquadramentoProcessoAto) {
				rpa.setReenquadramentoTipoFinalidadeUsoAguaCollection(reenquadramentoTipoFinalidadeUsoAguaDAOImpl.listar(rpa));
				rpa.setReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection(reenquadramentoProcessoAtoObjetivoAtividadeManejoDAOImpl.listar(rpa));
				rpa.setReenquadramentoProcessoAtoTipoAtividadeFaunaCollection(reenquadramentoProcessoAtoTipoAtividadeFaunaDAOImpl.listar(rpa));
				rpa.setReenquadramentoTipologiaEmpreendimentoCollection(reenquadramentoTipologiaEmpreendimentoDAOImpl.listar(rpa));
			}
		}
	
		
		return listaReenquadramentoProcessoAto;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ReenquadramentoProcessoAto> listarReenquadramentoProcessoAtoPorEnquadramento(Enquadramento enquadramento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReenquadramentoProcessoAto.class);
		
		criteria
			.createAlias("ideReenquadramentoProcesso", "rp", JoinType.INNER_JOIN)
			.createAlias("rp.ideNotificacao", "no", JoinType.INNER_JOIN)
			.createAlias("no.ideProcesso", "pr", JoinType.INNER_JOIN)
			.createAlias("pr.processoReenquadramentoCollection", "pe", JoinType.INNER_JOIN)
			.createAlias("pe.enquadramentoCollection", "en", JoinType.INNER_JOIN)
			.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
			.createAlias("pa.tipologia", "tp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pa.atoAmbiental", "at", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("en.ideEnquadramento", enquadramento.getIdeEnquadramento()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideReenquadramentoProcessoAto"), "ideReenquadramentoProcessoAto")
				.add(Projections.property("ideNovaTipologia.ideTipologia"), "ideNovaTipologia.ideTipologia")
				.add(Projections.property("ideNovoAtoAmbiental.ideAtoAmbiental"), "ideNovoAtoAmbiental.ideAtoAmbiental")
				.add(Projections.property("ideProcessoAto.ideProcessoAto"), "ideProcessoAto.ideProcessoAto")
				.add(Projections.property("tp.ideTipologia"), "ideProcessoAto.tipologia.ideTipologia")
				.add(Projections.property("at.ideAtoAmbiental"), "ideProcessoAto.atoAmbiental.ideAtoAmbiental")
				
				.add(Projections.property("ideReenquadramentoProcesso.ideReenquadramentoProcesso"), "ideReenquadramentoProcesso.ideReenquadramentoProcesso")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReenquadramentoProcessoAto.class))
		;
		
		return reenquadramentoProcessoAtoDAO.listarPorCriteria(criteria);
	}
	
	
}