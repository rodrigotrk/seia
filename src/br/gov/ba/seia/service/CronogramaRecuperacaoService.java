package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.CronogramaRecuperacaoDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.CronogramaRecuperacao;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CronogramaRecuperacaoService {
	
	@Inject
	CronogramaRecuperacaoDAOImpl daoImpl;
	
	@Inject
	IDAO<CronogramaRecuperacao> dao;
	
	@Inject
	IDAO<DocumentoImovelRural> documentoObrigatorioDAO;
	
	@Inject
	IDAO<LocalizacaoGeografica> localizacaoGeograficaDAO;
	
	public CronogramaRecuperacao listarCronogramaRecuperacaoByReservaLegal(ReservaLegal pReservaLegal) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CronogramaRecuperacao.class, "cronogramaRecuperacao");
		criteria.add(Restrictions.eq("ideReservaLegal", pReservaLegal));
		criteria.addOrder(Order.asc("ideReservaLegal"));
		return dao.buscarPorCriteria(criteria);
	}

	public List<CronogramaRecuperacao> listarCronogramaRecuperacaoByImovel(ImovelRural pImovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CronogramaRecuperacao.class, "cronogramaRecuperacao");
		criteria.add(Restrictions.eq("ideReservaLegal.ideImovelRural", pImovelRural));
		return dao.listarPorCriteria(criteria);
	}

	public List<CronogramaRecuperacao> listarCronogramaRecuperacaoById(CronogramaRecuperacao pCronogramaRecuperacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CronogramaRecuperacao.class, "cronogramaRecuperacao");
		criteria.add(Restrictions.eq("ideCronogramaRecuperacao", pCronogramaRecuperacao.getIdeCronogramaRecuperacao()));
		return dao.listarPorCriteria(criteria);
	}

	public CronogramaRecuperacao filtrarCronogramaRecuperacaoById(CronogramaRecuperacao pCronogramaRecuperacao) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideCronogramaRecuperacao", pCronogramaRecuperacao.getIdeCronogramaRecuperacao());
		return dao.buscarEntidadePorNamedQuery("CronogramaRecuperacao.findByIdeCronogramaRecuperacao", parametros );
	}

	public List<CronogramaRecuperacao> filtrarCronogramaRecuperacaoByDocumento(CronogramaRecuperacao pCronogramaRecuperacao) {
		 Map<String, Object> param = new HashMap<String, Object>();
	        param.put("ideDocumentoObrigatorio", pCronogramaRecuperacao.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio());
			return dao.buscarPorNamedQuery("CronogramaRecuperacao.findByIdeDocumentoObrigatorio", param);
	}

	public CronogramaRecuperacao listarCronogramaRecuperacaoByApp(App pApp) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CronogramaRecuperacao.class, "cronogramaRecuperacao");
		criteria.add(Restrictions.eq("ideApp", pApp));
		criteria.addOrder(Order.asc("ideApp"));
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CronogramaRecuperacao pCronogramaRecuperacao) {
		if(!Util.isNullOuVazio(pCronogramaRecuperacao.getIdeDocumentoObrigatorio())){
			documentoObrigatorioDAO.salvarOuAtualizar(pCronogramaRecuperacao.getIdeDocumentoObrigatorio());
		}
		if(!Util.isNullOuVazio(pCronogramaRecuperacao.getLocalizacaoGeografica())){
			localizacaoGeograficaDAO.salvarOuAtualizar(pCronogramaRecuperacao.getLocalizacaoGeografica());
		}
		this.daoImpl.salvar(pCronogramaRecuperacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(CronogramaRecuperacao pCronogramaRecuperacao) {
		this.daoImpl.atualizar(pCronogramaRecuperacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void salvarDocumentoPrad(DocumentoImovelRural arqSalvar) {
        documentoObrigatorioDAO.salvar(arqSalvar);
    }

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removerDocumentoPrad(CronogramaRecuperacao cronogramaRecuperacao) {
		DocumentoImovelRural arqExcluido = cronogramaRecuperacao.getIdeDocumentoObrigatorio();
		cronogramaRecuperacao.setIdeDocumentoObrigatorio(null);
		dao.salvarOuAtualizar(cronogramaRecuperacao);
        
        documentoObrigatorioDAO.remover(arqExcluido);
    }

	public void removerCronogramaRecuperacao(CronogramaRecuperacao pCronogramaRecuperacao) {
		this.dao.remover(pCronogramaRecuperacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(CronogramaRecuperacao pCronogramaRecuperacao) {
		if(!Util.isNullOuVazio(pCronogramaRecuperacao.getIdeDocumentoObrigatorio())){
			documentoObrigatorioDAO.salvarOuAtualizar(pCronogramaRecuperacao.getIdeDocumentoObrigatorio());
		}
		if(!Util.isNullOuVazio(pCronogramaRecuperacao.getLocalizacaoGeografica())){
			localizacaoGeograficaDAO.salvarOuAtualizar(pCronogramaRecuperacao.getLocalizacaoGeografica());
		}
		this.daoImpl.salvarOuAtualizar(pCronogramaRecuperacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CronogramaRecuperacao pCronogramaRecuperacao) {
		String deleteSQL = "DELETE FROM cronograma_recuperacao WHERE ide_cronograma_recuperacao = :ideCronogramaRecuperacao";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideCronogramaRecuperacao", pCronogramaRecuperacao.getIdeCronogramaRecuperacao());
		dao.executarNativeQuery(deleteSQL, params);
	}
}
