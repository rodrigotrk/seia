package br.gov.ba.seia.dao;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento;

public class CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoDAOImpl extends BaseDAO<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento>{
	
	@Inject
	IDAO<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> dao;
	
	@Override
	protected IDAO<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> getDao() {
		return dao;
	}
	@Override
	public void salvar(CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento responsavelEmpreendimento) {
		dao.salvar(responsavelEmpreendimento);
	}
	@Override
	public void salvarOuAtualizar(CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento responsavelEmpreendimento){
		dao.salvarOuAtualizar(responsavelEmpreendimento);
	}

	public List<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> listarPorCadastroAtividade(Integer ideCadastroAtividadeNaoSujeitaLic){
		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento.class);
		criteria.createAlias("ideCadastroAtividadeNaoSujeitaLic", "cadastroAtividade");
		criteria.createAlias("ideResponsavelEmpreendimento", "responsavelEmpreendimento");
		criteria.add(Restrictions.eq("cadastroAtividade.ideCadastroAtividadeNaoSujeitaLic", ideCadastroAtividadeNaoSujeitaLic));
		return dao.listarPorCriteria(criteria);
	}
	
}
