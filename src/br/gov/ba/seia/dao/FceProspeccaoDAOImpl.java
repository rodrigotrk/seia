package br.gov.ba.seia.dao;

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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralProspeccao;
import br.gov.ba.seia.entity.FceProspeccao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author Alexandre Queiroz
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceProspeccaoDAOImpl {

    @Inject
    private IDAO<FceProspeccao> fceProspeccaoDAO;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<FceProspeccao> listarFcePesquisaMineralProspeccaoBy(FcePesquisaMineral ideFcePesquisaMineral) {
	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FcePesquisaMineralProspeccao.class)
		.createAlias("ideMetodoProspeccao", "ideMetodoProspeccao")
		.add(Restrictions.eq("ideFcePesquisaMineral.ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral()))
		.setProjection(Projections.projectionList()
			.add(Projections.property("ideFcePesquisaMineral.ideFcePesquisaMineral"),"ideFcePesquisaMineral.ideFcePesquisaMineral")
			.add(Projections.property("ideMetodoProspeccao.ideMetodoProspeccao"),"ideMetodoProspeccao.ideMetodoProspeccao")
			.add(Projections.property("ideMetodoProspeccao.nomMetodoProspeccao"),"ideMetodoProspeccao.nomMetodoProspeccao")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(FcePesquisaMineralProspeccao.class));
	return fceProspeccaoDAO.listarPorCriteria(detachedCriteria, Order.asc("ideFcePesquisaMineral.ideFcePesquisaMineral"));
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void salvarFceProspeccao(FceProspeccao ideFceProspeccao){
    	fceProspeccaoDAO.salvar(ideFceProspeccao);
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void salvarListaFceProspeccao(List<FceProspeccao> listaFceProspeccao){
    	fceProspeccaoDAO.salvarEmLote(listaFceProspeccao);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<FceProspeccao>  listarFceProspeccaoBy(FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao){
	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceProspeccao.class)
		.createAlias("ideLocalizacaoGeografica", "lg")
		.createAlias("lg.ideSistemaCoordenada", "sc")
		.createAlias("lg.ideClassificacaoSecao", "cla")
		.createAlias("lg.dadoGeograficoCollection", "dg",JoinType.INNER_JOIN)
		.add(Restrictions.eq("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao", ideFcePesquisaMineralProspeccao.getIdeFcePesquisaMineralProspeccao()))
		.setProjection(Projections.projectionList()
			.add(Projections.property("ideFceProspeccao"),"ideFceProspeccao")
			.add(Projections.property("seqProspeccao"),"seqProspeccao")
			.add(Projections.property("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao"),"ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao")
			.add(Projections.property("lg.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica.ideLocalizacaoGeografica")
			.add(Projections.property("sc.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
			.add(Projections.property("cla.ideClassificacaoSecao"),"ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(FceProspeccao.class));
	return fceProspeccaoDAO.listarPorCriteria(detachedCriteria);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void excluirFceProspeccaoBy(FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao) {
    	Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFcePesquisaMineralProspeccao", ideFcePesquisaMineralProspeccao.getIdeFcePesquisaMineralProspeccao());
		fceProspeccaoDAO.executarNamedQuery("FceProspeccao.removerByFcePesquisaMineralProspeccao", parameters);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<FceProspeccao> listarFceProspeccaoByIdeFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) {
	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceProspeccao.class)
		.createAlias("ideFcePesquisaMineralProspeccao", "ideFcePesquisaMineralProspeccao")
		.add(Restrictions.eq("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineral.ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral()));
	return fceProspeccaoDAO.listarPorCriteria(detachedCriteria);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void excluirFceProspeccao(FceProspeccao ideFceProspeccao) {
	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("ideFceProspeccao", ideFceProspeccao.getIdeFceProspeccao());
	fceProspeccaoDAO.executarNamedQuery("FceProspeccao.remover", parameters);

    }

}
