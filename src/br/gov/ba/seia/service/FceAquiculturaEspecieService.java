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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.FceAquiculturaEspecie;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 21/11/2014
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaEspecieService {

	@Inject
	private IDAO<FceAquiculturaEspecie> fceAquiculturaEspecieIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaEspecie> listarFceAquiculturaByIdeFceAquicultura(FceAquicultura fceAquicultura) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquiculturaEspecie.class);
		criteria.createAlias("ideEspecieAquiculturaTipoAtividade", "ea", JoinType.INNER_JOIN);
		criteria.createAlias("ea.ideEspecie", "especie");
		criteria.createAlias("ea.ideAquiculturaTipoAtividade", "ta");
		criteria.add(Restrictions.eq("ideFceAquicultura.ideFceAquicultura", fceAquicultura.getIdeFceAquicultura()));
		return fceAquiculturaEspecieIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaEspecie> listarFceAquiculturaByIdeRequerimentoAndTipoAquicultura(Requerimento requerimento, TipoAquiculturaEnum tipoAquiculturaEnum) {

		 DetachedCriteria criteria = buscarCriteriaFceAquiculturaEspecieBy(requerimento)
				.createAlias("fce.ideRequerimento", "req")
				.createAlias("eat.ideAquiculturaTipoAtividade", "ata");

		if(tipoAquiculturaEnum.equals(TipoAquiculturaEnum.VIVEIRO_ESCAVADO)){
			criteria.add(Restrictions.eq("taPai.ideTipoAquicultura", TipoAquiculturaEnum.VIVEIRO_ESCAVADO.getId()));
		}
		else if(tipoAquiculturaEnum.equals(TipoAquiculturaEnum.TANQUE_REDE)){
			criteria.add(Restrictions.eq("taPai.ideTipoAquicultura", TipoAquiculturaEnum.TANQUE_REDE.getId()));
		}
		criteria.setProjection(
				Projections.projectionList()
				.add(Projections.property("ideFceAquiculturaEspecie"), "ideFceAquiculturaEspecie")
				.add(Projections.property("fce.ideFce"), "ideFceAquicultura.ideFce.ideFce")
				.add(Projections.property("fa.ideFceAquicultura"), "ideFceAquicultura.ideFceAquicultura")
				.add(Projections.property("ta.ideTipoAquicultura"), "ideFceAquicultura.ideTipoAquicultura.ideTipoAquicultura")
				.add(Projections.property("taPai.ideTipoAquicultura"), "ideFceAquicultura.ideTipoAquicultura.ideTipoAquiculturaPai.ideTipoAquicultura")
				.add(Projections.property("eat.ideEspecieAquiculturaTipoAtividade"), "ideEspecieAquiculturaTipoAtividade.ideEspecieAquiculturaTipoAtividade")
				.add(Projections.property("ata.ideAquiculturaTipoAtividade"), "ideEspecieAquiculturaTipoAtividade.ideAquiculturaTipoAtividade.ideAquiculturaTipoAtividade")
				.add(Projections.property("ata.nomAquiculturaTipoAtividade"), "ideEspecieAquiculturaTipoAtividade.ideAquiculturaTipoAtividade.nomAquiculturaTipoAtividade")
				.add(Projections.property("esp.nomEspecie"), "ideEspecieAquiculturaTipoAtividade.ideEspecie.nomEspecie")
				.add(Projections.property("esp.nomPopularEspecie"), "ideEspecieAquiculturaTipoAtividade.ideEspecie.nomPopularEspecie")
				.add(Projections.property("esp.ideEspecie"), "ideEspecieAquiculturaTipoAtividade.ideEspecie.ideEspecie"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(FceAquiculturaEspecie.class));
		return fceAquiculturaEspecieIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private DetachedCriteria buscarCriteriaFceAquiculturaEspecieBy(Requerimento requerimento) {
		return DetachedCriteria.forClass(FceAquiculturaEspecie.class)
				.createAlias("ideFceAquicultura", "fa")
				.createAlias("fa.ideFce", "fce")
				.createAlias("ideEspecieAquiculturaTipoAtividade", "eat")
				.createAlias("eat.ideEspecie", "esp")
				.createAlias("fa.ideTipoAquicultura", "ta")
				.createAlias("ta.ideTipoAquiculturaPai", "taPai")
				.add(Restrictions.eq("fce.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAquicultura(List<FceAquiculturaEspecie> listaFceAquiculturaEspecie) {
		fceAquiculturaEspecieIDAO.salvarEmLote(listaFceAquiculturaEspecie);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaEspecieByIdeFceAquicultura(FceAquicultura fceAquicultura) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceAquicultura", fceAquicultura.getIdeFceAquicultura());
		fceAquiculturaEspecieIDAO.executarNamedQuery("FceAquiculturaEspecie.removeByIdeFceAquicultura",parameters);
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaEspecie> listarFceAquiculturaEspecieSemRepetirEspecieAquiculturaTipoAtividadeBy(Requerimento requerimento, TipoAquiculturaEnum tipoAquiculturaEnum) throws Exception{
		
		DetachedCriteria criteria = buscarCriteriaFceAquiculturaEspecieBy(requerimento)
				.add(Restrictions.eq("taPai.ideTipoAquicultura", tipoAquiculturaEnum.getId()))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("eat.ideEspecieAquiculturaTipoAtividade"),"ideEspecieAquiculturaTipoAtividade.ideEspecieAquiculturaTipoAtividade")
						.add(Projections.groupProperty("eat.ideEspecieAquiculturaTipoAtividade"))
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(FceAquiculturaEspecie.class));
		return fceAquiculturaEspecieIDAO.listarPorCriteria(criteria, Order.asc("eat.ideEspecieAquiculturaTipoAtividade"));
	}
}