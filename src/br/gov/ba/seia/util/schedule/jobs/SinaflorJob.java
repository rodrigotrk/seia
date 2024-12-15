package br.gov.ba.seia.util.schedule.jobs;

import java.util.Date;

import org.apache.log4j.Level;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import br.gov.ba.seia.middleware.sinaflor.facade.SinaflorServiceFacade;
import br.gov.ba.seia.util.ExceptionUtil;
import br.gov.ba.seia.util.Log4jUtil;

public class SinaflorJob implements Job {

    public SinaflorJob() {}
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	try {
    		JobDataMap map = context.getJobDetail().getJobDataMap();
			SinaflorServiceFacade sinaflorServiceFacade = (SinaflorServiceFacade) map.get("sinaflorServiceFacade");
			
			sinaflorServiceFacade.jobSinaflor();
			
			JobKey jobKey = context.getJobDetail().getKey();
			
			System.out.println("Job says: " + jobKey + " executing at " + new Date());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			ExceptionUtil.enviarLog(e, ExceptionUtil.getLog(e), ExceptionUtil.getEmailsSEIA());
		}
    }
}
