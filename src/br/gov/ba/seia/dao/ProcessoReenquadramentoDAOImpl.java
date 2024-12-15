package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TramitacaoReenquadramentoProcesso;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoReenquadramentoDAOImpl {

	@Inject
	private IDAO<ProcessoReenquadramento> processoReenquadramentoDAO;
	
	@Inject
	private PessoaDAOImpl pessoaDAOImpl;
	
	@Inject
	private IDAO<ProcessoReenquadramentoDTO> dtoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ProcessoReenquadramento processoReenquadramento)  {
		processoReenquadramentoDAO.salvarOuAtualizar(processoReenquadramento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ProcessoReenquadramento processoReenquadramento) {
		processoReenquadramentoDAO.atualizar(processoReenquadramento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoReenquadramento buscarUltimo()  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoReenquadramento.class);
		
		criteria
			.addOrder(Order.desc("ideProcessoReenquadramento"))
			.setProjection(
				Projections.projectionList()
				.add(Projections.property("ideProcessoReenquadramento"), "ideProcessoReenquadramento")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoReenquadramento.class))
		;
		
		List<ProcessoReenquadramento> listarPorCriteriaDemanda = processoReenquadramentoDAO.listarPorCriteriaDemanda(criteria, 0, 1);
		
		if(Util.isNullOuVazio(listarPorCriteriaDemanda)) {
			return null;
		}
		else {
			return listarPorCriteriaDemanda.get(0);
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcessoReenquadramentoDTO> listarProcessoReenquadramentoDTO(Map<String, Object> params, Integer first, Integer pageSize)  {
		DetachedCriteria criteria = getCriteria(params);
		criteria
			.addOrder(Order.desc("dtcFormacao"))
			.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("ideProcessoReenquadramento"), "processoReenquadramento.ideProcessoReenquadramento")
				.add(Projections.property("numProcesso"), "processoReenquadramento.numProcesso")
				.add(Projections.property("dtcFormacao"), "processoReenquadramento.dtcFormacao")
				.add(Projections.property("indExcluido"), "processoReenquadramento.indExcluido")
				.add(Projections.property("dtcExclusao"), "processoReenquadramento.dtcExclusao")
				.add(Projections.property("indAceiteRequerente"), "processoReenquadramento.indAceiteRequerente")
				.add(Projections.property("dtcAceiteRequerente"), "processoReenquadramento.dtcAceiteRequerente")
				.add(Projections.property("p.ideProcesso"), "processoReenquadramento.ideProcesso.ideProcesso")
				.add(Projections.property("p.numProcesso"), "processoReenquadramento.ideProcesso.numProcesso")
				.add(Projections.property("p.ideRequerimento.ideRequerimento"), "processoReenquadramento.ideProcesso.ideRequerimento.ideRequerimento")
				.add(Projections.property("org.ideOrgao"), "processoReenquadramento.ideProcesso.ideOrgao.ideOrgao")
				.add(Projections.property("org.dscOrgao"), "processoReenquadramento.ideProcesso.ideOrgao.dscOrgao")
				.add(Projections.property("org.dscSiglaOrgao"), "processoReenquadramento.ideProcesso.ideOrgao.dscSiglaOrgao")
				.add(Projections.property("sr.ideStatusReenquadramento"), "statusAtual.ideStatusReenquadramento")
				.add(Projections.property("sr.nomStatusReenquadramento"), "statusAtual.nomStatusReenquadramento")
				.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")
				.add(Projections.property("req.numRequerimento"), "requerimento.numRequerimento")
				.add(Projections.property("req.desCaminhoResumoOriginal"), "requerimento.desCaminhoResumoOriginal")
				.add(Projections.property("emp.ideEmpreendimento"), "empreendimento.ideEmpreendimento")
				.add(Projections.property("emp.nomEmpreendimento"), "empreendimento.nomEmpreendimento")
				.add(Projections.property("pe.idePessoa"), "requerente.idePessoa")
				.add(Projections.property("pf.idePessoaFisica"), "requerente.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"), "requerente.pessoaFisica.nomPessoa")
				.add(Projections.property("pf.numCpf"), "requerente.pessoaFisica.numCpf")
				.add(Projections.property("pj.idePessoaJuridica"), "requerente.pessoaJuridica.idePessoaJuridica")
				.add(Projections.property("pj.nomRazaoSocial"), "requerente.pessoaJuridica.nomRazaoSocial")
				.add(Projections.property("pj.numCnpj"), "requerente.pessoaJuridica.numCnpj"))
			
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoReenquadramentoDTO.class))
		;
		
		List<ProcessoReenquadramentoDTO> lista = dtoDAO.listarPorCriteriaDemanda(criteria, first, pageSize);
		
		if(!Util.isNullOuVazio(lista)) {
			for(ProcessoReenquadramentoDTO pr : lista) {
				carregarRequerente(pr);
			}
		}
		
		return lista;
		
	}

	private void carregarRequerente(ProcessoReenquadramentoDTO pr) {
		pr.setListaPessoaRequerimento(pessoaDAOImpl.listarPessoasRequerimento(pr.getRequerimento()));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countProcessoReenquadramentoDTO(Map<String, Object> params)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoReenquadramento.class);
		criteria
			.add(Property.forName("ideProcessoReenquadramento").in(
					getCriteria(params).setProjection(Projections.property("ideProcessoReenquadramento")))
			)
		;
		return dtoDAO.count(criteria);
	}

	private DetachedCriteria getCriteria(Map<String, Object> params) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoReenquadramento.class, "pr");
		criteria
			.createAlias("pr.tramitacaoReenquadramentoProcessoCollection", "trp", JoinType.INNER_JOIN)
			.createAlias("trp.ideStatusReenquadramento", "sr", JoinType.INNER_JOIN)
			
			.createAlias("pr.ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("p.ideOrgao", "org", JoinType.INNER_JOIN)
			.createAlias("p.ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("p.processoAtoCollection", "pa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pa.atoAmbiental", "aa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("aa.ideTipoAto", "ta", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pa.tipologia", "t", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("req.requerimentoPessoaCollection", "rp", JoinType.INNER_JOIN)
			.createAlias("rp.ideTipoPessoaRequerimento", "tpr", JoinType.INNER_JOIN)
			.createAlias("rp.pessoa", "pe", JoinType.INNER_JOIN)
			.createAlias("pe.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pe.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("req.requerimentoTipologiaCollection","rt", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("req.empreendimentoRequerimentoCollection", "er", JoinType.LEFT_OUTER_JOIN)
			.createAlias("er.ideEmpreendimento", "emp", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("rt.ideUnidadeMedidaTipologiaGrupo","umtg", JoinType.LEFT_OUTER_JOIN)
			.createAlias("umtg.ideTipologiaGrupo","tg", JoinType.LEFT_OUTER_JOIN)
			.createAlias("tg.ideTipologia","tipo", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("req.outorgaCollection","o", JoinType.LEFT_OUTER_JOIN)
            .createAlias("o.outorgaLocalizacaoGeograficaCollection","olg", JoinType.LEFT_OUTER_JOIN)
            .createAlias("olg.outorgaLocalizacaoGeograficaFinalidadeCollection","olgf", JoinType.LEFT_OUTER_JOIN)
            .createAlias("olgf.ideTipoFinalidadeUsoAgua", "tf", JoinType.LEFT_OUTER_JOIN)    
            
			.add(Restrictions.eq("tpr.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()))
			
			.add(Property.forName("trp.ideTramitacaoReenquadramentoProcesso").eq(
				DetachedCriteria.forClass(TramitacaoReenquadramentoProcesso.class,"trp2")
				.createAlias("trp2.ideProcessoReenquadramento", "pr2")
				.add(Restrictions.eqProperty("pr2.ideProcessoReenquadramento", "pr.ideProcessoReenquadramento"))
				.setProjection(Projections.max("ideTramitacaoReenquadramentoProcesso"))
			)
		);
			
		adicionarFiltros(params, criteria);
		
		return criteria;
	}

	private void adicionarFiltros(Map<String, Object> params, DetachedCriteria criteria) {
		
		if (!Util.isNull(params.get("numProcesso"))) {
			String numProcesso = ((String) params.get("numProcesso")).replace(" ", "");
			criteria.add(Restrictions.ilike("p.numProcesso", numProcesso, MatchMode.ANYWHERE));
		}
		
		if (!Util.isNull(params.get("periodoInicio")) && !Util.isNull(params.get("periodoFim"))) {
			criteria.add(Restrictions.between("dtcFormacao", params.get("periodoInicio"), params.get("periodoFim")));
		}
		
		if (!Util.isNull(params.get("periodoFim")) 
				&&( !Util.isNull(params.get("oldProcessos")) &&((Boolean) params.get("oldProcessos")) )) {
			criteria.add(Restrictions.lt("dtcFormacao", params.get("periodoFim")));
		}
		
		if (!Util.isNull(params.get("statusReenquadramento"))) {
			criteria.add(Restrictions.eq("sr.ideStatusReenquadramento", params.get("statusReenquadramento")));
		}
		if(!Util.isNull(params.get("tipoAto"))) {
			criteria.add(Restrictions.eq("ta.ideTipoAto", params.get("tipoAto")));
		}
		
		if(!Util.isNull(params.get("atoAmbiental"))) {
			criteria.add(Restrictions.eq("aa.ideAtoAmbiental", params.get("atoAmbiental")));
		}
		
		if(!Util.isNull(params.get("tipologia"))) {
			criteria.add(Restrictions.eq("t.ideTipologia", params.get("tipologia")));
		}
		
		if(!Util.isNull(params.get("finalidade"))) {
			criteria.add(Restrictions.eq("tf.ideTipoFinalidadeUsoAgua", params.get("finalidade")));
		}
		
		if(!Util.isNull(params.get("empreendimento"))) {
			criteria.add(Restrictions.eq("emp.id", 
					params.get("empreendimento")));
		}
		
		if(!Util.isNull(params.get("nomeRequerente"))) {
			Criterion fisicaCrt = Restrictions.like("pf.nomPessoa",
					params.get("nomeRequerente"));
			Criterion juridicaCrt = Restrictions.like("pj.nomRazaoSocial",
					params.get("nomeRequerente"));
			criteria.add(Restrictions.or(fisicaCrt, juridicaCrt));
			
		}
		
	
		if(!Util.isNull(params.get("cpfOrCnpjRequerente"))) {
			Criterion cnpjCrt = Restrictions.eq("pj.numCnpj", params.get("cpfOrCnpjRequerente"));
			Criterion cpfCrt = Restrictions.eq("pf.numCpf", params.get("cpfOrCnpjRequerente"));
			criteria
			.add(Restrictions.or(cnpjCrt, cpfCrt));
		}
		
		if(!Util.isNull(params.get("finalidade"))) {
			criteria.add(Restrictions.eq("tf.ideTipoFinalidadeUsoAgua", params.get("finalidade")));
		}
		
		if (!Util.isNull(params.get("tipologiaAtividade")) || !Util.isNullOuVazio(params.get("listaIdeTipologiaAtividade"))) {
			Tipologia tipologiaAtividade = (Tipologia) params.get("tipologiaAtividade");
			Collection<Integer> listaIdeTipologiaAtividade = Util.castCollection(params.get("listaIdeTipologiaAtividade"), Integer.class);
			criteria
				.add(Restrictions.eq("tg.indExcluido", false))
				.add(Restrictions.eq("tipo.indExcluido", false))
			;
			if(Util.isNull(tipologiaAtividade)) {
				criteria.add(Restrictions.in("tipo.ideTipologia", listaIdeTipologiaAtividade));
			}
			else{
				criteria.add(Restrictions.eq("tipo.ideTipologia", tipologiaAtividade.getIdeTipologia()));
			}
		}
		
		if (!Util.isNull(params.get("listaPessoas"))){
			criteria.add(Restrictions.in("pe.idePessoa", Util.castCollection(params.get("listaPessoas"), Integer.class)));
		}
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoReenquadramento processoReenquadramentoPorRequerimento(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoReenquadramento.class);
		
		criteria.createAlias("ideProcesso", "pr", JoinType.INNER_JOIN)
			.createAlias("pr.ideRequerimento", "re", JoinType.INNER_JOIN)
			.add(Restrictions.eq("re.ideRequerimento", requerimento.getIdeRequerimento()))
			.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("ideProcessoReenquadramento"), "ideProcessoReenquadramento")
				.add(Projections.property("pr.ideProcesso"), "ideProcesso.ideProcesso")
				
			))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoReenquadramento.class));
		
		criteria.addOrder(Order.desc("ideProcessoReenquadramento"));
		List<ProcessoReenquadramento> lista = processoReenquadramentoDAO.listarPorCriteria(criteria);
		
		if (!Util.isNullOuVazio(lista)){
			return lista.get(0);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoReenquadramento obterProcessoReenquadramentoPorProcesso(Integer ideProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoReenquadramento.class);
		
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("p.ideRequerimento", "r", JoinType.INNER_JOIN)
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("ideProcessoReenquadramento"), "ideProcessoReenquadramento")
				.add(Projections.property("numProcesso"), "numProcesso")
				.add(Projections.property("p.ideProcesso"), "ideProcesso.ideProcesso")
				.add(Projections.property("p.numProcesso"), "ideProcesso.numProcesso")
				.add(Projections.property("r.ideRequerimento"), "ideProcesso.ideRequerimento.ideRequerimento")
			))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoReenquadramento.class));
		
		List<ProcessoReenquadramento> lista = processoReenquadramentoDAO.listarPorCriteria(criteria, Order.desc("ideProcessoReenquadramento"));
		
		if (!Util.isNullOuVazio(lista)){
			return lista.get(0);
		}
		
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoReenquadramento obterPenultimoProcessoReenquadramentoPorProcesso(Processo ideProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoReenquadramento.class);
		
		criteria.createAlias("ideProcesso", "pr", JoinType.INNER_JOIN)
			.add(Restrictions.eq("pr.ideProcesso", ideProcesso.getIdeProcesso()))
			.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("ideProcessoReenquadramento"), "ideProcessoReenquadramento")
			))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoReenquadramento.class));
		
		List<ProcessoReenquadramento> lista = processoReenquadramentoDAO.listarPorCriteria(criteria, Order.desc("ideProcessoReenquadramento"));
		
		if (!Util.isNullOuVazio(lista) && !lista.isEmpty()){
	
				return lista.get(0);
		}
		
		return null;
	}
}