package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceSaa;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaDadosConcedidos;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaElevatoriaBruta;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaElevatoriaTratada;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaEta;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaReservatorio;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class FceSaaDAOImpl {

	@Inject
	IDAO<FceSaa> fceSaaIDao;
	
	@Inject
	IDAO<FceSaaLocalizacaoGeograficaElevatoriaBruta> fceSaaLocalizacaoGeograficaBrutaIDAO;
	
	@Inject
	IDAO<FceSaaLocalizacaoGeograficaEta> fceSaaLocalizacaoGeograficaEtaIDAO;
	
	@Inject
	IDAO<FceSaaLocalizacaoGeograficaElevatoriaTratada> fceSaaLocalizacaoGeograficaTratadaIDAO;
	
	@Inject
	IDAO<FceSaaLocalizacaoGeograficaReservatorio> fceSaaLocalizacaoGeograficaReservatorioIDAO;
	

	@Inject
	IDAO<FceSaaLocalizacaoGeograficaDadosConcedidos> fceSaaLocalizacaoGeograficaDadosConcedidosIDAO;
	
	@Inject
	LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaa(FceSaa fceSaa) {
		fceSaaIDao.salvarOuAtualizar(fceSaa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoBruta(FceSaaLocalizacaoGeograficaElevatoriaBruta localizacaoBruta)  {
		fceSaaLocalizacaoGeograficaBrutaIDAO.salvarOuAtualizar(localizacaoBruta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoDadosConcedidos(FceSaaLocalizacaoGeograficaDadosConcedidos localizacao){
		fceSaaLocalizacaoGeograficaDadosConcedidosIDAO.salvarOuAtualizar(localizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoBruta(FceSaaLocalizacaoGeograficaElevatoriaBruta localizacaoBruta) {
		String sql = "delete from FceSaaLocalizacaoGeograficaElevatoriaBruta  bruta where bruta.ideFceSaaLocalizacaoGeograficaElevatoriaBruta= :ideFceSaaLocalizacaoGeograficaElevatoriaBruta";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSaaLocalizacaoGeograficaElevatoriaBruta", localizacaoBruta.getIdeFceSaaLocalizacaoGeograficaElevatoriaBruta());
		
		fceSaaLocalizacaoGeograficaBrutaIDAO.executarQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoReservatorio(FceSaaLocalizacaoGeograficaReservatorio localizacaoReservatorio)  {
		String sql = "delete from FceSaaLocalizacaoGeograficaReservatorio  reservatorio where reservatorio.ideFceSaaLocalizacaoGeograficaReservatorio= :ideFceSaaLocalizacaoGeograficaReservatorio";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSaaLocalizacaoGeograficaReservatorio", localizacaoReservatorio.getIdeFceSaaLocalizacaoGeograficaReservatorio());
		
		fceSaaLocalizacaoGeograficaReservatorioIDAO.executarQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoTratada(FceSaaLocalizacaoGeograficaElevatoriaTratada localizacaoTratada)  {
		String sql = "delete from FceSaaLocalizacaoGeograficaElevatoriaTratada  tratada where tratada.ideFceSaaLocalizacaoGeograficaElevatoriaTratada= :ideFceSaaLocalizacaoGeograficaElevatoriaTratada";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSaaLocalizacaoGeograficaElevatoriaTratada", localizacaoTratada.getIdeFceSaaLocalizacaoGeograficaElevatoriaTratada());
		
		fceSaaLocalizacaoGeograficaTratadaIDAO.executarQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoEta(FceSaaLocalizacaoGeograficaEta localizacaoEta) {
		String sql = "delete from FceSaaLocalizacaoGeograficaEta  eta where eta.ideFceSaaLocalizacaoGeograficaEta= :ideFceSaaLocalizacaoGeograficaEta";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSaaLocalizacaoGeograficaEta", localizacaoEta.getIdeFceSaaLocalizacaoGeograficaEta());
		
		fceSaaLocalizacaoGeograficaEtaIDAO.executarQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoEta(FceSaaLocalizacaoGeograficaEta localizacaoEta)  {
		fceSaaLocalizacaoGeograficaEtaIDAO.salvarOuAtualizar(localizacaoEta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoTratada(FceSaaLocalizacaoGeograficaElevatoriaTratada localizacaoTratada) {
		fceSaaLocalizacaoGeograficaTratadaIDAO.salvarOuAtualizar(localizacaoTratada);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoReservatorio(FceSaaLocalizacaoGeograficaReservatorio localizacaoReservatorio)  {
		fceSaaLocalizacaoGeograficaReservatorioIDAO.salvarOuAtualizar(localizacaoReservatorio);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSaa buscarFceSaaByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSaa.class);
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		return fceSaaIDao.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaElevatoriaBruta> listarLocalizacaoBrutaByIdeFceSaa(
			FceSaa fceSaa)  {
		
		List<FceSaaLocalizacaoGeograficaElevatoriaBruta> list;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSaaLocalizacaoGeograficaElevatoriaBruta.class,"locaBruta");
		
		criteria
			.createAlias("locaBruta.ideLocalizacaoGeografica", "localizacao",JoinType.INNER_JOIN)
			.createAlias("localizacao.ideSistemaCoordenada", "sistemaCoordenada",JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideFceSaa.ideFceSaa", fceSaa.getIdeFceSaa()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("locaBruta.ideFceSaaLocalizacaoGeograficaElevatoriaBruta"), "ideFceSaaLocalizacaoGeograficaElevatoriaBruta")
				.add(Projections.property("locaBruta.ideFceSaa.ideFceSaa"), "ideFceSaa.ideFceSaa")
				.add(Projections.property("locaBruta.valorVazao"), "valorVazao")
				.add(Projections.property("localizacao.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("sistemaCoordenada.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FceSaaLocalizacaoGeograficaElevatoriaBruta.class));
		
		list = fceSaaLocalizacaoGeograficaBrutaIDAO.listarPorCriteria(criteria, Order.asc("ideFceSaa.ideFceSaa"));
		
		for(FceSaaLocalizacaoGeograficaElevatoriaBruta locBruta : list){
			locBruta.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(locBruta.getIdeLocalizacaoGeografica(), ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
		}

		return list;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaElevatoriaTratada> buscarLocalizacaoTratadaByIdeFceSaa(
			FceSaa fceSaa)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSaaLocalizacaoGeograficaElevatoriaTratada.class,"locaTratada");
		criteria
			.createAlias("locaTratada.ideLocalizacaoGeografica", "localizacao",JoinType.INNER_JOIN)
			.createAlias("localizacao.ideSistemaCoordenada", "sistemaCoordenada",JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideFceSaa.ideFceSaa", fceSaa.getIdeFceSaa()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("locaTratada.ideFceSaaLocalizacaoGeograficaElevatoriaTratada"), "ideFceSaaLocalizacaoGeograficaElevatoriaTratada")
				.add(Projections.property("locaTratada.ideFceSaa.ideFceSaa"), "ideFceSaa.ideFceSaa")
				.add(Projections.property("locaTratada.valorVazaoMedia"), "valorVazaoMedia")
				.add(Projections.property("locaTratada.nomeIdentificacao"), "nomeIdentificacao")
				.add(Projections.property("localizacao.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("sistemaCoordenada.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
			
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FceSaaLocalizacaoGeograficaElevatoriaTratada.class));
		
		List<FceSaaLocalizacaoGeograficaElevatoriaTratada> localizacaoTratada = fceSaaLocalizacaoGeograficaTratadaIDAO.listarPorCriteria(criteria);
		if(!Util.isNullOuVazio(localizacaoTratada)){
			
			for (FceSaaLocalizacaoGeograficaElevatoriaTratada fceSaaLocalizacaoGeograficaElevatoriaTratada : localizacaoTratada) {
				fceSaaLocalizacaoGeograficaElevatoriaTratada.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(fceSaaLocalizacaoGeograficaElevatoriaTratada.getIdeLocalizacaoGeografica(), ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
			}
		}
		
		return localizacaoTratada;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaEta> listarLocalizacaoEtaByIdeFceSaa(
			FceSaa fceSaa)  {
		
		List<FceSaaLocalizacaoGeograficaEta> list;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSaaLocalizacaoGeograficaEta.class,"locaEta");
		criteria
			.createAlias("locaEta.ideLocalizacaoGeografica", "localizacao",JoinType.INNER_JOIN)
			.createAlias("localizacao.ideSistemaCoordenada", "sistemaCoordenada",JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideFceSaa.ideFceSaa", fceSaa.getIdeFceSaa()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("locaEta.ideFceSaaLocalizacaoGeograficaEta"), "ideFceSaaLocalizacaoGeograficaEta")
				.add(Projections.property("locaEta.ideFceSaa.ideFceSaa"), "ideFceSaa.ideFceSaa")
				.add(Projections.property("locaEta.valorVazaoMedia"), "valorVazaoMedia")
				.add(Projections.property("locaEta.nomeIdentificacao"), "nomeIdentificacao")
				.add(Projections.property("localizacao.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("sistemaCoordenada.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FceSaaLocalizacaoGeograficaEta.class));
		
		list = fceSaaLocalizacaoGeograficaEtaIDAO.listarPorCriteria(criteria, Order.asc("ideFceSaa.ideFceSaa"));
		
		for(FceSaaLocalizacaoGeograficaEta locEta : list){
			locEta.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(locEta.getIdeLocalizacaoGeografica(), ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
		}

		return list;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSaaLocalizacaoGeograficaDadosConcedidos buscarLocalizacaoDadosConcedidosByIdeFceSaaAndTipoCaptacao(
			FceSaa fceSaa, Integer ideTipoCaptacao)  {
		
			DetachedCriteria criteria = DetachedCriteria.forClass(FceSaaLocalizacaoGeograficaDadosConcedidos.class,"locaConcedido");
		criteria
			.createAlias("locaConcedido.ideLocalizacaoGeografica", "localizacao",JoinType.INNER_JOIN)
			.createAlias("localizacao.ideSistemaCoordenada", "sistemaCoordenada",JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideFceSaa.ideFceSaa", fceSaa.getIdeFceSaa()))
			.add(Restrictions.eq("ideTipoCaptacao.ideTipoCaptacao", ideTipoCaptacao))
			.setProjection(Projections.projectionList()
				.add(Projections.property("locaConcedido.ideFceSaaLocalizacaoGeograficaDadosConcedidos"), "ideFceSaaLocalizacaoGeograficaDadosConcedidos")
				.add(Projections.property("locaConcedido.ideFceSaa.ideFceSaa"), "ideFceSaa.ideFceSaa")
				.add(Projections.property("locaConcedido.valorVazao"), "valorVazao")
				.add(Projections.property("locaConcedido.nomeCorpoHidrico"), "nomeCorpoHidrico")
				.add(Projections.property("locaConcedido.numPortaria"), "numPortaria")
				.add(Projections.property("localizacao.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("sistemaCoordenada.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FceSaaLocalizacaoGeograficaDadosConcedidos.class));
		

		FceSaaLocalizacaoGeograficaDadosConcedidos fceLocDadosConcedidos= fceSaaLocalizacaoGeograficaDadosConcedidosIDAO.buscarPorCriteria(criteria);

		 if(!Util.isNullOuVazio(fceLocDadosConcedidos)){
			 fceLocDadosConcedidos.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(fceLocDadosConcedidos.getIdeLocalizacaoGeografica(), ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
		 }
		 
		 return fceLocDadosConcedidos;
	
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaReservatorio> listarLocalizacaoReservatorioByIdeFceSaa(
			FceSaa fceSaa)  {
		
		List<FceSaaLocalizacaoGeograficaReservatorio> list;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceSaaLocalizacaoGeograficaReservatorio.class,"locaReservatorio");
		criteria
			.createAlias("locaReservatorio.ideTipoReservatorio", "tipoReservatorio",JoinType.INNER_JOIN)
			.createAlias("locaReservatorio.ideLocalizacaoGeografica", "localizacao",JoinType.INNER_JOIN)
			.createAlias("localizacao.ideSistemaCoordenada", "sistemaCoordenada",JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideFceSaa.ideFceSaa", fceSaa.getIdeFceSaa()))
			.setProjection(Projections.projectionList()
					.add(Projections.property("locaReservatorio.ideFceSaaLocalizacaoGeograficaReservatorio"), "ideFceSaaLocalizacaoGeograficaReservatorio")
					.add(Projections.property("locaReservatorio.ideFceSaa.ideFceSaa"), "ideFceSaa.ideFceSaa")
					.add(Projections.property("locaReservatorio.valorCapacidade"), "valorCapacidade")
					.add(Projections.property("locaReservatorio.nomeIdentificacao"), "nomeIdentificacao")
					.add(Projections.property("tipoReservatorio.ideTipoReservatorio"), "ideTipoReservatorio.ideTipoReservatorio")
					.add(Projections.property("localizacao.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
					.add(Projections.property("sistemaCoordenada.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(FceSaaLocalizacaoGeograficaReservatorio.class));
		
		list = fceSaaLocalizacaoGeograficaReservatorioIDAO.listarPorCriteria(criteria, Order.asc("ideFceSaa.ideFceSaa"));
		
		for(FceSaaLocalizacaoGeograficaReservatorio locReservatorio : list){
			locReservatorio.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(locReservatorio.getIdeLocalizacaoGeografica(), ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
		}

		return list;
	}
	
}
