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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.EtaTipoComposicao;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaEta;
import br.gov.ba.seia.entity.FceSaaTipoComposicao;
import br.gov.ba.seia.entity.FceSesTipoComposicao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoComposicaoService {

	@Inject
	private IDAO<FceSaaTipoComposicao> tipoComposicaoIDAO;
	
	@Inject
	private IDAO<FceSesTipoComposicao> SesTipoComposicaoIDAO;
	
	@Inject
	private IDAO<EtaTipoComposicao> etaTipoComposicaoIDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaTipoComposicao> listaTipoComposicao() throws Exception{
		return tipoComposicaoIDAO.listarPorCriteria(
				DetachedCriteria.forClass(FceSaaTipoComposicao.class),
				Order.asc("ideFceSaaTipoComposicao"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSesTipoComposicao> listaSesTipoComposicao() throws Exception{
		return SesTipoComposicaoIDAO.listarPorCriteria(
				DetachedCriteria.forClass(FceSesTipoComposicao.class),
				Order.asc("ideFceSesTipoComposicao"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EtaTipoComposicao> listaTipoComposicaoByIdeLocalizacaoEta(FceSaaLocalizacaoGeograficaEta LocalizacaoEta) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EtaTipoComposicao.class);
		
		criteria.add(Restrictions.eq("ideFceSaaLocalizacaoGeograficaEta", LocalizacaoEta));
		
		return etaTipoComposicaoIDAO.listarPorCriteria(criteria, Order.asc("ideFceSaaLocalizacaoGeograficaEta"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipoComposicaoByIdeLocalizacaoEta(FceSaaLocalizacaoGeograficaEta localizacaoEta) throws Exception {

		String sql = "delete from EtaTipoComposicao  etc where etc.ideFceSaaLocalizacaoGeograficaEta.ideFceSaaLocalizacaoGeograficaEta= :ideFceSaaLocalizacaoGeograficaEta";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceSaaLocalizacaoGeograficaEta", localizacaoEta.getIdeFceSaaLocalizacaoGeograficaEta());
		
		etaTipoComposicaoIDAO.executarQuery(sql, params);
	}
	
	
}
