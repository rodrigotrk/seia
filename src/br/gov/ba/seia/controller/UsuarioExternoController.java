package br.gov.ba.seia.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.dto.UsuarioExternoDTO;
import br.gov.ba.seia.entity.Escolaridade;
import br.gov.ba.seia.entity.EstadoCivil;
import br.gov.ba.seia.entity.Ocupacao;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaFisicaHistorico;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.PerfilService;
import br.gov.ba.seia.service.PessoaFisicaHistoricoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.UsuarioExternoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("usuarioExternoController")
@ViewScoped
public class UsuarioExternoController extends SeiaControllerAb {

	private String cpf;

	private String nome;

	private UsuarioExternoDTO usuarioExterno;

	private LazyDataModel<PessoaFisica> modelPessoasFisica;

	private Perfil perfilUsuarioExterno;

	private UIForm formularioASerLimpo;

	private boolean desabilitaCampos = true;

	private boolean editando;

	private Integer count;

	@EJB
	private UsuarioExternoService usuarioExternoService;

	@EJB
	private ParametroService parametroService;

	@EJB
	private PerfilService perfilService;

	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private PessoaFisicaHistoricoService pessoaFisicaHistoricoService;

	private Date dataAtual = new Date();

	public UsuarioExternoController() {
		count = 0;
	}

	public void consultarCpf() {

		if (!editando) {
			if (!cpfCadastrado()) {
				// habilita os campos
				desabilitaCampos = false;
			} else {
				// emite mensagem de erro
				desabilitaCampos = true;
				if (!Util.isNull(usuarioExterno.getPessoaFisica().getNumCpf())) {
					JsfUtil.addErrorMessage("CPF já cadastrado, favor entrar em contato com a Central de Atendimento.");
				}
			}
		}
	}
	
	public void consultarCpfCadastro() {

		if (!editando) {
			PessoaFisica pessoa = pessoaFisicaService.filtrarPessoaFisicaByCpfCadastro(usuarioExterno.getPessoaFisica());
			if(Util.isNullOuVazio(pessoa)){
				if (!cpfTelaCadastrado()) {
					// habilita os campos
					desabilitaCampos = false;
				} else {
					// emite mensagem de erro
					desabilitaCampos = true;
					if (!Util.isNull(usuarioExterno.getPessoaFisica().getNumCpf())) {
						JsfUtil.addErrorMessage("CPF já cadastrado, favor entrar em contato com a Central de Atendimento.");
					}
				}
			} else {
				if(Util.isNullOuVazio(pessoa.getUsuario())) {
					usuarioExterno.setPessoaFisica(pessoa);
					desabilitaCampos = false;
				}else {
					desabilitaCampos = true;
					JsfUtil.addErrorMessage("CPF já cadastrado, favor entrar em contato com a Central de Atendimento.");					
				}
			}
		}
	}

	public boolean cpfCadastrado() {

		Exception erro = null;
		try {
			PessoaFisica lPessoaFisica = pessoaFisicaService.filtrarPessoaFisicaByCpf(usuarioExterno.getPessoaFisica());
			if (Util.isNullOuVazio(lPessoaFisica)) {
				return false;
			} else if (Util.isNullOuVazio(lPessoaFisica.getUsuario())) {
				usuarioExterno.setPessoaFisica(lPessoaFisica);
				usuarioExterno.getUsuario().setPessoaFisica(lPessoaFisica);
				lPessoaFisica.setUsuario(usuarioExterno.getUsuario());
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);// log
		} finally {
			if (erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
		}
		return true;
	}

	public boolean cpfTelaCadastrado() {

		Exception erro = null;
		try {
			PessoaFisica lPessoaFisica = pessoaFisicaService.filtrarPessoaFisicaByCpfCadastro(usuarioExterno.getPessoaFisica());
			if (Util.isNullOuVazio(lPessoaFisica)) {
				return false;
			} else if (Util.isNullOuVazio(lPessoaFisica.getUsuario())) {
				usuarioExterno.setPessoaFisica(lPessoaFisica);
				usuarioExterno.getUsuario().setPessoaFisica(lPessoaFisica);
				lPessoaFisica.setUsuario(usuarioExterno.getUsuario());
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);// log
		} finally {
			if (erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
		}
		return true;
	}

	
	
	public void pesquisarPessoasFisica() {

		modelPessoasFisica = new LazyDataModel<PessoaFisica>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<PessoaFisica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {

				List<PessoaFisica> pessoasFisicas = null;

				try{
					pessoasFisicas = (List<PessoaFisica>) usuarioExternoService.filtrarListaPessoasFisicasPorDemanda(getPessoaFisicaParam(), first, pageSize);
				}
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
				return pessoasFisicas;
			}

		};
		modelPessoasFisica.setRowCount(getRowCountPessoasFisicas());
	}

	private PessoaFisica getPessoaFisicaParam() {
		PessoaFisica pf = new PessoaFisica(nome, cpf);
		pf.setUsuario(new Usuario(false));
		pf.getUsuario().setIndExcluido(false);
		return pf;
	}

	protected int getRowCountPessoasFisicas() {
		int totalRowCount = 0;
		try {
			totalRowCount = usuarioExternoService.filtrarListaPessoasFisicasCount(getPessoaFisicaParam());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return totalRowCount;
	}

	public void novoUsuarioExterno() {

		usuarioExterno = new UsuarioExternoDTO();
		limparTela();
	}

	public void limparTela() {

		desabilitaCampos = false;
		editando = true;
		limparComponentesFormulario(formularioASerLimpo);
	}



	public void salvarAtualizarUsuarioExterno() {

		if (editando || (usuarioExterno.getUsuario().isValidaSenha()
				&&  usuarioExterno.getPessoaFisica().getPessoa().isValidaEmail()
				&& usuarioExterno.getUsuario().isValidaLogin()) ) {
			Exception erro = null;
			try {
				if ((!Util.isNullOuVazio(usuarioExterno.getPessoaFisica().getIdeOcupacao()) && usuarioExterno.getPessoaFisica().getIdeOcupacao().equals("Selecione..."))
						|| Util.isNullOuVazio(usuarioExterno.getPessoaFisica().getDesNaturalidade())
						|| Util.isNullOuVazio(usuarioExterno.getPessoaFisica().getPessoa().getIdePessoa()) || Util.isNullOuVazio(usuarioExterno.getUsuario().getIdePessoaFisica())) {

					// SALVAR NOVO

					// Verifica se o login já existe
					if (verificaLogin()) {
						try {

							// Obtem o id do perfil de usuário externo da tabela de parametros.
							Parametro parametro = parametroService.obterPorId(ParametroEnum.USUARIO_EXTERNO.getIdeParametro());
							Integer idePerfilUsuarioExterno = parametroService.obterValorInt(parametro);

							if (!Util.isNull(idePerfilUsuarioExterno)) {
								perfilUsuarioExterno = perfilService.filtrarPerfilById(idePerfilUsuarioExterno);
							}
						} catch (Exception e) {
							erro = e;
							Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);// log
						} finally {
							if (erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
						}

						perfilUsuarioExterno.setDtcCriacao(new Date());
						usuarioExterno.getUsuario().setIdePerfil(perfilUsuarioExterno);

						if (Util.isNullOuVazio(usuarioExterno.getPessoaFisica().getPessoa())) {
							usuarioExterno.getPessoaFisica().getPessoa().setDtcCriacao(new Date());
						}

						usuarioExternoService.salvarUsuarioExterno(usuarioExterno, JsfUtil.getIpExterno());
						RequestContext.getCurrentInstance().addPartialUpdateTarget(":formListaUsuariosExterno:dataTableUsuariosExterno");
						SessaoUtil.adicionarObjetoSessao("msgCadastroConcluido", "Usuário cadastrado com sucesso. Vocé receberá no seu e-mail um link para ativar sua conta.");
						JsfUtil.redirect("/login.xhtml?cadastroConcluido=true");
					} else {
						JsfUtil.addErrorMessage("Já existe um usuário cadastrado com este login. Favor informar outro.");
						getRequestContext().addCallbackParam("validationFailed", true);

						return;
					}
				} else {

					// ATUALIZAR

					Usuario ue = usuarioExternoService.filtrarPorLoginCompleto(getUsuarioExterno().getUsuario().getDscLogin());

					if (!Util.isNullOuVazio(ue) && !Util.isNullOuVazio(ue.getPessoaFisica()) && !Util.isNullOuVazio(ue.getPessoaFisica().getNumCpf())
							&& !Util.isNullOuVazio(usuarioExterno.getPessoaFisica().getNumCpf())
							&& !ue.getPessoaFisica().getNumCpf().equals(usuarioExterno.getPessoaFisica().getNumCpf())) {

						JsfUtil.addErrorMessage("Já existe um usuário cadastrado com este login. Favor informar outro.");
						getRequestContext().addCallbackParam("validationFailed", true);

						return;

					} else {
						PessoaFisica dadosAnteriores = pessoaFisicaService.filtrarPessoaFisicaByCpfCadastro(usuarioExterno.getPessoaFisica());
						
						usuarioExternoService.atualizarUsuarioExterno(usuarioExterno);
						
						if(!dadosAnteriores.getNomPessoa().equals(usuarioExterno.getPessoaFisica().getNomPessoa()) || !dadosAnteriores.getPessoa().getDesEmail().equals(usuarioExterno.getPessoaFisica().getPessoa().getDesEmail())) {
							pessoaFisicaHistoricoService.salvarHistoricoPessoaFisica(preencherHistoricoPessoaFisica(dadosAnteriores));
						}
						
						getRequestContext().execute("dlgUsuarioExterno.hide();");
						JsfUtil.addSuccessMessage("O cadastro foi atualizado com sucesso.");
						RequestContext.getCurrentInstance().addPartialUpdateTarget("panelUsuarioExterno");
					}
				}
			} catch (Exception exception) {
				JsfUtil.addErrorMessage("Erro ao cadastrar usuário. Por favor, tente novamente.");
			}
		} else {
			getRequestContext().addCallbackParam("validationFailed", true);
		}
	}
	
	public PessoaFisicaHistorico preencherHistoricoPessoaFisica(PessoaFisica dadosAnteriores) {
		PessoaFisicaHistorico pessoaFisicaHistorico = new PessoaFisicaHistorico();
		
		pessoaFisicaHistorico.setIdePessoaFisica(usuarioExterno.getPessoaFisica());
		pessoaFisicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaFisicaHistorico.setDtcHistorico(new Date());
		
		if(!dadosAnteriores.getNomPessoa().equals(usuarioExterno.getPessoaFisica().getNomPessoa())){
			pessoaFisicaHistorico.setNomPessoa(usuarioExterno.getPessoaFisica().getNomPessoa());
		}
		
		if(!dadosAnteriores.getPessoa().getDesEmail().equals(usuarioExterno.getPessoaFisica().getPessoa().getDesEmail())){
			pessoaFisicaHistorico.setDesEmail(usuarioExterno.getPessoaFisica().getPessoa().getDesEmail());
		}
		
		return pessoaFisicaHistorico;
	}

	public void limparCampos() {

		usuarioExterno.getPessoaFisica().getPessoa().setDesEmail("");
		usuarioExterno.getPessoaFisica().setNumCpf("");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("formProblemaEmailUsuario");
	}


	private boolean verificaLogin() {

		Exception erro = null;

		try {
			if (Util.isNullOuVazio(usuarioExternoService.filtrarPorLogin(getUsuarioExterno().getUsuario().getDscLogin()))) {
				return true;
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);// log
		} finally {
			if (erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
		}

		return false;
	}

	public void excluirUsuarioExterno() {

		try {
			if (true) {
				usuarioExterno.getPessoaFisica().getUsuario().setIndExcluido(true);
				usuarioExterno.getPessoaFisica().getUsuario().setDtcExclusao(new Date());
				usuarioExternoService.atualizarUsuarioExterno(usuarioExterno);
				setUsuarioExterno(new UsuarioExternoDTO());
				//this.setModelPessoasFisica(null);
				pesquisarPessoasFisica();
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			JsfUtil.addErrorMessage("Este usuário possui pendências cadastrais e não pode ser removido!");
		}
	}

	public void validador(AjaxBehaviorEvent evt){

		if(!Util.isNullOuVazio(usuarioExterno.getUsuario().getDscLogin()) && !usuarioExterno.getUsuario().getDscLogin().matches("^[\\._a-z0-9]+$")){
			MensagemUtil.erro("Por favor insira somente caracteres validos!");
		}
	}

	public Collection<Escolaridade> getColEscolaridades() {

		return usuarioExternoService.filtrarListaEscolaridades(null);
	}

	public Collection<EstadoCivil> getColEstadosCivil() {

		return usuarioExternoService.filtrarListaEstadosCivil(null);
	}

	public Collection<Ocupacao> getColOcupacoes() {

		return usuarioExternoService.filtrarListaOcupacoes(null);
	}

	public Collection<Pais> getColPaises() {

		try {
			return usuarioExternoService.filtrarListaPaises(null);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return null;
	}

	public LazyDataModel<PessoaFisica> getModelPessoasFisica() {

		return modelPessoasFisica;
	}

	public String cancelar() {

		return "/login.xhtml";
	}

	public void setModelPessoasFisica(LazyDataModel<PessoaFisica> modelPessoasFisica) {

		this.modelPessoasFisica = modelPessoasFisica;
	}

	public UsuarioExternoDTO getUsuarioExterno() {

		if (Util.isNull(usuarioExterno)) usuarioExterno = new UsuarioExternoDTO();
		return usuarioExterno;
	}

	public void setUsuarioExterno(UsuarioExternoDTO usuarioExterno) {

		this.usuarioExterno = usuarioExterno;
	}

	public String getCpf() {

		return cpf;
	}

	public void setCpf(String cpf) {

		this.cpf = cpf;
	}

	public String getNome() {

		return nome;
	}

	public void setNome(String nome) {

		this.nome = nome.trim().replaceAll("  +", " ");
	}

	public UIForm getFormularioASerLimpo() {

		return formularioASerLimpo;
	}

	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {

		this.formularioASerLimpo = formularioASerLimpo;
	}

	public boolean isDesabilitaCampos() {

		return desabilitaCampos;
	}

	public void setDesabilitaCampos(boolean desabilitaCampos) {

		this.desabilitaCampos = desabilitaCampos;
	}

	public Date getDataAtual() {

		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {

		this.dataAtual = dataAtual;
	}

	public boolean getEditando() {

		return editando;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}