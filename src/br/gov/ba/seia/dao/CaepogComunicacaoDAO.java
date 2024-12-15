package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.CaepogComunicacao;

public class CaepogComunicacaoDAO {

	@Inject
	IDAO<CaepogComunicacao> caepogComunicacaoIDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogComunicacao> listarComunicacaoPorCaepog(Caepog caepog) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogComunicacao.class)
			.createAlias("ideCaepog", "ideCaepog", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideCaepog.ideCaepog", caepog.getIdeCaepog()))
			.add(Restrictions.eq("indEnviado", true));
		
		return caepogComunicacaoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CaepogComunicacao caepogComunicacao) {
		caepogComunicacaoIDAO.salvarOuAtualizar(caepogComunicacao);		
	}
}