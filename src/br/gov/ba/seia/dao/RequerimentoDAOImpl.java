package br.gov.ba.seia.dao;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Requerimento;

public class RequerimentoDAOImpl extends AbstractDAO<Requerimento>{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private IDAO<Requerimento> dao;
	
	@Override
	protected IDAO<Requerimento> getDAO() {
		return dao;
	}


	public Requerimento getMaiorNumeroRequerimentoByOrgaoByAnoAtual(Requerimento requerimento,String tipoRequerimento) {
		try {
			int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
			StringBuilder lSql = new StringBuilder();
			lSql.append("SELECT"
					+ "        substring(r.num_requerimento,1,9)||rpad(SPLIT_PART(substr(r.num_requerimento,10),'/',1),6,'0')||substring(num_requerimento FROM strpos(num_requerimento, '/')) num_requerimento  "
					+ "     FROM"
					+ "         requerimento r "
					+ "     WHERE"
					+ "         r.num_requerimento IS NOT NULL"
					+ "			AND r.num_requerimento ilike '%"+tipoRequerimento+"'"
					+ "			AND substring(num_requerimento, 1, 4) ='"+anoAtual+"'"
				    + "		order by"
				    + "			rpad(SPLIT_PART(substr(r.num_requerimento,10),'/',1),6,'0') desc limit 1");
	
			EntityManager lEntityManager = DAOFactory.getEntityManager();
	
			Query lQuery = lEntityManager.createNativeQuery(lSql.toString());
	

			Object ultimoNum = lQuery.getSingleResult();
	
			requerimento.setNumRequerimento((String) ultimoNum);
		}catch (NoResultException e) {
			requerimento.setNumRequerimento(null);
		}
		
		return requerimento;
	}
}
