package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Classe;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.PotencialPoluicao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClasseDAOImpl {

	@Inject
	IDAO<Classe> classeDAO;	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Classe buscarClasseAtividade(Porte porte, PotencialPoluicao idePotencialPoluicao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Classe.class);

		criteria.createAlias("classePortePotencialPoluicaoCollection", "cppp")
			.createAlias("cppp.classe", "classe")
			.createAlias("cppp.porte", "porte").createAlias("cppp.potencialPoluicao", "potencial")

			.add(Restrictions.or(Restrictions.eq("cppp.indAtivo", true), Restrictions.isNull("cppp.indAtivo")))
			.add(Restrictions.eq("potencial.idePotencialPoluicao", idePotencialPoluicao.getIdePotencialPoluicao()))
			.add(Restrictions.eq("porte.idePorte", porte.getIdePorte()))
		;

		return classeDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Classe> listarClasse() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Classe.class);
		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideClasse"), "ideClasse")
				.add(Projections.property("nomClasse"), "nomClasse")
				.add(Projections.property("indAtivo"), "indAtivo")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Classe.class))
		;
		
		return classeDAO.listarPorCriteria(criteria);
	}

}
