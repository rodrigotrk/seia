package br.gov.ba.seia.util.auditoria;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.hibernate.LazyInitializationException;

import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.interfaces.Auditoria;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RegexUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityService;
/**
 * Classe utilitaria de auditoria
 * @author 
 *
 */
public class AuditoriaUtil {

	public static <T extends Object> String obterNomeTabelaPorEntidade(final T obj) {
		Table annotation = obj.getClass().getAnnotation(Table.class);
		if (!Util.isNull(annotation)) {
			return annotation.name();
		}
		return "";
	}
	/**
	 * 
	 * @param <T>
	 * @param objAntigo
	 * @param objNovo
	 * @return changes
	 */

	public static <T extends Object> Map<String, String> compararObjetosObterValoresNovos(final T objAntigo, final T objNovo, boolean removerDaLista) {
		Map<String, String> changes = new HashMap<String, String>();
		final Field[] fieldsObjetoAntigo = objAntigo.getClass().getDeclaredFields();
		Object object = null;
		for (Field fieldObjetoAntigo : fieldsObjetoAntigo) {
			try {
				fieldObjetoAntigo.setAccessible(true);
				object = null;
				if (!Util.isNullOuVazio(fieldObjetoAntigo.get(objAntigo))) {
					if (!fieldObjetoAntigo.get(objAntigo).equals(fieldObjetoAntigo.get(objNovo))) {
						object = fieldObjetoAntigo.get(objNovo);
						if (!Util.isNullOuVazio(object)) {
							if(object.getClass().getAnnotation(Transient.class) != null) {
								continue;
							}
							if (object instanceof Collection) {
								List<String> lCollection = tratarEntidadesAndColecoes(fieldObjetoAntigo.get(objAntigo), object, removerDaLista);
								if(!lCollection.isEmpty()) {
									changes.put(fieldObjetoAntigo.getName(), lCollection.toString());
								}
								continue;
							}else if(object instanceof Date) {
								changes.put(fieldObjetoAntigo.getName(), converteData(object.toString()));
								continue;
							}
						} else {
							if(object != null && object instanceof Integer) {
								changes.put(fieldObjetoAntigo.getName(), object.toString());
								continue;
							}
							changes.put(fieldObjetoAntigo.getName(), "null");
							continue;
						}
						changes.put(fieldObjetoAntigo.getName(), object.toString());
					}
				}else if (!Util.isNullOuVazio(fieldObjetoAntigo.get(objNovo))) {
					changes.put(fieldObjetoAntigo.getName(), fieldObjetoAntigo.get(objNovo).toString());
				}
				
			} catch (IllegalArgumentException e) {
				Log4jUtil.log(AuditoriaUtil.class.getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			} catch (IllegalAccessException e) {
					Log4jUtil.log(AuditoriaUtil.class.getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}catch (LazyInitializationException e) {
				System.out.println(e.getMessage());
			}catch (ParseException e) {
				System.out.println(e.getMessage());
			}
		}
				
		return changes;
	}
	/**
	 * 
	 * @param <T>
	 * @param objAntigo
	 * @param objNovo
	 * @return changes
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 */

	public static <T extends Object> Map<String, String> compararObjetosObterValoresNovos2(final T objAntigo, final T objNovo, boolean removerDaLista) throws IllegalArgumentException, IllegalAccessException, ParseException {
		final Field[] fieldsObjetoAntigo = objAntigo.getClass().getDeclaredFields();
		Map<String, String> changes = new HashMap<String, String>();
		Object object = null;
		for (Field fieldObjetoAntigo : fieldsObjetoAntigo) {
			fieldObjetoAntigo.setAccessible(true);
			object = null;
			
			if (!Util.isNullOuVazio(fieldObjetoAntigo.get(objAntigo))) {
				if (!fieldObjetoAntigo.get(objAntigo).equals(fieldObjetoAntigo.get(objNovo))) {
					object = fieldObjetoAntigo.get(objNovo);
					if (!Util.isNullOuVazio(object)) {
						if(object.getClass().getAnnotation(Transient.class) != null) {
							continue;
						}
						if (object instanceof Collection) {
							List<String> lCollection = tratarEntidadesAndColecoes(fieldObjetoAntigo.get(objAntigo), object, removerDaLista);
							if(!lCollection.isEmpty()) {
								changes.put(fieldObjetoAntigo.getName(), lCollection.toString());
							}
							continue;
						}else if(object instanceof Date) {
							changes.put(fieldObjetoAntigo.getName(), converteData(object.toString()));
							continue;
						}
					} else {
						if(object != null && object instanceof Integer) {
							changes.put(fieldObjetoAntigo.getName(), object.toString());
							continue;
						}
						changes.put(fieldObjetoAntigo.getName(), "null");
						continue;
					}
					changes.put(fieldObjetoAntigo.getName(), object.toString());
				}
			}else if (!Util.isNullOuVazio(fieldObjetoAntigo.get(objNovo))) {
				changes.put(fieldObjetoAntigo.getName(), fieldObjetoAntigo.get(objNovo).toString());
			}
		}
		
		return changes;
	}
	/**
	 * 
	 * @param pAntigo
	 * @param pNovo
	 * @return
	 */
	private static List<String> tratarEntidadesAndColecoes(final Object pAntigo, final Object pNovo, boolean removerDaLista) {
		String[] idsObjNovo = null;
		String[] idsObjAntigo = null;
		List<String> lNovoClone = new ArrayList<String>();
		if(!Util.isNullOuVazio(pAntigo) && !Util.isNullOuVazio(pNovo)) {
			idsObjNovo = pNovo.toString().replaceAll("\\[", "").replaceAll("]", "").split("\\,");
			idsObjAntigo = pAntigo.toString().replaceAll("\\[", "").replaceAll("]", "").split("\\,");
		}
		if(!Util.isNullOuVazio(idsObjNovo) && !Util.isNullOuVazio(idsObjAntigo)) {
			List<String> lNovo = new ArrayList<String>(Arrays.asList(idsObjNovo));
			lNovoClone = new ArrayList<String>(Arrays.asList(idsObjNovo));
			List<String> lAntigo = new ArrayList<String>(Arrays.asList(idsObjAntigo));

			for (String antigo : lAntigo) {
				for (String novo : lNovo) {
					if(novo.trim().equals(antigo.trim()) && removerDaLista) {
						lNovoClone.remove(novo);
					}
				}
			}
		}
		return lNovoClone;
	}
	/**
	 * 
	 * @return
	 */
	public static Usuario obterUsuario() {
		return SecurityService.getUser();
	}
	
	public static String obterIp() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
		return request.getRemoteAddr();
	}
	
	public static String converteData(String possivelData) throws ParseException {
		if (RegexUtil.isDateUSA(possivelData)) {
			SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
			Date date = sdf.parse(possivelData);
			sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			return sdf.format(date);
		}else if(RegexUtil.isDateBR(possivelData)) {
			SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ROOT);
			Date date = sdf.parse(possivelData);
			sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			return sdf.format(date);
		}else if(RegexUtil.isDateNormalizada(possivelData)) {
			return possivelData;
		}
		return null;
	}
	
	public static String obterCaminhoPaginaRequisicao() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
		return request.getRequestURI();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T auditoria(T objeto, Object... objects) {
		
		
		if(objeto instanceof Auditoria){
			
			((Auditoria) objeto).capturarCamposAuditoria();
			
		}else if(objeto instanceof Auditoria[]){
			
			if(!Util.isNullOuVazio(objects)){
				
				((Map<String, Object>) objects[0]).put("idePessoaFisicaUsuario", AuditoriaUtil.obterUsuario().getIdePessoaFisica());
				((Map<String, Object>) objects[0]).put("caminhoRequisicao", AuditoriaUtil.obterCaminhoPaginaRequisicao());
				((Map<String, Object>) objects[0]).put("enderecoIp", AuditoriaUtil.obterIp());
			}
			
		}
		return objeto;
	}

}
