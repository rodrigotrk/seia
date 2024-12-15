package br.gov.ba.seia.util.schedule.jobs;

import java.util.Date;

import org.apache.log4j.Level;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import br.gov.ba.seia.util.ExceptionUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.schedule.jobservice.NotificacaoJobService;

/**
 * 
 * @author carlos.galvao
 *
 */

public class NotificacaoJob implements Job {
	

    public NotificacaoJob() {
    	
    }
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			
			JobDataMap map = context.getJobDetail().getJobDataMap();
			NotificacaoJobService notificacaoJobService = (NotificacaoJobService) map.get("notificacaoJobService");
			
			notificacaoJobService.processaNotificacoesNaoRespondidas();
			
			JobKey jobKey = context.getJobDetail().getKey();
			
			System.out.println("Job says: " + jobKey + " executing at " + new Date());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			ExceptionUtil.enviarLog(e, ExceptionUtil.getLog(e), ExceptionUtil.getEmailsSEIA());
		}    	
    }
}
