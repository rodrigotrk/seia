package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.DisposicaoFinalResiduo;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DisposicaoFinalResiduoDAOImpl {

	@Inject
	private IDAO<DisposicaoFinalResiduo> disposicaoFinalResiduoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DisposicaoFinalResiduo> listarTodos() {
		List<DisposicaoFinalResiduo> listaBanco = new ArrayList<DisposicaoFinalResiduo>();
		listaBanco = this.disposicaoFinalResiduoDAO.listarTodos();
		
		DisposicaoFinalResiduo aux = new DisposicaoFinalResiduo();
		if(!Util.isNull(listaBanco)){
			for(DisposicaoFinalResiduo dfr: listaBanco){
				if(dfr.getIdeDisposicaoFinalResiduo().equals(19)){
					aux = dfr;
					break;
				}
			}
		}
		
		listaBanco.remove(aux);
		
		Collections.sort(listaBanco, new Comparator<DisposicaoFinalResiduo>() {
			public int compare(DisposicaoFinalResiduo dfr1, DisposicaoFinalResiduo dfr2) {
				return dfr1.getDesDisposicaoFinalResiduo().compareTo(dfr2.getDesDisposicaoFinalResiduo());
			}
		});
		
		listaBanco.add(aux);
			
		return listaBanco;
	}
}
