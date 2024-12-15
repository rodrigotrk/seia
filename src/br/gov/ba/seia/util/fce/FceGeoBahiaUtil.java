package br.gov.ba.seia.util.fce;

import static br.gov.ba.seia.util.Util.criarStreamComUrl;

import java.io.IOException;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceTurismo;
import br.gov.ba.seia.entity.FceTurismoLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.enumerator.ConfigEnum;

public class FceGeoBahiaUtil {

	/**
	 * Método para retornar o endereço IP do servidor com a aplicação do GEOBAHIA.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 04/03/2015
	 * @return
	 */
	private static String urlServidorGeoBahia(){
		return ConfigEnum.GEOBAHIA_SERVER + "/";
	}

	/**
	 * Método para retonrar o endereço da aplicação GEOBAHIA 5.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 04/03/2015
	 * @return
	 */
	private static String urlGeoBahia(){
		return urlServidorGeoBahia()+"geobahia5/";
	}
	/**
	 * Método para retonar a URL que será importada para gerar a imagem com a Poligonal Externa do Empreendimento e as APP's do {@link FceTurismo}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 03/03/2015
	 * @param ideFce
	 * @return
	 * @throws IOException
	 * @see #6563
	 */
	public static String criarURLToVisualizarShapeEmpreendimentoAndAppInRelatorioByIdeFce(Integer ideFce) throws IOException {
		StringBuilder buffer = getURLGeoBahiaToFce(ideFce);
		return urlServidorGeoBahia() + buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20");
	}

	/**
	 * Método para conseguir a {@link StringBuilder} com a URL do GEOBAHIA para Impressão de Croqui.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 03/03/2015
	 * @param ideFce
	 * @return
	 * @throws IOException
	 * @see #6563
	 */
	private static StringBuilder getURLGeoBahiaToFce(Integer ideFce) throws IOException {
		return criarStreamComUrl(obterUrlGeoBahiaToFce(paraImprimirCroqui(ideFce)));
	}

	/**
	 * Método que retorna o caminho da página para ser transformada em imagem dentro do relatório.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 04/03/2015
	 * @param ideFce
	 * @return
	 */
	private static String paraImprimirCroqui(Integer ideFce) {
		return "seia_fce_certificado.php?idfce="+ideFce.toString()+"&res=640%20480";
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 03/03/2015
	 * @param parametros
	 * @return
	 */
	private static String obterUrlGeoBahiaToFce(String parametros) {
		return urlGeoBahia() + parametros;
	}

	/**
	 * Método que verifica o que deve ser exibido em tela.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 04/03/2015
	 * @param object
	 * @return
	 */
	private static String paraVisualizarEmTela(Object object) {
		String param = "";
		String paramId = "";
		if(object instanceof Fce){
			Fce fce = (Fce) object;
			param = "_fce";
			paramId = "idfce="+fce.getIdeFce().toString();
		}	
		else if(object instanceof Empreendimento) {
			Empreendimento emp = (Empreendimento) object;
			paramId = "idloc="+emp.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString()+"&tipo=1";
		}
		else if(object instanceof FceTurismoLocalizacaoGeografica) {
			FceTurismoLocalizacaoGeografica fceTurLocGeo = (FceTurismoLocalizacaoGeografica) object;
			paramId = "idloc="+fceTurLocGeo.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString()+"&tipo=3"; // TIPO = 3, Visualização em verde da APP
		}	
		else if(object instanceof LocalizacaoGeografica) {
			LocalizacaoGeografica loc = (LocalizacaoGeografica) object;
			paramId = "idloc="+loc.getIdeLocalizacaoGeografica().toString()+"&tipo=1";
		}
		return "index_seia" + param +".php?acao=view&" + paramId;
	}

	/**
	 * Método que abre nova página com a URL para visualização da Poligonal do Empreendimento com suas APP's.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 03/03/2015
	 * @param ideFce
	 * @return {@link String}
	 */
	public static String criarURLToVisualizarShapeInFce(Object obj) {
		StringBuilder lUrl = new StringBuilder();
		StringBuilder lReturn = new StringBuilder();
		lUrl.append(obterUrlGeoBahiaToFce(paraVisualizarEmTela(obj)));
		lReturn.append("window.open('");
		lReturn.append(lUrl.toString());
		lReturn.append("');");
		return lReturn.toString();
	}
}