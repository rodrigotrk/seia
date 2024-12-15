package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PerguntaRequerimentoService {

	@Inject
	IDAO<PerguntaRequerimento> perguntaRequerimentoDAO;

	@EJB
	PerguntaService perguntaService;
	@EJB
	AtoAmbientalService atoAmbientalService;
	/**************
	/*			  *
	//XXX: SALVAR *
	/* 			  *
	/**************/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarPerguntaReq(PerguntaRequerimento perguntaRequerimento) {
		perguntaRequerimentoDAO.salvarOuAtualizar(perguntaRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarPerguntaReq(PerguntaRequerimento perguntaRequerimento) {
		perguntaRequerimentoDAO.atualizar(perguntaRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPerguntaReq(PerguntaRequerimento perguntaRequerimento) {
		perguntaRequerimentoDAO.remover(perguntaRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPerguntaReq(PerguntaRequerimento perguntaRequerimento, OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		Integer ideOutorgaLocalizacaoGeografica = Util.isNull(outorgaLocalizacaoGeografica) ? null : outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica();
		PerguntaRequerimento pr = consultarPerguntaRequerimentoByIdePerguntaAndIdeRequerimento(perguntaRequerimento.getIdePergunta().getIdePergunta(), perguntaRequerimento.getIdeRequerimento().getIdeRequerimento(), perguntaRequerimento.getIdeImovel() != null ? perguntaRequerimento.getIdeImovel().getIdeImovel() : null,null, ideOutorgaLocalizacaoGeografica, perguntaRequerimento.getIdeLicenca() != null ?  new Licenca[]{perguntaRequerimento.getIdeLicenca()} : new Licenca[]{}); 
		
		if(pr != null) {
			pr.setDtcResposta(perguntaRequerimento.getDtcResposta());
			pr.setIndResposta(perguntaRequerimento.getIndResposta());
			pr.setIdeLocalizacaoGeografica(perguntaRequerimento.getIdeLocalizacaoGeografica());
			perguntaRequerimentoDAO.salvarOuAtualizar(pr);
		}
		else {
			perguntaRequerimentoDAO.salvar(perguntaRequerimento);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPerguntaReqFlorestal(PerguntaRequerimento perguntaRequerimento) {
		PerguntaRequerimento pr = consultarPerguntaRequerimentoByIdePerguntaAndIdeRequerimento(perguntaRequerimento.getIdePergunta().getIdePergunta(), perguntaRequerimento.getIdeRequerimento().getIdeRequerimento(), perguntaRequerimento.getIdeImovel() != null ? perguntaRequerimento.getIdeImovel().getIdeImovel() : null); 
		
		if(pr != null) {
			pr.setDtcResposta(perguntaRequerimento.getDtcResposta());
			pr.setIndResposta(perguntaRequerimento.getIndResposta());
			pr.setIdeLocalizacaoGeografica(perguntaRequerimento.getIdeLocalizacaoGeografica());
			perguntaRequerimentoDAO.atualizar(pr);
		}
		else {
			perguntaRequerimentoDAO.salvarOuAtualizar(perguntaRequerimento);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPerguntaReqByIdLocalizacaoGeografica(LocalizacaoGeografica locGeo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideLocalizacaoGeografica", locGeo);
		perguntaRequerimentoDAO.executarNamedQuery("PerguntaRequerimento.excluirByIdLocalizacaoGeografica",params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPerguntaReqByIdLicenca(Licenca ideLicenca) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideLicenca", ideLicenca);
		perguntaRequerimentoDAO.executarNamedQuery("PerguntaRequerimento.excluirByIdLicenca",params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPerguntaReqByIdeOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideOutorgaLocalizacaoGeografica", outorgaLocalizacaoGeografica);
		perguntaRequerimentoDAO.executarNamedQuery("PerguntaRequerimento.excluirByIdeOutorgaLocGeo",params);
	}
	
	public void salvaListPerguntaRequerimento(List<PerguntaRequerimento> listPerguntaRequerimentoAba, Requerimento requerimento)  {
		this.salvaListPerguntaRequerimento(listPerguntaRequerimentoAba, requerimento, null, null, null);
	}
	
	/**
	 * Percorre uma lista de Pergunta Requerimento e salva um por um.
	 * @param listPerguntaRequerimentoAba
	 * @param requerimento
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvaListPerguntaRequerimento(List<PerguntaRequerimento> listPerguntaRequerimentoAba, Requerimento requerimento, Imovel imovel, LocalizacaoGeografica lg, OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica)  {
		
		PerguntaRequerimento pr;
		
		for (PerguntaRequerimento perguntaRequerimento : listPerguntaRequerimentoAba) {
			
			if(perguntaRequerimento.getIndResposta() != null){
				perguntaRequerimento.setDtcResposta(new Date());
				perguntaRequerimento.setIdeRequerimento(requerimento);
				perguntaRequerimento.setIdeImovel(imovel != null ? imovel : null);
				
				if(!Util.isNullOuVazio(lg)){
					perguntaRequerimento.setIdeLocalizacaoGeografica(lg);
				}
				
				salvarPerguntaReq(perguntaRequerimento, outorgaLocalizacaoGeografica);
			} else {
				Integer ideOutorgaLocalizacaoGeografica = Util.isNull(outorgaLocalizacaoGeografica) ? null : outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica();
				pr = consultarPerguntaRequerimentoByIdePerguntaAndIdeRequerimento(perguntaRequerimento.getIdePergunta().getIdePergunta(), requerimento.getIdeRequerimento(), imovel != null ? imovel.getIdeImovel() : null, ideOutorgaLocalizacaoGeografica, lg != null ? lg.getIdeLocalizacaoGeografica() : null);
				
				if(pr != null){
					removerPerguntaReq(pr);
				}
			}
		}
	}
	
	
	
	/**
	 * Percorre uma lista de Pergunta Requerimento e salva um por um.
	 * Utilizado para salvar as perguntas do dialog Florestal.
	 * @param listPerguntaRequerimentoAba
	 * @param requerimento
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvaListPerguntaRequerimentoFlorestal(List<PerguntaRequerimento> listPerguntaRequerimentoAba, Requerimento requerimento, Imovel imovel)  {
		for (PerguntaRequerimento perguntaRequerimento : listPerguntaRequerimentoAba) {
			if(perguntaRequerimento.getIndResposta() != null){
				perguntaRequerimento.setDtcResposta(new Date());
				perguntaRequerimento.setIdeRequerimento(requerimento);
				perguntaRequerimento.setIdeImovel(imovel != null ? imovel : null);
				salvarPerguntaReqFlorestal(perguntaRequerimento);
			}else{
				Integer idePergunta = perguntaRequerimento.getIdePergunta().getIdePergunta();
				Integer ideRequerimento = requerimento.getIdeRequerimento();
				this.removerPerguntasRequerimento(idePergunta,ideRequerimento);
			}
		}
	}
	
	/**
	 * Carrega todas as perguntas requerimento que estiverem respondidas.
	 * @param list
	 * @param requerimento
	 * @return
	 * @
	 */
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PerguntaRequerimento> carregarListaPerguntaRequerimentoRespondida(List<PerguntaRequerimento> list, Requerimento requerimento, Imovel imovel, LocalizacaoGeografica lg, OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica, Licenca... ideLicenca)  {
		Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
		
		for(PerguntaRequerimento pr : list){
			PerguntaRequerimento prFound = consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimento(pr.getIdePergunta().getCodPergunta(), requerimento.getIdeRequerimento(), imovel != null ? imovel.getIdeImovel() : null, lg != null ? lg.getIdeLocalizacaoGeografica() : null, outorgaLocalizacaoGeografica != null ? outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica() : null, ideLicenca.length > 0 ?  new Licenca[]{ideLicenca[0]} : null);
			if(prFound != null && pr.getIdePergunta().getIdePergunta().equals(prFound.getIdePergunta().getIdePergunta())){
				map.put(list.indexOf(pr), prFound.getIndResposta());
				pr.setIdePerguntaRequerimento(prFound.getIdePerguntaRequerimento());
				pr.setIdeLocalizacaoGeografica(prFound.getIdeLocalizacaoGeografica());
			}
		}

		for(PerguntaRequerimento pr : list){
			if(map.containsKey(list.indexOf(pr))){
				pr.setIndResposta(map.get(list.indexOf(pr)));
			}
		}
		
		return list;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PerguntaRequerimento carregarPerguntaByIde(Integer idePerguntaRequerimento) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idePergunta", idePerguntaRequerimento);
		return perguntaRequerimentoDAO.obterPorNamedQuery("PerguntaRequerimento.findByIdePerguntaRequerimento", param);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PerguntaRequerimento> carregarPerguntaReqDoRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		criteria.createAlias("idePergunta", "pergunta");
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica");
		criteria.createAlias("localizacaoGeografica.dadoGeograficoCollection", "dadoGeografico");
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		criteria.add(Restrictions.eq("indExcluido", !true));
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		List<PerguntaRequerimento> listPergReq= perguntaRequerimentoDAO.listarPorCriteria(criteria);
		Set<PerguntaRequerimento> setPergReq = new HashSet<PerguntaRequerimento>();
		setPergReq.addAll(listPergReq);
		listPergReq.clear();
		listPergReq.addAll(setPergReq);
		return listPergReq;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PerguntaRequerimento> listarPerguntaRequerimentoPor(Requerimento requerimento, List<String> listaCodPergunta) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		criteria
			.createAlias("idePergunta", "p", JoinType.INNER_JOIN)
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()))
			.add(Restrictions.in("p.codPergunta", listaCodPergunta))
			.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY)
		;
		return perguntaRequerimentoDAO.listarPorCriteria(criteria);
	}
	
	public PerguntaRequerimento consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimento(String codPergunta, int ideRequerimento,Licenca ideLicenca) {
		return consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimento(codPergunta, ideRequerimento,null,null,null,ideLicenca);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PerguntaRequerimento consultarPerguntaRequerimentoByIdePerguntaAndIdeRequerimento(int idePergunta, int ideRequerimento, Integer ideImovel, Integer ideLocGeo, Integer ideOutorgaLocGeo, Licenca... ideLicenca) {
		 DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		 criteria.createAlias("idePergunta", "p");
		 criteria.createAlias("ideRequerimento", "r");
		 criteria.add(Restrictions.isNull("indExcluido"));
		 criteria.add(Restrictions.eq("p.idePergunta", idePergunta));
		 criteria.add(Restrictions.eq("r.ideRequerimento", ideRequerimento));
		 if(ideImovel != null){
			 criteria.add(Restrictions.eq("ideImovel.ideImovel", ideImovel));
		 }
		 if(ideLocGeo != null){
			 criteria.add(Restrictions.eq("ideLocalizacaoGeografica.ideLocalizacaoGeografica", ideLocGeo));
		 }
		 if(ideOutorgaLocGeo != null){
			 criteria.add(Restrictions.eq("ideOutorgaLocalizacaoGeografica.ideOutorgaLocalizacaoGeografica", ideOutorgaLocGeo));
		 }
		 if(ideLicenca.length > 0){
			 criteria.add(Restrictions.eq("ideLicenca.ideLicenca", ideLicenca[0].getIdeLicenca())); 
		 }
		 return perguntaRequerimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PerguntaRequerimento consultarPerguntaRequerimentoByIdePerguntaAndIdeRequerimento(int idePergunta, int ideRequerimento, Integer ideImovel) {
		 DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		 criteria.createAlias("idePergunta", "p");
		 criteria.createAlias("ideRequerimento", "r");
		 criteria.add(Restrictions.eq("p.idePergunta", idePergunta));
		 criteria.add(Restrictions.eq("r.ideRequerimento", ideRequerimento));
		 if(ideImovel != null){
			 criteria.add(Restrictions.eq("ideImovel.ideImovel", ideImovel));
		 }
		 return perguntaRequerimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PerguntaRequerimento buscarPerguntaRequerimentoByIdePerguntaAndIdeLocalizacaoGeografica(int idePergunta, int ideLocalizacao) {
		 DetachedCriteria criteria = getCriteriaBuscaPergunta(ideLocalizacao);
		 criteria.add(Restrictions.eq("p.idePergunta", idePergunta));
		 return perguntaRequerimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PerguntaRequerimento> buscarPerguntaRequerimentoByIdeLocalizacaoGeografica(int ideLocalizacao) {
		 DetachedCriteria criteria = getCriteriaBuscaPergunta(ideLocalizacao);
		return perguntaRequerimentoDAO.listarPorCriteria(criteria);
	}

	private DetachedCriteria getCriteriaBuscaPergunta(int ideLocalizacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		 criteria.createAlias("idePergunta", "p");
		 criteria.createAlias("ideRequerimento", "r");
		 criteria.createAlias("ideLocalizacaoGeografica", "localizacao");
		 criteria.add(Restrictions.eq("localizacao.ideLocalizacaoGeografica", ideLocalizacao));
		return criteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PerguntaRequerimento consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimento(String codPergunta, Integer ideRequerimento, Integer ideImovel, Integer ideLocGeo,
			Integer ideOutorgaLocGeo, Licenca... ideLicenca)  {
	
	
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		
		criteria.createAlias("idePergunta", "p");
		criteria.createAlias("ideRequerimento", "r");
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("localizacaoGeografica.ideSistemaCoordenada", "sistemaCoordenada", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("localizacaoGeografica.ideClassificacaoSecao", "classificacaoSecao", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.isNull("indExcluido"));
		criteria.add(Restrictions.like("p.codPergunta", codPergunta, MatchMode.EXACT));
		criteria.add(Restrictions.eq("r.ideRequerimento", ideRequerimento));
		
		if (ideImovel != null) {
			criteria.add(Restrictions.eq("ideImovel.ideImovel", ideImovel));
		}
		
		if (ideLocGeo != null) {
			criteria.add(Restrictions.eq("ideLocalizacaoGeografica.ideLocalizacaoGeografica", ideLocGeo));
		}
		
		if (!Util.isNullOuVazio(ideLicenca)) {
			criteria.add(Restrictions.eq("ideLicenca.ideLicenca", ideLicenca[0].getIdeLicenca()));
		}
		
		if (ideOutorgaLocGeo != null) {
			criteria.add(Restrictions.eq("ideOutorgaLocalizacaoGeografica.ideOutorgaLocalizacaoGeografica", ideOutorgaLocGeo));
		}
		
		return perguntaRequerimentoDAO.buscarPorCriteria(criteria);
	}
	
	public Collection<PerguntaRequerimento> carregarByIdeProcesso(Integer ideProcesso)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PerguntaRequerimento.class)
				.createAlias("ideRequerimento", "req")
				.createAlias("idePergunta", "pergunta")
				.createAlias("ideLocalizacaoGeografica", "loc")
				.createAlias("req.processoCollection", "proc")
			.add(Restrictions.eq("proc.ideProcesso", ideProcesso))
			.add(Restrictions.isNull("indExcluido"));
		return perguntaRequerimentoDAO.listarPorCriteria(detachedCriteria);
	}

	private void removerPerguntasRequerimento(Integer idePergunta, Integer ideRequerimento)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idePergunta", idePergunta);
		parametros.put("ideRequerimento", ideRequerimento);
		this.perguntaRequerimentoDAO.executarNativeQuery("delete from pergunta_requerimento where ide_pergunta = :idePergunta and ide_requerimento = :ideRequerimento  ", parametros);
	}

	public void carregarListaPerguntaRequerimentoRespondida(Collection<PerguntaRequerimento> listaPerguntasRequerimento, Requerimento requerimento)  {
		this.carregarListaPerguntaRequerimentoRespondida((List<PerguntaRequerimento>)listaPerguntasRequerimento, requerimento, null, null, null);
	}
	
	public void carregarListaPerguntaRequerimentoRespondida(Collection<PerguntaRequerimento> listaPerguntasRequerimento, Requerimento requerimento,Imovel ideImovel)  {
		this.carregarListaPerguntaRequerimentoRespondida((List<PerguntaRequerimento>)listaPerguntasRequerimento, requerimento, ideImovel, null, null);
	}
	
	public void carregarListaPerguntaRequerimentoRespondida(Collection<PerguntaRequerimento> listaPerguntasRequerimento, Requerimento requerimento,Imovel ideImovel, LocalizacaoGeografica localizacaoGeografica)  {
		this.carregarListaPerguntaRequerimentoRespondida((List<PerguntaRequerimento>)listaPerguntasRequerimento, requerimento, ideImovel, localizacaoGeografica, null);
	}
	
	public void carregarListaPerguntaRequerimentoRespondida(Collection<PerguntaRequerimento> listaPerguntasRequerimento, Requerimento requerimento, OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica)  {
		this.carregarListaPerguntaRequerimentoRespondida((List<PerguntaRequerimento>)listaPerguntasRequerimento, requerimento, null, null, outorgaLocalizacaoGeografica);
	}
	
	public void carregarListaPerguntaRequerimentoRespondida(Collection<PerguntaRequerimento> listaPerguntasRequerimento, Requerimento requerimento,Licenca licenca)  {
		this.carregarListaPerguntaRequerimentoRespondida((List<PerguntaRequerimento>)listaPerguntasRequerimento, requerimento, null, null, null, licenca);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PerguntaRequerimento buscarPerguntaRequerimentoPorRequerimentoECodPergunta(Requerimento req, String codPergunta){
		 
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class)
					.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
					.createAlias("idePergunta", "p", JoinType.INNER_JOIN)
					.createAlias("ideImovel", "imov", JoinType.LEFT_OUTER_JOIN)
					
					.setProjection(
						Projections.projectionList()
							.add(Projections.property("idePerguntaRequerimento"), "idePerguntaRequerimento")
							.add(Projections.property("indResposta"), "indResposta")
							.add(Projections.property("dtcResposta"), "dtcResposta")
							.add(Projections.property("indExcluido"), "indExcluido")
							
							.add(Projections.property("r.ideRequerimento"), "ideRequerimento.ideRequerimento")
							.add(Projections.property("r.numRequerimento"), "ideRequerimento.numRequerimento")
							
							.add(Projections.property("p.idePergunta"), "idePergunta.idePergunta")
							.add(Projections.property("p.codPergunta"), "idePergunta.codPergunta")
							.add(Projections.property("p.dscPergunta"), "idePergunta.dscPergunta")
							
							.add(Projections.property("imov.ideImovel"), "ideImovel.ideImovel")
							
					).setResultTransformer(new AliasToNestedBeanResultTransformer(PerguntaRequerimento.class))
					
					.add(Restrictions.eq("r.ideRequerimento", req.getIdeRequerimento()))
					.add(Restrictions.like("p.codPergunta", codPergunta, MatchMode.EXACT));
			 
			 return perguntaRequerimentoDAO.buscarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PerguntaRequerimento> listarPerguntaRequerimentoPorRequerimentoECodPergunta(Requerimento req, String codPergunta){
		 
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class)
					.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
					.createAlias("idePergunta", "p", JoinType.INNER_JOIN)
					.createAlias("ideImovel", "imov", JoinType.LEFT_OUTER_JOIN)
					
					.setProjection(
						Projections.projectionList()
							.add(Projections.property("idePerguntaRequerimento"), "idePerguntaRequerimento")
							.add(Projections.property("indResposta"), "indResposta")
							.add(Projections.property("dtcResposta"), "dtcResposta")
							.add(Projections.property("indExcluido"), "indExcluido")
							
							.add(Projections.property("r.ideRequerimento"), "ideRequerimento.ideRequerimento")
							.add(Projections.property("r.numRequerimento"), "ideRequerimento.numRequerimento")
							
							.add(Projections.property("p.idePergunta"), "idePergunta.idePergunta")
							.add(Projections.property("p.codPergunta"), "idePergunta.codPergunta")
							.add(Projections.property("p.dscPergunta"), "idePergunta.dscPergunta")
							
							.add(Projections.property("imov.ideImovel"), "ideImovel.ideImovel")
							
					).setResultTransformer(new AliasToNestedBeanResultTransformer(PerguntaRequerimento.class))
					
					.add(Restrictions.eq("r.ideRequerimento", req.getIdeRequerimento()))
					.add(Restrictions.like("p.codPergunta", codPergunta, MatchMode.EXACT));
			 
			 return perguntaRequerimentoDAO.listarPorCriteria(criteria, Order.asc("p.codPergunta"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PerguntaRequerimento consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimentoAndIdeOutorgaLocGeo(String codPergunta, Integer ideRequerimento, Integer ideOutorgaLocGeo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		criteria.createAlias("idePergunta", "p");
		criteria.createAlias("ideRequerimento", "r");
		criteria.add(Restrictions.like("p.codPergunta", codPergunta, MatchMode.EXACT));
		criteria.add(Restrictions.eq("r.ideRequerimento", ideRequerimento));
		if(ideOutorgaLocGeo != null){
			 criteria.add(Restrictions.eq("ideOutorgaLocalizacaoGeografica.ideOutorgaLocalizacaoGeografica", ideOutorgaLocGeo));
		 }
		 return perguntaRequerimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PerguntaRequerimento> listarPerguntaRequerimentoRespondidaAbaFlorestalComLocalizacaoGeografica(Requerimento ideRequerimento)  {
		
		Collection<AtoAmbiental> atosAmbientais = ideRequerimento.getAtosAmbientais();
		Boolean isAtoFlorestal = false;
		
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = atosAmbientais.iterator(); iterator.hasNext();) {
			AtoAmbiental atoAmbiental = (AtoAmbiental) iterator.next();
			atoAmbiental = atoAmbientalService.carregarById(atoAmbiental.getIdeAtoAmbiental());
			if(atoAmbiental.getIdeTipoAto().getIdeTipoAto().equals(TipoAtoEnum.FLORESTAL.getId())){
				isAtoFlorestal = true;
			}
			
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		criteria
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("r.empreendimentoRequerimentoCollection", "er", JoinType.INNER_JOIN)
			.createAlias("er.ideEmpreendimento", "e", JoinType.INNER_JOIN)
			.createAlias("idePergunta", "p", JoinType.INNER_JOIN)
			.createAlias("ideImovel", "i", JoinType.INNER_JOIN);
			if(isAtoFlorestal){
				criteria.createAlias("ideLocalizacaoGeografica", "l", JoinType.INNER_JOIN);
			} else {
				criteria
				.createAlias("e.ideLocalizacaoGeografica", "l", JoinType.INNER_JOIN);
			}
			
		criteria
			.createAlias("l.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN)
			.createAlias("l.ideClassificacaoSecao", "csg", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("indResposta", true))
			.add(Restrictions.eq("r.ideRequerimento", ideRequerimento.getIdeRequerimento()))
			
			.setProjection(
				Projections.projectionList()
					.add(Projections.groupProperty("idePerguntaRequerimento"),"idePerguntaRequerimento")
					.add(Projections.groupProperty("indResposta"),"indResposta")
					.add(Projections.groupProperty("p.idePergunta"),"idePergunta.idePergunta")
					.add(Projections.groupProperty("p.codPergunta"),"idePergunta.codPergunta")
					.add(Projections.groupProperty("i.ideImovel"),"ideImovel.ideImovel")
					.add(Projections.groupProperty("l.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica.ideLocalizacaoGeografica")
					.add(Projections.groupProperty("l.dtcCriacao"),"ideLocalizacaoGeografica.dtcCriacao")
					.add(Projections.groupProperty("l.indExcluido"),"ideLocalizacaoGeografica.indExcluido")
					.add(Projections.groupProperty("l.dtcExclusao"),"ideLocalizacaoGeografica.dtcExclusao")
					.add(Projections.groupProperty("l.fonteCoordenada"),"ideLocalizacaoGeografica.fonteCoordenada")
					.add(Projections.groupProperty("l.desLocalizacaoGeografica"),"ideLocalizacaoGeografica.desLocalizacaoGeografica")
					.add(Projections.groupProperty("sc.ideSistemaCoordenada"),"ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
					.add(Projections.groupProperty("csg.ideClassificacaoSecao"),"ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(PerguntaRequerimento.class))
		;
		
		return perguntaRequerimentoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PerguntaRequerimento> listarPerguntaRequerimentoRespondidaAbaFlorestal(Integer ideRequerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		criteria
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("idePergunta", "p", JoinType.INNER_JOIN)
			.createAlias("ideImovel", "i", JoinType.INNER_JOIN)
			.createAlias("ideLocalizacaoGeografica", "l", JoinType.INNER_JOIN)
			.createAlias("l.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN)
			.createAlias("l.ideClassificacaoSecao", "csg", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("indResposta", true))
			.add(Restrictions.eq("r.ideRequerimento", ideRequerimento))
			
			.setProjection(
				Projections.projectionList()
					.add(Projections.groupProperty("idePerguntaRequerimento"),"idePerguntaRequerimento")
					.add(Projections.groupProperty("indResposta"),"indResposta")
					.add(Projections.groupProperty("p.idePergunta"),"idePergunta.idePergunta")
					.add(Projections.groupProperty("p.codPergunta"),"idePergunta.codPergunta")
					.add(Projections.groupProperty("i.ideImovel"),"ideImovel.ideImovel")
					.add(Projections.groupProperty("l.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica.ideLocalizacaoGeografica")
					.add(Projections.groupProperty("l.dtcCriacao"),"ideLocalizacaoGeografica.dtcCriacao")
					.add(Projections.groupProperty("l.indExcluido"),"ideLocalizacaoGeografica.indExcluido")
					.add(Projections.groupProperty("l.dtcExclusao"),"ideLocalizacaoGeografica.dtcExclusao")
					.add(Projections.groupProperty("l.fonteCoordenada"),"ideLocalizacaoGeografica.fonteCoordenada")
					.add(Projections.groupProperty("l.desLocalizacaoGeografica"),"ideLocalizacaoGeografica.desLocalizacaoGeografica")
					.add(Projections.groupProperty("sc.ideSistemaCoordenada"),"ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
					.add(Projections.groupProperty("csg.ideClassificacaoSecao"),"ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(PerguntaRequerimento.class))
		;
		
		return perguntaRequerimentoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PerguntaRequerimento> listarPerguntaRequerimentoRespondidaAbaFlorestal(Requerimento req)  {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class)
				.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
				.createAlias("idePergunta", "p", JoinType.INNER_JOIN)
				.createAlias("ideImovel", "i", JoinType.INNER_JOIN)
				
				.setProjection(Projections.projectionList()
					.add(Projections.groupProperty("idePerguntaRequerimento"),"idePerguntaRequerimento")
					.add(Projections.groupProperty("indResposta"),"indResposta")
					.add(Projections.groupProperty("r.ideRequerimento"),"ideRequerimento.ideRequerimento")
					.add(Projections.groupProperty("r.numRequerimento"),"ideRequerimento.numRequerimento")
					.add(Projections.groupProperty("p.idePergunta"),"idePergunta.idePergunta")
					.add(Projections.groupProperty("p.codPergunta"),"idePergunta.codPergunta")
					.add(Projections.groupProperty("i.ideImovel"),"ideImovel.ideImovel")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(PerguntaRequerimento.class))
				
				.add(Restrictions.eq("indResposta", true))
				.add(Restrictions.eq("r.ideRequerimento", req.getIdeRequerimento()));
			
			return perguntaRequerimentoDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		} 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PerguntaRequerimento consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimento(String codPergunta, Integer ideRequerimento) {
		 DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		 criteria.createAlias("idePergunta", "p");
		 criteria.createAlias("ideRequerimento", "r");
		 criteria.add(Restrictions.isNull("indExcluido"));
		 criteria.add(Restrictions.like("p.codPergunta", codPergunta, MatchMode.EXACT));
		 criteria.add(Restrictions.eq("r.ideRequerimento", ideRequerimento));
		 return perguntaRequerimentoDAO.buscarPorCriteria(criteria);
	}

	/**
	 * Método que lista todas as {@link PerguntaRequerimento} respondidas como "Sim" no requerimento. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 24/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param ideRequerimento
	 * @return
	 * @
	 */	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PerguntaRequerimento> listarPerguntasRespondidasByIdeRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class)
				.createAlias("idePergunta", "p")
				.createAlias("ideRequerimento", "r")
				.add(Restrictions.eq("indResposta", !true))
				.add(Restrictions.eq("r.ideRequerimento", ideRequerimento))
				.setProjection(Projections.projectionList()
						.add(Projections.property("indResposta"), "indResposta")
						.add(Projections.property("p.codPergunta"), "idePergunta.codPergunta")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(PerguntaRequerimento.class))
						;
		return perguntaRequerimentoDAO.listarPorCriteria(criteria);
	}

	public Collection<PerguntaRequerimento> listarPerguntaRequerimentoByCodPerguntaAndIdeRequerimento(String codPergunta, Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		criteria.createAlias("idePergunta", "p");
		criteria.createAlias("ideRequerimento", "r");
		criteria.add(Restrictions.like("p.codPergunta", codPergunta, MatchMode.EXACT));
		criteria.add(Restrictions.eq("r.ideRequerimento", ideRequerimento));
		return perguntaRequerimentoDAO.listarPorCriteria(criteria);
	}
}