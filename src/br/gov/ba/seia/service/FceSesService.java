package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceSesDAOImpl;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceSes;
import br.gov.ba.seia.entity.FceSesCaracteristicaLancamento;
import br.gov.ba.seia.entity.FceSesCoordenadasLancamento;
import br.gov.ba.seia.entity.FceSesCoordenadasLancamentoLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceSesDadosElevatoria;
import br.gov.ba.seia.entity.FceSesDadosEstacaoTipoComposicao;
import br.gov.ba.seia.entity.FceSesDadosEstacaoTratamentoEsgoto;
import br.gov.ba.seia.entity.FceSesElevatoriaLocalizacaoGeografica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceSesService {

	@Inject
	private FceSesDAOImpl fceSesDaoImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSes(FceSes fceSes) {
		fceSesDaoImpl.salvarFceSes(fceSes);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesCoordenadasLancamento(FceSesCoordenadasLancamento fceSesCoordenadasLancamento) {
		fceSesDaoImpl.salvarFceSesCoordenadasLancamento(fceSesCoordenadasLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesCaracteristicaLancamento(FceSesCaracteristicaLancamento fceSesCaracteristicaLancamento) {
		fceSesDaoImpl.salvarFceSesCaracteristicaLancamento(fceSesCaracteristicaLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesCaracteristicaLancamento(FceSesCoordenadasLancamentoLocalizacaoGeografica fceSesLancamento) {
		fceSesDaoImpl.excluirFceSesCaracteristicaLancamento(fceSesLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesElevatoriaLocalizacaoGeografica(FceSesElevatoriaLocalizacaoGeografica localizacaoElevatoria) {
		fceSesDaoImpl.salvarFceSesElevatoriaLocalizacaoGeografica(localizacaoElevatoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSes buscarFceSesByIdeFce(Fce fce)  {
		return fceSesDaoImpl.buscarFceSesByIdeFce(fce);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesDadosElevatoria(FceSesDadosElevatoria dadosElevatoria)  {
		fceSesDaoImpl.salvarFceSesDadosElevatoria(dadosElevatoria);
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosEstacaoTratamentoEsgoto(FceSesDadosEstacaoTratamentoEsgoto localizacaoTratamento)  {
		fceSesDaoImpl.excluirFceSesDadosEstacaoTratamentoEsgoto(localizacaoTratamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosElevatoria(FceSesDadosElevatoria dadosElevatoria)  {
		fceSesDaoImpl.excluirFceSesDadosElevatoria(dadosElevatoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosElevatoriaImpl(FceSesDadosElevatoria dadosElevatoria)  {
		fceSesDaoImpl.excluirFceSesDadosElevatoriaImpl(dadosElevatoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesElevatoriaLocalizacaoGeografica(FceSesDadosElevatoria localizacaoElevatoria)  {
		fceSesDaoImpl.excluirFceSesElevatoriaLocalizacaoGeografica(localizacaoElevatoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCoordenadasLancamentoLocalizacaoGeografica(FceSesCoordenadasLancamentoLocalizacaoGeografica fceSesCoordenadasLancamento) {
		fceSesDaoImpl.excluirCoordenadasLancamentoLocalizacaoGeografica(fceSesCoordenadasLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesDadosEstacaoTratamentoEsgoto(
			FceSesDadosEstacaoTratamentoEsgoto dadosTratamento)  {
		fceSesDaoImpl.salvarFceSesDadosEstacaoTratamentoEsgoto(dadosTratamento);
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesDadosEstacaoTipoComposicao(
			FceSesDadosEstacaoTipoComposicao composicao)  {
		
		fceSesDaoImpl.salvarFceSesDadosEstacaoTipoComposicao(composicao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosEstacaoTipoComposicao> buscarComposicoesSelecionadas(FceSesDadosEstacaoTratamentoEsgoto dadosTratamento) {
		
		return fceSesDaoImpl.buscarComposicoesSelecionadas(dadosTratamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesElevatoriaLocalizacaoGeografica> buscarLocalizacaoElevatoria(FceSesDadosElevatoria fceSesLocalizacao) {
		
		return fceSesDaoImpl.buscarLocalizacaoElevatoria(fceSesLocalizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesCoordenadasLancamentoLocalizacaoGeografica> buscarfceSesCoordenadasLancamentoLocalizacaoGeografica(FceSes fceSes)  {
		return fceSesDaoImpl.buscarfceSesCoordenadasLancamentoLocalizacaoGeografica(fceSes);
	}
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSesCoordenadasLancamento buscarFceSesCoordenadasLancamentoByIdeFceSes(FceSes fceSes) {
		return fceSesDaoImpl.buscarFceSesCoordenadasLancamentoByIdeFceSes(fceSes);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesCoordenadasLancamentoLocalizacaoGeografica(
			FceSesCoordenadasLancamentoLocalizacaoGeografica coordenadaLocalizacao)  {
		fceSesDaoImpl.salvarFceSesCoordenadasLancamentoLocalizacaoGeografica(coordenadaLocalizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesCaracteristicaLancamento> buscarFceCaracteristicasLancamento(FceSesCoordenadasLancamento fceSesCoordenadaLancamento)  {
		return fceSesDaoImpl.buscarFceCaracteristicasLancamento(fceSesCoordenadaLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosElevatoria> listarDadosElevatoria(FceSes fceSes) {
		return fceSesDaoImpl.listarDadosElevatoria(fceSes);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosEstacaoTratamentoEsgoto> listarDadosEstacaoEsgoto(
			FceSes fceSes) {
		
		return fceSesDaoImpl.listarDadosEstacaoEsgoto(fceSes);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosEstacaoTipoComposicao> listarComposicoes(
			FceSesDadosEstacaoTratamentoEsgoto dadosTratamento)  {
		return fceSesDaoImpl.listarComposicoesByFceIdeEstacaoEsgoto(dadosTratamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosEstacaoTipoComposicaoImpl(FceSesDadosEstacaoTratamentoEsgoto localizacaoTratamento)  {
		fceSesDaoImpl.excluirFceSesDadosEstacaoTipoComposicaoImpl(localizacaoTratamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSes buscarFceSesByIdeFceSes(FceSes fceSes)  {
		return fceSesDaoImpl.buscarFceSesByIdeFceSes(fceSes);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSesDadosElevatoria buscarFceSesDadosElevatoriaByIdeDadosElevatoria(FceSesDadosElevatoria dadosElevatoria)  {
		return fceSesDaoImpl.buscarFceSesDadosElevatoriaByIdeDadosElevatoria(dadosElevatoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSesDadosEstacaoTratamentoEsgoto buscarDadosEstacaoTratamentoEsgotoByIdeDadosEstacao(FceSesDadosEstacaoTratamentoEsgoto estacaoTratamento)  {
		return fceSesDaoImpl.buscarDadosEstacaoTratamentoEsgotoByIdeDadosEstacao(estacaoTratamento);
	}
}
