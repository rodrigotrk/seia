package br.gov.ba.seia.util.schedule.jobs;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import br.gov.ba.seia.entity.CerhNotificacaoEmail;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.util.ExceptionUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.schedule.jobservice.EnvioEmailGeracaoDaeJobService;

/**
 * 
 * @author rodrigo.santos
 *
 */

public class VerificarEnvioEmailDaeJob implements Job {
	
	public static Scheduler agendador;
	

    public VerificarEnvioEmailDaeJob() {
    	
    }
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			
			JobDataMap map = context.getJobDetail().getJobDataMap();

			EnvioEmailGeracaoDaeJobService verificarEnvioEmailDae = (EnvioEmailGeracaoDaeJobService) map.get("envioEmailGeracaoDaeJobService");
			JobKey jobKey = context.getJobDetail().getKey();
			
			System.out.println("Job says: " + jobKey + " executing at " + new Date());
			
			List<CerhNotificacaoEmail> listaEmails = new ArrayList<CerhNotificacaoEmail>();
			listaEmails = (List<CerhNotificacaoEmail>) verificarEnvioEmailDae.verificarEmailsPendentes();
			SchedulerFactory sf = new StdSchedulerFactory();
			if(listaEmails.size() > 0){
				
				//verificar se o job existe
				if(!sf.getScheduler().checkExists(JobKey.jobKey("EnvioEmailJob","grupo4"))) {
					agendador = sf.getScheduler();
					jobEnvioEmailNotificacaoDae(context);
					agendador.start();
				}
			}
			else{
				sf.getScheduler().deleteJob(JobKey.jobKey("EnvioEmailJob","grupo4"));
			}
			
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			ExceptionUtil.enviarLog(e, ExceptionUtil.getLog(e), ExceptionUtil.getEmailsSEIA());
		}    	
    }
    
	private void jobEnvioEmailNotificacaoDae(JobExecutionContext context) throws Exception, SchedulerException {
		System.out.println("chegou no Job de envio...");
		
		JobDataMap map = context.getJobDetail().getJobDataMap();
		
		ParametroService parametroService = (ParametroService) map.get("parametroService");
		EnvioEmailGeracaoDaeJobService verificarEnvioEmailDae = (EnvioEmailGeracaoDaeJobService) map.get("envioEmailGeracaoDaeJobService");
		
		Parametro p = parametroService.obterPorEnum(ParametroEnum.HORARIO_ROTINA_VERIFICAR_ENVIO_EMAILS);
		
		JobDetail jobEmail = newJob(EnvioEmailGeracaoDaeJob.class).withIdentity("EnvioEmailJob", "grupo4").build();
		jobEmail.getJobDataMap().put("envioEmailGeracaoDaeJobService", verificarEnvioEmailDae);

		CronTrigger triggerEmail = newTrigger().withIdentity("trigger4", "grupo4").withSchedule(cronSchedule(p.getDscValor())).build();
		
		Date dataEmail = agendador.scheduleJob(jobEmail, triggerEmail);
		System.out.println(jobEmail.getKey() + " foi agendado para rodar em : " + dataEmail + " e repetir baseado na expressão: " + triggerEmail.getCronExpression());
	}
}
