package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.FceSesController;
import br.gov.ba.seia.entity.CaracteristicaEfluente;
import br.gov.ba.seia.entity.EtaTipoComposicao;
import br.gov.ba.seia.entity.FaixaDiametroAdutora;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaEta;
import br.gov.ba.seia.entity.FceSes;
import br.gov.ba.seia.entity.FceSesCaracteristicaLancamento;
import br.gov.ba.seia.entity.FceSesCoordenadasLancamento;
import br.gov.ba.seia.entity.FceSesCoordenadasLancamentoLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceSesDadosElevatoria;
import br.gov.ba.seia.entity.FceSesDadosEstacaoTipoComposicao;
import br.gov.ba.seia.entity.FceSesDadosEstacaoTratamentoEsgoto;
import br.gov.ba.seia.entity.FceSesElevatoriaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceSesTipoComposicao;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.service.CaracteristicaEfluenteService;
import br.gov.ba.seia.service.FaixaDiametroAdutoraService;
import br.gov.ba.seia.service.FceSesService;
import br.gov.ba.seia.service.TipoComposicaoService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceSesFacade {

	@EJB
	private FaixaDiametroAdutoraService faixaDiametroService;
	
	@EJB
	private TipoComposicaoService tipoComposicaoService;
	
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	
	@EJB
	private FceSesService fceSesService;
	
	@EJB
	private FceServiceFacade fceServiceFacade;
	
	@EJB
	private FceOutorgaServiceFacade fceOutorgaServiceFacade;
	
	@EJB
	private CaracteristicaEfluenteService caracteristicaEfluenteService;
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FaixaDiametroAdutora> carregarFaixasDiametro(Integer tipoFase01, Integer tipoFase02, Integer tipoFase03 ) throws Exception{
		
		return faixaDiametroService.carregarFaixasDiametro(tipoFase01, tipoFase02, tipoFase03);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesTipoComposicao> carregarTipoComposicoes() throws Exception {
		
		return tipoComposicaoService.listaSesTipoComposicao();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EtaTipoComposicao> listaTipoComposicaoByIdeLocalizacaoEta(FceSaaLocalizacaoGeograficaEta localizacaoEta) throws Exception {
		return tipoComposicaoService.listaTipoComposicaoByIdeLocalizacaoEta(localizacaoEta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipoComposicaoByIdeLocalizacaoEta(FceSaaLocalizacaoGeograficaEta localizacaoEta) throws Exception {
		tipoComposicaoService.excluirTipoComposicaoByIdeLocalizacaoEta(localizacaoEta);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String buscarGeometriaShape(Integer ideLocalizacaoGeografica) throws Exception {
		return validacaoGeoSeiaService.buscarGeometriaShape(ideLocalizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesElevatoriaLocalizacaoGeografica(FceSesDadosElevatoria localizacaoElevatoria) throws Exception {
		fceSesService.excluirFceSesElevatoriaLocalizacaoGeografica(localizacaoElevatoria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSes(FceSes fceSes) throws Exception{
		fceSesService.salvarFceSes(fceSes);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesCoordenadasLancamento(FceSesCoordenadasLancamento fceSesCoordenadasLancamento) throws Exception{
		fceSesService.salvarFceSesCoordenadasLancamento(fceSesCoordenadasLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCoordenadasLancamentoLocalizacaoGeografica(FceSesCoordenadasLancamentoLocalizacaoGeografica fceSesCoordenadasLancamento) throws Exception{
		fceSesService.excluirCoordenadasLancamentoLocalizacaoGeografica(fceSesCoordenadasLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosEstacaoTratamentoEsgoto(FceSesDadosEstacaoTratamentoEsgoto localizacaoTratamento) throws Exception {
		fceSesService.excluirFceSesDadosEstacaoTratamentoEsgoto(localizacaoTratamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosElevatoria(FceSesDadosElevatoria dadosElevatoria) throws Exception {
		fceSesService.excluirFceSesDadosElevatoria(dadosElevatoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosElevatoriaImpl(FceSesDadosElevatoria dadosElevatoria) throws Exception {
		fceSesService.excluirFceSesDadosElevatoriaImpl(dadosElevatoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesCaracteristicaLancamento(FceSesCaracteristicaLancamento fceSesCaracteristicaLancamento) throws Exception{
		fceSesService.salvarFceSesCaracteristicaLancamento(fceSesCaracteristicaLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesCaracteristicaLancamento(FceSesCoordenadasLancamentoLocalizacaoGeografica fceSesLancamento) throws Exception{
		fceSesService.excluirFceSesCaracteristicaLancamento(fceSesLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesElevatoriaLocalizacaoGeografica(FceSesElevatoriaLocalizacaoGeografica localizacaoElevatoria) throws Exception {
		fceSesService.salvarFceSesElevatoriaLocalizacaoGeografica(localizacaoElevatoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceSesController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSes buscarFceSesByIdeFce(Fce fce) throws Exception{
		return fceSesService.buscarFceSesByIdeFce(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarFce(Fce fce) throws Exception{
		fceServiceFacade.salvarFce(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getBacia(LocalizacaoGeografica localizacaoGeografica){
		return fceOutorgaServiceFacade.getBacia(localizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesDadosElevatoria(FceSesDadosElevatoria dadosElevatoria) throws Exception {
		fceSesService.salvarFceSesDadosElevatoria(dadosElevatoria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesDadosEstacaoTratamentoEsgoto(
			FceSesDadosEstacaoTratamentoEsgoto dadosTratamento) throws Exception {
		fceSesService.salvarFceSesDadosEstacaoTratamentoEsgoto(dadosTratamento);
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesDadosEstacaoTipoComposicao(
			FceSesDadosEstacaoTipoComposicao composicao) throws Exception {
		
		fceSesService.salvarFceSesDadosEstacaoTipoComposicao(composicao);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosEstacaoTipoComposicao> buscarComposicoesSelecionadas(FceSesDadosEstacaoTratamentoEsgoto dadosTratamento) throws Exception{
		
		return fceSesService.buscarComposicoesSelecionadas(dadosTratamento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesElevatoriaLocalizacaoGeografica> buscarLocalizacaoElevatoria(FceSesDadosElevatoria fceSesLocalizacao) throws Exception {
		
		return fceSesService.buscarLocalizacaoElevatoria(fceSesLocalizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSesCoordenadasLancamentoLocalizacaoGeografica(
			FceSesCoordenadasLancamentoLocalizacaoGeografica coordenadaLocalizacao) throws Exception {
		fceSesService.salvarFceSesCoordenadasLancamentoLocalizacaoGeografica(coordenadaLocalizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesCaracteristicaLancamento> buscarFceCaracteristicasLancamento(FceSesCoordenadasLancamento fceSesCoordenadaLancamento) throws Exception {
		return fceSesService.buscarFceCaracteristicasLancamento(fceSesCoordenadaLancamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesCoordenadasLancamentoLocalizacaoGeografica> buscarFceSesCoordenadasLancamentoLocalizacaoGeografica(FceSes fceSes) throws Exception {
		
		return fceSesService.buscarfceSesCoordenadasLancamentoLocalizacaoGeografica(fceSes);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSesCoordenadasLancamento buscarFceSesCoordenadasLancamentoByIdeFceSes(FceSes fceSes) throws Exception{
		return fceSesService.buscarFceSesCoordenadasLancamentoByIdeFceSes(fceSes);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosElevatoria> listarDadosElevatoria(FceSes fceSes) throws Exception{
		return fceSesService.listarDadosElevatoria(fceSes);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosEstacaoTratamentoEsgoto> listarDadosEstacaoEsgoto(
			FceSes fceSes) throws Exception {
		return fceSesService.listarDadosEstacaoEsgoto(fceSes);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesDadosEstacaoTipoComposicao> listarComposicoes(
			FceSesDadosEstacaoTratamentoEsgoto dadosTratamento) throws Exception {
		return fceSesService.listarComposicoes(dadosTratamento);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaEfluente> listarCaracteristicaEfluenteSemFosforo() throws Exception {
		
		return caracteristicaEfluenteService.listarCaracteristicaEfluenteSemFosforo();
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSesDadosEstacaoTipoComposicaoImpl(FceSesDadosEstacaoTratamentoEsgoto localizacaoTratamento) throws Exception {
		fceSesService.excluirFceSesDadosEstacaoTipoComposicaoImpl(localizacaoTratamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSes buscarFceSesByIdeFceSes(FceSes fceSes) throws Exception{
		return fceSesService.buscarFceSesByIdeFceSes(fceSes);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSesDadosElevatoria buscarFceSesDadosElevatoriaByIdeDadosElevatoria(FceSesDadosElevatoria dadosElevatoria) throws Exception{
		return fceSesService.buscarFceSesDadosElevatoriaByIdeDadosElevatoria(dadosElevatoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSesDadosEstacaoTratamentoEsgoto buscarDadosEstacaoTratamentoEsgotoByIdeDadosEstacao(FceSesDadosEstacaoTratamentoEsgoto estacaoTratamento) throws Exception{
		return fceSesService.buscarDadosEstacaoTratamentoEsgotoByIdeDadosEstacao(estacaoTratamento);
	}
}
