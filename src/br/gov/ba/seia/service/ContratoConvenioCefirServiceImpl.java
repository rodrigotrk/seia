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
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ContratoConvenioCefir;
import br.gov.ba.seia.entity.ContratoConvenioCefirMunicipio;
import br.gov.ba.seia.entity.GestorFinanceiroCefir;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContratoConvenioCefirServiceImpl{
	
	@Inject
	private ImovelRuralService imovelRuralService;

	@Inject
	private IDAO<ContratoConvenioCefir> contratoConvenioCefirDAO;
	
	@Inject 
	private IDAO<ContratoConvenioCefirMunicipio> contratoConvenioCefirMunicipioDao;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ContratoConvenioCefir contratoConvenio) throws Exception  {
		validarCamposObrigatorios(contratoConvenio);
		validarNumeroUnico(contratoConvenio);
		contratoConvenioCefirDAO.salvarOuAtualizar(contratoConvenio);
		excluirTodosContratoConvenioCefirMunicipios(contratoConvenio);
		if(contratoConvenio.isIndProjetoBndes()) {
			incluirTodosContratoConvenioCefirMunicipios(contratoConvenio);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void validarNumeroUnico(ContratoConvenioCefir contratoConvenioAvaliado) throws Exception   {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT cc.ide_contrato_convenio_cefir ");
		lSql.append("FROM contrato_convenio_cefir cc  ");
		lSql.append("WHERE cc.num_contrato_convenio_cefir = :numContratoConvenio ");

		
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());

		lQuery.setParameter("numContratoConvenio", contratoConvenioAvaliado.getNumContratoConvenioCefir());

		List<Integer> result = lQuery.getResultList();
		
		for (Integer object : result) {
			if(!Util.isNull(object) && !object.equals(contratoConvenioAvaliado.getIdeContratoConvenioCefir())) {
				if(!Util.isNull(contratoConvenioAvaliado.getIdeContratoConvenioCefir())) {
					lSql = new StringBuilder();
					lSql.append("SELECT cc.num_contrato_convenio_cefir ");
					lSql.append("FROM contrato_convenio_cefir cc  ");
					lSql.append("WHERE cc.ide_contrato_convenio_cefir = :ideContratoConvenio ");
					lQuery = lEntityManager.createNativeQuery(lSql.toString());
					
					lQuery.setParameter("ideContratoConvenio", contratoConvenioAvaliado.getIdeContratoConvenioCefir());
					
					List<String> result2 = lQuery.getResultList();
					contratoConvenioAvaliado.setNumContratoConvenioCefir(result2.get(0));
				}
				throw new Exception(Util.getString("cefir_msg_contrato_convenio_numero_repetido"));
			}
		}
	}
	
	private void validarCamposObrigatorios(ContratoConvenioCefir contratoConvenio) throws CampoObrigatorioException {
		StringBuilder mensagem = new StringBuilder();
		
		if(Util.isNull(contratoConvenio.getNomContratoConvenioCefir()) || contratoConvenio.getNomContratoConvenioCefir().isEmpty()) {
			mensagem.append(Util.getString("javax.faces.component.UIInput.REQUIRED", Util.getString("cefir_lbl_nome_contrato_convenio")) + "<br />");
		}

		if(Util.isNull(contratoConvenio.getNumContratoConvenioCefir()) || contratoConvenio.getNumContratoConvenioCefir().isEmpty()) {
			mensagem.append(Util.getString("javax.faces.component.UIInput.REQUIRED", Util.getString("cefir_lbl_numero_contrato_convenio")) + "<br />");
		}
		
		if(Util.isNull(contratoConvenio.getIdeGestorFinanceiroCefir())) {
			mensagem.append(Util.getString("javax.faces.component.UIInput.REQUIRED", Util.getString("cefir_lbl_gestor_financeiro")) + "<br />");
		}
		
		if(contratoConvenio.isIndProjetoBndes()) {
			if(Util.isNull(contratoConvenio.getIdePessoaJuridica()) || Util.isNull(contratoConvenio.getIdePessoaJuridica().getNumCnpj()) || contratoConvenio.getIdePessoaJuridica().getNumCnpj().isEmpty()) {
				mensagem.append(Util.getString("javax.faces.component.UIInput.REQUIRED", Util.getString("cefir_lbl_cnpj_contrato_convenio")) + "<br />");
			}
			
			if(Util.isNullOuVazio(contratoConvenio.getContratoConvenioCefirMunicipioCollection())) {
				mensagem.append(Util.getString("javax.faces.component.UIInput.REQUIRED", Util.getString("cefir_lbl_municipio_contrato_convenio")) + "<br />");
			}
		}
		
		if(mensagem.length() > 0) {
			throw new CampoObrigatorioException(mensagem.toString());
		}
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ContratoConvenioCefir contratoConvenio) throws Exception  {		
		if(imovelRuralService.isExisteImovelRuralPorContratoConvenio(contratoConvenio)) {
			throw new Exception("Exclusão não permitida.");
		}
		if(contratoConvenio.isIndProjetoBndes()) {
			excluirTodosContratoConvenioCefirMunicipios(contratoConvenio);
			contratoConvenio.setContratoConvenioCefirMunicipioCollection(null);
		}
			
		contratoConvenioCefirDAO.remover(contratoConvenio);
	}

	public ContratoConvenioCefir buscarContratoConvenioByNumero(String numContratoConvenioCefir) {	
		DetachedCriteria criteria = DetachedCriteria.forClass(ContratoConvenioCefir.class, "contratoConvenioCefir");
		criteria.createAlias("contratoConvenioCefir.ideGestorFinanceiroCefir", "ideGestorFinanceiro");
		criteria.createAlias("contratoConvenioCefir.idePessoaJuridica", "idePessoaJuridica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("contratoConvenioCefir.contratoConvenioCefirMunicipioCollection", "contratoConvenioCefirMunicipioCollection", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("contratoConvenioCefir.numContratoConvenioCefir",  numContratoConvenioCefir));
		
		return contratoConvenioCefirDAO.buscarPorCriteria(criteria);
	}

	public List<ContratoConvenioCefir> listContratoConvenio() {
		return contratoConvenioCefirDAO.listarTodos();
	}

	@SuppressWarnings("unchecked")
	public List<ContratoConvenioCefir> filtrarContratoConvenio(ContratoConvenioCefir contratoConvenioCefir)	 {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT cc.ide_contrato_convenio_cefir, cc.nom_contrato_convenio_cefir, cc.num_contrato_convenio_cefir, cc.ide_gestor_financeiro_cefir, gf.nom_gestor_financeiro_cefir, ");
		lSql.append("(SELECT COUNT(*) FROM IMOVEL_RURAL IR WHERE IR.IDE_CONTRATO_CONVENIO = cc.ide_contrato_convenio_cefir )  ");
		lSql.append("FROM contrato_convenio_cefir cc  ");
		lSql.append("INNER JOIN gestor_financeiro_cefir gf ON (cc.ide_gestor_financeiro_cefir = gf.ide_gestor_financeiro_cefir) ");
		lSql.append("WHERE (1=1) ");

		
		if(!Util.isNullOuVazio(contratoConvenioCefir.getNomContratoConvenioCefir())) {
			lSql.append("AND cc.nom_contrato_convenio_cefir ilike '%" + contratoConvenioCefir.getNomContratoConvenioCefir() +"%'");
		}
		if(!Util.isNullOuVazio(contratoConvenioCefir.getNumContratoConvenioCefir())) {
			lSql.append("AND cc.num_contrato_convenio_cefir ilike '%" + contratoConvenioCefir.getNumContratoConvenioCefir() + "%'");
		}
		if(!Util.isNull(contratoConvenioCefir.getIdeGestorFinanceiroCefir()) && !Util.isNull(contratoConvenioCefir.getIdeGestorFinanceiroCefir().getIdeGestorFinanceiroCefir())) {
			lSql.append("AND cc.ide_gestor_financeiro_cefir =" + contratoConvenioCefir.getIdeGestorFinanceiroCefir() );
		}
		
		lSql.append("ORDER BY cc.nom_contrato_convenio_cefir ");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());


		List<Object[]> result = lQuery.getResultList();
		List<ContratoConvenioCefir> listContratoConvenio = new ArrayList<ContratoConvenioCefir>();
		ContratoConvenioCefir cc = null;
		for (Object object[] : result) {
			cc = new ContratoConvenioCefir();
			cc.setIdeContratoConvenioCefir(Integer.valueOf(object[0].toString()));
			cc.setNomContratoConvenioCefir(object[1].toString());
			cc.setNumContratoConvenioCefir(object[2].toString());
			cc.setIdeGestorFinanceiroCefir(new GestorFinanceiroCefir(Integer.valueOf(object[3].toString()), object[4].toString()));
			cc.setQtdImovel(Integer.valueOf(object[5].toString()));
			listContratoConvenio.add(cc);
		}
		
		return listContratoConvenio;
	}

	public void incluirTodosContratoConvenioCefirMunicipios(ContratoConvenioCefir contratoConvenioCefir) {
		for (ContratoConvenioCefirMunicipio contratoConvenioCefirMunicipio : contratoConvenioCefir.getContratoConvenioCefirMunicipioCollection()) {
			contratoConvenioCefirMunicipio.setIdeContratoConvenioCefir(contratoConvenioCefir);
			contratoConvenioCefirMunicipioDao.salvar(contratoConvenioCefirMunicipio);
		}
	}

	public void excluirTodosContratoConvenioCefirMunicipios(ContratoConvenioCefir contratoConvenioCefir) {
		String delSQL = "DELETE FROM contrato_convenio_cefir_municipio WHERE ide_contrato_convenio_cefir = :ide_contrato_convenio_cefir";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ide_contrato_convenio_cefir", contratoConvenioCefir.getIdeContratoConvenioCefir());
		contratoConvenioCefirMunicipioDao.executarNativeQuery(delSQL, params);
	}

	public List<ContratoConvenioCefir> listarContratoConvenioDoBndes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ContratoConvenioCefir.class, "contratoConvenioCefir");
		criteria.createAlias("contratoConvenioCefir.idePessoaJuridica", "idePessoaJuridica");		
		criteria.add(Restrictions.eq("contratoConvenioCefir.indProjetoBndes", true));
		List<ContratoConvenioCefir> listContratoConvenioCefir = contratoConvenioCefirDAO.listarPorCriteria(criteria, Order.asc("contratoConvenioCefir.nomContratoConvenioCefir"));
		
		for (ContratoConvenioCefir contratoConvenioCefir : listContratoConvenioCefir) {
			if(!Util.isNullOuVazio(contratoConvenioCefir.getIdePessoaJuridica().getProcuradorRepresentanteCollection())){
				Hibernate.initialize(contratoConvenioCefir.getIdePessoaJuridica().getProcuradorRepresentanteCollection());
			}
		}		
		return listContratoConvenioCefir;
	}

	
	public boolean isUsuarioVinculadoProjetoBndes(Usuario usuario) {
		
		if(ContextoUtil.getContexto().getUsuarioVinculadoBndes()==null){
			List<ContratoConvenioCefir> listContratoConvenioCefir = listarContratoConvenioDoBndes();
			
			ContextoUtil.getContexto().setUsuarioVinculadoBndes(false);
			
			for (ContratoConvenioCefir contratoConvenioCefir : listContratoConvenioCefir) {
				if(!Util.isNullOuVazio(contratoConvenioCefir.getIdePessoaJuridica().getProcuradorRepresentanteCollection())) {
					for (ProcuradorRepresentante procuradorRepresentante : contratoConvenioCefir.getIdePessoaJuridica().getProcuradorRepresentanteCollection()) {
						if(!procuradorRepresentante.getIndExcluido() && usuario.getIdePessoaFisica().equals(procuradorRepresentante.getIdeProcurador().getIdePessoaFisica())) {
							ContextoUtil.getContexto().setUsuarioVinculadoBndes(true);
							break;
						}
					}
				}
			}
			
		}
			
		return ContextoUtil.getContexto().getUsuarioVinculadoBndes();
	}

	public ContratoConvenioCefir carregarContratoConvenio(ContratoConvenioCefir contratoConvenioCefir)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ContratoConvenioCefir.class, "contratoConvenioCefir");
		criteria.createAlias("contratoConvenioCefir.ideGestorFinanceiroCefir", "ideGestorFinanceiro");
		criteria.createAlias("contratoConvenioCefir.idePessoaJuridica", "idePessoaJuridica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("contratoConvenioCefir.contratoConvenioCefirMunicipioCollection", "contratoConvenioCefirMunicipioCollection", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("contratoConvenioCefir.ideContratoConvenioCefir",  contratoConvenioCefir.getIdeContratoConvenioCefir()));
		return contratoConvenioCefirDAO.buscarPorCriteria(criteria);
	}

}
