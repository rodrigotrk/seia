package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.VWTipologiaTipoAtoUnidadeMedidaDAOImpl;
import br.gov.ba.seia.entity.ParametroReferencia;
import br.gov.ba.seia.entity.TipologiaTipoAto;
import br.gov.ba.seia.entity.UnidadeMedidaTipologiaGrupo;
import br.gov.ba.seia.entity.VwTipologiaTipoAtoUnidadeMedida;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class VwTipologiaTipoAtoUnidadeMedidaService {
	
	@Inject
	VWTipologiaTipoAtoUnidadeMedidaDAOImpl vWTipologiaTipoAtoUnidadeMedidaDAOImpl;
	
	
	@EJB
	TipologiaTipoAtoService tipologiaTipoAtoService;
	@EJB
	UnidadeMedidaTipologiaGrupoService unidadeMedidaTipologiaGrupoService;
	@EJB
	ParametroReferenciaService parametroReferenciaService;
		
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<VwTipologiaTipoAtoUnidadeMedida> listarVwTipologiaTipoAtoUnidadeMedida(Integer ideTipologia, Integer ideTipologiaGrupo) throws Exception {
		  return vWTipologiaTipoAtoUnidadeMedidaDAOImpl.listarVwTipologiaTipoAtoUnidadeMedidaPorTipologiaGrupo(ideTipologiaGrupo);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtoReferencia(List<VwTipologiaTipoAtoUnidadeMedida>  vwTipologiasTipoAtoUnidadeMedidas) throws Exception{
		
		for (VwTipologiaTipoAtoUnidadeMedida vwTipologiaTipoAtoUnidadeMedida : vwTipologiasTipoAtoUnidadeMedidas) {
			
			if(valorMinimoMaximoExiste(vwTipologiaTipoAtoUnidadeMedida)){
				
				TipologiaTipoAto tipologiaTipoAto 						= tipologiaTipoAtoService.carregarTipologiaTipoAtos(vwTipologiaTipoAtoUnidadeMedida.getIdeTipologiaTipoAto());
				UnidadeMedidaTipologiaGrupo unidadeMedidaTipologiaGrupo = unidadeMedidaTipologiaGrupoService.carregarUnidadeMedidaTipologiaGrupo(vwTipologiaTipoAtoUnidadeMedida.getIdeUnidadeMedidaTipologiaGrupo());
				
				ParametroReferencia parametroReferencia = new ParametroReferencia();
				ParametroReferencia pRef = parametroReferenciaService.buscarParametroReferencia(tipologiaTipoAto,unidadeMedidaTipologiaGrupo); 
				if(pRef==null){
					parametroReferencia.setIdeTipologiaTipoAto(tipologiaTipoAto);
					parametroReferencia.setIdeUnidadeMedidaTipologiaGrupo(unidadeMedidaTipologiaGrupo);
					parametroReferencia.setValReferenciaMinimo(BigDecimal.valueOf(Double.parseDouble(vwTipologiaTipoAtoUnidadeMedida.getValorMinimo())));
					parametroReferencia.setValReferenciaMaximo(BigDecimal.valueOf(Double.parseDouble(vwTipologiaTipoAtoUnidadeMedida.getValorMaximo())));
					
				}else{
					parametroReferencia = pRef;
					parametroReferencia.setValReferenciaMinimo(BigDecimal.valueOf(Double.parseDouble(vwTipologiaTipoAtoUnidadeMedida.getValorMinimo())));
					parametroReferencia.setValReferenciaMaximo(BigDecimal.valueOf(Double.parseDouble(vwTipologiaTipoAtoUnidadeMedida.getValorMaximo())));
				}
				parametroReferenciaService.salvarParametroReferencia(parametroReferencia);

			}
			
		}
	}
	
	public Boolean valorMinimoMaximoExiste(VwTipologiaTipoAtoUnidadeMedida vwTipologiaTipoAtoUnidadeMedida){
		Boolean resultado = Boolean.TRUE;
		if(vwTipologiaTipoAtoUnidadeMedida.getValorMinimo().trim().isEmpty() && vwTipologiaTipoAtoUnidadeMedida.getValorMaximo().trim().isEmpty()){
			resultado = Boolean.FALSE;
		}
		return resultado;
	}
	
	
	
	
	
}