package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AgroTipoAdubacao;
import br.gov.ba.seia.entity.ClassificacaoAdubacao;
import br.gov.ba.seia.entity.FceAgrossilvopastoril;
import br.gov.ba.seia.entity.TipoAdubacao;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AdubacaoService {

	@Inject
	private IDAO<TipoAdubacao> tipoAdubacaoDAO;
	@Inject
	private IDAO<ClassificacaoAdubacao> classificacaoAdubacaoDAO;
	@Inject
	private IDAO<AgroTipoAdubacao> agroTipoAdubacaoDAO;


	//LISTAR ELEMENTOS QUE PREENCHEM AS CHECKBOXES NA TELA
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAdubacao> listarTipoAdubacao () {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAdubacao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoAdubacaoDAO.listarPorCriteria(criteria);
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ClassificacaoAdubacao> listarClassificacaoAdubacao () {
		DetachedCriteria criteria = DetachedCriteria.forClass(ClassificacaoAdubacao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return classificacaoAdubacaoDAO.listarPorCriteria(criteria);
	}
	//FIM LISTAR ELEMENTOS QUE PREENCHEM AS CHECKBOXES NA TELA


	//BUSCAR NAS TABELA ASSOCIATIVAS
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AgroTipoAdubacao> buscarAgroTipoAdubacaoByIdeFceAgro (FceAgrossilvopastoril fceAgrossilvopastoril) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AgroTipoAdubacao.class);
		criteria.add(Restrictions.eq("ideFceAgrossilvopastoril", fceAgrossilvopastoril));
		criteria.setFetchMode("ideFceAgrossilvopastoril", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoAdubacao", FetchMode.JOIN);
		return agroTipoAdubacaoDAO.listarPorCriteria(criteria);
	}
	//FIM BUSCAR NAS TABELA ASSOCIATIVAS



	//LISTAR OS OBJETOS ASSOCIADOS DE CADA TIPO
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAdubacao>  listarTipoAdubacaoByIdeFceAgro(FceAgrossilvopastoril fceAgrossilvopastoril)  {
		List<TipoAdubacao> listaTipoAdubacao = new ArrayList<TipoAdubacao>();
		List<AgroTipoAdubacao> tmpAgroTipoAdubacao = buscarAgroTipoAdubacaoByIdeFceAgro(fceAgrossilvopastoril);
		for (AgroTipoAdubacao agroTipoAdubacao : tmpAgroTipoAdubacao){
			listaTipoAdubacao.add(agroTipoAdubacao.getIdeTipoAdubacao());
		}
		return listaTipoAdubacao;
	}
	//FIM LISTAR OS OBJETOS ASSOCIADOS DE CADA TIPO


	//SALVAR AS TABELAS ASSOCIATIVAS
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAgroTipoAdubacao(AgroTipoAdubacao agroTipoAdubacao) {
		agroTipoAdubacaoDAO.salvarOuAtualizar(agroTipoAdubacao);
	}
	//FIM SALVAR AS TABELAS ASSOCIATIVAS


	//EXCLUIR AS LIGAÇÕES
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLigacaoTipoAdubacao(FceAgrossilvopastoril fceAgrossilvopastoril) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideFceAgrossilvopastoril", fceAgrossilvopastoril);
		agroTipoAdubacaoDAO.executarNamedQuery("AgroTipoAdubacao.removerByIdeFceAgro", params);
	}

	//FIM EXCLUIR AS LIGAÇÕES

}
