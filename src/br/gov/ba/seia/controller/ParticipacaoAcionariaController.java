package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.NaturezaJuridica;
import br.gov.ba.seia.entity.ParticipacaoAcionaria;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.enumerator.NaturezaJuridicaEnum;
import br.gov.ba.seia.enumerator.TipoAcionistaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ParticipacaoAcionariaService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("participacaoAcionariaController")
@ViewScoped
public class ParticipacaoAcionariaController extends SeiaControllerAb {

	private List<ParticipacaoAcionaria> participacaoAcionariaList;
	private ParticipacaoAcionaria participacaoAcionaria;
	private PessoaJuridica pessoaJuridica;
	private Pessoa pessoaPersist;
	private PessoaFisica pessoaFisicaPersist;
	private PessoaJuridica pessoaJuridicaPersist;

	private Boolean editMode;
	private Boolean incluiNovo;
	private Boolean telaCpf;
	private Boolean telaCnpj;
	private Boolean flagTableParticipacaoAcionaria;
	private Boolean disablePorcentagemPartAcio;
	private Boolean emptyList;
	private Boolean enableButtonAddParticipacaoAcionaria;
	private Boolean enableFormPessoaFisica;
	private Boolean enableFormPessoaJuridica;
	private Boolean enableBotaoPesquisa;
	
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private ParticipacaoAcionariaService participacaoAcionariaService;

	@PostConstruct
	public void init() {

		pessoaPersist = new Pessoa();
		pessoaFisicaPersist = new PessoaFisica();
		pessoaJuridica = new PessoaJuridica();
		pessoaJuridicaPersist = new PessoaJuridica();
		
		participacaoAcionaria = new ParticipacaoAcionaria();
		participacaoAcionaria.setIdePessoa(new Pessoa());
		participacaoAcionaria.getIdePessoa().setPessoaFisica(new PessoaFisica());
		participacaoAcionaria.getIdePessoa().setPessoaJuridica(new PessoaJuridica());
		participacaoAcionaria.getIdePessoa().getPessoaJuridica().setIdeNaturezaJuridica(new NaturezaJuridica());
		
		this.telaCpf = Boolean.TRUE;
		this.telaCnpj = Boolean.FALSE;
		
		
		enableFormPessoaFisica = Boolean.TRUE;
		enableFormPessoaJuridica = Boolean.TRUE;
		enableBotaoPesquisa = Boolean.TRUE;
		disablePorcentagemPartAcio = Boolean.TRUE;

		editMode = Boolean.FALSE;
		incluiNovo = Boolean.TRUE;
		
		tratarPessoaJuridicaSessao();
		carregarListaParticipacaoAcionaria();
		verificarEnableBotaoAddParticipacaoAcionaria();
	}
	
	private void tratarPessoaJuridicaSessao() {
		if (!Util.isNull(ContextoUtil.getContexto().getPessoaJuridica())) {
			
			pessoaJuridica = ContextoUtil.getContexto().getPessoaJuridica();
			
			if (Util.isNull(pessoaJuridica)) {
				pessoaJuridica = new PessoaJuridica();
			}
		}
	}
	
	public void carregarListaParticipacaoAcionaria() {
		try {
			this.participacaoAcionariaList = new ArrayList<ParticipacaoAcionaria>(participacaoAcionariaService.buscaParticipacaoAcionariaPorPessoaJuridica(this.pessoaJuridica));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void verificarEnableBotaoAddParticipacaoAcionaria() {
		if (!Util.isNull(pessoaJuridica.getIdeNaturezaJuridica()) && !Util.isNull(pessoaJuridica.getIdeNaturezaJuridica().getIdeNaturezaJuridica())) {
			
			if (!pessoaJuridica.getIdeNaturezaJuridica().getIdeNaturezaJuridica().equals(NaturezaJuridicaEnum.SOCIEDADE_ANONIMA.getId())
					&& (participacaoAcionariaService.calcularTotalParticipacaoAcionaria(pessoaJuridica).compareTo(new BigDecimal(100)) == -1)) {
				
				enableButtonAddParticipacaoAcionaria = Boolean.TRUE;
			} else {
				if (participacaoAcionariaService.calcularTotalParticipacaoAcionaria(pessoaJuridica).compareTo(new BigDecimal(100)) == 0) {
					enableButtonAddParticipacaoAcionaria = Boolean.FALSE;
				} else {
					enableButtonAddParticipacaoAcionaria = Boolean.TRUE;
				}
			}
		} else {
			enableButtonAddParticipacaoAcionaria = Boolean.TRUE;
		}
	}

	public void salvar() {
		if (!editMode && existePessoaNaComposicao()) {
			JsfUtil.addErrorMessage("Pessoa já cadastrada.");
			return;
		}
		
		try {
			if (participacaoAcionariaService.validateParticipacaoAcionaria(participacaoAcionaria, pessoaJuridica, editMode)) {
				if (telaCpf) {
					if (validatePessoaFisica()) {
						salvarPessoaFisica();
					} else {
						selecionarRadioPessoaFisica();
						return;
					}
				} else {
					if (validatePessoaJuridica()) {
						salvarPessoaJuridica();
					} else {
						selecionarRadioPessoaJuridica();
						return;
					}
				}

				verificarEnableBotaoAddParticipacaoAcionaria();
				carregarListaParticipacaoAcionaria();
				this.flagTableParticipacaoAcionaria = true;
				
				RequestContext.getCurrentInstance().execute("dialogCompAcionaria.hide()");
			} else {
				JsfUtil.addErrorMessage("O total das participações acionárias deve ser igual a 100%");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void salvarPessoaFisica() {
		try {
			if (Util.isNull(pessoaFisicaPersist.getIdePessoaFisica())) {
				pessoaPersist.setDtcCriacao(new Date());
				pessoaPersist.setIndExcluido(Boolean.FALSE);
				pessoaFisicaPersist.setPessoa(pessoaPersist);
				pessoaFisicaService.salvarPessoaFisica(pessoaFisicaPersist);
			} else {
				pessoaFisicaPersist.setPessoa(pessoaPersist);
				pessoaFisicaService.salvarOuAtualizarPessoaFisica(pessoaFisicaPersist);
			}
			
			salvarParticipacaoAcionaria(TipoAcionistaEnum.PESSOA_FISICA);
			limparObjetosSemMudarTela();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void salvarPessoaJuridica() {
		try {
			if (Util.isNull(pessoaJuridicaPersist.getIdePessoaJuridica())) {
				pessoaPersist.setDtcCriacao(new Date());
				pessoaPersist.setIndExcluido(Boolean.FALSE);
				pessoaJuridicaPersist.setPessoa(pessoaPersist);
				pessoaJuridicaService.salvarPessoaJuridica(pessoaJuridicaPersist);
			} else {
				pessoaJuridicaPersist.setPessoa(pessoaPersist);
				pessoaJuridicaService.salvarOuAtualizarPessoaJuridica(pessoaJuridicaPersist);
			}
			
			salvarParticipacaoAcionaria(TipoAcionistaEnum.PESSOA_JURIDICA);
			limparObjetosSemMudarTela();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private Boolean validatePessoaFisica() {
		Boolean retorno = null;
		Boolean teveErro = false;
		
		if (!Util.isNull(pessoaFisicaPersist.getNumCpf()) && !Util.isEmptyString(pessoaFisicaPersist.getNumCpf())) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo CPF é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
			teveErro = true;
		}
		
		if (!Util.isNull(pessoaFisicaPersist.getNomPessoa()) && !Util.isEmptyString(pessoaFisicaPersist.getNomPessoa())) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo Nome é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
			teveErro = true;
		}
		
		if (!Util.isNull(pessoaFisicaPersist.getIdePais())) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo Nacionalidade é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
			teveErro = true;
		}
		
		if (!Util.isNull(pessoaFisicaPersist.getDtcNascimento())) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo Data de Nascimento é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
			teveErro = true;
		}
		
		if (!Util.isNull(pessoaPersist.getDesEmail()) && !Util.isEmptyString(pessoaPersist.getDesEmail())) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo E-mail é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
			teveErro = true;
		}
		
		if (!Util.isNull(participacaoAcionaria.getPrcParticipacaoAcionaria()) && !(participacaoAcionaria.getPrcParticipacaoAcionaria().compareTo(new BigDecimal(0)) == 0)) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo Porcentagem Acionária é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
			teveErro = true;
		}
		
		return retorno && !teveErro;
	}

	private Boolean validatePessoaJuridica() {
		Boolean retorno = null;
		if (!Util.isNull(pessoaJuridicaPersist.getNumCnpj()) && !Util.isEmptyString(pessoaJuridicaPersist.getNumCnpj())) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo CNPJ é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}
		if (!Util.isNull(pessoaJuridicaPersist.getNomRazaoSocial()) && !Util.isEmptyString(pessoaJuridicaPersist.getNomRazaoSocial())) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo Razão Social é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}
		if (!Util.isNull(pessoaJuridicaPersist.getNomeFantasia()) && !Util.isEmptyString(pessoaJuridicaPersist.getNomeFantasia())) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo Nome Fantasia é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}
		if (!Util.isNull(pessoaJuridicaPersist.getIdeNaturezaJuridica())) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo Natureza Jurídica é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}
		if (!Util.isNull(pessoaJuridicaPersist.getDtcAbertura())) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo Data de Abertura é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}
		if (!Util.isNull(participacaoAcionaria.getPrcParticipacaoAcionaria()) && !(participacaoAcionaria.getPrcParticipacaoAcionaria().compareTo(new BigDecimal(0)) == 0)) {
			retorno = Boolean.TRUE;
		} else {
			JsfUtil.addErrorMessage("O campo Porcentagem Acionária é de preenchimento obrigatório.");
			retorno = Boolean.FALSE;
		}
		return retorno;
	}

	private void salvarParticipacaoAcionaria(TipoAcionistaEnum tipoAcionista) {
		try {
			String msg = "Inclusão efetuada com sucesso!";
			
			if (!Util.isNull(participacaoAcionaria.getIdeParticipacaoAcionaria())) {
				msg = "Alteração efetuada com sucesso!";
			}
			
			participacaoAcionaria.setIdePessoa(pessoaPersist);
			participacaoAcionaria.setIdePessoaJuridica(this.pessoaJuridica);
			participacaoAcionaria.setTipAcionista(tipoAcionista.getId());
			participacaoAcionaria.setDtcCriacao(new Date());
			participacaoAcionariaService.salvarParticipacaoAcionaria(this.participacaoAcionaria);
			
			JsfUtil.addSuccessMessage(msg);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void editarParticipacaoAcionaria(ParticipacaoAcionaria participacaoAcionaria) {
		this.participacaoAcionaria = participacaoAcionaria;
		RequestContext.getCurrentInstance().execute("dialogAcionaria.show()");
	}

	public void excluirParticipacaoAcionaria() throws Exception {
		
		try {
			participacaoAcionariaService.excluirParticipacaoAcionaria(participacaoAcionaria);
			carregarListaParticipacaoAcionaria();
			verificarEnableBotaoAddParticipacaoAcionaria();
			
			addMensagemInformativa("Participação Acionária excluída com sucesso", null);
		} catch (Exception e) {
			addMensagemErro("Erro ao excluir a participação acionária", e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void alterarLayout(ValueChangeEvent event) {
		Boolean value = (Boolean) event.getNewValue();
		if (value) {
			selecionarRadioPessoaFisica();
		} else {
			selecionarRadioPessoaJuridica();
		}
	}
	
	public void fecharDialogCompAcionaria() {
		
		limparObjetosSemMudarTela();
		
		RequestContext.getCurrentInstance().execute("dialogCompAcionaria.hide()");
	}
	
	public void limparObjetos() {
		limparObjetosSemMudarTela();
		selecionarRadioPessoaFisica();
		
		enableFormPessoaFisica = false;
		enableFormPessoaJuridica = false;
	}

	private void limparObjetosSemMudarTela() {
		participacaoAcionaria = new ParticipacaoAcionaria();
		pessoaPersist = new Pessoa();
		pessoaFisicaPersist = new PessoaFisica();
		pessoaJuridicaPersist = new PessoaJuridica();
	}

	public void pesquisarPessoaFisica() {
		try {
			
			String numCpf = pessoaFisicaPersist.getNumCpf();
			
			if (!Util.isNull(numCpf) && !Util.isEmptyString(numCpf)) {
				pessoaFisicaPersist = pessoaFisicaService.filtrarPessoaFisicaByCpf(pessoaFisicaPersist);
			
				if (!Util.isNull(pessoaFisicaPersist)) {
					pessoaPersist = pessoaFisicaPersist.getPessoa();
				} else {
					pessoaFisicaPersist = new PessoaFisica();
					pessoaFisicaPersist.setNumCpf(numCpf);
				}
				
				if (Util.isNull(pessoaFisicaPersist.getNomPessoa()) && Util.isEmptyString(pessoaFisicaPersist.getNomPessoa())
						|| Util.isNull(pessoaFisicaPersist.getIdePais())
						|| Util.isNull(pessoaFisicaPersist.getDtcNascimento())
						|| Util.isNull(pessoaPersist.getDesEmail()) && Util.isEmptyString(pessoaPersist.getDesEmail())) {
					
					enableFormPessoaFisica = true;
				}
				
				disablePorcentagemPartAcio = Boolean.FALSE;
			} else {
				JsfUtil.addErrorMessage("CPF é uma informação obrigatória.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void pesquisarPessoaJuridica() {
		try {
			
			String numCnpj = pessoaJuridicaPersist.getNumCnpj();
			
			if (!Util.isNull(numCnpj) && !Util.isEmptyString(numCnpj)) {
				pessoaJuridicaPersist = pessoaJuridicaService.filtrarPessoaFisicaByCnpj(pessoaJuridicaPersist);
				
				if (!Util.isNull(pessoaJuridicaPersist)) {
					pessoaPersist = pessoaJuridicaPersist.getPessoa();
				} else {
					pessoaJuridicaPersist = new PessoaJuridica();
					pessoaJuridicaPersist.setNumCnpj(numCnpj);
				}
				
				if (Util.isNull(pessoaJuridicaPersist.getNomRazaoSocial()) && Util.isEmptyString(pessoaJuridicaPersist.getNomRazaoSocial())
						|| Util.isNull(pessoaJuridicaPersist.getNomeFantasia()) && Util.isEmptyString(pessoaJuridicaPersist.getNomeFantasia())
						|| Util.isNull(pessoaJuridicaPersist.getIdeNaturezaJuridica())
						|| Util.isNull(pessoaJuridicaPersist.getDtcAbertura())) {
					
					enableFormPessoaJuridica = true;
				}
				
				disablePorcentagemPartAcio = Boolean.FALSE;
			} else {
				JsfUtil.addErrorMessage("CNPJ é uma informação obrigatória.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void prepararParaInclusao() {
		
		limparObjetos();
		
		editMode = false;
		incluiNovo = true;
		enableFormPessoaFisica = false;
		enableFormPessoaJuridica = false;
		enableBotaoPesquisa = true;
		disablePorcentagemPartAcio = false;
	}

	public void prepararParaEdicao() {
		try {
			participacaoAcionaria = participacaoAcionariaService.buscarParticipacaoAcionariaPorID(participacaoAcionaria.getIdeParticipacaoAcionaria());
			pessoaPersist = participacaoAcionaria.getIdePessoa();
			
			preparaRadioPFouPJ();
			
			editMode = true;
			incluiNovo = false;
			enableFormPessoaFisica = false;
			enableFormPessoaJuridica = false;
			enableBotaoPesquisa = true;
			disablePorcentagemPartAcio = false;
		} catch (Exception e) {
			addMensagemErro("Erro ao buscar o responsavel do empreendimento.", e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void prepararParaVisualizacao() {
		try {
			participacaoAcionaria = participacaoAcionariaService.buscarParticipacaoAcionariaPorID(participacaoAcionaria.getIdeParticipacaoAcionaria());
			pessoaPersist = participacaoAcionaria.getIdePessoa();
			
			preparaRadioPFouPJ();
			
			editMode = false;
			incluiNovo = false;
			enableFormPessoaFisica = false;
			enableFormPessoaJuridica = false;
			enableBotaoPesquisa = false;
			disablePorcentagemPartAcio = true;
		} catch (Exception e) {
			addMensagemErro("Erro ao buscar o responsavel do empreendimento.", e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void preparaRadioPFouPJ() {
		
		if (participacaoAcionaria.getTipAcionista() == TipoAcionistaEnum.PESSOA_FISICA.getId()) {
			pessoaFisicaPersist = pessoaPersist.getPessoaFisica();
			selecionarRadioPessoaFisica();
		} else {
			pessoaJuridicaPersist = pessoaPersist.getPessoaJuridica();
			selecionarRadioPessoaJuridica();
		}
	}

	private void selecionarRadioPessoaFisica() {
		telaCnpj = Boolean.FALSE;
		telaCpf = Boolean.TRUE;
	}

	private void selecionarRadioPessoaJuridica() {
		telaCnpj = Boolean.TRUE;
		telaCpf = Boolean.FALSE;
	}

	public Boolean existePessoaNaComposicao() {
		try {
			List<ParticipacaoAcionaria> lista = (List<ParticipacaoAcionaria>) participacaoAcionariaService.buscaParticipacaoAcionariaPorPessoaJuridica(this.pessoaJuridica);
			if (telaCpf) {
				return existePessoaFisicaNaComposicao(pessoaFisicaPersist, lista);
			}
			if (!telaCpf) {
				return existePessoaJuridicaNaComposicao(pessoaJuridicaPersist, lista);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return false;
	}

	private Boolean existePessoaFisicaNaComposicao(PessoaFisica pessoaFisica, List<ParticipacaoAcionaria> lista) {
		for (ParticipacaoAcionaria p : lista) {
			if (!Util.isNullOuVazio(p.getIdePessoa().getPessoaFisica()) && p.getIdePessoa().getPessoaFisica().getNumCpf().equals(pessoaFisicaPersist.getNumCpf())) {
				return true;
			}
		}
		return false;
	}

	private Boolean existePessoaJuridicaNaComposicao(PessoaJuridica pessoaJuridica, List<ParticipacaoAcionaria> lista) {
		for (ParticipacaoAcionaria p : lista) {
			if (!Util.isNullOuVazio(p.getIdePessoa().getPessoaJuridica()) && p.getIdePessoa().getPessoaJuridica().getNumCnpj().equals(pessoaJuridicaPersist.getNumCnpj())) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 
	 * 
	 * 
	 * GETS AND SETS
	 * 
	 * 
	 * 
	 */
	
	public ParticipacaoAcionaria getParticipacaoAcionaria() {
		return participacaoAcionaria;
	}

	public void setParticipacaoAcionaria(ParticipacaoAcionaria participacaoAcionaria) {
		this.participacaoAcionaria = participacaoAcionaria;
	}

	public Boolean getTelaCpf() {
		return telaCpf;
	}

	public void setTelaCpf(Boolean telaCpf) {
		this.telaCpf = telaCpf;
	}

	public Boolean getTelaCnpj() {
		return telaCnpj;
	}

	public void setTelaCnpj(Boolean telaCnpj) {
		this.telaCnpj = telaCnpj;
	}

	public Boolean getFlagTableParticipacaoAcionaria() {
		return flagTableParticipacaoAcionaria;
	}

	public void setFlagTableParticipacaoAcionaria(Boolean flagTableParticipacaoAcionaria) {
		this.flagTableParticipacaoAcionaria = flagTableParticipacaoAcionaria;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
		carregarListaParticipacaoAcionaria();
	}

	public Pessoa getPessoaPersist() {
		return pessoaPersist;
	}

	public void setPessoaPersist(Pessoa pessoaPersist) {
		this.pessoaPersist = pessoaPersist;
	}

	public PessoaFisica getPessoaFisicaPersist() {
		return pessoaFisicaPersist;
	}

	public void setPessoaFisicaPersist(PessoaFisica pessoaFisicaPersist) {
		this.pessoaFisicaPersist = pessoaFisicaPersist;
	}

	public PessoaJuridica getPessoaJuridicaPersist() {
		return pessoaJuridicaPersist;
	}

	public void setPessoaJuridicaPersist(PessoaJuridica pessoaJuridicaPersist) {
		this.pessoaJuridicaPersist = pessoaJuridicaPersist;
	}

	public Boolean getEnableButtonAddParticipacaoAcionaria() {
		return enableButtonAddParticipacaoAcionaria;
	}

	public void setEnableButtonAddParticipacaoAcionaria(Boolean enableButtonAddParticipacaoAcionaria) {
		this.enableButtonAddParticipacaoAcionaria = enableButtonAddParticipacaoAcionaria;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public Boolean getEnableBotaoPesquisa() {
		return enableBotaoPesquisa;
	}

	public void setEnableBotaoPesquisa(Boolean enableBotaoPesquisa) {
		this.enableBotaoPesquisa = enableBotaoPesquisa;
	}

	public Boolean getEnableFormPessoaFisica() {
		return enableFormPessoaFisica;
	}

	public void setEnableFormPessoaFisica(Boolean enableFormPessoaFisica) {
		this.enableFormPessoaFisica = enableFormPessoaFisica;
	}

	public Boolean getEnableFormPessoaJuridica() {
		return enableFormPessoaJuridica;
	}

	public void setEnableFormPessoaJuridica(Boolean enableFormPessoaJuridica) {
		this.enableFormPessoaJuridica = enableFormPessoaJuridica;
	}

	public Boolean getEmptyList() {
		if (participacaoAcionariaList.size() == 0) {
			emptyList = Boolean.FALSE;
		} else {
			emptyList = Boolean.TRUE;
		}
		return emptyList;
	}

	public void setEmptyList(Boolean emptyList) {
		this.emptyList = emptyList;
	}

	public Boolean getIncluiNovo() {
		return incluiNovo;
	}

	public void setIncluiNovo(Boolean incluiNovo) {
		this.incluiNovo = incluiNovo;
	}

	public Boolean getDisablePorcentagemPartAcio() {
		return disablePorcentagemPartAcio;
	}

	public void setDisablePorcentagemPartAcio(Boolean disablePorcentagemPartAcio) {
		this.disablePorcentagemPartAcio = disablePorcentagemPartAcio;
	}

	public List<ParticipacaoAcionaria> getParticipacaoAcionariaList() {
		return participacaoAcionariaList;
	}

	public void setParticipacaoAcionariaList(List<ParticipacaoAcionaria> participacaoAcionariaList) {
		this.participacaoAcionariaList = participacaoAcionariaList;
	}
}
