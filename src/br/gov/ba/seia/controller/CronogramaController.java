package br.gov.ba.seia.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleCronograma;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Cronograma;
import br.gov.ba.seia.entity.EquipeProcesso;
import br.gov.ba.seia.entity.FuncionalidadeAcaoPessoaFisica;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.ItemCronograma;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AcaoControlePermissaoAcessoPautaEnum;
import br.gov.ba.seia.enumerator.ItemCronogramaEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.CronogramaService;
import br.gov.ba.seia.service.FuncionalidadeAcaoPessoaFisicaService;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.SecurityPautaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("cronogramaController")
@ViewScoped

public class CronogramaController extends SeiaControllerAb {

	@EJB
	private CronogramaService cronogramaService;
	@EJB
	private AreaService areaService;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private FuncionarioService funcionarioService;
	@EJB
	private SecurityPautaService securityPautaService;
	
	private UIForm formularioASerLimpo;
	private VwConsultaProcesso vwProcesso;
	private Cronograma cronograma;
	private Integer tela; 
	private ControleCronograma controleCronogramaReuniao;
	private ControleCronograma controleCronogramaConclusaoInspecao;
	private ControleCronograma controleCronogramaConclusaoParecer;
	
	//VARIAVEL PARA INSERÇÃO DE ITEM CRONOGRAMA
	private ItemCronograma itemCronograma;
	private ControleCronograma atividadeCronogramaAdd;
	private ControleCronograma delAtividadeCronograma;
	private ControleCronograma atividCronogData;
	private String maxDataPrevista;
	private Date minDataPrevista;
	private List<ControleCronograma> modelAtividControleCronograma;
	private List<ItemCronograma> listaTipoAtividade = new ArrayList<ItemCronograma>();
	private Usuario usuario;
	private String msgDataMaiorQConclusao = "";
	private Area areaLogada;
	private boolean renderedCronograma; 
	
	@EJB
	private FuncionalidadeAcaoPessoaFisicaService funcionalidadeacaoPessoaFisicaService;
	
	@PostConstruct
	private void init() {
		renderedCronograma = false;
	}
	
	public void load(Integer tela){
		try {
			this.tela=tela;
			itemCronograma = new ItemCronograma();
			atividadeCronogramaAdd = new ControleCronograma();
			atividCronogData = new ControleCronograma();
			usuario = ContextoUtil.getContexto().getUsuarioLogado();
			areaLogada = areaService.buscarAreaPorPessoaFisica(usuario.getIdePessoaFisica());
			carregarCronograma();
			renderedCronograma = true;
			RequestContext.getCurrentInstance().addPartialUpdateTarget("mainPanelCronograma");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void limparTela() {
		limparComponentesFormulario(formularioASerLimpo);
	}

	public void novoCronograma() {
		setControleCronogramaReuniao(null);
		setControleCronogramaConclusaoInspecao(null);
		setControleCronogramaConclusaoParecer(null);
		limparTela();
	}
	
	/**
	 * @author micael.coutinho
	 * Carrega do banco o cronograma que pertence ao processo
	 */
	public void carregarCronograma(){
		try {
			 Processo processoBuscado = new Processo(vwProcesso.getIdeProcesso(), vwProcesso.getNumProcesso(), vwProcesso.getDtcFormacao());
			 if(Util.isNullOuVazio(areaLogada)){
				 this.areaLogada = buscarAreaByControleTramite(processoBuscado);
			 }
			 
			 Cronograma cronoTemp = cronogramaService.buscarCronogramaDoProcesso(processoBuscado,this.areaLogada);
			 if(!Util.isNullOuVazio(cronoTemp)){
				 cronograma =  cronoTemp;
				 if(!Util.isNullOuVazio(cronoTemp.getControleCronogramaCollection())){
					 definirMinDataPrevista(cronoTemp);
					 definirMaxDataPrevista(cronoTemp);
				 }
				 else{
					 modelAtividControleCronograma = new ArrayList<ControleCronograma>();
					 maxDataPrevista = "";
				 }
			 }else{
				 modelAtividControleCronograma = new ArrayList<ControleCronograma>();
			 }

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	private Area buscarAreaByControleTramite(Processo processoBuscado) {
		 Collection<ControleTramitacao> listaTemp = controleTramitacaoService.listarControleTramitacaoPorIdeProcesso(processoBuscado.getIdeProcesso());
		 for(ControleTramitacao controleTramitacao : listaTemp){
			 if(Util.isNull(controleTramitacao.getIndAreaSecundaria()) || !controleTramitacao.getIndAreaSecundaria()){
				 return controleTramitacao.getIdeArea();  
			 }
		 }
		 return null;
	}
	
	private void definirMinDataPrevista(Cronograma cronoTemp){
		minDataPrevista = cronoTemp.getIdeProcesso().getDtcFormacao();
	}

	private void definirMaxDataPrevista(Cronograma cronoTemp) {
		modelAtividControleCronograma = new ArrayList<ControleCronograma>((List<ControleCronograma>) cronoTemp.getControleCronogramaCollection());
		for (Iterator<ControleCronograma> iterator = modelAtividControleCronograma.iterator(); iterator.hasNext();) {
			ControleCronograma controlCrono = iterator.next();
			if(controlCrono.getIdeItemCronograma().getIdeItemCronograma().equals(ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde())){
				SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yy");  
				maxDataPrevista = spf.format(controlCrono.getDtcItemPrevista());
			}
		}
	}
	
	public String getMinDataPrevistaFormatada() {
		if(!Util.isNullOuVazio(minDataPrevista)){
			SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yy");  
			return spf.format(minDataPrevista);
		}else
			return "";
	}
	
	public void definirMaxDataPrevista(){
		if(Util.isNullOuVazio(cronograma) && Util.isNullOuVazio(cronograma.getIdeCronograma()) && Util.isNullOuVazio(cronograma.getControleCronogramaCollection()))
			definirMaxDataPrevista(cronograma);
		
		if(getUsuarioLogadoGestor()){
			maxDataPrevista = "";
		}
		else if(atividCronogData.getIdeItemCronograma().getIdeItemCronograma() == ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde()){
			SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yy");
			maxDataPrevista = spf.format(atividCronogData.getDtcItemPrevista());
		}
	}
	
	/**
	 * @author micael.coutinho
	 * Adiciona uma Atividade ao cronograma
	 */
	public void adicionarControleCronograma() {
		msgDataMaiorQConclusao = "Data da Atividade deve ser menor que a data de conclusão do parecer.";
		if(Util.isNullOuVazio(cronograma)){
			carregarOuCriarCronograma();
		}
		
		Exception erro = null;
		
		try {
				if(!Util.isNullOuVazio(itemCronograma) && !Util.isNullOuVazio(itemCronograma.getIdeItemCronograma()) && !Util.isNullOuVazio(atividadeCronogramaAdd.getDtcItemPrevista()) ){
					atividadeCronogramaAdd.setIdeItemCronograma(itemCronograma);
					if(!atividadeCronogramaAdd.getIdeItemCronograma().getIdeItemCronograma().equals(ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde())){
						if(isDataAtividadeMenorQConclusao(atividadeCronogramaAdd)){
							cronogramaService.adcionarControleCronograma(cronograma, atividadeCronogramaAdd);
							//define a descrição do itemCronograma, para que seja exibido na tabela para o usuário, e sem precisar ir no banco.
							atividadeCronogramaAdd.getIdeItemCronograma().setDscItemCronograma(ItemCronogramaEnum.REUNIAO.getNomeItemCronograma(atividadeCronogramaAdd.getIdeItemCronograma().getIdeItemCronograma()));
							carregarCronograma();
						}else
							JsfUtil.addWarnMessage(msgDataMaiorQConclusao);
					}else{
						cronogramaService.adcionarControleCronograma(cronograma, atividadeCronogramaAdd);
						//define a descrição do itemCronograma, para que seja exibido na tabela para o usuário, e sem precisar ir no banco.
						atividadeCronogramaAdd.getIdeItemCronograma().setDscItemCronograma(ItemCronogramaEnum.REUNIAO.getNomeItemCronograma(atividadeCronogramaAdd.getIdeItemCronograma().getIdeItemCronograma()));
						carregarCronograma();
					}
				}else if(Util.isNullOuVazio(atividadeCronogramaAdd.getDtcItemPrevista())){
					JsfUtil.addErrorMessage("O campo Data Prevista é de preenchimento obrigatório.");
				}else if(Util.isNullOuVazio(itemCronograma.getIdeItemCronograma())){
					JsfUtil.addErrorMessage("O campo Tipo da Atividade é de preenchimento obrigatório.");
				}
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				JsfUtil.addErrorMessage(e.getMessage());
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
			
			atividadeCronogramaAdd = new ControleCronograma();
			itemCronograma.setIdeItemCronograma(null);
	}
	
	private Boolean isDataAtividadeMenorQConclusao(ControleCronograma atividadeCronograma){
		if(Util.isNullOuVazio(cronograma.getControleCronogramaCollection())){
			msgDataMaiorQConclusao = "Deve ser definido primeiramente a Data de Conclusão do Parecer.";
		}else{
			for (Iterator<ControleCronograma> iterator = cronograma.getControleCronogramaCollection().iterator(); iterator.hasNext();) {
				ControleCronograma conclusaoParecer = iterator.next();
				if(conclusaoParecer.getIdeItemCronograma().getIdeItemCronograma().equals(ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde())){
					if(atividadeCronograma.getDtcItemPrevista().compareTo(conclusaoParecer.getDtcItemPrevista()) <= 0)
						return true;
					else
						return false;
				}
			}
		}
		return false;
	}
	
	private void carregarOuCriarCronograma() {
		
		Exception erro = null;
		try {
			Processo processoBuscado = new Processo(vwProcesso.getIdeProcesso(), vwProcesso.getNumProcesso(), vwProcesso.getDtcFormacao());
			Area coordenadorArea = ContextoUtil.getContexto().getPessoaFisica().getFuncionario().getIdeArea();
			cronograma = cronogramaService.buscarCronogramaDoProcesso(processoBuscado,coordenadorArea);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		
	}

	/**
	 * @author micael.coutinho
	 */
	public void showDefinirData(){
		if(Util.isNullOuVazio(atividCronogData.getDtcItemRealizada()))
			atividCronogData.setDtcItemRealizada(new Date());
		if(Util.isNullOuVazio(atividCronogData.getDtcItemPrevista()))
			atividCronogData.setDtcItemPrevista(new Date());
		definirMaxDataPrevista(cronograma);
	}
	
	public void definirDataRealizacao(){
		
		Exception erro =null;
		try {
			if(atividCronogData.getDtcItemRealizada().after(atividCronogData.getDtcItemPrevista()) && atividCronogData.getDscJustificativa().isEmpty()){
				RequestContext request = RequestContext.getCurrentInstance();
		    	request.addCallbackParam("flagValidate", false);
		    	request.addCallbackParam("validationFailed", true);
		    	atividCronogData.setDtcItemRealizada(null);
				JsfUtil.addWarnMessage("Como a data de Realização é maior que a Prevista, a justificativa é obrigatória.");
			}else{
				cronogramaService.atualizarControleCronograma(atividCronogData);
				JsfUtil.addSuccessMessage("Data de Realização salva com sucesso!");
				carregarCronograma();
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public void replanejarData(){
		Exception erro = null;
		try {
			Processo processoBuscado = new Processo(vwProcesso.getIdeProcesso(), vwProcesso.getNumProcesso(), vwProcesso.getDtcFormacao());
			ControleTramitacao tramitacaoAtual = controleTramitacaoService.buscarUltimoPorProcesso(new Processo(vwProcesso.getIdeProcesso()));
			Area coordenadorArea = tramitacaoAtual.getIdeArea();
			Cronograma cronoTemp = cronogramaService.buscarCronogramaDoProcesso(processoBuscado,coordenadorArea);
			List<ControleCronograma> listaControleCronograma = (List<ControleCronograma>) cronoTemp.getControleCronogramaCollection();
			boolean erroDataAnterior = false;
			if(atividCronogData.getIdeItemCronograma().getIdeItemCronograma().equals(ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde())){
				for (ControleCronograma controleCronograma : listaControleCronograma) {
					if (atividCronogData.getDtcItemPrevista().before(controleCronograma.getDtcItemPrevista())
							|| !Util.isNullOuVazio(controleCronograma.getDtcItemRealizada()) && atividCronogData.getDtcItemPrevista().before(controleCronograma.getDtcItemRealizada())	
							) {
						erroDataAnterior = true;
						JsfUtil.addErrorMessage("Data de replanejamento da Conclusão do Parecer deve ser posterior a de uma atividade já cadastrada!");
						break;
					}
				}
			}else{
				SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yy");  
				Date dataMinima = (Date)spf.parse(spf.format(getMinDataPrevista())); 
				for (ControleCronograma controleCronograma : listaControleCronograma) {
					if (controleCronograma.getIdeItemCronograma().getIdeItemCronograma().equals(ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde())){
						if(atividCronogData.getDtcItemPrevista().before(dataMinima) || atividCronogData.getDtcItemPrevista().after(controleCronograma.getDtcItemPrevista()) ){
							JsfUtil.addErrorMessage("Data de replanejamento da atividade deve ser Maior que a Data de Formação do Projeto e Menor que a data de Conclusão do Parecer!");
							erroDataAnterior = true;
							break;
						}
					}
				}
			}
			
			if (!erroDataAnterior) {
				cronogramaService.atualizarControleCronograma(atividCronogData);
				JsfUtil.addSuccessMessage("Data Replanejada com sucesso!");
				carregarCronograma();
			} else {
				carregarCronograma();
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public Boolean isEmptyDate(ControleCronograma controlCronog){
		return Util.isNullOuVazio(controlCronog.getDtcItemRealizada());
	}
	
	public Boolean isConclusaoParecer(ControleCronograma controlCronog){
		return controlCronog.getIdeItemCronograma().getIdeItemCronograma().equals(ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde());
	}
	
	public void excluirAtividade(){
		
		Exception erro =null;
		
		try {
			cronogramaService.excluirAtividadeCronograma(delAtividadeCronograma);
			carregarCronograma();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public void changeListenerReplanDataPrevista(ValueChangeEvent pValueChangeEvent) {
		if (pValueChangeEvent.getNewValue() instanceof Date) {
			atividCronogData.setDtcItemPrevista((Date)pValueChangeEvent.getNewValue());
		}
		atividCronogData.setDtcItemRealizada(null);
	}
	
	
	
	public Boolean getUsuarioLogadoGestor() {
		
		
		
		if(usuario.getIdePerfil().getIdePerfil().equals( PerfilEnum.COORDENADOR.getId())
		   ||usuario.getIdePerfil().getIdePerfil().equals(PerfilEnum.COORD_CTGA.getId())){
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public Boolean habilitaReplanejarData(ControleCronograma atividade){
		
		if(getUsuarioLogadoGestor() || isTemPermissaoParaDefinirCronograma()){
			return true;
		}
		else { 
		  if(atividade.getIdeItemCronograma().getIdeItemCronograma() == ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde()){
			  return false;
		  }
		  else {
			  return true;
		  }
		}
	}

	private boolean isTemPermissaoParaDefinirCronograma() {
		int usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica();
		Boolean retorno = null;
		try{
			retorno = funcionalidadeacaoPessoaFisicaService.isTemPermissao(usuarioLogado , AcaoControlePermissaoAcessoPautaEnum.DEFINIR_CRONOGRAMA.getId());
			if(retorno == null) {
				retorno = false;
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		return retorno;			
	}
	
	public VwConsultaProcesso getVwProcesso() {

		return vwProcesso;
	}
	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public Cronograma getCronograma() {
		if (Util.isNullOuVazio(cronograma)) cronograma = new Cronograma();
		return cronograma;
	}
	public void setCronograma(Cronograma cronograma) {
		this.cronograma = cronograma;
	}

	public ControleCronograma getControleCronogramaReuniao() {
		if (Util.isNullOuVazio(controleCronogramaReuniao)) controleCronogramaReuniao = new ControleCronograma();
		return controleCronogramaReuniao;
	}
	public void setControleCronogramaReuniao(ControleCronograma controleCronogramaReuniao) {
		this.controleCronogramaReuniao = controleCronogramaReuniao;
	}

	public ControleCronograma getControleCronogramaConclusaoInspecao() {

		if (Util.isNullOuVazio(controleCronogramaConclusaoInspecao)) controleCronogramaConclusaoInspecao = new ControleCronograma();
		return controleCronogramaConclusaoInspecao;
	}
	public void setControleCronogramaConclusaoInspecao(ControleCronograma controleCronogramaConclusaoInspecao) {
		this.controleCronogramaConclusaoInspecao = controleCronogramaConclusaoInspecao;
	}

	public ControleCronograma getControleCronogramaConclusaoParecer() {

		if (Util.isNullOuVazio(controleCronogramaConclusaoParecer)) controleCronogramaConclusaoParecer = new ControleCronograma();
		return controleCronogramaConclusaoParecer;
	}
	public void setControleCronogramaConclusaoParecer(ControleCronograma controleCronogramaConclusaoParecer) {
		this.controleCronogramaConclusaoParecer = controleCronogramaConclusaoParecer;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}

	public boolean isPautaTecnica() {
		if (tela != null && tela == 2){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isStatusAnaliseTecnica(Integer i){
		if(i == StatusFluxoEnum.ANALISE_TECNICA.getStatus()){
			return false;
		}
		
		return true;
	}
	
	public boolean isStatusNotificacaoRespondida(Integer i){
		if(i == StatusFluxoEnum.NOTIFICACAO_RESPONDIDA.getStatus()){
			return false;
		}
		
		return true;
	}
	
	public boolean disableCronograma(Integer statusFluxo) {
		if ((statusFluxo == StatusFluxoEnum.NOTIFICACAO_RESPONDIDA.getStatus()) 
			|| (statusFluxo == StatusFluxoEnum.ANALISE_TECNICA.getStatus())
			|| (statusFluxo == StatusFluxoEnum.NOTIFICACAO_NAO_RESPONDIDA.getStatus())) {
			return false;
		} else {
			return true;
		}
	}
	
	public List<ItemCronograma> getListaAtividades() {
		List<ItemCronograma> lista = new ArrayList<ItemCronograma>();
        for (int j = 0; j < ItemCronogramaEnum.values().length; j++) {
        	lista.add( new ItemCronograma(ItemCronogramaEnum.values()[j].getIde(), ItemCronogramaEnum.values()[j].getNomeItemCronograma() ) );
        }
        
        return lista;
	}
	
	public boolean isUsuarioTecnico(){
		if(Util.isNullOuVazio(vwProcesso)){
			return false;
		}
		if(usuario.getIdePerfil().getIdePerfil().equals(PerfilEnum.TECNICO.getId()) && Util.isNullOuVazio( vwProcesso.getFuncionarioLiderEquipe())){
			return true;
		}
		
		Funcionario funcionarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getFuncionario();
		Processo processoBuscado = new Processo(vwProcesso.getIdeProcesso());
		EquipeProcesso eq = null;
		try{

			 /*eq = equipeProcessoService.buscarPorProcessoPorFuncionario(processoBuscado,funcionarioLogado);*/
		}
		catch(Exception e){
			eq = new EquipeProcesso();
		}
		
		if((ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil().equals(PerfilEnum.TECNICO.getId())
		   || ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil().equals(PerfilEnum.TEC_CTGA.getId()))
		   && !eq.getIndLiderEquipe()){
			return true;
		}else
			return false;
	}
	
	public boolean isUsuarioLider(){
		if(Util.isNull(vwProcesso) || Util.isNull(vwProcesso.getFuncionarioLiderEquipe())){
			return false;
		}
		if(vwProcesso.getFuncionarioLiderEquipe().getIdePessoaFisica().intValue() == usuario.getIdePessoaFisica().intValue()){
			return true;
		}
		return false;
	}
	
	public boolean isApenasVisualizacao() {
		
		try {
			if(isFluxoDiruc()) {
				return true;
			}
			else if(isUsuarioTemPermissaoDefinirCronograma()) {
				return false;
			}			
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return true;
	}
	
	private boolean isFluxoDiruc()  {
		
		boolean isFluxoDiruc = false;
		
		if(!Util.isNull(vwProcesso)) {
			Integer ideProcesso = vwProcesso.getIdeProcesso();
			Integer idePessoaFisicaLogada = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica();
			Area areaFluxoDiruc = areaService.buscaAreaProcessoIndAreaSecundariaTrue(ideProcesso);
			
			Integer idePessoaFisicaCoordenadorAreaFluxoDiruc = !Util.isNull(areaFluxoDiruc) ? areaFluxoDiruc.getIdePessoaFisica().getIdePessoaFisica() : null;
			
			if(idePessoaFisicaLogada.equals(idePessoaFisicaCoordenadorAreaFluxoDiruc)) {
				isFluxoDiruc = true;
			}
			else {
				List<FuncionalidadeAcaoPessoaFisica> permissoes = null;
				permissoes = funcionalidadeacaoPessoaFisicaService.listarPermissoes(idePessoaFisicaLogada, idePessoaFisicaCoordenadorAreaFluxoDiruc);
				if(!Util.isNullOuVazio(permissoes)) {
					for(FuncionalidadeAcaoPessoaFisica fapf : permissoes) {
						if(fapf.getIdeAcao().equals(new Acao(AcaoControlePermissaoAcessoPautaEnum.DEFINIR_CRONOGRAMA.getId()))) {
							isFluxoDiruc = true;
							break;
						}
					}
				}
			}			
		}
		
		return isFluxoDiruc;
	}
	
	private boolean isUsuarioTemPermissaoDefinirCronograma() {
		if(isPessoaLogadaCoordenadorAreaAtual()) {
			return true;
		}
		else{
			int permissaoDistribuir = AcaoControlePermissaoAcessoPautaEnum.DEFINIR_CRONOGRAMA.getId();
			int usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica();
			return funcionalidadeacaoPessoaFisicaService.isTemPermissao(usuarioLogado , permissaoDistribuir);
		}
	}
	
	private boolean isPessoaLogadaCoordenadorAreaAtual() {
		try {
			int idePessoaFisicaLogada = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica();
			boolean indAreaSecundaria = false;
			ControleTramitacao ct = null ;
			
			if(!Util.isNull(vwProcesso)) {
				ct = controleTramitacaoService.buscarUltimaTramitacaoPorProcessoCoordenador(vwProcesso.getIdeProcesso(), idePessoaFisicaLogada, indAreaSecundaria);
				if(!Util.isNull(ct)) {
					return true;
				}				
			}
			return false;			
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}		
	}

	public Integer getTela() {
		return tela;
	}

	public void setTela(Integer tela) {
		this.tela = tela;
	}

	/**
	 * @return the listaTipoAtividade
	 */
	public List<ItemCronograma> getListaTipoAtividade() {
		return listaTipoAtividade;
	}

	/**
	 * @param listaTipoAtividade the listaTipoAtividade to set
	 */
	public void setListaTipoAtividade(List<ItemCronograma> listaTipoAtividade) {
		this.listaTipoAtividade = listaTipoAtividade;
	}

	/**
	 * @return the atividadeCronograma
	 */
	public ControleCronograma getAtividadeCronograma() {
		return atividadeCronogramaAdd;
	}

	/**
	 * @param atividadeCronograma the atividadeCronograma to set
	 */
	public void setAtividadeCronograma(ControleCronograma atividadeCronograma) {
		this.atividadeCronogramaAdd = atividadeCronograma;
	}

	/**
	 * @return the itemCronograma
	 */
	public ItemCronograma getItemCronograma() {
		return itemCronograma;
	}

	/**
	 * @param itemCronograma the itemCronograma to set
	 */
	public void setItemCronograma(ItemCronograma itemCronograma) {
		this.itemCronograma = itemCronograma;
	}

	/**
	 * @return the atividadeCronogramaAdd
	 */
	public ControleCronograma getAtividadeCronogramaAdd() {
		return atividadeCronogramaAdd;
	}

	/**
	 * @param atividadeCronogramaAdd the atividadeCronogramaAdd to set
	 */
	public void setAtividadeCronogramaAdd(ControleCronograma atividadeCronogramaAdd) {
		this.atividadeCronogramaAdd = atividadeCronogramaAdd;
	}

	/**
	 * @return the modelAtividControleCronograma
	 */
	public List<ControleCronograma> getModelAtividControleCronograma() {
		return modelAtividControleCronograma;
	}

	/**
	 * @param modelAtividControleCronograma the modelAtividControleCronograma to set
	 */
	public void setModelAtividControleCronograma(
			List<ControleCronograma> modelAtividControleCronograma) {
		this.modelAtividControleCronograma = modelAtividControleCronograma;
	}

	/**
	 * @return the delAtividadeCronograma
	 */
	public ControleCronograma getDelAtividadeCronograma() {
		return delAtividadeCronograma;
	}

	/**
	 * @param delAtividadeCronograma the delAtividadeCronograma to set
	 */
	public void setDelAtividadeCronograma(ControleCronograma delAtividadeCronograma) {
		this.delAtividadeCronograma = delAtividadeCronograma;
	}

	/**
	 * @return the atividCronogData
	 */
	public ControleCronograma getAtividCronogData() {
		return atividCronogData;
	}

	/**
	 * @param atividCronogData the atividCronogData to set
	 */
	public void setAtividCronogData(ControleCronograma atividCronogData) {
		this.atividCronogData = atividCronogData;
	}

	/**
	 * @return the maxDataPrevista
	 */
	public String getMaxDataPrevista() {
		return maxDataPrevista;
	}

	/**
	 * @param maxDataPrevista the maxDataPrevista to set
	 */
	public void setMaxDataPrevista(String maxDataPrevista) {
		this.maxDataPrevista = maxDataPrevista;
	}

	/**
	 * @return the minDataPrevista
	 */
	public Date getMinDataPrevista() {
		return minDataPrevista;
	}

	/**
	 * @param minDataPrevista the minDataPrevista to set
	 */
	public void setMinDataPrevista(Date minDataPrevista) {
		this.minDataPrevista = minDataPrevista;
	}

	public boolean isRenderedCronograma() {
		return renderedCronograma;
	}

	public void setRenderedCronograma(boolean renderedCronograma) {
		this.renderedCronograma = renderedCronograma;
	}
}