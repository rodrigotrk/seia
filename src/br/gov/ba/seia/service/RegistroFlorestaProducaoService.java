package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.RegistroFlorestaProducao;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegistroFlorestaProducaoService {
	
	@Inject
	private IDAO<RegistroFlorestaProducao> registroFlorestaProducaoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducao(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
		try {
			if(ideRegistroFlorestaProducao.getIdeRegistroFlorestaProducaoImovel()!=null){
				registroFlorestaProducaoDAOImpl.remover(ideRegistroFlorestaProducao);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
		try {
			registroFlorestaProducaoDAOImpl.salvarOuAtualizar(ideRegistroFlorestaProducao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RegistroFlorestaProducao carregarRegistroFlorestaProducao(Requerimento ideRequerimento) {
		try {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RegistroFlorestaProducao.class)
				
				.createAlias("ideAtoDeclaratorio","atoDeclaratorio", JoinType.INNER_JOIN)
				.createAlias("atoDeclaratorio.ideRequerimento", "requerimento", JoinType.INNER_JOIN)
				.createAlias("atoDeclaratorio.ideDocumentoObrigatorio", "documentoObrigatorio", JoinType.INNER_JOIN)

					.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento.getIdeRequerimento()))
					.addOrder(Order.desc("atoDeclaratorio.dtcCriacao"))

				.setProjection(Projections.projectionList()
						
					.add(Projections.property("ideRegistroFlorestaProducao"),"ideRegistroFlorestaProducao")
					.add(Projections.property("dtPrevistaUltimoCorte"),"dtPrevistaUltimoCorte")
					.add(Projections.property("indAceiteResponsabilidade"),"indAceiteResponsabilidade")
					.add(Projections.property("indCienteTermoCompromisso"),"indCienteTermoCompromisso")
					
					.add(Projections.property("atoDeclaratorio.indConcluido"),"ideAtoDeclaratorio.indConcluido")
					.add(Projections.property("atoDeclaratorio.ideAtoDeclaratorio"),"ideAtoDeclaratorio.ideAtoDeclaratorio")
					.add(Projections.property("atoDeclaratorio.dtcCriacao"),"ideAtoDeclaratorio.dtcCriacao")
					
					.add(Projections.property("requerimento.ideRequerimento"),"ideAtoDeclaratorio.ideRequerimento.ideRequerimento")
					.add(Projections.property("documentoObrigatorio.ideDocumentoObrigatorio"),"ideAtoDeclaratorio.ideDocumentoObrigatorio.ideDocumentoObrigatorio")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(RegistroFlorestaProducao.class));
			

			return registroFlorestaProducaoDAOImpl.buscarPorCriteriaMaxResult(detachedCriteria);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}		

}
