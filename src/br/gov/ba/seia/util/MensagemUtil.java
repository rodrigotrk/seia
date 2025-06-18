package br.gov.ba.seia.util;

import java.util.List;

import br.gov.ba.seia.enumerator.TipoMensagemEnum;

/**
 * @author eduardo.fernandes
 * @author alexandre.queiroz
 * @since 11/01/2017
 * @see <a href="https://10.105.17.77/svn/projeto_seia_v2/trunk/seiav2_analise/2-%20An%c3%a1lise/SEIA_Requerimento%20-%20Documento%20de%20Mensagens.doc">Documento</a>
 *
 */
public class MensagemUtil {

	/*****************
	/*				 *
	//XXX: MENSAGENS *
	/* 				 *
	/*****************/

	/**
	 * <li><b>MSG - 0001</b></li>
	 * <p><i> Inclusão realizada com sucesso! </i></p>
	 **/
	public static void msg0001() {
		exibir(TipoMensagemEnum.SUCESSO, Util.getString("MSG-001"));
	}


	/**
	 * <p><i> Alteração realizada com sucesso! </i></p>
	 * <li><b>MSG - 0002</b></li>g
	 **/
	public static void msg0002() {
		exibir(TipoMensagemEnum.SUCESSO, Util.getString("MSG-002"));
	}


	/**
	 * <li><b>MSG - 0003</b></li>
	 *
	 * <p><i> [nome do campo] é de preenchimento obrigatório. </i></p>
	 ***/
	public static void msg0003(String nomeDoCampo) {
		exibir(TipoMensagemEnum.ERRO, Util.getString("MSG-003",nomeDoCampo));
	}


	/**
	 * <li><b>MSG - 0004</b></li>
	 * <p><i> Deseja realmente excluir o registro?  </i></p>
	 **/
	public static void msg0004() {
		exibir(TipoMensagemEnum.ALERTA, Util.getString("MSG-004"));
	}


	/**
	 * <li><b>MSG - 0005</b></li>
	 * <p><i> Exclusão realizada com sucesso. </i></p>
	 **/
	public static void msg0005() {
		exibir(TipoMensagemEnum.SUCESSO, Util.getString("MSG-005"));
	}


	/**
	 * <li><b>MSG - 0006</b></li>
	 * <p><i> [nome do campo] é inválido. </i></p>
	 **/
	public static void msg0006(String nomeDoCampo) {
		exibir(TipoMensagemEnum.ALERTA,nomeDoCampo +" "+Util.getString("MSG-006"));
	}


	/**
	 * <li><b>MSG - 0007</b></li>
	 * <p><i> Nenhum registro encontrado. </i></p>
	 **/
	public static void msg0007() {
		exibir(TipoMensagemEnum.ALERTA,Util.getString("MSG-007"));
	}


	/**
	 * <li><b>MSG - 0008</b></li>
	 * <p><i> Inserção desse arquivo excede o espaço permitido. </i></p>
	 **/
	public static void msg0008() {
		exibir(TipoMensagemEnum.ALERTA,Util.getString("MSG-008"));
	}


	/**
	 * <li><b>MSG - 0009</b></li>
	 * <p><i> Favor inserir todas as informações necessárias para a pesquisa. </i></p>
	 **/
	public static void msg0009() {
		exibir(TipoMensagemEnum.ALERTA,Util.getString("MSG-009"));
	}


	/**
	 * <li><b>MSG - 0010</b></li>
	 * <p><i> Dados salvos com sucesso! </i></p>
	 **/
	public static void msg0010() {
		exibir(TipoMensagemEnum.SUCESSO,Util.getString("MSG-010"));
	}


	/**
	 * <li><b>MSG - 0011</b></li>
	 * <p><i>Tem certeza que deseja excluir a(s) opção(ões) selecionada(s)? </i></p>
	 **/
	public static void msg0011() {
		exibir(TipoMensagemEnum.ALERTA,Util.getString("MSG-011"));
	}


	/**
	 * <li><b>MSG - 0012</b></li>
	 * <p><i> Operação realizada com sucesso </i></p>
	 **/
	public static void msg0012() {
		exibir(TipoMensagemEnum.SUCESSO,Util.getString("MSG-012"));
	}


	/**
	 * <li><b>MSG - 0013</b></li>
	 * <p><i> Favor selecionar 'SIM' ou 'NÃO' na pergunta [número da pergunta do requerimento]. </i></p>
	 **/
	public static void msg0013() {
		exibir(TipoMensagemEnum.ALERTA,Util.getString("MSG-013"));
	}


	/**
	 * <li><b>MSG - 0014</b></li>
	 * <p><i> Selecione alguma opção do campo [número da pergunta do requerimento]. </i></p>
	 **/
	public static void msg0014() {
		exibir(TipoMensagemEnum.ALERTA,Util.getString("MSG-014"));
	}


	/**
	 * <li><b>MSG - 0015</b></li>
	 * <p><i>Transferência de titularidade excluída com sucesso. </i></p>
	 **/
	public static void msg0015() {
		exibir(TipoMensagemEnum.SUCESSO,Util.getString("MSG-015"));
	}


	/**
	 * <li><b>MSG - 0016</b></li>
	 * <p><i> Dados da Análise Técnica cadastrados com sucesso. </i></p>
	 **/
	public static void msg0016() {
		exibir(TipoMensagemEnum.SUCESSO,Util.getString("MSG-016"));
	}


	/**
	 * <li><b>MSG - 0017</b></li>
	 * <p><i> Análise Técnica enviada com sucesso. </i></p>
	 **/
	public static void msg0017() {
		JsfUtil.addSuccessMessage(Util.getString("MSG-017"));
	}


	/**
	 * <li><b>MSG - 0018</b></li>
	 * <p><i> Dados da portaria salvo com sucesso. </i></p>
	 **/
	public static void msg0018() {
		JsfUtil.addSuccessMessage(Util.getString("MSG-018"));
	}


	/**
	 * <li><b>MSG - 0019</b></li>
	 * <p><i> Deseja realmente reabrir o processo? </i></p>
	 **/
	public static void msg0019() {
		JsfUtil.addWarnMessage(Util.getString("MSG-019"));
	}


	/**
	 * <li><b>MSG - 0020</b></li>
	 * <p><i> Processo reaberto com sucesso. </i></p>
	 **/
	public static void msg0020() {
		JsfUtil.addSuccessMessage(Util.getString("MSG-020"));
	}


	/**
	 * <li><b>MSG - 0021</b></li>
	 * <p><i> Deseja realmente cancelar a reserva de água ?</i></p>
	 **/
	public static void msg0021() {
		JsfUtil.addWarnMessage(Util.getString("MSG-021"));
	}


	/**
	 * <li><b>MSG - 0022</b></li>
	 * <p><i>Reserva de água cancelada com sucesso. </i></p>
	 **/
	public static void msg0022() {
		JsfUtil.addSuccessMessage(Util.getString("MSG-022"));
	}


	/**
	 * <li><b>MSG - 0023</b></li>
	 * <p><i> O processo informado não está concluído. </i></p>
	 **/
	public static void msg0023() {
		JsfUtil.addWarnMessage(Util.getString("MSG-023"));
	}


	/**
	 * <li><b>MSG - 0024</b></li>
	 * <p><i> Favor entrar em contato com a empresa e solicitar que a mesma se cadastre no SEIA. </i></p>
	 **/
	public static void msg0024() {
		JsfUtil.addWarnMessage(Util.getString("MSG-024"));
	}

	/********************
	/*					*
	//XXX: INFORMAÇÕES  *
	/* 					*
	/********************/

	/**
	 * <li><b>INFO - 001</b></li>
	 * <p><i>Requerimento salvo com sucesso!</i></p>
	 **/
	public static void info0001() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0001"));
	}

	/**
	 * <li><b>INFO - 002</b></li>
	 * <p><i>O requerimento foi aberto com sucesso. O número para acompanhamento é: [número do requerimento]. Você receberá por e-mail a relação de documentos para abertura do processo. Após o envio da documentação você receberá o boleto para pagamento da taxa ambiental.</i></p>
	 **/
	public static void info0002() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0002"));
	}

	/**
	 * <li><b>INFO - 003</b></li>
	 * <p><i> Localização geográfica salva com sucesso!</i></p>
	 **/
	public static void info0003() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0003"));
	}

	/**
	 * <li><b>INFO - 004</b></li>
	 * <p><i> Ato Ambiental salvo com Sucesso.</i></p>
	 **/
	public static void info0004() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0004"));
	}

	/**
	 * <li><b>INFO - 005</b></li>
	 * <p><i>Após a está tela de confirmação a notificação será automaticamente enviada para o requerente. Deseja realmente aprovar esta notificação?</i></p>
	 */
	public static void info0005() {
		JsfUtil.addWarnMessage(Util.getString("INF-0005"));
	}

	/**
	 * <li><b>INFO - 006</b></li>
	 * <p><i>Posto de Combustível inserido com sucesso.</i></p>
	 */
	public static void info0006() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0006"));
	}

	/**
	 * <li><b>INFO - 007</b></li>
	 * <p><i>O resultado das somas das áreas internas do empreendimento informadas, não pode ser maior que o empreendimento.</i></p>
	 */
	public static void info0007() {
		JsfUtil.addWarnMessage(Util.getString("INF-0007"));
	}

	/**
	 * <li><b>INFO - 008</b></li>
	 * <p><i>Dados do Posto de Combustível inseridos com sucesso.</i></p>
	 */
	public static void info0008() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0008"));
	}

	/**
	 * <li><b>INFO - 009</b></li>
	 * <p><i>Dados do Sistema de Controle inseridos com sucesso.</i></p>
	 */
	public static void info0009() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0009"));
	}

	/**
	 * <li><b>INFO - 010</b></li>
	 * <p><i>Dados da manutenção inseridos com sucesso.</i></p>
	 */
	public static void info0010() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0010"));
	}

	/**
	 * <li><b>INFO - 011</b></li>
	 * <p><i>O empreendimento não está regular perante a legislação ambiental. Aceite os termos exibidos anteriormente para permitir tal regularização.</i></p>
	 */
	public static void info0011() {
		JsfUtil.addWarnMessage(Util.getString("INF-0011"));
	}

	/**
	 * <li><b>INFO - 012</b></li>
	 * <p><i>Deseja imprimir o relatório da LAC</i></p>
	 */
	public static void info0012() {
		JsfUtil.addWarnMessage(Util.getString("INF-0012"));
	}

	/**
	 * <li><b>INFO - 013</b></li>
	 * <p><i>Você precisa estar ciente de que todas as correspondências do presente empreendimento serão encaminhadas para o e-mail informado.</i></p>
	 */
	public static void info0013() {
		JsfUtil.addWarnMessage(Util.getString("INF-0013"));
	}

	/**
	 * <li><b>INFO - 014</b></li>
	 * <p><i>Processo não disponível para distribuição. </i></p>
	 */
	public static void info0014() {
		JsfUtil.addWarnMessage(Util.getString("INF-0014"));
	}

	/**
	 * <li><b>INFO - 015</b></li>
	 * <p><i> De acordo com o que determina o § 1º do Art. 116 do Decreto 14.024/2012, alterado pelo decreto 14.032/2012, se há agravamento dos impactos ambientais, mas a alteração não está incluída no mesmo objeto da atividade/empreendimento licenciado, a solicitação deve ser de uma nova licença, e não de uma Licença de alteração.</i></p>
	 */
	public static void info0015() {
		JsfUtil.addWarnMessage(Util.getString("INF-0015"));
	}


	/**
	 * <li><b>INFO - 016</b></li>
	 * <p><i> De acordo com o que determina o § 1º do Art. 116 do Decreto 14.024/2012, alterado pelo decreto 14.032/2012, tendo sido negativas as respostas aos itens 1.1 e 1.2, a solicitação não se configura como licença de alteração.</i></p>
	 */
	public static void info0016() {
		JsfUtil.addWarnMessage(Util.getString("INF-0016"));
	}


	/**
	 * <li><b>INFO - 017</b></li>
	 * <p><i> Processo não encontrado.</i></p>
	 */
	public static void info0017() {
		JsfUtil.addWarnMessage(Util.getString("INF-0017"));
	}


	/**
	 * <li><b>INFO - 018</b></li>
	 * <p><i> Existe transferência de licença ambiental (TLA) em trâmite para esse ato.</i></p>
	 */
	public static void info0018() {
		JsfUtil.addWarnMessage(Util.getString("INF-0018"));
	}


	/**
	 * <li><b>INFO - 019</b></li>
	 * <p><i> Ao inserir o número do processo, favor atentar para a inserção do número completo, seguindo o padrão da numeração do sistema que foi gerado. Ex.: Padrão do numero de processo SEIA: 2015.001.XXXXX/INEMA/LIC-0XXXX, Padrão do número de processos CERBERUS: 2001-0XXXXX/TEC/LL-0XXX, Padrão do número de processos PROHIDROS: 2013-0XXXXX/OUT/APPO-0XXX.</i></p>
	 */
	public static void info0019() {
		JsfUtil.addWarnMessage(Util.getString("INF-0019"));
	}

	/**
	 * <li><b>INFO - 020</b></li>
	 * <p><i>Deseja realmente cancelar o TCCA? </i></p>
	 */
	public static void info0020() {
		JsfUtil.addWarnMessage(Util.getString("INF-0020"));
	}

	/**
	 * <li><b>INFO - 021</b></li>
	 * <p><i> TCCA cancelado com sucesso. </i></p>
	 */
	public static void info0021() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0021"));
	}

	/**
	 * <li><b>INFO - 022</b></li>
	 * <p><i>A origem selecionada não possui saldo suficiente para executar esta operação.</i></p>
	 */
	public static void info0022() {
		JsfUtil.addWarnMessage(Util.getString("INF-0022"));
	}

	/**
	 * <li><b>INFO - 023</b></li>
	 * <p><i>Deseja criar uma cópia deste TCCA e aproveitar todos os dados já cadastrados?</i></p>
	 */
	public static void info0023() {
		JsfUtil.addWarnMessage(Util.getString("INF-0023"));
	}

	/**
	 * <li><b>INFO - 024</b></li>
	 * <p><i>Deseja realmente cancelar o Projeto?</i></p>
	 */
	public static void info0024() {
		JsfUtil.addWarnMessage(Util.getString("INF-0024"));
	}

	/**
	 * <li><b>INFO - 025</b></li>
	 * <p><i>Projeto cancelado com sucesso. </i></p>
	 */
	public static void info0025() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0025"));
	}

	/**
	 * <li><b>INFO - 026</b></li>
	 * <p><i>Deseja realmente desativar a empresa executora? </i></p>
	 */
	public static void info0026() {
		JsfUtil.addWarnMessage(Util.getString("INF-0026"));
	}

	/**
	 * <li><b>INFO - 027</b></li>
	 * <p><i>Empresa executora desativada com sucesso. </i></p>
	 */
	public static void info0027() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0027"));
	}

	/**
	 * <li><b>INFO - 028</b></li>
	 * <p><i>Caro(a) Usuário(a), além do(s) município(s) já cadastrado(s), foi identificado que o shapefile se sobrepõe aos municípios de< <nomes dos municípios>>. Deseja que o sistema atualize a lista de municípios no cadastro do endereço do empreendimento? </i></p>
	 */
	public static void info0028() {
		JsfUtil.addWarnMessage(Util.getString("INF-0028"));
	}

	/**
	 * <li><b>INFO - 029</b></li>
	 * <p><i>Caro(a) Usuário(a), o shape inserido transpassa município não informado. Por favor, corrija a lista de municípios cadastrados no endereço do empreendimento. </i></p>
	 */
	public static void info0029() {
		JsfUtil.addWarnMessage(Util.getString("INF-0029"));
	}

	/**
	 * <li><b>INFO - 030</b></li>
	 * <p><i> RESERVARDO \u2013 AGUARDANDO ENVIO DA MENSAGEM.</i></p>
	 */

	/** Habilitar Esse Metodo quando a INF-30 for liberada
	 *
		public static void info0030() {
			JsfUtil.addWarnMessage(Util.getString("INF-0030"));
		}
	 * */


	/**
	 * <li><b>INFO - 031</b></li>
	 * <p><i>Para realizar a alteração da razão social é necessário que exista algum ato ambiental em vigor cujo processo esteja concluído. </i></p>
	 */
	public static void info0031() {
		JsfUtil.addWarnMessage(Util.getString("INF-0031"));
	}

	/**
	 * <li><b>INFO - 032</b></li>
	 * <p><i>Caro(a) Usuário(a), o shape inserido não contempla todos os municípios cadastrados no empreendimento. Por favor, revise a lista de municípios. </i></p>
	 */
	public static void info0032() {
		JsfUtil.addWarnMessage(Util.getString("INF-0032"));
	}

	/**
	 * <li><b>INFO - 033</b></li>
	 * <p><i>Número CAR inválido.</i></p>
	 */
	public static void info0033() {
		JsfUtil.addWarnMessage(Util.getString("INF-0033"));
	}

	/**
	 * <li><b>INFO - 034</b></li>
	 * <p><i>Entende-se como resíduo de ASV restos de material vegetal  não passível de destinação socioeconômica, resultante de autorização de supressão de vegetação nativa</i></p>
	 */
	public static void info0034() {
		JsfUtil.addWarnMessage(Util.getString("INF-0034"));
	}

	/**
	 * <li><b>INFO - 035</b></li>
	 * <p><i> Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.</i></p>
	 */
	public static void info0035() {
		JsfUtil.addWarnMessage(Util.getString("INF-0035"));
	}

	/**
	 * <li><b>INFO - 036</b></li>
	 * <p><i>Não é possível solicitar declaração de inexigibilidade para atividades de outorga em rios federais. </i></p>
	 */
	public static void info0036() {
		JsfUtil.addWarnMessage(Util.getString("INF-0036"));
	}

	/**
	 * <li><b>INFO - 037</b></li>
	 * <p><i>Para este valor de vazão, não se aplica a inexigibilidade de licenciamento prevista no decreto 14.389/2013. Será necessário solicitar o licenciamento ambiental do empreendimento.</i></p>
	 */
	public static void info0037() {
		JsfUtil.addWarnMessage(Util.getString("INF-0037"));
	}

	/**
	 * <li><b>INFO - 038</b></li>
	 * <p><i>O município informado não se encontra em estado de emergência decorrente de seca ou estiagem, sendo necessário solicitar o licenciamento para esta atividade.</i></p>
	 */
	public static void info0038() {
		JsfUtil.addWarnMessage(Util.getString("INF-0038"));
	}

	/**
	 * <li><b>INFO - 039</b></li>
	 * <p><i>De acordo com a Resolução CONERH 96/2014, será necessária a solicitação de outorga para esta atividade.</i></p>
	 */
	public static void info0039() {
		JsfUtil.addWarnMessage(Util.getString("INF-0039"));
	}

	/**
	 * <li><b>INFO - 040</b></li>
	 * <p><i>O imóvel informado já foi cadastrado.</i></p>
	 */
	public static void info0040() {
		JsfUtil.addErrorMessage(Util.getString("INF-0040"));
	}

	/**
	 * <li><b>INFO - 041</b></li>
	 * <p><i>O período inicial não pode ser maior que o final e o final menor que o inicial.</i></p>
	 */
	public static void info0041() {
		JsfUtil.addErrorMessage(Util.getString("INF-0041"));
	}

	/****************
	/*				*
	//XXX: ALERTAS  *
	/* 				*
	/****************/

	/**
	 * <li><b>ALE - 001</b></li>
	 * <p>
	 * <i>Inclusão realizada com sucesso!</i>
	 * </p>
	 */
	public static void alerta0001() {
		JsfUtil.addSuccessMessage(Util.getString("ALE-0001"));
	}

	/**
	 * <li><b>ALE - 002</b></li>
	 * <p><i>Favor, informar ao menos uma tipologia para alteração.</i></p>
	 */
	public static void alerta0002() {
		JsfUtil.addWarnMessage(Util.getString("ALE-0002"));
	}

	/**
	 * <li><b>ALE - 003</b></li>
	 * <p><i>Favor marcar uma opção no campo 3.</i></p>
	 */
	public static void alerta0003() {
		JsfUtil.addWarnMessage(Util.getString("ALE-0003"));
	}

	/**
	 * <li><b>ALE - 004</b></li>
	 * <p><i>Favor selecionar uma data no campo 3.1.</i></p>
	 */
	public static void alerta0004() {
		JsfUtil.addWarnMessage(Util.getString("ALE-0004"));
	}

	/**
	 * <li><b>ALE - 005</b></li>
	  * <p><i>Favor informar as atividades de licença.</i></p>
	 */
	public static void alerta0005() {
		JsfUtil.addWarnMessage(Util.getString("ALE-0005"));
	}

	/**
	 * <li><b>ALE - 006</b></li>
	 * <p><i>Favor informar o valor de todas as atividades de licença.</i></p>
	 */
	public static void alerta0006() {
		JsfUtil.addWarnMessage(Util.getString("ALE-0006"));
	}

	/**
	 * <li><b>ALE - 007</b></li>
	 * <p><i>Favor informar a opção para todas as atividades de licença.</i></p>
	 */
	public static void alerta0007() {
		JsfUtil.addWarnMessage(Util.getString("ALE-0007"));
	}

	/**
	 * <li><b>ALE - 008</b></li>
	 * <p><i>Por favor, inclua os dados do(s) processo(s) concluído(s) ou em trâmite.</i></p>
	 */
	public static void alerta0008() {
		JsfUtil.addWarnMessage(Util.getString("ALE-0008"));
	}

	/**
	 * <li><b>ALE - 009</b></li>
	 * <p><i>Selecione o(s) ato(s) ambiental(is) a ser(em) transferido(s).</i></p>
	 */
	public static void alerta0009() {
		JsfUtil.addWarnMessage(Util.getString("ALE-0008"));
	}

	/**
	 * <li><b>ALE - 010</b></li>
	 * <p><i>Coordenada informada está fora dos limites da Localidade do empreendimento.</i></p>
	 */
	public static void alerta0010() {
		JsfUtil.addWarnMessage(Util.getString("ALE-0010"));
	}

	/*************
	/*			 *
	//XXX: ERROS *
	/* 			 *
	/*************/

	/**
	 * <li><b>ERRO - 001</b></li>
	 * <p><i>Não será possível abrir um novo requerimento para este empreendimento, pois o mesmo já apresenta um requerimento em aberto aguardando a formação do processo. </i></p>
	 */
	public static void erro001() {
		JsfUtil.addErrorMessage(Util.getString("ERRO-0001"));
	}

	/**
	 * <li><b>ERRO - 002</b></li>
	 * <p><i>Para o cálculo do porte informe o valor das atividades corretamente. Não é permitido valor vazio ou menor ou igual a 0.</i></p>
	 */
	public static void erro002() {
		JsfUtil.addErrorMessage(Util.getString("ERRO-0002"));
	}

	/**
	 * <li><b>ERRO - 003</b></li>
	 * <p><i>Seus dados de [Localização Geográfica, Endereço, Imóvel e/ou Tipologia ] estão incompletos. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.</i></p>
	 */
	public static void erro003() {
		JsfUtil.addErrorMessage(Util.getString("ERRO-0003"));
	}

	/**
	 * <li><b>ERRO - 004</b></li>
	 * <p><i>Ocorreu um erro ao carregar [nome do campo].</i></p>
	 */
	public static void erro004() {
		JsfUtil.addErrorMessage(Util.getString("ERRO-0004"));
	}

	/**
	 * <li><b>ERRO - 005</b></li>
	 * <p><i>Tamanho deve estar entre 1 e 40.</i></p>
	 */
	public static void erro005() {
		JsfUtil.addErrorMessage(Util.getString("ERRO-0005"));
	}

	/**
	 * <li><b>ERRO - 006</b></li>
	 * <p><i>Existe saldo disponível no TCCA xxxx. Favor realizar o remanejamento desejado.</i></p>
	 */
	public static void erro006() {
		JsfUtil.addErrorMessage(Util.getString("ERRO-0006"));
	}

	/**
	 * <li><b>ERRO - 007</b></li>
	 * <p><i>Valor indicado superior ao saldo disponível</i></p>
	 */
	public static void erro007() {
		JsfUtil.addErrorMessage(Util.getString("ERRO-0007"));
	}

	/**
	 * <li><b>ERRO - 008</b></li>
	 * <p><i>Erro ao carregar formulário.</i></p>
	 */
	public static void erro008() {
		JsfUtil.addErrorMessage(Util.getString("ERRO-0008"));
	}
	

	/**
	 * <li><b>ERRO - 009</b></li>
	 * <p><i>Erro ao realizar a consulta.</i></p>
	 */
	public static void erro009() {
		JsfUtil.addErrorMessage(Util.getString("ERRO-0009"));
	}
	
	/**
	 * <li><b>ERRO - 010</b></li>
	 * <p><i>Arquivo não encontrado.</i></p>
	 */
	public static void erro010() {
		JsfUtil.addErrorMessage(Util.getString("ERRO-0010"));
	}
	
	

	/**
	 * Remover metodos daqui pra baixo quando possivel
	 */


	/**
	 * <li><b>MSG - 001</b></li>
	 * <p>
	 * <i>Inclusão realizada com sucesso!</i>
	 * </p>
	 */
	@Deprecated
	public static void exibirMensagem001() {
		JsfUtil.addSuccessMessage(Util.getString("MSG-001"));
	}

	/**
	 * <li><b>MSG - 002</b></li>
	 * <p>
	 * <i>Alteração realizada com sucesso!</i>
	 * </p>
	 */
	@Deprecated
	public static void exibirMensagem0002() {
		JsfUtil.addSuccessMessage(Util.getString("MSG-002"));
	}

	/**
	 * <li><b>MSG - 003</b></li>
	 * <p>
	 * <i>[nome do campo] é de preenchimento obrigatório.</i>
	 * </p>
	 */
	@Deprecated
	public static void exibirMensagem003(String nomeDoCampo) {
		JsfUtil.addErrorMessage(nomeDoCampo +" "+ Util.getString("messagem_003"));
	}

	/**
	 * <li><b>MSG - 004</b></li>
	 * <p>
	 * <i>Deseja realmente excluir o registro? </i>
	 * </p>
	 */
	@Deprecated
	public static void exibirMensagem004() {
		JsfUtil.addSuccessMessage(Util.getString("messagem_004"));
	}

	/**
	 * <li><b>MSG - 005</b></li>
	 * <p>
	 * <i>Exclusão realizada com sucesso.</i>
	 * </p>
	 */
	@Deprecated
	public static void exibirMensagem005() {
		JsfUtil.addSuccessMessage(Util.getString("messagem_005"));
	}

	/**
	 * <li><b>MSG - 010</b></li>
	 * <p>
	 * <i>Dados salvos com sucesso!</i>
	 * </p>
	 */
	@Deprecated
	public static void exibirMensagem010() {
		JsfUtil.addSuccessMessage(Util.getString("messagem_010"));
	}

	/**
	 *
	 * <li><b>ERRO - 008</b></li>
	 * <p>
	 * <i>Erro ao carregar formulário.</i>
	 * </p>
	 */
	@Deprecated
	public static void exibirErro008() {
		JsfUtil.addWarnMessage(Util.getString("erro_008"));
	}



	/**
	 * Mensagem extraída do documento de Mensagens do projeto LAC - FCE
	 *
	 * <li><b>INF - 001</b></li>
	 * <p>
	 * <i>Favor entrar em contato com o INEMA através do e-mail
	 * atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada
	 * seja realizado.</i>
	 * </p>
	 */
	public static void exibirInformacao001() {
		JsfUtil.addWarnMessage(Util.getString("msg_generica_cadastro_outros"));
	}

	public static void exibirInformacao002() {
		JsfUtil.addWarnMessage(Util.getString("msg_generica_cadastro_outros2"));
	}
	/**
	 *
	 * <li><b>INF - 033</b></li>
	 * <p>
	 * <i>Número CAR inválido.</i>
	 * </p>
	 */
	public static void exibirInformacao033() {
		JsfUtil.addWarnMessage(Util.getString("INF-0033"));
	}

	/**
	 *
	 * <li><b>INF - 040</b></li>
	 * <p>
	 * <i>O imóvel informado já foi cadastrado.</i>
	 * </p>
	 */
	public static void exibirInformacao040() {
		JsfUtil.addWarnMessage(Util.getString("INF-0040"));
	}


	/**
	 *
	 * <li><b>INF - 041</b></li>
	 * <p>
	 * <i>O período inicial não pode ser maior que o final e o final menor que o inicial.</i>
	 * </p>
	 */
	public static void exibirInformacao041() {
		JsfUtil.addSuccessMessage(Util.getString("INF-0041"));
	}

	public static void exibirErro007() {
		JsfUtil.addWarnMessage(Util.getString("ERRO-0007"));
	}

	public static void sucesso(String msg){
		exibir(TipoMensagemEnum.SUCESSO, msg);
	}
	public static void erro(String msg){
		exibir(TipoMensagemEnum.ERRO, msg);
	}

	public static void erro(List<String> msg){
		exibir(TipoMensagemEnum.ERRO, msg);
	}

	public static void alerta(List<String> msg){
		exibir(TipoMensagemEnum.ALERTA, msg);
	}
	public static void alerta(String msg){
		exibir(TipoMensagemEnum.ALERTA, msg);
	}

	public static void avisoOutros(){
		exibir(TipoMensagemEnum.ALERTA, Util.getString("msg_generica_cadastro_outros"));
	}

	private static void exibir(TipoMensagemEnum tipoMensagem, List<String> mensagens){
		for (String mensagem : mensagens) {
			exibir(tipoMensagem, mensagem);
		}
	}

	private static void exibir(TipoMensagemEnum tipoMensagem, String mensagem){
		if(tipoMensagem.equals(TipoMensagemEnum.SUCESSO)){
			JsfUtil.addSuccessMessage(mensagem);
		}

		else if(tipoMensagem.equals(TipoMensagemEnum.ERRO)){
			JsfUtil.addErrorMessage(mensagem);
		}

		else if(tipoMensagem.equals(TipoMensagemEnum.ALERTA)){
			JsfUtil.addWarnMessage(mensagem);
		}
	}

}