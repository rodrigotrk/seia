package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BairroService {
	
	
	@Inject
	IDAO<Bairro> bairroDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarBairro(Bairro bairro) {
		bairroDAO.salvarOuAtualizar(bairro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Bairro filtrarBairroById(Bairro bairro) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Bairro.class);
		criteria.setFetchMode("logradouroCollection", FetchMode.JOIN)
				.setFetchMode("ideMunicipio", FetchMode.JOIN)
				.add((Restrictions.eq("ideBairro", bairro.getIdeBairro())));
		return bairroDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Bairro> filtrarBairroByCep(Integer numCep)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Bairro.class);
		//criteria.setFetchMode("logradouroCollection", FetchMode.JOIN)
		criteria.createAlias("ideMunicipio", "ideMunicipio")
		.setFetchMode("ideMunicipio", FetchMode.JOIN)
		.add((Restrictions.eq("ideMunicipio.numCep", numCep)));
		return bairroDAO.listarPorCriteria(criteria);
	}

	public Bairro filtrarBairroByIdeBairro(Integer ideBairro)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Bairro.class)
		.add((Restrictions.eq("ideBairro", ideBairro)));
		return bairroDAO.buscarPorCriteria(criteria);
	}
	public List<Bairro> filtrarBairroPorCepSemDuplicidade(Integer numCep){
//		String sql = "SELECT m.ide_municipio, m.nom_municipio, b.ide_bairro, b.nom_bairro, b.ind_origem_correio, l.ide_logradouro, l.nom_logradouro, l.ind_origem_correio"
		String sql = "SELECT b.* "
					+ "FROM municipio m "
					+ "JOIN bairro b ON b.ide_municipio = m.ide_municipio "
					+ "JOIN logradouro l ON l.ide_bairro = b.ide_bairro "
					+ "WHERE m.num_cep = "+ numCep +" AND b.ind_origem_correio is true OR (l.num_cep = "+ numCep +" AND m.nom_municipio like '%' || sp_get_municipio_cep("+numCep+") AND b.ind_origem_correio is true) "
					+ "ORDER BY m.nom_municipio, b.nom_bairro, l.nom_logradouro";
		
		List<Bairro> listTemp = bairroDAO.listarClasseComNativeQuery(sql, Bairro.class);
		List<Bairro> listFinal = new ArrayList<Bairro>();
		for (Bairro bairroTemp : listTemp) {
			boolean possui = false;
			for (Bairro bairroOriginal : listFinal) {
				bairroOriginal.setLogradouroCollection(null);
				if(bairroOriginal.getNomBairro().trim().equalsIgnoreCase(bairroTemp.getNomBairro().trim())) {
					possui = true;
					break;
				}
			}
			if(!possui) {
				listFinal.add(bairroTemp);
			}
		}
		return listFinal;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Bairro> listarBairroByLogradouro(Logradouro logradouro) {
		Integer id = null;
		if(!Util.isNull(logradouro)){ 
			id = logradouro.getIdeLogradouro();
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Bairro.class)
				.createAlias("logradouroCollection", "logr")
				.createAlias("ideMunicipio", "mun")
				.createAlias("mun.ideEstado", "est")
				.add(Restrictions.eq("logr.indOrigemApi", true))
				.add(Restrictions.eqProperty("ideMunicipio", "logr.ideMunicipio"))
				.add((Restrictions.or(Restrictions.eq("mun.numCep", logradouro.getNumCep()),
						Restrictions.eq("logr.numCep", logradouro.getNumCep())))
					)
						.setProjection(Projections.projectionList()
								.add(Projections.groupProperty("ideBairro"), "ideBairro")
								.add(Projections.groupProperty("nomBairro"), "nomBairro")
								.add(Projections.groupProperty("indOrigemCorreio"), "indOrigemCorreio")
								.add(Projections.groupProperty("indOrigemApi"), "indOrigemApi")
								.add(Projections.groupProperty("mun.ideMunicipio"), "ideMunicipio.ideMunicipio")
								.add(Projections.groupProperty("mun.nomMunicipio"), "ideMunicipio.nomMunicipio")
								.add(Projections.groupProperty("mun.indEstadoEmergencia"), "ideMunicipio.indEstadoEmergencia")
								.add(Projections.groupProperty("est.ideEstado"), "ideMunicipio.ideEstado.ideEstado")
								.add(Projections.groupProperty("est.nomEstado"), "ideMunicipio.ideEstado.nomEstado")
								
								).setResultTransformer(new AliasToNestedBeanResultTransformer(Bairro.class))
								;
		return bairroDAO.listarPorCriteria(criteria, Order.asc("nomBairro"));
	}

	/**
	 * 
	 * Método que retorna o {@link Bairro} apenas com: 
	 * <ul>
	 * <li>ideBairro</li>
	 * <li>indOrigemCorreio</li>
	 * </ul>
	 * 
	 * @author eduardo.fernandes 
	 * @since 09/02/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8338">#8338</a>
	 * @param ideEndereco
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Bairro carregarBairroById(Integer ideEndereco){
		DetachedCriteria criteria = DetachedCriteria.forClass(Bairro.class, "b")
				.createAlias("b.logradouroCollection", "log")
				.createAlias("log.enderecoCollection", "e")
				.add((Restrictions.eq("e.ideEndereco", ideEndereco)))
				.setProjection(Projections.projectionList()
						.add(Projections.property("b.ideBairro"), "ideBairro")
						.add(Projections.property("b.indOrigemCorreio"), "indOrigemCorreio")
						.add(Projections.property("b.indOrigemApi"), "indOrigemApi"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(Bairro.class))
						;
		return bairroDAO.buscarPorCriteria(criteria);
	}
	
	
	
	public Bairro buscarBairroByNome(String nome, Integer idMunicipio) {
		String sql = "SELECT b.* "
				+ "FROM municipio m "
				+ "JOIN bairro b ON b.ide_municipio = m.ide_municipio "
				+ "WHERE  upper(b.nom_bairro) = '"+nome.toUpperCase().replace("'", "''")+"' AND m.ide_municipio =  "+ idMunicipio + " order by ide_bairro asc limit 1";
	
		List<Bairro> listTemp = bairroDAO.listarClasseComNativeQuery(sql, Bairro.class);
		
		for (Bairro bairroTemp : listTemp) {
			return bairroTemp;
		}
			return null;
	}
	
	public void adicionarBairroApi(String nome, Integer idMunicipio) {
		EntityManager entityManager = DAOFactory.getEntityManager();
		
		String sql = "INSERT INTO bairro"
				+ " (ide_municipio, nom_bairro, ind_origem_correio, ind_origem_api) "
				+ "VALUES (" + idMunicipio + ", '" + nome.replace("'", "''") + "', false, true) ";
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.executeUpdate();
	}
}
