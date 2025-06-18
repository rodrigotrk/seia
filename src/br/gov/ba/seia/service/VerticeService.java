package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class VerticeService {
	@Inject
	IDAO<DadoGeografico> verticeDAO;
	
	@Inject
	IDAO<SistemaCoordenada> siscoordDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarVertice(DadoGeografico vertice) {
		verticeDAO.salvarOuAtualizar(vertice);		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarVertice(DadoGeografico vertice, SistemaCoordenada sisCoord) {
		verticeDAO.salvarOuAtualizar(vertice);
	
		if(Util.isNullOuVazio(sisCoord.getSrid()))
			sisCoord = siscoordDAO.carregarGet(sisCoord.getIdeSistemaCoordenada());

		String sql = "select fn_inserir_pontos('{"+"\""+vertice.getCoordGeoNumerica()+"\"}', "+sisCoord.getSrid()+", "+vertice.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()+");";
		if(verticeDAO.executarFuncaoNativeQuery(sql, null).equalsIgnoreCase("Sucesso"))
			Log4jUtil.log(VerticeService.class.getName(), Level.INFO, "Persistência do(s) vértice(s) realizada com sucesso!!");
		else
			Log4jUtil.log(VerticeService.class.getName(), Level.ERROR, "Persistência do(s) vértice(s) MAL SUCESSEDIDA!");
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarVertice(DadoGeografico vertice) {
		verticeDAO.salvar(vertice);		
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void mergeVertice(DadoGeografico vertice) {
		verticeDAO.merge(vertice);		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(DadoGeografico vertice){
		vertice = verticeDAO.carregarGet(vertice.getIdeDadoGeografico());
		verticeDAO.remover(vertice);		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public DadoGeografico carregar(DadoGeografico vertice){
		return verticeDAO.carregarLoad(vertice.getIdeDadoGeografico());		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public DadoGeografico buscarEntidadePorLocalizacaoGeografica(DadoGeografico vertice)  {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(DadoGeografico.class, "dado")
				.createAlias("dado.ideLocalizacaoGeografica", "locGeo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("locGeo.ideSistemaCoordenada", "sisCoord", JoinType.LEFT_OUTER_JOIN);
				
		ProjectionList projecao = Projections.projectionList()
					.add(Projections.property("dado.ideDadoGeografico"), "ideDadoGeografico")
					.add(Projections.property("dado.coordGeoNumerica"), "coordGeoNumerica")
					
					.add(Projections.property("locGeo.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
					.add(Projections.property("locGeo.dtcCriacao"), "ideLocalizacaoGeografica.dtcCriacao")
					.add(Projections.property("locGeo.indExcluido"), "ideLocalizacaoGeografica.indExcluido")
					.add(Projections.property("locGeo.dtcExclusao"), "ideLocalizacaoGeografica.dtcExclusao")
					.add(Projections.property("locGeo.fonteCoordenada"), "ideLocalizacaoGeografica.fonteCoordenada")
					.add(Projections.property("locGeo.desLocalizacaoGeografica"), "ideLocalizacaoGeografica.desLocalizacaoGeografica")
					
					.add(Projections.property("sisCoord.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
					.add(Projections.property("sisCoord.nomSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.nomSistemaCoordenada")
					.add(Projections.property("sisCoord.srid"), "ideLocalizacaoGeografica.ideSistemaCoordenada.srid");
		
		criteria.setProjection(projecao).setResultTransformer(new AliasToNestedBeanResultTransformer(DadoGeografico.class))
				.add(Restrictions.eq("locGeo.ideLocalizacaoGeografica", vertice.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
		
		return verticeDAO.buscarPorCriteria(criteria);
	}
}