package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.CaracteristicaEfluente;
import br.gov.ba.seia.entity.CaracterizacaoEfluente;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLancamentoEfluente;
import br.gov.ba.seia.entity.FceLancamentoEfluenteCaracteristica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.service.CaracteristicaEfluenteService;
import br.gov.ba.seia.service.CaracterizacaoEfluenteService;
import br.gov.ba.seia.service.FceLancamentoEfluentesCaracteristicaService;
import br.gov.ba.seia.service.FceLancamentoEfluentesService;
import br.gov.ba.seia.service.TipoPeriodoDerivacaoService;
import br.gov.ba.seia.util.Util;

/**
 *
 * @author eduardo.fernandes
 * @since 22/04/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLancamentoEfluentesServiceFacade {

	@EJB
	private TipoPeriodoDerivacaoService tipoPeriodoDerivacaoService;
	@EJB
	private CaracteristicaEfluenteService caracteristicaEfluenteService;
	@EJB
	private FceLancamentoEfluentesCaracteristicaService fceLancamentoEfluentesCaracteristicaService;
	@EJB
	private CaracterizacaoEfluenteService caracterizacaoEfluenteService;
	@EJB
	private FceOutorgaServiceFacade fceOutorgaServiceFacade;
	@EJB
	private FceServiceFacade fceServiceFacade;
	@EJB
	private FceLancamentoEfluentesService fceLancamentoEfluentesService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracterizacaoEfluente> listarCaracterizacaoElfuentes() throws Exception {
		return caracterizacaoEfluenteService.listarCaracterizacaoEfluente();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaEfluente> listarCaracteristicaEfluentes() throws Exception {
		return caracteristicaEfluenteService.listarTodosCaracteristicaEfluente();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLancamentoEfluenteCaracteristica> listarFceLancamentoEfluenteCaracteristicaBy(FceLancamentoEfluente fceLancamentoEfluente) throws Exception {
		return fceLancamentoEfluentesCaracteristicaService.buscarFceLancamentoEfluenteCaracteristicaByFceLancEflu(fceLancamentoEfluente);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaEfluente> listarCaracteristicaEfluentesSemFosforo() throws Exception {
		return caracteristicaEfluenteService.listarCaracteristicaEfluenteSemFosforo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoPeriodoDerivacao> listarTipoPeriodoDerivacao() throws Exception {
		return tipoPeriodoDerivacaoService.listarTipoPeriodoDerivacao();
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaByIdeFce(Fce fce) throws Exception{
		return fceOutorgaServiceFacade.listarFceOutorgaLocalizacaoGeograficaByIdeFce(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaByFceLancamentoEfluentes(FceLancamentoEfluente fceLancamentoEfluente) throws Exception{
		return fceOutorgaServiceFacade.listarFceOutorgaLocGeoToFceLancamentoEfluentes(fceLancamentoEfluente);
	}

	public FceLancamentoEfluente carregarFceLancamentoEfluenteBy(FceLancamentoEfluente fceLancamentoEfluente) throws Exception {
		return fceLancamentoEfluentesService.buscarFceLancamentoEfluentesByFceLancamentoEfluente(fceLancamentoEfluente);
	}

	public FceLancamentoEfluente carregarFceLancamentoEfluenteBy(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) throws Exception {
		return fceLancamentoEfluentesService.buscarLancamentoEfluenteByIdeFceOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) throws Exception {
		if(Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeFce())){
			fceServiceFacade.salvarFce(fceOutorgaLocalizacaoGeografica.getIdeFce());
		}
		fceOutorgaLocalizacaoGeografica.setIdeFceLancamentoEfluente(null);
		fceOutorgaServiceFacade.salvarFceOutorgaLocGeo(fceOutorgaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLancamentoEfluentes(FceLancamentoEfluente fceLancamentoEfluente) throws Exception {
		fceOutorgaServiceFacade.salvarFceOutorgaLocGeo(fceLancamentoEfluente.getIdeFceOutorgaLocalizacaoGeografica());
		fceLancamentoEfluentesService.salvarFceLancamentoEfluentes(fceLancamentoEfluente);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceLancamentoEfluentesCaracteristica(List<FceLancamentoEfluenteCaracteristica> listaFceLancamentoEfluenteCaracteristica) throws Exception {
		fceLancamentoEfluentesCaracteristicaService.salvarFceLancamentoEfluenteCaracteristica(listaFceLancamentoEfluenteCaracteristica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceOutorgaLocalizacaoGeografica(List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLocalizacaoGeografica) throws Exception {
		fceOutorgaServiceFacade.salvarListaFceOutorgaLocalizacaoGeografica(listaFceOutorgaLocalizacaoGeografica);
	}
}