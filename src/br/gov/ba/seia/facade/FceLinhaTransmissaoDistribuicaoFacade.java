package br.gov.ba.seia.facade;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.FceLinhaTransmissaoDistribuicaoController;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoConcedidoDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoDAOImpl;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.DestinoResiduo;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLinhaEnergia;
import br.gov.ba.seia.entity.FceLinhaEnergiaDestinoResiduo;
import br.gov.ba.seia.entity.FceLinhaEnergiaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceLinhaEnergiaResiduoGerado;
import br.gov.ba.seia.entity.FceLinhaEnergiaTipoEnergia;
import br.gov.ba.seia.entity.FceLinhaEnergiaTipoSubestacao;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.TipoEnergia;
import br.gov.ba.seia.entity.TipoResiduoGerado;
import br.gov.ba.seia.entity.TipoSubestacao;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.DestinoResiduoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.FceLinhaEnergiaDestinoResiduoService;
import br.gov.ba.seia.service.FceLinhaEnergiaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.FceLinhaEnergiaResiduoGeradoService;
import br.gov.ba.seia.service.FceLinhaEnergiaService;
import br.gov.ba.seia.service.FceLinhaEnergiaTipoEnergiaService;
import br.gov.ba.seia.service.FceLinhaEnergiaTipoSubestacaoService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.TipoEnergiaService;
import br.gov.ba.seia.service.TipoResiduoGeradoService;
import br.gov.ba.seia.service.TipoSubestacaoService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.util.BigDecimalUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLinhaTransmissaoDistribuicaoFacade {
	
	@EJB
	private TipoEnergiaService tipoEnergiaService;
	
	@EJB
	private TipoResiduoGeradoService tipoResiduoGeradoSerivce;
	
	@EJB
	private FceLinhaEnergiaService fceLinhaEnergiaService;
	
	@EJB
	private DestinoResiduoService destinoResiduoService;
	
	@EJB
	private TipoSubestacaoService tipoSubestacaoService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;
	
	@EJB
	private FceLinhaEnergiaResiduoGeradoService fceLinhaEnergiaResiduoGeradoService;
	
	@EJB
	private FceLinhaEnergiaDestinoResiduoService fceLinhaEnergiaDestinoResiduoService;
	
	@EJB
	private FceLinhaEnergiaTipoSubestacaoService fceLinhaEnergiaTipoSubestacaoService;
	
	@EJB
	private FceLinhaEnergiaLocalizacaoGeograficaService fceLinhaEnergiaLocalizacaoGeograficaService;
	
	@EJB
	private FceLinhaEnergiaTipoEnergiaService fceLinhaEnergiaTipoEnergiaService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	
	@EJB
	private ProcessoAtoDAOImpl processoAtoDAOImpl;
	
	@EJB
	private ProcessoAtoConcedidoDAOImpl processoAtoConcedidoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<TipoEnergia> buscarTiposEnergias() throws Exception {
		return tipoEnergiaService.buscarTiposEnergias();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoResiduoGerado> buscarListaTipoResiduoGerado() throws Exception {
		return tipoResiduoGeradoSerivce.buscarListaTipoResiduoGerado();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(FceLinhaEnergia fceLinhaEnergia) throws Exception {
		fceLinhaEnergiaService.salvarOuAtualizar(fceLinhaEnergia);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLinha(FceLinhaEnergia fceLinhaEnergia, FceLinhaEnergia fceLinhaEnergiaRemover) throws Exception {
		removerTodos(fceLinhaEnergiaRemover);
		salvarOuAtualizar(fceLinhaEnergia);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DestinoResiduo> buscarListaDestinoResiduo() throws Exception {
		return destinoResiduoService.buscarListaDestinoResiduo();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoSubestacao> buscarTipoSubestacao() throws Exception {
		return tipoSubestacaoService.buscarTipoSubestacao();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoSubestacao buscarPorId(TipoSubestacao tipoSubestacao) throws Exception {
		return tipoSubestacaoService.buscarPorId(tipoSubestacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscarEmpreendimentoPorRequerimento(Integer ideRequerimento) throws Exception {
		return empreendimentoService.buscarEmpreendimentoPorRequerimento(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Double> listarCodIBGE(Integer ideLocalizacaoGeografica) throws Exception {
		return localizacaoGeograficaService.listarCodIBGE(ideLocalizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double retornarAreaShape(LocalizacaoGeografica locGeo) throws Exception {
		return validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(obterTheGeom(locGeo));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLocalizacaoGeografica(FceLinhaEnergiaLocalizacaoGeografica fceLinhaEnergiaLocalizacaoGeografica) throws Exception {
		
		LocalizacaoGeografica localizacaoGeografica = fceLinhaEnergiaLocalizacaoGeografica.getIdeLocalizacaoGeografica();
		
		if (!Util.isNull(fceLinhaEnergiaLocalizacaoGeografica.getIdeFceLinhaEnergiaLocalizacaoGeografica())) {
			// salvarOuAtualizar(fceLinhaEnergiaLocalizacaoGeografica.getIdeFceLinhaEnergia());
			removerFceLinhaEnergiaLocalizacaoGeografica(fceLinhaEnergiaLocalizacaoGeografica);
		}
		
		fceLinhaEnergiaLocalizacaoGeografica.setIdeLocalizacaoGeografica(null);
		
		localizacaoGeograficaService.excluirTudoPorLocalizacaoGeografica(localizacaoGeografica);
	}
	
	/**
	 * A partir da localização geografica informada, se existir o shape, preenche a o valor da área
	 * @param localizacaoGeografica
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LocalizacaoGeografica obterValAreaByTheGemo(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		
		String theGeom = obterTheGeom(localizacaoGeografica);
		
		if (!Util.isNullOuVazio(theGeom)) {
			Double valorArea = validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom);
			
			if (!Util.isNullOuVazio(valorArea)) {
				localizacaoGeografica.setVlrArea(BigDecimalUtil.aproximar(BigDecimal.valueOf(valorArea.doubleValue()), 4));
			}
		}
		
		return localizacaoGeografica;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String obterTheGeom(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		return validacaoGeoSeiaService.buscarGeometriaShape(localizacaoGeografica.getIdeLocalizacaoGeografica());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> listarMunicipios(LocalizacaoGeografica ideLocalizacaoGeografica) throws Exception {
		
		return (List<Municipio>) municipioService.listarMunicipioPorListaIBGE(
				localizacaoGeograficaService.listarCodIBGE(ideLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
	}
	
	public Double buscarValorAtividadeByRequerimentoAndTipologia(Integer ideRequerimento, Integer ideTipologia) throws Exception {
		return requerimentoTipologiaService.buscarValorAtividadeByRequerimentoAndTipologia(ideRequerimento, ideTipologia).doubleValue();
	}
	
	public FceLinhaEnergia buscarFceLinhaEnergiaPorFce(Fce fce) throws Exception {
		return fceLinhaEnergiaService.buscarFceLinhaEnergiaPorFce(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaResiduoGeradoById(FceLinhaEnergiaResiduoGerado fceLinhaEnergiaResiduoGerado) throws Exception {
		fceLinhaEnergiaResiduoGeradoService.removerFceLinhaEnergiaResiduoGeradoById(fceLinhaEnergiaResiduoGerado);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaDestinoResiduo(FceLinhaEnergiaDestinoResiduo fceLinhaEnergiaDestinoResiduo) throws Exception {
		fceLinhaEnergiaDestinoResiduoService.removerFceLinhaEnergiaDestinoResiduo(fceLinhaEnergiaDestinoResiduo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaTipoSubestacao(FceLinhaEnergiaTipoSubestacao fceLinhaEnergiaTipoSubestacao) throws Exception {
		fceLinhaEnergiaTipoSubestacaoService.removerFceLinhaEnergiaTipoSubestacao(fceLinhaEnergiaTipoSubestacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaLocalizacaoGeografica(FceLinhaEnergiaLocalizacaoGeografica fceLinhaEnergiaLocalizacaoGeografica) throws Exception {
		fceLinhaEnergiaLocalizacaoGeograficaService.removerFceLinhaEnergiaLocalizacaoGeografica(fceLinhaEnergiaLocalizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaTipoEnergia(FceLinhaEnergiaTipoEnergia fceLinhaEnergiaTipoEnergia) throws Exception {
		fceLinhaEnergiaTipoEnergiaService.removerFceLinhaEnergiaTipoEnergia(fceLinhaEnergiaTipoEnergia);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerTodos(FceLinhaEnergia fceLinhaEnergia) {
		try {
			if (!Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaResiduoGeradoCollection())
					&& !Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaDestinoResiduoCollection())
					&& !Util.isNull(fceLinhaEnergia.getFceLinhaEnergiaTipoSubestacaoCollection())
					&& !Util.isNull(fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection())
					&& !Util.isNullOuVazio(fceLinhaEnergia.getFceLinhaEnergiaTipoEnergiaCollection())) {
				
				for (FceLinhaEnergiaTipoEnergia removeLinhaTipoEnergia : fceLinhaEnergia.getFceLinhaEnergiaTipoEnergiaCollection()) {
					removerFceLinhaEnergiaTipoEnergia(removeLinhaTipoEnergia);
				}
				
				for (FceLinhaEnergiaResiduoGerado removeResiduo : fceLinhaEnergia.getFceLinhaEnergiaResiduoGeradoCollection()) {
					removerFceLinhaEnergiaResiduoGeradoById(removeResiduo);
				}
				
				for (FceLinhaEnergiaDestinoResiduo removeDestino : fceLinhaEnergia.getFceLinhaEnergiaDestinoResiduoCollection()) {
					removerFceLinhaEnergiaDestinoResiduo(removeDestino);
				}
				
				for (FceLinhaEnergiaTipoSubestacao tipoSubestacao : fceLinhaEnergia.getFceLinhaEnergiaTipoSubestacaoCollection()) {
					removerFceLinhaEnergiaTipoSubestacao(tipoSubestacao);
				}
				
				for (FceLinhaEnergiaLocalizacaoGeografica removeLocGeo : fceLinhaEnergia.getFceLinhaEnergiaLocalizacaoGeograficaCollection()) {
					removerFceLinhaEnergiaLocalizacaoGeografica(removeLocGeo);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LocalizacaoGeografica duplicarLocalizacaoGeografica(LocalizacaoGeografica locGeo) throws Exception {
		return localizacaoGeograficaService.duplicarLocalizacaoGeografica(locGeo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo buscarPorRequerimento(Integer ideRequerimento) throws Exception {
		return processoService.buscarPorRequerimento(ideRequerimento);
	}
	
	public List<Notificacao> listarPorProcesso(Processo proc) {
		return notificacaoDAOImpl.listarPorProcesso(proc);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ArquivoProcesso> listaArquivoProcessoPorIdeProcesso(Integer ideRequerimento) throws Exception {
		return arquivoProcessoService.listarArquivoProcessoPorProcesso(processoService.buscarPorRequerimento(ideRequerimento).getIdeProcesso());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LocalizacaoGeografica carregar(Integer ide) throws Exception {
		return localizacaoGeograficaService.carregar(ide);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao carregarById(Integer ideNotificacao) throws Exception {
		return notificacaoDAOImpl.carregarById(ideNotificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> listarAtosPorProcesso(Processo ideProcesso) throws Exception {
		return processoAtoDAOImpl.listarAtosPorProcesso(ideProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcelinhaConcedido(ProcessoAtoConcedido processoAtoConcedido, FceLinhaEnergia fceLinhaEnergia, FceLinhaEnergia fceLinhaEnergiaRemover) throws Exception {
		salvarFceLinha(fceLinhaEnergia, fceLinhaEnergiaRemover);
		processoAtoConcedidoDAOImpl.salvar(processoAtoConcedido);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAtoConcedido> listarProcessoAtoConcedido(Integer ideProcessoAto) throws Exception {
		return processoAtoConcedidoDAOImpl.listarProcessoAtoConcedido(ideProcessoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean existeProcessoAtoConcedido(Integer ideProcessoAto) throws Exception {
		return processoAtoConcedidoDAOImpl.existeProcessoAtoConcedido(ideProcessoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceLinhaTransmissaoDistribuicaoController fceLinhaTransmissaoDistribuicaoController) throws Exception {
		fceLinhaTransmissaoDistribuicaoController.prepararParaFinalizar();
	}
}
