package br.gov.ba.seia.service;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.MunicipioDAOImpl;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.EnderecoEmpreendimentoMunicipio;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.FceCanal;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.enumerator.EstadoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoMunicipioEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MunicipioService {

	@Inject
	IDAO<Municipio> daoMunicipio;
	
	@Inject
	MunicipioDAOImpl municipioDAOImpl;
	
	@Inject
	IDAO<Double> doubleDAO;
	
	public void salvarMunicipio(Municipio municipio)  {
		daoMunicipio.salvar(municipio);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(Municipio municipio)  {
		daoMunicipio.salvarOuAtualizar(municipio);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> listarMunicipioAdicional(Integer ideEmpreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimentoMunicipio.class);
		criteria
			.createAlias("ideMunicipio", "m", JoinType.INNER_JOIN)
			.createAlias("ideEnderecoEmpreendimento", "ee", JoinType.INNER_JOIN)
			.createAlias("ee.ideEmpreendimento", "e", JoinType.INNER_JOIN)
			.add(Restrictions.eq("e.ideEmpreendimento", ideEmpreendimento))
			.addOrder(Order.asc("nomMunicipio"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("m.ideMunicipio"),"ideMunicipio")
				.add(Projections.property("m.nomMunicipio"),"nomMunicipio")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Municipio.class))
		;
		return  daoMunicipio.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> listarTodosMunicipiosEnvolvidos(Integer ideEmpreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class);
		
		criteria
			.createAlias("logradouroCollection", "l", JoinType.INNER_JOIN)
			.createAlias("l.enderecoCollection", "end", JoinType.INNER_JOIN)
			.createAlias("end.enderecoEmpreendimentoCollection", "ee", JoinType.INNER_JOIN)
			.createAlias("ee.ideEmpreendimento", "emp", JoinType.INNER_JOIN)
			.createAlias("ee.ideTipoEndereco", "t", JoinType.INNER_JOIN)
			.add(Restrictions.or(
					Property.forName("ideMunicipio").in(
						DetachedCriteria.forClass(EnderecoEmpreendimento.class)
							.createAlias("ideTipoEndereco", "t", JoinType.INNER_JOIN)
							.createAlias("ideEmpreendimento", "emp", JoinType.INNER_JOIN)
							.createAlias("enderecoEmpreendimentoMunicipioCollection", "eem", JoinType.LEFT_OUTER_JOIN)
							.createAlias("eem.ideMunicipio", "m", JoinType.LEFT_OUTER_JOIN)
							.add(Restrictions.eq("emp.ideEmpreendimento", ideEmpreendimento))
							.add(Restrictions.eq("t.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
							.setProjection(Projections.property("m.ideMunicipio"))
					),
					Restrictions.and(
						Restrictions.eq("emp.ideEmpreendimento", ideEmpreendimento), 
						Restrictions.eq("t.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId())
					)
				)
			)
			.addOrder(Order.asc("nomMunicipio"))
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideMunicipio"),"ideMunicipio")
				.add(Projections.groupProperty("nomMunicipio"),"nomMunicipio")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Municipio.class))
		;
		return  daoMunicipio.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> listarMunicipioPorListaIBGE(Collection<Double> listaIBGE)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class);
		criteria
			.add(Restrictions.in("coordGeobahiaMunicipio", listaIBGE))
			.addOrder(Order.asc("nomMunicipio"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideMunicipio"),"ideMunicipio")
				.add(Projections.property("nomMunicipio"),"nomMunicipio")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Municipio.class))
		;
		return  daoMunicipio.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> listarMunicipio()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class).addOrder(Order.asc("nomMunicipio"))
			.add(Restrictions.eq("indExcluido", false));

		return  daoMunicipio.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> filtrarListaMunicipios(Municipio municipio) {
		return  daoMunicipio.listarPorExemplo(municipio);
	}
	
	public void excluirMunicipios(Municipio municipio)  {
		daoMunicipio.remover(municipio);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> obterMunicipiosBahiaByNome(String nomeMunicipio)  {
		DetachedCriteria detachedCriteria = getCriteria(nomeMunicipio);
		
		detachedCriteria
		//adicionado
			.add(Restrictions.eq("indExcluido", false))
			.addOrder(Order.asc("nomMunicipio"))
			.setProjection(
				Projections.projectionList()
				.add(Projections.property("ideMunicipio"),"ideMunicipio")
				.add(Projections.property("nomMunicipio"),"nomMunicipio")
		)
		.setResultTransformer(new AliasToNestedBeanResultTransformer(Municipio.class));
		
		return daoMunicipio.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> obterMunicipiosBahiaByNomeDemanda(String nomeMunicipio, Integer first, Integer pageSize)  {
		DetachedCriteria detachedCriteria = getCriteria(nomeMunicipio);
		
		detachedCriteria
		.addOrder(Order.asc("nomMunicipio"))
		.setProjection(
			Projections.projectionList()
				.add(Projections.property("ideMunicipio"),"ideMunicipio")
				.add(Projections.property("nomMunicipio"),"nomMunicipio")
			)
		.setResultTransformer(new AliasToNestedBeanResultTransformer(Municipio.class));
		
		return daoMunicipio.listarPorCriteriaDemanda(detachedCriteria, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer obterMunicipiosBahiaByNomeCount(String nomeMunicipio)  {
		DetachedCriteria detachedCriteria = getCriteria(nomeMunicipio);
		return daoMunicipio.count(detachedCriteria);
	}

	private DetachedCriteria getCriteria(String nomeMunicipio) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Municipio.class);
		
		detachedCriteria
			.add(Restrictions.ilike("nomMunicipio", nomeMunicipio,MatchMode.ANYWHERE))		
			.add(Restrictions.eq("ideEstado.ideEstado", EstadoEnum.BAHIA.getId() ))
			.add(Restrictions.in("ideTipoMunicipio.ideTipoMunicipio", getTiposMunicipios()));
			
		return detachedCriteria;
	}
	
	
	/**
	 * Método para buscar os múnicipios pelo nome desconsiderando os acentos.
	 * @param nomeMunicipio
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> obterMunicipiosBahiaByNomeSemAcento(String nomeMunicipio)  {
		Integer estadoBahia = 5;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Municipio.class);
		
		detachedCriteria.setProjection(
				Projections.projectionList().add(Projections.property("nomMunicipio"),"nomMunicipio")
				.add(Projections.property("ideMunicipio"),"ideMunicipio")
		);
		
		String parametro = Normalizer.normalize(nomeMunicipio, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase();  
        
		detachedCriteria
		.add(Restrictions.sqlRestriction("TRANSLATE(Upper ({alias}.nom_municipio),'ÂÃÀÁÉÈÍÕÓÒÔÚÇ','AAAAEEIOOOOUC') LIKE  '%" + parametro + "%'"))  
			.add(Restrictions.eq("ideEstado.ideEstado", estadoBahia ))
			.add(Restrictions.in("ideTipoMunicipio.ideTipoMunicipio", getTiposMunicipios()))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Municipio.class));
		
		return daoMunicipio.listarPorCriteria(detachedCriteria);
	}

	private List<Integer> getTiposMunicipios() {
		Integer[] tiposMunicipio = {1,2};
		return Arrays.asList(tiposMunicipio);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> filtrarListaMunicipiosPorEstado(Estado estado)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideEstado", estado);
		//params.put("indExcluido", false);
	
		
		return daoMunicipio.buscarPorNamedQuery("Municipio.findByIdeEstado", params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> listarMunicipiosPorEstado(Estado estado)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class)
				.createAlias("ideEstado", "e")
				.add(Restrictions.eq("ideEstado.ideEstado", estado.getIdeEstado()))
				.add(Restrictions.eq("indExcluido", false))
				.setProjection(
						Projections.projectionList()
							.add(Projections.property("ideMunicipio"), "ideMunicipio")
							.add(Projections.property("nomMunicipio"), "nomMunicipio")
							.add(Projections.property("e.ideEstado"), "ideEstado.ideEstado")
							.add(Projections.property("e.nomEstado"), "ideEstado.nomEstado")
							
						).setResultTransformer(new AliasToNestedBeanResultTransformer(Municipio.class))
						;
		return daoMunicipio.listarPorCriteria(criteria, Order.asc("nomMunicipio"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio filtrarMunicipioById(Municipio municipio)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideMunicipio", municipio.getIdeMunicipio());
		return daoMunicipio.buscarEntidadePorNamedQuery("Municipio.findByIdeMunicipio", params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> filtrarMunicipiosUR(Area ur)  {
		HashMap< String, Object> params = new HashMap<String, Object>();
		params.put("area", ur);
		
		return daoMunicipio.buscarPorNamedQuery("Municipio.findByUr", params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio getMunicipioByEmpreendimento(Empreendimento empreendimento)  {
		return municipioDAOImpl.getMunicipioByEmpreendimento(empreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio buscarCoordGeoMunicipioPorRequerimento(Integer ideRequerimento)  {
		return municipioDAOImpl.buscarCoordGeoMunicipioPorRequerimento(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio buscarCoordGeoMunicipioPorEmpreendimento(Integer ideEmpreendimento)  {
		return municipioDAOImpl.buscarCoordGeoMunicipioPorEmpreendimento(ideEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> listarMunicipioByCep(Logradouro logradouro)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class)
		   .add(Restrictions.eq("numCep", logradouro.getNumCep()));
		return daoMunicipio.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> filtrarListaMunicipiosDaBahia()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class, "municipio")
		    .createAlias("ideTipoMunicipio", "ideTipoMunicipio", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideEstado.ideEstado", EstadoEnum.BAHIA.getId() ))
			.add(Restrictions.eq("ideTipoMunicipio.ideTipoMunicipio", TipoMunicipioEnum.MUNICIPIO.getId()))
			//editado aqui
			.add(Restrictions.eq("indExcluido", false))
			.addOrder(Order.asc("nomMunicipio"));
		
		return daoMunicipio.listarPorCriteria(criteria);
	}
	
	

	public Municipio validarCep(Integer numCep)  {
		String sql = "SELECT sp_get_municipio_cep("+numCep+") ";
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(sql);
								
		String nomeMunicipio = (String)lQuery.getSingleResult();
		
		return obterMunicipiosBahiaPorNome(nomeMunicipio);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio obterMunicipiosBahiaPorNome(String nomeMunicipio)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Municipio.class, "municipio")
			.add(Restrictions.ilike("nomMunicipio", nomeMunicipio,MatchMode.EXACT))		
			.add(Restrictions.eq("ideEstado.ideEstado", EstadoEnum.BAHIA.getId() ))
			.add(Restrictions.eq("indExcluido", false));
		
		return daoMunicipio.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio obterMunicipioPorCep(int cep) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Municipio.class)
			.add(Restrictions.eq("numCep", cep))		
			.add(Restrictions.eq("ideEstado.ideEstado", EstadoEnum.BAHIA.getId() ))
			.add(Restrictions.eq("ideTipoMunicipio.ideTipoMunicipio", TipoMunicipioEnum.MUNICIPIO.getId()));
			
			
		return daoMunicipio.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double obterCodigoIBGE(Municipio municipio) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class)
				.add(Restrictions.eq("ideMunicipio", municipio.getIdeMunicipio()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("coordGeobahiaMunicipio"), "coordGeobahiaMunicipio"))
				;
		return doubleDAO.buscarPorCriteria(criteria);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio carregarMunicipioByLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica){
		return municipioDAOImpl.carregarMunicipioByLocalizacaoGeografica(localizacaoGeografica);
	}
	
	public List<Municipio> listaMunicipiosPorFceCanal(FceCanal fceCanal) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fceCanal", fceCanal);
		return daoMunicipio.buscarPorNamedQuery("Municipio.findByFceCanal", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio obterMunicipio(Integer idMunicipio) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideMunicipio", idMunicipio);
		return daoMunicipio.buscarEntidadePorNamedQuery("Municipio.findByIdeMunicipio", parametros);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer buscarIdeMunicipioByNome(String nome) {
		String sql = "SELECT m.ide_municipio "
				+ "FROM municipio m "
				+ "WHERE m.ind_excluido = false and upper(m.nom_municipio) = '"+nome.toUpperCase().replace("'", "''")+"' order by ide_municipio asc limit 1";
				
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql);
								
		Integer obj = (Integer) lQuery.getSingleResult();
        if(!Util.isNull(obj)){
        	return obj; 
        }
			return null;
	
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer buscarIdeMunicipioByNomeANDIdeEstado(String nome, Integer ideEstado) {
		String sql = "SELECT m.ide_municipio "
				+ "FROM municipio m "
				+ "WHERE m.ind_excluido = false and upper(m.nom_municipio) = '"+nome.toUpperCase().replace("'", "''")
				+ "' and ide_estado="+ideEstado
				+" order by ide_municipio asc limit 1";
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql);
								
		Integer obj = (Integer) lQuery.getSingleResult();
        if(!Util.isNull(obj)){
        	return obj; 
        }
			return null;
	
	}
	
	/**
	 * Query para encontra o idMunicipio caso a API venha NULL {@link Municipio}
	 * 
	 * @author luiz.melo 
	 * @since 11/04/2022
	 * @see <a href="http://172.16.0.27/issues/33770">#33770</a>
	 * @param event
	 */
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer buscarIdeMunicipioByNomeByCep(String nome, String numCep) {
		String sql = "SELECT m.ide_municipio "
				+ "FROM municipio m "
				+ "WHERE m.nom_municipio = '"+nome.replace("'", "''")
				+ "' OR num_cep = " + numCep 
				+ " ORDER  BY ide_municipio asc limit 1";
	
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql);
								
		Integer obj = (Integer) lQuery.getSingleResult();
        if(!Util.isNull(obj)){
        	return obj; 
        }
			return null;
	
	}	
	
}
