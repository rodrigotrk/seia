package br.gov.ba.seia.service;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.Stateless;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.DocumentoIdentificacaoRequerimento;
import br.gov.ba.seia.entity.DocumentoObrigatorioReenquadramento;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.DocumentoRepresentacaoRequerimento;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
public class GerenciaArquivoService {

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");

	public void uploadArquivo(FileUploadEvent event) throws Exception {
		ReadableByteChannel inChannel = abrirFluxoEntradaArquivo(event);
		ByteBuffer buffer = criarBuffer(event);
		criarDiretorio("15201");
		File arquivo = criarArquivoDiretorio(event, "15201");
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
	}

	public void uploadArquivoEmpreendimento(FileUploadEvent event, Empreendimento empreendimento) throws Exception {
		ReadableByteChannel inChannel = abrirFluxoEntradaArquivo(event);
		ByteBuffer buffer = criarBuffer(event);
		criarDiretorio(empreendimento.getIdeEmpreendimento().toString());
		File arquivo = criarArquivoDiretorio(event, empreendimento.getIdeEmpreendimento().toString());
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
	}



	public String uploadArquivoDocumentoObrigatorioRequerimento(
			DocumentoObrigatorioRequerimento docObrigatorioRequerimento) throws Exception {
		InputStream in = docObrigatorioRequerimento.getFile().getStream();
		File arquivo = this.criarArquivoDiretorio(docObrigatorioRequerimento.getFile().getName(),
				docObrigatorioRequerimento.getIdeDocumentoObrigatorioRequerimento(),
				DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.toString());
		FileOutputStream out = new FileOutputStream(arquivo);
		int b;
		while ((b = in.read()) > -1) {
			out.write(b);
		}
		in.close();
		out.close();
		return arquivo.getAbsolutePath();
	}

	public String uploadArquivoDocumentoIdentificacaoRequerimento(
			DocumentoIdentificacaoRequerimento docObrigatorioRequerimento) throws Exception {
		InputStream in = docObrigatorioRequerimento.getFile().getStream();
		File arquivo = this.criarArquivoDiretorio(docObrigatorioRequerimento.getFile().getName(),
				docObrigatorioRequerimento.getIdeDocumentoIdentificacaoRequerimento(),
				DiretorioArquivoEnum.DOCUMENTOIDENTIFICACAO.toString());
		FileOutputStream out = new FileOutputStream(arquivo);
		int b;
		while ((b = in.read()) > -1) {
			out.write(b);
		}
		in.close();
		out.close();
		return arquivo.getAbsolutePath();
	}
	
	public void deletarArquivo(String diretorio) {
		File file = new File(diretorio);
		if (file.exists()) {
			if (!file.delete()) {
				file.deleteOnExit();
			}
		}
	}

	public ReadableByteChannel abrirFluxoEntradaArquivo(FileUploadEvent event) throws Exception {
		UploadedFile file = event.getFile();
		return  Channels.newChannel(file.getInputstream());
	}

	public ByteBuffer criarBuffer(FileUploadEvent event) {
		return ByteBuffer.allocate((int) event.getFile().getSize());
		 
	}

	public String criarDiretorio(String requerimento) {
		File diretorio = new File(BUNDLE.getString("diretorio.arquivo") + requerimento);
		if (!diretorio.exists()) {
			diretorio.mkdir();
		}
		return diretorio.getAbsolutePath();
	}

	public File criarArquivoDiretorio(FileUploadEvent event, String requerimento) throws Exception {
		String nomeArquivo = event.getFile().getFileName();
		String tipoArquivo = nomeArquivo.substring(nomeArquivo.indexOf("."));
		File arquivo = new File(BUNDLE.getString("diretorio.arquivo") + requerimento + File.separator
				+ event.getComponent().getId() + tipoArquivo);
		return arquivo;
	}

	public File criarArquivoDiretorio(String nomeArquivo, Integer idEntidadeArquivo, String nomeEntidadeArquivo)
			throws Exception {
		File file = new File(nomeEntidadeArquivo + idEntidadeArquivo + File.separator);
		Boolean diretorios = file.mkdirs();
		file = new File(file.getAbsolutePath() + File.separator + nomeArquivo);
		if (diretorios) {
			file.createNewFile();
		}
		return file;
	}

	@SuppressWarnings("resource")
	public FileChannel abrirFluxoSaidaArquivo(File arquivo) throws FileNotFoundException {
		return new FileOutputStream(arquivo).getChannel();
	}

	public void uploadDados(ReadableByteChannel inChannel, ByteBuffer buffer) throws IOException {
		inChannel.read(buffer);
		buffer.flip();
	}

	public void gravarArquivo(FileChannel outChannel, ByteBuffer buffer) throws IOException  {
		outChannel.write(buffer);
		outChannel.close();
	}

	public HashMap<String, StreamedContent> downloadArquivo(String requerimento) throws FileNotFoundException {
		File diretorio = new File(BUNDLE.getString("diretorio.arquivo") + requerimento);
		List<String> listaArquivo = Arrays.asList(diretorio.list());
		HashMap<String, StreamedContent> hashStreamed = new HashMap<String, StreamedContent>();
		for (String arq : listaArquivo) {
			File arquivo = new File(BUNDLE.getString("diretorio.arquivo") + requerimento + File.separator + arq);
			String nomeArquivo = arquivo.getName();
			String tipoDocumento = nomeArquivo.substring(0, nomeArquivo.indexOf("."));
			String tipoArquivo = nomeArquivo.substring(nomeArquivo.indexOf(".") + 1);
			InputStream stream = new FileInputStream(arquivo);
			StreamedContent streamFile = new DefaultStreamedContent(stream, "application/" + tipoArquivo, nomeArquivo);
			hashStreamed.put(tipoDocumento, streamFile);
		}
		return hashStreamed;
	}

	public Collection<String> downloadArquivoLista(String requerimento) {
		File diretorio = new File(BUNDLE.getString("diretorio.arquivo") + requerimento);
		List<String> listaArquivo = Arrays.asList(diretorio.list());
		Collection<String> listaDocumentos = new ArrayList<String>();
		for (String arq : listaArquivo) {
			File arquivo = new File(BUNDLE.getString("diretorio.arquivo") + requerimento + File.separator + arq);
			String nomeArquivo = arquivo.getName();
			String tipoDocumento = nomeArquivo.substring(0, nomeArquivo.indexOf("."));
			listaDocumentos.add(tipoDocumento);
		}
		return listaDocumentos;
	}

	public String uploadArquivoComprovanteBoleto(UploadedFile file, BoletoPagamentoRequerimento pBoleto)
			throws Exception {
		ReadableByteChannel inChannel = Channels.newChannel(file.getInputstream());
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		String id = pBoleto.getIdeBoletoPagamentoRequerimento().toString();
		String diretorio = "boleto" + File.separator + id;
		File arquivo = criarArquivoDiretorio(file, diretorio, "comprovante_boleto_" + id);
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}

	public String uploadArquivoComprovanteBoletoDae(UploadedFile file, BoletoDaeRequerimento pBoleto, String tipo) throws Exception {
		ReadableByteChannel inChannel = Channels.newChannel(file.getInputstream());
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		String id = pBoleto.getIdeBoletoDaeRequerimento().toString();
		String diretorio = "boleto" + File.separator + id;
		File arquivo = criarArquivoDiretorio(file, diretorio, "comprovante_" + tipo + "_" + id);
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}

	public File criarArquivoDiretorio(UploadedFile file, String diretorio, String nome) throws Exception {
		String nomeArquivo = file.getFileName();
		String tipoArquivo = nomeArquivo.substring(nomeArquivo.indexOf("."));
		criarSubDiretorio(diretorio + File.separator);
		File arquivo = new File(BUNDLE.getString("diretorio.arquivo") + diretorio + File.separator + nome + tipoArquivo);
		return arquivo;
	}

	public void criarSubDiretorio(String diretorio) {
		String lPath = BUNDLE.getString("diretorio.arquivo") + diretorio;
		File lDiretorio = new File(lPath);
		lDiretorio.mkdirs();
	}

	public String criarSubDiretorioRetornandoPath(String diretorios) {

		File path = new File(diretorios);
		if (!path.exists()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}
	
	public StreamedContent getContentFile(String caminhoArquivo) throws Exception {
		File file = new File(caminhoArquivo);
		InputStream stream = new FileInputStream(file);
		StreamedContent sc = new DefaultStreamedContent(stream, getContentType(caminhoArquivo), FileUploadUtil.getFileName(caminhoArquivo));
		return sc;
	}

	public String getContentType(String caminhoArquivo) {
		return URLConnection.guessContentTypeFromName(caminhoArquivo);
	}

	public static void main(String[] args) throws Exception {
		GerenciaArquivoService g = new GerenciaArquivoService();
		try {
			StreamedContent c = g.getContentFile("D:\\DOCUMENTOOBRIGATORIO\\3\\form8.jpg");
			System.out.println(c.getName());
		} catch (FileNotFoundException e) {
			Log4jUtil.log(GerenciaArquivoService.class.getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public String uploadArquivoDocumentoObrigatorioRequerimentoExperimental(DocumentoObrigatorioRequerimento docObrigatorioRequerimento) throws Exception {
	
		ReadableByteChannel inChannel = Channels.newChannel(docObrigatorioRequerimento.getFileUpload().getFile().getInputstream());
		
		ByteBuffer buffer = ByteBuffer.allocate((int) docObrigatorioRequerimento.getFileUpload().getFile().getSize());
		
		File arquivo = criarArquivoDiretorio(FileUploadUtil.getFileName(docObrigatorioRequerimento.getFile().getName()),
				docObrigatorioRequerimento.getIdeDocumentoObrigatorioRequerimento(), DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.toString());
		
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		
		uploadDados(inChannel, buffer);
		
		gravarArquivo(outChannel, buffer);
		
		inChannel.close();
		
		return arquivo.getAbsolutePath();
	}

	public String uploadArquivoDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao, UploadedFile file)	throws Exception {

		StreamedContent streamed = new DefaultStreamedContent(new DataInputStream(new BufferedInputStream(file.getInputstream())), file.getContentType(), file.getFileName());
		ReadableByteChannel inChannel = Channels.newChannel(streamed.getStream());
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		File arquivo = criarArquivoDiretorio(FileUploadUtil.getFileName(file.getFileName()),documentoIdentificacao.getIdeDocumentoIdentificacao(),DiretorioArquivoEnum.DOCUMENTOIDENTIFICACAO.toString());
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}
	
	public String uploadArquivoDocumentoRepresentanteLegal(RepresentanteLegal representanteLegal, UploadedFile file) throws Exception {
		
		StreamedContent streamed = new DefaultStreamedContent(new DataInputStream(new BufferedInputStream(file.getInputstream())), file.getContentType(), file.getFileName());
		ReadableByteChannel inChannel = Channels.newChannel(streamed.getStream());
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		File arquivo = criarArquivoDiretorio(FileUploadUtil.getFileName(file.getFileName()),
				representanteLegal.getIdeRepresentanteLegal(),
				DiretorioArquivoEnum.REPRESENTANTE_LEGAL.toString());
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}
	
	public String uploadArquivoDocumentoProcuradorRepresentante(ProcuradorRepresentante procuradorRepresentante, UploadedFile file) throws Exception {
		
		StreamedContent streamed = new DefaultStreamedContent(new DataInputStream(new BufferedInputStream(file.getInputstream())), file.getContentType(), file.getFileName());
		ReadableByteChannel inChannel = Channels.newChannel(streamed.getStream());
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		File arquivo = criarArquivoDiretorio(FileUploadUtil.getFileName(file.getFileName()),
				procuradorRepresentante.getIdeProcuradorRepresentante(),
				DiretorioArquivoEnum.PROCURADOR_REPRESENTANTE.toString());
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}
	
	public String uploadArquivoDocumentoProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica, UploadedFile file) throws Exception {
		
		StreamedContent streamed = new DefaultStreamedContent(new DataInputStream(new BufferedInputStream(file.getInputstream())), file.getContentType(), file.getFileName());
		ReadableByteChannel inChannel = Channels.newChannel(streamed.getStream());
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		File arquivo = criarArquivoDiretorio(FileUploadUtil.getFileName(file.getFileName()),
				procuradorPessoaFisica.getIdeProcuradorPessoaFisica(),
				DiretorioArquivoEnum.PROCURADOR_PESSOA_FISICA.toString());
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}

	@SuppressWarnings("resource")
	public String transferArquivoDocumentoObrigatorioRequerimentoExperimental(
			DocumentoObrigatorioRequerimento docObrigatorioRequerimento) throws Exception {
		FileInputStream fin = new FileInputStream(docObrigatorioRequerimento.getCaminhoArquivoAnterior());
		File newFile = criarArquivoDiretorio(
				docObrigatorioRequerimento.getCaminhoArquivoAnterior().substring(
						docObrigatorioRequerimento.getCaminhoArquivoAnterior().lastIndexOf(File.separator) + 1),
				docObrigatorioRequerimento.getIdeDocumentoObrigatorioRequerimento(),
				DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.toString());
		FileOutputStream fout = new FileOutputStream(newFile);
		FileChannel in = fin.getChannel();
		FileChannel out = fout.getChannel();
		long numbytes = in.size();
		in.transferTo(0, numbytes, out);
		in.close();
		return newFile.getAbsolutePath();
	}

	public String uploadArquivoDocumentoRepresentacaoRequerimentoExperimental(
			DocumentoRepresentacaoRequerimento docObrigatorioRequerimento) throws Exception {
		ReadableByteChannel inChannel = Channels.newChannel(docObrigatorioRequerimento.getFileUpload().getFile()
				.getInputstream());
		ByteBuffer buffer = ByteBuffer.allocate((int) docObrigatorioRequerimento.getFileUpload().getFile().getSize());
		File arquivo = criarArquivoDiretorio(
				FileUploadUtil.getFileName(docObrigatorioRequerimento.getFile().getName()),
				docObrigatorioRequerimento.getIdeDocumentoRepresentacaoRequerimento(),
				DiretorioArquivoEnum.PESSOA.toString());
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}

	public String transferirArquivo(String dscCaminhoArquivoAntigo, String novoNomeArquivo, Integer id, String newPath)	throws Exception {
		FileInputStream fin = new FileInputStream(dscCaminhoArquivoAntigo);
		File newFile = criarArquivoDiretorio(novoNomeArquivo, id, newPath);
		FileOutputStream fout = new FileOutputStream(newFile);
		FileChannel in = fin.getChannel();
		FileChannel out = fout.getChannel();
		long numbytes = in.size();
		in.transferTo(0, numbytes, out);
		in.close();
		out.close();
		fin.close();
		fout.close();
		return newFile.getAbsolutePath();
	}

	@SuppressWarnings("resource")
	public void copyFile(String src, String dest) throws Exception {
		
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;
		try {
		
			File source = new File(src);
			File destination = new File(dest);
			
			if (destination.exists())
				destination.delete();

			sourceChannel = new FileInputStream(source).getChannel();
			destinationChannel = new FileOutputStream(destination).getChannel();
			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		} finally {
			if (sourceChannel != null && sourceChannel.isOpen())
				sourceChannel.close();
			if (destinationChannel != null && destinationChannel.isOpen())
				destinationChannel.close();
		}
	}
	
	private String criarDiretorioTemporario(String path) {
		File diretorio = new File("/tmp/" + path);
		if (!diretorio.exists()) {
			diretorio.mkdir();
		}
		return diretorio.getAbsolutePath();
	}
	
	public void salvarRelatorioTemporario(StreamedContent streamContent, String path, String nomeArquivo) {
		
		int BUFF_SIZE = 1024;
		byte[] buffer = new byte[BUFF_SIZE];
		
		String absolutePath = criarDiretorioTemporario(path);
		
		try {
			InputStream inputStream = streamContent.getStream();
			FileOutputStream out = new FileOutputStream(new File(absolutePath+"/"+nomeArquivo));
			int byteCount = 0;
			do {
	                byteCount = inputStream.read(buffer);
	                if (byteCount == -1) {
	                    break;
	                }
	                out.write(buffer, 0, byteCount);
	                out.flush();
	            } while (inputStream.available() > 0);
			
			out.close();
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}	
	
	public String uploadBoletoManual(UploadedFile file, Requerimento requerimento) throws Exception { //#9241
		ReadableByteChannel inChannel = Channels.newChannel(file.getInputstream());
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		String id = requerimento.getIdeRequerimento().toString();
		String diretorio = "FINANCEIRO" + File.separator + "BOLETO_REQUERIMENTO";
		File arquivo = criarArquivoDiretorio(file, diretorio, "boleto_requerimento_" + id);
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}
	
	public String uploadTermoCompromisso(UploadedFile file, Requerimento requerimento) throws Exception { //#9241
		ReadableByteChannel inChannel = Channels.newChannel(file.getInputstream());
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		String id = requerimento.getIdeRequerimento().toString();
		String diretorio = "REQUERIMENTO" + File.separator + id;
		File arquivo = criarArquivoDiretorio(file, diretorio, "termo_compromisso_" + id);
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}
	
	public String uploadRetornoBoleto(UploadedFile file, String numero) throws Exception {
		ReadableByteChannel inChannel = Channels.newChannel(file.getInputstream());
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		String diretorio = "FINANCEIRO" + File.separator + "LOTE_RETORNO_BOLETO";
		File arquivo = criarArquivoDiretorio(file, diretorio, "retorno_"+numero);
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}
	
	public String uploadArquivoDocumentoObrigatorioReenquadramento(DocumentoObrigatorioReenquadramento docObrigatorioRequerimento) throws Exception {
		
		ReadableByteChannel inChannel = Channels.newChannel(docObrigatorioRequerimento.getFileUpload().getFile().getInputstream());
		
		ByteBuffer buffer = ByteBuffer.allocate((int) docObrigatorioRequerimento.getFileUpload().getFile().getSize());
		
		File arquivo = criarArquivoDiretorio(FileUploadUtil.getFileName(docObrigatorioRequerimento.getFile().getName()),
				docObrigatorioRequerimento.getIdeDocumentoObrigatorioReenquadramento(), DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.toString());
		
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		
		uploadDados(inChannel, buffer);
		
		gravarArquivo(outChannel, buffer);
		
		inChannel.close();
		
		return arquivo.getAbsolutePath();
	}
	
	public String uploadArquivoRequerimentoOriginal(UploadedFile file, Requerimento requerimento) throws Exception {
		ReadableByteChannel inChannel = Channels.newChannel(file.getInputstream());
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		String id = requerimento.getIdeRequerimento().toString();
		String diretorio = "REQUERIMENTO" + File.separator + id;
		File arquivo = criarArquivoDiretorio(file, diretorio, "resumo_requerimento_original__" + id);
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}


	public String ObterShapeDiretorio(String caminho, String nomeDoArquivo, String extensao) {
		String CAMINHO_ARQUIVO = caminho + getFileSeparator() + nomeDoArquivo + extensao;
		
		if(existeArquivo(new File(CAMINHO_ARQUIVO))){
			return CAMINHO_ARQUIVO;
		}
		return null;
	}
	
	public static boolean existeArquivo(File f) { 
		 if (f.isDirectory()) { 
			 File[] files = f.listFiles(); 
			 for (File file : files) { 
				 boolean sucesso = existeArquivo(file);
				 if(!sucesso){
					 return false;
				 }
			 } 
		 }
		 boolean retorno = f.exists();
		 f = null;
		 return retorno;
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
	
	public String uploadParecerTecnico(UploadedFile file, Requerimento requerimento) throws Exception {
		ReadableByteChannel inChannel = Channels.newChannel(file.getInputstream());
		ByteBuffer buffer = ByteBuffer.allocate((int) file.getSize());
		String id = requerimento.getIdeRequerimento().toString();
		String diretorio = "DAE" + File.separator + "PARECER_TECNICO";
		File arquivo = criarArquivoDiretorio(file, diretorio, "parecer_tecnico_" + id);
		FileChannel outChannel = abrirFluxoSaidaArquivo(arquivo);
		uploadDados(inChannel, buffer);
		gravarArquivo(outChannel, buffer);
		inChannel.close();
		return arquivo.getAbsolutePath();
	}
}
