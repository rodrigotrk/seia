package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.CerhBarragemAproveitamentoHidreletricoDAOImpl;
import br.gov.ba.seia.dao.CerhBarragemCaracterizacaoDAOImpl;
import br.gov.ba.seia.dao.CerhBarragemCaracterizacaoFinalidadeDAOImpl;
import br.gov.ba.seia.dao.CerhIntervencaoCaracterizacaoDAOImpl;
import br.gov.ba.seia.dao.CerhIntervencaoServicoDAOImpl;
import br.gov.ba.seia.dao.CerhObrasHidraulicasDAOImpl;
import br.gov.ba.seia.entity.CerhBarragemAproveitamentoHidreletrico;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacao;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhIntervencaoCaracterizacao;
import br.gov.ba.seia.entity.CerhIntervencaoServico;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhObrasHidraulicas;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhAbaIntervencaoFacade extends CerhAbasFacade {

	@EJB
	private CerhObrasHidraulicasDAOImpl cerhObrasHidraulicasDAO;
	
	@EJB
	private CerhIntervencaoServicoDAOImpl cerhIntervencaoServicoDAO;
	
	@EJB
	private CerhIntervencaoCaracterizacaoDAOImpl cerhIntervencaoCaracterizacaoDAO;
	
	@EJB
	private CerhBarragemCaracterizacaoDAOImpl cerhBarragemCaracterizacaoDAO;
	
	@EJB
	private CerhBarragemAproveitamentoHidreletricoDAOImpl cerhBarragemAproveitamentoHidreletricoDAO;
	
	@EJB
	private CerhBarragemCaracterizacaoFinalidadeDAOImpl cerhBarragemCaracterizacaoFinalidadeDAO; 
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhObrasHidraulicas> listarObrasHidraulicas()   {
		return cerhObrasHidraulicasDAO.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhIntervencaoServico> listarServicos()  {
		return cerhIntervencaoServicoDAO.listar();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhIntervencaoCaracterizacao carregarIdeCerhIntervencaoCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		try {
			return
				cerhIntervencaoCaracterizacaoDAO.buscar(
						Restrictions.eq("ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica", cerhLocalizacaoGeografica.getIdeCerhLocalizacaoGeografica()));
			
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhIntervencaoCaracterizacao carregarCerhIntervencaoCaracterizacao(CerhLocalizacaoGeografica clg)  {
		return cerhIntervencaoCaracterizacaoDAO.carregarCerhIntervencaoCaracterizacao(clg);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhIntervencaoServico carregarCerhIntervencaoServico(CerhIntervencaoCaracterizacao cerhIntervencaoCaracterizacao)  {
		if(!Util.isNull(cerhIntervencaoCaracterizacao.getIdeCerhIntervencaoServico())){
			return cerhIntervencaoServicoDAO.buscar(Restrictions.eq("ideCerhIntervencaoServico", cerhIntervencaoCaracterizacao.getIdeCerhIntervencaoServico().getId()));
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhObrasHidraulicas carregarCerhObrasHidraulicas(CerhIntervencaoCaracterizacao cerhIntervencaoCaracterizacao)  {
		if(!Util.isNull(cerhIntervencaoCaracterizacao.getIdeCerhObrasHidraulicas())){
			return cerhObrasHidraulicasDAO.buscar(Restrictions.eq("ideCerhObrasHidraulicas", cerhIntervencaoCaracterizacao.getIdeCerhObrasHidraulicas().getId()));
		}
		
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemCaracterizacao carregarCerhBarragemCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica)  {
		return cerhBarragemCaracterizacaoDAO.carregarCerhBarragemCaracterizacao(cerhLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemAproveitamentoHidreletrico carregarCerhBarragemAproveitamentoHidreletrico(CerhBarragemCaracterizacaoFinalidade caracterizacaoFinalidade)  {
		return cerhBarragemAproveitamentoHidreletricoDAO.carregar(caracterizacaoFinalidade.getIdeCerhBarragemCaracterizacaoFinalidade());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhBarragemCaracterizacaoFinalidade> listarCerhBarragemCaracterizacaoFinalidade(CerhBarragemCaracterizacao cerhBarragemCaracterizacao)  {
		return cerhBarragemCaracterizacaoFinalidadeDAO.listar(cerhBarragemCaracterizacao.getIdeCerhBarragemCaracterizacao());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhIntervencaoCaracterizacao carregarCerhIntervencaoCaracterizacao(CerhIntervencaoCaracterizacao cerhIntervencaoCaracterizacao) {
		try {
			return cerhIntervencaoCaracterizacaoDAO.carregarIdeCerhIntervencaoCaracterizacao(cerhIntervencaoCaracterizacao);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhBarragemAproveitamentoHidreletrico> carregarCerhBarragemAproveitamentoHidreletrico(CerhIntervencaoCaracterizacao cerhIntervencaoCaracterizacao)  {
		return cerhBarragemAproveitamentoHidreletricoDAO.carregar(cerhIntervencaoCaracterizacao);
	}
	
	
}
