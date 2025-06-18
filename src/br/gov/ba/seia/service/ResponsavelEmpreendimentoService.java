package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ResponsavelEmpreendimentoService {
	
	@Inject	
	IDAO<ResponsavelEmpreendimento> daoResponsavelEmpreendimento;
	
	@Inject
	IDAO<Empreendimento> daoEmpreendimento;
	
	@Inject
	IDAO<PessoaFisica> daoPessoaFisica;
	
	@Inject
	IDAO<Pessoa> daoPessoa;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ResponsavelEmpreendimento salvarOuAtualizarResponsavelEmpreendimento(ResponsavelEmpreendimento responsavel)  {
		
		if (Util.isNullOuVazio(responsavel.getIdePessoaFisica().getPessoa())) {
			responsavel.getIdePessoaFisica().getPessoa().setDtcCriacao(new Date());
		}
		
		daoPessoa.salvarOuAtualizar(responsavel.getIdePessoaFisica().getPessoa());
		daoPessoaFisica.salvarOuAtualizar(responsavel.getIdePessoaFisica());
		daoResponsavelEmpreendimento.salvarOuAtualizar(responsavel);
		
		return responsavel;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ResponsavelEmpreendimento> filtrarResponsaveisPorEmpreendimentoComProjection(Empreendimento empreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResponsavelEmpreendimento.class,"respEmpreendimento");
		criteria.createAlias("respEmpreendimento.idePessoaFisica", "pessoaFisica");
		criteria.createAlias("pessoaFisica.pessoa", "pessoa");
		criteria.createAlias("respEmpreendimento.ideEmpreendimento", "ideEmpreendimento");
		criteria.add(Restrictions.eq("ideEmpreendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		criteria.add(Restrictions.eq("respEmpreendimento.indExcluido", false));
		criteria.addOrder(Order.asc("pessoaFisica.idePessoaFisica"));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("respEmpreendimento.ideResponsavelEmpreendimento"),"ideResponsavelEmpreendimento")
				.add(Projections.property("pessoa.desEmail"),"idePessoaFisica.pessoa.desEmail")
				.add(Projections.property("ideEmpreendimento.nomEmpreendimento"),"ideEmpreendimento.nomEmpreendimento")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(ResponsavelEmpreendimento.class));
		
		return daoResponsavelEmpreendimento.listarPorCriteria(criteria);
		/**
		
		Empreendimento tempEmpreendimento =  daoEmpreendimento.carregarGet(empreendimento.getIdeEmpreendimento());		
		List<ResponsavelEmpreendimento> lista = (List<ResponsavelEmpreendimento>) tempEmpreendimento.getResponsavelEmpreendimentoCollection();
		Hibernate.initialize(lista);
		return 	lista; **/
	}
	
	/**
	 * Remove logicamente o Reponsavel Tecnico selecionado, verificando se o requerimento deste empreendimento nao esta aberto. 
	 * @param respon
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerResponsavel(ResponsavelEmpreendimento respon)  {
		
		respon.setIndExcluido(true);
		respon.setDtcExclusao(new Date());
		
		daoResponsavelEmpreendimento.atualizar(respon);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ResponsavelEmpreendimento> listarResponsaveisPorEmpreendimento(Empreendimento empreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResponsavelEmpreendimento.class,"respEmpreendimento")
			.createAlias("respEmpreendimento.idePessoaFisica", "pessoaFisica")
			.createAlias("respEmpreendimento.ideEmpreendimento", "ideEmpreendimento")
			.createAlias("pessoaFisica.ideEscolaridade", "escolaridade", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideEmpreendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()))
			.add(Restrictions.eq("respEmpreendimento.indExcluido", false))
			
			.addOrder(Order.asc("pessoaFisica.idePessoaFisica"));
		
		return daoResponsavelEmpreendimento.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ResponsavelEmpreendimento buscarResponsavelEmpreendimentoPorId(Integer ideResponsavelEmpreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResponsavelEmpreendimento.class)
			.createAlias("idePessoaFisica", "resp")
			.createAlias("resp.idePais", "pais")
			.createAlias("resp.ideEscolaridade", "esc",JoinType.LEFT_OUTER_JOIN)
			.createAlias("resp.pessoa", "pes")
			.createAlias("ideEmpreendimento", "emp")
		
		.setProjection(Projections.projectionList()
			.add(Projections.property("ideResponsavelEmpreendimento"),"ideResponsavelEmpreendimento")
			.add(Projections.property("dtcCriacao"),"dtcCriacao")
			.add(Projections.property("numART"),"numART")
			.add(Projections.property("dscCaminhoArquivoART"),"dscCaminhoArquivoART")
			
			.add(Projections.property("resp.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
			.add(Projections.property("resp.nomPessoa"),"idePessoaFisica.nomPessoa")
			.add(Projections.property("resp.numCpf"),"idePessoaFisica.numCpf")
			.add(Projections.property("resp.ideEstadoCivil.ideEstadoCivil"),"idePessoaFisica.ideEstadoCivil.ideEstadoCivil")
			.add(Projections.property("resp.ideOcupacao.ideOcupacao"),"idePessoaFisica.ideOcupacao.ideOcupacao")
			.add(Projections.property("resp.tipSexo"),"idePessoaFisica.tipSexo")
			.add(Projections.property("resp.dtcNascimento"),"idePessoaFisica.dtcNascimento")
			.add(Projections.property("resp.desNaturalidade"),"idePessoaFisica.desNaturalidade")
			.add(Projections.property("resp.nomPai"),"idePessoaFisica.nomPai")
			.add(Projections.property("resp.nomMae"),"idePessoaFisica.nomMae")
			
			.add(Projections.property("pais.idePais"),"idePessoaFisica.idePais.idePais")
							
			.add(Projections.property("esc.ideEscolaridade"),"idePessoaFisica.ideEscolaridade.ideEscolaridade")
			
			.add(Projections.property("pes.idePessoa"),"idePessoaFisica.pessoa.idePessoa")
			.add(Projections.property("pes.desEmail"),"idePessoaFisica.pessoa.desEmail")
			.add(Projections.property("pes.dtcCriacao"),"idePessoaFisica.pessoa.dtcCriacao")
		).setResultTransformer(new AliasToNestedBeanResultTransformer(ResponsavelEmpreendimento.class))
		
		.add(Restrictions.eq("this.ideResponsavelEmpreendimento", ideResponsavelEmpreendimento));
		
		return daoResponsavelEmpreendimento.buscarPorCriteria(criteria);	
	}
	
	public List<ResponsavelEmpreendimento> listarResponsavelEmpreendimentoPor(Integer ideEmpreendimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ResponsavelEmpreendimento.class)
			
			.createAlias("idePessoaFisica", "pf", JoinType.INNER_JOIN)
			.createAlias("ideEmpreendimento", "e", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("this.ideResponsavelEmpreendimento", ideEmpreendimento))
	
			.setProjection(Projections.projectionList()
					.add(Projections.property("ideResponsavelEmpreendimento"),"ideResponsavelEmpreendimento")
					.add(Projections.property("dtcCriacao"),"dtcCriacao")
					.add(Projections.property("indExcluido"),"indExcluido")
					.add(Projections.property("dtcExclusao"),"dtcExclusao")
					.add(Projections.property("numART"),"numART")
					.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
					.add(Projections.property("pf.nomPessoa"),"idePessoaFisica.nomPessoa")
			)
	
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ResponsavelEmpreendimento.class))
		;
		
		return	daoResponsavelEmpreendimento.listarPorCriteria(criteria);	
	}
	
	public List<ResponsavelEmpreendimento> listaResponsavelEmpreendimentoPorRequerimentoComArt(Requerimento ideRequerimento, Empreendimento ideEmpreendimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ResponsavelEmpreendimento.class, "rE");
		
		criteria.createAlias("rE.ideEmpreendimento", "emp", JoinType.INNER_JOIN);
		criteria.createAlias("emp.empreendimentoRequerimentoCollection", "empReq", JoinType.INNER_JOIN);
		criteria.createAlias("rE.idePessoaFisica", "pF", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("rE.ideEmpreendimento.ideEmpreendimento", ideEmpreendimento.getIdeEmpreendimento()));
		
		criteria.add(Restrictions.eq("empReq.ideRequerimento.ideRequerimento", ideRequerimento.getIdeRequerimento()));
		
		criteria.add(Restrictions.eq("rE.indExcluido", Boolean.FALSE));
		
		criteria.add(Restrictions.isNotNull("rE.dscCaminhoArquivoART"));
		
		return daoResponsavelEmpreendimento.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ResponsavelEmpreendimento> listarPorCadastroAtividadeNaoSujeitaLic(Integer ideCadastroAtividadeNaoSujeitaLic) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResponsavelEmpreendimento.class);
		criteria.createAlias("cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento", "cadastroResponsavelEmpreendimento");
		criteria.createAlias("cadastroResponsavelEmpreendimento.ideCadastroAtividadeNaoSujeitaLic", "cadastroAtividade");
		criteria.createAlias("idePessoaFisica", "pessoaFisica");
		criteria.add(Restrictions.eq("cadastroAtividade.ideCadastroAtividadeNaoSujeitaLic", ideCadastroAtividadeNaoSujeitaLic));
		
		return daoResponsavelEmpreendimento.listarPorCriteria(criteria);
	}
}
