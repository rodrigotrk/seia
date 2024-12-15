package br.gov.ba.seia.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;

import br.gov.ba.seia.service.DownloadPortariaService;
import br.gov.ba.seia.util.Log4jUtil;

public class DownloadPortariaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final String PATH = "/tmp/portarias";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DownloadPortariaService downloadPortatiaService = DownloadPortariaService.getInstance();

		List<Object[]> listaArquivoPortaria = null;

		try {
			listaArquivoPortaria = downloadPortatiaService.getListaArquivoPortaria();
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}

		if (listaArquivoPortaria != null) {
			try {
				criarDiretorioTemporario();
				copiarArquivos(listaArquivoPortaria);
				compactarArquivos();
				iniciarDownload(response);
				deletarDiretorioTemporario();
			} 
			catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}

	private void criarDiretorioTemporario() throws Exception {
		File file = new File(PATH);
		file.mkdirs();
	}
	
	private void deletarDiretorioTemporario() throws Exception {
		rm(new File(PATH));
		rm(new File(PATH+".zip"));
	}
	
	private boolean rm(File file) throws Exception {
		if(file.isDirectory()) {
			String[] children = file.list();
			for(int i=0 ; i < children.length; i++) {
				boolean returno = rm(new File(file,children[i]));
				if(!returno) {
					return false;
				}
			}
		}		
		return file.delete();			
	}
	

	private void iniciarDownload(HttpServletResponse response) throws IOException {
		InputStream inputStream = new FileInputStream(PATH.substring(0, PATH.length()) + ".zip");
		response.reset();
		OutputStream out = response.getOutputStream();
		out.write(IOUtils.toByteArray(inputStream));
		out.close();
	}

	private void copiarArquivos(List<Object[]> listaArquivoPortaria) throws IOException {


		for (Object[] arquivo : listaArquivoPortaria) {
			
			String numProcesso = ((String) arquivo[0]).replace("/", "-");
			String caminhoArquivoOriginal = (String) arquivo[1];
			String nomeArquivoOriginal = caminhoArquivoOriginal.substring(caminhoArquivoOriginal.lastIndexOf("/") + 1,	caminhoArquivoOriginal.length());
			
			InputStream inputStream = new FileInputStream(caminhoArquivoOriginal);
			
			String diretorioDestino = PATH + "/" + numProcesso;
			
			if(new File(diretorioDestino).exists() == false) {
				new File(diretorioDestino).mkdir();
			}
			
			OutputStream outputStream = new FileOutputStream(diretorioDestino + "/" + nomeArquivoOriginal);
			outputStream.write(IOUtils.toByteArray(inputStream));
			outputStream.close();
		}
	}

	private void compactarArquivos() throws FileNotFoundException, IOException {

		final int BUFFER = 2048;

		BufferedInputStream origin = null;
		FileOutputStream dest = new FileOutputStream(PATH.substring(0,PATH.length()) + ".zip");
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
		// out.setMethod(ZipOutputStream.DEFLATED);
		byte data[] = new byte[BUFFER];
		// get a list of files from current directory
		File f = new File(PATH);
		String pastas[] = f.list();

		for (int j = 0; j < pastas.length; j++) {
			
			String files[] = new File(PATH + "/" + pastas[j]).list();
			
			for(int i=0; i < files.length; i++){
				
				String caminhoArquivo = PATH + "/" + pastas[j] + "/" + files[i];
				
				if (new File(caminhoArquivo).exists()) {
					System.out.println("Adding: " + caminhoArquivo);
					FileInputStream fi = new FileInputStream(caminhoArquivo);
					origin = new BufferedInputStream(fi, BUFFER);
					ZipEntry entry = new ZipEntry(caminhoArquivo.replace(PATH, ""));
					out.putNextEntry(entry);
					int count;
					while ((count = origin.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					}
					origin.close();
				}
			}
		}
		out.close();
	}
}
