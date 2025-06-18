/**
 * 
 */
package br.gov.ba.seia.service;

import java.util.ArrayList;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Pergunta;

/**
 * @author mario.junior
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PerguntaService {

	@Inject
	IDAO<Pergunta> perguntaDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarPergunta(Pergunta pergunta) {
		perguntaDAO.salvarOuAtualizar(pergunta);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pergunta> listarPerguntasAtivasComLocGeo() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("indLocalizacaoGeografica", 0);
		param.put("indAtivo", true);
		return perguntaDAO.buscarPorNamedQuery("Pergunta.findByIndLocGeoByIndAtivo", param);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pergunta carregarPerguntaByIde(Integer idePergunta) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idePergunta", idePergunta);
		return perguntaDAO.obterPorNamedQuery("Pergunta.findByIdePergunta", param);
		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pergunta carregarPerguntabyCodPergunta(String codPergunta)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Pergunta.class);
		criteria.add(Restrictions.like("codPergunta", codPergunta, MatchMode.EXACT));
		return perguntaDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pergunta> carregarPerguntasNovoRequerimento(int aba)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Pergunta.class, "pergunta");

		if(aba == 0){
			criteria.add(Restrictions.like("codPergunta", "NR_A0", MatchMode.START));
		}
		else if(aba == 1){
			criteria.add(Restrictions.like("codPergunta", "NR_A1", MatchMode.START));
		}
		else if(aba == 2){
			criteria.add(Restrictions.like("codPergunta", "NR_A2", MatchMode.START));
		}
		else if(aba == 3){
			criteria.add(Restrictions.like("codPergunta", "NR_A3", MatchMode.START));
		}
		else if(aba == 4){
			criteria.add(Restrictions.like("codPergunta", "NR_A4", MatchMode.START));
		}
		else if(aba == 5){
			criteria.add(Restrictions.like("codPergunta", "NR_A5", MatchMode.START));
		}
		else if(aba == 6){
			criteria.add(Restrictions.like("codPergunta", "NR_A6", MatchMode.START));
		} 

		return perguntaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pergunta> carregarPerguntasPorDialog(String nomeDialog)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Pergunta.class, "pergunta");
		
		if(nomeDialog.equals("capSub")){
			criteria.add(Restrictions.like("codPergunta", "NR_A4_DCAPSUB", MatchMode.START));
		}
		else if(nomeDialog.equals("capSup")){
			criteria.add(Restrictions.like("codPergunta", "NR_A4_DCAPSUP", MatchMode.START));
		}
		else if(nomeDialog.equals("intervencao")){
			criteria.add(Restrictions.like("codPergunta", "NR_A4_DINTERV", MatchMode.START));
		}
		else if(nomeDialog.equals("lancEfluentes")){
			criteria.add(Restrictions.like("codPergunta", "NR_A4_DCHID", MatchMode.START));
		}
		
		return perguntaDAO.listarPorCriteria(criteria);
	}
}