package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.Map;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.SilosArmazen;
import br.gov.ba.seia.entity.SilosArmazensImovel;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SilosArmazensImovelRuralService {

	@Inject
	private IDAO<SilosArmazensImovel> idao;
	
	@Inject
	private IDAO<Imovel> idaoImovel;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensImovelRural(SilosArmazensImovel armazensImovelRural) throws Exception{
		idao.salvarOuAtualizar(armazensImovelRural);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel buscarImovelPorNumeroCar(ImovelRural imovelRural) throws  Exception {
		
		try{
			return idaoImovel.buscarPorCriteria(
				DetachedCriteria.forClass(Imovel.class)
					.createAlias("imovelRural", "ideImovelRural", JoinType.INNER_JOIN)
					.createAlias("imovelRural.imovelRuralSicar", "ideImovelRuralSicar", JoinType.INNER_JOIN)
					.createAlias("ideEndereco", "ideEndereco", JoinType.INNER_JOIN)
					.createAlias("ideEndereco.ideLogradouro", "ideLogradouro", JoinType.LEFT_OUTER_JOIN)
					.createAlias("ideLogradouro.ideMunicipio", "ideMunicipio", JoinType.LEFT_OUTER_JOIN)
					
					.add(Restrictions.eq("ideImovelRuralSicar.numSicar", imovelRural.getImovelRuralSicar().getNumSicar()))
					
					.setProjection(Projections.projectionList()
						.add(Projections.property("ideImovel"),"ideImovel")
						.add(Projections.property("ideImovelRural.ideImovelRural"),"imovelRural.ideImovelRural")
						.add(Projections.property("ideImovelRural.desDenominacao"),"imovelRural.desDenominacao")
						.add(Projections.property("ideImovelRural.valArea"),"imovelRural.valArea")
						.add(Projections.property("ideImovelRuralSicar.ideImovelRuralSicar"),"imovelRural.imovelRuralSicar.ideImovelRuralSicar")
						.add(Projections.property("ideImovelRuralSicar.numSicar"),"imovelRural.imovelRuralSicar.numSicar")
						.add(Projections.property("ideEndereco.ideEndereco"),"ideEndereco.ideEndereco")
						.add(Projections.property("ideLogradouro.ideLogradouro"),"ideEndereco.ideLogradouro.ideLogradouro")
						.add(Projections.property("ideMunicipio.ideMunicipio"),"ideEndereco.ideLogradouro.ideMunicipio.ideMunicipio")
						.add(Projections.property("ideMunicipio.indBloqueioDQC"),"ideEndereco.ideLogradouro.ideMunicipio.indBloqueioDQC")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class))
				);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
	
	
	public Boolean existeImoveisRuraisByEmpreendimento(Empreendimento empreendimento) throws Exception {
		/** Empreendimento e = empreendimentoService.carregarById(empreendimento.getIdeEmpreendimento());
		Hibernate.initialize(e.getImovelCollection());
		return e.getImovelCollection(); **/
		DetachedCriteria criteria = DetachedCriteria.forClass(Imovel.class);
		criteria.createAlias("imovelRural", "imovelR",JoinType.INNER_JOIN);
		criteria.createAlias("empreendimentoCollection", "emprendimento");
		criteria.add(Restrictions.eq("emprendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		Integer imoveis = idaoImovel.count(criteria); 
		
		if(imoveis<=0){
			return false;
		}
		return true; 
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirImovelRural(SilosArmazen ideSilosArmazens) throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideSilosArmazen", ideSilosArmazens);
		idao.executarNamedQuery("SilosArmazensImovel.removerByIdeSilos", params);
	}
	
}
