package br.gov.ba.seia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.exception.AppExceptionError;



public class FileUploadUtil {
	
	
	public static String getFileName(String fileName) {
		String retorno = null;
		if (!Util.isNull(fileName)) {
			if(fileName.contains(getFileSeparator())){
				retorno =  fileName.substring((fileName.lastIndexOf(getFileSeparator())+1),fileName.length());
			}else{
				retorno =  fileName.substring((fileName.lastIndexOf("/")+1),fileName.length());
			}
		}
		return retorno;
	}
	
	public static String Enviar(FileUploadEvent event, String caminho) {

		UploadedFile arq = event.getFile();
		InputStream in = null;
		File file = null;
		FileOutputStream out = null;
		String retornoCaminhoAbsoluto = "";
		
		try {
			in = arq.getInputstream();
			criarPasta(caminho);
			
			int pos = 0;
			String nomeArquivo = StringUtil.stripAccents(arq.getFileName());
			
			if(nomeArquivo.contains("\\")){
				for(int i = 0;i<nomeArquivo.length();i++){
					if(nomeArquivo.charAt(i)=='\\'){
						pos = i;
					}
				}
				nomeArquivo = nomeArquivo.substring(pos+1, nomeArquivo.length());
			}
			
			file = new File(novoNomeSeExiste(caminho + getFileSeparator()  + nomeArquivo));

			retornoCaminhoAbsoluto = file.getAbsolutePath();

			out = new FileOutputStream(file);

			moverArquivo(in, out);

		}
		catch (Exception e) {
			Log4jUtil.log(FileUploadUtil.class.getName(),Level.ERROR, e);
		}
		finally {
			file = null;
			finalizarIO(in, out);
		}
		
		return retornoCaminhoAbsoluto;
	}
	
	
	private static void finalizarIO(InputStream in, FileOutputStream out) {
		try{
			if(!Util.isNull(in)){
				in.close();
				in = null;
			}
			
			if(!Util.isNull(out)){
				out.close();
				out = null;
			}
		 }
		 catch(Exception e) {
			 Log4jUtil.log(FileUploadUtil.class.getName(),Level.ERROR, e);
		 }
	}

	
	/**
	 * Realiza o upload dos arquivos de ShapeFile e retorna o caminho em que o arquivo foi salvo.
	 * @param event
	 * @param caminho
	 * @param nomeDoArquivo
	 * @return String
	 */
	public static String EnviarShape(FileUploadEvent event, String caminho, String nomeDoArquivo) {

		UploadedFile arq = event.getFile();
		InputStream in = null;
		FileOutputStream out = null;
		File file = null;
		String retornoCaminhoAbsolutoDoArquivo = "";
		try {

			in = arq.getInputstream();
			criarPasta(caminho);
			
			nomeDoArquivo = nomeDoArquivo+"."+arq.getFileName().split("\\.")[1].toLowerCase();
			
			final String CAMINHO_ARQUIVO = caminho + getFileSeparator() + nomeDoArquivo;
			
            file = new File(CAMINHO_ARQUIVO);
            
            // Implementacaon da regra se ja existe arquivo renomea-lo com a
            // data e hora no final do nome e salvar o mais novo com o nome
            // correto pedido Silvio
            
            if (file.exists() && !isShapeTemporario(caminho)) {
                
            	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                
                String dateTime = "_" + df.format(new Date());
                
                String[] arquivo = nomeDoArquivo.split("\\.");
                
                String CAMINHO_ARQUIVO_RENOMEADO = caminho + getFileSeparator() + arquivo[0] + dateTime +"."+ arquivo[1];
                
                File fileAntigoRenomeado = new File(CAMINHO_ARQUIVO_RENOMEADO);
                
                file.renameTo(fileAntigoRenomeado);                
                file = new File(CAMINHO_ARQUIVO);
            }

            retornoCaminhoAbsolutoDoArquivo = file.getAbsolutePath();

			out = new FileOutputStream(file);

			moverArquivo(in, out);
		}
		catch (Exception e) {
			Log4jUtil.log(FileUploadUtil.class.getName(),Level.ERROR, e);
		}
		finally {
			file = null;
			finalizarIO(in, out);
		}
		return retornoCaminhoAbsolutoDoArquivo;
	}
	
	/**
	 * Realiza o upload de arquivo e retorna o caminho em que o arquivo foi salvo.
	 * @param uploadedFile
	 * @param caminho
	 * @param nomeDoArquivo
	 * @return String
	 */
	public static String EnviarArquivo(UploadedFile uploadedFile, String caminho, String nomeDoArquivo) {

		UploadedFile arq = uploadedFile;
		InputStream in = null;
		FileOutputStream out = null;
		File file = null;
		String retornoCaminhoAbsolutoDoArquivo = "";
		try {

			in = arq.getInputstream();
			criarPasta(caminho);
			
			nomeDoArquivo = nomeDoArquivo+"."+arq.getFileName().split("\\.")[1].toLowerCase();
			
			final String CAMINHO_ARQUIVO = caminho + getFileSeparator() + nomeDoArquivo;
			
            file = new File(CAMINHO_ARQUIVO);
            
            // Implementacaon da regra se ja existe arquivo renomea-lo com a
            // data e hora no final do nome e salvar o mais novo com o nome
            // correto pedido Silvio
            
            if (file.exists() && !isShapeTemporario(caminho)) {
                
            	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                
                String dateTime = "_" + df.format(new Date());
                
                String[] arquivo = nomeDoArquivo.split("\\.");
                
                String CAMINHO_ARQUIVO_RENOMEADO = caminho + getFileSeparator() + arquivo[0] + dateTime +"."+ arquivo[1];
                
                File fileAntigoRenomeado = new File(CAMINHO_ARQUIVO_RENOMEADO);
                
                file.renameTo(fileAntigoRenomeado);                
                file = new File(CAMINHO_ARQUIVO);
            }

            retornoCaminhoAbsolutoDoArquivo = file.getAbsolutePath();

			out = new FileOutputStream(file);

			moverArquivo(in, out);
		}
		catch (Exception e) {
			Log4jUtil.log(FileUploadUtil.class.getName(),Level.ERROR, e);
		}
		finally {
			file = null;
			finalizarIO(in, out);
		}
		return retornoCaminhoAbsolutoDoArquivo;
	}
	
	private static void criarPasta(String folder) {
		File file = new File(folder);
		if(!file.exists()) {
			file.mkdirs();
	
		}
		file = null;
	}

	public static void deletarArquivo(String diretorio) {
		File file = new File(diretorio);
		if (file.exists()) {
			if (!file.delete()) {
				file.deleteOnExit();
			}
		}
		file = null;
	}
	
	private static String novoNomeSeExiste(String arquivo) {		
		int contador = 0;
		String nome = arquivo;
		File file = null;
		while(true) {
			file = new File(nome);
			if(file.exists()) {
				contador++;
				nome = arquivo.substring(0,arquivo.lastIndexOf(".")) +"-"+contador+ arquivo.substring(arquivo.lastIndexOf("."),arquivo.length());
			}
			else {
				break;
			}
			file = null;
		}
		return nome;
	}
	
	public static String getFileSeparator(){
		if (IsOsLInux()) {
			return "/";
		}
		else {
			return "\\";
		}
	}
	
	private static Boolean IsOsLInux() {
		final String osName  = System.getProperty("os.name");
		
		if(osName.contains("unix") || osName.equals("Linux")){
			return true;
		}		
		return false;
	}
	
	public static void moverArquivoShapePastaTemporariaParaPastaOriginal(LocalizacaoGeografica localizacao, String nomePasta, String ideImovelRural) throws Exception {
		InputStream in = null;
		FileOutputStream out = null;

		try {
			String diretorioShapeTemp = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + ideImovelRural + "_" + nomePasta + File.separator;
			String diretorioShape = DiretorioArquivoEnum.SHAPEFILES.toString() + localizacao.getIdeLocalizacaoGeografica() + File.separator;
		
			criarPasta(diretorioShape);
		
			String arquivoTemp = diretorioShapeTemp + ideImovelRural + "_" + localizacao.getIdeSistemaCoordenada().getSrid();
			String arquivo = diretorioShape + localizacao.getIdeLocalizacaoGeografica() + "_" + localizacao.getIdeSistemaCoordenada().getSrid();
		
			String[] arrayArquivosTemp = {
					arquivoTemp + ".shp", 
					arquivoTemp + ".dbf", 
					arquivoTemp + ".shx", 
					arquivoTemp + ".prj",
					arquivoTemp + ".json"
			};
		
			for (String caminhoArquivo : arrayArquivosTemp){
				File file = new File(caminhoArquivo);
				
				if(file.exists()){
					in = new FileInputStream(file);
					out = new FileOutputStream(new File(arquivo + "." + (FilenameUtils.getExtension(file.getName()))));
					moverArquivo(in, out);
					finalizarIO(in, out);
				}
			}
			
			removerArquivos(new File(diretorioShapeTemp));
		} catch (Exception e) {
			Log4jUtil.log(FileUploadUtil.class.getName(),Level.ERROR, e);
			throw new AppExceptionError("Falha ao mover arquivos.");
		} finally {
			finalizarIO(in, out);
		}
	}

	public static void moverArquivo(InputStream in, OutputStream out) throws Exception {
		while (in.available() != 0) {
			out.write(in.read());
		}
	}
	
	 private static boolean isShapeTemporario(String caminho) {
		 return caminho.contains("SHAPESTEMP");
	 }
	 
	 public static boolean removerArquivos(File f) { 
		 if (f.isDirectory()) { 
			 File[] files = f.listFiles(); 
			 for (File file : files) { 
				 boolean sucesso = removerArquivos(file);
				 if(!sucesso){
					 return false;
				 }
			 } 
		 }
		 boolean retorno = f.delete();
		 f = null;
		 return retorno;
	 }

	 public static void removerPastasPorPrefixo(String caminhoPasta, String prefixoPasta) {
		 File f = new File(caminhoPasta);
		 File[] files = f.listFiles(); 
		 
		 if (!Util.isNull(files)){
			 for (File file : files) { 
				 if(file.isDirectory() && file.getName().startsWith(prefixoPasta)) {
					 removerArquivos(file);
				 }
			 }
			 f = null;
			 files = null;
		 }
	 }
	 
	 public static String obterReferenciaEspacial(String caminhoArquivo) {
			Scanner scanner = null;
			String arquivo = null;
			File arquivoPRJ = null;
			try {
				arquivoPRJ = new File(caminhoArquivo);
				scanner = new Scanner(arquivoPRJ);
				arquivo = scanner.useDelimiter("\\Z").next();

			} catch (Exception e) {
				Log4jUtil.log(FileUploadUtil.class.getName(),Level.ERROR, e);
			} finally {
				if (scanner != null) {
					scanner.close();
				}
				arquivoPRJ = null;
			}
			return arquivo;
	}
	 
	 /**
	  * Moves os shapes salvos na Pasta ShepesTemp para a past Shapes 	 
	  * @param localizacao
	  * @param nomePasta
	  * @param ideImovelRural
	  * @throws Exception
	  */
		public static void moverArquivoShapePastaTemporariaParaPastaOriginalCefir(LocalizacaoGeografica localizacao, String nomePasta, String ideImovelRural) throws Exception {
			 
			 File fileSHP = null;
			 File fileSHX = null;
			 File fileDBF = null;
			 File fileOriginalSHP = null;
			 File fileOriginalSHX = null;
			 File fileOriginalDBF = null;
			 InputStream in = null;
			 FileOutputStream out = null;
			 
		     try{
		 		String pathname = DiretorioArquivoEnum.SHAPEFILESTEMP.toString()+ideImovelRural+"_"+nomePasta;
		 		File diretorioExcluir = new File(pathname);
		 		
		 		String caminhoArquivoTemporario = pathname + File.separator;
		 		criarPasta(caminhoArquivoTemporario);
				
				
		 		String sridApp = localizacao.getIdeSistemaCoordenada().getSrid();
		 		String nomeArquivoTemporario = ideImovelRural+"_"+sridApp;
				
				String caminhoArquivoOriginal = DiretorioArquivoEnum.SHAPEFILES.toString() + getFileSeparator() + localizacao.getIdeLocalizacaoGeografica() + getFileSeparator();
				
				criarPasta(caminhoArquivoOriginal);
				
				String nomeArquivoOriginal = localizacao.getIdeLocalizacaoGeografica()+"_"+sridApp;
				
				fileSHP = new File(caminhoArquivoTemporario+nomeArquivoTemporario+".shp");
				fileSHX = new File(caminhoArquivoTemporario+nomeArquivoTemporario+".shx");
				fileDBF = new File(caminhoArquivoTemporario+nomeArquivoTemporario+".dbf");
				
				fileOriginalSHP = new File(caminhoArquivoOriginal+nomeArquivoOriginal+".shp");
				fileOriginalSHX = new File(caminhoArquivoOriginal+nomeArquivoOriginal+".shx");
				fileOriginalDBF = new File(caminhoArquivoOriginal+nomeArquivoOriginal+".dbf");
				
				
				 // Implementacão da regra se ja existe arquivo renomea-lo conforme a regra existente em empreendimento(adcição data e hora no final do nome)
				// e salvar o mais novo com o nome correto.
				//pedido de Kelson
	            renomearArquivoNaPastaDestino(fileOriginalSHP,caminhoArquivoOriginal,nomeArquivoOriginal+".shp");
				
				in = new FileInputStream(fileSHP);
		        out = new FileOutputStream(fileOriginalSHP);
		        
		        moverArquivo(in, out);
		        
		        renomearArquivoNaPastaDestino(fileOriginalSHX,caminhoArquivoOriginal,nomeArquivoOriginal+".shx");
		        
		        in = new FileInputStream(fileSHX);
		        out = new FileOutputStream(fileOriginalSHX);
		        moverArquivo(in, out);
		        
		        renomearArquivoNaPastaDestino(fileOriginalDBF,caminhoArquivoOriginal,nomeArquivoOriginal+".dbf");
		        
		        
		        in = new FileInputStream(fileDBF);
		        out = new FileOutputStream(fileOriginalDBF);
		        moverArquivo(in, out);
		        
		        removerArquivos(diretorioExcluir);
			 }
			 catch(Exception e) {
				 Log4jUtil.log(FileUploadUtil.class.getName(),Level.ERROR, e);
				 throw new AppExceptionError("Falha ao mover arquivos.");
			 }
			 finally {
				 fileSHP = null;
				 fileSHX = null;
				 fileDBF = null;
				 fileOriginalSHP = null;
				 fileOriginalSHX = null;
				 fileOriginalDBF = null;
				 finalizarIO(in, out);
			 }
		}
		
		
		private static void renomearArquivoNaPastaDestino(File file,String caminho, String nomeDoArquivo) {
			if (file.exists() && isShapePastaDestino(caminho)) {
			    
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			    
			    String dateTime = "_" + df.format(new Date());
			    
			    String[] arquivo = nomeDoArquivo.split("\\.");
			    
			    String CAMINHO_ARQUIVO_RENOMEADO = caminho + getFileSeparator() + arquivo[0] + dateTime +"."+ arquivo[1];
			    
			    File fileAntigoRenomeado = new File(CAMINHO_ARQUIVO_RENOMEADO);
			    
			    file.renameTo(fileAntigoRenomeado);                
			}
		}
		
	public static void renomearArquivoShape(String caminhoArquivo, String nome, String nomeNovo) throws AppExceptionError {
		String[] arrayArquivosTemp = {
				caminhoArquivo + nome + ".shp", 
				caminhoArquivo + nome + ".dbf",
				caminhoArquivo + nome + ".shx", 
				caminhoArquivo + nome + ".prj",
				caminhoArquivo + nome + ".json"
		};
		InputStream in = null;
		FileOutputStream out = null;
	
		try {
			for (String arquivo : arrayArquivosTemp){
				File file = new File(arquivo);
				
				if(file.exists()){
					in = new FileInputStream(file);
					out = new FileOutputStream(new File(caminhoArquivo + nomeNovo + "." + (FilenameUtils.getExtension(file.getName()))));
					moverArquivo(in, out);
					finalizarIO(in, out);
					file.delete();
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(FileUploadUtil.class.getName(),Level.ERROR, e);
			throw new AppExceptionError("Falha ao renomear arquivos.");
		} finally {
			finalizarIO(in, out);
		}
	}
	private static boolean isShapePastaDestino(String caminho) {
		return caminho.contains("SHAPES");
	}
}
