package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.ControleTramitacaoDAOImpl;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ControleTramitacaoService {

	@EJB
	private ControleTramitacaoDAOImpl controleTramitacaoDAOImpl;
	@EJB
	private AlertaService alertaService;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ControleTramitacao controleTramitacao)  {
		controleTramitacaoDAOImpl.salvar(controleTramitacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ControleTramitacao controleTramitacao)  {
		controleTramitacaoDAOImpl.salvarOuAtualizar(controleTramitacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ControleTramitacao tramitacao)  {
		controleTramitacaoDAOImpl.atualizar(tramitacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarUltimoPorProcesso(Processo processo) {
		return controleTramitacaoDAOImpl.buscarUltimoPorProcesso(processo.getIdeProcesso());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarUltimoPorProcessoComTresStatus(Processo processo) {
		return controleTramitacaoDAOImpl.buscarUltimoPorProcessoComTresStatus(processo.getIdeProcesso());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarPenultimoPorProcesso(Processo processo) {
		return controleTramitacaoDAOImpl.buscarPenultimoPorProcesso(processo);
		
	}
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ControleTramitacao> buscarEquipePorProcesso(Processo processo) {
		ControleTramitacao ct = new ControleTramitacao();
		ct.setIndFimDaFila(true);
		ct.setIdeProcesso(processo);
		List<ControleTramitacao>lista = controleTramitacaoDAOImpl.getControlesTramitacao(ct);
		if(!Util.isNullOuVazio(lista)) {
			return lista;
		}
		return null;
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ControleTramitacao> listarControleTramitacaoPorIdeProcesso(Integer ideProcesso) {
		return controleTramitacaoDAOImpl.listarControleTramitacaoPorIdeProcesso(ideProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarUltimaTramitacaoPorProcessoCoordenador(Integer ideProcesso, Integer idePessoaFisica, boolean areaSegundaria) {
		return controleTramitacaoDAOImpl.buscarUltimaTramitacaoPorProcessoCoordenador(ideProcesso, idePessoaFisica, areaSegundaria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarUltimaTramitacaPorProcessoCoordenadorIndAreaSecundariaTrue(Integer ideProcesso, Integer idePessoaFisica) {
		return controleTramitacaoDAOImpl.buscarUltimaTramitacaPorProcessoCoordenadorIndAreaSecundariaTrue(ideProcesso, idePessoaFisica);
	}
	
	public List<ControleTramitacao> listarPorExemplo(ControleTramitacao controle) {
		return controleTramitacaoDAOImpl.getControlesTramitacao(controle);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarLote(List<ControleTramitacao> lista)  {
		controleTramitacaoDAOImpl.salvarEmLote(lista);
		
	}
	
	public void resetarTramitacoes(Processo processo)  {
		resetarTramitacoes(processo.getIdeProcesso());
	}
	
	public void resetarTramitacoes(Integer ideProcesso) {
		controleTramitacaoDAOImpl.resetarTramitacaoPorProcesso(ideProcesso);
	}
	
	public List<ControleTramitacao> retornarFinaisDeFilaPorProcessoPorArea(Processo pProcesso, Area pArea) {
		return controleTramitacaoDAOImpl.retornarFinaisDeFilaPorProcessoPorArea(pProcesso, pArea);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarNovaTramitacao(Integer ideProcesso, Integer ideArea, Integer idePauta, StatusFluxoEnum statusFluxoEnum, Integer idePessoaFisica)  {
		salvarNovaTramitacaoCompleta(ideProcesso, ideArea, idePauta, statusFluxoEnum, idePessoaFisica, false, null);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarNovaTramitacao(Integer ideProcesso, Integer ideArea, Integer idePauta, StatusFluxoEnum statusFluxoEnum, Integer idePessoaFisica, String observacao)  {
		salvarNovaTramitacaoCompleta(ideProcesso, ideArea, idePauta, statusFluxoEnum, idePessoaFisica, false, observacao);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarNovaTramitacaoTecnicoLider(Integer ideProcesso, Integer ideArea, Integer idePauta, StatusFluxoEnum statusFluxoEnum, Integer idePessoaFisica, String observacao)  {
		salvarNovaTramitacaoCompleta(ideProcesso, ideArea, idePauta, statusFluxoEnum, idePessoaFisica, true, observacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarNovaTramitacaoCompleta(Integer ideProcesso, Integer ideArea, Integer idePauta, StatusFluxoEnum statusFluxoEnum, Integer idePessoaFisica, boolean indTecnicoLider, String observacao)  {
		
		controleTramitacaoDAOImpl.resetarTramitacaoPorProcesso(ideProcesso);
		
		ControleTramitacao controleTramitacao = new ControleTramitacao();
		controleTramitacao.setIdeProcesso(new Processo(ideProcesso));
		controleTramitacao.setIdeArea(new Area(ideArea));
		controleTramitacao.setIdePauta(new Pauta(idePauta));
		controleTramitacao.setIdeStatusFluxo(new StatusFluxo(statusFluxoEnum.getStatus()));
		controleTramitacao.setIdePessoaFisica(new PessoaFisica(idePessoaFisica));
		controleTramitacao.setIndResponsavel(indTecnicoLider);
		controleTramitacao.setDscComentarioInterno(observacao);
		controleTramitacao.setDtcTramitacao(new Date());
		controleTramitacao.setIndFimDaFila(true);

		salvar(controleTramitacao);
		
		alertaService.criarAlerta(controleTramitacao);
	}
	
	/**
	 * Método responsável por criar uma nova tramitação para o processo desejado mantendo a area da ultima tramitação.
	 * 
	 * @author Lucas Oliveira Monteiro
	 * @param pProcesso - Processo que a ser tramitado.
	 * @param areaDesitno - Area para onde o processo será destinado.
	 * @param observacao - Texto a ser exibido na tramitação
	 * @param pautaDestino - Pauta que receberá o processo tramitado.
	 * @param indTecnicoLider
	 * @param status - Status que ficará o processo logo após a tramitação.
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarNovaTramitacao(Processo pProcesso, Pauta pautaDestino,String observacao,Boolean indTecnicoLider, StatusFluxo status )  {
		salvarNovaTramitacao(pProcesso, pautaDestino,observacao,indTecnicoLider,status, true);		
	}
	
	/**
	 * Método responsável por criar uma nova tramitação para o processo desejado mantendo a area da ultima tramitação.
	 * 
	 * @author Lucas Oliveira Monteiro
	 * @param pProcesso - Processo que a ser tramitado.
	 * @param areaDesitno - Area para onde o processo será destinado.
	 * @param observacao - Texto a ser exibido na tramitação
	 * @param pautaDestino - Pauta que receberá o processo tramitado.
	 * @param indTecnicoLider
	 * @param status - Status que ficará o processo logo após a tramitação.
	 * @param indFimDaFila - Valor do fim da fila da tramitação.
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarNovaTramitacao(Processo pProcesso, Pauta pautaDestino,String observacao,Boolean indTecnicoLider, StatusFluxo status, boolean indFimDaFila )  {
		
		ControleTramitacao tramitacaoAnterior = buscarUltimoPorProcesso(pProcesso);
		tramitacaoAnterior.setIndFimDaFila(false);
		
		ControleTramitacao tramitacao=new ControleTramitacao();
		
		tramitacao.setIdeProcesso(pProcesso);
		tramitacao.setIdePauta(pautaDestino);
		tramitacao.setIdeArea(tramitacaoAnterior.getIdeArea());
		tramitacao.setIdeStatusFluxo(status);
		tramitacao.setIndResponsavel(indTecnicoLider);
		tramitacao.setDscComentarioInterno(observacao);
		tramitacao.setIndFimDaFila(indFimDaFila);
		tramitacao.setDtcTramitacao(new Date());
		tramitacao.setIdePessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
		//salvando nova tramitacao
		salvar(tramitacao);
		//removendo a tramitacao anterior do fim da fila
		atualizar(tramitacaoAnterior);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarControleTramitacaoAguardandoAprovacaoNotificacaoUltimoPorProcesso(Processo processo) {
		return controleTramitacaoDAOImpl.buscarControleTramitacaoAguardandoAprovacaoNotificacaoUltimoPorProcesso(processo.getIdeProcesso());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarUltimoPorProcessoPautaCoordenador(Processo processo) {
		return controleTramitacaoDAOImpl.buscarUltimoPorProcessoPautaCoordenador(processo.getIdeProcesso());
	}
}