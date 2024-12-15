package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicImovel;
import br.gov.ba.seia.entity.ImovelRural;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeNaoSujeitoLicImovelService {

	@Inject
	IDAO<CadastroAtividadeNaoSujeitaLicImovel> dao ;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLicImovel carregarPorCadastroEImovel(CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSujeitaLic, ImovelRural ideImovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLicImovel.class);
		criteria.add(Restrictions.eq("ideCadastroAtividadeNaoSujeitaLic", ideCadastroAtividadeNaoSujeitaLic));
		criteria.add(Restrictions.eq("ideImovelRural", ideImovelRural));
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeNaoSujeitaLicImovel cadastroAtividadeNaoSujeitoLicImovel) {
		dao.salvarOuAtualizar(cadastroAtividadeNaoSujeitoLicImovel);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicImovel> listarPorCadastro(Integer ideCadastroAtividadeNaoSujeitaLic) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLicImovel.class);
		criteria.createAlias("ideCadastroAtividadeNaoSujeitaLic", "cadastro");
		criteria.add(Restrictions.eq("cadastro.ideCadastroAtividadeNaoSujeitaLic", ideCadastroAtividadeNaoSujeitaLic));
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(CadastroAtividadeNaoSujeitaLicImovel cadastroAtividadeNaoSujeitoLicImovel) {
		dao.remover(cadastroAtividadeNaoSujeitoLicImovel);
	}
	
}
