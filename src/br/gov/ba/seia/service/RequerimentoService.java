package br.gov.ba.seia.service;

import java.util.ArrayList;


import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import br.gov.ba.seia.util.Util;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.Classe;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoImovel;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.RequerimentoUnidadeConservacao;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.TipoAreaPreservacaoPermanente;
import br.gov.ba.seia.entity.TipoAreaProtegida;
import br.gov.ba.seia.entity.TipoImovelRequerimento;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import org.apache.log4j.Level;
import br.gov.ba.ws.entity.RequerimentoWS;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RequerimentoService {

	@Inject
	private IDAO<Requerimento> requerimentoDAO;
	
	@Inject
	private IDAO<BoletoPagamentoRequerimento> boletoDAO;
	
	@Inject
	private IDAO<EmpreendimentoRequerimento> empreendimentoRequerimentoDAO;
	@Inject
	private IDAO<RequerimentoPessoa> requerimentoPessoaDAO;
	@Inject
	private IDAO<RequerimentoImovel> requerimentoImovelDAO;
	@Inject
	private IDAO<TipoImovelRequerimento> tipoRequerimentoImovelDAO;
	@Inject 
	private IDAO<SolicitacaoAdministrativo> solicitacaoAdministrativoDAO;
	@Inject 
	private IDAO<RequerimentoUnidadeConservacao> requerimentoUnidadeConservacaoDAO;
	@Inject 
	private IDAO<TipoAreaProtegida> tipoAreaProtegidaDAO;
	@Inject 
	private IDAO<TipoAreaPreservacaoPermanente> tipoAreaPreservacaoPermanenteDAO;
	@Inject 
	private IDAO<DeclaracaoInexigibilidade> declaracaoInexigibilidadeDAO;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private SolicitacaoAdministrativoAtoAmbientalService solicitacaoAdministrativoAtoAmbientalService;
	
    @PersistenceContext
    private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRequerimentoPessoa(RequerimentoPessoa requerimentoPessoa) {
		requerimentoPessoaDAO.salvarOuAtualizar(requerimentoPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRequerimentoImovel(RequerimentoImovel requerimentoImovel) {
		requerimentoImovelDAO.salvarOuAtualizar(requerimentoImovel);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public RequerimentoPessoa obterPorRequerimentoETipoPessoa(Requerimento pRequerimento, TipoPessoaRequerimento pTipoPessoaRequerimento)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideRequerimento", pRequerimento.getIdeRequerimento());
		parametros.put("ideTipoPessoaRequerimento", pTipoPessoaRequerimento.getIdeTipoPessoaRequerimento());
		return requerimentoPessoaDAO.buscarEntidadePorNamedQuery("RequerimentoPessoa.findByRequerimentoPessoa", parametros);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Requerimento buscarEntidadePorExemplo(Requerimento pRequerimento) {
		pRequerimento = requerimentoDAO.buscarEntidadePorExemplo(pRequerimento);
		// Obtem a lista de Pessoas do requerimento
		if (!Util.isNull(pRequerimento)) {
			Hibernate.initialize(pRequerimento.getRequerimentoPessoaCollection());
		}
		return pRequerimento;
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Requerimento buscarEntidadePorId(Requerimento pRequerimento)  {
	
		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class)
				.createAlias("requerimentoPessoaCollection", "pessoas")
				.add(Restrictions.eq("this.ideRequerimento", pRequerimento.getIdeRequerimento()));
		
		Requerimento req = this.requerimentoDAO.buscarPorCriteria(criteria);
		
		req.getRequerimentoPessoaCollection().iterator().next();
		
		return req;
	}
	
	   @TransactionAttribute(TransactionAttributeType.REQUIRED)
	    public void atualizarNumeroEDataFinalizacao(Integer ideRequerimento, String numRequerimento, Date dtcFinalizacao) {
	        try {
	        	
	            if (Util.isNullOuVazio(ideRequerimento)) {
	                throw new Exception("ID do requerimento não pode ser nulo para atualização");
	            }
	            
	            String jpql = "UPDATE Requerimento r SET " +
	                          "r.numRequerimento = :numRequerimento, " +
	                          "r.dtcFinalizacao = :dtcFinalizacao " +
	                          "WHERE r.ideRequerimento = :ideRequerimento";
	            
	            Query query = entityManager.createQuery(jpql);
	            query.setParameter("numRequerimento", numRequerimento);
	            query.setParameter("dtcFinalizacao", dtcFinalizacao);
	            query.setParameter("ideRequerimento", ideRequerimento);
	            
	            int rowsUpdated = query.executeUpdate();
	            
	            if (rowsUpdated == 0) {
	                throw new Exception("Nenhum requerimento foi atualizado com o ID: " + ideRequerimento);
	            }
	            
	            // Força o flush para garantir que o update seja executado imediatamente
	            entityManager.flush();
	            
	        } catch (Exception e) {
	            Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
	            throw Util.capturarException(e, Util.SEIA_EXCEPTION);
	        }
	    }
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean existeRequerimento(Requerimento pRequerimento)  {
	
		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class)
			.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("ideRequerimento")), "ideRequerimento"))
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Requerimento.class))
			
			.add(Restrictions.eq("this.ideRequerimento", pRequerimento.getIdeRequerimento()));
		
		Requerimento req = this.requerimentoDAO.buscarPorCriteria(criteria);
		
		if(!Util.isNullOuVazio(req)) {
			return true;
		}
		
		return false; 
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public RequerimentoPessoa buscarComunicacaoRequerente(RequerimentoPessoa requerimentoPessoa) {
		 
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimento", requerimentoPessoa.getRequerimento().getIdeRequerimento());
		Requerimento requerimento = requerimentoDAO.buscarEntidadePorNamedQuery("Requerimento.findByIdeRequerimento", parametros);
		Hibernate.initialize(requerimento.getComunicacaoRequerimentoCollection());
		
		parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimento", requerimentoPessoa.getRequerimento().getIdeRequerimento());
		parametros.put("ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId());
			
		requerimentoPessoa = requerimentoPessoaDAO.buscarEntidadePorNamedQuery("RequerimentoPessoa.findByRequerimentoPessoa", parametros);
		
		requerimentoPessoa.setRequerimento(requerimento);				
		Hibernate.initialize(requerimentoPessoa.getPessoa());
		
		return requerimentoPessoa;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarComunicacao(Requerimento req, String texto)  {
		ComunicacaoRequerimento comunicacao = new ComunicacaoRequerimento();
		comunicacao.setIdeRequerimento(req);
		comunicacao.setDtcComunicacao(new Date());
		comunicacao.setDesMensagem(texto);
		comunicacaoRequerimentoService.salvar(comunicacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void inserirRequerimento(Requerimento requerimento) {
		requerimentoDAO.salvarOuAtualizar(requerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarRequerimento(Requerimento requerimento) {
		requerimentoDAO.atualizar(requerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarEmpreendRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento)  {
	
		if (!Util.isNull(empreendimentoRequerimento) && !Util.isNull(empreendimentoRequerimento.getIdeRequerimento())
				&& !Util.isNull(empreendimentoRequerimento.getIdeRequerimento().getIdeRequerimento()) 
				&& !Util.isNull(empreendimentoRequerimento.getIdeEmpreendimento())
				&& !Util.isNull(empreendimentoRequerimento.getIdeEmpreendimento().getIdeEmpreendimento())) {
			
			EmpreendimentoRequerimento ideEmpreendimentoRequerimento = 
					empreendimentoService.listarEmpreendimentoRequerimento(empreendimentoRequerimento.getIdeRequerimento(), empreendimentoRequerimento.getIdeEmpreendimento());
			
			ideEmpreendimentoRequerimento.setIdeRequerimento(empreendimentoRequerimento.getIdeRequerimento());
			
			if (Util.isNullOuVazio(ideEmpreendimentoRequerimento)) {
				empreendimentoRequerimentoDAO.salvar(empreendimentoRequerimento);
			} else {
				
				if (!Util.isNullOuVazio(empreendimentoRequerimento.getIdeFaseEmpreendimento())) {
					ideEmpreendimentoRequerimento.setIdeFaseEmpreendimento(empreendimentoRequerimento.getIdeFaseEmpreendimento());
				}
				if (!Util.isNullOuVazio(empreendimentoRequerimento.getDtcFaseEmpreendimento())) {
					ideEmpreendimentoRequerimento.setDtcFaseEmpreendimento(empreendimentoRequerimento.getDtcFaseEmpreendimento());
				}
				if (!Util.isNullOuVazio(empreendimentoRequerimento.getIdePorte())){
					ideEmpreendimentoRequerimento.setIdePorte(empreendimentoRequerimento.getIdePorte());
				}
				if (!Util.isNullOuVazio(empreendimentoRequerimento.getIdeClasse())) {
					ideEmpreendimentoRequerimento.setIdeClasse(empreendimentoRequerimento.getIdeClasse());
				}				
				if(empreendimentoRequerimento.isDeveSalvarClasseNulo()){
					ideEmpreendimentoRequerimento.setIdeClasse(new Classe());
					ideEmpreendimentoRequerimento.setIsDeveSalvarClasseNulo(Boolean.TRUE);
				}
				if (!Util.isNullOuVazio(empreendimentoRequerimento.getIndDla())) {
					ideEmpreendimentoRequerimento.setIndDla(empreendimentoRequerimento.getIndDla());
				}
				if (!Util.isNullOuVazio(empreendimentoRequerimento.getNumProcessoAna())) {
					ideEmpreendimentoRequerimento.setNumProcessoAna(empreendimentoRequerimento.getNumProcessoAna());
				}
				if (!Util.isNullOuVazio(empreendimentoRequerimento.getNumPortariaAna())) {
					ideEmpreendimentoRequerimento.setNumPortariaAna(empreendimentoRequerimento.getNumPortariaAna());
				}
				if (!Util.isNullOuVazio(empreendimentoRequerimento.getNumVazaoTotal())) {
					ideEmpreendimentoRequerimento.setNumVazaoTotal(empreendimentoRequerimento.getNumVazaoTotal());
				}
				
				ideEmpreendimentoRequerimento.setProgramaGoverno(empreendimentoRequerimento.getProgramaGoverno());
				
				empreendimentoRequerimento.setIdeEmpreendimentoRequerimento(ideEmpreendimentoRequerimento.getIdeEmpreendimentoRequerimento());
				atualizarEmpreendimentoRequerimentoHQL(ideEmpreendimentoRequerimento);
			}
		}
	}
	
	public void atualizarEmpreendimentoRequerimentoHQL(EmpreendimentoRequerimento er)  {
		
		StringBuilder sql = new StringBuilder()
			.append("UPDATE EmpreendimentoRequerimento ")
			
			.append("SET ")
			
			.append("dtcFaseEmpreendimento = :dtcFaseEmpreendimento , ")
			.append("indDla = :indDla , ")
			.append("numProcessoAna = :numProcessoAna , ")
			.append("numPortariaAna = :numPortariaAna , ")
			.append("numVazaoTotal = :numVazaoTotal ");
			
			if (!Util.isNull(er.getIdeRequerimento()) && !Util.isNull(er.getIdeRequerimento().getIdeRequerimento())) {
				sql.append(", ideRequerimento.ideRequerimento = :ideRequerimento ");
			}
			
			if (!Util.isNullOuVazio(er.getIdeEmpreendimento())) {
				sql.append(", ideEmpreendimento.ideEmpreendimento = :ideEmpreendimento ");
			}
			
			if (!Util.isNullOuVazio(er.getIdeFaseEmpreendimento())) {
				sql.append(", ideFaseEmpreendimento.ideFaseEmpreendimento = :ideFaseEmpreendimento ");
			}
			
			if (!Util.isNullOuVazio(er.getIdePorte())) {
				sql.append(", idePorte.idePorte = :idePorte ");
			}
			
			if (!Util.isNullOuVazio(er.getIdeClasse())) {
				sql.append(", ideClasse.ideClasse = :ideClasse ");
			}
			
			if (er.isDeveSalvarClasseNulo()) {
				sql.append(", ideClasse = null ");
			}
			
			if (!Util.isNullOuVazio(er.getProgramaGoverno())) {
				sql.append(", programaGoverno.ideProgramaGoverno = :programaGoverno ");
			}
			
			sql.append("WHERE ")
			
			.append("ideEmpreendimentoRequerimento = :ideEmpreendimentoRequerimento");
		
		Map<String, Object> params = new HashMap<String, Object>();
			params.put("dtcFaseEmpreendimento", er.getDtcFaseEmpreendimento());
			params.put("indDla", er.getIndDla());
			params.put("numProcessoAna", er.getNumProcessoAna());
			params.put("numPortariaAna", er.getNumPortariaAna());
			params.put("numVazaoTotal", er.getNumVazaoTotal());
			
			if (!Util.isNull(er.getIdeRequerimento()) && !Util.isNull(er.getIdeRequerimento().getIdeRequerimento())) {
				params.put("ideRequerimento", er.getIdeRequerimento().getIdeRequerimento());
			}
			
			if (!Util.isNullOuVazio(er.getIdeEmpreendimento())) {
				params.put("ideEmpreendimento", er.getIdeEmpreendimento().getIdeEmpreendimento());
			}
			
			if (!Util.isNullOuVazio(er.getIdeFaseEmpreendimento())) {
				params.put("ideFaseEmpreendimento", er.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento());
			}
			
			if (!Util.isNullOuVazio(er.getIdePorte())) {
				params.put("idePorte", er.getIdePorte().getIdePorte());
			}
			
			if (!Util.isNullOuVazio(er.getIdeClasse())) {
				params.put("ideClasse", er.getIdeClasse().getIdeClasse());
			}
			
			if (!Util.isNullOuVazio(er.getProgramaGoverno())) {
				params.put("programaGoverno", er.getProgramaGoverno().getIdeProgramaGoverno());
			}
			
			params.put("ideEmpreendimentoRequerimento", er.getIdeEmpreendimentoRequerimento());
			
		empreendimentoRequerimentoDAO.executarQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmpreendRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento) {
		empreendimentoRequerimentoDAO.salvar(empreendimentoRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarSemSalvarEmpreendRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento) {
		empreendimentoRequerimentoDAO.atualizar(empreendimentoRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Requerimento carregar(Requerimento requerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class, "req");
		
		criteria
			.createAlias("req.requerimentoPessoaCollection", "reqPess")
			.createAlias("reqPess.pessoa", "pessoa")
			.setFetchMode("reqPess.ideTipoPessoaRequerimento", FetchMode.JOIN)
			.setFetchMode("pessoa.pessoaJuridica", FetchMode.JOIN)
			.setFetchMode("pessoa.pessoaFisica", FetchMode.JOIN)
			
			.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()))
			.add(Restrictions.eq("pessoa.indExcluido", false))
		;
		
		Requerimento req = requerimentoDAO.buscarPorCriteria(criteria);
		
		return req;
	}
	
	/**
	 * Metodo que retorna um {@link Requerimento} pelo seu numero (num_requerimento).
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param requerimento - Unico filtro a ser utilizado sera o numero do {@link Requerimento}
	 * @return {@link Requerimento}
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Requerimento buscarRequerimentoPeloNumero(Requerimento requerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class, "req")
				.createAlias("req.requerimentoPessoaCollection", "reqPess")
				.createAlias("req.tramitacaoRequerimentoCollection", "tramitacao")
				.createAlias("tramitacao.ideStatusRequerimento", "statusRequerimento")
				.createAlias("reqPess.pessoa", "pessoa")
				.setFetchMode("reqPess.ideTipoPessoaRequerimento", FetchMode.JOIN)
				.setFetchMode("pessoa.pessoaJuridica", FetchMode.JOIN)
				.setFetchMode("pessoa.pessoaFisica", FetchMode.JOIN)
				.add(Restrictions.eq("req.numRequerimento", requerimento.getNumRequerimento()));
				Requerimento req = requerimentoDAO.buscarPorCriteria(criteria);
				
		if(req != null && req.getRequerimentoPessoaCollection() != null) {
			req.getRequerimentoPessoaCollection().iterator().next(); 
		}
		
		if(req != null && req.getTramitacaoRequerimentoCollection() != null) {
			req.getTramitacaoRequerimentoCollection().iterator().next(); 
		}
					
		return req;
	}
	
	/**
	 * Método que busca um {@link Requerimento} através da ID do {@link BoletoPagamentoRequerimento}.
	 * 
	 * @author micael.coutinho
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Requerimento buscarRequerimentoPeloBoleto(Integer ideBoleto)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class, "boleto")
				.createAlias("ideRequerimento", "req", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideProcesso", "processo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("processo.ideRequerimento", "reqP", JoinType.LEFT_OUTER_JOIN)
				
				.add(Restrictions.eq("ideBoletoPagamentoRequerimento", ideBoleto));
				BoletoPagamentoRequerimento boleto = boletoDAO.buscarPorCriteria(criteria);
				
		Requerimento req = null;
		if(boleto != null && boleto.getIdeRequerimento() != null) {
			req = boleto.getIdeRequerimento();
		}else if(boleto != null && boleto.getIdeProcesso() != null) {
			req = boleto.getIdeProcesso().getIdeRequerimento();
		}
		return req;	
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean verificarRequerimentoPessoa(Pessoa pessoa)  {
		Boolean existeRequerimentoPessoa = Boolean.TRUE;
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoPessoa.class);
		criteria.createAlias("pessoa", "pessoa");
		criteria.add(Restrictions.eq("pessoa.idePessoa", pessoa.getIdePessoa()));
		Collection<RequerimentoPessoa> requerimentosPessoas = requerimentoPessoaDAO.listarPorCriteria(criteria);
		if (requerimentosPessoas.isEmpty()) {
			existeRequerimentoPessoa = Boolean.FALSE;
		} 
		return existeRequerimentoPessoa;
	}


	public TipoImovelRequerimento carregarTipoImovelRequerimentoById(int id){
		return tipoRequerimentoImovelDAO.carregarGet(id);
	}
	
	
	public Requerimento carregarRequerimentoByIdeImovel(Integer ideImovel)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class)
				.createAlias("ideTipoRequerimento", "tipo")
				.createAlias("ideOrgao", "orgao")
				.createAlias("requerimentoImovelCollection", "ri");

		criteria.setProjection(Projections.projectionList()
				.add(Projections.distinct(Projections.property("ideRequerimento")), "ideRequerimento")
				
				.add(Projections.property("orgao.ideOrgao"), "ideOrgao.ideOrgao")
				.add(Projections.property("orgao.codOrgao"), "ideOrgao.codOrgao")
		);
				
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Requerimento.class));
		
		criteria.add(Restrictions.eq("ri.imovel.ideImovel", ideImovel));
		criteria.add(Restrictions.eq("tipo.ideTipoRequerimento", TipoRequerimentoEnum.REQUERIMENTO_DE_CADASTRO_DE_IMOVEL_RURAL.getIde()));
		criteria.add(Restrictions.eq("ri.indExcluido", false));
		
		return requerimentoDAO.buscarPorCriteria(criteria);
	}


	public void removerRequerimentosIncompletos(Integer ideEmpreendimento) {
		try {
			DAOFactory.getEntityManager()
			.createNativeQuery("select removerRequerimentosSemNumero(:id);")
			.setParameter("id", ideEmpreendimento)
			.getSingleResult();
		} catch (Exception e) {
			System.err.println("Erro ao remover requerimentos incompletos.");
		}
	}
	
	



	public void atualizarIndEmergencia(Requerimento requerimento)  {
		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("emergencia",requerimento.getIndEstadoEmergencia());
		parametros.put("requerimento",requerimento.getIdeRequerimento());
		this.requerimentoDAO.executarNativeQuery("update requerimento set ind_estado_emergencia = :emergencia where ide_requerimento = :requerimento", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarArea(Requerimento requerimento)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("area", requerimento.getIdeArea().getIdeArea());
		parametros.put("requerimento", requerimento.getIdeRequerimento());
		this.requerimentoDAO.executarNativeQuery("update requerimento set ide_area = :area where ide_requerimento = :requerimento", parametros);
	}

	public Requerimento carregarEmail(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(Requerimento.class, "req")
				.setProjection(
						Projections.projectionList().add(Projections.property("ideRequerimento"), "ideRequerimento")
								.add(Projections.property("desEmail"), "desEmail"))
				.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Requerimento.class));

		return requerimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Requerimento carregarDadosBasicos(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(Requerimento.class, "req")
				.createAlias("req.ideArea", "area", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.ideOrgao", "orgao", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.empreendimentoRequerimentoCollection", "er", JoinType.LEFT_OUTER_JOIN)
				.createAlias("er.ideEmpreendimento", "emp", JoinType.LEFT_OUTER_JOIN)
				.createAlias("emp.ideLocalizacaoGeografica", "lg", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.requerimentoPessoaCollection", "rpc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.ideTipoPessoaRequerimento", "tpr", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rpc.pessoa", "p", JoinType.LEFT_OUTER_JOIN)
				.createAlias("p.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("p.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("emp.enderecoEmpreendimentoCollection", "enderecoEmpreendimento", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoEmpreendimento.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoEmpreendimento.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("endereco.ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouro.ideBairro", "bairro", JoinType.LEFT_OUTER_JOIN)
				.createAlias("logradouro.ideMunicipio", "lmun", JoinType.LEFT_OUTER_JOIN)
				.createAlias("bairro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN)
				.createAlias("municipio.ideEstado", "estado", JoinType.LEFT_OUTER_JOIN);

		criteria.setProjection(
				Projections
						.projectionList()
						.add(Projections.property("req.numRequerimento"), "numRequerimento")
						.add(Projections.property("req.ideRequerimento"), "ideRequerimento")
						.add(Projections.property("req.ideTipoRequerimento.ideTipoRequerimento"), "ideTipoRequerimento.ideTipoRequerimento")

						.add(Projections.property("req.dtcCriacao"), "dtcCriacao")
						
						.add(Projections.property("area.ideArea"), "ideArea.ideArea")

						.add(Projections.property("orgao.ideOrgao"), "ideOrgao.ideOrgao")
						.add(Projections.property("orgao.codOrgao"), "ideOrgao.codOrgao")

						.add(Projections.property("emp.ideEmpreendimento"), "ultimoEmpreendimento.ideEmpreendimento")
						.add(Projections.property("emp.nomEmpreendimento"), "ultimoEmpreendimento.nomEmpreendimento")
						
						.add(Projections.property("lg.ideLocalizacaoGeografica"), "ultimoEmpreendimento.ideLocalizacaoGeografica.ideLocalizacaoGeografica")

						.add(Projections.property("p.idePessoa"), "requerente.idePessoa")
						.add(Projections.property("pf.nomPessoa"), "requerente.pessoaFisica.nomPessoa")
						.add(Projections.property("pf.numCpf"), "requerente.pessoaFisica.numCpf")
						.add(Projections.property("pj.nomRazaoSocial"), "requerente.pessoaJuridica.nomRazaoSocial")
						.add(Projections.property("pj.numCnpj"), "requerente.pessoaJuridica.numCnpj")

						.add(Projections.property("endereco.ideEndereco"), "ultimoEmpreendimento.endereco.ideEndereco")
						.add(Projections.property("endereco.numEndereco"), "ultimoEmpreendimento.endereco.numEndereco")
						.add(Projections.property("endereco.desComplemento"),
								"ultimoEmpreendimento.endereco.desComplemento")

						.add(Projections.property("logradouro.nomLogradouro"),
								"ultimoEmpreendimento.endereco.ideLogradouro.nomLogradouro")
						.add(Projections.property("logradouro.numCep"),
								"ultimoEmpreendimento.endereco.ideLogradouro.numCep")

						.add(Projections.property("bairro.ideBairro"),
								"ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.ideBairro")
						.add(Projections.property("bairro.nomBairro"),
								"ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.nomBairro")

						.add(Projections.property("lmun.ideMunicipio"),
								"ultimoEmpreendimento.endereco.ideLogradouro.ideMunicipio.ideMunicipio")
						.add(Projections.property("lmun.indEstadoEmergencia"),
								"ultimoEmpreendimento.endereco.ideLogradouro.ideMunicipio.indEstadoEmergencia")
						.add(Projections.property("lmun.nomMunicipio"),
								"ultimoEmpreendimento.endereco.ideLogradouro.ideMunicipio.nomMunicipio")

						.add(Projections.property("municipio.ideMunicipio"),
								"ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideMunicipio")
						.add(Projections.property("municipio.indEstadoEmergencia"),
								"ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.indEstadoEmergencia")
						.add(Projections.property("municipio.nomMunicipio"),
								"ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")

						.add(Projections.property("estado.ideEstado"),
								"ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.ideEstado")
						.add(Projections.property("estado.desSigla"),
								"ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.desSigla")

		).setResultTransformer(new AliasToNestedBeanResultTransformer(Requerimento.class));

		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		criteria.add(Restrictions.or(
				Restrictions.eq("tpr.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()),
				Restrictions.isNull("tpr.ideTipoPessoaRequerimento")));
		criteria.add(Restrictions.or(
				Restrictions.eq("tipoEndereco.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()),
				Restrictions.isNull("tipoEndereco.ideTipoEndereco")));

		return this.requerimentoDAO.buscarPorCriteria(criteria);
	}
	
	public boolean isRequerimentoAntigo(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class, "req")
			.createAlias("req.requerimentoUnico", "reqUnico", JoinType.INNER_JOIN);
		
		criteria.setProjection(
				Projections
				.projectionList()
				.add(Projections.property("req.numRequerimento"), "numRequerimento")
				.add(Projections.property("req.ideRequerimento"), "ideRequerimento")
				
				.add(Projections.property("reqUnico.ideRequerimentoUnico"), "ideRequerimentoUnico"));
		
		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		
		return !Util.isNullOuVazio(this.requerimentoDAO.buscarPorCriteria(criteria));
	}

	public Requerimento carregarComBoleto(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class, "req")
				.setFetchMode("req.requerimentoPessoaCollection", FetchMode.JOIN)
				.setFetchMode("reqPess.ideTipoPessoaRequerimento", FetchMode.JOIN)
				.setFetchMode("req.boletoPagamentoRequerimento", FetchMode.JOIN)
				.setFetchMode("req.boletoDaeRequerimeno", FetchMode.JOIN)
				.setFetchMode("pessoa.pessoaJuridica", FetchMode.JOIN)
				.setFetchMode("pessoa.pessoaFisica", FetchMode.JOIN)
				.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()));
		Requerimento req = requerimentoDAO.buscarPorCriteria(criteria);
		return req;
	}

	public void finalizar(Requerimento requerimento, Pessoa operador,AreaEnum area) throws Exception  {
		this.tramitacaoRequerimentoService.tramitar(requerimento, StatusRequerimentoEnum.FORMADO, operador);
		this.processoService.gerarProcesso(requerimento, operador,area.getId(), false);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(Requerimento requerimento, Pessoa operador, Boolean isDirucSecundaria) throws Exception  {
		this.tramitacaoRequerimentoService.tramitar(requerimento, StatusRequerimentoEnum.FORMADO, operador);
		this.processoService.gerarProcesso(requerimento, operador,requerimento.getIdeArea().getIdeArea(), isDirucSecundaria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void duplicarControleTramitacaoParaAreaSecundaria(Requerimento requerimento, Pessoa operador, Boolean isDirucSecundaria) {
		this.processoService.gerarControleTramitacaoParaAreaSecundaria(requerimento, operador, requerimento.getIdeArea().getIdeArea(), isDirucSecundaria);	
	}

	public void finalizarDLA(Requerimento requerimento, Pessoa operador) throws Exception  {
		this.tramitacaoRequerimentoService.tramitar(requerimento, StatusRequerimentoEnum.REQUERIMENTO_CONCLUIDO, operador);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isSomenteDLA(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class)
				.createAlias("ideRequerimento", "req")
				.createAlias("ideEmpreendimento", "emp")
				.createAlias("ideClasse", "classe",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideFaseEmpreendimento", "fase",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePorte", "porte",JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", ideRequerimento));
		criteria.add(Restrictions.eq("indDla", true));
		
		
		DetachedCriteria exists = DetachedCriteria.forClass(EnquadramentoAtoAmbiental.class)
				.createAlias("enquadramento", "enq")
				.createAlias("enq.ideRequerimento", "req2")
				.setProjection(Projections.projectionList().add(Property.forName("enquadramento.ideEnquadramento")))
				.add(Restrictions.eqProperty("req2.ideRequerimento", "req.ideRequerimento"))
				.add(Restrictions.eq("enq.indEnquadramentoAprovado", true));
		
		criteria.add(Subqueries.notExists(exists));
		
		return !Util.isNull(requerimentoDAO.buscarPorCriteria(criteria)) ;
		
	}
	
	public EmpreendimentoRequerimento carregarEmpreendimentoRequerimento(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class)
				.createAlias("ideRequerimento", "req")
				.createAlias("ideEmpreendimento", "emp")
				.createAlias("ideClasse", "classe",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideFaseEmpreendimento", "fase",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePorte", "porte",JoinType.LEFT_OUTER_JOIN)
				.createAlias("programaGoverno", "programaGoverno", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", ideRequerimento));
		
		return empreendimentoRequerimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RequerimentoWS buscaDadosRequerimentoByIdeProcesso(Integer ideProcesso) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT distinct new br.gov.ba.ws.entity.RequerimentoWS(r.numRequerimento,r.dtcCriacao,pf.nomPessoa, pj.nomRazaoSocial, emp.nomEmpreendimento,r.ideRequerimento) from Processo p ");
		lSql.append("INNER JOIN p.ideRequerimento r ");
		lSql.append("INNER JOIN r.empreendimentoRequerimentoCollection er ");
		lSql.append("INNER JOIN er.ideEmpreendimento emp ");
		lSql.append("INNER JOIN r.requerimentoPessoaCollection rp ");
		lSql.append("INNER JOIN rp.pessoa pe ");
		lSql.append("left JOIN pe.pessoaFisica pf ");
		lSql.append("left JOIN pe.pessoaJuridica pj ");
		lSql.append("WHERE p.indExcluido = false  ");
		lSql.append(" AND rp.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = 1");
		lSql.append(" AND p.ideProcesso = :ideProcesso");
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		
		Query lQuery = lEntityManager.createQuery(lSql.toString());
		if (!Util.isNullOuVazio(ideProcesso)) {
			lQuery.setParameter("ideProcesso", ideProcesso);
		}
			
		RequerimentoWS requerimento = (RequerimentoWS) lQuery.getSingleResult();
		return requerimento;
	}
	
	/**
	 * Busca um {@link Requerimento} por ID trazendo as collections de {@link RequerimentoPessoa} e {@link TramitacaoRequerimento} já carregadas.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param pRequerimento
	 * @return
	 * @
	 */
	public Requerimento buscarEntidadeCarregadaPorId(Requerimento pRequerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class, "req")
				.createAlias("req.requerimentoPessoaCollection", "reqPess")
				.createAlias("req.tramitacaoRequerimentoCollection", "tramitacao")
				.createAlias("tramitacao.ideStatusRequerimento", "statusRequerimento")
				.createAlias("reqPess.pessoa", "pessoa")
				.setFetchMode("reqPess.ideTipoPessoaRequerimento", FetchMode.JOIN)
				.setFetchMode("pessoa.pessoaJuridica", FetchMode.JOIN)
				.setFetchMode("pessoa.pessoaFisica", FetchMode.JOIN)
				.add(Restrictions.eq("req.ideRequerimento", pRequerimento.getIdeRequerimento()));
		
		Requerimento req = requerimentoDAO.buscarPorCriteria(criteria);
				
		if(req != null && req.getRequerimentoPessoaCollection() != null) {
			req.getRequerimentoPessoaCollection().iterator().next(); 
		}
		
		if(req != null && req.getTramitacaoRequerimentoCollection() != null) {
			req.getTramitacaoRequerimentoCollection().iterator().next(); 
		}
					
		return req;
	}
	
	/**
	 * @author Alexandre Queiroz 
	 * @since 18/12/2014
	 * @param empreendimentoRequerimento
	 * @return
	 * @ 
	 */
	public boolean isExisteEmpreendimentoRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class)
				.createAlias("ideRequerimento", "r")
				.createAlias("ideEmpreendimento","e")
				.add(Restrictions.eq("r.ideRequerimento", empreendimentoRequerimento.getIdeRequerimento().getIdeRequerimento()))
				.add(Restrictions.eq("e.ideEmpreendimento", empreendimentoRequerimento.getIdeEmpreendimento().getIdeEmpreendimento()));
						
						
		EmpreendimentoRequerimento er = empreendimentoRequerimentoDAO.buscarPorCriteria(detachedCriteria);
		
		if(Util.isNullOuVazio(er)){
			return false;
		}
		else {
			return true;
		}
	}
	
	public EmpreendimentoRequerimento buscarIdeEmpreendimentoRequerimento(EmpreendimentoRequerimento empReq)  {
	
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class)
				
			.setProjection(Projections.projectionList()
					.add(Projections.property("ideEmpreendimentoRequerimento"), "ideEmpreendimentoRequerimento")
					.add(Projections.property("dtcFaseEmpreendimento"), "dtcFaseEmpreendimento")
					.add(Projections.property("indDla"), "indDla")
					.add(Projections.property("numProcessoAna"), "numProcessoAna")
					.add(Projections.property("numPortariaAna"), "numPortariaAna")
					.add(Projections.property("numVazaoTotal"), "numVazaoTotal")
				
					.add(Projections.property("ideRequerimento.ideRequerimento"), "ideRequerimento.ideRequerimento")
				
					.add(Projections.property("ideEmpreendimento.ideEmpreendimento"), "ideEmpreendimento.ideEmpreendimento")
				
					.add(Projections.property("ideFaseEmpreendimento.ideFaseEmpreendimento"), "ideFaseEmpreendimento.ideFaseEmpreendimento")

					.add(Projections.property("idePorte.idePorte"), "idePorte.idePorte")
					
					.add(Projections.property("ideClasse.ideClasse"), "ideClasse.ideClasse")
					
					.add(Projections.property("programaGoverno.ideProgramaGoverno"), "programaGoverno.ideProgramaGoverno")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(EmpreendimentoRequerimento.class))
			
			.add(Restrictions.eq("ideRequerimento.ideRequerimento", empReq.getIdeRequerimento().getIdeRequerimento()))
			.add(Restrictions.eq("ideEmpreendimento.ideEmpreendimento", empReq.getIdeEmpreendimento().getIdeEmpreendimento()));
		
		List<EmpreendimentoRequerimento> empreendimentoRequerimentoList = empreendimentoRequerimentoDAO.listarPorCriteria(detachedCriteria);
		
		return empreendimentoRequerimentoList.get(0);
	}


	/**
	 * @author Alexandre (Alexandre Queiroz)
	 * @since 08/10/2015
	 * @param requerimento
	 * @return
	 * @ 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isSolicitacaoAdminstrativoTla(Requerimento requerimento)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class)
				.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()))
				.add(Restrictions.eq("ideTipoSolicitacao.ideTipoSolicitacao", TipoSolicitacaoEnum.TRANSFERENCIA_LICENCA_AMBIENTAL.getId()));
				
		return !Util.isNullOuVazio(requerimentoDAO.isExiste(detachedCriteria));
	}

	
	/**
	 * @author Alexandre (Alexandre Queiroz)
	 * @since 08/10/2015
	 * @param requerimento
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SolicitacaoAdministrativo getSolicitacaoAdminstrativo(Requerimento requerimento, TipoSolicitacaoEnum tipoSolicitacaoEnum) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class)
			.createAlias("ideEmpreendimento", "emp", JoinType.LEFT_OUTER_JOIN)
				
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideSolicitacaoAdministrativo"), "ideSolicitacaoAdministrativo")
				.add(Projections.property("numProcesso"), "numProcesso")
				.add(Projections.property("numPortaria"), "numPortaria")
				.add(Projections.property("dtcPublicacaoPortaria"), "dtcPublicacaoPortaria")
				.add(Projections.property("dtcValidade"), "dtcValidade")
				.add(Projections.property("numCertificado"), "numCertificado")
				.add(Projections.property("numCondicionante"), "numCondicionante")
				.add(Projections.property("nomRazaoSocialNova"), "nomRazaoSocialNova")
				.add(Projections.property("indDetentorLicenca"), "indDetentorLicenca")
				
				.add(Projections.property("emp.ideEmpreendimento"), "ideEmpreendimento.ideEmpreendimento")
				.add(Projections.property("emp.nomEmpreendimento"), "ideEmpreendimento.nomEmpreendimento")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(SolicitacaoAdministrativo.class))
				
			.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()))
			.add(Restrictions.eq("ideTipoSolicitacao.ideTipoSolicitacao", tipoSolicitacaoEnum.getId()));
		
		SolicitacaoAdministrativo sa = solicitacaoAdministrativoDAO.buscarPorCriteria(detachedCriteria);
		
		if(!Util.isNullOuVazio(sa)) {
			sa.setSolicitacaoAdminstrativoAtoAmbientalCollection(solicitacaoAdministrativoAtoAmbientalService.listaAtoBySolicitacaoComTipologia(sa));
		}
		
		return sa;
	}

	/**
	 * @author Alexandre (Alexandre Queiroz)
	 * @since 08/10/2015
	 * @param requerimento
	 * @return
	 */
	public List<SolicitacaoAdministrativo> getSolicitacaoAdminstrativoList(Requerimento requerimento, TipoSolicitacaoEnum tipoSolicitacaoEnum) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class)
				.createAlias("solicitacaoAdminstrativoAtoAmbientalCollection","solato")
				.createAlias("solato.ideAtoAmbiental","ato")
				.createAlias("solato.ideTipologia","tipologia", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideTipoSolicitacao",tipoSolicitacaoEnum.getId()));
		return solicitacaoAdministrativoDAO.listarPorCriteria(detachedCriteria);
	}
	

	/**
	 * @author Alexandre Queiroz 
	 * @since 11/01/2016
	 * @param empreendimentoRequerimento
	 * @return
	 * @ 
	 */
	public boolean isExisteNumRequerimento(Requerimento requerimento)  {
//		if(Util.isNotNullAndTrue(requerimento) || Util.isNotNullAndTrue(requerimento.getNumRequerimento())) {
//			return false;
//		}
		if(StringUtils.isBlank(requerimento.getNumRequerimento())){
			return false;
		}
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Requerimento.class)
			.add(Restrictions.eq("numRequerimento", requerimento.getNumRequerimento()));
		
		return !Util.isNullOuVazio(requerimentoDAO.buscarPorCriteria(detachedCriteria));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Integer> getRequerimentoNaoVisiveisByRepresentanteLegalInativado(PessoaFisica usuarioLogado) {
		List<Integer> requerimentos = new ArrayList<Integer>(); 
		StringBuilder sql = new StringBuilder();
		 
			sql
			.append(" select ")
				.append(" r.ide_requerimento ")
			.append(" from requerimento r ")
			.append(" inner join empreendimento_requerimento er on er.ide_requerimento = r.ide_requerimento ")
				.append(" inner join empreendimento e on e.ide_empreendimento = er.ide_empreendimento ")
				.append(" inner join pessoa_juridica pj on pj.ide_pessoa_juridica = e.ide_pessoa ")
				.append(" inner join requerimento_pessoa rp on rp.ide_requerimento = r.ide_requerimento ")
				.append(" inner join representante_legal rl on rl.ide_pessoa_fisica = rp.ide_pessoa ")
			.append(" where (1=1) ")
			.append(" and rp.ide_tipo_pessoa_requerimento = 3 ")
			.append(" and rp.ide_pessoa = :idePessoaFisica ")
		
			.append(" and ide_representante_legal in ( ")
				.append(" (select ide_representante_legal from ( ")
					.append(" select ")
						.append(" max(rl.ide_representante_legal) ide_representante_legal ")
						.append(" from representante_legal rl ")
						.append(" inner join requerimento_pessoa rp on rp.ide_pessoa = rl.ide_pessoa_fisica ")
					.append(" where (1=1) ")
					.append(" and rp.ide_tipo_pessoa_requerimento = 3 ")
					.append(" and rl.ide_pessoa_juridica = pj.ide_pessoa_juridica ")
					.append(" and rl.ide_pessoa_fisica  = :idePessoaFisica ")
					.append(" group by ")
					.append(" rl.ide_pessoa_fisica, ")
					.append(" rl.ide_pessoa_juridica ")
			.append(" ) as ide_representante_legal)) ")
			
			.append(" and r.ide_requerimento not in ( ")		
				.append(" select rp.ide_requerimento from ")
				.append(" requerimento_pessoa rp ")
				.append(" where ide_tipo_pessoa_requerimento in (1,2,3) ")
				.append(" and rp.ide_pessoa = :idePessoaFisica ) ")
			
			.append(" and rl.ind_excluido = true ")
			.append(" group by ")
			.append(" rl.ide_representante_legal, ")
			.append(" r.ide_requerimento, ")
			.append(" e.ide_empreendimento, ")
			.append(" pj.ide_pessoa_juridica, ")
			.append(" rp.ide_pessoa, ")
			.append(" rp.ind_solicitante, ")
			.append(" rl.ind_excluido ")
			.append(" order by pj.ide_pessoa_juridica, ")
			.append(" rp.ide_pessoa ");
		
		Map<String,Object> parametros = new HashMap<String, Object>();
		parametros.put("idePessoaFisica", usuarioLogado.getIdePessoaFisica());
			 
	  @SuppressWarnings("rawtypes")
	  List result =  requerimentoDAO.buscarPorNativeQuery(sql.toString(),parametros);
		
	  if(!Util.isNullOuVazio(result)){ 
		  for(Object resultElement: result){
		  	if(!Util.isNullOuVazio(resultElement)){
		  		requerimentos.add( (Integer)resultElement);		  		
		  	}
		  }
	  }

	  return requerimentos;
	}
	
	public RequerimentoPessoa buscarRequerimentoDTRPPorSolicitante(Pessoa pessoa, TipoPessoaRequerimento tipoPessoa)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RequerimentoPessoa.class)
			.createAlias("requerimento", "requerimento", JoinType.INNER_JOIN)
			.createAlias("requerimento.ideTipoRequerimento", "tipoRequerimento", JoinType.INNER_JOIN)
			.createAlias("requerimento.ideOrgao", "ideOrgao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("requerimento.atoDeclaratorioCollection", "atoDeclaratorio", JoinType.INNER_JOIN)
			.createAlias("atoDeclaratorio.declaracaoTransporte", "declaracaoTransporte", JoinType.INNER_JOIN)
			
			.createAlias("ideTipoPessoaRequerimento", "tipoPessoa", JoinType.INNER_JOIN)
			.createAlias("pessoa", "pessoa", JoinType.INNER_JOIN)
			
			.add(Restrictions.isNull("requerimento.dtcFinalizacao"))
			.add(Restrictions.eq("tipoRequerimento.ideTipoRequerimento", TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO.getIde()))
			.add(Restrictions.eq("pessoa.idePessoa", pessoa.getIdePessoa()))
			.add(Restrictions.eq("tipoPessoa.ideTipoPessoaRequerimento", tipoPessoa.getIdeTipoPessoaRequerimento()));
			
		return requerimentoPessoaDAO.buscarPorCriteria(detachedCriteria);
	}
	
	public RequerimentoPessoa buscarRequerimentoRepflorPorSolicitante(Pessoa pessoa, TipoPessoaRequerimento tipoPessoa)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RequerimentoPessoa.class)
			.createAlias("requerimento", "requerimento", JoinType.INNER_JOIN)
			.createAlias("requerimento.ideTipoRequerimento", "tipoRequerimento", JoinType.INNER_JOIN)
			.createAlias("requerimento.ideOrgao", "ideOrgao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("requerimento.cumprimentoReposicaoFlorestal", "repflor", JoinType.INNER_JOIN)
			
			.createAlias("ideTipoPessoaRequerimento", "tipoPessoa", JoinType.INNER_JOIN)
			.createAlias("pessoa", "pessoa", JoinType.INNER_JOIN)
			
			.add(Restrictions.isNull("requerimento.dtcFinalizacao"))
			.add(Restrictions.eq("tipoRequerimento.ideTipoRequerimento", TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO.getIde()))
			.add(Restrictions.eq("pessoa.idePessoa", pessoa.getIdePessoa()))
			.add(Restrictions.eq("tipoPessoa.ideTipoPessoaRequerimento", tipoPessoa.getIdeTipoPessoaRequerimento()));
		
		return requerimentoPessoaDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoInexigibilidade buscarRequerimentoDeclaracaoInexigibilidadePorSolicitante(Pessoa pessoa)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidade.class)
				.createAlias("requerimento", "requerimento")
				.createAlias("requerimento.requerimentoPessoaCollection", "req_pessoa")
				.createAlias("req_pessoa.pessoa", "pessoa")
				.createAlias("req_pessoa.ideTipoPessoaRequerimento", "tipoPessoa")
				.createAlias("requerimento.ideTipoRequerimento", "tipoRequerimento")
//				.createAlias("requerimento.tramitacaoRequerimentoCollection", "tramitacao")
//				.createAlias("tramitacao.ideStatusRequerimento", "ideStatusRequerimento")

		.add(Restrictions.isNull("requerimento.dtcFinalizacao"))
		.add(Restrictions.eq("tipoRequerimento.ideTipoRequerimento", TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO.getIde()))
		.add(Restrictions.eq("pessoa.idePessoa",             pessoa.getIdePessoa()))
		.add(Restrictions.eq("tipoPessoa.ideTipoPessoaRequerimento",     TipoPessoaRequerimentoEnum.REQUERENTE.getId()))
		.add(Restrictions.eq("indCienteRecomendacao",     Boolean.FALSE))
		.add(Restrictions.isNull("requerimento.dtcFinalizacao"));

		DetachedCriteria critMax = DetachedCriteria.forClass(TramitacaoRequerimento.class)
				.createAlias("ideRequerimento", "requerimento")
				.createAlias("requerimento.requerimentoPessoaCollection", "req_pessoa")
				.createAlias("requerimento.ideTipoRequerimento", "tipoRequerimento")
				.createAlias("req_pessoa.pessoa", "pessoa")
				.createAlias("req_pessoa.ideTipoPessoaRequerimento", "tipoPessoa")
				
				.add(Restrictions.eq("pessoa.idePessoa", pessoa.getIdePessoa()))
				.add(Restrictions.isNull("requerimento.dtcFinalizacao"))
				.add(Restrictions.eq("tipoRequerimento.ideTipoRequerimento", TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO.getIde()))
				.add(Restrictions.eq("tipoPessoa.ideTipoPessoaRequerimento",     TipoPessoaRequerimentoEnum.REQUERENTE.getId()))
				.setProjection(Projections.max("dtcMovimentacao"));
		
		DetachedCriteria critTramitacaoRequerimento = DetachedCriteria.forClass(TramitacaoRequerimento.class)
				.createAlias("ideStatusRequerimento", "statusRequerimento")
				.createAlias("ideRequerimento", "requerimento")
				.createAlias("requerimento.requerimentoPessoaCollection", "req_pessoa")
				.createAlias("requerimento.ideTipoRequerimento", "tipoRequerimento")
				.createAlias("req_pessoa.pessoa", "pessoa")
				.createAlias("req_pessoa.ideTipoPessoaRequerimento", "tipoPessoa")
				
				.setProjection(Projections.property("ideRequerimento"))
				.add(Restrictions.eq("pessoa.idePessoa", pessoa.getIdePessoa()))
				.add(Restrictions.isNull("requerimento.dtcFinalizacao"))
				.add(Restrictions.eq("tipoRequerimento.ideTipoRequerimento", TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO.getIde()))
				.add(Restrictions.eq("tipoPessoa.ideTipoPessoaRequerimento",     TipoPessoaRequerimentoEnum.REQUERENTE.getId()))
				.add(Restrictions.eq("ideStatusRequerimento.ideStatusRequerimento", Integer.valueOf(14)))
				
				.add(Property.forName("dtcMovimentacao").eq(critMax));
		
		detachedCriteria.add(Property.forName("requerimento.ideRequerimento").eq(critTramitacaoRequerimento));
				
		return declaracaoInexigibilidadeDAO.buscarPorCriteria(detachedCriteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoInexigibilidade buscarRequerimentoDeclaracaoInexigibilidadeFinalizadaPor(Requerimento requerimento)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidade.class)
				.createAlias("requerimento", "requerimento")

		.add(Restrictions.eq("requerimento.ideRequerimento", requerimento.getIdeRequerimento()));

		return declaracaoInexigibilidadeDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED) 
	public void salvarRequerimentoUnidadeConservacao(RequerimentoUnidadeConservacao requerimentoUnidadeConservacao) {
		requerimentoUnidadeConservacaoDAO.salvarOuAtualizar(requerimentoUnidadeConservacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED) 
	public void excluirRequerimentoUnidadeConservacao(RequerimentoUnidadeConservacao requerimentoUnidadeConservacao){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideRequerimentoUnidadeConservacao", requerimentoUnidadeConservacao.getIdeRequerimentoUnidadeConservacao());
		requerimentoUnidadeConservacaoDAO.executarNamedQuery("RequerimentoUnidadeConservacao.removerByIdeRequerimentoUnidadeConservacao", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAreaProtegida> obterTipoAreaProtegidaByRequerimento(Requerimento requerimento) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideRequerimento", requerimento.getIdeRequerimento());
		return tipoAreaProtegidaDAO.buscarPorNamedQuery("TipoAreaProtegida.findByIdeRequerimento", parameters);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAreaPreservacaoPermanente> obterTipoAreaPreservacaoPermanenteByRequerimento(Requerimento requerimento) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideRequerimento", requerimento.getIdeRequerimento());
		return tipoAreaPreservacaoPermanenteDAO.buscarPorNamedQuery("TipoAreaPreservacaoPermanente.findByIdeRequerimento", parameters);
	}
}