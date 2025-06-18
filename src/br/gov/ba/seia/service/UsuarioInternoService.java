package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.AreaDAOImpl;
import br.gov.ba.seia.dao.ControleTramitacaoDAOImpl;
import br.gov.ba.seia.dao.EscolaridadeDAOImpl;
import br.gov.ba.seia.dao.EstadoCivilDAOImpl;
import br.gov.ba.seia.dao.OcupacaoDAOImpl;
import br.gov.ba.seia.dao.PaisDAOImpl;
import br.gov.ba.seia.dao.PerfilDAOImpl;
import br.gov.ba.seia.dao.PessoaFisicaDAOImpl;
import br.gov.ba.seia.dao.UsuarioDAOImpl;
import br.gov.ba.seia.dto.UsuarioInternoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Escolaridade;
import br.gov.ba.seia.entity.EstadoCivil;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Ocupacao;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.TipoPauta;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UsuarioInternoService {

	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private PessoaService pessoaService;
	@EJB
	private FuncionarioService funcionarioService;
	@EJB
	private UsuarioService usuarioService;
	@EJB
	private ParametroService parametroService;
	@EJB
	private PautaService pautaService;
	@EJB
	private AreaService areaService;
	@EJB
	private PessoaFisicaDAOImpl pessoaFisicaDAOImpl;
	@EJB
	private PaisDAOImpl paisDAOImpl;
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarUsuarioInterno(UsuarioInternoDTO pUsuarioInterno) throws Exception {		
		pUsuarioInterno.getFuncionario().setPessoaFisica(pUsuarioInterno.getPessoaFisica());
		pessoaService.salvarPessoa(pUsuarioInterno.getPessoaFisica().getPessoa());
		pessoaFisicaService.salvarOuAtualizar(pUsuarioInterno.getPessoaFisica());
		pUsuarioInterno.getFuncionario().setPessoaFisica(pUsuarioInterno.getPessoaFisica());
		funcionarioService.salvarFuncionario(pUsuarioInterno.getFuncionario());		
		criaPautaValidaCoordenacaoUsuarioInterno(pUsuarioInterno);
		// No cadastro de usu�rio interno, o mesmo j� estar� automaticamente validado
		pUsuarioInterno.getUsuario().setIndValidacao(true);
		pUsuarioInterno.getUsuario().setPessoaFisica(pUsuarioInterno.getPessoaFisica());
		pUsuarioInterno.getUsuario().setIndTipoUsuario(true);
		pUsuarioInterno.getUsuario().setDscSenha(Util.toMD5(pUsuarioInterno.getUsuario().getDscSenha()));
		usuarioService.salvarUsuario(pUsuarioInterno.getUsuario());				
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarUsuarioInterno(UsuarioInternoDTO pUsuarioInterno) throws Exception {
		if(Util.isNullOuVazio(pUsuarioInterno.getFuncionario().getIdePessoaFisica())){
			pUsuarioInterno.getFuncionario().setPessoaFisica(pUsuarioInterno.getPessoaFisica());
			pUsuarioInterno.getFuncionario().setIdePessoaFisica(pUsuarioInterno.getPessoaFisica().getIdePessoaFisica());
			funcionarioService.salvarFuncionarioSomente(pUsuarioInterno.getFuncionario());
		}else{
			funcionarioService.atualizarFuncionario(pUsuarioInterno.getFuncionario());
		}
		pUsuarioInterno.getUsuario().setIndValidacao(true);
		pUsuarioInterno.getPessoaFisica().setUsuario(pUsuarioInterno.getUsuario());
		pUsuarioInterno.getPessoaFisica().getUsuario().setIndTipoUsuario(true);
		pessoaFisicaService.atualizarPessoaFisicaControleAcesso(pUsuarioInterno.getPessoaFisica());   
		criaPautaValidaCoordenacaoUsuarioInterno(pUsuarioInterno);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void criaPautaValidaCoordenacaoUsuarioInterno(UsuarioInternoDTO pUsuarioInterno) throws Exception {

		Pauta lPauta = pautaService.filtrarPauta(new Pauta(new Funcionario(pUsuarioInterno.getFuncionario().getIdePessoaFisica())));
		if (Util.isNullOuVazio(lPauta)) lPauta = new Pauta();
			
		lPauta.setIdePessoaFisica((pUsuarioInterno.getFuncionario()));
			
		if (PerfilEnum.COORDENADOR.getId().equals(pUsuarioInterno.getUsuario().getIdePerfil().getIdePerfil())
			|| PerfilEnum.COORD_CTGA.getId().equals(pUsuarioInterno.getUsuario().getIdePerfil().getIdePerfil())
			|| PerfilEnum.DIRETOR.getId().equals(pUsuarioInterno.getUsuario().getIdePerfil().getIdePerfil())) {
				
			//Ainda preciso vê uma implementação para o funcionário com a area nula
			if(Util.isNullOuVazio(pUsuarioInterno.getFuncionario().getIdeArea())){
				
			} else{
				//carregando os dados da nova area atribuída ao funcionario
				Area areaNova = areaService.carregarGet(pUsuarioInterno.getFuncionario().getIdeArea().getIdeArea());
				String msg = "";
				
				//verificando o perfil do usuário do funcionario
				if(PerfilEnum.DIRETOR.getId().equals(pUsuarioInterno.getUsuario().getIdePerfil().getIdePerfil())){
					lPauta.setIdeTipoPauta(new TipoPauta(TipoPautaEnum.PAUTA_DIRETOR_AREA.getTipo()));
					msg = "Diretor";
				}
				else{
					lPauta.setIdeTipoPauta(new TipoPauta(TipoPautaEnum.PAUTA_COORDENADOR_AREA.getTipo()));
					msg = "Coordenador";
				}
				
				//verificando se a area já tem um responsável
				if (!Util.isNullOuVazio(areaNova.getIdePessoaFisica())){
					if(!Util.isNullOuVazio(pUsuarioInterno.getPessoaFisica()) 
					   && !areaNova.getIdePessoaFisica().getIdePessoaFisica().equals(pUsuarioInterno.getPessoaFisica().getIdePessoaFisica())){
					   
						throw new AppExceptionError("Esta área já possui um " + msg + ".");
					}					
				}
				
				//verificando se o funcionário é coordenador de uma outra área
				Area areaAnterior = areaService.obterAreaFuncionarioCoordenadorPorIdeFuncionario(pUsuarioInterno.getPessoaFisica().getIdePessoaFisica());
				
				if(!Util.isNullOuVazio(areaAnterior)){
					if(!areaAnterior.getIdeArea().equals(areaNova.getIdeArea())) {
						areaAnterior.setIdePessoaFisica(null);
						areaService.merge(areaAnterior);
					}
				}
				
				//atribuindo nova área ao funcionário
				pUsuarioInterno.getFuncionario().setIdeArea(areaNova);
				pUsuarioInterno.getFuncionario().getIdeArea().setIdePessoaFisica(pUsuarioInterno.getFuncionario());
				lPauta.setIdeArea(pUsuarioInterno.getFuncionario().getIdeArea());
				
				if(!Util.isNullOuVazio(areaAnterior) && !Util.isNullOuVazio(areaAnterior.getIdeArea())){
					if(!areaAnterior.getIdeArea().equals(areaNova.getIdeArea())) {
						areaService.atualizar(pUsuarioInterno.getFuncionario().getIdeArea());
					}
				}
			}
		} else {
			if (!Util.isNullOuVazio(pUsuarioInterno.getFuncionario().getIdeArea()) 
				&& !Util.isNullOuVazio(pUsuarioInterno.getFuncionario().getIdeArea().getIdePessoaFisica())
				&& !Util.isNullOuVazio(pUsuarioInterno.getFuncionario().getIdeArea().getIdePessoaFisica().getIdePessoaFisica())
				&& pUsuarioInterno.getFuncionario().getIdePessoaFisica().equals(pUsuarioInterno.getFuncionario().getIdeArea().getIdePessoaFisica().getIdePessoaFisica())) {
				
				pUsuarioInterno.getFuncionario().getIdeArea().setIdePessoaFisica(null);
				areaService.atualizar(pUsuarioInterno.getFuncionario().getIdeArea());
			}
			lPauta.setIdeTipoPauta(new TipoPauta(TipoPautaEnum.PAUTA_TECNICA.getTipo()));
		}
		pautaService.salvar(lPauta);
	}

	public List<PessoaFisica> filtrarListaPessoasFisicas(PessoaFisica pPessoaFisica) throws Exception {
		List<PessoaFisica> pfs;
		pfs = new ArrayList<PessoaFisica>(pessoaFisicaDAOImpl.filtrarPessoasFisicas(pPessoaFisica));
		return pfs;
	}
	
	public Integer filtrarListaPessoasFisicasCount(PessoaFisica pPessoaFisica) throws Exception {
		return pessoaFisicaDAOImpl.filtrarPessoasFisicasCount(pPessoaFisica);
	}
	
	public Collection<PessoaFisica> filtrarListaPessoasFisicasPorDemanda(PessoaFisica pPessoaFisica, Integer startPage, Integer maxPage) throws Exception {
		Collection<PessoaFisica> pfs;
		pfs = pessoaFisicaDAOImpl.filtrarPessoasFisicasPorDemanda(pPessoaFisica,startPage, maxPage);
		return pfs;
	}

	//Depend�ncias Pessoa F�sica
	public Collection<Escolaridade> filtrarListaEscolaridades(Escolaridade pEscolaridade) {
		return new EscolaridadeDAOImpl().getEscolaridades(pEscolaridade);
	}

	public Collection<EstadoCivil> filtrarListaEstadosCivil(EstadoCivil pEstadoCivil) {
		return new EstadoCivilDAOImpl().getEstadosCivil(pEstadoCivil);
	}

	public Collection<Ocupacao> filtrarListaOcupacoes(Ocupacao pOcupacao) {
		return new OcupacaoDAOImpl().getOcupacoes(pOcupacao);
	}

	public Collection<Pais> filtrarListaPaises(Pais pPais) {
		try{
			return paisDAOImpl.getPaises(pPais);
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return null;
	}

	//Depend�ncias Funcion�rio
	public Collection<Area> filtrarListaAreas(Area pArea) {
		return new AreaDAOImpl().getAreas(pArea);
	}
	
	public Collection<Area> filtrarListaAreasTipo(Area pArea) {
		return new AreaDAOImpl().getAreasTipo(pArea);
	}

	//Depend�ncias Usu�rio
	public Collection<Perfil> filtrarListaPerfis(Perfil pPerfil) {
		return new PerfilDAOImpl().getPerfis(pPerfil);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Usuario> filtrarPorLogin(String pLogin) {
		return usuarioService.filtrarPorLogin(pLogin);
	}

	public boolean existeProcessoParaUsuarioComPerfilAdministrador(ControleTramitacao pControleTramitacao) {
		Parametro lParametroPerfilCoordenador = null;
		try {
			lParametroPerfilCoordenador = parametroService.obterPorId(ParametroEnum.IDE_PERFIL_COORDENADOR.getIdeParametro());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		if (!Util.isNullOuVazio(lParametroPerfilCoordenador)) {
			Usuario lUsuario = new UsuarioDAOImpl().getUsuario(new Usuario(pControleTramitacao.getIdePauta().getIdePessoaFisica().getIdePessoaFisica()));
			if (!Util.isNullOuVazio(lUsuario) && Integer.valueOf(lParametroPerfilCoordenador.getDscValor()).equals(lUsuario.getIdePerfil().getIdePerfil())) {
				return !Util.isNullOuVazio(new ControleTramitacaoDAOImpl().getControlesTramitacao(pControleTramitacao));
			}
		}
		return false;
	}
	
	/**
	 * Método que retorna um {@link Usuario} através da {@link PessoaFisica}
	 * 
	 * @author eduardo.fernandes
	 * @since 21/09/2012
	 */
	public Usuario carregarUsuarioByPessoaFisica(PessoaFisica idePessoaFisica) throws Exception{
		return usuarioService.buscarUsuarioPorPessoaFisica(idePessoaFisica);
	}
}