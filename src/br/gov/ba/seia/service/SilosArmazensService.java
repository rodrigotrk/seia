package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.SilosArmazen;
import br.gov.ba.seia.entity.SilosArmazensResponsavelTecnico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SilosArmazensService {

	@Inject
	private IDAO<SilosArmazen> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazens(SilosArmazen silos) throws Exception{
		idao.salvarOuAtualizar(silos);
	}
	
	
	public SilosArmazen buscarSilosArmazens(CadastroAtividadeNaoSujeitaLic cadastro) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SilosArmazen.class);
		
		criteria.createAlias("ideCadastroAtividadeNaoSujeitaLic", "cANSL")
		.createAlias("silosArmazensResponsavelTecnicos", "sART", JoinType.LEFT_OUTER_JOIN)
		.createAlias("cANSL.idePessoaRequerente", "pR", JoinType.LEFT_OUTER_JOIN);
//		.createAlias("sART.idePessoaFisica", "pF", JoinType.LEFT_OUTER_JOIN)
//		.createAlias("pF.pessoa", "p", JoinType.LEFT_OUTER_JOIN)
//		.createAlias("p.telefoneCollection", "tel", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("cANSL.ideCadastroAtividadeNaoSujeitaLic", cadastro.getIdeCadastroAtividadeNaoSujeitaLic()));
		
		SilosArmazen silos = idao.listarPorCriteria(criteria).get(0);
		
		
		/*for(SilosArmazensResponsavelTecnico tecnico : silos.getSilosArmazensResponsavelTecnicos()){
			
			Hibernate.initialize(tecnico.getIdePessoaFisica().getPessoa().getTelefoneCollection());
			
		}*/
		
		
		return silos;
	}
}
