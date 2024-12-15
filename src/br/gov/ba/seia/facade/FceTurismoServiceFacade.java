package br.gov.ba.seia.facade;

import java.math.BigDecimal;
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

import br.gov.ba.seia.controller.FceTurismoController;
import br.gov.ba.seia.dao.ProcessoAtoConcedidoDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoDAOImpl;
import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.CategoriaTurismo;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceCategoriaTurismo;
import br.gov.ba.seia.entity.FceTurismo;
import br.gov.ba.seia.entity.FceTurismoLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceTurismoOrigemEnergia;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoApp;
import br.gov.ba.seia.entity.TipoOrigemEnergia;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.CategoriaTurismoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.FaseEmpreendimentoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.service.AppService;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.CategoriaTurismoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.FceCategoriaTurismoService;
import br.gov.ba.seia.service.FceTurismoLocalizacaoGeograficaService;
import br.gov.ba.seia.service.FceTurismoOrigemEnergiaService;
import br.gov.ba.seia.service.FceTurismoService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.TipoAppService;
import br.gov.ba.seia.service.TipoOrigemEnergiaService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceTurismoServiceFacade {

	@EJB
	private FceTurismoService fceTurismoService;
	@EJB
	private FceCategoriaTurismoService fceCategoriaTurismoService;
	@EJB
	private FceTurismoOrigemEnergiaService fceTurismoOrigemEnergiaService;
	@EJB
	private FceTurismoLocalizacaoGeograficaService fceTurismoLocalizacaoGeograficaService;
	@EJB
	private TipoOrigemEnergiaService origemEnergiaService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private CategoriaTurismoService categoriaTurismoService;
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;
	@EJB
	private TipologiaService tipologiaService;
	@EJB
	private TipoAppService tipoAppService;
	@EJB
	private AppService appService;
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	@EJB
	private LocalizacaoGeograficaServiceFacade locGeoServiceFacade;
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	@EJB
	private ProcessoAtoConcedidoDAOImpl processoAtoConcedidoDAOImpl;
	@EJB
	private ProcessoAtoDAOImpl processoAtoDAOImpl;

	/*
	 * Início dos métodos de busca/listagem
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CategoriaTurismo> listarTipologiaByTipologiaEnum(TipologiaEnum tipologiaEnum) throws Exception {
		return categoriaTurismoService.listarCategoriaTurismoByTipologiaEnum(tipologiaEnum);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoOrigemEnergia> listarTipoOrigemEnergia() throws Exception{
		return origemEnergiaService.buscarListaTipoOrigemEnergia();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoOrigemEnergia> listarTipoOrigemEnergiaByFceTurismoCategoriaTurismo(FceTurismoController controller) throws Exception{
		List<TipoOrigemEnergia> lista = new ArrayList<TipoOrigemEnergia>();
		List<FceTurismoOrigemEnergia> listaFceTurismoOrigemEnergia = fceTurismoOrigemEnergiaService.listarFceTurismoOrigemEnergiaByFceTurismo(controller.getFceTurismo());
		if(!Util.isNullOuVazio(listaFceTurismoOrigemEnergia)){
			for(FceTurismoOrigemEnergia fceTurismoOrigemEnergia : listaFceTurismoOrigemEnergia){
				lista.add(fceTurismoOrigemEnergia.getIdeTipoOrigemEnergia());
			}
		}
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoApp> listarTipoApp() throws Exception{
		return (List<TipoApp>) tipoAppService.listarTipoApp();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tipologia> listarTipologiaByEmpreendimento(Empreendimento empreendimento) throws Exception{
		return tipologiaService.buscarTipologiasByIdeEmpreendimento(empreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceTurismoLocalizacaoGeografica> montarListaFceTurismoLocalizacaoGeograficaByImovelRural(Empreendimento empreendimento, FceTurismo fceTurismo) throws Exception{
		List<FceTurismoLocalizacaoGeografica> listaFceTurismoLocalizacaoGeografica = new ArrayList<FceTurismoLocalizacaoGeografica>();
		for(Imovel imovel : empreendimento.getImovelCollection()){
			imovel.getImovelRural().setAppCollection(listarAppByImovelRural(imovel.getImovelRural()));
			for(App app : imovel.getImovelRural().getAppCollection()) {
				app.getIdeTipoApp().setNomeImovelRural(imovel.getImovelRural().getDesDenominacao());
				listaFceTurismoLocalizacaoGeografica.add(new FceTurismoLocalizacaoGeografica(app, fceTurismo, imovel));
			}
		}
		return listaFceTurismoLocalizacaoGeografica;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceTurismoLocalizacaoGeografica> listarFceTurismoLocalizacaoGeografica(FceTurismoController controller) throws Exception{
		return fceTurismoLocalizacaoGeograficaService.listarFceTurismoLocalizacaoGeograficaByIdeFceTurismo(controller.getFceTurismo());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceCategoriaTurismo> listarFceCategoriaTurismo(FceTurismoController controller) throws Exception{
		return fceCategoriaTurismoService.listarFceCategoriaTurismoByFceTurismoAndTipologiaEnum(controller.getFceTurismo());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<App> listarAppByImovelRural(ImovelRural imovelRural) throws Exception{
		return appService.listarAppByImovelRural(imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isFaseEmpreendimentoImplantacao(Empreendimento empreendimento, Requerimento requerimento) throws Exception{
		EmpreendimentoRequerimento empreendimentoRequerimento = buscarEmpreendimentoRequerimento(empreendimento, requerimento);
		return empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento().equals(FaseEmpreendimentoEnum.IMPLANTACAO.getId());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscarEmpreendimento(FceTurismoController controller) throws Exception{
		return empreendimentoService.carregarEmpreendimentoComLocGeoByRequerimento(controller.getRequerimento());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private EmpreendimentoRequerimento buscarEmpreendimentoRequerimento(Empreendimento empreendimento, Requerimento requerimento) throws Exception{
		return empreendimentoService.buscarEmpreendimentoRequerimento(requerimento, empreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double retonarAreaShapeByGeometria(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		return validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String retornarGeometriaShapeByLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		return validacaoGeoSeiaService.buscarGeometriaShape(localizacaoGeografica.getIdeLocalizacaoGeografica());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal retonarValorAtividade(Requerimento requerimento, TipologiaEnum tipologiaEnum) throws Exception{
		return requerimentoTipologiaService.retornarValorAtividadeByRequerimentoAndTipologia(requerimento, tipologiaEnum);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceTurismo buscarFceTurismo(FceTurismoController controller) throws Exception{
		FceTurismo fceTurismo = fceTurismoService.buscarFceTurismoByIdeFce(controller.getFce());
		if(!Util.isNullOuVazio(fceTurismo.getIdeDocumentoObrigatorioRequerimento())){
			controller.carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(fceTurismo.getIdeDocumentoObrigatorioRequerimento());
		}
		return fceTurismo;
	}
	/*
	 * Fim dos métodos de busca/listagem
	 */

	/*
	 * Início dos métodos de persistência
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceturismo(FceTurismoController controller) throws Exception{
		controller.getFceTurismo().setIdeDocumentoObrigatorioRequerimento(controller.getDocumentoUpado());
		fceTurismoService.salvarFceTurismo(controller.getFceTurismo());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceCateogoriaTurismo(FceTurismoController controller) throws Exception{
		FceTurismo fceTurismo = controller.getFceTurismo();
		if(controller.isFceSalvo()) {
			excluirFceCategoriaTurismo(fceTurismo);
		}
		if(controller.isTipologiaG11() /*&& !Util.isNullOuVazio(listaTipologiaG11selected)*/) {
			fceCategoriaTurismoService.salvarListaFceCategoriaTurismo(fceTurismo, controller.getListaTipologiaG11selected());
		}
		if(controller.isTipologiaG21() /*&& !Util.isNullOuVazio(listaTipologiaG21selected)*/) {
			fceCategoriaTurismoService.salvarListaFceCategoriaTurismo(fceTurismo, controller.getListaTipologiaG21selected());
		}
		if(controller.isTipologiaG22() /*&& !Util.isNullOuVazio(listaTipologiaG22selected)*/) {
			fceCategoriaTurismoService.salvarListaFceCategoriaTurismo(fceTurismo, controller.getListaTipologiaG22selected());
		}
		if(controller.isTipologiaG23()) {
			fceCategoriaTurismoService.montarAndSalvarFceCategoriaTurismo(fceTurismo, new CategoriaTurismo(CategoriaTurismoEnum.CONJUNTOS_HABITACIONAIS));
		}
		if(controller.isTipologiaG24()) {
			fceCategoriaTurismoService.montarAndSalvarFceCategoriaTurismo(fceTurismo, new CategoriaTurismo(CategoriaTurismoEnum.HABITACAO_INTERESSE_SOCIAL));
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceTurismoOrigemEnergia(FceTurismoController controller) throws Exception{
		for(TipoOrigemEnergia origemEnergia : controller.getListaTipoOrigemEnergiaEscolhidos()){
			FceTurismoOrigemEnergia fceTurismoOrigemEnergia = new FceTurismoOrigemEnergia(controller.getFceTurismo(), origemEnergia);
			fceTurismoOrigemEnergiaService.salvarFceTurismoOrigemEnergia(fceTurismoOrigemEnergia);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceTurismoLocalizacaoGeografica(FceTurismoController controller) throws Exception{
		if(!Util.isNullOuVazio(controller.getListaFceTurismoLocalizacaoGeografica())){
			if(controller.isFceSalvo()) {
				fceTurismoLocalizacaoGeograficaService.excluirFceCategoriaTurismoByIdeFceTurismo(controller.getFceTurismo());
				for(FceTurismoLocalizacaoGeografica turismoLocalizacaoGeografica : controller.getListaFceTurismoLocalizacaoGeografica()) {
					turismoLocalizacaoGeografica.setIdeFceTurismoLocalizacaoGeografica(null);
				}
			}
			fceTurismoLocalizacaoGeograficaService.salvarListaFceTurismoLocalizacaoGeografica(controller.getListaFceTurismoLocalizacaoGeografica());
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceTurismoLocalizacaoGeografica(FceTurismoLocalizacaoGeografica fceTurismoLocalizacaoGeografica) throws Exception{
		fceTurismoLocalizacaoGeograficaService.salvarFceTurismoLocalizacaoGeografica(fceTurismoLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceTurismoParaVisualizacaoGeoBahia(FceTurismoLocalizacaoGeografica fceTurismoLocalizacaoGeografica, FceTurismoController controller) throws Exception{
		if(!controller.isFceSalvo()){
			controller.salvarFce();
		}
		salvarFceturismo(controller);
		fceTurismoLocalizacaoGeografica.setIdeFceTurismo(controller.getFceTurismo());
		salvarFceTurismoLocalizacaoGeografica(controller);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarFceTurismoDuplicacao(FceTurismoController controller) throws Exception {
		controller.getFce().setIndConcluido(false);
		
		finalizarFceTurismoAll(controller);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarFceTurismo(FceTurismoController controller) throws Exception {
		controller.getFce().setIndConcluido(true);
		
		finalizarFceTurismoAll(controller);
	}

	private void finalizarFceTurismoAll(FceTurismoController controller) throws Exception {
		controller.salvarFce();
		controller.salvarDocumentoAdicional(controller.getRequerimento(), new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_TURISMO_ADICIONAIS.getId()));
		salvarFceturismo(controller);
		if(controller.isZonaUrbana()){
			salvarFceTurismoLocalizacaoGeografica(controller);
		}
		salvarFceCateogoriaTurismo(controller);
		if(!controller.isLi()) {
			salvarFceTurismoOrigemEnergia(controller);
		}
		
		if(controller.isFceTecnico() && !Util.isNullOuVazio(controller.getListProcessoAtoConcedido())) {
			for (ProcessoAtoConcedido pac : controller.getListProcessoAtoConcedido()) {
				processoAtoConcedidoDAOImpl.excluirPorProcessoAto(pac.getIdeProcessoAto());
				salvarProcessoAtoConcedido(pac);
			}
		}
	}
	
	public List<ProcessoAtoConcedido> obterProcessoAtoConcedidoByLocalizacao(LocalizacaoGeografica locGeo) throws Exception {
		return processoAtoConcedidoDAOImpl.obterByLocalizacaoGeografica(locGeo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LocalizacaoGeografica duplicarLocalizacaoGeografica(LocalizacaoGeografica locGeo) throws Exception{
		return locGeoServiceFacade.duplicarLocalizacaoGeografica(locGeo);
	}
	/*
	 * Fim dos métodos de persistência
	 */

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirFceCategoriaTurismo(FceTurismo fceTurismo) throws Exception{
		fceCategoriaTurismoService.excluirFceCategoriaTurismoByIdeFceTurismo(fceTurismo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ArquivoProcesso> listarArquivoProcessoPorProcesso(Processo p) {
		try {
			return arquivoProcessoService.listarArquivoProcessoPorProcesso(p.getIdeProcesso());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> listarProcessoAtoPorFce(Fce fce) {
		try {
			return processoAtoDAOImpl.listarProcessoAtoPorFce(fce);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcessoAtoConcedido(ProcessoAtoConcedido pac) {
		try {
			processoAtoConcedidoDAOImpl.salvar(pac);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceTurismoController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
}