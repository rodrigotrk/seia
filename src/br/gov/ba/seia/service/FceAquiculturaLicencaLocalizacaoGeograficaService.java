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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaLocalizacaoGeografica;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * Service para retornar as informações cadastrados de {@link FceAquiculturaLicencaLocalizacaoGeografica} no banco de dados.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 10/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaLicencaLocalizacaoGeograficaService {

	@Inject
	private IDAO<FceAquiculturaLicencaLocalizacaoGeografica> fceAquiculturaLicencaLocalizacaoGeograficaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaLocalizacaoGeografica> listarFceAquiculturaLicencaLocalizacaoGeograficaBy(FceAquiculturaLicenca fceAquiculturaLicenca) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaLocalizacaoGeografica.class)
				.createAlias("ideFceAquiculturaLicenca", "aquiLic")
				.createAlias("ideLocalizacaoGeografica", "locGeo")
				.createAlias("locGeo.ideClassificacaoSecao", "secao")
				.createAlias("locGeo.ideSistemaCoordenada", "cordenadas")
				.createAlias("ideTipologia", "tipologia")
				.add(Restrictions.eq("aquiLic.ideFceAquiculturaLicenca", fceAquiculturaLicenca.getIdeFceAquiculturaLicenca()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideFceAquiculturaLicencaLocalizacaoGeografica"), "ideFceAquiculturaLicencaLocalizacaoGeografica")
						.add(Projections.property("nomRio"), "nomRio")

						.add(Projections.property("aquiLic.ideFceAquiculturaLicenca"), "ideFceAquiculturaLicenca.ideFceAquiculturaLicenca") 

						.add(Projections.property("tipologia.ideTipologia"), "ideTipologia.ideTipologia")
						.add(Projections.property("tipologia.desTipologia"), "ideTipologia.desTipologia")
						.add(Projections.property("locGeo.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
						.add(Projections.property("cordenadas.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
						.add(Projections.property("cordenadas.srid"), "ideLocalizacaoGeografica.ideSistemaCoordenada.srid")
						.add(Projections.property("secao.ideClassificacaoSecao"), "ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(FceAquiculturaLicencaLocalizacaoGeografica.class));
		return fceAquiculturaLicencaLocalizacaoGeograficaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAquiculturaLicencaLocalizacaoGeografica(List<FceAquiculturaLicencaLocalizacaoGeografica> listaFceAquiculturaLicencaLocalizacaoGeografica) {
		fceAquiculturaLicencaLocalizacaoGeograficaIDAO.salvarEmLote(listaFceAquiculturaLicencaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaLicencaLocalizacaoGeograficaBy(FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquiculturaEnum tipoAquiculturaEnum) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicenca", fceAquiculturaLicenca.getIdeFceAquiculturaLicenca());
		parameters.put("ideTipologia", TipologiaEnum.INTERVENCAO_CORPO_HIDRICO.getId());
		if(tipoAquiculturaEnum.equals(TipoAquiculturaEnum.TANQUE_REDE)){
			fceAquiculturaLicencaLocalizacaoGeograficaIDAO.executarNamedQuery("FceAquiculturaLicencaLocalizacaoGeografica.removeByIdeFceAquiculturaLicencaAndIdeTipologia", parameters);
		}
		else if(tipoAquiculturaEnum.equals(TipoAquiculturaEnum.VIVEIRO_ESCAVADO)){
			fceAquiculturaLicencaLocalizacaoGeograficaIDAO.executarNamedQuery("FceAquiculturaLicencaLocalizacaoGeografica.removeByIdeFceAquiculturaLicencaAndNotIdeTipologia", parameters);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaLicencaLocalizacaoGeograficaBy(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicencaLocalizacaoGeografica", fceAquiculturaLicencaLocalizacaoGeografica.getIdeFceAquiculturaLicencaLocalizacaoGeografica());
		fceAquiculturaLicencaLocalizacaoGeograficaIDAO.executarNamedQuery("FceAquiculturaLicencaLocalizacaoGeografica.removeByIde", parameters);
	}
}