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

import br.gov.ba.seia.controller.FceLicenciamentoAquiculturaController;
import br.gov.ba.seia.controller.FceLicenciamentoAquiculturaDadosGeraisController;
import br.gov.ba.seia.controller.TanqueRedeController;
import br.gov.ba.seia.controller.ViveiroEscavadoController;
import br.gov.ba.seia.controller.abstracts.BaseFceLicenciamentoAquiculturaController;
import br.gov.ba.seia.dao.ProcessoAtoConcedidoDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoDAOImpl;
import br.gov.ba.seia.dto.AquiculturaAtividadeDTO;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Especie;
import br.gov.ba.seia.entity.EspecieAquiculturaTipoAtividade;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquiculturaEspecie;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceAquiculturaLicencaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeTipoProducao;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoLocalizacaoCultivo;
import br.gov.ba.seia.entity.FceLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceLocalizacaoGeograficaPK;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoAquicultura;
import br.gov.ba.seia.entity.TipoLocalizacaoCultivo;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.service.AquiculturaTipoAtividadeService;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnquadramentoDocumentoAtoService;
import br.gov.ba.seia.service.EspecieAquiculturaTipoAtividadeService;
import br.gov.ba.seia.service.EspecieService;
import br.gov.ba.seia.service.FceAquiculturaEspecieService;
import br.gov.ba.seia.service.FceAquiculturaLicencaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.FceAquiculturaLicencaService;
import br.gov.ba.seia.service.FceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaService;
import br.gov.ba.seia.service.FceAquiculturaLicencaTipoLocalizacaoCultivoService;
import br.gov.ba.seia.service.FceLocalizacaoGeograficaService;
import br.gov.ba.seia.service.TipoLocalizacaoCultivoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * Facade para gerenciar os Services relativos ao <b>FCE - Licenciamento para Aquicultura</b>.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 27/05/2015
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoAquiculturaServiceFacade {

	@EJB
	private FceOutorgaAquiculturaServiceFacade fceOutorgaAquiculturaServiceFacade;
	@EJB
	private FceServiceFacade fceServiceFacade;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private AquiculturaTipoAtividadeService atividadeService;
	@EJB
	private EspecieAquiculturaTipoAtividadeService especieAquiculturaTipoAtividadeService;
	@EJB
	private FceAquiculturaEspecieService fceAquiculturaEspecieService;
	@EJB
	private EnquadramentoDocumentoAtoService enquadramentoDocAtoService;
	@EJB
	private TipoLocalizacaoCultivoService tipoLocalizacaoCultivoService;
	@EJB
	private LocalizacaoGeograficaServiceFacade facadeLocGeoService;
	@EJB
	private FceLocalizacaoGeograficaService fceLocGeoService;
	@EJB
	private FceAquiculturaLicencaService fceAquiculturaLicencaService;
	@EJB
	private FceAquiculturaLicencaLocalizacaoGeograficaService fceAquiculturaLicencaLocalizacaoGeograficaService;
	@EJB
	private FceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaService fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaService;
	@EJB
	private FceAquiculturaLicencaTipoLocalizacaoCultivoService fceAquiculturaLicencaTipoLocalizacaoCultivoService;
	@EJB
	private TipologiaService tipologiaService;
	@EJB
	private EspecieService especieService;
	@EJB
	private FceOutorgaServiceFacade fceOutorgaServiceFacade;
	@EJB
	private CaracterizacaoCultivoServiceFacade caracterizacaoCultivoFacade;
	@EJB
	private AquiculturaTipoAtividadeService aquiculturaTipoAtividadeService;
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	@EJB
	private ProcessoAtoConcedidoDAOImpl processoAtoConcedidoDAOImpl;
	@EJB
	private ProcessoAtoDAOImpl processoAtoDAOImpl;

	/*
	 * INI - CONSULTA
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal buscarVazaoRequeridaInCaptacao(Requerimento requerimento) throws Exception {
		return fceOutorgaAquiculturaServiceFacade.buscarVazaoRequeridaInCaptacao(requerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal buscarVazaoRequeridaInLancamento(Requerimento requerimento) throws Exception {
		return fceOutorgaAquiculturaServiceFacade.buscarVazaoRequeridaInLancamento(requerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscarEmpreendimento(Requerimento requerimento) throws Exception{
		return empreendimentoService.carregarEmpreendimentoComLocGeoByRequerimento(requerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double retonarAreaShapeByGeometria(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		return facadeLocGeoService.retornarAreaShape(localizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String retornarGeometriaShapeByLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		return facadeLocGeoService.retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isTipoGeometriaPonto(Integer ideLocalizacao, String geometria) throws Exception {
		return facadeLocGeoService.isTipoGeometriaPonto(ideLocalizacao, geometria);
	}
	
	public boolean isTheGeomPersistido(LocalizacaoGeografica localizacaoGeografica) throws Exception{
		return !Util.isNullOuVazio(retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica));
	}
	
	/**
	 * Método que verifica se a {@link FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica} (Poligonal do Cultivo) está dentro dos limites do {@link Empreendimento}. 
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 03/11/2015
	 * @see <b>RN 00131</b>
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isValidarPoligonalCultivoInEmpreendimento(FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica poligonalCultivo, Empreendimento empreendimento) throws Exception {
		return facadeLocGeoService.isSobrePosicaoCompleta(empreendimento.getIdeLocalizacaoGeografica(), poligonalCultivo.getIdeLocalizacaoGeografica(), new Double(100.0));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isLocGeoComShapePersistido(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		String theGeom = retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica);
		if(!Util.isNullOuVazio(theGeom)){
			return !isTipoGeometriaPonto(localizacaoGeografica.getIdeLocalizacaoGeografica(), theGeom);
		}
		return false;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaEspecie> listarFceAquiculturaEspecieToViveiroEscavadoBy(Requerimento requerimento) throws Exception{
		return fceAquiculturaEspecieService.listarFceAquiculturaByIdeRequerimentoAndTipoAquicultura(requerimento, TipoAquiculturaEnum.VIVEIRO_ESCAVADO);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaEspecie> listarFceAquiculturaEspecieToTanqueRedeBy(Requerimento requerimento) throws Exception{
		return fceAquiculturaEspecieService.listarFceAquiculturaByIdeRequerimentoAndTipoAquicultura(requerimento, TipoAquiculturaEnum.TANQUE_REDE);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaEspecie> listarDistinctFceAquiculturaEspecieToTanqueRedeBy(Requerimento requerimento) throws Exception{
		return fceAquiculturaEspecieService.listarFceAquiculturaEspecieSemRepetirEspecieAquiculturaTipoAtividadeBy(requerimento, TipoAquiculturaEnum.TANQUE_REDE);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoLocalizacaoCultivo> listarTipoLocalizacaoCultivo() throws Exception{
		return tipoLocalizacaoCultivoService.listarTipoLocalizacaoCultivo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tipologia> listarTipologiasDoRequerimento(Requerimento requerimento) throws Exception{
		return fceOutorgaAquiculturaServiceFacade.listarTipologiasDoRequerimento(requerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceLocalizacaoGeografica buscarFceLocalizacaoGeograficaByFce(Fce fce) throws Exception{
		return fceLocGeoService.buscarFceLocalizacaoGeograficaByFce(fce);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceAquiculturaLicenca buscarFceAquiculturaLicencaByFce(Fce fce) throws Exception{
		return fceAquiculturaLicencaService.buscarFceAquiculturaByFce(fce);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaLocalizacaoGeografica> listarFceAquiculturaLicencaLocalizacaoGeograficaBy(FceAquiculturaLicenca fceAquiculturaLicenca, List<Tipologia> listaTipoLogia) throws Exception{
		List<FceAquiculturaLicencaLocalizacaoGeografica> lista = fceAquiculturaLicencaLocalizacaoGeograficaService.listarFceAquiculturaLicencaLocalizacaoGeograficaBy(fceAquiculturaLicenca);
		List<FceAquiculturaLicencaLocalizacaoGeografica> listaToRemove = new ArrayList<FceAquiculturaLicencaLocalizacaoGeografica>();
		for(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica : lista){
			fceAquiculturaLicencaLocalizacaoGeografica.setConfirmado(true);
			fceAquiculturaLicencaLocalizacaoGeografica.setIdeFceAquiculturaLicenca(fceAquiculturaLicenca);
			if(!listaTipoLogia.contains(fceAquiculturaLicencaLocalizacaoGeografica.getIdeTipologia())){
				listaToRemove.add(fceAquiculturaLicencaLocalizacaoGeografica);
			} else {
				if(listaTipoLogia.size() > 1){
					montarBaciaAndSubBacia(fceAquiculturaLicencaLocalizacaoGeografica);
				}
			}
		}
		for(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica : listaToRemove){
			lista.remove(fceAquiculturaLicencaLocalizacaoGeografica);
		}
		return lista;
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquiculturaLicencaLocalizacaoGeografica
	 * @throws Exception
	 * @since 14/08/2015
	 */
	public void montarBaciaAndSubBacia(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica) throws Exception {
		fceOutorgaServiceFacade.tratarBaciaAndSubBaciaFromCefir(fceAquiculturaLicencaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getBacia(LocalizacaoGeografica localizacaoGeografica){
		return fceOutorgaServiceFacade.getBacia(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getSubBacia(LocalizacaoGeografica localizacaoGeografica){
		return fceOutorgaServiceFacade.getSubBacia(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getRpga(LocalizacaoGeografica localizacaoGeografica){
		return fceOutorgaServiceFacade.getRpga(localizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void montarListaTipoLocalizacaoCultivoSelecionada(FceAquiculturaLicenca fceAquiculturaLicenca, List<AquiculturaAtividadeDTO> listaDTO) throws Exception{
		for(AquiculturaAtividadeDTO dto : listaDTO){
			if(!Util.isNull(dto.getTipoAtividade())){
				dto.setListaTipoLocalizacaoCultivo(tipoLocalizacaoCultivoService.listarTipoLocalizacaoCultivoBy(fceAquiculturaLicenca, dto.getTipoAtividade()));
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AquiculturaTipoAtividade> listarAquiculturaTipoAtividade() throws Exception{
		return fceOutorgaAquiculturaServiceFacade.listarAquiculturaTipoAtividade();
	}

	/**
	 * Método que retorna a lista de <i>Poligonal do Cultivo</i> daquela atividade.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 05/11/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> listarFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividade aquiculturaTipoAtividade, TipoAquiculturaEnum aquiculturaEnum) throws Exception{
		return fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaService.listarFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(fceAquiculturaLicenca, aquiculturaTipoAtividade, new TipoAquicultura(aquiculturaEnum));
	}

	/**
	 * Método que lista as {@link Tipologia}:
	 * <ul>
	 * 	<li>302 = Captação Superficial</li>
	 * 	<li>303 = Captação Subterrânea</li>
	 * 	<li>304 = Lançamento de Efluentes</li>
	 * </ul>
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @throws Exception
	 * @since 18/06/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tipologia> listarTipologia() throws Exception{
		return tipologiaService.listarTipologiaBy(TipologiaEnum.CAPTACAO_SUPERFICIAL, TipologiaEnum.LANCAMENTO_EFLUENTES);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia buscarTipologia() throws Exception{
		return tipologiaService.carregarTipologiaPorIde(TipologiaEnum.INTERVENCAO_CORPO_HIDRICO.getId());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AquiculturaTipoAtividade> listarAquiculturaTipoAtividadeBy(List<AquiculturaTipoAtividadeEnum> listEnum) throws Exception{
		return fceOutorgaAquiculturaServiceFacade.listarAquiculturatipoAtividadeBy(listEnum);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieAquiculturaTipoAtividade> listarEspecieAquiculturaTivoAtividadeByTipoAtividade(AquiculturaTipoAtividade tipoAtividade) throws Exception {
		return fceOutorgaAquiculturaServiceFacade.listarEspecieAquiculturaTivoAtividadeBy(tipoAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Especie> listarEspecieByAquiculturaTipoAtividade(AquiculturaTipoAtividade tipoAtividade) throws Exception {
		return especieService.listarEspecieByIdeAquiculturaTipoAtividade(tipoAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividade> listarFceAquiculturaLicencaTipoAtividadeToViveiroEscavadoBy(Requerimento requerimento) throws Exception {
		return caracterizacaoCultivoFacade.listarCaracterizacaoCultivoToViveiroEscavadoBy(requerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquiculturaLicencaTipoAtividade> listarFceAquiculturaLicencaTipoAtividadeToTanqueRedeBy(Requerimento requerimento) throws Exception {
		return caracterizacaoCultivoFacade.listarCaracterizacaoCultivoToTanqueRedeBy(requerimento);
	}
	
	/**
	 * Método que busca as {@link AquiculturaTipoAtividade} que foram selecionadas na aba Viveiro Escavado - Captação do FCE - Outorga para Aquicultura
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 19/11/2015
	 */
	public List<AquiculturaTipoAtividade> listarAquiculturaTipoAtividadeObrigatoriasInViveiroEscavado(Requerimento requerimento) throws Exception{
		return aquiculturaTipoAtividadeService.listarAquiculturaTipoAtividadePreenchidosNoFceOutorga(requerimento, TipoAquiculturaEnum.VIVEIRO_ESCAVADO); 
	}
	
	/**
	 * Método que busca as {@link AquiculturaTipoAtividade} que foram selecionadas nas abas Tanque Rede do FCE - Outorga para Aquicultura
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 19/11/2015
	 */
	public List<AquiculturaTipoAtividade> listarAquiculturaTipoAtividadeObrigatoriasInTanqueRede(Requerimento requerimento) throws Exception{
		return aquiculturaTipoAtividadeService.listarAquiculturaTipoAtividadePreenchidosNoFceOutorga(requerimento, TipoAquiculturaEnum.TANQUE_REDE); 
	}
	/*
	 * FIM - CONSULTA
	 */

	public List<FceAquiculturaLicencaTipoAtividade> listaFceAquiculturaTipoAtividadeViveiroEscavado(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividadeEnum aquiculturaTipoAtividadeEnum) throws Exception {
		return caracterizacaoCultivoFacade.listarCaracterizacaoCultivoToViveiroEscavadoBy(fceAquiculturaLicenca, aquiculturaTipoAtividadeEnum);
	}
	
	public void prepararEdicaoCaracterizacaoCultivo(List<FceAquiculturaLicencaTipoAtividade> lista) throws Exception{
		caracterizacaoCultivoFacade.montarRespostas(lista);
	}

	public List<FceAquiculturaLicencaTipoAtividade> listaFceAquiculturaTipoAtividadeTanqueRede(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividadeEnum aquiculturaTipoAtividadeEnum) throws Exception {
		return caracterizacaoCultivoFacade.listarCaracterizacaoCultivoToTanqueRedeBy(fceAquiculturaLicenca, aquiculturaTipoAtividadeEnum);
	}

	public void listarDependentesFormasJovens(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception{
		caracterizacaoCultivoFacade.listarDependentesFormasJovens(fceAquiculturaLicencaTipoAtividade);
	}

	public void listarDependentesEngorda(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception{
		caracterizacaoCultivoFacade.listarDependentesEngorda(fceAquiculturaLicencaTipoAtividade);
	}

	/**
	 * Método que verifica se é possível preencher o <b>FCE - Licenciamento para Aquicultura</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param requerimento
	 * @return
	 * @throws Exception
	 * @since 01/06/2015
	 */
	public boolean podeResponderFceLicenciamentoParaAquicultura(Requerimento requerimento) throws Exception{
		if(necessitaDeFceOutorgaParaAquiculturaIn(requerimento)){
			return isFceOutorgaParaAquiculturaRespondido(requerimento);
		}
		return true;
	}

	/**
	 * Método que verifica a presença de <b>FCE - Outorga para Aquicultura</b> na lista de Documentos solicitados no enquadramento.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param requerimento
	 * @return
	 * @throws Exception
	 * @since 01/06/2015
	 */
	private boolean necessitaDeFceOutorgaParaAquiculturaIn(Requerimento requerimento) throws Exception{
		return !Util.isNullOuVazio(enquadramentoDocAtoService.listarEnquadramentoDocAtoByRequerimentoToFceOutorgaAquicultura(requerimento));
	}

	/**
	 * Método que verifica se o <b>FCE - Outorga para Aquicultura</b> foi preenchido.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param requerimento
	 * @return
	 * @throws Exception
	 * @since 01/06/2015
	 */
	private boolean isFceOutorgaParaAquiculturaRespondido(Requerimento requerimento) throws Exception{
		Fce fce = fceServiceFacade.buscarFceByIdeRequerimentoDocumentoObrigatorioAndOrigemFce(requerimento, new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId()), DadoOrigemEnum.REQUERIMENTO);
		if(!Util.isNull(fce)){
			return !Util.isNull(fceServiceFacade.retornarFce(fce));
		}
		return false;
	}

	public Boolean isIntervencaoBarragem(Integer ideRequerimento, OutorgaLocalizacaoGeografica olg) throws Exception {
		return fceOutorgaAquiculturaServiceFacade.isIntervencaoBarragem(ideRequerimento, olg);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLocalizacaoGeograficaBy(LocalizacaoGeografica localizacaoGeografica) throws Exception{
		facadeLocGeoService.excluirDadoGeografico(localizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceLocalizacaoGeografica(FceLocalizacaoGeografica fceLocalizacaoGeografica) throws Exception{
		excluirLocalizacaoGeograficaBy(fceLocalizacaoGeografica.getIdeLocalizacaoGeografica());
		fceLocalizacaoGeografica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		if(!Util.isNull(fceLocalizacaoGeografica.getIdeFceLocalizacaoGeograficaPK())){
			fceLocGeoService.excluirFceLocalizacaoGeografica(fceLocalizacaoGeografica);
		}
		fceLocalizacaoGeografica.setIdeFceLocalizacaoGeograficaPK(null);
	}

	/**
	 * Método para salvar a <i>Poligonal do Galpão de Armazenamento (...)</i> na <b>Aba Dados Gerais</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceLocalizacaoGeografica
	 * @throws Exception
	 * @since 11/06/2015
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceLocalizacaoGeografica(FceLocalizacaoGeografica fceLocalizacaoGeografica) throws Exception{
		fceLocGeoService.salvarOuAtualizartFceLocalizacaoGeograficaPerfuracaoPoco(fceLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceAquiculturaLicenca(FceAquiculturaLicenca fceAquiculturaLicenca) throws Exception{
		if(Util.isNullOuVazio(fceAquiculturaLicenca.getIdeFce())){
			salvarFce(fceAquiculturaLicenca.getIdeFce());
		}
		if(Util.isNullOuVazio(fceAquiculturaLicenca.getNumVazaoCaptacao())){
			fceAquiculturaLicenca.setNumVazaoCaptacao(null);
		}
		if(Util.isNullOuVazio(fceAquiculturaLicenca.getNumVazaoLancamento())){
			fceAquiculturaLicenca.setNumVazaoLancamento(null);
		}
		fceAquiculturaLicencaService.salvarFceAquiculturaLicenca(fceAquiculturaLicenca);
	}

	/**
	 * Método para salvar as <i>Poligonais da Área de Cultivo</i>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param lista
	 * @throws Exception
	 * @since 11/06/2015
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica(List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica>  listaPoligonalCultivo) throws Exception{
		fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaService.salvarListaFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica(listaPoligonalCultivo);
	}

	/**
	 * Método para salvar a lista de {@link TipoLocalizacaoCultivo}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 07/11/2015
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceAquiculturaLicencaTipoLocalizacaoCultivo(List<FceAquiculturaLicencaTipoLocalizacaoCultivo> lista) throws Exception{
		
		if(!Util.isNullOuVazio(lista)) {
			excluirListaFceAquiculturaLicencaTipoLocalizacaoBy(lista.get(0).getIdeFceAquiculturaLicenca());
		}
		
		fceAquiculturaLicencaTipoLocalizacaoCultivoService.salvarListaFceAquiculturaLicencaTipoLocalizacaoCultivo(lista);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaFceAquiculturaLicencaLocalizacaoGeografica(List<FceAquiculturaLicencaLocalizacaoGeografica> listaFceAquiculturaLicencaLocalizacaoGeografica) throws Exception{
		for(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica : listaFceAquiculturaLicencaLocalizacaoGeografica){
			fceAquiculturaLicencaLocalizacaoGeografica.setIdeFceAquiculturaLicencaLocalizacaoGeografica(null);
		}
		fceAquiculturaLicencaLocalizacaoGeograficaService.salvarListaFceAquiculturaLicencaLocalizacaoGeografica(listaFceAquiculturaLicencaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirListaFceAquiculturaLicencaTipoLocalizacaoBy(FceAquiculturaLicenca fceAquiculturaLicenca) throws Exception {
		fceAquiculturaLicencaTipoLocalizacaoCultivoService.excluirListaFceAquiculturaLicencaTipoLocalizacaoCultivoBy(fceAquiculturaLicenca);
	}

	/**
	 * Método para excluir a lista de <i>Poligonal do Cultivo</i>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquiculturaLicenca
	 * @throws Exception
	 * @since 26/06/2015
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirListaPoligonalCultivoByAba(FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquiculturaEnum tipoAquiculturaEnum) throws Exception {
		fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaService.excluirListaFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(fceAquiculturaLicenca, tipoAquiculturaEnum);
	}
	
	// USADO NO FCE - OUTORGA
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceAquiculturaLicencaTipoLocalizacaoByAtividade(FceAquiculturaLicenca fceAquiculturaLicenca, AquiculturaTipoAtividade tipoAtividade) throws Exception {
		fceAquiculturaLicencaTipoLocalizacaoCultivoService.excluirListaFceAquiculturaLicencaTipoLocalizacaoCultivoBy(fceAquiculturaLicenca, tipoAtividade);
	}
	
	// USADO NO FCE - OUTORGA PARA AQUICULTURA
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaPoligonalCultivoByAtividade(FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquicultura tipoAquicultura, AquiculturaTipoAtividade tipoAtividade) throws Exception {
		fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaService.excluirListaFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(fceAquiculturaLicenca, tipoAquicultura, tipoAtividade);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica fceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica) throws Exception {
		if(!Util.isNull(fceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica.getIdeFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica())){
			fceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaService.excluirFceAquiculturaLicencaTipoAtividadeLocalizacaoGeograficaBy(fceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarFce(Fce fce) throws Exception{
		fceServiceFacade.salvarFce(fce);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarAbaDadosGerais(FceLicenciamentoAquiculturaDadosGeraisController controller) throws Exception{
		
		if(Util.isNull(controller.getFceLocalizacaoGeografica().getIdeFceLocalizacaoGeograficaPK())){
			prepararFceLocalizacaoGeografica(controller.getFceLocalizacaoGeografica());
		}
		
		salvarFceLocalizacaoGeografica(controller.getFceLocalizacaoGeografica());
		
		if(controller.getLicenciamentoAquiculturaController().isFceTecnico()) {
			if(!Util.isNullOuVazio(controller.getListProcessoAtoConcedidoEmpreendimento())) {
				for (ProcessoAtoConcedido pac : controller.getListProcessoAtoConcedidoEmpreendimento()) {
					salvarProcessoAtoConcedido(pac);
				}
			}
			
			if(!Util.isNullOuVazio(controller.getListProcessoAtoConcedidoGalpao())) {
				for (ProcessoAtoConcedido pac : controller.getListProcessoAtoConcedidoGalpao()) {
					salvarProcessoAtoConcedido(pac);
				}
			}
		}
	}

	/**
	 * Método que monta a chave composta do {@link FceLocalizacaoGeografica}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceLocalizacaoGeografica
	 * @param fceAquiculturaLicenca
	 * @since 11/06/2015
	 */
	private void prepararFceLocalizacaoGeografica(FceLocalizacaoGeografica fceLocalizacaoGeografica) {
		fceLocalizacaoGeografica.setIdeFceLocalizacaoGeograficaPK(new FceLocalizacaoGeograficaPK(fceLocalizacaoGeografica.getIdeFce().getIdeFce(), fceLocalizacaoGeografica.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void finalizarAba(BaseFceLicenciamentoAquiculturaController controller, TipoAquiculturaEnum tipoAquiculturaEnum) throws Exception{
		salvarFceAquiculturaLicenca(controller.getFceAquiculturaLicenca());

		if(controller.isPermitirCadastroOutorgaAbaAtiva()){
			excluirListaLocalizacaoCadastradaByAba(controller.getFceAquiculturaLicenca(), tipoAquiculturaEnum);
			salvarListaFceAquiculturaLicencaLocalizacaoGeografica(controller.getListaFceAquiculturaLicencaLocGeo());
		}
		for(AquiculturaAtividadeDTO aquiculturaAtividadeDTO : controller.getListaAtividadeDTO()){
			if(!Util.isNull(aquiculturaAtividadeDTO.getTipoAtividade())){
				salvarFceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica(aquiculturaAtividadeDTO.getListaPoligonalCultivo());
				for(FceAquiculturaLicencaTipoAtividade caracterizacaoCultivo : aquiculturaAtividadeDTO.getListaCaracterizacaoCultivo()){
					if(Util.isNull(caracterizacaoCultivo.getIdeTipoAquicultura())){
						caracterizacaoCultivo.setIdeTipoAquicultura(new TipoAquicultura(tipoAquiculturaEnum.getId()));
					}
					caracterizacaoCultivoFacade.salvarCaractericazaoCultivo(caracterizacaoCultivo);
				}
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarAbaViveiroEscavado(ViveiroEscavadoController controller) throws Exception{
		finalizarAba(controller, TipoAquiculturaEnum.VIVEIRO_ESCAVADO);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarAbaTanqueRede(TanqueRedeController controller) throws Exception{
		finalizarAba(controller, TipoAquiculturaEnum.TANQUE_REDE);
		salvarListaFceAquiculturaLicencaTipoLocalizacaoCultivo(montarListaLocalizacaoCultivoSelecionado(controller));
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 04/11/2015
	 */
	private List<FceAquiculturaLicencaTipoLocalizacaoCultivo> montarListaLocalizacaoCultivoSelecionado(TanqueRedeController controller){
		List<FceAquiculturaLicencaTipoLocalizacaoCultivo> listaToSavar = new ArrayList<FceAquiculturaLicencaTipoLocalizacaoCultivo>();
		for(AquiculturaAtividadeDTO dto : controller.getListaAtividadeDTO()){
			if(!Util.isNull(dto.getTipoAtividade())){
				for(TipoLocalizacaoCultivo tipoLocalizacaoCultivo : dto.getListaTipoLocalizacaoCultivo()){
					listaToSavar.add(new FceAquiculturaLicencaTipoLocalizacaoCultivo(controller.getFceAquiculturaLicenca(), dto.getTipoAtividade(), tipoLocalizacaoCultivo));
				}
			}
		}
		return listaToSavar;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAbaViveiroEscavado(ViveiroEscavadoController controller) throws Exception{
		excluirListaLocalizacaoCadastradaByAba(controller.getFceAquiculturaLicenca(), TipoAquiculturaEnum.VIVEIRO_ESCAVADO);
		excluirListaPoligonalCultivoByAba(controller.getFceAquiculturaLicenca(), TipoAquiculturaEnum.VIVEIRO_ESCAVADO);
		excluirListasCaracterizadas(controller);
		excluirListaCaracterizacaoCultivo(controller, TipoAquiculturaEnum.VIVEIRO_ESCAVADO);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAbaTanqueRede(TanqueRedeController controller) throws Exception{
		excluirListaFceAquiculturaLicencaTipoLocalizacaoBy(controller.getFceAquiculturaLicenca());
		excluirListaLocalizacaoCadastradaByAba(controller.getFceAquiculturaLicenca(), TipoAquiculturaEnum.TANQUE_REDE);
		excluirListaPoligonalCultivoByAba(controller.getFceAquiculturaLicenca(), TipoAquiculturaEnum.TANQUE_REDE);
		excluirListasCaracterizadas(controller);
		excluirListaCaracterizacaoCultivo(controller, TipoAquiculturaEnum.TANQUE_REDE);
	}

	/**
	 * Método para excluir a lista de {@link FceAquiculturaLicencaTipoAtividade} (Caracterização do Cultivo)
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 12/11/2015
	 */
	private void excluirListaCaracterizacaoCultivo(BaseFceLicenciamentoAquiculturaController controller, TipoAquiculturaEnum tipoAquiculturaEnum) throws Exception {
		caracterizacaoCultivoFacade.excluirListaFceAquiculturaLicencaTipoAtividadeBy(controller.getFceAquiculturaLicenca(), tipoAquiculturaEnum);
	}

	/**
	 * Método para excluir as listas preenchidas pra cada {@link FceAquiculturaLicencaTipoAtividadeTipoProducao} (Sistema de Cultivo, Tipo de Instalação, Organismos)
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 12/11/2015
	 */
	private void excluirListasCaracterizadas(BaseFceLicenciamentoAquiculturaController controller) throws Exception {
		for(AquiculturaAtividadeDTO atividadeDTO : controller.getListaAtividadeDTO()){
			if(!Util.isNull(atividadeDTO.getTipoAtividade())){
				for(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade : atividadeDTO.getListaCaracterizacaoCultivo()){
					caracterizacaoCultivoFacade.excluirSemSaberTipoProducao(fceAquiculturaLicencaTipoAtividade);
				}
			}
		}
	}

	/**
	 * Método para excluir a lista de Localizações cadastradas na(s) <b>ABA Viveiro Escavado</b> e/ou <b>ABA Tanque Rede</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquiculturaLicenca
	 * @throws Exception
	 * @since 26/06/2015
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirListaLocalizacaoCadastradaByAba(FceAquiculturaLicenca fceAquiculturaLicenca, TipoAquiculturaEnum tipoAquiculturaEnum) throws Exception{
		fceAquiculturaLicencaLocalizacaoGeograficaService.excluirFceAquiculturaLicencaLocalizacaoGeograficaBy(fceAquiculturaLicenca, tipoAquiculturaEnum);
	}
	
	/**
	 * Método para excluir a Localização cadastradas na(s) <b>ABA Viveiro Escavado</b> e/ou <b>ABA Tanque Rede</b>.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquiculturaLicenca
	 * @throws Exception
	 * @since 26/06/2015
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAquiculturaLicencaLocalizacaoGeograficaBy(FceAquiculturaLicencaLocalizacaoGeografica fceAquiculturaLicencaLocalizacaoGeografica) throws Exception{
		if(!Util.isNull(fceAquiculturaLicencaLocalizacaoGeografica.getIdeFceAquiculturaLicencaLocalizacaoGeografica())){
			fceAquiculturaLicencaLocalizacaoGeograficaService.excluirFceAquiculturaLicencaLocalizacaoGeograficaBy(fceAquiculturaLicencaLocalizacaoGeografica);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoCultivo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception{
		salvarFceAquiculturaLicenca(fceAquiculturaLicencaTipoAtividade.getIdeFceAquiculturaLicenca());
		caracterizacaoCultivoFacade.salvarCaractericazaoCultivo(fceAquiculturaLicencaTipoAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoCultivo(FceAquiculturaLicencaTipoAtividade fceAquiculturaLicencaTipoAtividade) throws Exception {
		caracterizacaoCultivoFacade.excluirCaracterizacaoCultivo(fceAquiculturaLicencaTipoAtividade);
	}
	
	public LocalizacaoGeografica duplicarLocalizacaoGeografica(LocalizacaoGeografica locGeo) throws Exception{
		return facadeLocGeoService.duplicarLocalizacaoGeografica(locGeo);
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
	public void finalizar(FceLicenciamentoAquiculturaController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
}