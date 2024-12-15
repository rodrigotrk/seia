/**
 * 
 */
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
import br.gov.ba.seia.entity.FceBarragLicencLocaGeo;
import br.gov.ba.seia.entity.FceBarragemLicenciamento;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceBarragLicencLocaGeoService {

	@Inject
	IDAO<FceBarragLicencLocaGeo> idao;
	
	public List<FceBarragLicencLocaGeo> listarByFceBarragemLicenciamento(FceBarragemLicenciamento fceBarragemLicenciamento){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ideFceBarragemLicenciamento", fceBarragemLicenciamento.getIdeFceBarragemLicenciamento());
		return idao.buscarPorNamedQuery("FceBarragLicencLocaGeo.listarByIdeFceBarragemLicenciamento", param);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void excluirFceBarragLicencLocaGeo(FceBarragLicencLocaGeo fceBarragLicencLocaGeo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ideFceBarragemLicenciamento", fceBarragLicencLocaGeo.getFceBarragLicencLocaGeoPK().getIdeFceBarragemLicenciamento());
		param.put("ideLocalGeoInicioEixo", fceBarragLicencLocaGeo.getFceBarragLicencLocaGeoPK().getIdeLocalGeoInicioEixo());
		param.put("ideLocalGeoFimEixo", fceBarragLicencLocaGeo.getFceBarragLicencLocaGeoPK().getIdeLocalGeoFimEixo());
		idao.executarNamedQuery("FceBarragLicencLocaGeo.removeByPk", param);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(List<FceBarragLicencLocaGeo> barragemLicenciamentoLocalizacaoGeo) {
		idao.salvarEmLote(barragemLicenciamentoLocalizacaoGeo);
	}
}
