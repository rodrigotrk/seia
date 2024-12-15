package br.gov.ba.seia.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.entity.CategoriaDocumento;
import br.gov.ba.seia.entity.HistoricoSuspensaoCadastro;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaFisicaHistorico;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaHistorico;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.auditoria.FiltroAuditoria;
import br.gov.ba.seia.entity.auditoria.HistCampo;
import br.gov.ba.seia.entity.auditoria.HistTabela;
import br.gov.ba.seia.entity.auditoria.HistValor;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaFisicaHistoricoDAOImpl {
	
	@Inject
	private IDAO<PessoaFisicaHistorico> pessoaFisicaHistoricoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(PessoaFisicaHistorico pessoaFisicaHistorico) {
		pessoaFisicaHistoricoDAO.salvar(pessoaFisicaHistorico);
		pessoaFisicaHistoricoDAO.sessionFlush();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(PessoaFisicaHistorico pessoaFisicaHistorico) {
		pessoaFisicaHistoricoDAO.salvarOuAtualizar(pessoaFisicaHistorico);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(PessoaFisicaHistorico pessoaFisicaHistorico) {
		pessoaFisicaHistoricoDAO.atualizar(pessoaFisicaHistorico);
	}
	
	@SuppressWarnings("unchecked")
	public List<PessoaFisicaHistorico> listarHistoricoPessoaPorIdePessoa(PessoaFisica pessoaFisica, Date dataInicio, Date dataFim) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT pfh.* ");
		sql.append(" FROM pessoa_fisica_historico pfh ");
		sql.append(" WHERE pfh.ide_pessoa_fisica = :idePessoaFisica ");

		if (!Util.isNullOuVazio(dataInicio)) {
		    sql.append(" AND CAST(pfh.dtc_historico AS date) >= CAST(:dataInicio AS date) ");
		}
		if (!Util.isNullOuVazio(dataFim)) {
		    sql.append(" AND CAST(pfh.dtc_historico AS date) <= CAST(:dataFim AS date) ");
		}
		
		sql.append(" ORDER BY pfh.dtc_historico DESC ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString(), PessoaFisicaHistorico.class);
		
		lQuery.setParameter("idePessoaFisica", pessoaFisica.getIdePessoaFisica());

		if (!Util.isNullOuVazio(dataInicio)) {
		    lQuery.setParameter("dataInicio", new SimpleDateFormat("dd/MM/yyyy").format(dataInicio));
		}
		if (!Util.isNullOuVazio(dataFim)) {
		    lQuery.setParameter("dataFim", new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
		}
		
		List<PessoaFisicaHistorico> resultList = lQuery.getResultList();
		
		List<PessoaFisicaHistorico> listaHistorico = new ArrayList<PessoaFisicaHistorico>();
		
		for(PessoaFisicaHistorico objetoHistorico: resultList) {
			
			objetoHistorico.setIdeUsuarioAlteracao((new UsuarioDAOImpl().getUsuario(objetoHistorico.getIdeUsuarioAlteracao())));
			
			if(!Util.isNullOuVazio(objetoHistorico.getIdeProcuradorPessoaFisica())) {
				objetoHistorico.getIdeProcuradorPessoaFisica().setIdePessoaFisica(selecionarProcuradorPorIdeProcuradorPessoaFisica(objetoHistorico.getIdeProcuradorPessoaFisica()));
			}
			
			listaHistorico.add(objetoHistorico);
		}
		
		return listaHistorico;
	}
	
	public PessoaFisicaHistorico selecionarPorIdeProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica) {
		PessoaFisicaHistorico ultimoHistComProcurador = new PessoaFisicaHistorico();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT pfh.* ");
		sql.append(" FROM pessoa_fisica_historico pfh ");
		sql.append(" WHERE pfh.ide_procurador_pessoa_fisica = :ideProcuradorPessoaFisica ");
		sql.append(" AND status_procurador IS TRUE ");
		sql.append(" ORDER BY pfh.dtc_historico DESC LIMIT 1 ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString(), PessoaFisicaHistorico.class);
		
		lQuery.setParameter("ideProcuradorPessoaFisica", procuradorPessoaFisica.getIdeProcuradorPessoaFisica());

		ultimoHistComProcurador = (PessoaFisicaHistorico) lQuery.getSingleResult();
		
		return ultimoHistComProcurador;
		
	}
	
	public PessoaFisica selecionarProcuradorPorIdeProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica) {
		PessoaFisica procurador = new PessoaFisica();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT pf.* ");
		sql.append(" FROM pessoa_fisica pf ");
		sql.append(" WHERE pf.ide_pessoa_fisica = :idePessoaFisica ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString(), PessoaFisica.class);
		
		lQuery.setParameter("idePessoaFisica", procuradorPessoaFisica.getIdeProcurador());

		procurador = (PessoaFisica) lQuery.getSingleResult();
		
		return procurador;
		
	}
	
	public PessoaFisicaHistorico buscarHistoricoAnterior(PessoaFisica pessoaFisica) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT pfh.* ");
		sql.append(" FROM pessoa_fisica_historico pfh ");
		sql.append(" WHERE pfh.ide_pessoa_fisica = :idePessoaFisica ");
		sql.append(" ORDER BY pfh.dtc_historico LIMIT 1 ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString(), PessoaFisicaHistorico.class);
		
		lQuery.setParameter("idePessoaFisica", pessoaFisica.getIdePessoaFisica());
		
		List listaHistorico = lQuery.getResultList();
		
		if(!Util.isNullOuVazio(listaHistorico)) {
			return (PessoaFisicaHistorico) (lQuery.getSingleResult());
		} else {
			return null;
		}

		
	}
	
}







