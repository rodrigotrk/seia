/**
 * 		07/01/14
 * @author eduardo.fernandes
 */
package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DadoOrigem;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLancamentoEfluente;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ReservaAgua;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceOutorgaLocalizacaoGeograficaService {

	@Inject
	private IDAO<FceOutorgaLocalizacaoGeografica> fceOutorgaLocalizacaoGeograficaIDAO;
	@EJB
	private FceOutorgaLocalizacaoGeograficaFinalidadeService fceOutorgaLocalizacaoGeograficaFinalidadeService;
	@EJB
	private LocalizacaoGeograficaServiceFacade facadeLocalizacaoGeografica;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocGeoByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoGeografica.class)
				.createAlias("ideFce", "fce")
				.createAlias("ideOutorgaLocalizacaoGeografica","olg")
				.createAlias("olg.ideTipoBarragem", "barragem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("olg.ideLocalizacaoGeografica", "lg")
				.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.LEFT_OUTER_JOIN)
				.createAlias("localCaptacao", "loc", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		List<FceOutorgaLocalizacaoGeografica> lista = fceOutorgaLocalizacaoGeograficaIDAO.listarPorCriteria(criteria);
		if(!Util.isNullOuVazio(lista)){
			for(FceOutorgaLocalizacaoGeografica fceLocalizacaoGeografica : lista){
				fceLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
				if(!Util.isNullOuVazio(fceLocalizacaoGeografica.getNumTempoCaptacao()) || !Util.isNullOuVazio(fceLocalizacaoGeografica.getNomRio())){
					fceLocalizacaoGeografica.setConfirmado(true);
				}
			}
		}
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocGeoAnaliseTecnicaByIdeFce(Fce fce) throws CloneNotSupportedException {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoGeografica.class)
				.createAlias("ideFce", "fce")
				.createAlias("localCaptacao", "lc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideLocalizacaoGeografica", "lg")
				.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.INNER_JOIN)
				.createAlias("listaReservaAgua", "lra", JoinType.LEFT_OUTER_JOIN)
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
		.add(Restrictions.eq("fce.ideFce", fce.getIdeFce()))
		;
		List<FceOutorgaLocalizacaoGeografica> lista = fceOutorgaLocalizacaoGeograficaIDAO.listarPorCriteria(criteria);
		if(!Util.isNullOuVazio(lista)){
			for(FceOutorgaLocalizacaoGeografica fceLocalizacaoGeografica : lista){
				fceLocalizacaoGeografica.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
				FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica = null;
				if(!Util.isNullOuVazio(fceLocalizacaoGeografica.getIdeFceLancamentoEfluente())){
					fceOutorgaLocalizacaoGeografica = fceLocalizacaoGeografica.getIdeFceLancamentoEfluente().getIdeFceOutorgaLocalizacaoGeografica().clone();
					fceLocalizacaoGeografica.getIdeFceLancamentoEfluente().setIdeFceOutorgaLocalizacaoGeografica(new FceOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica()));
				} else if (!Util.isNullOuVazio(fceLocalizacaoGeografica.getIdeFceLancamentoEfluente())){
					fceOutorgaLocalizacaoGeografica = fceLocalizacaoGeografica.getIdeFceCaptacaoSubterranea().getIdeFceOutorgaLocalizacaoGeografica().clone();
					fceLocalizacaoGeografica.getIdeFceCaptacaoSubterranea().setIdeFceOutorgaLocalizacaoGeografica(new FceOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica()));
				} else {
					fceOutorgaLocalizacaoGeografica = fceLocalizacaoGeografica.getIdeFceCaptacaoSuperficial().getIdeFceOutorgaLocalizacaoGeografica().clone();
					fceLocalizacaoGeografica.getIdeFceCaptacaoSuperficial().setIdeFceOutorgaLocalizacaoGeografica(new FceOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica()));
				}
				if(!Util.isNullOuVazio(fceLocalizacaoGeografica.getNumTempoCaptacao()) || !Util.isNullOuVazio(fceLocalizacaoGeografica.getNomRio())){
					fceLocalizacaoGeografica.setConfirmado(true);
				}
			}
		}
		
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocGeoAnaliseTecnicaToOutorgaAquiculturaByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoGeografica.class)
				.createAlias("ideFce", "f1")
				.createAlias("f1.ideRequerimento", "r")
				.createAlias("r.fceCollection", "f2")
				.createAlias("f1.ideDocumentoObrigatorio", "doc")
				.createAlias("f1.ideDadoOrigem", "orig")
				.createAlias("localCaptacao", "lc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideLocalizacaoGeografica", "lg")
				.createAlias("lg.ideSistemaCoordenada", "sc")
				.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.INNER_JOIN)
				.add(Restrictions.eq("f2.ideFce", fce.getIdeFce()))
				.add(Restrictions.eq("orig.ideDadoOrigem", DadoOrigemEnum.TECNICO.getId()))
				.add(Restrictions.eq("doc.ideDocumentoObrigatorio", DocumentoObrigatorioEnum.FCE_LANCAMENTO_EFLUENTES.getId()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideFceOutorgaLocalizacaoGeografica"), "ideFceOutorgaLocalizacaoGeografica")
						.add(Projections.property("nomRio"), "nomRio")
						.add(Projections.property("numTempoCaptacao"), "numTempoCaptacao")
						.add(Projections.property("lg.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
						.add(Projections.property("sc.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
						.add(Projections.property("sc.srid"), "ideLocalizacaoGeografica.ideSistemaCoordenada.srid")
						.add(Projections.property("sc.nomSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.nomSistemaCoordenada")
						.add(Projections.property("f1.ideFce"), "ideFce.ideFce")
						.add(Projections.property("f2.ideDadoOrigem.ideDadoOrigem"), "ideFce.ideDadoOrigem.ideDadoOrigem")
						.add(Projections.property("ideTipologia.ideTipologia"), "ideTipologia.ideTipologia")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(FceOutorgaLocalizacaoGeografica.class)
								);
		return fceOutorgaLocalizacaoGeograficaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocTipoCaptacaoGeoByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoGeografica.class);
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		criteria.createAlias("ideFce", "fce");
		criteria.createAlias("ideOutorgaLocalizacaoGeografica","olg");
		criteria.createAlias("olg.ideTipoBarragem", "barragem", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("olg.ideLocalizacaoGeografica", "lg");
		criteria.createAlias("olg.ideOutorga", "o");
		criteria.createAlias("o.tipoCaptacaoCollection", "t");
		criteria.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("localCaptacao", "loc", JoinType.LEFT_OUTER_JOIN);
		List<FceOutorgaLocalizacaoGeografica> lista = fceOutorgaLocalizacaoGeograficaIDAO.listarPorCriteria(criteria);
		if(!Util.isNullOuVazio(lista)){
			for(FceOutorgaLocalizacaoGeografica fceLocalizacaoGeografica : lista){
				fceLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
				if(!Util.isNullOuVazio(fceLocalizacaoGeografica.getNomRio())){
					fceLocalizacaoGeografica.setConfirmado(true);
					fceLocalizacaoGeografica.setRioPreenchido(true);
				}
				fceLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeOutorga().setTipoCaptacao(fceLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeOutorga().getTipoCaptacaoCollection().iterator().next());
			}
		}
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceOutorgaLocGeo(FceOutorgaLocalizacaoGeografica fceOuorgaLocalizacaoGeografica) {
		fceOutorgaLocalizacaoGeograficaIDAO.salvarOuAtualizar(fceOuorgaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceOutorgaLocGeo(List<FceOutorgaLocalizacaoGeografica> listaFceOuorgaLocalizacaoGeografica) {
		for(FceOutorgaLocalizacaoGeografica folg : listaFceOuorgaLocalizacaoGeografica) {
			fceOutorgaLocalizacaoGeograficaIDAO.salvarOuAtualizar(folg);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceOutorgaLocalizacaoGeografica buscarFceOutorgaLocalizacaoGeograficaByOutorgaLocGeo(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica, DadoOrigem origemFce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoGeografica.class)
				.createAlias("ideFce","fce", JoinType.INNER_JOIN)
				.createAlias("ideOutorgaLocalizacaoGeografica","olg")
				.createAlias("olg.ideLocalizacaoGeografica", "lg")
				.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("olg.ideOutorgaLocalizacaoGeografica", outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica()))
				.add(Restrictions.eq("fce.ideDocumentoObrigatorio.ideDocumentoObrigatorio", DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId()))
				.add(Restrictions.eq("fce.ideDadoOrigem.ideDadoOrigem", origemFce.getIdeDadoOrigem()))
				.add(Restrictions.isNotNull("fce.ideFce"));
		return fceOutorgaLocalizacaoGeograficaIDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaBy(Requerimento requerimento, TipologiaEnum tipologiaEnum, TipoFinalidadeUsoAguaEnum finalidadeUsoAguaEnum) {
		List<FceOutorgaLocalizacaoGeograficaFinalidade> lista = fceOutorgaLocalizacaoGeograficaFinalidadeService.listarFceOutorgaLocGeoFinalidadeBy(requerimento, tipologiaEnum, finalidadeUsoAguaEnum);
		List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLocGeo = new ArrayList<FceOutorgaLocalizacaoGeografica>();
		for(FceOutorgaLocalizacaoGeograficaFinalidade fceOutorgaLocalizacaoGeograficaFinalidade : lista){
			listaFceOutorgaLocGeo.add(fceOutorgaLocalizacaoGeograficaFinalidade.getIdeFceOutorgaLocalizacaoGeografica());
		}
		return listaFceOutorgaLocGeo;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocalizacaoGeograficaBy(FceLancamentoEfluente fceLancamentoEfluente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoGeografica.class)
				.createAlias("ideLocalizacaoGeografica", "lg")
				.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideFceOutorgaLocalizacaoGeografica", fceLancamentoEfluente.getIdeFceOutorgaLocalizacaoGeografica().getIdeFceOutorgaLocalizacaoGeografica()));
		return fceOutorgaLocalizacaoGeograficaIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocGeoAnaliseTecnicaByIdeRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoGeografica.class)
				.createAlias("ideFce", "fce")
				.createAlias("localCaptacao", "lc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideLocalizacaoGeografica", "lg")
				.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.INNER_JOIN)
				.createAlias("listaReservaAgua", "lra", JoinType.LEFT_OUTER_JOIN)
				.createAlias("lra.ideStatusReservaAgua", "sra", JoinType.LEFT_OUTER_JOIN)
				;
		DetachedCriteria subCriteria = DetachedCriteria.forClass(ReservaAgua.class,"ra")
				.createAlias("ra.ideFceOutorgaLocalizacaoGeografica", "folgr", JoinType.LEFT_OUTER_JOIN)
				.createAlias("folgr.ideFce", "f2", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eqProperty("f2.ideFce", "fce.ideFce"))
				.setProjection(
						Projections.projectionList()
						.add(Projections.max("ra.dtcStatusReservaAgua"))
						);
		criteria.add(Restrictions.or(Subqueries.propertyEq("lra.dtcStatusReservaAgua", subCriteria),
					Restrictions.isNull("lra.reservaAguaPK")))
		.add(Restrictions.eq("fce.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()))
		.add(Restrictions.eq("fce.ideDadoOrigem.ideDadoOrigem", DadoOrigemEnum.TECNICO.getId()));
		
		List<FceOutorgaLocalizacaoGeografica> lista = fceOutorgaLocalizacaoGeograficaIDAO.listarPorCriteria(criteria);
		if(!Util.isNullOuVazio(lista)){
			for(FceOutorgaLocalizacaoGeografica fceLocalizacaoGeografica : lista){
				fceLocalizacaoGeografica.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
				if(!Util.isNullOuVazio(fceLocalizacaoGeografica.getNumTempoCaptacao()) || !Util.isNullOuVazio(fceLocalizacaoGeografica.getNomRio())){
					fceLocalizacaoGeografica.setConfirmado(true);
				}
			}
		}
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeografica> listarFceOutorgaLocGeoByModalidadeIdeRequerimento(ModalidadeOutorgaEnum modalidadeOutorgaEnum, Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaLocalizacaoGeografica.class);
		criteria.createAlias("ideOutorgaLocalizacaoGeografica","olg", JoinType.INNER_JOIN);
		criteria.createAlias("olg.ideLocalizacaoGeografica", "lg");
		criteria.createAlias("olg.ideOutorga", "o");
		criteria.createAlias("o.ideRequerimento", "r");
		criteria.createAlias("o.ideModalidadeOutorga", "mo");
		criteria.createAlias("lg.dadoGeograficoCollection", "dg");
		criteria.createAlias("ideFce", "fce", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("fce.ideAnaliseTecnica", "at", JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade()));
		criteria.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()));
		
		List<FceOutorgaLocalizacaoGeografica> lista = fceOutorgaLocalizacaoGeograficaIDAO.listarPorCriteria(criteria);
		if(!Util.isNullOuVazio(lista)){
			for(FceOutorgaLocalizacaoGeografica fceLocalizacaoGeografica : lista){
				fceLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
				if(!Util.isNullOuVazio(fceLocalizacaoGeografica.getNumTempoCaptacao()) || !Util.isNullOuVazio(fceLocalizacaoGeografica.getNomRio())){
					fceLocalizacaoGeografica.setConfirmado(true);
				}
				fceLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().setBaciaHidrografica(facadeLocalizacaoGeografica.getBacia(fceLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
				fceLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().setRpga(facadeLocalizacaoGeografica.getRPGA(fceLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
			}
		}
		return lista;
	}

}
