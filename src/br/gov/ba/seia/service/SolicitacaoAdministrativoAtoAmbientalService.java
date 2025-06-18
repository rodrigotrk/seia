package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.SolicitacaoAdministrativoAtoAmbiental;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SolicitacaoAdministrativoAtoAmbientalService {

	@Inject
	private IDAO<SolicitacaoAdministrativoAtoAmbiental> solicitacaoAdministrativoAtoAmbientalIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarSolicitacaoAdministrativaAtoAmbiental(SolicitacaoAdministrativoAtoAmbiental solAdmAto)	 {
		solicitacaoAdministrativoAtoAmbientalIDAO.salvarOuAtualizar(solAdmAto);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSolicitacaoAdministrativaAtoAmbientalbySolicitacaoAdm(SolicitacaoAdministrativo solAdm) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideSolicitacaoAdministrativo", solAdm);
		solicitacaoAdministrativoAtoAmbientalIDAO.executarNamedQuery(
				"SolicitacaoAdministrativoAtoAmbiental.removedByIdeSolicitacao", parametros);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SolicitacaoAdministrativoAtoAmbiental> listaAtoBySolicitacaoComTipologia(SolicitacaoAdministrativo sa) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(SolicitacaoAdministrativoAtoAmbiental.class)
			.createAlias("ideAtoAmbiental", "aa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipologia", "tipo", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideSolicitacaoAdministrativo", "sa", JoinType.LEFT_OUTER_JOIN)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideSolicitacaoAtoAdminstrativoAtoAmbiental"), "ideSolicitacaoAtoAdminstrativoAtoAmbiental")
				
				.add(Projections.property("aa.ideAtoAmbiental"), "ideAtoAmbiental.ideAtoAmbiental")
				.add(Projections.property("aa.sglAtoAmbiental"), "ideAtoAmbiental.sglAtoAmbiental")
				.add(Projections.property("aa.nomAtoAmbiental"), "ideAtoAmbiental.nomAtoAmbiental")
				.add(Projections.property("aa.indDeclaratorio"), "ideAtoAmbiental.indDeclaratorio")
				.add(Projections.property("aa.numDiasValidade"), "ideAtoAmbiental.numDiasValidade")
				.add(Projections.property("aa.indAtivo"), "ideAtoAmbiental.indAtivo")
				.add(Projections.property("aa.indVisivelSolicitacaoTla"), "ideAtoAmbiental.indVisivelSolicitacaoTla")
				.add(Projections.property("aa.indAutomatico"), "ideAtoAmbiental.indAutomatico")
				
				.add(Projections.property("tipo.ideTipologia"), "ideTipologia.ideTipologia")
				.add(Projections.property("tipo.codTipologia"), "ideTipologia.codTipologia")
				.add(Projections.property("tipo.desTipologia"), "ideTipologia.desTipologia")
				.add(Projections.property("tipo.dtcCriacao"), "ideTipologia.dtcCriacao")
				.add(Projections.property("tipo.dtcExclusao"), "ideTipologia.dtcExclusao")
				.add(Projections.property("tipo.indExcluido"), "ideTipologia.indExcluido")
				.add(Projections.property("tipo.indAutorizacao"), "ideTipologia.indAutorizacao")
				.add(Projections.property("tipo.indPossuiFilhos"), "ideTipologia.indPossuiFilhos")
				
				.add(Projections.property("sa.ideSolicitacaoAdministrativo"), "ideSolicitacaoAdministrativo.ideSolicitacaoAdministrativo")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(SolicitacaoAdministrativoAtoAmbiental.class))
			
			.add(Restrictions.eq("sa.ideSolicitacaoAdministrativo", sa.getIdeSolicitacaoAdministrativo()));
			
		 return solicitacaoAdministrativoAtoAmbientalIDAO.listarPorCriteria(criteria);
	}
}