package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceSaaDAOImpl;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceSaa;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaDadosConcedidos;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaElevatoriaBruta;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaElevatoriaTratada;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaEta;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaReservatorio;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceSaaService {

	@Inject
	private FceSaaDAOImpl fceSaaDaoImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaa(FceSaa fceSaa) {
		fceSaaDaoImpl.salvarFceSaa(fceSaa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoBruta(FceSaaLocalizacaoGeograficaElevatoriaBruta localizacaoBruta) {
		fceSaaDaoImpl.salvarFceSaaLocalizacaoBruta(localizacaoBruta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoDadosConcedidos(FceSaaLocalizacaoGeograficaDadosConcedidos localizacao)  {
		fceSaaDaoImpl.salvarFceSaaLocalizacaoDadosConcedidos(localizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoBruta(FceSaaLocalizacaoGeograficaElevatoriaBruta localizacaoBruta) {
		fceSaaDaoImpl.excluirFceSaaLocalizacaoBruta(localizacaoBruta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoReservatorio(FceSaaLocalizacaoGeograficaReservatorio localizacaoReservatorio) {
		fceSaaDaoImpl.excluirFceSaaLocalizacaoReservatorio(localizacaoReservatorio);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoTratada(FceSaaLocalizacaoGeograficaElevatoriaTratada localizacaoTratada)  {
		fceSaaDaoImpl.excluirFceSaaLocalizacaoTratada(localizacaoTratada);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoEta(FceSaaLocalizacaoGeograficaEta localizacaoEta) {
		fceSaaDaoImpl.excluirFceSaaLocalizacaoEta(localizacaoEta);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaElevatoriaBruta> listarLocalizacaoBrutaByIdeFceSaa(FceSaa fceSaa)  {
		return fceSaaDaoImpl.listarLocalizacaoBrutaByIdeFceSaa(fceSaa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoEta(FceSaaLocalizacaoGeograficaEta localizacaoEta)  {
		fceSaaDaoImpl.salvarFceSaaLocalizacaoEta(localizacaoEta);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaEta> listarLocalizacaoEtaByIdeFceSaa(FceSaa fceSaa)  {
		return fceSaaDaoImpl.listarLocalizacaoEtaByIdeFceSaa(fceSaa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSaaLocalizacaoGeograficaDadosConcedidos buscarLocalizacaoDadosConcedidosByIdeFceSaaAndTipoCaptacao(FceSaa fceSaa,Integer ideTipoCaptacao)  {
		return fceSaaDaoImpl.buscarLocalizacaoDadosConcedidosByIdeFceSaaAndTipoCaptacao(fceSaa,ideTipoCaptacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoTratada(FceSaaLocalizacaoGeograficaElevatoriaTratada localizacaoTratada)  {
		fceSaaDaoImpl.salvarFceSaaLocalizacaoTratada(localizacaoTratada);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaElevatoriaTratada> buscarLocalizacaoTratadaByIdeFceSaa(FceSaa fceSaa)  {
		return fceSaaDaoImpl.buscarLocalizacaoTratadaByIdeFceSaa(fceSaa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoReservatorio(FceSaaLocalizacaoGeograficaReservatorio localizacaoReservatorio)  {
		fceSaaDaoImpl.salvarFceSaaLocalizacaoReservatorio(localizacaoReservatorio);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaReservatorio> listarLocalizacaoReservatorioByIdeFceSaa(FceSaa fceSaa)  {
		return fceSaaDaoImpl.listarLocalizacaoReservatorioByIdeFceSaa(fceSaa);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSaa buscarFceSaaByIdeFce(Fce fce)  {
		return fceSaaDaoImpl.buscarFceSaaByIdeFce(fce);
	}
	
}
