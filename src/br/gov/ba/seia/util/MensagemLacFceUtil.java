package br.gov.ba.seia.util;

import br.gov.ba.seia.util.JsfUtil;

/**
 * UTIL centralizador das mensagens existentes no Documentos de Mensagens do SEIA, projeto LAC-FCE.
 * 
 * @author eduardo.fernandes 
 * @since 16/11/2016
 * @see <a href="https://10.105.17.77/svn/projeto_seia_lacfce/trunk/seialacfce_analise/2-%20An%c3%a1lise/Documento%20de%20Mensagens-%20SEIA_LACsFCEs.doc">Documento</a>
 * 
 */
public class MensagemLacFceUtil{

	/**
	 * <li><b>MSG - 001</b></li>
	 * <p>
	 * <i>Inclusão realizada com sucesso!</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 */
	public static void exibirMensagem001() {
		JsfUtil.addSuccessMessage(Util.getString("msg_generica_inclusao_efetuada"));
	}

	/**
	 * <li><b>MSG - 002</b></li>
	 * <p>
	 * <i>Alteração realizada com sucesso!</i>
	 * </p>
	 *
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 */
	public static void exibirMensagem002() {
		JsfUtil.addSuccessMessage(Util.getString("msg_generica_alteracao_efetuada"));
	}


	/**
	 * <li><b>MSG - 003</b></li>
	 * <p>
	 * <i>[nome do campo] é de preenchimento obrigatório.</i>
	 * </p>
	 *
	 * @author AlexandreQueiroz
	 * @since 27/06/2016
	 */
	public static void exibirMensagem003(String nomeDoCampo) {
		JsfUtil.addErrorMessage(nomeDoCampo +" "+ Util.getString("messagem_003"));
	}
	
	/**
	 * <li><b>MSG - 013</b></li>
	 * <p>
	 * <i>[nome do campo] é de preenchimento obrigatório e deve ser maior que zero.</i>
	 * </p>
	 *
	 * @author eduardo.fernandes
	 * @since 16/11/2016
	 */
	public static void exibirMensagem013(String nomeDoCampo) {
		JsfUtil.addErrorMessage(nomeDoCampo +" "+ Util.getString("msg_generica_null_ou_vazio"));
	}


	/**
	 * <li><b>MSG - 004</b></li>
	 * <p>
	 * <i>Deseja realmente excluir o registro? </i>
	 * </p>
	 * 
	 * @author alexandre.queiroz
	 * @since 27/06/2016
	 */
	public static void exibirMensagem004() {
		JsfUtil.addSuccessMessage(Util.getString("messagem_004"));
	}

	/**
	 * <li><b>MSG - 005</b></li>
	 * <p>
	 * <i>Exclusão realizada com sucesso.</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 */
	public static void exibirMensagem005() {
		JsfUtil.addSuccessMessage(Util.getString("msg_generica_exclusao_efetuada"));
	}
	
	/**
	 * <li><b>MSG - 015</b></li>
	 * <p>
	 * <i>salvo com sucesso!.</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 16/12/2016
	 */
	public static void exibirMensagem015(String nomeDoCampo) {
		JsfUtil.addSuccessMessage(nomeDoCampo + " "+ Util.getString("msg_generica_015"));
	}

	/**
	 * <li><b>MSG - 016</b></li>
	 * <p>
	 * <i>atualizado com sucesso!.</i>
	 * </p>
	 * 
	 * @author alexandre.queiroz
	 * @since 27/06/2016
	 */
	public static void exibirMensagem016(String nomeDoCampo) {
		JsfUtil.addSuccessMessage(nomeDoCampo + " "+ Util.getString("messagem_016"));
	}


	/**
	 * <li><b>INF - 001</b></li>
	 * <p>
	 * <i>Favor entrar em contato com o INEMA através do e-mail
	 * atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada
	 * seja realizado.</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 */
	public static void exibirInformacao001() {
		JsfUtil.addWarnMessage(Util.getString("msg_generica_cadastro_outros"));
	}
	
	/**
	 * 
	 * <li><b>INF - 023</b></li>
	 * <p>
	 * <i>É obrigatório enviar o arquivo na extensão “.pdf”, “.jpg” ou “.png”,  clicando no botão “Upload dos Dados Adicionais” 
	 * [nome do botão upload].</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes 
	 * @param documento
	 */
	public static void exibirInformacao023(String documento) {
		JsfUtil.addErrorMessage(Util.getString("msg_generica_upload_inf_0023") + documento);
	}

	/**
	 * <li><b>INF - 041</b></li>
	 * <p>
	 * <i>É obrigatória a seleção de ao menos <um/uma> [nome do campo].</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @param campo
	 * @since 23/11/2016
	 */
	public static void exibirInformacao041(String campo) {
		JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + campo + ".");
	}

	/**
	 * <li><b>INF - 065</b></li>
	 * <p>
	 * <i>O Cadastro de Atividade de Pesquisa Mineral só poderá ser realizado para o requerente do tipo Pessoa Jurídica.</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 23/11/2016
	 */
	public static void exibirInformacao065() {
		JsfUtil.addWarnMessage(Util.getString("msg_cpm_inf_065"));
	}

	/**
	 * <li><b>INF - 066</b></li>
	 * <p>
	 * <i>"Para preenchimento do cadastro, é necessário que a atividade a ser realizada se enquadre no CNAE de Indústrias Extrativas. Atualize o CNAE do requerente no Cadastro de Pessoa Jurídica do SEIA e tente novamente..</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 23/11/2016
	 */
	public static void exibirInformacao066() {
		JsfUtil.addWarnMessage(Util.getString("msg_cpm_inf_066"));
	}

	/**
	 * <li><b>INF - 067</b></li>
	 * <p>
	 * <i>É necessário que atividade de pesquisa mineral sem guia de utilização tenha ao menos um responsável técnico.</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 23/11/2016
	 */
	public static void exibirInformacao067() {
		JsfUtil.addWarnMessage(Util.getString("msg_cpm_inf_067"));
	}

	/**
	 * <li><b>INF - 068</b></li>
	 * <p>
	 * <i>É obrigatório o cadastrado do número da Carteira de identidade de conselho de classe do Responsável Técnico pela atividade de Pesquisa Mineral sem guia de utilização. Atualize os documentos do Responsável Técnico no Cadastro de Pessoa Física do SEIA e tente novamente.</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 23/11/2016
	 */
	public static void exibirInformacao068() {
		JsfUtil.addWarnMessage(Util.getString("msg_cpm_inf_068"));
	}

	/**
	 * <li><b>INF - 070</b></li>
	 * <p>
	 * <i>O shape inserido sobrepõe uma poligonal do DNPM já cadastrada.</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 */
	public static void exibirInformacao070() {
		JsfUtil.addWarnMessage(Util.getString("msg_info_070"));
	}

	/**
	 * 
	 * <li><b>ERRO - 007</b></li>
	 * <p>
	 * <i>Ocorreu um erro ao carregar o arquivo. Verifique se a extensão é .pdf, .jpg ou .png.</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 29/11/2016
	 */
	public static void exibirErro007() {
		JsfUtil.addWarnMessage(Util.getString("msg_erro_007"));
	}
	
	/**
	 * <li><b>INF - 074</b></li>
	 * <p>
	 * <i>Data final do período é anterior à data inicial. Preencha um período válido e tente novamente.</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 18/01/2017
	 */
	public static void exibirInformacao074() {
		JsfUtil.addWarnMessage(Util.getString("msg_info_074"));
	}
	
	/**
	 * <li><b>INF - 075</b></li>
	 * <p>
	 * <i>É obrigatório o preenchimento de ao menos um campo de filtro para realizar a consulta.</i>
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 */
	public static void exibirInformacao075() {
		JsfUtil.addWarnMessage(Util.getString("msg_info_075"));
	}
	
	/**
	 * <li><b>INF - 076</b></li>
	 * <p>
	 * <i>As coordenadas informadas  já foram cadastras para este método de propecção mineral. Verifique as coordenadas informadas e tente novamente.
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 23/01/2017
	 */
	public static void exibirInformacao076() {
		JsfUtil.addWarnMessage(Util.getString("msg_info_076"));
	}
	
}
