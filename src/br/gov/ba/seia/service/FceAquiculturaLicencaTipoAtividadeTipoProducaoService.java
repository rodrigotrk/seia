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
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoProducao;

/**
 * Service para retornar as informações cadastrados de {@link FceAquiculturaLicencaTipoAtividadeTipoProducao} no banco de dados.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 10/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaLicencaTipoAtividadeTipoProducaoService {
	@Inject
	private IDAO<FceAquiculturaLicencaTipoAtividadeTipoProducao> fceAquiculturaLicencaTipoAtividadeTipoProducaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividadeTipoProducao> listarFceAquiculturaLicencaTipoAtividadeProducaoBy(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade){
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoAtividadeTipoProducao.class)
				.createAlias("fceAquiculturaLicencaTipoAtividade", "fceAquicLicTipoAtividade")
				.createAlias("ideTipoProducao", "tipProducao")
				.add(Restrictions.eq("fceAquicLicTipoAtividade.ideFceAquiculturaLicencaTipoAtividade", fceAquiculturaLicencaTipoAtividade.getIdeFceAquiculturaLicencaTipoAtividade()));
		return fceAquiculturaLicencaTipoAtividadeTipoProducaoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAquiculturaLicencaTipoAtividadeTipoProducao(List<FceAquiculturaLicencaTipoAtividadeTipoProducao> listaToSalvar) {
		fceAquiculturaLicencaTipoAtividadeTipoProducaoIDAO.salvarEmLote(listaToSalvar);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaLicencaTipoAtividadeTipoProducaoBy(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaAtividade) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicencaTipoAtividade", fceAquiculturaLicencaAtividade.getIdeFceAquiculturaLicencaTipoAtividade());
		fceAquiculturaLicencaTipoAtividadeTipoProducaoIDAO.executarNamedQuery("FceAquiculturaLicencaTipoAtividadeTipoProducao.removeByIdeFceAquiculturaLicencaTipoAtividade", parameters);
	}
}