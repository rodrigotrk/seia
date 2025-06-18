package br.gov.ba.seia.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Level;

import br.gov.ba.seia.dao.EscolaridadeDAOIf;
import br.gov.ba.seia.dao.EstadoCivilDAOIf;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.OcupacaoDAOIf;
import br.gov.ba.seia.dao.PaisDAOImpl;
import br.gov.ba.seia.dao.PessoaFisicaDAOImpl;
import br.gov.ba.seia.dto.UsuarioExternoDTO;
import br.gov.ba.seia.entity.Escolaridade;
import br.gov.ba.seia.entity.EstadoCivil;
import br.gov.ba.seia.entity.Ocupacao;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UsuarioExternoService {

	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private UsuarioService usuarioService;
	@Inject
	private IDAO<Perfil> daoPerfil;
	@Inject
	private PaisDAOImpl paisDAOImpl;
	@Inject
	private EstadoCivilDAOIf estadoCivilDAOImpl;
	@Inject
	private OcupacaoDAOIf ocupacaoDAOImpl;
	@Inject
	private EscolaridadeDAOIf escolaridadeDAOImpl;
	@EJB
	private PessoaFisicaDAOImpl pessoaFisicaDAOImpl; 

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarUsuarioExterno(UsuarioExternoDTO pUsuarioExterno, String ip) throws Exception {
		pUsuarioExterno.getPessoaFisica().getNomPessoa().replaceAll("  +", " ");
		pUsuarioExterno.getPessoaFisica().setUsuario(null);
		pessoaFisicaService.salvarPessoaFisicaControleAcesso(pUsuarioExterno.getPessoaFisica());
		
		pUsuarioExterno.getUsuario().setPessoaFisica(pUsuarioExterno.getPessoaFisica());
		pUsuarioExterno.getUsuario().setIndTipoUsuario(false);
		pUsuarioExterno.getUsuario().setDscSenha(Util.toMD5(pUsuarioExterno.getUsuario().getDscSenha()));
		
		usuarioService.salvarUsuario(pUsuarioExterno.getUsuario());
		
		if (!Util.isNullOuVazio(pUsuarioExterno.getPessoaFisica().getPessoa().getDesEmail())) {
			this.enviarEmail(pUsuarioExterno.getPessoaFisica().getPessoa().getDesEmail(), pUsuarioExterno.getUsuario().getPessoaFisica().getNomPessoa(), pUsuarioExterno.getUsuario().getIdePessoaFisica().toString(), ip, pUsuarioExterno.getUsuario().getDscLogin());
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void enviarEmail(String pEmail, String pLoginUsuario, String idUsuario, String ip,String login) {
		try {
			idUsuario = Util.encodeBase64(Util.encodeSHA256(idUsuario).getBytes()).substring(0, 64).concat(Util.encodeBase64(Util.encrypt(idUsuario, Util.CHAVEENCRIPITACAO)));
			
			EmailService.enviarEmail(pEmail, pLoginUsuario, login ,"http://sistema.seia.ba.gov.br/login.xhtml?ativarUsuario=true&loginUsuario=" + idUsuario);
		} catch (EmailException e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		} catch (NoSuchAlgorithmException e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		} catch (UnsupportedEncodingException e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarUsuarioExterno(UsuarioExternoDTO pUsuarioExterno)  {
		pUsuarioExterno.getPessoaFisica().getUsuario().setIndTipoUsuario(false);
		usuarioService.atualizarUsuario(pUsuarioExterno.getPessoaFisica().getUsuario());
		pessoaFisicaService.atualizarPessoaFisicaControleAcesso(pUsuarioExterno.getPessoaFisica());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirUsuarioExterno(UsuarioExternoDTO pUsuarioExterno)  {
		usuarioService.excluirUsuarioSemAdmContainerDaEntidadeUsuario(pUsuarioExterno.getPessoaFisica().getUsuario());
	}

	public Collection<PessoaFisica> filtrarListaPessoasFisicas(PessoaFisica pPessoaFisica) {
		Collection<PessoaFisica> pfs;
		pfs = pessoaFisicaDAOImpl.filtrarPessoasFisicas(pPessoaFisica);
		return pfs;
	}
	
	public Integer filtrarListaPessoasFisicasCount(PessoaFisica pPessoaFisica) {
		return pessoaFisicaDAOImpl.filtrarPessoasFisicasCount(pPessoaFisica);
	}
	
	public Collection<PessoaFisica> filtrarListaPessoasFisicasPorDemanda(PessoaFisica pPessoaFisica, Integer startPage, Integer maxPage) {
		Collection<PessoaFisica> pfs;
		pfs = pessoaFisicaDAOImpl.filtrarPessoasFisicasPorDemanda(pPessoaFisica,startPage, maxPage);
		return pfs;
	}

	//Depend�ncias Pessoa F�sica
	public Collection<Escolaridade> filtrarListaEscolaridades(Escolaridade pEscolaridade) {
		return escolaridadeDAOImpl.getEscolaridades(pEscolaridade);
	}

	public Collection<EstadoCivil> filtrarListaEstadosCivil(EstadoCivil pEstadoCivil) {
		return estadoCivilDAOImpl.getEstadosCivil(pEstadoCivil);
	}

	public Collection<Ocupacao> filtrarListaOcupacoes(Ocupacao pOcupacao) {
		return ocupacaoDAOImpl.getOcupacoes(pOcupacao);
	}

	public Collection<Pais> filtrarListaPaises(Pais pPais) throws Exception {
		return paisDAOImpl.getPaises(pPais);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Usuario> filtrarPorLogin(String pLogin)  {
		return usuarioService.filtrarPorLogin(pLogin);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario filtrarPorLoginCompleto(String pLogin)  {
		return usuarioService.filtrarPorLoginCompleto(pLogin);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Usuario> listarPorEmailCPF(UsuarioExternoDTO pUsuarioExterno)  {
		return usuarioService.listarPorEmailCPF(pUsuarioExterno);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Perfil> filtrarListaPerfis(Perfil pPerfil) {
		return daoPerfil.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarUsuarioExterno(UsuarioExternoDTO pUsuarioExterno) throws Exception {
		
		pessoaFisicaService.salvarPessoaFisicaControleAcesso(pUsuarioExterno.getPessoaFisica());
		
		pUsuarioExterno.getUsuario().setPessoaFisica(pUsuarioExterno.getPessoaFisica());
		pUsuarioExterno.getUsuario().setIndTipoUsuario(false);
		pUsuarioExterno.getUsuario().setDscSenha(Util.toMD5(pUsuarioExterno.getUsuario().getDscSenha()));
		
		usuarioService.salvarUsuario(pUsuarioExterno.getUsuario());
		
	}
	
}