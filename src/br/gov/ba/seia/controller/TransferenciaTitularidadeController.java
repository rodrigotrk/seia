package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.AtoAmbientalTipologia;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.SolicitacaoAdministrativoAtoAmbiental;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.PessoaENUM;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.ProcessoAtoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoAtoAmbientalService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.service.TransferenciaAmbientalService;
import br.gov.ba.seia.util.DataUtil;
import br.gov.ba.seia.util.ExpressaoRegularUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.validators.CnpjValidator;
import br.gov.ba.seia.validators.CpfValidator;

@Named("transferenciaTitularidadeController")
@ViewScoped
public class TransferenciaTitularidadeController extends BaseDialogController{
	
	private List<AtoAmbiental> listaAtoAmbiental;

	private List<Empreendimento> listaEmpreendimentos;

	private SolicitacaoAdministrativo solicitacaoTransferenciaTitularidade;

	private Pessoa pessoa;
	
	private Integer indTipoPessoa;

	private String documento;
	
	@Inject
	private AbaProcessoController abaProcessoController;
	@EJB
	private TipologiaService tipologiaService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private PessoaService pessoaService;
	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativaService;
	@EJB
	private SolicitacaoAdministrativoAtoAmbientalService solicitacaoAdministrativaAtoAmbientalService;
	@EJB
	private ProcessoAtoService processoAtoService;
	@EJB
	private TransferenciaAmbientalService transferenciaAmbientalService;
	
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	
	@EJB
	private RequerimentoService requerimentoService;

	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;

	@EJB
	private SolicitacaoAdministrativoAtoAmbientalService solicitacaoAdministrativoAtoAmbientalService;

	
	private boolean exibirAtos;
	private boolean isVisualizarMode;
	
	@PostConstruct
	public void example(){
		
	}
	
	public void load() {
		
		exibirAtos = false;
		this.limpar();
	
		this.editMode =false;
		listaAtoAmbiental = new ArrayList<AtoAmbiental>();
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dialogTransfTitularidade:pngTbAtosTransTitularidade");
		RequestContext.getCurrentInstance().addPartialUpdateTarget(":dialogTransfTitularidade:pngTbAtosTransTitularidade");
	}

	void limpar() {
		this.solicitacaoTransferenciaTitularidade = new SolicitacaoAdministrativo(super.getRequerimento());
		this.indTipoPessoa = null;
		this.documento = null;
		this.pessoa = null;
	}
	
	public String getTextoMassacaraProcesso(){
		return	
		  "Ao inserir o número do processo, favor atentar para a inserção do número completo," +
		  "seguindo o padrão da numeração do sistema que foi gerado. Ex:         	     	 " +
		  "Padrão do numero de processos SEIA:      2015.001.XXXXX/INEMA/LIC-0XXXX,			 " + 
		  "Padrão do número de processos CERBERUS:  2001-0XXXXX/TEC/LL-0XXX,				 " + 
		  "Padrão do número de processos PROHIDROS: 2013-0XXXXX/OUT/APPO-0XXX.				 " ;	  
	}

	public void atualizarPosicionamentoDialog(){
		RequestContext.getCurrentInstance().execute("transfTitularidade.show()");
		RequestContext.getCurrentInstance().addPartialUpdateTarget(":panelTransferDialog");
	}
	
	private boolean processoValido(String numProcesso) throws Exception{
		
		if(!ExpressaoRegularUtil.isProcessoSeiaOrCereberusOrProHidros(numProcesso)){
			JsfUtil.addWarnMessage("Digite um número de processo válido.");
			return false;	
		}
		
		if(!ExpressaoRegularUtil.isProcessoCereberusOrProHidros(numProcesso)){
		
			if(isProcessoTlaNaoTransferido(numProcesso)){
				JsfUtil.addWarnMessage("Não existem atos ambientais deferidos para esse processo.");
				return false;
			}
		
			if(isProcessoNaoConcluido(numProcesso)){
				JsfUtil.addWarnMessage("Este processo ainda não foi concluído.");
				return false;
			}
		}
		return true;
	}
	
	private boolean isProcessoNaoConcluido(String numProcesso) {
		Processo processo = processoService.buscarProcessoPorNumero(numProcesso);
		
		if(!Util.isNullOuVazio(processo)){
			if(!controleTramitacaoService.buscarUltimoPorProcesso(processo).getIdeStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.CONCLUIDO.getStatus())){
				return true;
			}	
		}
		return false;
	}

	private boolean isProcessoTlaNaoTransferido(String numProcesso) throws Exception{
		Processo processo = processoService.buscarProcessoPorNumero(numProcesso);
		
		if(!Util.isNullOuVazio(processo)){
			
			boolean possuiStatusTransferido= false;
			for (ProcessoAto pa : processoAtoService.listarAtosPorProcesso(processo.getIdeProcesso())) {
				if(transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(pa.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.DEFERIDO.getId())||
				   transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(pa.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.TRANSFERIDO.getId())){
					possuiStatusTransferido = true;		
				}
			}
			
			if(!possuiStatusTransferido &&  transferenciaAmbientalService.isProcessoPossuiSolicitacaoAmbiental(processo.getIdeProcesso(), TipoSolicitacaoEnum.TLA.getId())){
				return true;	
			}	
		}
		return false;
	}
	

	private boolean isProcessoTlaTransferido(Processo processo) throws Exception{
		boolean possuiStatusTransferido= false;
		for (ProcessoAto pa : processoAtoService.listarAtosPorProcesso(processo.getIdeProcesso())) {
			if(transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(pa.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.TRANSFERIDO.getId())
					|| transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(pa.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.DEFERIDO.getId())
					|| transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(pa.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.EMITIDO.getId())){
				possuiStatusTransferido = true;		
			}
		}
	
		if(possuiStatusTransferido && transferenciaAmbientalService.isProcessoPossuiSolicitacaoAmbiental(processo.getIdeProcesso(), TipoSolicitacaoEnum.TLA.getId())){
			return true;	
		}	
		return false;
	}
	

	private void carregaAtoByProcessoPai(Processo processo) throws Exception {
		
		processo.setProcessoAtoCollection(processoAtoService.getProcessoAtoProcessoPai(processo));

		if(transferenciaAmbientalService.isTransferenciaComOrigem(processo)){
			processo.setProcessoAtoCollection(processoAtoService.getProcessoAtoProcessoPai(processo));	
		}else{
			processo = prepararParaExibirProcessoSemAto(processo);	
		}
		carregaListaProcessoAtoTipologia(processo);
	}

	private Processo prepararParaExibirProcessoSemAto(Processo processo)throws Exception {
		List<ProcessoAto> paList = new ArrayList<ProcessoAto>(); 
		ProcessoAto pa;
		
		processo = processoService.carregarProcesso(processo.getIdeProcesso());
		SolicitacaoAdministrativo solicitacaoAdminstrativo = requerimentoService.getSolicitacaoAdminstrativo(processo.getIdeRequerimento(),TipoSolicitacaoEnum.TRANSFERENCIA_LICENCA_AMBIENTAL);

		if (!Util.isNullOuVazio(solicitacaoAdminstrativo)) {
			solicitacaoAdminstrativo.setSolicitacaoAdminstrativoAtoAmbientalCollection(solicitacaoAdministrativoAtoAmbientalService.listaAtoBySolicitacaoComTipologia(solicitacaoAdminstrativo));
		}

		if (!Util.isNullOuVazio(solicitacaoAdminstrativo) && !Util.isNullOuVazio(solicitacaoAdminstrativo.getSolicitacaoAdminstrativoAtoAmbientalCollection())) {
			
			if(Util.isLazyInitExcepOuNull(solicitacaoAdminstrativo.getSolicitacaoAdminstrativoAtoAmbientalCollection())){
				solicitacaoAdminstrativo.setSolicitacaoAdminstrativoAtoAmbientalCollection(solicitacaoAdministrativoAtoAmbientalService.listaAtoBySolicitacaoComTipologia(solicitacaoAdminstrativo));
			}
			
			for(SolicitacaoAdministrativoAtoAmbiental sol : solicitacaoAdminstrativo.getSolicitacaoAdminstrativoAtoAmbientalCollection()) {
				
				pa = new ProcessoAto();
				pa.setAtoAmbiental(sol.getIdeAtoAmbiental());
				if (Util.isNullOuVazio(sol.getIdeTipologia())) {
					pa.setTipologia(sol.getIdeTipologia());
				}
				paList.add(pa);
			}	
		}
		processo.setProcessoAtoCollection(paList);
		return processo;
	}
	
	private boolean carregaAtoByProcesso(Processo processo) throws Exception {

		boolean encontrouAto = false;
		List<ProcessoAto> processoAtoDeferidos = new ArrayList<ProcessoAto>();
		
		if(Util.isNullOuVazio(processoAtoService.listarAtosTlaByProcesso(processo.getIdeProcesso(), true))){
			if(!Util.isNullOuVazio(processoAtoService.listarAtosTlaByProcesso(processo.getIdeProcesso(), false))){
				encontrouAto = true;
			}
		}
		
		for (ProcessoAto processoAto : processoAtoService.listarAtosTlaByProcesso(processo.getIdeProcesso(), true)){
			encontrouAto = true;
			if(transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(processoAto.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.DEFERIDO.getId())
					|| transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(processoAto.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.TRANSFERIDO.getId())
					|| transferenciaAmbientalService.getIdeStatusProcessoAtoMaisRecente(processoAto.getIdeProcessoAto()).equals(StatusProcessoAtoEnum.EMITIDO.getId())){
				
				if(!processoAtoDeferidos.contains(processoAto)){
					processoAtoDeferidos.add(processoAto);					
				}
			}
		}

		if(encontrouAto && Util.isNullOuVazio(processoAtoDeferidos)){
			JsfUtil.addWarnMessage("Não existe ato deferido ou passível de TLA para este processo.");
		}
		
		processo.setProcessoAtoCollection(processoAtoDeferidos);		
		carregaListaProcessoAtoTipologia(processo);
		
		return !encontrouAto;
	}

	private void carregaListaProcessoAtoTipologia(Processo processo) {
		for (ProcessoAto processoAto: (List<ProcessoAto>) processo.getProcessoAtoCollection()) {
			if(!Util.isNullOuVazio(processoAto.getTipologia())){
				processoAto.getAtoAmbiental().setTipologia(processoAto.getTipologia());
			}
			listaAtoAmbiental.add(processoAto.getAtoAmbiental());
		}
	}
	
	private void carregarListaAtoGenerico() throws Exception {
	
		//Atos com tipologia
		for(AtoAmbientalTipologia atoTipo: atoAmbientalService.listarAtoAmbientalTipologiaByIsVisivelTLA(true)){
			listaAtoAmbiental.add(carregarAtoAmbiental(atoTipo));
		}
		
		ArrayList<Integer> ideAtoAmbiental = new ArrayList<Integer>();
		for (AtoAmbiental ato : listaAtoAmbiental) {
			ideAtoAmbiental.add(ato.getIdeAtoAmbiental());				
		}

		//Atos sem tipologia
		listaAtoAmbiental.addAll(atoAmbientalService.listarAtoAmbientalSemTipologiaByIsVisivelTLA(true, ideAtoAmbiental));	
		Collections.sort(listaAtoAmbiental);
		
	}
	
	private AtoAmbiental carregarAtoAmbiental(AtoAmbientalTipologia ambientalTipologia){
		
		AtoAmbiental atoAmbiental = new AtoAmbiental();
		atoAmbiental.setIdeAtoAmbiental(ambientalTipologia.getIdeAtoAmbiental().getIdeAtoAmbiental());
		if(!Util.isNullOuVazio(ambientalTipologia.getIdeAtoAmbiental())){
			atoAmbiental.setTipologia(ambientalTipologia.getIdeTipologia());
		}
		atoAmbiental.setNomAtoAmbiental(ambientalTipologia.getIdeAtoAmbiental().getNomAtoAmbiental());
		return atoAmbiental;
	}
	
	
	public <T> void editar(T solicitacaoAdministrativo){
		try {
			
			isVisualizarMode = false;
			editMode = true;
			
			dadosDaInicializacao((SolicitacaoAdministrativo)solicitacaoAdministrativo);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
           	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public <T> void visualizar(T solicitacaoAdministrativo){
		try {
			isVisualizarMode = true;
			editMode = false;
			
			dadosDaInicializacao((SolicitacaoAdministrativo)solicitacaoAdministrativo);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
           	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void dadosDaInicializacao(SolicitacaoAdministrativo solicitacaoAdministrativo) throws Exception{
		
		pessoa = pessoaService.buscarPessoaByDocumento(documento);
		solicitacaoTransferenciaTitularidade =  solicitacaoAdministrativo;
		carregaDadosPessoa();
		carregarAtosDoProcesso();	
		exibirAtos = true;
		
		RequestContext.getCurrentInstance().addPartialUpdateTarget(":dialogTransfTitularidade");
		
	}
	
	public void carregarAtosAmbientaisOnBlur() {
		Processo processo = null;
		listaAtoAmbiental = new ArrayList<AtoAmbiental>();
		boolean processoValido = true;
		
		try {
			if(processoValido(solicitacaoTransferenciaTitularidade.getNumProcesso())){
				processo = processoService.buscarProcessoPorNumero(solicitacaoTransferenciaTitularidade.getNumProcesso());				
			
				if(!Util.isNullOuVazio(processo)){
				
					if(isProcessoTlaTransferido(processo)){
						carregaAtoByProcessoPai(processo);
					}else{
						processoValido = carregaAtoByProcesso(processo);
					}
				}
				if(Util.isNullOuVazio(listaAtoAmbiental) && processoValido){
					carregarListaAtoGenerico();					
				}
				
				exibirAtos = true;	
			}
					
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarAtosDoProcesso(){
		carregaListaEmpreendimentos();
		carregarAtosAmbientaisOnBlur();
		
		try{	
			
			List<SolicitacaoAdministrativoAtoAmbiental> solicitacaoAtoAmbiental = solicitacaoAdministrativaAtoAmbientalService.listaAtoBySolicitacaoComTipologia(solicitacaoTransferenciaTitularidade);
			List <AtoAmbiental> listaSegundaria = new ArrayList<AtoAmbiental>();
			
			//serve pra carregar a tipologia transiente dentro do ato 
			for(SolicitacaoAdministrativoAtoAmbiental solicitacaoAtoTipologia: solicitacaoAtoAmbiental){
				AtoAmbiental atoAmbiental = carregarAtoAmbiental(solicitacaoAtoTipologia.getIdeAtoAmbiental());		
				
				if(solicitacaoAtoTipologia.getIdeTipologia() != null){							
					atoAmbiental.setTipologia(carregarTipologia(solicitacaoAtoTipologia.getIdeTipologia()));					
				}
				
				listaSegundaria.add(atoAmbiental);
			}
			
			for (AtoAmbiental atoParaSeremMarcados: listaAtoAmbiental ) {
				for(AtoAmbiental atoParaSeremComparados: listaSegundaria){
					
					if(atoParaSeremComparados.getTipologia()==null){
						if(atoParaSeremMarcados.getIdeAtoAmbiental().equals( atoParaSeremComparados.getIdeAtoAmbiental())){
							atoParaSeremMarcados.setRowSelect(Boolean.TRUE);
						}
					}else{
						if(atoParaSeremMarcados.getIdeAtoAmbiental().equals( atoParaSeremComparados.getIdeAtoAmbiental()) &&
						   atoParaSeremMarcados.getTipologia().equals(atoParaSeremComparados.getTipologia())){
						   atoParaSeremMarcados.setRowSelect(Boolean.TRUE);
						}
					}
				}
				
			}
			
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private AtoAmbiental carregarAtoAmbiental(AtoAmbiental ambiental){
		
		AtoAmbiental atoAmbiental = new AtoAmbiental();
	
		atoAmbiental.setIdeAtoAmbiental(ambiental.getIdeAtoAmbiental());
		atoAmbiental.setTipologia(null);
		atoAmbiental.setNomAtoAmbiental(ambiental.getNomAtoAmbiental());
		
		return atoAmbiental;
	}
	
	private Tipologia carregarTipologia(Tipologia t1){
		
		Tipologia t2= new Tipologia();
			t2 = t1.cloneImpl();	
		return t2;
	}
	
	private void carregaDadosPessoa() throws Exception{
		
		Pessoa pessoaPesquisa;

		if(solicitacaoTransferenciaTitularidade.getIndDetentorLicenca()){
			pessoaPesquisa = solicitacaoTransferenciaTitularidade.getIdePessoaNovoTitular();
		}else{
			pessoaPesquisa = solicitacaoTransferenciaTitularidade.getIdePessoaDetentorLicenca();
		} 
		 
		if(pessoaPesquisa.isPF()){
			indTipoPessoa = PessoaENUM.FISICA.getId();
		}else{
			indTipoPessoa = PessoaENUM.JURIDICA.getId();
		}
		
		documento = pessoaPesquisa.getCpfCnpj();
		listaEmpreendimentos = (List<Empreendimento>) empreendimentoService.listarEmpreendimento(pessoaPesquisa);
			
	}
	
	public void carregaListaEmpreendimentos(){
		try{
			if(isDocumentoValido()){
				this.listaEmpreendimentos = (List<Empreendimento>) this.empreendimentoService.listarEmpreendimento(this.pessoa);
			}
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private boolean isDocumentoValido(){
		
		boolean retorno = true;
		
		try{
			
			documento = Util.replaceString(documento, new String[] { "/",".", "-" });
			String cpfCnpj;
			Boolean documentoValido;
			
			if (indTipoPessoa == PessoaENUM.FISICA.getId().intValue()) {
				cpfCnpj = "C.P.F.";
				documentoValido = CpfValidator.validaCPF(documento);
			}else{
				cpfCnpj = "C.N.P.J.";
				documentoValido = CnpjValidator.validarCNPJ(documento);
			}
			
			if(documentoValido){
				pessoa = pessoaService.buscarPessoaByDocumento(documento);
			}else{
				JsfUtil.addWarnMessage("Por favor, informe um "+ cpfCnpj + " válido.");
				
			}
			
			if(Util.isNullOuVazio(pessoa)){
				
				if(verificarNovoTitularIgualAoAtual()){
					JsfUtil.addWarnMessage("Por favor, informe um "+ cpfCnpj + " que não seja do atual detentor.");	
				}
				
				listaEmpreendimentos = null;
				pessoa = null;

				retorno = false;
			}
			
			return retorno;
			
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean verificarNovoTitularIgualAoAtual() {
		NovoRequerimentoController nrc = 
				(NovoRequerimentoController) SessaoUtil.recuperarManagedBean("#{novoRequerimentoController}", NovoRequerimentoController.class);
		
		if(!Util.isNullOuVazio(pessoa) && nrc != null && !Util.isNullOuVazio(nrc.getPessoa())) {
			return pessoa.equals(nrc.getPessoa());
		}
		
		return false;
	}
	

	public void salvar(){
		try {
			if(validar()){
				if (solicitacaoTransferenciaTitularidade.getIndDetentorLicenca()) {
					solicitacaoTransferenciaTitularidade.setIdePessoaDetentorLicenca(null);
					solicitacaoTransferenciaTitularidade.setIdePessoaNovoTitular(this.pessoa);
				}else{
					solicitacaoTransferenciaTitularidade.setIdePessoaNovoTitular(null);
					solicitacaoTransferenciaTitularidade.setIdePessoaDetentorLicenca(this.pessoa);
				}		
				solicitacaoTransferenciaTitularidade.setIdeTipoSolicitacao(this.getTipoSolicitacao());
				solicitacaoAdministrativaService.salvarOuAtualizarSolicitacaoAdministrativa(solicitacaoTransferenciaTitularidade);
				salvarAtosTranferenciaTitularidade();
				
				abaProcessoController.adicionarOuAtualizarListaTransferencia(this.solicitacaoTransferenciaTitularidade);
				
				if(editMode){
					JsfUtil.addSuccessMessage("Transferência de titularidade atualizada com sucesso.");
				}else{
					JsfUtil.addSuccessMessage("Transferência de titularidade salva com sucesso.");
				}
				
				RequestContext.getCurrentInstance().execute("transfTitularidade.hide()");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private TipoSolicitacao getTipoSolicitacao() {
		return new TipoSolicitacao(TipoSolicitacaoEnum.TRANSFERENCIA_LICENCA_AMBIENTAL.getId());
	}
	
	private void salvarAtosTranferenciaTitularidade() {
		this.excluirAtosTranferencia();
		for (AtoAmbiental ato : this.listaAtoAmbiental) {
			if (ato.isRowSelect()) {
				
				SolicitacaoAdministrativoAtoAmbiental saa;
				
		
				if(ato.getTipologia()!= null && ato.getTipologia().getIdeTipologia() != null){					
					saa = new SolicitacaoAdministrativoAtoAmbiental(ato, new Tipologia(ato.getTipologia().getIdeTipologia()), solicitacaoTransferenciaTitularidade);
				}else{
					saa = new SolicitacaoAdministrativoAtoAmbiental(ato, solicitacaoTransferenciaTitularidade);
				}
				
				try {
					this.solicitacaoAdministrativaAtoAmbientalService.salvarOuAtualizarSolicitacaoAdministrativaAtoAmbiental(saa);
				} catch (Exception e) {
					JsfUtil.addErrorMessage("Erro ao salvar ato da solicitação. ");
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		}
	}

	private void excluirAtosTranferencia() {
		try {
			this.solicitacaoAdministrativaAtoAmbientalService
				.excluirSolicitacaoAdministrativaAtoAmbientalbySolicitacaoAdm(this.solicitacaoTransferenciaTitularidade);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro na exclusão dos ato da solicitação");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	
	public boolean validar() {

		boolean valido = true;

		if (Util.isNullOuVazio(this.solicitacaoTransferenciaTitularidade.getNumProcesso())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 1.");
			valido = false;
		} 
		
		if (Util.isNullOuVazio(this.solicitacaoTransferenciaTitularidade.getNumPortaria())) {
			JsfUtil.addWarnMessage("Por favor, preencha o campo 2.");
			valido = false;
		}
		
		Date dtcPublicacaoPortaria = this.solicitacaoTransferenciaTitularidade.getDtcPublicacaoPortaria();
		Date dtcValidade = this.solicitacaoTransferenciaTitularidade.getDtcValidade();
		
		if (Util.isNullOuVazio(dtcPublicacaoPortaria)) {
			JsfUtil.addWarnMessage("Por favor, insira a data de publicação da portaria.");
			valido = false;
		} else if (!DataUtil.validaDataSeMaiorQueAtual(dtcPublicacaoPortaria)) {
			JsfUtil.addWarnMessage("Data de publicação da portaria não pode ser superior a data de hoje.");
			valido = false;
		}
		
		if (Util.isNullOuVazio(dtcValidade)) {
			JsfUtil.addWarnMessage("Por favor, insira a data de validade.");
			valido = false;
		} else if (Util.validarDuasDatas(dtcValidade, dtcPublicacaoPortaria)) {
			JsfUtil.addWarnMessage("Data de validade não pode ser inferior a data de publicação da portaria.");
			valido = false;
		}
		if (Util.isNullOuVazio(this.solicitacaoTransferenciaTitularidade.getIndDetentorLicenca())) {
			JsfUtil.addWarnMessage("Selecione alguma opção na questão 5.");
			valido = false;
		} else {
			if (Util.isNullOuVazio(this.documento)) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo 5.2.");
				valido = false;
			}
		}
		
		if(Util.isNullOuVazio(this.solicitacaoTransferenciaTitularidade.getIdeEmpreendimento())){
			JsfUtil.addWarnMessage("Por favor, informe o empreendimento.");
			valido = false;
		}
		
		if(Util.isNullOuVazio(this.pessoa)){
			JsfUtil.addWarnMessage("Erro: Não foi possível concluir.");
			valido = false;
		}
		
		
		if(isProcessoNaoConcluido(solicitacaoTransferenciaTitularidade.getNumProcesso())){
			JsfUtil.addWarnMessage("Este processo ainda não foi concluído.");
			valido = false;
		}else{

			boolean isAtoValido = false;
			
			for (AtoAmbiental atoAmbiental : this.listaAtoAmbiental) {
				if(atoAmbiental.isRowSelect()){
					isAtoValido = true;
					break;
				}else{
					isAtoValido = false;
				}			
			}
			
			if(!isAtoValido){
				JsfUtil.addWarnMessage("Selecione o(s) ato(s) ambiental(is) a ser(em) transferido(s):");
				valido = false;
			}
		}
		
		return valido;
	}
	
	public List<AtoAmbiental> getListaAtoAmbiental() {
		return listaAtoAmbiental;
	}

	public void setListaAtoAmbiental(List<AtoAmbiental> listaAtoAmbiental) {
		this.listaAtoAmbiental = listaAtoAmbiental;
	}

	public SolicitacaoAdministrativo getSolicitacaoTransferenciaTitularidade() {
		return solicitacaoTransferenciaTitularidade;
	}

	public void setSolicitacaoTransferenciaTitularidade(SolicitacaoAdministrativo solicitacaoTransferenciaTitularidade) {
		this.solicitacaoTransferenciaTitularidade = solicitacaoTransferenciaTitularidade;
	}

	public Integer getIndTipoPessoa() {
		return indTipoPessoa;
	}
	
	public String getNomeRazaoPessoaString() {
		String label = "";
		if(!Util.isNullOuVazio(pessoa)){
			if(!Util.isNullOuVazio(pessoa.getPessoaFisica())){
				label = "Nome: "+pessoa.getPessoaFisica().getNomPessoa();
			}else if(!Util.isNullOuVazio(pessoa.getPessoaJuridica())){
				label = "Razão Social: "+pessoa.getPessoaJuridica().getNomRazaoSocial();
			}
		}
		return label;
	}

	public void setIndTipoPessoa(Integer indTipoPessoa) {
		this.indTipoPessoa = indTipoPessoa;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public List<Empreendimento> getListaEmpreendimentos() {
		return listaEmpreendimentos;
	}

	public void setListaEmpreendimentos(List<Empreendimento> listaEmpreendimentos) {
		this.listaEmpreendimentos = listaEmpreendimentos;
	}


	public boolean isExibirAtos() {
		return exibirAtos;
	}


	public void setExibirAtos(boolean isExibirAtos) {
		this.exibirAtos = isExibirAtos;
	}

	public boolean isVisualizar() {
		return isVisualizarMode;
	}

	public void setVisualizarMode(boolean isVisualizarMode) {
		this.isVisualizarMode = isVisualizarMode;
	}


}
