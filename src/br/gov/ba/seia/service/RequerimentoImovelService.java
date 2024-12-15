package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ObjetivoLimpezaArea;
import br.gov.ba.seia.entity.ObjetivoRequerimentoLimpezaArea;
import br.gov.ba.seia.entity.Pergunta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoImovel;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.util.Util;

/**
 * @author micael.coutinho
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RequerimentoImovelService {

	@Inject
	private IDAO<RequerimentoImovel> requerimentoImovelDAO;
	
	@Inject
	private IDAO<LocalizacaoGeografica> locGeoDAO;
	
	@Inject
	private IDAO<ObjetivoRequerimentoLimpezaArea> objetivoRequerimentoLimpezaAreaDAO;
	
	@Inject
	private LocalizacaoGeograficaService locGeoService;
	
	/**
	 * @author micael.coutinho
	 * Exclui a localização do requerimento, e em seguida, exclui o requerimento imovel da lista da pergunta de acordo com o index informado. 
	 * @param pergunta
	 * @param index
	 * @
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirReqImovelDaPerguntaDoRequerimento(Pergunta pergunta, Requerimento requerimento) {
		List<Object[]> listaObj = null;
		boolean tinhaLimpezaDeArea = false;
		if(pergunta.getCodPergunta().equals(PerguntaEnum.PERGUNTA_PRODUCAO_VOLUMETRICA_DE_MADEIRA.getCod()) && pergunta.getResposta())
			tinhaLimpezaDeArea = true;
		
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT r.ide_requerimento, ri.ide_requerimento_imovel, lg.ide_localizacao_geografica, pr.ide_pergunta_requerimento");
		if(tinhaLimpezaDeArea)
			lSql.append(", orla.ide_objetivo_requerimento_limpeza_area, orla.ide_localizacao_geografica");
		lSql.append(" FROM requerimento_imovel ri ");
		lSql.append("inner join requerimento r on r.ide_requerimento = ri.ide_requerimento ");
		lSql.append("inner join pergunta_requerimento pr on pr.ide_requerimento = ri.ide_requerimento and ri.ide_localizacao_geografica = pr.ide_localizacao_geografica ");
		if(pergunta.getCodPergunta().equals(PerguntaEnum.PERGUNTA_PRODUCAO_VOLUMETRICA_DE_MADEIRA.getCod()))
			lSql.append("and pr.ind_resposta = "+!pergunta.getResposta()+" ");
		lSql.append("inner join localizacao_geografica lg on lg.ide_localizacao_geografica = ri.ide_localizacao_geografica ");
		if(tinhaLimpezaDeArea)
			lSql.append("inner join objetivo_requerimento_limpeza_area orla on orla.ide_requerimento = ri.ide_requerimento ");
		lSql.append("inner join pergunta p on p.ide_pergunta = pr.ide_pergunta and p.ide_pergunta = "+pergunta.getIdePergunta().toString());
		lSql.append(" where r.ide_requerimento = "+requerimento.getIdeRequerimento().toString()+" and ri.ind_excluido = false;");
		
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());
		
		listaObj  = lQuery.getResultList();
		for (Iterator<Object[]> iterator = listaObj.iterator(); iterator.hasNext();) {
			Object[] integer = iterator.next();
			
			Integer ideReqImovel = (Integer) integer[1];
			Integer idelocGeo    = (Integer) integer[2];
			Integer idePergReq   = (Integer) integer[3];
			
			
			Map<String,Object> params = new HashMap<String, Object>();
			String delSQL = "";
			if(tinhaLimpezaDeArea){
				Integer ideObjReqLimpArea = (Integer) integer[4];
				Integer ideLocGeoObjReqLimpArea = (Integer) integer[5];
				
				delSQL = "UPDATE localizacao_geografica set ind_excluido = true, dtc_exclusao = now() where ide_localizacao_geografica = :ideLocalizacaoGeografica";
				params = new HashMap<String, Object>();
				params.put("ideLocalizacaoGeografica", ideLocGeoObjReqLimpArea);
				locGeoDAO.executarNativeQuery(delSQL, params);
				
				delSQL = "DELETE FROM objetivo_requerimento_limpeza_area where ide_objetivo_requerimento_limpeza_area = :ideObjReqLimpezaArea";
				params = new HashMap<String, Object>();
				params.put("ideObjReqLimpezaArea", ideObjReqLimpArea);
				locGeoDAO.executarNativeQuery(delSQL, params);
				
				delSQL = "UPDATE pergunta_requerimento set ind_excluido = true where ide_pergunta = :idePergunta and ide_requerimento = :ideRequerimento and ind_excluido = false and ind_resposta = :respostaContraria";
				params = new HashMap<String, Object>();
				params.put("idePergunta", pergunta.getIdePergunta().intValue());
				params.put("ideRequerimento", requerimento.getIdeRequerimento().intValue());
				params.put("respostaContraria", !pergunta.getResposta());
				locGeoDAO.executarNativeQuery(delSQL, params);
			}
			
			delSQL = "UPDATE localizacao_geografica set ind_excluido = true, dtc_exclusao = now() where ide_localizacao_geografica = :ideLocalizacaoGeografica";
			params = new HashMap<String, Object>();
			params.put("ideLocalizacaoGeografica", idelocGeo);
			locGeoDAO.executarNativeQuery(delSQL, params);
			
			if(!tinhaLimpezaDeArea){
				delSQL = "UPDATE pergunta_requerimento set ind_excluido = true where ide_pergunta_requerimento = :idePerguntaRequerimento";
				params = new HashMap<String, Object>();
				params.put("idePerguntaRequerimento", idePergReq);
				locGeoDAO.executarNativeQuery(delSQL, params);
			}
			
			delSQL = "UPDATE requerimento_imovel set ind_excluido = true, dtc_exclusao = now() where ide_requerimento_imovel = :ideRequerimentoImovel";
			params = new HashMap<String, Object>();
			params.put("ideRequerimentoImovel", ideReqImovel);
			locGeoDAO.executarNativeQuery(delSQL, params);
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<RequerimentoImovel> listarReqImoveisDaPergunta(PerguntaRequerimento pergReq) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoImovel.class);
		criteria.createAlias("imovel", "imovel");
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("objetivoRequerimentoLimpezaAreaCollection", "objetivosLimpArea", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("requerimento", "requerimento");
		criteria.createAlias("requerimento.perguntaRequerimentoCollection", "perguntaReq");
		criteria.createAlias("perguntaReq.idePergunta", "pergunta");
		criteria.add(Restrictions.eq("pergunta.idePergunta", pergReq.getIdePergunta().getIdePergunta()));
		criteria.add(Restrictions.eq("perguntaReq.indResposta", pergReq.getIndResposta()));
		criteria.add(Restrictions.eqProperty("perguntaReq.ideLocalizacaoGeografica.ideLocalizacaoGeografica", "ideLocalizacaoGeografica.ideLocalizacaoGeografica"));
		criteria.add(Restrictions.eq("requerimento.ideRequerimento", pergReq.getIdeRequerimento().getIdeRequerimento()));
		criteria.add(Restrictions.eq("indExcluido", false));
		
		List<RequerimentoImovel> listaReqImoveis = null;
		listaReqImoveis = requerimentoImovelDAO.listarPorCriteria(criteria);
		
		for (RequerimentoImovel requerimentoImovel : listaReqImoveis) {
			requerimentoImovel.setIdeLocalizacaoGeografica(locGeoService.carregar(requerimentoImovel.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
		}
		
		for (RequerimentoImovel requerimentoImovel : listaReqImoveis) {
			requerimentoImovel.setLocalizacaoGeograficaNaLista(new ArrayList<LocalizacaoGeografica>());
			requerimentoImovel.getLocalizacaoGeograficaNaLista().add(requerimentoImovel.getIdeLocalizacaoGeografica());
		}
		if (Util.isNullOuVazio(listaReqImoveis)) {
			listaReqImoveis = new ArrayList<RequerimentoImovel>();
		}
		return listaReqImoveis;
	}
	
	/**
	 * Retorna as lista de requerimento imovel  não excluídos de acordo com os parametros. retorna lista nula caso a consulta não  seja vazia.
	 * ESSA FUNÇÃO É UTILIZADA PARA TESTAR SE JÁ EXISTE ALGUM REQ_IMOVEL COM A PERGUNTA E IMOVEL INFORMADOS. 
	 * @param perg
	 * @param imovel
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<RequerimentoImovel> getReqImovelDaPerguntaAndImovel(Pergunta perg, Imovel imovel, Requerimento req) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoImovel.class);
		criteria.createAlias("imovel", "imovel");
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("objetivoRequerimentoLimpezaAreaCollection", "objetivosLimpArea", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("requerimento", "requerimento");
		criteria.createAlias("requerimento.perguntaRequerimentoCollection", "perguntaReq");
		criteria.createAlias("perguntaReq.idePergunta", "pergunta");
		criteria.add(Restrictions.eq("requerimento.ideRequerimento", req.getIdeRequerimento()));
		criteria.add(Restrictions.eq("pergunta.idePergunta", perg.getIdePergunta()));
		criteria.add(Restrictions.eq("perguntaReq.indResposta", perg.getResposta()));
		criteria.add(Restrictions.eq("perguntaReq.indExcluido", false));
		criteria.add(Restrictions.eq("perguntaReq.ideImovel.ideImovel", imovel.getIdeImovel()));
		criteria.add(Restrictions.eq("imovel.ideImovel", imovel.getIdeImovel()));
		criteria.add(Restrictions.eq("indExcluido", false));
		
		List<RequerimentoImovel> listaReqImoveis = null;
		listaReqImoveis = requerimentoImovelDAO.listarPorCriteria(criteria);
		
		return listaReqImoveis;
	}
	
	/**
	 * Retorna as lista de requerimento imovel  não excluídos de acordo com os parametros. retorna lista nula caso a consulta não  seja vazia.
	 * ESSA FUNÇÃO É UTILIZADA PARA TESTAR SE JÁ EXISTE ALGUM REQ_IMOVEL COM A PERGUNTA E IMOVEL INFORMADOS. 
	 * @param perg
	 * @param imovel
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<RequerimentoImovel> getObjReqLimpAreaDaPerguntaByImovelByObj(Pergunta perg, Imovel imovel, ObjetivoLimpezaArea obj, Requerimento req) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoImovel.class);
		criteria.createAlias("imovel", "imovel");
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("objetivoRequerimentoLimpezaAreaCollection", "objetReqLimpArea");
		criteria.createAlias("objetReqLimpArea.ideObjetivoLimpezaArea", "objLimpArea");
		criteria.createAlias("requerimento", "requerimento");
		criteria.createAlias("requerimento.perguntaRequerimentoCollection", "perguntaReq");
		criteria.createAlias("perguntaReq.idePergunta", "pergunta");
		criteria.add(Restrictions.eq("requerimento.ideRequerimento", req.getIdeRequerimento()));
		criteria.add(Restrictions.eq("pergunta.idePergunta", perg.getIdePergunta()));
		criteria.add(Restrictions.eq("perguntaReq.indResposta", perg.getResposta()));
		criteria.add(Restrictions.eq("perguntaReq.ideImovel.ideImovel", imovel.getIdeImovel()));
		criteria.add(Restrictions.eq("imovel.ideImovel", imovel.getIdeImovel()));
		criteria.add(Restrictions.eq("objLimpArea.ideObjetivoLimpezaArea", obj.getIdeObjetivoLimpezaArea()));
		criteria.add(Restrictions.eq("indExcluido", false));
		
		List<RequerimentoImovel> listaReqImoveis = null;
		listaReqImoveis = requerimentoImovelDAO.listarPorCriteria(criteria);
		
		return listaReqImoveis;
	}
	
	/**
	 * @author micael.coutinho
	 * Exclui a localização Geografica do requerimento e, em seguida, o requerimento informado.
	 * @param reqImovel
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirReqImovel(RequerimentoImovel reqImovel) {
		if(!Util.isNullOuVazio(reqImovel.getIdeLocalizacaoGeografica()) && !Util.isNullOuVazio(reqImovel.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()))
			locGeoService.excluirDadosPersistidos(reqImovel.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		int l = 0;
		if(!Util.isNullOuVazio(reqImovel.getIdeLocalizacaoGeografica()) && !Util.isNullOuVazio(reqImovel.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()))
			l= reqImovel.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica();
		
		try {
			if(!Util.isNullOuVazio(reqImovel.getObjetivoRequerimentoLimpezaAreaCollection()) && reqImovel.getObjetivoRequerimentoLimpezaAreaCollection().size() >= 1){
				for (ObjetivoRequerimentoLimpezaArea objReqImovLimpArea : reqImovel.getObjetivoRequerimentoLimpezaAreaCollection()) {
					if(objReqImovLimpArea.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() == l){
						parametros.put("ideObjetivoRequerimentoLimpezaArea", objReqImovLimpArea.getIdeObjetivoRequerimentoLimpezaArea());
						objetivoRequerimentoLimpezaAreaDAO.executarNamedQuery("ObjetivoRequerimentoLimpezaArea.deleteByIde", parametros);
						objReqImovLimpArea.setIdeLocalizacaoGeografica(null);
						objReqImovLimpArea.setIdeObjetivoRequerimentoLimpezaArea(null);
					}
				}
			}else{
				parametros.put("ideLocalizacaoGeograficaAdeletar", l);
				parametros.put("ideLocalizacaoGeografica", null);
				objetivoRequerimentoLimpezaAreaDAO.executarNamedQuery("ObjetivoRequerimentoLimpezaArea.updateLocGeo", parametros);
			}
		} catch (Exception e) {
			DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoImovel.class, "requerimentoImovel");
			criteria.createAlias("objetivoRequerimentoLimpezaAreaCollection", "objtsReqLimpArea", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("objtsReqLimpArea.ideLocalizacaoGeografica", "localizacaoGeograficaObjReqLimpArea", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.eq("ideRequerimentoImovel", reqImovel.getIdeRequerimentoImovel()));
			reqImovel = requerimentoImovelDAO.buscarPorCriteria(criteria);
			if(!Util.isNullOuVazio(reqImovel.getObjetivoRequerimentoLimpezaAreaCollection()) && reqImovel.getObjetivoRequerimentoLimpezaAreaCollection().size() >= 1){
				for (ObjetivoRequerimentoLimpezaArea objReqImovLimpArea : reqImovel.getObjetivoRequerimentoLimpezaAreaCollection()) {
					if(objReqImovLimpArea.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() == l){
						parametros.put("ideObjetivoRequerimentoLimpezaArea", objReqImovLimpArea.getIdeObjetivoRequerimentoLimpezaArea());
						objetivoRequerimentoLimpezaAreaDAO.executarNamedQuery("ObjetivoRequerimentoLimpezaArea.deleteByIde", parametros);
						objReqImovLimpArea.setIdeLocalizacaoGeografica(null);
						objReqImovLimpArea.setIdeObjetivoRequerimentoLimpezaArea(null);
					}
				}
			}else{
				parametros.put("ideLocalizacaoGeograficaAdeletar", l);
				parametros.put("ideLocalizacaoGeografica", null);
				objetivoRequerimentoLimpezaAreaDAO.executarNamedQuery("ObjetivoRequerimentoLimpezaArea.updateLocGeo", parametros);
			}
		}
		
		parametros = new HashMap<String, Object>();
		parametros.put("ideLocalizacaoGeografica", reqImovel.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		List<RequerimentoImovel> lReqImov = requerimentoImovelDAO.buscarPorNamedQuery("RequerimentoImovel.findByIdeLocalizacaoGeografica", parametros);
		RequerimentoImovel r = new RequerimentoImovel();
		if(!Util.isNullOuVazio(lReqImov))
			r = lReqImov.iterator().next();
				
		parametros = new HashMap<String, Object>();
		parametros.put("ideLocalizacaoGeografica", reqImovel.getIdeLocalizacaoGeografica());
		locGeoDAO.executarNamedQuery("LocalizacaoGeografica.atualizarLocGeoReqImovel", parametros);
		
		locGeoDAO.executarNamedQuery("LocalizacaoGeografica.deletarPerguntaReqImovel", parametros);
		
		parametros = new HashMap<String, Object>();
		parametros.put("ideLocalizacaoGeografica", l);
		if(!Util.isNullOuVazio(reqImovel.getRequerimento()) && !Util.isLazyInitExcepOuNull(reqImovel.getRequerimento().getRequerimentoUnico()) 
			&& !Util.isNullOuVazio(reqImovel.getRequerimento().getRequerimentoUnico().getIdeLocalizacaoGeografica()) 
			&&  reqImovel.getRequerimento().getRequerimentoUnico().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().equals(l))
		{
			locGeoDAO.executarNamedQuery("LocalizacaoGeografica.updateReqUnicoByIdeLocGeo", parametros);
		}
		objetivoRequerimentoLimpezaAreaDAO.executarNamedQuery("ObjetivoRequerimentoLimpezaArea.deleteByIdeLoc", parametros);
		locGeoDAO.executarNamedQuery("LocalizacaoGeografica.deleteByIde", parametros);
		
		parametros = new HashMap<String, Object>();
		parametros.put("ideRequerimentoImovel", r.getIdeRequerimentoImovel());
		requerimentoImovelDAO.executarNamedQuery("RequerimentoImovel.deleteByIde", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequerimentoImovel obterUltimoRequerimentoImovel(Imovel imovel)  {
	    DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoImovel.class);
		criteria.createAlias("imovel", "imovel");
		criteria.createAlias("requerimento", "requerimento");
		criteria.createAlias("ideTipoImovelRequerimento", "ideTipoImovelRequerimento");
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("objetivoRequerimentoLimpezaAreaCollection", "objetivosLimpArea", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("imovel.ideImovel", imovel.getIdeImovel()));
		
		criteria.addOrder(Order.desc("ideRequerimentoImovel"));
		List<RequerimentoImovel> lListRequerimento = requerimentoImovelDAO.listarPorCriteria(criteria);
		
		if(!Util.isNullOuVazio(lListRequerimento)) {
		    return lListRequerimento.get(0);
		}
		return null;
	}
	
}