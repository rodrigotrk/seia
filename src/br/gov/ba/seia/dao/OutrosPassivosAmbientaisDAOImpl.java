package br.gov.ba.seia.dao;


import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.OutrosPassivosAmbientais;
import br.gov.ba.seia.util.Util;

public class OutrosPassivosAmbientaisDAOImpl {

	@Inject
	IDAO<OutrosPassivosAmbientais> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public OutrosPassivosAmbientais filtrarById(OutrosPassivosAmbientais pOutrosPassivosAmbientais) {
		return dao.buscarEntidadePorExemplo(pOutrosPassivosAmbientais);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(OutrosPassivosAmbientais pOutrosPassivosAmbientais)  {
		dao.salvar(pOutrosPassivosAmbientais);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(OutrosPassivosAmbientais pOutrosPassivosAmbientais)  {
		dao.atualizar(pOutrosPassivosAmbientais);
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(OutrosPassivosAmbientais pOutrosPassivosAmbientais)  {
		String deleteSQL = "delete from outros_passivos_ambientais where ide_outros_passivos_ambientais = :ideOutrosPassivosAmbientais";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideOutrosPassivosAmbientais", pOutrosPassivosAmbientais.getIdeOutrosPassivosAmbientais());
		dao.executarNativeQuery(deleteSQL, params);	
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public OutrosPassivosAmbientais carregarTudo(OutrosPassivosAmbientais pOutrosPassivosAmbientais) {
		OutrosPassivosAmbientais outrosPassivosAmbientais = new OutrosPassivosAmbientais();
		DetachedCriteria criteria = DetachedCriteria.forClass(OutrosPassivosAmbientais.class, "outrosPassivosAmbientais");
		criteria.createAlias("ideDocumentoPrad", "documentoPrad", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideImovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cronogramaRecuperacao", "cronogramaRecuperacao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideOutrosPassivosAmbientais", pOutrosPassivosAmbientais.getIdeOutrosPassivosAmbientais()));
		outrosPassivosAmbientais = dao.buscarPorCriteria(criteria);
		if(!Util.isNullOuVazio(outrosPassivosAmbientais) && !Util.isNullOuVazio(outrosPassivosAmbientais.getCronogramaRecuperacao()))
			Hibernate.initialize(outrosPassivosAmbientais.getCronogramaRecuperacao().getCronogramaEtapaCollection());
		return outrosPassivosAmbientais;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public OutrosPassivosAmbientais buscaOutrosPassivosAmbientaisByImovelRural(ImovelRural imovelRural) {
		OutrosPassivosAmbientais outrosPassivosAmbientais = new OutrosPassivosAmbientais();
		DetachedCriteria criteria = DetachedCriteria.forClass(OutrosPassivosAmbientais.class, "outrosPassivosAmbientais");
		criteria.createAlias("ideDocumentoPrad", "documentoPrad", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cronogramaRecuperacao", "cronogramaRecuperacao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		criteria.addOrder(Order.asc("ideOutrosPassivosAmbientais"));
		outrosPassivosAmbientais = dao.buscarPorCriteria(criteria);
		if(!Util.isNullOuVazio(outrosPassivosAmbientais) && !Util.isNullOuVazio(outrosPassivosAmbientais.getCronogramaRecuperacao()))
			Hibernate.initialize(outrosPassivosAmbientais.getCronogramaRecuperacao().getCronogramaEtapaCollection());
		return outrosPassivosAmbientais;
	}

}
