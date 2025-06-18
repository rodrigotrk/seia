package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIForm;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Equipe;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.IntegranteEquipe;
import br.gov.ba.seia.entity.Ocupacao;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoIntegranteEquipe;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.FormarEquipeServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.ProcessoAtoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("formarEquipeController")
@ViewScoped
public class FormarEquipeController extends FuncionalidadePautaController {

	@EJB
	private FormarEquipeServiceFacade formarEquipeServiceFacade;
	
	@EJB
	private AreaService areaService;
	
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	
	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private ProcessoAtoService processoAtoService;
	
	private UIForm formularioASerLimpo ;
	private PessoaFisica pessoaFisica;
	private IntegranteEquipe integranteEquipe;
	private String observacao;
	private Collection<IntegranteEquipe> listaIntegranteEquipe;
	private DualListModel<ProcessoAto> listaProcessoAto;
	/* FLUXO DA DIRUC */
	@SuppressWarnings("unused")
	private Boolean desabilitaSalvar;
	private Area areaPrincipal;
	private Boolean processoComAcompanhamento;
	private Boolean tecnicoDaAreaSecundariaAcompanhando;
	private Equipe equipe;

	public FormarEquipeController() {
		
	}
	
	@PostConstruct
	private void init() {
		limparTela();
	}
	
	@Override
	public void load(VwConsultaProcesso vwProcesso,Pauta pauta) {
		try{
			limparTela();
			
			super.load(vwProcesso, pauta);
			
			verificarProcessoComAcompanhamento();
			carregarEquipe();
			carregarListaIntegranteEquipe();
			ajustarRegistroReenquadramento();
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isReenquadramento(){
		return !Util.isNull(vwProcesso) && !Util.isNull(vwProcesso.getProcessoReenquadramento());
	}
	
	private void ajustarRegistroReenquadramento() throws Exception{
		List<ProcessoAto> lColProcessoAtoNaoSelecionado = (List<ProcessoAto>) getColProcessoAto();
		Collection<IntegranteEquipe> lista = formarEquipeServiceFacade.listarIntegranteEquipeComAtos(equipe.getIdeEquipe());
		
		for (IntegranteEquipe integranteEquipe : lista) {
			
			for (ProcessoAto processoAto : lColProcessoAtoNaoSelecionado) {
				
				for (String ato : integranteEquipe.getListaAto()) {
					if (!ato.equals(processoAto.getAtoAmbiental().getNomAtoAmbiental())){
						if (integranteEquipe.getListaAto().size() > 1){
							integranteEquipe.getListaAto().remove(ato);
						}
					}
				}
			}
		}
	}

	private RequestContext rc() {
		return RequestContext.getCurrentInstance();
	}
	
	private void addMensagemInformativa(String string) {
		JsfUtil.addSuccessMessage(string);
	}

	private void verificarProcessoComAcompanhamento() throws Exception {
		
		processoComAcompanhamento = false;
		
		Collection<ControleTramitacao> lista = controleTramitacaoService.listarControleTramitacaoPorIdeProcesso(vwProcesso.getIdeProcesso());
		for(ControleTramitacao ct : lista) {
			if(ct.getIndAreaSecundaria() != null && ct.getIndAreaSecundaria()) {
				areaPrincipal = ct.getIdeArea();
				processoComAcompanhamento=true;
				break;
			}
		}
	}
	
	private void limparTela() {
		observacao = "";
		pessoaFisica = null;
		listaProcessoAto = null;
		integranteEquipe = new IntegranteEquipe();
		processoComAcompanhamento = false;
		
		/* FLUXO DA DIRUC */
		tecnicoDaAreaSecundariaAcompanhando = null;
		
	}
	
	private void carregarListaIntegranteEquipe() throws Exception {
		Collection<IntegranteEquipe> lista = formarEquipeServiceFacade.listarIntegranteEquipeComAtos(equipe.getIdeEquipe());
		if(processoComAcompanhamento && new Area(AreaEnum.COGES.getId()).equals(area)) {
			if(Util.isNullOuVazio(lista) == false) {
				listaIntegranteEquipe = new ArrayList<IntegranteEquipe>();
				for(IntegranteEquipe ie : lista) {
					if(area.equals(ie.getIdeArea())) {
						listaIntegranteEquipe.add(ie);
					}
				}
			}
		}
		else {
			listaIntegranteEquipe = lista;
		}
	}

	private void carregarEquipe() throws Exception {
		Processo processo = new Processo(vwProcesso.getIdeProcesso());
		if(processoComAcompanhamento) {
			equipe = formarEquipeServiceFacade.carregarOuCriarNovaEquipe(processo, areaPrincipal);
		}
		else {
			equipe = formarEquipeServiceFacade.carregarOuCriarNovaEquipe(processo, area);
		}
	}

	public void salvarIntegranteEquipe() {
		try {
			if (Util.isNullOuVazio(integranteEquipe.getIdePessoaFisica())) {
				JsfUtil.addWarnMessage("Nenhum técnico foi selecionado.");
			}
			
			else if (getListaProcessoAto().getTarget().isEmpty()) {
				JsfUtil.addWarnMessage("Nenhum ato foi selecionado.");
			} else {
				
				equipeProcessoAdicionarAtos();
				integranteEquipe.setIdeEquipe(equipe);
				
				if(processoComAcompanhamento) {
					integranteEquipe.setIdeArea(area);
				}
				
				formarEquipeServiceFacade.salvarIntegranteEquipe(integranteEquipe);
				verificarTecnicoAcompanhando();
				this.addMensagemInformativa("Inclusão efetuada com sucesso.");
				rc().addPartialUpdateTarget("formEquipeProcesso:pickListAtos");
			}
			
			integranteEquipe = new IntegranteEquipe();
			this.setPessoaFisica(null);
			this.setListaProcessoAto(null);
			carregarListaIntegranteEquipe();
		}
		catch (EJBException e) {
			if (e.getCausedByException().toString().contains("uq_equipe_processo")) {
				JsfUtil.addErrorMessage("Esta pessoa já fez parte de uma equipe deste processo, nesta mesma área.");
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	

	private void equipeProcessoAdicionarAtos() {
		ArrayList<ProcessoAtoIntegranteEquipe> listaProcessoAtoIntegranteEquipe = new ArrayList<ProcessoAtoIntegranteEquipe>();
		for (ProcessoAto processoAtoSelecionado : getListaProcessoAto().getTarget()) {
			ProcessoAtoIntegranteEquipe processoAtoIntegranteEquipe = new ProcessoAtoIntegranteEquipe();
			processoAtoIntegranteEquipe.setIdeProcessoAto(processoAtoSelecionado);
			listaProcessoAtoIntegranteEquipe.add(processoAtoIntegranteEquipe);
		}
		integranteEquipe.setProcessoAtoIntegranteEquipeCollection(listaProcessoAtoIntegranteEquipe);
	}
	
	public void excluirIntegranteEquipe() {
		try {
			formarEquipeServiceFacade.excluirIntegranteEquipe(integranteEquipe);
			carregarListaIntegranteEquipe();
			this.addMensagemInformativa("Exclusão efetuada com sucesso.");
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isDisabledBotaoDistribuir() {
		
		if(Util.isNullOuVazio(vwProcesso)) {
			return true;
		}
		
		try{
			Processo processoEmAnalise = new Processo(vwProcesso.getIdeProcesso());
			ControleTramitacao ultimaTramitacao = controleTramitacaoService.buscarUltimoPorProcesso(processoEmAnalise);
			if(StatusFluxoEnum.ANALISE_TECNICA.getStatus()==ultimaTramitacao.getIdeStatusFluxo().getIdeStatusFluxo()){
				return true;
			}
			
			 //FLUXO DA DIRUC 
			if(!Util.isNull(processoComAcompanhamento) && processoComAcompanhamento && new Area(AreaEnum.COGES.getId()).equals(area)) {
				return true;
			}
		
			if(Util.isNullOuVazio(getColProcessoAto())){
				return true;
			}
			
			if(Util.isNullOuVazio(listaIntegranteEquipe)){
				return true;
			}
			
			Set<AtoAmbiental> atosAchadosHash = new HashSet<AtoAmbiental>(); 
			Boolean temLider = Boolean.FALSE;
			List<AtoAmbiental> listaAtos = new ArrayList<AtoAmbiental>();
			for(ProcessoAto processoAto: getColProcessoAto()){
				listaAtos.add(processoAto.getAtoAmbiental());
				for(IntegranteEquipe ie: listaIntegranteEquipe){
					for(ProcessoAtoIntegranteEquipe paie : ie.getProcessoAtoIntegranteEquipeCollection()){
						if(ie.getIndLiderEquipe()){
							temLider = true;
						}
						if(processoAto.equals(paie.getIdeProcessoAto()) ){
							atosAchadosHash.add(processoAto.getAtoAmbiental());
						}
					}
				}
			}
			
			return (!temLider && !atosAchadosHash.containsAll(listaAtos));
		
		} 
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
		
	public void distribuir() {
		try {			
			Boolean msgAtoExcluido = false;			
			Processo processo = new Processo(vwProcesso.getIdeProcesso());
			/*Area areaCoordenador = buscarAreaDaEquipe();*/
			first:
				for(IntegranteEquipe ie :listaIntegranteEquipe){
					for(String nomeAto : ie.getListaAto()){
						AtoAmbiental ato = atoAmbientalService.getAtoAmbientalParams(nomeAto);
						if(!Util.isNullOuVazio(ato)) {
							ProcessoAto pa = processoAtoService.getProcessoAtoByProcessoByAtoAmbiental(processo,ato,null);
							if(pa.getIndExcluido()){
								msgAtoExcluido = true;
								break first;
							}
						}
					}	
				}

			if(!msgAtoExcluido){
				formarEquipeServiceFacade.distribuirProcesso(vwProcesso.getIdeProcesso(), listaIntegranteEquipe, area/*Coordenador*/,observacao);
				rc().execute("atualizarPauta();");
				this.addMensagemInformativa("Equipe distribuída com sucesso.");
			}else{
				MensagemUtil.alerta("O ato selecionado está excluído ou reenquadrado, verifique a lista de atos da equipe do processo.");
			}
		}
		catch(SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void changeListenerArea(ValueChangeEvent pValueChangeEvent) {

		if (pValueChangeEvent.getNewValue() instanceof Area) {

			getPessoaFisica().setFuncionario(new Funcionario());
			getPessoaFisica().getFuncionario().setIdeArea((Area)pValueChangeEvent.getNewValue());
		}
	}

	public void changeListenerOcupacao(ValueChangeEvent pValueChangeEvent) {

		if (pValueChangeEvent.getNewValue() instanceof Ocupacao) {

			getPessoaFisica().setIdeOcupacao((Ocupacao)pValueChangeEvent.getNewValue());
		}
	}

    public Collection<ProcessoAto> getColProcessoAto() {
    	if(Util.isNull(vwProcesso)) {
    		return new ArrayList<ProcessoAto>();
    	}
    	return formarEquipeServiceFacade.filtrarListaProcessoAto(new ProcessoAto(new Processo(vwProcesso.getIdeProcesso())));
    }
    
	public Collection<Funcionario> getColFuncionarios() {

		if (!Util.isNullOuVazio(getPessoaFisica().getFuncionario()) && !Util.isNullOuVazio(getPessoaFisica().getFuncionario().getIdeArea())) { 
			try {
				return formarEquipeServiceFacade.listarFuncionarios(getPessoaFisica());
			}
			catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
		
		return null;
    }
	
	public Collection<Ocupacao> getColOcupacoes() {

    	return formarEquipeServiceFacade.filtrarListaOcupacoes(null);
    }
	
	public Collection<Area> getColAreas() {
		Area areaCOGES = new Area(AreaEnum.COGES.getId());
		if(processoComAcompanhamento && areaCOGES.equals(area)) {
			return formarEquipeServiceFacade.filtrarListaAreas(areaCOGES);
		} else {
			return formarEquipeServiceFacade.filtrarListaAreas(new Area());
		}
    }

	public DualListModel<ProcessoAto> getListaProcessoAto() {

		if (Util.isNullOuVazio(listaProcessoAto)) {
			List<ProcessoAto> lColProcessoAtoNaoSelecionado = (List<ProcessoAto>) getColProcessoAto();
			listaProcessoAto = new DualListModel<ProcessoAto>(lColProcessoAtoNaoSelecionado, new ArrayList<ProcessoAto>());
		}
		return listaProcessoAto;
	}
	
	public void setListaProcessoAto(DualListModel<ProcessoAto> listaProcessoAto) {
		this.listaProcessoAto = listaProcessoAto;
	}
	
	
	/*private Area retornaArea(){
		Area areaDoGestor = null;

		try{
			areaDoGestor = area;
			
			if(areaDoGestor==null){
				throw new Exception("Área nula");
			}
			
			return areaDoGestor;
			
		}catch(Exception e1) {
			try{
				Integer ideFuncionarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
				
				areaDoGestor = areaService.obterAreaFuncionarioCoordenadorPorIdeFuncionario(ideFuncionarioLogado);
				
				if(areaDoGestor==null){
					throw new Exception("Área nula");
				}
				
				return areaDoGestor;
			} catch(Exception e2) {
				Area area = new Area();
				area.setIdeArea(null);
				return area;
			}
		}		
	}*/

	public Collection<IntegranteEquipe> getListaIntegranteEquipe() {
		return listaIntegranteEquipe;
	}
	
	public boolean tratarExclusaoTecnico(IntegranteEquipe integranteEquipe){
		
		//Se a area for COGES, sempre permite exclusao
		if(!Util.isNullOuVazio(area) && area.getIdeArea().equals(AreaEnum.COGES.getId())) {
			return false;
		}
		
		//Se a area do responsavel pelo tecnico for igual a area do coordenador logado, permite a exclusao
		if(!Util.isNull(integranteEquipe.getIdeEquipe().getIdeArea())
			&& integranteEquipe.getIdeEquipe().getIdeArea().equals(area)) {
			return false;
		} 
		else {
			return true;
		}
	}

	public PessoaFisica getPessoaFisica() {
		if (Util.isNull(pessoaFisica)) {
			Usuario usuario = new Usuario();
			usuario.setIndTipoUsuario(true);
			pessoaFisica = new PessoaFisica();
			pessoaFisica.setFuncionario(new Funcionario());
			pessoaFisica.setUsuario(usuario);
		}
		return pessoaFisica;
	}
	
	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}

	public boolean isDisabledBotaoSalvar() {
		
		if(Util.isNullOuVazio(vwProcesso)) {
			return false;
		}
		else {
			try{
				Processo processoEmAnalise = new Processo(vwProcesso.getIdeProcesso());
				ControleTramitacao ultimaTramitacao = controleTramitacaoService.buscarUltimoPorProcesso(processoEmAnalise);
				if(StatusFluxoEnum.ANALISE_TECNICA.getStatus()==ultimaTramitacao.getIdeStatusFluxo().getIdeStatusFluxo()) {
					return true;
				}
			}
			catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}
			return !(verificarExistenciaDeAtoAmbiental() || desabilitaDistribuicaoEquipeProcesso());
		}
	}

	/**
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return
	 */
	private boolean desabilitaDistribuicaoEquipeProcesso() {

		return !Util.isNull(vwProcesso.getIdeProcesso()) && formarEquipeServiceFacade.desabilitaDistribuicaoEquipeProcesso(new Processo(vwProcesso.getIdeProcesso()));
	}

	/**
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return
	 */
	private boolean verificarExistenciaDeAtoAmbiental() {

		return (!Util.isNullOuVazio(getListaProcessoAto()) && (getListaProcessoAto().getSource().size() != 0 || getListaProcessoAto().getTarget().size() != 0)); 
				
	}

	public void setDesabilitaSalvar(Boolean desabilitaSalvar) {
		this.desabilitaSalvar = desabilitaSalvar;
	}
	
	public boolean isStatusFluxoAnaliseTecnica(){
		
		try{
			Processo processoEmAnalise = new Processo(vwProcesso.getIdeProcesso());
			ControleTramitacao ultimaTramitacao = controleTramitacaoService.buscarUltimoPorProcesso(processoEmAnalise);
			return (StatusFluxoEnum.ANALISE_TECNICA.getStatus()==ultimaTramitacao.getIdeStatusFluxo().getIdeStatusFluxo());
		}
		catch(Exception e){
			return true;
		}
		
		
	}
	
	public boolean isDisabledBtnRemoverTecnico(IntegranteEquipe integranteEquipe) {
		return isStatusFluxoAnaliseTecnica() || tratarExclusaoTecnico(integranteEquipe);
	}
	
	
	/* FLUXO DA DIRUC */
	/*private void armazenarVariaveisParaProcessoComAcompanhamento(){
		try {
			verificarDirucComoAreaSecundariaAndRecuperararea(vwProcesso);
			verificarTecnicoAcompanhando();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}*/
	
	/*private void verificarDirucComoAreaSecundariaAndRecuperararea(VwConsultaProcesso vwConsultaProcesso){
		try {
			Collection<ControleTramitacao> controleTramitacao = controleTramitacaoService.listarControleTramitacaoPorIdeProcesso(vwConsultaProcesso.getIdeProcesso());
			processoComAcompanhamento = isDirucAreaSecundaria(controleTramitacao);
			area = recuperaarea(controleTramitacao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	}*/
	
	public boolean isDirucAreaSecundaria(Collection<ControleTramitacao> col) throws Exception{
		
		if(!Util.isNullOuVazio(col) && col.size() > 1){
			for(ControleTramitacao tramitacao : col){
				if(!Util.isNull(tramitacao.getIndAreaSecundaria()) && tramitacao.getIndAreaSecundaria()){
					return true;
				}
			}	
		}
		
		/*String[] atos = vwProcesso.getIdesAtosAmbientais().split(",");
		
		if(!Util.isNullOuVazio(atos) && atos.length > 1) {
			for(String string : atos){
				if(string.equals("47")){
					return true;
				}
			}
		}*/
		
		return false;
	}
	
	private void verificarTecnicoAcompanhando() throws Exception{
		tecnicoDaAreaSecundariaAcompanhando = isTecnicoAreaSecundariaNoProcesso(vwProcesso);
	}
	
	public boolean isTecnicoAreaSecundariaNoProcesso(VwConsultaProcesso vwConsultaProcesso) throws Exception{
		
		Collection<IntegranteEquipe> listaIntegranteEquipe = null;
		if(!Util.isNull(equipe)) {
			listaIntegranteEquipe = formarEquipeServiceFacade.listarIntegranteEquipe(equipe.getIdeEquipe());
		}
		
		if(!Util.isNullOuVazio(listaIntegranteEquipe) && processoComAcompanhamento){
			for(IntegranteEquipe integranteEquipe : listaIntegranteEquipe){
				if(integranteEquipe.getIdePessoaFisica().getIdeArea().equals(new Area(AreaEnum.COGES.getId()))){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Area recuperaarea(Collection<ControleTramitacao> col) throws Exception{
		if(!Util.isNullOuVazio(col) && processoComAcompanhamento){
			for(ControleTramitacao tramitacao : col){
				if(!tramitacao.getIndAreaSecundaria()){
					return tramitacao.getIdeArea();
				}
			}
		}
		return null;
	}
	
	/*private boolean validarAcompanhamentoPelaAreaSecundaria() throws Exception{
		
		if(!Util.isNull(processoComAcompanhamento) && !Util.isNull(tecnicoDaAreaSecundariaAcompanhando)){
			return processoComAcompanhamento && tecnicoDaAreaSecundariaAcompanhando;
		}
		
		return false;
	}*/
	
	private boolean isCoordenadorDaarea(){
		
		if(!Util.isNull(processoComAcompanhamento) && !Util.isNullOuVazio(area)){
			return processoComAcompanhamento 
					&& ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getFuncionario().getIdePessoaFisica().equals(
							area.getIdePessoaFisica().getIdePessoaFisica());
		}
		
		return false;
	}
	
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Boolean getProcessoComAcompanhamento() {
		return processoComAcompanhamento;
	}
	
	public void setProcessoComAcompanhamento(Boolean processoComAcompanhamento) {
		this.processoComAcompanhamento = processoComAcompanhamento;
	}
	
	public Boolean getTecnicoDaAreaSecundariaAcompanhando() {
		return tecnicoDaAreaSecundariaAcompanhando;
	}
	
	public void setTecnicoDaAreaSecundariaAcompanhando(Boolean tecnicoDaAreaSecundariaAcompanhando) {
		this.tecnicoDaAreaSecundariaAcompanhando = tecnicoDaAreaSecundariaAcompanhando;
	}
	
	public IntegranteEquipe getIntegranteEquipe() {
		return integranteEquipe;
	}

	public void setIntegranteEquipe(IntegranteEquipe integranteEquipe) {
		this.integranteEquipe = integranteEquipe;
	}
	
}