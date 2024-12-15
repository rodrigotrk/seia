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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ResponsavelImovelRural;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ResponsavelImovelRuralService {

	@Inject	
	IDAO<ResponsavelImovelRural> daoResponsavelImovelRural;
	@Inject
	IDAO<ImovelRural> daoImovelRural;
	@Inject
	IDAO<PessoaFisica> daoPessoaFisica;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ResponsavelImovelRural salvarOuAtualizarResponsavelImovelRural(ResponsavelImovelRural responsavel)  {
		if (Util.isNullOuVazio(responsavel.getIdePessoaFisica().getPessoa().getIdePessoa())) {
			responsavel.getIdePessoaFisica().getPessoa().setDtcCriacao(new Date());
		}
		daoPessoaFisica.salvarOuAtualizar(responsavel.getIdePessoaFisica());
		daoResponsavelImovelRural.salvarOuAtualizar(responsavel);
		return responsavel;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ResponsavelImovelRural> filtrarResponsaveisPorImovelRural(ImovelRural imovelRural)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResponsavelImovelRural.class,"respImovelRural");
		criteria.createAlias("respImovelRural.idePessoaFisica", "resp");
		criteria.createAlias("respImovelRural.ideImovelRural", "ideImovelRural");
		criteria.createAlias("resp.ideEscolaridade", "esc",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideDocumentoResponsavel", "docRes",JoinType.LEFT_OUTER_JOIN);		
		criteria.add(Restrictions.eq("ideImovelRural.ideImovelRural", imovelRural.getIdeImovelRural()));
		criteria.add(Restrictions.eq("respImovelRural.indExcluido", false));
		criteria.addOrder(Order.asc("resp.idePessoaFisica"));
		
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideDocumentoResponsavel"),"ideDocumentoResponsavel")
				.add(Projections.property("ideImovelRural"),"ideImovelRural")
				.add(Projections.property("ideResponsavelImovelRural"),"ideResponsavelImovelRural")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")

				.add(Projections.property("resp.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("resp.nomPessoa"),"idePessoaFisica.nomPessoa")
				.add(Projections.property("resp.numCpf"),"idePessoaFisica.numCpf")
				.add(Projections.property("resp.tipSexo"),"idePessoaFisica.tipSexo")
				.add(Projections.property("resp.dtcNascimento"),"idePessoaFisica.dtcNascimento")
				.add(Projections.property("resp.desNaturalidade"),"idePessoaFisica.desNaturalidade")
				.add(Projections.property("resp.usuario.idePessoaFisica"),"idePessoaFisica.usuario.idePessoaFisica")
				
				.add(Projections.property("docRes.ideDocumentoImovelRural"),"ideDocumentoResponsavel.ideDocumentoImovelRural")
				.add(Projections.property("docRes.nomDocumentoObrigatorio"),"ideDocumentoResponsavel.nomDocumentoObrigatorio")
				.add(Projections.property("docRes.dscCaminhoArquivo"),"ideDocumentoResponsavel.dscCaminhoArquivo")
				.add(Projections.property("docRes.imovelRural"),"ideDocumentoResponsavel.imovelRural")
				.add(Projections.property("docRes.documentoImovelRuralPosse"),"ideDocumentoResponsavel.documentoImovelRuralPosse")
				
				.add(Projections.property("esc.ideEscolaridade"),"idePessoaFisica.ideEscolaridade.ideEscolaridade")

				).setResultTransformer(new AliasToNestedBeanResultTransformer(ResponsavelImovelRural.class));
		return daoResponsavelImovelRural.listarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerResponsavel(ResponsavelImovelRural respon)  {
		daoResponsavelImovelRural.remover(respon);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ResponsavelImovelRural filtrarResponsavelImovelRuralById(Integer ideResponsavelImovelRural)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResponsavelImovelRural.class)
			.createAlias("idePessoaFisica", "resp")
			.createAlias("resp.idePais", "pais", JoinType.LEFT_OUTER_JOIN)
			.createAlias("resp.ideEscolaridade", "esc", JoinType.LEFT_OUTER_JOIN)
			.createAlias("resp.pessoa", "pes")
			.createAlias("ideImovelRural", "ir")
			.createAlias("resp.funcionario", "func", JoinType.LEFT_OUTER_JOIN)
			.createAlias("resp.usuario", "usuario", JoinType.LEFT_OUTER_JOIN);
		
		/*criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideDocumentoResponsavel"),"ideDocumentoResponsavel")
				.add(Projections.property("ideImovelRural"),"ideImovelRural")
				
				.add(Projections.property("ideResponsavelImovelRural"),"ideResponsavelImovelRural")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")

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
				.add(Projections.property("resp.usuario.idePessoaFisica"),"idePessoaFisica.usuario.idePessoaFisica")
				.add(Projections.property("func.ideArea.ideArea"),"idePessoaFisica.funcionario.ideArea.ideArea")
				.add(Projections.property("usuario.dscLogin"),"idePessoaFisica.usuario.dscLogin")
				.add(Projections.property("usuario.dscSenha"),"idePessoaFisica.usuario.dscSenha")
				.add(Projections.property("usuario.idePerfil.idePerfil"),"idePessoaFisica.usuario.idePerfil.idePerfil")
				
				.add(Projections.property("pais.idePais"),"idePessoaFisica.idePais.idePais")
								
				.add(Projections.property("esc.ideEscolaridade"),"idePessoaFisica.ideEscolaridade.ideEscolaridade")
				
				.add(Projections.property("pes.idePessoa"),"idePessoaFisica.pessoa.idePessoa")
				.add(Projections.property("pes.desEmail"),"idePessoaFisica.pessoa.desEmail")
		).setResultTransformer(new AliasToNestedBeanResultTransformer(ResponsavelImovelRural.class));*/
		
		criteria.add(Restrictions.eq("this.ideResponsavelImovelRural", ideResponsavelImovelRural));
		
		ResponsavelImovelRural resp = daoResponsavelImovelRural.buscarPorCriteria(criteria);
		return resp;		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ResponsavelImovelRural filtrarResponsaveisPorDocumento(DocumentoImovelRural documentoImovelRural)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResponsavelImovelRural.class,"respImovelRural");
		criteria.createAlias("respImovelRural.idePessoaFisica", "pessoaFisica");
		criteria.createAlias("respImovelRural.ideImovelRural", "ideImovelRural");
		criteria.createAlias("pessoaFisica.ideEscolaridade", "escolaridade",JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideDocumentoResponsavel.ideDocumentoImovelRural", documentoImovelRural.getIdeDocumentoObrigatorio()));
		criteria.add(Restrictions.eq("respImovelRural.indExcluido", false));
		
		return daoResponsavelImovelRural.buscarPorCriteria(criteria);	
	}
	
	/**
	 * Método para retornar a lista {@link ResponsavelImovelRural} ativo com sua PK.
	 * 
	 * @author eduardo.fernandes
	 * @param idePessoaFisica
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ResponsavelImovelRural> listarResponsaveisPorIdePessoaFisica(Integer idePessoaFisica)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResponsavelImovelRural.class)
				.createAlias("idePessoaFisica", "pf")
				.add(Restrictions.eq("pf.idePessoaFisica", idePessoaFisica))
				.add(Restrictions.eq("indExcluido", false))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideResponsavelImovelRural"),"ideResponsavelImovelRural")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(ResponsavelImovelRural.class));
		return daoResponsavelImovelRural.listarPorCriteria(criteria);	
	}
}
