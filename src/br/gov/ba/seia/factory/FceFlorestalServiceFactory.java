package br.gov.ba.seia.factory;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.facade.ExecucaoPlanoManejoFlorestalService;
import br.gov.ba.seia.facade.FceFlorestalAbstractService;
import br.gov.ba.seia.facade.FceServiceFacade;
/**
 *Classe de serviço do fce florestal 
 * @author 
 *
 */
@Stateless
public class FceFlorestalServiceFactory {
	
	@EJB
	private FceServiceFacade fceServiceFacade;
	@EJB
	private ExecucaoPlanoManejoFlorestalService execucaoPlanoManejoFlorestalService;
	/**
	 * 	
	 * @param documentoObrigatorio
	 * @return
	 * @throws Exception
	 */
	public FceFlorestalAbstractService getInstance(DocumentoObrigatorio documentoObrigatorio) throws Exception {
		return makeInstance(documentoObrigatorio);
	}
	/**
	 * 
	 * @param fce
	 * @return
	 * @throws Exception
	 */
	public FceFlorestalAbstractService getInstance(Fce fce) throws Exception {
		return makeInstance(fce.getIdeDocumentoObrigatorio());
	}
	/**
	 * 
	 * @param documentoObrigatorio
	 * @return service da execução do plano de manejo florestal
	 */
	private FceFlorestalAbstractService makeInstance(DocumentoObrigatorio documentoObrigatorio) {
		DocumentoObrigatorioEnum documentoObrigatorioEnum = DocumentoObrigatorioEnum.getEnum(documentoObrigatorio);
		
		switch (documentoObrigatorioEnum) {
		case FCE_EPMF:
			return execucaoPlanoManejoFlorestalService;
		default:
			throw new RuntimeException("O documento obrigatorio não representa um FCE Florestal service.");
		}
	}

}
