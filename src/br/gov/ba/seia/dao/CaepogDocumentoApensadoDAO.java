package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.CaepogDocumento;
import br.gov.ba.seia.entity.CaepogDocumentoApensado;

public class CaepogDocumentoApensadoDAO {

	@Inject
	IDAO<CaepogDocumentoApensado> caepogDocumentoApensadoIDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CaepogDocumentoApensado buscarCaepogDocumentoApensadoByTipoByCaepog(Caepog caepog, CaepogDocumento caepogDocumento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogDocumentoApensado.class)
				.createAlias("idePessoaFisicaEnvio", "pfEnvio", JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaFisicaValidacao", "pfValidacao", JoinType.LEFT_OUTER_JOIN)
				.createAlias("caepog", "caepog", JoinType.LEFT_OUTER_JOIN)
				.createAlias("caepogDocumento", "caepogDocumento", JoinType.LEFT_OUTER_JOIN)
				
				.add(Restrictions.eq("caepog.ideCaepog", caepog.getIdeCaepog()))
				.add(Restrictions.eq("caepogDocumento.ideCaepogDocumento", caepogDocumento.getIdeCaepogDocumento()));
		
		return caepogDocumentoApensadoIDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(CaepogDocumentoApensado caepogDocumentoApensado){
		caepogDocumentoApensadoIDAO.salvarOuAtualizar(caepogDocumentoApensado);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorCaepog(Caepog c)  {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideCaepog", c.getIdeCaepog());
		
		StringBuilder sql = new StringBuilder();
		sql.append("delete from CaepogDocumentoApensado cda where cda.caepog.ideCaepog = :ideCaepog");
		caepogDocumentoApensadoIDAO.executarQuery(sql.toString(), params);
	}
}
