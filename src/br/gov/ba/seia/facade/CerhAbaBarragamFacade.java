package br.gov.ba.seia.facade;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import br.gov.ba.seia.dao.TipoAproveitamentoHidreletricoDAOImpl;
import br.gov.ba.seia.dto.BarragemDTO;
import br.gov.ba.seia.entity.Barragem;
import br.gov.ba.seia.entity.CerhBarragemAproveitamentoHidreletrico;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacao;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhIntervencaoCaracterizacao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoAproveitamentoHidreletrico;
import br.gov.ba.seia.entity.TipoBarragem;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;
import br.gov.ba.seia.service.BarragemService;
import br.gov.ba.seia.service.TipoBarragemService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhAbaBarragamFacade extends CerhFinalidadeFacade {

	@EJB
	private BarragemService barragemService;
	@EJB
	private TipoBarragemService tipoBarragemService;
	@EJB
	private TipoAproveitamentoHidreletricoDAOImpl tipoAproveitamentoHidreletricoDAO;
	@EJB
	private CerhBarragemCaracterizacaoDAOImpl cerhBarragemCaracterizacaoDAO;
	@EJB
	private CerhBarragemCaracterizacaoFinalidadeDAOImpl cerhBarragemCaracterizacaoFinalidadeDAO;
	@EJB
	private CerhBarragemAproveitamentoHidreletricoDAOImpl cerhBarragemAproveitamentoHidreletricoDAO; 
	
	@EJB
	private CerhIntervencaoCaracterizacaoDAOImpl cerhIntervencaoCaracterizacaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<BarragemDTO> listarBarragem()  {
		return barragemService.montarListaBarragem();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BarragemDTO carregarBarragemOutrosDTO()  {
		return barragemService.carregarBarragemOutrosDTO();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Barragem carregarBarragem(Integer ideBarragem)  {
		return barragemService.carregar(ideBarragem);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BarragemDTO carregarBarragemDTO(Integer ideBarragem)  {
		BarragemDTO dto = barragemService.carregarBarragemDTO(ideBarragem);
		dto.setIndSelecionado(true);
		return dto;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoBarragem> listarTipoBarragem() throws Exception{
		return tipoBarragemService.listarTipoBarragemByIndAtivo();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAproveitamentoHidreletrico> listarTipoAproveitamentoHidreletrico() {
		return tipoAproveitamentoHidreletricoDAO.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemCaracterizacao carregarIdeCerhBarragemCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		try {
			return cerhBarragemCaracterizacaoDAO.carregarIdeCerhBarragemCaracterizacao(cerhLocalizacaoGeografica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemCaracterizacao carregarCerhBarragemCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		return cerhBarragemCaracterizacaoDAO.buscar(Restrictions.eq("ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica", cerhLocalizacaoGeografica.getId()));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhBarragemCaracterizacaoFinalidade> listarCerhBarragemCaracterizacaoFinalidade(CerhBarragemCaracterizacao cerhBarragemCaracterizacao)  {
		return cerhBarragemCaracterizacaoFinalidadeDAO.listar(cerhBarragemCaracterizacao.getIdeCerhBarragemCaracterizacao());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemAproveitamentoHidreletrico carregarCerhBarragemAproveitamentoHidreletrico(CerhBarragemCaracterizacaoFinalidade caracterizacaoFinalidade)  {
		return cerhBarragemAproveitamentoHidreletricoDAO.carregar(caracterizacaoFinalidade.getIdeCerhBarragemCaracterizacaoFinalidade());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhIntervencaoCaracterizacao carregarCerhIntervencaoCaracterizacao(CerhLocalizacaoGeografica clg)  {
		return cerhIntervencaoCaracterizacaoDAO.carregarCerhIntervencaoCaracterizacao(clg);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacao(CerhBarragemCaracterizacao cerhCaracterizacao)  {
		if(!Util.isNullOuVazio(cerhCaracterizacao.getCerhBarragemCaracterizacaoFinalidadeCollection())){
			for (CerhBarragemCaracterizacaoFinalidade cerhCaracterizacaoFinalidade : cerhCaracterizacao.getCerhBarragemCaracterizacaoFinalidadeCollection()) {
				if(cerhCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAproveitamentoHidreletrico()){
					super.excluirDadoFinalidade((CerhDadosFinalidadeInterface) cerhCaracterizacaoFinalidade.getIdeCerhBarragemAproveitamentoHidreletrico());
				}
			}
			super.excluirFinalidades(cerhCaracterizacao.getFinalidadeCollection());
		}
		super.excluirCaracterizacao(cerhCaracterizacao);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCerhCaracterizacao(CerhCaracterizacaoInterface caracterizacaoIF) {
		CerhBarragemCaracterizacao barragemCaracterizacao = (CerhBarragemCaracterizacao) caracterizacaoIF;
		if(barragemCaracterizacao.getIdeBarragem().isOutros()){
			barragemCaracterizacao.getIdeBarragem().setIndOrigemUsuario(true);
			salvarBarragem(barragemCaracterizacao);
		}
		super.salvarCerhCaracterizacao(caracterizacaoIF);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarBarragem(CerhBarragemCaracterizacao barragemCaracterizacao) {
		barragemService.salvar(barragemCaracterizacao.getIdeBarragem());
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideLocalizacaoGeografica", barragemCaracterizacao.getIdeCerhLocalizacaoGeografica().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		parametros.put("ideBarragem", barragemCaracterizacao.getIdeBarragem().getIdeBarragem());
		barragemService.gravarBarragemTheGeom(parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemAproveitamentoHidreletrico carregarCerhBarragemAproveitamentoHidreletrico(CerhBarragemCaracterizacao cerhBarragemCaracterizacao)  {
		return cerhBarragemAproveitamentoHidreletricoDAO.carregar(cerhBarragemCaracterizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemCaracterizacao carregarCerhBarragemCaracterizacao(CerhBarragemCaracterizacao cerhBarragemCaracterizacao) {
		return cerhBarragemCaracterizacaoDAO.carregarIdeCerhBarragemCaracterizacao(cerhBarragemCaracterizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhIntervencaoCaracterizacao> carregarCerhIntervencaoCaracterizacao(CerhBarragemCaracterizacao cerhBarragemCaracterizacao) {
		return cerhIntervencaoCaracterizacaoDAO.listar(Restrictions.eq("ideCerhLocalizacaoGeograficaBarragem.ideCerhLocalizacaoGeografica", cerhBarragemCaracterizacao.getIdeCerhLocalizacaoGeografica().getId()));
	}
	
}