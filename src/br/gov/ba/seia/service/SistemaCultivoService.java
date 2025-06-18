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
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeSistemaCultivo;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoProducao;
import br.gov.ba.seia.entity.SistemaCultivo;
import br.gov.ba.seia.entity.TipoAtividadeProducaoCultivoInstalacao;
import br.gov.ba.seia.enumerator.TipoProducaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * Service para retornar as informações cadastrados de {@link SistemaCultivo} no banco de dados.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 03/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SistemaCultivoService {
	@Inject
	private IDAO<SistemaCultivo> sistemaCultivoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SistemaCultivo> listarSistemaCultivo() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(SistemaCultivo.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return sistemaCultivoIDAO.listarPorCriteria(criteria, Order.asc("ideSistemaCultivo"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SistemaCultivo> listarSistemaCultivoBy(TipoProducaoEnum tipoProducao) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAtividadeProducaoCultivoInstalacao.class)
				.createAlias("ideSistemaCultivo", "cultivo")
				.add(Restrictions.eq("ideTipoProducao.ideTipoProducao", tipoProducao.getId()))
				.add(Restrictions.eq("cultivo.indAtivo", true))
				.setProjection(montarProjectionSistemaCultivo())
				.setResultTransformer(new AliasToNestedBeanResultTransformer(SistemaCultivo.class));
		return sistemaCultivoIDAO.listarPorCriteria(criteria, Order.asc("cultivo.nomSistemaCultivo"));
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 16/06/2015
	 */
	private ProjectionList montarProjectionSistemaCultivo() {
		return Projections.projectionList()
				.add(Projections.property("cultivo.ideSistemaCultivo"),"ideSistemaCultivo")
				.add(Projections.property("cultivo.nomSistemaCultivo"), "nomSistemaCultivo")
				.add(Projections.groupProperty("cultivo.ideSistemaCultivo"))
				.add(Projections.groupProperty("cultivo.nomSistemaCultivo"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SistemaCultivo> listarSistemaCultivoBy(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaTipoAtividadeTipoProducao) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoAtividadeSistemaCultivo.class)
				.createAlias("sistemaCultivo", "cultivo")
				.add(Restrictions.eq("fceAquiculturaLicencaTipoAtividadeTipoProducao.ideFceAquiculturaLicencaTipoAtividadeTipoProducao", 
						fceAquiculturaLicencaTipoAtividadeTipoProducao.getIdeFceAquiculturaLicencaTipoAtividadeTipoProducao()))
						.setProjection(montarProjectionSistemaCultivo()).setResultTransformer(new AliasToNestedBeanResultTransformer(SistemaCultivo.class));
		return sistemaCultivoIDAO.listarPorCriteria(criteria);
	}
}