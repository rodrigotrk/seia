package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PerguntaRequerimentoDaoImpl {
	
	@Inject
	private IDAO<PerguntaRequerimento> perguntaRequerimentoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PerguntaRequerimento> listarPorRequerimentoECodPerguntaEIndResposta(Integer ideRequerimento, String codPergunta, boolean indResposta) {
		 
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class)
			.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("idePergunta", "pg", JoinType.INNER_JOIN)
			.createAlias("ideLicenca", "lic", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideOutorgaLocalizacaoGeografica", "outLocGeo", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideLocalizacaoGeografica", "locGeo", JoinType.LEFT_OUTER_JOIN)
			.createAlias("locGeo.ideSistemaCoordenada", "sisCoord", JoinType.LEFT_OUTER_JOIN)
			.createAlias("locGeo.ideClassificacaoSecao", "classSecao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideImovel", "imv", JoinType.INNER_JOIN)
			.createAlias("imv.imovelRural", "imvRural", JoinType.INNER_JOIN)
			 
			.setProjection
				(Projections.projectionList()
					.add(Projections.property("idePerguntaRequerimento"), "idePerguntaRequerimento")
					.add(Projections.property("indResposta"), "indResposta")
					.add(Projections.property("dtcResposta"), "dtcResposta")
					.add(Projections.property("indExcluido"), "indExcluido")
					
					.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
					.add(Projections.property("pg.idePergunta"), "idePergunta.idePergunta")
					.add(Projections.property("lic.ideLicenca"), "ideLicenca.ideLicenca")
					
					.add(Projections.property("outLocGeo.ideOutorgaLocalizacaoGeografica"), "ideOutorgaLocalizacaoGeografica.ideOutorgaLocalizacaoGeografica")
					.add(Projections.property("locGeo.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
					.add(Projections.property("sisCoord.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
					.add(Projections.property("sisCoord.nomSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.nomSistemaCoordenada")
					.add(Projections.property("classSecao.ideClassificacaoSecao"), "ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")
					.add(Projections.property("classSecao.nomClassificacaoSecao"), "ideLocalizacaoGeografica.ideClassificacaoSecao.nomClassificacaoSecao")
					
					.add(Projections.property("imv.ideImovel"), "ideImovel.ideImovel")
					.add(Projections.property("imvRural.ideImovelRural"), "ideImovel.imovelRural.ideImovelRural")
					.add(Projections.property("imvRural.desDenominacao"), "ideImovel.imovelRural.desDenominacao")
				)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(PerguntaRequerimento.class))
			
			.add(Restrictions.like("pg.codPergunta", codPergunta, MatchMode.EXACT))
			.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
			.add(Restrictions.eq("indResposta", indResposta));
			 
		 return perguntaRequerimentoDAO.listarPorCriteria(criteria);
	}
}