package br.gov.ba.seia.controller;

import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirMensagem001;
import static br.gov.ba.seia.util.MensagemLacFceUtil.exibirMensagem005;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicDocApensado;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalCaracterizacaoAtmosferica;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalDestinacaoFinal;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalEquipamentoControle;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalMedidaControleEmissao;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalOrigemAgua;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalSilosArmazen;
import br.gov.ba.seia.entity.ClassificacaoResiduo;
import br.gov.ba.seia.entity.ClassificacaoResiduoCaracterizacaoAmbiental;
import br.gov.ba.seia.entity.DestinacaoFinal;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.EquipamentoControle;
import br.gov.ba.seia.entity.FormacaoProfissional;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MedidaControleEmissao;
import br.gov.ba.seia.entity.OperacaoDesenvolvidaSilosArmazen;
import br.gov.ba.seia.entity.OrigemAguaTipoConcessionaria;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaCnae;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.SilosArmazen;
import br.gov.ba.seia.entity.SilosArmazensCaracterizacaoAtmosferica;
import br.gov.ba.seia.entity.SilosArmazensImovel;
import br.gov.ba.seia.entity.SilosArmazensLancamentoEfluente;
import br.gov.ba.seia.entity.SilosArmazensOperacaoDesenvolvida;
import br.gov.ba.seia.entity.SilosArmazensOrigemAgua;
import br.gov.ba.seia.entity.SilosArmazensResponsavelTecnico;
import br.gov.ba.seia.entity.SilosArmazensSistemaSeguranca;
import br.gov.ba.seia.entity.SilosArmazensSistemaTratamentoAgua;
import br.gov.ba.seia.entity.SilosArmazensTipoCombustivel;
import br.gov.ba.seia.entity.SilosArmazensUnidadeArmazenadora;
import br.gov.ba.seia.entity.SistemaSegurancaSilosArmazen;
import br.gov.ba.seia.entity.SistemaTratamentoAgua;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoArmazem;
import br.gov.ba.seia.entity.TipoCombustivelSiloArmazen;
import br.gov.ba.seia.entity.TipoConcessionaria;
import br.gov.ba.seia.entity.TipoEspecieArmazem;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DestinacaoFinalEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.EquipamentoControleEnum;
import br.gov.ba.seia.enumerator.MedidaControleEmissaoEnum;
import br.gov.ba.seia.enumerator.OperacaoDesenvolvidaSilosArmazenEnum;
import br.gov.ba.seia.enumerator.SilosArmazensOrigemAguaEnum;
import br.gov.ba.seia.enumerator.SilosArmazensSistemaSegurancaEnum;
import br.gov.ba.seia.enumerator.SistemaTratamentoAguaEnum;
import br.gov.ba.seia.enumerator.TelaDestinoEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.enumerator.TipoCombustivelSiloArmazenEnum;
import br.gov.ba.seia.enumerator.TipoDocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoTelefoneEnum;
import br.gov.ba.seia.facade.SilosArmazensFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

@ViewScoped
@Named("silosArmazensController")
public class SilosArmazensController {

	@EJB
	private SilosArmazensFacade silosArmazensFacade;
	
	@EJB
	private ImovelRuralService imovelRuralService;
	
	private static final Integer ABA_DADOS_BASICOS = 0;
	
	private static final Integer ABA_UNIDADE_ARMAZENADORA = 1;
	
	private static final Integer ABA_CARACTERIZACAO_AMBIENTAL = 2;
	
	private static final Integer ABA_TELA_SEGURANCA = 3;
	
	private static final Integer ABA_TELA_DOCUMENTOS_ESTUDOS = 4;
	
	private int activeTab;
	
	private Boolean primeiraDeclaracao;
	
	private Boolean segundaDeclaracao;
	
	private Boolean terceiraDeclaracao;
	
	private static String URL = "/paginas/manter-atividade-nao-sujeita-licenciamento/silos_armazens/silos_armazens_abas.xhtml";

	private CadastroAtividadeNaoSujeitaLic cadastro;
	
	private Empreendimento empreendimento;
	
	private Pessoa requerente ;
	
	private PessoaFisica requerentePessoaFisica;
	
	private PessoaJuridica requerentePessoaJuridica;
	
	private SilosArmazen silosArmazen;
	
	private Endereco enderecoCorrespondencia; 
	
	private List<FormacaoProfissional> listaFormacaoProfissional;
	
	private String numSicar;
	
	private SilosArmazensImovel silosArmazensImovelRural;
	
	private List<SilosArmazensImovel> silosArmazensImovelRuralList = new ArrayList<SilosArmazensImovel>();
	
	private SilosArmazensImovel excluirArmazensImovelRural;
	
	private List<OperacaoDesenvolvidaSilosArmazen> operacaoDesenvolvidaSilosArmazenList;
	
	private List<TipoCombustivelSiloArmazen> tipoCombustivelSiloArmazensList;
	
	private SilosArmazensUnidadeArmazenadora silosArmazensUnidadeArmazenadora = new SilosArmazensUnidadeArmazenadora();
	
	private List<TipoEspecieArmazem> tipoEspecieArmazems;
	
	private TipoEspecieArmazem tipoEspecieArmazem;
	
	private List<TipoArmazem> tipoArmazems = new ArrayList<TipoArmazem>();
	
	private LocalizacaoGeografica excluirLocalizacaoUnidade;
	
	private SilosArmazensUnidadeArmazenadora excluirArmazensUnidadeArmazenadora;
	
	private CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen = new CaracterizacaoAmbientalSilosArmazen();
	
	private CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAgua = new CaracterizacaoAmbientalOrigemAgua();
	
	private List<SilosArmazensOrigemAgua> silosArmazensOrigemAguas;
	
	private CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAguaPoco = new CaracterizacaoAmbientalOrigemAgua();
	
	private CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAguaRecurso = new CaracterizacaoAmbientalOrigemAgua();
	
	private CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAguaConcessionaria = new CaracterizacaoAmbientalOrigemAgua();

	private List<SistemaTratamentoAgua> sistemaTratamentoAguaList;

	private List<SilosArmazensCaracterizacaoAtmosferica> silosArmazensCaracterizacaoAtmosfericaList;

	private List<EquipamentoControle> equipamentoControleList;
	
	private List<MedidaControleEmissao> medidaControleEmissaoList;

	private List<ClassificacaoResiduo> classificacaoResiduoList;

	private List<DestinacaoFinal> destinacaoFinalList;
	
	private SilosArmazensLancamentoEfluente silosArmazensLancamentoEfluente = new SilosArmazensLancamentoEfluente();

	private List<SilosArmazensSistemaSeguranca> silosArmazensSistemaSegurancasList;
	
	private SistemaSegurancaSilosArmazen sistemaSegurancaSilosArmazen = new SistemaSegurancaSilosArmazen();
	
	private List<TipoConcessionaria> tipoConcessionarias;
	
	private boolean primeiraGridOrigem;
	
	private boolean segundaGridOrigem;
	
	private boolean terceiraGridOrigem;
	
	private List<CadastroAtividadeNaoSujeitaLicDocApensado> cadastroAtividadeNaoSujeitaLicDocApensadosList; 
	
	private List<CadastroAtividadeNaoSujeitaLicDocApensado> cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList = new ArrayList<CadastroAtividadeNaoSujeitaLicDocApensado>();
	
	private List<CadastroAtividadeNaoSujeitaLicDocApensado> cadastroAtividadeNaoSujeitaLicDocApensadosEstudosList = new ArrayList<CadastroAtividadeNaoSujeitaLicDocApensado>();
	
	private CadastroAtividadeNaoSujeitaLicDocApensado cadastroAtividadeNaoSujeitaLicDocApensado;
	
	private Boolean isUnidadeEditavel;
	
	private Boolean editarUnidadeArmazenadora;
	
	private Boolean isCarteiraCPM;
	
	private Boolean isVisivel;
	
	private Boolean temImovelRural;
	
	private boolean isNovaInstanciaReponsalvelTecnico;
	
	@PostConstruct
	public void init(){
		 
		try {
			this.requerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa();
			
			if(isNovoCadastro()){
					
				if(verificarRequerente(requerente)){
					
					inicializar();
				}
				
			}else if(isEdicaoCadastro()){
				
				// PEGA O CADASTRO DA QUE VEM DA TELA DE CONSULTA
					
				CadastroAtividadeNaoSujeitaLic cadastro = ContextoUtil.getContexto().getCadastro();

				isVisivel = cadastro.isVisualizar();
				
				inicializarEdicao(cadastro);
				
			}
		
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void poll() {
		RequestContext.getCurrentInstance().execute("dadosBasicosPoll.stop();");

		String msg = ContextoUtil.getContexto().getUpdateMessage();
		Boolean sucesso = ContextoUtil.getContexto().getSucessMessage();
		if(!Util.isNullOuVazio(msg)){
			if(sucesso){
				MensagemUtil.sucesso(msg);
			} 
			else {
				MensagemUtil.alerta(msg);
			}
		}

		ContextoUtil.getContexto().setUpdateMessage(null);
		ContextoUtil.getContexto().setSucessMessage(null);
	}
	
	public void carregarSilosArmazens(){
		
		try {
			this.silosArmazen =	silosArmazensFacade.carregarSilosArmazens(cadastro);
			
			cadastro = this.silosArmazen.getIdeCadastroAtividadeNaoSujeitaLic();
			
			for(SilosArmazensResponsavelTecnico resp: silosArmazen.getSilosArmazensResponsavelTecnicos()){
				
				resp.getIdePessoaFisica().getPessoa().setTelefoneCollection(silosArmazensFacade.buscarTelefonesPorPessoa(resp.getIdePessoaFisica().getPessoa()));
				
			}
			
			carregarCaracterizacaoAmbientalSilosArmazen();
		
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarCaracterizacaoAmbientalSilosArmazen(){
		
		//caracterizacao
		for(CaracterizacaoAmbientalSilosArmazen caract:silosArmazen.getCaracterizacaoAmbientalSilosArmazens()){
			this.caracterizacaoAmbientalSilosArmazen = caract;
		}
		
		try {
			if(!Util.isNullOuVazio(cadastro)){
				
				cadastroAtividadeNaoSujeitaLicDocApensadosList = silosArmazensFacade.listarDocumentosApensados(cadastro);
			}
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	}
	
	public void carregarDadosRequerente(){
	
		requerente = new Pessoa(cadastro.getIdePessoaRequerente().getIdePessoa());
		
		requerente.setPessoaJuridica(silosArmazensFacade.obterPessoaJuridicaMontada(cadastro.getIdePessoaRequerente().getIdePessoa()));
		requerente.setPessoaFisica(silosArmazensFacade.obterPessoaFisicaMontada(cadastro.getIdePessoaRequerente().getIdePessoa()));
		
		this.empreendimento = cadastro.getIdeEmpreendimento();
	}
	
	public void carregarTecnicosResponsaveis(){
		
		try {
			getEmpreendimento().setResponsavelEmpreendimentoCollection(silosArmazensFacade.listarResponsaveisTecnicos(getEmpreendimento()));
			
			if(isExisteResponsavelPeloSilosArmazen()){
				for (SilosArmazensResponsavelTecnico silosArmazenlResponsavelTecnico : silosArmazen.getSilosArmazensResponsavelTecnicos()) {
					for (ResponsavelEmpreendimento responsavelEmpreendimento : getEmpreendimento().getResponsavelEmpreendimentoCollection()) {
						if(silosArmazenlResponsavelTecnico.getIdePessoaFisica().equals(responsavelEmpreendimento.getIdePessoaFisica())){
							responsavelEmpreendimento.setSelecionado(true);
							silosArmazenlResponsavelTecnico.setNumeroCarteiraConselho(silosArmazensFacade.obterNumeroCarteiraConselhoClasse(silosArmazenlResponsavelTecnico.getIdePessoaFisica().getPessoa()));
							isCarteiraCPM = true;
							break;
						}
					}
				}
			}
			
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarAbaDadosGerais(){
		
		carregarTecnicosResponsaveis();
		carregarEnderecoCorrespondencia();
		
		if(silosArmazen.getIndQueimaCombustivel()){
			
			carregarTipoCombustivelUtilizado();
			
			if(!Util.isNullOuVazio(silosArmazen.getSilosArmazensTipoCombustivels())){
				
				for(SilosArmazensTipoCombustivel silosTipo: silosArmazen.getSilosArmazensTipoCombustivels()){
					for(TipoCombustivelSiloArmazen tipo: tipoCombustivelSiloArmazensList){
					
						
						if(silosTipo.getTipoCombustivelSiloArmazen().getIdeTipoCombustivelSiloArmazens() == tipo.getIdeTipoCombustivelSiloArmazens()){
							tipo.setIndSelecionado(true);
							
							if(silosTipo.getTipoCombustivelSiloArmazen().getIdeTipoCombustivelSiloArmazens() == TipoCombustivelSiloArmazenEnum.MADEIRA.getId()){
								
								for(TipoCombustivelSiloArmazen tipoAux: tipo.getTipoCombustivelSiloArmazensAuxiliar()){
									for(SilosArmazensTipoCombustivel silosTipoFilho: silosArmazen.getSilosArmazensTipoCombustivels()){
										
										if(silosTipoFilho.getTipoCombustivelSiloArmazen().getIdeTipoCombustivelSiloArmazens() == tipoAux.getIdeTipoCombustivelSiloArmazens()){
											tipoAux.setIndSelecionado(true);
											tipoAux.setNumeroRaf(silosTipoFilho.getNumRaf());
										}
									}
									
								}
							}
						}
					}
				}
				
			}
		}
		
		if(!Util.isNullOuVazio(silosArmazen.getSilosArmazensOperacaoDesenvolvidas())){
			
			carregarOperaçãoDesensolvida();
			
			for(SilosArmazensOperacaoDesenvolvida silosOp : silosArmazen.getSilosArmazensOperacaoDesenvolvidas()){
				
				for(OperacaoDesenvolvidaSilosArmazen op: this.operacaoDesenvolvidaSilosArmazenList){
					
					if(silosOp.getOperacaoDesenvolvidaSilosArmazen().getIdeOperacaoDesenvolvidaSilosArmazens() == op.getIdeOperacaoDesenvolvidaSilosArmazens()){
						op.setIndSelecionado(true);
						
						if(silosOp.getOperacaoDesenvolvidaSilosArmazen().getIdeOperacaoDesenvolvidaSilosArmazens() == OperacaoDesenvolvidaSilosArmazenEnum.BENEFICIAMENTO.getId()){
							
							for(OperacaoDesenvolvidaSilosArmazen opAux:op.getOperacaoDesenvolvidaSilosArmazenAuxiliar()){
								
								for(SilosArmazensOperacaoDesenvolvida silosFilho : silosArmazen.getSilosArmazensOperacaoDesenvolvidas()){
									
									if(silosFilho.getOperacaoDesenvolvidaSilosArmazen().getIdeOperacaoDesenvolvidaSilosArmazens() == opAux.getIdeOperacaoDesenvolvidaSilosArmazens()){
										
										opAux.setIndSelecionado(true);
									}
								}
								
							}
							
						}
					}
				}
			}
		}
	}
	
	public Boolean verificarRequerente(Pessoa requerente) throws Exception{
		
		if(!Util.isNullOuVazio(requerente)){
			
			Integer ideEndereco = silosArmazensFacade.buscarEnderecoPorPessoa(requerente).getIdeEndereco().getIdeEndereco();
			Endereco end = silosArmazensFacade.buscarEnderecoPorPessoa(ideEndereco);

			if(!Util.isNullOuVazio(requerente.getPessoaJuridica())){
				
				this.requerente.getPessoaJuridica().setPessoaJuridicaCnaeCollection(silosArmazensFacade.buscaPessoaJuridicaCnaePorPessoaJuridica(requerente.getPessoaJuridica()));
				this.requerente.getPessoaJuridica().getPessoa().setTelefoneCollection(silosArmazensFacade.buscarTelefonesPorPessoa(requerente));
				this.requerente.getPessoaJuridica().getPessoa().setEndereco(end);
				this.requerente.getPessoaJuridica().setRepresentanteLegalCollection(silosArmazensFacade.listarRepresentanteLegalPorPessoaJuridica(requerente.getPessoaJuridica()));
				
				if(!Util.isNullOuVazio(this.requerente.getPessoaJuridica().getRepresentanteLegalCollection())){
					
					for(RepresentanteLegal repre : this.requerente.getPessoaJuridica().getRepresentanteLegalCollection()){
						
						repre.setIdePessoaFisica(silosArmazensFacade.carregarPessoaFisicaGet(repre.getIdePessoaFisica().getIdePessoaFisica()));
						repre.getIdePessoaFisica().getPessoa().setTelefoneCollection(silosArmazensFacade.buscarTelefonesPorPessoa(repre.getIdePessoaFisica().getPessoa()));
					}
				}
				requerente.setPessoaFisica(null);
			}else if(!Util.isNullOuVazio(requerente.getPessoaFisica())){
				this.requerente.getPessoaFisica().getPessoa().setTelefoneCollection(silosArmazensFacade.buscarTelefonesPorPessoa(requerente));
				this.requerente.getPessoaFisica().getPessoa().setEndereco(end);
				requerente.setPessoaJuridica(null);
			}
			
		}else{
			return false;
		}
		
		return true;
	}
	
	public Boolean verificarCnae(Pessoa requerente){
		
		if(!Util.isNullOuVazio(requerente.getPessoaJuridica())){
			
			try {
				requerente.getPessoaJuridica().setPessoaJuridicaCnaeCollection(silosArmazensFacade.buscaPessoaJuridicaCnaePorPessoaJuridica(requerente.getPessoaJuridica()));
			} catch (Exception e) {
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			
			for(PessoaJuridicaCnae cnae: requerente.getPessoaJuridica().getPessoaJuridicaCnaeCollection()){
				
				if("52.11-7/01".equals(cnae.getIdeCnae().getCodCnae()) || "52.11-7/99".equals(cnae.getIdeCnae().getCodCnae())){
					return true;
				}
			}
			
			JsfUtil.addErrorMessage("O CNAE cadastrado não corresponde a silos e armazéns.");
		}
		
		return false; 
	}
	
	public String validarDeclaracoes(){
		
		boolean validar = true;
		
		if(!this.primeiraDeclaracao){
			validar = false;
		}
		
		if(!this.segundaDeclaracao){
			validar = false;
		}
		
		if(!this.terceiraDeclaracao){
			validar = false;
		}
		
		if(validar){
			return URL + "?faces-redirect=true";
		}else{
			JsfUtil.addWarnMessage("É obrigatório a seleção da(s) opção(ões) na tela de Instrução do Cadastro.");
		}
		
		return "";
	}

	public void carregarEnderecoCorrespondencia(){
		try {
			enderecoCorrespondencia = silosArmazensFacade.carregarEnderecoCorrespondencia(getEmpreendimento());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o endereço de correspondência do Empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}
	
	private void adicionarResponsavelTecnico(ResponsavelEmpreendimento responsavelTecncio) {
		try {
			if (!isExisteResponsavelPeloSilosArmazen()) {
				silosArmazen.setSilosArmazensResponsavelTecnicos(new ArrayList<SilosArmazensResponsavelTecnico>());
				isNovaInstanciaReponsalvelTecnico = true;
			}else{
				
				isNovaInstanciaReponsalvelTecnico = false;
			}
			
			SilosArmazensResponsavelTecnico respTec = new SilosArmazensResponsavelTecnico(silosArmazen, responsavelTecncio);
			if (!silosArmazen.getSilosArmazensResponsavelTecnicos().contains(respTec)) {
				respTec.setNumeroCarteiraConselho(silosArmazensFacade.obterNumeroCarteiraConselhoClasse(responsavelTecncio.getIdePessoaFisica().getPessoa()));
				responsavelTecncio.getIdePessoaFisica().getPessoa().setTelefoneCollection(silosArmazensFacade.buscarTelefonesPorPessoa(responsavelTecncio.getIdePessoaFisica().getPessoa()));
				
				if (Util.isNullOuVazio(respTec.getNumeroCarteiraConselho())) {
					respTec.setNumeroCarteiraConselho("Não informado");
					isCarteiraCPM = false;
					MensagemUtil.alerta("É obrigatório o cadastrado do número da Carteira de identidade de conselho de classe do Responsável Técnico pela atividade de Silos e Armazéns sem guia de utilização. Atualize os documentos do Responsável Técnico no Cadastro de Pessoa Física do SEIA e tente novamente.");
				} 
				else { 
					exibirMensagem001();
					isCarteiraCPM = true;
				}
				respTec.setListaFormacaoProfissional(getlistaFormacaoProfissional());
				silosArmazen.getSilosArmazensResponsavelTecnicos().add(respTec);
			}
			else {
				MensagemUtil.alerta("O responsável técnico já foi adicionado.");
			}
		}
		catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar") + " o número da " + Util.getString("geral_lbl_carteira_conselho_classe") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void changeCheckResponsavelTecnico(ResponsavelEmpreendimento responsavelEmpreendimento){
		if(responsavelEmpreendimento.isSelecionado()){
			adicionarResponsavelTecnico(responsavelEmpreendimento);
		} 
		else {
			excluirResponsavelTecnico(responsavelEmpreendimento);
		}
		
		if(!isExisteResponsavelPeloSilosArmazen() || isNovaInstanciaReponsalvelTecnico){
			silosArmazen.setIndExisteComunidade(null);
			silosArmazen.setValAreaTotalConstruida(null);
			silosArmazen.setValAreaTotalTerreno(null);
			silosArmazen.setIndQueimaCombustivel(null);
			silosArmazen.setIndEmpreendimentoCaldeira(null);
			carregarOperaçãoDesensolvida();
			carregarTipoCombustivelUtilizado();
//			verificarEmpreendimentoTemImovelRural();
			listarImoveisParaEmpreendimentoSelecionado();
			
			Html.atualizar("formSilosArmazen:tabViewSilosArmazens");
		}
		
		if((!isExisteResponsavelPeloSilosArmazenSemCarteira() && isCarteiraCPM) || !isCarteiraCPM){
			
			//Html.executarJS("updateGridSilos();");
			Html.atualizar("formSilosArmazen:tabViewSilosArmazens:silosSicar");
			Html.atualizar("formSilosArmazen:groupBotoes");
		}
		limparGridOperacao();
	}
	
	private void excluirResponsavelTecnico(ResponsavelEmpreendimento responsavelEmpreendimento) {
		try {
			SilosArmazensResponsavelTecnico resp = getResponsavelTecnicoBy(responsavelEmpreendimento.getIdePessoaFisica());
			silosArmazen.getSilosArmazensResponsavelTecnicos().remove(resp);
			exibirMensagem005();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " o " + Util.getString("lbl_cpm_responsavel_tecnico") + ".");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

	}
	
	private SilosArmazensResponsavelTecnico getResponsavelTecnicoBy(PessoaFisica pf){
		for (SilosArmazensResponsavelTecnico responsavelTecnico : silosArmazen.getSilosArmazensResponsavelTecnicos()) {
			if(responsavelTecnico.getIdePessoaFisica().equals(pf)){
				return responsavelTecnico;
			}
		}
		return null;
	}
	
	private void listarFormacaoProfissional(){
		try {
			if(Util.isNullOuVazio(getListaFormacaoProfissional())){
				setListaFormacaoProfissional(silosArmazensFacade.listarFormacaoProfissional());
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de formação profissional.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private List<FormacaoProfissional> getlistaFormacaoProfissional() {
		listarFormacaoProfissional();
		List<FormacaoProfissional> lista = new ArrayList<FormacaoProfissional>();
		lista.addAll(listaFormacaoProfissional);
		return lista;
	}
	
	public void limparGridOperacao(){
		
		int i = 0;
		
		for(OperacaoDesenvolvidaSilosArmazen op : operacaoDesenvolvidaSilosArmazenList){
			
			if(!Util.isNullOuVazio(op.getIndSelecionado()) && op.getIndSelecionado()){
				i++;
			}
		}
		
		if(i == 0){
			Html.atualizar("formSilosArmazen:tabViewSilosArmazens:gridOperacao");
			
		}
	}
	
	public void prepararDialogIncluirImovel() {
		setNumSicar(new String());
		
		if(Util.isNullOuVazio(silosArmazen.getSilosArmazensImovelRurals())){
			silosArmazen.setSilosArmazensImovelRurals(new ArrayList<SilosArmazensImovel>());
		}
		silosArmazensImovelRural = new SilosArmazensImovel(silosArmazen);
	}
	
	public void buscarImovel() {
		try {
			ImovelRural imo = new ImovelRural();
			imo.setImovel(silosArmazensFacade.buscarImovelPorNumeroCar(new ImovelRural(numSicar)));
			
			if(!Util.isNullOuVazio(imo.getImovel())){
				
				if(!Util.isNull(silosArmazen.getSilosArmazensImovelRurals())){
					
					int count = 0;
					
					for(SilosArmazensImovel imovel : silosArmazen.getSilosArmazensImovelRurals()){
						ImovelRural ir = new ImovelRural();
						
						ir = imovelRuralService.carregarById(imovel.getIdeImovel().getIdeImovel());
						
						if(!Util.isNullOuVazio(imovel.getIdeImovel().getImovelRural())){
							if(numSicar.equals(imovel.getIdeImovel().getImovelRural().getImovelRuralSicar().getNumSicar())){
								JsfUtil.addWarnMessage("O imóvel informado já foi cadastrado!");
								count++;
								break;
							}
						}
					}
					
					if(count == 0){
						
						silosArmazensImovelRural.setIdeImovel(imo.getImovel());
						silosArmazensImovelRural.setIndNumCar(true);
					}
				}
				
			}else {
				
				JsfUtil.addErrorMessage("Número CAR inválido.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarTipoCombustivel(ValueChangeEvent event){
		
		if((Boolean)event.getNewValue()){
			
			if (Util.isNullOuVazio(tipoCombustivelSiloArmazensList)) {
				
				carregarTipoCombustivelUtilizado();
			}
			
		}
		
	}
	
	public void carregarOperaçãoDesensolvida(){
		
		try {
			this.operacaoDesenvolvidaSilosArmazenList =  silosArmazensFacade.listarOperacaoDesenvolvidaSilosArmazen();
		
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	} 
	
	public void carregarTipoCombustivelUtilizado(){
		
		try {
			this.tipoCombustivelSiloArmazensList =  silosArmazensFacade.listarTipoCombustivelSiloArmazen();
			
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void valueChangeOperacaoOutros(AjaxBehaviorEvent event) {
		
		OperacaoDesenvolvidaSilosArmazen op =(OperacaoDesenvolvidaSilosArmazen)	event.getComponent().getAttributes().get("operacao");
	
		
		if(op.getIndSelecionado()){

			if("Outro".equals(op.getNomOperacaoDesevolvida())){
				JsfUtil.addWarnMessage("Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.");
			}	
		}else{
			if(op.getIdeOperacaoDesenvolvidaSilosArmazens() == 5){
				for(OperacaoDesenvolvidaSilosArmazen aux : op.getOperacaoDesenvolvidaSilosArmazenAuxiliar()){
					aux.setIndSelecionado(false);
				}
			}
		}
	}
	
	public void valueChangeCombustivelUtilizadoOutros(AjaxBehaviorEvent event){
		
		TipoCombustivelSiloArmazen comb = (TipoCombustivelSiloArmazen)event.getComponent().getAttributes().get("combustivel");
		
		
		if (!Util.isNullOuVazio(comb)) {
			
			if(!comb.getIndSelecionado()
					&& comb.getIdeTipoCombustivelSiloArmazens() == 4){
				for(TipoCombustivelSiloArmazen aux : comb.getTipoCombustivelSiloArmazensAuxiliar()){
					aux.setIndSelecionado(false);
					aux.setNumeroRaf(null);
				}
				
/*				if(!Util.isNullOuVazio(cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList)){
					for(CadastroAtividadeNaoSujeitaLicDocApensado doc : cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList){
						if(DocumentoObrigatorioEnum.CERTIFICADO_MADEIRA_NATIVA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
							if(!Util.isNullOuVazio(doc.getUrlDocumento())){
								MensagemUtil.alerta("Essa alteração impactará os dados na aba de Documentos e Estudos.");
								break;
							}
						}else if(DocumentoObrigatorioEnum.CERTIFICADO_MADEIRA_EXOTICA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
							if(!Util.isNullOuVazio(doc.getUrlDocumento())){
								MensagemUtil.alerta("Essa alteração impactará os dados na aba de Documentos e Estudos.");
								break;
							}
						}
					}
				}*/
				
			}
			
			if (comb.getIndSelecionado()
					&& "Outro".equals(comb.getNomTipoCombustivel())) {
				JsfUtil.addWarnMessage("Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.");
			} 
		}
	}
	
	
	public void limparCheckCombustiveis(){
		if(!Util.isNullOuVazio(silosArmazen.getIndQueimaCombustivel())){
			if(silosArmazen.getIndQueimaCombustivel()){
				for(TipoCombustivelSiloArmazen aux : tipoCombustivelSiloArmazensList){
					if(!Util.isNullOuVazio(aux.getIndSelecionado())){
						if(aux.getIndSelecionado()
								&& aux.getIdeTipoCombustivelSiloArmazens() == 4){
							for(TipoCombustivelSiloArmazen auxFilho : aux.getTipoCombustivelSiloArmazensAuxiliar()){
								auxFilho.setIndSelecionado(false);
								auxFilho.setNumeroRaf(null);
							}
/*							if(!Util.isNullOuVazio(cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList)){
								for(CadastroAtividadeNaoSujeitaLicDocApensado doc : cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList){
									if(DocumentoObrigatorioEnum.CERTIFICADO_MADEIRA_NATIVA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
										if(!Util.isNullOuVazio(doc.getUrlDocumento())){
											MensagemUtil.alerta("Essa alteração impactará os dados na aba de Documentos e Estudos.");
											break;
										}
									}else if(DocumentoObrigatorioEnum.CERTIFICADO_MADEIRA_EXOTICA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
										if(!Util.isNullOuVazio(doc.getUrlDocumento())){
											MensagemUtil.alerta("Essa alteração impactará os dados na aba de Documentos e Estudos.");
											break;
										}
									}
								}
							}*/
						}
					}
					aux.setIndSelecionado(false);
				}
			}
		}
	}
	
	
	public void limparInputCombustivel(){
		
		if(!Util.isNullOuVazio(tipoCombustivelSiloArmazensList)){

			for(TipoCombustivelSiloArmazen comb : tipoCombustivelSiloArmazensList){
				
				if(!Util.isNullOuVazio(comb.getTipoCombustivelSiloArmazensAuxiliar())){
					
					for(TipoCombustivelSiloArmazen combAux:comb.getTipoCombustivelSiloArmazensAuxiliar()){
						
						
						if(Util.isNullOuVazio(combAux.getIndSelecionado()) || !combAux.getIndSelecionado()){
							combAux.setNumeroRaf(null);
							Html.atualizar("formSilosArmazen:tabViewSilosArmazens:gridCombustivel");
						}
						
						/*if(TipoCombustivelSiloArmazenEnum.MADEIRA_NATIVA.getId() == combAux.getIdeTipoCombustivelSiloArmazens() && !Util.isNullOuVazio(combAux.getIndSelecionado())){
							if(!combAux.getIndSelecionado()){
								
								if(!Util.isNullOuVazio(cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList)){
									for(CadastroAtividadeNaoSujeitaLicDocApensado doc : cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList){
										if(DocumentoObrigatorioEnum.CERTIFICADO_MADEIRA_NATIVA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()) || DocumentoObrigatorioEnum.COMPROVANTE_ATUALIZADO_RAF.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
											if(!Util.isNullOuVazio(doc.getUrlDocumento())){
												ContextoUtil.getContexto().setUpdateMessage("Essa alteração impactará os dados na aba de Documentos e Estudos.");
												ContextoUtil.getContexto().setSucessMessage(false);
												break;
											}
										}
									}
								}
							}
						}
						
						if(TipoCombustivelSiloArmazenEnum.MADEIRA_EXOTICA.getId() == combAux.getIdeTipoCombustivelSiloArmazens() && !Util.isNullOuVazio(combAux.getIndSelecionado())){
							if(!combAux.getIndSelecionado()){
								
								if(!Util.isNullOuVazio(cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList)){
									for(CadastroAtividadeNaoSujeitaLicDocApensado doc : cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList){
										if(DocumentoObrigatorioEnum.CERTIFICADO_MADEIRA_EXOTICA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()) || DocumentoObrigatorioEnum.COMPROVANTE_ATUALIZADO_RAF.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
											if(!Util.isNullOuVazio(doc.getUrlDocumento())){
												ContextoUtil.getContexto().setUpdateMessage("Essa alteração impactará os dados na aba de Documentos e Estudos.");
												ContextoUtil.getContexto().setSucessMessage(false);												
												break;
											}
										}
									}
								}
							}
						}*/
					}
				}
			}
		}
		
	}
	
	public void verificarUsoAgua(){
		if(!caracterizacaoAmbientalSilosArmazen.getIndUtilizaAgua()){
			
			caracterizacaoAmbientalOrigemAgua.setSilosArmazensOrigemAguaList(new ArrayList<SilosArmazensOrigemAgua>());
			caracterizacaoAmbientalOrigemAguaPoco.setIndSelecionado(null);
			caracterizacaoAmbientalOrigemAguaPoco.setNumDocumentoPortaria(null);
			caracterizacaoAmbientalOrigemAguaPoco.setNumDocumentoDispensa(null);
			caracterizacaoAmbientalOrigemAguaRecurso.setIndSelecionado(null);
			caracterizacaoAmbientalOrigemAguaRecurso.setNumDocumentoDispensa(null);
			caracterizacaoAmbientalOrigemAguaRecurso.setNumDocumentoPortaria(null);
			caracterizacaoAmbientalOrigemAguaConcessionaria.setTipoConcessionariasList(new ArrayList<TipoConcessionaria>());
			primeiraGridOrigem = false;
			segundaGridOrigem = false;
			terceiraGridOrigem = false;
			
/*			if(!Util.isNullOuVazio(cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList)){
				for(CadastroAtividadeNaoSujeitaLicDocApensado doc : cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList){
					if(DocumentoObrigatorioEnum.COPIA_PORTARIA_OUTORGA_CAPTACAO_A_SER_REALIZADA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
						if(!Util.isNullOuVazio(doc.getUrlDocumento())){
							MensagemUtil.alerta("Essa alteração impactará os dados na aba de Documentos e Estudos.");
							break;
						}
					}else if(DocumentoObrigatorioEnum.COPIA_PORTARIA_OUTORGA_CAPTACAO_A_SER_REALIZADA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
						if(!Util.isNullOuVazio(doc.getUrlDocumento())){
							MensagemUtil.alerta("Essa alteração impactará os dados na aba de Documentos e Estudos.");
							break;
						}
					}
				}
			}*/
			
		}
		Html.atualizar("formSilosArmazen:tabViewSilosArmazens:gridSelectOrigem");
		Html.atualizar("formSilosArmazen:tabViewSilosArmazens:selectOrigem");
		Html.atualizar("formSilosArmazen:tabViewSilosArmazens:gridOrigem");
	}
	
	public void valueChangeOperacaoBeneficiamentoOutros(ValueChangeEvent event){
		
		Boolean evento = (Boolean)event.getNewValue();
		
		OperacaoDesenvolvidaSilosArmazen operacao = (OperacaoDesenvolvidaSilosArmazen) ((UIInput) event.getSource()).getAttributes().get("outrosOP");
		
		if("Outro".equals(operacao.getNomOperacaoDesevolvida()) && !Util.isNullOuVazio(evento)){
			
			if(evento){
				JsfUtil.addWarnMessage("Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.");
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void valueChangeSistemaTratamentoOutros(ValueChangeEvent event){
		
		List<SistemaTratamentoAgua> tratamento = (List<SistemaTratamentoAgua>) event.getNewValue();
		
		List<SistemaTratamentoAgua> tratamentoAntigo = (List<SistemaTratamentoAgua>) event.getOldValue();
		
		boolean isOutrosAntigo = false;
		
		boolean isOutrosNovo = false;
		
		if(!Util.isNullOuVazio(tratamentoAntigo)){
			for(SistemaTratamentoAgua sisAntigo : tratamentoAntigo){
				
				if(sisAntigo.getIndAtivo() && "Outros".equals(sisAntigo.getNomSistemaTratamentoAgua())){
					isOutrosAntigo = true;
					break;
				}
			}
		}
		
		if(!Util.isNullOuVazio(tratamento)){
			for(SistemaTratamentoAgua sis : tratamento){
				
				if(sis.getIndAtivo() && "Outros".equals(sis.getNomSistemaTratamentoAgua())){
					isOutrosNovo = true;
					break;
				}
			}
		}
		
		if((!isOutrosAntigo) && (isOutrosNovo)){
			JsfUtil.addWarnMessage("Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void valueChangeEquipamentoControleOutros(ValueChangeEvent event){
			

		List<EquipamentoControle> equipamento = (List<EquipamentoControle>) event.getNewValue();
		
		List<EquipamentoControle> equipamentoAntigo = (List<EquipamentoControle>) event.getOldValue();
		
		boolean isOutrosAntigo = false;
		
		boolean isOutrosNovo = false;
		
		if(!Util.isNullOuVazio(equipamentoAntigo)){
			for(EquipamentoControle eqAntigo : equipamentoAntigo){
				
				if(eqAntigo.getIndAtivo() && "Outros".equals(eqAntigo.getNomEquipamentoControle())){
					isOutrosAntigo = true;
					break;
				}
			}
		}
		
		if(!Util.isNullOuVazio(equipamento)){
			for(EquipamentoControle eq : equipamento){
				
				if(eq.getIndAtivo() && "Outros".equals(eq.getNomEquipamentoControle())){
					isOutrosNovo = true;
					break;
				}
			}
		}
			
		if((!isOutrosAntigo) && (isOutrosNovo)){
			JsfUtil.addWarnMessage("Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.");
		}
			
	}
	
	@SuppressWarnings("unchecked")
	public void valueChangeMedidaOutros(ValueChangeEvent event){
		
		List<MedidaControleEmissao> equipamento = (List<MedidaControleEmissao>) event.getNewValue();
		
		List<MedidaControleEmissao> equipamentoAntigo = (List<MedidaControleEmissao>) event.getOldValue();
		
		boolean isOutrosAntigo = false;
		
		boolean isOutrosNovo = false;
		
		if(!Util.isNullOuVazio(equipamentoAntigo)){
			for(MedidaControleEmissao medAntigo : equipamentoAntigo){
				
				if(medAntigo.getIndAtivo() && "Outros".equals(medAntigo.getNomMedidaControleEmissao())){
					isOutrosAntigo = true;
					break;
				}
			}
		}
		
		if(!Util.isNullOuVazio(equipamento)){
			for(MedidaControleEmissao med : equipamento){
				
				if(med.getIndAtivo() && "Outros".equals(med.getNomMedidaControleEmissao())){
					isOutrosNovo = true;
					break;
				}
			}
		}
			
		
		if((!isOutrosAntigo) && (isOutrosNovo)){
			JsfUtil.addWarnMessage("Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.");
		}
			
	}
	
	
	@SuppressWarnings("unchecked")
	public void valueChangeDestinacaoFinalOutros(ValueChangeEvent event){
			
			List<DestinacaoFinal> destinacao = (List<DestinacaoFinal>) event.getNewValue();
			
			List<DestinacaoFinal> destinacaoAntigo = (List<DestinacaoFinal>) event.getOldValue();
			
			boolean isOutrosAntigo = false;
			
			boolean isOutrosNovo = false;
			
			if(!Util.isNullOuVazio(destinacaoAntigo)){
				for(DestinacaoFinal destAntigo : destinacaoAntigo){
					
					if(destAntigo.getIndAtivo() && "Outros".equals(destAntigo.getNomDestinacaoFinal())){
						isOutrosAntigo = true;
						break;
					}
				}
			}
			
			if(!Util.isNullOuVazio(destinacao)){
				for(DestinacaoFinal dest : destinacao){
					
					if(dest.getIndAtivo() && "Outros".equals(dest.getNomDestinacaoFinal())){
						isOutrosNovo = true;
						break;
					}
				}
			}
			
			if((!isOutrosAntigo) && (isOutrosNovo)){
				JsfUtil.addWarnMessage("Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.");
			}
			
	}
	
	
	@SuppressWarnings("unchecked")
	public void valueChangeSistemaSegurancaOutros(ValueChangeEvent event){
		
		List<SilosArmazensSistemaSeguranca> sistema = (List<SilosArmazensSistemaSeguranca>) event.getNewValue();
		
		List<SilosArmazensSistemaSeguranca> sistemaAntigo = (List<SilosArmazensSistemaSeguranca>) event.getOldValue();
		
		boolean isOutrosAntigo = false;
		
		boolean isOutrosNovo = false;
		
		if(!Util.isNullOuVazio(sistemaAntigo)){
			for(SilosArmazensSistemaSeguranca sisAntigo : sistemaAntigo){
				
				if(sisAntigo.getIndAtivo() && "Outros".equals(sisAntigo.getNomSistemaSegurana())){
					isOutrosAntigo = true;
					break;
				}
			}
		}
		
		if(!Util.isNullOuVazio(sistema)){
			for(SilosArmazensSistemaSeguranca sis : sistema){
				
				if(sis.getIndAtivo() && "Outros".equals(sis.getNomSistemaSegurana())){
					isOutrosNovo = true;
					break;
				}
			}
		}
			
	
		if((!isOutrosAntigo) && (isOutrosNovo)){
			JsfUtil.addWarnMessage("Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.");
		}
			
	}
	
	public void valueChangeEfluente(ValueChangeEvent event){
		
		String value = (String)event.getNewValue();
		
		if("Portaria".equals(value)){
			silosArmazensLancamentoEfluente.setNumDocumentoDispensa(null);
		}else{
			silosArmazensLancamentoEfluente.setNumDocumentoPortaria(null);
		}
	}
	
	public Boolean validarOutros(){
		
		boolean retorno = true;

		if(!Util.isNullOuVazio(operacaoDesenvolvidaSilosArmazenList)){
			
		
			for(OperacaoDesenvolvidaSilosArmazen operacao: operacaoDesenvolvidaSilosArmazenList){
				
				if(!Util.isNullOuVazio(operacao.getIndSelecionado())){
					
					if((OperacaoDesenvolvidaSilosArmazenEnum.OUTROS.getId() == operacao.getIdeOperacaoDesenvolvidaSilosArmazens() 
							&& operacao.getIndSelecionado()) || (OperacaoDesenvolvidaSilosArmazenEnum.OUTROS_BENEFICIAMENTO.getId() == operacao.getIdeOperacaoDesenvolvidaSilosArmazens()
							&& operacao.getIndSelecionado())){
						
						retorno = false;
						break;
					}
					
					if(OperacaoDesenvolvidaSilosArmazenEnum.BENEFICIAMENTO.getId() == operacao.getIdeOperacaoDesenvolvidaSilosArmazens()){
						for(OperacaoDesenvolvidaSilosArmazen opFilho: operacao.getOperacaoDesenvolvidaSilosArmazenAuxiliar()){
							
							if(!Util.isNullOuVazio(opFilho.getIndSelecionado())){
								
								if((OperacaoDesenvolvidaSilosArmazenEnum.OUTROS_BENEFICIAMENTO.getId() == opFilho.getIdeOperacaoDesenvolvidaSilosArmazens()
										&& opFilho.getIndSelecionado())){
									retorno = false;
									break;
								}
							}
						}
					}
				}
			}
		}
		
		if(!Util.isNullOuVazio(tipoCombustivelSiloArmazensList)){
					
			for(TipoCombustivelSiloArmazen combustivel : tipoCombustivelSiloArmazensList){
				
				if(!Util.isNullOuVazio(combustivel.getIndSelecionado())){
					
				
					if(TipoCombustivelSiloArmazenEnum.OUTROS.getId() == combustivel.getIdeTipoCombustivelSiloArmazens()
							&& combustivel.getIndSelecionado()){
						
						retorno = false;
						break;
					}
				}
			}
		}
		
		if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen)){
				
			
				if(caracterizacaoAmbientalSilosArmazen.getIndSistemaTratamento()){
					
				
					for(SistemaTratamentoAgua tratamentoAgua: caracterizacaoAmbientalSilosArmazen.getSistemaTratamentoAguas()){
						
						if(SistemaTratamentoAguaEnum.OUTROS.getId() == tratamentoAgua.getIdeSistemaTratamentoAgua()){
							retorno = false;
							break;
						}
					}
				}
				
				if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getEquipamentoControles())){
					
					for(EquipamentoControle equipamento:caracterizacaoAmbientalSilosArmazen.getEquipamentoControles()){
						
						if(EquipamentoControleEnum.OUTROS.getId() == equipamento.getIdeEquipamentoControle()){
							retorno = false;
							break;
						}
					}
				}
				
				if(caracterizacaoAmbientalSilosArmazen.getIndMedidaControleEmissao()){
					
					if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getMedidaControleEmissaos())){
						
						for(MedidaControleEmissao medidaControle:caracterizacaoAmbientalSilosArmazen.getMedidaControleEmissaos()){
							
							if(MedidaControleEmissaoEnum.OUTROS.getId() == medidaControle.getIdeMedidaControleEmissao()){
								retorno = false;
								break;
							}
						}
					}
				}
				
				if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getDestinacaoFinals())){
					
					for(DestinacaoFinal destinacao : caracterizacaoAmbientalSilosArmazen.getDestinacaoFinals()){
						
						if(DestinacaoFinalEnum.OUTROS.getId() == destinacao.getIdeDestinacaoFinal()){
							retorno = false;
							break;
						}
					}
				}
				
		}
		
		if(!Util.isNullOuVazio(sistemaSegurancaSilosArmazen.getSilosArmazensSistemaSegurancas())){
			
			for(SilosArmazensSistemaSeguranca sistemaSeguranca:sistemaSegurancaSilosArmazen.getSilosArmazensSistemaSegurancas()){
				
				if(SilosArmazensSistemaSegurancaEnum.OUTROS.getId() == sistemaSeguranca.getIdeSilosArmazensSistemaSeguranca()){
					retorno = false;
					break;
				}
			}
		}
		
		if(!retorno){
			JsfUtil.addWarnMessage("Favor entrar em contato com o INEMA através do e-mail atendimento.seia@inema.ba.gov.br para que o cadastro da opção desejada seja realizado.");
		}
		
		return retorno;
		
	}
	
	public void adicionarSicar(){
		
		
		silosArmazen.getSilosArmazensImovelRurals().add(silosArmazensImovelRural);
		
		Html.atualizar("formSilosArmazen:tabViewSilosArmazens:silosSicar");
		Html.esconder("dialogIncluirImovelCARSilos");
		JsfUtil.addSuccessMessage("Imóvel cadastrado com sucesso!");
	}
	
	
	public void excluirSicar(){
		for(int x = 0; x < this.silosArmazen.getSilosArmazensImovelRurals().size() ; x++){
			if(this.silosArmazen.getSilosArmazensImovelRurals().get(x) == this.excluirArmazensImovelRural){
				this.silosArmazen.getSilosArmazensImovelRurals().remove(x);
				break;
			}
		}
		if(this.silosArmazen.getSilosArmazensImovelRurals().size() == 0){
		
/*			if(!Util.isNullOuVazio(cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList)){
				for(CadastroAtividadeNaoSujeitaLicDocApensado doc : cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList){
					if(DocumentoObrigatorioEnum.AUTORIZACAO_PROPRIETARIO_SUPERFICIARIO.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
						if(!Util.isNullOuVazio(doc.getUrlDocumento())){
							ContextoUtil.getContexto().setUpdateMessage("Essa alteração impactará os dados na aba de Documentos e Estudos.");
							ContextoUtil.getContexto().setSucessMessage(false);
							break;
						}
					}else if(DocumentoObrigatorioEnum.AUTORIZACAO_PROPRIETARIO_SUPERFICIARIO.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
						if(!Util.isNullOuVazio(doc.getUrlDocumento())){
							ContextoUtil.getContexto().setUpdateMessage("Essa alteração impactará os dados na aba de Documentos e Estudos.");
							ContextoUtil.getContexto().setSucessMessage(false);
							break;
						}
					}
				}
			}*/
			
		}
		Html.atualizar("formSilosArmazen:tabViewSilosArmazens:silosSicar");
		Html.esconder("confirmarExclusaoImovelCar");
	}
	
	public boolean isExisteResponsavelPeloSilosArmazen(){
		return !Util.isNullOuVazio(silosArmazen.getSilosArmazensResponsavelTecnicos());
	}
	
	public boolean isExisteResponsavelPeloSilosArmazenSemCarteira(){
		
		boolean retorno = false;
		
		if(!Util.isNullOuVazio(silosArmazen.getSilosArmazensResponsavelTecnicos())){
			
			for(SilosArmazensResponsavelTecnico resp : silosArmazen.getSilosArmazensResponsavelTecnicos()){
				if(Util.isNullOuVazio(resp.getNumeroCarteiraConselho()) || "Não informado".equals(resp.getNumeroCarteiraConselho())){
					retorno = true;
					break;
				}
			}
		}else {
			retorno = true;
		}
		
		if(!retorno){
			isCarteiraCPM = true;
		}
		
		return retorno;
	}
	
	private boolean isNovoCadastro(){
		return Util.isNull(ContextoUtil.getContexto().getCadastro()) 
				&& !Util.isNullOuVazio(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente())
				&& !Util.isNullOuVazio(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa());
	}
	
	private boolean isEdicaoCadastro(){
		return !Util.isNull(ContextoUtil.getContexto().getCadastro());
	}
	
	private PessoaFisica getUsuarioRealizandoAcao() {
		return ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
	}
	
	private Boolean isEempreendimentoCadastroIncompleto(){
		try {
			CadastroAtividadeNaoSujeitaLic cadastro = silosArmazensFacade.buscarCadastroIncompletoPorPessoaEmpreendimento(requerente, this.empreendimento, TipoAtividadeNaoSujeitaLicenciamentoEnum.SILOS_E_ARMAZENS);
			
			if(!Util.isNullOuVazio(cadastro) && this.empreendimento.getIdeEmpreendimento().equals(cadastro.getIdeEmpreendimento().getIdeEmpreendimento())){
				inicializarEdicao(cadastro);
				return true;
			}
			
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return false;
	}
	
	public void inicializarEdicao(CadastroAtividadeNaoSujeitaLic cadastro) throws Exception{
		
		this.cadastro= cadastro;
		carregarSilosArmazens();
		carregarDadosRequerente();
		
		if(verificarRequerente(requerente)){
			if(!Util.isNullOuVazio(cadastro)){
				
				cadastro.setCadastroAtividadeNaoSujeitaLicStatus(silosArmazensFacade.listar(cadastro));
			}
			carregarAbaDadosGerais();
			carregarAbaCaracterizacao();
			carregarAbaSeguranca();
			carregarAbaDocumentosEstudos();
			buscarEmpreendimento(this.empreendimento);
//			carregarEmpreendimento(this.empreendimento);
		}
	}
	
	public TelaDestinoEnum getTelaDestionEnum(){
		return TelaDestinoEnum.SILOS_ARMAZENS;
	}
	
	public String getLabelTelefone(Telefone teleone){
		if(!TipoTelefoneEnum.FAX.getId().equals(teleone.getIdeTipoTelefone().getIdeTipoTelefone())){
 			return "Tel ";
		}
		return "";
	}
	
	public void carregarEmpreendimento(Empreendimento empreendimento) throws Exception{
		this.empreendimento = empreendimento;
		
		if(isEempreendimentoCadastroIncompleto()){
			Html.atualizar("formSilosArmazen:tabViewSilosArmazens");
			
		}else if(!Util.isNullOuVazio(this.empreendimento) && existeEnderecoLocalizacao(this.empreendimento) ){
			
			if(!Util.isNullOuVazio(this.silosArmazen)){
				
				inicializar();
				
				limparTela();
			}
			
			this.empreendimento.setResponsavelEmpreendimentoCollection(silosArmazensFacade.filtrarResponsaveisPorEmpreendimento(this.empreendimento));
			
			this.silosArmazen.getIdeCadastroAtividadeNaoSujeitaLic().setIdeEmpreendimento(this.empreendimento);
			this.silosArmazen.setIdeEmpreendimento(this.empreendimento);
			this.silosArmazen.setIndIndustrializacao(true);
			this.silosArmazen.setIndAceiteEmpreendimentoAreaProtegida(true);
			this.silosArmazen.setIndAceiteInstrucoes(true);
			this.silosArmazen.setIndAceiteDeclaracaoFinal(false);
			
			verificarEmpreendimentoTemImovelRural();
			
			if(Util.isNullOuVazio(this.empreendimento.getResponsavelEmpreendimentoCollection())){
				JsfUtil.addErrorMessage("Não existe Reponsável técnico cadastrado para o empreendimento, por favor efetue o cadastro!");
				Html.atualizar("formSilosArmazen:tabViewSilosArmazens:tabDadosBasicos");
				Html.atualizar("formSilosArmazen:tabViewSilosArmazens:gridTable");
				Html.atualizar("msg");
				
			}else{
				
				Html.atualizar("formSilosArmazen:tabViewSilosArmazens:tabDadosBasicos");
				Html.atualizar("formSilosArmazen:tabViewSilosArmazens:gridTable");
				Html.atualizar("msg");
			}
			
			if(!Util.isNullOuVazio(this.silosArmazen.getSilosArmazensResponsavelTecnicos())){
				this.silosArmazen.getSilosArmazensResponsavelTecnicos().clear();
			}
			
			if(!Util.isNullOuVazio(this.silosArmazen.getSilosArmazensImovelRurals())){
				this.silosArmazen.getSilosArmazensImovelRurals().clear();
			}
			listarImoveisParaEmpreendimentoSelecionado();
		}else{
			JsfUtil.addWarnMessage("Não existe endereço cadastrado para o empreendimento, por favor efetue o cadastro!");
		}
		
	}
	
	public void limparTela(){
		carregarOperaçãoDesensolvida();
		carregarTipoCombustivelUtilizado();
		
		caracterizacaoAmbientalSilosArmazen = new CaracterizacaoAmbientalSilosArmazen();
		carregarAbaCaracterizacao();
		carregarAbaSeguranca();
		carregarAbaDocumentosEstudos();
		
		Html.atualizar("formSilosArmazen");
	}
	
	public void inicializar(){
		
		this.silosArmazen = new SilosArmazen();
		
		setCadastro(new CadastroAtividadeNaoSujeitaLic(getUsuarioRealizandoAcao(), requerente, TipoAtividadeNaoSujeitaLicenciamentoEnum.SILOS_E_ARMAZENS));
		isVisivel = cadastro.isVisualizar();
		silosArmazen.setIdeCadastroAtividadeNaoSujeitaLic(getCadastro()); 
		if(!Util.isNull(ContextoUtil.getContexto().getPessoa())){
			ContextoUtil.getContexto().setPessoa(null);
		}
	}
	
	public Boolean existeEnderecoLocalizacao(Empreendimento empreendimento){
		boolean retorno = false;
		
		if(!Util.isNullOuVazio(empreendimento.getEnderecoEmpreendimentoCollection())){
			
			for(EnderecoEmpreendimento end : empreendimento.getEnderecoEmpreendimentoCollection()){
				
				if(!Util.isNullOuVazio(end.getIdeTipoEndereco())){
					
					if(TipoEnderecoEnum.LOCALIZACAO.getId() == end.getIdeTipoEndereco().getIdeTipoEndereco()){
						
						return true;
					}
				}
				
			}
		
		}
		
		this.empreendimento = null;
		
		return retorno;
	}
	
	public void buscarEmpreendimento(Empreendimento empreendimento){
		
		try {
			
		this.empreendimento.setEndereco(silosArmazensFacade.obterEnderecoEmpreendimento(this.empreendimento.getIdeEmpreendimento()));	
		
		verificarEmpreendimentoTemImovelRural();
		
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void verificarEmpreendimentoTemImovelRural(){
		
		if(!Util.isNullOuVazio(this.empreendimento)){
			
			try {
				this.temImovelRural = this.silosArmazensFacade.existemImovelRuralImpreendimento(this.empreendimento);
				
			} catch (Exception e) {
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}
	
	public void listarImoveisParaEmpreendimentoSelecionado(){
		
		try {
				
				List<Imovel> listaImovel;
					listaImovel = this.silosArmazensFacade.listarImoveisRurais(empreendimento);
				if(!Util.isNullOuVazio(listaImovel)){
					
					this.silosArmazen.setSilosArmazensImovelRurals(new ArrayList<SilosArmazensImovel>());
					
					for(Imovel imovel :listaImovel){
						SilosArmazensImovel imoRural = new SilosArmazensImovel(silosArmazen);
						imoRural.setIdeImovel(imovel);
						imoRural.setIndNumCar(false);
						
						this.silosArmazen.getSilosArmazensImovelRurals().add(imoRural);
					}
				}
				
				if(Util.isNullOuVazio(operacaoDesenvolvidaSilosArmazenList)){
					
					carregarOperaçãoDesensolvida();
				}
				
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		
	}
	
	public void salvar(){
		try {
			
			if(isAbaDadosBasicos()){
//				prepararSilosArmazensOperacaoDesenvolvidas();
//				prepararSilosArmazensTipoCombustivels();
				silosArmazensFacade.salvarAbaDadosBasicosSilosArmazens(silosArmazen);
			}
			
			if(isAbaUnidadeArmazenadora()){
				salvarAbaUnidadeArmazenadora();
			}
			
			if(isCaracterizacaoAmbiental()){
				prepararOrigemAgua();
				prepararSistemaTratamento();
				prepararEfluentesCorpoHidrico();
				prepararCaracterizacaoAtmosferica();
				prepararEquipamentoControle();
				prepararMedidasControleEmissoes();
				prepararResiduosSolidos();
				prepararDestinacaoFinal();
				salvarAbaCaracterizacaoAmbiental();
			}
			
			if(isAbaSistemaSeguranca()){
				
				prepararAbaSeguranca();
				salvarAbaSeguranca();
			}
			
			if(isAbaDocumentosEstudos()){
				
				prepararAbaDocumentoEstudos();
				salvarAbaDocumentoEstudos();
			}
			
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void prepararSilosArmazensOperacaoDesenvolvidas(){

		if(!Util.isNullOuVazio(operacaoDesenvolvidaSilosArmazenList)){
		
		silosArmazen.setSilosArmazensOperacaoDesenvolvidas(new ArrayList<SilosArmazensOperacaoDesenvolvida>());
		
			for(OperacaoDesenvolvidaSilosArmazen op: operacaoDesenvolvidaSilosArmazenList){
				
				if(!Util.isNullOuVazio(op.getIndSelecionado()) && op.getIndSelecionado()){
					SilosArmazensOperacaoDesenvolvida silosOp = new SilosArmazensOperacaoDesenvolvida();
					
					silosOp.setSilosArmazen(silosArmazen);
					silosOp.setOperacaoDesenvolvidaSilosArmazen(op);
					silosArmazen.getSilosArmazensOperacaoDesenvolvidas().add(silosOp);
					
					for(OperacaoDesenvolvidaSilosArmazen opFilho: op.getOperacaoDesenvolvidaSilosArmazenAuxiliar()){
						
						if(opFilho.getIndSelecionado()){
							SilosArmazensOperacaoDesenvolvida siloOpAux = new SilosArmazensOperacaoDesenvolvida();
							
							siloOpAux.setSilosArmazen(silosArmazen);
							siloOpAux.setOperacaoDesenvolvidaSilosArmazen(opFilho);
							silosArmazen.getSilosArmazensOperacaoDesenvolvidas().add(siloOpAux);
						}
					}
				}
			}
		}
		
	}
	
	public void prepararSilosArmazensTipoCombustivels(){

		if(!Util.isNullOuVazio(tipoCombustivelSiloArmazensList)){
			
		silosArmazen.setSilosArmazensTipoCombustivels(new ArrayList<SilosArmazensTipoCombustivel>());

			for(TipoCombustivelSiloArmazen tipo: tipoCombustivelSiloArmazensList){
				
				if(!Util.isNullOuVazio(tipo.getIndSelecionado()) && tipo.getIndSelecionado()){
					SilosArmazensTipoCombustivel silosComb = new SilosArmazensTipoCombustivel();
					
					silosComb.setSilosArmazen(silosArmazen);
					silosComb.setTipoCombustivelSiloArmazen(tipo);
					silosComb.setNumRaf(tipo.getNumeroRaf());
					silosArmazen.getSilosArmazensTipoCombustivels().add(silosComb);
					
					
					if(tipo.getIdeTipoCombustivelSiloArmazens() == TipoCombustivelSiloArmazenEnum.MADEIRA.getId()){
						
						for(TipoCombustivelSiloArmazen tipoAux: tipo.getTipoCombustivelSiloArmazensAuxiliar()){
							
							
							if(tipoAux.getIndSelecionado()){
								
								SilosArmazensTipoCombustivel silosCombFilho = new SilosArmazensTipoCombustivel();
								
								silosCombFilho.setSilosArmazen(silosArmazen);
								silosCombFilho.setTipoCombustivelSiloArmazen(tipoAux);
								silosCombFilho.setNumRaf(tipoAux.getNumeroRaf());
								silosArmazen.getSilosArmazensTipoCombustivels().add(silosCombFilho);
							}
							
						}
						
					}
					
					
				}
			}
		}
	}
	
	public void carregarCadastrarUnidade(){
		
		
		try {
		this.editarUnidadeArmazenadora = false;	
			
		this.tipoEspecieArmazems = silosArmazensFacade.listarTipoEspecieArmazem();
		
		this.silosArmazensUnidadeArmazenadora = new SilosArmazensUnidadeArmazenadora();
		
		this.tipoEspecieArmazem = new TipoEspecieArmazem();
			
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		Html.atualizar("formCadastrarUnidade");
	}
	
	public void visualizarCadastrarUnidade(SilosArmazensUnidadeArmazenadora silosArmazensUnidadeArmazenadora){
		
		try {
			this.tipoEspecieArmazems = silosArmazensFacade.listarTipoEspecieArmazem();
			
			this.tipoEspecieArmazem = silosArmazensUnidadeArmazenadora.getTipoArmazem().getTipoEspecieArmazem();
			
			Html.atualizar("dialogCadastrarUnidade");
			
			if(!Util.isNullOuVazio(this.tipoArmazems)){
				tipoArmazems.clear();
			}
			
			for (TipoArmazem tipo : silosArmazensFacade.listarTipoArmazem()) {
				
				if(this.tipoEspecieArmazem.getIdeTipoEspecieArmazem() == tipo.getTipoEspecieArmazem().getIdeTipoEspecieArmazem()){
					
					this.tipoArmazems.add(tipo);
				}
			}
			
			Html.atualizar("formCadastrarUnidade:panelTipoArmazem");
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	}
	
	public void editarCadastrarUnidade(SilosArmazensUnidadeArmazenadora silosArmazensUnidadeArmazenadora){
		
		try {
			this.editarUnidadeArmazenadora = true;
			
			this.tipoEspecieArmazems = silosArmazensFacade.listarTipoEspecieArmazem();
			
			this.tipoEspecieArmazem = silosArmazensUnidadeArmazenadora.getTipoArmazem().getTipoEspecieArmazem();
			
			Html.atualizar("dialogCadastrarUnidade");
			
			if(!Util.isNullOuVazio(this.tipoArmazems)){
				tipoArmazems.clear();
			}
			
			for (TipoArmazem tipo : silosArmazensFacade.listarTipoArmazem()) {
				
				if(this.tipoEspecieArmazem.getIdeTipoEspecieArmazem() == tipo.getTipoEspecieArmazem().getIdeTipoEspecieArmazem()){
					
					this.tipoArmazems.add(tipo);
				}
			}
			
			Html.atualizar("formCadastrarUnidade:panelTipoArmazem");
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
			
	}
	
	
	public void salvarCadastrarUnidade(){
		if(validarCadastrarUnidade()){
			
		
			if(Util.isNull(editarUnidadeArmazenadora) || !editarUnidadeArmazenadora){
				
				this.silosArmazensUnidadeArmazenadora.getTipoArmazem().setTipoEspecieArmazem(tipoEspecieArmazem);
				this.silosArmazensUnidadeArmazenadora.setSilosArmazen(this.silosArmazen);
				this.silosArmazen.getSilosArmazensUnidadeArmazenadoras().add(silosArmazensUnidadeArmazenadora);
			}
			Html.atualizar("formSilosArmazen:tabViewSilosArmazens:gridUnidadeArmazenadora");
			Html.esconder("dialogCadastrarUnidade");
		}
	}
	
	
	public Boolean validarCadastrarUnidade(){
		
		boolean retorno = true;
		
		if(Util.isNullOuVazio(silosArmazensUnidadeArmazenadora.getNomUnidadeArmazenadora())){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Identificação da unidade armazenadora é de preenchimento obrigatório!");
		}
		
		if(Util.isNullOuVazio(silosArmazensUnidadeArmazenadora.getCodCda())){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Código CDA é de preenchimento obrigatório!");
		}
		
		if(Util.isNullOuVazio(tipoEspecieArmazem)){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Espécie de armazém é de preenchimento obrigatório!");
		}
		
		if(Util.isNullOuVazio(silosArmazensUnidadeArmazenadora.getTipoArmazem())){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Tipo de armazém é de preenchimento obrigatório!");
		}
		
		if(Util.isNullOuVazio(silosArmazensUnidadeArmazenadora.getValCapacidadeEstatica()) || silosArmazensUnidadeArmazenadora.getValCapacidadeEstatica() == new BigDecimal("0.00")){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Capacidade estática é de preenchimento obrigatório!");
		}
		
		if(Util.isNullOuVazio(silosArmazensUnidadeArmazenadora.getIdeLocalizacaoGeografica())){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Incluir localização geográfica é de preenchimento obrigatório!");
		}
		
		return retorno;
	}
	
	public String visualizarLocalizacao(LocalizacaoGeografica locGeo) {
			return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(locGeo);
	}
	
	public void carregarTipoArmazen(ValueChangeEvent event){
		
		try {
			TipoEspecieArmazem tipoEspecieArmazen = (TipoEspecieArmazem) event.getNewValue();
			
			this.tipoArmazems.clear();
			
			for (TipoArmazem tipo : silosArmazensFacade.listarTipoArmazem()) {
				
				if(tipoEspecieArmazen.getIdeTipoEspecieArmazem() == tipo.getTipoEspecieArmazem().getIdeTipoEspecieArmazem()){
					
					this.tipoArmazems.add(tipo);
				}
			}
			
			Html.atualizar("formCadastrarUnidade:panelTipoArmazem");
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	
	public void excluirLocalizacaoCadastarUnidade(){
		try {
			silosArmazensFacade.excluirByIdLocalizacaoGeografica(silosArmazensUnidadeArmazenadora.getIdeLocalizacaoGeografica());
			silosArmazensUnidadeArmazenadora.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			Html.atualizar("formCadastrarUnidade:gridLocalizacaoGeo");
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	
	public void excluirUnidadeArmazenadora(){
		for(int i=0; i<this.silosArmazen.getSilosArmazensUnidadeArmazenadoras().size();i++){
			if(this.silosArmazen.getSilosArmazensUnidadeArmazenadoras().get(i) == this.excluirArmazensUnidadeArmazenadora ){
				this.silosArmazen.getSilosArmazensUnidadeArmazenadoras().remove(i);
				break;
			}
		}
		Html.atualizar("formSilosArmazen:tabViewSilosArmazens:gridUnidadeArmazenadora");
	}
	
	
	public void salvarAbaUnidadeArmazenadora(){
		try {
			this.silosArmazensUnidadeArmazenadora.setSilosArmazen(this.silosArmazen);
			
			silosArmazensFacade.salvarSilosArmazensUnidadeArmazenadora(this.silosArmazen.getSilosArmazensUnidadeArmazenadoras());
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarAbaCaracterizacao(){
		
		try {
			this.silosArmazensOrigemAguas = this.silosArmazensFacade.listarSilosArmazensOrigemAgua();
			this.sistemaTratamentoAguaList = this.silosArmazensFacade.listarSistemaTratamentoAgua();
			this.silosArmazensCaracterizacaoAtmosfericaList = this.silosArmazensFacade.listarSilosArmazensCaracterizacaoAtmosferica();
			this.equipamentoControleList = this.silosArmazensFacade.listarEquipamentoControle();
			this.medidaControleEmissaoList = this.silosArmazensFacade.listarMedidaControleEmissao();
			this.classificacaoResiduoList = this.silosArmazensFacade.listarClassificacaoResiduo();
			this.destinacaoFinalList = this.silosArmazensFacade.listarDestinacaoFinal();
			
			if(!Util.isNullOuVazio(silosArmazen.getCaracterizacaoAmbientalSilosArmazens())){
				
			
			carregarOrigem();
			
			carregarSistemaTratamento();
			
			carregarLancamentoEfluente();
			
			carregarCaracterizacao();
			
			carregarEquipamentosControle();
			
			carregarMedida();
			
			carregarClassificacaoResiduos();
			
			carregarDestinacaoFinal();
			}
			
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarOrigem() throws Exception{
		
		//origem
		if(!Util.isNullOuVazio(this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalOrigemAguas()) && this.caracterizacaoAmbientalSilosArmazen.getIndUtilizaAgua()){
			
			for(CaracterizacaoAmbientalOrigemAgua origem : this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalOrigemAguas()){
				this.caracterizacaoAmbientalOrigemAgua.getSilosArmazensOrigemAguaList().add(origem.getSilosArmazensOrigemAgua());
				
				
				if(origem.getSilosArmazensOrigemAgua().getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.POCO_DE_CAPTACAO.getId()){
					this.caracterizacaoAmbientalOrigemAguaPoco = origem;
					if(Util.isNullOuVazio(origem.getNumDocumentoDispensa())){
						this.caracterizacaoAmbientalOrigemAguaPoco.setIndSelecionado(true);
					}else if(Util.isNullOuVazio(origem.getNumDocumentoPortaria())){
						
						this.caracterizacaoAmbientalOrigemAguaPoco.setIndSelecionado(false);
					}
					primeiraGridOrigem = true;
				}
				
				if(origem.getSilosArmazensOrigemAgua().getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.RECURSO_HIDRICO_SUPERFICIAL.getId()){
					this.caracterizacaoAmbientalOrigemAguaRecurso = origem;
					if(Util.isNullOuVazio(origem.getNumDocumentoDispensa())){
						this.caracterizacaoAmbientalOrigemAguaRecurso.setIndSelecionado(true);
					}else if(Util.isNullOuVazio(origem.getNumDocumentoPortaria())){
						
						this.caracterizacaoAmbientalOrigemAguaRecurso.setIndSelecionado(false);
					}
					segundaGridOrigem = true;
				}
				
				if(origem.getSilosArmazensOrigemAgua().getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.CONCESSIONARIA.getId()){
					
					if(Util.isNullOuVazio(tipoConcessionarias)){
						
						tipoConcessionarias = silosArmazensFacade.listarTipoConcessionaria();
					}
					
					this.caracterizacaoAmbientalOrigemAguaConcessionaria = origem;
					this.caracterizacaoAmbientalOrigemAguaConcessionaria.setIdeCaracterizacaoAmbientalOrigemAgua(origem.getIdeCaracterizacaoAmbientalOrigemAgua());
					this.caracterizacaoAmbientalOrigemAguaConcessionaria.setIndSelecionado(true);
					terceiraGridOrigem = true;
					this.caracterizacaoAmbientalOrigemAguaConcessionaria.setTipoConcessionariasList(new ArrayList<TipoConcessionaria>());
					for(OrigemAguaTipoConcessionaria aguaTipo: origem.getOrigemAguaTipoConcessionarias()){
						this.caracterizacaoAmbientalOrigemAguaConcessionaria.getTipoConcessionariasList().add(aguaTipo.getTipoConcessionaria()); 
					}
				}
				
			}
			
		}
	}
	
	public void carregarSistemaTratamento(){
		
		if(caracterizacaoAmbientalSilosArmazen.getIndSistemaTratamento()){
			if(Util.isNullOuVazio(this.caracterizacaoAmbientalSilosArmazen.getSistemaTratamentoAguas())){
				this.caracterizacaoAmbientalSilosArmazen.setSistemaTratamentoAguas(new ArrayList<SistemaTratamentoAgua>());
			}
			for(SilosArmazensSistemaTratamentoAgua tratamento: this.caracterizacaoAmbientalSilosArmazen.getSilosArmazensSistemaTratamentoAguas()){
				this.caracterizacaoAmbientalSilosArmazen.getSistemaTratamentoAguas().add(tratamento.getSistemaTratamentoAgua()); 
			}
		}
	}
	
	public void carregarLancamentoEfluente(){
		
		if(caracterizacaoAmbientalSilosArmazen.getIndLancaEfluente()){
			
			
			for(SilosArmazensLancamentoEfluente lancamento:caracterizacaoAmbientalSilosArmazen.getSilosArmazensLancamentoEfluentes()){
				
				if(!Util.isNullOuVazio(lancamento.getNumDocumentoPortaria())){
					this.silosArmazensLancamentoEfluente = lancamento;
					this.silosArmazensLancamentoEfluente.setTipoDocumento("Portaria");
				}
				
				if(!Util.isNullOuVazio(lancamento.getNumDocumentoDispensa())){
					this.silosArmazensLancamentoEfluente = lancamento;
					this.silosArmazensLancamentoEfluente.setTipoDocumento("Declaracao");
				}	
			}
		}
	}
	
	public void carregarCaracterizacao(){
		
		if(!Util.isNullOuVazio(this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalCaracterizacaoAtmosfericas())){
			
			this.caracterizacaoAmbientalSilosArmazen.setSilosArmazensCaracterizacaoAtmosfericas(new ArrayList<SilosArmazensCaracterizacaoAtmosferica>());
			for(CaracterizacaoAmbientalCaracterizacaoAtmosferica atm : this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalCaracterizacaoAtmosfericas()){
				
				this.caracterizacaoAmbientalSilosArmazen.getSilosArmazensCaracterizacaoAtmosfericas().add(atm.getSilosArmazensCaracterizacaoAtmosferica()); 
			}
		}
	}
	
	public void carregarEquipamentosControle(){
		
		if(!Util.isNullOuVazio(this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalEquipamentoControles())){
			
			this.caracterizacaoAmbientalSilosArmazen.setEquipamentoControles(new ArrayList<EquipamentoControle>());
			for(CaracterizacaoAmbientalEquipamentoControle equipamento:this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalEquipamentoControles()){
				
				this.caracterizacaoAmbientalSilosArmazen.getEquipamentoControles().add(equipamento.getEquipamentoControle()); 
			}
		}
	}
	
	
	public void carregarMedida(){
		
		if(caracterizacaoAmbientalSilosArmazen.getIndMedidaControleEmissao()){
			
			if(!Util.isNullOuVazio(this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalMedidaControleEmissaos())){
				
				this.caracterizacaoAmbientalSilosArmazen.setMedidaControleEmissaos(new ArrayList<MedidaControleEmissao>());
				for(CaracterizacaoAmbientalMedidaControleEmissao medida: this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalMedidaControleEmissaos()){
					
					this.caracterizacaoAmbientalSilosArmazen.getMedidaControleEmissaos().add(medida.getMedidaControleEmissao());
				}
			}
		}
		
	}
	
	public void carregarClassificacaoResiduos(){
		
		if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getClassificacaoResiduoCaracterizacaoAmbientals())){
			
			this.caracterizacaoAmbientalSilosArmazen.setClassificacaoResiduos(new ArrayList<ClassificacaoResiduo>());
			for(ClassificacaoResiduoCaracterizacaoAmbiental residuo: caracterizacaoAmbientalSilosArmazen.getClassificacaoResiduoCaracterizacaoAmbientals()){
				
				this.caracterizacaoAmbientalSilosArmazen.getClassificacaoResiduos().add(residuo.getClassificacaoResiduo());
			}
			
		}
	}
	
	public void carregarDestinacaoFinal(){
		
		if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalDestinacaoFinals())){
			
			this.caracterizacaoAmbientalSilosArmazen.setDestinacaoFinals(new ArrayList<DestinacaoFinal>());
			
			for(CaracterizacaoAmbientalDestinacaoFinal destinacao : caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalDestinacaoFinals()){
				
				this.caracterizacaoAmbientalSilosArmazen.getDestinacaoFinals().add(destinacao.getDestinacaoFinal());
			}
		}
	}
	
	public void prepararOrigemAgua(){
		
		this.caracterizacaoAmbientalSilosArmazen.setCaracterizacaoAmbientalOrigemAguas(new ArrayList<CaracterizacaoAmbientalOrigemAgua>());
		if(!Util.isNullOuVazio(caracterizacaoAmbientalOrigemAgua.getSilosArmazensOrigemAguaList())){
			
			for(SilosArmazensOrigemAgua origem: caracterizacaoAmbientalOrigemAgua.getSilosArmazensOrigemAguaList()){
				
				if(origem.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.POCO_DE_CAPTACAO.getId() && !Util.isNullOuVazio(this.caracterizacaoAmbientalOrigemAguaPoco.getIndSelecionado())){
						
					if(this.caracterizacaoAmbientalOrigemAguaPoco.getIndSelecionado()){
						this.caracterizacaoAmbientalOrigemAguaPoco.setNumDocumentoDispensa(null);
					}else{
						this.caracterizacaoAmbientalOrigemAguaPoco.setNumDocumentoPortaria(null);
					}
					
					this.caracterizacaoAmbientalOrigemAguaPoco.setSilosArmazensOrigemAgua(origem);
					this.caracterizacaoAmbientalOrigemAguaPoco.setCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazen);
					this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalOrigemAguas().add(caracterizacaoAmbientalOrigemAguaPoco);
					
				}
				
				if(origem.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.RECURSO_HIDRICO_SUPERFICIAL.getId() && !Util.isNullOuVazio(this.caracterizacaoAmbientalOrigemAguaRecurso.getIndSelecionado())){
					
					if(this.caracterizacaoAmbientalOrigemAguaRecurso.getIndSelecionado()){
						this.caracterizacaoAmbientalOrigemAguaRecurso.setNumDocumentoDispensa(null);
					}else{
						this.caracterizacaoAmbientalOrigemAguaRecurso.setNumDocumentoPortaria(null);
					}
					
					this.caracterizacaoAmbientalOrigemAguaRecurso.setSilosArmazensOrigemAgua(origem);
					this.caracterizacaoAmbientalOrigemAguaRecurso.setCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazen);
					this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalOrigemAguas().add(caracterizacaoAmbientalOrigemAguaRecurso);
					
				}
				
				if(origem.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.CONCESSIONARIA.getId() && !Util.isNullOuVazio(this.caracterizacaoAmbientalOrigemAguaConcessionaria.getTipoConcessionariasList())){
					
					this.caracterizacaoAmbientalOrigemAguaConcessionaria.setSilosArmazensOrigemAgua(origem);
					this.caracterizacaoAmbientalOrigemAguaConcessionaria.setCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazen);
					this.caracterizacaoAmbientalOrigemAguaConcessionaria.setOrigemAguaTipoConcessionarias(new ArrayList<OrigemAguaTipoConcessionaria>());
					for(TipoConcessionaria tipo : this.caracterizacaoAmbientalOrigemAguaConcessionaria.getTipoConcessionariasList()){
						OrigemAguaTipoConcessionaria aguaTipo = new OrigemAguaTipoConcessionaria();
						
						aguaTipo.setTipoConcessionaria(tipo);
						aguaTipo.setCaracterizacaoAmbientalOrigemAgua(this.caracterizacaoAmbientalOrigemAguaConcessionaria);
						this.caracterizacaoAmbientalOrigemAguaConcessionaria.getOrigemAguaTipoConcessionarias().add(aguaTipo);
					}
					
					this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalOrigemAguas().add(caracterizacaoAmbientalOrigemAguaConcessionaria);
				}
				
			}
			
		}
		
		
	}
	
	
	public void prepararEfluentesCorpoHidrico(){
		
		if(this.caracterizacaoAmbientalSilosArmazen.getIndLancaEfluente()){
			this.caracterizacaoAmbientalSilosArmazen.setSilosArmazensLancamentoEfluentes(new ArrayList<SilosArmazensLancamentoEfluente>());
			this.silosArmazensLancamentoEfluente.setCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazen);
			this.caracterizacaoAmbientalSilosArmazen.getSilosArmazensLancamentoEfluentes().add(this.silosArmazensLancamentoEfluente);
		}
	}
	
	public void prepararSistemaTratamento(){
		
		if(this.caracterizacaoAmbientalSilosArmazen.getIndSistemaTratamento()){
			
			this.caracterizacaoAmbientalSilosArmazen.setSilosArmazensSistemaTratamentoAguas(new ArrayList<SilosArmazensSistemaTratamentoAgua>());
			for(SistemaTratamentoAgua tratamentoAgua: caracterizacaoAmbientalSilosArmazen.getSistemaTratamentoAguas()){
				
				SilosArmazensSistemaTratamentoAgua sistemaTratamentoAgua = new SilosArmazensSistemaTratamentoAgua();
				
				sistemaTratamentoAgua.setCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazen);
				sistemaTratamentoAgua.setSistemaTratamentoAgua(tratamentoAgua);
				this.caracterizacaoAmbientalSilosArmazen.getSilosArmazensSistemaTratamentoAguas().add(sistemaTratamentoAgua);
			}
		}
	}
	
	public void prepararCaracterizacaoAtmosferica(){
		
		this.caracterizacaoAmbientalSilosArmazen.setCaracterizacaoAmbientalCaracterizacaoAtmosfericas(new ArrayList<CaracterizacaoAmbientalCaracterizacaoAtmosferica>());
		for(SilosArmazensCaracterizacaoAtmosferica atmosferica: caracterizacaoAmbientalSilosArmazen.getSilosArmazensCaracterizacaoAtmosfericas()){
			CaracterizacaoAmbientalCaracterizacaoAtmosferica caractAtmos = new CaracterizacaoAmbientalCaracterizacaoAtmosferica();
			
			caractAtmos.setCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazen);
			caractAtmos.setSilosArmazensCaracterizacaoAtmosferica(atmosferica);
			this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalCaracterizacaoAtmosfericas().add(caractAtmos);
		}
	}
	
	public void prepararEquipamentoControle(){
		this.caracterizacaoAmbientalSilosArmazen.setCaracterizacaoAmbientalEquipamentoControles(new ArrayList<CaracterizacaoAmbientalEquipamentoControle>());
		for(EquipamentoControle equipamento: caracterizacaoAmbientalSilosArmazen.getEquipamentoControles()){
			CaracterizacaoAmbientalEquipamentoControle equipamentoControle = new CaracterizacaoAmbientalEquipamentoControle();
			
			equipamentoControle.setCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazen);
			equipamentoControle.setEquipamentoControle(equipamento);
			this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalEquipamentoControles().add(equipamentoControle);
		}
		
	}
	
	public void prepararMedidasControleEmissoes(){
		
		if(caracterizacaoAmbientalSilosArmazen.getIndMedidaControleEmissao()){
			this.caracterizacaoAmbientalSilosArmazen.setCaracterizacaoAmbientalMedidaControleEmissaos(new ArrayList<CaracterizacaoAmbientalMedidaControleEmissao>());
			for(MedidaControleEmissao medida: caracterizacaoAmbientalSilosArmazen.getMedidaControleEmissaos()){
				CaracterizacaoAmbientalMedidaControleEmissao medidaControle = new CaracterizacaoAmbientalMedidaControleEmissao();
				
				medidaControle.setCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazen);
				medidaControle.setMedidaControleEmissao(medida);
				this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalMedidaControleEmissaos().add(medidaControle);
			}
		}
	}
	
	public void prepararResiduosSolidos(){
		
		this.caracterizacaoAmbientalSilosArmazen.setClassificacaoResiduoCaracterizacaoAmbientals(new ArrayList<ClassificacaoResiduoCaracterizacaoAmbiental>());
		for(ClassificacaoResiduo classficacao : caracterizacaoAmbientalSilosArmazen.getClassificacaoResiduos()){
			ClassificacaoResiduoCaracterizacaoAmbiental caracterizacaoAmbiental = new ClassificacaoResiduoCaracterizacaoAmbiental();
			
			caracterizacaoAmbiental.setCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazen);
			caracterizacaoAmbiental.setClassificacaoResiduo(classficacao);
			this.caracterizacaoAmbientalSilosArmazen.getClassificacaoResiduoCaracterizacaoAmbientals().add(caracterizacaoAmbiental);
		}
	}
	
	public void prepararDestinacaoFinal(){
		this.caracterizacaoAmbientalSilosArmazen.setCaracterizacaoAmbientalDestinacaoFinals(new ArrayList<CaracterizacaoAmbientalDestinacaoFinal>());
		for(DestinacaoFinal destinacao: caracterizacaoAmbientalSilosArmazen.getDestinacaoFinals()){
			
			CaracterizacaoAmbientalDestinacaoFinal destinacaoFinal = new CaracterizacaoAmbientalDestinacaoFinal();
			
			destinacaoFinal.setCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazen);
			destinacaoFinal.setDestinacaoFinal(destinacao);
			this.caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalDestinacaoFinals().add(destinacaoFinal);
		}
	}
	
	public Boolean validarAbaCaracterizacaoAmbiental(){
		
		boolean retorno = true;
		
		if(Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getIndUtilizaAgua())){
			
			retorno = false;
			JsfUtil.addErrorMessage("O campo A atividade utiliza ou vai utilizar água é de preenchimento obrigatório!");
		}
		
		if(caracterizacaoAmbientalSilosArmazen.getIndUtilizaAgua()){
			
			if(Util.isNullOuVazio(caracterizacaoAmbientalOrigemAgua.getSilosArmazensOrigemAguaList())){
				retorno = false;
				JsfUtil.addErrorMessage("O campo Origem é de preenchimento obrigatório!");
			}else{
				
				for(SilosArmazensOrigemAgua origemAgua: caracterizacaoAmbientalOrigemAgua.getSilosArmazensOrigemAguaList()){
					
					if(origemAgua.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.POCO_DE_CAPTACAO.getId()){
						
						if(Util.isNullOuVazio(caracterizacaoAmbientalOrigemAguaPoco)){
							retorno = false;
							JsfUtil.addErrorMessage("Por favor escolha entre Portaria vigente ou Declaração de Dispensa!");
						}else{
							
							if(Util.isNullOuVazio(caracterizacaoAmbientalOrigemAguaPoco.getIndSelecionado())){
								retorno = false;
								JsfUtil.addErrorMessage("Por favor escolha entre Portaria vigente ou Declaração de Dispensa!");
							}else{
								if(caracterizacaoAmbientalOrigemAguaPoco.getIndSelecionado() && Util.isNullOuVazio(caracterizacaoAmbientalOrigemAguaPoco.getNumDocumentoPortaria())){
									retorno = false;
									JsfUtil.addErrorMessage("O campo N° do documento é de preenchimento obrigatório");
								}else if(!caracterizacaoAmbientalOrigemAguaPoco.getIndSelecionado() && Util.isNullOuVazio(caracterizacaoAmbientalOrigemAguaPoco.getNumDocumentoDispensa())){
									retorno = false;
									JsfUtil.addErrorMessage("O campo N° do documento é de preenchimento obrigatório");
								}
							}
						}
						
					}
					
					if(origemAgua.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.RECURSO_HIDRICO_SUPERFICIAL.getId()){
						
						if(Util.isNullOuVazio(caracterizacaoAmbientalOrigemAguaRecurso)){
							retorno = false;
							JsfUtil.addErrorMessage("Por favor escolha entre Portaria vigente ou Declaração de Dispensa!");
						}else{
							
							if(Util.isNullOuVazio(caracterizacaoAmbientalOrigemAguaRecurso.getIndSelecionado())){
								retorno = false;
								JsfUtil.addErrorMessage("Por favor escolha entre Portaria vigente ou Declaração de Dispensa!");
							}else{
								if(caracterizacaoAmbientalOrigemAguaRecurso.getIndSelecionado() && Util.isNullOuVazio(caracterizacaoAmbientalOrigemAguaRecurso.getNumDocumentoPortaria())){
									retorno = false;
									JsfUtil.addErrorMessage("O campo N° do documento é de preenchimento obrigatório");
								}else if(!caracterizacaoAmbientalOrigemAguaRecurso.getIndSelecionado() && Util.isNullOuVazio(caracterizacaoAmbientalOrigemAguaRecurso.getNumDocumentoDispensa())){
									retorno = false;
									JsfUtil.addErrorMessage("O campo N° do documento é de preenchimento obrigatório");
								}
							}
							
						}
						
					}
					
					if(origemAgua.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.CONCESSIONARIA.getId()){
					
						if(Util.isNullOuVazio(caracterizacaoAmbientalOrigemAguaConcessionaria.getTipoConcessionariasList())){
							
							retorno = false;
							JsfUtil.addErrorMessage("Por favor escolha os Tipos de concessionárias!");
						}
					}
					
				}
				
			}
		}
		
		if(Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getIndSistemaTratamento())){
			
			retorno = false;
			JsfUtil.addErrorMessage("O campo Possui sistema de tratamento é de preenchimento obrigatório!");
		}
		
		if(caracterizacaoAmbientalSilosArmazen.getIndSistemaTratamento() && Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getSistemaTratamentoAguas())){
			
			retorno = false;
			JsfUtil.addErrorMessage("O campo selecione o(s) sistema(s) é de preenchimento obrigatório");
		}
		
		if(Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getIndLancaEfluente())){
			
			retorno = false;
			JsfUtil.addErrorMessage("O campo A empresa lança Efluentes em corpo Hídrico é de preenchimento obrigatório!");
		}
		
		if(caracterizacaoAmbientalSilosArmazen.getIndLancaEfluente()){
			
			if(Util.isNullOuVazio(silosArmazensLancamentoEfluente)){
				
				retorno = false;
				JsfUtil.addErrorMessage("Por favor selecione Portaria vigente ou Declaração de dispensa!");
				
			}else{
				
				if(Util.isNullOuVazio(silosArmazensLancamentoEfluente.getTipoDocumento())){
					retorno = false;
					JsfUtil.addErrorMessage("Por favor selecione Portaria vigente ou Declaração de dispensa!");
				}else{
					
					if("Declaracao".equals(silosArmazensLancamentoEfluente.getTipoDocumento()) && Util.isNullOuVazio(silosArmazensLancamentoEfluente.getNumDocumentoDispensa())){
						
						retorno = false;
						JsfUtil.addErrorMessage("O campo N° do Documento é de preenchimento obrigatório!");
					}else if("Portaria".equals(silosArmazensLancamentoEfluente.getTipoDocumento()) && Util.isNullOuVazio(silosArmazensLancamentoEfluente.getNumDocumentoPortaria())){
						retorno = false;
						JsfUtil.addErrorMessage("O campo N° do Documento é de preenchimento obrigatório!");
					}
				}
				
			}
		}
		
		if(Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getSilosArmazensCaracterizacaoAtmosfericas())){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Caracterização é de preenchimento obrigatório!");
		}
		
		if(Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getEquipamentoControles())){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Equipamentos de controle é de preenchimento obrigatório!");
		}
		
		if(Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getIndMedidaControleEmissao())){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Possui medidas de controle de emissões é de preenchimento obrigatório!");
		}
		
		if(caracterizacaoAmbientalSilosArmazen.getIndMedidaControleEmissao()){
			
			if(Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getMedidaControleEmissaos())){
				
				retorno = false;
				JsfUtil.addErrorMessage("O campo Medida é de preenchimento obrigatório!");
			}
		}
		
		if(Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getClassificacaoResiduos())){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Classificação dos resíduos é de preenchimento obrigatório!");
		}
		
		if(Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getDestinacaoFinals())){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Destinação Final é de preenchimento obrigatório!");
		}
		
		return retorno;
	}
	
	public void salvarAbaCaracterizacaoAmbiental(){
		
		try {
			this.caracterizacaoAmbientalSilosArmazen.setSilosArmazen(silosArmazen);
			this.silosArmazensFacade.salvarAbaCaracterizacaoAmbiental(this.caracterizacaoAmbientalSilosArmazen, this.caracterizacaoAmbientalOrigemAguaConcessionaria);
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarAbaSeguranca(){
		
		try {
			this.silosArmazensSistemaSegurancasList = this.silosArmazensFacade.listarSilosArmazensSistemaSeguranca();
			
			if(!Util.isNullOuVazio(this.silosArmazen.getSistemaSegurancaSilosArmazens())){
				
				for(SistemaSegurancaSilosArmazen sistema : this.silosArmazen.getSistemaSegurancaSilosArmazens()){
					
					if(this.sistemaSegurancaSilosArmazen.getIdeSistemaSegurancaSilosArmazens() == null){
						
						this.sistemaSegurancaSilosArmazen = sistema;
						this.sistemaSegurancaSilosArmazen.setSilosArmazensSistemaSegurancas(new ArrayList<SilosArmazensSistemaSeguranca>());
					}
					
					this.sistemaSegurancaSilosArmazen.getSilosArmazensSistemaSegurancas().add(sistema.getSilosArmazensSistemaSeguranca());
				}
				
			}
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarAbaDocumentosEstudos(){
		
		try {
			cadastroAtividadeNaoSujeitaLicDocApensadosList = silosArmazensFacade.listarDocumentosApensados(cadastro);
			
			if(Util.isNullOuVazio(cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList) && Util.isNullOuVazio(cadastroAtividadeNaoSujeitaLicDocApensadosEstudosList)){
				
			
				for(CadastroAtividadeNaoSujeitaLicDocApensado apensado:cadastroAtividadeNaoSujeitaLicDocApensadosList){
					
					if(apensado.getIdeDocumentoObrigatorio().getIdeTipoDocumentoObrigatorio().getIdeTipoDocumentoObrigatorio() == TipoDocumentoObrigatorioEnum.DOCUMENTO.getId()){
						if(!cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList.contains(apensado)){
							cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList.add(apensado);
						}
					}else 
						if(apensado.getIdeDocumentoObrigatorio().getIdeTipoDocumentoObrigatorio().getIdeTipoDocumentoObrigatorio() == TipoDocumentoObrigatorioEnum.ESTUDO.getId()){
							if(!cadastroAtividadeNaoSujeitaLicDocApensadosEstudosList.contains(apensado)){
								cadastroAtividadeNaoSujeitaLicDocApensadosEstudosList.add(apensado);
							}
						}
				}
			}
			verificarTipoCombustivelMadeira();
			verificarObrigatoriedadeDocumentos();
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void verificarObrigatoriedadeDocumentos() {
		
				
				for(CadastroAtividadeNaoSujeitaLicDocApensado doc : cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList){
					
					if(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.COPIA_PORTARIA_OUTORGA_CAPTACAO_A_SER_REALIZADA.getId())){
						
						if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getIndUtilizaAgua())){
							
							if(caracterizacaoAmbientalSilosArmazen.getIndUtilizaAgua()){
								for(SilosArmazensOrigemAgua saoa : caracterizacaoAmbientalOrigemAgua.getSilosArmazensOrigemAguaList()){
									if(saoa.getIdeSilosArmazensOrigemAgua() == 1 || saoa.getIdeSilosArmazensOrigemAgua() == 2){
										doc.setExibir(true);
										break;
									}else{
										doc.setExibir(false);
										doc.setUrlDocumento(null);
									}
								}
							}else{
								doc.setExibir(false);
								doc.setUrlDocumento(null);
							}	
						}
					}
					
					if(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.COPIA_PORTARIA_OUTORGA_LANCAMENTO_EFLUENTES_A_SER_REALIZADA.getId())){
						if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getIndLancaEfluente())){
							
							if(caracterizacaoAmbientalSilosArmazen.getIndLancaEfluente()){
								doc.setExibir(true);
							}else{
								doc.setExibir(false);
								doc.setUrlDocumento(null);
							}
						}
					}
					
					if(!Util.isNullOuVazio(silosArmazen.getSilosArmazensImovelRurals())){
						for(SilosArmazensImovel aux : silosArmazen.getSilosArmazensImovelRurals()){
							
							if(!Util.isNullOuVazio(aux.getIndNumCar())){
								if(aux.getIndNumCar()){
									if(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.AUTORIZACAO_PROPRIETARIO_SUPERFICIARIO.getId())){
										doc.setExibir(true);
										break;
									}
									
								}else{
									if(!Util.isNull(isVisivel)){
										if(!isVisivel){
											if(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.AUTORIZACAO_PROPRIETARIO_SUPERFICIARIO.getId())){
												doc.setExibir(false);
												doc.setUrlDocumento(null);
											}
										}
									}
								}
							}else{
								if(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.AUTORIZACAO_PROPRIETARIO_SUPERFICIARIO.getId())){
									doc.setExibir(false);
									doc.setUrlDocumento(null);
								}
							}
						}
						
					}else{
						if(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.AUTORIZACAO_PROPRIETARIO_SUPERFICIARIO.getId())){
							doc.setExibir(false);
							doc.setUrlDocumento(null);
						}
					}	
				}
		if(RequestContext.getCurrentInstance()!= null){
			Html.atualizar("formSilosArmazen:tabViewSilosArmazens:tabDocumentosEstudos");
		}
	}

	public void uploadArquivo(FileUploadEvent event) {
		try {
			cadastroAtividadeNaoSujeitaLicDocApensado.setDtcEnvioDocumento(new Date());
			cadastroAtividadeNaoSujeitaLicDocApensado.setIdePessoaFisicaEnvio(getUsuarioRealizandoAcao());

			String caminhoArqivoNovo = DiretorioArquivoEnum.SILOS_ARMAZENS.toString();

			/*if (!Util.isNullOuVazio(silosArmazen.getIdeCadastroAtividadeNaoSujeitaLic().getIdeCadastroAtividadeNaoSujeitaLic())) {
				caminhoArqivoNovo += "_" + cadastroAtividadeNaoSujeitaLicDocApensado.getIdeCadastroAtividadeNaoSujeitaLicDocApensado();
			}
			else {*/
				caminhoArqivoNovo += "_" + silosArmazen.getIdeSilosArmazens();
//			}

			silosArmazensFacade.excluirDocumentoApensadoAnteriormente(cadastroAtividadeNaoSujeitaLicDocApensado.getUrlDocumento());
			cadastroAtividadeNaoSujeitaLicDocApensado.setUrlDocumento(FileUploadUtil.Enviar(event, caminhoArqivoNovo));
			JsfUtil.addSuccessMessage(Util.getString("msg_generica_arquivo_enviado"));
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o arquivo.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}
	
	public void verificarTipoCombustivelMadeira(){
		
		boolean madeiraNativa = false;
		
		boolean madeiraExotica = false;
		
		if(!Util.isNullOuVazio(tipoCombustivelSiloArmazensList)){
			
			for(TipoCombustivelSiloArmazen comb : tipoCombustivelSiloArmazensList){
				for(TipoCombustivelSiloArmazen combFilho : comb.getTipoCombustivelSiloArmazensAuxiliar()){
					
				
					if(TipoCombustivelSiloArmazenEnum.MADEIRA_NATIVA.getId() == combFilho.getIdeTipoCombustivelSiloArmazens() && !Util.isNullOuVazio(combFilho.getIndSelecionado())){
						if(combFilho.getIndSelecionado()){
							
							madeiraNativa = true;
						}
					}
					
					if(TipoCombustivelSiloArmazenEnum.MADEIRA_EXOTICA.getId() == combFilho.getIdeTipoCombustivelSiloArmazens() && !Util.isNullOuVazio(combFilho.getIndSelecionado())){
						if(combFilho.getIndSelecionado()){
							
							madeiraExotica = true;
						}
					}
				}
			}
			
		}
		
		try {
			for(CadastroAtividadeNaoSujeitaLicDocApensado doc : cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList){
				
				if(DocumentoObrigatorioEnum.CERTIFICADO_MADEIRA_NATIVA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
					doc.setExibir(madeiraNativa);
					if(!madeiraNativa){
						if(!Util.isNullOuVazio(doc.getUrlDocumento())){
							silosArmazensFacade.excluirDocumentoApensadoAnteriormente(doc);
							doc.setUrlDocumento(null);
						}
					}
				}else if(DocumentoObrigatorioEnum.CERTIFICADO_MADEIRA_EXOTICA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
					doc.setExibir(madeiraExotica);
					if(!madeiraExotica){
						if(!Util.isNullOuVazio(doc.getUrlDocumento())){
							silosArmazensFacade.excluirDocumentoApensadoAnteriormente(doc);
							doc.setUrlDocumento(null);
						}
					}
				}else{
					doc.setExibir(true);
				}
				
				if(DocumentoObrigatorioEnum.COMPROVANTE_ATUALIZADO_RAF.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
					if(!Util.isNullOuVazio(tipoCombustivelSiloArmazensList)){
						for(TipoCombustivelSiloArmazen comb : tipoCombustivelSiloArmazensList){
							if(!Util.isNullOuVazio(comb.getIndSelecionado())){
								if(!comb.getIndSelecionado() && comb.getIdeTipoCombustivelSiloArmazens() == 4){
									doc.setExibir(false);
									doc.setUrlDocumento(null);
								}else if(comb.getIndSelecionado() && comb.getIdeTipoCombustivelSiloArmazens() == 4){
									doc.setExibir(true);
								}
							}else{
								if(comb.getIdeTipoCombustivelSiloArmazens() == 4){
									doc.setExibir(false);
									doc.setUrlDocumento(null);
								}
							}
						}
					}else{
						doc.setExibir(false);
						doc.setUrlDocumento(null);
					}
				}
				
			}	
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
			
	}
	
	public boolean isDocumentoToDownload(CadastroAtividadeNaoSujeitaLicDocApensado docApensado) {
		return !Util.isNullOuVazio(docApensado.getIdeDocumentoObrigatorio().getDscCaminhoArquivo());
	}
	
	public StreamedContent getArquivoBaixar(Object obj) {
		return silosArmazensFacade.baixarArquivo(obj);
	}
	
	public boolean isDocumentoEnviado(CadastroAtividadeNaoSujeitaLicDocApensado docApensado) {
		return !Util.isNullOuVazio(docApensado.getUrlDocumento());
	}
	
	public Boolean validarAbaSeguranca(){
		
		boolean retorno = true;
		
		if(Util.isNullOuVazio(sistemaSegurancaSilosArmazen.getSilosArmazensSistemaSegurancas())){
			retorno = false;
			JsfUtil.addErrorMessage("O campo Sistema de segurança é de preenchimento obrigatório!");
		}
		
		return retorno;
	}
	
	public void salvarAbaSeguranca(){
		
		try {
		
			this.silosArmazensFacade.salvarAbaSeguranca(silosArmazen.getSistemaSegurancaSilosArmazens());
		
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void prepararAbaSeguranca(){
		
		silosArmazen.setSistemaSegurancaSilosArmazens(new ArrayList<SistemaSegurancaSilosArmazen>());
		for(SilosArmazensSistemaSeguranca seguranca : sistemaSegurancaSilosArmazen.getSilosArmazensSistemaSegurancas()){
			SistemaSegurancaSilosArmazen sistemaSeguranca = new SistemaSegurancaSilosArmazen();
			
			sistemaSeguranca.setSilosArmazen(silosArmazen);
			sistemaSeguranca.setSilosArmazensSistemaSeguranca(seguranca);
			silosArmazen.getSistemaSegurancaSilosArmazens().add(sistemaSeguranca);
		}
	}
	
	public boolean validarAbaDocumentos() {
		boolean valido = true;
		for (CadastroAtividadeNaoSujeitaLicDocApensado docs : cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList) {
			if (!isDocumentoEnviado(docs) && docs.getExibir()) {
				JsfUtil.addErrorMessage(Util.getString("documento_obrigatorio_msg_envio_pendencia"));
				valido = false;
				break;
			}
		}
		
		for (CadastroAtividadeNaoSujeitaLicDocApensado docs : cadastroAtividadeNaoSujeitaLicDocApensadosEstudosList) {
			if (!isDocumentoEnviado(docs)) {
				JsfUtil.addErrorMessage(Util.getString("documento_obrigatorio_msg_envio_pendencia"));
				valido = false;
				break;
			}
		}
		
		return valido;
	}
	
	public void prepararAbaDocumentoEstudos(){
		
		cadastro.setCadastroAtividadeNaoSujeitaLicDocApensados(new ArrayList<CadastroAtividadeNaoSujeitaLicDocApensado>());
		
		for (CadastroAtividadeNaoSujeitaLicDocApensado docs : cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList) {
			docs.setCadastroAtividadeNaoSujeitaLic(cadastro);
			
			cadastro.getCadastroAtividadeNaoSujeitaLicDocApensados().add(docs);
		}
		
		for (CadastroAtividadeNaoSujeitaLicDocApensado docs : cadastroAtividadeNaoSujeitaLicDocApensadosEstudosList) {
			docs.setCadastroAtividadeNaoSujeitaLic(cadastro);
			
			cadastro.getCadastroAtividadeNaoSujeitaLicDocApensados().add(docs);
		}
		
	}

	public void salvarAbaDocumentoEstudos(){
		try {
			silosArmazensFacade.salvarAbaDocumentosEstudos(cadastro);
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void valueChangeOrigemAgua(ValueChangeEvent event) throws Exception{
		
		List<SilosArmazensOrigemAgua> origemAgua = (List<SilosArmazensOrigemAgua>)event.getNewValue();
		
		List<SilosArmazensOrigemAgua> origemAguaOld = (List<SilosArmazensOrigemAgua>)event.getOldValue();
		
		List<Integer> ordemRender = new ArrayList<Integer>();
		
		List<Integer> ordemOld = new ArrayList<Integer>();
		
		for(SilosArmazensOrigemAgua origem: origemAgua){
			
			
			if(origem.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.POCO_DE_CAPTACAO.getId()){
				ordemRender.add(SilosArmazensOrigemAguaEnum.POCO_DE_CAPTACAO.getId());
			}
			
			
			if(origem.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.RECURSO_HIDRICO_SUPERFICIAL.getId()){
				ordemRender.add(SilosArmazensOrigemAguaEnum.RECURSO_HIDRICO_SUPERFICIAL.getId());
			}
			
			
			if(origem.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.CONCESSIONARIA.getId()){
				
				ordemRender.add(SilosArmazensOrigemAguaEnum.CONCESSIONARIA.getId());
				if(Util.isNullOuVazio(tipoConcessionarias)){
					
					tipoConcessionarias = silosArmazensFacade.listarTipoConcessionaria();
				}
			}
			
		}
		
		if (!Util.isNullOuVazio(origemAguaOld)) {

			for (SilosArmazensOrigemAgua origemOld : origemAguaOld) {

				if (origemOld.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.POCO_DE_CAPTACAO.getId()) {
					ordemOld.add(SilosArmazensOrigemAguaEnum.POCO_DE_CAPTACAO.getId());
				}

				if (origemOld.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.RECURSO_HIDRICO_SUPERFICIAL.getId()) {
					ordemOld.add(SilosArmazensOrigemAguaEnum.RECURSO_HIDRICO_SUPERFICIAL.getId());
				}

				if (origemOld.getIdeSilosArmazensOrigemAgua() == SilosArmazensOrigemAguaEnum.CONCESSIONARIA.getId()) {

					ordemOld.add(SilosArmazensOrigemAguaEnum.CONCESSIONARIA.getId());
				}
			}
		}
		
			
			if(ordemRender.contains(SilosArmazensOrigemAguaEnum.POCO_DE_CAPTACAO.getId()) && !ordemOld.contains(SilosArmazensOrigemAguaEnum.POCO_DE_CAPTACAO.getId())){
				primeiraGridOrigem = true;
				Html.atualizar("formSilosArmazen:tabViewSilosArmazens:grid1");
			}else if(!ordemRender.contains(SilosArmazensOrigemAguaEnum.POCO_DE_CAPTACAO.getId())){
				primeiraGridOrigem = false;
				this.caracterizacaoAmbientalOrigemAguaPoco = new CaracterizacaoAmbientalOrigemAgua();
				Html.atualizar("formSilosArmazen:tabViewSilosArmazens:grid1");
			}
			
			if(ordemRender.contains(SilosArmazensOrigemAguaEnum.RECURSO_HIDRICO_SUPERFICIAL.getId()) && !ordemOld.contains(SilosArmazensOrigemAguaEnum.RECURSO_HIDRICO_SUPERFICIAL.getId())){
				segundaGridOrigem = true;
				Html.atualizar("formSilosArmazen:tabViewSilosArmazens:grid2");
			}else if(!ordemRender.contains(SilosArmazensOrigemAguaEnum.RECURSO_HIDRICO_SUPERFICIAL.getId())){
				segundaGridOrigem = false;
				this.caracterizacaoAmbientalOrigemAguaRecurso = new CaracterizacaoAmbientalOrigemAgua();
				Html.atualizar("formSilosArmazen:tabViewSilosArmazens:grid2");
			}
			
			if(ordemRender.contains(SilosArmazensOrigemAguaEnum.CONCESSIONARIA.getId()) && !ordemOld.contains(SilosArmazensOrigemAguaEnum.CONCESSIONARIA.getId())){
				terceiraGridOrigem = true;
				Html.atualizar("formSilosArmazen:tabViewSilosArmazens:grid3");
			}else if(!ordemRender.contains(SilosArmazensOrigemAguaEnum.CONCESSIONARIA.getId())){
				terceiraGridOrigem = false;
				Html.atualizar("formSilosArmazen:tabViewSilosArmazens:grid3");
			}
	}
	
	public void avancarAbas(){
		
		
		if(validarAbas()){
			salvar();
			
			this.activeTab++;
			
			if(activeTab == 1){
				
			}else if(activeTab == 2){
				carregarAbaCaracterizacao();
			}else if(activeTab == 3){
				carregarAbaSeguranca();
			}else if(activeTab == 4){
				carregarAbaDocumentosEstudos();
			}
		}
	}
	
	public void voltar(){
		this.activeTab--;
		
	}
	
	
	public void finalizar(){
		
		if(isAbaDocumentosEstudos() && validarAbas()){
			
			prepararAbaDocumentoEstudos();
			salvarAbaDocumentoEstudos();
			Html.exibir("dialogDeclaracaoLegal");
		}
		
	}
	
	public void salvarDeclaracaoFinal(){
		
		if(validarTodasAbas() && silosArmazen.getIndAceiteDeclaracaoFinal() && validarOutros()){
			
			try {
				silosArmazensFacade.salvarAceiteFinal(this);
			
				//JsfUtil.addSuccessMessage("O cadastro nº " + silosArmazen.getIdeCadastroAtividadeNaoSujeitaLic().getNumCadastro() + ", foi salvo com sucesso!");
				Html.redirecionarPagina(TelaDestinoEnum.CADASTRO_ATIVIDADE_SEM_LICENCIAMENTO.getUrlTela());
				//MensagemUtil.sucesso("O cadastro nº " + silosArmazen.getIdeCadastroAtividadeNaoSujeitaLic().getNumCadastro() + ", foi salvo com sucesso!");
				ContextoUtil.getContexto().setUpdateMessage("O cadastro nº " + silosArmazen.getIdeCadastroAtividadeNaoSujeitaLic().getNumCadastro() + ", foi salvo com sucesso!");
				ContextoUtil.getContexto().setSucessMessage(true);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			
		}
	}
	
	public void salvarTodasAbas() throws Exception{
		
			prepararSilosArmazensOperacaoDesenvolvidas();
			prepararSilosArmazensTipoCombustivels();
			silosArmazensFacade.salvarAbaDadosBasicosSilosArmazens(silosArmazen);
		
			salvarAbaUnidadeArmazenadora();
		
			prepararOrigemAgua();
			prepararSistemaTratamento();
			prepararEfluentesCorpoHidrico();
			prepararCaracterizacaoAtmosferica();
			prepararEquipamentoControle();
			prepararMedidasControleEmissoes();
			prepararResiduosSolidos();
			prepararDestinacaoFinal();
			salvarAbaCaracterizacaoAmbiental();
		
			
			prepararAbaSeguranca();
			salvarAbaSeguranca();
		
			
			prepararAbaDocumentoEstudos();
			salvarAbaDocumentoEstudos();
	}
	
	public Boolean validarTodasAbas(){
		
		boolean retorno = true;
		
		if(!validarAbaDadosBasicos()){
			return false;
		}
		
		if(!validarAbaUnidadeArmazenadora()){
			return false;
		}
		
		if(!validarAbaCaracterizacaoAmbiental()){
			return false;
		}
		
		if(!validarAbaSeguranca()){
			return false;
		}
		
		if(!validarAbaDocumentos()){
			return false;
		}
		
		return retorno;
	}
	
	public Boolean validarAbas(){
		
		if(isAbaDadosBasicos()){
			prepararSilosArmazensOperacaoDesenvolvidas();
			prepararSilosArmazensTipoCombustivels();
			return validarAbaDadosBasicos();
		}
		else if(isAbaUnidadeArmazenadora()){
			return validarAbaUnidadeArmazenadora();
		}
		else if(isCaracterizacaoAmbiental()){
			return validarAbaCaracterizacaoAmbiental();
		}
		else if(isAbaSistemaSeguranca()){
			return validarAbaSeguranca();
		}
		else if(isAbaDocumentosEstudos()){
			return validarAbaDocumentos();
		}
		
		return false;
	}
	
	public Boolean validarAbaDadosBasicos(){
		
		boolean retorno = true;
		
		if(Util.isNullOuVazio(silosArmazen.getSilosArmazensResponsavelTecnicos())){
			
			retorno = false;
			JsfUtil.addErrorMessage("O campo Responsável técnico é de preenchimento obrigatório!");
		}
		
		if(Util.isNullOuVazio(silosArmazen.getSilosArmazensImovelRurals()) && this.temImovelRural){
			
			retorno = false;
			JsfUtil.addErrorMessage("O campo Imóvel Rural é de preenchimento obrigatório!");
		}
		
			if(Util.isNullOuVazio(silosArmazen.getIndExisteComunidade())){
				
				retorno = false;
				JsfUtil.addErrorMessage("O campo Existe alguma comunidade num raio de 1km do empreendimento é de preenchimento obrigatório!");
			}
			
			if(Util.isNullOuVazio(silosArmazen.getValAreaTotalConstruida()) || silosArmazen.getValAreaTotalConstruida() == new BigDecimal("0.00")){
				
				retorno = false;
				JsfUtil.addErrorMessage("O campo Área total construída é de preenchimento obrigatório!");
			}
			
			if(Util.isNullOuVazio(silosArmazen.getValAreaTotalTerreno()) || silosArmazen.getValAreaTotalTerreno() == new BigDecimal("0.00")){
				
				retorno = false;
				JsfUtil.addErrorMessage("O campo Área total do terreno é de preenchimento obrigatório!");
			}

			if(silosArmazen.getValAreaTotalConstruida().compareTo(silosArmazen.getValAreaTotalTerreno())>0){
				
				retorno = false;
				JsfUtil.addErrorMessage("O valor do campo Área total construída não pode ser maior do quer o valor do campo Área total do terreno");
			}

			if(Util.isNullOuVazio(silosArmazen.getSilosArmazensOperacaoDesenvolvidas())){
				
				retorno = false;
				JsfUtil.addErrorMessage("O campo Operações desenvolvidas no empreendimento é de preenchimento obrigatório!");
			}
			
			if(!Util.isNullOuVazio(silosArmazen.getSilosArmazensOperacaoDesenvolvidas())){
				
				for(SilosArmazensOperacaoDesenvolvida op: silosArmazen.getSilosArmazensOperacaoDesenvolvidas()){
					
					if(op.getOperacaoDesenvolvidaSilosArmazen().getIdeOperacaoDesenvolvidaSilosArmazens() == OperacaoDesenvolvidaSilosArmazenEnum.BENEFICIAMENTO.getId()){
						int i = 0;
						for(SilosArmazensOperacaoDesenvolvida opFilho: silosArmazen.getSilosArmazensOperacaoDesenvolvidas()){
							
							if(!Util.isNullOuVazio(opFilho.getOperacaoDesenvolvidaSilosArmazen().getIdeOperacaoDesenvolvidaPai())){
								i++;
								break;
							}
						}
						
						if(i==0){
							
							retorno = false;
							JsfUtil.addErrorMessage("Você selecionou a Operação beneficiamento, por favor selecione o Tipo de Beneficiamento!");
							break;
						}
					}
				}
			}
			
			if(Util.isNullOuVazio(silosArmazen.getIndQueimaCombustivel())){
				
				retorno = false;
				JsfUtil.addErrorMessage("O campo No empreendimento é realizada a queima de combustíveis para secagem? é de preenchimento obrigatório!");
			}
			
			if(silosArmazen.getIndQueimaCombustivel()){
				
				if(Util.isNullOuVazio(silosArmazen.getSilosArmazensTipoCombustivels())){
					
					retorno = false;
					JsfUtil.addErrorMessage("O campo Qual o tipo de combustível utilizado é de preenchimento obrigatório!");
				}
				
				if(!Util.isNullOuVazio(silosArmazen.getSilosArmazensTipoCombustivels())){
					
					for(SilosArmazensTipoCombustivel tipoComb:silosArmazen.getSilosArmazensTipoCombustivels()){
						
						if(tipoComb.getTipoCombustivelSiloArmazen().getIdeTipoCombustivelSiloArmazens() == TipoCombustivelSiloArmazenEnum.MADEIRA.getId()){
							
							int i = 0;
							for(SilosArmazensTipoCombustivel tipoCombFilho:silosArmazen.getSilosArmazensTipoCombustivels()){
								if(!Util.isNullOuVazio(tipoCombFilho.getTipoCombustivelSiloArmazen().getIdeTipoCombustivelSiloArmazensPai())){
									
									if(Util.isNullOuVazio(tipoCombFilho.getNumRaf())){
										
										retorno = false;
										JsfUtil.addErrorMessage("O campo "+ tipoCombFilho.getTipoCombustivelSiloArmazen().getNomTipoCombustivel() + 
												" - N° do RAF é de preenchimento Obrigatório!");
										
									}
									i++;
								}
								
							}
							
							if(i == 0){
								
								retorno = false;
								JsfUtil.addErrorMessage("Você selecionou o tipo de combustível Madeira, por favor selecione o tipo da Madeira!");
								break;
							}
						}
					}
				}
				
			}
			
			if(Util.isNullOuVazio(silosArmazen.getIndEmpreendimentoCaldeira())){
				
				retorno = false;
			}
			
		return retorno;
		
	}
	
	public Boolean validarAbaUnidadeArmazenadora(){
		
		boolean retorno = true;
		
		if(Util.isNullOuVazio(silosArmazen.getSilosArmazensUnidadeArmazenadoras())){
			
			retorno = false;
			JsfUtil.addErrorMessage("Incluir unidade armazenadora é de preenchimento obrigatório!");
		}
		
		return retorno;
	}
	
	public void onTabChangeEvent(TabChangeEvent event){
		
		String activeTab = event.getTab().getId();
		
		if("tabDocumentosEstudos".equals(activeTab)){
			carregarAbaDocumentosEstudos();
		}
		
	}
	
	private void limparCheckControlEmissoes(){
		if(!caracterizacaoAmbientalSilosArmazen.getIndMedidaControleEmissao()){
			caracterizacaoAmbientalSilosArmazen.setMedidaControleEmissaos(new ArrayList<MedidaControleEmissao>());
		}
		Html.atualizar("formSilosArmazen:tabViewSilosArmazens:gridMedida");

	}
	
	private void limparCheckSistemaTratamento(){
		if(!caracterizacaoAmbientalSilosArmazen.getIndSistemaTratamento()){
			caracterizacaoAmbientalSilosArmazen.setSistemaTratamentoAguas(new ArrayList<SistemaTratamentoAgua>());
		}
		Html.atualizar("formSilosArmazen:tabViewSilosArmazens:gridLancamentoEfluente");
	}
	
	private void limparCheckEfluente(){
		if(!caracterizacaoAmbientalSilosArmazen.getIndLancaEfluente()){
			silosArmazensLancamentoEfluente.setTipoDocumento(null);
			silosArmazensLancamentoEfluente.setNumDocumentoDispensa(null);
			silosArmazensLancamentoEfluente.setNumDocumentoPortaria(null);
			
/*			if(!Util.isNullOuVazio(cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList)){
				for(CadastroAtividadeNaoSujeitaLicDocApensado doc : cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList){
					if(DocumentoObrigatorioEnum.COPIA_PORTARIA_OUTORGA_LANCAMENTO_EFLUENTES_A_SER_REALIZADA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
						if(!Util.isNullOuVazio(doc.getUrlDocumento())){
							MensagemUtil.alerta("Essa alteração impactará os dados na aba de Documentos e Estudos.");
							break;
						}
					}else if(DocumentoObrigatorioEnum.COPIA_PORTARIA_OUTORGA_LANCAMENTO_EFLUENTES_A_SER_REALIZADA.getId().equals(doc.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
						if(!Util.isNullOuVazio(doc.getUrlDocumento())){
							MensagemUtil.alerta("Essa alteração impactará os dados na aba de Documentos e Estudos.");
							break;
						}
					}
				}
			}*/
		}
		
		Html.atualizar("formSilosArmazen:tabViewSilosArmazens:gridLancamentoEfluente");
	}
	
	public int size(List<Object> lista){
		return lista.size();
	}
	
	private boolean isAbaDadosBasicos() {
		return getActiveTab() == ABA_DADOS_BASICOS;
	}
	
	private boolean isAbaUnidadeArmazenadora() {
		return getActiveTab() == ABA_UNIDADE_ARMAZENADORA;
	}
	
	private boolean isCaracterizacaoAmbiental() {
		return getActiveTab() == ABA_CARACTERIZACAO_AMBIENTAL;
	}
	
	private boolean isAbaSistemaSeguranca() {
		return getActiveTab() == ABA_TELA_SEGURANCA;
	}
	
	private boolean isAbaDocumentosEstudos() {
		return getActiveTab() == ABA_TELA_DOCUMENTOS_ESTUDOS;
	}
	
	public int getSomentePontos() {
		return ClassificacaoSecaoEnum.COM_PONTO_OU_SHAPEFILE.getId().intValue();
	}
	
	public boolean isEmpreendimentoEditavel(){
		return isNovoCadastro();
	}
	
	public Boolean getPrimeiraDeclaracao() {
		return primeiraDeclaracao;
	}

	public void setPrimeiraDeclaracao(Boolean primeiraDeclaracao) {
		this.primeiraDeclaracao = primeiraDeclaracao;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public Boolean getSegundaDeclaracao() {
		return segundaDeclaracao;
	}

	public void setSegundaDeclaracao(Boolean segundaDeclaracao) {
		this.segundaDeclaracao = segundaDeclaracao;
	}

	public Boolean getTerceiraDeclaracao() {
		return terceiraDeclaracao;
	}

	public void setTerceiraDeclaracao(Boolean terceiraDeclaracao) {
		this.terceiraDeclaracao = terceiraDeclaracao;
	}

	public CadastroAtividadeNaoSujeitaLic getCadastro() {
		return cadastro;
	}

	public void setCadastro(CadastroAtividadeNaoSujeitaLic cadastro) {
		this.cadastro = cadastro;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public PessoaFisica getRequerentePessoaFisica() {
		return requerentePessoaFisica;
	}

	public void setRequerentePessoaFisica(PessoaFisica requerentePessoaFisica) {
		this.requerentePessoaFisica = requerentePessoaFisica;
	}

	public PessoaJuridica getRequerentePessoaJuridica() {
		return requerentePessoaJuridica;
	}

	public void setRequerentePessoaJuridica(PessoaJuridica requerentePessoaJuridica) {
		this.requerentePessoaJuridica = requerentePessoaJuridica;
	}

	public SilosArmazen getSilosArmazen() {
		return silosArmazen;
	}

	public void setSilosArmazen(SilosArmazen silosArmazen) {
		this.silosArmazen = silosArmazen;
	}

	public Endereco getEnderecoCorrespondencia() {
		return enderecoCorrespondencia;
	}

	public void setEnderecoCorrespondencia(Endereco enderecoCorrespondencia) {
		this.enderecoCorrespondencia = enderecoCorrespondencia;
	}

	public List<FormacaoProfissional> getListaFormacaoProfissional() {
		return listaFormacaoProfissional;
	}

	public void setListaFormacaoProfissional(
			List<FormacaoProfissional> listaFormacaoProfissional) {
		this.listaFormacaoProfissional = listaFormacaoProfissional;
	}

	public String getNumSicar() {
		return numSicar;
	}

	public void setNumSicar(String numSicar) {
		this.numSicar = numSicar;
	}

	public SilosArmazensImovel getSilosArmazensImovelRural() {
		return silosArmazensImovelRural;
	}

	public void setSilosArmazensImovelRural(SilosArmazensImovel silosArmazensImovelRural) {
		this.silosArmazensImovelRural = silosArmazensImovelRural;
	}

	public List<SilosArmazensImovel> getSilosArmazensImovelRuralList() {
		return silosArmazensImovelRuralList;
	}

	public void setSilosArmazensImovelRuralList(
			List<SilosArmazensImovel> silosArmazensImovelRuralList) {
		this.silosArmazensImovelRuralList = silosArmazensImovelRuralList;
	}

	public SilosArmazensImovel getExcluirArmazensImovelRural() {
		return excluirArmazensImovelRural;
	}

	public void setExcluirArmazensImovelRural(SilosArmazensImovel excluirArmazensImovelRural) {
		this.excluirArmazensImovelRural = excluirArmazensImovelRural;
	}

	public List<OperacaoDesenvolvidaSilosArmazen> getOperacaoDesenvolvidaSilosArmazenList() {
		return operacaoDesenvolvidaSilosArmazenList;
	}

	public void setOperacaoDesenvolvidaSilosArmazenList(
			List<OperacaoDesenvolvidaSilosArmazen> operacaoDesenvolvidaSilosArmazenList) {
		this.operacaoDesenvolvidaSilosArmazenList = operacaoDesenvolvidaSilosArmazenList;
	}

	public List<TipoCombustivelSiloArmazen> getTipoCombustivelSiloArmazensList() {
		return tipoCombustivelSiloArmazensList;
	}

	public void setTipoCombustivelSiloArmazensList(
			List<TipoCombustivelSiloArmazen> tipoCombustivelSiloArmazensList) {
		this.tipoCombustivelSiloArmazensList = tipoCombustivelSiloArmazensList;
	}

	public SilosArmazensUnidadeArmazenadora getSilosArmazensUnidadeArmazenadora() {
		return silosArmazensUnidadeArmazenadora;
	}

	public void setSilosArmazensUnidadeArmazenadora(
			SilosArmazensUnidadeArmazenadora silosArmazensUnidadeArmazenadora) {
		this.silosArmazensUnidadeArmazenadora = silosArmazensUnidadeArmazenadora;
	}

	public List<TipoEspecieArmazem> getTipoEspecieArmazems() {
		return tipoEspecieArmazems;
	}

	public void setTipoEspecieArmazems(List<TipoEspecieArmazem> tipoEspecieArmazems) {
		this.tipoEspecieArmazems = tipoEspecieArmazems;
	}

	public TipoEspecieArmazem getTipoEspecieArmazem() {
		return tipoEspecieArmazem;
	}

	public void setTipoEspecieArmazem(TipoEspecieArmazem tipoEspecieArmazem) {
		this.tipoEspecieArmazem = tipoEspecieArmazem;
	}

	public List<TipoArmazem> getTipoArmazems() {
		return tipoArmazems;
	}

	public void setTipoArmazems(List<TipoArmazem> tipoArmazems) {
		this.tipoArmazems = tipoArmazems;
	}

	public LocalizacaoGeografica getExcluirLocalizacaoUnidade() {
		return excluirLocalizacaoUnidade;
	}

	public void setExcluirLocalizacaoUnidade(LocalizacaoGeografica excluirLocalizacaoUnidade) {
		this.excluirLocalizacaoUnidade = excluirLocalizacaoUnidade;
	}

	public SilosArmazensUnidadeArmazenadora getExcluirArmazensUnidadeArmazenadora() {
		return excluirArmazensUnidadeArmazenadora;
	}

	public void setExcluirArmazensUnidadeArmazenadora(
			SilosArmazensUnidadeArmazenadora excluirArmazensUnidadeArmazenadora) {
		this.excluirArmazensUnidadeArmazenadora = excluirArmazensUnidadeArmazenadora;
	}

	public CaracterizacaoAmbientalSilosArmazen getCaracterizacaoAmbientalSilosArmazen() {
		return caracterizacaoAmbientalSilosArmazen;
	}

	public void setCaracterizacaoAmbientalSilosArmazen(
			CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		this.caracterizacaoAmbientalSilosArmazen = caracterizacaoAmbientalSilosArmazen;
	}

	public List<SilosArmazensOrigemAgua> getSilosArmazensOrigemAguas() {
		return silosArmazensOrigemAguas;
	}

	public void setSilosArmazensOrigemAguas(List<SilosArmazensOrigemAgua> silosArmazensOrigemAguas) {
		this.silosArmazensOrigemAguas = silosArmazensOrigemAguas;
	}

	public CaracterizacaoAmbientalOrigemAgua getCaracterizacaoAmbientalOrigemAgua() {
		return caracterizacaoAmbientalOrigemAgua;
	}

	public void setCaracterizacaoAmbientalOrigemAgua(
			CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAgua) {
		this.caracterizacaoAmbientalOrigemAgua = caracterizacaoAmbientalOrigemAgua;
	}

	public List<SistemaTratamentoAgua> getSistemaTratamentoAguaList() {
		return sistemaTratamentoAguaList;
	}

	public void setSistemaTratamentoAguaList(
			List<SistemaTratamentoAgua> sistemaTratamentoAguaList) {
		this.sistemaTratamentoAguaList = sistemaTratamentoAguaList;
	}

	public List<SilosArmazensCaracterizacaoAtmosferica> getSilosArmazensCaracterizacaoAtmosfericaList() {
		return silosArmazensCaracterizacaoAtmosfericaList;
	}

	public void setSilosArmazensCaracterizacaoAtmosfericaList(
			List<SilosArmazensCaracterizacaoAtmosferica> silosArmazensCaracterizacaoAtmosfericaList) {
		this.silosArmazensCaracterizacaoAtmosfericaList = silosArmazensCaracterizacaoAtmosfericaList;
	}

	public List<EquipamentoControle> getEquipamentoControleList() {
		return equipamentoControleList;
	}

	public void setEquipamentoControleList(List<EquipamentoControle> equipamentoControleList) {
		this.equipamentoControleList = equipamentoControleList;
	}

	public List<MedidaControleEmissao> getMedidaControleEmissaoList() {
		return medidaControleEmissaoList;
	}

	public void setMedidaControleEmissaoList(
			List<MedidaControleEmissao> medidaControleEmissaoList) {
		this.medidaControleEmissaoList = medidaControleEmissaoList;
	}

	public List<ClassificacaoResiduo> getClassificacaoResiduoList() {
		return classificacaoResiduoList;
	}

	public void setClassificacaoResiduoList(List<ClassificacaoResiduo> classificacaoResiduoList) {
		this.classificacaoResiduoList = classificacaoResiduoList;
	}

	public List<DestinacaoFinal> getDestinacaoFinalList() {
		return destinacaoFinalList;
	}

	public void setDestinacaoFinalList(List<DestinacaoFinal> destinacaoFinalList) {
		this.destinacaoFinalList = destinacaoFinalList;
	}

	public SilosArmazensLancamentoEfluente getSilosArmazensLancamentoEfluente() {
		return silosArmazensLancamentoEfluente;
	}

	public void setSilosArmazensLancamentoEfluente(
			SilosArmazensLancamentoEfluente silosArmazensLancamentoEfluente) {
		this.silosArmazensLancamentoEfluente = silosArmazensLancamentoEfluente;
	}

	public List<SilosArmazensSistemaSeguranca> getSilosArmazensSistemaSegurancasList() {
		return silosArmazensSistemaSegurancasList;
	}

	public void setSilosArmazensSistemaSegurancasList(
			List<SilosArmazensSistemaSeguranca> silosArmazensSistemaSegurancas) {
		this.silosArmazensSistemaSegurancasList = silosArmazensSistemaSegurancas;
	}

	public SistemaSegurancaSilosArmazen getSistemaSegurancaSilosArmazen() {
		return sistemaSegurancaSilosArmazen;
	}

	public void setSistemaSegurancaSilosArmazen(
			SistemaSegurancaSilosArmazen sistemaSegurancaSilosArmazen) {
		this.sistemaSegurancaSilosArmazen = sistemaSegurancaSilosArmazen;
	}

	public List<TipoConcessionaria> getTipoConcessionarias() {
		return tipoConcessionarias;
	}

	public void setTipoConcessionarias(List<TipoConcessionaria> tipoConcessionarias) {
		this.tipoConcessionarias = tipoConcessionarias;
	}

	public boolean isPrimeiraGridOrigem() {
		return primeiraGridOrigem;
	}

	public void setPrimeiraGridOrigem(boolean primeiraGridOrigem) {
		this.primeiraGridOrigem = primeiraGridOrigem;
	}

	public boolean isSegundaGridOrigem() {
		return segundaGridOrigem;
	}

	public void setSegundaGridOrigem(boolean segundaGridOrigem) {
		this.segundaGridOrigem = segundaGridOrigem;
	}

	public boolean isTerceiraGridOrigem() {
		return terceiraGridOrigem;
	}

	public void setTerceiraGridOrigem(boolean terceiraGridOrigem) {
		this.terceiraGridOrigem = terceiraGridOrigem;
	}

	public CaracterizacaoAmbientalOrigemAgua getCaracterizacaoAmbientalOrigemAguaPoco() {
		return caracterizacaoAmbientalOrigemAguaPoco;
	}

	public void setCaracterizacaoAmbientalOrigemAguaPoco(
			CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAguaPoco) {
		this.caracterizacaoAmbientalOrigemAguaPoco = caracterizacaoAmbientalOrigemAguaPoco;
	}

	public CaracterizacaoAmbientalOrigemAgua getCaracterizacaoAmbientalOrigemAguaRecurso() {
		return caracterizacaoAmbientalOrigemAguaRecurso;
	}

	public void setCaracterizacaoAmbientalOrigemAguaRecurso(
			CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAguaRecurso) {
		this.caracterizacaoAmbientalOrigemAguaRecurso = caracterizacaoAmbientalOrigemAguaRecurso;
	}

	public CaracterizacaoAmbientalOrigemAgua getCaracterizacaoAmbientalOrigemAguaConcessionaria() {
		return caracterizacaoAmbientalOrigemAguaConcessionaria;
	}

	public void setCaracterizacaoAmbientalOrigemAguaConcessionaria(
			CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAguaConcessionaria) {
		this.caracterizacaoAmbientalOrigemAguaConcessionaria = caracterizacaoAmbientalOrigemAguaConcessionaria;
	}

	public List<CadastroAtividadeNaoSujeitaLicDocApensado> getCadastroAtividadeNaoSujeitaLicDocApensadosList() {
		return cadastroAtividadeNaoSujeitaLicDocApensadosList;
	}

	public void setCadastroAtividadeNaoSujeitaLicDocApensadosList(
			List<CadastroAtividadeNaoSujeitaLicDocApensado> cadastroAtividadeNaoSujeitaLicDocApensadosList) {
		this.cadastroAtividadeNaoSujeitaLicDocApensadosList = cadastroAtividadeNaoSujeitaLicDocApensadosList;
	}

	public CadastroAtividadeNaoSujeitaLicDocApensado getCadastroAtividadeNaoSujeitaLicDocApensado() {
		return cadastroAtividadeNaoSujeitaLicDocApensado;
	}

	public void setCadastroAtividadeNaoSujeitaLicDocApensado(
			CadastroAtividadeNaoSujeitaLicDocApensado cadastroAtividadeNaoSujeitaLicDocApensado) {
		this.cadastroAtividadeNaoSujeitaLicDocApensado = cadastroAtividadeNaoSujeitaLicDocApensado;
	}

	public List<CadastroAtividadeNaoSujeitaLicDocApensado> getCadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList() {
		return cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList;
	}

	public void setCadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList(
			List<CadastroAtividadeNaoSujeitaLicDocApensado> cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList) {
		this.cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList = cadastroAtividadeNaoSujeitaLicDocApensadosDocumentosList;
	}

	public List<CadastroAtividadeNaoSujeitaLicDocApensado> getCadastroAtividadeNaoSujeitaLicDocApensadosEstudosList() {
		return cadastroAtividadeNaoSujeitaLicDocApensadosEstudosList;
	}

	public void setCadastroAtividadeNaoSujeitaLicDocApensadosEstudosList(
			List<CadastroAtividadeNaoSujeitaLicDocApensado> cadastroAtividadeNaoSujeitaLicDocApensadosEstudosList) {
		this.cadastroAtividadeNaoSujeitaLicDocApensadosEstudosList = cadastroAtividadeNaoSujeitaLicDocApensadosEstudosList;
	}

	public Boolean getIsUnidadeEditavel() {
		return isUnidadeEditavel;
	}

	public void setIsUnidadeEditavel(Boolean isUnidadeEditavel) {
		this.isUnidadeEditavel = isUnidadeEditavel;
	}

	public Boolean getIsCarteiraCPM() {
		return isCarteiraCPM;
	}

	public void setIsCarteiraCPM(Boolean isCarteiraCPM) {
		this.isCarteiraCPM = isCarteiraCPM;
	}

	public Boolean getIsVisivel() {
		return isVisivel;
	}

	public void setIsVisivel(Boolean isVisivel) {
		this.isVisivel = isVisivel;
	}

	public Boolean getTemImovelRural() {
		return temImovelRural;
	}

	public void setTemImovelRural(Boolean temImovelRural) {
		this.temImovelRural = temImovelRural;
	}

	public Boolean getEditarUnidadeArmazenadora() {
		return editarUnidadeArmazenadora;
	}

	public void setEditarUnidadeArmazenadora(Boolean editarUnidadeArmazenadora) {
		this.editarUnidadeArmazenadora = editarUnidadeArmazenadora;
	}

	public boolean isNovaInstanciaReponsalvelTecnico() {
		return isNovaInstanciaReponsalvelTecnico;
	}

	public void setNovaInstanciaReponsalvelTecnico(
			boolean isNovaInstanciaReponsalvelTecnico) {
		this.isNovaInstanciaReponsalvelTecnico = isNovaInstanciaReponsalvelTecnico;
	}


}
