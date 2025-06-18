package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.FceCanalController;
import br.gov.ba.seia.entity.CaracteristicaCanal;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceCanal;
import br.gov.ba.seia.entity.FceCanalTipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.FceCanalTrecho;
import br.gov.ba.seia.entity.FceCanalTrechoSecaoGeometrica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.SecaoGeometrica;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.entity.TipoCanal;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoRevestimento;
import br.gov.ba.seia.entity.TipoRio;
import br.gov.ba.seia.service.CaracteristicaCanalService;
import br.gov.ba.seia.service.FceCanalService;
import br.gov.ba.seia.service.FceCanalTipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.FceCanalTrechoSecaoGeometricaService;
import br.gov.ba.seia.service.FceCanalTrechoService;
import br.gov.ba.seia.service.FceService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.SecaoGeometricaService;
import br.gov.ba.seia.service.SistemaCoordenadaService;
import br.gov.ba.seia.service.TipoCanalService;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.TipoRevestimentoService;
import br.gov.ba.seia.service.TipoRioService;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCanalServiceFacade {

	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;
	
	@EJB
	private CaracteristicaCanalService caracteristicaCanalService;
	
	@EJB
	private SistemaCoordenadaService sistemaCoordenadaService;
	
	@EJB
	private SecaoGeometricaService secaoGeometricaService;
	
	@EJB
	private TipoCanalService tipoCanalService;
	
	@EJB
	private TipoRevestimentoService tipoRevestimentoService;
	
	@EJB
	private TipoRioService tipoRioService;
	
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade locGeoFacade;
	
	@EJB
	private FceCanalService fceCanalService;
	
	@EJB
	private FceService fceService;
	
	@EJB
	private FceCanalTrechoService fceCanalTrechoService;
	
	@EJB
	private FceCanalTrechoSecaoGeometricaService fceCanalTrechoSecaoGeometricaService; 
	
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;

	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	
	@EJB
	private FceCanalTipoFinalidadeUsoAguaService fceCanalTipoFinalidadeUsoAguaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAgua() throws Exception{
		return tipoFinalidadeUsoAguaService.listarTipoFinalidadeUsoAguaRequerimentoAtivo();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaCanal> listarCaracteristicasCanal() throws Exception{
		return caracteristicaCanalService.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SistemaCoordenada> listarSistemasCoordenadas() throws Exception{
		return sistemaCoordenadaService.listar();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoCanal> listarTiposCanal() throws Exception{
		return tipoCanalService.listar();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SecaoGeometrica> listarSecoesGeometricas() throws Exception{
		return secaoGeometricaService.listar();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoRevestimento> listarTipoRevestimento() throws Exception{
		return tipoRevestimentoService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoRio> listarTipoRio() throws Exception{
		return tipoRioService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> listarMunicipio(LocalizacaoGeografica localizacaoGeografica) throws Exception{
		Collection<Double> cdIBGE = locGeoFacade.listarCodIBGE(localizacaoGeografica.getIdeLocalizacaoGeografica());
		if(!Util.isNullOuVazio(cdIBGE)){
			return (List<Municipio>) municipioService.listarMunicipioPorListaIBGE(cdIBGE);
		}
		return new ArrayList<Municipio>();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararParaSalvar(FceCanalController ctrl) throws Exception {
		ctrl.salvarFceCanais();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararParaFinalizar(FceCanalController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceCanal(FceCanal fceCanal) throws Exception{
		addMunicipiosDbo(fceCanal);
		
		if(Util.isNull(fceCanal.getIdeFceCanal())){
			fceCanalService.salvarFceCanal(fceCanal);
			salvarAtualizarTrecho(fceCanal);
			salvarFceCanalTiposFinalidadesUsoAgua(fceCanal);
		}else{
			fceCanalService.atualizarFceCanal(fceCanal);
			salvarAtualizarTrecho(fceCanal);
//			deletarTrechos(fceCanal);
			deletarFceCanalTipoFinalidadeUsoAgua(fceCanal);
			salvarFceCanalTiposFinalidadesUsoAgua(fceCanal);
		}
	}
	
	private void salvarFceCanalTiposFinalidadesUsoAgua(FceCanal fceCanal) throws Exception{
		for(FceCanalTipoFinalidadeUsoAgua t : fceCanal.getFceCanalTiposFinalidadesUsoAgua()){
			if(t.getIdeFceCanalTipoFinalidadeUsoAgua() == null){
				fceCanalTipoFinalidadeUsoAguaService.salvarFceCanalTipoFinalidadeUsoAguasPorCanal(t);
			}
		}
	}
	
	private void addMunicipiosDbo(FceCanal fceCanal) throws Exception{
		List<Municipio> municipios = new ArrayList<Municipio>();
		for(Municipio municipio : fceCanal.getMunicipios()){
			municipios.add(municipioService.obterMunicipio(municipio.getIdeMunicipio()));
		}
		fceCanal.setMunicipios(municipios);
	}
	
	private void salvarAtualizarTrecho(FceCanal fceCanal) throws Exception{
		for (FceCanalTrecho trecho : fceCanal.getCanalTrechos()) {
			if(Util.isNullOuVazio(trecho.getIdFceCanalTrecho())) {
				fceCanalTrechoService.salvarFceCanalTrecho(trecho);
			} else {
				//remover trecho Seção 
				removerFceCanalTrechoSecaoGeometrica(fceCanal);
				fceCanalTrechoService.atualizarFceCanalTrecho(trecho);
			}
		}
	}
	
	private void removerFceCanalTrechoSecaoGeometrica(FceCanal fceCanal) throws Exception{
		for(FceCanalTrecho trecho : fceCanal.getCanalTrechos()){
			if(!Util.isNullOuVazio(trecho.getIdFceCanalTrecho())){
				for(FceCanalTrechoSecaoGeometrica item : fceCanalTrechoService.getFceCanalTrechoSecaoGeometricaProjetado(trecho)){
					if(!trecho.getFceCanalTrechoSecaoGeometrica().contains(item)){
						fceCanalTrechoSecaoGeometricaService.removerFceCanalTrechoSecaoGeometrica(item);
					}
				}
			}
		}
	}
	
	private void deletarFceCanalTipoFinalidadeUsoAgua(FceCanal fceCanal) throws Exception{
		//remover Tipos de Uso de água excluidos pelo usuário
		for(FceCanalTipoFinalidadeUsoAgua t :fceCanalTipoFinalidadeUsoAguaService.listarFceCanalTipoFinalidadeUsoAguasPorCanal(fceCanal)){
			if(!fceCanal.getFceCanalTiposFinalidadesUsoAgua().contains(t)){
				fceCanalTipoFinalidadeUsoAguaService.deletarFceCanalTipoFinalidadeUsoAguasPorCanal(t);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void deletarTrechos(FceCanal fceCanal) throws Exception{
		//remover Trechos excluidos pelo usuário
		for(FceCanalTrecho t :fceCanalTrechoService.listarPorFceCanal(fceCanal)){
			if(!fceCanal.getCanalTrechos().contains(t)){
				t.setTiposRevestimentos(null);
				for(FceCanalTrechoSecaoGeometrica item : t.getFceCanalTrechoSecaoGeometrica()){
					fceCanalTrechoSecaoGeometricaService.removerFceCanalTrechoSecaoGeometrica(item);
				}
				t.setFceCanalTrechoSecaoGeometrica(null);
				fceCanalTrechoService.atualizarFceCanalTrecho(t);
				fceCanalTrechoService.deleteFceCanalTrecho(t);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceCanal getFceCanal(Fce fce) throws Exception {
		FceCanal fceCanal = fceCanalService.buscarPorFce(fce);
		if(!Util.isNullOuVazio(fceCanal)){
			//municipios
			fceCanal.setMunicipios(municipioService.listaMunicipiosPorFceCanal(fceCanal));
			//tipo uso água
			fceCanal.setFceCanalTiposFinalidadesUsoAgua(fceCanalTipoFinalidadeUsoAguaService.listarFceCanalTipoFinalidadeUsoAguasPorCanal(fceCanal));
			//
			fceCanal.setFce(fce);
			//canalTrechos
			fceCanal.setCanalTrechos(fceCanalTrechoService.listarPorFceCanal(fceCanal));
		}
		return fceCanal;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RequerimentoTipologia getRequerimentoTipologia(Requerimento requerimento) throws Exception{
		return requerimentoTipologiaService.buscarRequerimentoTipologiaPrincipal(requerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PerguntaRequerimento getPerguntaRequerimento(Requerimento requerimento, String codPergunta) throws Exception{
		return this.perguntaRequerimentoService.buscarPerguntaRequerimentoPorRequerimentoECodPergunta(requerimento, codPergunta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceCanalTrecho(FceCanalTrecho trecho) throws Exception {
		fceCanalTrechoService.removerFceCanalTrecho(trecho);
	}
	
}
