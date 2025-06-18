package br.gov.ba.seia.dao;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ControleCronograma;
import br.gov.ba.seia.entity.Cronograma;
import br.gov.ba.seia.enumerator.ItemCronogramaEnum;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.util.Util;

public class ControleCronogramaDAOImpl {
	
	@Inject
	IDAO<ControleCronograma> controleCronogramaDAO;
	
	/**
	 * Retorna o controle de cronograma de conclusão de parecer que faz parte do cronograma informado no pametro.
	 * @param cronograma
	 * @return ControleCronograma
	 * @throws Exception 
	 * @author micael.coutinho
	 */
    public ControleCronograma getControleCronogramaConclusaoParecerByCronograma(Cronograma cronograma) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(ControleCronograma.class, "controleCronograma" );
		criteria.createAlias("ideItemCronograma", "itemCronograma", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("controleCronograma.ideCronograma.ideCronograma", cronograma.getIdeCronograma()));
		criteria.add(Restrictions.eq("controleCronograma.indExcluido", Boolean.FALSE));
		criteria.add(Restrictions.eq("ideItemCronograma.ideItemCronograma", ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde()));
		
		return controleCronogramaDAO.buscarPorCriteria(criteria);		
		
    }
    
    /**
     * Adcionar uma atividade ao cronograma do processo. Ou seja...
	 * Adiciona um crotroleCronograma ao cronograma, verificando se o cronograma já tem conclusão de parecer e se já tiver verifica se o itemCronograma do 
     * @param cronograma
     * @param controlCrono
     * @throws Exception 
	 * @author micael.coutinho
     */
    public void adcionarControleCronogramaAoCronograma(Cronograma cronograma, ControleCronograma controlCrono) throws Exception {
    	if(!Util.isNullOuVazio(cronograma) && !Util.isNullOuVazio(cronograma.getIdeCronograma()))
    		controlCrono.setIdeCronograma(cronograma);
    	else
    		throw new AppExceptionError("Não é possível adicionar essa atividade. É preciso informar a qual cronograma ela pertence");
    	
    	if(controlCrono.getIdeItemCronograma().getIdeItemCronograma().equals(ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde()) && cronogramaJaTemConclusaoParecer(cronograma))
    		throw new AppExceptionError("Não é possível adicionar essa atividade. Um cronograma só pode ter apenas uma Conclusão de Parecer");
    	else{
    		controlCrono.setIndExcluido(false);
    		controleCronogramaDAO.salvar(controlCrono);
    	}
	}
    
    public Boolean cronogramaJaTemConclusaoParecer(Cronograma cronograma) {
    	ControleCronograma controlCrono = getControleCronogramaConclusaoParecerByCronograma(cronograma);
    	if(Util.isNullOuVazio(controlCrono) || Util.isNullOuVazio(controlCrono.getIdeItemCronograma()))
    		return false;
    	else
    		return true;
    }
    
    /**
     * Remove um controle cronograma verificando se ele tem o itemCronograma Conclusão de parecer, caso seja verdade retorna exceção informando que não não é possível exclir conclusão de parecer.
     * @author micael.coutinho
     * @param delControlCrono
     * @throws Exception
     */
    public void excluirControleCronograma(ControleCronograma delControlCrono) {
    	if(delControlCrono.getIdeItemCronograma().getIdeItemCronograma().equals(ItemCronogramaEnum.CONCLUSAO_DE_PARECER.getIde()))
    		throw new AppExceptionError("Não é permitido excluir a Conclusão de Parecer do Cronograma.");
    	else{
    		delControlCrono.setIndExcluido(Boolean.TRUE);
    		delControlCrono.setDtcExclusao(new Date());
    		controleCronogramaDAO.salvarOuAtualizar(delControlCrono);
    	}
	}
    
    /**
     * 
     * @author micael.coutinho
     * @param controlCrono
     * @throws Exception
     */
    public void atualizarControleCronograma(ControleCronograma controlCrono)  {
    	controleCronogramaDAO.salvarOuAtualizar(controlCrono);
	}
    
    
    public List<ControleCronograma> getControleCronogramaByIdeProcesso(Integer ideProcesso) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(ControleCronograma.class, "controleCronograma" );
		criteria.createAlias("ideItemCronograma", "itemCronograma", JoinType.INNER_JOIN);
		criteria.createAlias("ideCronograma", "cronograma", JoinType.INNER_JOIN);
		criteria.createAlias("cronograma.ideProcesso", "processo", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("processo.ideProcesso", ideProcesso));
		criteria.add(Restrictions.eq("controleCronograma.indExcluido", Boolean.FALSE));
		criteria.addOrder(Order.asc("dtcItemPrevista"));
		return controleCronogramaDAO.listarPorCriteria(criteria);		
		
    }
    	
}