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
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
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
public class PessoaJuridicaHistoricoDAOImpl {
	
	@Inject
	private IDAO<PessoaJuridicaHistorico> pessoaJuridicaDAO;
	
	@Inject
	private PessoaJuridicaDAOImpl pessoaJuridicaDaoImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(PessoaJuridicaHistorico pessoaJuridicaHistorico) {
		pessoaJuridicaDAO.salvar(pessoaJuridicaHistorico);
		pessoaJuridicaDAO.sessionFlush();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(PessoaJuridicaHistorico pessoaJuridicaHistorico) {
		pessoaJuridicaDAO.salvarOuAtualizar(pessoaJuridicaHistorico);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(PessoaJuridicaHistorico pessoaJuridicaHistorico) {
		pessoaJuridicaDAO.atualizar(pessoaJuridicaHistorico);
	}
	
	public PessoaJuridicaHistorico buscarHistoricoAnterior(PessoaJuridica pessoaJuridica) {
			
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT pjh.* ");
		sql.append(" FROM pessoa_juridica_historico pjh ");
		sql.append(" WHERE pjh.ide_pessoa_juridica = :idePessoaJuridica ");
		sql.append(" ORDER BY pjh.dtc_abertura LIMIT 1 ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString(), PessoaJuridicaHistorico.class);
				
		if(!Util.isNullOuVazio(pessoaJuridica)) {
			lQuery.setParameter("idePessoaJuridica", pessoaJuridica.getIdePessoaJuridica());
		} else {
			return null;
		}
		
		List listaHistorico = lQuery.getResultList();
		
		if(!Util.isNullOuVazio(listaHistorico)) {
			return (PessoaJuridicaHistorico) (lQuery.getSingleResult());
		} else {
			return null;
		}

		
	}
	
	@SuppressWarnings("unchecked")
	public List<PessoaJuridicaHistorico> listarHistoricoPessoaPorIdePessoa(PessoaJuridica pessoaJuridica, Date dataInicio, Date dataFim) {
		
		if(Util.isNullOuVazio(pessoaJuridica.getIdePessoaJuridica())) {
			pessoaJuridica.setIdePessoaJuridica(pessoaJuridicaDaoImpl.filtrarPessoaJuridicaByCnpj(pessoaJuridica).getIdePessoaJuridica());
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT pjh.* ");
		sql.append(" FROM pessoa_juridica_historico pjh ");
		sql.append(" WHERE pjh.ide_pessoa_juridica = :idePessoaJuridica ");

		if (!Util.isNullOuVazio(dataInicio)) {
		    sql.append(" AND CAST(pjh.dtc_historico AS date) >= CAST(:dataInicio AS date) ");
		}
		if (!Util.isNullOuVazio(dataFim)) {
		    sql.append(" AND CAST(pjh.dtc_historico AS date) <= CAST(:dataFim AS date) ");
		}
		
		sql.append(" ORDER BY pjh.dtc_historico DESC ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString(), PessoaJuridicaHistorico.class);
		
		lQuery.setParameter("idePessoaJuridica", pessoaJuridica.getIdePessoaJuridica());

		if (!Util.isNullOuVazio(dataInicio)) {
		    lQuery.setParameter("dataInicio", new SimpleDateFormat("dd/MM/yyyy").format(dataInicio));
		}
		if (!Util.isNullOuVazio(dataFim)) {
		    lQuery.setParameter("dataFim", new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
		}
		
		List<PessoaJuridicaHistorico> resultList = lQuery.getResultList();
		
		List<PessoaJuridicaHistorico> listaHistorico = new ArrayList<PessoaJuridicaHistorico>();
		
		for(PessoaJuridicaHistorico objetoHistorico: resultList) {
			
			objetoHistorico.setIdeUsuarioAlteracao((new UsuarioDAOImpl().getUsuario(objetoHistorico.getIdeUsuarioAlteracao())));
			
			if(!Util.isNullOuVazio(objetoHistorico.getIdeProcuradorRepresentante())) {
				objetoHistorico.getIdeProcuradorRepresentante().setIdeProcurador(selecionarProcuradorRepresentantePorIdeProcuradorPessoaJuridica(objetoHistorico.getIdeProcuradorRepresentante()));
			}	
			
			if(!Util.isNullOuVazio(objetoHistorico.getIdeRepresentanteLegal())) {
				objetoHistorico.getIdeRepresentanteLegal().setIdePessoaFisica(selecionarRepresentanteLegalPorIdeRepresentanteLegal(objetoHistorico.getIdeRepresentanteLegal()));
			}
			
			listaHistorico.add(objetoHistorico);
		}
		
		return listaHistorico;
	}
	
	public PessoaFisica selecionarProcuradorRepresentantePorIdeProcuradorPessoaJuridica(ProcuradorRepresentante procuradorRepresentante) {
		PessoaFisica procurador = new PessoaFisica();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT pf.* ");
		sql.append(" FROM pessoa_fisica pf ");
		sql.append(" INNER JOIN procurador_representante pr ");
		sql.append(" ON pr.ide_procurador = pf.ide_pessoa_fisica ");
		sql.append(" WHERE pr.ide_procurador_representante = :ideProcuradorRepresentante ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString(), PessoaFisica.class);
		
		lQuery.setParameter("ideProcuradorRepresentante", procuradorRepresentante.getIdeProcuradorRepresentante());

		procurador = (PessoaFisica) lQuery.getSingleResult();
		
		return procurador;
		
	}
	
	public PessoaFisica selecionarRepresentanteLegalPorIdeRepresentanteLegal(RepresentanteLegal representanteLegal) {
		PessoaFisica representante = new PessoaFisica();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT pf.* ");
		sql.append(" FROM pessoa_fisica pf ");
		sql.append(" INNER JOIN representante_legal rl ");
		sql.append(" ON rl.ide_pessoa_fisica = pf.ide_pessoa_fisica ");
		sql.append(" WHERE rl.ide_representante_legal = :ideRepresentanteLegal ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(sql.toString(), PessoaFisica.class);
		
		lQuery.setParameter("ideRepresentanteLegal", representanteLegal.getIdeRepresentanteLegal());

		representante = (PessoaFisica) lQuery.getSingleResult();
		
		return representante;
		
	}
	
}







