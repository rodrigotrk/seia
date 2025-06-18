package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.FceCaptacaoSuperficialController;
import br.gov.ba.seia.entity.CaracteristicaCaptacao;
import br.gov.ba.seia.entity.CaracteristicaSistemaCaptacao;
import br.gov.ba.seia.entity.DominioBarramento;
import br.gov.ba.seia.entity.FceCaptacaoSuperficial;
import br.gov.ba.seia.entity.FceIntervencaoBarragem;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalCaptacao;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoValor;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.service.CaracteristicaCaptacaoService;
import br.gov.ba.seia.service.CaracteristicaSistemaCaptacaoService;
import br.gov.ba.seia.service.DominioBarramentoService;
import br.gov.ba.seia.service.FceCaptacaoSuperficialService;
import br.gov.ba.seia.service.FceIntervencaoBarragemService;
import br.gov.ba.seia.service.LocalCaptacaoService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.TipoValorService;

/**
 *
 * @author eduardo.fernandes
 * @since 05/01/2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCaptacaoSuperficialServiceFacade {
	
	@EJB
	private CaracteristicaCaptacaoService caracteristicaCaptacaoService;
	@EJB
	private DominioBarramentoService dominioBarramentoService;
	@EJB
	private CaracteristicaSistemaCaptacaoService caracteristicaSistemaCaptacaoService;
	@EJB
	private LocalCaptacaoService localCaptacaoService;
	@EJB
	private TipoValorService tipoValorService;
	@EJB
	private FceCaptacaoSuperficialService fceCapSuperService;
	@EJB
	private FceIntervencaoBarragemService intervencaoBarragemService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaCaptacao> listarCaracteristicaCaptacao() throws Exception{
		return caracteristicaCaptacaoService.buscarListaCaracteristicaCaptacao();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaSistemaCaptacao> listarCaracteristicaSistemaCaptacao() throws Exception{
		return caracteristicaSistemaCaptacaoService.buscarListaCaracteristicaSistemaCaptacao();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DominioBarramento> listarDominioBarramento() throws Exception{
		return dominioBarramentoService.buscarListaDominioBarramento();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoValor> listarTipoValor() throws Exception{
		return tipoValorService.buscarListaTipoValor();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LocalCaptacao> listarLocalCaptacao() throws Exception{
		return localCaptacaoService.buscarListaLocalCaptacao();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceCaptacaoSuperficial buscarFceCaptacaoSuperficialBy(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) throws Exception{
		return fceCapSuperService.buscarFceCaptacaoSuperficialByIdeFceOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceIntervencaoBarragem buscarFceIntervencaoBarragem(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaSelecionada) throws Exception{
		return intervencaoBarragemService.buscarFceIntervencaoBarragemByIdeOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeograficaSelecionada.getIdeOutorgaLocalizacaoGeografica());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PerguntaRequerimento buscarPerguntaRequerimento(Requerimento requerimento, OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) throws Exception{
		return perguntaRequerimentoService.consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimentoAndIdeOutorgaLocGeo(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA4_D2_121.getCod(), requerimento.getIdeRequerimento(), outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceCaptacaoSuperficial(FceCaptacaoSuperficial fceCaptacaoSuperficial) throws Exception{
		fceCapSuperService.salvarFceCaptacaoSuperficial(fceCaptacaoSuperficial);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceCaptacaoSuperficialController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
}