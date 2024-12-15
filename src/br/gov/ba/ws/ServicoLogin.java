package br.gov.ba.ws;

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Level;
import org.json.JSONObject;

import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.service.UsuarioService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.ws.entity.TipoDispositivo;
import br.gov.ba.ws.entity.UsuarioDispositivo;
import br.gov.ba.ws.service.UsuarioDispositivoService;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;


@Path("sse/login")
public class ServicoLogin {
	
	@EJB
	UsuarioService usuarioService;
	
	@EJB
	UsuarioDispositivoService usuarioDispositivoService;
	
	/**Método para verificar o login do usuário é válido
	 * 
	 * @param login login do usuário
	 * @param md5 senha criptografada em MD5
	 * @return retorna um JSON do objeto usuário.
	 * @author jorge.ramos
	 */
	  @POST
	  @Path("getLogin")
	  @Produces("text/html; charset=UTF-8")
	  public String getLogin(@FormParam("login") String login, @FormParam("md5") String md5, @FormParam("chave") String chave) {
		  
		Hashtable<String, Object> parametros = new Hashtable<String, Object>();  
		  
		try {
				Usuario usuario =  usuarioService.buscarPorLogin(login);
				JSONSerializer serializer = new JSONSerializer(); 
				String user = serializer.exclude("*.class").include("idePerfil.idePerfil").transform(new DateTransformer("dd/MM/yyyy"), "dtcCriacao").serialize(usuario);
				if (!Util.isNullOuVazio(usuario)) {
					if(usuario.getDscSenha().equals(md5)){
						parametros.put("USUARIO", new JSONObject(user));
					} else {
						parametros.put("ERRO", "001");
						parametros.put("MSGERRO", "Usuário e/ou senha incorretos!");
					}
				}else{
					parametros.put("ERRO", "002");
					parametros.put("MSGERRO", "Usuário incorreto ou não encontrado na base. Cadastre-se no sistema SEIA: www.sistema.seia.ba.gov.br");
				}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametros);
		return objJson.toString();
	  }
	  
	  
	  /**
	   * Método para inserir o dispositivo do usuário na tabela usuario_dispositivo
	   * 
	   * @param ideUsuario ide do usuário.
	   * @param codDispositivo código identificador do dispositivo
	   * @param tipo inteiro para indicar o tipo do dispositivo 1-Android 2-IOS
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("inserirDispositivo")
	  @Produces("text/html; charset=UTF-8")
	  public String inserirDispositivo(@FormParam("ideUsuario") Integer ideUsuario, @FormParam("codDispositivo") String codDispositivo, @FormParam("tipo") Integer tipo) {
		  Usuario usuario = new Usuario(ideUsuario);
		  TipoDispositivo tipoDispositivo = new TipoDispositivo(tipo);
		  UsuarioDispositivo usuarioDispositivo = new UsuarioDispositivo(usuario, tipoDispositivo, codDispositivo) ;
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>();  
		  try {
			usuarioDispositivoService.excluirUsuarioDispositivobyCodDispositivo(usuarioDispositivo);
			usuarioDispositivoService.salvarUsuarioDispositivo(usuarioDispositivo);
			parametros.put("ERRO", "000");
			parametros.put("MSGERRO", "Incluído com sucesso OK");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		  JSONObject objJson = new JSONObject(parametros);
		 return objJson.toString();
	  }
	  
	  /**Método para excluir o registro da tabela usuario_dispositivo
	   * 
	   * @param ideUsuario 
	   * @param codDispositivo
	   * @return
	   * @author jorge.ramos
	   */
	  @POST
	  @Path("excluirDispositivo")
	  @Produces("text/html; charset=UTF-8")
	  public String excluirDispositivo(@FormParam("ideUsuario") Integer ideUsuario, @FormParam("codDispositivo") String codDispositivo) {
		  Usuario usuario = new Usuario(ideUsuario);
		  UsuarioDispositivo usuarioDispositivo = new UsuarioDispositivo(usuario, null, codDispositivo) ;
		  Hashtable<String, Object> parametros = new Hashtable<String, Object>();  
		  try {
			usuarioDispositivoService.excluirUsuarioDispositivo(usuarioDispositivo);
			parametros.put("ERRO", "000");
			parametros.put("MSGERRO", "Excluído com sucesso.");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		  JSONObject objJson = new JSONObject(parametros);
		  return objJson.toString();
	  }
		  
	
}
