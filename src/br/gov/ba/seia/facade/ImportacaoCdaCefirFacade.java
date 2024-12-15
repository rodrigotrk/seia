package br.gov.ba.seia.facade;



import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.entity.ImportacaoCdaCefir;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.ImportacaoCdaCefirService;
import br.gov.ba.seia.service.ParamPersistDadoGeoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ImportacaoCdaCefirFacade {

	@EJB
	private ImportacaoCdaCefirService importacaoCdaCefirService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private ParamPersistDadoGeoService paramPersistDadoGeoService;
	@Resource
	private UserTransaction tx;
	
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImportacaoCdaCefir> listarImportacaoCdaCefir() {		
		return importacaoCdaCefirService.listarImportacaoCdaCefir();		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void importarImoveis(UploadedFile planilha, UploadedFile dbf, UploadedFile shp, UploadedFile shx, Usuario usuarioLogado) throws Exception {
		ImportacaoCdaCefir importacaoCdaCefir = new ImportacaoCdaCefir();
		try{
			tx.begin();
			importacaoCdaCefirService.validarArquivosImportacao(planilha, dbf, shp, shx);
			importacaoCdaCefir = importacaoCdaCefirService.salvar(usuarioLogado);
			importacaoCdaCefirService.uploadArquivosImportacao(importacaoCdaCefir, planilha, dbf, shp, shx);
			importacaoCdaCefirService.persistirShapeTheGeomImportacaoCdaCefir(importacaoCdaCefir.getIdeImportacaoCdaCefir());			
			importacaoCdaCefirService.inserirImoveisCda(importacaoCdaCefir);
			tx.commit();
		}catch(Exception e) {
			if(!Util.isNull(importacaoCdaCefir.getIdeImportacaoCdaCefir())){ 
				importacaoCdaCefirService.removerArquivosImportacao(importacaoCdaCefir);
			}
			tx.rollback();
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw new Exception(e.getMessage());			
		}
		
	}
	
	public StreamedContent getContentFile(String caminhoArquivo) throws Exception {
		return gerenciaArquivoService.getContentFile(caminhoArquivo);
	}
	
	
}
