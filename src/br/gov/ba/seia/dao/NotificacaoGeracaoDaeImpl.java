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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;

import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.GeoBahia;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificacaoGeracaoDaeImpl {
	
	@Inject
	private IDAO<Cerh> cerhDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Cerh> listarCerh(Empreendimento empreendimento) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Cerh.class, "c");
		criteria
			.createAlias("c.ideCerhStatus", "cs", JoinType.INNER_JOIN)
			.add(Restrictions.ne("cs.ideCerhStatus", CerhStatusEnum.CANCELADO.getId()))
			.add(Restrictions.eq("c.ideEmpreendimento", empreendimento))
			.setProjection(Projections.property("ideCerh"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Cerh.class))
		;
		
		return cerhDAO.listarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Cerh> listarCerh(Integer first, Integer pageSize, Map<String, Object> params) throws Exception {
		
		DetachedCriteria criteria = getCriteriaListarCerh(params);
		criteria
			.addOrder(Order.desc("c.dtcCadastro"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Cerh.class))
		;
		
		return cerhDAO.listarPorCriteriaDemanda(criteria, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer listarCerhCount(Map<String, Object> params) throws Exception {
		
		DetachedCriteria criteria = getCriteriaListarCerh(params)
			.setProjection(Projections.groupProperty("c.ideCerh"))
		;
		
		DetachedCriteria criteriaCount = DetachedCriteria.forClass(Cerh.class)
			.add(Property.forName("ideCerh").in(criteria))
		;
		return cerhDAO.count(criteriaCount);		
		
	}

	private void adicionarRestrictions(DetachedCriteria criteria, Map<String, Object> params) {
		criteria	
			.add(Restrictions.eq("te.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
			.add(Restrictions.isNull("c.indHistorico"))
		;
			
		
		if(!Util.isNull(params.get("requerente"))) {
			Pessoa requerente = (Pessoa) params.get("requerente");
			if(!Util.isNullOuVazio(requerente)) {
				criteria.add(Restrictions.eq("r.idePessoa", requerente.getIdePessoa()));
			}
		}
		
		if(!Util.isNull(params.get("nomEmpreendimento"))) {
			String nomEmpreendimento = (String) params.get("nomEmpreendimento");
			criteria.add(Restrictions.ilike("emp.nomEmpreendimento", nomEmpreendimento,MatchMode.ANYWHERE));
		}
		
		if(!Util.isNull(params.get("rpgaSelecionado"))) {
			GeoBahia rpgaSelecionado = (GeoBahia) params.get("rpgaSelecionado");
			criteria.add(Restrictions.sqlRestriction("ST_Intersects(dg19_.the_geom,(select the_geom from geo_rpga where gid=?))", rpgaSelecionado.getGid(), StandardBasicTypes.INTEGER));
		}
		
		if(ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno()) {
			criteria
				.createAlias("pf.procuradorPessoaFisicaCollection", "ppf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pj.procuradorRepresentanteCollection", "ppj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pj.representanteLegalCollection", "rpj", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.disjunction()
					.add(Restrictions.eq("r.idePessoa", ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()))
					.add(Restrictions.eq("ppf.ideProcurador.idePessoaFisica", ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()))
					.add(Restrictions.eq("rpj.idePessoaFisica.idePessoaFisica", ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()))
					.add(Restrictions.eq("ppj.ideProcurador.idePessoaFisica", ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()))
				)
			;
		}
	}


	private void adicionarProjection(DetachedCriteria criteria) {
		criteria.setProjection(Projections.projectionList()
			.add(Projections.groupProperty("c.ideCerh"),"ideCerh")
			.add(Projections.groupProperty("c.dtcCadastro"),"dtcCadastro")
			.add(Projections.groupProperty("c.numCadastro"),"numCadastro")
			.add(Projections.groupProperty("cs.dtcStatus"),"statusAtual.dtcStatus")
			.add(Projections.groupProperty("emp.ideEmpreendimento"),"ideEmpreendimento.ideEmpreendimento")
			.add(Projections.groupProperty("emp.nomEmpreendimento"),"ideEmpreendimento.nomEmpreendimento")
			.add(Projections.groupProperty("emp.idePessoa.idePessoa"),"ideEmpreendimento.idePessoa.idePessoa")
			.add(Projections.groupProperty("c.idePessoaRequerente"),"idePessoaRequerente")
			.add(Projections.groupProperty("pf.nomPessoa"),"idePessoaRequerente.pessoaFisica.nomPessoa")
			.add(Projections.groupProperty("pf.numCpf"),"idePessoaRequerente.pessoaFisica.numCpf")
			.add(Projections.groupProperty("pj.nomRazaoSocial"),"idePessoaRequerente.pessoaJuridica.nomRazaoSocial")
			.add(Projections.groupProperty("pj.numCnpj"),"idePessoaRequerente.pessoaJuridica.numCnpj")
			.add(Projections.groupProperty("pfc.idePessoaFisica"),"idePessoaFisicaCadastro.idePessoaFisica")
		);
		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private DetachedCriteria getCriteriaListarCerh(Map<String, Object> params) {
		
		//Adicionar novos joins sempre no final para não quebrar a consulta
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Cerh.class, "c");
		criteria
			.createAlias("c.ideCerhPai", "cp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("c.ideCerhStatus", "cs", JoinType.LEFT_OUTER_JOIN)
			.createAlias("c.idePessoaRequerente", "r", JoinType.INNER_JOIN)
			.createAlias("c.idePessoaFisicaCadastro", "pfc", JoinType.LEFT_OUTER_JOIN)
			.createAlias("c.ideEmpreendimento", "emp", JoinType.INNER_JOIN)
			.createAlias("emp.enderecoEmpreendimentoCollection", "ee", JoinType.INNER_JOIN)
			.createAlias("ee.ideTipoEndereco", "te", JoinType.INNER_JOIN)
			.createAlias("ee.ideEndereco", "end", JoinType.INNER_JOIN)
			.createAlias("end.ideLogradouro","l")
			.createAlias("l.ideBairro","b")
			.createAlias("l.ideMunicipio","m")
			.createAlias("c.ideContratoConvenio", "cc", JoinType.LEFT_OUTER_JOIN)
			.createAlias("r.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("r.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
			.createAlias("c.ideCertificado", "cert", JoinType.LEFT_OUTER_JOIN)
			.createAlias("c.cerhTipoUsoCollection", "ctu", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ctu.cerhLocalizacaoGeograficaCollection", "clg", JoinType.LEFT_OUTER_JOIN)
			.createAlias("clg.ideLocalizacaoGeografica", "lg", JoinType.LEFT_OUTER_JOIN)
			.createAlias("lg.dadoGeograficoCollection", "dg", JoinType.LEFT_OUTER_JOIN)
		;
		
		adicionarRestrictions(criteria, params);
		adicionarProjection(criteria);
		
		return criteria;
	}

}
