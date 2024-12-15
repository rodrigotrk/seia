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

import br.gov.ba.seia.entity.EnquadramentoFinalidadeUsoAgua;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnquadramentoFinalidadeUsoAguaDaoImpl {
	
	@Inject
	private IDAO<EnquadramentoFinalidadeUsoAgua> efuaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(EnquadramentoFinalidadeUsoAgua efua) {
		try {
			efuaDAO.salvarOuAtualizar(efua);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnquadramentoFinalidadeUsoAgua> listarPorEnquadramentoAtoAmbiental(Integer ideEnquadramentoAtoAmbiental) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(EnquadramentoFinalidadeUsoAgua.class)
				.createAlias("ideEnquadramentoAtoAmbiental", "eaa", JoinType.INNER_JOIN)
				.createAlias("eaa.enquadramento", "enq", JoinType.LEFT_OUTER_JOIN)
				.createAlias("eaa.tipologia", "tipo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("eaa.atoAmbiental", "aa", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("ideTipoFinalidadeUsoAgua", "tfua", JoinType.INNER_JOIN)
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideEnquadramentoFinalidadeUsoAgua"), "ideEnquadramentoFinalidadeUsoAgua")
					
					.add(Projections.property("eaa.ideEnquadramentoAtoAmbiental"), "ideEnquadramentoAtoAmbiental.ideEnquadramentoAtoAmbiental")
					
					.add(Projections.property("enq.ideEnquadramento"),"ideEnquadramentoAtoAmbiental.enquadramento.ideEnquadramento")
					
					.add(Projections.property("tipo.ideTipologia"),"ideEnquadramentoAtoAmbiental.tipologia.ideTipologia")
					.add(Projections.property("tipo.desTipologia"),"ideEnquadramentoAtoAmbiental.tipologia.desTipologia")
					
					.add(Projections.property("aa.ideAtoAmbiental"),"ideEnquadramentoAtoAmbiental.atoAmbiental.ideAtoAmbiental")
					.add(Projections.property("aa.nomAtoAmbiental"),"ideEnquadramentoAtoAmbiental.atoAmbiental.nomAtoAmbiental")
					.add(Projections.property("aa.sglAtoAmbiental"),"ideEnquadramentoAtoAmbiental.atoAmbiental.sglAtoAmbiental")
					
					.add(Projections.property("tfua.ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
					.add(Projections.property("tfua.nomTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(EnquadramentoFinalidadeUsoAgua.class))
				
				.add(Restrictions.eq("eaa.ideEnquadramentoAtoAmbiental", ideEnquadramentoAtoAmbiental));
				
			return efuaDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	

}