package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnderecoEmpreendimentoService {
	
	@Inject
	private IDAO<EnderecoEmpreendimento> enderecoEmpreendimentoDAO;
	
	@EJB
	EnderecoService enderecoService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoEmpreendimento(EnderecoEmpreendimento enderecoEmpreendimento)  {
		enderecoEmpreendimentoDAO.salvarOuAtualizar(enderecoEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EnderecoEmpreendimento obterEnderecoEmpreendimento(Integer ideEmpreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimento.class)
				.createAlias("ideEmpreendimento", "emp", JoinType.INNER_JOIN)
				.createAlias("ideEndereco", "end", JoinType.INNER_JOIN)
				.createAlias("ideTipoEndereco", "t", JoinType.INNER_JOIN)
				.add(Restrictions.eq("emp.ideEmpreendimento", ideEmpreendimento))
				.add(Restrictions.eq("t.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideEnderecoEmpreendimento"), "ideEnderecoEmpreendimento")
					.add(Projections.property("end.ideEndereco"), "ideEndereco.ideEndereco")
					.add(Projections.property("emp.ideEmpreendimento"), "ideEmpreendimento.ideEmpreendimento")
					.add(Projections.property("t.ideTipoEndereco"), "ideTipoEndereco.ideTipoEndereco")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(EnderecoEmpreendimento.class))
		;
		
		return enderecoEmpreendimentoDAO.buscarPorCriteria(criteria);
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco filtrarEnderecoByEnderecoEmpreendimento(EnderecoEmpreendimento enderecoEmpreendimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimento.class,"ee");
		criteria.createAlias("ee.ideEndereco", "endereco");
		criteria.createAlias("ee.ideTipoEndereco", "tipoEndereco");
		
		criteria.add(Restrictions.eq("ee.ideEmpreendimento.ideEmpreendimento", enderecoEmpreendimento.getIdeEmpreendimento().getIdeEmpreendimento()));
		criteria.add(Restrictions.eq("ee.ideTipoEndereco.ideTipoEndereco", enderecoEmpreendimento.getIdeTipoEndereco().getIdeTipoEndereco()));
		
		EnderecoEmpreendimento tempEndereco = enderecoEmpreendimentoDAO.buscarPorCriteria(criteria);
		
		if(!Util.isNullOuVazio(tempEndereco)){
			return enderecoService.carregar(tempEndereco.getIdeEndereco().getIdeEndereco());
		}
		
		
		
		if (!Util.isNull(enderecoEmpreendimento) && !Util.isNull(enderecoEmpreendimento.getIdeEmpreendimento()) && !Util.isNull(enderecoEmpreendimento.getIdeTipoEndereco())) {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("ideEmpreendimento", enderecoEmpreendimento.getIdeEmpreendimento().getIdeEmpreendimento());
			parametros.put("ideTipoEndereco", enderecoEmpreendimento.getIdeTipoEndereco().getIdeTipoEndereco());
			EnderecoEmpreendimento lEnderecoEmpreendimento = enderecoEmpreendimentoDAO.buscarEntidadePorNamedQuery("EnderecoEmpreendimento.findByIdeEmpreendimentoIdeTipoEnderecoTipo", parametros);
			if (!Util.isNull(lEnderecoEmpreendimento)) {
				return lEnderecoEmpreendimento.getIdeEndereco();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnderecoEmpreendimento> filtrarEnderecoByEmpreendimento(EnderecoEmpreendimento enderecoEmpreendimento) {

		if (!Util.isNullOuVazio(enderecoEmpreendimento) && !Util.isNullOuVazio(enderecoEmpreendimento.getIdeEmpreendimento())) {

			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("ideEmpreendimento", enderecoEmpreendimento.getIdeEmpreendimento().getIdeEmpreendimento());

			return enderecoEmpreendimentoDAO.buscarPorNamedQuery("EnderecoEmpreendimento.findByIdeEmpreendimento", parametros);
		}

		return null;
	}
	
	/**
	 * Retorna uma lista de {@link EnderecoEmpreendimento} com os objetos carregados.
	 * 
	 * @param enderecoEmpreendimento
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnderecoEmpreendimento> filtraEnderecoEmpreendimentoByEmpreendimento(EnderecoEmpreendimento enderecoEmpreendimento)  {

		if (!Util.isNullOuVazio(enderecoEmpreendimento) && !Util.isNullOuVazio(enderecoEmpreendimento.getIdeEmpreendimento())) {
			
			DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimento.class, "ee")
					.createAlias("ee.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
					.createAlias("ee.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
					.createAlias("ee.ideEmpreendimento", "empreendimento", JoinType.LEFT_OUTER_JOIN)
					.add(Restrictions.eq("ee.ideEmpreendimento.ideEmpreendimento", enderecoEmpreendimento.getIdeEmpreendimento().getIdeEmpreendimento()));
			
			return enderecoEmpreendimentoDAO.listarPorCriteria(criteria);
		}

		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EnderecoEmpreendimento> filtraEnderecoEmpreendimentoByEmpreendimento(Empreendimento empreendimento)  {

		if (!Util.isNullOuVazio(empreendimento)) {
			
			DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimento.class, "ee")
					.createAlias("ee.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
					.createAlias("ee.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
					.createAlias("ee.ideEmpreendimento", "empreendimento", JoinType.LEFT_OUTER_JOIN)
					.add(Restrictions.eq("ee.ideEmpreendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
			
			return enderecoEmpreendimentoDAO.listarPorCriteria(criteria);
		}

		return null;
	}
	
	/**
	 * Carrega o ID do Endereco Empreendimento através da unique Empreendimento/Endereco/Tipo Endereco.
	 * 
	 * @param enderecoEmpreendimento
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EnderecoEmpreendimento carregarIDEnderecoEmpreendimentoExistente(EnderecoEmpreendimento enderecoEmpreendimento)  {

		if (!Util.isNullOuVazio(enderecoEmpreendimento) 
				&& !Util.isNullOuVazio(enderecoEmpreendimento.getIdeEmpreendimento())
				&& !Util.isNullOuVazio(enderecoEmpreendimento.getIdeEndereco())
				&& !Util.isNullOuVazio(enderecoEmpreendimento.getIdeTipoEndereco())) {
			
			DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimento.class, "ee")
					.createAlias("ee.ideEmpreendimento", "empreendimento", JoinType.LEFT_OUTER_JOIN)
					.createAlias("ee.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
					.createAlias("ee.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
					
					.add(Restrictions.eq("ee.ideEmpreendimento.ideEmpreendimento", enderecoEmpreendimento.getIdeEmpreendimento().getIdeEmpreendimento()))
					.add(Restrictions.eq("ee.ideEndereco.ideEndereco", enderecoEmpreendimento.getIdeEndereco().getIdeEndereco()))
					.add(Restrictions.eq("ee.ideTipoEndereco.ideTipoEndereco", enderecoEmpreendimento.getIdeTipoEndereco().getIdeTipoEndereco()));
			
			return enderecoEmpreendimentoDAO.buscarPorCriteria(criteria);
		}

		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarBloqueioDQC(Integer ideEmpreendimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimento.class)
			.createAlias("ideTipoEndereco", "t", JoinType.INNER_JOIN)
			.createAlias("ideEmpreendimento", "emp", JoinType.INNER_JOIN)
			.createAlias("ideEndereco", "e", JoinType.INNER_JOIN)
			.createAlias("e.ideLogradouro", "l", JoinType.INNER_JOIN)
			.createAlias("l.ideMunicipio", "m", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("emp.ideEmpreendimento", ideEmpreendimento))
			.add(Restrictions.eq("t.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
			.add(Restrictions.eq("m.indBloqueioDQC", true))
			
			.setProjection(Projections.property("ideEnderecoEmpreendimento").as("ideEnderecoEmpreendimento"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EnderecoEmpreendimento.class))
		;
		
		EnderecoEmpreendimento empreendimento = enderecoEmpreendimentoDAO.buscarPorCriteria(criteria);
		 
		return !Util.isNull(empreendimento);
		 
		
		 
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerEndereco(EnderecoEmpreendimento enderecoEmpreendimento) {
		enderecoEmpreendimentoDAO.remover(enderecoEmpreendimento);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EnderecoEmpreendimento obterEnderecoCorrespondenciaEmpreendimento(Integer ideEmpreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimento.class)
				.createAlias("ideEmpreendimento", "emp", JoinType.INNER_JOIN)
				.createAlias("ideEndereco", "end", JoinType.INNER_JOIN)
				.createAlias("ideTipoEndereco", "t", JoinType.INNER_JOIN)
				.add(Restrictions.eq("emp.ideEmpreendimento", ideEmpreendimento))
				.add(Restrictions.eq("t.ideTipoEndereco", TipoEnderecoEnum.CORRESPONDENCIA.getId()))
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideEnderecoEmpreendimento"), "ideEnderecoEmpreendimento")
					.add(Projections.property("end.ideEndereco"), "ideEndereco.ideEndereco")
					.add(Projections.property("emp.ideEmpreendimento"), "ideEmpreendimento.ideEmpreendimento")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(EnderecoEmpreendimento.class))
		;
		
		return enderecoEmpreendimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean enderecoLocalizacaoEmpreendimentoExists(Empreendimento empreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimento.class);
		criteria.createAlias("ideEmpreendimento", "empreendimento");
		criteria.createAlias("ideTipoEndereco", "tipoEndereco");
		criteria.add(Restrictions.eq("empreendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		criteria.add(Restrictions.eq("tipoEndereco.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()));
		
		List<EnderecoEmpreendimento> lista = enderecoEmpreendimentoDAO.listarPorCriteria(criteria);
		
		return !Util.isNullOuVazio(lista);
	}
}
