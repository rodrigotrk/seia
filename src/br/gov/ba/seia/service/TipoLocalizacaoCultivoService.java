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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoLocalizacaoCultivo;
import br.gov.ba.seia.entity.TipoLocalizacaoCultivo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * Service para retornar as informações cadastrados de {@link TipoLocalizacaoCultivo} no banco de dados.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 01/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoLocalizacaoCultivoService {
	@Inject
	private IDAO<TipoLocalizacaoCultivo> tipoLocalizacaoCultivoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoLocalizacaoCultivo> listarTipoLocalizacaoCultivo() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoLocalizacaoCultivo.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoLocalizacaoCultivoIDAO.listarPorCriteria(criteria, Order.asc("ideTipoLocalizacaoCultivo"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoLocalizacaoCultivo> listarTipoLocalizacaoCultivoBy(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividade aquiculturaTipoAtividade) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoLocalizacaoCultivo.class)
				.createAlias("ideTipoLocalizacaoCultivo", "locCultivo")
				.add(Restrictions.eq("ideFceAquiculturaLicenca.ideFceAquiculturaLicenca", fceAquiculturaLicenca.getIdeFceAquiculturaLicenca()))
				.add(Restrictions.eq("ideAquiculturaTipoAtividade.ideAquiculturaTipoAtividade", aquiculturaTipoAtividade.getIdeAquiculturaTipoAtividade()))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("locCultivo.ideTipoLocalizacaoCultivo"), "ideTipoLocalizacaoCultivo")
						.add(Projections.property("locCultivo.nomTipoLocalizacaoCultivo"), "nomTipoLocalizacaoCultivo")
						.add(Projections.property("locCultivo.indAtivo"), "indAtivo")
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(TipoLocalizacaoCultivo.class));
		return tipoLocalizacaoCultivoIDAO.listarPorCriteria(criteria);
	}
}