/**
 * 
 */
package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.FceBarragemController;
import br.gov.ba.seia.dto.DocumentoObrigatorioEnquadramentoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceBarragLicencLocaGeo;
import br.gov.ba.seia.entity.FceBarragLicencLocaGeoPK;
import br.gov.ba.seia.entity.FceBarragem;
import br.gov.ba.seia.entity.FceBarragemLicenciamento;
import br.gov.ba.seia.entity.FceIntervencaoBarragem;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MaterialUtilizado;
import br.gov.ba.seia.entity.ObrasInfraComplementares;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.UsoReservatorio;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.service.BiomaService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.FceBarragLicencLocaGeoService;
import br.gov.ba.seia.service.FceBarragemLicenciamentoService;
import br.gov.ba.seia.service.FceBarragemService;
import br.gov.ba.seia.service.FceIntervencaoBarragemService;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.MaterialUtilizadoService;
import br.gov.ba.seia.service.ObrasInfraComplementaresService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.service.UsoReservatorioService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceBarragemFacade {
	
	@EJB
	private UsoReservatorioService usoReservatorioService;
	
	@EJB
	private MaterialUtilizadoService materialUtilizadoService;
	
	@EJB
	private ObrasInfraComplementaresService  obrasInfraComplementaresService;
	
	@EJB
	private FceBarragemService fcebarragemService;
	
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	
	@EJB
	private FceBarragemLicenciamentoService barragemLicenciamentoService;
	
	@EJB
	private BiomaService biomaService;
	
	@EJB
	private FceBarragLicencLocaGeoService barragLicencLocaGeoService; 
	
	@EJB
	private FceIntervencaoBarragemService fceIntervencaoBarragemService;
	
	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	
	@EJB
	private PerguntaService perguntaService;
	
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	
	@EJB
	private FceBarragLicencLocaGeoService fceBarragLicencLocaGeoService;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@EJB
	private FceOutorgaLocalizacaoGeograficaService fceOutorgaLocalizacaoGeograficaService;
	
	@EJB
	private FceBarragemLicenciamentoService fceBarragemLicenciamentoService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UsoReservatorio> listarUsoReservatorios() throws Exception {
		return usoReservatorioService.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MaterialUtilizado> listarMateriaislUtilizados() throws Exception {
		return materialUtilizadoService.listar();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ObrasInfraComplementares> listarObrasInfraComplementares() throws Exception {
		return obrasInfraComplementaresService.listar();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceBarragem(FceBarragem fceBarragem) throws Exception {
		fcebarragemService.salvarOuAtualizar(fceBarragem);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirFceBarragLicencLocaGeo(FceBarragemLicenciamento fceBarragemLicenciamento) throws Exception{
		List<FceBarragLicencLocaGeo> dataDBO = barragLicencLocaGeoService.listarByFceBarragemLicenciamento(fceBarragemLicenciamento);
		for(FceBarragLicencLocaGeo item : dataDBO){
			if(!fceBarragemLicenciamento.getBarragemLicenciamentoLocalizacaoGeo().contains(item)){
				barragLicencLocaGeoService.excluirFceBarragLicencLocaGeo(item);
			}
		}
	}
	
	private void buildFceBarragLicencLocaGeoPK(FceBarragemLicenciamento barragemLicenciamento, boolean analiseTecnica){
		if(!Util.isNullOuVazio(barragemLicenciamento) && !Util.isNullOuVazio(barragemLicenciamento.getBarragemLicenciamentoLocalizacaoGeo())){
			for (FceBarragLicencLocaGeo l : barragemLicenciamento.getBarragemLicenciamentoLocalizacaoGeo()) {
				if(Util.isNullOuVazio(l.getFceBarragLicencLocaGeoPK()) || analiseTecnica){
					l.setFceBarragLicencLocaGeoPK(new FceBarragLicencLocaGeoPK(barragemLicenciamento.getIdeFceBarragemLicenciamento(), l.getIdeLocalGeoInicioEixo().getIdeLocalizacaoGeografica(),
							l.getIdeLocalGeoFimEixo().getIdeLocalizacaoGeografica()));
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceBarragem buscarFceBarragem(Fce fce) throws Exception{
		return fcebarragemService.buscarFcebarragem(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double getAreaDoShape(LocalizacaoGeografica localizacaoGeografica) throws Exception{
		if(!Util.isNullOuVazio(localizacaoGeografica) && !Util.isNullOuVazio(localizacaoGeografica.getIdeClassificacaoSecao())){
			String theGeom = validacaoGeoSeiaService.buscarGeometriaShape(localizacaoGeografica.getIdeLocalizacaoGeografica());
			if(theGeom != null){
				return validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom);
			}
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getBioma(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		return biomaService.getBiomaByLocalizacaoGeo(localizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceBarragemLicenciamento(FceBarragemLicenciamento fceBarragemLicenciamento) throws Exception {
		barragemLicenciamentoService.salvarOuAtualizar(fceBarragemLicenciamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceBarragLicencLocaGeo(FceBarragemLicenciamento fceBarragemLicenciamento, boolean analiseTecnica) throws Exception{
		if (!Util.isNullOuVazio(fceBarragemLicenciamento.getBarragemLicenciamentoLocalizacaoGeo())){
			buildFceBarragLicencLocaGeoPK(fceBarragemLicenciamento, analiseTecnica);
			fceBarragLicencLocaGeoService.salvarEmLote(fceBarragemLicenciamento.getBarragemLicenciamentoLocalizacaoGeo());
		}
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceBarragemController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceBarragemIntervencaoLista(List<FceIntervencaoBarragem> lista) throws Exception {
		fceIntervencaoBarragemService.salvarEmLote(lista);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSemFlush(FceIntervencaoBarragem fceIntervencaoBarragem) throws Exception {
		fceIntervencaoBarragemService.salvarSemFlush(fceIntervencaoBarragem);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceBarragemIntervencaoLista(FceIntervencaoBarragem fceIntervencaoBarragem) throws Exception {
		fceIntervencaoBarragemService.salvarSemFlush(fceIntervencaoBarragem);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceBarragemIntervencao(FceIntervencaoBarragem fceIntervencaoBarragem) throws Exception {
		fceIntervencaoBarragemService.salvarFceIntervencaoBarragem(fceIntervencaoBarragem);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaByModalidadeOutorga(Requerimento requerimento) {
		try {
			return outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByModalidadeOutorgaRequerimento(ModalidadeOutorgaEnum.INTERVENCAO, requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaByModalidadeOutorgaTecnico(Requerimento requerimento, ModalidadeOutorgaEnum modalidadeOutorgaEnum, DadoOrigemEnum dadoOrigem) {
		try {
			return outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByModalidadeOutorgaRequerimentoTecnico(modalidadeOutorgaEnum, requerimento, dadoOrigem);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean disableAbaFceBarragem(ModalidadeOutorgaEnum intervencao, Requerimento requerimento){
		List<OutorgaLocalizacaoGeografica> outorgaLocalizacaoGeografica = null;
		try {
			outorgaLocalizacaoGeografica = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByModalidadeOutorga(intervencao, requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return Util.isNullOuVazio(outorgaLocalizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean disableAbaFceBarragemLicenciamento(Requerimento requerimento){
		try {
			if (!Util.isNullOuVazio(requerimento)){
				/*
				Pergunta pergunta = this.perguntaService.carregarPerguntabyCodPergunta("NR_A3_P1");
				PerguntaRequerimento perguntaRequerimento = new PerguntaRequerimento(pergunta);
				return !perguntaRequerimento.getIndResposta();
				*/
				Boolean retorno = false;
				List<String> lista = new ArrayList<String>();
				lista.add("NR_A3_P1");
				lista.add("NR_A2_P1");
				List<PerguntaRequerimento> perguntas = perguntaRequerimentoService.listarPerguntaRequerimentoPor(requerimento, lista);
				
				if (!Util.isNullOuVazio(perguntas)){
					for (PerguntaRequerimento perguntaRequerimento : perguntas) {
						retorno = perguntaRequerimento.getIndResposta();
						
						if(!Util.isNullOuVazio(retorno) && retorno) {
							return !retorno;
						}
					}
					
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return true;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceIntervencaoBarragem obterIntervencaoBarragem(Fce fce, FceBarragem fceBarragem, OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) throws Exception {
		FceIntervencaoBarragem fceIntervencaoBarragem = fceIntervencaoBarragemService.buscarFceIntervencaoBarragemByIdeOutorgaLocalizacaoGeograficaFceBarragem(outorgaLocalizacaoGeografica);
		if (!Util.isNullOuVazio(fceIntervencaoBarragem)){
			return fceIntervencaoBarragem;
		}
		return new FceIntervencaoBarragem(fce, fceBarragem, outorgaLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarLP(Requerimento requerimento, boolean isValidacao) throws Exception {
		List<DocumentoObrigatorioEnquadramentoDTO> listaDocumentoObrigatorioEnquadramentoDTO = documentoObrigatorioService.listaDocumentoObrigatorioEnquadramentoDTO(requerimento.getIdeRequerimento(), isValidacao);
		
		for (DocumentoObrigatorioEnquadramentoDTO documentoObrigatorioEnquadramentoDTO : listaDocumentoObrigatorioEnquadramentoDTO) {
			AtoAmbiental atoAmbiental = documentoObrigatorioEnquadramentoDTO.getEnquadramentoAtoAmbiental().getAtoAmbiental();
			if (AtoAmbientalEnum.LP.getId().toString().equals(atoAmbiental.getIdeAtoAmbiental().toString())){
				return true;
			}
		}
		
		return false;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarSeOpcaoOutros(List<UsoReservatorio> usosReservatorio, FceBarragemLicenciamento fceBarragemLicenciamento) throws Exception {
		if (!Util.isNullOuVazio(usosReservatorio)){
			for (UsoReservatorio usoReservatorio : usosReservatorio) {
				if (usoReservatorio.getUsoReservatorio().equals("Outros")){
					return true;
				}
			}
		}
		if (!Util.isNullOuVazio(fceBarragemLicenciamento)){
			for (MaterialUtilizado materialUtilizado : fceBarragemLicenciamento.getMaterialUtilizados()) {
				if (materialUtilizado.getMaterialUtilizado().equals("Outros")){
					return true;
				}
			}
			
			for (ObrasInfraComplementares itemFce : fceBarragemLicenciamento.getObrasInfraComplementares()){
				if(itemFce.getIdeObrasInfraComplementares().toString().equals("4")){
					return true;
				}
			}
		}
		
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarOutorgaLocalizacaoGeografica(List<FceIntervencaoBarragem> listaFceIntervencaoBarragem, Fce fce) throws Exception {
		
		for (FceIntervencaoBarragem fceIntervencaoBarragem : listaFceIntervencaoBarragem) {
			OutorgaLocalizacaoGeografica outorgaLocGeoOriginal = fceIntervencaoBarragem.getIdeOutorgaLocalizacaoGeografica();
			outorgaLocalizacaoGeograficaService.salvarAtualizar(outorgaLocGeoOriginal);
			
			fceIntervencaoBarragem.setIdeOutorgaLocalizacaoGeografica(outorgaLocGeoOriginal);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void duplicarFce(FceBarragemController ctrl) throws Exception {
		ctrl.salvarFce();
		ctrl.salvarFcebarragem();
		
		FceBarragemLicenciamento fceBarragemLicenciamento = ctrl.getFceBarragem().getFceBarragemLicenciamento();
		List<FceIntervencaoBarragem> listaFceIntervencaoBarragem = ctrl.getFceBarragem().getFceIntervencaoBarragems();
		ctrl.getFceBarragem().setFceBarragemLicenciamento(null);
		
		if (!Util.isNullOuVazio(fceBarragemLicenciamento)){
			fceBarragemLicenciamento.setFceBarragem(ctrl.getFceBarragem());
			salvarFceBarragemLicenciamento(fceBarragemLicenciamento);
			salvarFceBarragLicencLocaGeo(fceBarragemLicenciamento, true);
		}
		
		if (!Util.isNullOuVazio(listaFceIntervencaoBarragem)){
			salvarOutorgaLocalizacaoGeografica(listaFceIntervencaoBarragem, ctrl.getFce());
			ctrl.salvarFceIntervencaoBarragem();
		}
		
		if (!Util.isNullOuVazio(ctrl.getListaOutorgaConcedida())){
			ctrl.salvarListaOutorgaConcedida(ctrl.getListaOutorgaConcedida());
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceBarragLicencLocaGeo(FceBarragLicencLocaGeo fceBarragLicencLocaGeo) throws Exception {
		if (!Util.isNullOuVazio(fceBarragLicencLocaGeo.getFceBarragLicencLocaGeoPK())){
			fceBarragLicencLocaGeoService.excluirFceBarragLicencLocaGeo(fceBarragLicencLocaGeo);
		}
		
		removerLocalizacaoGeografica(fceBarragLicencLocaGeo.getIdeLocalGeoInicioEixo());
		removerLocalizacaoGeografica(fceBarragLicencLocaGeo.getIdeLocalGeoFimEixo());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		localizacaoGeograficaService.excluirDadosPersistidos(localizacaoGeografica);
	}

}
