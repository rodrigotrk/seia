package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dto.CerhConsultarDaeDto;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.GeoRpga;
import br.gov.ba.seia.entity.SituacaoDae;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhConsultarDaeGeradoDaoImpl {

	@Inject
	IDAO<CerhConsultarDaeDto> cerhIdao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhConsultarDaeDto> consultarDaeGerado(Map<String, Object> params) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Cerh.class, "c");
		
		criteria
		.createAlias("c.cerhCobrancaCollection", "cC", JoinType.INNER_JOIN)
		.createAlias("c.idePessoaRequerente", "r", JoinType.INNER_JOIN)
		.createAlias("r.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
		.createAlias("c.ideEmpreendimento", "emp", JoinType.INNER_JOIN)
		.createAlias("cC.cerhParcelasCobrancasCollection", "cPC", JoinType.INNER_JOIN)
		.createAlias("cPC.cerhParcelasDaeCollection", "cPD",JoinType.INNER_JOIN)
		.createAlias("cPD.ideCerhDae", "cD", JoinType.INNER_JOIN)
		.createAlias("cD.ideCerhDaeLocGeografica", "cDLG", JoinType.INNER_JOIN)
		.createAlias("cDLG.ideCerhLocalizacaoGeografica", "cLG", JoinType.INNER_JOIN)
		.createAlias("cLG.gid", "gR", JoinType.INNER_JOIN)
		.createAlias("cD.histSituacaoDae", "cHSD", JoinType.INNER_JOIN)
		.createAlias("cHSD.ideSituacaoDae", "cSD", JoinType.INNER_JOIN)
		.createAlias("cD.pagamentoDaeCollection", "cPagD", JoinType.LEFT_OUTER_JOIN);
		
		
		adicionarRestrictions(criteria, params);
		
		adicionarProjection(criteria);
		
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(CerhConsultarDaeDto.class));
		
		return cerhIdao.listarPorCriteria(criteria);
	}
	
	private void adicionarProjection(DetachedCriteria criteria) {
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("emp.ideEmpreendimento"),"ideEmpreendimento.ideEmpreendimento")
				.add(Projections.groupProperty("emp.nomEmpreendimento"),"ideEmpreendimento.nomEmpreendimento")
				.add(Projections.groupProperty("r.idePessoa"),"idePessoaRequerente.idePessoa")
				.add(Projections.groupProperty("pf.nomPessoa"),"idePessoaRequerente.pessoaFisica.nomPessoa")
				.add(Projections.groupProperty("cD.dtVencimento"),"cerhDae.dtVencimento")
				.add(Projections.groupProperty("cD.vlrPrincipal"),"cerhDae.vlrPrincipal")
				.add(Projections.groupProperty("cD.vlrAcrescimo"),"cerhDae.vlrAcrescimo")
				.add(Projections.groupProperty("cD.numParcelaReferencia"),"cerhDae.numParcelaReferencia")
				.add(Projections.groupProperty("cPagD.dataPagamento"), "cerhPagamentoDae.dataPagamento")
				.add(Projections.groupProperty("cSD.dscSituacaoDae"),"cerhSituacaoDae.dscSituacaoDae")
				.add(Projections.groupProperty("gR.nomRpga"),"geoRpga.nomRpga")
			);
		
	}
	
	private void adicionarRestrictions(DetachedCriteria criteria, Map<String, Object> params) {
		
		if(!Util.isNullOuVazio(params.get("numAnoCobranca"))){
			String anoCobranca = (String) params.get("numAnoCobranca");
			criteria.add(Restrictions.eq("cC.numAnoCobranca", Integer.parseInt(anoCobranca)));
		}
		
		if(!Util.isNullOuVazio(params.get("geoRpga"))){
			GeoRpga rpga = (GeoRpga) params.get("geoRpga");
			criteria.add(Restrictions.eq("gR.gid", rpga.getGid()));
		}
		
		if(!Util.isNullOuVazio(params.get("cerhSitucaoDae"))){
			SituacaoDae situacaoDae = (SituacaoDae) params.get("cerhSitucaoDae");
			criteria.add(Restrictions.eq("cSD.ideCerhSitucaoDae", situacaoDae.getIdeSitucaoDae()));
		}
		
		if(!Util.isNullOuVazio(params.get("empreendimento"))){
			Empreendimento empreendimento = (Empreendimento) params.get("empreendimento");
			criteria.add(Restrictions.eq("emp.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
			
		}
	
	}	
		
}
