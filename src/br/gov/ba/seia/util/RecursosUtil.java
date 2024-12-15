package br.gov.ba.seia.util;

import java.io.File;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.gov.ba.seia.enumerator.TipoArquivoEnum;

public class RecursosUtil {
	
	private static final String BARRA = File.separator;
	
	public static String resolverCaminhoRecurso(String recurso){
		return 
			(new File(((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getServletContext().getRealPath(recurso))).getAbsoluteFile().toString();
	}

	
	public static String retornaCaminho(TipoArquivoEnum tipoArquivo, String arquivo){
		String url = BARRA;
		
		if(tipoArquivo == TipoArquivoEnum.IMAGEM){
			url += Uri.URL_IMAGEM + BARRA + arquivo;
		}else if(tipoArquivo == TipoArquivoEnum.RELATORIO){
			url += Uri.URL_RELATORIO_PADRAO ;
		}
		
		return resolverCaminhoRecurso(url);
		
	}
}
	
