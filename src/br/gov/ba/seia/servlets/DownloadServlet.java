package br.gov.ba.seia.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypeException;

import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private String obterContentType(InputStream inputStream) throws MimeTypeException, IOException{
		TikaConfig config = TikaConfig.getDefaultConfig();
		MediaType mediaType = config.getMimeRepository().detect(inputStream, new Metadata());
		return mediaType.getType();
	}
	
	private void validarParametro(InputStream inputStream, String nomeArquivo) throws SeiaException{

		if (Util.isNullOuVazio(inputStream)){
			throw new SeiaException("Sessão \"arquivo\" não criada.");
		}
		
		if (Util.isNullOuVazio(nomeArquivo)){
			throw new SeiaException("Sessão \"nomeArquivo\" não criada.");
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {

			InputStream inputStream = (InputStream) request.getSession().getAttribute("arquivo");
			String nomeArquivo = (String) request.getSession().getAttribute("nomeArquivo");
			
			validarParametro(inputStream, nomeArquivo);
			
			request.getSession().removeAttribute("arquivo");
			request.getSession().removeAttribute("nomeArquivo");
			response.reset();
			
			response.setContentType(obterContentType(inputStream));
			
			response.setHeader("Content-Disposition", "attachment;filename=".concat(nomeArquivo));
			
			OutputStream out = response.getOutputStream();
		//	out.write(IOUtils.toByteArray(inputStream));
	
			int nRead;
			byte[] data = new byte[31384];
	
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				out.write(data, 0, nRead);
			}
			
			out.close();
			
		} catch (MimeTypeException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} catch (SeiaException e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
}
