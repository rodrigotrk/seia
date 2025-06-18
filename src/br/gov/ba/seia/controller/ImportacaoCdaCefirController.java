package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.entity.ImportacaoCdaCefir;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.facade.ImportacaoCdaCefirFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * Esta class tem por objetivo apoiar as views de Importação dos imóveis do CDA para o CEFIR.
 * @author carlos.duarte
 * 
 */

@Named("importacaoCdaCefirController")
@ViewScoped
public class ImportacaoCdaCefirController {
	
	private List<ImportacaoCdaCefir> listImportacaoCdaCefir;
	private UploadedFile planilha;
	private UploadedFile dbf;
	private UploadedFile shp;
	private UploadedFile shx;

	@EJB
	private ImportacaoCdaCefirFacade importacaoCdaCefirFacade;
	
	@PostConstruct
	public void init() {		
		
	}
	
	public String importarImoveis(){
		try {
			Usuario usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
			importacaoCdaCefirFacade.importarImoveis(planilha, dbf, shp, shx, usuarioLogado);
			limparArquivos();
			JsfUtil.addSuccessMessage("Importação realizada com sucesso. Verifique o arquivo xls de saída.");
			return "/paginas/manter-imoveis/cefir/listaImportacaoCdaCefir.xhtml";
		} catch (Exception e) {
			limparArquivos();
			JsfUtil.addErrorMessage(e.getMessage());
			return "";
		}
	}	
	
	public void limparArquivos(){
		this.planilha = null;
		this.dbf = null;
		this.shp = null;
		this.shx = null;
	}

	public List<ImportacaoCdaCefir> getListImportacaoCdaCefir() {
		if(Util.isNullOuVazio(listImportacaoCdaCefir)){
			listImportacaoCdaCefir = new ArrayList<ImportacaoCdaCefir>();
			try {
				listImportacaoCdaCefir = importacaoCdaCefirFacade.listarImportacaoCdaCefir();
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro ao carregar lista de importações CDA.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		return listImportacaoCdaCefir;
	}
	
	public StreamedContent getArquivoImportacao(String caminhoArquivo) {		
		if(!Util.isNullOuVazio(caminhoArquivo)) {
			try {
				return importacaoCdaCefirFacade.getContentFile(caminhoArquivo);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage("Erro ao tentar fazer o download do arquivo");				
			}
		}			
		return null;
	}

	public UploadedFile getPlanilha() {
		return planilha;
	}

	public void setPlanilha(UploadedFile planilha) {
		this.planilha = planilha;
	}

	public UploadedFile getDbf() {
		return dbf;
	}

	public void setDbf(UploadedFile dbf) {
		this.dbf = dbf;
	}

	public UploadedFile getShp() {
		return shp;
	}

	public void setShp(UploadedFile shp) {
		this.shp = shp;
	}

	public UploadedFile getShx() {
		return shx;
	}

	public void setShx(UploadedFile shx) {
		this.shx = shx;
	}	
	
}