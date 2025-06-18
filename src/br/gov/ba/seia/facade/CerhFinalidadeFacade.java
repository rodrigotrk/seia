package br.gov.ba.seia.facade;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.CerhCaptacaoAbastecimentoIndustrialDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoAbastecimentoPublicoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoDadosIrrigacaoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoDadosMineracaoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoOutrosUsoDAOImpl;
import br.gov.ba.seia.dao.CerhCaptacaoTermoeletricaDAOImpl;
import br.gov.ba.seia.dao.CerhCaracterizacaoFinalidadeDAO;
import br.gov.ba.seia.dao.CerhDadosFinalidadeDAO;
import br.gov.ba.seia.dao.CerhLancamentoCaracterizacaoOrigemDAOImpl;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhCaptacaoOutrosUsos;
import br.gov.ba.seia.entity.CerhLancamentoCaracterizacaoOrigem;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;
import br.gov.ba.seia.service.TipologiaAtividadeService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhFinalidadeFacade extends CerhAbasFacade {

	@EJB
	private TipologiaAtividadeService tipologiaAtividadeService;

	@EJB
	private CerhCaracterizacaoFinalidadeDAO caracterizacaoFinalidadeDAO;

	@EJB
	private CerhDadosFinalidadeDAO dadoFinalidadeDAO;

	@EJB
	private CerhCaptacaoOutrosUsoDAOImpl cerhCaptacaoOutrosUsoDAOImpl;

	@EJB
	private CerhCaptacaoDadosIrrigacaoDAOImpl cerhCaptacaoDadosIrrigacaoDAOImpl;

	@EJB
	private CerhCaptacaoAbastecimentoIndustrialDAOImpl cerhCaptacaoAbastecimentoIndustrialDAOImpl;

	@EJB
	private CerhCaptacaoAbastecimentoPublicoDAOImpl cerhCaptacaoAbastecimentoPublicoDAOImpl;

	@EJB
	private CerhCaptacaoDadosMineracaoDAOImpl cerhCaptacaoDadosMineracaoDAOImpl;

	@EJB
	private CerhCaptacaoTermoeletricaDAOImpl cerhCaptacaoTermoeletricaDAOImpl;
	
	@EJB
	private CerhLancamentoCaracterizacaoOrigemDAOImpl cerhLancamentoCaracterizacaoOrigemDAOImpl;



	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipologiaAtividade> listarTipologiaAtividadeIrrigacao() {
		return tipologiaAtividadeService.buscarTipologiaAtividadeByIdeTipologia(new Tipologia(TipologiaEnum.AGRICULTURA_IRRIGADA_OUT.getId()));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoFinalidade(CerhFinalidadeUsoAguaInterface caracterizacaoFinalidade)  {
		if(caracterizacaoFinalidade!= null &&
		   caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua() != null &&
		   caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua()!= null){

			if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.IRRIGACAO.getId()){
				cerhCaptacaoDadosIrrigacaoDAOImpl.excluir((CerhCaptacaoCaracterizacaoFinalidade) caracterizacaoFinalidade);
			}

			else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getId()){
				cerhCaptacaoAbastecimentoIndustrialDAOImpl.excluir((CerhCaptacaoCaracterizacaoFinalidade) caracterizacaoFinalidade);
			}

			else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_PUBLICO.getId()){
				cerhCaptacaoAbastecimentoPublicoDAOImpl.excluir((CerhCaptacaoCaracterizacaoFinalidade) caracterizacaoFinalidade);
			}

			else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.MINERACAO.getId()){
				cerhCaptacaoDadosMineracaoDAOImpl.excluir((CerhCaptacaoCaracterizacaoFinalidade) caracterizacaoFinalidade);
			}

			else if(caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua() == TipoFinalidadeUsoAguaEnum.TERMOELETRICA.getId()){
				if(caracterizacaoFinalidade instanceof CerhLancamentoCaracterizacaoOrigem) {
					cerhLancamentoCaracterizacaoOrigemDAOImpl.deletar((CerhLancamentoCaracterizacaoOrigem) caracterizacaoFinalidade);
				}else {
					cerhCaptacaoTermoeletricaDAOImpl.excluir((CerhCaptacaoCaracterizacaoFinalidade) caracterizacaoFinalidade);
				}
			}
		}

		caracterizacaoFinalidadeDAO.excluir(caracterizacaoFinalidade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCerhCaptacaoOutrosUsos(CerhCaptacaoOutrosUsos cerhCaptacaoOutrosUsos)  {
		cerhCaptacaoOutrosUsoDAOImpl.excluir(cerhCaptacaoOutrosUsos);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDadoFinalidade(CerhDadosFinalidadeInterface dadosFinalidade)  {
		dadoFinalidadeDAO.excluir(dadosFinalidade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFinalidades(Collection<CerhFinalidadeUsoAguaInterface> cerhFinalidadeUsoAguaInterfaceColl)  {
		if(!Util.isNullOuVazio(cerhFinalidadeUsoAguaInterfaceColl)){
			for (CerhFinalidadeUsoAguaInterface finalidadeUsoAgua : cerhFinalidadeUsoAguaInterfaceColl) {
				excluirCaracterizacaoFinalidade(finalidadeUsoAgua);
			}
		}
	}
}
