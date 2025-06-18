package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.DeclaracaoQueimaControladaDaoImpl;
import br.gov.ba.seia.dao.DeclaracaoQueimaControladaImovelDaoImpl;
import br.gov.ba.seia.dao.DeclaracaoQueimaControladaResponsavelTecnicoDaoImpl;
import br.gov.ba.seia.dao.ElementoIntervencaoQueimaControladaDaoImpl;
import br.gov.ba.seia.dao.ObjetivoQueimaControladaDaoImpl;
import br.gov.ba.seia.dao.PerguntaRequerimentoDaoImpl;
import br.gov.ba.seia.dao.TecnicaQueimaControladaDaoImpl;
import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.DeclaracaoQueimaControlada;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaElementoIntervencao;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaImovel;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaImovelObjetivoQueimaControlada;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaResponsavelTecnico;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaTecnicaUtilizada;
import br.gov.ba.seia.entity.ElementoIntervencaoQueimaControlada;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ObjetivoQueimaControlada;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.TecnicaQueimaControlada;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoQueimaControladaFacade extends AtoDeclaratorioFacade {
	
	@EJB
	DeclaracaoQueimaControladaDaoImpl dqcDaoImpl;
	
	@EJB
	DeclaracaoQueimaControladaImovelDaoImpl dqcImovelDaoImpl;
	
	@EJB
	DeclaracaoQueimaControladaResponsavelTecnicoDaoImpl dqcRtDaoImpl;
	
	@EJB
	TecnicaQueimaControladaDaoImpl tqcDaoImpl;
	
	@EJB
	ObjetivoQueimaControladaDaoImpl oqcDaoImpl;
	
	@EJB
	ElementoIntervencaoQueimaControladaDaoImpl eiqcDaoImpl;
	
	@EJB
	PerguntaRequerimentoDaoImpl prDaoImpl;

	@EJB
	LocalizacaoGeograficaServiceFacade localizacaoGeograficaFacade;
	
	/************
	/*			*
	//XXX: DQC  *
	/* 			*
	/************/

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDeclaracaoQueimaControlada(DeclaracaoQueimaControlada dqc) {
		try {
			dqcDaoImpl.salvar(dqc);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoQueimaControlada buscarDeclaracaoQueimaControladaPorAtoDeclaratorio(AtoDeclaratorio ad) {
		try {
			return dqcDaoImpl.buscarPorAtoDeclaratorio(ad);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/****************
	/*				*
	//XXX: IMÓVEL   *
	/* 				*
	/****************/

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoQueimaControladaImovel> listarDeclaracaoQueimaControladaImovelPorDQC(DeclaracaoQueimaControlada dqc) {
		try {
			List<DeclaracaoQueimaControladaImovel> dqci = dqcImovelDaoImpl.listarPorDeclaracaoQueimaControlada(dqc);
			
			for (DeclaracaoQueimaControladaImovel i : dqci) {
				if(!Util.isNullOuVazio(i.getIdeLocalizacaoGeografica())) {
					i.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(
							localizacaoGeograficaFacade.getDadoGeograficoCollection(i.getIdeLocalizacaoGeografica()));
					
					i.getIdeLocalizacaoGeografica().setParamPersistDadoGeoCollection(
							localizacaoGeograficaFacade.listarParamPersistDadoGeoPorLocalizacaoGeografica(i.getIdeLocalizacaoGeografica()));
				}
			}
			
			return dqci;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDeclaracaoQueimaControladaImovel(DeclaracaoQueimaControladaImovel dqcI) {
		try {
			if(Util.isNullOuVazio(dqcI.getIdeLocalizacaoGeografica())) {
				dqcI.setIdeLocalizacaoGeografica(null);
			}
			
			dqcImovelDaoImpl.salvar(dqcI);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDeclaracaoQueimaControladaImovel(DeclaracaoQueimaControladaImovel dqcI) {
		try {
			dqcImovelDaoImpl.excluir(dqcI);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		try {
			localizacaoGeograficaFacade.excluirTudoPorLocalizacaoGeografica(localizacaoGeografica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/****************************
	/*							*
	//XXX: RESPONSÁVEL TÉCNICO  *
	/* 							*
	/****************************/

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDQCResponsavelTecnico(DeclaracaoQueimaControladaResponsavelTecnico dqcRt) {
		try {
			dqcRtDaoImpl.excluir(dqcRt);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDQCResponsavelTecnico(DeclaracaoQueimaControladaResponsavelTecnico dqcRt) {
		try {
			dqcRtDaoImpl.salvar(dqcRt);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoQueimaControladaResponsavelTecnico> listarDQCResponsavelTecnicoPorDQC(DeclaracaoQueimaControlada dqc) {
		try {
			
			List<DeclaracaoQueimaControladaResponsavelTecnico> list = dqcRtDaoImpl.listarPorDeclaracaoQueimaControlada(dqc);
			
			for (DeclaracaoQueimaControladaResponsavelTecnico dqcRt : list) {
				dqcRt.setIdePessoaFisica(buscarPessoaFisicaPorCPF(dqcRt.getIdePessoaFisica()));
			}
			
			return list;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/****************
	/*				*
	//XXX: TÉCNICA  *
	/* 				*
	/****************/
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TecnicaQueimaControlada> listarTodosTecnicaQueimaControlada() {
		try {
			return tqcDaoImpl.listarTodos();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDQCTecnicaUtilizada(DeclaracaoQueimaControladaTecnicaUtilizada dqcTqc) {
		try {
			tqcDaoImpl.salvarDQCTecnicaUtilizada(dqcTqc);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoQueimaControladaTecnicaUtilizada> listarDQCTecnicaUtilizadaPorDQC(DeclaracaoQueimaControlada dqc) {
		try {
			return tqcDaoImpl.listarPorDeclaracaoQueimaControlada(dqc);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDQCTecnicaUtilizada(DeclaracaoQueimaControladaTecnicaUtilizada dqcTqc) {
		try {
			tqcDaoImpl.excluirDQCTecnicaUtilizada(dqcTqc);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/****************
	/*				*
	//XXX: OBJETIVO *
	/* 				*
	/****************/
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ObjetivoQueimaControlada> listarTodosObjetivoQueimaControlada() {
		try {
			return oqcDaoImpl.listarTodos();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoQueimaControladaImovelObjetivoQueimaControlada> listarDQCIObjetivoQueimaControladaPorDQCI(DeclaracaoQueimaControladaImovel dqci) {
		try {
			return oqcDaoImpl.listarPorDeclaracaoQueimaControladaImovel(dqci);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDQCIObjetivoQueimaControlada(DeclaracaoQueimaControladaImovelObjetivoQueimaControlada dqcEi) {
		try {
			oqcDaoImpl.salvarDQCIObjetivoQueimaControlada(dqcEi);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDQCIObjetivoQueimaControlada(DeclaracaoQueimaControladaImovelObjetivoQueimaControlada dqcEi) {
		try {
			oqcDaoImpl.excluirDQCIObjetivoQueimaControlada(dqcEi);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/****************************
	/*							*
	//XXX: ELEMENTO INTERVENÇÃO *
	/* 							*
	/****************************/
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ElementoIntervencaoQueimaControlada> listarTodosElementoIntervencaoQueimaControlada() {
		try {
			return eiqcDaoImpl.listarTodos();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoQueimaControladaElementoIntervencao> listarDQCElementoIntervencaoPorDQC(DeclaracaoQueimaControlada dqc) {
		try {
			return eiqcDaoImpl.listarPorDeclaracaoQueimaControlada(dqc);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDQCElementoIntervencao(DeclaracaoQueimaControladaElementoIntervencao dqcEi) {
		try {
			eiqcDaoImpl.salvarDQCElementoIntervencao(dqcEi);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDQCElementoIntervencao(DeclaracaoQueimaControladaElementoIntervencao dqcEi) {
		try {
			eiqcDaoImpl.excluirDQCElementoIntervencao(dqcEi);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/*****************************
	/*							 *
	//XXX: PERGUNTA REQUERIMENTO *
	/* 							 *
	/*****************************/
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PerguntaRequerimento> listarPerguntaRequerimentoRespondidasParaDQC(Integer ideRequerimento) {
		try {
			return prDaoImpl.listarPorRequerimentoECodPerguntaEIndResposta(ideRequerimento, PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_16.getCod(), true);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}