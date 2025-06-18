package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Classe;
import br.gov.ba.seia.entity.DetalhamentoBoleto;
import br.gov.ba.seia.entity.DetalhamentoBoletoFinalidade;
import br.gov.ba.seia.entity.ParametroCalculo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ParametroCalculoService {

	@Inject
	private IDAO<ParametroCalculo> parametroDAO;
	
	private DetachedCriteria getCriteriaBasica() {
		return  DetachedCriteria.forClass(ParametroCalculo.class)
			.createAlias("ideAtoAmbiental", "ato", JoinType.INNER_JOIN)
			.createAlias("ideTipologia", "tipologia", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipoFinalidadeUsoAgua", "finalidade", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideClasse", "classe", JoinType.LEFT_OUTER_JOIN)
			
			.setProjection(
				Projections.projectionList()
				.add(Property.forName("ideParametroCalculo"), "ideParametroCalculo")
				.add(Property.forName("valorTaxa"), "valorTaxa")
				.add(Property.forName("areaMinima"), "areaMinima")
				.add(Property.forName("areaMaxima"), "areaMaxima")
				.add(Property.forName("numUfir"), "numUfir")
				.add(Property.forName("fatorMultiplicador"), "fatorMultiplicador")
				.add(Property.forName("dtcCriacao"), "dtcCriacao")
				.add(Property.forName("indBoleto"), "indBoleto")
				.add(Property.forName("indAtivo"), "indAtivo")
				.add(Property.forName("vazaoMinima"), "vazaoMinima")
				.add(Property.forName("vazaoMaxima"), "vazaoMaxima")
				
				.add(Property.forName("classe.ideClasse"), "ideClasse.ideClasse")
				.add(Property.forName("classe.nomClasse"), "ideClasse.nomClasse")
				.add(Property.forName("classe.indAtivo"), "ideClasse.indAtivo")
				
				.add(Property.forName("tipologia.ideTipologia"), "ideTipologia.ideTipologia")
				.add(Property.forName("tipologia.codTipologia"), "ideTipologia.codTipologia")
				.add(Property.forName("tipologia.desTipologia"), "ideTipologia.desTipologia")
				.add(Property.forName("tipologia.dtcCriacao"), "ideTipologia.dtcCriacao")
				.add(Property.forName("tipologia.dtcExclusao"), "ideTipologia.dtcExclusao")
				.add(Property.forName("tipologia.indAutorizacao"), "ideTipologia.indAutorizacao")
				.add(Property.forName("tipologia.indPossuiFilhos"), "ideTipologia.indPossuiFilhos")
				
				.add(Property.forName("finalidade.ideTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
				.add(Property.forName("finalidade.nomTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
				
				.add(Property.forName("ato.ideAtoAmbiental"), "ideAtoAmbiental.ideAtoAmbiental")
				.add(Property.forName("ato.sglAtoAmbiental"), "ideAtoAmbiental.sglAtoAmbiental")
				.add(Property.forName("ato.numDiasValidade"), "ideAtoAmbiental.numDiasValidade")
				.add(Property.forName("ato.indDeclaratorio"), "ideAtoAmbiental.indDeclaratorio")
				.add(Property.forName("ato.indAtivo"), "ideAtoAmbiental.indAtivo")
				.add(Property.forName("ato.indVisivelSolicitacaoTla"), "ideAtoAmbiental.indVisivelSolicitacaoTla")
				.add(Property.forName("ato.indAutomatico"), "ideAtoAmbiental.indAutomatico")
				
			).setResultTransformer(new AliasToNestedBeanResultTransformer(ParametroCalculo.class))
			.add(Restrictions.eq("indAtivo", true));
	}
	
	public Collection<ParametroCalculo> listar(DetalhamentoBoleto detalhamentoBoleto, Classe classe, BigDecimal vazao)  {
		
		DetachedCriteria detachedCriteria = getCriteriaBasica();
		
		detachedCriteria.add(Restrictions.eq("ato.ideAtoAmbiental", detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental()));
		
		if(!Util.isNull(detalhamentoBoleto.getIdeTipoFinalidadeUsoAgua())){
			detachedCriteria.add(Restrictions.eq("finalidade.ideTipoFinalidadeUsoAgua", detalhamentoBoleto.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua()));
		}else{
			detachedCriteria.add(Restrictions.isNull("finalidade.ideTipoFinalidadeUsoAgua"));
		}
		
		if(!Util.isNull(classe)){
			detachedCriteria.add(Restrictions.or(Restrictions.eq("classe.ideClasse", classe.getIdeClasse()), Restrictions.isNull("classe.ideClasse")));
		}

		if(!Util.isNull(detalhamentoBoleto.getIdeTipologia()) 
				&& !detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.AUTORIZACAO_AMBIENTAL.getId())) {
			detachedCriteria.add(Restrictions.eq("tipologia.ideTipologia", detalhamentoBoleto.getIdeTipologia().getIdeTipologia()));
		} else {
			detachedCriteria.add(Restrictions.isNull("tipologia.ideTipologia"));
		}
		
		if(!Util.isNullOuVazio(vazao) && !detalhamentoBoleto.getIdeAtoAmbiental().isDispensaOutorga()) {
			detachedCriteria.add(Restrictions.or(Restrictions.lt("vazaoMinima", vazao), Restrictions.eq("vazaoMinima", vazao)));
			detachedCriteria.add(
					Restrictions.or(
							(Restrictions.or(Restrictions.gt("vazaoMaxima", vazao), Restrictions.eq("vazaoMaxima", vazao))), 
							Restrictions.isNull("vazaoMaxima")));
		}
		
		if(!Util.isNullOuVazio(detalhamentoBoleto.getTipoCriadouroFaunaCollection())) {
			detachedCriteria.add(Restrictions.in("ideTipoCriadouroFauna", detalhamentoBoleto.getTipoCriadouroFaunaCollection()));
		}
		
		return parametroDAO.listarPorCriteria(detachedCriteria);
	}

	public Collection<ParametroCalculo> listarParametrosDAEPorBioma(Integer ideBioma)  {
		DetachedCriteria detachedCriteria =  DetachedCriteria.forClass(ParametroCalculo.class)
			.createAlias("ideBioma", "b")
			.add(Restrictions.eq("b.ideBioma", ideBioma))
			.add(Restrictions.eq("indBoleto", false))
			.add(Restrictions.eq("indAtivo", true))
		;
		
		return parametroDAO.listarPorCriteria(detachedCriteria, Order.asc("ideParametroCalculo"));	
	}
	
	public Collection<ParametroCalculo> listarParametrosDAE(Integer ideAtoAmbiental)  {
		DetachedCriteria detachedCriteria =  DetachedCriteria.forClass(ParametroCalculo.class)
				.createAlias("ideAtoAmbiental", "ato")
				.createAlias("ideTipologia", "tipologia",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideClasse", "classe",JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("ato.ideAtoAmbiental", ideAtoAmbiental))
			.add(Restrictions.eq("indBoleto", false))
			.add(Restrictions.eq("indAtivo", true));
			
			return parametroDAO.listarPorCriteria(detachedCriteria, Order.asc("ideParametroCalculo"));	
	}
	
	public Collection<ParametroCalculo> listarParametrosBoleto(DetalhamentoBoleto detalhamentoBoleto,Classe classe)  {
		DetachedCriteria detachedCriteria =  DetachedCriteria.forClass(ParametroCalculo.class)
				.createAlias("ideAtoAmbiental", "ato")
				.createAlias("ideTipologia", "tipologia",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoFinalidadeUsoAgua", "ideTipoFinalidadeUsoAgua",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideClasse", "classe",JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ato.ideAtoAmbiental", detalhamentoBoleto.getIdeAtoAmbiental().getIdeAtoAmbiental()))
				.add(Restrictions.eq("indBoleto", true));
		
		if(!Util.isNull(classe)){
			detachedCriteria.add(Restrictions.or(
					Restrictions.eq("classe.ideClasse", classe.getIdeClasse()),
					Restrictions.isNull("classe.ideClasse")));
		}
		
		if(!Util.isNull(detalhamentoBoleto.getIdeTipoFinalidadeUsoAgua())){
			detachedCriteria.add(Restrictions.eq("ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua", detalhamentoBoleto.getIdeTipoFinalidadeUsoAgua()
					.getIdeTipoFinalidadeUsoAgua()));
		}
		
		if(!Util.isNull(detalhamentoBoleto.getIdeTipologia()))
			detachedCriteria.add(Restrictions.eq("tipologia.ideTipologia", detalhamentoBoleto.getIdeTipologia().getIdeTipologia()));
		
		return parametroDAO.listarPorCriteria(detachedCriteria);	
	}
	
	public List<ParametroCalculo> listarParametrosAtivosPorRequerimento(Integer ideRequerimento, List<AtoAmbiental> atos)  {
		DetachedCriteria criteria =  DetachedCriteria.forClass(Requerimento.class)
				.createAlias("enquadramentoCollection", "enq",JoinType.INNER_JOIN)
				.createAlias("enq.enquadramentoAtoAmbientalCollection","enqAto",JoinType.INNER_JOIN)
				.createAlias("enqAto.atoAmbiental","ato",JoinType.INNER_JOIN)
				.createAlias("ato.parametroCalculoCollection","pCalculo",JoinType.INNER_JOIN)
				.createAlias("pCalculo.ideTipologia", "tipologia",JoinType.LEFT_OUTER_JOIN)
				.createAlias("pCalculo.ideTipoFinalidadeUsoAgua", "finalidade",JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideRequerimento", ideRequerimento))
				.add(Restrictions.in("enqAto.atoAmbiental", atos))
				.add(Restrictions.eq("pCalculo.indAtivo", true))
				.addOrder(Order.asc("ato.nomAtoAmbiental"))
				.addOrder(Order.asc("tipologia.desTipologia"))
				.addOrder(Order.asc("finalidade.nomTipoFinalidadeUsoAgua"))
				.setProjection(Projections.distinct(Projections.projectionList()
						.add(Projections.property("pCalculo.ideParametroCalculo"),"ideParametroCalculo")
						.add(Projections.property("pCalculo.valorTaxa"),"valorTaxa")
						.add(Projections.property("pCalculo.areaMinima"),"areaMinima")
						.add(Projections.property("pCalculo.ideClasse"),"ideClasse")
						.add(Projections.property("pCalculo.numUfir"),"numUfir")
						.add(Projections.property("pCalculo.fatorMultiplicador"),"fatorMultiplicador")
						.add(Projections.property("pCalculo.dtcCriacao"),"dtcCriacao")
						.add(Projections.property("pCalculo.indBoleto"),"indBoleto")
						.add(Projections.property("pCalculo.indAtivo"),"indAtivo")
						.add(Projections.property("tipologia.ideTipologia"),"ideTipologia.ideTipologia")
						.add(Projections.property("tipologia.desTipologia"),"ideTipologia.desTipologia")
						.add(Projections.property("finalidade.ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
						.add(Projections.property("finalidade.nomTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
						.add(Projections.property("ato.ideAtoAmbiental"),"ideAtoAmbiental.ideAtoAmbiental")
						.add(Projections.property("ato.nomAtoAmbiental"),"ideAtoAmbiental.nomAtoAmbiental")
					))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ParametroCalculo.class));
		
		return parametroDAO.listarPorCriteria(criteria);
		
	}
	
	public List<ParametroCalculo> obterParametroCalculoPorAto(Integer ideAtoAmbiental)  {
		DetachedCriteria criteria =  DetachedCriteria.forClass(ParametroCalculo.class)
				.add(Restrictions.eq("ideAtoAmbiental.ideAtoAmbiental", ideAtoAmbiental))
				.add(Restrictions.isNotNull("ideClasse"))
				.add(Restrictions.eq("indAtivo", true))
				.addOrder(Order.asc("ideClasse"));
		
		return parametroDAO.listarPorCriteria(criteria);
	}
	
	public List<ParametroCalculo> obterParametroCalculoPorAtoSemClasse(Integer ideAtoAmbiental)  {
		DetachedCriteria criteria =  DetachedCriteria.forClass(ParametroCalculo.class)
				.add(Restrictions.eq("ideAtoAmbiental.ideAtoAmbiental", ideAtoAmbiental))
				.add(Restrictions.eq("indAtivo", true));
		
		return parametroDAO.listarPorCriteria(criteria);
	}	
	
	public List<ParametroCalculo> obterParametroCalculoPorMenorClasseEValor(Integer ideAtoAmbiental)  {
		DetachedCriteria criteria =  DetachedCriteria.forClass(ParametroCalculo.class)
				.add(Restrictions.eq("ideAtoAmbiental.ideAtoAmbiental", ideAtoAmbiental))
				.add(Restrictions.isNotNull("ideClasse"))
				.add(Restrictions.isNotNull("ideTipologia"))
				.add(Restrictions.eq("indAtivo", true))
				.addOrder(Order.asc("ideClasse"))
				.addOrder(Order.asc("valorTaxa"));
		
		return parametroDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ParametroCalculo> listarPorDetalhamentoBoletoFinalidade(DetalhamentoBoletoFinalidade dbf) {
		
		try {
			DetachedCriteria criteria = getCriteriaBasica()
				.add(Restrictions.eq("ato.ideAtoAmbiental", dbf.getIdeDetalhamentoBoleto().getIdeAtoAmbiental().getIdeAtoAmbiental()))
				.add(Restrictions.eq("tipologia.ideTipologia", dbf.getIdeDetalhamentoBoleto().getIdeTipologia().getIdeTipologia()));
			
			if(!AtoAmbientalEnum.AOUT.getId().equals(dbf.getIdeDetalhamentoBoleto().getIdeAtoAmbiental().getIdeAtoAmbiental())) {
				criteria.add(Restrictions.eq("finalidade.ideTipoFinalidadeUsoAgua", dbf.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua()));
			}
				
			if(!Util.isNullOuVazio(dbf.getNumAreaDessedentacaoAnimal())) {
				criteria.add(Restrictions.or(
					(Restrictions.or(
						Restrictions.lt("areaMinima", dbf.getNumAreaDessedentacaoAnimal()), 
						Restrictions.eq("areaMinima", dbf.getNumAreaDessedentacaoAnimal()))),
					Restrictions.isNull("areaMinima")));
				
				criteria.add(Restrictions.or(
					(Restrictions.or(
						Restrictions.gt("areaMaxima", dbf.getNumAreaDessedentacaoAnimal()), 
						Restrictions.eq("areaMaxima", dbf.getNumAreaDessedentacaoAnimal()))),
					Restrictions.isNull("areaMaxima")));
			}
			
			//Especificidade de abastecimento industrial - DEPOIS MELHORAR
			if(dbf.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getId())) {
				
				if(!Util.isNullOuVazio(dbf.getIndAbastecimentoEmDistritoIndustrial()) && dbf.getIndAbastecimentoEmDistritoIndustrial()) {
					criteria.add(Restrictions.eq("classe.ideClasse", 2));
				} else {
					criteria.add(Restrictions.eq("classe.ideClasse", 1));
				}
			}
		
		return parametroDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}