package br.gov.ba.seia.util.schedule.jobs;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.service.AlterarSenhaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public class InativacaoUsuarioJob implements Job {

    public InativacaoUsuarioJob() {}

    public void execute(JobExecutionContext context) throws JobExecutionException {
    	
    	JobDataMap map = context.getJobDetail().getJobDataMap();
    	
    	AlterarSenhaService alterarSenhaService = (AlterarSenhaService) map.get("alterarSenhaService");
    	
    	try {
    		List<Usuario> list = alterarSenhaService.listarUsuariosParaInativacao();
    		
    		for (Usuario user : list) {
    			alterarSenhaService.inativarUsuario(user.getIdePessoaFisica());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
    	
    	JobKey jobKey = context.getJobDetail().getKey();
        System.out.println("Job says: " + jobKey + " executing at " + new Date());
    }
}