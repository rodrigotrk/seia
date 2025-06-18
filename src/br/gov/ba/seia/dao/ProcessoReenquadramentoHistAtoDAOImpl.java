package br.gov.ba.seia.dao;

import java.util.List;

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

import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.ProcessoReenquadramentoHistAto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoReenquadramentoHistAtoDAOImpl {
	
	@Inject
	IDAO<ProcessoReenquadramentoHistAto> processoReenquadramentoHistAtoIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ProcessoReenquadramentoHistAto processoReenquadramentoHistAto) {
		processoReenquadramentoHistAtoIDAO.salvarOuAtualizar(processoReenquadramentoHistAto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoReenquadramentoHistAto obterProcessoReenquadramentoHistAtoReenquadrado(ProcessoReenquadramento processoReenquadramento,
			EnquadramentoAtoAmbiental enquadramentoAtoAmbiental)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoReenquadramentoHistAto.class);
		
		criteria
			.createAlias("ideProcessoReenquadramento", "pr", JoinType.INNER_JOIN)
			.createAlias("ideAtoAmbientalOriginal", "aa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipologiaOriginal", "tp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideEnquadramentoAtoAmbientalReenquadrado", "eb", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("pr.ideProcessoReenquadramento", processoReenquadramento.getIdeProcessoReenquadramento()))
			.add(Restrictions.eq("eb.ideEnquadramentoAtoAmbiental", enquadramentoAtoAmbiental.getIdeEnquadramentoAtoAmbiental()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcessoReenquadramento"), "ideProcessoReenquadramento")
				.add(Projections.property("dtcReenquadramento"), "dtcReenquadramento")
				.add(Projections.property("aa.ideAtoAmbiental"), "ideAtoAmbientalOriginal.ideAtoAmbiental")
				.add(Projections.property("tp.ideTipologia"), "ideTipologiaOriginal.ideTipologia")
				.add(Projections.property("eb.ideEnquadramentoAtoAmbiental"), "ideEnquadramentoAtoAmbientalReenquadrado.ideEnquadramentoAtoAmbiental")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoReenquadramentoHistAto.class))
		;
		
		return processoReenquadramentoHistAtoIDAO.buscarPorCriteria(criteria);	
	}

	public List<ProcessoReenquadramentoHistAto> obterProReenHistAtoPorProcessoReenquadramento(ProcessoReenquadramento processoReenquadramento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoReenquadramentoHistAto.class);
		
		criteria
			.createAlias("ideProcessoReenquadramento", "pr", JoinType.INNER_JOIN)
			.createAlias("ideAtoAmbientalOriginal", "aa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipologiaOriginal", "tp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideEnquadramentoAtoAmbientalReenquadrado", "eb", JoinType.INNER_JOIN)
			.createAlias("eb.atoAmbiental", "eaa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("eb.tipologia", "etp", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("pr.ideProcessoReenquadramento", processoReenquadramento.getIdeProcessoReenquadramento()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcessoReenquadramento"), "ideProcessoReenquadramento")
				.add(Projections.property("dtcReenquadramento"), "dtcReenquadramento")
				.add(Projections.property("aa.ideAtoAmbiental"), "ideAtoAmbientalOriginal.ideAtoAmbiental")
				.add(Projections.property("aa.nomAtoAmbiental"), "ideAtoAmbientalOriginal.nomAtoAmbiental")
				.add(Projections.property("tp.ideTipologia"), "ideTipologiaOriginal.ideTipologia")
				.add(Projections.property("tp.desTipologia"), "ideTipologiaOriginal.desTipologia")
				.add(Projections.property("eb.ideEnquadramentoAtoAmbiental"), "ideEnquadramentoAtoAmbientalReenquadrado.ideEnquadramentoAtoAmbiental")
				.add(Projections.property("eaa.ideAtoAmbiental"), "ideEnquadramentoAtoAmbientalReenquadrado.atoAmbiental.ideAtoAmbiental")
				.add(Projections.property("eaa.nomAtoAmbiental"), "ideEnquadramentoAtoAmbientalReenquadrado.atoAmbiental.nomAtoAmbiental")
				.add(Projections.property("etp.ideTipologia"), "ideEnquadramentoAtoAmbientalReenquadrado.tipologia.ideTipologia")
				.add(Projections.property("etp.desTipologia"), "ideEnquadramentoAtoAmbientalReenquadrado.tipologia.desTipologia")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoReenquadramentoHistAto.class))
		;
		
		return processoReenquadramentoHistAtoIDAO.listarPorCriteria(criteria);	
	}
}
