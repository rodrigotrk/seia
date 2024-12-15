package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoInstalacao;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoProducao;
import br.gov.ba.seia.entity.TipoAtividadeProducaoCultivoInstalacao;
import br.gov.ba.seia.entity.TipoInstalacao;
import br.gov.ba.seia.enumerator.TipoProducaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * Service para retornar as informações cadastrados de {@link TipoInstalacao} no banco de dados.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 03/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoInstalacaoService {
	@Inject
	private IDAO<TipoInstalacao> tipoInstalacaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoInstalacao> listarTipoInstalacao() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoInstalacao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoInstalacaoIDAO.listarPorCriteria(criteria, Order.asc("ideTipoInstalacao"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoInstalacao> listarTipoInstalacaoFormasJovens() throws Exception{
		DetachedCriteria criteria = montarCriteira(TipoProducaoEnum.FORMAS_JOVENS)
				.setProjection(montarProjectionTipoInstalacao()).setResultTransformer(new AliasToNestedBeanResultTransformer(TipoInstalacao.class));
		return tipoInstalacaoIDAO.listarPorCriteria(criteria, Order.asc("instalacao.ideTipoInstalacao"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoInstalacao> listarTipoInstalacaoEngorda(AquiculturaTipoAtividade aquiculturaTipoAtividade) throws Exception{
		DetachedCriteria criteria = montarCriteira(TipoProducaoEnum.ENGORDA)
				.add(Restrictions.eq("ideAquiculturaTipoAtividade.ideAquiculturaTipoAtividade", aquiculturaTipoAtividade.getIdeAquiculturaTipoAtividade()))
				.setProjection(	montarProjectionTipoInstalacao()
						.add(Projections.groupProperty("ideAquiculturaTipoAtividade.ideAquiculturaTipoAtividade")))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(TipoInstalacao.class));
		return tipoInstalacaoIDAO.listarPorCriteria(criteria, Order.asc("instalacao.ideTipoInstalacao"));
	}


	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param producaoEnum
	 * @return
	 * @since 17/06/2015
	 */
	private DetachedCriteria montarCriteira(TipoProducaoEnum producaoEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAtividadeProducaoCultivoInstalacao.class)
				.createAlias("ideTipoInstalacao", "instalacao")
				.add(Restrictions.eq("ideTipoProducao.ideTipoProducao", producaoEnum.getId()))
				.add(Restrictions.eq("instalacao.indAtivo", true));
		return criteria;
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 16/06/2015
	 */
	private ProjectionList montarProjectionTipoInstalacao() {
		return Projections.projectionList()
				.add(Projections.property("instalacao.ideTipoInstalacao"),"ideTipoInstalacao")
				.add(Projections.property("instalacao.nomTipoInstalacao"), "nomTipoInstalacao")
				.add(Projections.groupProperty("instalacao.ideTipoInstalacao"))
				.add(Projections.groupProperty("instalacao.nomTipoInstalacao"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoInstalacao> listarTipoInstalacaoBy(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeProducao) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoAtividadeTipoInstalacao.class)
				.createAlias("tipoInstalacao", "instalacao")
				.add(Restrictions.eq("fceAquiculturaLicencaTipoAtividadeTipoProducao.ideFceAquiculturaLicencaTipoAtividadeTipoProducao", 
						fceAquiculturaLicencaTipoAtividadeProducao.getIdeFceAquiculturaLicencaTipoAtividadeTipoProducao()))
						.setProjection(montarProjectionTipoInstalacao()
						.add(Projections.property("numEstrutura"), "numEstrutura")
						.add(Projections.groupProperty("numEstrutura"))
					).setResultTransformer(new AliasToNestedBeanResultTransformer(TipoInstalacao.class));
		return tipoInstalacaoIDAO.listarPorCriteria(criteria);
	}
}