package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.CerhProcessoDAOImpl;
import br.gov.ba.seia.dao.CerhSituacaoRegularizacaoDAOImpl;
import br.gov.ba.seia.dao.CerhTipoAtoDispensaDAOImpl;
import br.gov.ba.seia.dao.CerhTipoAutorizacaoOutorgadoDAOImpl;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhProcessoService {

	@EJB
	private CerhProcessoDAOImpl cerhProcessoListagemDAOImpl;
	
	@EJB
	private CerhSituacaoRegularizacaoDAOImpl cerhSituacaoRegularizacaoDAOImpl;
	
	@EJB
	private CerhTipoAutorizacaoOutorgadoDAOImpl cerhTipoAutorizacaoOutorgadoDAOImpl;
	
	@EJB
	private CerhTipoAtoDispensaDAOImpl cerhTipoAtoDispensaDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhProcesso buscarCerhProcesso(String chave, Cerh cerh) {
		try{
			CerhProcesso cerhProcesso = cerhProcessoListagemDAOImpl.buscar(Restrictions.and(Restrictions.eq("numProcesso", chave), Restrictions.eq("ideCerh.ideCerh", cerh.getId())));
			if(cerhProcesso != null){
				cerhProcesso.setIdeCerhSituacaoRegularizacao(cerhSituacaoRegularizacaoDAOImpl.buscar(cerhProcesso.getIdeCerhSituacaoRegularizacao()));
				cerhProcesso.setIdeCerhTipoAutorizacaoOutorgado(cerhTipoAutorizacaoOutorgadoDAOImpl.buscar(cerhProcesso.getIdeCerhTipoAutorizacaoOutorgado()));
				cerhProcesso.setIdeCerhTipoAtoDispensa(cerhTipoAtoDispensaDAOImpl.buscar(cerhProcesso.getIdeCerhTipoAtoDispensa()));
			}
			return cerhProcesso;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
