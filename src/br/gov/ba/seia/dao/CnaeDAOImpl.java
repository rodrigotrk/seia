package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Cnae;
import br.gov.ba.seia.entity.Tipologia;


public class CnaeDAOImpl {
	
	@Inject
	IDAO<Cnae> cnaeDAO;
	
	
	public Collection<Cnae> listarCnaeSecao() {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideNivelCnae", new Integer("1"));
		return cnaeDAO.buscarPorNamedQuery("Cnae.findNivel", parametros);
		
	}
	
	public Collection<Cnae> listarCnaePorPai(Cnae cnaeSecao) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideCnaePai", cnaeSecao.getIdeCnae());
		return cnaeDAO.buscarPorNamedQuery("Cnae.findByPai", parametros);
	}
	
	public Cnae obterCnaePaiPorCnae(Cnae cnae) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideCnae", cnae.getIdeCnaePai().getIdeCnae());
		return cnaeDAO.buscarEntidadePorNamedQuery("Cnae.findByIdeCnae", parametros);

	}
	
	public void salvarCnae(Cnae cnae)  {		
		cnaeDAO.salvar(cnae);
	}
	
	public void atualizarCnae(Cnae cnae){		
		cnaeDAO.atualizar(cnae);
	}
	
	
	public Cnae carregarCnae(Integer pIdeCnae){
		return cnaeDAO.carregarGet(pIdeCnae);
	}
	
	public void excluirTipologiaCnae(Cnae cnae, Tipologia tipologia) {
		String deleteSQL = "delete from tipologia_cnae where ide_tipologia = :ideTipologia and ide_cnae = :ideCnae";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideTipologia", tipologia.getIdeTipologia());
		params.put("ideCnae", cnae.getIdeCnae());
		cnaeDAO.executarNativeQuery(deleteSQL, params);
	}

	public Cnae carregarCnae(String codCnae)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Cnae.class);
		criteria.add(Restrictions.eq("codCnae",codCnae));
		return this.cnaeDAO.buscarPorCriteria(criteria);
	}

}