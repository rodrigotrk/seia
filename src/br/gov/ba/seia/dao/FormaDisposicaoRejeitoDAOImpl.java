package br.gov.ba.seia.dao;

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

import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FormaDisposicaoRejeito;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FormaDisposicaoRejeitoDAOImpl {

	@Inject
	private IDAO<FormaDisposicaoRejeito> iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFormaDisposicaoRejeito(FormaDisposicaoRejeito formaDisposicaoRejeito)  {
		iDAO.salvarOuAtualizar(formaDisposicaoRejeito);
	}

	/**
	 * Método que retorna a lista de {@link FormaDisposicaoRejeito} através do
	 * {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param licenciamentoMineral
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FormaDisposicaoRejeito> listarFormaDisposicaoRejeitoBy(FceLicenciamentoMineral licenciamentoMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FormaDisposicaoRejeito.class)
				.createAlias("tipoResiduoGerado", "residuo")
				.createAlias("classificacaoRejeitoDnpm", "rejeito")
				.createAlias("tipoEstrutura", "estrutura")
				.createAlias("fceLicenciamentoMineral", "flm")
				.add(Restrictions.eq("flm.ideFceLicenciamentoMineral", licenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideFormaDisposicaoRejeito"),"ideFormaDisposicaoRejeito")
						.add(Projections.property("areaOcupada"), "areaOcupada")
						.add(Projections.property("indSistemaImpermeabilizacao"), "indSistemaImpermeabilizacao")
						.add(Projections.property("flm.ideFceLicenciamentoMineral"), "fceLicenciamentoMineral.ideFceLicenciamentoMineral")
						.add(Projections.property("residuo.ideTipoResiduoGerado"), "tipoResiduoGerado.ideTipoResiduoGerado")
						.add(Projections.property("residuo.dscTipoResiduoGerado"), "tipoResiduoGerado.dscTipoResiduoGerado")
						.add(Projections.property("rejeito.ideClassificacaoRejeitoDnpm"), "classificacaoRejeitoDnpm.ideClassificacaoRejeitoDnpm")
						.add(Projections.property("rejeito.desClassificacaoRejeitoDnpm"), "classificacaoRejeitoDnpm.desClassificacaoRejeitoDnpm")
						.add(Projections.property("estrutura.ideTipoEstrutura"), "tipoEstrutura.ideTipoEstrutura")
						.add(Projections.property("estrutura.nomTipoEstrutura"), "tipoEstrutura.nomTipoEstrutura")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(FormaDisposicaoRejeito.class));
		return iDAO.listarPorCriteria(criteria);
	}

	/**
	 *
	 * @author eduardo.fernandes
	 * @since 27/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a>
	 * @param fceLicenciamentoMineral
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral());
		iDAO.executarNamedQuery("FormaDisposicaoRejeito.removeByIdeFceLicenciamentoMineral", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(FormaDisposicaoRejeito disposicaoRejeito)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFormaDisposicaoRejeito", disposicaoRejeito.getIdeFormaDisposicaoRejeito());
		iDAO.executarNamedQuery("FormaDisposicaoRejeito.removeByIdeFormaDisposicaoRejeito", parameters);
	}
}
