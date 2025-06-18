package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.FceLicenciamentoMineracaoController;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ClassificacaoRejeitoDnpm;
import br.gov.ba.seia.entity.DestinoResiduo;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLicenciamentoExploracaoRegimeExploracao;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralDestinoResiduo;
import br.gov.ba.seia.entity.FceLicenciamentoMineralEmissaoAtmosferica;
import br.gov.ba.seia.entity.FceLicenciamentoMineralMetodoLavra;
import br.gov.ba.seia.entity.FceLicenciamentoMineralMetodoRecuperacao;
import br.gov.ba.seia.entity.FceLicenciamentoMineralOrigemEnergia;
import br.gov.ba.seia.entity.FceLicenciamentoMineralProducaoProduto;
import br.gov.ba.seia.entity.FceLicenciamentoMineralProducaoProdutoPK;
import br.gov.ba.seia.entity.FceLicenciamentoMineralServidaoMineraria;
import br.gov.ba.seia.entity.FceLicenciamentoMineralServidaoMinerariaPK;
import br.gov.ba.seia.entity.FceLicenciamentoMineralSistemaTratamento;
import br.gov.ba.seia.entity.FceLicenciamentoMineralSubstanciaMineralTipologia;
import br.gov.ba.seia.entity.FceLicenciamentoMineralSubstanciaMineralTipologiaPK;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTecnicaLavra;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTipoApp;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTipoAppPK;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTipoResiduo;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTransporteMineiro;
import br.gov.ba.seia.entity.FormaDisposicaoRejeito;
import br.gov.ba.seia.entity.MetodoLavra;
import br.gov.ba.seia.entity.MetodoRecuperacaoIntervencao;
import br.gov.ba.seia.entity.ProducaoProduto;
import br.gov.ba.seia.entity.RegimeExploracao;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ServidaoMineraria;
import br.gov.ba.seia.entity.SubstanciaMineralTipologia;
import br.gov.ba.seia.entity.TecnicaLavra;
import br.gov.ba.seia.entity.TipoApp;
import br.gov.ba.seia.entity.TipoEmissaoAtmosferica;
import br.gov.ba.seia.entity.TipoEstrutura;
import br.gov.ba.seia.entity.TipoOrigemEnergia;
import br.gov.ba.seia.entity.TipoResiduoGerado;
import br.gov.ba.seia.entity.TipoSistemaTratamento;
import br.gov.ba.seia.entity.TipoTransporteMinerio;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.service.ClassificacaoRejeitoDnpmService;
import br.gov.ba.seia.service.FceLicenciamentoExploracaoRegimeExploracaoService;
import br.gov.ba.seia.service.FceLicenciamentoMineralDestinoResiduoService;
import br.gov.ba.seia.service.FceLicenciamentoMineralEmissaoAtmosfericaService;
import br.gov.ba.seia.service.FceLicenciamentoMineralMetodoLavraService;
import br.gov.ba.seia.service.FceLicenciamentoMineralMetodoRecuperacaoService;
import br.gov.ba.seia.service.FceLicenciamentoMineralOrigemEnergiaService;
import br.gov.ba.seia.service.FceLicenciamentoMineralProducaoProdutoService;
import br.gov.ba.seia.service.FceLicenciamentoMineralService;
import br.gov.ba.seia.service.FceLicenciamentoMineralServidaoMinerariaService;
import br.gov.ba.seia.service.FceLicenciamentoMineralSistemaTratamentoService;
import br.gov.ba.seia.service.FceLicenciamentoMineralSubstanciaMineralService;
import br.gov.ba.seia.service.FceLicenciamentoMineralTecnicaLavraService;
import br.gov.ba.seia.service.FceLicenciamentoMineralTipoAppService;
import br.gov.ba.seia.service.FceLicenciamentoMineralTipoResiduoService;
import br.gov.ba.seia.service.FceLicenciamentoMineralTransporteMineiroService;
import br.gov.ba.seia.service.FormaDisposicaoRejeitoService;
import br.gov.ba.seia.service.MetodoLavraService;
import br.gov.ba.seia.service.ProducaoProdutoService;
import br.gov.ba.seia.service.RegimeExploracaoService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.ServidaoMinerariaService;
import br.gov.ba.seia.service.TecnicaLavraService;
import br.gov.ba.seia.service.TipoAppService;
import br.gov.ba.seia.service.TipoEmissaoAtmosfericaService;
import br.gov.ba.seia.service.TipoEstruturaService;
import br.gov.ba.seia.service.TipoSistemaTratamentoService;
import br.gov.ba.seia.service.TipoTransporteMinerioService;
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
public class FceLicenciamentoMineracaoFacade extends FceMineracaoFacade {
	
	@EJB
	private TipoSistemaTratamentoService tipoSistemaTratamentoService;
	
	@EJB
	private TipoEmissaoAtmosfericaService tipoEmissaoAtmosfericaService;
	
	@EJB
	private TipoAppService tipoAppService;
	
	@EJB
	private RegimeExploracaoService regimeExploracaoService;
	
	@EJB
	private MetodoLavraService metodoLavraService;
	
	@EJB
	private TecnicaLavraService tecnicaLavraService;
	
	@EJB
	private ServidaoMinerariaService servidaoMinerariaService;
	
	@EJB
	private TipoEstruturaService tipoEstruturaService;
	
	@EJB
	private ClassificacaoRejeitoDnpmService classificacaoRejeitoDnpmService;
	
	@EJB
	private TipoTransporteMinerioService tipoTransporteMineiroService;
	
	@EJB
	private FceLicenciamentoMineralService fceLicenciamentoMineralService;
	
	@EJB
	private FceLicenciamentoExploracaoRegimeExploracaoService fceLicenciamentoExploracaoRegimeExploracaoService;
	
	@EJB
	private FceLicenciamentoMineralOrigemEnergiaService fceLicenciamentoMineralOrigemEnergiaService;
	
	@EJB
	private FceLicenciamentoMineralMetodoRecuperacaoService fceLicenciamentoMineralMetodoRecuperacaoService;
	
	@EJB
	private FceLicenciamentoMineralTransporteMineiroService fceLicenciamentoMineralTransporteMineiroService;
	
	@EJB
	private FceLicenciamentoMineralTecnicaLavraService fceLicenciamentoMineralTecnicaLavraService;
	
	@EJB
	private FceLicenciamentoMineralMetodoLavraService fceLicenciamentoMineralMetodoLavraService;
	
	@EJB
	private FceLicenciamentoMineralDestinoResiduoService fceLicenciamentoMineralDestinoResiduoService;
	
	@EJB
	private FceLicenciamentoMineralTipoResiduoService fceLicenciamentoMineralTipoResiduoService;
	
	@EJB
	private FceLicenciamentoMineralEmissaoAtmosfericaService fceLicenciamentoMineralEmissaoAtmosfericaService;
	
	@EJB
	private FceLicenciamentoMineralSistemaTratamentoService fceLicenciamentoMineralSistemaTratamentoService;
	
	@EJB
	private FceLicenciamentoMineralTipoAppService fceLicenciamentoMineralTipoAppService;
	
	@EJB
	private FormaDisposicaoRejeitoService formaDisposicaoRejeitoService;
	
	@EJB
	private FceLicenciamentoMineralServidaoMinerariaService fceLicenciamentoMineralServidaoMinerariaService;
	
	@EJB
	private FceLicenciamentoMineralSubstanciaMineralService fceLicenciamentoMineralSubstanciaMineralService;
	
	@EJB
	private ProducaoProdutoService producaoProdutoService;
	
	@EJB
	private FceLicenciamentoMineralProducaoProdutoService fceLicenciamentoMineralProducaoProdutoService;
	
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;
	
	/**
	 * Lista dos Atos de Licenças
	 *
	 * @author eduardo.fernandes
	 * @return
	 * @since 20/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<AtoAmbiental> getListaAtosLicenca() {
		List<AtoAmbiental> listaAtosLicenca = new ArrayList<AtoAmbiental>();
		listaAtosLicenca.add(new AtoAmbiental(AtoAmbientalEnum.LP.getId()));
		listaAtosLicenca.add(new AtoAmbiental(AtoAmbientalEnum.LI.getId()));
		listaAtosLicenca.add(new AtoAmbiental(AtoAmbientalEnum.LR.getId()));
		listaAtosLicenca.add(new AtoAmbiental(AtoAmbientalEnum.LO.getId()));
		listaAtosLicenca.add(new AtoAmbiental(AtoAmbientalEnum.RLO.getId()));
		listaAtosLicenca.add(new AtoAmbiental(AtoAmbientalEnum.LU.getId()));
		listaAtosLicenca.add(new AtoAmbiental(AtoAmbientalEnum.RLU.getId()));
		return listaAtosLicenca;
	}

	/**
	 * RN 00164 - Informação do requerimento sobre processo de outorga <br/>
	 * Quando o requerimento não for enquadrado com AA e Outorga ou Licença e Outorga(...).
	 *
	 * @author eduardo.fernandes
	 * @return
	 * @since 20/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7895">#7895</a>
	 */
	public boolean isEnquadramentoDeLincencaComOutorga(Requerimento requerimento) throws Exception {
		boolean isOutorga = false;
		boolean isLicenca = false;
		Collection<AtoAmbiental> outorgas = super.listarAtosOutorga();
		for (EnquadramentoAtoAmbiental ea : super.listarEnquadramentoAtoAmbiental(requerimento)) {
			if (outorgas.contains(ea.getAtoAmbiental())) {
				isOutorga = true;
			}
			if (getListaAtosLicenca().contains(ea.getAtoAmbiental())) {
				isLicenca = true;
			}
		}
		return isOutorga && isLicenca;
	}

	/**
	 * RN 00162 - Supressão de vegetação nativa <br/>
	 * (...) se o requerimento for enquadrado com Licença e ASV.
	 *
	 * @author eduardo.fernandes
	 * @since 20/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param requerimento
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isEnquadramentoLicencaAndASV(Requerimento requerimento) throws Exception {
		boolean isAsv = false;
		boolean isLicenca = false;
		for(EnquadramentoAtoAmbiental ea : super.listarEnquadramentoAtoAmbiental(requerimento)){
			if(ea.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.ASV.getId())){
				isAsv = true;
			}
			if(getListaAtosLicenca().contains(ea.getAtoAmbiental())){
				isLicenca = true;
			}
		}
		return isAsv && isLicenca;
	}
	
	/**
	 * RN 00171 - Poligonal da área de servidão <br/>
	 * (...) O cadastro não é obrigatório para FCE de LP (...)
	 *
	 * @author eduardo.fernandes
	 * @since 22/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param requerimento
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isEnquadramentoLP(Requerimento requerimento) throws Exception {
		for(EnquadramentoAtoAmbiental ea : super.listarEnquadramentoAtoAmbiental(requerimento)){
			if(ea.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.LP.getId())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Método que retorna a lista de {@link TipoApp}.
	 *
	 * @author eduardo.fernandes
	 * @since 13/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link TipoApp}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoApp> listarApps() throws Exception {
		return (List<TipoApp>) tipoAppService.listarTipoApp();
	}
	
	/**
	 * Método que retorna a lista de {@link ClassificacaoRejeitoDnpm}.
	 *
	 * @author eduardo.fernandes
	 * @since 13/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link ClassificacaoRejeitoDnpm}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ClassificacaoRejeitoDnpm> listarClassificacaoRejeito() throws Exception {
		return classificacaoRejeitoDnpmService.listarClassificacaoRejeitoDnpm();
	}
	
	/**
	 * Método que retorna a lista de {@link MetodoLavra}.
	 *
	 * @author eduardo.fernandes
	 * @since 13/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link MetodoLavra}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoLavra> listarMetodoLavra() throws Exception {
		return metodoLavraService.listarMetodoLavra();
	}
	
	
	/**
	 * Método que retorna a lista de {@link RegimeExploracao}.
	 *
	 * @author eduardo.fernandes
	 * @since 13/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link RegimeExploracao}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegimeExploracao> listarRegimeExploracao() throws Exception {
		return regimeExploracaoService.listarRegimeExploracao();
	}
	
	/**
	 * Método que retorna a lista de {@link ServidaoMineraria}.
	 *
	 * @author eduardo.fernandes
	 * @since 13/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link ServidaoMineraria}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ServidaoMineraria> listarServidaoMineraria() throws Exception {
		return servidaoMinerariaService.listarServidaoMineraria();
	}
	
	/**
	 * Método que retorna a lista de {@link TecnicaLavra} de acorodo com o
	 * {@link MetodoLavra}.
	 *
	 * @author eduardo.fernandes
	 * @since 13/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link TecnicaLavra}
	 * @throws Exception
	 * @param
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TecnicaLavra> listarTecnicaLavraByMetodoLavra(MetodoLavra metodoLavra) throws Exception {
		return tecnicaLavraService.listarTecnicaLavraBy(metodoLavra);
	}
	
	/**
	 * Método que retorna a lista de {@link TecnicaLavra} de acorodo com a
	 * {@link TecnicaLavra} pai.
	 *
	 * @author eduardo.fernandes
	 * @since 13/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link TecnicaLavra}
	 * @throws Exception
	 * @param
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TecnicaLavra> listarTecnicaLavraByTecnicaLavra(TecnicaLavra tecnicaLavra) throws Exception {
		return tecnicaLavraService.listarTecnicaLavraBy(tecnicaLavra);
	}
	
	/**
	 * Método que retorna a lista de {@link TipoEmissaoAtmosferica}.
	 *
	 * @author eduardo.fernandes
	 * @since 10/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link TipoEmissaoAtmosferica}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoEmissaoAtmosferica> listarTipoEmissaoAtmosferica() throws Exception {
		return tipoEmissaoAtmosfericaService.buscarListaTipoEmissaoAtmosferica();
	}
	
	/**
	 * Método que retorna a lista de {@link TipoEstrutura}.
	 *
	 * @author eduardo.fernandes
	 * @since 13/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link TipoEstrutura}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoEstrutura> listarTipoEstrutura() throws Exception {
		return tipoEstruturaService.listarTipoEstrutura();
	}
	
	/**
	 * Método que retorna a lista de {@link TipoSistemaTratamento}.
	 *
	 * @author eduardo.fernandes
	 * @since 10/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link TipoSistemaTratamento}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoSistemaTratamento> listarTipoSistemaTratamento() throws Exception {
		return tipoSistemaTratamentoService.buscarListaTipoSistemaTratamento();
	}
	
	/**
	 * Método que retorna a lista de {@link TipoTransporteMinerio}.
	 *
	 * @author eduardo.fernandes
	 * @since 13/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link TipoTransporteMinerio}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoTransporteMinerio> listarTransporteMinerio() throws Exception {
		return tipoTransporteMineiroService.listarTipoTransporteMinerio();
	}
	
	/**
	 * Método para montar a lista de
	 * {@link FceLicenciamentoExploracaoRegimeExploracao} no
	 * {@link FceLicenciamentoMineral} persistido.
	 *
	 * @author eduardo.fernandes
	 * @param fceLicenciamentoMineral
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<FceLicenciamentoExploracaoRegimeExploracao> montarListaFceLicenciamentoExploracaoRegimeExploracao(FceLicenciamentoMineracaoController controller) {
		List<FceLicenciamentoExploracaoRegimeExploracao> lista = new ArrayList<FceLicenciamentoExploracaoRegimeExploracao>();
		for(RegimeExploracao regimeExploracao : controller.getListaRegimeExploracaoSelected()){
			lista.add(new FceLicenciamentoExploracaoRegimeExploracao(controller.getFceLicenciamentoMineral(), regimeExploracao));
		}
		return lista;
	}
	
	/**
	 * Método para montar a lista de
	 * {@link FceLicenciamentoMineralDestinoResiduo} no
	 * {@link FceLicenciamentoMineral} persistido.
	 *
	 * @author eduardo.fernandes
	 * @param controller
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<FceLicenciamentoMineralDestinoResiduo> montarListaFceLicenciamentoMineralDestinoResiduo(FceLicenciamentoMineracaoController controller) {
		List<FceLicenciamentoMineralDestinoResiduo> lista = new ArrayList<FceLicenciamentoMineralDestinoResiduo>();
		for(DestinoResiduo destinoResiduo : controller.getListaDestinoResiduoSelected()){
			lista.add(new FceLicenciamentoMineralDestinoResiduo(controller.getFceLicenciamentoMineral(), destinoResiduo));
		}
		return lista;
	}
	
	/**
	 * Método para montar a lista de
	 * {@link FceLicenciamentoMineralEmissaoAtmosferica} no
	 * {@link FceLicenciamentoMineral} persistido.
	 *
	 * @author eduardo.fernandes
	 * @param controller
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<FceLicenciamentoMineralEmissaoAtmosferica> montarListaFceLicenciamentoMineralEmissaoAtmosferica(FceLicenciamentoMineracaoController controller) {
		List<FceLicenciamentoMineralEmissaoAtmosferica> lista = new ArrayList<FceLicenciamentoMineralEmissaoAtmosferica>();
		for(TipoEmissaoAtmosferica emissaoAtmosferica : controller.getListaTipoEmissaoAtmosfericaSelected()){
			lista.add(new FceLicenciamentoMineralEmissaoAtmosferica(controller.getFceLicenciamentoMineral(), emissaoAtmosferica));
		}
		return lista;
	}
	
	/**
	 * Método para montar a lista de {@link FceLicenciamentoMineralMetodoLavra}
	 * no {@link FceLicenciamentoMineral} persistido.
	 *
	 * @author eduardo.fernandes
	 * @param controller
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<FceLicenciamentoMineralMetodoLavra> montarListaFceLicenciamentoMineralMetodoLavra(FceLicenciamentoMineracaoController controller) {
		List<FceLicenciamentoMineralMetodoLavra> lista = new ArrayList<FceLicenciamentoMineralMetodoLavra>();
		for(MetodoLavra metodoLavra : controller.getListaMetodoLavraSelected()){
			lista.add(new FceLicenciamentoMineralMetodoLavra(controller.getFceLicenciamentoMineral(), metodoLavra));
		}
		return lista;
	}
	
	/**
	 * Método para montar a lista de
	 * {@link FceLicenciamentoMineralMetodoRecuperacao} no
	 * {@link FceLicenciamentoMineral} persistido.
	 *
	 * @author eduardo.fernandes
	 * @param controller
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<FceLicenciamentoMineralMetodoRecuperacao> montarListaFceLicenciamentoMineralMetodoRecuperacao(FceLicenciamentoMineracaoController controller) {
		List<FceLicenciamentoMineralMetodoRecuperacao> lista = new ArrayList<FceLicenciamentoMineralMetodoRecuperacao>();
		for(MetodoRecuperacaoIntervencao recuperacaoIntervencao : controller.getListaMetodoRecuperacaoIntervencaoSelected()){
			lista.add(new FceLicenciamentoMineralMetodoRecuperacao(controller.getFceLicenciamentoMineral(), recuperacaoIntervencao));
		}
		return lista;
	}
	
	/**
	 * Método para montar a lista de
	 * {@link FceLicenciamentoMineralOrigemEnergia} no
	 * {@link FceLicenciamentoMineral} persistido.
	 *
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<FceLicenciamentoMineralOrigemEnergia> montarListaFceLicenciamentoMineralOrigemEnergia(FceLicenciamentoMineracaoController controller) {
		List<FceLicenciamentoMineralOrigemEnergia> lista = new ArrayList<FceLicenciamentoMineralOrigemEnergia>();
		for(TipoOrigemEnergia origemEnergia : controller.getListaTipoOrigemEnergiaSelected()){
			lista.add(new FceLicenciamentoMineralOrigemEnergia(controller.getFceLicenciamentoMineral(), origemEnergia));
		}
		return lista;
	}
	
	/**
	 * Método para montar a lista de
	 * {@link FceLicenciamentoMineralSistemaTratamento} no
	 * {@link FceLicenciamentoMineral} persistido.
	 *
	 * @author eduardo.fernandes
	 * @param controller
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<FceLicenciamentoMineralSistemaTratamento> montarListaFceLicenciamentoMineralSistemaTratamento(FceLicenciamentoMineracaoController controller) {
		List<FceLicenciamentoMineralSistemaTratamento> lista = new ArrayList<FceLicenciamentoMineralSistemaTratamento>();
		for(TipoSistemaTratamento sistemaTratamento : controller.getListaTipoSistemaTratamentoSelected()){
			lista.add(new FceLicenciamentoMineralSistemaTratamento(controller.getFceLicenciamentoMineral(), sistemaTratamento));
		}
		return lista;
	}
	
	/**
	 * Método para montar a lista de {@link FceLicenciamentoMineralTecnicaLavra}
	 * no {@link FceLicenciamentoMineral} persistido.
	 *
	 * @author eduardo.fernandes
	 * @param controller
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<FceLicenciamentoMineralTecnicaLavra> montarListaFceLicenciamentoMineralTecnicaLavra(FceLicenciamentoMineracaoController controller) {
		if(!Util.isNullOuVazio(controller.getListaTecnicaLavraCeuAbertoSelected()) || !Util.isNullOuVazio(controller.getListaTecnicaLavraSubterraneaSelected())){
			List<FceLicenciamentoMineralTecnicaLavra> lista = new ArrayList<FceLicenciamentoMineralTecnicaLavra>();
			if(!Util.isNullOuVazio(controller.getListaTecnicaLavraCeuAbertoSelected())){
				for(TecnicaLavra tecnicaLavra : controller.getListaTecnicaLavraCeuAbertoSelected()){
					lista.add(new FceLicenciamentoMineralTecnicaLavra(controller.getFceLicenciamentoMineral(), tecnicaLavra));
				}
				if(!Util.isNullOuVazio(controller.getListaTecnicaLavraCeuAbertoDragagemSelected())){
					for(TecnicaLavra tecnicaLavra : controller.getListaTecnicaLavraCeuAbertoDragagemSelected()){
						lista.add(new FceLicenciamentoMineralTecnicaLavra(controller.getFceLicenciamentoMineral(), tecnicaLavra));
					}
				}
			}
			if(!Util.isNullOuVazio(controller.getListaTecnicaLavraSubterraneaSelected())){
				for(TecnicaLavra tecnicaLavra : controller.getListaTecnicaLavraSubterraneaSelected()){
					lista.add(new FceLicenciamentoMineralTecnicaLavra(controller.getFceLicenciamentoMineral(), tecnicaLavra));
				}
			}
			return lista;
		}
		return null;
	}
	
	/**
	 * Método para montar a lista de {@link FceLicenciamentoMineralTipoResiduo}
	 * no {@link FceLicenciamentoMineral} persistido.
	 *
	 * @author eduardo.fernandes
	 * @param controller
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<FceLicenciamentoMineralTipoResiduo> montarListaFceLicenciamentoMineralTipoResiduo(FceLicenciamentoMineracaoController controller) {
		List<FceLicenciamentoMineralTipoResiduo> lista = new ArrayList<FceLicenciamentoMineralTipoResiduo>();
		for(TipoResiduoGerado residuoGerado : controller.getListaTipoResiduoGeradoSelected()){
			lista.add(new FceLicenciamentoMineralTipoResiduo(controller.getFceLicenciamentoMineral(), residuoGerado));
		}
		return lista;
	}
	
	/**
	 * Método para montar a lista de
	 * {@link FceLicenciamentoMineralTransporteMineiro} no
	 * {@link FceLicenciamentoMineral} persistido.
	 *
	 * @author eduardo.fernandes
	 * @param controller
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<FceLicenciamentoMineralTransporteMineiro> montarListaFceLicenciamentoMineralTransporteMineiro(FceLicenciamentoMineracaoController controller) {
		List<FceLicenciamentoMineralTransporteMineiro> lista = new ArrayList<FceLicenciamentoMineralTransporteMineiro>();
		for(TipoTransporteMinerio transporteMineiro : controller.getListaTipoTransporteMinerioSelected()){
			lista.add(new FceLicenciamentoMineralTransporteMineiro(controller.getFceLicenciamentoMineral(), transporteMineiro));
		}
		return lista;
	}
	
	/**
	 * Método para salvar à aba <b>Aspectos Ambientais</b>.
	 *
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineracaoController
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAbaAspectosAmbientais(FceLicenciamentoMineracaoController controller) throws Exception {
		salvarFceLicenciamentoMineral(controller);
		salvarListaFceLicenciamentoMineralSistemaTratamento(controller);
		salvarListaFceLicenciamentoMineralEmissaoAtmosferica(controller);
		salvarListaFceLicenciamentoMineralTipoResiduo(controller);
		salvarListaFceLicenciamentoMineralDestinoResiduo(controller);
	}
	
	/**
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralDestinoResiduo}.
	 *
	 * @author eduardo.fernandes
	 * @since 07/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoMineralDestinoResiduo(FceLicenciamentoMineracaoController controller) throws Exception {
		fceLicenciamentoMineralDestinoResiduoService.excluirListaFceLicenciamentoMineralDestinoResiduo(controller.getFceLicenciamentoMineral());
		fceLicenciamentoMineralDestinoResiduoService.salvarListaFceLicenciamentoMineralDestinoResiduo(montarListaFceLicenciamentoMineralDestinoResiduo(controller));
		
	}
	
	/**
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralTipoResiduo}.
	 *
	 * @author eduardo.fernandes
	 * @since 07/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoMineralTipoResiduo(FceLicenciamentoMineracaoController controller) throws Exception {
		fceLicenciamentoMineralTipoResiduoService.excluirListaFceLicenciamentoMineralTipoResiduo(controller.getFceLicenciamentoMineral());
		fceLicenciamentoMineralTipoResiduoService.salvarListaFceLicenciamentoMineralTipoResiduo(montarListaFceLicenciamentoMineralTipoResiduo(controller));
		
	}
	
	/**
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralEmissaoAtmosferica}
	 *
	 * @author eduardo.fernandes
	 * @since 07/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoMineralEmissaoAtmosferica(FceLicenciamentoMineracaoController controller) throws Exception {
		fceLicenciamentoMineralEmissaoAtmosfericaService.excluirListaFceLicenciamentoMineralEmissaoAtmosferica(controller.getFceLicenciamentoMineral());
		fceLicenciamentoMineralEmissaoAtmosfericaService.salvarListaFceLicenciamentoMineralEmissaoAtmosferica(montarListaFceLicenciamentoMineralEmissaoAtmosferica(controller));
		
	}
	
	/**
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralSistemaTratamento}
	 *
	 * @author eduardo.fernandes
	 * @since 07/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoMineralSistemaTratamento(FceLicenciamentoMineracaoController controller) throws Exception {
		fceLicenciamentoMineralSistemaTratamentoService.excluirListaFceLicenciamentoMineralSistemaTratamento(controller.getFceLicenciamentoMineral());
		fceLicenciamentoMineralSistemaTratamentoService.salvarListaFceLicenciamentoMineralSistemaTratamento(montarListaFceLicenciamentoMineralSistemaTratamento(controller));
	}
	
	/**
	 * Método para salvar à aba <b>Caracterização de Atividades</b>.
	 *
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineracaoController
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAbaCaracterizacaoDeAtividades(FceLicenciamentoMineracaoController controller) throws Exception {
		salvarFceLicenciamentoMineral(controller);
		salvarListaFceLicenciamentoMineralMetodoLavra(controller);
		salvarListaFceLicenciamentoMineralTecnicaLavra(controller);
		if(controller.isFceTecnico()) {
			salvarOuExcluirListaFceLicenciamentoMineralProducaoProduto(controller);
		}
		salvarOuExcluirListaFormaDisposicaoRejeito(controller);
		salvarListaFceLicenciamentoMineralServidaoMineraria(controller);
		salvarListaFceLicenciamentoMineralTransporteMineiro(controller);
		salvarListaFceLicenciamentoMineralMetodoRecuperacao(controller);
		salvarOuExcluirListaFceLicenciamentoMineralTipoApp(controller);
	}
	
	/**
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralServidaoMineraria}
	 *
	 * @author eduardo.fernandes
	 * @since 08/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoMineralServidaoMineraria(FceLicenciamentoMineracaoController controller) throws Exception {
		fceLicenciamentoMineralServidaoMinerariaService.salvarListaFceLicenciamentoMineralServidaoMineraria(prepararListaFceLicenciamentoServidaoMineraria(controller.getListaFceLicenciamentoMineralServidaoMineraria()));
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param listaFceLicenciamentoMineralServidaoMineraria
	 * @return
	 */
	private List<FceLicenciamentoMineralServidaoMineraria> prepararListaFceLicenciamentoServidaoMineraria(List<FceLicenciamentoMineralServidaoMineraria> listaFceLicenciamentoMineralServidaoMineraria) {
		for(FceLicenciamentoMineralServidaoMineraria servidaoMineraria : listaFceLicenciamentoMineralServidaoMineraria){
			servidaoMineraria.setIdeFceLicenciamentoMineralServidaoMinerariaPK(new FceLicenciamentoMineralServidaoMinerariaPK(servidaoMineraria.getFceLicenciamentoMineral(), servidaoMineraria.getServidaoMineraria()));
		}
		return listaFceLicenciamentoMineralServidaoMineraria;
	}
	
	/**
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralTipoApp}
	 *
	 * @author eduardo.fernandes
	 * @since 08/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarOuExcluirListaFceLicenciamentoMineralTipoApp(FceLicenciamentoMineracaoController controller) throws Exception {
		if(!controller.getFceLicenciamentoMineral().getIndApp()) {
			fceLicenciamentoMineralTipoAppService.excluirListaFceLicenciamentoMineralTipoApp(controller.getFceLicenciamentoMineral());
		}
		else {
			fceLicenciamentoMineralTipoAppService.salvarListaFceLicenciamentoMineralTipoApp(prepararListaFceLicenciamentoMineralTipoApp(controller.getListaFceLicenciamentoMineralTipoApp()));
		}
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 14/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param lista
	 * @return
	 */
	private List<FceLicenciamentoMineralTipoApp> prepararListaFceLicenciamentoMineralTipoApp(List<FceLicenciamentoMineralTipoApp> lista) {
		for(FceLicenciamentoMineralTipoApp app : lista){
			app.setIdeFceLicenciamentoMineralTipoAppPK(new FceLicenciamentoMineralTipoAppPK(app.getFceLicenciamentoMineral(), app.getTipoApp()));
		}
		return lista;
	}
	
	/**
	 * Método para salvar a tabela {@link FormaDisposicaoRejeito}
	 *
	 * @author eduardo.fernandes
	 * @since 08/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarOuExcluirListaFormaDisposicaoRejeito(FceLicenciamentoMineracaoController controller) throws Exception {
		if(!controller.getFceLicenciamentoMineral().getIndBeneficiamentoMineracao()) {
			formaDisposicaoRejeitoService.excluirListaFormaDisposicaoRejeito(controller.getFceLicenciamentoMineral());
		}
		else if(!Util.isNullOuVazio(controller.getListaFormaDisposicaoRejeito())){
			formaDisposicaoRejeitoService.salvarListaFormaDisposicaoRejeito(controller.getListaFormaDisposicaoRejeito());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFormaDisposicaoRejeito(FormaDisposicaoRejeito formaDisposicaoRejeito) throws Exception{
		formaDisposicaoRejeitoService.excluirFormaDisposicaoRejeito(formaDisposicaoRejeito);
	}
	
	/**
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralMetodoRecuperacao}
	 *
	 * @author eduardo.fernandes
	 * @since 07/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoMineralMetodoRecuperacao(FceLicenciamentoMineracaoController controller) throws Exception {
		fceLicenciamentoMineralMetodoRecuperacaoService.excluirListaFceLicenciamentoMineralMetodoRecuperacao(controller.getFceLicenciamentoMineral());
		fceLicenciamentoMineralMetodoRecuperacaoService.salvarListaFceLicenciamentoMineralMetodoRecuperacao(montarListaFceLicenciamentoMineralMetodoRecuperacao(controller));
	}
	
	/**
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralTransporteMineiro}
	 *
	 * @author eduardo.fernandes
	 * @since 07/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoMineralTransporteMineiro(FceLicenciamentoMineracaoController controller) throws Exception {
		fceLicenciamentoMineralTransporteMineiroService.excluirListaFceLicenciamentoMineralTransporteMineiro(controller.getFceLicenciamentoMineral());
		fceLicenciamentoMineralTransporteMineiroService.salvarListaFceLicenciamentoMineralTransporteMineiro(montarListaFceLicenciamentoMineralTransporteMineiro(controller));
		
	}
	
	/**
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralTecnicaLavra}
	 *
	 * @author eduardo.fernandes
	 * @since 07/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoMineralTecnicaLavra(FceLicenciamentoMineracaoController controller) throws Exception {
		fceLicenciamentoMineralTecnicaLavraService.excluirListaFceLicenciamentoMineralTecnicaLavra(controller.getFceLicenciamentoMineral());
		fceLicenciamentoMineralTecnicaLavraService.salvarListaFceLicenciamentoMineralTecnicaLavra(montarListaFceLicenciamentoMineralTecnicaLavra(controller));
		
	}
	
	/**
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralMetodoLavra}
	 *
	 * @author eduardo.fernandes
	 * @since 07/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoMineralMetodoLavra(FceLicenciamentoMineracaoController controller) throws Exception {
		fceLicenciamentoMineralMetodoLavraService.excluirListaFceLicenciamentoMineralMetodoLavra(controller.getFceLicenciamentoMineral());
		fceLicenciamentoMineralMetodoLavraService.salvarListaFceLicenciamentoMineralMetodoLavra(montarListaFceLicenciamentoMineralMetodoLavra(controller));
	}
	
	/**
	 *
	 * Método para salvar à aba <b>Dados Gerais</b>.
	 *
	 * @author eduardo.fernandes
	 * @since 08/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAbaDadosGerais(FceLicenciamentoMineracaoController controller) throws Exception {
		salvarFceLicenciamentoMineral(controller);
		salvarListaFceLicenciamentoMineralSubstanciaMineral(controller);
		salvarListaFceLicenciamentoExploracaoRegimeExploracao(controller);
		if(controller.isExisteProcessoOutorga()) {
			super.salvarListaOutorgaMineracao(controller);
		}
		salvarListaFceLicenciamentoMineralOrigemEnergia(controller);
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @return
	 */
	private List<FceLicenciamentoMineralSubstanciaMineralTipologia> prepararListaFceLicenciamentoSubstanciaMineral(FceLicenciamentoMineracaoController controller) {
		for(FceLicenciamentoMineralSubstanciaMineralTipologia substanciaMineral : controller.getListaFceLicenciamentoMineralSubstanciaMineral()){
			substanciaMineral.setIdeFceLicenciamentoMineralSubstanciaMineralPK(new FceLicenciamentoMineralSubstanciaMineralTipologiaPK(substanciaMineral.getFceLicenciamentoMineral(), substanciaMineral.getSubstanciaMineralTipologia()));
		}
		return controller.getListaFceLicenciamentoMineralSubstanciaMineral();
	}
	
	/**
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralSubstanciaMineralTipologia}
	 *
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoMineralSubstanciaMineral(FceLicenciamentoMineracaoController controller) throws Exception {
		// fceLicenciamentoMineralSubstanciaMineralService.excluirListaFceLicenciamentoMineralSubstanciaMineral(controller.getFceLicenciamentoMineral());
		fceLicenciamentoMineralSubstanciaMineralService.salvarListaFceLicenciamentoMineralSubstanciaMineral(prepararListaFceLicenciamentoSubstanciaMineral(controller));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceLicenciamentoMineralSubstanciaMineral(FceLicenciamentoMineral fceLicenciamentoMineral, SubstanciaMineralTipologia substanciaMineralTipologia) throws Exception{
		fceLicenciamentoMineralSubstanciaMineralService.excluirFceLicenciamentoMineralSubstanciaMineral(fceLicenciamentoMineral, substanciaMineralTipologia);
	}
	
	/**
	 *
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoMineralOrigemEnergia}
	 *
	 * @author eduardo.fernandes
	 * @since 08/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoMineralOrigemEnergia(FceLicenciamentoMineracaoController controller) throws Exception {
		fceLicenciamentoMineralOrigemEnergiaService.excluirListaFceLicenciamentoMineralOrigemEnergia(controller.getFceLicenciamentoMineral());
		fceLicenciamentoMineralOrigemEnergiaService.salvarListaFceLicenciamentoMineralOrigemEnergia(montarListaFceLicenciamentoMineralOrigemEnergia(controller));
	}
	
	/**
	 *
	 * Método para salvar a tabela associativa
	 * {@link FceLicenciamentoExploracaoRegimeExploracao}
	 *
	 * @author eduardo.fernandes
	 * @since 08/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceLicenciamentoExploracaoRegimeExploracao(FceLicenciamentoMineracaoController controller) throws Exception {
		fceLicenciamentoExploracaoRegimeExploracaoService.excluirListaFceLicenciamentoExploracaoRegimeExploracao(controller.getFceLicenciamentoMineral());
		fceLicenciamentoExploracaoRegimeExploracaoService.salvarListaFceLicenciamentoExploracaoRegimeExploracao(montarListaFceLicenciamentoExploracaoRegimeExploracao(controller));
	}
	
	/**
	 * Método para salvar à aba <b>Quadro de Áreas</b>.
	 *
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAbaQuadroDeAreas(FceLicenciamentoMineracaoController controller) throws Exception {
		salvarFceLicenciamentoMineral(controller);
		super.salvarListaProcessoDNPM(controller.getListaProcessoDnpm());
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceLicenciamentoMineral(FceLicenciamentoMineracaoController controller) throws Exception {
		salvarFceLicenciamentoMineral(controller.getFceLicenciamentoMineral());
	}
	
	/**
	 * Método para persistir o {@link FceLicenciamentoMineral}
	 *
	 * @author eduardo.fernandes
	 * @since 28/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		salvarFce(fceLicenciamentoMineral);
		fceLicenciamentoMineralService.salvarFceLicenciamentoMineral(fceLicenciamentoMineral);
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFce(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		if(Util.isNullOuVazio(fceLicenciamentoMineral.getIdeFce())){
			super.salvarFce(fceLicenciamentoMineral.getIdeFce());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceLicenciamentoMineral buscarFceLicenciamentoMineralBy(Fce fce) throws Exception {
		FceLicenciamentoMineral fceLicenciamentoMineral = fceLicenciamentoMineralService.buscarFceLicenciamentoMineralByFce(fce);
		if(!Util.isNullOuVazio(fceLicenciamentoMineral.getIdeLocalizacaoGeograficaLavra())){
			fceLicenciamentoMineral.setAreaDeLavra(super.retornarAreaShape(fceLicenciamentoMineral.getIdeLocalizacaoGeograficaLavra()));
		}
		if(!Util.isNullOuVazio(fceLicenciamentoMineral.getIdeLocalizacaoGeograficaServidao())){
			fceLicenciamentoMineral.setAreaDeServidao(super.retornarAreaShape(fceLicenciamentoMineral.getIdeLocalizacaoGeograficaServidao()));
		}
		fceLicenciamentoMineral.setListaProcessoDNPM(super.listarProcessoDNPMby(fceLicenciamentoMineral));
		return fceLicenciamentoMineral;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegimeExploracao> listarRegimeExploracaoSalvoBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		return regimeExploracaoService.listarRegimeExploracao(fceLicenciamentoMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoOrigemEnergia> listarOrigemEnergiaSalvoBy(FceLicenciamentoMineral licenciamentoMineral) throws Exception {
		return super.tipoOrigemEnergiaService.listarTipoOrigemEnergiaBy(licenciamentoMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoLavra> listarMetodoLavraSalvoBy(FceLicenciamentoMineral licenciamentoMineral) throws Exception {
		return metodoLavraService.listarMetodoLavraBy(licenciamentoMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TecnicaLavra> listarTecnicaLavraSalvoBy(FceLicenciamentoMineral licenciamentoMineral) throws Exception {
		return tecnicaLavraService.listarTecnicaLavraBy(licenciamentoMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FormaDisposicaoRejeito> listarFormaDisposicaoRejeitoBy(FceLicenciamentoMineral licenciamentoMineral) throws Exception {
		return formaDisposicaoRejeitoService.listarFormaDisposicaoRejeitoBy(licenciamentoMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoTransporteMinerio> listarTipoTransporteMinerioSalvoBy(FceLicenciamentoMineral licenciamentoMineral) throws Exception {
		return tipoTransporteMineiroService.listarTipoTransporteMinerioBy(licenciamentoMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoRecuperacaoIntervencao> listarMetodoRecuperacaoIntervencaoSalvoBy(FceLicenciamentoMineral licenciamentoMineral) throws Exception {
		return metodoRecuperacaoIntervencaoService.listarMetodoRecuperacaoIntervencaoBy(licenciamentoMineral);
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralTipoApp> listarFceLicenciamentoMineralTipoAppSalvoBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		return fceLicenciamentoMineralTipoAppService.listarFceLicenciamentoMineralTipoAppBy(fceLicenciamentoMineral);
		
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoSistemaTratamento> listarTipoSistemaTratamentoSalvoBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		return tipoSistemaTratamentoService.listarTipoSistemaTratamentoBy(fceLicenciamentoMineral);
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoEmissaoAtmosferica> listaTipoEmissaoAtmosfericaSalvoBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		return tipoEmissaoAtmosfericaService.listaTipoEmissaoAtmosfericaSalvoBy(fceLicenciamentoMineral);
		
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoResiduoGerado> listarTipoResiduoGeradoSalvoBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		return tipoResiduoGeradoService.listarTipoResiduoGeradoBy(fceLicenciamentoMineral);
		
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DestinoResiduo> listarDestinoResiduoSalvoBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		return destinoResiduoService.listarDestinoResiduoBy(fceLicenciamentoMineral);
		
	}
	
	/**
	 *
	 *
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralSubstanciaMineralTipologia> listarFceLicenciamentoMineralSubstanciaMineralBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		return fceLicenciamentoMineralSubstanciaMineralService.listarFceLicenciamentoMineralSubstanciaMineralBy(fceLicenciamentoMineral);
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralServidaoMineraria> listarFceLicenciamentoMineralServidaoMinerariaBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		return fceLicenciamentoMineralServidaoMinerariaService.listarFceLicenciamentoMineralServidaoMinerariaBy(fceLicenciamentoMineral);
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 22/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProducaoProduto> listarProducaoProduto() throws Exception {
		List<ProducaoProduto> lista = producaoProdutoService.listarProducaoProduto();
		Collections.sort(lista);
		/*ProducaoProduto outros = producaoProdutoService.carregarProducaoProdutoOutros();
		lista.remove(outros);
		lista.add(lista.size(), outros);*/
		return lista;
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 25/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralProducaoProduto> listarFceLicenciamentoMineralProducaoProdutoBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception{
		return fceLicenciamentoMineralProducaoProdutoService.listarFceLicenciamentoMineralProducaoProdutoBy(fceLicenciamentoMineral);
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 25/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuExcluirListaFceLicenciamentoMineralProducaoProduto(FceLicenciamentoMineracaoController controller) throws Exception {
		if(!controller.getFceLicenciamentoMineral().getIndBeneficiamentoMineracao()) {
			fceLicenciamentoMineralProducaoProdutoService.excluirListaFceLicenciamentoMineralProducaoProduto(controller.getFceLicenciamentoMineral());
		}
		else {
			if(!Util.isNullOuVazio(controller.getListaFceLicenciamentoMineralProducaoProduto())){
				fceLicenciamentoMineralProducaoProdutoService.salvarListaFceLicenciamentoMineralProducaoProduto(prepararListaFceLicenciamentoMineralProducaoProduto(controller.getListaFceLicenciamentoMineralProducaoProduto()));
			}
		}
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 27/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a> 
	 * @param listaFceLicenciamentoMineralProducaoProduto
	 * @return
	 */
	private List<FceLicenciamentoMineralProducaoProduto> prepararListaFceLicenciamentoMineralProducaoProduto(List<FceLicenciamentoMineralProducaoProduto> listaFceLicenciamentoMineralProducaoProduto) {
		for(FceLicenciamentoMineralProducaoProduto fceLicenciamentoMineralProducaoProduto : listaFceLicenciamentoMineralProducaoProduto){
			fceLicenciamentoMineralProducaoProduto.setIdeFceLicenciamentoMineralProducaoProdutoPK(new FceLicenciamentoMineralProducaoProdutoPK(fceLicenciamentoMineralProducaoProduto.getFceLicenciamentoMineral(), fceLicenciamentoMineralProducaoProduto.getProducaoProduto()));
		}
		return listaFceLicenciamentoMineralProducaoProduto;
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double obterValorDaAtividade(Requerimento requerimento, Tipologia tipologia) throws Exception{
		return requerimentoTipologiaService.buscarValorAtividadeByRequerimentoAndTipologia(requerimento.getIdeRequerimento(), tipologia.getIdeTipologia()).doubleValue();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tipologia> listarTipologiaDaEtapa7(Requerimento requerimento) throws Exception{
		return super.tipologiaService.listarTipologiasToSubstanciaMineral(requerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceLicenciamentoMineracaoController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
}
