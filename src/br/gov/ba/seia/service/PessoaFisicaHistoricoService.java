package br.gov.ba.seia.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.PessoaFisicaHistoricoDAOImpl;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaFisicaHistorico;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaHistorico;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.auditoria.FiltroAuditoria;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaFisicaHistoricoService extends PessoaService {

	@EJB
	private PessoaFisicaHistoricoDAOImpl pessoaFisicaHistoricoDAOImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvarHistoricoPessoaFisica(PessoaFisicaHistorico pessoaFisicaHistorico) {
		pessoaFisicaHistoricoDAOImpl.salvar(pessoaFisicaHistorico);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisicaHistorico selecionarPorIdeProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica) {
		return pessoaFisicaHistoricoDAOImpl.selecionarPorIdeProcuradorPessoaFisica(procuradorPessoaFisica);
	}
	
	public PessoaFisicaHistorico buscarHistoricoAnterior(PessoaFisica pessoaFisica)  {
		return pessoaFisicaHistoricoDAOImpl.buscarHistoricoAnterior(pessoaFisica);
		
	}
	
	public StreamedContent imprimirHistorico(Integer idePessoaFisica, Date dataInicioSet, Date dataFimSet)
			throws Exception {
		final Map<String, Object> lParametros = new HashMap<String, Object>();
		
		try {
			
			lParametros.put("idePessoaFisica", idePessoaFisica);
			
			Calendar cal = Calendar.getInstance();
			
	        if (dataInicioSet == null) {
	            cal.set(2012, Calendar.JANUARY, 1, 0, 0, 0);
	            cal.set(Calendar.MILLISECOND, 0);
	        } else {
	            cal.setTime(dataInicioSet);
	            cal.set(Calendar.HOUR_OF_DAY, 0);
	            cal.set(Calendar.MINUTE, 0);
	            cal.set(Calendar.SECOND, 0);
	            cal.set(Calendar.MILLISECOND, 0);
	        }
	        Timestamp dataInicio = new Timestamp(cal.getTimeInMillis());

	        if (dataFimSet == null) {
	            cal.setTime(new Date());
	        } else {
	            cal.setTime(dataFimSet);
	        }
	        cal.set(Calendar.HOUR_OF_DAY, 23);
	        cal.set(Calendar.MINUTE, 59);
	        cal.set(Calendar.SECOND, 59);
	        cal.set(Calendar.MILLISECOND, 999);
	        Timestamp dataFim = new Timestamp(cal.getTimeInMillis());
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        
	        lParametros.put("dataInicio", new Timestamp(sdf.parse(sdf.format(dataInicio)).getTime()));
	        lParametros.put("dataFim", new Timestamp(sdf.parse(sdf.format(dataFim)).getTime()));
			
			return new RelatorioUtil("relatorio_historico_pessoa_fisica.jasper", lParametros, "logoInemaRelatorio.png",
					"sema_vertical.png").gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);

		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return null;
		}
	}
	

}