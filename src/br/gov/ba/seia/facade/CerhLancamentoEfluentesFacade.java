package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.CerhLancamentoAbastecimentoPublicoDAOImpl;
import br.gov.ba.seia.dao.CerhLancamentoCaracterizacaoOrigemDAOImpl;
import br.gov.ba.seia.dao.CerhLancamentoEfluenteCaracterizacaoDAOImpl;
import br.gov.ba.seia.dao.CerhLancamentoEfluenteSazonalidadeDAOImpl;
import br.gov.ba.seia.dao.CerhLancamentoOutrosUsosDAOImpl;
import br.gov.ba.seia.dao.CerhProcessoDAOImpl;
import br.gov.ba.seia.dao.CerhTipoPrestadorServicoDAOImpl;
import br.gov.ba.seia.dao.CerhTratamentoEfluentesDAOImpl;
import br.gov.ba.seia.dao.TipoCorpoHidricoDAOImpl;
import br.gov.ba.seia.entity.CerhLancamentoAbastecimentoPublico;
import br.gov.ba.seia.entity.CerhLancamentoCaracterizacaoOrigem;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteCaracterizacao;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteSazonalidade;
import br.gov.ba.seia.entity.CerhLancamentoOutrosUsos;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhOutrosUsos;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhTipoPrestadorServico;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.CerhTratamentoEfluente;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhLancamentoEfluentesFacade extends CerhCaracterizacaoCaptacaoLancamentoFacade {

	@EJB
	private CerhTratamentoEfluentesDAOImpl cerhTratamentoEfluentesDAOImpl;
	
	@EJB
	private CerhTipoPrestadorServicoDAOImpl cerhTipoPrestadorServicoDAOImpl;
	
	@EJB
	private CerhLancamentoOutrosUsosDAOImpl cerhLancamentoOutrosUsosDAOImpl;
	
	@EJB
	private CerhLancamentoEfluenteCaracterizacaoDAOImpl cerhLancamentoEfluenteCaracterizacaoDAOImpl;
	
	@EJB
	private CerhLancamentoCaracterizacaoOrigemDAOImpl cerhLancamentoCaracterizacaoOrigemDAOImpl;
	
	@EJB
	private CerhLancamentoEfluenteSazonalidadeDAOImpl cerhLancamentoEfluenteSazonalidadeDAOImpl;
	
	@EJB
	private CerhLancamentoAbastecimentoPublicoDAOImpl cerhLancamentoAbastecimentoPublicoDAOImpl;
	
	@EJB
	private CerhProcessoDAOImpl cerhProcessoListagemDAOImpl;
	
	@EJB
	private TipoCorpoHidricoDAOImpl tipoCorpoHidricoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTratamentoEfluente> listarCerhTratamentoEfluente(){
		try{
			return cerhTratamentoEfluentesDAOImpl.listar(Order.asc("dscTratamentoEfluente"));
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhLancamentoOutrosUsos> listarCerhLancamentoOutrosUsos(){
		try{
			return cerhLancamentoOutrosUsosDAOImpl.listar(Order.asc("ideCerhLancamentoOutrosUsos"));
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhLocalizacaoGeografica> listarCerhLocalizacaoGeografia(CerhTipoUso cerhTipoUso)  throws Exception{
		try{
			Collection<CerhLocalizacaoGeografica> lista = super.cerhLocalizacaoGeograficaDAO.listar(cerhTipoUso);
			
			for (CerhLocalizacaoGeografica cerhLocalizacaoGeografica : lista) {
				if(!Util.isNullOuVazio(cerhLocalizacaoGeografica.getIdeCerhLancamentoEfluenteCaracterizacao())){
					cerhLocalizacaoGeografica.getIdeCerhLancamentoEfluenteCaracterizacao()
						.setCerhLancamentoEfluenteSazonalidadeCollection(listarCerhLancamentoEfluenteSazonalidade(cerhLocalizacaoGeografica.getIdeCerhLancamentoEfluenteCaracterizacao()));
				}
				
			}
			return lista;
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhLancamentoEfluenteSazonalidade> listarCerhLancamentoEfluenteSazonalidade(CerhLancamentoEfluenteCaracterizacao cerhLancamentoEfluenteCaracterizacao)  throws Exception{
		try{
			return cerhLancamentoEfluenteSazonalidadeDAOImpl.listarPorCerhLancamentoEfluenteCaracterizacao(cerhLancamentoEfluenteCaracterizacao);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhLancamentoCaracterizacaoOrigem> listarCerhLancamentoCaracterizacaoOrigem(CerhLancamentoEfluenteCaracterizacao cerhLancamentoEfluenteCaracterizacao) {
		try{
			return cerhLancamentoCaracterizacaoOrigemDAOImpl.listarCerhLancamentoCaracterizacaoOrigem(cerhLancamentoEfluenteCaracterizacao);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhOutrosUsos buscarCerhLacamentoOutrosUsos(CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		try{
			return super.outrosUsosDAO.buscarCerhLacamentoOutrosUsos(cerhLancamentoCaracterizacaoOrigem);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhTipoPrestadorServico buscarCerhTipoPrestadorServico(CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		try{
			return cerhTipoPrestadorServicoDAOImpl.buscarCerhTipoPrestadorServico(cerhLancamentoCaracterizacaoOrigem);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhProcesso carregarCerhProcesso(CerhProcesso ideCerhProcesso) {
		try{
			return cerhProcessoListagemDAOImpl.carregarCerhProcesso(ideCerhProcesso);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhLancamentoEfluenteCaracterizacao carregarCerhLancamentoEfluenteCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		try{
			CerhLancamentoEfluenteCaracterizacao cerhLancamentoEfluenteCaracterizacao = null;
			if(cerhLocalizacaoGeografica.getIdeCerhLocalizacaoGeografica()!=null){
				cerhLancamentoEfluenteCaracterizacao = cerhLancamentoEfluenteCaracterizacaoDAOImpl.carregar(cerhLocalizacaoGeografica);
				
				if(cerhLancamentoEfluenteCaracterizacao != null){
					cerhLancamentoEfluenteCaracterizacao.setIdeCerhLocalizacaoGeografica(cerhLocalizacaoGeografica);
					Collection<CerhLancamentoCaracterizacaoOrigem> listarCerhLancamentoCaracterizacaoOrigem = cerhLancamentoCaracterizacaoOrigemDAOImpl.listarCerhLancamentoCaracterizacaoOrigem(cerhLancamentoEfluenteCaracterizacao);
					
					for (CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem : listarCerhLancamentoCaracterizacaoOrigem) {
						if(cerhLancamentoCaracterizacaoOrigem.getIdeTipoFinalidadeUsoAgua().isFinalidadeEsgotamentoSanitario()){
							cerhLancamentoCaracterizacaoOrigem.setIdeCerhCaptacaoAbastecimentoPublico(
								cerhLancamentoAbastecimentoPublicoDAOImpl.buscar(Restrictions.eq("ideCerhLancamentoCaracterizacaoOrigem.ideCerhLancamentoCaracterizacaoOrigem", cerhLancamentoCaracterizacaoOrigem.getId()))
							);
						}
						else if(cerhLancamentoCaracterizacaoOrigem.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos()){
							cerhLancamentoCaracterizacaoOrigem
								.setIdeCerhLancamentoOutrosUsos(cerhLancamentoOutrosUsosDAOImpl.buscar(Restrictions.eq("ideCerhLancamentoCaracterizacaoOrigem.ideCerhLancamentoCaracterizacaoOrigem", cerhLancamentoCaracterizacaoOrigem.getId())));
						}
					}
					
					cerhLancamentoEfluenteCaracterizacao.setCerhLancamentoCaracterizacaoOrigemCollection(listarCerhLancamentoCaracterizacaoOrigem);
				}
				
				if(!Util.isNullOuVazio(cerhLancamentoEfluenteCaracterizacao)){
					cerhLancamentoEfluenteCaracterizacao.setCerhLancamentoEfluenteSazonalidadeCollection(
						cerhLancamentoEfluenteSazonalidadeDAOImpl
							.listar(Restrictions.eq("ideCerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao", cerhLancamentoEfluenteCaracterizacao.getId()))
					);
					
				}
				
			}
			
			cerhLocalizacaoGeografica.setIdeCerhLancamentoEfluenteCaracterizacao(cerhLancamentoEfluenteCaracterizacao);
			return cerhLocalizacaoGeografica.getIdeCerhLancamentoEfluenteCaracterizacao();
					
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhLancamentoAbastecimentoPublico obterCerhFinalidadeAbastecimentoPublico(CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		try{
			return cerhLancamentoAbastecimentoPublicoDAOImpl.buscar(cerhLancamentoCaracterizacaoOrigem);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhLancamentoOutrosUsos obterCerhFinalidadeOutrosUsos(CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		try{
			return cerhLancamentoOutrosUsosDAOImpl.buscar(Restrictions.eq("ideCerhLancamentoCaracterizacaoOrigem.ideCerhLancamentoCaracterizacaoOrigem", cerhLancamentoCaracterizacaoOrigem.getId()));
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarCerhLancamentoAbastecimentoPublico (CerhLancamentoAbastecimentoPublico cerhLancamentoAbastecimentoPublico){
		try{
			cerhLancamentoAbastecimentoPublicoDAOImpl.deletar(cerhLancamentoAbastecimentoPublico);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarCerhLancamentoOutrosUsos (CerhLancamentoOutrosUsos cerhLancamentoOutrosUsos){
		try{
			 cerhLancamentoOutrosUsosDAOImpl.deletar(cerhLancamentoOutrosUsos);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarFinalidadeOutra(CerhLancamentoOutrosUsos cerhLancamentoOutrosUsos) {
		try{
			cerhLancamentoOutrosUsosDAOImpl.deletar(cerhLancamentoOutrosUsos);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCerhLancamentoSazonalidade(CerhLancamentoEfluenteCaracterizacao cerhLancamentoEfluenteCaracterizacao) {
		try{
			cerhLancamentoEfluenteSazonalidadeDAOImpl.deletar(cerhLancamentoEfluenteCaracterizacao);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCerhLancamentoAbastecimentoPublico(Integer ideCerhLancamentoCaracterizacaoOrigem) {
		try{
			cerhLancamentoAbastecimentoPublicoDAOImpl.excluirCerhLancamentoAbastecimentoPublico(ideCerhLancamentoCaracterizacaoOrigem);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
}
