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
import br.gov.ba.seia.entity.Especie;
import br.gov.ba.seia.entity.EspecieAquiculturaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaEspecie;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EspecieAquiculturaTipoAtividadeService {

	@Inject
	private IDAO<EspecieAquiculturaTipoAtividade> especieAquiculturaTipoAtividadeDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieAquiculturaTipoAtividade> listarEspecieAquiculturaTipoAtividadeByIdeAquiculturaTipoAtividade(AquiculturaTipoAtividade tipoAtividade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieAquiculturaTipoAtividade.class);
		criteria.createAlias("ideEspecie", "e");
		criteria.createAlias("ideAquiculturaTipoAtividade", "ta");
		criteria.add(Restrictions.eq("ta.ideAquiculturaTipoAtividade", tipoAtividade.getIdeAquiculturaTipoAtividade()));
		return especieAquiculturaTipoAtividadeDAO.listarPorCriteria(criteria,Order.asc("e.ideEspecie"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EspecieAquiculturaTipoAtividade buscarEspecieAquiculturaTipoAtividadeByIdeAquiculturaTipoAtividadeAndIdeEspecie(AquiculturaTipoAtividade tipoAtividade, Especie especie) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieAquiculturaTipoAtividade.class);
		criteria.add(Restrictions.eq("ideAquiculturaTipoAtividade.ideAquiculturaTipoAtividade", tipoAtividade.getIdeAquiculturaTipoAtividade()));
		criteria.add(Restrictions.eq("ideEspecie.ideEspecie", especie.getIdeEspecie()));
		return especieAquiculturaTipoAtividadeDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EspecieAquiculturaTipoAtividade buscarEspecieAquiculturaTipoAtividadeByIdeEspecieAquiculturaTipoAtividade(EspecieAquiculturaTipoAtividade especieAquiculturaTipoAtividade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieAquiculturaTipoAtividade.class);
		criteria.createAlias("ideEspecie", "e");
		criteria.add(Restrictions.eq("ideEspecieAquiculturaTipoAtividade", especieAquiculturaTipoAtividade.getIdeEspecieAquiculturaTipoAtividade()));
		return especieAquiculturaTipoAtividadeDAO.buscarPorCriteria(criteria);
	}

	public List<EspecieAquiculturaTipoAtividade> listarFceAquiculturaEspecieByRequerimentoAntTipoAquicultura(Requerimento requerimento, TipoAquiculturaEnum tipoAquiculturaEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaEspecie.class)
				.createAlias("ideFceAquicultura", "fa")
				.createAlias("ideEspecieAquiculturaTipoAtividade", "eat")
				.createAlias("fa.ideTipoAquicultura", "ta")
				.createAlias("ta.ideTipoAquiculturaPai", "taPai")
				.createAlias("fa.ideFce", "f")
				.createAlias("f.ideRequerimento", "r")
				.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()))
				.add(Restrictions.eq("taPai.ideTipoAquicultura", tipoAquiculturaEnum.getId()))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("eat.ideEspecieAquiculturaTipoAtividade"), "ideEspecieAquiculturaTipoAtividade.ideEspecieAquiculturaTipoAtividade")
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(EspecieAquiculturaTipoAtividade.class));
		return especieAquiculturaTipoAtividadeDAO.listarPorCriteria(criteria);
	}

	public List<EspecieAquiculturaTipoAtividade> listarFceAquiculturaEspecieByRequerimentoAntTipoAquicultura(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaLicencaTipoAtividade.class)
				.createAlias("ideFceAquiculturaLicenca", "fal")
				.createAlias("ideEspecieAquiculturaTipoAtividade", "eat")
				.createAlias("fal.ideFce", "f")
				.createAlias("f.ideRequerimento", "r")
				.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("eat.ideEspecieAquiculturaTipoAtividade"), "ideEspecieAquiculturaTipoAtividade.ideEspecieAquiculturaTipoAtividade")
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(EspecieAquiculturaTipoAtividade.class));
		return especieAquiculturaTipoAtividadeDAO.listarPorCriteria(criteria);
	}
}
