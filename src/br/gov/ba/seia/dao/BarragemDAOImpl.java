package br.gov.ba.seia.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Barragem;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 24/03/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BarragemDAOImpl extends AbstractDAO<Barragem> {
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<Barragem> dao;
	
	@Override
	protected IDAO<Barragem> getDAO() {
		return dao;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> listarBarragemByNativeQuery() {
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery
				("SELECT b.ide_barragem, b.nom_barragem, b.ide_objeto_pai, ST_X (b.the_geom), ST_Y(b.the_geom), "
				+"(SELECT nom_municipio	FROM municipio WHERE ide_tipo_municipio =1 and cod_ibge_municipio = " 
				+"(SELECT CAST (sp_get_municipio(b.the_geom) AS numeric) )) as municipio "
				+"FROM barragem b "
				+"ORDER BY b.nom_barragem;");
		return lQuery.getResultList();
	}
	
	public Object[] carregarBarragemByNativeQuery(Integer ideBarragem) {
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT b.ide_barragem, b.nom_barragem, b.ide_objeto_pai, ST_X (b.the_geom), ST_Y(b.the_geom), "
					+"(SELECT nom_municipio	FROM municipio WHERE ide_tipo_municipio =1 and cod_ibge_municipio = " 
					+"(SELECT CAST (sp_get_municipio(b.the_geom) AS numeric) )) as municipio "
					+"FROM barragem b "
					+"WHERE b.ide_barragem = " + ideBarragem
					+" ORDER BY b.nom_barragem;");
		return (Object[]) lQuery.getSingleResult();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Barragem carregar(Integer ide)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Barragem.class)
				.add(Restrictions.eq("ideBarragem", ide))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideBarragem"), "ideBarragem")
						.add(Projections.property("nomBarragem"), "nomBarragem")
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						.add(Projections.property("indOrigemUsuario"), "indOrigemUsuario")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(Barragem.class))
				;
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Barragem barragem){
		dao.salvar(barragem);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateTheGeomInBarragem(Map<String, Object> parametros) {
		dao.executarNativeQuery("UPDATE barragem SET the_geom = "
				+ "(SELECT the_geom FROM dado_geografico WHERE ide_localizacao_geografica = :ideLocalizacaoGeografica)"
				+ "WHERE ide_barragem = :ideBarragem", parametros);
	}


}
