package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.BarragemDAOImpl;
import br.gov.ba.seia.dao.CerhBarragemCaracterizacaoDAOImpl;
import br.gov.ba.seia.dao.CerhBarragemCaracterizacaoFinalidadeDAOImpl;
import br.gov.ba.seia.dao.TipoBarragemDAOImpl;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.util.Log4jUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhBarragemCaracterizacaoService {

	@EJB
	private CerhBarragemCaracterizacaoDAOImpl cerhBarragemCaracterizacaoDAOImpl;
	
	@EJB
	private CerhBarragemCaracterizacaoFinalidadeDAOImpl cerhBarragemCaracterizacaoFinalidadeDAOImpl;
	
	@EJB
	private BarragemDAOImpl barragemDAOImpl;
	
	@EJB
	private TipoBarragemDAOImpl  tipoBarragemDAOIml;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarParaHistorico(CerhLocalizacaoGeografica clg) {
		if(clg.getIdeCerhBarragemCaracterizacao()!=null){
			try {
				CerhBarragemCaracterizacao cbc =  cerhBarragemCaracterizacaoDAOImpl.buscar(clg.getIdeCerhBarragemCaracterizacao());	
				
				if(cbc!=null){
					cbc.setCerhBarragemCaracterizacaoFinalidadeCollection(cerhBarragemCaracterizacaoFinalidadeDAOImpl.listarParaHistorico(clg.getIdeCerhBarragemCaracterizacao().getId())
							
				);
					
				clg.setIdeCerhBarragemCaracterizacao(cbc);
					
			}
				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			
		}
		
	}

}
