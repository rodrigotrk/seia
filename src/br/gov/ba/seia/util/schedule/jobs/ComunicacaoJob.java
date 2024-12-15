package br.gov.ba.seia.util.schedule.jobs;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.schedule.jobservice.ComunicacaoJobService;

public class ComunicacaoJob implements Job{


	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
		JobDataMap map = context.getJobDetail().getJobDataMap();
		ComunicacaoJobService comunicacaoJobService = (ComunicacaoJobService) map.get("comunicacaoJobService");
		comunicacaoJobService.processaComunicacoesDoDia();
		JobKey jobKey = context.getJobDetail().getKey();
		System.out.println("Job says: " + jobKey + " executing at " + new Date());
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

}
