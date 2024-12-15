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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoLocalizacaoCultivo;

/**
 * Service de {@link FceAquiculturaLicencaTipoLocalizacaoCultivo}.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 11/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaLicencaTipoLocalizacaoCultivoService {
	@Inject
	private IDAO<FceAquiculturaLicencaTipoLocalizacaoCultivo> fceAquiculturaLicencaTipoLocalizacaoCultivoIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceAquiculturaLicencaTipoLocalizacaoCultivoBy(FceAquiculturaLicenca fceAquiculturaLicenca) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicenca", fceAquiculturaLicenca.getIdeFceAquiculturaLicenca());
		fceAquiculturaLicencaTipoLocalizacaoCultivoIDAO.executarNamedQuery("FceAquiculturaLicencaTipoLocalizacaoCultivo.removeByIdeFceAquiculturaLicenca", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceAquiculturaLicencaTipoLocalizacaoCultivoBy(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividade aquiculturaTipoAtividade) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicenca", fceAquiculturaLicenca.getIdeFceAquiculturaLicenca());
		parameters.put("ideAquiculturaTipoAtividade", aquiculturaTipoAtividade.getIdeAquiculturaTipoAtividade());
		fceAquiculturaLicencaTipoLocalizacaoCultivoIDAO.executarNamedQuery("FceAquiculturaLicencaTipoLocalizacaoCultivo.removeByIdeFceAquiculturaLicencaAndTipoAtividade", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAquiculturaLicencaTipoLocalizacaoCultivo(List<FceAquiculturaLicencaTipoLocalizacaoCultivo> lista) {
		fceAquiculturaLicencaTipoLocalizacaoCultivoIDAO.salvarEmLote(lista);
	}
}