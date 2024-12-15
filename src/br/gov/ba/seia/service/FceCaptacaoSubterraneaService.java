package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceCaptacaoSubterranea;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ReservaAgua;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCaptacaoSubterraneaService {

	@Inject
	private IDAO<FceCaptacaoSubterranea> fceCaptacaoSubterraneaIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaptacaoSubteranea(FceCaptacaoSubterranea paramCaptacaoSubterranea) {
		fceCaptacaoSubterraneaIDAO.salvarOuAtualizar(paramCaptacaoSubterranea);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSomenteCaptacaoSubteranea(FceCaptacaoSubterranea paramCaptacaoSubterranea) {
		fceCaptacaoSubterraneaIDAO.salvar(paramCaptacaoSubterranea);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceCaptacaoSubterranea> listarFceCaptacaoSubterraneaByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCaptacaoSubterranea.class)
				.createAlias("ideFce", "f", JoinType.LEFT_OUTER_JOIN)
				.createAlias("f.ideDadoOrigem", "of", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoPoco", "ideTipoPoco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoAquifero", "ideTipoAquifero", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideAquifero", "ideAquifero", JoinType.LEFT_OUTER_JOIN);
		if(FceUtil.isExibirParaAnaliseTecnica(fce)){
			criteria.createAlias("ideFceOutorgaLocalizacaoGeografica","folg")
			.createAlias("folg.ideLocalizacaoGeografica", "lg")
			.createAlias("folg.listaReservaAgua", "lra", JoinType.LEFT_OUTER_JOIN)
			.createAlias("lra.ideStatusReservaAgua", "sra", JoinType.LEFT_OUTER_JOIN)
			;
			DetachedCriteria subCriteria = DetachedCriteria.forClass(ReservaAgua.class,"ra")
					.createAlias("ra.ideFceOutorgaLocalizacaoGeografica", "folgr", JoinType.LEFT_OUTER_JOIN)
					.createAlias("folgr.ideFce", "f2", JoinType.LEFT_OUTER_JOIN)
					.add(Restrictions.eq("f2.ideFce", fce.getIdeFce()))
					.setProjection(
							Projections.projectionList()
							.add(Projections.max("ra.dtcStatusReservaAgua"))
							);
			criteria.add(Restrictions.or(Subqueries.propertyEq("lra.dtcStatusReservaAgua", subCriteria),
					Restrictions.isNull("lra.reservaAguaPK")))
			;
		} else {
			criteria.createAlias("ideOutorgaLocalizacaoGeografica","olg")
			.createAlias("olg.ideLocalizacaoGeografica", "lg");
		}
		criteria.createAlias("ideUnidadeGeologicaAflorante", "ideUnidadeGeologicaAflorante", JoinType.LEFT_OUTER_JOIN)
		.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.INNER_JOIN)
		.add(Restrictions.eq("f.ideFce", fce.getIdeFce()));
		List<FceCaptacaoSubterranea> lista = fceCaptacaoSubterraneaIDAO.listarPorCriteria(criteria, Order.asc("dg.ideLocalizacaoGeografica"));
		if(!Util.isNullOuVazio(lista)){
			for(FceCaptacaoSubterranea fceCaptacaoSubterranea : lista){
				if(FceUtil.isExibirParaAnaliseTecnica(fce)){
					fceCaptacaoSubterranea.getIdeFceOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
				} else {
					fceCaptacaoSubterranea.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
				}
			}
		}
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceCaptacaoSubterranea> buscarFceCaptacaoSubterranea(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCaptacaoSubterranea.class);
		criteria.createAlias("ideFce", "ideFce");
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		return fceCaptacaoSubterraneaIDAO.listarPorCriteria(criteria, Order.asc("ideFce"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceCaptacaoSubterranea> listarFceCaptacaoSubterraneaTecnicoByIdeRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCaptacaoSubterranea.class)
				.createAlias("ideFce", "f", JoinType.LEFT_OUTER_JOIN)
				.createAlias("f.ideDadoOrigem", "of", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoPoco", "ideTipoPoco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoAquifero", "ideTipoAquifero", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideAquifero", "ideAquifero", JoinType.LEFT_OUTER_JOIN);


			criteria.createAlias("ideFceOutorgaLocalizacaoGeografica","folg")
			.createAlias("folg.ideLocalizacaoGeografica", "lg")
			.createAlias("folg.listaReservaAgua", "lra", JoinType.LEFT_OUTER_JOIN)
			.createAlias("lra.ideStatusReservaAgua", "sra", JoinType.LEFT_OUTER_JOIN)
			;
			DetachedCriteria subCriteria = DetachedCriteria.forClass(ReservaAgua.class,"ra")
					.createAlias("ra.ideFceOutorgaLocalizacaoGeografica", "folgr", JoinType.LEFT_OUTER_JOIN)
					.createAlias("folgr.ideFce", "f2", JoinType.LEFT_OUTER_JOIN)
					.add(Restrictions.eqProperty("f2.ideFce", "f.ideFce"))
					.setProjection(
							Projections.projectionList()
							.add(Projections.max("ra.dtcStatusReservaAgua"))
							);
			criteria.add(Restrictions.or(Subqueries.propertyEq("lra.dtcStatusReservaAgua", subCriteria),
					Restrictions.isNull("lra.reservaAguaPK")))
			;

			
		criteria.createAlias("ideUnidadeGeologicaAflorante", "ideUnidadeGeologicaAflorante", JoinType.LEFT_OUTER_JOIN)
		.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.INNER_JOIN)
		.add(Restrictions.eq("f.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()))
		.add(Restrictions.eq("of.ideDadoOrigem", DadoOrigemEnum.TECNICO.getId()));
		
		
		List<FceCaptacaoSubterranea> lista = fceCaptacaoSubterraneaIDAO.listarPorCriteria(criteria, Order.asc("dg.ideLocalizacaoGeografica"));
		if(!Util.isNullOuVazio(lista)){
			for(FceCaptacaoSubterranea fceCaptacaoSubterranea : lista){
				fceCaptacaoSubterranea.getIdeFceOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			}
		}
		return lista;
	}
}
