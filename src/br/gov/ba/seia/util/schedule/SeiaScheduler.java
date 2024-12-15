package br.gov.ba.seia.util.schedule;  

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Level;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.middleware.sinaflor.facade.SinaflorServiceFacade;
import br.gov.ba.seia.service.AlertaService;
import br.gov.ba.seia.service.AlterarSenhaService;
import br.gov.ba.seia.service.NotificacaoServiceFacade;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.schedule.jobs.ComunicacaoJob;
import br.gov.ba.seia.util.schedule.jobs.InativacaoUsuarioJob;
import br.gov.ba.seia.util.schedule.jobs.NotificacaoJob;
import br.gov.ba.seia.util.schedule.jobs.SinaflorJob;
import br.gov.ba.seia.util.schedule.jobs.VerificarEnvioEmailDaeJob;
import br.gov.ba.seia.util.schedule.jobservice.ComunicacaoJobService;
import br.gov.ba.seia.util.schedule.jobservice.EnvioEmailGeracaoDaeJobService;
import br.gov.ba.seia.util.schedule.jobservice.NotificacaoJobService;

public class SeiaScheduler implements ServletContextListener {

	public static Scheduler agendador;
	
	@EJB
	private NotificacaoServiceFacade notificacaoService;
	
	@EJB
	private NotificacaoJobService notificacaoJobService;
	
	@EJB
	private AlertaService alertaService;
	
	@EJB
	private AlterarSenhaService alterarSenhaService;
	
	@EJB
	private ParametroService parametroService;
	
	@EJB
	private SinaflorServiceFacade sinaflorServiceFacade;

	@EJB
	private EnvioEmailGeracaoDaeJobService envioEmailGeracaoDaeJobService;
	
	@EJB
	private ComunicacaoJobService comunicacaoJobService;

	private Object put;

	

	public void contextDestroyed(ServletContextEvent evt) {
		try {
			SeiaScheduler.agendador.shutdown(true);
		} 
		catch (Exception e) {
			Log4jUtil.log(SeiaScheduler.class.getName(), Level.ERROR,"Erro ao tentar reiniciar as tarefas");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void contextInitialized(ServletContextEvent evt) {
		
		/**
		
		30 em 30 seg. 0/30 * * * * ?
		60 em 60 seg. 0/60 * * * * ?
		3h da madrugada. 0 0 3 * * ?
		0 0/30 * * * ?"          = a cada 30 minutos
		0 0/59 8-18 ? * MON-FRI" = a cada 59 minutos entre 08-18 hrs de segunda a sexta
		0 15 10 * * ? = às 10h15 todos os dias
		
		Use o de baixo para testes
		CronTrigger trigger = newTrigger().withIdentity("trigger1", "grupo1").withSchedule(cronSchedule("0/60 * * * * ?")).build()
		
		*/
		
		try{
			Log4jUtil.log(SeiaScheduler.class.getName(), Level.INFO,"------- Inicializando Agendador -------------------");

			SchedulerFactory sf = new StdSchedulerFactory();
			agendador = sf.getScheduler();
			
			Log4jUtil.log(SeiaScheduler.class.getName(), Level.INFO,"------- Inicialização Completa --------");
			Log4jUtil.log(SeiaScheduler.class.getName(), Level.INFO,"----------- Agendando Jobs ------------");

			jobNotificacao();
			jobInativacaoUsuario();
			jobSinaflor();
			/**jobVerificarEmailsPendendesDae();*/
			//jobComunicacao();
			
			Log4jUtil.log(SeiaScheduler.class.getName(), Level.INFO,"-------- Startando Agendador ----------");
			agendador.start();
			Log4jUtil.log(SeiaScheduler.class.getName(), Level.INFO,"-------- Agendador Concluído ----------");
			


		}
		catch(Exception e){
			Log4jUtil.log(SeiaScheduler.class.getName(), Level.ERROR,"\n\nErro ao tentar iniciar SeiaScheduler.\n\n");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void jobComunicacao() throws  SchedulerException{
		JobDetail jobComunicacao = newJob(ComunicacaoJob.class).withIdentity("comunicaoJob", "grupo4").build();
		jobComunicacao.getJobDataMap().put("comunicacaoJobService", comunicacaoJobService);
		Parametro param = parametroService.obterPorEnum(ParametroEnum.HORARIO_ROTINA_ENVIO_COMUNICACAO);
		CronTrigger triggerComunicacao = newTrigger().withIdentity("triggerComunicacao", "grupo4").withSchedule(cronSchedule(param.getDscValor())).build();
		Date dataNot = agendador.scheduleJob(jobComunicacao, triggerComunicacao);
		extracted(jobComunicacao, triggerComunicacao, dataNot);
	}

	private void extracted(JobDetail jobComunicacao, CronTrigger triggerComunicacao, Date dataNot) {
		Log4jUtil.log(SeiaScheduler.class.getName(), Level.INFO,jobComunicacao.getKey() + " foi agendado para rodar em : " + dataNot + " e repetir baseado na expressão: " + triggerComunicacao.getCronExpression());
	}

	private void jobNotificacao() throws SchedulerException {
		
		JobDetail jobNot = newJob(NotificacaoJob.class).withIdentity("notificacaoJob", "grupo1").build();
		jobNot.getJobDataMap().put("notificacaoJobService", notificacaoJobService);
		
		Parametro p = parametroService.obterPorEnum(ParametroEnum.HORARIO_ROTINA_NOTIFICACAO_PRAZO);
		
		CronTrigger triggerNot = newTrigger().withIdentity("trigger1", "grupo1").withSchedule(cronSchedule(p.getDscValor())).build();
		
		Date dataNot = agendador.scheduleJob(jobNot, triggerNot);
		extracted(jobNot, triggerNot, dataNot);
	}
	
	private void jobInativacaoUsuario() throws SchedulerException {
		
		JobDetail jobUser = newJob(InativacaoUsuarioJob.class).withIdentity("inativacaoUsuarioJob", "grupo2").build();
		jobUser.getJobDataMap().put("alterarSenhaService", alterarSenhaService);
		
		Parametro p = parametroService.obterPorEnum(ParametroEnum.HORARIO_ROTINA_INATIVACAO_USUARIO);
		
		CronTrigger triggerUser = newTrigger().withIdentity("trigger2", "grupo2").withSchedule(cronSchedule(p.getDscValor())).build();
		
		Date dataUser = agendador.scheduleJob(jobUser, triggerUser);
		extracted(jobUser, triggerUser, dataUser);
	}
	
	private void jobSinaflor() throws SchedulerException {
		
		JobDetail jobSinaflor = newJob(SinaflorJob.class).withIdentity("sinaflorJob", "grupo3").build();
		jobSinaflor.getJobDataMap().put("sinaflorServiceFacade", sinaflorServiceFacade);
		
		Parametro p = parametroService.obterPorEnum(ParametroEnum.HORARIO_ROTINA_SINAFLOR);
		
		CronTrigger triggerUser = newTrigger().withIdentity("trigger3", "grupo3").withSchedule(cronSchedule(p.getDscValor())).build();
		
		Date dataUser = agendador.scheduleJob(jobSinaflor, triggerUser);
		extracted(jobSinaflor, triggerUser, dataUser);
	}

	private void jobVerificarEmailsPendendesDae() throws Exception, SchedulerException {
		
		JobDetail jobEmail = newJob(VerificarEnvioEmailDaeJob.class).withIdentity("VerificarEnvioEmailDaeJob", "grupo3").build();
		jobEmail.getJobDataMap().put("envioEmailGeracaoDaeJobService", envioEmailGeracaoDaeJobService);
		
		Parametro p = parametroService.obterPorEnum(ParametroEnum.HORARIO_ROTINA_VERIFICAR_EMAILS_PENDENTES);
		
		jobEmail.getJobDataMap().put("parametroService", parametroService);
		
		CronTrigger triggerEmail = newTrigger().withIdentity("trigger3", "grupo3").withSchedule(cronSchedule(p.getDscValor())).build();
		
		Date dataEmail = agendador.scheduleJob(jobEmail, triggerEmail);
		extracted(jobEmail, triggerEmail, dataEmail);
	}
}