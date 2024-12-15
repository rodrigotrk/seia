package br.gov.ba.seia.dao;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhCadastroCancelado;
import br.gov.ba.seia.entity.CerhStatus;
import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.GeoBahia;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.ProjectionUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCadastroCanceladoDAOImpl extends AbstractDAO<CerhCadastroCancelado> {
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<Cerh> cerhDAO;
	
	@Inject
	private IDAO<CerhCadastroCancelado> cerhCadastroCanceladoDAO;
	
	
	@Override
	protected IDAO<CerhCadastroCancelado> getDAO() {
		return cerhCadastroCanceladoDAO;
	}
	

	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh getCerhAnterior(Cerh cerh) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Cerh.class)
			.createAlias("ideCerhCadastroCancelado", "ideCerhCadastroCancelado", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("ideCerh", cerh.getIdeObjetoPai()))
			.setProjection(ProjectionUtil.add(Cerh.class))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(Cerh.class));
		
		return cerhDAO.buscarPorCriteria(detachedCriteria);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh getUltimoNumeroCadastro(Cerh c) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Cerh.class)
			.add(Restrictions.eq("ideCerhPai.ideCerh", c.getId()))
			.add(Restrictions.eq("indHistorico", true))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideCerh"),"ideCerh")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(Cerh.class));
		return cerhDAO.buscarPorCriteria(detachedCriteria);
	}

	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh buscarCerhPor(Integer ideCerh) {
		return 
			cerhDAO.buscarPorCriteria(
				DetachedCriteria.forClass(Cerh.class,"c")
					.createAlias("c.ideCerhStatus", "cs", JoinType.INNER_JOIN)
					.createAlias("c.ideCerhPai", "cp", JoinType.LEFT_OUTER_JOIN)
					.createAlias("c.ideContratoConvenio", "cc", JoinType.LEFT_OUTER_JOIN)
					.createAlias("c.ideCertificado", "cert", JoinType.LEFT_OUTER_JOIN)
					.createAlias("c.ideEmpreendimento", "emp", JoinType.INNER_JOIN)
					.createAlias("emp.enderecoEmpreendimentoCollection", "ee", JoinType.INNER_JOIN)
					.createAlias("ee.ideTipoEndereco", "te", JoinType.INNER_JOIN)
					.createAlias("ee.ideEndereco", "end", JoinType.INNER_JOIN)
					.createAlias("end.ideLogradouro","l", JoinType.INNER_JOIN)
					.createAlias("l.ideBairro","b", JoinType.INNER_JOIN)
					.createAlias("l.ideMunicipio","m", JoinType.INNER_JOIN)
					.createAlias("m.ideEstado","est", JoinType.INNER_JOIN)
				
					.add(Restrictions.eq("c.ideCerh", ideCerh))
					.add(Restrictions.eq("te.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
							
					.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("c.ideCerh"),"ideCerh")
						.add(Projections.groupProperty("c.ideObjetoPai"),"ideObjetoPai")
						.add(Projections.groupProperty("c.dtcCadastro"),"dtcCadastro")
						.add(Projections.groupProperty("c.dtcExclusao"),"dtcExclusao")
						.add(Projections.groupProperty("c.indExcluido"),"indExcluido")
						.add(Projections.groupProperty("c.indHistorico"),"indHistorico")
						
						.add(Projections.groupProperty("c.numCadastro"),"numCadastro")
						.add(Projections.groupProperty("cc.ideContratoConvenio"),"ideContratoConvenio.ideContratoConvenio")
						.add(Projections.groupProperty("cc.nomContratoConvenio"),"ideContratoConvenio.nomContratoConvenio")
						.add(Projections.groupProperty("cc.numContrato"),"ideContratoConvenio.numContrato")
						.add(Projections.groupProperty("cc.ideObjetoPai"),"ideContratoConvenio.ideObjetoPai")
						
						.add(Projections.groupProperty("idePessoaFisicaCadastro.idePessoaFisica"),"idePessoaFisicaCadastro.idePessoaFisica")
						
						.add(Projections.groupProperty("idePessoaRequerente.idePessoa"),"idePessoaRequerente.idePessoa")
						
						.add(Projections.groupProperty("cp.ideCerh"),"ideCerhPai.ideCerh")
						
						.add(Projections.groupProperty("cert.ideCertificado"),"ideCertificado.ideCertificado")
						
						.add(Projections.groupProperty("cs.ideCerhStatus"),"ideCerhStatus.ideCerhStatus")
						.add(Projections.groupProperty("cs.dscStatus"),"ideCerhStatus.dscStatus")
						
						.add(Projections.groupProperty("emp.ideEmpreendimento"),"ideEmpreendimento.ideEmpreendimento")
						.add(Projections.groupProperty("emp.nomEmpreendimento"),"ideEmpreendimento.nomEmpreendimento")
						.add(Projections.groupProperty("end.ideEndereco"),"ideEmpreendimento.endereco.ideEndereco")
						.add(Projections.groupProperty("end.numEndereco"),"ideEmpreendimento.endereco.numEndereco")
						.add(Projections.groupProperty("end.desComplemento"),"ideEmpreendimento.endereco.desComplemento")
						.add(Projections.groupProperty("l.ideLogradouro"),"ideEmpreendimento.endereco.ideLogradouro.ideLogradouro")
						.add(Projections.groupProperty("l.nomLogradouro"),"ideEmpreendimento.endereco.ideLogradouro.nomLogradouro")
						.add(Projections.groupProperty("l.numCep"),"ideEmpreendimento.endereco.ideLogradouro.numCep")
						.add(Projections.groupProperty("b.ideBairro"),"ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideBairro")
						.add(Projections.groupProperty("b.nomBairro"),"ideEmpreendimento.endereco.ideLogradouro.ideBairro.nomBairro")
						.add(Projections.groupProperty("m.ideMunicipio"),"ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideMunicipio")
						.add(Projections.groupProperty("m.nomMunicipio"),"ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")
						.add(Projections.groupProperty("m.coordGeobahiaMunicipio"),"ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.coordGeobahiaMunicipio")
						.add(Projections.groupProperty("est.ideEstado"),"ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.ideEstado")
						.add(Projections.groupProperty("est.nomEstado"),"ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.nomEstado")
						.add(Projections.groupProperty("est.desSigla"),"ideEmpreendimento.endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.desSigla")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(Cerh.class)));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Cerh> listarHistorico(Integer ideCerh, Integer first, Integer pageSize)  {
		return
			cerhDAO
				.listarPorCriteriaDemanda(
					getCriteriaHistorico(ideCerh)
					.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("ideCerh"),"ideCerh")
						.add(Projections.groupProperty("ideObjetoPai"),"ideObjetoPai")
						.add(Projections.groupProperty("dtcCadastro"),"dtcCadastro")
						.add(Projections.groupProperty("dtcExclusao"),"dtcExclusao")
						.add(Projections.groupProperty("indExcluido"),"indExcluido")
						.add(Projections.groupProperty("indHistorico"),"indHistorico")
						.add(Projections.groupProperty("numCadastro"),"numCadastro")
						
						.add(Projections.groupProperty("pfc.idePessoaFisica"),"idePessoaFisicaCadastro.idePessoaFisica")
						.add(Projections.groupProperty("pfc.nomPessoa"),"idePessoaFisicaCadastro.nomPessoa")
						.add(Projections.groupProperty("idePessoaRequerente.idePessoa"),"idePessoaRequerente.idePessoa")
						.add(Projections.groupProperty("cp.ideCerh"),"ideCerhPai.ideCerh")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(Cerh.class)),first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer listarHistoricoCount(Integer ideCerh)  {
		return cerhDAO.count(getCriteriaHistorico(ideCerh));
	}

	private DetachedCriteria getCriteriaHistorico(Integer ideCerh) {
		return 
			DetachedCriteria.forClass(Cerh.class)
				.createAlias("idePessoaFisicaCadastro", "pfc", JoinType.INNER_JOIN)
				.createAlias("ideCerhPai", "cp", JoinType.LEFT_OUTER_JOIN)
				
				.add(Restrictions.or(
					Restrictions.eq("ideCerh", ideCerh), 
					Restrictions.eq("cp.ideCerh", ideCerh))
				);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Cerh cerh) {
		cerhDAO.salvarOuAtualizar(cerh);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Cerh> listarCerh(Empreendimento empreendimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Cerh.class, "c");
		criteria
			.createAlias("c.ideCerhStatus", "cs", JoinType.INNER_JOIN)
			.add(Restrictions.ne("cs.ideCerhStatus", CerhStatusEnum.CANCELADO.getId()))
			.add(Restrictions.eq("c.ideEmpreendimento", empreendimento))
			.add(Restrictions.eq("c.indHistorico", false))
			.setProjection(Projections.property("ideCerh"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Cerh.class))
		;
		
		return cerhDAO.listarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Cerh> listarCerh(Integer first, Integer pageSize, Map<String, Object> params)  {
		
		DetachedCriteria criteria = 
			getCriteriaListarCerh(params)
				.addOrder(Order.desc("c.dtcCadastro"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Cerh.class));
		
		return cerhDAO.listarPorCriteriaDemanda(criteria, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer listarCerhCount(Map<String, Object> params)  {
		
		DetachedCriteria criteria = getCriteriaListarCerh(params)
			.setProjection(Projections.groupProperty("c.ideCerh"))
		;
		
		DetachedCriteria criteriaCount = DetachedCriteria.forClass(Cerh.class)
			.add(Property.forName("ideCerh").in(criteria))
		;
		return cerhDAO.count(criteriaCount);		
		
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


	private void adicionarRestrictions(DetachedCriteria criteria, Map<String, Object> params) {
		
		criteria	
			.add(Restrictions.eq("te.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
			.add(Restrictions.eq("c.indHistorico", false))
		;
		
		if(!Util.isNull(params.get("requerente"))) {
			Pessoa requerente = (Pessoa) params.get("requerente");
			if(!Util.isNullOuVazio(requerente)) {
				criteria.add(Restrictions.eq("r.idePessoa", requerente.getIdePessoa()));
			}
		}
		
		if(!Util.isNull(params.get("numCadastro"))) {
			String numCadastro  = (String) params.get("numCadastro");
			criteria.add(
				Restrictions.or(
					Restrictions.and(Restrictions.isNull("c.ideCerhPai"), Restrictions.ilike("c.numCadastro", numCadastro.replace(" ",""), MatchMode.ANYWHERE)),
					Restrictions.ilike("cp.numCadastro", numCadastro.replace(" ",""), MatchMode.ANYWHERE)
				)
			);
		}
		
		if(!Util.isNull(params.get("periodoInicio")) && !Util.isNull(params.get("periodoFim"))) {
			Date periodoInicio =  (Date) params.get("periodoInicio");
			Date periodoFim =  Util.adicionarUmDia((Date) params.get("periodoFim"));
			
			criteria.add(Restrictions.between("cp.dtcCadastro", periodoInicio, periodoFim));
					
			criteria.add(
				Restrictions.or(
					Restrictions.and(Restrictions.isNull("c.ideCerhPai"), Restrictions.between("c.dtcCadastro", periodoInicio, periodoFim)),
					Restrictions.between("cp.dtcCadastro", periodoInicio, periodoFim)
				)					
			);
		}
		
		if(!Util.isNull(params.get("municipio"))) {
			Municipio municipio = (Municipio) params.get("municipio");
			criteria.add(Restrictions.eq("m.ideMunicipio", municipio.getIdeMunicipio()));
		}
		
		if(!Util.isNull(params.get("nomEmpreendimento"))) {
			String nomEmpreendimento = (String) params.get("nomEmpreendimento");
			criteria.add(Restrictions.ilike("emp.nomEmpreendimento", nomEmpreendimento,MatchMode.ANYWHERE));
		}
		
		if(!Util.isNull(params.get("contratoConvenio"))) {
			ContratoConvenio contratoConvenio = (ContratoConvenio) params.get("contratoConvenio");
			criteria.add(Restrictions.eq("cc.ideContratoConvenio", contratoConvenio.getIdeContratoConvenio()));
		}
		
		if(!Util.isNull(params.get("cerhTipoStatus"))) {
			CerhStatus cerhTipoStatus =  (CerhStatus) params.get("cerhTipoStatus");
			criteria.add(Restrictions.eq("cs.ideCerhStatus", cerhTipoStatus.getIdeCerhStatus()));
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
				);
		}
	}


	private void adicionarProjection(DetachedCriteria criteria) {
		criteria.setProjection(Projections.projectionList()
			.add(Projections.groupProperty("c.ideCerh"),"ideCerh")
			.add(Projections.groupProperty("c.dtcCadastro"),"dtcCadastro")
			.add(Projections.groupProperty("c.dtcExclusao"),"dtcExclusao")
			.add(Projections.groupProperty("c.indExcluido"),"indExcluido")
			.add(Projections.groupProperty("c.numCadastro"),"numCadastro")
			.add(Projections.groupProperty("cp.ideCerh"),"ideCerhPai.ideCerh")
			.add(Projections.groupProperty("cp.numCadastro"),"ideCerhPai.numCadastro")
			.add(Projections.groupProperty("cc.ideContratoConvenio"),"ideContratoConvenio.ideContratoConvenio")
			.add(Projections.groupProperty("cs.ideCerhStatus"),"ideCerhStatus.ideCerhStatus")
			.add(Projections.groupProperty("cs.dscStatus"),"ideCerhStatus.dscStatus")
			.add(Projections.groupProperty("emp.ideEmpreendimento"),"ideEmpreendimento.ideEmpreendimento")
			.add(Projections.groupProperty("emp.nomEmpreendimento"),"ideEmpreendimento.nomEmpreendimento")
			.add(Projections.groupProperty("end.ideEndereco"),"ideEmpreendimento.endereco.ideEndereco")
			.add(Projections.groupProperty("l.ideLogradouro"),"ideEmpreendimento.endereco.ideLogradouro.ideLogradouro")
			.add(Projections.groupProperty("m.ideMunicipio"),"ideEmpreendimento.endereco.ideLogradouro.ideMunicipio.ideMunicipio")
			.add(Projections.groupProperty("m.nomMunicipio"),"ideEmpreendimento.endereco.ideLogradouro.ideMunicipio.nomMunicipio")
			.add(Projections.groupProperty("r.idePessoa"),"idePessoaRequerente.idePessoa")
			.add(Projections.groupProperty("pf.nomPessoa"),"idePessoaRequerente.pessoaFisica.nomPessoa")
			.add(Projections.groupProperty("pf.numCpf"),"idePessoaRequerente.pessoaFisica.numCpf")
			.add(Projections.groupProperty("pj.nomRazaoSocial"),"idePessoaRequerente.pessoaJuridica.nomRazaoSocial")
			.add(Projections.groupProperty("pj.numCnpj"),"idePessoaRequerente.pessoaJuridica.numCnpj")
			.add(Projections.groupProperty("pfc.idePessoaFisica"),"idePessoaFisicaCadastro.idePessoaFisica")
			.add(Projections.groupProperty("cert.ideCertificado"),"ideCertificado.ideCertificado")
		);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getUltimoNumeroCadastro() {
		return cerhDAO.executarFuncaoNativeQuery("select max(substr(num_cadastro, 10, 6)) from cerh where substr(num_cadastro, 0, 5) = '" + Calendar.getInstance().get(Calendar.YEAR) + "'", null);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh buscarCerhDadosRequerente(Integer ide) {
		return 
			cerhDAO.buscarPorCriteria(
				DetachedCriteria.forClass(Cerh.class)
					.createAlias("idePessoaRequerente", "pr", JoinType.INNER_JOIN)
					.createAlias("pr.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
					.createAlias("pr.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
					
					.add(Restrictions.eq("ideCerh", ide))
					.setProjection(Projections.projectionList()
						.add(Projections.property("ideCerh"),"ideCerh")
						.add(Projections.property("pr.idePessoa"),"idePessoaRequerente.idePessoa")
						.add(Projections.property("pf.nomPessoa"),"idePessoaRequerente.pessoaFisica.nomPessoa")
						.add(Projections.property("pf.numCpf"),"idePessoaRequerente.pessoaFisica.numCpf")
						.add(Projections.property("pj.nomRazaoSocial"),"idePessoaRequerente.pessoaJuridica.nomRazaoSocial")
						.add(Projections.property("pj.numCnpj"),"idePessoaRequerente.pessoaJuridica.numCnpj")
						.add(Projections.property("pr.desEmail"),"idePessoaRequerente.desEmail")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(Cerh.class)));
	}

	
	
}
