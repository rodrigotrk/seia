package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.RegistroFlorestaProducao;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoResponsavelTecnico;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegistroFlorestaProducaoResponsavelTecnicoDAOImpl {

	@Inject
	private IDAO<RegistroFlorestaProducaoResponsavelTecnico> registroFlorestaProducaoResponsavelTecnicoIDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoResponsavelTecnico(RegistroFlorestaProducaoResponsavelTecnico registroFlorestaProducaoResponsavelTecnico)  {
	
		try {
			if(registroFlorestaProducaoResponsavelTecnico.getIdeRegistroFlorestaProducaoResponsavelTecnico()!=null){
				registroFlorestaProducaoResponsavelTecnicoIDAO.remover(registroFlorestaProducaoResponsavelTecnico);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<RegistroFlorestaProducaoResponsavelTecnico> registroFlorestaProducaoResponsavelTecnicoList)  {
		
		try {
		
			for (RegistroFlorestaProducaoResponsavelTecnico registroFlorestaProducaoResponsavelTecnico : registroFlorestaProducaoResponsavelTecnicoList) {
				if(registroFlorestaProducaoResponsavelTecnico.getIdeRegistroFlorestaProducaoResponsavelTecnico()==null){
					registroFlorestaProducaoResponsavelTecnicoIDAO.salvar(registroFlorestaProducaoResponsavelTecnico);
				}else{
					registroFlorestaProducaoResponsavelTecnicoIDAO.atualizar(registroFlorestaProducaoResponsavelTecnico);
				}
					
					
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<RegistroFlorestaProducaoResponsavelTecnico> listarRegistroFlorestaProducaoResponsavelTecnico(RegistroFlorestaProducao ideRegistroFlorestaProducao)  {

		try {
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RegistroFlorestaProducaoResponsavelTecnico.class)
				.createAlias("idePessoaFisica", "idePessoaFisica", JoinType.INNER_JOIN)
				.createAlias("idePessoaFisica.pessoa", "idePessoa", JoinType.INNER_JOIN)
				.createAlias("idePessoaFisica.idePais", "idePais", JoinType.INNER_JOIN)
				
				.add(Restrictions.eq("ideRegistroFlorestaProducao.ideRegistroFlorestaProducao",ideRegistroFlorestaProducao.getIdeRegistroFlorestaProducao() ))
				
				.setProjection(Projections.projectionList()
					
					.add(Projections.property("ideRegistroFlorestaProducaoResponsavelTecnico"),"ideRegistroFlorestaProducaoResponsavelTecnico")
					.add(Projections.property("ideRegistroFlorestaProducao.ideRegistroFlorestaProducao"),"ideRegistroFlorestaProducao.ideRegistroFlorestaProducao")
					
					.add(Projections.property("idePessoaFisica.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
					.add(Projections.property("idePessoaFisica.nomPessoa"),"idePessoaFisica.nomPessoa")
					.add(Projections.property("idePessoaFisica.dtcNascimento"),"idePessoaFisica.dtcNascimento")
					.add(Projections.property("idePessoaFisica.desNaturalidade"),"idePessoaFisica.desNaturalidade")
					.add(Projections.property("idePessoaFisica.nomPai"),"idePessoaFisica.nomPai")
					.add(Projections.property("idePessoaFisica.nomMae"),"idePessoaFisica.nomMae")
					.add(Projections.property("idePessoaFisica.numCpf"),"idePessoaFisica.numCpf")
					
					.add(Projections.property("idePessoa.idePessoa"),"idePessoaFisica.pessoa.idePessoa")
					.add(Projections.property("idePessoa.desEmail"),"idePessoaFisica.pessoa.desEmail")
					
					.add(Projections.property("idePessoaFisica.idePais.idePais"),"idePessoaFisica.idePais.idePais")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(RegistroFlorestaProducaoResponsavelTecnico.class));
		
			return registroFlorestaProducaoResponsavelTecnicoIDAO.listarPorCriteria(detachedCriteria);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
