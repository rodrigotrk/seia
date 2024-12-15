package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.Util;

public class ComprovantePagamentoDaeImpl {
	
	@Inject
	IDAO<ComprovantePagamentoDae> comprovantePagamentoDaeDAO;
	
	public void salvarOuAtualizar(ComprovantePagamentoDae comprovantePagamentoDae) {
		comprovantePagamentoDaeDAO.salvarOuAtualizar(comprovantePagamentoDae);
	}
	
	public void remover(ComprovantePagamentoDae comprovantePagamentoDae) {
		comprovantePagamentoDaeDAO.remover(comprovantePagamentoDae);
	}
	
	public List<ComprovantePagamentoDae> obterPorRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamentoDae.class,"c1");
		criteria.createAlias("c1.ideBoletoDaeRequerimento", "boleto");
		criteria.createAlias("boleto.ideRequerimento", "requerimento");
		criteria.createAlias("idePessoaUpload", "idePessoa");
		criteria.add(Restrictions.eq("requerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		return comprovantePagamentoDaeDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ComprovantePagamentoDae>  obterPorIdRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamentoDae.class,"c1")
				.createAlias("c1.ideBoletoDaeRequerimento", "bpr", JoinType.INNER_JOIN)
				.createAlias("bpr.ideRequerimento", "req", JoinType.INNER_JOIN)
				.createAlias("c1.idePessoaValidacao", "idePessoaValidacao", JoinType.LEFT_OUTER_JOIN)
				.createAlias("c1.idePessoaValidacao.pessoaFisica", "pessoaFisica", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("req.ideRequerimento",ideRequerimento));

		return comprovantePagamentoDaeDAO.listarPorCriteria(criteria);
	}
	
	public Collection<ComprovantePagamentoDae> listarByIdeRequerimento(Integer ideRequerimento, Integer ideBoletoDaeReq)  {
		Collection<ComprovantePagamentoDae> comprovantes = new ArrayList<ComprovantePagamentoDae>();
		
		ComprovantePagamentoDae certificado = this.carregarCertificadoByIdeRequerimento(ideRequerimento, ideBoletoDaeReq);
		if(!Util.isNullOuVazio(certificado))
			comprovantes.add(certificado);
		
		ComprovantePagamentoDae vistoria = this.carregarVistoriaByIdeRequerimento(ideRequerimento, ideBoletoDaeReq);
		if(!Util.isNullOuVazio(vistoria))
			comprovantes.add(vistoria);
		
		return comprovantes;
	}

	public ComprovantePagamentoDae carregarVistoriaByIdeRequerimento(Integer ideRequerimento, Integer ideBoletoDaeReq){
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamentoDae.class,"c1");
		criteria.createAlias("c1.ideBoletoDaeRequerimento", "boleto");
		criteria.createAlias("boleto.ideRequerimento", "requerimento");
		criteria.createAlias("c1.idePessoaUpload", "idePessoa");
		
		criteria.add(Restrictions.isNotNull("boleto.vlrTotalVistoria"));
		criteria.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));
		criteria.add(Restrictions.eq("boleto.ideBoletoDaeRequerimento", ideBoletoDaeReq));
		
		return comprovantePagamentoDaeDAO.buscarPorCriteria(criteria);
	}

	public ComprovantePagamentoDae carregarCertificadoByIdeRequerimento(Integer ideRequerimento, Integer ideBoletoDaeReq) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamentoDae.class,"c1");
		criteria.createAlias("c1.ideBoletoDaeRequerimento", "boleto");
		criteria.createAlias("boleto.ideRequerimento", "requerimento");
		criteria.add(Restrictions.isNotNull("boleto.vlrTotalCertificado"));
		criteria.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));
		criteria.add(Restrictions.eq("boleto.ideBoletoDaeRequerimento", ideBoletoDaeReq));
				
		return comprovantePagamentoDaeDAO.buscarPorCriteria(criteria);
	}
	
	public ComprovantePagamentoDae carregarCertificadoByIdeRequerimentoDae(Integer ideRequerimento, Integer ideBoletoDaeReq) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamentoDae.class,"c1");
		criteria.createAlias("c1.ideBoletoDaeRequerimento", "boleto");
		criteria.createAlias("boleto.ideRequerimento", "requerimento");
		
		criteria.add(Restrictions.isNotNull("boleto.vlrTotalCertificado"));
		criteria.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));
		criteria.add(Restrictions.eq("boleto.ideBoletoDaeRequerimento", ideBoletoDaeReq));
		
		List<ComprovantePagamentoDae> lista = comprovantePagamentoDaeDAO.listarPorCriteria(criteria, Order.desc("ideComprovantePagamentoDae"));
		
		if(Util.isNullOuVazio(lista))
			return null;
		else
			return lista.get(0);
	}
	
	public List<ComprovantePagamentoDae> consultarPorIdBoletoDaeRequerimento(Integer idBoletoDaeRequerimento) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("idBoletoDaeRequerimento", idBoletoDaeRequerimento);
		return comprovantePagamentoDaeDAO.buscarPorNamedQuery("ComprovantePagamentoDae.findByIdBoletoDaeRequerimento", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ComprovantePagamentoDae>  obterPorIdeProcessoReenquadramento(Integer ideProcessoReenquadramento){
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamentoDae.class,"c1")
				.createAlias("c1.ideBoletoDaeRequerimento", "bpr", JoinType.INNER_JOIN)
				.createAlias("bpr.ideProcessoReenquadramento", "reenquadramento", JoinType.INNER_JOIN)
				.createAlias("c1.idePessoaValidacao", "idePessoaValidacao", JoinType.LEFT_OUTER_JOIN)
				.createAlias("c1.idePessoaValidacao.pessoaFisica", "pessoaFisica", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("reenquadramento.ideProcessoReenquadramento",ideProcessoReenquadramento));

		return comprovantePagamentoDaeDAO.listarPorCriteria(criteria);
	}
}
