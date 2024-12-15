package br.gov.ba.seia.util.security;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.Secao;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public class SecurityService {

	private static final String DATA_SOURCE = "java:/seiaDs";
	
	private static JdbcTemplate jdbc;

	private static SecurityService instance;

	private SecurityService() {

	}

	public static synchronized SecurityService getInstance() {
		if (instance == null) {
			instance = new SecurityService();
		}
		return instance;
	}

	static {
		try {
			jdbc = new JdbcTemplate((DataSource) InitialContext.doLookup(DATA_SOURCE));
		} catch (NamingException e) {
			Log4jUtil.log(SecurityService.class.getName(),Level.ERROR, e);
		}
	}

	public boolean canAccess(SecurityUser pUser, SecurityItem item) throws Exception {

		if (pUser == null) {
			return false;
		}

		String sql = item.getSql();
		// LOG.info(sql);

		List<SecurityItem> result = jdbc.query(sql, item, item.getParameters(pUser.getIdePerfil()));

		return (result != null && !result.isEmpty());
	}

	public boolean canAccessUsingCache(SecurityUser pUser, String key) throws Exception {
		
		if(!Util.isNull(pUser) && !Util.isNull(pUser.getUrls())){
			SecurityItem item = new SecurityItem(key);
			for (SecurityItem securityItem : pUser.getItens()) {
				if(securityItem.equals(item)){
					return true;
				}
			}
		}
		return false;
	}

	public boolean canAccessUrl(SecurityUser pUser, String pUrlPagina) throws Exception {

		if(!Util.isNull(pUser) && !Util.isNull(pUser.getUrls())){
			for (SecurityItem securityItem : pUser.getUrls()) {
				if(securityItem.getUrl().equals(pUrlPagina)){
					return true;
				}
			}
		}
		
		return false;

	}

	public List<SecurityItem> getUserItens(SecurityUser pUser) throws Exception {

		SecurityItem item = new SecurityItem();

		String sql = item.getSqlAllItens();
		// LOG.info(sql);
		return jdbc.query(sql, item, item.getParametersAllItens(pUser.getIdePerfil()));
	}

	public List<SecurityItem> getUserUrls(SecurityUser pUser) {

		SecurityItem item = new SecurityItem();
		
		String sql = "SELECT g.ide_acao,g.ide_funcionalidade,f.ide_secao,u.dsc_url FROM  rel_grupo_perfil_funcionalidade g "+
					"INNER JOIN funcionalidade f ON g.ide_funcionalidade = f.ide_funcionalidade "+
					"INNER JOIN funcionalidade_url u ON u.ide_funcionalidade = f.ide_funcionalidade "+
					"where g.ide_perfil = ? and ind_excluido = false  "+
					"UNION "+
					"select ide_acao, funcionalidade.ide_funcionalidade, ide_secao, dsc_url from funcionalidade_acao_pessoa_fisica "+ 
					"INNER JOIN  funcionalidade on funcionalidade_acao_pessoa_fisica.ide_funcionalidade=funcionalidade.ide_funcionalidade "+
					"INNER JOIN funcionalidade_url on funcionalidade.ide_funcionalidade=funcionalidade_url.ide_funcionalidade "+
					"where  funcionalidade_acao_pessoa_fisica.ide_pessoa_fisica = ?";
							
		return jdbc.query(sql, item, new Object[] { pUser.getIdePerfil(), pUser.getUsuario().getIdePessoaFisica()});
	}

	public static Usuario getUser() {
		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				SecurityUser user = (SecurityUser) principal;
				return user.getUsuario();
			}
		}
		return null;

	}

	public boolean verificarCadastroCompletoUsuario(Usuario usuario) {
		return (verificarEnderecoUsuario(usuario) && verificarDocumentoUsuario(usuario) && (verificarTelefoneUsuario(usuario)));
	}

	private boolean verificarEnderecoUsuario(Usuario usuario) {
		
		if(!Util.isNull(usuario.getIdePerfil())) {
			return true;
		}else {
		
		String query = "select ep.ide_endereco_pessoa from endereco_pessoa ep  "
				+ "inner join endereco e on e.ide_endereco = ep.ide_endereco "
				+ "where ep.ide_pessoa = ? and e.ind_excluido = false limit 1";
		try {
			Integer idEnderecoPessoa = jdbc.queryForInt(query, usuario.getIdePessoaFisica());
			if (Util.isNull(idEnderecoPessoa)) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	  }
	}		

	private boolean verificarDocumentoUsuario(Usuario usuario) {

		if(!Util.isNull(usuario.getIdePerfil())) {
			return true;
		}else {
		
		String query = "select ide_documento_identificacao from documento_identificacao where ide_pessoa = ? and ind_excluido = false limit 1";
		try {
			Integer idDocumento = jdbc.queryForInt(query, usuario.getIdePessoaFisica());
			if (Util.isNull(idDocumento)) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	  }
	}

	private boolean verificarTelefoneUsuario(Usuario usuario) {

		if(!Util.isNull(usuario.getIdePerfil())) {
			return true;
		}else {
		
		String query = "select tp.ide_telefone from telefone_pessoa tp "
				+ "inner join telefone t on tp.ide_telefone = t.ide_telefone "
				+ "where t.ind_excluido = false and ide_pessoa = ? limit 1";
		try {
			Integer idTelefone = jdbc.queryForInt(query, usuario.getIdePessoaFisica());
			if (Util.isNull(idTelefone)) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	  }
	}
	
	/**
	 * A {@link String} <i>key</i> deve conter os valores de {@link Secao}, {@link Funcionalidade} e {@link Acao} separados por '<b>.</b>'
	 * @param key
	 */
	public boolean canAccessUsingCache(String key) {
		if (SecurityContextHolder.getContext() != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				SecurityUser user = (SecurityUser) principal;
				try {
					return canAccessUsingCache(user, key);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		}
		return false;

	}
	
	public boolean canAccess(SecurityUser pUser, String itemAcesso) throws Exception {

		if (pUser == null) {
			return false;
		}
		SecurityItem item = new SecurityItem(itemAcesso);
		String sql = item.getSqlCount();
		// LOG.info(sql);

		Integer result = jdbc.queryForInt(sql, item.getParameters(pUser.getIdePerfil()));

		return (result != null && result!=0);
	}
	
	public int obterValorPorParametro(ParametroEnum parametroEnum) {
		String query = "select dsc_valor from parametro where ide_parametro = ?";
		
		try {
			Integer valorParametro = jdbc.queryForInt(query, parametroEnum.getIdeParametro());
			
			if(valorParametro == null) throw new Exception("Não foi possível obter o valor do parâmetro");
			
			return valorParametro;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void atualizaDtcUltimoLogin(Integer idePessoaFisica,String ip) throws Exception {
		String query = "UPDATE usuario SET dtc_ultimo_login = ?, ide_usuario = ?, caminho_requisicao = ?, endereco_ip = ? where ide_pessoa_fisica = ?";
		jdbc.update(query, (new Date()),idePessoaFisica,"/login.xhtml",ip, idePessoaFisica);
	}
	
	public boolean verificaUsuarioInativado(Integer idePessoaFisica) throws Exception {
		String query = "SELECT ind_excluido FROM usuario where ide_pessoa_fisica = " + idePessoaFisica.toString() + ";";
		List<Boolean> list = jdbc.queryForList(query, Boolean.class);
		
		for (Boolean u : list) {
			return u;
		}
		
		return false;
	}
	/* Verificar a quantidade de acesso diario e bloqueio após 4ª tentativa encaminhando e-mail para o usuario 
	 * 
	 * */
	public String verificarTentativaAcesso(String login,String ip) throws Exception{
		String url = "/login.xhtml?erro=true";
		

		String query = "SELECT count(*) FROM usuario_bloqueio where dsc_login = '"+login
				+"' AND to_char(dt_acesso, 'YYYY-MM-dd') = to_char(CURRENT_DATE,'YYYY-MM-dd')";
		Integer qtTentativa = jdbc.queryForInt(query);
		
		if(qtTentativa>=3){
	
			bloqueioAlterarSenha(login, ip, url);
			url="/login.xhtml?erro=true&userblock=true";
			
		}else{
			String insertSql = "INSERT INTO usuario_bloqueio (id,dsc_login,dsc_endereco_ip,dt_acesso)"
					+ " VALUES"
					+ "(nextval('usuario_bloqueio_id_seq'),'"+login+"','"
					+ ip + "',"
					+ "NOW())";
			jdbc.execute(insertSql);
		}
			

		return url;		
		
		
		
		
		
	}
	protected Integer getCountErroLogin(String login){
		String query = "SELECT count(*) FROM usuario_bloqueio where dsc_login = '"+login
				+"' AND to_char(dt_acesso, 'YYYY-MM-dd') = to_char(CURRENT_DATE,'YYYY-MM-dd')";
		return jdbc.queryForInt(query);
	}
	
	protected void bloqueioAlterarSenha(String login, String ip, String url) throws Exception{
		try{
		String sqlUsuario = "select distinct u.ind_excluido, p.ide_pessoa, "
				+ "pf.ide_pessoa_fisica, u.dsc_login, pf.nom_pessoa,"
				+ "p.des_email from usuario u "
				+" inner join pessoa_fisica pf " 
				+" ON pf.ide_pessoa_fisica = u.ide_pessoa_fisica "
				+" inner join pessoa p "
				+" ON p.ide_pessoa = pf.ide_pessoa_fisica "
				+" where u.dsc_login like '"+login+"' ";

			Map<String, Object> usuarioMap = jdbc.queryForMap(sqlUsuario);
			if (!Boolean.parseBoolean(usuarioMap.get("ind_excluido").toString())) {
				
				// String senhaNovaTemp = SenhaUtil.gerarSenha(8);
				String updateUsuario = "UPDATE USUARIO SET ind_excluido = TRUE " + ", ide_usuario = "+ usuarioMap.get("ide_pessoa_fisica") + ", caminho_requisicao = '" + url + "', endereco_ip = '" + ip
						+ "' WHERE ide_pessoa_fisica = "
						+ usuarioMap.get("ide_pessoa_fisica");
				jdbc.execute(updateUsuario);
				
				EmailUtil sendEmail = new EmailUtil();
				StringBuilder lMsg = new StringBuilder();
				/*Prezado(a) [nome do usuário], saudações! Foram identificadas sucessivas tentativas sem sucesso de acesso ao sistema SEIA utilizando seu login. Por essa razão e para sua segurança seu acesso está bloqueado. 
				 * 
				 * 
				 * */
				lMsg.append("<html><body>")
					.append(" Prezado(a) "+usuarioMap.get("nom_pessoa")+ ", saudações!")
					.append("<p> Foram identificadas sucessivas tentativas sem sucesso de acesso ao sistema SEIA utilizando seu login.<br><br>")
					.append(" Por essa razão e para sua segurança seu acesso está bloqueado. ")
					.append("Para reverter essa situação, acesse o SEIA e na tela apresentada, através da opção: \"Deseja reativar seu usuário?\" " )
					.append("solicite a reativação do seu usuário.<br><br> Após esse procedimento, novas orientações para a recuperação do acesso serão enviadas para este seu e-mail. </p>")
					.append("<br><br> <a href='http://sistema.seia.ba.gov.br'>http://sistema.seia.ba.gov.br</a>")
					.append("<br><br> Atenciosamente,<br><br> <b>Central de Atendimento - INEMA</b> ")
						.append("</html></body>");
				sendEmail.enviarEmailHtml(usuarioMap.get("des_email").toString(), "Bloqueio de Acesso SEIA", lMsg.toString());
			}
		} catch (Exception ex) {

			Log4jUtil.log(this.getClass().getName(),Level.ERROR, ex);
		}

	}
	
	public void apagarRegistroTentativa(Usuario user){

		String sql = "DELETE FROM usuario_bloqueio WHERE dsc_login= '" + user.getDscLogin() + "'";
		jdbc.execute(sql);
	}
	
	public void atualizarUltimoLogin(Authentication user, String ip){
		
			try {
				SecurityUser usuario = (SecurityUser) user.getPrincipal();
				atualizaDtcUltimoLogin(usuario.getUsuario().getIdePessoaFisica(), ip);
			} catch (Exception e) {
				e.printStackTrace();
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
	}
}