package br.gov.ba.seia.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

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
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.PerfilService;
import br.gov.ba.seia.service.UsuarioExternoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

/**
 * Controller que deverá substituir o {@link UsuarioExternoController}
 * @author eduardo.fernandes
 * @since 22/09/2016
 */
/*@Named("usuarioExternoController")
@ViewScoped*/
public class UsuarioExternoControllerNew extends CadastrarUsuario {
	
	@EJB
	private UsuarioExternoService usuarioExternoService;
	
	@EJB
	private ParametroService parametroService;
	
	@EJB
	private PerfilService perfilService;
	
	private UsuarioExternoDTO usuarioExterno;
	private Perfil perfilUsuarioExterno;
	
	@Override
	public boolean isCPFcadastrado() {
		PessoaFisica pf = pessoaFisicaService.filtrarPessoaFisicaByCpf(usuarioExterno.getPessoaFisica());
		if(Util.isNullOuVazio(pf)) {
			return false;
		} 
		else if (Util.isNullOuVazio(pf.getUsuario())) {
			usuarioExterno.setPessoaFisica(pf);
			usuarioExterno.getUsuario().setPessoaFisica(pf);
			pf.setUsuario(usuarioExterno.getUsuario());
			return false;
		} 
		else {
			return true;
		}
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
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return totalRowCount;
	}
	@Override
	public void novoUsuario() {
		usuarioExterno = new UsuarioExternoDTO();
		limparTela();
	}

	@Override
	public void limparTela() {
		desabilitaCampos = false;
		super.limparTela();
	}
	@Override
	public void salvarAtualizarUsuario() {
	
		if (usuarioExterno.getUsuario().isValidaSenha() || editando) {

			try {
				if ((!Util.isNullOuVazio(usuarioExterno.getPessoaFisica().getIdeOcupacao()) && usuarioExterno.getPessoaFisica().getIdeOcupacao().equals("Selecione..."))
						|| Util.isNullOuVazio(usuarioExterno.getPessoaFisica().getDesNaturalidade())
						|| Util.isNullOuVazio(usuarioExterno.getPessoaFisica().getPessoa().getIdePessoa()) || Util.isNullOuVazio(usuarioExterno.getUsuario().getIdePessoaFisica())) {
					
					if (validarLogin()) {
						try {
							
							Parametro parametro = parametroService.obterPorId(ParametroEnum.USUARIO_EXTERNO.getIdeParametro());
							Integer idePerfilUsuarioExterno = parametroService.obterValorInt(parametro);
							
							if (!Util.isNull(idePerfilUsuarioExterno)) {
								perfilUsuarioExterno = perfilService.filtrarPerfilById(idePerfilUsuarioExterno);
							}
						} catch (Exception e) {
							Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
						} 
						
						perfilUsuarioExterno.setDtcCriacao(new Date());
						usuarioExterno.getUsuario().setIdePerfil(perfilUsuarioExterno);
						
						if (Util.isNullOuVazio(usuarioExterno.getPessoaFisica().getPessoa())) {
							usuarioExterno.getPessoaFisica().getPessoa().setDtcCriacao(new Date());
						}
						
						usuarioExternoService.salvarUsuarioExterno(usuarioExterno);
						
						if (!Util.isNullOuVazio(usuarioExterno.getPessoaFisica().getPessoa().getDesEmail())) {
							usuarioExternoService.enviarEmail(usuarioExterno.getPessoaFisica().getPessoa().getDesEmail(),usuarioExterno.getUsuario().getPessoaFisica().getNomPessoa(), usuarioExterno.getUsuario().getIdePessoaFisica().toString(), JsfUtil.getIpExterno(), usuarioExterno.getUsuario().getDscLogin());
						}
						
						RequestContext.getCurrentInstance().addPartialUpdateTarget(":formListaUsuariosExterno:dataTableUsuariosExterno");
						SessaoUtil.adicionarObjetoSessao("msgCadastroConcluido", "Usuário cadastrado com sucesso. Vocé receberá no seu e-mail um link para ativar sua conta.");
						JsfUtil.redirect("/login.xhtml?cadastroConcluido=true");
					} else {
						JsfUtil.addErrorMessage("Já existe um usuário cadastrado com este login. Favor informar outro.");
						getRequestContext().addCallbackParam("validationFailed", true);
						
						return;
					}
				} else {
					
					Usuario ue = usuarioExternoService.filtrarPorLoginCompleto(getUsuarioExterno().getUsuario().getDscLogin());
					
					if (!Util.isNullOuVazio(ue) && !Util.isNullOuVazio(ue.getPessoaFisica()) && !Util.isNullOuVazio(ue.getPessoaFisica().getNumCpf())
							&& !Util.isNullOuVazio(usuarioExterno.getPessoaFisica().getNumCpf())
							&& !ue.getPessoaFisica().getNumCpf().equals(usuarioExterno.getPessoaFisica().getNumCpf())) {
						
						JsfUtil.addErrorMessage("Já existe um usuário cadastrado com este login. Favor informar outro.");
						getRequestContext().addCallbackParam("validationFailed", true);
						
						return;
						
					} else {
						usuarioExternoService.atualizarUsuarioExterno(usuarioExterno);
						getRequestContext().execute("dlgUsuarioExterno.hide();");
						JsfUtil.addSuccessMessage("O cadastro foi atualizado com sucesso.");
						RequestContext.getCurrentInstance().addPartialUpdateTarget("panelUsuarioExterno");
					}
				}
			} catch (Exception exception) {
				JsfUtil.addErrorMessage(exception.getMessage());
			}
		} else {
			this.addMensagemErro("Favor informar senhas iguais!", null);
			getRequestContext().addCallbackParam("validationFailed", true);
		}
	}
	
	public void limparCampos() {
	
		usuarioExterno.getPessoaFisica().getPessoa().setDesEmail("");
		usuarioExterno.getPessoaFisica().setNumCpf("");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("formProblemaEmailUsuario");
	}
	
	public void excluirUsuarioExterno() {
	
		try {
			if (true) {
				usuarioExterno.getPessoaFisica().getUsuario().setIndExcluido(true);
				usuarioExterno.getPessoaFisica().getUsuario().setDtcExclusao(new Date());
				usuarioExternoService.atualizarUsuarioExterno(usuarioExterno);
				setUsuarioExterno(new UsuarioExternoDTO());
				pesquisarPessoasFisica();
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			JsfUtil.addErrorMessage("Este usuário possui pendências cadastrais e não pode ser removido!");
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
	
	public String cancelar() {
		return "/login.xhtml";
	}
	
	public UsuarioExternoDTO getUsuarioExterno() {
		if (Util.isNull(usuarioExterno)) usuarioExterno = new UsuarioExternoDTO();
		return usuarioExterno;
	}
	
	public void setUsuarioExterno(UsuarioExternoDTO usuarioExterno) {
		this.usuarioExterno = usuarioExterno;
	}

	@Override
	public boolean validarLogin(){
		return Util.isNullOuVazio(usuarioExternoService.filtrarPorLogin(getUsuarioExterno().getUsuario().getDscLogin()));
	}
}