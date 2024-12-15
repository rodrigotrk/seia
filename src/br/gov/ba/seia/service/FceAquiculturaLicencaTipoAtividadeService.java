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
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 09/06/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaLicencaTipoAtividadeService {

	@Inject
	private IDAO<FceAquiculturaLicencaTipoAtividade> fceAquiculturaLicencaTipoAtividadeIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividade> listarFceAquiculturaLicencaTipoAtividadeBy(FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquiculturaEnum tipoAquiculturaEnum, AquiculturaTipoAtividadeEnum aquiculturaTipoAtividadeEnum){
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoAtividade.class)
				.createAlias("ideFceAquiculturaLicenca", "fceAquicLic")
				.createAlias("fceAquicLic.ideFce", "fce")
				.createAlias("ideEspecieAquiculturaTipoAtividade", "espAqui")
				.createAlias("espAqui.ideAquiculturaTipoAtividade", "aquicAtividade")
				.createAlias("espAqui.ideEspecie", "especie")
				.createAlias("ideTipoAquicultura", "tipAquic")
				.add(Restrictions.eq("fceAquicLic.ideFceAquiculturaLicenca", fceAquiculturaLicenca.getIdeFceAquiculturaLicenca()))
				.add(Restrictions.eq("tipAquic.ideTipoAquicultura", tipoAquiculturaEnum.getId()))
				.add(Restrictions.eq("aquicAtividade.ideAquiculturaTipoAtividade", aquiculturaTipoAtividadeEnum.getId()));
		return fceAquiculturaLicencaTipoAtividadeIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividade> listarFceAquiculturaLicencaTipoAtividadeBy(TipoAquiculturaEnum tipoAquiculturaEnum, Requerimento requerimento){
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoAtividade.class, "falta")
				.createAlias("falta.ideFceAquiculturaLicenca", "fal")
				.createAlias("falta.ideTipoAquicultura", "ta")
				.createAlias("fal.ideFce", "f")
				.add(Restrictions.eq("ta.ideTipoAquicultura", tipoAquiculturaEnum.getId()))
				.add(Restrictions.eq("f.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		return fceAquiculturaLicencaTipoAtividadeIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceAquiculturaLicencaTipoAtividade(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) {
		fceAquiculturaLicencaTipoAtividadeIDAO.salvarOuAtualizar(fceAquiculturaLicencaTipoAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceAquiculturaLicencaTipoAtividadeBy(FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquiculturaEnum tipoAquiculturaEnum) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquiculturaLicenca", fceAquiculturaLicenca.getIdeFceAquiculturaLicenca());
		parameters.put("ideTipoAquicultura", tipoAquiculturaEnum.getId());
		fceAquiculturaLicencaTipoAtividadeIDAO.executarNamedQuery("FceAquiculturaLicencaTipoAtividade.removeByIdeFceAquiculturaLicencaAndTipoAquicultura", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaLicencaTipoAtividadeBy(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideTipoAquicultura", fceAquiculturaLicencaTipoAtividade.getIdeTipoAquicultura().getIdeTipoAquicultura());
		parameters.put("ideEspecieAquiculturaTipoAtividade", fceAquiculturaLicencaTipoAtividade.getIdeEspecieAquiculturaTipoAtividade().getIdeEspecieAquiculturaTipoAtividade());
		parameters.put("ideFceAquiculturaLicenca", fceAquiculturaLicencaTipoAtividade.getIdeFceAquiculturaLicenca().getIdeFceAquiculturaLicenca());
		fceAquiculturaLicencaTipoAtividadeIDAO.executarNamedQuery("FceAquiculturaLicencaTipoAtividade.removeByIdeTipoAquiculturaAndEspecieAquiculturaTipoAtividadeAndFceAquiculturaLicenca", parameters);
	}
}