package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.ArquivoBaixaDaeDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ArquivoBaixaDae;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.MemoriaCalculoDaeParcela;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ArquivoBaixaDaeService {

	@Inject
	ArquivoBaixaDaeDAOImpl arquivoBaixaDaeDAOImpl;
	
	@Inject
	IDAO<MemoriaCalculoDaeParcela> memoriaCalculoDaeParcelaDAO;
	
	@Inject
	IDAO<RequerimentoPessoa> requerimentoPessoaDAO;
	

	public void salvarArquivoBaixa(ArquivoBaixaDae arquivoBaixaDae) {
		arquivoBaixaDaeDAOImpl.salvarProcessodeBaixa(arquivoBaixaDae);
	}
	
	public void atualizarArquivoBaixa(ArquivoBaixaDae arquivoBaixaDae) {
		arquivoBaixaDaeDAOImpl.atualizarProcessoBaixa(arquivoBaixaDae);
	}

	public List<ArquivoBaixaDae> carregarArquivosBaixaDae(Integer codigoReceita) {
		return arquivoBaixaDaeDAOImpl.carregarArquivosBaixaDae(codigoReceita);
	}
	
	public List<Pessoa> listarPessoasVinculasAosDaes(List<Dae> idesDaes){
		List<Pessoa> pessoas = new ArrayList<Pessoa>();

		try {

			
			DetachedCriteria criteria = DetachedCriteria.forClass(MemoriaCalculoDaeParcela.class);
			
			criteria.createAlias("ideDae", "dae", JoinType.INNER_JOIN);
			criteria.createAlias("ideMemoriaCalculoDae", "m", JoinType.INNER_JOIN);
			criteria.createAlias("m.ideCumprimentoReposicaoFlorestal", "f", JoinType.INNER_JOIN);
			criteria.createAlias("f.requerimento", "req", JoinType.INNER_JOIN);
			
			criteria.add(Restrictions.in("ideDae", idesDaes));
			
			criteria.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideMemoriaCalculoDae"), "ideMemoriaCalculoDae.ideMemoriaCalculoDae")
						.add(Projections.property("req.ideRequerimento"), "ideMemoriaCalculoDae.ideCumprimentoReposicaoFlorestal.requerimento.ideRequerimento")
					)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(MemoriaCalculoDaeParcela.class));
		
		
			List<MemoriaCalculoDaeParcela> listDaes = memoriaCalculoDaeParcelaDAO.listarPorCriteria(criteria, Order.asc("ideDae.ideDae"));
			
			for (MemoriaCalculoDaeParcela memoriaCalculoDae : listDaes) {
				pessoas.addAll(listarRequerimentoPessoaPorRequerimento(memoriaCalculoDae.getIdeMemoriaCalculoDae().getIdeCumprimentoReposicaoFlorestal().getRequerimento()));
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return pessoas;
	}
	
	public List<Pessoa> listarRequerimentoPessoaPorRequerimento(Requerimento ideRequerimento){
		
		List<Integer> tiposPessoas = new ArrayList<Integer>(Arrays.asList(1,2,3));
		List<Pessoa>listPessoa = new ArrayList<Pessoa>();

		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoPessoa.class);

			criteria.createAlias("pessoa", "p", JoinType.INNER_JOIN);
			criteria.createAlias("ideTipoPessoaRequerimento", "tpr", JoinType.INNER_JOIN);
			criteria.createAlias("p.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("p.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN);
			
			
			criteria.add(Restrictions.in("tpr.ideTipoPessoaRequerimento",tiposPessoas));
			criteria.add(Restrictions.eq("requerimento",ideRequerimento));
			
			criteria.setProjection(
					Projections.projectionList()
						.add(Projections.property("p.desEmail"), "pessoa.desEmail")
						.add(Projections.property("p.idePessoa"), "pessoa.idePessoa")
						.add(Projections.property("pf.idePessoaFisica"), "pessoa.pessoaFisica.idePessoaFisica")
						.add(Projections.property("pf.nomPessoa"), "pessoa.pessoaFisica.nomPessoa")
						.add(Projections.property("pj.idePessoaJuridica"), "pessoa.pessoaJuridica.idePessoaJuridica")
						.add(Projections.property("pj.nomRazaoSocial"), "pessoa.pessoaJuridica.nomRazaoSocial")						
					)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(RequerimentoPessoa.class));
		
			List<RequerimentoPessoa> listRequerimentoPessoa = requerimentoPessoaDAO.listarPorCriteria(criteria, Order.asc("tpr.ideTipoPessoaRequerimento"));
			
				for(RequerimentoPessoa rp : listRequerimentoPessoa){
					listPessoa.add(rp.getPessoa());
				}
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return listPessoa;
	}
	
}
