/**
 * 		04/10/13
 * @author eduardo.fernandes
 */
package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.controller.FceBarragemController;
import br.gov.ba.seia.controller.FceIntervencaoBarragemController;
import br.gov.ba.seia.controller.FceIntervencaoBarragemControllerSemPonto;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceIntervencaoBarragem;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoBarragem;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIntervencaoBarragemService {

	@Inject
	private IDAO<TipoBarragem> barragemDAO;
	@Inject
	private IDAO<FceIntervencaoBarragem> fceIntervencaoBarragemDAO;

	/**
	 * Método usado para listar os Tipos de Barragem ativos.
	 * @return List<TipoBarragem>
	 * @
	 * @author eduardo.fernandes
	 * @deprecated
	 */
	@Deprecated
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoBarragem> listarTipoBarragemByIndAtivo() {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoBarragem.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return barragemDAO.listarPorCriteria(criteria);
	}

	/**
	 * Método usado para salvar ou atualizar uma FceIntervencaoBarragem.
	 * @param fceIntervencaoBarragem
	 * @
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceIntervencaoBarragem (FceIntervencaoBarragem fceIntervencaoBarragem) {
		fceIntervencaoBarragemDAO.salvarOuAtualizar(fceIntervencaoBarragem);
	}

	/**
	 * Método usado para buscar a FceIntervencaoBarragem quando for edição.
	 * @return FceIntervencaoBarragem
	 * @
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceIntervencaoBarragem> listarFceIntervencaoBarragemByIdeFce (Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIntervencaoBarragem.class)
		.createAlias("ideDocumentoObrigatorioRequerimento", "doc", JoinType.LEFT_OUTER_JOIN);
		if(fce.getIdeDadoOrigem().getIdeDadoOrigem().equals(DadoOrigemEnum.REQUERIMENTO.getId())){
			criteria.createAlias("ideOutorgaLocalizacaoGeografica", "olg", JoinType.INNER_JOIN)
			.createAlias("olg.ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
			.createAlias("olg.ideTipoBarragem", "tb", JoinType.INNER_JOIN)
			.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
			List<FceIntervencaoBarragem> lista = fceIntervencaoBarragemDAO.listarPorCriteria(criteria);
			for(FceIntervencaoBarragem barragem : lista){
				barragem.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
				barragem.setIdeFce(fce);
			}
			return lista;
		}
		else {
			criteria.createAlias("ideTipoBarragem", "tb", JoinType.INNER_JOIN)
			.createAlias("ideFceOutorgaLocalizacaoGeografica", "folg", JoinType.INNER_JOIN)
			.createAlias("folg.ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
			.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
			List<FceIntervencaoBarragem> lista = fceIntervencaoBarragemDAO.listarPorCriteria(criteria);
			for(FceIntervencaoBarragem barragem : lista){
				barragem.getIdeFceOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
				barragem.setIdeFce(fce);
			}
			return lista;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceIntervencaoBarragem buscarFceIntervencaoBarragemByIdeOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocGeoSelecionada) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIntervencaoBarragem.class);
		criteria.createAlias("ideDocumentoObrigatorioRequerimento", "doc", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideOutorgaLocalizacaoGeografica", outorgaLocGeoSelecionada));
		return fceIntervencaoBarragemDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceIntervencaoBarragem buscarFceIntervencaoBarragemByIdeOutorgaLocalizacaoGeograficaFceBarragem(OutorgaLocalizacaoGeografica outorgaLocGeoSelecionada) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIntervencaoBarragem.class);
		criteria.createAlias("ideDocumentoObrigatorioRequerimento", "doc", JoinType.LEFT_OUTER_JOIN);
		
		criteria.createAlias("ideOutorgaLocalizacaoGeografica", "olg", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("fceBarragem", "fba", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideFce", "fce", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideFceOutorgaLocalizacaoGeografica", "oflg", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("ideOutorgaLocalizacaoGeografica", outorgaLocGeoSelecionada));
		return fceIntervencaoBarragemDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceIntervencaoBarragem buscarFceIntervencaoBarragemByIde(FceIntervencaoBarragem fceIntervencaoBarragem) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIntervencaoBarragem.class)
				.createAlias("ideDocumentoObrigatorioRequerimento", "doc", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideFceIntervencaoBarragem", fceIntervencaoBarragem.getIdeFceIntervencaoBarragem()));
		return fceIntervencaoBarragemDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceIntervencaoBarragem(FceIntervencaoBarragem fceIntervencaoBarragem)  {
		fceIntervencaoBarragemDAO.remover(fceIntervencaoBarragem);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceIntervencaoBarragemController ctrl) throws Exception  {
		ctrl.prepararParaFinalizar();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarSemPonto(FceIntervencaoBarragemControllerSemPonto ctrl) throws Exception  {
		ctrl.prepararParaFinalizar();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(List<FceIntervencaoBarragem> lista) {
		fceIntervencaoBarragemDAO.salvarEmLote(lista);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(FceIntervencaoBarragem fceIntervencaoBarragem) {
		fceIntervencaoBarragemDAO.salvarSemFlush(fceIntervencaoBarragem);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSemFlush(FceIntervencaoBarragem fceIntervencaoBarragem) {
		fceIntervencaoBarragemDAO.salvarSemFlush(fceIntervencaoBarragem);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDialogFceBarragemRegularizacao(FceIntervencaoBarragemController ctrl, FceBarragemController fceBarragemController) {
		ctrl.salvarCaracterizacaoBarragem();
		ctrl.fecharDialogBarragemRegularizacao();
		
		try {
			if (Util.isNullOuVazio(fceBarragemController.getFceBarragem().getFceIntervencaoBarragems())){
				fceBarragemController.getFceBarragem().setFceIntervencaoBarragems(new ArrayList<FceIntervencaoBarragem>());
				fceBarragemController.getFceBarragem().getFceIntervencaoBarragems().add(ctrl.getIntervencaoBarragem());
			} 
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	}
}