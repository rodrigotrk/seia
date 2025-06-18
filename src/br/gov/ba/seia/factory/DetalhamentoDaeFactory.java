package br.gov.ba.seia.factory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dto.DetalhamentoDaeDTO;
import br.gov.ba.seia.dto.GerarDaeDTO;
import br.gov.ba.seia.dto.ParcelaDaeDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.service.DetalhamentoDaeCrfService;
import br.gov.ba.seia.service.GerarDaeCrfService;
/**
 * Classe de detalhamento do DAE
 * @author 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DetalhamentoDaeFactory {

	@EJB
	private DetalhamentoDaeCrfService detalhamentoDaeCrfService;
	
	@EJB
	private GerarDaeCrfService gerarDaeCrfService;
	
	/**
	 * Metodo para gerar detalhamento do dae
	 * @param atoAmbiental
	 * @param requerimento
	 * @return null
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DetalhamentoDaeDTO gerar(AtoAmbiental atoAmbiental, Requerimento requerimento) throws Exception {
		
		AtoAmbientalEnum atoAmbientalEnum = AtoAmbientalEnum.getEnum(atoAmbiental);
		
		switch (atoAmbientalEnum) {
			case CRF:
				return detalhamentoDaeCrfService.gerarDetalhamentoDae(atoAmbiental, requerimento);
			default:
				return null;
		}
	}
	/**
	 * Metodo para gerar parcelas
	 * @param gerarDaeDTO
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarParcelas(GerarDaeDTO gerarDaeDTO) throws Exception {
		for (DetalhamentoDaeDTO detalhamentoDaeDTO : gerarDaeDTO.getListaDetalhamentoDaeDTO()) {
			
			AtoAmbientalEnum atoAmbientalEnum = AtoAmbientalEnum.getEnum(detalhamentoDaeDTO.getAtoAmbiental());
			
			switch (atoAmbientalEnum) {
				case CRF:
					gerarDaeCrfService.gerarParcelas(gerarDaeDTO);
			}
		}
	}
	/**Metodo para gerar parcela dae
	 * 
	 * @param atoAmbiental
	 * @param parcelaDaeDTO
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarParcelaDae(AtoAmbiental atoAmbiental, ParcelaDaeDTO parcelaDaeDTO,GerarDaeDTO gerarDaeDTO) throws Exception {
		
		AtoAmbientalEnum atoAmbientalEnum = AtoAmbientalEnum.getEnum(atoAmbiental);
		
		switch (atoAmbientalEnum) {
			case CRF:
				gerarDaeCrfService.gerarParcelaDae(atoAmbiental, parcelaDaeDTO, gerarDaeDTO);
		}
	}
}
