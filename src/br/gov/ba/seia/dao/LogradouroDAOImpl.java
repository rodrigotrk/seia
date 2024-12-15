package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

public class LogradouroDAOImpl {
	
	@Inject
	private IDAO<Logradouro> logradouroDAO;
	
	public void salvarLogradouro(Logradouro logradouro)  {
		logradouroDAO.salvarOuAtualizar(logradouro);
	}
		
	public List<Logradouro> filtrarLogradouroByCep(Logradouro logradouro)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Logradouro.class);
		criteria.setFetchMode("ideTipoLogradouro", FetchMode.JOIN)
			.createAlias("ideMunicipio", "m", JoinType.INNER_JOIN)
			.setFetchMode("ideBairro", FetchMode.JOIN)
			.add(Restrictions.or(Restrictions.eq("numCep", logradouro.getNumCep()), Restrictions.eq("m.numCep", logradouro.getNumCep())))
			.add(Restrictions.or(Restrictions.eq("indOrigemCorreio", true),Restrictions.eq("indOrigemApi", true)));
		
			
			
		return logradouroDAO.listarPorCriteria(criteria);		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Logradouro> filtrarLogradouroByCep(Integer numCep, Integer ideLogradouro)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Logradouro.class)
				.createAlias("ideTipoLogradouro", "tipoLogradouro", JoinType.INNER_JOIN)
				.createAlias("ideMunicipio", "m", JoinType.INNER_JOIN)
				.createAlias("ideBairro", "b", JoinType.INNER_JOIN)
				.add(Restrictions.eq("numCep",numCep))
				.add(Restrictions.or(
						Restrictions.or(
								Restrictions.and(Restrictions.eq("indOrigemCorreio", true),Restrictions.eq("b.indOrigemCorreio", true)), 
								Restrictions.eq("indOrigemApi", true)), 
						Restrictions.eq("ideLogradouro", ideLogradouro)));
		return logradouroDAO.listarPorCriteria(criteria);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Logradouro> filtrarLogradouroByCepSemIndCorreio(Logradouro logradouro)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Logradouro.class);
		criteria.setFetchMode("ideTipoLogradouro", FetchMode.JOIN)
				.createAlias("ideMunicipio", "m", JoinType.INNER_JOIN)
				.createAlias("ideBairro", "b", JoinType.INNER_JOIN)
			.add(Restrictions.eq("numCep",logradouro.getNumCep()));
		return logradouroDAO.listarPorCriteria(criteria);		
	}
		
	public Logradouro filtrarLogradouroById(Logradouro logradouro) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Logradouro.class);
		criteria.setFetchMode("ideTipoLogradouro", FetchMode.JOIN)
				  .setFetchMode("ideMunicipio", FetchMode.JOIN)
				  .setFetchMode("ideMunicipio.ideEstado", FetchMode.JOIN)
			    .setFetchMode("ideBairro", FetchMode.JOIN)
			    .add(Restrictions.eq("ideLogradouro", logradouro.getIdeLogradouro()));
		return logradouroDAO.buscarPorCriteria(criteria);
	}
	
	public List<Logradouro> filtrarLogradouroByBairro(Bairro bairro, Integer numCep)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Logradouro.class, "logradouro");
		criteria.setFetchMode("ideTipoLogradouro", FetchMode.JOIN)
			    .setFetchMode("ideMunicipio", FetchMode.JOIN)
			    .createAlias("ideBairro", "bairro")
			    .add(Restrictions.eq("numCep", numCep))
    			.add(Restrictions.eq("bairro.ideBairro", bairro.getIdeBairro()))
    			.add(Restrictions.or(Restrictions.eq("logradouro.indOrigemCorreio", true),Restrictions.eq("logradouro.indOrigemApi", true)));
		return logradouroDAO.listarPorCriteria(criteria);
	}
	
	public List<Logradouro> filtrarLogradouroByBairroAndApi(Bairro bairro, Integer numCep)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Logradouro.class, "logradouro");
		criteria.setFetchMode("ideTipoLogradouro", FetchMode.JOIN)
			    .setFetchMode("ideMunicipio", FetchMode.JOIN)
			    .createAlias("ideBairro", "bairro")
			    .add(Restrictions.eq("numCep", numCep))
    			.add(Restrictions.eq("bairro.ideBairro", bairro.getIdeBairro()))
    			.add(Restrictions.eq("logradouro.indOrigemApi", true));
		return logradouroDAO.listarPorCriteria(criteria);
	}

	public List<Logradouro> filtrarLogradouroByBairroSemIndCorreio(Bairro bairro, Integer numCep)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Logradouro.class);
		criteria.setFetchMode("ideTipoLogradouro", FetchMode.JOIN)
			    .setFetchMode("ideMunicipio", FetchMode.JOIN)
			    .createAlias("ideBairro", "bairro")
			    .add(Restrictions.eq("numCep", numCep))
			    .add(Restrictions.eq("bairro.ideBairro", bairro.getIdeBairro()));	
		return logradouroDAO.listarPorCriteria(criteria);
	}
	
	public List<Logradouro> filtrarLogradouroByNome(Bairro bairro, Integer numCep)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Logradouro.class)
			.setFetchMode("ideTipoLogradouro", FetchMode.JOIN)
		    .setFetchMode("ideMunicipio", FetchMode.JOIN)
		    .createAlias("ideBairro", "bairro")
		    .add(Restrictions.and(
				Restrictions.and(Restrictions.eq("numCep", numCep), Restrictions.or(Restrictions.eq("indOrigemCorreio", true),Restrictions.eq("indOrigemApi", true))),
				Restrictions.sqlRestriction("rtrim(nom_bairro) ilike '" + bairro.getNomBairro().trim() +"' and estado5_.ide_estado = 5 and nom_municipio like '%' || sp_get_municipio_cep("+numCep+") AND this_.ind_origem_correio is true")));
		
		List<Logradouro> listTemp = logradouroDAO.listarPorCriteria(criteria, Order.asc("nomLogradouro"));
		
		List<Logradouro> listFinal = new ArrayList<Logradouro>();
		
		for (Logradouro logradouroTemp : listTemp) {
			boolean possui = false;
			
			for (Logradouro logradouroOriginal : listFinal) {
				
				if(logradouroOriginal.getNomLogradouro().trim().equalsIgnoreCase(logradouroTemp.getNomLogradouro().trim())) {
					possui = true;
					break;
				}
			}
			
			if(!possui) {
				listFinal.add(logradouroTemp);
			}
		}
		
		return listFinal;
	}
	
	public List<Logradouro> filtrarLogradouroByNomeAndApi(Bairro bairro, Integer numCep)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Logradouro.class)
			.setFetchMode("ideTipoLogradouro", FetchMode.JOIN)
		    .setFetchMode("ideMunicipio", FetchMode.JOIN)
		    .createAlias("ideBairro", "bairro")
		    .add(Restrictions.and(
				Restrictions.and(Restrictions.eq("numCep", numCep), Restrictions.eq("indOrigemApi", true)),
				Restrictions.sqlRestriction("rtrim(nom_bairro) ilike '" + bairro.getNomBairro().trim() +"' and estado5_.ide_estado = 5 and nom_municipio like '%' || sp_get_municipio_cep("+numCep+") AND this_.ind_origem_api is true")));
		
		List<Logradouro> listTemp = logradouroDAO.listarPorCriteria(criteria, Order.asc("nomLogradouro"));
		
		List<Logradouro> listFinal = new ArrayList<Logradouro>();
		
		for (Logradouro logradouroTemp : listTemp) {
			boolean possui = false;
			
			for (Logradouro logradouroOriginal : listFinal) {
				
				if(logradouroOriginal.getNomLogradouro().trim().equalsIgnoreCase(logradouroTemp.getNomLogradouro().trim())) {
					possui = true;
					break;
				}
			}
			
			if(!possui) {
				listFinal.add(logradouroTemp);
			}
		}
		
		return listFinal;
	}
	
	public List<Logradouro> listarLogradourosByMunicipio(Municipio municipio) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideMunicipio", municipio);
		return logradouroDAO.buscarPorNamedQuery("Logradouro.findByIdeMunicipio", params);
	}

	public Logradouro getLogradouroById(int ideLogradouro) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Logradouro.class, "logradouro")
				.add(Restrictions.eq("logradouro.ideLogradouro", ideLogradouro));
		
		return logradouroDAO.buscarPorCriteria(criteria);
	}

	/**
	 * 
	 * Método que retorna o {@link Logradouro} apenas com: 
	 * <ul>
	 * <li>ideLogradouro</li>
	 * <li>indOrigemCorreio</li>
	 * </ul>
	 * 
	 * @author eduardo.fernandes 
	 * @since 09/02/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8338">#8338</a>
	 * @param bairro
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Logradouro getLogradouroBy(Endereco endereco) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Endereco.class, "e")
				.createAlias("e.ideLogradouro", "log")
				.add(Restrictions.eq("e.ideEndereco", endereco.getIdeEndereco()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("log.ideLogradouro"),"ideLogradouro")
						.add(Projections.property("log.indOrigemCorreio"),"indOrigemCorreio")
						.add(Projections.property("log.indOrigemApi"),"indOrigemApi")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(Logradouro.class))
				;
		return logradouroDAO.buscarPorCriteria(criteria);
	}
	
	public Logradouro getLogradouroByEndereco(String nmLogradouro, Integer ideBairro, Integer ideCidade,
			Integer numCep, Integer ideTipoLogradouro ) {
		String wh_bairro = (ideBairro==null)?"ide_bairro is null":"ide_bairro = " + ideBairro;
		String sql = "SELECT l.* FROM logradouro l WHERE " + wh_bairro 
				+ " AND ide_municipio = "+ ideCidade 
				+ " AND upper(nom_logradouro) = '" + nmLogradouro.toUpperCase().replace("'", "\\\'") + "' " 
				+ " AND num_cep = " + numCep
				+ " AND ide_tipo_logradouro = " + ideTipoLogradouro
				+ " ORDER  BY ind_origem_api ASC LIMIT 1 ";

		List<Logradouro> listTemp = logradouroDAO.listarClasseComNativeQuery(sql, Logradouro.class);
		for (Logradouro logradouroTemp : listTemp) {
			return logradouroTemp;
		}
		return null;
	}
}
