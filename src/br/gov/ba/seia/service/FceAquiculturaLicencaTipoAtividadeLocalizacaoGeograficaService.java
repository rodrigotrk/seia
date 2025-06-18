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
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoAquicultura;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;

/**
 * Serviço para {@link FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica} <i>Poligonal da área de Cultivo</i>.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 11/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaService {

	@Inject
	private IDAO<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica(List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> listaFceAquiculturaLicencaTipoAtividade) {
		fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaIDAO.salvarEmLote(listaFceAquiculturaLicencaTipoAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> listarFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividade aquiculturaTipoAtividade, TipoAquicultura tipoAquicultura){
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica.class)
				.createAlias("ideLocalizacaoGeografica", "locGeo")
				.createAlias("ideFceAquiculturaLicenca", "fal")
				.createAlias("ideAquiculturaTipoAtividade", "ata")
				.createAlias("ideTipoAquicultura", "ta")
				.add(Restrictions.eq("fal.ideFceAquiculturaLicenca", fceAquiculturaLicenca.getIdeFceAquiculturaLicenca()))
				.add(Restrictions.eq("ata.ideAquiculturaTipoAtividade", aquiculturaTipoAtividade.getIdeAquiculturaTipoAtividade()))
				.add(Restrictions.eq("ta.ideTipoAquicultura", tipoAquicultura.getIdeTipoAquicultura())
				);
		return fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquiculturaEnum tipoAquiculturaEnum) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicenca", fceAquiculturaLicenca.getIdeFceAquiculturaLicenca());
		parameters.put("ideTipoAquicultura", tipoAquiculturaEnum.getId());
		fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaIDAO.executarNamedQuery("FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica.removeByIdeFceAquiculturaLicencaAndTipoAquicultura", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquicultura tipoAquicultura, AquiculturaTipoAtividade aquiculturaTipoAtividade) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicenca", fceAquiculturaLicenca.getIdeFceAquiculturaLicenca());
		parameters.put("ideTipoAquicultura", tipoAquicultura.getIdeTipoAquicultura());
		parameters.put("ideAquiculturaTipoAtividade", aquiculturaTipoAtividade.getIdeAquiculturaTipoAtividade());
		fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaIDAO.executarNamedQuery("FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica.removeByIdeFceAquiculturaLicencaAndTipoAquiculturaAndTipoAtividade", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica fceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica", fceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica.getIdeFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica());
		fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaIDAO.executarNamedQuery("FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica.removeByIde", parameters);
	}

}