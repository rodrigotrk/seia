package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.DadoOrigem;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipologiaDAOImpl {

	@Inject
	private IDAO<Tipologia> tipologiaDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@SuppressWarnings("unchecked")
	public List<Tipologia> getTipologias(Tipologia pTipologia) {

		StringBuilder lEjbql = new StringBuilder(
				"select tipologia from Tipologia tipologia ");

		if (!Util.isNull(pTipologia)) {

			lEjbql.append("where tipologia.indExcluido = :indExcluido ");

			if (!Util.isNull(pTipologia.getIdeTipologia()))
				lEjbql.append(" AND tipologia.ideTipologia = :ideTipologia");

			if (!Util.isNull(pTipologia.getDesTipologia()))
				lEjbql.append(" AND lower(tipologia.desTipologia) LIKE lower(:desTipologia)");

			if (!Util.isNull(pTipologia.getIdeTipologiaPai())) {

				if (!Util.isNullOuVazio(pTipologia.getIdeTipologiaPai()
						.getIdeTipologia())) {

					lEjbql.append(" AND tipologia.ideTipologiaPai = :ideTipologiaPai");
				} else {

					lEjbql.append(" AND tipologia.ideTipologiaPai is null");
				}
			}
		}

		lEjbql.append(" order by tipologia.codTipologia");

		EntityManager lEntityManager = DAOFactory.getEntityManager();

		Query lQuery = lEntityManager.createQuery(lEjbql.toString());

		if (!Util.isNull(pTipologia)) {

			lQuery.setParameter("indExcluido", pTipologia.getIndExcluido());

			if (!Util.isNull(pTipologia.getIdeTipologia()))
				lQuery.setParameter("ideTipologia",
						pTipologia.getIdeTipologia());

			if (!Util.isNull(pTipologia.getDesTipologia()))
				lQuery.setParameter("desTipologia",
						pTipologia.getDesTipologia() + "%");

			if (!Util.isNull(pTipologia.getIdeTipologiaPai())
					&& !Util.isNullOuVazio(pTipologia.getIdeTipologiaPai()
							.getIdeTipologia()))
				lQuery.setParameter("ideTipologiaPai", pTipologia
						.getIdeTipologiaPai().getIdeTipologia());
		}

		return lQuery.getResultList();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> listarTipologiaDadosConcedidos(Processo processo)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tipologia.class)
				
				.createAlias("atoAmbientalTipologiaCollection", "aa", JoinType.INNER_JOIN)
				.createAlias("aa.documentoAtoCollection", "da", JoinType.INNER_JOIN)
				
				.createAlias("processoAtoCollection", "pa", JoinType.INNER_JOIN)
				.createAlias("pa.processo", "p", JoinType.INNER_JOIN)
				.createAlias("p.analiseTecnicaCollection", "at", JoinType.INNER_JOIN)
				.createAlias("at.fceCollection", "fce", JoinType.INNER_JOIN)
				.createAlias("fce.fceOutorgaLocalizacaoGeografica", "folg", JoinType.INNER_JOIN)
				.createAlias("folg.ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
				.createAlias("lg.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN)
				
				.createAlias("pa.controleProcessoAtoCollection", "cpa", JoinType.INNER_JOIN)
				.createAlias("cpa.ideStatusProcessoAto", "st", JoinType.INNER_JOIN)
				
				.add(Restrictions.eqProperty("aa.ideAtoAmbiental", "pa.atoAmbiental.ideAtoAmbiental"))
				.add(Restrictions.eqProperty("ideTipologia", "pa.tipologia.ideTipologia"))
				.add(Restrictions.eqProperty("fce.ideDocumentoObrigatorio", "da.ideDocumentoObrigatorio"))
				
				.add(Restrictions.eq("p.ideProcesso", processo.getIdeProcesso()))
				
				.add(Property.forName("cpa.ideControleProcessoAto").eq(
						DetachedCriteria.forClass(ControleProcessoAto.class)
						.createAlias("ideProcessoAto", "pa2", JoinType.INNER_JOIN)
						.createAlias("ideStatusProcessoAto", "st", JoinType.INNER_JOIN)
						.add(Restrictions.eqProperty("pa2.ideProcessoAto", "pa.ideProcessoAto"))
						.add(Restrictions.eq("st.ideStatusProcessoAto", StatusProcessoAtoEnum.DEFERIDO.getId()))
						.setProjection(Projections.max("ideControleProcessoAto"))
				))
				.add(Restrictions.eqProperty("fce.ideDocumentoObrigatorio", "da.ideDocumentoObrigatorio"))
				.add(Restrictions.eq("fce.ideDadoOrigem", new DadoOrigem(DadoOrigemEnum.TECNICO.getId())))
						
				.setProjection(Projections.distinct(
					Projections.projectionList()
						.add(Property.forName("ideTipologia"), "ideTipologia")
						.add(Property.forName("desTipologia"), "desTipologia")
					)
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class));
			
			return tipologiaDAO.listarPorCriteria(detachedCriteria);
	}
}