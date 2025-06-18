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

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.FceAutorizacaoMineracaoController;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DestinoResiduo;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralDestinoResiduo;
import br.gov.ba.seia.entity.FcePesquisaMineralMetodoRecuperacao;
import br.gov.ba.seia.entity.FcePesquisaMineralOrigemEnergia;
import br.gov.ba.seia.entity.FcePesquisaMineralOrigemEnergiaPK;
import br.gov.ba.seia.entity.FcePesquisaMineralProspeccao;
import br.gov.ba.seia.entity.FcePesquisaMineralProspeccaoGeofisica;
import br.gov.ba.seia.entity.FcePesquisaMineralSubstanciaMineralTipologia;
import br.gov.ba.seia.entity.FcePesquisaMineralSubstanciaMineralTipologiaPK;
import br.gov.ba.seia.entity.FcePesquisaMineralTipoResiduo;
import br.gov.ba.seia.entity.FceProspeccao;
import br.gov.ba.seia.entity.Geofisica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MetodoProspeccao;
import br.gov.ba.seia.entity.MetodoRecuperacaoIntervencao;
import br.gov.ba.seia.entity.OutorgaMineracao;
import br.gov.ba.seia.entity.ProcessoDnpm;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoOrigemEnergia;
import br.gov.ba.seia.entity.TipoResiduoGerado;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.MetodoProspeccaoEnum;
import br.gov.ba.seia.service.FcePesquisaMineralDestinoResiduoService;
import br.gov.ba.seia.service.FcePesquisaMineralMetodoRecuperacaoService;
import br.gov.ba.seia.service.FcePesquisaMineralOrigemEnergiaService;
import br.gov.ba.seia.service.FcePesquisaMineralProspeccaoGeofisicaService;
import br.gov.ba.seia.service.FcePesquisaMineralProspeccaoService;
import br.gov.ba.seia.service.FcePesquisaMineralService;
import br.gov.ba.seia.service.FcePesquisaMineralSubstanciaMineralService;
import br.gov.ba.seia.service.FcePesquisaMineralTipoResiduoService;
import br.gov.ba.seia.service.FceProspeccaoService;
import br.gov.ba.seia.service.GeofisicaService;
import br.gov.ba.seia.service.MetodoProspeccaoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * FACADE responsável por gerenciar as transações do <b>FCE - Mineração</b>
 * 
 * @author eduardo.fernandes
 * @since 09/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAutorizacaoMineracaoFacade extends FceMineracaoFacade {
	
	private static final Integer ABA_DADOS_GERAIS = 0;
	private static final Integer ABA_ATIVIDADES_PESQUISA = 1;
	private static final Integer ABA_ASPECTOS_AMBIENTAIS = 2;
	
	@EJB
	private FcePesquisaMineralService fcePesquisaMineralService;
	
	@EJB
	private FcePesquisaMineralOrigemEnergiaService fcePesquisaMineralOrigemEnergiaService;
	
	@EJB
	private FcePesquisaMineralSubstanciaMineralService fcePesquisaMineralSubstanciaMineralService;

	@EJB
	private FcePesquisaMineralProspeccaoService fcePesquisaMineralProspeccaoService;
	
	@EJB
	private FcePesquisaMineralTipoResiduoService fcePesquisaMineralTipoResiduoService;
	
	@EJB 
	private FcePesquisaMineralDestinoResiduoService fcePesquisaMineralDestinoResiduoService;

	@EJB
	private FceProspeccaoService fceProspeccaoService;
	
	@EJB
	private MetodoProspeccaoService metodoProspeccaoService;
	
	@EJB
	private FcePesquisaMineralMetodoRecuperacaoService fcePesquisaMineralMetodoRecuperacaoService;
	
	@EJB
	private GeofisicaService geofisicaService;
	
	@EJB
	private FcePesquisaMineralProspeccaoGeofisicaService fcePesquisaMineralProspeccaoGeofisicaService;
	
	@EJB
	private FcePesquisaMineralTipoResiduoService fceTipoResiduoGeradoService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceAutorizacaoMineracaoController cfpm) throws Exception{
		
		salvarFce(cfpm.getFce());				
		cfpm.getIdeFcePesquisaMineral().setIdeFce(cfpm.getFce());
		salvarFcePesquisaMineral(cfpm.getIdeFcePesquisaMineral());
			
		if(cfpm.getActiveTab() == ABA_DADOS_GERAIS){
			salvarAbaDadosGerais(cfpm);
		}else if(cfpm.getActiveTab() == ABA_ATIVIDADES_PESQUISA){
			salvarAtividadesPesquisa(cfpm);
		}else if(cfpm.getActiveTab() == ABA_ASPECTOS_AMBIENTAIS){
			salvarAbaDadosGerais(cfpm);
			salvarAtividadesPesquisa(cfpm);
			salvarAspectosAmbientais(cfpm);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarAbaDadosGerais(FceAutorizacaoMineracaoController cfpm) {
		salvarListaFceSubstanciaMineral(cfpm.getListaFcePesquisaMineralSubstanciaMineral(), cfpm.getIdeFcePesquisaMineral());
		salvarListaProcessoDnpm(cfpm.getListaProcessoDnmp(), cfpm.getIdeFcePesquisaMineral());
		salvarListaFceSuprimentoEnergia(cfpm.getListaTipoOrigemEnergia(), cfpm.getIdeFcePesquisaMineral());
		salvarOutorgaMineracao(cfpm.getListaOutorgaMineracao(),cfpm.getIdeFcePesquisaMineral() );		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarAtividadesPesquisa(FceAutorizacaoMineracaoController cfpm) {
		salvarMetodosPropeccao(cfpm);
		salvarGeofisica(cfpm);
		salvarRecuperacaoAreaIntervencao(cfpm);
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarAspectosAmbientais(FceAutorizacaoMineracaoController cfpm) {
		salvarTipoResiduosGerado(cfpm);
		salvarDestinoResiduos(cfpm);	
	}
	
	/*Dados Gerais*/
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceSubstanciaMineral(List<FcePesquisaMineralSubstanciaMineralTipologia> listFcePesquisaMineralSubstanciaMineral, FcePesquisaMineral ideFcePesquisaMineral){
		try{
			fcePesquisaMineralSubstanciaMineralService.excluirFcePesquisaMineralSubstanciaMineral(ideFcePesquisaMineral);
			for (FcePesquisaMineralSubstanciaMineralTipologia fcePesquisaMineralSubstanciaMineral : listFcePesquisaMineralSubstanciaMineral) {
				FcePesquisaMineralSubstanciaMineralTipologiaPK fcePesquisaMineralSubstanciaMineralPK =  new  FcePesquisaMineralSubstanciaMineralTipologiaPK();
				fcePesquisaMineralSubstanciaMineralPK.setIdeFcePesquisaMineral(ideFcePesquisaMineral.getIdeFcePesquisaMineral());
				fcePesquisaMineralSubstanciaMineralPK.setIdeSubstanciaMineralTipologia(fcePesquisaMineralSubstanciaMineral.getSubstanciaMineralTipologia().getIdeSubstanciaMineralTipologia());
				fcePesquisaMineralSubstanciaMineral.setIdeFcePesquisaMineralSubstanciaMineralPK(fcePesquisaMineralSubstanciaMineralPK);
				fcePesquisaMineralSubstanciaMineral.getIdeFcePesquisaMineralSubstanciaMineralPK().setIdeFcePesquisaMineral(ideFcePesquisaMineral.getIdeFcePesquisaMineral());
			}
			fcePesquisaMineralSubstanciaMineralService.salvarFcePesquisaMineralSubstanciaMineralByNativeQuery(listFcePesquisaMineralSubstanciaMineral);
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaProcessoDnpm(List<ProcessoDnpm> listaProcessoDnmp, FcePesquisaMineral ideFcePesquisaMinera) {
		try{
			for (ProcessoDnpm processoDnpm: listaProcessoDnmp){
				processoDnpm.setIdeFcePesquisaMineral(ideFcePesquisaMinera);
			}
			super.processoDnpmService.salvarListaProcessoDnpm(listaProcessoDnmp);
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarOutorgaMineracao(List<OutorgaMineracao> listaOutorgaMineracao, FcePesquisaMineral fcePesquisaMineral){
		try {
		
			if(!Util.isNullOuVazio(listaOutorgaMineracao)){
				for(OutorgaMineracao outorgaMineracao: listaOutorgaMineracao ){
					outorgaMineracao.setFcePesquisaMineral(fcePesquisaMineral);
				}
				outorgaMineracaoService.salvarListaOutorgaMineracao(listaOutorgaMineracao);
			}
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceSuprimentoEnergia(List<TipoOrigemEnergia> listaTipoOrigemEnergia, FcePesquisaMineral ideFcePesquisaMineral){
		List<FcePesquisaMineralOrigemEnergia > listaFcePesquisaMineralOrigemEnergia = new ArrayList<FcePesquisaMineralOrigemEnergia>();
		
		try{
			
			for (TipoOrigemEnergia tipoOrigemEnergia : listaTipoOrigemEnergia) {
				if(tipoOrigemEnergia.isChecked()){
					FcePesquisaMineralOrigemEnergiaPK fcePesquisaMineralOrigemEnergiaPK = new FcePesquisaMineralOrigemEnergiaPK();
					fcePesquisaMineralOrigemEnergiaPK.setIdeFcePesquisaMineral(ideFcePesquisaMineral.getIdeFcePesquisaMineral());
					fcePesquisaMineralOrigemEnergiaPK.setIdeTipoOrigemEnergia(tipoOrigemEnergia.getIdeTipoOrigemEnergia());
					FcePesquisaMineralOrigemEnergia fcePesquisaMineralOrigemEnergia = new FcePesquisaMineralOrigemEnergia();
					fcePesquisaMineralOrigemEnergia.setIdeFcePesquisaMineralOrigemEnergiaPK(fcePesquisaMineralOrigemEnergiaPK);
					listaFcePesquisaMineralOrigemEnergia.add(fcePesquisaMineralOrigemEnergia);
				}
			}
			
			fcePesquisaMineralOrigemEnergiaService.excluirFceSuprimentoEnergia(ideFcePesquisaMineral);
			fcePesquisaMineralOrigemEnergiaService.salvarFcePesquisaMineralOrigemEnergiaByNativeQuery(listaFcePesquisaMineralOrigemEnergia, ideFcePesquisaMineral);
			listaFcePesquisaMineralOrigemEnergia.clear();
		
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/*Atividades E pesquisa*/
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarMetodosPropeccao(FceAutorizacaoMineracaoController cfpm) {
		
		List<FcePesquisaMineralProspeccao> listaParaSalvar = new ArrayList<FcePesquisaMineralProspeccao>();
		List<FceProspeccao> listaFceProspeccao = new ArrayList<FceProspeccao>();
		FcePesquisaMineralProspeccao fp = new FcePesquisaMineralProspeccao();
		try{
			
			excluirMetodosDeProspeccaoNaoSelecionados(cfpm);
			
			for(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao: cfpm.getListaFcePesquisaMineralProspeccao()){		
				for(MetodoProspeccao metodoProspeccao: cfpm.getListaMetodoProspeccao()){
					if(metodoProspeccao.getIdeMetodoProspeccao().equals(fcePesquisaMineralProspeccao.getIdeMetodoProspeccao().getIdeMetodoProspeccao()) && (metodoProspeccao.isChecked())){
						fp = new FcePesquisaMineralProspeccao(cfpm.getIdeFcePesquisaMineral(), fcePesquisaMineralProspeccao.getIdeMetodoProspeccao());
						fp.setListaFceProspeccao(fcePesquisaMineralProspeccao.getListaFceProspeccao());
						listaParaSalvar.add(fp);
					}
				}
			}
		
			for(FcePesquisaMineralProspeccaoGeofisica fcePesquisaMineralProspeccaoGeofisica: fcePesquisaMineralProspeccaoGeofisicaService.listarFcePesquisaMineralProspeccaoGeofisicaByFcePesquisaMineral(cfpm.getIdeFcePesquisaMineral())){
				fcePesquisaMineralProspeccaoGeofisicaService.excluirIdeFcePesquisaMineralProspeccaoGeofisicaBy(fcePesquisaMineralProspeccaoGeofisica);
			}
			
			for(FceProspeccao fceProspeccao: listarFceProspeccaoByIdeFcePesquisaMineral(cfpm.getIdeFcePesquisaMineral())){
				fceProspeccaoService.excluirFceProspeccao(fceProspeccao);	
			}
			
			if(!Util.isNullOuVazio(cfpm.getListaFcePesquisaMineralProspeccaoExclusao())) {
				for(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccaoExclusao: cfpm.getListaFcePesquisaMineralProspeccaoExclusao()) {
					
					fceProspeccaoService.excluirFceProspeccaoBy(fcePesquisaMineralProspeccaoExclusao);
				}
				
				cfpm.getListaFcePesquisaMineralProspeccaoExclusao().clear();
			}
			
			fcePesquisaMineralProspeccaoService.excluirFcePesquisaMineralProspeccao(cfpm.getIdeFcePesquisaMineral());
			fcePesquisaMineralProspeccaoService.salvarFcePesquisaMineralProspeccao(listaParaSalvar);
			
			for(FcePesquisaMineralProspeccao f : listaParaSalvar){
				if(f.getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(MetodoProspeccaoEnum.GEOFISICA.getId())){
					cfpm.setFcePesquisaMineralProspeccao(f);
				}
			}
			
			cfpm.setListaFcePesquisaMineralProspeccaoExclusao(new ArrayList<FcePesquisaMineralProspeccao>(listaParaSalvar));
			
			for(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao: listaParaSalvar){	
				for(FceProspeccao fceProspeccao: fcePesquisaMineralProspeccao.getListaFceProspeccao()){
					 
					 FceProspeccao fceP = fceProspeccao.clone();	
					
					 fceP.setIdeFcePesquisaMineralProspeccao(fcePesquisaMineralProspeccao) ;
					 listaFceProspeccao.add(fceP);
					 fceP.setIdeFceProspeccao(null);
					 
				}
			}
			fceProspeccaoService.salvarListaFceProspeccao(listaFceProspeccao);
			
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
		
	public void excluirMetodosDeProspeccaoNaoSelecionados(FceAutorizacaoMineracaoController cfpm) throws Exception{
		List<MetodoProspeccao> metodoProspeccaoParaExcluir = new ArrayList<MetodoProspeccao>();
		
		for(MetodoProspeccao metodoProspeccao: cfpm.getListaMetodoProspeccao()){
			if(!metodoProspeccao.isChecked()){				
				metodoProspeccaoParaExcluir.add(metodoProspeccao);
			}
		}
		
		for(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao : cfpm.getListaFcePesquisaMineralProspeccao()){
			for(MetodoProspeccao metodoProspeccao: metodoProspeccaoParaExcluir){
				if(fcePesquisaMineralProspeccao.getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(metodoProspeccao.getIdeMetodoProspeccao())){
					if(metodoProspeccao.getIdeMetodoProspeccao().equals(MetodoProspeccaoEnum.GEOFISICA.getId())){
						List<FcePesquisaMineralProspeccaoGeofisica> fcePesquisaMineralPropeccaoGeofisica = fcePesquisaMineralProspeccaoGeofisicaService.listarFcePesquisaMineralProspeccaoGeofisicaByFcePesquisaMineral(cfpm.getIdeFcePesquisaMineral());
						for (FcePesquisaMineralProspeccaoGeofisica fcePesquisaMineralProspeccaoGeofisica :fcePesquisaMineralPropeccaoGeofisica) {
							fcePesquisaMineralProspeccaoGeofisicaService.excluirIdeFcePesquisaMineralProspeccaoGeofisicaBy(fcePesquisaMineralProspeccaoGeofisica);
						}
					}
					fceProspeccaoService.excluirFceProspeccaoBy(fcePesquisaMineralProspeccao);
					fcePesquisaMineralProspeccaoService.excluirFcePesquisaMineralProspeccao(fcePesquisaMineralProspeccao);
				}
			}
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarGeofisica(FceAutorizacaoMineracaoController cfpm){
		try{
			List<FcePesquisaMineralProspeccaoGeofisica> listaParaSalvar = new ArrayList<FcePesquisaMineralProspeccaoGeofisica>();
			
			if(isGeofisicaSelecionada(cfpm)){
				for (Geofisica geofisica : cfpm.getListaGeofisca()){
					if(geofisica.isChecked() ){		
						
						listaParaSalvar.add(new FcePesquisaMineralProspeccaoGeofisica(geofisica,cfpm.getFcePesquisaMineralProspeccao()));
					}
				}			
				fcePesquisaMineralProspeccaoGeofisicaService.salvarListaFcePesquisaMineralProspeccao(listaParaSalvar);
			}
		
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isGeofisicaSelecionada(FceAutorizacaoMineracaoController cfpm){
		for(MetodoProspeccao metodoProspeccao : cfpm.getListaMetodoProspeccao()){
			if(metodoProspeccao.getIdeMetodoProspeccao().equals(MetodoProspeccaoEnum.GEOFISICA.getId())){
				return metodoProspeccao.isChecked();				
			}
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarRecuperacaoAreaIntervencao(FceAutorizacaoMineracaoController cfpm){
		List<FcePesquisaMineralMetodoRecuperacao> listaMetodoRecuperacaoIntervencao = new ArrayList<FcePesquisaMineralMetodoRecuperacao>();
		
		try{
			for (MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao : cfpm.getListaMetodoRecuperacaoIntervencao()) {
				if(metodoRecuperacaoIntervencao.isChecked()){
					listaMetodoRecuperacaoIntervencao.add(new FcePesquisaMineralMetodoRecuperacao(cfpm.getIdeFcePesquisaMineral(),metodoRecuperacaoIntervencao));
				}
			}
			fcePesquisaMineralMetodoRecuperacaoService.excluirFcePesquisaMineralMetodoRecuperacaoBy(cfpm.getIdeFcePesquisaMineral());
			fcePesquisaMineralMetodoRecuperacaoService.salvarListaFcePesquisaMineralMetodoRecuperacaoNativeQuery(listaMetodoRecuperacaoIntervencao);
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarTipoResiduosGerado(FceAutorizacaoMineracaoController cfpm){
		List<FcePesquisaMineralTipoResiduo> fcePesquisaMineralTipoResiduo = new ArrayList<FcePesquisaMineralTipoResiduo>();
		try {
			for (TipoResiduoGerado tipoResiduosGerado : cfpm.getListaTipoResiduoGerado()) {
				if(tipoResiduosGerado.isChecked()){
					fcePesquisaMineralTipoResiduo.add(new FcePesquisaMineralTipoResiduo(cfpm.getIdeFcePesquisaMineral(), tipoResiduosGerado));
				}
			}
			fceTipoResiduoGeradoService.excluirIdeFcePesquisaMineralTipoResiduo(cfpm.getIdeFcePesquisaMineral());
			fceTipoResiduoGeradoService.salvarListaFcePesquisaMineralTipoResiduo(fcePesquisaMineralTipoResiduo);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	 
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarDestinoResiduos(FceAutorizacaoMineracaoController cfpm){
		List<FcePesquisaMineralDestinoResiduo> fcePesquisaMineralDestinoResiduo = new ArrayList<FcePesquisaMineralDestinoResiduo>();
		try {
			
			for (DestinoResiduo destinoResiduo : cfpm.getListaDestinoResiduo()) {
				if(destinoResiduo.isChecked()){
					fcePesquisaMineralDestinoResiduo.add(new FcePesquisaMineralDestinoResiduo(cfpm.getIdeFcePesquisaMineral(),destinoResiduo ));
				}
			}
			
			fcePesquisaMineralDestinoResiduoService.excluirFcePesquisaMineralDestinoResiduoBy(cfpm.getIdeFcePesquisaMineral());
			fcePesquisaMineralDestinoResiduoService.salvarListaFcePesquisaMineralDestinoResiduo(fcePesquisaMineralDestinoResiduo);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isEnquadramentoDeOutorgaComAA(Requerimento requerimento) throws Exception {
		boolean isOutorga = false;
		boolean isAA = false;
		Collection<AtoAmbiental> outorgas = super.listarAtosOutorga();
		for (EnquadramentoAtoAmbiental ea : super.listarEnquadramentoAtoAmbiental(requerimento)) {
			if (outorgas.contains(ea.getAtoAmbiental())) {
				isOutorga = true;
			}
			if (ea.getAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.AA.getId()))) {
				isAA = true;
			}
		}
		return isOutorga && isAA;
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralOrigemEnergia> listarFcePesquisaMineralOrigemEnergiaBy(FcePesquisaMineral ideFcePesquisaMineral) throws Exception{
		 return fcePesquisaMineralOrigemEnergiaService.listarFcePesquisaMineralOrigemEnergiaBy(ideFcePesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralSubstanciaMineralTipologia> listarFcePesquisaMineralSubstanciaMineralBy(FcePesquisaMineral ideFcePesquisaMineral) throws Exception{
		 return fcePesquisaMineralSubstanciaMineralService.listarFcePesquisaMineralSubstanciaMineral(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralProspeccao>  listarFcePesquisaMineralProspeccaoBy(FcePesquisaMineral ideFcePesquisaMineral) throws Exception{
		 return fcePesquisaMineralProspeccaoService.listarFcePesquisaMineralProspeccaoBy(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralTipoResiduo> listarFcePesquisaMineralTipoResiduoBy(FcePesquisaMineral ideFcePesquisaMineral) throws Exception{
		 return fcePesquisaMineralTipoResiduoService.listarFcePesquisaMineralTipoResiduoBy(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralDestinoResiduo> listarFcePesquisaMineralDestinoResiduoBy(FcePesquisaMineral ideFcePesquisaMineral) throws Exception{
		 return fcePesquisaMineralDestinoResiduoService.listarFcePesquisaMineralDestinoResiduoBy(ideFcePesquisaMineral);
	}
	
	/**
	 * Método que carrega o {@link FcePesquisaMineral} .
	 * 
	 * @author alexandre.queiroz
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
	 * @return {@link FcePesquisaMineral}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralSubstanciaMineral(List<FcePesquisaMineralSubstanciaMineralTipologia> ideFcePesquisaMineralSubstanciaMineral) throws Exception{
		fcePesquisaMineralSubstanciaMineralService.salvarFcePesquisaMineralSubstanciaMineral(ideFcePesquisaMineralSubstanciaMineral);
	}
	
	/**
	 * Método que carrega o {@link FcePesquisaMineral} .
	 * 
	 * @author alexandre.queiroz
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
	 * @return {@link FcePesquisaMineral}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralOrigemEnergia(List<FcePesquisaMineralOrigemEnergia> ideFcePesquisaMineralOrigemEnergia) throws Exception{
		fcePesquisaMineralOrigemEnergiaService.salvarFcePesquisaMineralOrigemEnergia(ideFcePesquisaMineralOrigemEnergia);
	}
	
	
	/**
	 * Método que carrega o {@link FcePesquisaMineral} .
	 * 
	 * @author alexandre.queiroz
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
	 * @return {@link FcePesquisaMineral}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FcePesquisaMineral getFcePesquisaMineralBy(Fce ideFce) throws Exception{
		return fcePesquisaMineralService.getFcePesquisaMineralBy(ideFce);
	}

	/**
	 * Método para persistencia do FcePesquisaMineral o {@link FcePesquisaMineral} .
	 * 
	 * @author alexandre.queiroz
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
	 * @return {@link FcePesquisaMineral}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) throws Exception{
		fcePesquisaMineralService.salvarFcePesquisaMineral(ideFcePesquisaMineral);
	}

	
	/**
	 * Método que carrega a lista de {@link metodoProspeccao} .
	 * 
	 * @author alexandre.queiroz
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
	 * @return {@link metodoProspeccao}
	 * @throws Exception
	 */
	public List<MetodoProspeccao> listarMetodosProspeccao() throws Exception{
		return metodoProspeccaoService.listarMetodoProspeccao();
	}

	/**
	 * RN 00162 - Supressão de vegetação nativa <br/>
	 * (...) se o requerimento for enquadrado com AA
	 * 
	 * @author eduardo.fernandes
	 * @since 22/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param requerimento
	 * @return
	 * @throws Exception
	 */
	public boolean isEnquadramentoAA(Requerimento requerimento) throws Exception {
		boolean isAsv = false;
		boolean isAA = false;
		for(EnquadramentoAtoAmbiental ea : super.listarEnquadramentoAtoAmbiental(requerimento)){
			if(ea.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.ASV.getId())){
				isAsv = true;
			}
			if(ea.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.AA.getId())){
				isAA = true;
			}
		}
		return isAsv && isAA;
	}
	
	/**
	 * Método que lista o {@link Geofisca} 
	 * 
	 * @author alexandre.queiroz
	 * @since 20/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Geofisica> listarGeofisica()throws Exception {
		return geofisicaService.listarGeofisica();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Geofisica> listarGeofisicaBy(FcePesquisaMineral idefcePesquisaMineral) throws Exception {
		return geofisicaService.listarGeofisicaBy(idefcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralMetodoRecuperacao> listarMetodoRecuperacaoIntervencaoByFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) throws Exception{
		return fcePesquisaMineralMetodoRecuperacaoService.listarFcePesquisaMineralMetodoRecuperacao(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceProspeccao> listarFceProspeccaoBy(FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao)  throws Exception{
		List<FceProspeccao> lista = fceProspeccaoService.listarFceProspeccaoBy(ideFcePesquisaMineralProspeccao);
		for(FceProspeccao prospeccao : lista){
			tratarPontoLoc(prospeccao.getIdeLocalizacaoGeografica());
		}
		return lista;
	}
	
	public void tratarPontoLoc(LocalizacaoGeografica localizacaoGeografica){
		super.localizacaoGeograficaFacade.tratarPonto(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<FceProspeccao> listarFceProspeccaoByIdeFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral)throws Exception{
		return fceProspeccaoService.listarFceProspeccaoByIdeFcePesquisaMineral(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceAutorizacaoMineracaoController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
}
