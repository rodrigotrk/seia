package br.gov.ba.seia.controller;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.faces.ViewScoped;


@Named("userWizard")
@ViewScoped
public class UserWizard {
	
	
		@PostConstruct
		public void init() {
			user.setPessoaFisica(new PessoaFisica());
			user.getPessoaFisica().setPessoa(new Pessoa());
		}
		
	    private Usuario user = new Usuario();  
	      
	    private boolean skip;  
	    
	    private boolean teste; 
	      
	    private static Logger logger = Logger.getLogger(UserWizard.class.getName());  
	  
	    public Usuario getUser() {  
	        return user;  
	    }  
	  
	    public void setUser(Usuario user) {  
	        this.user = user;  
	    }  
	      
	    public void save(ActionEvent actionEvent) {  
	        //Persist user  
	          
	        FacesMessage msg = new FacesMessage("Successful", "Welcome :" + user.getPessoaFisica().getNomPessoa());  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	    }  
	      
	    public boolean isSkip() {  
	        return skip;  
	    }  
	  
	    public void setSkip(boolean skip) {  
	        this.skip = skip;  
	    }  
	      
	    public String onFlowProcess(FlowEvent event) {  
	        logger.info("Current wizard step:" + event.getOldStep());  
	        logger.info("Next step:" + event.getNewStep());  
	          
	        if(skip) {  
	            skip = false;   //reset in case user goes back  
	            return "confirm";  
	        }  
	        else {  
	            return event.getNewStep();  
	        }  
	    }

		public boolean isTeste() {
			return teste;
		}

		public void setTeste(boolean teste) {
			this.teste = teste;
		}  

}
