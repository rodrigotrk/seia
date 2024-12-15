package br.gov.ba.seia.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.SituacaoMunicipio;
import br.gov.ba.seia.entity.TipoMunicipio;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class MunicipioDAOImpl implements MunicipioDAOIf {
	
	@Inject
	IDAO<Municipio> municipioDAO;

    @Override
	@SuppressWarnings("unchecked")
    public List<Municipio> getMunicipios(Municipio pMunicipio) {

    	StringBuilder lEjbql = new StringBuilder("select municipio from Municipio municipio ");

    	if (!Util.isNull(pMunicipio)) {

    		lEjbql.append("where 1 = 1 ");

    		if (!Util.isNull(pMunicipio.getIdeMunicipio())) lEjbql.append(" AND municipio.ideMunicipio = :ideMunicipio");

    		if (!Util.isNull(pMunicipio.getNomMunicipio())) lEjbql.append(" AND lower(municipio.nomMunicipio) LIKE lower(:nomMunicipio)");
    	}

    	lEjbql.append(" order by municipio.nomMunicipio");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pMunicipio)) {

    		if (!Util.isNull(pMunicipio.getIdeMunicipio())) lQuery.setParameter("ideMunicipio", pMunicipio.getIdeMunicipio());

    		if (!Util.isNull(pMunicipio.getNomMunicipio())) lQuery.setParameter("nomMunicipio", pMunicipio.getNomMunicipio() + "%");
    	}

    	return lQuery.getResultList();
    }
    
    @Override
    public Municipio getMunicipioByEmpreendimento(Empreendimento empreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class, "municipio");
		criteria.createAlias("logradouroCollection", "logradouros", JoinType.INNER_JOIN);
		criteria.createAlias("logradouros.enderecoCollection", "enderecos", JoinType.INNER_JOIN);
		criteria.createAlias("enderecos.enderecoEmpreendimentoCollection", "enderecoEmpreendimento", JoinType.INNER_JOIN);
		criteria.createAlias("enderecoEmpreendimento.ideEmpreendimento", "empreendimento", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("empreendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		criteria.add(Restrictions.eq("enderecoEmpreendimento.ideTipoEndereco.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()));
		
		return municipioDAO.buscarPorCriteria(criteria);
			
	}

	public Municipio buscarCoordGeoMunicipioPorRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class);
		criteria.createAlias("bairroCollection", "bairro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bairro.logradouroCollection", "logradouro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.enderecoCollection", "endereco", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("endereco.enderecoEmpreendimentoCollection", "enderecoEmpreendimentoCollection", JoinType.LEFT_OUTER_JOIN,Restrictions.eq("enderecoEmpreendimentoCollection.ideTipoEndereco.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()));		
		criteria.createAlias("enderecoEmpreendimentoCollection.ideEmpreendimento", "empreendimento", JoinType.LEFT_OUTER_JOIN);		
		criteria.createAlias("empreendimento.empreendimentoRequerimentoCollection", "empreq", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("empreq.ideRequerimento", "requerimento", JoinType.LEFT_OUTER_JOIN);
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.distinct(Projections.property("coordGeobahiaMunicipio")),"coordGeobahiaMunicipio"));
		
		criteria.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));
		
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Municipio.class));
		
		return municipioDAO.buscarPorCriteria(criteria);
	}


	public Municipio buscarCoordGeoMunicipioPorEmpreendimento(Integer ideEmpreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class);
		criteria.createAlias("bairroCollection", "bairro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bairro.logradouroCollection", "logradouro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.enderecoCollection", "endereco", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("endereco.enderecoEmpreendimentoCollection", "enderecoEmpreendimentoCollection", JoinType.LEFT_OUTER_JOIN,
					Restrictions.eq("enderecoEmpreendimentoCollection.ideTipoEndereco.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()));		
		criteria.createAlias("enderecoEmpreendimentoCollection.ideEmpreendimento", "empreendimento", JoinType.LEFT_OUTER_JOIN);		
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.distinct(Projections.property("coordGeobahiaMunicipio")),"coordGeobahiaMunicipio"));
		
		criteria.add(Restrictions.eq("empreendimento.ideEmpreendimento", ideEmpreendimento));
		
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Municipio.class));
		
		return municipioDAO.buscarPorCriteria(criteria);
	}
	
	public Municipio buscarMunicipioByCodigoIbge(Double codigoIbge) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("coordGeobahiaMunicipio", codigoIbge);
		return municipioDAO.buscarEntidadePorNamedQuery("Municipio.findByCoordGeobahiaMunicipio", params);
	}
	
	public Municipio carregarMunicipioByLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica){
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM municipio "
				+ "WHERE cod_ibge_municipio IN ("
				+ "SELECT CAST (sp_get_municipio("+ localizacaoGeografica.getIdeLocalizacaoGeografica().toString() + ") as numeric))");
		Object[] obj = (Object[]) lQuery.getSingleResult();
		Municipio municipio = null;
		if(!Util.isNull(obj)){
			municipio = new Municipio();
			municipio.setIdeMunicipio((Integer) obj[0]);	
			municipio.setIdeEstado(new Estado((Integer) obj[1]));	
			municipio.setIdeTipoMunicipio(new TipoMunicipio((Integer) obj[2]));	
			municipio.setIdeSituacaoMunicipio(new SituacaoMunicipio((Integer) obj[3]));
			municipio.setNomMunicipio((String) obj[4]);
			BigDecimal bd = (BigDecimal) obj[5];
			municipio.setNumCep(bd != null ? Integer.parseInt(bd.toString()) : null);
			// obj[5] cod_ibge_municipio
			municipio.setIndEstadoEmergencia((Boolean) obj[7]);
			municipio.setIndBloqueioDQC((Boolean) obj[8]);
		}
		return municipio;
	}
	
}