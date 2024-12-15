package br.gov.ba.seia.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.component.UIForm;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.dto.UsuarioInternoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Escolaridade;
import br.gov.ba.seia.entity.EstadoCivil;
import br.gov.ba.seia.entity.Ocupacao;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaFisicaHistorico;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.PessoaFisicaHistoricoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.UsuarioInternoService;
import br.gov.ba.seia.service.UsuarioService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("usuarioInternoController")
@ViewScoped
public class UsuarioInternoController extends SeiaControllerAb {

	private String cpf;
	private String nome;
	private UsuarioInternoDTO usuarioInterno;
	private LazyDataModel<PessoaFisica> modelPessoasFisica;
	private UIForm formularioASerLimpo;
	private boolean editando,trocaPerfil;
	private boolean erroAoSalvarUsuario = false;
	
	@EJB
	private UsuarioInternoService usuarioInternoService;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private FuncionarioService funcionarioService;
	
	@EJB
	private PessoaFisicaHistoricoService pessoaFisicaHistoricoService;

	public UsuarioInternoController() {
	}
	
	
	private boolean desabilitaForm = true;

	public void consultarCpf() {
		String cpf = usuarioInterno.getPessoaFisica().getNumCpf();
		setUsuarioInterno(new UsuarioInternoDTO());
		usuarioInterno.getPessoaFisica().setNumCpf(cpf);
		if (!Util.isNull(cpf)) {
			try {
				PessoaFisica pf =  pessoaFisicaService.filtrarPessoaFisicaByCpf(usuarioInterno.getPessoaFisica());
				if (!Util.isNullOuVazio(pf) && !Util.isNullOuVazio(pf.getIdePessoaFisica())) {
					usuarioInterno.setPessoaFisica(pf);
					usuarioInterno.setUsuario(usuarioService.carregar(new Usuario(pf.getIdePessoaFisica())));
					if(usuarioInterno.getUsuario().getIdePessoaFisica()!=null && !usuarioInterno.getUsuario().isUsuarioExterno()){
						desabilitaForm = true;
						this.trocaPerfil = false;
						JsfUtil.addErrorMessage("CPF já cadastrado, favor entrar em contato com a Central de Atendimento.");
					}
					else if(Util.isNullOuVazio(usuarioInterno.getUsuario().getIdePessoaFisica())){
						desabilitaForm = false;
					}
					else{
						desabilitaForm = false;
						this.trocaPerfil = true;
						usuarioInterno.getUsuario().setDscConfirmacaoSenha(usuarioInterno.getUsuario().getDscSenha());
						JsfUtil.addWarnMessage("Usuário cadastrado como externo, para alterar para interno preencha os dados e clique em salvar.");
					}
				}
				else {
					usuarioInterno.setPessoaFisica(new PessoaFisica("",cpf));
					desabilitaForm = false;
				}
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	public void pesquisarPessoasFisica() {
		
		modelPessoasFisica = new LazyDataModel<PessoaFisica>() {
			
			private static final long serialVersionUID = -2898632550041663086L;
			@Override
			public List<PessoaFisica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				
				List<PessoaFisica> pessoasFisicas = null;
				
				try{
					pessoasFisicas = (List<PessoaFisica>) usuarioInternoService.filtrarListaPessoasFisicasPorDemanda(getPessoaFisicaParam(), first, pageSize);
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
		pf.setUsuario(new Usuario(true));
		pf.getUsuario().setIndExcluido(false);
		return pf;
	}
	
	protected int getRowCountPessoasFisicas() {
		int totalRowCount = 0;
		try {
			totalRowCount = usuarioInternoService.filtrarListaPessoasFisicasCount(getPessoaFisicaParam());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return totalRowCount;
	}

	public void novoUsuarioInterno() {
		editando = false;
		setUsuarioInterno(new UsuarioInternoDTO());
	}

	public void limparTela() {
		editando = true;
		//		setUsuarioInterno(new UsuarioInternoDTO());
		limparComponentesFormulario(formularioASerLimpo);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarUsuarioInterno() {
		if (getEditando() || trocaPerfil || (getUsuarioInterno().getUsuario().isValidaSenha())) {
			try {
				if (Util.isNullOuVazio(getUsuarioInterno().getPessoaFisica().getPessoa().getIdePessoa())) {
					if (verificaLogin()) {
						
						if (Util.isNullOuVazio(usuarioInterno.getPessoaFisica().getPessoa())) {
							usuarioInterno.getPessoaFisica().getPessoa().setDtcCriacao(new Date());
						}
						
						
						usuarioInternoService.salvarUsuarioInterno(getUsuarioInterno());
						
						pessoaFisicaHistoricoService.salvarHistoricoPessoaFisica(preencherHistoricoPessoaFisicaNovoUsuario());
						
						JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
					} else {
						JsfUtil.addErrorMessage("Já existe um usuário cadastrado com este login. Favor informar outro.");
						getRequestContext().addCallbackParam("validationFailed", true);
						return;
					}
				} else {
					if(isPossivelAlterarPerfil()){
						PessoaFisica dadosAnteriores = pessoaFisicaService.filtrarPessoaFisicaByCpfCadastro(usuarioInterno.getPessoaFisica());
						
						usuarioInternoService.atualizarUsuarioInterno(usuarioInterno);
						
						if(!dadosAnteriores.getNomPessoa().equals(usuarioInterno.getPessoaFisica().getNomPessoa()) || !dadosAnteriores.getPessoa().getDesEmail().equals(usuarioInterno.getPessoaFisica().getPessoa().getDesEmail())) {
							pessoaFisicaHistoricoService.salvarHistoricoPessoaFisica(preencherHistoricoPessoaFisica(dadosAnteriores));
						}
						
						getRequestContext().execute("dlgUsuarioInterno.hide();");
						JsfUtil.addSuccessMessage("Alteração efetuada com sucesso!");
					} 
					else {
						exibirAlerta();
					}
				}
				
			}catch (EJBTransactionRolledbackException exception) {
				erroAoSalvarUsuario = true;
				if(exception.getMessage().contains("uq_usuario_dsc_login"))
					JsfUtil.addErrorMessage("Já existe um usuário cadastrado com este login. Favor informar outro.");
				else{
					JsfUtil.addErrorMessage("Erro ao salvar Usuário. Favor entrar em contato com o suporte.");
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
				}
			}catch (Exception exception) {
				erroAoSalvarUsuario = true;
				JsfUtil.addErrorMessage("Erro ao salvar Usuário. Favor entrar em contato com o suporte.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			}
		} else {
			this.addMensagemErro("Favor informar senhas iguais!", null);
			getRequestContext().addCallbackParam("validationFailed", true);
		}
	}
	
	public PessoaFisicaHistorico preencherHistoricoPessoaFisica(PessoaFisica dadosAnteriores) {
		PessoaFisicaHistorico pessoaFisicaHistorico = new PessoaFisicaHistorico();
		
		pessoaFisicaHistorico.setIdePessoaFisica(usuarioInterno.getPessoaFisica());
		pessoaFisicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaFisicaHistorico.setDtcHistorico(new Date());
		
		if(!dadosAnteriores.getNomPessoa().equals(usuarioInterno.getPessoaFisica().getNomPessoa())){
			pessoaFisicaHistorico.setNomPessoa(usuarioInterno.getPessoaFisica().getNomPessoa());
		}
		
		if(!dadosAnteriores.getPessoa().getDesEmail().equals(usuarioInterno.getPessoaFisica().getPessoa().getDesEmail())){
			pessoaFisicaHistorico.setDesEmail(usuarioInterno.getPessoaFisica().getPessoa().getDesEmail());
		}
		
		return pessoaFisicaHistorico;
	}
	
	public PessoaFisicaHistorico preencherHistoricoPessoaFisicaNovoUsuario() {
		PessoaFisicaHistorico pessoaFisicaHistorico = new PessoaFisicaHistorico();
		
		pessoaFisicaHistorico.setIdePessoaFisica(usuarioInterno.getPessoaFisica());
		pessoaFisicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaFisicaHistorico.setDtcHistorico(new Date());
		pessoaFisicaHistorico.setNomPessoa(usuarioInterno.getPessoaFisica().getNomPessoa());
		pessoaFisicaHistorico.setDesEmail(usuarioInterno.getPessoaFisica().getPessoa().getDesEmail());
		
		return pessoaFisicaHistorico;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void verificarErroDeInsercao(){
		if(erroAoSalvarUsuario){
			try{
				funcionarioService.excluirFuncionario(getUsuarioInterno().getFuncionario());
				pessoaFisicaService.excluirPessoaFisicaFisicamente(getUsuarioInterno().getPessoaFisica().getIdePessoaFisica());
				pessoaFisicaService.excluirPessoaFisicamente(getUsuarioInterno().getPessoaFisica().getIdePessoaFisica());
			}
			catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);					
			}			
		}
	}

	private boolean verificaLogin() {
		try {
			if (Util.isNullOuVazio(usuarioInternoService.filtrarPorLogin(getUsuarioInterno().getUsuario().getDscLogin()))) {
				return true;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return false;
	}

	public void excluirUsuarioInterno() {
		try {
			usuarioInterno.getPessoaFisica().getUsuario().setIndExcluido(true);
			usuarioInterno.getPessoaFisica().getUsuario().setDtcExclusao(new Date());
			usuarioInternoService.atualizarUsuarioInterno(usuarioInterno);
			setUsuarioInterno(new UsuarioInternoDTO());
			//this.setModelPessoasFisica(null);
			pesquisarPessoasFisica();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}


	/**
	 * Método para exibir a mensagem de alerta:
	 * <ul><i>Esta alteração foi bloqueada temporariamente, por favor entre em contato com o Suporte</i></ul>
	 * 
	 * @author eduardo.fernandes
	 * @since 21/09/2012
	 * @see <a href="http://10.105.12.26/redmine/issues/7857">#7857</a>
	 */
	private void exibirAlerta(){
		JsfUtil.addWarnMessage(Util.getString("cad_usuario_interno_alerta_temporario"));
		getRequestContext().addCallbackParam("validationFailed", true);
		return;
	}

	/**
	 * Método que realiza a verificação do {@link Perfil} a ser atualizado e se o {@link Usuario} que está sendo editado pode receber essa atualização.
	 * 
	 * @author eduardo.fernandes
	 * @throws Exception 
	 * @since 21/09/2012
	 * @see <a href="http://10.105.12.26/redmine/issues/7857">#7857</a>
	 */
	private boolean isPossivelAlterarPerfil() throws Exception{
		if(usuarioInterno.getUsuario().getIdePerfil().equals(new Perfil(PerfilEnum.COORDENADOR.getId())) || usuarioInterno.getUsuario().getIdePerfil().equals(new Perfil(PerfilEnum.DIRETOR.getId()))){
			Usuario usuarioPersitido = usuarioInternoService.carregarUsuarioByPessoaFisica(usuarioInterno.getPessoaFisica());
			Perfil tecnico = new Perfil(PerfilEnum.TECNICO.getId());
			if(usuarioPersitido.getIdePerfil().equals(tecnico)){
				usuarioInterno.getUsuario().setIdePerfil(tecnico);
				return false;
			}
		}
		return true;
	}

	public boolean isDesabilitadaCampoExisteProcessoParaUsuarioComPerfilAdministrador() {
		if (editando) {
			ControleTramitacao ct = new ControleTramitacao();
			ct.setIdePauta(new Pauta(usuarioInterno.getFuncionario()));
			ct.setIndFimDaFila(true);
			return usuarioInternoService.existeProcessoParaUsuarioComPerfilAdministrador(ct);
		}
		return false;
	}

	public Collection<Escolaridade> getColEscolaridades() {
		return usuarioInternoService.filtrarListaEscolaridades(null);
	}

	public Collection<EstadoCivil> getColEstadosCivil() {
		return usuarioInternoService.filtrarListaEstadosCivil(null);
	}

	public Collection<Ocupacao> getColOcupacoes() {
		return usuarioInternoService.filtrarListaOcupacoes(null);
	}

	public Collection<Pais> getColPaises() {
		return usuarioInternoService.filtrarListaPaises(null);
	}

	public Collection<Area> getColAreas() {
		Area area = new Area();		
		try{
		//	int perfilUsuarioDoCadastro = usuarioInterno.getUsuario().getIdePerfil().getIdePerfil();
			/*
			if(perfilUsuarioDoCadastro == PerfilEnum.DIRETOR.getId()){
				area.setIdeTipoArea(1);
			}
			else{
				area.setIdeTipoArea(2);
			}*/
			return usuarioInternoService.filtrarListaAreas(area);			
		}
		catch(Exception e){
			return usuarioInternoService.filtrarListaAreas(area);
		}
		
	}

	public Collection<Perfil> getColPerfis() {
		Collection<Perfil> perfisUsuarioInterno = usuarioInternoService.filtrarListaPerfis(new Perfil());
		perfisUsuarioInterno.remove(new Perfil(PerfilEnum.USUARIO_EXTERNO.getId()));
		return perfisUsuarioInterno;
	}

	public LazyDataModel<PessoaFisica> getModelPessoasFisica() {
		return modelPessoasFisica;
	}

	public void setModelPessoasFisica(LazyDataModel<PessoaFisica> modelPessoasFisica) {
		this.modelPessoasFisica = modelPessoasFisica;
	}

	public UsuarioInternoDTO getUsuarioInterno() {
		if (Util.isNull(usuarioInterno))
			usuarioInterno = new UsuarioInternoDTO();
		return usuarioInterno;
	}

	public void setUsuarioInterno(UsuarioInternoDTO usuarioInterno) {
		this.usuarioInterno = usuarioInterno;
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
		this.nome = nome;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}

	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}

	public boolean getEditando() {
		return editando;
	}

	public boolean isDesabilitaForm() {
		return desabilitaForm;
	}

	public void setDesabilitaForm(boolean desabilitaForm) {
		this.desabilitaForm = desabilitaForm;
	}

	public void setEditando(boolean editando) {
		this.editando = editando;
	}

	public boolean isTrocaPerfil() {
		return trocaPerfil;
	}

	public void setTrocaPerfil(boolean trocaPerfil) {
		this.trocaPerfil = trocaPerfil;
	}
	
}