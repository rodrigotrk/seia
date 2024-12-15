package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.CerhCaptacaoMineracaoExtracaoAreiaDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoTermoeletricaDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoTransposicaoDAOImpl;
import br.gov.ba.seia.dao.CerhFinalidadeTransposicaoDAOImpl;
import br.gov.ba.seia.dto.BarragemDTO;
import br.gov.ba.seia.entity.Barragem;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhCaptacaoMineracaoExtracaoAreia;
import br.gov.ba.seia.entity.CerhCaptacaoTermoeletrica;
import br.gov.ba.seia.entity.CerhCaptacaoTransposicao;
import br.gov.ba.seia.entity.CerhFinalidadeTransposicao;
import br.gov.ba.seia.service.BarragemService;

/**
 * @author eduardo.fernandes 
 * @since 23/03/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhAbaCaptacaoSuperficialFacade  extends CerhAbasCaptacoesFacade {

	@EJB
	private BarragemService barragemService;
	
	@EJB
	private CerhFinalidadeTransposicaoDAOImpl transposicaoDAO;
	
	@EJB
	private CerhCaptacaoMineracaoExtracaoAreiaDAOImpl captacaoExtracaoAreiaDAO;
	
	@EJB
	private CerhCaptacaoTermoeletricaDAOImpl captacaoTermoeletricaDAO;
	
	@EJB
	private CerhCaptacaoTransposicaoDAOImpl captacaoTransposicaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<BarragemDTO> listarBarragem()   {
		return barragemService.montarListaBarragem();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BarragemDTO carregarBarragemOutrosDTO()  {
		return barragemService.carregarBarragemOutrosDTO();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Barragem carregarBarragem(Integer ide)  {
		return barragemService.carregar(ide);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BarragemDTO carregarBarragemDTO(Integer ideBarragem)  {
		BarragemDTO dto = barragemService.carregarBarragemDTO(ideBarragem);
		dto.setIndSelecionado(true);
		return dto;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhFinalidadeTransposicao> listarTransposicao() {
		return transposicaoDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoMineracaoExtracaoAreia carregarCaptacaoExtracaoAreia(CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade)  {
		return captacaoExtracaoAreiaDAO.buscar(Restrictions.eq("ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade", caracterizacaoFinalidade.getId()));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoTermoeletrica carregarCaptacaoTermoeletrica(CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade)  {
		return captacaoTermoeletricaDAO.carregar(caracterizacaoFinalidade.getIdeCerhCaptacaoCaracterizacaoFinalidade());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoTransposicao> carregarCaptacaoTransposicao(CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade)  {
		return captacaoTransposicaoDAO.listar(caracterizacaoFinalidade.getIdeCerhCaptacaoCaracterizacaoFinalidade());
	}
	
}
