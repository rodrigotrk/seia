package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 16/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AtoDeclaratorioDAOImpl {

	@Inject
	private IDAO<AtoDeclaratorio> dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(AtoDeclaratorio atoDeclaratorio){
		dao.salvarOuAtualizar(atoDeclaratorio);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtoDeclaratorio buscar(Requerimento requerimento, DocumentoObrigatorio docObrigatorio) {
		DetachedCriteria criteria = getCriteria(requerimento)
				.add(Restrictions.eq("doc.ideDocumentoObrigatorio", docObrigatorio.getIdeDocumentoObrigatorio()));
		adicionarProjection(criteria);
		
		AtoDeclaratorio atoDeclaratorio = dao.buscarPorCriteriaMaxResult(criteria);
		if(!Util.isNullOuVazio(atoDeclaratorio)){
			return atoDeclaratorio;
		}
		
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtoDeclaratorio> listar(Requerimento requerimento) {
		DetachedCriteria criteria = getCriteria(requerimento);
		adicionarProjection(criteria);
		return dao.listarPorCriteria(criteria);
	}

	
	public DetachedCriteria getCriteria(Requerimento requerimento){
		return DetachedCriteria.forClass(AtoDeclaratorio.class)
			.createAlias("ideDocumentoObrigatorio", "doc")
			.createAlias("ideRequerimento", "ideRequerimento")
			.addOrder(Order.desc("dtcCriacao"))
			.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
	}

	private void adicionarProjection(DetachedCriteria criteria) {
        Projection projection = Projections.projectionList()
	        .add(Projections.property("ideAtoDeclaratorio"),"ideAtoDeclaratorio")
	        .add(Projections.property("doc.ideDocumentoObrigatorio"),"ideDocumentoObrigatorio.ideDocumentoObrigatorio")
	        .add(Projections.property("doc.nomDocumentoObrigatorio"),"ideDocumentoObrigatorio.nomDocumentoObrigatorio")
	        .add(Projections.property("ideRequerimento.ideRequerimento"),"ideRequerimento.ideRequerimento")
	        .add(Projections.property("ideRequerimento.numRequerimento"),"ideRequerimento.numRequerimento")
	        .add(Projections.property("dtcCriacao"),"dtcCriacao")
	        .add(Projections.property("indConcluido"),"indConcluido");
        criteria.setProjection(projection).setResultTransformer(new AliasToNestedBeanResultTransformer(AtoDeclaratorio.class));
    }

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtoDeclaratorio carregarAtoDeclaratorio(AtoDeclaratorio ideAtoDeclaratorio) {
		
		try {
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AtoDeclaratorio.class)
				.createAlias("ideRequerimento", "ideRequerimento")	
					
				.add(Restrictions.eq("ideAtoDeclaratorio", ideAtoDeclaratorio.getIdeAtoDeclaratorio()))
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideAtoDeclaratorio"),"ideAtoDeclaratorio")
					.add(Projections.property("dtcCriacao"),"dtcCriacao")
					.add(Projections.property("ideDocumentoObrigatorio"),"ideDocumentoObrigatorio")
					.add(Projections.property("indConcluido"),"indConcluido")
					.add(Projections.property("ideRequerimento.ideRequerimento"),"ideRequerimento.ideRequerimento")
					.add(Projections.property("ideRequerimento.numRequerimento"),"ideRequerimento.numRequerimento")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(AtoDeclaratorio.class));

			return dao.buscarPorCriteria(detachedCriteria);
					
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}	
	}

	public Collection<? extends Object> listarPorProcessoReenquadramento(ProcessoReenquadramento reenquadramento) {

		return null;
	}	
}