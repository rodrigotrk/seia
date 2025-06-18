package br.gov.ba.seia.service;

import java.util.Arrays;
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

import org.hamcrest.core.IsNull;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnidadeConservacao;
import br.gov.ba.seia.entity.TipoAreaPreservacaoPermanente;
import br.gov.ba.seia.entity.TipoAreaProtegida;
import br.gov.ba.seia.entity.TipoGestao;
import br.gov.ba.seia.entity.UnidadeConservacao;
import br.gov.ba.seia.enumerator.CrudOperationEnum;
import br.gov.ba.seia.enumerator.TipoAreaProtegidaEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.util.Util;

/**
 * 
 * @author eduardo.fernandes
 * 
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LicencaService {

	@Inject
	private IDAO<Licenca> licencaDAO;

	@Inject
	private IDAO<UnidadeConservacao> unidadeConservacaoDAO;

	@Inject
	private IDAO<TipoAreaProtegida> tipoAreaProtegidaDAO;

	@Inject
	private IDAO<TipoAreaPreservacaoPermanente> tipoAreaPreservacaoPermanenteDAO;

	@Inject
	private IDAO<TipoGestao> tipoGestaoDAO;
	
	@Inject
	private IDAO<RequerimentoUnidadeConservacao> requerimentoUnidadeConservacaoDAO;

	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Licenca> carregarListaLicenca(Licenca listaLicencaSelecionada) {
		return licencaDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Licenca obterLicenca(Licenca licenca)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideLicenca", licenca.getIdeLicenca());
		return licencaDAO.buscarEntidadePorNamedQuery("Licenca.findByIdeLicenca", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSalvarLicenca(Licenca licencaAExcluir, Licenca licencaASalvar)  {
		excluirLicenca(licencaAExcluir);
			if(Util.isNullOuVazio(licencaASalvar.getIndExcluido())) {
				licencaASalvar.setIndExcluido(false);
			licencaDAO.salvarOuAtualizar(licencaASalvar);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarLicenca(Licenca licenca) {
	    if (licenca == null) {
	        throw new IllegalArgumentException("O objeto Licenca não pode ser nulo.");
	    }
	    
	    if(Util.isNullOuVazio(licenca.getIndExcluido())) {
			licenca.setIndExcluido(false);
	    }
	    
	    if (!licenca.getIndExcluido()) {
	        try {
	            licencaDAO.salvarOuAtualizar(licenca);
	        } catch (Exception e) {
	            throw new RuntimeException("Falha ao salvar a licença", e);
	        }
	    }
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLicenca(Licenca licenca)  {
		if(!Util.isNullOuVazio(licenca.getIdeLicenca())){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideLicenca", licenca.getIdeLicenca());
		
			licencaDAO.executarNamedQuery("Licenca.removeByIdeLicenca", params);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarLicenca(Licenca licenca)  {
		licencaDAO.persistir(licenca, CrudOperationEnum.UPDATE);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public UnidadeConservacao obterUnidadeConservacao(UnidadeConservacao unidadeConservacao)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideUnidadeConservacao", unidadeConservacao.getIdeUnidadeConservacao());
		return unidadeConservacaoDAO.buscarEntidadePorNamedQuery("UnidadeConservacao.findByIdeUnidadeConservacao",
				parameters);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeConservacao> obterListaUnidadeConservacao() {
		return unidadeConservacaoDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeConservacao> obterListaUnidadeConservacaoByIdeTipoGestao(int ideTipoGestao)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(UnidadeConservacao.class);
		criteria.add(Restrictions.eq("ideTipoGestao.ideTipoGestao", ideTipoGestao));
		return unidadeConservacaoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAreaProtegida> obterListaAreaProtegida() {
		return tipoAreaProtegidaDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoGestao> obterListaTipoGestao() {
		return tipoGestaoDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAreaPreservacaoPermanente> obterListaAreaPreservacaoPermanente(int ideTipoAreaProtegida)
			 {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAreaPreservacaoPermanente.class);
		criteria.add(Restrictions.eq("ideTipoAreaProtegida.ideTipoAreaProtegida", ideTipoAreaProtegida));
		return tipoAreaPreservacaoPermanenteDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Licenca buscarLicencaPor(Requerimento requerimento, TipoAreaProtegidaEnum tipoAreaProtegidaEnum)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Licenca.class);
		criteria
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("r.tipoAreaProtegidaCollection", "ta")
			.add(Restrictions.eq("ideRequerimento", requerimento))
			.add(Restrictions.eq("ta.ideTipoAreaProtegida", tipoAreaProtegidaEnum.getId()));
		return licencaDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Licenca getLicencaByIdeRequerimento(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Licenca.class)
				.createAlias("ideTipoSolicitacao", "ts", JoinType.INNER_JOIN)
				.add(Restrictions.eq("ideRequerimento", requerimento))
				.add(Restrictions.eq("indExcluido", false))
				.add(Restrictions.or(Restrictions.eq("ts.ideTipoSolicitacao", TipoSolicitacaoEnum.NOVA_LICENCA.getId())
						, Restrictions.eq("ts.ideTipoSolicitacao", TipoSolicitacaoEnum.POSSUI_LICENCA_MUNICIPAL_FEDERAL.getId())
						)
					);
		return licencaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Licenca getLicencaByIdeRequerimentoTipoAlteracao(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Licenca.class);
		criteria.add(Restrictions.eq("ideRequerimento", requerimento));
		criteria.add(Restrictions.eq("indExcluido", false));
		criteria.createAlias("ideTipoSolicitacao", "ts", JoinType.FULL_JOIN);
		criteria.add(Restrictions.eq("ts.ideTipoSolicitacao", TipoSolicitacaoEnum.ALTERACAO_LICENCA.getId()));
		return licencaDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAreaProtegida> getListTipoAreaProtegidaByLicenca(Requerimento requerimento) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideRequerimento", requerimento.getIdeRequerimento());
		return tipoAreaProtegidaDAO.buscarPorNamedQuery("TipoAreaProtegida.findByIdeRequerimento", map);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAreaPreservacaoPermanente> getListTipoAppByLicenca(Requerimento requerimento) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideRequerimento", requerimento.getIdeRequerimento());
		return tipoAreaPreservacaoPermanenteDAO.buscarPorNamedQuery("TipoAreaPreservacaoPermanente.findByIdeRequerimento",
				map);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Licenca> getListaLicencaByIdeRequerimento(Requerimento req)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Licenca.class);
		criteria.createAlias("ideTipoSolicitacao", "ts", JoinType.FULL_JOIN);
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", req.getIdeRequerimento()));
		criteria.add(Restrictions.eq("indExcluido", false));
		criteria.add(Restrictions.in("ts.ideTipoSolicitacao", Arrays.asList(TipoSolicitacaoEnum.ALTERACAO_LICENCA.getId(), TipoSolicitacaoEnum.RENOVACAO_LICENCA.getId())));
		return licencaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Licenca getLicencaByIdeRequerimentoSimples(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Licenca.class);
		criteria.createAlias("ideTipoSolicitacao", "ts", JoinType.FULL_JOIN);
		criteria.add(Restrictions.eq("ideRequerimento", requerimento));
		criteria.add(Restrictions.eq("indExcluido",false));
		criteria.add(Restrictions.in("ts.ideTipoSolicitacao", Arrays.asList(TipoSolicitacaoEnum.ALTERACAO_LICENCA.getId(), TipoSolicitacaoEnum.RENOVACAO_LICENCA.getId(), TipoSolicitacaoEnum.NOVA_LICENCA.getId())));
		return licencaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RequerimentoUnidadeConservacao> obterRequerimentoUnidadeConservacaoByRequerimento(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoUnidadeConservacao.class);
		criteria.createAlias("ideUnidadeConservacao", "uc", JoinType.FULL_JOIN);
		criteria.createAlias("uc.ideTipoGestao", "tg", JoinType.FULL_JOIN);
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		return requerimentoUnidadeConservacaoDAO.listarPorCriteria(criteria);
	}
}
