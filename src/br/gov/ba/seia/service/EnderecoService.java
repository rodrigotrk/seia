package br.gov.ba.seia.service;

import java.util.Date;
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
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.util.Log4jUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnderecoService {

	@Inject
	private IDAO<Endereco> enderecoDAO;

	@Inject IDAO<Municipio> municipioDAO;


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerEndereco(Endereco endereco) {
		enderecoDAO.remover(endereco);
	}


	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void salvarEndereco(Endereco endereco)  {
		endereco.setDtcCriacao(new Date());
		enderecoDAO.salvarOuAtualizar(endereco);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio retornarMunicipioByCep (Integer cep){
		Municipio municipio = new Municipio();
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(Municipio.class);
			criteria.add(Restrictions.eq("numCep", cep));
			municipio = municipioDAO.buscarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return municipio;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void salvarOrAtualizarEndereco(Endereco endereco)  {
		enderecoDAO.salvarOuAtualizar(endereco);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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


	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Endereco buscarEnderecoPorPessoa(Integer ideEndereco)  {

		DetachedCriteria criteria = getCriteriaEnderecoPorPessoa();

		criteria
			.add(Restrictions.eq("endereco.ideEndereco", ideEndereco))
			.add(Restrictions.eq("endereco.indExcluido", false))
		;

		return enderecoDAO.buscarPorCriteria(criteria);

	}

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

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Endereco obterPorNamedQuery(String namedQuery,Map<String, Object> parametros) {
		return enderecoDAO.obterPorNamedQuery(namedQuery, parametros);
	}

	/**
	 * Atualiza os dados básicos da entidade {@link Endereco} via native query.
	 */
	public void atualizarEnderecoSQL(Endereco e)  {
		String lSql = "UPDATE endereco SET num_endereco = :numEndereco, des_complemento = :desComplemento, des_ponto_referencia = :desPontoReferencia ";

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


}