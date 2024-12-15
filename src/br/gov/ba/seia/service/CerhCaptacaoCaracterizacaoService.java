package br.gov.ba.seia.service;

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

import br.gov.ba.seia.dao.CerhCaptacaoAbastecimentoIndustrialDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoAbastecimentoPublicoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoCaracterizacaoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoCaracterizacaoFinalidadeDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoDadosIrrigacaoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoDadosMineracaoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoMineracaoExtracaoAreiaDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoTermoeletricaDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoTransposicaoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoVazaoSazonalidadeDAOImpl;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacao;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaptacaoCaracterizacaoService {

	@EJB
	private CerhCaptacaoCaracterizacaoDAOImpl cerhCaptacaoCaracterizacaoDAOImpl;
	
	@EJB
	private CerhCaptacaoVazaoSazonalidadeDAOImpl cerhCaptacaoVazaoSazonalidadeDAOImpl;
	
	@EJB
	private CerhCaptacaoCaracterizacaoFinalidadeDAOImpl cerhCaptacaoCaracterizacaoFinalidadeDAOImpl;
	
	@EJB
	private CerhCaptacaoAbastecimentoIndustrialDAOImpl cerhCaptacaoAbastecimentoIndustrialDAOImpl;
	
	@EJB
	private CerhCaptacaoDadosIrrigacaoDAOImpl cerhCaptacaoDadosIrrigacaoDAOImpl;
	
	@EJB
	private CerhCaptacaoTransposicaoDAOImpl cerhCaptacaoTransposicaoDAOImpl;
	
	@EJB
	private CerhCaptacaoDadosMineracaoDAOImpl cerhCaptacaoDadosMineracaoDAOImpl;
	
	@EJB
	private CerhCaptacaoTermoeletricaDAOImpl cerhCaptacaoTermoeletricaDAOImpl;
	
	@EJB
	private CerhCaptacaoAbastecimentoPublicoDAOImpl captacaoAbsPublicoDAO;
	
	@EJB
	private CerhCaptacaoMineracaoExtracaoAreiaDAOImpl captacaoExtracaoAreiaDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarParaHistorico(CerhLocalizacaoGeografica clg) {
		try {
			CerhCaptacaoCaracterizacao ccc = cerhCaptacaoCaracterizacaoDAOImpl.carregarParaHistorico(clg);
			
			ccc.setCerhCaptacaoVazaoSazonalidadeCollection(
				cerhCaptacaoVazaoSazonalidadeDAOImpl.listar(Restrictions.eq("ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao", clg.getIdeCerhCaptacaoCaracterizacao().getId()), Order.asc("ideMes"))
			);
			

			Collection<CerhCaptacaoCaracterizacaoFinalidade> finalidades = cerhCaptacaoCaracterizacaoFinalidadeDAOImpl.listarParaHistorico(clg.getIdeCerhCaptacaoCaracterizacao().getId());
			

			for (CerhCaptacaoCaracterizacaoFinalidade finalidade : finalidades) {
				if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getId())){
					finalidade.setCerhCaptacaoAbastecimentoIndustrialCollection(cerhCaptacaoAbastecimentoIndustrialDAOImpl.listar(finalidade.getId()));
				}
				
				else if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.IRRIGACAO.getId())){
					finalidade.setCerhCaptacaoDadosIrrigacaoCollection(null);
				}
				
				else if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.MINERACAO.getId())){
					finalidade.setCerhCaptacaoDadosMineracaoCollection(null);
				}
				
				else if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.TERMOELETRICA.getId())){
					finalidade.setCerhCaptacaoTransposicaoCollection(null);
				}
				
				else if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.TRANSPOSICAO.getId())){
					finalidade.setCerhCaptacaoTransposicaoCollection(null);
				}
			}
			ccc.setCerhCaptacaoCaracterizacaoFinalidadeCollection(finalidades);
			clg.setIdeCerhCaptacaoCaracterizacao(ccc);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarCaptacaoCaracterizacao(CerhLocalizacaoGeografica clg) {
		try {
			CerhCaptacaoCaracterizacao ccc = cerhCaptacaoCaracterizacaoDAOImpl.carregarParaHistorico(clg);
			
			if(!Util.isNullOuVazio(ccc)) {
				
				ccc.setCerhCaptacaoVazaoSazonalidadeCollection(
						cerhCaptacaoVazaoSazonalidadeDAOImpl.listar(Restrictions.eq("ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao", clg.getIdeCerhCaptacaoCaracterizacao().getId()), Order.asc("ideMes"))
						);
				
				
				Collection<CerhCaptacaoCaracterizacaoFinalidade> finalidades = cerhCaptacaoCaracterizacaoFinalidadeDAOImpl.listarParaHistorico(clg.getIdeCerhCaptacaoCaracterizacao().getId());
				
				for (CerhCaptacaoCaracterizacaoFinalidade finalidade : finalidades) {
					if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getId())){
						finalidade.setCerhCaptacaoAbastecimentoIndustrialCollection(cerhCaptacaoAbastecimentoIndustrialDAOImpl.listar(finalidade.getId()));
					}
					
					else if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.IRRIGACAO.getId())){
						finalidade.setCerhCaptacaoDadosIrrigacaoCollection(cerhCaptacaoDadosIrrigacaoDAOImpl.listar(finalidade.getId()));
					}
					
					else if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.MINERACAO.getId())){
						finalidade.setCerhCaptacaoDadosMineracaoCollection(cerhCaptacaoDadosMineracaoDAOImpl.listar(finalidade.getId()));
					}
					
					else if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.TERMOELETRICA.getId())){
						finalidade.setIdeCerhCaptacaoTermoeletrica(cerhCaptacaoTermoeletricaDAOImpl.carregar(finalidade.getId()));
					}
					
					else if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.TRANSPOSICAO.getId())){
						finalidade.setCerhCaptacaoTransposicaoCollection(cerhCaptacaoTransposicaoDAOImpl.listar(finalidade.getId()));
					}
					
					else if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_PUBLICO.getId())){
						finalidade.setIdeCerhCaptacaoAbastecimentoPublico(captacaoAbsPublicoDAO.carregar(finalidade.getId()));
					}
					
					else if(finalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.MINERACAO_EXTRACAO_AREIA.getId())){
						finalidade.setIdeCerhCaptacaoMineracaoExtracaoAreia(captacaoExtracaoAreiaDAO.buscar(finalidade.getId()));
						
					}
					
				}
				
				ccc.setCerhCaptacaoCaracterizacaoFinalidadeCollection(finalidades);
			}

			clg.setIdeCerhCaptacaoCaracterizacao(ccc);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}	
	
}
