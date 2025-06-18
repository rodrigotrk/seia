package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
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

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.EnderecoEmpreendimentoMunicipio;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.service.EnderecoEmpreendimentoService;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnderecoEmpreendimentoMunicipioDAOImpl {
	
	@EJB
	private EnderecoEmpreendimentoService enderecoEmpreendimentoService;
	
	@Inject
	private IDAO<EnderecoEmpreendimentoMunicipio> enderecoEmpreendimentoMunicipioDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Empreendimento empreendimento, Collection<Municipio> listaMunicipio) throws Exception {
		EnderecoEmpreendimento ideEnderecoEmpreendimento = enderecoEmpreendimentoService.obterEnderecoEmpreendimento(empreendimento.getIdeEmpreendimento());
		for(Municipio ideMunicipio : listaMunicipio) {
			EnderecoEmpreendimentoMunicipio eem = new EnderecoEmpreendimentoMunicipio();
			eem.setIdeMunicipio(ideMunicipio);
			eem.setIdeEnderecoEmpreendimento(ideEnderecoEmpreendimento);
			enderecoEmpreendimentoMunicipioDAO.salvar(eem);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Municipio municipio, Empreendimento empreendimento) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("delete from EnderecoEmpreendimentoMunicipio eem where eem.ideEnderecoEmpreendimentoMunicipio in (");
		
		sql.append("select eem1.ideEnderecoEmpreendimentoMunicipio from EnderecoEmpreendimentoMunicipio eem1 ");
		sql.append("inner join eem1.ideEnderecoEmpreendimento ee ");
		sql.append("where eem.ideMunicipio=:ideMunicipio and ee.ideEmpreendimento=:ideEmpreendimento)");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideEmpreendimento", empreendimento);
		parametros.put("ideMunicipio", municipio);
		
		enderecoEmpreendimentoMunicipioDAO.executarQuery(sql.toString(), parametros);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EnderecoEmpreendimentoMunicipio buscarEnderecoEmpreendimentoMunicipio(Integer ideEmpreendimento, Integer ideMunicipio) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EnderecoEmpreendimentoMunicipio.class);
		criteria
			.createAlias("ideEnderecoEmpreendimento", "ee",JoinType.INNER_JOIN)
			.createAlias("ee.ideEmpreendimento", "e",JoinType.INNER_JOIN)
			.createAlias("ideMunicipio", "m",JoinType.INNER_JOIN)
			.add(Restrictions.eq("e.ideEmpreendimento", ideEmpreendimento))
			.add(Restrictions.eq("m.ideMunicipio", ideMunicipio))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideEnderecoEmpreendimentoMunicipio"),"ideEnderecoEmpreendimentoMunicipio")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(EnderecoEmpreendimentoMunicipio.class))
		;
		return enderecoEmpreendimentoMunicipioDAO.buscarPorCriteria(criteria);
	}
}