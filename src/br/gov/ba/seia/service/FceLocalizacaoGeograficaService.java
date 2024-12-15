package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.controller.FceLocalizacaoGeograficaController;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceLocalizacaoGeograficaPK;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLocalizacaoGeograficaService {


	@Inject
	private IDAO<FceLocalizacaoGeografica> fceLocalizacaoGeograficaPerfuracaoPocoIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLocalizacaoGeograficaPerfuracaoPoco(FceLocalizacaoGeografica paramFceLocalizacaoGeograficaPerfuracaoPoco) {
		fceLocalizacaoGeograficaPerfuracaoPocoIDAO.salvar(paramFceLocalizacaoGeograficaPerfuracaoPoco);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizartFceLocalizacaoGeograficaPerfuracaoPoco(FceLocalizacaoGeografica paramFceLocalizacaoGeograficaPerfuracaoPoco) {
		fceLocalizacaoGeograficaPerfuracaoPocoIDAO.salvarOuAtualizar(paramFceLocalizacaoGeograficaPerfuracaoPoco);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLocalizacaoGeografica> listarFceLocalizacaoGeograficaPerfuracaoPocoByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLocalizacaoGeografica.class)
				.createAlias("ideFce", "f", JoinType.INNER_JOIN)
				.createAlias("ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
				.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.INNER_JOIN)
				.add(Restrictions.eq("f.ideFce", fce.getIdeFce()));

		return fceLocalizacaoGeograficaPerfuracaoPocoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceLocalizacaoGeografica buscarFceLocalizacaoGeograficaByFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLocalizacaoGeografica.class)
			.createAlias("ideFce", "fce")
			.createAlias("ideLocalizacaoGeografica", "locGeo")
			.createAlias("locGeo.ideClassificacaoSecao", "sec")
			.createAlias("locGeo.ideSistemaCoordenada", "sis")
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("fce.ideFce"), "ideFce.ideFce")
				.add(Projections.property("locGeo.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("sec.ideClassificacaoSecao"), "ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")
				.add(Projections.property("sis.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FceLocalizacaoGeografica.class))
		
			.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		
		FceLocalizacaoGeografica fceLocalizacaoGeografica = fceLocalizacaoGeograficaPerfuracaoPocoIDAO.buscarPorCriteria(criteria);
		
		if(!Util.isNull(fceLocalizacaoGeografica)){
			fceLocalizacaoGeografica.setIdeFceLocalizacaoGeograficaPK(new FceLocalizacaoGeograficaPK(fceLocalizacaoGeografica.getIdeFce(), fceLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
		}
		
		return fceLocalizacaoGeografica;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceLocalizacaoGeografica(FceLocalizacaoGeografica localizacaoGeograficaPerfuracaoPoco) {
		fceLocalizacaoGeograficaPerfuracaoPocoIDAO.remover(localizacaoGeograficaPerfuracaoPoco);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLocalizacaoGeografica> listarFcePerfuracaoPocoByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLocalizacaoGeografica.class);
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		return fceLocalizacaoGeograficaPerfuracaoPocoIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLocalizacaoGeografica> buscarFceLocalizacaoGeograficaByRequerimento(Requerimento req)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLocalizacaoGeografica.class)
				.createAlias("ideFce","fce", JoinType.INNER_JOIN)
				.createAlias("fce.ideRequerimento","requerimento", JoinType.INNER_JOIN)
		
				.add(Restrictions.eq("req.ideRequerimento", req.getIdeRequerimento()));
		
		return fceLocalizacaoGeograficaPerfuracaoPocoIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int countFceLocalizacaoGeograficaByRequerimento(Requerimento req)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLocalizacaoGeografica.class)
				.createAlias("ideFce", "fce", JoinType.INNER_JOIN)
				.createAlias("fce.ideRequerimento", "req", JoinType.INNER_JOIN)
				.createAlias("this.ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.INNER_JOIN)
				.createAlias("localizacaoGeografica.dadoGeograficoCollection", "dadoGeografico", JoinType.INNER_JOIN)
				
				.add(Restrictions.eq("req.ideRequerimento", req.getIdeRequerimento()));
		
		return fceLocalizacaoGeograficaPerfuracaoPocoIDAO.count(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceLocalizacaoGeograficaController ctrl) throws Exception  {
		ctrl.prepararParaFinalizar();
	}
}