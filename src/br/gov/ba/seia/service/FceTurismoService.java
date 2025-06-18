package br.gov.ba.seia.service;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceTurismo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceTurismoService {

	@Inject
	private IDAO<FceTurismo> fceTurismoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceTurismo buscarFceTurismoByIdeFce(Fce fce) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceTurismo.class, "turismo")
			.createAlias("turismo.ideDocumentoObrigatorioRequerimento", "doc", JoinType.LEFT_OUTER_JOIN)
			.createAlias("turismo.ideFce", "fce", JoinType.INNER_JOIN)
			.createAlias("fce.ideAnaliseTecnica", "at", JoinType.LEFT_OUTER_JOIN)
			.createAlias("at.ideProcesso", "proc", JoinType.LEFT_OUTER_JOIN)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("turismo.ideFceTurismo"), "ideFceTurismo")
				.add(Projections.property("turismo.dscLeiMunicipal"), "dscLeiMunicipal")
				.add(Projections.property("turismo.indPlanoDiretor"), "indPlanoDiretor")
				.add(Projections.property("turismo.indZonaExtensao"), "indZonaExtensao")
				.add(Projections.property("turismo.numAreaConstruida"), "numAreaConstruida")
				.add(Projections.property("turismo.numAreaApp"), "numAreaApp")
				
				.add(Projections.property("doc.ideDocumentoObrigatorioRequerimento"), "ideDocumentoObrigatorioRequerimento.ideDocumentoObrigatorioRequerimento")
				.add(Projections.property("doc.dscCaminhoArquivo"), "ideDocumentoObrigatorioRequerimento.dscCaminhoArquivo")
				.add(Projections.property("doc.indDocumentoValidado"), "ideDocumentoObrigatorioRequerimento.indDocumentoValidado")
				.add(Projections.property("doc.indSigiloso"), "ideDocumentoObrigatorioRequerimento.indSigiloso")
				.add(Projections.property("doc.dtcValidacao"), "ideDocumentoObrigatorioRequerimento.dtcValidacao")
				.add(Projections.property("doc.indDocumentoValidado"), "ideDocumentoObrigatorioRequerimento.indDocumentoValidado")
				
				.add(Projections.property("fce.ideFce"), "ideFce.ideFce")
				.add(Projections.property("fce.dtcCriacao"), "ideFce.dtcCriacao")
				.add(Projections.property("fce.indConcluido"), "ideFce.indConcluido")
				
				.add(Projections.property("at.ideAnaliseTecnica"), "ideFce.ideAnaliseTecnica.ideAnaliseTecnica")
				.add(Projections.property("at.indAprovado"), "ideFce.ideAnaliseTecnica.indAprovado")
				
				.add(Projections.property("proc.ideProcesso"), "ideFce.ideAnaliseTecnica.ideProcesso.ideProcesso")
				.add(Projections.property("proc.numProcesso"), "ideFce.ideAnaliseTecnica.ideProcesso.numProcesso")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(FceTurismo.class))
			
			.add(Restrictions.eq("fce.ideFce", fce.getIdeFce()));
		
		return fceTurismoIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceTurismo(FceTurismo fceTurismo) {
		fceTurismoIDAO.salvarOuAtualizar(fceTurismo);
	}
}