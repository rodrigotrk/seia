package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelPlantio;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegistroFlorestaProducaoImovelPlantioDAOImpl {

	@Inject
	private IDAO<RegistroFlorestaProducaoImovelPlantio> registroFlorestaProducaoImovelPlantioIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoImovelPlantio(RegistroFlorestaProducaoImovelPlantio ideRegistroFlorestaProducaoImovelPlantio) {
		try {
			if(ideRegistroFlorestaProducaoImovelPlantio.getIdeRegistroFlorestaImovelPlantio()!=null){
				registroFlorestaProducaoImovelPlantioIDAO.remover(ideRegistroFlorestaProducaoImovelPlantio);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoImovelPlantio(List<RegistroFlorestaProducaoImovelPlantio> ideRegistroFlorestaProducaoImovelPlantio) {
		try {
			for (RegistroFlorestaProducaoImovelPlantio registroFlorestaProducaoImovelPlantio : ideRegistroFlorestaProducaoImovelPlantio) {
				if(registroFlorestaProducaoImovelPlantio.getIdeRegistroFlorestaImovelPlantio()!=null){
					registroFlorestaProducaoImovelPlantioIDAO.remover(registroFlorestaProducaoImovelPlantio);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducaoImovelPlantio(List<RegistroFlorestaProducaoImovelPlantio> registroFlorestaProducaoImovelPlantioList) {
		try {
			registroFlorestaProducaoImovelPlantioIDAO.salvarEmLote(registroFlorestaProducaoImovelPlantioList);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducaoImovelPlantio(RegistroFlorestaProducaoImovelPlantio registroFlorestaProducaoImovelPlantio) {
		try {
			if(Util.isNullOuVazio(registroFlorestaProducaoImovelPlantio.getIdeRegistroFlorestaImovelPlantio())){
				registroFlorestaProducaoImovelPlantioIDAO.salvar(registroFlorestaProducaoImovelPlantio);
			}else{
				registroFlorestaProducaoImovelPlantioIDAO.atualizar(registroFlorestaProducaoImovelPlantio);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegistroFlorestaProducaoImovelPlantio> listarRegistroFlorestaProducaoImovel(RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel) {
		
		try {
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RegistroFlorestaProducaoImovelPlantio.class)
				.createAlias("ideSituacaoAtualFlorestaProducao", "ideSituacaoAtualFlorestaProducao")

				.createAlias("ideLocalizacaoGeografica", "ideLocalizacaoGeografica",  JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideLocalizacaoGeografica.dadoGeograficoCollection", "dadoGeografico",   JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideLocalizacaoGeografica.ideSistemaCoordenada", "ideSistemaCoordenada",  JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideLocalizacaoGeografica.ideClassificacaoSecao", "ideClassificacaoSecao",  JoinType.LEFT_OUTER_JOIN)

				.add(Restrictions.eq("ideRegistroFlorestaProducaoImovel.ideRegistroFlorestaProducaoImovel",ideRegistroFlorestaProducaoImovel.getIdeRegistroFlorestaProducaoImovel()))
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideRegistroFlorestaProducaoImovel"),"ideRegistroFlorestaProducaoImovel")
						
					.add(Projections.property("ideRegistroFlorestaImovelPlantio"),"ideRegistroFlorestaImovelPlantio")
					.add(Projections.property("valAreaPlantio"),"valAreaPlantio")
					.add(Projections.property("dtInicioPeriodoImplantacao"),"dtInicioPeriodoImplantacao")
					.add(Projections.property("dtInicioPrevistaImplantacao"),"dtInicioPrevistaImplantacao")
					.add(Projections.property("dtFimPeriodoImplantacao"),"dtFimPeriodoImplantacao")
					.add(Projections.property("dtFimPrevistaImplantacao"),"dtFimPrevistaImplantacao")
					
					.add(Projections.property("ideSituacaoAtualFlorestaProducao.ideSituacaoAtualFlorestaProducao"),"ideSituacaoAtualFlorestaProducao.ideSituacaoAtualFlorestaProducao")
					.add(Projections.property("ideSituacaoAtualFlorestaProducao.desSituacaoFlorestaProducao"),"ideSituacaoAtualFlorestaProducao.desSituacaoFlorestaProducao")
					.add(Projections.property("ideSituacaoAtualFlorestaProducao.dtcCriacao"),"ideSituacaoAtualFlorestaProducao.dtcCriacao")
					.add(Projections.property("ideSituacaoAtualFlorestaProducao.dtcExclusao"),"ideSituacaoAtualFlorestaProducao.dtcExclusao")
					.add(Projections.property("ideSituacaoAtualFlorestaProducao.indExcluido"),"ideSituacaoAtualFlorestaProducao.indExcluido")				
					
					.add(Projections.property("ideLocalizacaoGeografica.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica.ideLocalizacaoGeografica")		
					.add(Projections.property("ideLocalizacaoGeografica.dtcCriacao"),"ideLocalizacaoGeografica.dtcCriacao")		
					.add(Projections.property("ideLocalizacaoGeografica.indExcluido"),"ideLocalizacaoGeografica.indExcluido")
					.add(Projections.property("ideLocalizacaoGeografica.dtcExclusao"),"ideLocalizacaoGeografica.dtcExclusao")
					.add(Projections.property("ideLocalizacaoGeografica.fonteCoordenada"),"ideLocalizacaoGeografica.fonteCoordenada")
					.add(Projections.property("ideLocalizacaoGeografica.desLocalizacaoGeografica"),"ideLocalizacaoGeografica.desLocalizacaoGeografica")
					
					.add(Projections.property("dadoGeografico.ideDadoGeografico"),"ideLocalizacaoGeografica.dadoGeograficoCollection.ideDadoGeografico")
					.add(Projections.property("dadoGeografico.coordGeoNumerica"),"ideLocalizacaoGeografica.dadoGeograficoCollection.coordGeoNumerica")
					
					.add(Projections.property("ideSistemaCoordenada.ideSistemaCoordenada"),"ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
					.add(Projections.property("ideSistemaCoordenada.nomSistemaCoordenada"),"ideLocalizacaoGeografica.ideSistemaCoordenada.nomSistemaCoordenada")
					
					.add(Projections.property("ideClassificacaoSecao.ideClassificacaoSecao"),"ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")
					.add(Projections.property("ideClassificacaoSecao.nomClassificacaoSecao"),"ideLocalizacaoGeografica.ideClassificacaoSecao.nomClassificacaoSecao")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(RegistroFlorestaProducaoImovelPlantio.class))	;

			return registroFlorestaProducaoImovelPlantioIDAO.listarPorCriteria(detachedCriteria);
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	
}
