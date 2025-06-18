package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
public class UsuarioReativarService {

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
	@Inject
	private IDAO<PessoaFisica> pessoaFisicaDAO;


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarUsuarioExterno(UsuarioExternoDTO pUsuarioExterno) throws Exception {

		pessoaFisicaService.salvarPessoaFisicaControleAcesso(pUsuarioExterno.getPessoaFisica());
		pUsuarioExterno.getUsuario().setPessoaFisica(pUsuarioExterno.getPessoaFisica());
		pUsuarioExterno.getUsuario().setIndTipoUsuario(false);

		pUsuarioExterno.getUsuario().setDscSenha(Util.toMD5(pUsuarioExterno.getUsuario().getDscSenha()));

		usuarioService.salvarUsuario(pUsuarioExterno.getUsuario());

		if (!Util.isNullOuVazio(pUsuarioExterno.getPessoaFisica().getPessoa().getDesEmail())) {

			this.enviarEmail(pUsuarioExterno.getPessoaFisica().getPessoa().getDesEmail(), pUsuarioExterno.getUsuario().getDscLogin(), 
					pUsuarioExterno.getUsuario().getIdePessoaFisica().toString(), pUsuarioExterno.getUsuario().getPessoaFisica().getNomPessoa(), pUsuarioExterno.getUsuario().getDscLogin());
		}
	}

	public void enviarEmail(String pEmail, String pLoginUsuario, String idUsuario, String nomeUsuario, String login ) {
		try {
			EmailService.enviarEmail(pEmail,nomeUsuario, login,"http://sistema.seia.ba.gov.br/login.xhtml?ativarUsuario=true&loginUsuario=" + idUsuario);
		}
		catch (EmailException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarUsuario(UsuarioExternoDTO pUsuarioExterno) throws Exception {
		
		usuarioService.atualizarUsuario(pUsuarioExterno.getUsuario());
		usuarioService.apagarRegistroTentativa(pUsuarioExterno.getUsuario());
		pessoaFisicaService.atualizarPessoaFisicaControleAcesso(pUsuarioExterno.getPessoaFisica());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirUsuarioExterno(UsuarioExternoDTO pUsuarioExterno) throws Exception {

		usuarioService.excluirUsuarioSemAdmContainerDaEntidadeUsuario(pUsuarioExterno.getPessoaFisica().getUsuario());
	}

	public Collection<PessoaFisica> filtrarListaPessoasFisicas(PessoaFisica pPessoaFisica) throws Exception {
		pPessoaFisica.setUsuario(new Usuario(false));
		pPessoaFisica.getUsuario().setIndExcluido(true);
		return pessoaFisicaService.filtrarListaPessoasFisicasControleAcesso(pPessoaFisica, null);
	}
	
	public PessoaFisica carregarPessoaFisica(PessoaFisica p){
		
		return pessoaFisicaDAO.carregarGet(p.getIdePessoaFisica());
	}
	
	public Collection<PessoaFisica> filtrarListaPessoasFisicasExcluidas(PessoaFisica pPessoaFisica) throws Exception {		
				
		
		String sql ="SELECT pf " +
				    "FROM PessoaFisica pf " +
				    "WHERE pf.usuario.indExcluido=:indExcluido ";
		
		Map<String,Object> parametros =new HashMap<String, Object>();
		parametros.put("indExcluido", true);
		
		if(!Util.isNullOuVazio(pPessoaFisica.getNumCpf())){
			
			sql += "and pf.numCpf=:numCpf ";
			parametros.put("numCpf", pPessoaFisica.getNumCpf());
						
		}
		else if(!Util.isNullOuVazio(pPessoaFisica.getNomPessoa())){
			
			sql += "and upper(pf.nomPessoa) like :nomPessoa ";
			parametros.put("nomPessoa", pPessoaFisica.getNomPessoa().toUpperCase()+"%");
						
		}	
		
		Collection<PessoaFisica> pfs = pessoaFisicaDAO.listarPorQuery(sql, parametros);
		
		for(PessoaFisica pf : pfs){
			Ocupacao ocupacao = pf.getIdeOcupacao();
			if(ocupacao!=null){
				ocupacao.getNomOcupacao();
			}
			
		}
		
		return pfs;
	}
	

	//Dependencias Pessoa Fisica
	public Collection<Escolaridade> filtrarListaEscolaridades(Escolaridade pEscolaridade) {

		return escolaridadeDAOImpl.getEscolaridades(pEscolaridade);
	}
	public Collection<EstadoCivil> filtrarListaEstadosCivil(EstadoCivil pEstadoCivil) {

		return estadoCivilDAOImpl.getEstadosCivil(pEstadoCivil);
	}
	public Collection<Ocupacao> filtrarListaOcupacoes(Ocupacao pOcupacao) {

		return ocupacaoDAOImpl.getOcupacoes(pOcupacao);
	}	
	public Collection<Pais> filtrarListaPaises(Pais pPais) {

		try {
			return paisDAOImpl.getPaises(pPais);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Usuario> filtrarPorLogin(String pLogin) throws Exception {
		return usuarioService.filtrarPorLogin(pLogin);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Perfil> filtrarListaPerfis(Perfil pPerfil) {

		return daoPerfil.listarTodos();
	}
}