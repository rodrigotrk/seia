package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.CerhLocalizacaoGeograficaDAOImpl;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhLocalizacaoGeograficaService {

	@EJB
	private CerhLocalizacaoGeograficaDAOImpl cerhLocalizacaoGeograficaDAOImpl;
	
	@EJB
	private DadoGeograficoService dadoGeograficoService;
	
	@EJB
	private CerhBarragemCaracterizacaoService cerhBarragemCaracterizacaoService;
	
	@EJB
	private CerhCaptacaoCaracterizacaoService cerhCaptacaoCaracterizacaoService;
	
	@EJB 
	private CerhLancamentoEfluenteCaracterizacaoService cerhLancamentoEfluenteCaracterizacaoService;
	
	@EJB 
	private CerhIntervencaoCaracterizacaoService cerhIntervencaoCaracterizacaoService;
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CerhLocalizacaoGeografica> listarParaHistorico(Cerh c, TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {

		try {
			List<CerhLocalizacaoGeografica> listarParaHistorico = cerhLocalizacaoGeograficaDAOImpl.listarParaHistorico(c, tipoUsoRecursoHidricoEnum);
				for (CerhLocalizacaoGeografica clg : listarParaHistorico) {
					dadoGeograficoService.carregarParaHistorico(clg.getIdeLocalizacaoGeografica());

					if(tipoUsoRecursoHidricoEnum  == TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE){
						cerhLancamentoEfluenteCaracterizacaoService.carregarParaHistorico(clg);
					}	
					
					else if(tipoUsoRecursoHidricoEnum  == TipoUsoRecursoHidricoEnum.BARRAGEM){
						cerhBarragemCaracterizacaoService.carregarParaHistorico(clg);
					}	

					else if(tipoUsoRecursoHidricoEnum  == TipoUsoRecursoHidricoEnum.INTERVENCAO){
						cerhIntervencaoCaracterizacaoService.carregarParaHistorico(clg);
					}		
					
					else if(tipoUsoRecursoHidricoEnum  == TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL){
						cerhCaptacaoCaracterizacaoService.carregarParaHistorico(clg);
					}
					
					else if(tipoUsoRecursoHidricoEnum  == TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA){
						cerhCaptacaoCaracterizacaoService.carregarParaHistorico(clg);
					}
				}
				
			return listarParaHistorico;

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CerhLocalizacaoGeografica cerhLocalizacaoGeografica)  {
		try {
			cerhLocalizacaoGeograficaDAOImpl.excluir(cerhLocalizacaoGeografica);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
