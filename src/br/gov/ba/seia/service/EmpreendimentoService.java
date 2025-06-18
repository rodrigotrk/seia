package br.gov.ba.seia.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Level;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.controller.FceTurismoController;
import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.EmpreendimentoDAOImpl;
import br.gov.ba.seia.dao.EmpreendimentoMunicipioConsultaDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.TipologiaGrupoDAOImpl;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.EmpreendimentoTipologia;
import br.gov.ba.seia.entity.EmpreendimentoTipologiaPK;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelEmpreendimento;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelUrbano;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.TipoImovel;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.VwConsultaEmpreendimento;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoImovelEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmpreendimentoService implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject
	private EmpreendimentoDAOImpl empreendimentoDAOImpl;

	@Inject
	IDAO<Empreendimento> daoEmpreendimento;
	
	@Inject
	IDAO<Imovel> daoImovel;
	
	@Inject
	IDAO<TramitacaoRequerimento> tramitacaoRequerimentoDao;

	@Inject
	IDAO<Tipologia> tipologiaDAO;
	
	@Inject
	EmpreendimentoMunicipioConsultaDAOImpl empreendimentoMunicipioDAOImpl;
	
	@Inject
	IDAO<EmpreendimentoTipologia> daoEmpreendimentoTipologia;
	
	@Inject
	IDAO<EnderecoEmpreendimento> daoEnderecoEmpreendimento;
	
	@Inject
	IDAO<EmpreendimentoRequerimento> empreendimentoRequerimentoDAO;
	
	@Inject
    IDAO<EmpreendimentoRequerimento> empreendimentoRequerimentoIDAO; 
	
	@Inject
	ImovelRuralService imovelRuralService;
	
	@Inject
	LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@Inject
	IDAO<ImovelEmpreendimento> imovelEmpreendimendoDao;
	
	@EJB
	private PermissaoPerfilService permissaoPerfilService;

	@EJB
	private ImovelService imovelService;
	
	@EJB
	private ImovelUrbanoService imovelUrbanoService;

	@EJB
	private EnderecoEmpreendimentoService enderecoEmpreendimentoService;

	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	private static final int QTD_MODULOS_FISCAIS_OBRIGATORIO = 4;

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscar(Empreendimento empreendimento) {
		return empreendimentoDAOImpl.buscar(empreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarNomeEmpreendimento(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class)
			.createAlias("ideRequerimento", "requerimento")
			.createAlias("ideEmpreendimento", "ideEmpreendimento")
			
			.add(Restrictions.eq("requerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		EmpreendimentoRequerimento empreedimentoRequerimento = empreendimentoRequerimentoDAO.buscarPorCriteria(criteria);
		
		if(empreedimentoRequerimento!=null && empreedimentoRequerimento.getIdeEmpreendimento()!=null){
			return empreedimentoRequerimento.getIdeEmpreendimento();
		}
		
		return null;
	}

	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<Empreendimento> listarEmpreendimento(Pessoa pPessoa) {
		Map<String, Object> paramEmpreendimento = new HashMap<String, Object>();
		paramEmpreendimento.put("indExcluido", false);
		paramEmpreendimento.put("idePessoa", pPessoa.getIdePessoa());
		return daoEmpreendimento.buscarPorNamedQuery("Empreendimento.findByIdePessoaAtivado", paramEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscarEmpreendimentoPorProcesso(Integer ideProcesso)  {

		StringBuilder sql = new StringBuilder();

		sql.append("select distinct e ");
		sql.append("from Empreendimento e ");
		sql.append("     inner join e.empreendimentoRequerimentoCollection er ");
		sql.append("     inner join er.ideRequerimento r ");
		sql.append("     inner join r.processoCollection p ");
		sql.append("where p.ideProcesso = :ideProcesso ");

		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideProcesso", ideProcesso);

		return daoEmpreendimento.buscarPorQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscarEmpreendimentoPorRequerimento(Integer ideRequerimento)  {
		return empreendimentoDAOImpl.buscarEmpreendimentoPorRequerimento(ideRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscarEmpreendimentoPorRequerimentoRetornandoImoveis(Integer ideRequerimento)  {

		StringBuilder sql = new StringBuilder();

		sql.append("select e ");
		sql.append("from Empreendimento e ");
		sql.append("     inner join e.empreendimentoRequerimentoCollection er ");
		sql.append("     inner join er.ideRequerimento r ");
		sql.append("     left join fetch e.imovelCollection i ");
		sql.append("     left join fetch i.ideTipoImovel ti ");
		sql.append("where r.ideRequerimento = :ideRequerimento ");

		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideRequerimento", ideRequerimento);

		return daoEmpreendimento.buscarPorQuery(sql.toString(), params);
	}

	/**
	 * @author eduardo Alexandre Queiroz
	 * @ 
	 * */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento getEmpreendimentoByRequerimento(Requerimento requerimento) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Empreendimento.class)
				.createAlias("empreendimentoRequerimentoCollection", "empReq", JoinType.INNER_JOIN)
				.createAlias("empReq.ideEmpreendimento", "emp", JoinType.INNER_JOIN)
				.createAlias("empReq.ideRequerimento", "req", JoinType.INNER_JOIN)
				.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()));
		return daoEmpreendimento.buscarPorCriteria(detachedCriteria);
	}
	
	
	/**
	 * @author Alexandre Queiroz
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer requerimentoVinculadoAoEmpreendimento(int ideEmpreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class)
				.createAlias("ideEmpreendimento","e")
				.add(Restrictions.eq("e.ideEmpreendimento",ideEmpreendimento))
				.setProjection(Projections.max("ideEmpreendimentoRequerimento"));
			
		 Object ideRequerimentoEmpreendimento = empreendimentoRequerimentoDAO.buscarPorCriteria(criteria); 
	     
		 if(Util.isNullOuVazio(ideRequerimentoEmpreendimento) || Util.isNullOuVazio(requerimentoByEmpreendimentoRequerimento((Integer)ideRequerimentoEmpreendimento))){
			 return -1;
		 } 
		 else{
			 return requerimentoByEmpreendimentoRequerimento((Integer)ideRequerimentoEmpreendimento);
		 }
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer requerimentoByEmpreendimentoRequerimento(int ideEmpreendimentoRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class)
			.add(Restrictions.eq("ideEmpreendimentoRequerimento", ideEmpreendimentoRequerimento));
		
		if(Util.isNullOuVazio(empreendimentoRequerimentoDAO.buscarPorCriteria(criteria).getIdeRequerimento().getIdeRequerimento())){
			return -1;
		}
		else {
			return empreendimentoRequerimentoDAO.buscarPorCriteria(criteria).getIdeRequerimento().getIdeRequerimento();
		}	
	}
	
	
	/**
	 * @author Alexandre Queiroz
	 * @ 
	 * @since 12/2014
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Integer> listRequerimentosDoEmpreendimento(Integer ideEmpreendimento) {
	
		DetachedCriteria criteria  = DetachedCriteria.forClass(EmpreendimentoRequerimento.class)
			.createAlias("Empreendimento", "E")
			.createAlias("Requerimento","r")
			.add(Restrictions.eq("E.ideEmpreendimento", ideEmpreendimento));
		
		List<EmpreendimentoRequerimento> listEmpreendimentoRequerimento = empreendimentoRequerimentoDAO.listarPorCriteria(criteria);		
		List<Integer> requerimentosDoEmprendimento = new ArrayList<Integer>();
		
		
		for (EmpreendimentoRequerimento er : listEmpreendimentoRequerimento) {
			requerimentosDoEmprendimento.add(er.getIdeRequerimento().getIdeRequerimento());
		}
		
		
		return requerimentosDoEmprendimento;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Integer statusEmpreedinmentoRequerimento (Integer ideEmpreendimento)  {
		
		DetachedCriteria criteriaSub = DetachedCriteria.forClass(TramitacaoRequerimento.class)
				.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
				.createAlias("req.empreendimentoRequerimentoCollection", "empReq", JoinType.INNER_JOIN)                
				.createAlias("empReq.ideEmpreendimento", "emp", JoinType.INNER_JOIN)
			.add(Restrictions.eq("emp.ideEmpreendimento", ideEmpreendimento))			
			.setProjection(Projections.max("ideTramitacaoRequerimento"));
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TramitacaoRequerimento.class)
			.add(Subqueries.propertyEq("ideTramitacaoRequerimento", criteriaSub))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideStatusRequerimento.ideStatusRequerimento"))
			);
	     Object  retorno  = tramitacaoRequerimentoDao.buscarPorCriteria(criteria); 
	     if(retorno==null) {return -1;}
	     return (Integer) retorno;
	}	

	/*
	 * @TransactionAttribute(TransactionAttributeType.REQUIRED) public void
	 * salvarEmpreendimento(Empreendimento pEmpreendimento) { if
	 * (!Util.isNullOuVazio(pEmpreendimento.getImovelCollection())) { for (Imovel
	 * lImovel : pEmpreendimento.getImovelCollection()) { ImovelUrbano lImovelUrbano
	 * = !Util.isNullOuVazio(lImovel.getImovelUrbano()) ?
	 * lImovel.getImovelUrbano().getClone() : null; lImovel.setImovelUrbano(null);
	 * imovelService.salvarAtualizarImovel(lImovel); if
	 * (!pEmpreendimento.getIndCessionario()) {
	 * lImovel.setImovelUrbano(lImovelUrbano); } else {
	 * lImovel.setImovelUrbano(null); } if (!Util.isNullOuVazio(lImovelUrbano)) {
	 * lImovelUrbano.setIdeImovelUrbano(lImovel.getIdeImovel());
	 * imovelUrbanoService.salvarAtualizarImovelUrbano(lImovelUrbano);
	 * lImovel.setImovelUrbano(lImovelUrbano); } } }
	 * 
	 * pEmpreendimento.setDtcCriacao(new Date());
	 * daoEmpreendimento.salvar(pEmpreendimento); }
	 */
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmpreendimento(Empreendimento empreendimento) {
		try {
		    if (Util.isNullOuVazio(empreendimento.getImovelCollection())) {
		    	JsfUtil.addErrorMessage("Erro ao registrar imóvel. Favor contatar administrador do sistema.");
		    } 
		    
		    Collection<Imovel> imoveisProcessados = new ArrayList<Imovel>();
		    for (Imovel imovel : empreendimento.getImovelCollection()) {
		    	if(imovel.getIdeTipoImovel().getIdeTipoImovel() == TipoImovelEnum.URBANO.getId()){
			    	processarImovel(empreendimento, imovel);
		    	} else {
		    		imovel.setImovelUrbano(null);
		    	}
		    	imoveisProcessados.add(imovel);
		    }
		    
		    empreendimento.setImovelCollection(imoveisProcessados);
		    
		    empreendimento.setDtcCriacao(new Date());
		    daoEmpreendimento.salvar(empreendimento);
		    
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage("Erro ao registrar empreendimento. Favor contatar administrador do sistema.");
		}
		

	}

	private void processarImovel(Empreendimento empreendimento, Imovel imovel) {
	    ImovelUrbano originalImovelUrbano = getClone(imovel.getImovelUrbano());
	    imovel.setImovelUrbano(null);
	    imovel.setImovelRural(null);

	    imovelService.salvarAtualizarImovel(imovel);

	    if (empreendimento.getIndCessionario()) {
	        imovel.setImovelUrbano(null);
	    } else {
	        imovel.setImovelUrbano(originalImovelUrbano);
	    }

	    if (!Util.isNullOuVazio(originalImovelUrbano)) {
	    	originalImovelUrbano.setIndExcluido(false);
	    	atualizarImovelUrbano(imovel, originalImovelUrbano);
	    }
	}

	private ImovelUrbano getClone(ImovelUrbano imovelUrbano) {
	    return (imovelUrbano != null) ? imovelUrbano.getClone() : null;
	}


	private void atualizarImovelUrbano(Imovel imovel, ImovelUrbano imovelUrbano) {
	    imovelUrbano.setIdeImovelUrbano(imovel.getIdeImovel());
	    imovelUrbanoService.salvarAtualizarImovelUrbano(imovelUrbano);
	    imovel.setImovelUrbano(imovelUrbano);
	}
	
	/**
	 * @author micael.coutinho
	 * 
	 * Retorna o imóvel com menor data Empreendimentos Associados ao Imovel e Ordenado Por Data de Criação onde o imovel é igual ao do parametro.
	 * @param imovel
	 * @return List<Empreendimento>
	 * @throws Exception 
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscaEmpreendimentoAssociadoAoImovelOrdenadoPorDataCriacao(Imovel imovel) throws Exception {
		List<Empreendimento> list = empreendimentoDAOImpl.listaEmpreendimentoAssociadoAoImovelOrdenadoPorDataCriacao(imovel);
		if(!Util.isNullOuVazio(list)) {
			return list.get(0);
		} else {
			JsfUtil.addErrorMessage("Não há empreendimento relacionado ligado ao Imóvel informado.");
			throw new Exception("Não há empreendimento relacionado ligado ao Imóvel informado.");
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmpreendimentoTipologia(EmpreendimentoTipologia pEmpreendimentoTipologia) throws Exception  {
		
		if(pEmpreendimentoTipologia != null && !Util.isNullOuVazio(pEmpreendimentoTipologia.getEmpreendimento())
				&& !Util.isNullOuVazio(pEmpreendimentoTipologia.getTipologiaGrupo())) {
			
			pEmpreendimentoTipologia.setEmpreendimentoTipologiaPK(
					new EmpreendimentoTipologiaPK(
							pEmpreendimentoTipologia.getEmpreendimento().getIdeEmpreendimento().intValue(), 
							pEmpreendimentoTipologia.getTipologiaGrupo().getIdeTipologiaGrupo().intValue()));
			
			daoEmpreendimentoTipologia.salvar(pEmpreendimentoTipologia);
		} else {
			JsfUtil.addErrorMessage("Erro ao selecionar a tipologia!");
			throw new Exception("Erro ao selecionar a tipologia!"); 
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirEmpreendimentoTipologia(EmpreendimentoTipologia pEmpreendimentoTipologia)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideEmpreendimento", pEmpreendimentoTipologia.getEmpreendimento().getIdeEmpreendimento());
		parametros.put("ideTipologiaGrupo", pEmpreendimentoTipologia.getTipologiaGrupo().getIdeTipologiaGrupo());
		daoEmpreendimentoTipologia.executarNamedQuery("EmpreendimentoTipologia.delete", parametros);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Collection<EmpreendimentoTipologia> buscarEmpreendimentoTipologia(Empreendimento empreendimento)  {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(EmpreendimentoTipologia.class, "et")
			.createAlias("et.tipologiaGrupo", "tg", JoinType.LEFT_OUTER_JOIN)
			.createAlias("tg.ideTipologia", "t", JoinType.LEFT_OUTER_JOIN)
			.createAlias("et.empreendimento", "emp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("tg.idePotencialPoluicao", "pp", JoinType.LEFT_OUTER_JOIN)

			.add(Restrictions.eq("et.empreendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()))
			.add(Restrictions.eq("tg.indExcluido", false))

			.setProjection(Projections.projectionList()
					.add(Projections.property("et.empreendimentoTipologiaPK"), "empreendimentoTipologiaPK")
					.add(Projections.property("et.indPermanente"), "indPermanente")
					.add(Projections.property("emp.ideEmpreendimento"), "empreendimento.ideEmpreendimento")
					.add(Projections.property("tg.ideTipologiaGrupo"), "tipologiaGrupo.ideTipologiaGrupo")
					.add(Projections.property("tg.dtcCriacao"), "tipologiaGrupo.dtcCriacao")
					.add(Projections.property("tg.dtcExcluido"), "tipologiaGrupo.dtcExcluido")
					.add(Projections.property("tg.indExcluido"), "tipologiaGrupo.indExcluido")
					.add(Projections.property("t.ideTipologia"), "tipologiaGrupo.ideTipologia.ideTipologia")
					.add(Projections.property("t.codTipologia"), "tipologiaGrupo.ideTipologia.codTipologia")
					.add(Projections.property("t.desTipologia"), "tipologiaGrupo.ideTipologia.desTipologia")
					.add(Projections.property("t.dtcCriacao"), "tipologiaGrupo.ideTipologia.dtcCriacao")
					.add(Projections.property("t.dtcExclusao"), "tipologiaGrupo.ideTipologia.dtcExclusao")
					.add(Projections.property("t.indExcluido"), "tipologiaGrupo.ideTipologia.indExcluido")
					.add(Projections.property("t.indAutorizacao"), "tipologiaGrupo.ideTipologia.indAutorizacao")
					.add(Projections.property("t.indPossuiFilhos"), "tipologiaGrupo.ideTipologia.indPossuiFilhos")
					.add(Projections.property("pp.idePotencialPoluicao"), "tipologiaGrupo.idePotencialPoluicao.idePotencialPoluicao")
					.add(Projections.property("pp.sglPotencialPoluicao"), "tipologiaGrupo.idePotencialPoluicao.sglPotencialPoluicao")
					.add(Projections.property("pp.nomPotencialPoluicao"), "tipologiaGrupo.idePotencialPoluicao.nomPotencialPoluicao")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EmpreendimentoTipologia.class))
		;

		return daoEmpreendimentoTipologia.listarPorCriteria(detachedCriteria);
	}

	@SuppressWarnings({ "deprecated", "deprecation" })
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarEmpreendimento(Empreendimento pEmpreendimento)  {
		
		if (!Util.isNullOuVazio(pEmpreendimento.getImovelCollection())) {
		    List<Imovel> imovelCollection = (List<Imovel>) pEmpreendimento.getImovelCollection();

		    for (int i = 0; i < imovelCollection.size(); i++) {
		    	
		        Imovel imovel = imovelCollection.get(i);
		        Imovel updatedImovel = atualizarImoveis(imovel);
		        imovelCollection.set(i, updatedImovel);
		    }
		    
		    pEmpreendimento.setImovelCollection(imovelCollection);
		}
		
		if(!Util.isLazyInitExcepOuNull(pEmpreendimento.getEnderecoEmpreendimentoCollection())) {

			List<EnderecoEmpreendimento> lEE = new ArrayList<EnderecoEmpreendimento>();

			for (EnderecoEmpreendimento ee : pEmpreendimento.getEnderecoEmpreendimentoCollection()) {

				EnderecoEmpreendimento enderecoEmpreendimento = enderecoEmpreendimentoService.carregarIDEnderecoEmpreendimentoExistente(ee);

				if(!Util.isNullOuVazio(enderecoEmpreendimento)) {
					lEE.add(enderecoEmpreendimento);
				}
			}

			pEmpreendimento.setEnderecoEmpreendimentoCollection(lEE);
		}

		daoEmpreendimento.salvarOuAtualizar(pEmpreendimento);
	}
	
	private Imovel atualizarImoveis(Imovel imovel) {
	    ImovelUrbano imovelUrbano = imovelUrbanoService.buscarByIdeImovel(imovel.getIdeImovel());

	    if (validarImovelUrbano(imovel)) {
	        imovel = atualizarImovelUrbanoEmpreendimento(imovel);
	    } else if (Util.isLazyInitExcepOuNull(imovel.getIdeTipoImovel())) {
	        lazyInitializedImovel(imovel, imovelUrbano);
	    } else if (isTipoImovelCessionario(imovel.getIdeTipoImovel().getIdeTipoImovel())) {
	        tratarImovelCessionario(imovel, imovelUrbano);
	    } else {
	        excluirImovelUrbano(imovelUrbano);
	    }

	    return imovel;
	}
	
	private Imovel atualizarImovelUrbanoEmpreendimento(Imovel imovel) {
	    ImovelUrbano imovelUrbano = imovel.getImovelUrbano();

	    if (Util.isNullOuVazio(imovel.getIdeImovel())) {
	        imovel = new Imovel();
	        imovel.setIdeTipoImovel(new TipoImovel(TipoImovelEnum.URBANO.getId()));
	        imovel.setIndArrendado(false);
	        imovel.setIndExcluido(false);
	        imovel.setDtcCriacao(new Date());

	        imovelService.salvarAtualizarImovel(imovel);
	    }

	    imovelUrbano.setIdeImovelUrbano(imovel.getIdeImovel());
	    imovelUrbano.setIndExcluido(false);

	    imovelUrbanoService.salvarAtualizarImovelUrbano(imovelUrbano);
	    
	    imovel.setImovelUrbano(imovelUrbano);

	    return imovel;
	}
	
	private void lazyInitializedImovel(Imovel imovel, ImovelUrbano imovelUrbano) {
	    if (isTipoImovelCessionario(imovel.getIdeImovel())) {
	        if (!Util.isNullOuVazio(imovelUrbano)) {
	        	excluirImovelUrbano(imovelUrbano);
	        }

		    imovel.setIdeTipoImovel(new TipoImovel(TipoImovelEnum.CESSIONARIO.getId()));
		    imovelService.salvarAtualizarImovel(imovel);
	    }
	}
	
	private void tratarImovelCessionario(Imovel imovel, ImovelUrbano imovelUrbano) {
	    if (!Util.isNullOuVazio(imovelUrbano)) {
	    	excluirImovelUrbano(imovelUrbano);
	    }

	    imovelService.salvarAtualizarImovel(imovel);
	}
	
	private void excluirImovelUrbano(ImovelUrbano imovelUrbano) {
	    if (!Util.isNullOuVazio(imovelUrbano)) {
	        imovelUrbano.setIndExcluido(true);
	        imovelUrbanoService.salvarAtualizarImovelUrbano(imovelUrbano);
	    }
	}

	private boolean isTipoImovelCessionario(Integer ideImovel) {
	    return (ideImovel).equals(TipoImovelEnum.CESSIONARIO.getId());
	}
	
	private boolean validarImovelUrbano(Imovel imovel) {
	    return !Util.isNullOuVazio(imovel.getImovelUrbano())
	            && !Util.isNullOuVazio(imovel.getImovelUrbano().getNumInscricaoIptu());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarLocalizacaoGeograficaEmpreendimento(Empreendimento empreendimento)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideLocGeo", empreendimento.getIdeLocalizacaoGeografica());
		parametros.put("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		daoEmpreendimento.executarNamedQuery("Empreendimento.atualizarLocGeoEmpreendimento", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Empreendimento empreendimento)  {
		daoEmpreendimento.atualizar(empreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void setaPai(TreeNode pRaiz, Tipologia pTipologia) {
		if (pRaiz.getData() instanceof Tipologia) {
			Tipologia lTipologia = (Tipologia) pRaiz.getData();

			if (pTipologia.getIdeTipologiaPai().getIdeTipologia().equals(lTipologia.getIdeTipologia())) {
				if (!pTipologia.getIndPossuiFilhos()) {
					Collection<TipologiaGrupo> lColTipologiaGrupo = new TipologiaGrupoDAOImpl().getTipologiasGrupos(new TipologiaGrupo(pTipologia));

					if (!Util.isNullOuVazio(lColTipologiaGrupo)) {
						for (TipologiaGrupo lTipologiaGrupo : lColTipologiaGrupo) {
							Tipologia lCloneTipologia = pTipologia.getClone();
							lCloneTipologia.setTipologiaGrupo(lTipologiaGrupo);
							new DefaultTreeNode(lCloneTipologia, pRaiz);
						}

					}
				} else {
					new DefaultTreeNode(pTipologia, pRaiz);
				}
			}
		}

		if (pRaiz.getChildCount() > 0) {
			for (TreeNode lTreeNode : pRaiz.getChildren()) {
				setaPai(lTreeNode, pTipologia);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TreeNode montarArvoreTipologia(Tipologia pTipologiaSelecionada) {
		TreeNode lNoRaiz = new DefaultTreeNode("RAIZ", null);
		List<Tipologia> lColecaoTipologia = empreendimentoDAOImpl.getTipologiasByCriteria();
		Exception erro = null;
		try {

			Collection<Tipologia> tipologiaCollectionAuxiliar = new ArrayList<Tipologia>();
			if (!Util.isNullOuVazio(lColecaoTipologia)) {
				for (Tipologia lTipologia : lColecaoTipologia) {
					if (!Util.isNullOuVazio(lTipologia.getTipologiaGrupo())){
						Tipologia tipologiaClone = lTipologia.getClone();
						tipologiaClone.setDesTipologia(lTipologia.getDesTipologia());
						tipologiaCollectionAuxiliar.add(tipologiaClone);
					} else {
						tipologiaCollectionAuxiliar.add(lTipologia);
					}
				}
			}

			if (!Util.isNullOuVazio(tipologiaCollectionAuxiliar)) {
				for (Tipologia lTipologia : tipologiaCollectionAuxiliar) {
					// Tipologia tem filhos então tipologia é pai
					if (lTipologia.getIdeTipologia().equals(pTipologiaSelecionada.getIdeTipologia())) {
						TreeNode noTiplogiaPaiCombo = new DefaultTreeNode(lTipologia, lNoRaiz);
						montarTreeNodeTipologia(noTiplogiaPaiCombo, tipologiaCollectionAuxiliar);
					}
				}
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

		return lNoRaiz;
	}

	public TreeNode montarTreeNodeTipologia(TreeNode lNoRaiz, Collection<Tipologia> tipologias) {
		TreeNode noPai = lNoRaiz;
		Tipologia tipologiaPai = (Tipologia) lNoRaiz.getData();
		/** tipologia tem filhos*/
		if (tipologiaPai.getIndPossuiFilhos()) {
			/** Verifica os filhos do pai*/
			for (Tipologia tipologia : tipologias) {
				/** filho é realmente filho de pai*/
				if (!Util.isNullOuVazio(tipologia.getIdeTipologiaPai())
						&& tipologia.getIdeTipologiaPai().getIdeTipologia().equals(tipologiaPai.getIdeTipologia())) {
					@SuppressWarnings("unused")
					TreeNode noTipologiaFilho = new DefaultTreeNode(tipologia, noPai);
				}
			}

			if (noPai.getChildCount() > 0) {
				for (TreeNode lTreeNode : noPai.getChildren()) {
					montarTreeNodeTipologia(lTreeNode, tipologias);
				}
			}
		}
		return lNoRaiz;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<EnderecoEmpreendimento> listarEnderecosEmpreendimentoComParametro(Municipio mun, Pessoa requerente, String nome)  {
		/** Se usu�rio externo, mostra todos os empreendimentos seguindo as
		regras 56 e 177.*/
		if (ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil() == 2) {
			List<Integer> lst = new ArrayList<Integer>();
			lst.addAll(permissaoPerfilService.listarIdesPessoaFisicaAptos());
			lst.addAll(permissaoPerfilService.listarIdesPessoaJuridicaAptos());

			return empreendimentoMunicipioDAOImpl.listaEmpreendimentosComParametro(mun, requerente, nome, lst);
		} else {
			return empreendimentoMunicipioDAOImpl.listaEmpreendimentosComParametro(mun, requerente, nome, null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento filtrarEmpreendimento(Empreendimento pEmpreendimento)  {
	
		try {
			
			List<EmpreendimentoTipologia> lColecaoEmpreendimentoTipologiaNaoExcluidos = new ArrayList<EmpreendimentoTipologia>();
			
			pEmpreendimento = empreendimentoDAOImpl.buscarEmpreendimentoComImovelPessoaELocalizacaoCarregadas(pEmpreendimento);
			
			Empreendimento lEmpreendimento = pEmpreendimento.clone();
			
			lEmpreendimento.setEmpreendimentoTipologiaCollection(buscarEmpreendimentoTipologia(lEmpreendimento));
			
			for (EmpreendimentoTipologia lEmpreendimentoTipologia : lEmpreendimento.getEmpreendimentoTipologiaCollection()) {
				if (!Util.isNullOuVazio(lEmpreendimentoTipologia.getTipologiaGrupo()) && !lEmpreendimentoTipologia.getTipologiaGrupo().getIndExcluido()) {
					lColecaoEmpreendimentoTipologiaNaoExcluidos.add(lEmpreendimentoTipologia);
				}
			}
			
			lEmpreendimento.setEmpreendimentoTipologiaCollection(lColecaoEmpreendimentoTipologiaNaoExcluidos);
			
			lEmpreendimento.setImovelCollection(imovelService.filtrarListaImovelPorEmpreendimento(lEmpreendimento));
			
			return lEmpreendimento;
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirEmpreendimento(Empreendimento pEmpreendimento)  {
		empreendimentoDAOImpl.excluirEmpreendimento(pEmpreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEndereco(EnderecoEmpreendimento pEnderecoEmpreendimento)  {
		daoEnderecoEmpreendimento.salvarOuAtualizar(pEnderecoEmpreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarById(Integer id){
		Empreendimento emp;
		emp = daoEmpreendimento.carregarGet(id);
		//Hibernate.initialize(emp.getImovelCollection());
		emp.setImovelCollection(imovelService.filtrarListaImovelPorEmpreendimento(emp));
		Hibernate.initialize(emp.getResponsavelEmpreendimentoCollection());

		 for ( ResponsavelEmpreendimento respEmpreend :
			 emp.getResponsavelEmpreendimentoCollection()) {
			 Hibernate.initialize(respEmpreend.getIdePessoaFisica()); 
		 }
		 
		return emp;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarById(Empreendimento empreendimento){
		
		if(Util.isLazyInitExcepOuNull(empreendimento.getImovelCollection())) {
			/* Hibernate.initialize(empreendimento.getImovelCollection()); */
			empreendimento.setImovelCollection(imovelService.filtrarListaImovelPorEmpreendimento(empreendimento));

		}
		if(Util.isLazyInitExcepOuNull(empreendimento.getResponsavelEmpreendimentoCollection())) {
			Hibernate.initialize(empreendimento.getResponsavelEmpreendimentoCollection());
		}
		
		 for ( ResponsavelEmpreendimento respEmpreend :
			 empreendimento.getResponsavelEmpreendimentoCollection()) {
			 //Hibernate.initialize(respEmpreend.getIdePessoaFisica());
			 respEmpreend.setIdePessoaFisica(pessoaFisicaService.buscarPessoaFisica(respEmpreend.getIdePessoaFisica()));
		 }

		 Empreendimento emp = daoEmpreendimento.carregarGet(empreendimento.getId());
		 
		 empreendimento.setIdePessoa(emp.getIdePessoa());

		return empreendimento;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Empreendimento carregarPorIdComMunicipio(Integer ideEmpreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Empreendimento.class, "empreendimento" )
			.createAlias("enderecoEmpreendimentoCollection", "enderecoEmpreendColl", JoinType.INNER_JOIN)
			.createAlias("enderecoEmpreendimentoCollection.ideTipoEndereco", "tipoEndereco", JoinType.INNER_JOIN)
			.createAlias("enderecoEmpreendColl.ideEndereco", "endereco", JoinType.INNER_JOIN)
			.createAlias("endereco.ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN)
			.createAlias("logradouro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN)
			.createAlias("logradouro.ideTipoLogradouro", "tipoLogradouro", JoinType.LEFT_OUTER_JOIN)
			.createAlias("logradouro.ideBairro", "bairro", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideLocalizacaoGeografica", "localizacao", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("ideEmpreendimento", ideEmpreendimento));
		Empreendimento emp = daoEmpreendimento.buscarPorCriteria(criteria);
		
		Hibernate.initialize(emp.getImovelCollection());
		Hibernate.initialize(emp.getResponsavelEmpreendimentoCollection());
		
		if(!Util.isNullOuVazio(emp.getEnderecoEmpreendimentoCollection())) {
			for (EnderecoEmpreendimento endEmpreend : emp.getEnderecoEmpreendimentoCollection()) {
				if (endEmpreend.getIdeTipoEndereco().getIdeTipoEndereco().equals(TipoEnderecoEnum.LOCALIZACAO.getId())) {
					emp.setEndereco(endEmpreend.getIdeEndereco());
				} else if(endEmpreend.getIdeTipoEndereco().getIdeTipoEndereco().equals(TipoEnderecoEnum.CORRESPONDENCIA.getId())) {
					emp.setEnderecoCorrespondencia(endEmpreend.getIdeEndereco());
					if(emp.getEnderecoEmpreendimentoCollection().size() == 1){
						emp.setEndereco(endEmpreend.getIdeEndereco());
					}
				}
			}
		}
		
		return emp;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<EnderecoEmpreendimento> listarEnderecoByEmpreendimento(EnderecoEmpreendimento enderecoEmpreendimento)  {
		if (!Util.isNullOuVazio(enderecoEmpreendimento) && !Util.isNullOuVazio(enderecoEmpreendimento.getIdeEmpreendimento())) {
			DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimento.class);
			criteria.createAlias("ideEndereco", "endereco");
			criteria.add(Restrictions.eq("ideEmpreendimento.ideEmpreendimento", enderecoEmpreendimento.getIdeEmpreendimento().getIdeEmpreendimento()));
			return daoEnderecoEmpreendimento.listarPorCriteria(criteria, Order.asc("ideEnderecoEmpreendimento"));
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarComLocalizacaoGeografica(Empreendimento pEmpreendimento)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(Empreendimento.class);
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("localizacaoGeografica.ideSistemaCoordenada", "datum", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("localizacaoGeografica.ideClassificacaoSecao", "classificacaoSecao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideEmpreendimento", pEmpreendimento.getIdeEmpreendimento()));
		Empreendimento empreend = daoEmpreendimento.buscarPorCriteria(criteria);

		if(!Util.isNullOuVazio(empreend.getIdeLocalizacaoGeografica()) && !Util.isNullOuVazio(empreend.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			LocalizacaoGeografica loc = empreend.getIdeLocalizacaoGeografica();
			empreend.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(loc, loc.getIdeClassificacaoSecao().getIdeClassificacaoSecao()));
		}
		return empreend;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Empreendimento obterTipoImovelPorIdeEmpreendimento(Integer ideEmpreendimento){
		Empreendimento emp;
		emp = daoEmpreendimento.carregarGet(ideEmpreendimento);
		Hibernate.initialize(emp.getImovelCollection());
		for (Imovel i : emp.getImovelCollection()) {
			Hibernate.initialize(i.getIdeTipoImovel());
		}
		return emp;
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EmpreendimentoTipologia> listaEmpreendimentoTipologia(Integer ideEmpreendimento)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoTipologia.class);

		criteria
		.createAlias("tipologiaGrupo","tg",JoinType.INNER_JOIN)
		.createAlias("tg.ideTipologia","t",JoinType.INNER_JOIN)
		.createAlias("empreendimento","emp",JoinType.INNER_JOIN)
		.add(Restrictions.eq("t.indExcluido", false))
		.add(Restrictions.eq("tg.indExcluido", false))
		.add(Restrictions.eq("emp.ideEmpreendimento", ideEmpreendimento))
		.setProjection(Projections.projectionList()
				.add(Projections.property("emp.ideEmpreendimento"), "empreendimento.ideEmpreendimento")
				.add(Projections.property("emp.indBaseOperacional"), "empreendimento.indBaseOperacional")
				.add(Projections.property("tg.ideTipologiaGrupo"), "tipologiaGrupo.ideTipologiaGrupo")
				.add(Projections.property("t.ideTipologia"), "tipologiaGrupo.ideTipologia.ideTipologia")
				.add(Projections.property("indPermanente"), "indPermanente")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(EmpreendimentoTipologia.class))
		;
		return daoEmpreendimentoTipologia.listarPorCriteria(criteria);
	}


	/**
	 * Valida o Empreendimentode acordo com a regra: ZCR/RN0026
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean isEmpreendimentoValido(Empreendimento empreendimento)  {
		Exception erro = null;
		try{
			if (Util.isNullOuVazio(empreendimento) || Util.isNullOuVazio(empreendimento.getIdeEmpreendimento())) {
				System.out.println("Empreendimento nulo.");
				return false;
			}
			empreendimento = carregarById(empreendimento);
			boolean imovelUrbano = false;

			if (Util.isNullOuVazio(empreendimento.getIdePessoa())) {
				JsfUtil.addErrorMessage("Seus dados do Empreendimento não contém um requerente. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
				return false;
			}
			if (Util.isNullOuVazio(empreendimento.getNomEmpreendimento())) {
				JsfUtil.addErrorMessage("Seus dados do Empreendimento não contém um nome válido. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
				return false;
			}

			if (Util.isNullOuVazio(empreendimento.getDesEmail())) {
				JsfUtil.addErrorMessage("Seus dados de Empreendimento não contém o email. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
				return false;
			}

			
			List<EmpreendimentoTipologia> listaEmpreendimentoTipologia = listaEmpreendimentoTipologia(empreendimento.getIdeEmpreendimento());
			empreendimento.setEmpreendimentoTipologiaCollection(listaEmpreendimentoTipologia);

			if (empreendimento.getEmpreendimentoTipologiaCollection().size() < 1) {
				JsfUtil.addErrorMessage("Seus dados de Empreendimento não contém Tipologia. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
				return false;
			}

			for(EmpreendimentoTipologia empTipologia : empreendimento.getEmpreendimentoTipologiaCollection()){
				if(empTipologia.getTipologiaGrupo().getIdeTipologia().getIdeTipologia() == 207 && Util.isNullOuVazio(empreendimento.getIndBaseOperacional())){//Se a tipologia selecionada for "Transportadora de resíduos e/ou produtos perigosos e de serviços de saúde"
					JsfUtil.addErrorMessage("A pergunta O empreendimento possui base operacional no estado da bahia, no cadastro de Empreendimento não está preenchida. Favor retornar ao Cadastro do Empreendimento e responder a pergunta.");
					return false;
				}
			}
			empreendimento.setEnderecoEmpreendimentoCollection(enderecoEmpreendimentoService.filtraEnderecoEmpreendimentoByEmpreendimento(empreendimento));
			
			if (empreendimento.getEnderecoEmpreendimentoCollection().size() < 1) {
				JsfUtil.addErrorMessage("Seus dados de endereço na aba de Empreendimento estão incompletos. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
				return false;
			}

			Empreendimento empreendimentoTemp = carregarComLocalizacaoGeografica(empreendimento);
			
			empreendimento.setIdeLocalizacaoGeografica(empreendimentoTemp.getIdeLocalizacaoGeografica());
			
			if (Util.isNullOuVazio(empreendimento.getIdeLocalizacaoGeografica()) || Util.isNullOuVazio(empreendimento.getIdeLocalizacaoGeografica().getDadoGeograficoCollection())) {
				JsfUtil.addErrorMessage("Seus dados de Localização Geográfica estão incompletos. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
				return false;
			}

			if(!Util.isNullOuVazio(empreendimento.getImovelCollection())){
				for(Imovel imovel : empreendimento.getImovelCollection()){
					if(imovel.getImovelUrbano() != null){
						imovelUrbano = true;
						break;
					}
				}
			}

			//Se chegar até aqui e for um empreendimento cessionário então está tudo ok com o empreendimento.

			if(isEmpreendimentoCessionario(empreendimento) || isEmpreendimentoConversaoTcra(empreendimento)){
				return true;
			}

			if(!imovelUrbano && Util.isNullOuVazio(empreendimento.getImovelCollection())  ){
				JsfUtil.addErrorMessage("Seus dados de Imóvel estão incompletos. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
				return false;
			}
			else{
				if(!imovelUrbano && !isImoveisEmpreendimentoValidado(empreendimento)){
					JsfUtil.addErrorMessage("Seus dados de Imóvel estão incompletos. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
					return false;
				}
			}

			if(!imovelUrbano){
				if(Util.isLazyInitExcepOuNull(empreendimento.getImovelCollection())) {
					Hibernate.initialize(empreendimento.getImovelCollection());
				}
				if (empreendimento.getImovelCollection().isEmpty()) {
					JsfUtil.addErrorMessage("Seus dados de Imóvel estão incompletos. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
					return false;
				} else {
					for (Imovel imovel : empreendimento.getImovelCollection()) {
						Boolean valido = false;
						if (Util.isNullOuVazio(imovel.getImovelUrbano())) {
							valido = imovelRuralService.isImovelRuralValido(imovel.getImovelRural());
						} else {
							valido = true;
						}
						if (!valido) {
							return valido;
						}
						
						
					}
				}
			}
			
			if (imovelUrbano && empreendimento.getImovelCollection() != null) {
				for (Imovel imovel : empreendimento.getImovelCollection()) {
					if (imovel.getImovelUrbano().getNumInscricaoIptu() == null
							|| imovel.getImovelUrbano().getNumInscricaoIptu().isEmpty()) {
						JsfUtil.addErrorMessage(
								"Seus dados de Imóvel estão incompletos. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
						return false;
					}
				}

			}
			
		}catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro na validação do Empreendimento.");
		}	finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
		return true;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<VwConsultaEmpreendimento> listarPorCriteriaDemanda(Municipio mun, Pessoa requerente, String nome, int first, int pageSize)  {
		List<Integer> listaIdesAptos = new ArrayList<Integer>();
		listaIdesAptos.addAll(permissaoPerfilService.listarIdesPessoaFisicaAptos());
		listaIdesAptos.addAll(permissaoPerfilService.listarIdesPessoaJuridicaAptos());

		return this.empreendimentoDAOImpl.listarPorCriteriaDemanda(mun, requerente, nome, listaIdesAptos, first, pageSize);
	}

	public Boolean isImoveisEmpreendimentoValidado(Empreendimento empreendimento) {
		Boolean valido = true;
		for (Imovel imovel:empreendimento.getImovelCollection()) {
			if(!validaImovelparaRequerimento(imovel.getImovelRural())){
				valido = false;
				break;
			}
		}
		return valido;
	}

	/**
	 * 
	 * Valida o imovel rural de acordo com a regra: ZCR/RN0027
	 * @
	 */
	public Boolean validaImovelparaRequerimento(ImovelRural imovelRural)  {

		if(Util.isNullOuVazio(imovelRural)){
			JsfUtil.addErrorMessage("Seus dados de Empreendimento não contém um imóvel. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}

		if(Util.isNullOuVazio(imovelRural.getNumItr()) && imovelRural.getQtdModuloFiscal() >= QTD_MODULOS_FISCAIS_OBRIGATORIO){
			JsfUtil.addErrorMessage("Seus dados de Imóvel não contém um número de itr. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}

		if(Util.isNullOuVazio(imovelRural.getDesDenominacao())){
			JsfUtil.addErrorMessage("Seus dados de Imóvel não contém uma denominação. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}
		imovelRural.getImovel().setPessoaImovelCollection(imovelRuralService.filtrarPessoasPorImovel(imovelRural.getImovel()));
		
		if(imovelRural.getImovel().getPessoaImovelCollection().isEmpty()){
			JsfUtil.addErrorMessage("Seus dados de Imóvel não contém um proprietário. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}

		if(Util.isNullOuVazio(imovelRural.getValArea())){
			JsfUtil.addErrorMessage("Seus dados de Imóvel não contém o valor de área válido. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}

		if(Util.isNullOuVazio(imovelRural.getNumFolha()) && imovelRural.getQtdModuloFiscal() >= QTD_MODULOS_FISCAIS_OBRIGATORIO) {
			JsfUtil.addErrorMessage("Seus dados de Imóvel não contém o número da folha. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}

		if(Util.isNullOuVazio(imovelRural.getDesLivro()) && imovelRural.getQtdModuloFiscal() >= QTD_MODULOS_FISCAIS_OBRIGATORIO) {
			JsfUtil.addErrorMessage("Seus dados de Imóvel não contém uma descrição do livro. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}

		if(Util.isNullOuVazio(imovelRural.getDesCartorio()) && imovelRural.getQtdModuloFiscal() >= QTD_MODULOS_FISCAIS_OBRIGATORIO) {
			JsfUtil.addErrorMessage("Seus dados de Imóvel não contém uma descrição do cartório. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}

		if(Util.isNullOuVazio(imovelRural.getDesComarca()) && imovelRural.getQtdModuloFiscal() >= QTD_MODULOS_FISCAIS_OBRIGATORIO) {
			JsfUtil.addErrorMessage("Seus dados de Imóvel não contém uma comarca válida. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}

		if(Util.isNullOuVazio(imovelRural.getNumMatricula()) && Util.isNullOuVazio(imovelRural.getNumRegistro()) && imovelRural.getQtdModuloFiscal() >= QTD_MODULOS_FISCAIS_OBRIGATORIO) {
			JsfUtil.addErrorMessage("Seus dados de Imóvel  não contém um número de mátricula e registro válidos. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}

		if(Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica())){
			JsfUtil.addErrorMessage("Seus dados de Localização Geográfica na aba de Imóveis estão incompletos. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}


		if(Util.isNullOuVazio(imovelRural.getImovel().getIdeEndereco())) {
			JsfUtil.addErrorMessage("Seus dados de Endereço na aba Imóvel estão incompletos. Favor retornar ao Cadastro do Empreendimento e preencher as informações necessárias.");
			return false;
		}
		return true;

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Integer count(Municipio mun, Pessoa requerente, String nome)  {
		List<Integer> listaIdesAptos = new ArrayList<Integer>();
		listaIdesAptos.addAll(permissaoPerfilService.listarIdesPessoaFisicaAptos());
		listaIdesAptos.addAll(permissaoPerfilService.listarIdesPessoaJuridicaAptos());

		return empreendimentoDAOImpl.count(mun, requerente, nome, listaIdesAptos);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarByIdeRequerimento(Integer ideRequerimento)  {
		return empreendimentoDAOImpl.carregarByIdeRequerimento(ideRequerimento);
	}

	private Boolean isEmpreendimentoCessionario(Empreendimento empreendimento){
		
			return (!Util.isNullOuVazio(empreendimento.getIndCessionario()) && empreendimento.getIndCessionario());
	}

	private Boolean isEmpreendimentoConversaoTcra(Empreendimento empreendimento){
		return !Util.isNullOuVazio(empreendimento.getIndConversaoTcraLac()) && empreendimento.getIndConversaoTcraLac();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Empreendimento> buscarEmpreendimentoPorRequerimento(Requerimento requerimento) {
		
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimento", requerimento.getIdeRequerimento());
		return daoEmpreendimento.buscarPorNamedQuery("Empreendimento.findByIdeRequerimento", parametros);
	}
	
	public Collection<Tipologia> buscarTipologias(Empreendimento empreendimento, boolean filtrarTipologiaExcluida, boolean filtrarTipologiaGrupoExcluida)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class)
			.createAlias("tipologiaGrupo", "tg", JoinType.LEFT_OUTER_JOIN)
			.createAlias("tg.unidadeMedidaTipologiaGrupo", "utg", JoinType.LEFT_OUTER_JOIN)
			.createAlias("utg.ideUnidadeMedida", "un", JoinType.LEFT_OUTER_JOIN)
			.createAlias("tg.empreendimentoTipologiaCollection", "et")
			.createAlias("et.empreendimento", "ep")
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipologia"),"ideTipologia")
				.add(Projections.property("codTipologia"),"codTipologia")
				.add(Projections.property("desTipologia"),"desTipologia")
		
				.add(Projections.property("tg.ideTipologiaGrupo"),"tipologiaGrupo.ideTipologiaGrupo")
		
				.add(Projections.property("utg.ideUnidadeMedidaTipologiaGrupo"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo")
		
				.add(Projections.property("un.ideUnidadeMedida"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedida.ideUnidadeMedida")
				.add(Projections.property("un.codUnidadeMedida"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedida.codUnidadeMedida")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class))
			
			.add(Restrictions.ne("codTipologia", "X"))
			.add(Restrictions.ne("codTipologia", "Y"))
	
			.add(Restrictions.eq("ep.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
			
			if(filtrarTipologiaExcluida) criteria.add(Restrictions.eq("indExcluido", false));
			if(filtrarTipologiaGrupoExcluida) criteria.add(Restrictions.eq("tg.indExcluido", false));
		
		return tipologiaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> buscarTodasTipologiasDoEmpreendimento(Empreendimento empreendimento)  {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tipologia.class)
				.createAlias("tipologiaGrupo", "tg",JoinType.LEFT_OUTER_JOIN)
				.createAlias("tg.unidadeMedidaTipologiaGrupo", "utg",JoinType.LEFT_OUTER_JOIN)
				.createAlias("utg.ideUnidadeMedida", "un",JoinType.LEFT_OUTER_JOIN)
				.createAlias("tg.empreendimentoTipologiaCollection", "et")
				.createAlias("et.empreendimento", "ep");

		detachedCriteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipologia"),"ideTipologia")
				.add(Projections.property("codTipologia"),"codTipologia")
				.add(Projections.property("desTipologia"),"desTipologia")

				.add(Projections.property("tg.ideTipologiaGrupo"),"tipologiaGrupo.ideTipologiaGrupo")

				.add(Projections.property("utg.ideUnidadeMedidaTipologiaGrupo"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo")

				.add(Projections.property("un.ideUnidadeMedida"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedida.ideUnidadeMedida")
				.add(Projections.property("un.codUnidadeMedida"),"tipologiaGrupo.unidadeMedidaTipologiaGrupo.ideUnidadeMedida.codUnidadeMedida")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class));

		detachedCriteria.add(Restrictions.eq("ep.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		detachedCriteria.add(Restrictions.eq("indExcluido", false));
		detachedCriteria.add(Restrictions.eq("tg.indExcluido", false));

		return 	tipologiaDAO.listarPorCriteria(detachedCriteria);
	}


	/**
	 *@author eduardo.fernandes
	 * Método de busca para retornar um EmpreendimentoRequerimento
	 * @param requerimento
	 * @param empreendimento
	 * @return EmpreendimentoRequerimento
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpreendimentoRequerimento buscarEmpreendimentoRequerimento(Requerimento requerimento, Empreendimento empreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class);
		criteria.setFetchMode("ideRequerimento", FetchMode.JOIN);
		criteria.setFetchMode("ideEmpreendimento", FetchMode.JOIN);
		criteria.setFetchMode("ideFaseEmpreendimento", FetchMode.JOIN);
		criteria.setFetchMode("idePorte", FetchMode.JOIN);
		criteria.setFetchMode("programaGoverno", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("ideRequerimento", requerimento));
		criteria.add(Restrictions.eq("ideEmpreendimento", empreendimento));
		return empreendimentoRequerimentoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpreendimentoRequerimento listarEmpreendimentoRequerimento(Requerimento requerimento, Empreendimento empreendimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class);
		
		criteria
			
			.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("ideEmpreendimento", "emp", JoinType.INNER_JOIN)
			.createAlias("ideFaseEmpreendimento", "faseEmp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePorte", "porte", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideClasse", "classe", JoinType.LEFT_OUTER_JOIN)
			.createAlias("programaGoverno", "progGov", JoinType.LEFT_OUTER_JOIN)
					
			.add(Restrictions.eq("ideRequerimento", requerimento))
			.add(Restrictions.eq("ideEmpreendimento", empreendimento))
			
			
			.setProjection(Projections.projectionList()
					.add(Projections.property("ideEmpreendimentoRequerimento"),"ideEmpreendimentoRequerimento")
					.add(Projections.property("dtcFaseEmpreendimento"),"dtcFaseEmpreendimento")
					.add(Projections.property("indDla"),"indDla")
					.add(Projections.property("numProcessoAna"),"numProcessoAna")
					.add(Projections.property("numPortariaAna"),"numPortariaAna")
					.add(Projections.property("numVazaoTotal"),"numVazaoTotal")
					.add(Projections.property("req.ideRequerimento"),"ideRequerimento.ideRequerimento")
					.add(Projections.property("emp.ideEmpreendimento"),"ideEmpreendimento.ideEmpreendimento")
					.add(Projections.property("faseEmp.ideFaseEmpreendimento"),"ideFaseEmpreendimento.ideFaseEmpreendimento")
					.add(Projections.property("porte.idePorte"),"idePorte.idePorte")
					.add(Projections.property("classe.ideClasse"),"ideClasse.ideClasse")
					.add(Projections.property("progGov.ideProgramaGoverno"),"programaGoverno.ideProgramaGoverno")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EmpreendimentoRequerimento.class))
		;
		
		List<EmpreendimentoRequerimento> list = empreendimentoRequerimentoDAO.listarPorCriteria(criteria);
		
		if(!Util.isLazyInitExcepOuNull(list) && !Util.isNullOuVazio(list)) {
			return list.get(0);
		}
		
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRural> imovelSemPermissaoASV(Empreendimento empreendimento)  {
		return this.imovelRuralService.imovelSemPermissaoASV(empreendimento);
	}

	/**
	 * Salvar somente os dados do empreendimento.
	 * @param pEmpreendimento
<<<<<<< HEAD
=======
<<<<<<< HEAD
	 * @
=======
>>>>>>> Correção ERRO
	 * 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDadosEmpreendimento(Empreendimento pEmpreendimento)  {
		daoEmpreendimento.salvarOuAtualizar(pEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpreendimentoRequerimento buscarEmpreendimentoRequerimento(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class)
				.createAlias("ideRequerimento", "requerimento", JoinType.INNER_JOIN)
				.createAlias("ideEmpreendimento", "empreendimento", JoinType.INNER_JOIN)
				.createAlias("ideClasse", "classe",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideFaseEmpreendimento", "faseEmpreendimento",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePorte", "porte",JoinType.LEFT_OUTER_JOIN);
				//.createAlias("empreendimento.empreendimentoTipologiaCollection", "empreendimentoTipologiaCollection",JoinType.LEFT_OUTER_JOIN)
				//.createAlias("empreendimentoTipologiaCollection.tipologiaGrupo", "tipologiaGrupo",JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq("requerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		return empreendimentoRequerimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpreendimentoRequerimento buscarEmpreendimentoRequerimentoComTipologia(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class)
				.createAlias("ideRequerimento", "requerimento", JoinType.INNER_JOIN)
				.createAlias("ideEmpreendimento", "empreendimento", JoinType.INNER_JOIN)
				.createAlias("ideClasse", "classe",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideFaseEmpreendimento", "faseEmpreendimento",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePorte", "porte",JoinType.LEFT_OUTER_JOIN)
				.createAlias("empreendimento.empreendimentoTipologiaCollection", "empreendimentoTipologiaCollection",JoinType.LEFT_OUTER_JOIN);
				//.createAlias("empreendimentoTipologiaCollection.tipologiaGrupo", "tipologiaGrupo",JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq("requerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		EmpreendimentoRequerimento empreendimentoRequerimento = empreendimentoRequerimentoDAO.buscarPorCriteria(criteria);
		
		empreendimentoRequerimento.getIdeEmpreendimento().setTipologias(buscarTodasTipologiasDoEmpreendimento(empreendimentoRequerimento.getIdeEmpreendimento()));
		
		return empreendimentoRequerimento;
	}

	/**
	 * 
	 * Método criado para carregar o {@link Empreendimento} com sua {@link LocalizacaoGeografica} e sua {@link ClassificacaoSecaoGeometrica} para ser utilizado no FCE - Turismo;
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 04/12/2014
	 * @param requerimento
	 * @see FceTurismoController
	 * @return {@link Empreendimento}
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarEmpreendimentoComLocGeoByRequerimento(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class)
				.createAlias("ideRequerimento", "r")
				.createAlias("ideEmpreendimento", "e")
				.createAlias("e.ideLocalizacaoGeografica", "loc")
				.createAlias("loc.ideClassificacaoSecao", "clas")
				.createAlias("loc.ideSistemaCoordenada", "sis")
				.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("e.ideEmpreendimento"), "ideEmpreendimento")
				.add(Projections.property("loc.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("clas.ideClassificacaoSecao"), "ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")
				.add(Projections.property("sis.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Empreendimento.class));

		Empreendimento empreendimento = daoEmpreendimento.buscarPorCriteria(criteria);
		empreendimento.setImovelCollection(imovelService.filtrarListaImovelPorEmpreendimento(empreendimento));
		return empreendimento;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Imovel> buscarVinculoImovelEmpreendimento(Empreendimento empreendimento) {
	    try {
			String jpql = "SELECT i FROM Imovel i " +
				"JOIN i.imovelEmpreendimentoCollection ie " +
				"JOIN ie.ideEmpreendimento e " +
				"LEFT JOIN i.imovelRural ir " +
				"WHERE e.ideEmpreendimento = :ideEmpreendimento " +
				"AND ir IS NOT NULL";
			
				
			EntityManager entityManager = DAOFactory.getEntityManager();
			
			TypedQuery<Imovel> query = entityManager.createQuery(jpql, Imovel.class);
			query.setParameter("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
			List<Imovel> listaImoveis = query.getResultList();

	        return listaImoveis != null ? listaImoveis : new ArrayList<Imovel>();

	    } catch (NullPointerException ne) {
	        return new ArrayList<Imovel>();
	    } catch (Exception e) {
	        JsfUtil.addErrorMessage("Erro ao pesquisar imóveis vinculados.");
	        Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
	        throw Util.capturarException(e, Util.SEIA_EXCEPTION);
	    }
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscarEmpreendimentoPorIde(Integer ideEmpreendimento){
		return daoEmpreendimento.carregarGet(ideEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmpreendimentoRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento){
		empreendimentoRequerimentoDAO.salvarOuAtualizar(empreendimentoRequerimento);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Empreendimento> listarEmpreendimentoBy(Pessoa pessoa, String nomEmpreendimento) {
		return empreendimentoDAOImpl.listarEmpreendimentoBy(pessoa, nomEmpreendimento);
	}
}