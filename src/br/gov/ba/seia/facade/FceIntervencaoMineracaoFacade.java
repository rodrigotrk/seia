/**
 * 
 */
package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.FceIntervencaoMineracaoController;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceIntervencaoCaracteristicaExtracao;
import br.gov.ba.seia.entity.FceIntervencaoMineracao;
import br.gov.ba.seia.entity.FceIntervencaoTipoCaractExtracao;
import br.gov.ba.seia.entity.LancamentoEfluenteLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoCaracteristicaExtracao;
import br.gov.ba.seia.entity.TipoMineralExtraido;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.service.FceIntervencaoCaracteristicaExtracaoService;
import br.gov.ba.seia.service.FceIntervencaoMineracaoService;
import br.gov.ba.seia.service.FceIntervencaoTipoCaractExtracaoService;
import br.gov.ba.seia.service.LancamentoEfluenteLocalizacaoGeograficaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.TipoCaracteristicaExtracaoService;
import br.gov.ba.seia.service.TipoMineralExtraidoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIntervencaoMineracaoFacade {
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	
	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	
	@EJB
	private TipoCaracteristicaExtracaoService tipoCaracteristicaExtracaoService;
	
	@EJB
	private TipoMineralExtraidoService tipoMineralExtraidoService; 
	
	@EJB
	private MunicipioService municipioService; 
	
	@EJB
	private FceIntervencaoCaracteristicaExtracaoService fceIntervencaoCaracteristicaExtracaoService;
	
	@EJB
	private FceIntervencaoMineracaoService fceIntervencaoMineracaoService;
	
	@EJB
	private LancamentoEfluenteLocalizacaoGeograficaService lancamentoEfluenteLocalizacaoGeograficaService;
	
	@EJB
	private FceIntervencaoTipoCaractExtracaoService FceIntervencaoTipoCaractExtracaoService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> getOutorgas(Requerimento requerimento) throws Exception{
		return this.outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByModalidadeOutorga(ModalidadeOutorgaEnum.INTERVENCAO, requerimento);
	}
	
	public String getLatitude(LocalizacaoGeografica localizacaoGeografica){
		localizacaoGeografica.setDadoGeograficoCollection(localizacaoGeograficaServiceFacade.getDadoGeograficoCollection(localizacaoGeografica));
		for(DadoGeografico dg : localizacaoGeografica.getDadoGeograficoCollection()){
			if(!localizacaoGeograficaServiceFacade.getPonto(dg).get("latitude").equals("--")){
				return localizacaoGeograficaServiceFacade.getPonto(dg).get("latitude");
			}
		}
		return null;
	}

	public String getLongitude(LocalizacaoGeografica localizacaoGeografica){
		localizacaoGeografica.setDadoGeograficoCollection(localizacaoGeograficaServiceFacade.getDadoGeograficoCollection(localizacaoGeografica));
		for(DadoGeografico dg : localizacaoGeografica.getDadoGeograficoCollection()){
			if(!localizacaoGeograficaServiceFacade.getPonto(dg).get("longitude").equals("--")){
				return localizacaoGeograficaServiceFacade.getPonto(dg).get("longitude");
			}
		}
		return null;
	}

	public String getBaciaHidrografica(LocalizacaoGeografica localizacaoGeografica){
		return localizacaoGeograficaServiceFacade.getBacia(localizacaoGeografica);
	}

	public String getRpga(LocalizacaoGeografica localizacaoGeografica){
		return localizacaoGeograficaServiceFacade.getRPGA(localizacaoGeografica);
	}
	
	public List<TipoCaracteristicaExtracao> listarCaracteristicaExtracoes() throws Exception{
		return tipoCaracteristicaExtracaoService.listar();
	}
	
	public List<TipoMineralExtraido> listarTipoMineralExtraido() throws Exception{
		return tipoMineralExtraidoService.listar();
	}

	public Municipio getMunicipio(LocalizacaoGeografica localizacaoGeografica) throws Exception{
		return municipioService.carregarMunicipioByLocalizacaoGeografica(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceIntervencaoCaracteristicaExtracao(FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracao) throws Exception{
		if(fceIntervencaoCaracteristicaExtracao.getIdeFceIntervencaoCaracteristicaExtracao() != null){
			this.fceIntervencaoCaracteristicaExtracaoService.atualizar(fceIntervencaoCaracteristicaExtracao);
			this.removerFceIntervencaoTipoCaractExtracaoServiceOrfaos(fceIntervencaoCaracteristicaExtracao);
		}else{
			this.fceIntervencaoCaracteristicaExtracaoService.salvar(fceIntervencaoCaracteristicaExtracao);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void removerFceIntervencaoTipoCaractExtracaoServiceOrfaos(FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracao) throws Exception{
		 List<FceIntervencaoTipoCaractExtracao> dataDbo = this.FceIntervencaoTipoCaractExtracaoService.listarByFceIntervencaoCaracteristicaExtracao(fceIntervencaoCaracteristicaExtracao);
		 for(FceIntervencaoTipoCaractExtracao f : dataDbo){
			 if(!fceIntervencaoCaracteristicaExtracao.getFceIntervencaoTipoCaracticasExtracao().contains(f)){
				 this.FceIntervencaoTipoCaractExtracaoService.remover(f);
			 }
		 }
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceIntervencaoMineracao(FceIntervencaoMineracao fceIntervencaoMineracao) throws Exception{
		if(Util.isNullOuVazio(fceIntervencaoMineracao.getLancamentoEfluenteLocalizacoesGeografica())){
			fceIntervencaoMineracao.setDscTratamentoEfluente("");
		}
		if(fceIntervencaoMineracao.getIdeFceIntervencaoMineracao() != null){
			this.fceIntervencaoMineracaoService.atualizar(fceIntervencaoMineracao);
		}else{
			this.fceIntervencaoMineracaoService.salvar(fceIntervencaoMineracao);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarLancamentoEfluenteLocalizacaoGeografica(LancamentoEfluenteLocalizacaoGeografica lancamentoEfluenteLocalizacaoGeografica) {
		try {
			this.lancamentoEfluenteLocalizacaoGeograficaService.salvarOuAtualizar(lancamentoEfluenteLocalizacaoGeografica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceIntervencaoMineracao getFceIntervencaoMineracao(Fce fce) throws Exception{
		FceIntervencaoMineracao fceIntervencaoMineracao = this.fceIntervencaoMineracaoService.buscarPorFce(fce);
		
		if(!Util.isNullOuVazio(fceIntervencaoMineracao) && !Util.isNullOuVazio(fceIntervencaoMineracao.getLancamentoEfluenteLocalizacoesGeografica())){
			for(LancamentoEfluenteLocalizacaoGeografica l : fceIntervencaoMineracao.getLancamentoEfluenteLocalizacoesGeografica()){
				l.getIdeLocalizacaoGeografica().setLatitudeInicial(this.getLatitude(l.getIdeLocalizacaoGeografica()));
				l.getIdeLocalizacaoGeografica().setLongitudeInicial(this.getLongitude(l.getIdeLocalizacaoGeografica()));
				l.setEditar(false);
			}
		}
		
		return fceIntervencaoMineracao;
	}

	public void excluir(LancamentoEfluenteLocalizacaoGeografica lancamentoEfluenteLocalizacaoGeografica) throws Exception{
		this.lancamentoEfluenteLocalizacaoGeograficaService.excluir(lancamentoEfluenteLocalizacaoGeografica);
	}

	public void excluir(LocalizacaoGeografica localizacaoGeografica) throws Exception{
		this.localizacaoGeograficaServiceFacade.excluirTudoPorLocalizacaoGeografica(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceIntervencaoMineracaoController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LocalizacaoGeografica duplicarLocalizacaoGeografica(LocalizacaoGeografica loc) throws Exception {
		return localizacaoGeograficaServiceFacade.duplicarLocalizacaoGeografica(loc);
	}
}
