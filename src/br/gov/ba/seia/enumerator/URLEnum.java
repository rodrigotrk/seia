package br.gov.ba.seia.enumerator;

import java.util.Map;

import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.util.Util;

public enum URLEnum {
	
	CAMINHO_GEOBAHIA(ConfigEnum.GEOBAHIA_SERVER+"/geobahia5/index_seia.php"),
	CAMINHO_GEOBAHIA_CEFIR(ConfigEnum.GEOBAHIA_SERVER+"/geobahia5/interface_seia.php"),
	CAMINHO_GEOBAHIA_CERTIFICADO(ConfigEnum.GEOBAHIA_SERVER+"/geobahia5/seia_certificado.php?"),
	CAMINHO_GEOSEIA_CARREGAR_GEOMETRIA(ConfigEnum.GEOSEIA_SERVER+"/geoseia/shape2geom.php"),
	CAMINHO_NOVO_GEOBAHIA(ConfigEnum.NOVO_GEOBAHIA_SERVER+"/pra"),
	CAMINHO_GEOSEIA_IMPORTAR(ConfigEnum.GEOSEIA_SERVER+"/geoseia/geoseiav2.php"),
	CAMINHO_GEOSEIA_IMPORTAR_CDA(ConfigEnum.GEOSEIA_SERVER+"/geoseia/geoseia_cda.php"),
	CAMINHO_GEOSEIA_IMPORTAR_SHAPE_TIPO_PONTO(ConfigEnum.GEOSEIA_SERVER+"/geoseia/geoseiav2_shape_tipo_ponto.php"),
	SEIA_MONITORAMENTO("http://monitoramento.seia.ba.gov.br/login.xhtml"),
	VALIDAR_CERTIFICADO("http://sistema.seia.ba.gov.br/validarCertificado.xhtml");
	
	private String id;
	
	URLEnum(String id) {
		this.id = id;
	}
	
	public String getUrl(final Map<String, String> parametros) throws CampoObrigatorioException {
		StringBuilder url = new StringBuilder(this.id);
		if(Util.isNullOuVazio(url) || Util.isNullOuVazio(parametros)) {
			throw new CampoObrigatorioException("Parâmetros obrigatórios");
		}
		url.append("?");
		for (String key: parametros.keySet()) {
			url.append(key+"="+ parametros.get(key)+"&");
		}
		url.delete(url.length() - 1, url.length());
		return url.toString();
	}
	
	@Override
	public String toString(){
		return this.id;
	}	
	
}
