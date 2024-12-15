package br.gov.ba.seia.util.security;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.gov.ba.seia.util.Log4jUtil;

public class SecurityUserService implements UserDetailsService {


	private static final String DATA_SOURCE = "java:/seiaDs";
	
	private static JdbcTemplate jdbc;
	
	
	static {
		try {
			jdbc = new JdbcTemplate((DataSource) InitialContext.doLookup(DATA_SOURCE));
		} catch (NamingException e) {
			Log4jUtil.log(SecurityUserService.class.getName(),Level.ERROR, e);
		}
	}

	@Override
	public SecurityUser loadUserByUsername(String pUserName) throws UsernameNotFoundException {
		
		SecurityUser securityUser = obtemUser(pUserName);
		
		if (securityUser == null || securityUser.getPassword() == null || !securityUser.isValidado()) {
			throw new UsernameNotFoundException("O usuário '" + pUserName + "' não existe ou a senha está incorreta.");
		}
		
		return securityUser;
	}

	private SecurityUser obtemUser(String pUserName) throws DataAccessException{
		return jdbc.queryForObject(SecurityUser.SQL, new SecurityUser(), pUserName);
	}	
}