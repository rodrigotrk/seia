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

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.controller.BaseFceMineracaoController;
import br.gov.ba.seia.controller.FceLicenciamentoMineracaoController;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DestinoResiduo;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MetodoRecuperacaoIntervencao;
import br.gov.ba.seia.entity.OutorgaMineracao;
import br.gov.ba.seia.entity.ProcessoDnpm;
import br.gov.ba.seia.entity.ProcessoTramite;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SubstanciaMineral;
import br.gov.ba.seia.entity.SubstanciaMineralTipologia;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipoOrigemEnergia;
import br.gov.ba.seia.entity.TipoResiduoGerado;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.SistemaEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.DestinoResiduoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.MetodoRecuperacaoIntervencaoService;
import br.gov.ba.seia.service.OutorgaMineracaoService;
import br.gov.ba.seia.service.ProcessoDnpmService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.ProcessoTramiteService;
import br.gov.ba.seia.service.SubstanciaMineralService;
import br.gov.ba.seia.service.SubstanciaMineralTipologiaService;
import br.gov.ba.seia.service.TipoOrigemEnergiaService;
import br.gov.ba.seia.service.TipoResiduoGeradoService;
import br.gov.ba.seia.service.TipologiaService;
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
public class FceMineracaoFacade extends FceServiceFacade {

	@EJB
	protected DestinoResiduoService destinoResiduoService;

	@EJB
	protected TipoResiduoGeradoService tipoResiduoGeradoService;

	@EJB
	protected TipoOrigemEnergiaService tipoOrigemEnergiaService;

	@EJB
	private SubstanciaMineralService substanciaMineralService;
	
	@EJB
	private SubstanciaMineralTipologiaService substanciaMineralTipologiaService;
	
	@EJB
	protected TipologiaService tipologiaService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private ProcessoService processoService;

	@EJB
	private ProcessoTramiteService processoTramiteService;

	@EJB
	private EnquadramentoService enquadramentoService;

	@EJB
	protected LocalizacaoGeograficaServiceFacade localizacaoGeograficaFacade;

	@EJB
	protected ProcessoDnpmService processoDnpmService;

	@EJB
	protected OutorgaMineracaoService outorgaMineracaoService;
	
	@EJB
	protected MetodoRecuperacaoIntervencaoService metodoRecuperacaoIntervencaoService;
	
	@EJB
	private AtoAmbientalService atoAmbientalService; 

	/**
	 * Método que retorna a lista de {@link DestinoResiduo}.
	 * 
	 * @author eduardo.fernandes
	 * @since 09/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link DestinoResiduo}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DestinoResiduo> listarDestinoResiduo() throws Exception {
		return destinoResiduoService.buscarListaDestinoResiduo();
	}

	/**
	 * Método que retorna a lista de {@link TipoResiduoGerado}.
	 * 
	 * @author eduardo.fernandes
	 * @since 09/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link TipoResiduoGerado}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoResiduoGerado> listarTipoResiduoGerado() throws Exception {
		return tipoResiduoGeradoService.buscarListaTipoResiduoGerado();
	}

	/**
	 * Método que retorna a lista de {@link TipoOrigemEnergia}.
	 * 
	 * @author eduardo.fernandes
	 * @since 09/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link TipoOrigemEnergia}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoOrigemEnergia> listarTipoOrigemEnergia() throws Exception {
		return tipoOrigemEnergiaService.buscarListaTipoOrigemEnergia();
	}
	
	/**
	 * Método que retorna a lista de {@link SubstanciaMineral} viculadas a {@link Tipologia}.
	 * 
	 * @author alexandre.queiroz
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
	 * @return Lista {@link SubstanciaMineral}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SubstanciaMineralTipologia> listarSubstanciaMineralTipologiaBy(Tipologia tipologia) throws Exception {
		List<SubstanciaMineralTipologia> substanciasMineraisTipologias = substanciaMineralTipologiaService.listarSubstanciaMineralTipologiaBy(tipologia);
		SubstanciaMineralTipologia outros = substanciaMineralTipologiaService.buscarOutrosBy(tipologia);
		substanciasMineraisTipologias.remove(outros);
		substanciasMineraisTipologias.add(substanciasMineraisTipologias.size(), outros);
		return substanciasMineraisTipologias;
	}

	/**
	 * RN 00162 - Supressão de vegetação nativa <br/>
	 * (...) Será informado "Sim" se o usuário informou que existe processo de
	 * ASV no INEMA (independente se o ato foi deferido ou indeferido)
	 * 
	 * @author eduardo.fernandes
	 * @since 20/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param requerimento
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isExisteProcessoAsvEmTramite(Requerimento requerimento) throws Exception {
		List<ProcessoTramite> listarProcessoTramite = processoTramiteService.listarProcessoTramiteByRequerimento(requerimento);
		if(!Util.isNullOuVazio(listarProcessoTramite)){
			for(ProcessoTramite processoTramite : listarProcessoTramite){
				if(!Util.isNullOuVazio(processoService.buscarProcessoComAtoAmbientalByNumProcesso(processoTramite.getNumProcessoTramite(), new AtoAmbiental(AtoAmbientalEnum.ASV.getId())))){
					return true;
				}
			}
		}
		return false;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isProcessoDeOutorga(ProcessoTramite processoTramite) throws Exception {
		for(AtoAmbiental aa : listarAtosOutorga()){
			if(!Util.isNullOuVazio(processoService.buscarProcessoComAtoAmbientalByNumProcesso(processoTramite.getNumProcessoTramite(), aa))){
				return true;
			}
		}
		
		return false;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoTramite> listarProcessosOutorga(Requerimento requerimento) throws Exception {
		List<ProcessoTramite> lista = processoTramiteService.listarProcessoTramiteByRequerimento(requerimento);
		if(!Util.isNullOuVazio(lista)){
			List<ProcessoTramite> listaOutorga = new ArrayList<ProcessoTramite>();
			for(ProcessoTramite processoTramite : lista){
				if(processoTramite.getIdeSistema().getIdeSistema().equals(SistemaEnum.SEIA.getId()) && isProcessoDeOutorga(processoTramite)){
					listaOutorga.add(processoTramite);
				}
				else if(processoTramite.getIdeSistema().getIdeSistema().equals(SistemaEnum.PROHIDROS.getId())){
					listaOutorga.add(processoTramite);
				}
			}
			return listaOutorga;
		}
		return null;
	}

	/**
	 * 
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param requerimento
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	protected List<EnquadramentoAtoAmbiental> listarEnquadramentoAtoAmbiental(Requerimento requerimento) throws Exception {
		return enquadramentoService.buscarEnquadramentoAtoAmbientalByRequerimento(requerimento.getIdeRequerimento());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscarEmpreendimentoBy(Requerimento requerimento) throws Exception {
		return empreendimentoService.buscarEmpreendimentoPorRequerimento(requerimento.getIdeRequerimento());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double retornarAreaShape(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		return localizacaoGeograficaFacade.retornarAreaShape(localizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String retornarGeometriaShapeByLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		return localizacaoGeograficaFacade.retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaLocalizacaoGeografica(List<LocalizacaoGeografica> lista) throws Exception {
		if(!Util.isNullOuVazio(lista)){
			for(LocalizacaoGeografica localizacaoGeografica : lista){
				excluirLocalizacaoGeografica(localizacaoGeografica);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		localizacaoGeograficaFacade.excluirDadoGeografico(localizacaoGeografica);
	}
	
	/**
	 * Método que verifica se a nova poliognal inserida está contida dentro de outra.
	 * 
	 * @author eduardo.fernandes 
	 * @since 04/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param principal [Ex: Processo DPNM / Área de Lavra]
	 * @param contida 	[Ex: Área de Lavra / Área de Servidão]
	 * @return true - se uma poligonal estiver 100% contida na outra 
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isPoligonalContidaEmOutraPoligonal(LocalizacaoGeografica principal, LocalizacaoGeografica contida) throws Exception{ 
		return localizacaoGeograficaFacade.isSobrePosicaoCompleta(principal, contida, 100.00);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean naoExisteIntersecao(LocalizacaoGeografica localizacaoGeograficaA, LocalizacaoGeografica localizacaoGeograficaB) throws Exception {
		return localizacaoGeograficaFacade.naoExisteSobreposicao(localizacaoGeograficaA, localizacaoGeograficaB);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaProcessoDNPM(FceLicenciamentoMineracaoController controller) throws Exception {
		salvarListaProcessoDNPM(controller.getListaProcessoDnpm());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaProcessoDNPM(List<ProcessoDnpm> lista) throws Exception {
		processoDnpmService.salvarListaProcessoDnpm(lista);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcessoDnpm(ProcessoDnpm ideProcessoDnpm)throws Exception {
		processoDnpmService.excluirProcessoDnpmByIdeProcessoDnpm(ideProcessoDnpm);
	}
	
	
	/**
	 * 
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param object
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoDnpm> listarProcessoDNPMby(Object object) throws Exception {
		return processoDnpmService.listarProcessoDnpmBy(object);

	}

	/**
	 * 
	 * @author eduardo.fernandes
	 * @since 15/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaOutorgaMineracao(BaseFceMineracaoController controller) throws Exception {
		outorgaMineracaoService.salvarListaOutorgaMineracao(controller.getListaOutorgaMineracao());
	}

	/**
	 * 
	 * @author eduardo.fernandes
	 * @since 15/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param object
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaMineracao> listarOutorgaMineracaoBy(Object object) throws Exception {
		return outorgaMineracaoService.listarOutorgaMineracaoBy(object);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LocalizacaoGeografica duplicarLocalizacaoGeografica(LocalizacaoGeografica locGeo) throws Exception {
		return localizacaoGeograficaFacade.duplicarLocalizacaoGeografica(locGeo);
	}

	/**
	 * Método que retorna a lista de {@link MetodoRecuperacaoIntervencao}.
	 *
	 * @author eduardo.fernandes
	 * @since 13/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return Lista {@link MetodoRecuperacaoIntervencao}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoRecuperacaoIntervencao> listarMetodoRecuperacaoIntervencao() throws Exception{
		return metodoRecuperacaoIntervencaoService.listarMetodoRecuperacaoIntervencao();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TreeNode montarArvoreTipologiasMineracao(Tipologia divisao, Collection<Tipologia> listaTipologia) throws Exception {
		Collection<Tipologia> tipologiasMineracao = new ArrayList<Tipologia>();
		if(!Util.isNull(listaTipologia)){
			List<Tipologia> tipologiasNo = new ArrayList<Tipologia>();
			for(Tipologia tipologia : listaTipologia){
				if(tipologia.getIdeNivelTipologia().getIdeNivelTipologia().equals(3)){
					Tipologia tipologiaPai = tipologiaService.buscarTipologiaPaiByTipologiaFilho(tipologia);
					if(!tipologiasNo.contains(tipologia)){
						tipologiasNo.add(tipologiaPai);
					}
				}
				else if(tipologia.getIdeNivelTipologia().getIdeNivelTipologia().equals(4)){
					Tipologia tipoAvo = tipologiaService.buscarTipologiaPaiByTipologiaFilho(tipologia.getIdeTipologiaPai());
					if(!tipologiasNo.contains(tipoAvo)){
						tipologiasNo.add(tipoAvo);
					}
				}
			}
			tipologiasMineracao.addAll(tipologiasNo);
		} 
		else {
			tipologiasMineracao = tipologiaService.listarTipologias(divisao, false);
			tipologiasMineracao.remove(new Tipologia(TipologiaEnum.EXTRACAO_PETROLEO_GAS));
		}

		this.carregarTipologiaFilha(tipologiasMineracao);
		
		if(!Util.isNullOuVazio(listaTipologia)){
			List<Tipologia> tipologiasDescartaveis = new ArrayList<Tipologia>();
			for(Tipologia t : tipologiasMineracao){
				preencherTipologiasDescartaveis(t, listaTipologia, tipologiasDescartaveis);
			}
			removerTipologiasDescartaveis(tipologiasMineracao, tipologiasDescartaveis);
		} 

		TreeNode root = new DefaultTreeNode("RAIZ", null);

		this.gerarArvoreTipologia(tipologiasMineracao, root);
		
		return root;
	}
	
	private void preencherTipologiasDescartaveis(Tipologia tipologia, Collection<Tipologia> tipologiasRequerimento, Collection<Tipologia> tipologiasDescartaveis){
		if(!Util.isNullOuVazio(tipologia.getTipologiaCollection())){
			for(Tipologia t : tipologia.getTipologiaCollection()) {
				preencherTipologiasDescartaveis(t, tipologiasRequerimento, tipologiasDescartaveis);
			}
		}
		else {
			if(!tipologiasRequerimento.contains(tipologia)){
				tipologiasDescartaveis.add(tipologia);
			}
		}
	}
	
	private void removerTipologiasDescartaveis(Collection<Tipologia> tipologiasNo, Collection<Tipologia> tipologiasDescartaveis){
		Collection<Tipologia> temp = new ArrayList<Tipologia>();
		temp.addAll(tipologiasNo);
		for(Tipologia no : temp){
			if(!Util.isNullOuVazio(no.getTipologiaCollection())){
				removerTipologiasDescartaveis(no.getTipologiaCollection(), tipologiasDescartaveis);
			} 
			else {
				if(tipologiasDescartaveis.contains(no)){
					tipologiasNo.remove(no);
				} 
			}
			if(Util.isNullOuVazio(no.getTipologiaCollection()) && no.getIndPossuiFilhos()){
				tipologiasNo.remove(no);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarTipologiaFilha(Collection<Tipologia> tipologias) throws Exception {
		for (Tipologia tipologia : tipologias) {
			if(tipologia.getIndPossuiFilhos()){
				Collection<Tipologia> filhas = tipologiaService.listarTipologias(tipologia,false);
				tipologia.setTipologiaCollection(filhas);
				this.carregarTipologiaFilha(filhas);
			}
			else {
				tipologia.setSubstanciaMineralTipologiaCollection(listarSubstanciaMineralTipologiaBy(tipologia));
			}
		}
	}

	private void gerarArvoreTipologia(Collection<Tipologia> collection, TreeNode root) {
		for (Tipologia tipologia : collection) {
			TreeNode node = new DefaultTreeNode("tipologia", tipologia, root);
			if(tipologia.getIndPossuiFilhos()){
				this.gerarArvoreTipologia(tipologia.getTipologiaCollection(), node);
			} 
			else {
				for(SubstanciaMineralTipologia substanciaMineral : tipologia.getSubstanciaMineralTipologiaCollection()){
					new DefaultTreeNode("substancia", substanciaMineral, node);
				}
			}
		}
	}
	
	/**
	 * Método para listar os Atos considerados de "Outorga", todos do tipo Autorização com exceção de Sisfauna.
	 * 
	 * @author eduardo.fernandes
	 * @since 01/09/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> 
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)	
	public Collection<AtoAmbiental> listarAtosOutorga() throws Exception{
		Collection<AtoAmbiental> atoDeAutorizacao = atoAmbientalService.listarAtoAmbientalByTipoAtoByAtivo(new TipoAto(TipoAtoEnum.AUTORIZACAO.getId()));
		atoDeAutorizacao.remove(new AtoAmbiental(AtoAmbientalEnum.SISFAUNA.getId()));
		return atoDeAutorizacao; 
	}
}
