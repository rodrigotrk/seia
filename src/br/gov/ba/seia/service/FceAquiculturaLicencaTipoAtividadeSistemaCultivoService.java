package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeSistemaCultivo;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoProducao;

/**
 * Service para retornar as informações cadastrados de {@link FceAquiculturaLicencaTipoAtividadeSistemaCultivo} no banco de dados.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 10/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaLicencaTipoAtividadeSistemaCultivoService {
	@Inject
	private IDAO<FceAquiculturaLicencaTipoAtividadeSistemaCultivo> fceAquiculturaLicencaTipoAtividadeSistemaCultivoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividadeSistemaCultivo> listarFceAquiculturaLicencaTipoAtividadeSistemaCultivoBy(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoAtividadeSistemaCultivo.class)
				.createAlias("fceAquiculturaLicencaTipoAtividade", "fceAquicLicTipoAtividade")
				.createAlias("sistemaCultivo", "sisCultivo")
				.add(Restrictions.eq("fceAquicLicTipoAtividade.ideFceAquiculturaLicencaTipoAtividade", fceAquiculturaLicencaTipoAtividade.getIdeFceAquiculturaLicencaTipoAtividade()));
		return fceAquiculturaLicencaTipoAtividadeSistemaCultivoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAquiculturaLicencaTipoAtividadeSistemaCultivo(List<FceAquiculturaLicencaTipoAtividadeSistemaCultivo> listaToSalvar) {
		fceAquiculturaLicencaTipoAtividadeSistemaCultivoIDAO.salvarEmLote(listaToSalvar);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaLicencaTipoAtividadeSistemaCultivoBy(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaAtividadeTipoProducao) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicencaTipoAtividadeTipoProducao", fceAquiculturaLicencaAtividadeTipoProducao.getIdeFceAquiculturaLicencaTipoAtividadeTipoProducao());
		fceAquiculturaLicencaTipoAtividadeSistemaCultivoIDAO.executarNamedQuery("FceAquiculturaLicencaTipoAtividadeSistemaCultivo.removeByIdeFceAquiculturaLicencaTipoAtividadeTipoProducao", parameters);
	}
}