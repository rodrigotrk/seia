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
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.BaseOperacional;
import br.gov.ba.seia.entity.BaseOperacionalControleAmbiental;
import br.gov.ba.seia.entity.BaseOperacionalServico;
import br.gov.ba.seia.entity.LacTransporte;
import br.gov.ba.seia.entity.TipoControleAmbiental;
import br.gov.ba.seia.entity.TipoServicoBase;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BaseOperacionalService {
	
	@Inject
	private IDAO<TipoServicoBase> tipoServicoBaselDAO;
	@Inject
	private IDAO<BaseOperacional> baseOperacionallDAO;
	@Inject
	private IDAO<BaseOperacionalControleAmbiental> baseOperacionalControleAmbientallDAO;
	@Inject
	private IDAO<BaseOperacionalServico> baseOperacionalServicolDAO;
	@Inject
	private IDAO<TipoControleAmbiental> tipoControleAmbientallDAO;
	
	/*
	 * BASE OPERACIONAL
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarBaseOperacional(BaseOperacional baseOperacional) {
		baseOperacionallDAO.salvarOuAtualizar(baseOperacional);
	}
	/**
	 * @param baseOperacional
	 * @throws Exception
	 * @INFO Excluir base operacional passada por parametro do Banco de dados
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirBaseOperacional(BaseOperacional baseOperacional) {
		baseOperacionallDAO.remover(baseOperacional);
	}
	/**
	 * @param lacTransporte
	 * @throws Exception
	 * @INFO Excluir Base Operacional pelo id da Lac Transporte
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirBaseOperacional(LacTransporte lacTransporte) {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("ideLacTransporte", lacTransporte.getIdeLacTransporte());
		baseOperacionallDAO.executarNamedQuery("BaseOperacional.excluirByIdeLacTransporte", maps);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BaseOperacional buscarBaseOperacionalByIdeLacTransporte(LacTransporte lacTransporte){
		DetachedCriteria criteria = DetachedCriteria.forClass(BaseOperacional.class);
		criteria.add(Restrictions.eq("ideLacTransporte.ideLacTransporte", lacTransporte.getIdeLacTransporte()));
		return baseOperacionallDAO.buscarPorCriteria(criteria);
	}
	
	/*
	 * BASE OPERACIONAL CONTROLE AMBIENTAL
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarBaseOperacionalControleAmbiental(BaseOperacionalControleAmbiental baseOperacionalControleAmbiental) {
		baseOperacionalControleAmbientallDAO.salvar(baseOperacionalControleAmbiental);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BaseOperacionalControleAmbiental> listarBaseOperacionalControleAmbientalByIdBaseOperacional(BaseOperacional bO) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BaseOperacionalControleAmbiental.class);
		criteria.setFetchMode("ideTipoControleAmbiental", FetchMode.JOIN);
		criteria.add(Restrictions.eq("ideBaseOperacional", bO));
		return baseOperacionalControleAmbientallDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoControleAmbiental> listarTipoControleAmbiental() {
		return tipoControleAmbientallDAO.listarTodos();
	}
	/**
	 * 
	 * @param baseOperacional
	 * @throws Exception
	 * @INFO exclui todas os sistemas ligados a Base operacional a partir de seu ID (Base Operacional)
	 */
	public void excluirBaseOperacionalControleAmbientalByIdeBaseOperac(BaseOperacional baseOperacional) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideBaseOperacional", baseOperacional.getIdeBaseOperacional());
		baseOperacionalControleAmbientallDAO.executarNamedQuery("BaseOperacionalControleAmbiental.excluirByIdeBaseOperacional", params);
	}
	
	/*
	 * BASE OPERACIONAL TIPO SERVICO
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarBaseOperacionalServico(BaseOperacionalServico baseOperacionalServico) {
		baseOperacionalServicolDAO.salvar(baseOperacionalServico);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BaseOperacionalServico> listarBaseOperacionalServicoByIdBaseOperacional(BaseOperacional bO) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BaseOperacionalServico.class);
		criteria.setFetchMode("ideTipoServicoBase", FetchMode.JOIN);
		criteria.add(Restrictions.eq("ideBaseOperacional", bO));
		return baseOperacionalServicolDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoServicoBase> listarTipoBaseServico() {
		return tipoServicoBaselDAO.listarTodos();
	}
	/**
	 * 
	 * @param baseOperacional
	 * @throws Exception
	 * @INFO Exclui todos os serviços ligados a Base Operacional a partir de seu ID (Base Operacional).
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirBaseOperacionalServicoByIdeBaseOperac(BaseOperacional baseOperacional) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideBaseOperacional", baseOperacional.getIdeBaseOperacional());
		baseOperacionalServicolDAO.executarNamedQuery("BaseOperacionalServico.excluirByIdeBaseOperacional", params);
	}
}
