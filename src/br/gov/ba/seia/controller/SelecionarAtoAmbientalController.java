package br.gov.ba.seia.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ObjetivoAtividadeManejo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.entity.TipoAtividadeFauna;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.exception.SeiaRuntimeException;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.SelecionarAtoAmbientalServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;

@Named("selecionarAtoAmbientalController")
@ViewScoped
public class SelecionarAtoAmbientalController {

	private static final String TIPO_AUTO_OUTORGA = "OUTORGA";

	@EJB
	private SelecionarAtoAmbientalServiceFacade selecionarAtoAmbientalServiceFacade;
	
	@EJB
	private TipologiaService tipologiaService;

	private ReenquadramentoProcessoAto reenquadramentoProcessoAto;
	
	private TipoAto tipoAtoSelecionado;
	private AtoAmbiental atoAmbientalSelecionado;
	private Tipologia tipologiaSelecionada;
	private Collection<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaSelecionadas;
	private Collection<ObjetivoAtividadeManejo> objetivoAtividadeManejoSelecionados;
	private Collection<TipoAtividadeFauna> tipoAtividadeFaunaSelecionadas;
	private String justificativa;
	private Collection<TipoAto> listaTipoAto;
	private Collection<AtoAmbiental> listaAtoAmbiental;
	private Collection<Tipologia> listaTipologia;
	private Collection<TipoFinalidadeUsoAgua> listaTipoFinalidadeUsoAgua;
	private Collection<Tipologia> listaTipologiaEmpreendimento;
	private Collection<ObjetivoAtividadeManejo> listaObjetivoAtividadeManejo;
	private Collection<TipoAtividadeFauna> listaTipoAtividadeFauna;
	private Map<String,Object> parametros;

	private List<AtoAmbiental> listaAtoAutorizacaoOutorga;
	private Tipologia tipologia;
	
	private TreeNode root;
	private TreeNode selectedNode;
	
	private boolean  isAlteracao;
	
	@PostConstruct
	public void init() {
		
	}
	
	/**
	 * Método de inicialização da classe
	 * @param ActionEvent usado para passar parametros via tag <f:attribute>
	 */
	public void load(ActionEvent evt) {
		try {
			inicializarVariaveis(evt);
			preparar();
			Html.atualizar("frmSelecionarAtoAmbiental");
			Html.exibir("dlgSelecionarAtoAmbiental");
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método utilizado para capturar os parametros passados via tag <f:attribute>
	 * @throws Exception
	 */
	private void preparar() throws Exception {
		
		MetodoUtil metodoSelecionarReenquadramentoProcessoAto = (MetodoUtil) parametros.get("metodoSelecionarReenquadramentoProcessoAto");
		if(!Util.isNull(metodoSelecionarReenquadramentoProcessoAto)) {
			Object object = parametros.get("reenquadramentoProcessoAto");
			if(!Util.isNull(object)) {
				metodoSelecionarReenquadramentoProcessoAto.executeMethod(object);
				reenquadramentoProcessoAto = object instanceof ReenquadramentoProcessoAto ? (ReenquadramentoProcessoAto) object : null;
			}
		}
		
		Object alterarAtoAutorizativo = parametros.get("alterarAtoAutorizativo");
		if(!Util.isNull(alterarAtoAutorizativo)) {
			isAlteracao = (Boolean) alterarAtoAutorizativo;
		}
	}
	
	/**
	 * Método utilizado para iniciar as variáveis
	 * @throws Exception
	 */
	private void inicializarVariaveis(ActionEvent evt) throws Exception {
		atoAmbientalSelecionado = null;
		tipoAtoSelecionado = null;
		tipologiaSelecionada = null;
		tipoFinalidadeUsoAguaSelecionadas = null;
		objetivoAtividadeManejoSelecionados = null;
		listaTipoFinalidadeUsoAgua = null;
		parametros = evt.getComponent().getAttributes();
		listaTipoAto = selecionarAtoAmbientalServiceFacade.listarTipoAtoComAtoAmbiental();
		listaAtoAutorizacaoOutorga = Arrays.asList(new AtoAmbiental(AtoAmbientalEnum.OUTORGA), new AtoAmbiental(AtoAmbientalEnum.OUTP), new AtoAmbiental(AtoAmbientalEnum.DOUT));		
		listaTipologiaEmpreendimento = new ArrayList<Tipologia>();
		justificativa="";
		//#9319
		if(listaTipoAto != null || !listaTipoAto.isEmpty()){
			for (TipoAto item : listaTipoAto) {
				if(TIPO_AUTO_OUTORGA.equalsIgnoreCase(item.getNomTipoAto())){
					listaTipoAto.remove(item);
					break;
				}
			}
		}

	}
	
	/**
	 * Método acionado no onChange do <p:selectOneMenu> do campo categoria
	 */
	public void onChangeTipoAto() {
		try {
			atoAmbientalSelecionado = null;
			tipologiaSelecionada = null;
			tipoFinalidadeUsoAguaSelecionadas=null;
			objetivoAtividadeManejoSelecionados=null;
			if(Util.isNull(tipoAtoSelecionado)) {
				listaAtoAmbiental = null;
				listaTipologia = null;
			}
			else {
				carregarListaAtoAmbiental();
			}
			Html.atualizar("frmSelecionarAtoAmbiental");
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método que carrega a lista de atos ambientais que será exibida no <p:selectOneMenu> do campo ato ambiental
	 * @throws Exception
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void carregarListaAtoAmbiental() throws Exception, IllegalAccessException, InvocationTargetException {
		listaAtoAmbiental = selecionarAtoAmbientalServiceFacade.listarAtoAmbientalPorTipoAto(tipoAtoSelecionado);
		MetodoUtil metodoListarAtosExistentes = (MetodoUtil) parametros.get("metodoListarAtosExistentes");
		if(!Util.isNull(metodoListarAtosExistentes) && !Util.isNull(metodoListarAtosExistentes.executeMethod())) {
			Collection<ProcessoAto> listaProcessoAtoExistente = Util.castCollection(metodoListarAtosExistentes.executeMethod(), ProcessoAto.class);
			if(listaProcessoAtoExistente != null && !listaProcessoAtoExistente.isEmpty()){
				for(ProcessoAto pa : listaProcessoAtoExistente){
					if (Util.isNullOuVazio(pa.getTipologia())){
						for (Iterator<AtoAmbiental> it = listaAtoAmbiental.iterator(); it.hasNext(); ) {
							AtoAmbiental atoAmbiental = it.next();
							
							if(atoAmbiental.getIdeAtoAmbiental().equals(pa.getAtoAmbiental().getIdeAtoAmbiental())){
								it.remove();
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Método acionado no onChange do <p:selectOneMenu> do campo ato ambiental
	 */
	public void onChangeAtoAmbiental() {
		try {
			tipologiaSelecionada=null;
			tipoFinalidadeUsoAguaSelecionadas=null;
			objetivoAtividadeManejoSelecionados=null;
			tipoAtividadeFaunaSelecionadas=null;
			if(Util.isNull(atoAmbientalSelecionado)) {
				listaTipologia = null;
			}
			else if(isAMF()) {
				carregarObjetosParaAMF();
			}
			else {
				carregarListaTipologia();
			}
			Html.atualizar("frmSelecionarAtoAmbiental");
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método que carrega a lista de tipologias que será exibida no <p:selectOneMenu> do tipologia
	 * @throws Exception
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void carregarListaTipologia() throws Exception, IllegalAccessException, InvocationTargetException {
		listaTipologia = selecionarAtoAmbientalServiceFacade.listarTipologiaPorAtoAmbiental(atoAmbientalSelecionado);
		MetodoUtil metodoListarAtosExistentes = (MetodoUtil) parametros.get("metodoListarAtosExistentes");
		if(!Util.isNull(metodoListarAtosExistentes) && !Util.isNull(metodoListarAtosExistentes.executeMethod())) {
			Collection<ProcessoAto> listaProcessoAtoExistente = Util.castCollection(metodoListarAtosExistentes.executeMethod(), ProcessoAto.class);
			if(!Util.isNullOuVazio(listaProcessoAtoExistente)) {
				for(ProcessoAto pa : listaProcessoAtoExistente) {
					if (atoAmbientalSelecionado.equals(pa.getAtoAmbiental())){
						if (!Util.isNullOuVazio(pa.getTipologia())){
							for (Iterator<Tipologia> it = listaTipologia.iterator(); it.hasNext(); ) {
								Tipologia tipologia = it.next();
								if(pa.getTipologia().equals(tipologia)){
									it.remove();
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Método que carrega os objetos necessário para os atos de AMF
	 * @throws Exception
	 */
	private void carregarObjetosParaAMF() throws Exception {
		listaObjetivoAtividadeManejo = selecionarAtoAmbientalServiceFacade.listarObjetivoAtividadeManejo();
		listaTipoAtividadeFauna = selecionarAtoAmbientalServiceFacade.listarTipoAtividadeFauna();
	}
	
	public void onChangeTipologia() {
		try {
			tipoFinalidadeUsoAguaSelecionadas=null;
			if(Util.isNull(tipologiaSelecionada)) {
				listaTipoFinalidadeUsoAgua = null;
			}
			else {
				listaTipoFinalidadeUsoAgua = selecionarAtoAmbientalServiceFacade.listarTipoFinalidadeUsoAgua(atoAmbientalSelecionado, tipologiaSelecionada);
			}
			Html.atualizar("frmSelecionarAtoAmbiental");
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método de validação
	 */
	private void validar() {
		
		if(Util.isNull(tipoAtoSelecionado)) {
			throw new SeiaValidacaoRuntimeException("O campo categoria é de preenchimento obrigatório.");
		}
		
		if(Util.isNull(atoAmbientalSelecionado)) {
			throw new SeiaValidacaoRuntimeException("O campo ato ambiental é de preenchimento obrigatório.");
		}
		
		if(isRenderedTipologia() && Util.isNull(tipologiaSelecionada)) {
			throw new SeiaValidacaoRuntimeException("O campo tipologia é de preenchimento obrigatório.");
		}
		
		if(isRenderedJustificativa() && Util.isNullOuVazio(justificativa)) {
			throw new SeiaValidacaoRuntimeException("O campo justificativa para reenquadramento em outorga é de preenchimento obrigatório.");
		}		
		
		if(isRenderedFinalidade() && isRenderedFinalidadeOutorga() && Util.isNullOuVazio(tipoFinalidadeUsoAguaSelecionadas)) {
			throw new SeiaValidacaoRuntimeException("O campo finalidade é de preenchimento obrigatório.");
		}		
		
		if(isRenderedFinalidade() && isRenderedObjetivoManejoFauna() && Util.isNullOuVazio(objetivoAtividadeManejoSelecionados)) {
			throw new SeiaValidacaoRuntimeException("O campo finalidade para reenquadramento em autorização de manejo de fauna é de preenchimento obrigatório.");
		}		
		
		if(isRenderedTipoPlano() && Util.isNullOuVazio(tipoAtividadeFaunaSelecionadas)) {
			throw new SeiaValidacaoRuntimeException("O campo tipo do plano para reenquadramento em autorização de manejo de fauna é de preenchimento obrigatório.");
		}		
		
		if(isRenderedTipologiaEmpreendimento() && Util.isNullOuVazio(listaTipologiaEmpreendimento)) {
			throw new SeiaValidacaoRuntimeException("A seleção das atividades do empreendimento é obrigatória.");
		}		
	}
	
	/**
	 * Método que finaliza a inclusão do ato ambiental
	 */
	public void incluir() {
		try {
			validar();
			prepararObjetoAtoAmbiental();
			selecionarAtoAmbiental();
			executarMetodoExterno();
		}	
		catch (SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método que monta o objeto ato ambiental
	 */
	private void prepararObjetoAtoAmbiental() {
		atoAmbientalSelecionado.setIdeTipoAto(tipoAtoSelecionado);
		atoAmbientalSelecionado.setTipologia(tipologiaSelecionada);
		atoAmbientalSelecionado.setListaTipoFinalidadeUsoAgua(tipoFinalidadeUsoAguaSelecionadas);
		atoAmbientalSelecionado.setDscJustificativa(justificativa);
		atoAmbientalSelecionado.setIndAlteracao(isAlteracao);
		atoAmbientalSelecionado.setListaReenquadramentoTipologiaEmpreendimento(listaTipologiaEmpreendimento);
		atoAmbientalSelecionado.setListaObjetivoAtividadeManejo(objetivoAtividadeManejoSelecionados);
		atoAmbientalSelecionado.setListaTipoAtividadeFauna(tipoAtividadeFaunaSelecionadas);
	}
	
	/**
	 * Método que executa um ou vários métodos de outros controllers
	 */
	private void executarMetodoExterno() throws Exception {
		MetodoUtil metodoAdicionarReenquadramentoProcessoAto = (MetodoUtil) parametros.get("metodoAdicionarReenquadramentoProcessoAto");
		if(!Util.isNull(metodoAdicionarReenquadramentoProcessoAto)) {
			metodoAdicionarReenquadramentoProcessoAto.executeMethod();
		}
		else {
			Html.esconder("dlgSelecionarAtoAmbiental");
		}
	}
	
	/**
	 * Método que executa o método selecionar ato ambiental passado via tag <f:attribute>
	 */
	private void selecionarAtoAmbiental() throws Exception {
		MetodoUtil metodoSelecionarAtoAmbiental = (MetodoUtil) parametros.get("metodoSelecionarAtoAmbiental");
		if(Util.isNull(metodoSelecionarAtoAmbiental)) {
			throw new SeiaRuntimeException("O metodoSelecionarAtoAmbiental não foi passado via tag <f:attribute>.");
		}
		metodoSelecionarAtoAmbiental.executeMethod(atoAmbientalSelecionado);
	}
	
	/**
	 * Método que controla a exibição do campo tipologia
	 */
	public boolean isRenderedTipologia() {
		return !new TipoAto(TipoAtoEnum.LICENCA).equals(tipoAtoSelecionado)
				&& (new TipoAto(TipoAtoEnum.AUTORIZACAO).equals(tipoAtoSelecionado) && isAutorizacaoOutorga())
		; 
				
	}
	
	/**
	 * Método que controla a exibição do campo finalidade para os atos de Outorga
	 */
	public boolean isRenderedFinalidadeOutorga() {
		return !isAMF();
	}
	
	/**
	 * Método que controla a exibição do campo finalidade para os atos de AMF
	 */
	public boolean isRenderedObjetivoManejoFauna() {
		return isAMF();
	}
	
	/**
	 * Método que controla a exibição do bloco referente ao campo finalidade 
	 */
	public boolean isRenderedFinalidade() {
		
		if(isAMF()) {
			return true;
		}
				
		return !Util.isNullOuVazio(listaTipoFinalidadeUsoAgua)
				&& !Util.isNullOuVazio(tipologiaSelecionada)
				&& new TipoAto(TipoAtoEnum.AUTORIZACAO).equals(tipoAtoSelecionado) && isAutorizacaoOutorga();
	}
	
	/**
	 * Método que controla a exibição do campo tipo plano 
	 */
	public boolean isRenderedTipoPlano() {
		return isAMF();
	}
	
	/**
	 * Método que verifica se o ato selecionado é AMF
	 */
	private boolean isAMF() {
		if(Util.isNullOuVazio(atoAmbientalSelecionado)) {
			return false;
		}
		return Arrays.asList(new AtoAmbiental(AtoAmbientalEnum.AMF), new AtoAmbiental(AtoAmbientalEnum.ARTA)).contains(atoAmbientalSelecionado);
	}
	
	/**
	 * Método que controla a exibição do campo justificativa 
	 */
	public boolean isRenderedJustificativa() {
		if(new TipoAto(TipoAtoEnum.AUTORIZACAO).equals(tipoAtoSelecionado)) {
			if(isAutorizacaoOutorga() && !Util.isNull(reenquadramentoProcessoAto)) {
				return reenquadramentoProcessoAto.getIdeProcessoAto().getAtoAmbiental().isDispensaOutorga();
			}
		}
		return false;
	}
	
	/**
	 * Método que controla a exibição do campo tipologia do empreendimento 
	 */
	public boolean isRenderedTipologiaEmpreendimento() {
		return new TipoAto(TipoAtoEnum.LICENCA).equals(tipoAtoSelecionado) ;
				
	}
	
	/**
	 * Método que verifica se o ato selecionado é outorga
	 */
	private boolean isAutorizacaoOutorga() {
		return listaAtoAutorizacaoOutorga.contains(atoAmbientalSelecionado);
	}

	public void novaTipologia() {
		setRoot(null);
		setTipologiaSelecionada(tipologiaSelecionada);
		setSelectedNode(null);
	}


	public void adicionaGrupoTipologia(boolean fecharDialog) {
		selecionarAtoAmbientalServiceFacade.adicionaGrupoTipologia(fecharDialog, root, selectedNode,this.listaTipologiaEmpreendimento);
		novaTipologia();
		Html.atualizar("dataTableTipologias");
	}
	
	public void montarArvoreTipologia(Tipologia tipologia) {
		try {
			setRoot(selecionarAtoAmbientalServiceFacade.montarArvoreTipologia(tipologia));
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	public TipoAto getTipoAtoSelecionado() {
		return tipoAtoSelecionado;
	}

	public void setTipoAtoSelecionado(TipoAto tipoAtoSelecionado) {
		this.tipoAtoSelecionado = tipoAtoSelecionado;
	}

	public AtoAmbiental getAtoAmbientalSelecionado() {
		return atoAmbientalSelecionado;
	}

	public void setAtoAmbientalSelecionado(AtoAmbiental atoAmbientalSelecionado) {
		this.atoAmbientalSelecionado = atoAmbientalSelecionado;
	}

	public Tipologia getTipologiaSelecionada() {
		return tipologiaSelecionada;
	}

	public void setTipologiaSelecionada(Tipologia tipologiaSelecionada) {
		this.tipologiaSelecionada = tipologiaSelecionada;
	}

	public Collection<TipoAto> getListaTipoAto() {
		return listaTipoAto;
	}

	public void setListaTipoAto(Collection<TipoAto> listaTipoAto) {
		this.listaTipoAto = listaTipoAto;
	}

	public Collection<AtoAmbiental> getListaAtoAmbiental() {
		return listaAtoAmbiental;
	}

	public void setListaAtoAmbiental(Collection<AtoAmbiental> listaAtoAmbiental) {
		this.listaAtoAmbiental = listaAtoAmbiental;
	}

	public Collection<Tipologia> getListaTipologia() {
		return listaTipologia;
	}

	public void setListaTipologia(Collection<Tipologia> listaTipologia) {
		this.listaTipologia = listaTipologia;
	}

	public Collection<TipoFinalidadeUsoAgua> getListaTipoFinalidadeUsoAgua() {
		return listaTipoFinalidadeUsoAgua;
	}

	public void setListaTipoFinalidadeUsoAgua(Collection<TipoFinalidadeUsoAgua> listaTipoFinalidadeUsoAgua) {
		this.listaTipoFinalidadeUsoAgua = listaTipoFinalidadeUsoAgua;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public Collection<Tipologia> getListaTipologiaEmpreendimento() {
		return listaTipologiaEmpreendimento;
	}

	public void setListaTipologiaEmpreendimento(Collection<Tipologia> listaTipologiaEmpreendimento) {
		this.listaTipologiaEmpreendimento = listaTipologiaEmpreendimento;
	}
	
	public Collection<Tipologia> getColTipologias() {
		return tipologiaService.filtrarListaTipologias(new Tipologia(new Tipologia()));
	}
	
	public void changeListenerTipologia() {
		try {
			setRoot(null);
			if (!Util.isNullOuVazio(this.tipologia)) {
				setRoot(selecionarAtoAmbientalServiceFacade.montarArvoreTipologia(this.tipologia));
			}

		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public Tipologia getTipologia() {
		if (Util.isNull(tipologia))
			tipologia = new Tipologia();
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public Collection<TipoFinalidadeUsoAgua> getTipoFinalidadeUsoAguaSelecionadas() {
		return tipoFinalidadeUsoAguaSelecionadas;
	}

	public void setTipoFinalidadeUsoAguaSelecionadas(Collection<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaSelecionadas) {
		this.tipoFinalidadeUsoAguaSelecionadas = tipoFinalidadeUsoAguaSelecionadas;
	}

	public Collection<ObjetivoAtividadeManejo> getListaObjetivoAtividadeManejo() {
		return listaObjetivoAtividadeManejo;
	}

	public void setListaObjetivoAtividadeManejo(Collection<ObjetivoAtividadeManejo> listaObjetivoAtividadeManejo) {
		this.listaObjetivoAtividadeManejo = listaObjetivoAtividadeManejo;
	}

	public Collection<ObjetivoAtividadeManejo> getObjetivoAtividadeManejoSelecionados() {
		return objetivoAtividadeManejoSelecionados;
	}

	public void setObjetivoAtividadeManejoSelecionados(Collection<ObjetivoAtividadeManejo> objetivoAtividadeManejoSelecionados) {
		this.objetivoAtividadeManejoSelecionados = objetivoAtividadeManejoSelecionados;
	}

	public Collection<TipoAtividadeFauna> getTipoAtividadeFaunaSelecionadas() {
		return tipoAtividadeFaunaSelecionadas;
	}

	public void setTipoAtividadeFaunaSelecionadas(Collection<TipoAtividadeFauna> tipoAtividadeFaunaSelecionadas) {
		this.tipoAtividadeFaunaSelecionadas = tipoAtividadeFaunaSelecionadas;
	}

	public Collection<TipoAtividadeFauna> getListaTipoAtividadeFauna() {
		return listaTipoAtividadeFauna;
	}

	public void setListaTipoAtividadeFauna(Collection<TipoAtividadeFauna> listaTipoAtividadeFauna) {
		this.listaTipoAtividadeFauna = listaTipoAtividadeFauna;
	}
}
