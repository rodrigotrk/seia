package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.CerhBarragemAproveitamentoHidreletrico;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacao;
import br.gov.ba.seia.entity.CerhIntervencaoCaracterizacao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhBarragemAproveitamentoHidreletricoDAOImpl {

	@Inject
	private IDAO<CerhBarragemAproveitamentoHidreletrico> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemAproveitamentoHidreletrico carregar(Integer ideCerhBarragemCaracterizacaoFinalidade)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhBarragemAproveitamentoHidreletrico.class)
			.add(Restrictions.eq("ideCerhBarragemCaracterizacaoFinalidade.ideCerhBarragemCaracterizacaoFinalidade", ideCerhBarragemCaracterizacaoFinalidade))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideCerhBarragemAproveitamentoHidreletrico"), "ideCerhBarragemAproveitamentoHidreletrico")
					.add(Projections.property("indDesvioTrecho"), "indDesvioTrecho")
					.add(Projections.property("indEmOperacao"), "indEmOperacao")
					.add(Projections.property("indPch"), "indPch")
					.add(Projections.property("indPotenciaInstaladaIntervencao"), "indPotenciaInstaladaIntervencao")
					.add(Projections.property("valExtensao"), "valExtensao")
					.add(Projections.property("valPotenciaInstaladaTotal"), "valPotenciaInstaladaTotal")
					.add(Projections.property("valProducaoAnualEfetivamenteVerificada"), "valProducaoAnualEfetivamenteVerificada")
					.add(Projections.property("valTrechoVazaoReduzida"), "valTrechoVazaoReduzida")
					.add(Projections.property("dtInicioOperacao"), "dtInicioOperacao")
					.add(Projections.property("ideCerhBarragemCaracterizacaoFinalidade.ideCerhBarragemCaracterizacaoFinalidade"), "ideCerhBarragemCaracterizacaoFinalidade.ideCerhBarragemCaracterizacaoFinalidade")
					.add(Projections.property("ideTipoAproveitamentoHidreletrico.ideTipoAproveitamentoHidreletrico"), "ideTipoAproveitamentoHidreletrico.ideTipoAproveitamentoHidreletrico")
					.add(Projections.property("ideCerhLocalizacaoGeograficaIntervencao.ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeograficaIntervencao.ideCerhLocalizacaoGeografica")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhBarragemAproveitamentoHidreletrico.class));
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemAproveitamentoHidreletrico carregar(CerhBarragemCaracterizacao cerhBarragemCaracterizacao)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhBarragemAproveitamentoHidreletrico.class)
				.add(Restrictions.eq("ideCerhBarragemAproveitamentoHidreletrico", cerhBarragemCaracterizacao.getId()))
				.setFetchMode("ideCerhLocalizacaoGeograficaIntervencao", FetchMode.JOIN);
				
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhBarragemAproveitamentoHidreletrico> carregar(CerhIntervencaoCaracterizacao cerhIntervencaoCaracterizacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhBarragemAproveitamentoHidreletrico.class)
				.add(Restrictions.eq("ideCerhLocalizacaoGeograficaIntervencao.ideCerhLocalizacaoGeografica", cerhIntervencaoCaracterizacao.getIdeCerhLocalizacaoGeografica().getIdeCerhLocalizacaoGeografica()))
				.setFetchMode("ideCerhLocalizacaoGeograficaIntervencao", FetchMode.JOIN);		
		return dao.listarPorCriteria(criteria);
	}
}
