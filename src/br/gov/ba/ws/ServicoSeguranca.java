package br.gov.ba.ws;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.log4j.Level;

import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.security.SecurityService;
import br.gov.ba.seia.util.security.SecurityUser;

@Path("sse/seguranca")
public class ServicoSeguranca {
	
	@Inject
	private SecurityService securityService;
	
	
	/**
	 * Método criado para verificar se o perfil do usuário tem acesso a funcionalidade 
	 * @param idePerfil perfil do usuário logado
	 * @param funcionalidade Chave da funcionalidade que deseja verificar se o usuário tem acesso. (Mesma chave utilizada no sistema web) 
	 * @return
	 */
	  @POST
	  @Path("verificarAcesso")
	  public Boolean isAcesso(@FormParam("idePerfil") Integer idePerfil, @FormParam("funcionalidade") String funcionalidade) {
		  SecurityUser user = new SecurityUser(idePerfil);
		  try {
			 return securityService.canAccess(user,funcionalidade);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return null;
	  }
}
