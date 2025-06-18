package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.FceIrrigacaoController;
import br.gov.ba.seia.entity.DadoOrigem;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLancamentoEfluente;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaConcedida;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovel;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoBarragem;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoIntervencaoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoGeograficaFinalidadeService;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaConcedidaService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaFinalidadeService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaService;
import br.gov.ba.seia.service.TipoBarragemService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceOutorgaServiceFacade {

	@EJB
	private FceOutorgaLocalizacaoGeograficaService fceOutorgaLocGeoService;
	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	@EJB
	private OutorgaLocalizacaoGeograficaFinalidadeService outorgaLocalizacaoGeograficaFinalidadeService;
	@EJB
	private OutorgaService outorgaService;
	@EJB
	private TipoBarragemService tipoBarragemService;
	@EJB
	private LocalizacaoGeograficaServiceFacade facadeLocalizacaoGeografica;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private FceOutorgaLocalizacaoGeograficaFinalidadeService fceOutorgaLocalizacaoGeograficaFinalidadeService;
	@EJB
	private OutorgaConcedidaService outorgaConcedidaService;
	
	/*
	 * [INI] Métodos para BUSCAR
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaByIdeFce(Fce fce) throws Exception{
		return fceOutorgaLocGeoService.listarFceOutorgaLocGeoByIdeFce(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaAnaliseTecnicaByIdeFce(Fce fce) throws Exception{
		return fceOutorgaLocGeoService.listarFceOutorgaLocGeoAnaliseTecnicaByIdeFce(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaAnaliseTecnicaToOutorgaAquiculturaByIdeFce(Fce fce) throws Exception{
		List<FceOutorgaLocalizacaoGeografica> lista = fceOutorgaLocGeoService.listarFceOutorgaLocGeoAnaliseTecnicaToOutorgaAquiculturaByIdeFce(fce);
		if(!Util.isNullOuVazio(lista)){
			for(FceOutorgaLocalizacaoGeografica fceLocalizacaoGeografica : lista){
				fceLocalizacaoGeografica.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(facadeLocalizacaoGeografica.getDadoGeograficoCollection(fceLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
				if(!Util.isNullOuVazio(fceLocalizacaoGeografica.getNumTempoCaptacao()) || !Util.isNullOuVazio(fceLocalizacaoGeografica.getNomRio())){
					fceLocalizacaoGeografica.setConfirmado(true);
				}
			}
		}
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaBy(TipoCaptacaoEnum tipoCaptacaoEnum, Requerimento requerimento) throws Exception{
		return outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByTipoCaptacao(tipoCaptacaoEnum, requerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaBy(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua, Requerimento requerimento) throws Exception{
		return outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByRequerimentoAndIdeTipoFinalidade(requerimento, tipoFinalidadeUsoAgua);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaDeBarragem(Requerimento requerimento) throws Exception{
		return outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeogaficaDeTipoBarragem(requerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaLancamentoEfluenteBy(Requerimento requerimento) throws Exception{
		return outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaComDadoGeograficoByModalidadeOutorga(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES, requerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaLancamentoEfluenteBy(TipoFinalidadeUsoAgua finalidadeUsoAgua, Requerimento requerimento) throws Exception{
		List<OutorgaLocalizacaoGeografica> listaAquiculturamEmLancamento = new ArrayList<OutorgaLocalizacaoGeografica>();
		for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : listarOutorgaLocalizacaoGeograficaBy(finalidadeUsoAgua, requerimento)){
			if(outorgaLocalizacaoGeografica.getIdeOutorga().getIdeModalidadeOutorga().getIdeModalidadeOutorga().equals(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES.getIdModalidade())){
				listaAquiculturamEmLancamento.add(outorgaLocalizacaoGeografica);
			}
		}
		return listaAquiculturamEmLancamento;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeograficaImovel> listarOutorgaLocalizacaoGeograficaImovel(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) throws Exception{
		return outorgaLocalizacaoGeograficaService.buscarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaTipoIntervencaoBarragem(Requerimento requerimento) throws Exception{
		return outorgaLocalizacaoGeograficaService.listarOutorgaLocGeoByModalidadeOutorgaRequerimentoAndTipoIntervencao(ModalidadeOutorgaEnum.INTERVENCAO, requerimento, TipoIntervencaoEnum.CONSTRUCAO_BARRAGEM);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaTipoIntervencaoAquicultura(Requerimento requerimento) throws Exception{
		return outorgaLocalizacaoGeograficaService.listarOutorgaLocGeoByModalidadeOutorgaRequerimentoAndTipoIntervencao(ModalidadeOutorgaEnum.INTERVENCAO, requerimento, TipoIntervencaoEnum.AQUICULTURA);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoBarragem> listarTipoBarragem() throws Exception{
		return tipoBarragemService.listarTipoBarragemByIndAtivo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void tratarBaciaAndSubBaciaFromCefir(LocalizacaoGeografica locGeo) throws Exception {
		facadeLocalizacaoGeografica.tratarPontoLocalizacaoGeografica(locGeo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void tratarPontosListaOutorgaLocalizacaoGeografica(List<OutorgaLocalizacaoGeografica> lista) throws Exception{
		facadeLocalizacaoGeografica.tratarPontosOutorgaLocalizacaoGeografica(lista);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void tratarPontosListaFceOutorgaLocalizacaoGeografica(List<FceOutorgaLocalizacaoGeografica> lista) throws Exception{
		facadeLocalizacaoGeografica.tratarPontosFceOutorgaLocalizacaoGeografica(lista);
	}
	
	public void tratarSomentePonto(LocalizacaoGeografica localizacaoGeografica){
		facadeLocalizacaoGeografica.tratarPonto(localizacaoGeografica);
	}
	
	/**
	 * Método que monta (e retorna) uma lista de {@link FceOutorgaLocalizacaoGeografica} percorrendo a lista de {@link OutorgaLocalizacaoGeografica} passada no paramêtro.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param listaOutorgaLocalizacaoGeografica
	 * @return
	 * @throws Exception
	 * @since 02/06/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaComPontoBy(List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica, DadoOrigem origemFce) throws Exception {
		List<FceOutorgaLocalizacaoGeografica> lista = listarFceOutorgaLocalizacaoGeograficaBy(listaOutorgaLocalizacaoGeografica, origemFce);
		if(!Util.isNullOuVazio(lista)){
			tratarPontosListaFceOutorgaLocalizacaoGeografica(lista);
		}
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaBy(List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica, DadoOrigem origemFce) throws Exception {
		List<FceOutorgaLocalizacaoGeografica> lista = new ArrayList<FceOutorgaLocalizacaoGeografica>();
		if(!Util.isNullOuVazio(listaOutorgaLocalizacaoGeografica)){
			for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : listaOutorgaLocalizacaoGeografica){
				FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica = fceOutorgaLocGeoService.buscarFceOutorgaLocalizacaoGeograficaByOutorgaLocGeo(outorgaLocalizacaoGeografica, origemFce);
				
				if(!Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica)) {
					lista.add(fceOutorgaLocalizacaoGeografica);
				}
			}
			return lista;
		}
		else {
			return null;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeograficaFinalidade> listarOutorgaLocalizacaoGeograficaFinalidadeBy(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) throws Exception{
		return outorgaLocalizacaoGeograficaFinalidadeService.obterListaOutorgaLocalizacaoGeograficaFinalidades(outorgaLocalizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaCaptacaoSuperficialByFinalidade(Requerimento requerimento, TipoFinalidadeUsoAguaEnum finalidadeUsoAguaEnum) throws Exception{
		return fceOutorgaLocGeoService.listarFceOutorgaLocalizacaoGeograficaBy(requerimento, TipologiaEnum.CAPTACAO_SUPERFICIAL, finalidadeUsoAguaEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaCaptacaoSubterraneaByFinalidade(Requerimento requerimento, TipoFinalidadeUsoAguaEnum finalidadeUsoAguaEnum) throws Exception{
		return fceOutorgaLocGeoService.listarFceOutorgaLocalizacaoGeograficaBy(requerimento, TipologiaEnum.CAPTACAO_SUBTERRANEA, finalidadeUsoAguaEnum); 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaBy(FceLancamentoEfluente fceLancamentoEfluente) throws Exception{
		List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeografica = fceOutorgaLocGeoService.listarFceOutorgaLocalizacaoGeograficaBy(fceLancamentoEfluente);
		tratarPontosListaFceOutorgaLocalizacaoGeografica(listarFceOutorgaLocalizacaoGeografica);
		return listarFceOutorgaLocalizacaoGeografica; 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getBacia(LocalizacaoGeografica localizacaoGeografica){
		return facadeLocalizacaoGeografica.getBacia(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getSubBacia(LocalizacaoGeografica localizacaoGeografica){
		return facadeLocalizacaoGeografica.getSubBacia(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getRpga(LocalizacaoGeografica localizacaoGeografica){
		return facadeLocalizacaoGeografica.getRPGA(localizacaoGeografica);
	}
	/*
	 * [FIM] Métodos para BUSCAR
	 */


	/*
	 * [INI] Métodos de EXCLUSÃO
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) throws Exception{
		outorgaLocalizacaoGeograficaService.excluirOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirOutorgaLocalizacaoGeograficaImovel(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) throws Exception{
		outorgaLocalizacaoGeograficaService.excluirOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeografica);
	}
	/*
	 * [FIM] Métodos de EXCLUSÃO
	 */

	/*
	 * [INI] Métodos para SALVAR
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceOutorgaLocGeo(FceOutorgaLocalizacaoGeografica fceOuorgaLocalizacaoGeografica) throws Exception{
		fceOutorgaLocGeoService.salvarFceOutorgaLocGeo(fceOuorgaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceOutorgaLocalizacaoGeografica(List<FceOutorgaLocalizacaoGeografica> listaFceOuorgaLocalizacaoGeografica) throws Exception{
		fceOutorgaLocGeoService.salvarListaFceOutorgaLocGeo(listaFceOuorgaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOrAtualizarOutorga(Outorga outorga) throws Exception{
		outorgaService.salvarAtualizarOutorga(outorga);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOrAtualizarOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) throws Exception{
		outorgaLocalizacaoGeograficaService.salvarAtualizar(outorgaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOrAtualizarOutorgaLocalizacaoGeograficaImovel(OutorgaLocalizacaoGeograficaImovel outorgaLocalizacaoGeograficaImovel) throws Exception {
		outorgaLocalizacaoGeograficaService.salvarAtualizarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeograficaImovel);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LocalizacaoGeografica duplicarLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) throws Exception{
		return localizacaoGeograficaService.duplicarLocalizacaoGeografica(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceOutorgaLocGeoFinalidade(List<FceOutorgaLocalizacaoGeograficaFinalidade> listaFceOuorgaLocalizacaoGeograficaFinalidade)throws Exception{
		fceOutorgaLocalizacaoGeograficaFinalidadeService.salvarListaFceOutorgaLocalizacaoGeograficaFinalidade(listaFceOuorgaLocalizacaoGeograficaFinalidade);
	}
	
	/**
	 * Método que persiste a lista {@link OutorgaConcedida} no Banco de Dados. 
	 * @author eduardo.fernandes
	 * @throws Exception
	 * @see <a href="http://10.105.12.26/redmine/issues/7550">#7550</a>
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaOutorgaConcedida(List<OutorgaConcedida> listaOutorgaConcedida)throws Exception{
		outorgaConcedidaService.salvarListaOutorgaConcedida(listaOutorgaConcedida);
	}
	
	/*
	 * [FIM] Métodos para SALVAR
	 */

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @param fceLancamentoEfluente
	 * @return
	 * @throws Exception 
	 * @since 21/03/2016
	 */
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocGeoToFceLancamentoEfluentes(FceLancamentoEfluente fceLancamentoEfluente) throws Exception {
		return fceOutorgaLocGeoService.listarFceOutorgaLocalizacaoGeograficaBy(fceLancamentoEfluente);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceIrrigacaoController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
}