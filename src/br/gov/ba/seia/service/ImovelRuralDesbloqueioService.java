package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralDesbloqueio;
import br.gov.ba.seia.entity.ImovelRuralMudancaStatusJustificativa;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralDesbloqueioService {

	@Inject
	private IDAO<ImovelRuralDesbloqueio> imovelRuralDesbloqueioDAO;
	@Inject
	private ImovelRuralService imovelRuralService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ImovelRuralDesbloqueio imovelRuralDesbloqueio) throws Exception {
		try {
			
			Log4jUtil.log(this.getClass().getSimpleName(), Level.INFO, "Salvando Mudança Status Justificativa Imovel Rural- 631");
			imovelRuralDesbloqueioDAO.salvarOuAtualizar(imovelRuralDesbloqueio);
			return;
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw new Exception(e);
		}		

	}

	public Integer getImovelRural(ImovelRural ideImovelRural) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ide_imovel_rural_desbloqueio ");
		sql.append("from imovel_rural_desbloqueio ");
		sql.append("where ide_imovel_rural = :ideImovelRural ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString());

		lQuery.setParameter("ideImovelRural", ideImovelRural.getIdeImovelRural());

		@SuppressWarnings("unchecked")
		List<Integer> resultList = lQuery.getResultList();
		if (!Util.isNullOuVazio(resultList)) {
			if(resultList.get(0).intValue() != 0) {
				return resultList.get(0).intValue();
			}
		}
		return null;
	}
	
}
