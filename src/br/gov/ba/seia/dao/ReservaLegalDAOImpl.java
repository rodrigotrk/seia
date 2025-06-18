package br.gov.ba.seia.dao;


import java.util.Collection;
import java.util.HashMap;
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReservaLegalDAOImpl {

	@Inject
	IDAO<ReservaLegal> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaLegal buscarReservaLegal(Integer ideReservaLegal) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReservaLegal.class);
		criteria
			.createAlias("ideImovelRural", "ir",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideLocalizacaoGeografica", "lg",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipoArl", "tarl",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipoEstadoConservacao", "tec",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipoOrigemCertificado", "toc",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideStatus", "st",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideUsuarioAprovacao", "u",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideDocumentoAprovacao", "da",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideReservaLegalAverbada", "rla",JoinType.LEFT_OUTER_JOIN)
			.createAlias("cronogramaRecuperacao", "cr",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideDadoOrigem", "do",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideNotificacao", "n",JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideProcessoAto", "pa",JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("ideReservaLegal", ideReservaLegal))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideReservaLegal"),"ideReservaLegal")
				.add(Projections.property("valArea"),"valArea")
				.add(Projections.property("indProcessoTramite"),"indProcessoTramite")
				.add(Projections.property("numProcesso"),"numProcesso")
				.add(Projections.property("observacao"),"observacao")
				.add(Projections.property("dtcAprovacao"),"dtcAprovacao")
				.add(Projections.property("numSicarCompensacao"),"numSicarCompensacao")
				.add(Projections.property("dtcAprovacaoDeclarada"),"dtcAprovacaoDeclarada")
				.add(Projections.property("indSobreposicaoApp"),"indSobreposicaoApp")
				.add(Projections.property("indSobreposicaoAp"),"indSobreposicaoAp")
				.add(Projections.property("indMenorVintePorcento"),"indMenorVintePorcento")
				.add(Projections.property("indAverbada"),"indAverbada")
				.add(Projections.property("ir.ideImovelRural"),"ideImovelRural.ideImovelRural")
				.add(Projections.property("lg.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("tarl.ideTipoArl"),"ideTipoArl.ideTipoArl")
				.add(Projections.property("tec.ideTipoEstadoConservacao"),"ideTipoEstadoConservacao.ideTipoEstadoConservacao")
				.add(Projections.property("toc.ideTipoOrigemCertificado"),"ideTipoOrigemCertificado.ideTipoOrigemCertificado")
				.add(Projections.property("st.ideStatusReservaLegal"),"ideStatus.ideStatusReservaLegal")
				.add(Projections.property("u.idePessoaFisica"),"ideUsuarioAprovacao.idePessoaFisica")
				.add(Projections.property("da.ideDocumentoImovelRural"),"ideDocumentoAprovacao.ideDocumentoImovelRural")
				.add(Projections.property("rla.ideReservaLegalAverbada"),"ideReservaLegalAverbada.ideReservaLegalAverbada")
				.add(Projections.property("cr.ideCronogramaRecuperacao"),"cronogramaRecuperacao.ideCronogramaRecuperacao")
				.add(Projections.property("do.ideDadoOrigem"),"ideDadoOrigem.ideDadoOrigem")
				.add(Projections.property("n.ideNotificacao"),"ideNotificacao.ideNotificacao")
				.add(Projections.property("pa.ideProcessoAto"),"ideProcessoAto.ideProcessoAto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReservaLegal.class))
		;
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ReservaLegal> listarReservaLegalConcedida(Integer ideProcessoAto)  {
		DetachedCriteria criteria = getCriteriaReservaLegalConcedida(ideProcessoAto);
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaLegal buscarReservaLegalConcedida(Integer ideImovel, Integer ideProcessoAto)  {
		DetachedCriteria criteria = getCriteriaReservaLegalConcedida(ideProcessoAto);
		criteria.add(Restrictions.eq("ir.ideImovelRural", ideImovel));
		return dao.buscarPorCriteria(criteria);
	}
	
	private DetachedCriteria getCriteriaReservaLegalConcedida(Integer ideProcessoAto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReservaLegal.class);
		criteria
			
			.createAlias("ideReservaLegalPai", "rlp", JoinType.INNER_JOIN)
			.createAlias("rlp.ideImovelRural", "ir", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("ideDadoOrigem", "do", JoinType.INNER_JOIN)
			.createAlias("ideTipoArl", "tarl", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideFormaRealocacaoRl", "frrl", JoinType.INNER_JOIN)
			.createAlias("ideTipoEstadoConservacao", "tec", JoinType.INNER_JOIN)
			.createAlias("ideNotificacao", "n", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideProcessoAto", "pa", JoinType.INNER_JOIN)
			.createAlias("ideStatus", "st", JoinType.INNER_JOIN)
			.createAlias("ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
			.createAlias("lg.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("pa.ideProcessoAto", ideProcessoAto))
			.add(Restrictions.eq("do.ideDadoOrigem", DadoOrigemEnum.TECNICO.getId()))
			.add(Restrictions.isNull("indExcluido"))
			
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideReservaLegal"),"ideReservaLegal")
				.add(Projections.groupProperty("valArea"),"valArea")
				.add(Projections.groupProperty("indProcessoTramite"),"indProcessoTramite")
				.add(Projections.groupProperty("numProcesso"),"numProcesso")
				.add(Projections.groupProperty("numCertificado"),"numCertificado")
				
				.add(Projections.groupProperty("rlp.ideReservaLegal"),"ideReservaLegalPai.ideReservaLegal")
				.add(Projections.groupProperty("ir.ideImovelRural"),"ideReservaLegalPai.ideImovelRural.ideImovelRural")
				
				.add(Projections.groupProperty("tarl.ideTipoArl"),"ideTipoArl.ideTipoArl")
				.add(Projections.groupProperty("frrl.ideFormaRealocacaoRl"),"ideFormaRealocacaoRl.ideFormaRealocacaoRl")
				.add(Projections.groupProperty("tec.ideTipoEstadoConservacao"),"ideTipoEstadoConservacao.ideTipoEstadoConservacao")
				.add(Projections.groupProperty("do.ideDadoOrigem"),"ideDadoOrigem.ideDadoOrigem")
				.add(Projections.groupProperty("lg.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.groupProperty("sc.ideSistemaCoordenada"),"ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
				.add(Projections.groupProperty("sc.nomSistemaCoordenada"),"ideLocalizacaoGeografica.ideSistemaCoordenada.nomSistemaCoordenada")
				.add(Projections.groupProperty("n.ideNotificacao"),"ideNotificacao.ideNotificacao")
				.add(Projections.groupProperty("n.numNotificacao"),"ideNotificacao.numNotificacao")
				.add(Projections.groupProperty("pa.ideProcessoAto"),"ideProcessoAto.ideProcessoAto")
				.add(Projections.groupProperty("st.ideStatusReservaLegal"),"ideStatus.ideStatusReservaLegal")
				.add(Projections.sqlGroupProjection("(select true) as ind_concedido_", "ind_concedido_", new String[] {"ind_concedido_"}, new Type[] {StandardBasicTypes.BOOLEAN}),"indConcedido")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReservaLegal.class))
		;
		return criteria;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaLegal buscarReservaLegalPor(ImovelRural imovelRural)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReservaLegal.class);
		criteria
			.add(Restrictions.eq("ideImovelRural", imovelRural))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideReservaLegal"),"ideReservaLegal")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReservaLegal.class))
		;
		
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaLegal filtrarById(ReservaLegal pReservaLegal) {
		return dao.buscarEntidadePorExemplo(pReservaLegal);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReservaLegal pReservaLegal)  {
		dao.salvar(pReservaLegal);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ReservaLegal pReservaLegal)  {
		dao.salvarOuAtualizar(pReservaLegal);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ReservaLegal pReservaLegal)  {
		dao.atualizar(pReservaLegal);
	}
	
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<String> verificaProcessoCerberus(String processo, String certificado) {
		List<String> listaProcesso = null;
		
		StringBuilder lSql = new StringBuilder();		
		lSql.append("SELECT gafc.* ");
		lSql.append("FROM processo_externo pe ");
		lSql.append("JOIN geobahia_admin.adm_florestal gaf ON gaf.noprocflorestal=pe.processo ");
		lSql.append("JOIN geobahia_admin.adm_florestal_certificados gafc ON gafc.idflorestal=gaf.idflorestal ");
		lSql.append("WHERE pe.processo = '" + processo + "' AND pe.status = 'ConcluÃ­do' AND gafc.num_certificado = '" + certificado + "' AND pe.processo LIKE '%ARL%' ");
				
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());
		listaProcesso  = lQuery.getResultList();
				
		return listaProcesso;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerTudo(ReservaLegal pReservaLegal) {
		StringBuilder deleteSQL = null;
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideReservaLegal", pReservaLegal.getIdeReservaLegal());
		
		deleteSQL = new StringBuilder();
		deleteSQL.append("delete from reserva_legal_averbada where ide_reserva_legal = :ideReservaLegal");
		dao.executarNativeQuery(deleteSQL.toString(), params);	
		
		deleteSQL = new StringBuilder();
		deleteSQL.append("delete from reserva_legal where ide_reserva_legal = :ideReservaLegal");
		dao.executarNativeQuery(deleteSQL.toString(), params);	
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ReservaLegal pReservaLegal)  {
		String deleteSQL = "delete from reserva_legal where ide_reserva_legal = :ideReservaLegal";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideReservaLegal", pReservaLegal.getIdeReservaLegal());
		dao.executarNativeQuery(deleteSQL, params);	
	}

}