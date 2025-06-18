package br.gov.ba.seia.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.springframework.jdbc.core.JdbcTemplate;

import br.gov.ba.seia.util.Log4jUtil;

public class DownloadPortariaService {

	private static final String DATA_SOURCE = "java:/seiaDs";
	
	private static JdbcTemplate jdbc;

	private static DownloadPortariaService instance;

	private DownloadPortariaService() {

	}

	public static synchronized DownloadPortariaService getInstance() {
		if (instance == null) {
			instance = new DownloadPortariaService();
		}
		return instance;
	}

	static {
		try {
			jdbc = new JdbcTemplate((DataSource) InitialContext.doLookup(DATA_SOURCE));
		} 
		catch (NamingException e) {
			Log4jUtil.log(DownloadPortariaService.class.getName(),Level.ERROR, e);
		}
	}
	
	public List<Object[]> getListaArquivoPortaria() throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT	p.num_processo, ");
		sql.append("		arq.dsc_caminho_arquivo ");
		sql.append("FROM processo p ");
		sql.append("INNER JOIN arquivo_processo arq ");
		sql.append("	ON arq.ide_processo = p.ide_processo ");
		sql.append("INNER JOIN controle_tramitacao ct ");
		sql.append("	ON p.ide_processo = ct.ide_processo ");
		sql.append("INNER JOIN status_fluxo sf ");
		sql.append("	ON ct.ide_status_fluxo = sf.ide_status_fluxo ");
		sql.append("INNER JOIN requerimento r ");
		sql.append("	ON p.ide_requerimento = r.ide_requerimento ");
		sql.append("INNER JOIN empreendimento_requerimento er ");
		sql.append("	ON r.ide_requerimento = er.ide_requerimento ");
		sql.append("INNER JOIN empreendimento e ");
		sql.append("	ON er.ide_empreendimento = e.ide_empreendimento ");
		sql.append("INNER JOIN pessoa pes ");
		sql.append("	ON e.ide_pessoa = pes.ide_pessoa ");
		sql.append("INNER JOIN pessoa_juridica pj ");
		sql.append("	ON pes.ide_pessoa = pj.ide_pessoa_juridica ");
		sql.append("INNER JOIN processo_ato pa ");
		sql.append("	ON p.ide_processo = pa.ide_processo ");
		sql.append("INNER JOIN ato_ambiental aa ");
		sql.append("	ON pa.ide_ato_ambiental = aa.ide_ato_ambiental ");
		sql.append("INNER JOIN endereco_empreendimento ee ");
		sql.append("	ON e.ide_empreendimento = ee.ide_empreendimento ");
		sql.append("INNER JOIN endereco en ");
		sql.append("	ON ee.ide_endereco = en.ide_endereco ");
		sql.append("INNER JOIN logradouro l ");
		sql.append("	ON en.ide_logradouro = l.ide_logradouro ");
		sql.append("INNER JOIN municipio m ");
		sql.append("	ON l.ide_municipio = m.ide_municipio ");
		sql.append("WHERE p.ind_excluido IS FALSE ");
		sql.append("	AND ct.ind_fim_da_fila IS TRUE ");
		sql.append("	AND sf.ide_status_fluxo = 2 ");
		sql.append("	AND pj.num_cnpj = '15139629000194' ");
		sql.append("	AND aa.sgl_ato_ambiental = 'ASV' ");
		sql.append("	AND ct.dtc_tramitacao BETWEEN '2013-07-01 00:00:00.001' AND '2015-09-14 23:59:59.999' ");
		sql.append("	AND ee.ide_tipo_endereco = 4 ");
		sql.append("	AND arq.dsc_caminho_arquivo ilike '%portaria%' ");
		sql.append("	AND NOT arq.dsc_caminho_arquivo ilike '%minuta%' ");
		sql.append("ORDER BY p.num_processo ");
		
		ResultSet rs = jdbc.getDataSource().getConnection().createStatement().executeQuery(sql.toString());
		List<Object[]> lista = new ArrayList<Object[]>();
		
		while(rs.next()) {
			Object[] obj = {rs.getObject("num_processo"), rs.getObject("dsc_caminho_arquivo")};
			lista.add(obj);
		}
		
		return lista;
	}
}