package br.gov.ba.seia.facade;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.ReaberturaProcessoController;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReaberturaProcessoFacade {
	
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	
	@EJB
	private PautaService pautaService;
	
	
	@EJB
	private AreaService areaService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarReabertura(ReaberturaProcessoController rpc) {
		try {
			
			atualizarTramitacaoAnterior(rpc);
			
			salvarTramitacaoProcessoReaberto(rpc);
			
			salvarTramitacaoEncaminhadoParaArea(rpc);
			
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTramitacaoAnterior(ReaberturaProcessoController rpc) {
		ControleTramitacao tramitacaoAnterior = controleTramitacaoService.buscarUltimoPorProcesso(new Processo(rpc.getVwProcesso().getIdeProcesso()));
		tramitacaoAnterior.setIndFimDaFila(false);
		
		controleTramitacaoService.atualizar(tramitacaoAnterior);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTramitacaoAnterior(Integer ideProcesso) {
		ControleTramitacao tramitacaoAnterior = controleTramitacaoService.buscarUltimoPorProcesso(new Processo(ideProcesso));
		tramitacaoAnterior.setIndFimDaFila(false);
		
		controleTramitacaoService.atualizar(tramitacaoAnterior);
	}

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarTramitacaoProcessoReaberto(ReaberturaProcessoController rpc) {
		ControleTramitacao ct = new ControleTramitacao();
		ct.setDtcTramitacao(new Date());
		ct.setIndFimDaFila(false);
		ct.setDscComentarioExterno(rpc.getObservacao());
		ct.setDscComentarioInterno(rpc.getObservacao());
		ct.setIndResponsavel(false);
		ct.setIdeStatusFluxo(new StatusFluxo(StatusFluxoEnum.REABERTO.getStatus()));
		ct.setIdeProcesso(new Processo(rpc.getVwProcesso().getIdeProcesso()));
		ct.setIdePauta(pautaService.obtemPautaArea(rpc.getArea()));
		ct.setIdeArea(rpc.getArea());
		ct.setIdePessoaFisica(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		ct.setIndAreaSecundaria(null);
		
		controleTramitacaoService.salvar(ct);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTramitacaoEncaminhadoParaArea(ReaberturaProcessoController rpc) throws Exception {
		ControleTramitacao ct = new ControleTramitacao();
		ct.setDtcTramitacao(new Date());
		ct.setIndFimDaFila(true);
		ct.setDscComentarioExterno(null);
		ct.setDscComentarioInterno(null);
		ct.setIndResponsavel(false);
		ct.setIdeStatusFluxo(new StatusFluxo(StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus()));
		ct.setIdeProcesso(new Processo(rpc.getVwProcesso().getIdeProcesso()));
		ct.setIdePauta(obtemPautaGestor(rpc));
		ct.setIdeArea(rpc.getArea());
		ct.setIdePessoaFisica(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		ct.setIndAreaSecundaria(null);
		
		controleTramitacaoService.salvar(ct);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarControleTramitacao(Integer ideProcesso,
			Boolean isFimFila,
			String comentarioExterno,
			String comentarioInterno,
			Integer ideStatusFluxo,
			Integer	ideArea) {
		ControleTramitacao ct = new ControleTramitacao();
		ct.setDtcTramitacao(new Date());
		ct.setIndFimDaFila(isFimFila);
		ct.setDscComentarioExterno(comentarioExterno);
		ct.setDscComentarioInterno(comentarioInterno);
		ct.setIndResponsavel(false);
		ct.setIdeStatusFluxo(new StatusFluxo(ideStatusFluxo));
		ct.setIdeProcesso(new Processo(ideProcesso));
		ct.setIdePauta(pautaService.obtemPautaArea(ideArea));
		ct.setIdeArea(areaService.buscarPorID(ideArea));
		ct.setIdePessoaFisica(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		ct.setIndAreaSecundaria(null);
		
		controleTramitacaoService.salvar(ct);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarControleTramitacaoPautaGestor(Integer ideProcesso,
			Boolean isFimFila,
			String comentarioExterno,
			String comentarioInterno,
			Integer ideStatusFluxo,
			Integer	ideArea) {
		ControleTramitacao ct = new ControleTramitacao();
		ct.setDtcTramitacao(new Date());
		ct.setIndFimDaFila(isFimFila);
		ct.setDscComentarioExterno(comentarioExterno);
		ct.setDscComentarioInterno(comentarioInterno);
		ct.setIndResponsavel(false);
		ct.setIdeStatusFluxo(new StatusFluxo(ideStatusFluxo));
		ct.setIdeProcesso(new Processo(ideProcesso));		
		ct.setIdePauta(pautaService.obtemPautaAreaCoordenador(ideArea));
		ct.setIdeArea(areaService.buscarPorID(ideArea));
		ct.setIdePessoaFisica(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		ct.setIndAreaSecundaria(null);
		
		controleTramitacaoService.salvar(ct);
	} 
	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarControleTramitacaoPautaDiretorArea(Integer ideProcesso,
			Boolean isFimFila,
			String comentarioExterno,
			String comentarioInterno,
			Integer ideStatusFluxo,
			Integer	ideArea) throws Exception {
		ControleTramitacao ct = new ControleTramitacao();
		ct.setDtcTramitacao(new Date());
		ct.setIndFimDaFila(isFimFila);
		ct.setDscComentarioExterno(comentarioExterno);
		ct.setDscComentarioInterno(comentarioInterno);
		ct.setIndResponsavel(false);
		ct.setIdeStatusFluxo(new StatusFluxo(ideStatusFluxo));
		ct.setIdeProcesso(new Processo(ideProcesso));
		ct.setIdePauta(pautaService.obtemPautaCoordenadorArea(ideArea,TipoPautaEnum.PAUTA_DIRETOR_AREA));
		ct.setIdeArea(areaService.buscarPorID(ideArea));
		ct.setIdePessoaFisica(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		ct.setIndAreaSecundaria(null);
		
		controleTramitacaoService.salvar(ct);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Pauta obtemPautaGestor(ReaberturaProcessoController rpc) throws Exception {
	
		Pauta pautaGestor = pautaService.obtemPautaCoordenadorArea(rpc.getArea().getIdeArea(), TipoPautaEnum.PAUTA_COORDENADOR_AREA);
		
		if(Util.isNullOuVazio(pautaGestor)) {
			pautaGestor = pautaService.obtemPautaCoordenadorArea(rpc.getArea().getIdeArea(), TipoPautaEnum.PAUTA_DIRETOR_AREA);
		}
		
		return pautaGestor;
	}
	
	
}