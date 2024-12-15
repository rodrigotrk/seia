package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.CerhCaptacaoAbastecimentoIndustrialDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoAbastecimentoPublicoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoCaracterizacaoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoCaracterizacaoFinalidadeDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoDadosIrrigacaoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoDadosMineracaoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoMineracaoExtracaoAreiaDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoOutrosUsoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoVazaoSazonalidadeDAOImpl;
import br.gov.ba.seia.dto.CerhAbaDTO;
import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhCaptacaoAbastecimentoIndustrial;
import br.gov.ba.seia.entity.CerhCaptacaoAbastecimentoPublico;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacao;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhCaptacaoDadosIrrigacao;
import br.gov.ba.seia.entity.CerhCaptacaoDadosMineracao;
import br.gov.ba.seia.entity.CerhCaptacaoOutrosUsos;
import br.gov.ba.seia.entity.CerhCaptacaoTransposicao;
import br.gov.ba.seia.entity.CerhCaptacaoVazaoSazonalidade;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.MetodoIrrigacao;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;
import br.gov.ba.seia.service.MetodoIrrigacaoService;
import br.gov.ba.seia.service.UnidadeMedidaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 20/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhAbasCaptacoesFacade extends CerhCaracterizacaoCaptacaoLancamentoFacade {
	
	
	@EJB
	private UnidadeMedidaService unidadeMedidaService;
	
	@EJB
	private MetodoIrrigacaoService metodoIrrigacaoService;

	@EJB
	private CerhCaptacaoCaracterizacaoDAOImpl cerhCaptacaoCaracterizacaoDAO;
	
	@EJB
	private CerhCaptacaoVazaoSazonalidadeDAOImpl captacaoSazonalidadeDAO;
	
	@EJB
	private CerhCaptacaoCaracterizacaoFinalidadeDAOImpl captacaoFinalidadeDAO;
	
	@EJB
	private CerhCaptacaoAbastecimentoIndustrialDAOImpl captacaoAbsIndustrialDAO;
	
	@EJB
	private CerhCaptacaoAbastecimentoPublicoDAOImpl captacaoAbsPublicoDAO;
	
	@EJB
	private CerhCaptacaoDadosIrrigacaoDAOImpl captacaoIrrigacaoDAO;
	
	@EJB
	private CerhCaptacaoDadosMineracaoDAOImpl captacaoMineracaoDAO;
	
	@EJB
	private CerhCaptacaoMineracaoExtracaoAreiaDAOImpl cerhCaptacaoMineracaoExtracaoAreiaDAO;
	
	@EJB
	private CerhCaptacaoOutrosUsoDAOImpl captacaoOutrosUsosDAO;

	/*
	 * XXX - INI CARREGAR
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoCaracterizacao carregarIdeCerhCaptacaoCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		try {
			return cerhCaptacaoCaracterizacaoDAO.carregar(cerhLocalizacaoGeografica);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoCaracterizacao carregarCerhCaptacaoCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica)  {
		return cerhCaptacaoCaracterizacaoDAO.carregar(cerhLocalizacaoGeografica);
	}
	/*
	 * XXX - FIM CARREGAR
	 */
	
	/*
	 * XXX - INI LISTAR
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoVazaoSazonalidade> listarCerhCaptacaoVazaoSazonalidade(Integer ideCerhCaptacaoCaracterizacao)  {
		return captacaoSazonalidadeDAO.listar(ideCerhCaptacaoCaracterizacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoCaracterizacaoFinalidade> listarCerhCaptacaoCaracterizacaoFinalidade(Integer ideCerhCaptacaoCaracterizacao)  {
		return captacaoFinalidadeDAO.listar(ideCerhCaptacaoCaracterizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<UnidadeMedida> listarUnidadeMedida(TipologiaEnum tipologiaEnum) throws Exception {
		return unidadeMedidaService.listarUnidadeMedidaTipologia(tipologiaEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoAbastecimentoIndustrial> listarCaptacaoAbsIndustrial(CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade)  {
		return captacaoAbsIndustrialDAO.listar(caracterizacaoFinalidade.getIdeCerhCaptacaoCaracterizacaoFinalidade());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoAbastecimentoPublico listarCaptacaoAbsPublico(CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade)  {
		return captacaoAbsPublicoDAO.carregar(caracterizacaoFinalidade.getIdeCerhCaptacaoCaracterizacaoFinalidade());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoDadosIrrigacao> listarCaptacaoIrrigacao(CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade)  {
		return captacaoIrrigacaoDAO.listar(caracterizacaoFinalidade.getIdeCerhCaptacaoCaracterizacaoFinalidade());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoDadosMineracao> listarCaptacaoMineracao(CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade)  {
		return captacaoMineracaoDAO.listar(caracterizacaoFinalidade.getIdeCerhCaptacaoCaracterizacaoFinalidade());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoOutrosUsos listarCaptacaoOutrosUsos(CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade)  {
		return captacaoOutrosUsosDAO.carregar(caracterizacaoFinalidade.getIdeCerhCaptacaoCaracterizacaoFinalidade());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MetodoIrrigacao> listarMetodoIrrigacao()  {
		return metodoIrrigacaoService.listarMetodoIrrigacao();
	}

	/*
	 * XXX - FIM LISTAR
	 */

	
	/*
	 * XXX - INI SALVAR
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAba(Cerh cerh, CerhAbaDTO dto)  {
		for (CerhCaracterizacaoDTO cerhCaracterizacaoDTO : dto.getListaCaracterizacaoDTO()) {
			CerhCaptacaoCaracterizacao captacaoCaracterizacao = cerhCaracterizacaoDTO.getCerhLocalizacaoGeografica().getIdeCerhCaptacaoCaracterizacao();
			if(!Util.isNull(captacaoCaracterizacao) && !Util.isNull(captacaoCaracterizacao.getIdeCerhCaptacaoCaracterizacao())){
				captacaoCaracterizacao = carregarCerhCaptacaoCaracterizacao(captacaoCaracterizacao.getIdeCerhLocalizacaoGeografica());
				super.salvarCerhCaracterizacao(captacaoCaracterizacao);
			}
			super.salvarCerhLocalizacaoGeografica(cerh,cerhCaracterizacaoDTO.getCerhLocalizacaoGeografica(), dto.getTipoUsoRecursoHidricoEnum());
		}
	}
	/**
	 * XXX - FIM SALVAR
	 */
	
	/*
	 * XXX - INI EXCLUIR
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSazonalidades(CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao)  {
		captacaoSazonalidadeDAO.excluir(cerhCaptacaoCaracterizacao.getIdeCerhCaptacaoCaracterizacao());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacao(CerhCaptacaoCaracterizacao captacaoCaracterizacao)   {
		if(!Util.isNullOuVazio(captacaoCaracterizacao.getCerhCaptacaoCaracterizacaoFinalidadeCollection())){
			for (CerhCaptacaoCaracterizacaoFinalidade cerhCaracterizacaoFinalidade : captacaoCaracterizacao.getCerhCaptacaoCaracterizacaoFinalidadeCollection()) {
				if(cerhCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAbastecimentoIndustrial()){
					for (CerhCaptacaoAbastecimentoIndustrial cerhCaptacaoAbastecimentoIndustrial : cerhCaracterizacaoFinalidade.getCerhCaptacaoAbastecimentoIndustrialCollection()) {
						super.excluirDadoFinalidade((CerhDadosFinalidadeInterface) cerhCaptacaoAbastecimentoIndustrial);
					}
				}
				else if(cerhCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAbastecimentoPublico()){
					super.excluirDadoFinalidade((CerhDadosFinalidadeInterface) cerhCaracterizacaoFinalidade.getIdeCerhCaptacaoAbastecimentoPublico());
				}
				else if(cerhCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeIrrigacao()){
					for (CerhCaptacaoDadosIrrigacao cerhCaptacaoDadosIrrigacao : cerhCaracterizacaoFinalidade.getCerhCaptacaoDadosIrrigacaoCollection()) {
						super.excluirDadoFinalidade((CerhDadosFinalidadeInterface) cerhCaptacaoDadosIrrigacao);
					}
				}
				else if(cerhCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracao()){
					for (CerhCaptacaoDadosMineracao cerhCaptacaoDadosMineracao : cerhCaracterizacaoFinalidade.getCerhCaptacaoDadosMineracaoCollection()) {
						super.excluirDadoFinalidade((CerhDadosFinalidadeInterface) cerhCaptacaoDadosMineracao);
					}
				}
				else if(cerhCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracaoAreia()){
					super.excluirDadoFinalidade((CerhDadosFinalidadeInterface) cerhCaracterizacaoFinalidade.getIdeCerhCaptacaoMineracaoExtracaoAreia());
				}
				else if(cerhCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos()){
					super.excluirDadoFinalidade((CerhDadosFinalidadeInterface) cerhCaracterizacaoFinalidade.getIdeCerhCaptacaoOutrosUso());
				}
				else if(cerhCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTermoeletrica()){
					super.excluirDadoFinalidade((CerhDadosFinalidadeInterface) cerhCaracterizacaoFinalidade.getIdeCerhCaptacaoTermoeletrica());
				}
				else if(cerhCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeTransposicao()){
					for (CerhCaptacaoTransposicao cerhCaptacaoTransposicao : cerhCaracterizacaoFinalidade.getCerhCaptacaoTransposicaoCollection()) {
						super.excluirDadoFinalidade((CerhDadosFinalidadeInterface) cerhCaptacaoTransposicao);
					}
				}
			}
			super.excluirFinalidades(captacaoCaracterizacao.getFinalidadeCollection());
		}
		excluirSazonalidades(captacaoCaracterizacao);
		super.excluirCaracterizacao(captacaoCaracterizacao);
	}
	/*
	 * XXX - FIM EXCLUIR
	 */

	public CerhCaptacaoOutrosUsos carregarCerhCaptacaoOutrosUsos(CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade)  {
		return captacaoOutrosUsosDAO.carregar(cerhCaptacaoCaracterizacaoFinalidade.getIdeCerhCaptacaoCaracterizacaoFinalidade());
	}
	
	public CerhCaptacaoCaracterizacaoFinalidade carregarFinalidade(CerhCaptacaoCaracterizacaoFinalidade finalidade) {
		finalidade.setIdeCerhCaptacaoMineracaoExtracaoAreia(cerhCaptacaoMineracaoExtracaoAreiaDAO.buscar(finalidade.getId()));
		return finalidade;
	}
}
