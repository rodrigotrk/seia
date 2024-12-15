package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.PessoaFisicaDAOImpl;
import br.gov.ba.seia.dao.PessoaFisicaHistoricoDAOImpl;
import br.gov.ba.seia.dto.UsuarioInternoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaFisicaHistorico;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.auditoria.FiltroAuditoria;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaFisicaService extends PessoaService {

	@EJB
	private PessoaFisicaDAOImpl pessoaFisicaDAOImpl;
	
	@Inject
	private IDAO<PessoaFisica> pessoaFisicaDAO;
	
	@EJB
	private PermissaoPerfilService permissaoPerfilService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private ImovelRuralService imovelRuralService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isUsuarioConvenio() {
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(PessoaFisica pessoaFisica)  {
		try {
			if(pessoaFisica.getPessoa().getDtcCriacao()==null){
				pessoaFisica.getPessoa().setDtcCriacao(new Date());
			}
			pessoaFisica.getPessoa().setIndExcluido(true);
			pessoaFisicaDAOImpl.salvarOuAtualizar(pessoaFisica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean validarDadosBasicosPessoaFisica(PessoaFisica pessoaFisica)  {
		boolean resultado = true;
		if(Util.isNullOuVazio(pessoaFisica.getDtcNascimento()) ||
		   Util.isNullOuVazio(pessoaFisica.getNomMae()) ||
		   Util.isNullOuVazio(pessoaFisica.getDesNaturalidade())|| 
		   Util.isNullOuVazio(pessoaFisica.getIdePais()	)){
			resultado = false;
		}
		return resultado;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaFisica(PessoaFisica pessoaFisica)  {
		imovelRuralService.validarVinculoComImovel(pessoaFisica.getPessoa());
		if (!requerimentoService.verificarRequerimentoPessoa(pessoaFisica.getPessoa())) {

			if((empreendimentoService.listarEmpreendimento(pessoaFisica.getPessoa()).isEmpty())) {
			
				pessoaFisicaDAOImpl.excluirPessoaFisica(pessoaFisica);
				
			} else {
				throw new AppExceptionError("Não é permitida a exclusão, pois a pessoa possui um empreendimento em seu nome.");
			}
		} else {
			throw new AppExceptionError("Não é permitida a exclusão, pois a pessoa possui uma solicitação aberta em seu nome ou representa alguém em uma solicitação.");
		}
	}
	/**
	 * Método responsável por excluir uma pessoa física da base de dados de forma permanente.
	 * @param idePessoaFisica Id da pessoa física a ser removida da base de dados.
	 * @exception Exception
	 * @author Lucas Oliveira Monteiro
	 * */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaFisicaFisicamente(Integer idePessoaFisica)  {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idePessoaFisica", idePessoaFisica);
		StringBuilder sql = new StringBuilder();
		sql.append("delete from PessoaFisica pf where pf.idePessoaFisica = :idePessoaFisica ");
		pessoaFisicaDAO.executarQuery(sql.toString(), params);
	}
	/**
	 * Método responsável por excluir uma pessoa da base de dados de forma permanente.
	 * @param idePessoa Id da pessoa a ser removida da base de dados.
	 * @exception Exception
	 * @author Lucas Oliveira Monteiro
	 * */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaFisicamente(Integer idePessoa)  {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idePessoa", idePessoa);
		StringBuilder sql = new StringBuilder();
		sql.append("delete from Pessoa p where p.idePessoa = :idePessoa ");
		pessoaFisicaDAO.executarQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> listarPessoaFisicaPorArea(Area ideArea)  {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideArea", ideArea);
		params.put("indTipoUsuario", true);
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select pf ");
		sql.append("from PessoaFisica pf ");
		sql.append("     inner join pf.funcionario func");
		sql.append("     inner join pf.usuario u ");
		sql.append("where u.indTipoUsuario = :indTipoUsuario ");
		sql.append("      and func.ideArea = :ideArea ");
		sql.append("order by pf.nomPessoa asc ");
		
		return pessoaFisicaDAO.listarPorQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica buscarPessoaFisica(PessoaFisica idePessoaFisica)  {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idePessoaFisica", idePessoaFisica);
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select p ");
		sql.append("from PessoaFisica p ");
		sql.append("where p = :idePessoaFisica ");
		
		return pessoaFisicaDAO.buscarPorQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica buscarPessoaFisicaByCPF(PessoaFisica pessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class);
		criteria.add(Restrictions.eq("numCpf", pessoaFisica.getNumCpf()));
		return pessoaFisicaDAO.buscarPorCriteria(criteria);
	}
	

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica buscarPessoaFisicaByIde(Integer idePessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class);
		criteria.add(Restrictions.eq("idePessoaFisica", idePessoaFisica));
		return pessoaFisicaDAO.buscarPorCriteria(criteria);
	}	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> listarPessoaFisica()  {
		List<Integer> listaIdesPessoaFisicaAptos = permissaoPerfilService.listarIdesPessoaFisicaAptos();
		if(!listaIdesPessoaFisicaAptos.isEmpty()){
			return  pessoaFisicaDAOImpl.listarPessoaFisica(listaIdesPessoaFisicaAptos);
		}else{
			return new ArrayList<PessoaFisica>(); 
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaFisica> filtrarListaPessoaFisica(PessoaFisica pessoa)  {
		return pessoaFisicaDAOImpl.filtrarListaPessoaFisica(pessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica retornarPessoaFisicaResponsavelArea(Area area) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideArea", area.getIdeArea());
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select a.idePessoaFisica.pessoaFisica ");
		sql.append("from Area a ");
		sql.append("where a.ideArea = :ideArea ");
		
		PessoaFisica pf;
		Exception erro = null;
		try{
			pf = pessoaFisicaDAO.buscarPorQuery(sql.toString(), params);
		}
		catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			pf=new PessoaFisica();
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return pf;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica consultarPessoaFisicaByNumCpf(String numCpf)  {
		return  pessoaFisicaDAOImpl.consultarPessoaFisicaByNumCpf(numCpf);	
	}	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica consultarPessoaFisicaInativaByNumCpf(String numCpf)  {
		return  pessoaFisicaDAOImpl.consultarPessoaFisicaInativaByNumCpf(numCpf);	
	}	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica filtrarPessoaFisicaByCpf(PessoaFisica pessoa)  {
		return  pessoaFisicaDAOImpl.filtrarPessoaFisicaByCpf(pessoa);	
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica filtrarPessoaFisicaByCpfCadastro(PessoaFisica pessoa)  {
		return  pessoaFisicaDAOImpl.filtrarPessoaFisicaByCpfCadastro(pessoa);	
	}	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> consultarPessoaFisicaOrByNomeOrCpf(PessoaFisica pessoa) {
		return pessoaFisicaDAOImpl.consultarPessoaFisicaOrByNomeOrCpf(pessoa, permissaoPerfilService.listarIdesPessoaFisicaAptos());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> filtrarPessoasFisicaByCpfOrNome(PessoaFisica pessoa)  {		
		return pessoaFisicaDAOImpl.filtrarPessoasFisicaByCpfOrNome(pessoa, permissaoPerfilService.listarIdesPessoaFisicaAptos());	
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica)  {
		pessoaFisicaDAOImpl.salvarPessoaFisica(pessoaFisica);
		return pessoaFisica;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaFisica salvarOuAtualizarPessoaFisica(PessoaFisica pessoaFisica)  {
		pessoaFisicaDAOImpl.salvarOuAtualizarPessoaFisica(pessoaFisica);
		return pessoaFisica;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean existeCadastro(PessoaFisica pessoaFisica) {	
		return pessoaFisicaDAOImpl.existeCadastro(pessoaFisica);
	}	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica filtrarPessoaFisicaByIdControleAcesso(PessoaFisica pPessoaFisica)  {
		return this.pessoaFisicaDAOImpl.filtrarPessoaFisicaByIdControleAcesso(pPessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica filtrarPessoaFisicaByAreaFuncionarioPerfilTipoUsuario(UsuarioInternoDTO pUsuarioInterno)  {
		return this.pessoaFisicaDAOImpl.filtrarPessoaFisicaByAreaFuncionarioPerfilTipoUsuario(pUsuarioInterno);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> filtrarListaPessoasFisicasControleAcesso(PessoaFisica pPessoaFisica, Boolean indTipoUsuario)  {
		return this.pessoaFisicaDAOImpl.listarPessoasFisicaFuncionarioControleDeAcesso(pPessoaFisica, indTipoUsuario);
		
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> filtrarListaPessoasFisicasExcluidas(PessoaFisica pPessoaFisica)  {
		return this.pessoaFisicaDAOImpl.listarPessoasFisicaFuncionario(pPessoaFisica);		
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaFisica salvarPessoaFisicaControleAcesso(PessoaFisica pPessoaFisica)  {
		return this.pessoaFisicaDAOImpl.salvarOuAtualizarPessoaFisica(pPessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaFisica atualizarPessoaFisicaControleAcesso(PessoaFisica pPessoaFisica)  {
		return this.pessoaFisicaDAOImpl.atualizarPessoaFisicaControleAcesso(pPessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaFisicaControleAcesso(PessoaFisica pessoaFisica)  {
		this.pessoaFisicaDAOImpl.excluirPessoaFisicaControleAcesso(pessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaFisicaSemAdmContainerDaEntidadePessoaFisicaControleAcesso(PessoaFisica pessoaFisica)  {
		pessoaFisicaDAOImpl.excluirPessoaFisicaSemAdmContainerDaEntidadePessoaFisicaControleAcesso(pessoaFisica);	
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica filtrarPessoaFisicaByCpfControleAcesso(PessoaFisica pessoa)  {
		return pessoaFisicaDAOImpl.filtrarPessoaFisicaByCpfControleAcesso(pessoa);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> listarPessoaFisicaControleAcessoControleAcesso()  {
		return pessoaFisicaDAOImpl.listarPessoaFisicaControleAcessoControleAcesso();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaFisica> filtrarListaPessoaFisicaControleAcessoControleAcesso(PessoaFisica pessoa)  {
		return pessoaFisicaDAOImpl.filtrarListaPessoaFisicaControleAcesso(pessoa);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica carregarPessoaFisicaGet(Integer pIdePessoaFisica)  {
		return pessoaFisicaDAOImpl.carregarPessoaFisicaGet(pIdePessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaFisica> listarPorCriteriaDemanda(PessoaFisica pessoaFisica, Integer startPage, Integer maxPage) {	
		return pessoaFisicaDAOImpl.listarPorCriteriaDemanda(pessoaFisica, startPage, maxPage);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer count(PessoaFisica pessoaFisica) {		
		return pessoaFisicaDAOImpl.count(pessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> filtrarPessoaRequerente(Integer firstPage,Integer pageSize,PessoaFisica pPessoa)  {
		return pessoaFisicaDAOImpl.getPessoasFisicaRequerente(firstPage,pageSize,pPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> filtrarPessoaRequerenteSemAcento(PessoaFisica pPessoa)  {
		return pessoaFisicaDAOImpl.getPessoasFisicaRequerenteSemAcento(pPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int countFiltroPessoaFisicaSolicitante(PessoaFisica pPessoa)  {
		return pessoaFisicaDAOImpl.countFiltroPessoaFisicaSolicitante(pPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica carregarTudo(final PessoaFisica pessoaFisica) throws CampoObrigatorioException  {
		if(pessoaFisica == null) {
			throw new CampoObrigatorioException("Parâmetro Pessoa Física é obrigatório");
		}else if(Util.isNull(pessoaFisica.getIdePessoaFisica())) {
			throw new CampoObrigatorioException("Id da Pessoa Física é obrigatório");
		}
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaFisica.class);
		criteria.createAlias("ideEscolaridade", "escolaridade", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("idePessoaFisica", pessoaFisica.getIdePessoaFisica()));
		return pessoaFisicaDAO.buscarPorCriteria(criteria);
	}
	
	public Boolean existeRequerimentoPorPF(PessoaFisica pPessoaFisica)  {		
		return this.pessoaFisicaDAOImpl.existeRequerimentoPorPF(pPessoaFisica);
	}
	
	

}