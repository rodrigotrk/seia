package br.gov.ba.seia.dao;

import java.util.HashMap;
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

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnderecoDAOImpl {

	@Inject
	private IDAO<Endereco> enderecoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerEndereco(Endereco endereco) {
		enderecoDAO.remover(endereco);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEndereco(Endereco endereco) {
		enderecoDAO.salvarOuAtualizar(endereco);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco carregarEnderecoPor(Integer ideEmpreendimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Endereco.class);
		criteria
			.createAlias("ideLogradouro", "l", JoinType.INNER_JOIN)
			.createAlias("l.ideBairro", "b", JoinType.INNER_JOIN)
			.createAlias("l.ideTipoLogradouro", "tl", JoinType.INNER_JOIN)
			.createAlias("b.ideMunicipio", "m", JoinType.INNER_JOIN)
			.createAlias("m.ideEstado", "uf", JoinType.INNER_JOIN)
			.createAlias("uf.idePais", "p", JoinType.INNER_JOIN)
			.createAlias("enderecoEmpreendimentoCollection", "ee", JoinType.INNER_JOIN)
			.createAlias("ee.ideTipoEndereco", "te", JoinType.INNER_JOIN)
			.createAlias("ee.ideEmpreendimento", "e", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("e.ideEmpreendimento", ideEmpreendimento))
			.add(Restrictions.eq("te.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEndereco"),"ideEndereco")
				.add(Projections.property("numEndereco"),"numEndereco")
				.add(Projections.property("indExcluido"),"indExcluido")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")
				.add(Projections.property("dtcExclusao"),"dtcExclusao")
				.add(Projections.property("desComplemento"),"desComplemento")
				.add(Projections.property("desPontoReferencia"),"desPontoReferencia")
				.add(Projections.property("l.ideLogradouro"),"ideLogradouro.ideLogradouro")
				.add(Projections.property("l.nomLogradouro"),"ideLogradouro.nomLogradouro")
				.add(Projections.property("l.numCep"),"ideLogradouro.numCep")
				.add(Projections.property("l.indOrigemCorreio"),"ideLogradouro.indOrigemCorreio")
				.add(Projections.property("l.indOrigemApi"),"ideLogradouro.indOrigemApi")
				.add(Projections.property("tl.ideTipoLogradouro"),"ideLogradouro.ideTipoLogradouro.ideTipoLogradouro")
				.add(Projections.property("tl.sglTipoLogradouro"),"ideLogradouro.ideTipoLogradouro.sglTipoLogradouro")
				.add(Projections.property("tl.nomTipoLogradouro"),"ideLogradouro.ideTipoLogradouro.nomTipoLogradouro")
				.add(Projections.property("b.ideBairro"),"ideLogradouro.ideBairro.ideBairro")
				.add(Projections.property("b.nomBairro"),"ideLogradouro.ideBairro.nomBairro")
				.add(Projections.property("m.ideMunicipio"),"ideLogradouro.ideBairro.ideMunicipio.ideMunicipio")
				.add(Projections.property("m.nomMunicipio"),"ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")
				.add(Projections.property("m.numCep"),"ideLogradouro.ideBairro.ideMunicipio.numCep")
				.add(Projections.property("uf.ideEstado"),"ideLogradouro.ideBairro.ideMunicipio.ideEstado.ideEstado")
				.add(Projections.property("uf.nomEstado"),"ideLogradouro.ideBairro.ideMunicipio.ideEstado.nomEstado")
				.add(Projections.property("uf.desSigla"),"ideLogradouro.ideBairro.ideMunicipio.ideEstado.desSigla")
				.add(Projections.property("p.idePais"),"ideLogradouro.ideBairro.ideMunicipio.ideEstado.idePais.idePais")
				.add(Projections.property("p.sglPais"),"ideLogradouro.ideBairro.ideMunicipio.ideEstado.idePais.sglPais")
				.add(Projections.property("p.nomPais"),"ideLogradouro.ideBairro.ideMunicipio.ideEstado.idePais.nomPais")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Endereco.class))
		;
		
		return enderecoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco carregar(Integer ideEndereco)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(Endereco.class, "endereco");
		criteria.createAlias("endereco.ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("municipio.ideEstado", "estado", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("estado.idePais", "pais", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.ideBairro", "bairro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.ideTipoLogradouro", "tipo", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("endereco.ideEndereco", ideEndereco));

		return enderecoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco buscarEnderecoPorPessoa(Integer ideEndereco)  {

		DetachedCriteria criteria = getCriteriaEnderecoPorPessoa();
				
		criteria
			.add(Restrictions.eq("endereco.ideEndereco", ideEndereco))
			.add(Restrictions.eq("endereco.indExcluido", false))
		;

		return enderecoDAO.buscarPorCriteria(criteria);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private DetachedCriteria getCriteriaEnderecoPorPessoa() {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Endereco.class);
		
		criteria
			.createAlias("ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN)
			.createAlias("logradouro.ideTipoLogradouro", "tipo", JoinType.LEFT_OUTER_JOIN)
			.createAlias("logradouro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN)
			.createAlias("logradouro.ideBairro", "bairro", JoinType.LEFT_OUTER_JOIN)
			.createAlias("municipio.ideEstado", "estado", JoinType.LEFT_OUTER_JOIN)
			.createAlias("estado.idePais", "pais", JoinType.LEFT_OUTER_JOIN)
			.createAlias("enderecoPessoaCollection", "endPessoa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("endPessoa.idePessoa", "pessoa", JoinType.LEFT_OUTER_JOIN)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEndereco"),"ideEndereco")
				.add(Projections.property("numEndereco"),"numEndereco")
				.add(Projections.property("indExcluido"),"indExcluido")
				.add(Projections.property("desComplemento"),"desComplemento")
				.add(Projections.property("desPontoReferencia"),"desPontoReferencia")
				.add(Projections.property("logradouro.ideLogradouro"),"ideLogradouro.ideLogradouro")
				.add(Projections.property("logradouro.nomLogradouro"),"ideLogradouro.nomLogradouro")
				.add(Projections.property("logradouro.numCep"),"ideLogradouro.numCep")
				.add(Projections.property("bairro.ideBairro"),"ideLogradouro.ideBairro.ideBairro")
				.add(Projections.property("bairro.nomBairro"),"ideLogradouro.ideBairro.nomBairro")
				.add(Projections.property("municipio.ideMunicipio"),"ideLogradouro.ideMunicipio.ideMunicipio")
				.add(Projections.property("municipio.nomMunicipio"),"ideLogradouro.ideMunicipio.nomMunicipio")
				.add(Projections.property("estado.ideEstado"),"ideLogradouro.ideMunicipio.ideEstado.ideEstado")
				.add(Projections.property("estado.nomEstado"),"ideLogradouro.ideMunicipio.ideEstado.nomEstado")
				.add(Projections.property("estado.desSigla"),"ideLogradouro.ideMunicipio.ideEstado.desSigla")
			)
		;
		
		return criteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco obterPorNamedQuery(String namedQuery,Map<String, Object> parametros) {
		return enderecoDAO.obterPorNamedQuery(namedQuery, parametros);
	}

	/**
	 * Atualiza os dados básicos da entidade {@link Endereco} via native query.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarEnderecoSQL(Endereco e)  {
		String lSql = new String("UPDATE endereco SET num_endereco = :numEndereco, des_complemento = :desComplemento, des_ponto_referencia = :desPontoReferencia ");
		
		if(e.getIndExcluido()) {
			lSql = lSql.concat(", ind_excluido = :indExcluido, dtc_exclusao = :dtcExclusao ");
		}
		
		lSql = lSql.concat(" WHERE ide_endereco = :ideEndereco "); 
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("numEndereco", e.getNumEndereco());
		
		if(e.getIndExcluido()) {
			params.put("indExcluido", e.getIndExcluido());
			params.put("dtcExclusao", e.getDtcExclusao());
		}
		
		params.put("desComplemento", e.getDesComplemento());
		params.put("desPontoReferencia", e.getDesPontoReferencia());
		params.put("ideEndereco", e.getIdeEndereco());
		
		enderecoDAO.executarNativeQuery(lSql, params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco buscarEnderecoPor(Empreendimento empreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimento.class);

		criteria
			.createAlias("ideEmpreendimento", "emp", JoinType.INNER_JOIN)
			.createAlias("ideTipoEndereco", "te", JoinType.INNER_JOIN)
			.createAlias("ideEndereco", "end", JoinType.INNER_JOIN)
			.createAlias("end.ideLogradouro","l")
			.createAlias("l.ideBairro","b")
			.createAlias("l.ideMunicipio","m")
			.createAlias("m.ideEstado","est")
		
			.add(Restrictions.eq("ideEmpreendimento", empreendimento))
			.add(Restrictions.eq("te.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))

			.setProjection(Projections.projectionList()
				.add(Projections.property("end.ideEndereco"),"ideEndereco")
				.add(Projections.property("end.numEndereco"),"numEndereco")
				.add(Projections.property("end.desComplemento"),"desComplemento")

				.add(Projections.property("l.ideLogradouro"),"ideLogradouro.ideLogradouro")
				.add(Projections.property("l.nomLogradouro"),"ideLogradouro.nomLogradouro")
				.add(Projections.property("l.numCep"),"ideLogradouro.numCep")

				.add(Projections.property("b.ideBairro"),"ideLogradouro.ideBairro.ideBairro")
				.add(Projections.property("b.nomBairro"),"ideLogradouro.ideBairro.nomBairro")

				.add(Projections.property("m.ideMunicipio"),"ideLogradouro.ideBairro.ideMunicipio.ideMunicipio")
				.add(Projections.property("m.nomMunicipio"),"ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")
				.add(Projections.property("m.coordGeobahiaMunicipio"),"ideLogradouro.ideBairro.ideMunicipio.coordGeobahiaMunicipio")

				.add(Projections.property("est.ideEstado"),"ideLogradouro.ideBairro.ideMunicipio.ideEstado.ideEstado")
				.add(Projections.property("est.nomEstado"),"ideLogradouro.ideBairro.ideMunicipio.ideEstado.nomEstado")
				.add(Projections.property("est.desSigla"),"ideLogradouro.ideBairro.ideMunicipio.ideEstado.desSigla")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Endereco.class));

		return enderecoDAO.buscarPorCriteria(criteria);
	}

}