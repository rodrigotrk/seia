package br.gov.ba.seia.util.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.springframework.jdbc.core.RowMapper;

class SecurityItem implements RowMapper<SecurityItem> {

	private Integer secao;
	private Integer funcionalidade;
	private Integer acao;
	private String url;

	protected SecurityItem(String key) {
		loadKey(key);
	}

	protected SecurityItem() {

	}

	public String getSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("f.ide_funcionalidade,");
		sql.append("s.ide_secao,");
		sql.append("a.ide_acao ");
		sql.append("FROM perfil p ");
		sql.append("INNER JOIN rel_grupo_perfil_funcionalidade g ON p.ide_perfil = g.ide_perfil ");
		sql.append("INNER JOIN acao A ON g.ide_acao = a.ide_acao ");
		sql.append("INNER JOIN funcionalidade f ON g.ide_funcionalidade = f.ide_funcionalidade ");
		sql.append("INNER JOIN secao s ON f.ide_secao = s.ide_secao ");
		sql.append("WHERE p.ide_perfil = ? ");

		if (secao != null) {
			sql.append("AND s.ide_secao = ? ");
		}
		if (funcionalidade != null) {
			sql.append("AND f.ide_funcionalidade = ? ");
		}
		if (acao != null) {
			sql.append("AND a.ide_acao = ? ");
		}

		return sql.toString();
	}
	
	public String getSqlCount() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(f.ide_funcionalidade) ");
		sql.append("FROM perfil p ");
		sql.append("INNER JOIN rel_grupo_perfil_funcionalidade g ON p.ide_perfil = g.ide_perfil ");
		sql.append("INNER JOIN acao A ON g.ide_acao = a.ide_acao ");
		sql.append("INNER JOIN funcionalidade f ON g.ide_funcionalidade = f.ide_funcionalidade ");
		sql.append("INNER JOIN secao s ON f.ide_secao = s.ide_secao ");
		sql.append("WHERE p.ide_perfil = ? ");

		if (secao != null) {
			sql.append("AND s.ide_secao = ? ");
		}
		if (funcionalidade != null) {
			sql.append("AND f.ide_funcionalidade = ? ");
		}
		if (acao != null) {
			sql.append("AND a.ide_acao = ? ");
		}

		return sql.toString();
	}

	public String getSqlAllItens() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT s.ide_secao, f.ide_funcionalidade, a.ide_acao,u.dsc_url");
		sql.append(" FROM perfil p");
		sql.append(" INNER JOIN rel_grupo_perfil_funcionalidade g ON p.ide_perfil = g.ide_perfil ");
		sql.append(" INNER JOIN acao a ON g.ide_acao = a.ide_acao");
		sql.append(" INNER JOIN funcionalidade f ON g.ide_funcionalidade = f.ide_funcionalidade");
		sql.append(" LEFT JOIN funcionalidade_url u ON u.ide_funcionalidade = f.ide_funcionalidade");
		sql.append(" INNER JOIN secao s ON f.ide_secao = s.ide_secao");
		sql.append(" WHERE p.ide_perfil = ?");
		sql.append(" UNION");
		sql.append(" SELECT s.ide_secao, f.ide_funcionalidade, null,u.dsc_url");
		sql.append(" FROM perfil p");
		sql.append(" INNER JOIN rel_grupo_perfil_funcionalidade g ON p.ide_perfil = g.ide_perfil ");
		sql.append(" INNER JOIN funcionalidade f ON g.ide_funcionalidade = f.ide_funcionalidade");
		sql.append(" LEFT JOIN funcionalidade_url u ON u.ide_funcionalidade = f.ide_funcionalidade");
		sql.append(" INNER JOIN secao s ON f.ide_secao = s.ide_secao");
		sql.append(" WHERE p.ide_perfil = ?");
		sql.append(" UNION");
		sql.append(" SELECT f.ide_secao, null, null,u.dsc_url FROM perfil p");
		sql.append(" INNER JOIN rel_grupo_perfil_funcionalidade g ON p.ide_perfil = g.ide_perfil ");
		sql.append(" INNER JOIN funcionalidade f ON g.ide_funcionalidade = f.ide_funcionalidade");
		sql.append(" LEFT JOIN funcionalidade_url u ON u.ide_funcionalidade = f.ide_funcionalidade");
		sql.append(" WHERE p.ide_perfil = ?");
		return sql.toString();
	}

	public String getSqlWithUrl() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(1) ");
		sql.append("FROM  rel_grupo_perfil_funcionalidade g ");
		sql.append("INNER JOIN funcionalidade f ON g.ide_funcionalidade = f.ide_funcionalidade AND g.ide_perfil = ? ");
		sql.append("INNER JOIN funcionalidade_url u ON u.ide_funcionalidade = f.ide_funcionalidade AND u.dsc_url LIKE ?");
		return sql.toString();
	}

	public Object[] getParametersWithUrl(int idePerfil) {
		ArrayList<Object> param = new ArrayList<Object>();
		param.add(idePerfil);
		param.add(url);
		return param.toArray();
	}

	public Object[] getParametersAllItens(int idePerfil) {
		ArrayList<Object> param = new ArrayList<Object>();
		param.add(idePerfil);
		param.add(idePerfil);
		param.add(idePerfil);
		return param.toArray();
	}

	public Object[] getParameters(int idePerfil) {

		ArrayList<Object> param = new ArrayList<Object>();
		param.add(idePerfil);
		if (secao != null) {
			param.add(secao);
		}
		if (funcionalidade != null) {
			param.add(funcionalidade);
		}
		if (acao != null) {
			param.add(acao);
		}
		return param.toArray();
	}

	private void loadKey(String key) {
		StringTokenizer tokens = new StringTokenizer(key, ".");

		if (tokens.hasMoreTokens()) {
			secao = getInteger(tokens.nextToken());
		}
		if (tokens.hasMoreTokens()) {
			funcionalidade = getInteger(tokens.nextToken());
		}
		if (tokens.hasMoreTokens()) {
			acao = getInteger(tokens.nextToken());
		}
	}

	private Integer getInteger(String token) {
		try {
			return Integer.parseInt(token.trim());
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public SecurityItem mapRow(ResultSet rs, int i) throws SQLException {
		SecurityItem item = new SecurityItem();
		int aux = rs.getInt("ide_secao");
		if (!rs.wasNull()) {
			item.setSecao(aux);
		}

		String url = rs.getString("dsc_url");
		if (!rs.wasNull()) {
			item.setUrl(url);
		}

		aux = rs.getInt("ide_funcionalidade");
		if (!rs.wasNull()) {
			item.setFuncionalidade(aux);
		}
		aux = rs.getInt("ide_acao");
		if (!rs.wasNull()) {
			item.setAcao(aux);
		}
		return item;
	}

	public Integer getSecao() {
		return secao;
	}

	public void setSecao(Integer secao) {
		this.secao = secao;
	}

	public Integer getFuncionalidade() {
		return funcionalidade;
	}

	public void setFuncionalidade(Integer funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public Integer getAcao() {
		return acao;
	}

	public void setAcao(Integer acao) {
		this.acao = acao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[{");
		if (secao != null) {
			builder.append(secao);
		}
		if (funcionalidade != null) {
			builder.append(".");
			builder.append(funcionalidade);
		}
		if (acao != null) {
			builder.append(".");
			builder.append(acao);
		}
		builder.append("}");
		if (url != null) {
			builder.append(", url=");
			builder.append(url);
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acao == null) ? 0 : acao.hashCode());
		result = prime * result + ((funcionalidade == null) ? 0 : funcionalidade.hashCode());
		result = prime * result + ((secao == null) ? 0 : secao.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecurityItem other = (SecurityItem) obj;
		if (acao == null) {
			if (other.acao != null)
				return false;
		} else if (!acao.equals(other.acao))
			return false;
		if (funcionalidade == null) {
			if (other.funcionalidade != null)
				return false;
		} else if (!funcionalidade.equals(other.funcionalidade))
			return false;
		if (secao == null) {
			if (other.secao != null)
				return false;
		} else if (!secao.equals(other.secao))
			return false;
		return true;
	}

}