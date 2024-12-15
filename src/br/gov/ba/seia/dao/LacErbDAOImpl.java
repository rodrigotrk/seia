package br.gov.ba.seia.dao;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.LacErb;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

/***
 * 
 * @author luis
 *
 */
public class LacErbDAOImpl {

	@Inject
	IDAO<LacErb> daoLacErb;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(LacErb pLacErb)  {
		daoLacErb.salvar(pLacErb);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(LacErb pLacErb)  {
		daoLacErb.salvarOuAtualizar(pLacErb);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(LacErb pLacErb) {
		daoLacErb.atualizar(pLacErb);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(LacErb pLacErb) {
		daoLacErb.atualizar(pLacErb);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LacErb carregarByIdRequerimento(Integer pIde) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LacErb.class)
				.createAlias("ideDocumentoObrigatorio", "documento", JoinType.INNER_JOIN)				
				.createAlias("ideRequerimento", "requerimento", JoinType.INNER_JOIN)
				.createAlias("requerimento.ideOrgao", "orgao", JoinType.INNER_JOIN)
				.createAlias("requerimento.certificados", "certificado", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoModalidadeErb", "modalidade", JoinType.INNER_JOIN);

		criteria.setProjection(Projections.projectionList()

				.add(Projections.property("ideLac"), "ideLac")
				.add(Projections.property("dscNomeErb"), "dscNomeErb")
				.add(Projections.property("codErb"), "codErb")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				
				.add(Projections.property("vlrAreaTotalTerreno"), "vlrAreaTotalTerreno").add(Projections.property("vlrAlturaTorre"), "vlrAlturaTorre")
				.add(Projections.property("vlrAzimuteAntenaBase"), "vlrAzimuteAntenaBase")
				.add(Projections.property("vlrAlturaAntenaBase"), "vlrAlturaAntenaBase").add(Projections.property("vlrMenorDistLimite"), "vlrMenorDistLimite")

				.add(Projections.property("indCompartilhado"), "indCompartilhado")

				.add(Projections.property("dscNomeEdificacao"), "dscNomeEdificacao").add(Projections.property("vlrAlturaEdificacao"), "vlrAlturaEdificacao")
				.add(Projections.property("vlrAlturaMaior"), "vlrAlturaMaior").add(Projections.property("vlrAlturaMenor"), "vlrAlturaMenor")
				.add(Projections.property("vlrAlturaAntenaEdificacao"), "vlrAlturaAntenaEdificacao")

				.add(Projections.property("dscOutrosTipoDelimitacao"), "dscOutrosTipoDelimitacao")

				.add(Projections.property("dscNomeEstabelecimento"), "dscNomeEstabelecimento")
				.add(Projections.property("dscAtividadeEstabelecimento"), "dscAtividadeEstabelecimento")

				.add(Projections.property("vlrPotenciaTransmissor"), "vlrPotenciaTransmissor")
				.add(Projections.property("vlrFrequenciaUtilizada"), "vlrFrequenciaUtilizada")

				.add(Projections.property("requerimento.ideRequerimento"), "ideRequerimento.ideRequerimento")
				
				.add(Projections.property("orgao.ideOrgao"), "ideRequerimento.ideOrgao.ideOrgao")
				.add(Projections.property("orgao.codOrgao"), "ideRequerimento.ideOrgao.codOrgao")
				
				.add(Projections.property("certificado.ideCertificado"), "certificado.ideCertificado")
				
				.add(Projections.property("documento.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				
				.add(Projections.property("modalidade.ideTipoModalidadeErb"), "ideTipoModalidadeErb.ideTipoModalidadeErb"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(LacErb.class));

		criteria.add(Restrictions.eq("requerimento.ideRequerimento", pIde));
		criteria.add(Restrictions.or(Restrictions.eq("certificado.ideAtoAmbiental.ideAtoAmbiental", AtoAmbientalEnum.LAC.getId()),Restrictions.isNull("certificado.ideAtoAmbiental.ideAtoAmbiental")));
		return daoLacErb.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean hasLac(Integer pIdeRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LacErb.class)
				.createAlias("ideDocumentoObrigatorio", "documento", JoinType.INNER_JOIN)				
				.createAlias("ideRequerimento", "requerimento", JoinType.INNER_JOIN)
				.createAlias("requerimento.ideOrgao", "orgao", JoinType.INNER_JOIN)
				.createAlias("requerimento.certificados", "certificado", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoModalidadeErb", "modalidade", JoinType.INNER_JOIN);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideLac"), "ideLac"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(LacErb.class));

		criteria.add(Restrictions.eq("requerimento.ideRequerimento", pIdeRequerimento));
		criteria.add(Restrictions.or(Restrictions.eq("certificado.ideAtoAmbiental.ideAtoAmbiental", AtoAmbientalEnum.LAC.getId()),Restrictions.isNull("certificado.ideAtoAmbiental.ideAtoAmbiental")));
		
		return !Util.isNull(daoLacErb.buscarPorCriteria(criteria));
	}
	
}
