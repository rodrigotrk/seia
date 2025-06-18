package br.gov.ba.seia.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.MaskFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.hibernate.LazyInitializationException;
import org.joda.time.DateTimeComparator;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;

import br.gov.ba.seia.entity.EmpreendimentoTipologia;
import br.gov.ba.seia.entity.FceLocalizacaoGeografica;
import br.gov.ba.seia.entity.FcePesquisaMineralOrigemEnergia;
import br.gov.ba.seia.entity.GeoBahia;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ObjetivoRequerimentoLimpezaArea;
import br.gov.ba.seia.entity.ProcessoDnpmProspecao;
import br.gov.ba.seia.entity.ProspecaoDetalhamento;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.exception.SeiaRuntimeException;
import br.gov.ba.seia.util.auditoria.ZipFiles;

@SuppressWarnings("all")
public class Util {

	private final static Logger log = LoggerFactory.getLogger(Util.class); 
	
	public static final int SEIA_EXCEPTION = 1;
	public static final int APP_EXCEPTION = 2;

	public static final String LISTA_PESSOA_JURIDICA_SESSION = "listaPessoaJuridica";
	public static final String LISTA_RESPONSAVEL_TECNICO_SESSION = "listaResponsavelTecnico";
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	private static final String IV = "<6?Rt[ge<)?D++.,";
	
	public static final String CHAVEENCRIPITACAO = "ws$iI6(@BnH(+cwN";

	public static Boolean isNull(Object objeto) {
		return (objeto == null) ? Boolean.TRUE : Boolean.FALSE;
	}

	public static boolean canConvertToDouble(String str) {
		try{
			Double.parseDouble(str);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

	/**
	 * Função que realiza a soma de dois valores do tipo Double.
	 * @param val1 - primeiro Double.
	 * @param val2 - segundo Double.
	 * @param mathContext - o MathContext que será usado no cálculo.
	 * @return Retorna a soma dos Double's passados como parametro.
	 */
	public static Double somarDouble(Double val1, Double val2, MathContext mathContext) {
		return (new BigDecimal(val1,mathContext)).add(new BigDecimal(val2,mathContext)).doubleValue();
	}

	/**
	 * Função que realiza a soma de dois valores do tipo Double usando MathContext.DECIMAL32
	 * @return Retorna a soma dos Double's passados como parametro.
	 */
	public static Double somarDouble(Double val1, Double val2) {
		return (new BigDecimal(val1,MathContext.DECIMAL32)).add(new BigDecimal(val2,MathContext.DECIMAL32)).doubleValue();
	}

	public static Double subtrairDouble(Double val1, Double val2) {
		return (new BigDecimal(val1,MathContext.DECIMAL32)).subtract(new BigDecimal(val2,MathContext.DECIMAL32)).doubleValue();
	}

	public static boolean isInteiro(double valor) {
		return ((int)valor) == valor;
	}

	public static Boolean isEmptyString(String str) {
		return (!Util.isNull(str) && str.length() == 0) ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * A partir da Exception e tipo informado, encapsula a exception em
	 * uma SeiaRuntimeException ou RuntimeException
	 * @param e
	 * @param typeException
	 * @return
	 */
	public static RuntimeException capturarException(Throwable e, Integer typeException) {

		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}

		switch(typeException) {
			case SEIA_EXCEPTION:
				return new SeiaRuntimeException(e);
			default:
				return new RuntimeException(e);
		}
	}

	/**
	 * A partir da Exception e tipo informado, encapsula a exception e a menasgem informada em
	 * uma SeiaRuntimeException ou RuntimeException
	 * @param message
	 * @param e
	 * @param typeException
	 * @return
	 */
	public static RuntimeException capturarException(String message, Throwable e, Integer typeException) {

		log.error(message, e);
		
		switch (typeException) {
			case 1:
				return new SeiaRuntimeException(message, e);
			default:
				return new RuntimeException(message, e);
		}
	}

	public static boolean isNotNullAndTrue(Object pObjeto) {
		try {
			if (pObjeto == null) {
				return false;

			}
			return (Boolean) pObjeto;
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public static boolean isNullOuVazio(Object pObjeto) {

		try {

			if (pObjeto == null) {

				return true;

			} else if(pObjeto instanceof AbstractEntity){

				return ((AbstractEntity) pObjeto).getId() == null;

			} else if (pObjeto instanceof Object[]) {

				return ((Object[]) pObjeto).length <= 0;

			} else if (pObjeto instanceof Collection) {

				return ((Collection) pObjeto).isEmpty();

			} else if (pObjeto instanceof ArrayList) {

				return ((ArrayList) pObjeto).isEmpty();

			} else if(pObjeto instanceof List){

				return ((List) pObjeto).isEmpty();

			} else if (pObjeto instanceof String) {

				return ((String) pObjeto).trim().equals(StringUtils.EMPTY);

			} else if (pObjeto instanceof Integer) {

				return ((Integer) pObjeto).intValue() == 0;

			} else if (pObjeto instanceof Long) {

				return ((Long) pObjeto).longValue() == 0l;

			}  else if (pObjeto instanceof BigDecimal){

				return ((BigDecimal) pObjeto).compareTo(BigDecimal.ZERO) == 0;

			} else if (pObjeto instanceof Double) {

				return ((Double) pObjeto).doubleValue() == 0;

			} else if (pObjeto instanceof GeoBahia) {

				return ((GeoBahia) pObjeto).getGid() == null;

			} else if (pObjeto instanceof VwConsultaProcesso) {

				return ((VwConsultaProcesso) pObjeto).getIdeProcesso() == null;

			} else if(pObjeto instanceof FceLocalizacaoGeografica){

				return (((FceLocalizacaoGeografica) pObjeto).getIdeFce() == null
						&& ((FceLocalizacaoGeografica) pObjeto).getIdeLocalizacaoGeografica() == null);

			} else if(pObjeto instanceof EmpreendimentoTipologia){

				return ((EmpreendimentoTipologia) pObjeto).getEmpreendimentoTipologiaPK() == null;

			} else if (pObjeto instanceof ProcessoDnpmProspecao) {

				return ((ProcessoDnpmProspecao) pObjeto).getIdeProcessoDnpmProspecao() == null;

			} else if (pObjeto instanceof ProspecaoDetalhamento) {

				return ((ProspecaoDetalhamento) pObjeto).getIdeProspeccaoDetalhamento() == null;

			}

			/**
			 * A PARTIR DE AGORA DEVE-SE EXTENDER O AbstractEntity NA SUA ENTIDADE E NÃO ACRESCENTAR MAIS NOVOS CASOS AQUI.
			 * -EXCEÇÔES:
			 * -- DTO
			 * -- EMBEDDED ID
			 * -- OBJETOS SIMPLES
			 * -- ENTIDADES ONDE O 'GET ID' NÃO ESTEJA APONTANDO PARA O ID DA CLASSE
			 * -- REQUERIMENTO (SE VERIFICAR QUEBRA O SISTEMA POIS O METODO FOI USADO DE FORMA ERRADA AO LONGO DOS ANOS)
			 */

			return false;

		} catch (LazyInitializationException e) {
			return true;
		}
	}

	@Deprecated
	public static boolean isLazyInitExcepOuNull(Object pObjeto) {
		try {
			if (pObjeto == null) {
				return true;

			}else if (pObjeto instanceof Collection && ((Collection) pObjeto).isEmpty()) {
				return false;
			}else if(pObjeto.toString().equalsIgnoreCase("")){
				return false;
			} else {
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<ObjetivoRequerimentoLimpezaArea> clonaListaObjReqLimpArea(List<ObjetivoRequerimentoLimpezaArea> listReqImovel)throws CloneNotSupportedException {
		ArrayList lista = new ArrayList();
		for (ObjetivoRequerimentoLimpezaArea object : listReqImovel) {
			lista.add(object.clone());
		}
		return lista;
	}

	@Deprecated
	public static ArrayList<Object> clonaLista(List<Object> list)throws CloneNotSupportedException {
		ArrayList lista = new ArrayList();

		if(list!=null){
			for (Object object : lista) {
				if(object instanceof FcePesquisaMineralOrigemEnergia){
					FcePesquisaMineralOrigemEnergia fcePesquisaMineralOrigemEnergia = (FcePesquisaMineralOrigemEnergia) object;
	    			lista.add(fcePesquisaMineralOrigemEnergia.clone());
				}
			}
		}

		return lista;
	}


	public static String formatarNumero(String pValor) {

		String lNumeroFormatado = "";

		if (!isNullOuVazio(pValor.trim())) {

			for (int lIndice = 0; lIndice < pValor.length(); lIndice++) {

				if (Character.isDigit(pValor.charAt(lIndice)) || pValor.charAt(lIndice) == ',' || pValor.charAt(lIndice) == '.') {

					lNumeroFormatado = lNumeroFormatado + pValor.charAt(lIndice);
				}
			}
		}

		return lNumeroFormatado;
	}

	public static boolean validaEmail(String pEmail) {

		boolean lValidade = false;

		//OFICIAL REGEX E-MAIL VALIDATION
		//(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])
		//[\w\.-]*[a-zA-Z0-9_]@[\w\.-]*[a-zA-Z0-9]\.[a-zA-Z][a-zA-Z\.]*[a-zA-Z]
		String lEr = "[a-zA-Z0-9]+[a-zA-Z0-9_.-]+@(?:[a-zA-Z0-9_-]+\\.)+[a-zA-Z]{2,4}";

		Pattern lPattern = Pattern.compile(lEr);
		Matcher lMatcher = lPattern.matcher(pEmail);

		if (!isNullOuVazio(pEmail) && lMatcher.find() && lMatcher.group().equals(pEmail)) {
			lValidade = true;
		}

		return lValidade;
	}


	public static HttpSession getSession() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession();
		return session;
	}

	public static void updateAttributeSession(String attributeId, Object attribute) {
		getSession().setAttribute(attributeId, attribute);
	}

	public static Object getAttributeSession(String attributeId) {
		return getSession().getAttribute(attributeId);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> DataModel<T> castListToDataModel(Collection lista) {
		return new ListDataModel((List<T>) lista);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> DualListModel<T> castListToDualListModel(Collection pLista1, Collection pLista2) {
		return new DualListModel((List<T>) pLista1, (List<T>) pLista2);
	}

	@SuppressWarnings("rawtypes")
	public static <T> Boolean isObjectInList(T objeto, Collection lista) {
		Boolean retorno = Boolean.FALSE;
		if (lista != null) {
			for (Object t : lista) {
				if (t.equals(objeto)) {
					retorno = Boolean.TRUE;
				}
			}
		}
		return retorno;
	}

	public static boolean validaPeriodo(final Date pDate1, final Date pDate2) {

		if (Util.isNullOuVazio(pDate1) && Util.isNullOuVazio(pDate2)) {
			return true;
		}
		if (Util.isNullOuVazio(pDate1) && !Util.isNullOuVazio(pDate2) || !Util.isNullOuVazio(pDate1) && Util.isNullOuVazio(pDate2)) {
			return false;
		}
		if ((pDate1.compareTo(pDate2) > 0)) {
			return false;
		}

		return true;
	}


	/**
	* Prefira a função lPad no classe, String
	**/
	@Deprecated
	public static String lpad(String input, char padding, int length) {

		if (input == null) {
			input = new String();
		}

		if (input.length() >= length) {
			return input;
		} else {
			StringBuilder result = new StringBuilder();
			int numChars = length - input.length();
			for (int i = 0; i < numChars; i++) {
				result.append(padding);
			}
			result.append(input);
			return result.toString();
		}
	}
	
	public static String rpad(String input, int length, char padChar) {
        if (input == null) {
            return null;
        }
        if (input.length() >= length) {
            return input;
        }
        StringBuilder paddedString = new StringBuilder(input);
        while (paddedString.length() < length) {
            paddedString.append(padChar);
        }
        return paddedString.toString();
    }

	public static String zeroLTrim(String substring) {
		if (!substring.isEmpty() && substring.length() > 1) {
			while (substring.startsWith("0")) {
				substring = substring.substring(1, substring.length());
			}
		}
		return substring;
	}

	public static String toMD5(String password) throws Exception {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e);
		}
		BigInteger hash = new BigInteger(1, md.digest(password.getBytes()));
		String s = hash.toString(16);
		if (s.length() %2 != 0) {
			s = "0" + s;
		}

		return s;
	}

	public static String getImageFilePath(String nome) {
		String fileName = "imagens/" + nome + ".png";
		File file = new File(fileName);
		fileName = file.getAbsolutePath();
		if (file.exists()) {
			System.out.println("Using: " + fileName);
			return fileName;
		} else {
			System.out.println("File: " + fileName + " not Found!");
			fileName = "imagens/" + nome + ".png";
			file = new File(fileName);
			fileName = file.getAbsolutePath();
			if (file.exists()) {
				System.out.println("Using: " + fileName);
				return fileName;
			} else {
				System.out.println("File: " + fileName + " not Found!");
			}
		}
		return null;
	}

	public static enum enumStringContexto {
		PESSOA, EMPREENDIMENTO
	}

	public static String getReportFilePath(String nome) {
		String fileName = "reports/sources/";
		File file = new File(fileName);
		fileName = file.getAbsolutePath();
		if (file.exists()) {
			System.out.println("Using: " + fileName);
			return fileName;
		} else {
			System.out.println("File: " + fileName + " not Found!");
			fileName = "reports/sources/";
			file = new File(fileName);
			fileName = file.getAbsolutePath();
			if (file.exists()) {
				System.out.println("Using: " + fileName);
				return fileName;
			} else {
				System.out.println("File: " + fileName + " not Found!");
			}
		}
		return null;
	}

	public static Boolean assertEquals(Object object, Object expected) {
		if (isNullOuVazio(object) && !isNullOuVazio(expected)) {
			return false;
		}

		if (object == expected) {
			return true;
		} else {
			return false;
		}

	}

	public static Collection<Integer> stringToArrayInt(String valor) {
		StringTokenizer token = new StringTokenizer(valor, ",");
		Collection<Integer> ides = new ArrayList<Integer>();
		while (token.hasMoreElements()) {
			ides.add(Integer.valueOf(token.nextToken()));
		}
		return ides;
	}

	public static String arrayIntToString(List<Integer> ides) {
		StringBuilder valor = new StringBuilder();

		int limite = ides.size() -1;
		for (int i = 0; i < ides.size(); i++) {
			Integer id = ides.get(i);
			if(i != limite) {
				valor.append(id+",");
			} else {
				valor.append(id);
			}
		}

		return valor.toString();
	}

	public static <T> ArrayList<T> sigletonList(Collection<T> collection) {
		Collection<T> sigleton = new HashSet<T>(collection);
		return new ArrayList<T>(sigleton);
	}

	public static String formatData(Date data){
		return new SimpleDateFormat("dd/MM/yyyy").format(data);
	}

	public static String getStringAlfanumAleatoria(final int tam) {
		String stringAleatoria = "";

		final long milis = new java.util.GregorianCalendar().getTimeInMillis();
		final Random r = new Random(milis);

		int i = 0;
		while (i < tam) {
			final char c = (char) r.nextInt(255);
			if (((c >= '0') && (c <= '9')) || ((c >= 'a') && (c <= 'z'))
					|| ((c >= 'A') && (c <= 'Z'))) {
				stringAleatoria += c;
				i++;
			}
		}
		return stringAleatoria;
	}

	/**
	 * Valida CNPJ do usuário.
	 *
	 * @param cnpj String valor com 14 dígitos
	 */
	public static Boolean validaCNPJ(String cnpj) {

		if(cnpj == null) {
			return false;
		}

		if(cnpj.length() != 14){
			JsfUtil.addErrorMessage("CNPJ informado possui menos de 14 números.");
			return false;
		}

		try {
			Long.parseLong(cnpj);
		} catch (NumberFormatException e) { // CNPJ não possui somente números
			JsfUtil.addErrorMessage("O CNPJ deve possuir somente números.");
			return false;
		}

		int soma = 0;
		String cnpj_calc = cnpj.substring(0, 12);

		char chr_cnpj[] = cnpj.toCharArray();

		for(int i = 0; i < 4; i++) {
			if(chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
			}
		}

		for(int i = 0; i < 8; i++) {
			if(chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
			}
		}

		int dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();
		soma = 0;

		for(int i = 0; i < 5; i++) {
			if(chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
			}
		}

		for(int i = 0; i < 8; i++) {
			if(chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
			}
		}

		dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();

		if(!cnpj.equals(cnpj_calc) || cnpj.equals("00000000000000")){
			return false;
		}

		return true;
	}

	public static String converteNumToDscMeses(String pMeses) {
		String aux = "";

		for (String p : pMeses.split("-")) {
			if (p.contains("1")) {
				aux += "JAN, ";
			}
			if (p.contains("2")) {
				aux += "FEV, ";
			}
			if (p.contains("3")) {
				aux += "MAR, ";
			}
			if (p.contains("4")) {
				aux += "ABR, ";
			}
			if (p.contains("5")) {
				aux += "MAI, ";
			}
			if (p.contains("6")) {
				aux += "JUN, ";
			}
			if (p.contains("7")) {
				aux += "JUL, ";
			}
			if (p.contains("8")) {
				aux += "AGO, ";
			}
			if (p.contains("9")) {
				aux += "SET, ";
			}
			if (p.contains("10")) {
				aux += "OUT, ";
			}
			if (p.contains("11")) {
				aux += "NOV, ";
			}
			if (p.contains("12")) {
				aux += "DEZ, ";
			}
		}
		aux = aux.substring (0, aux.length()-2);
		return aux;
	}

	public static String converteNumToDscUsoAgua(Integer pUsoAgua) {
		switch (pUsoAgua) {
		case 1:
			return "Dessedentação";
		case 2:
			return "Limpeza";
		case 3:
			return "Dessedentação e Limpeza";
		default:
			return "Nenhum uso informado";
		}
	}

	public static boolean verificaDatas(String mesI, String anoI, String mesF, String anoF) {

		int mesInicial = Integer.parseInt(mesI.trim());
		int mesFinal = Integer.parseInt(mesF.trim());

		if(mesInicial < 1 || mesInicial > 12 || mesFinal < 1 || mesFinal > 12){
			JsfUtil.addErrorMessage("O campo Mês deve ter valores entre 1 e 12!");
			return false;
		}

		int anoInicial = Integer.parseInt(anoI.trim());
		int anoFinal = Integer.parseInt(anoF.trim());
		if(anoInicial > anoFinal) {
			JsfUtil.addErrorMessage("A data final deve ser maior que a data inicial");
			return false;
		}

		if(anoInicial == anoFinal && mesInicial > mesFinal) {
			JsfUtil.addErrorMessage("A data final deve ser maior que a data inicial");
			return false;
		}

		return true;
	}

	public static String getString(String key, Object... params  ) {
		try {
			return MessageFormat.format(BUNDLE.getString(key), params);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static Double formatarValor(String valor) {
		String valorAtividade = valor.replace(".", "").replace(",", ".");
		return Double.parseDouble(valorAtividade);
	}

	public static String replaceString(String str, String[] arrayStr, String... subst) {
		if (Util.isNull(subst) || subst.length < 1) {
			subst = new String[1];
			subst[0] = "";
		}
		for (String var : arrayStr) {
			str = str.replace(var, subst[0]);
		}
		return str;
	}

	/**
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return true se date1 < date2
	 * @Comentarios Verifica se a segunda data informada é superior a primeira
	 */
	public static boolean validarDuasDatas(Date date1, Date date2) {
		if (date1.compareTo(date2) < 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return true se date1 < date2
	 * @Comentarios Verifica se a primeira data informada é inferior a segunda
	 */
	public static boolean validarDuasDatasReposicao(Date date1, Date date2) {
		DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
		
		if (dateTimeComparator.compare(date1 ,date2) < 0) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * @return Data de Hoje (atual)
	 */
	public static String getDataHoje() {
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy");
		return formatarDate.format(data);
	}

	/**
	 *
	 * @return Data de validade maxima permitida na dialog prorrogação prazo de
	 *         validade
	 */
	public static String getDataMaxima() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, +60);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(calendar.getTime());
	}

	/**
	 *
	 * @param date
	 * @return boolean
	 * @Comentario Checa se a data informata é superior a 60 dias a partir de hoje
	 */
	public static boolean dataValidadeMaiorQueMaximoPermitido(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 60);
		if ((date.compareTo(calendar.getTime()) <= 0)) {
			return true;
		} else {
			JsfUtil.addWarnMessage("Data inválida!");
			return false;
		}
	}

	public static boolean validaTamanhoString(String string, int tamanho) {
		boolean valido = true;
		if (string.trim().length() > tamanho) {
			valido = false;
		}
		return valido;
	}

	public static String formatarCPF(String cpf){
		if(!cpf.contains(".")){
			return format("###.###.###-##", cpf);
		}
		return cpf;
	}

	public static String formatarCNPJ(String cnpj){
		if(!cnpj.contains(".")){
			return format("##.###.###/####-##", cnpj);
		}
		return cnpj;
	}
	/**
	 * Método que formata uma string de acordo com a máscara.
	 * Deve ser colocado na classe {@link Util} no futuro.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param pattern
	 * @param value
	 * @return
	 */
	public static String format(String pattern, Object value) {
		MaskFormatter mask;
		try {
			mask = new MaskFormatter(pattern);
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean verificaExistenciaArquivo(String caminhoArquivo) {

		try {
			File file = new File(caminhoArquivo);
			InputStream stream = new FileInputStream(file);
			StreamedContent sc = new DefaultStreamedContent(stream, URLConnection.guessContentTypeFromName(caminhoArquivo), FileUploadUtil.getFileName(caminhoArquivo));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Date formataData(String data) throws Exception {
		if (data == null || data.equals("")) {
			return null;
		}

		Date date = null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(data);
		} catch (ParseException e) {
			throw e;
		}
		return date;
	}

	public static Date formataDataComHora(String data) throws Exception {
		if (data == null || data.equals("") || data.contains("null")) {
			return null;
		}

		Date date = null;
		try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
			date = formatter.parse(data);
		} catch (ParseException e) {
			throw e;
		}
		return date;
	}


	public static Date calcularProximoDiaHoraFinal(Integer qtdDias) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, qtdDias);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	public static Date calcularProximoDiaHoraInicial(Integer qtdDias) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, qtdDias);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static String gerarChaveWs() throws Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return toMD5("SEIAMOBILE" + calendar.getTime().getTime());
	}

	public static Date removeTimeOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}


	/**
	 * Método para gerar um número <b>inteiro</b> dentro da faixa passada no parâmetro
	 * @author eduardo.fernandes
	 * @since 07/01/2015
	 * @param min
	 * @param max
	 * @return
	 */
	public static int geradorDeInteiros(int min, int max){
		Random gerador = new Random();
		int numero = gerador.nextInt(max+1);
		while (numero < min) {
			numero = gerador.nextInt(max+1);
		}
		return numero;
	}

	public static Double converterDoubleUmaCasa(final Double pNumero) throws Exception {
		if(Util.isNull(pNumero)) {
			throw new Exception("Valor a ser convertido está nulo");
		}
		
		BigDecimal lNumero = new BigDecimal(pNumero.toString());
		
		for (int scale = lNumero.scale()-1; scale > 0; scale--) {
			lNumero = lNumero.setScale(scale, RoundingMode.HALF_UP);
		}
		
		return lNumero.doubleValue();
	}

	public static Double converterDoubleDuasCasas(Double numero) throws ParseException {
		DecimalFormat a = new DecimalFormat("#.##");
		a.setRoundingMode(RoundingMode.FLOOR);
		String s = a.format(numero);

		NumberFormat nf = NumberFormat.getInstance();
		
		return nf.parse(s).doubleValue();
	}
	
	public static Double converterDoubleQuatroCasas(Double numero) throws ParseException {
		DecimalFormat a = new DecimalFormat("#.####");
		a.setRoundingMode(RoundingMode.HALF_UP);
		String s = a.format(numero);
		
		NumberFormat nf = NumberFormat.getInstance();
		
		return nf.parse(s).doubleValue();
	}
	
	public static String criarUrlShape(LocalizacaoGeografica lg) {
		String lUrl = URLEnum.CAMINHO_GEOBAHIA + "?acao=view&idloc=" + lg.getIdeLocalizacaoGeografica() + "&tipo=1";
		StringBuilder lReturn = new StringBuilder();
		lReturn.append("window.open('");
		lReturn.append(lUrl);
		lReturn.append("');");
		return lReturn.toString();
	}

	public static StringBuilder criarStreamComUrl(String pUrl) throws IOException {
		StringBuilder buffer = new StringBuilder();
		URL url = null;
		BufferedReader br = null;
		try {
			url = new URL(pUrl);
			br = new BufferedReader(new InputStreamReader(url.openStream()));

			String linha;
			while ((linha = br.readLine()) != null) {
				buffer.append(linha);
			}
		} catch (MalformedURLException e) {
			Log4jUtil.log(Util.class.getName(),Level.ERROR, e);
		} catch (IOException e) {
			Log4jUtil.log(Util.class.getName(),Level.ERROR, e);
		}finally{
			if(!Util.isNull(br)) {
				br.close();
			}
		}
		return buffer;
	}

	public static DecimalFormat getDecimalFormatPtBr() {
		return new DecimalFormat("#,###,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
	}

	public static Date adicionarUmDia(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DAY_OF_YEAR, 1);
		return gc.getTime();
	}

	public static Date adicionarDias(Date date, int dias) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DAY_OF_YEAR, dias);
		return gc.getTime();
	}
	
	public static Date adicionarMeses(Date date, int meses) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.MONTH, meses);
		return gc.getTime();
	}

	public static Date adicionarAnos(Date date, int anos) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.YEAR, anos);
		return gc.getTime();
	}

	/**
	 * Método responsável por criar um arquivo zip a partir de uma lista de {@link StreamedContent}.
	 *
	 * @author ivanildo.souza
	 * @since 06/10/16
	 * @param nome : Nome do arquivo Zip exportado.
	 * @throws IOException
	 */
	public static InputStream comprimirListaStreamedContentParaZip(List<StreamedContent> streamedContentList) throws IOException {
		byte[] byteArray = ZipFiles.comprimirStreamedContentListZip(streamedContentList).toByteArray();
		return new ByteArrayInputStream(byteArray);
	}

	/**
	 * Método para clonar uma lista e todo o seu conteúdo
	 */
	public static <T extends Cloneable> List<T> deepCloneList(Collection<T> listaOriginal) {
	    try {
	    	List<T> clonedList = new ArrayList<T>();
	    	if (!isNullOuVazio(listaOriginal)) {
	    		for(T tObject :listaOriginal) {
	    			Method cloneMethod = tObject.getClass().getDeclaredMethod("clone");
	    			clonedList.add((T) cloneMethod.invoke(tObject));
	    		}
	    	}
	    	return clonedList;
	    }
	    catch (Exception e) {
	        throw new RuntimeException("Couldn't clone list due to " + e.getMessage());
	    }
	}

	public static <T> List<T> castCollection(Object obj, Class<T> oClass) {
		List<T> result = new ArrayList<T>();
		if(obj instanceof Collection<?>) {
			for (Object o : (Collection<?>) obj) {
				result.add(oClass.cast(o));
			}
			return result;
		}
		throw new IllegalArgumentException("O parametro passado não é uma coleção");
	}
	
	public static <T> T objectTransform(Object obj, Class<T> oClass) throws InstantiationException, IllegalAccessException {
		Object newObj = oClass.newInstance();
		if(obj==null) {
			return null;
		}
		else {
			for(Field f1 : newObj.getClass().getDeclaredFields()) {
				f1.setAccessible(true);					
				for(Field f2 : obj.getClass().getDeclaredFields()) {
					f2.setAccessible(true);
					if(f1.getName().equals(f2.getName())) {
						if(f1.getType().equals(f2.getType())) {
							f1.set(newObj, f2.get(obj));
						}
						else {
							Object f1Obj = objectTransform(f2.get(obj), f1.getType());
							f1.set(newObj, f1Obj);
						}
					}
				}
			}
			return oClass.cast(newObj);
		}
	}
	
	public static String removerAcentos(String str) {
		if(StringUtils.isEmpty(str)){
			return StringUtils.EMPTY;
		}
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	public static String substituirCaracterEspecial(String string){
		return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	/**
	 * Método para clonar uma lista de Strings
	 */
	public static List<String> clonaListStrings(List<String> listaOriginal) {
	    try {
	    	if (listaOriginal == null || listaOriginal.size() < 1) {
	    		return new ArrayList<String>();
	    	}

	    	List<String>  clonedList = new ArrayList<String>();
	    	for(String tObject :listaOriginal) {
	    		clonedList.add(new String(tObject));
	    	}
	    	return clonedList;
	    }
	    catch (Exception e) {
	        System.err.println("Couldn't clone list due to " + e.getMessage());
	        return new ArrayList<String>();
	    }
	}

	public static AbstractEntity copiarEntity(AbstractEntity abstractEntity) throws Exception{
		AbstractEntity newAbstractEntity = abstractEntity.getClass().newInstance();

		for (Field field : abstractEntity.getClass().getDeclaredFields()) {
			Object value = abstractEntity.getClass().getMethod((field.getType().getName().equals(Boolean.class.getSimpleName().toLowerCase()) ? "is" : "get") + String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1)).invoke(abstractEntity);
			if(value != null){
				Method metodo = abstractEntity.getClass().getMethod("set" + String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1), field.getType());
				if(value instanceof List || value instanceof Collection){
					List<Object> list = new ArrayList<Object>();
					for(Object item : (List) value){
						list.add(item);
					}
					metodo.invoke(newAbstractEntity, list);
				}else{
					metodo.invoke(newAbstractEntity, value);
				}
			}
		}

		return newAbstractEntity;
	}

	public static String valorEmReaisParaExtenso(BigDecimal valor){
		return new Extenso(valor).toString();
	}
	
	public static byte[] encrypt(String textopuro, String chaveencriptacao)
			throws Exception {
		Cipher encripta = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
		SecretKeySpec key = new SecretKeySpec(
				chaveencriptacao.getBytes("UTF-8"), "AES");
		encripta.init(Cipher.ENCRYPT_MODE, key,
				new IvParameterSpec(IV.getBytes("UTF-8")));
		return encripta.doFinal(textopuro.getBytes("UTF-8"));
	}
	
	public static String decrypt(byte[] textoencriptado, String chaveencriptacao)
			 {
		Cipher decripta;
		
		try {
			decripta = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
			SecretKeySpec key = new SecretKeySpec(
					chaveencriptacao.getBytes("UTF-8"), "AES");
			decripta.init(Cipher.DECRYPT_MODE, key,
					new IvParameterSpec(IV.getBytes("UTF-8")));
			return new String(decripta.doFinal(textoencriptado), "UTF-8");
		} catch (Exception e) {
			
			Log4jUtil.log(Util.class.getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		} 
		
	}
	
	public static String encodeBase64(byte[] valor) {

		byte[] base64 = Base64.encode(valor);

		return new String(base64);
	}
	
	public static byte[] decodeBase64(String valor) {

		byte[] base64 = Base64.decode(valor.getBytes());

		return base64;
	}
	
	public static String encodeSHA256(String valor)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
		byte messageDigest[] = algorithm.digest(valor.getBytes("UTF-8"));

		StringBuilder hexString = new StringBuilder();
		for (byte b : messageDigest) {
			hexString.append(String.format("%02X", 0xFF & b));
		}

		return hexString.toString();
	}

	public static String formatarNumero(BigDecimal pValor){
		DecimalFormat df = Util.getDecimalFormatPtBr(); 
		return df.format(pValor);
	}
	
	/**
	 * Método para validacao de CPF
	 * @param CPF
	 * @return boolean
	 */
	public static boolean isCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
            CPF.equals("11111111111") ||
            CPF.equals("22222222222") || CPF.equals("33333333333") ||
            CPF.equals("44444444444") || CPF.equals("55555555555") ||
            CPF.equals("66666666666") || CPF.equals("77777777777") ||
            CPF.equals("88888888888") || CPF.equals("99999999999") ||
            (CPF.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
        // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
        // converte o i-esimo caractere do CPF em um numero:
        // por exemplo, transforma o caractere '0' no inteiro 0
        // (48 eh a posicao de '0' na tabela ASCII)
            num = (int)(CPF.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

        // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
            num = (int)(CPF.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                 dig11 = '0';
            else dig11 = (char)(r + 48);

        // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                 return(true);
            else return(false);
                } catch (InputMismatchException erro) {
                return(false);
            }
        }
	
	/**
	 * Metodo para validacao do CNPJ
	 * @param CNPJ
	 * @return boolean
	 */
	public static boolean isCNPJ(String CNPJ) {
		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		    if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
		        CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
		        CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
		        CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
		        CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
		       (CNPJ.length() != 14))
		       return(false);

		    char dig13, dig14;
		    int sm, i, r, num, peso;

		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		    try {
		// Calculo do 1o. Digito Verificador
		      sm = 0;
		      peso = 2;
		      for (i=11; i>=0; i--) {
		// converte o i-ésimo caractere do CNPJ em um número:
		// por exemplo, transforma o caractere '0' no inteiro 0
		// (48 eh a posição de '0' na tabela ASCII)
		        num = (int)(CNPJ.charAt(i) - 48);
		        sm = sm + (num * peso);
		        peso = peso + 1;
		        if (peso == 10)
		           peso = 2;
		      }

		      r = sm % 11;
		      if ((r == 0) || (r == 1))
		         dig13 = '0';
		      else dig13 = (char)((11-r) + 48);

		// Calculo do 2o. Digito Verificador
		      sm = 0;
		      peso = 2;
		      for (i=12; i>=0; i--) {
		        num = (int)(CNPJ.charAt(i)- 48);
		        sm = sm + (num * peso);
		        peso = peso + 1;
		        if (peso == 10)
		           peso = 2;
		      }

		      r = sm % 11;
		      if ((r == 0) || (r == 1))
		         dig14 = '0';
		      else dig14 = (char)((11-r) + 48);

		// Verifica se os dígitos calculados conferem com os dígitos informados.
		      if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
		         return(true);
		      else return(false);
		    } catch (InputMismatchException erro) {
		        return(false);
		    }
		  }
}