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
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoInstalacao;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoProducao;

/**
 * Service para {@link FceAquiculturaLicencaTipoAtividadeTipoInstalacao}
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 10/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaLicencaTipoAtividadeTipoInstalacaoService {
	@Inject
	private IDAO<FceAquiculturaLicencaTipoAtividadeTipoInstalacao> fceAquiculturaLicencaTipoAtividadeTipoInstalacaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividadeTipoInstalacao> listarFceAquiculturaLicencaTipoAtividadeTipoInstalacaoBy(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoAtividadeTipoInstalacao.class)
				.createAlias("fceAquiculturaLicencaTipoAtividade", "fceAquicLicTipoAtividade")
				.createAlias("tipoInstalacao", "tipInstalacao")
				.add(Restrictions.eq("fceAquicLicTipoAtividade.ideFceAquiculturaLicencaTipoAtividade", fceAquiculturaLicencaTipoAtividade.getIdeFceAquiculturaLicencaTipoAtividade()));
		return fceAquiculturaLicencaTipoAtividadeTipoInstalacaoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAquiculturaLicencaTipoAtividadeTipoInstalacao(List<FceAquiculturaLicencaTipoAtividadeTipoInstalacao> listaToSalvar) {
		fceAquiculturaLicencaTipoAtividadeTipoInstalacaoIDAO.salvarEmLote(listaToSalvar);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaLicencaTipoAtividadeTipoInstalacaoBy(FceAquiculturaLicencaTipoAtividadeTipoProducao fceAquiculturaLicencaAtividadeTipoProducao) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicencaTipoAtividadeTipoProducao", fceAquiculturaLicencaAtividadeTipoProducao.getIdeFceAquiculturaLicencaTipoAtividadeTipoProducao());
		fceAquiculturaLicencaTipoAtividadeTipoInstalacaoIDAO.executarNamedQuery("FceAquiculturaLicencaTipoAtividadeTipoInstalacao.removeByIdeFceAquiculturaLicencaTipoAtividadeTipoProducao", parameters);
	}
}