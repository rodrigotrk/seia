package br.gov.ba.seia.util;

import java.util.regex.Pattern;


public class  ExpressaoRegularUtil {

	/*FCE MINERACAO*/
	private final static String regexProcessoDnpm 	   = "^[0-9]{5}/[0-9]{4}$";	
	
	private final static String regexEmail 			   = "^[-_\\w]+(\\.[-_\\w]+)*@[\\w]+(\\.[\\w]+)*(\\.[A-Za-z]{2,})$";
	private final static String regexCPF 			   = "^(([0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2})|([0-9]{11}))$";
	private final static String regexNumSicar 		   = "^[A-Za-z0-9]{2}-[A-Za-z0-9]{7}-[A-Za-z0-9]{4}.[A-Za-z0-9]{4}.[A-Za-z0-9]{4}.[A-Za-z0-9]{4}.[A-Za-z0-9]{4}.[A-Za-z0-9]{4}.[A-Za-z0-9]{4}.[A-Za-z0-9]{4}$";

	/*REQUERIMENTO*/
	private final static String regexProcessoSeia 	   = "^[0-9]{4}.[0-9]{3}.[A-Za-z0-9]{6}/[A-Za-z]{5}/[a-zA-Z]{3}-[0-9]{1}[A-Za-z0-9]{4}$"  ;// SEIA: 	 2015.001.XXXXX/INEMA/LIC-0XXXX
	private final static String regexProcessoCeberus   = "^[0-9]{4}-[0-9]{1}[A-Za-z0-9]{5}/[A-Za-z]{3}/[A-Za-z]{2,3}-[0-9]{1}[A-Za-z0-9]{3}$" ;// CERBERUS:  2001-0XXXXX/TEC/LL-0XXX
	private final static String regexProcessoProHidros = "^[0-9]{4}-[0-9]{1}[A-Za-z0-9]{5}/[A-Za-z]{3}/[A-Za-z]{4,5}-[0-9]{1}[A-Za-z0-9]{3}$" ;// PROHIDROS: 2013-0XXXXX/OUT/APPO-0XXX 

	public final static String terminaComBarra = "^[\\w]*/$";
	
	public ExpressaoRegularUtil(){}
	
	/**
	 * @author Alexandre Queiroz
	 *  
	 * <h1> Validador de expressoes regulares </h1>
	 *  
	 * <br />
	 * @param valor que deseja verificar
	 * @param Expressao Regex 
	 * */
	public static Boolean validar(String campo, final String expressao){
		
		if(Util.isNullOuVazio(campo)){
			return false;
		}

		if(Util.isNullOuVazio(expressao)){
			return false;
		}
		
		return Pattern.compile(expressao).matcher(campo).matches();
	}
	
	public void terminaCom(){}
	
	public void iniciaCom(){}
	
	public void zeroOuMais(){}
	
	public static boolean matches(String texto, String regex){
		return regex.matches(texto);
	}
	
	public static boolean isProcessoSeiaOrCereberusOrProHidros(String numProcesso){	
		return numProcesso.matches(regexProcessoSeia) || numProcesso.matches(regexProcessoCeberus) || numProcesso.matches(regexProcessoProHidros);  
	}
	
	public static boolean isProcessoCereberusOrProHidros(String numProcesso){	
		return numProcesso.matches(regexProcessoCeberus) || numProcesso.matches(regexProcessoProHidros);  
	}
	
	public static boolean isProcessoDnpm(String numProcessoDnpm){
		return numProcessoDnpm.matches(numProcessoDnpm);
	}
	
	public static String getNumSicar() {
		return regexNumSicar;
	}
	
	/**
	 * Valor Esperado 
	 * <h1>
	 *	SEIA: 2015.001.XXXXX/INEMA/LIC-0XXXX
	 * </h1> 
	 * 
	 **/
	public static String getRegexProcessoSeia() {
		return regexProcessoSeia;
	}

	/**
	 * Valor Esperado 
	 * <h1>
	 *	CERBERUS:2001-0XXXXX/TEC/LL-0XXX
	 * </h1> 
	 * 
	 **/
	public static String getRegexProcessoCeberus() {
		return regexProcessoCeberus;
	}
	
	/**
	 * Valor Esperado 
	 * <h1>
	 *	PROHIDROS:2013-0XXXXX/OUT/APPO-0XXX 
	 * </h1> 
	 * 
	 **/
	public static String getRegexProcessoProHidros() {
		return regexProcessoProHidros;
	}

	/**
	 * Valor Esperado 
	 * <h1>
	 *	DNPM 84414/2015
	 * </h1> 
	 * 
	 **/
	public static String getRegexprocessodnpm() {
		return regexProcessoDnpm;
	}
	
	public static String getCpf() {
		return regexCPF;
	}
	
	/**
	 * @author Alexandre Queiroz
	 *  <h1> Expressão Regular para validar email </h1>
	 *  <br />
	 * 
	 *  <h1> Exemplos validos
	 * 
	 *  <table border="1" cellpadding="0" cellspacing="0" style="border-collapse:collapse" bordercolor="#111111" width="85%" id="composição"> 
	 *  <thead>
	 * 		<tr>
	 * 			<th>#</th>
	 * 			<th>Expressão</th>
	 * 		</tr>
	 * </thead>
	 * <tr>
	 * 		<td>01</td>
	 * 		<td>mail@mail.com</td>
	 * </tr>
	 * <tr>
	 * 		<td>02</td>
	 * 		<td>mail@mail.com.br</td>
	 * </tr>
	 * <tr>
	 * 		<td>02</td>
	 * 		<td>mail.mail@mail.br</td>
	 * </tr>
	 * </table>
	 *  <br />
	 * 
	 *  <h1> Exemplos NÃO validos </h1>
	 * 
	 *  <table border="1" cellpadding="0" cellspacing="0" style="border-collapse:collapse" bordercolor="#111111" width="85%" id="composição"> 
	 * <thead>
	 * 		<tr>
	 * 			<th>#</th>
	 * 			<th>Expressão</th>
	 * 			<th>Descrição</th>
	 * 		</tr>
	 * </thead>
	 * <tr>
	 * 		<td>01</td>
	 * 		<td>mail.@mail.com</td>
	 * 		<td>O carter que antece o '@' não pode ser um ponto</td>
	 * </tr>
	 * <tr>
	 * 		<td>02</td>
	 * 		<td>mail@.mail</td>
	 * 		<td>O carter que sucede o '@' não pode ser um ponto</td>
	 * </tr>
	 * <tr>
	 * 		<td>03</td>
     *		<td>mail.mail</td>
	 * 		<td>Não pode Haver Ausencia do Caratere @</td>
	 * </tr>
	 * </table>
	 * 
	 * 
	 * <h1>Existem muitos outros casos não validos para citar...</h1>
	 * */

	public static String getRegexEmail() {
		return regexEmail;
	}
}
