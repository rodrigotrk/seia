package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Level;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.entity.DestinoResiduo;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MetodoRecuperacaoIntervencao;
import br.gov.ba.seia.entity.OutorgaMineracao;
import br.gov.ba.seia.entity.ProcessoDnpm;
import br.gov.ba.seia.entity.ProcessoTramite;
import br.gov.ba.seia.entity.SubstanciaMineral;
import br.gov.ba.seia.entity.SubstanciaMineralTipologia;
import br.gov.ba.seia.entity.TipoOrigemEnergia;
import br.gov.ba.seia.entity.TipoResiduoGerado;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.MetodoRecuperacaoIntervencaoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.FceMineracaoFacade;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

/**
 * Abstração do <b>FCE - Mineração</b> (Licenciamento / Autorização)
 *
 * @author eduardo.fernandes
 * @since 09/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
public abstract class BaseFceMineracaoController extends FceController implements FceNavegacaoInterface {

	protected int activeTab;

	protected List<SubstanciaMineral> listaSubstanciaMineral;

	protected ProcessoDnpm processoDnpm;

	protected List<OutorgaMineracao> listaOutorgaMineracao;

	protected List<TipoOrigemEnergia> listaTipoOrigemEnergia;
	protected List<TipoOrigemEnergia> listaTipoOrigemEnergiaSelected;

	protected List<TipoResiduoGerado> listaTipoResiduoGerado;
	protected List<TipoResiduoGerado> listaTipoResiduoGeradoSelected;

	protected List<DestinoResiduo> listaDestinoResiduo;
	protected List<DestinoResiduo> listaDestinoResiduoSelected;

	protected Empreendimento empreendimento;

	protected List<LocalizacaoGeografica> listaLocalizacaoGeograficaParaExclusao;
	
	protected List<MetodoRecuperacaoIntervencao> listaMetodoRecuperacaoIntervencao;
	protected List<MetodoRecuperacaoIntervencao> listaMetodoRecuperacaoIntervencaoSelected;

	private TreeNode root;
	private TreeNode selectedNode;

	protected abstract void carregarAbaDadosGerais();

	protected abstract void carregarAbaAspectosAmbientais();

	protected abstract void incluirProcessoDnpm();

	public abstract void excluirSubstanciaMineral();

	protected abstract List<ProcessoDnpm> getListaProcessoDnpm();

	protected abstract FceMineracaoFacade getMineracaoFacade();
	
	/**
	 * Método que vai fechar o dialog de inclusão do {@link ProcessoDnpm}
	 * @author eduardo.fernandes
	 * @since 11/08/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7879">#7879</a>
	 */
	protected abstract void fecharDialogProcessoDNPM();

	/**
	 * RN 00162 - Supressão de vegetação nativa ​ O sistema informa se tem
	 * necessidade de supressão de vegetação nativa na atual fase do
	 * empreendimento, a partir da resposta cadastrada na etapa 1 do
	 * requerimento: Será informado "Sim" se o usuário informou que existe
	 * processo de ASV no INEMA (independente se o ato foi deferido ou
	 * indeferido) ou se o requerimento for enquadrado com AA ou Licença e ASV.
	 * Caso contrário será informado "Não".
	 *
	 * @author eduardo.fernandes
	 * @since 22/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 */
	protected abstract boolean necessidadeDeSupressao();

	public abstract void salvarSubstanciaMineralFechar();
	
	public abstract boolean salvarSubstanciaMineralContinuar();

	@Override
	public void init() {
		activeTab = 0;
		carregarAba();
		carregarEmpreendimento();
	}

	@Override
	public void limpar() {
		activeTab = 0;
		limparListaSubstanciaMineral();
		processoDnpm = null;
		listaOutorgaMineracao = null;
		empreendimento = null;
		listaLocalizacaoGeograficaParaExclusao = null;
	}

	protected void carregarListaTipoOrigemEnergia() throws Exception {
		listaTipoOrigemEnergia = getMineracaoFacade().listarTipoOrigemEnergia();
	}


	public void limparListaOutorgaMineracao() {
		listaOutorgaMineracao = null;
		
	}
	
	protected void limparListaTipoOrigemEnergia() {
		listaTipoOrigemEnergia = null;
		listaTipoOrigemEnergiaSelected = null;
	}

	protected void carregarListaTipoResiduoGerado() throws Exception {
		listaTipoResiduoGerado = getMineracaoFacade().listarTipoResiduoGerado();
	}

	protected void limparListaTipoResiduoGerado() {
		listaTipoResiduoGerado = null;
		listaTipoResiduoGeradoSelected = null;
	}

	protected void carregarListaDestinoResiduo() throws Exception {
		listaDestinoResiduo = getMineracaoFacade().listarDestinoResiduo();
	}

	protected void limparListaDestinoResiduo() {
		listaDestinoResiduo = null;
		listaDestinoResiduoSelected = null;
	}

	@SuppressWarnings("unchecked")
	public void changeDestinuoResiduo(ValueChangeEvent changeEvent) {
		List<DestinoResiduo> drOld = ((List<DestinoResiduo>) changeEvent.getOldValue());
		List<DestinoResiduo> drNew = ((List<DestinoResiduo>) changeEvent.getNewValue());
		if(!Util.isNullOuVazio(drNew)){
			DestinoResiduo outros = new DestinoResiduo(5, "Outros");
			if(drNew.contains(outros)){
				if(Util.isNullOuVazio(drOld) || !drOld.contains(outros)){
					super.exibirInformacao001();
				}
			}
		}
	}

	protected boolean isExisteDestinoResiduoOutros() {
		return listaDestinoResiduoSelected.contains(new DestinoResiduo(5, "Outros"));
	}

	@SuppressWarnings("unchecked")
	public void changeOrigemEnergia(ValueChangeEvent changeEvent) {
		List<TipoOrigemEnergia> oeOld = ((List<TipoOrigemEnergia>) changeEvent.getOldValue());
		List<TipoOrigemEnergia> oeNew = ((List<TipoOrigemEnergia>) changeEvent.getNewValue());
		if(!Util.isNullOuVazio(oeNew)){
			TipoOrigemEnergia outros = new TipoOrigemEnergia(7, "Outros");
			if(oeNew.contains(outros)){
				if(Util.isNullOuVazio(oeOld) || !oeOld.contains(outros)){
					super.exibirInformacao001();
				}
			}
		}
	}

	protected boolean isExisteOrigemEnergiaOutros() {
		return listaTipoOrigemEnergiaSelected.contains(new TipoOrigemEnergia(7, "Outros"));
	}

	@SuppressWarnings("unchecked")
	public void changeSubstanciaMineral(ValueChangeEvent event) {
		List<SubstanciaMineral> drOld = ((List<SubstanciaMineral>) event.getOldValue());
		List<SubstanciaMineral> drNew = ((List<SubstanciaMineral>) event.getNewValue());
		if(!Util.isNullOuVazio(drNew)){
			SubstanciaMineral outros = null;
			for(SubstanciaMineral substanciaMineral : drNew){
				if(substanciaMineral.isOutros()){
					outros = substanciaMineral;
					break;
				}
			}
			if(!Util.isNull(outros) && !drOld.contains(outros)){
				super.exibirInformacao001();
			}
		}
	}

	protected void limparListaSubstanciaMineral() {
		listaSubstanciaMineral = null;
	}

	protected void montarArvoreTipologiaMineracao(Collection<Tipologia> listaTipologia) throws Exception{
		setRoot(getMineracaoFacade().montarArvoreTipologiasMineracao(new Tipologia(TipologiaEnum.MINERACAO), listaTipologia));
	}

	/**
	 * RN 00162 - Supressão de vegetação nativa ​ O sistema informa se tem
	 * necessidade de supressão de vegetação nativa na atual fase do
	 * empreendimento, a partir da resposta cadastrada na etapa 1 do
	 * requerimento: Será informado "Sim" se o usuário informou que existe
	 * processo de ASV no INEMA (independente se o ato foi deferido ou
	 * indeferido)
	 *
	 * @author eduardo.fernandes
	 * @return
	 * @since 20/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @throws Exception
	 */
	protected boolean existeProcessoAsv() {
		try{
			return getMineracaoFacade().isExisteProcessoAsvEmTramite(super.requerimento);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 *  ADICIONAR COMENTÁRIO
	 *
	 * @author eduardo.fernandes
	 * @since 20/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @throws Exception
	 */
	public void carregarEmpreendimento() {
		try{
			empreendimento = getMineracaoFacade().buscarEmpreendimentoBy(super.requerimento);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("")); //  ADICIONAR MSG
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	public String visualizarLocalizacao(LocalizacaoGeografica locGeo) {
		if(isLocalizacaoGeograficaSalva(locGeo)){
			return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(locGeo);
		}
		return "";
	}

	/**
	 *  ADICIONAR COMENTÁRIO
	 *
	 * @author eduardo.fernandes
	 * @since 20/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param locGeo
	 * @return
	 */
	protected boolean isLocalizacaoGeograficaSalva(LocalizacaoGeografica locGeo) {
		try{
			return !Util.isNullOuVazio(locGeo) && !Util.isNull(getMineracaoFacade().retornarGeometriaShapeByLocalizacaoGeografica(locGeo));
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("")); /** ADICIONAR MSG DE ERRO*/
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	public void inserirProcessoDnpm() {
		try{
			if(isProcessoDnpmValido()){
				getListaProcessoDnpm().add(processoDnpm);
				incluirProcessoDnpm();
				super.exibirMensagem001();
				fecharDialogProcessoDNPM();
			}
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("")); /** ADICIONAR MSG*/
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void excluirProcessoDnpm() throws Exception {
		getListaProcessoDnpm().remove(processoDnpm);
		getMineracaoFacade().excluirProcessoDnpm(processoDnpm);
	}

	protected boolean isProcessoDnpmValido() {
		boolean valido = true;
		if(Util.isNullOuVazio(processoDnpm.getNumProcessoDnpm())){
			JsfUtil.addErrorMessage("O " + Util.getString("fce_lic_min_numero_processo") + " " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		return valido;
	}

	public boolean isProcessoDnpmAdicionado() {
		return !Util.isNullOuVazio(getListaProcessoDnpm());
	}

	/**
	 *  ADICIONAR COMENTÁRIO
	 *
	 * @author eduardo.fernandes
	 * @since 21/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param ideLocalizacaoGeografica
	 */
	protected void adicionarLocalizacaoGeograficaParaExclusao(LocalizacaoGeografica ideLocalizacaoGeografica) {
		if(Util.isNull(listaLocalizacaoGeograficaParaExclusao)){
			listaLocalizacaoGeograficaParaExclusao = new ArrayList<LocalizacaoGeografica>();
		}
		if(!Util.isNullOuVazio(ideLocalizacaoGeografica)){
			listaLocalizacaoGeograficaParaExclusao.add(ideLocalizacaoGeografica);
		}
	}

	protected void excluirLocalizacaoGeografica() {
		try{
			getMineracaoFacade().excluirListaLocalizacaoGeografica(listaLocalizacaoGeograficaParaExclusao);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("")); /** ADICIONAR MSG*/
			// DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	protected boolean validarAbaAspectosAmbientais() {
		boolean valido = true;
		if(Util.isNullOuVazio(listaTipoResiduoGeradoSelected)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um " + Util.getString("fce_lic_min_residuos_gerados") + ".");
			valido = false;
		}
		if(Util.isNullOuVazio(listaDestinoResiduoSelected)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um " + Util.getString("fce_lic_min_destino_residuos") + ".");
			valido = false;
		}
		return valido;
	}

	@Override
	public void voltarAba() {
		activeTab--;
	}

	protected void montarListaOutorgaMineracao(Object obj) throws Exception {
		if(super.isFceSalvo()){
			carregarListaOutorgaMineracao(obj);
		}
		else{
			List<ProcessoTramite> listaProcessosOutorga = getMineracaoFacade().listarProcessosOutorga(super.requerimento);
			if (!Util.isNullOuVazio(listaProcessosOutorga)) {
				listaOutorgaMineracao = new ArrayList<OutorgaMineracao>();
				for (ProcessoTramite processoTramite : listaProcessosOutorga) {
					listaOutorgaMineracao.add(new OutorgaMineracao(obj, processoTramite.getNumProcessoTramite()));
				}
			}
		}
	}

	public boolean isExisteProcessoOutorga() {
		return !Util.isNullOuVazio(listaOutorgaMineracao);
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 *
	 * @author eduardo.fernandes
	 * @since 15/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @throws Exception
	 */
	protected void carregarListaOutorgaMineracao(Object obj) throws Exception {
		listaOutorgaMineracao = getMineracaoFacade().listarOutorgaMineracaoBy(obj);
	}

	protected boolean isOutorgaMineracaoValida(OutorgaMineracao outorgaMineracao) {
		if(Util.isNullOuVazio(outorgaMineracao.getNumPortariaOutorga())){
			JsfUtil.addErrorMessage("O " + Util.getString("fce_lic_min_numero_portaria") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			return false;
		}
		return true;
	}

	public void confirmarOutorgaMineracao(ActionEvent event) {
		OutorgaMineracao outorgaMineracao = (OutorgaMineracao) event.getComponent().getAttributes().get("outorgaMineracao");
		if(isOutorgaMineracaoValida(outorgaMineracao)){
			outorgaMineracao.setConfirmado(true);
			if(!outorgaMineracao.isEdicao()){
				super.exibirMensagem001();
			}
			else{
				super.exibirMensagem002();
			}
		}
	}

	public void editarOutorgaMineracao(ActionEvent event) {
		OutorgaMineracao outorgaMineracao = (OutorgaMineracao) event.getComponent().getAttributes().get("outorgaMineracao");
		outorgaMineracao.setConfirmado(false);
		outorgaMineracao.setEdicao();
	}
	
	protected void carregarListaMetodoRecuperacaoIntervencao(boolean removerNaoSeAplica) throws Exception{
		listaMetodoRecuperacaoIntervencao = getMineracaoFacade().listarMetodoRecuperacaoIntervencao();
		if(removerNaoSeAplica){
			listaMetodoRecuperacaoIntervencao.remove(new MetodoRecuperacaoIntervencao(MetodoRecuperacaoIntervencaoEnum.NAO_SE_APLICA.getId()));
		}
	}

	/**
	 * Getters & Setters
	 */
	
	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public List<OutorgaMineracao> getListaOutorgaMineracao() {
		return listaOutorgaMineracao;
	}

	public List<TipoOrigemEnergia> getListaTipoOrigemEnergia() {
		return listaTipoOrigemEnergia;
	}

	public List<TipoResiduoGerado> getListaTipoResiduoGerado() {
		return listaTipoResiduoGerado;
	}

	public List<DestinoResiduo> getListaDestinoResiduo() {
		return listaDestinoResiduo;
	}

	public List<TipoOrigemEnergia> getListaTipoOrigemEnergiaSelected() {
		return listaTipoOrigemEnergiaSelected;
	}

	public void setListaTipoOrigemEnergiaSelected(List<TipoOrigemEnergia> listaOrigemEnergiaSelected) {
		this.listaTipoOrigemEnergiaSelected = listaOrigemEnergiaSelected;
	}

	public List<TipoResiduoGerado> getListaTipoResiduoGeradoSelected() {
		return listaTipoResiduoGeradoSelected;
	}

	public void setListaTipoResiduoGeradoSelected(List<TipoResiduoGerado> listaResiduoGeradoSelected) {
		this.listaTipoResiduoGeradoSelected = listaResiduoGeradoSelected;
	}

	public List<DestinoResiduo> getListaDestinoResiduoSelected() {
		return listaDestinoResiduoSelected;
	}

	public void setListaDestinoResiduoSelected(List<DestinoResiduo> listaDestinoResiduoSelected) {
		this.listaDestinoResiduoSelected = listaDestinoResiduoSelected;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public ProcessoDnpm getProcessoDnpm() {
		return processoDnpm;
	}

	public void setProcessoDnpm(ProcessoDnpm processoDnpm) {
		this.processoDnpm = processoDnpm;
	}

	public List<MetodoRecuperacaoIntervencao> getListaMetodoRecuperacaoIntervencao() {
		return listaMetodoRecuperacaoIntervencao;
	}
	
	public List<MetodoRecuperacaoIntervencao> getListaMetodoRecuperacaoIntervencaoSelected() {
		return listaMetodoRecuperacaoIntervencaoSelected;
	}

	public void setListaMetodoRecuperacaoIntervencaoSelected(List<MetodoRecuperacaoIntervencao> listaMetodoRecuperacaoIntervencaoSelected) {
		this.listaMetodoRecuperacaoIntervencaoSelected = listaMetodoRecuperacaoIntervencaoSelected;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return this.selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	public SubstanciaMineralTipologia getSubstanciaMineralSelecionada() {
		return (SubstanciaMineralTipologia) getSelectedNode().getData();
	}
	
	protected boolean isSubstanciaMineral(){
		if(Util.isNull(getSelectedNode()) || Util.isNull(getSelectedNode().getData())){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma " + Util.getString("fce_lic_min_substancia_mineral") + ".");
			return false;
		}
		else{
			if(!getSelectedNode().getType().equals("substancia")){
				JsfUtil.addErrorMessage("A opção escolhida não é uma Substância Mineral.");
				return false;
			}
		}
		return true;
	}
	
}
