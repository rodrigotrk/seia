package br.gov.ba.seia.util.security;

import org.springframework.security.core.GrantedAuthority;

public class SecurityAuthority implements GrantedAuthority {

	private static final long serialVersionUID = -5516242427303128878L;

	private String authority;

	public SecurityAuthority(final int pNomePerfil) {
		authority = "ROLE_" + pNomePerfil;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
}