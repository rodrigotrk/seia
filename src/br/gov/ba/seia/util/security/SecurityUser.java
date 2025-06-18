package br.gov.ba.seia.util.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.entity.Usuario;

public class SecurityUser implements UserDetails, RowMapper<SecurityUser> {

	private static final long serialVersionUID = -1848754333232191002L;
	
	public static final String SQL = "SELECT u.ide_pessoa_fisica, u.dsc_login, u.dsc_senha, u.ide_perfil, u.ind_validacao, fu.dsc_url, u.dtc_ultimo_login, u.dtc_ultima_senha, u.ind_excluido "
			+ "FROM usuario u "
			+ "INNER JOIN perfil p ON u.ide_perfil = p.ide_perfil "
			+ "LEFT JOIN funcionalidade_url fu ON p.ide_funcionalidade_principal = fu.ide_funcionalidade AND fu.ind_principal = true "
			+ "WHERE u.dsc_login = ? limit 1"; //u.ind_excluido = false and #7089

	private Usuario usuario;
	private String username;
	private String password;
	private boolean validado;
	private int perfil;
	private String urlInicial;
	private List<SecurityAuthority> authorities;
	private List<SecurityItem> itens;
	private List<SecurityItem> urls;
	private Boolean ativo;
	private Boolean cadastroCompleto;

	public SecurityUser() {
		
	}
	
	public SecurityUser(int pPerfil) {
		perfil = pPerfil;
	}
	
	public SecurityUser(Usuario user, int pPerfil) {
		usuario = user;
		username = user.getDscLogin();
		password = user.getDscSenha();
		validado = user.getIndValidacao();
		perfil = pPerfil;
		authorities = gerarAutorizacoes(perfil);
	}
	
	public SecurityUser(Usuario user, int pPerfil, String pUrlPrincipal) {
		usuario = user;
		username = user.getDscLogin();
		password = user.getDscSenha();
		validado = user.getIndValidacao();
		perfil = pPerfil;
		authorities = gerarAutorizacoes(perfil);
		urlInicial = pUrlPrincipal;
	}

	private List<SecurityAuthority> gerarAutorizacoes(int pPerfil) {
		
		List<SecurityAuthority> lAuthorities = new ArrayList<SecurityAuthority>();
		lAuthorities.add(new SecurityAuthority(perfil));
		return lAuthorities;
	}
	
	@Override
	public SecurityUser mapRow(ResultSet rs, int arg1) throws SQLException {
		
		Usuario user = new Usuario();
		
		user.setIdePessoaFisica(rs.getInt("ide_pessoa_fisica"));
		user.setDscLogin(rs.getString("dsc_login"));
		user.setDscSenha(rs.getString("dsc_senha"));
		
		int autorizacao = rs.getInt("ide_perfil");
		user.setIdePerfil(new Perfil(autorizacao));
		
		user.setIndValidacao(rs.getBoolean(("ind_validacao")));
		
		String url  = rs.getString("dsc_url");
		
		if (rs.wasNull()) {
			url = null;
		}
		
		user.setDtcUltimoLogin(rs.getDate("dtc_ultimo_login"));
		
		user.setDtcUltimaSenha(rs.getDate("dtc_ultima_senha"));
		
		user.setIndExcluido(rs.getBoolean("ind_excluido"));
		
		return new SecurityUser(user, autorizacao, url);
	}
	
	public int getIdePerfil() {
		return perfil;
	}
	
	public List<SecurityItem> getItens() {
		return itens;
	}

	public void setItens(List<SecurityItem> itens) {
		this.itens = itens;
	}

	public boolean isValidado() {
		return validado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getUrlInicial() {
		return urlInicial;
	}

	@Override
	public Collection<SecurityAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {

		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	@Override
	public int hashCode() {
        int hash = 0;
        hash += (this.getUsuario().getIdePessoaFisica() != null ? this.getUsuario().getIdePessoaFisica().hashCode() : 0);
        return hash;
	}
	
	@Override
	public boolean equals(Object arg0) {
		SecurityUser securityUser = (SecurityUser)arg0;
		return (this.getUsuario().getIdePessoaFisica().intValue() == securityUser.getUsuario().getIdePessoaFisica().intValue());
		
	}

	public List<SecurityItem> getUrls() {
		return urls;
	}

	public void setUrls(List<SecurityItem> urls) {
		this.urls = urls;
	}

	public Boolean getCadastroCompleto() {
		return cadastroCompleto;
	}

	public void setCadastroCompleto(Boolean cadastroCompleto) {
		this.cadastroCompleto = cadastroCompleto;
	}
}