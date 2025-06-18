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
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EspecieService {

	@Inject
	private IDAO<Especie> especieDAO;

	/**
	 * Método para buscar as {@link Especie} de acordo com o {@link AquiculturaTipoAtividade} escolhido pelo usuário.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 12/11/2014
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Especie> listarEspecieByIdeAquiculturaTipoAtividade(AquiculturaTipoAtividade tipoAtividade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieAquiculturaTipoAtividade.class);
		criteria.createAlias("ideEspecie", "e");
		criteria.createAlias("ideAquiculturaTipoAtividade", "ta");
		criteria.add(Restrictions.eq("ta.ideAquiculturaTipoAtividade", tipoAtividade.getIdeAquiculturaTipoAtividade()));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("e.ideEspecie"), "ideEspecie")
				.add(Projections.property("e.nomEspecie"), "nomEspecie")
				.add(Projections.property("e.nomPopularEspecie"), "nomPopularEspecie"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Especie.class));
		return especieDAO.listarPorCriteria(criteria,Order.asc("nomEspecie"));
	}
	
}
