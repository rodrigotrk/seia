package br.gov.ba.seia.factory;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.gov.ba.seia.controller.FceEPMFController;
import br.gov.ba.seia.controller.FceFlorestalAbstractController;
import br.gov.ba.seia.dto.DadoConcedidoFceImpl;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.facade.ExecucaoPlanoManejoFlorestalService;
import br.gov.ba.seia.facade.FceServiceFacade;
/**
 * Classe controller do FCE florestal
 * @author 
 *
 */
@Stateless
public class FceFlorestalControllerFactory {
	
	@EJB
	private FceServiceFacade fceServiceFacade;
	@EJB
	private ExecucaoPlanoManejoFlorestalService execucaoPlanoManejoFlorestalService;
	/**
	 * 	
	 * @param requerimento
	 * @param documentoObrigatorio
	 * @return
	 */
	public FceFlorestalAbstractController getInstance(Requerimento requerimento, DocumentoObrigatorio documentoObrigatorio)  {
		return makeInstance(requerimento, documentoObrigatorio, null);
	}
	/**
	 * 
	 * @param dadoConcedidoImpl
	 * @return
	 */
	public FceFlorestalAbstractController getInstance(DadoConcedidoFceImpl dadoConcedidoImpl)  {
		return makeInstance(dadoConcedidoImpl.getFce().getIdeRequerimento(), dadoConcedidoImpl.getFce().getIdeDocumentoObrigatorio(), dadoConcedidoImpl);
	}
	/**
	 * 
	 * @param fce
	 * @return
	 */
	public FceFlorestalAbstractController getInstance(Fce fce)  {
		return makeInstance( fce.getIdeRequerimento(), fce.getIdeDocumentoObrigatorio(), null);
	}
	/**
	 * 
	 * @param requerimento
	 * @param documentoObrigatorio
	 * @param dadoConcedidoImpl
	 * @return
	 */
	private FceFlorestalAbstractController makeInstance(Requerimento requerimento, DocumentoObrigatorio documentoObrigatorio, DadoConcedidoFceImpl dadoConcedidoImpl) {
		DocumentoObrigatorioEnum documentoObrigatorioEnum = DocumentoObrigatorioEnum.getEnum(documentoObrigatorio);
		
		switch (documentoObrigatorioEnum) {
		case FCE_EPMF:
			return new FceEPMFController(requerimento, documentoObrigatorio, dadoConcedidoImpl, fceServiceFacade, execucaoPlanoManejoFlorestalService);
		default:
			throw new RuntimeException("O documento obrigatorio não representa um FCE Florestal.");
		}
	}

}
