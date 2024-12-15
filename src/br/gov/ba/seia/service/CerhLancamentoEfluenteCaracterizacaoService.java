package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.CerhLancamentoCaracterizacaoOrigemDAOImpl;
import br.gov.ba.seia.dao.CerhLancamentoEfluenteCaracterizacaoDAOImpl;
import br.gov.ba.seia.dao.CerhLancamentoEfluenteSazonalidadeDAOImpl;
import br.gov.ba.seia.dao.CerhTratamentoEfluentesDAOImpl;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteCaracterizacao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.util.Log4jUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhLancamentoEfluenteCaracterizacaoService {

	@EJB
	private CerhLancamentoEfluenteCaracterizacaoDAOImpl cerhLancamentoEfluenteCaracterizacaoDAOImpl;
	
	@EJB
	private CerhLancamentoEfluenteSazonalidadeDAOImpl cerhLancamentoEfluenteSazonalidadeDAOImpl;
	
	@EJB 
	private CerhLancamentoCaracterizacaoOrigemDAOImpl cerhLancamentoCaracterizacaoOrigemDAOImpl;
	
	@EJB 
	private CerhTratamentoEfluentesDAOImpl cerhTratamentoEfluentesDAOImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarParaHistorico(CerhLocalizacaoGeografica clg) {
	
		if(clg.getIdeCerhLancamentoEfluenteCaracterizacao()!=null){
			CerhLancamentoEfluenteCaracterizacao clc;
			try {
				clc = cerhLancamentoEfluenteCaracterizacaoDAOImpl.carregarParaHistorico(clg.getIdeCerhLancamentoEfluenteCaracterizacao());

				if(clc.getIdeCerhTratamentoEfluente() != null){
					clc.setIdeCerhTratamentoEfluente(cerhTratamentoEfluentesDAOImpl.buscar(
							Restrictions.eq("ideCerhTratamentoEfluente", clc.getIdeCerhTratamentoEfluente().getId())));
				}
				
				clc.setCerhLancamentoEfluenteSazonalidadeCollection(
					cerhLancamentoEfluenteSazonalidadeDAOImpl
						.listar(Restrictions.eq("ideCerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao", clc.getId()))
				);
				
				clc.setCerhLancamentoCaracterizacaoOrigemCollection(cerhLancamentoCaracterizacaoOrigemDAOImpl.listarParaHistorico(clc));
				
				clg.setIdeCerhLancamentoEfluenteCaracterizacao(clc);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}
	
}
