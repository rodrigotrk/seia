package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.ClasseDAOImpl;
import br.gov.ba.seia.entity.Classe;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.PotencialPoluicao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClasseService {

	@EJB
	private ClasseDAOImpl classeDAOImpl;
	
	public Classe buscarClasseAtividade(Porte porte, PotencialPoluicao idePotencialPoluicao)  {
		return classeDAOImpl.buscarClasseAtividade(porte, idePotencialPoluicao);
	}

}
