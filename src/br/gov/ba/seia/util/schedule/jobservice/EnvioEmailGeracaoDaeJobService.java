package br.gov.ba.seia.util.schedule.jobservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.CerhNotificacaoEmailImpl;
import br.gov.ba.seia.entity.CerhNotificacaoEmail;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.facade.CerhNotificacaoEmailServiceFacade;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.util.EmailUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnvioEmailGeracaoDaeJobService {
	
	@EJB
	private CerhNotificacaoEmailImpl cerhNotificacaoEmailImpl;
	
	@EJB
	private CerhNotificacaoEmailServiceFacade cerhNotificacaoEmailServiceFacade;
	
	@EJB
	private EmailUtil emailUtil;
	
	@EJB
	private ParametroService parametroService;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhNotificacaoEmail> verificarEmailsPendentes() throws Exception{
    	Integer contador= 0;
    	Integer quantidadeEmails= 0;
		List<CerhNotificacaoEmail> listaEmails = new ArrayList<CerhNotificacaoEmail>();
		Parametro parametro = parametroService.obterPorEnum(ParametroEnum.NUMERO_MAXIMO_EMAIS_POR_CONSULTA);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhNotificacaoEmail.class);
		criteria
			.add(Restrictions.eq("indeEnviado", Boolean.FALSE));

		quantidadeEmails = cerhNotificacaoEmailServiceFacade.countEmailsPendentes();
		
		while (contador < quantidadeEmails){
			
			listaEmails.addAll((List<CerhNotificacaoEmail>) cerhNotificacaoEmailImpl.listarEmailsNaoEnviados(
					criteria,contador,Integer.parseInt(parametro.getDscValor())));
			
			contador += Integer.parseInt(parametro.getDscValor());
		}
		
		
			return listaEmails;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void enviarEmails() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhNotificacaoEmail.class);
		criteria
			.add(Restrictions.eq("indeEnviado", Boolean.FALSE));
    	Integer contador= 0;
		List<CerhNotificacaoEmail> listaEmails = new ArrayList<CerhNotificacaoEmail>();
		Parametro parametro = parametroService.obterPorEnum(ParametroEnum.NUMERO_MAXIMO_EMAIS_POR_CONSULTA);
		
		while (listaEmails.size() < 100){
			
			listaEmails.addAll( (List<CerhNotificacaoEmail>) cerhNotificacaoEmailImpl.listarEmailsNaoEnviados(
					criteria,contador,Integer.parseInt(parametro.getDscValor())));
			
		}
		
		if(listaEmails.size() > 0){
			System.out.println("começando a enviar emails...");
			for(CerhNotificacaoEmail cerhNotificacaoEmail: listaEmails){
				emailUtil.enviarEmail(cerhNotificacaoEmail.getDestinatario(), cerhNotificacaoEmail.getAssunto() , cerhNotificacaoEmail.getConteudo());
				cerhNotificacaoEmail.setIndeEnviado(Boolean.TRUE);
				cerhNotificacaoEmailServiceFacade.atualizarCerhNotificacaoEmail(cerhNotificacaoEmail);
			}
		}	
	}
		

}
