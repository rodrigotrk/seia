package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.DetalhamentoDaeDTO;
import br.gov.ba.seia.dto.MemoriaCalculoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ConsumidorReposicaoFlorestal;
import br.gov.ba.seia.entity.CumprimentoReposicaoFlorestal;
import br.gov.ba.seia.entity.DetentorReposicaoFlorestal;
import br.gov.ba.seia.entity.DevedorReposicaoFlorestal;
import br.gov.ba.seia.entity.PagamentoReposicaoFlorestal;
import br.gov.ba.seia.entity.ParametroCalculo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.PagamentoReposicaoFlorestalEnum;
import br.gov.ba.seia.enumerator.UnidadeMedidaEnum;
import br.gov.ba.seia.util.CalculoFlorestalUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DetalhamentoDaeCrfService {

	@EJB
	private CumprimentoReposicaoFlorestalService cumprimentoReposicaoFlorestalService;
	
	@EJB
	private DetentorReposicaoFlorestalService detentorReposicaoFlorestalService;
	
	@EJB
	private ConsumidorReposicaoFlorestalService consumidorReposicaoFlorestalService;
	
	@EJB
	private DevedorReposicaoFlorestalService devedorReposicaoFlorestalService;
	
	@EJB
	private ParametroCalculoService parametroCalculoService;
	
	public String formatarNumero(BigDecimal pValor){
		DecimalFormat df = Util.getDecimalFormatPtBr(); 
		return df.format(pValor);
	}
	
	public String formatarNumeroVolume(BigDecimal pValor){
		DecimalFormat df =  new DecimalFormat("#,###,##0.0000", new DecimalFormatSymbols(new Locale("pt", "BR")));
		return df.format(pValor);
	}
	
	public String formularDevedor(String valor, BigDecimal metrosCubicos) {
		Collection<ParametroCalculo> listaParametroCalculo = parametroCalculoService.listarParametrosDAE(AtoAmbientalEnum.CRF.getId());
		
		StringBuilder formula = new StringBuilder();
		
		for (ParametroCalculo parametroCalculo : listaParametroCalculo) {
			formula.append(valor
					.concat(" * ")
					.concat(formatarNumero(metrosCubicos))
					.concat(" * ")
					.concat(formatarNumero(parametroCalculo.getValorTaxa())));
		}
		return formula.toString();
	}
	
	public String formularDevedorVolumeReferencia(String valor, BigDecimal metrosCubicos)  {
		Collection<ParametroCalculo> listaParametroCalculo = parametroCalculoService.listarParametrosDAE(AtoAmbientalEnum.CRF.getId());
		
		StringBuilder formula = new StringBuilder();
		
		for (ParametroCalculo parametroCalculo : listaParametroCalculo) {
			formula.append(valor
					.concat(" * ")
					.concat(formatarNumero(metrosCubicos))
					.concat(" * ")
					.concat(formatarNumero(parametroCalculo.getValorTaxa())));
		}
		return formula.toString();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String formularDetentorConsumidor(String valor)  {
		Collection<ParametroCalculo> listaParametroCalculo = parametroCalculoService.listarParametrosDAE(AtoAmbientalEnum.CRF.getId());
		
		StringBuilder formula = new StringBuilder();
		
		for (ParametroCalculo parametroCalculo : listaParametroCalculo) {
			formula.append(valor
					.concat(" * ")
					.concat(formatarNumero(parametroCalculo.getValorTaxa())));

		}
		return formula.toString();
	}
	
	public BigDecimal converterUnidadeMedidaParaM3(UnidadeMedida unidadeMedida, BigDecimal valor) {
		
		if (!Util.isNullOuVazio(unidadeMedida) && !Util.isNullOuVazio(valor)) {
			
			if (UnidadeMedidaEnum.MDC.getId().equals(unidadeMedida.getIdeUnidadeMedida())) {
				return BigDecimal.valueOf(CalculoFlorestalUtil.converterMDCParaM3(valor.doubleValue()));
				
			} else if (UnidadeMedidaEnum.ST.getId().equals(unidadeMedida.getIdeUnidadeMedida())) {
				return BigDecimal.valueOf(CalculoFlorestalUtil.converterEstereoParaM3(valor.doubleValue()));
			}
			
			return valor;
		}
		
		return BigDecimal.ZERO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<MemoriaCalculoDTO> gerarMemoriaCalculo(CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal){
		List<MemoriaCalculoDTO> lista = new ArrayList<MemoriaCalculoDTO>();
		
		PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal = cumprimentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal();
		if (!Util.isNullOuVazio(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestalPai())) {
			pagamentoReposicaoFlorestal = pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestalPai();
		}
		
		if (PagamentoReposicaoFlorestalEnum.DETENTOR.getId().equals(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal())) {
			DetentorReposicaoFlorestal detentorReposicaoFlorestal = detentorReposicaoFlorestalService.obterDetentorReposicaoFlorestalPorCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal.getIdeCumprimentoReposicaoFlorestal());
			BigDecimal volumeAutorizado = converterUnidadeMedidaParaM3(detentorReposicaoFlorestal.getIdeUnidadeMedida(), detentorReposicaoFlorestal.getVolumeAutorizado());
			
			lista.add(new MemoriaCalculoDTO(Util.getString("crf_volume_informado"), formatarNumeroVolume(volumeAutorizado)));
			lista.add(new MemoriaCalculoDTO(Util.getString("crf_calculo_volume"), formularDetentorConsumidor(formatarNumeroVolume(volumeAutorizado))));
		}
		else if (PagamentoReposicaoFlorestalEnum.CONSUMIDOR.getId().equals(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal())) {
			ConsumidorReposicaoFlorestal consumidorReposicaoFlorestal = consumidorReposicaoFlorestalService.obterConsumidorReposicaoFlorestalPorCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal.getIdeCumprimentoReposicaoFlorestal());
			BigDecimal vlmMaterial = converterUnidadeMedidaParaM3(consumidorReposicaoFlorestal.getIdeUnidadeMedida(), consumidorReposicaoFlorestal.getVlmMaterialLenhosoConsumido());
			
			lista.add(new MemoriaCalculoDTO(Util.getString("crf_volume_informado"), formatarNumeroVolume(vlmMaterial)));
			lista.add(new MemoriaCalculoDTO(Util.getString("crf_calculo_volume"), formularDetentorConsumidor(formatarNumeroVolume(vlmMaterial))));
		}
		else if (PagamentoReposicaoFlorestalEnum.DEVEDOR.getId().equals(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal())) {
			DevedorReposicaoFlorestal devedorReposicaoFlorestal = devedorReposicaoFlorestalService.obterDevedorReposicaoFlorestalCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal.getIdeCumprimentoReposicaoFlorestal());
			
			MemoriaCalculoDTO calculo = new MemoriaCalculoDTO(Util.getString("crf_calculo_a_vb_120"), formularDevedor(formatarNumeroVolume(devedorReposicaoFlorestal.getVlrAreaSuprimida()), devedorReposicaoFlorestal.getIdeBioma().getMetrosCubicos()));
			
			lista.add(new MemoriaCalculoDTO(Util.getString("crf_area_informada"), formatarNumeroVolume(devedorReposicaoFlorestal.getVlrAreaSuprimida())));
			lista.add(new MemoriaCalculoDTO(Util.getString("crf_bioma"), devedorReposicaoFlorestal.getIdeBioma().getNomBioma()));
			lista.add(calculo);
			
			if(!Util.isNullOuVazio(devedorReposicaoFlorestal.getValVolumeReferencia())){
				calculo.setRendered(false);
				lista.add(new MemoriaCalculoDTO(Util.getString("crf_calculo_a_vrb_120"), formularDevedorVolumeReferencia(formatarNumeroVolume(devedorReposicaoFlorestal.getVlrAreaSuprimida()), devedorReposicaoFlorestal.getValVolumeReferencia())));
				lista.add(new MemoriaCalculoDTO(Util.getString("crf_vb_param"), Util.formatarNumero(devedorReposicaoFlorestal.getIdeBioma().getMetrosCubicos())));
			}
			
		}
		
		return lista;
	}
	
	public List<String> gerarLegendas(CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal) {
		
		List<String> legendas = new ArrayList<String>();
		
		if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestal)) {
			PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal = cumprimentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal();
			
			if (!Util.isNullOuVazio(pagamentoReposicaoFlorestal)) {
				if (!Util.isNullOuVazio(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestalPai())) {
					pagamentoReposicaoFlorestal = pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestalPai();
				}
				
				if (PagamentoReposicaoFlorestalEnum.DEVEDOR.getId().equals(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal())) {
					legendas.add(Util.getString("crf_a"));
					legendas.add(Util.getString("crf_vb"));
					legendas.add(Util.getString("crf_legenda"));
					try {
						DevedorReposicaoFlorestal  devedorReposicaoFlorestal = devedorReposicaoFlorestalService.obterDevedorReposicaoFlorestalCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal.getIdeCumprimentoReposicaoFlorestal());
						
						if(!Util.isNullOuVazio(devedorReposicaoFlorestal) && !Util.isNullOuVazio(devedorReposicaoFlorestal.getValVolumeReferencia())){
							legendas.add(Util.getString("crf_vrb"));
						}
					} catch (Exception e) {
						Log4jUtil.log(this.getClass().getSimpleName(),Level.ERROR,e);
						
					} 
					//legendas.add(Util.getString("crf_120")); *ISSUE 110377*
				} else {
					legendas.add(Util.getString("crf_volume"));
					legendas.add(Util.getString("crf_legenda"));
				}
			}
		}
		
		return legendas;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DetalhamentoDaeDTO gerarDetalhamentoDae(AtoAmbiental atoAmbiental, Requerimento requerimento){
		DetalhamentoDaeDTO detalhamentoDaeDTO = new DetalhamentoDaeDTO();
		
		CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal = cumprimentoReposicaoFlorestalService.obterCumprimentoReposicaoFlorestalPorRequerimento(requerimento);
		
		detalhamentoDaeDTO.setAtoAmbiental(atoAmbiental);
		detalhamentoDaeDTO.setValor(cumprimentoReposicaoFlorestal.getVlrPecuniario().doubleValue());
		detalhamentoDaeDTO.setListaMemoriaCalculoDTO(gerarMemoriaCalculo(cumprimentoReposicaoFlorestal));
		detalhamentoDaeDTO.setListaLegenda(gerarLegendas(cumprimentoReposicaoFlorestal));
		detalhamentoDaeDTO.setCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal);
		
		PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal = cumprimentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal();
		PagamentoReposicaoFlorestal pagamentoReposicaoFlorestalPai = cumprimentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestalPai();
		
		if (!Util.isNullOuVazio(pagamentoReposicaoFlorestalPai)) {
			pagamentoReposicaoFlorestal = pagamentoReposicaoFlorestalPai;
		}
		detalhamentoDaeDTO.setNomTipoCumprimento(pagamentoReposicaoFlorestal.getNomPagamentoReposicaoFlorestal());
		detalhamentoDaeDTO.setPagamentoReposicaoFlorestal(pagamentoReposicaoFlorestal);
		
		return detalhamentoDaeDTO;
	}

}
