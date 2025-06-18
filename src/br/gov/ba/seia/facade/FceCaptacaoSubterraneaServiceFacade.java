package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.FceCaptacaoSubterraneaController;
import br.gov.ba.seia.entity.Aquifero;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceCaptacaoSubterranea;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoAquifero;
import br.gov.ba.seia.entity.TipoPoco;
import br.gov.ba.seia.entity.UnidadeGeologicaAflorante;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.service.AquiferoService;
import br.gov.ba.seia.service.FceCaptacaoSubterraneaService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.TipoAquiferoService;
import br.gov.ba.seia.service.TipoPocoService;
import br.gov.ba.seia.service.UnidadeGeologicaAfloranteService;

/**
 *
 * @author eduardo.fernandes
 * @since 05/01/2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCaptacaoSubterraneaServiceFacade {
	
	@EJB
	private FceCaptacaoSubterraneaService fceCaptacaoSubterraneaService;
	@EJB
	private TipoPocoService tipoPocoService;
	@EJB
	private TipoAquiferoService tipoAquiferoService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	@EJB
	private UnidadeGeologicaAfloranteService unidadeGeologiacaAfloranteService;
	@EJB
	private AquiferoService aquiferoService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Aquifero> listarAquiferos() throws Exception{
		return aquiferoService.listarAquifero();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAquifero> listarTipoAquifero() throws Exception{
		return tipoAquiferoService.listarTipoAquifero();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeGeologicaAflorante> listarUnidadeAflorante() throws Exception{
		return unidadeGeologiacaAfloranteService.listarUnidadeGeologicaAflorante();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceCaptacaoSubterranea> listarFceCaptacaoSubterranea(Fce fce) throws Exception{
		return fceCaptacaoSubterraneaService.listarFceCaptacaoSubterraneaByIdeFce(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoPoco> listarTipoPoco() throws Exception{
		return tipoPocoService.listarTipoPoco();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PerguntaRequerimento buscarPerguntaRequerimento(Requerimento requerimento, FceCaptacaoSubterranea fceCaptacaoSubterranea) throws Exception{
		return perguntaRequerimentoService.consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimentoAndIdeOutorgaLocGeo(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA4_D1_12.getCod(), requerimento.getIdeRequerimento(), fceCaptacaoSubterranea.getIdeOutorgaLocalizacaoGeografica().getIdeOutorgaLocalizacaoGeografica());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceCaptacaoSubterranea(FceCaptacaoSubterranea fceCaptacaoSubterranea) throws Exception{
		fceCaptacaoSubterraneaService.salvarCaptacaoSubteranea(fceCaptacaoSubterranea);
		fceCaptacaoSubterranea.setConfirmado(true);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceCaptacaoSubterraneaController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
}