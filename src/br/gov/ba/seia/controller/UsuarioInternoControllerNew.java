package br.gov.ba.seia.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

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
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.UsuarioInternoService;
import br.gov.ba.seia.service.UsuarioService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
/**
 * Controller que deverá substituir o {@link UsuarioInternoController}
 * @author eduardo.fernandes
 * @since 22/09/2016
 */
/*@Named("usuarioInternoController")
@ViewScoped*/
public class UsuarioInternoControllerNew extends CadastrarUsuario {

	@EJB
	private UsuarioInternoService usuarioInternoService;
	@EJB
	private UsuarioService usuarioService;
	@EJB
	private FuncionarioService funcionarioService;
	
	private UsuarioInternoDTO usuarioInterno;
	
	@Override
	public boolean isCPFcadastrado() {
		PessoaFisica pf =  pessoaFisicaService.filtrarPessoaFisicaByCpf(usuarioInterno.getPessoaFisica());
		if(Util.isNullOuVazio(pf)){
			usuarioInterno.setPessoaFisica(new PessoaFisica("",cpf));
			return false;
		}
		else {
			Usuario user = usuarioService.carregar(new Usuario(pf.getIdePessoaFisica()));
			if(Util.isNull(user)){
				usuarioInterno.setPessoaFisica(pf);
				usuarioInterno.getUsuario().setPessoaFisica(pf);
				pf.setUsuario(usuarioInterno.getUsuario());
				return false;
			} 
			else if(user.isUsuarioExterno()){
				JsfUtil.addWarnMessage("Usuário cadastrado como externo, para alterar para interno preencha os dados e clique em salvar.");
				return true;
			} else {
				JsfUtil.addErrorMessage("CPF já cadastrado, favor entrar em contato com a Central de Atendimento.");
				return true;
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
		naoValidaTela();
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

	@Override
	public void novoUsuario() {
		usuarioInterno = new UsuarioInternoDTO();
		super.limparTela();
	}

	@Override
	public void salvarAtualizarUsuario() {
		try {
			if(editando){
				if(isPossivelAlterarPerfil()){
					usuarioInternoService.atualizarUsuarioInterno(usuarioInterno);
					getRequestContext().execute("dlgUsuarioInterno.hide();");
					JsfUtil.addSuccessMessage("Alteração efetuada com sucesso!");
				} 
				else {
					exibirAlerta();
				}
			} 
			else {
				if(usuarioInterno.getUsuario().isValidaSenha()) {
					if(validarLogin()) {
						if(Util.isNullOuVazio(usuarioInterno.getPessoaFisica().getPessoa())) {
							usuarioInterno.getPessoaFisica().getPessoa().setDtcCriacao(new Date());
						}
						usuarioInternoService.salvarUsuarioInterno(usuarioInterno);
						JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
					} 
					else {
						JsfUtil.addErrorMessage("Já existe um usuário cadastrado com este login. Favor informar outro.");
						naoValidaTela();
					}
				}
				else {
					JsfUtil.addErrorMessage("Favor informar senhas iguais!");
					naoValidaTela();
				}
			}
		} 
		catch (EJBTransactionRolledbackException exception) {
			if(exception.getMessage().contains("uq_usuario_dsc_login")){
				JsfUtil.addErrorMessage("Já existe um usuário cadastrado com este login. Favor informar outro.");
				naoValidaTela();
			}
			else{
				JsfUtil.addErrorMessage("Erro ao salvar Usuário. Favor entrar em contato com o suporte.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
//				erroAoSalvarUsuario = true; TODO
			}
		}
		catch (Exception exception) {
//			erroAoSalvarUsuario = true; TODO
			JsfUtil.addErrorMessage("Erro ao salvar Usuário. Favor entrar em contato com o suporte.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	} 

	private void naoValidaTela() {
		getRequestContext().addCallbackParam("validationFailed", true);
		return;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void verificarErroDeInsercao(){
//		if(erroAoSalvarUsuario){ TODO
			try{
				funcionarioService.excluirFuncionario(usuarioInterno.getFuncionario());
				pessoaFisicaService.excluirPessoaFisicaFisicamente(usuarioInterno.getPessoaFisica().getIdePessoaFisica());
				pessoaFisicaService.excluirPessoaFisicamente(usuarioInterno.getPessoaFisica().getIdePessoaFisica());
			}
			catch(Exception e){
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);					
			}			
//		} TODO
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

	@Override
	public boolean validarLogin() {
		return Util.isNullOuVazio(usuarioInternoService.filtrarPorLogin(usuarioInterno.getUsuario().getDscLogin()));
	}
	
}