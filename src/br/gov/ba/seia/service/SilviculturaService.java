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

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceAgroTipoConservacao;
import br.gov.ba.seia.entity.FceAgroTipoConservacaoPK;
import br.gov.ba.seia.entity.FceAgrossilvopastoril;
import br.gov.ba.seia.entity.Silvicultura;
import br.gov.ba.seia.entity.TipoConservacaoSoloAgua;
import br.gov.ba.seia.enumerator.TipoDadoSilviculturaEnum;
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SilviculturaService {
	
	@Inject
	private IDAO<Silvicultura> silviculturaIDAO;
	@Inject
	
	private IDAO<FceAgroTipoConservacao> fceAgroTipoConservacaoIDAO;
	
	
	/**
	 * @param silvicultura
	 * @throws Exception
	 * @INFO Salva ou atualiza a silvicultura passada
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarSilvicultura(Silvicultura silvicultura, FceAgrossilvopastoril fceAgrossilvopastoril) throws Exception{
		silvicultura.setIdeFceAgrossilvopastoril(fceAgrossilvopastoril);
		silviculturaIDAO.salvarOuAtualizar(silvicultura);
	}
	/**
	 * @param silvicultura
	 * @throws Exception
	 * @INFO Salva ou atualiza a lista de silvicultura passada
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarListaSilvicultura(List<Silvicultura> listaSilvi, FceAgrossilvopastoril fceAgrossilvopastoril) throws Exception{
		for (Silvicultura silvicultura : listaSilvi) {
			silvicultura.setIdeFceAgrossilvopastoril(fceAgrossilvopastoril);
			silvicultura.setIdeSilvicultura(null);
			silviculturaIDAO.salvarOuAtualizar(silvicultura);
		}
	}
	/**
	 * @param fceAgrossilvopastoril
	 * @return List<FceAgroTipoConservacao>
	 * @throws Exception
	 * @INFO Retorna uma lista com as Silvicultura, apartir dos ID's de FceAgrossilvopastoril e TipoDadoSilvicultura
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Silvicultura> buscarSilviculturaByIdAgrossilvoAndTipoDado(FceAgrossilvopastoril fceAgrossilvopastoril, TipoDadoSilviculturaEnum ideTipoDadoSilvicultura) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(Silvicultura.class);
		criteria.add(Restrictions.eq("ideFceAgrossilvopastoril.ideFceAgrossilvopastoril", fceAgrossilvopastoril.getIdeFceAgrossilvopastoril()));
		criteria.add(Restrictions.eq("ideTipoDadoSilvicultura.ideTipoDadoSilvicultura", ideTipoDadoSilvicultura.getId()));
		criteria.setFetchMode("ideEspecieHibrido", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoDadoSilvicultura", FetchMode.JOIN);
		criteria.setFetchMode("dtcMesPrevisaoCorte", FetchMode.JOIN);
		criteria.setFetchMode("dtcMesPrevisaoPlantio", FetchMode.JOIN);
		return silviculturaIDAO.listarPorCriteria(criteria,Order.desc("indProjeto"));
	}
	/**
	 * @param FceAgrossilvopastoril
	 * @throws Exception
	 * @INFO Exclui todas as silviculturas pelo ID de ideFceAgrossilvopastoril
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerSilviculturaByIdeFceAgrossilvopastoril(FceAgrossilvopastoril ideFceAgrossilvopastoril) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideFceAgrossilvopastoril", ideFceAgrossilvopastoril.getIdeFceAgrossilvopastoril());
		silviculturaIDAO.executarNamedQuery("Silvicultura.deleteByideFceAgrossilvopastoril", params);
	}
	
	/**
	 * @param tipoConservacaoSoloAgua
	 * @throws Exception
	 * @INFO Salva os "TipoConservacaoSoloAgua" selecionados pelo usua´rio na aba de Silvicultura
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTipoConservacaoSoloAgua(FceAgroTipoConservacao fceAgroTipoConservacao) throws Exception{
		fceAgroTipoConservacaoIDAO.salvarOuAtualizar(fceAgroTipoConservacao);
	}
	/**
	 * @param tipoConservacaoSoloAgua
	 * @throws Exception
	 * @INFO Salva os "TipoConservacaoSoloAgua" selecionados pelo usua´rio na aba de Silvicultura
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTipoConservacaoSoloAgua(List<TipoConservacaoSoloAgua> listaTipoConservacaoSoloAgua, FceAgrossilvopastoril fceAgrossilvopastoril) throws Exception{
		FceAgroTipoConservacao fceAgroTipoConservacao;
		for (TipoConservacaoSoloAgua tipoConservacaoSoloAgua : listaTipoConservacaoSoloAgua) {
			fceAgroTipoConservacao = new FceAgroTipoConservacao();
			fceAgroTipoConservacao.setId(new FceAgroTipoConservacaoPK(fceAgrossilvopastoril.getIdeFceAgrossilvopastoril(), tipoConservacaoSoloAgua.getIdeTipoConservacaoSoloAgua()));
			fceAgroTipoConservacao.setIdeFceAgrossilvopastoril(fceAgrossilvopastoril);
			fceAgroTipoConservacao.setIdeTipoConservacaoSoloAgua(tipoConservacaoSoloAgua);
			fceAgroTipoConservacaoIDAO.salvarOuAtualizar(fceAgroTipoConservacao);
		}
	}
	/**
	 * @param fceAgrossilvopastoril
	 * @return List<FceAgroTipoConservacao>
	 * @throws Exception
	 * @INFO Retorna uma lista com as TipoConservacaoSoloAgua selecionadas pelo usuário
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAgroTipoConservacao> buscarTipoConservacaoSoloAguaByIdAgrossilvo(FceAgrossilvopastoril fceAgrossilvopastoril) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAgroTipoConservacao.class);
		criteria.add(Restrictions.eq("ideFceAgrossilvopastoril.ideFceAgrossilvopastoril", fceAgrossilvopastoril.getIdeFceAgrossilvopastoril()));
		criteria.setFetchMode("ideTipoConservacaoSoloAgua", FetchMode.JOIN);
		return fceAgroTipoConservacaoIDAO.listarPorCriteria(criteria);
	}
	/**
	 * @param FceAgrossilvopastoril
	 * @throws Exception
	 * @INFO Exclui todas as FceAgroTipoConservacao pelo ID de ideFceAgrossilvopastoril
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceAgroTipoConservacaoByIdeFceAgrossilvopastoril(FceAgrossilvopastoril ideFceAgrossilvopastoril) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideFceAgrossilvopastoril", ideFceAgrossilvopastoril.getIdeFceAgrossilvopastoril());
		fceAgroTipoConservacaoIDAO.executarNamedQuery("FceAgroTipoConservacao.deleteByideFceAgrossilvopastoril", params);
	}
	
}
