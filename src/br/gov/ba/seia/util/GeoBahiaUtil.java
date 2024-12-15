package br.gov.ba.seia.util;

import br.gov.ba.seia.enumerator.ConfigEnum;

public class GeoBahiaUtil {
	
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
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 03/03/2015
	 * @param parametros
	 * @return
	 */
	public static String obterUrlGeoBahia(String parametros) {
		return urlGeoBahia() + parametros;
	}


}