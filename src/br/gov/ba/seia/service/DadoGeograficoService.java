package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.DadoGeograficoDAOImpl;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.util.Log4jUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DadoGeograficoService {

	@EJB
	private DadoGeograficoDAOImpl dadoGeograficoDAOImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarParaHistorico(LocalizacaoGeografica localizacaoGeografica) {
		try {
			localizacaoGeografica.setDadoGeograficoCollection(
					dadoGeograficoDAOImpl.listar(Restrictions.eq("ideLocalizacaoGeografica.ideLocalizacaoGeografica", localizacaoGeografica.getId()))
			);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	}
	
}
