package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BiomaRequerimentoDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Bioma;
import br.gov.ba.seia.entity.BiomaEnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.BiomaEnquadramentoAtoAmbientalPK;
import br.gov.ba.seia.entity.BiomaRequerimento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.BiomaEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BiomaRequerimentoService {
	
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private BiomaRequerimentoDAOImpl biomaRequerimentoDAOImpl;
	@EJB
	private EnquadramentoService enquadramentoService;
	@EJB
	private FlorestalService florestalService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	
	@Inject
	private IDAO<BiomaEnquadramentoAtoAmbiental> biomaEnquadramentoAtoAmbientalDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarListaBiomaRequerimento(Requerimento req, boolean atualizarBiomaRequerimentoEtapa7) throws Exception {
		Collection<BiomaRequerimento> listaBiomaRequerimento = biomaRequerimentoDAOImpl.listarPor(req.getIdeRequerimento());
		
		if(isNaoExisteBiomaRequerimento(listaBiomaRequerimento)) {
			PerguntaRequerimento perguntaRequerimentoAba51 = perguntaRequerimentoService.consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimento(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_1.getCod(),
					req.getIdeRequerimento());

			if(perguntaRequerimentoAba51 != null && perguntaRequerimentoAba51.getIndResposta()){
				gerarBiomaRequerimentoPeloEmpreendimento(req,listaBiomaRequerimento);
				gerarBiomaRequerimentoPelaAbaFlorestal(req, listaBiomaRequerimento);
				gravarBiomaRequerimento(listaBiomaRequerimento);
			}
			if(atualizarBiomaRequerimentoEtapa7){
				atualizarBiomaRequerimentoGeradoNaEtapa7(req, listaBiomaRequerimento);
			}
		}
		else if(isExisteBiomaRequerimentoGeradoNaEtapa7(listaBiomaRequerimento)) {
			atualizarBiomaRequerimentoGeradoNaEtapa7(req, listaBiomaRequerimento);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Boolean isExisteBiomaRequerimentoGeradoNaEtapa7(Collection<BiomaRequerimento> listaBiomaRequerimento) {
		return Util.isNull(listaBiomaRequerimento.iterator().next().getBiomaEnquadramentoAtoAmbiental());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isNaoExisteBiomaRequerimento(Collection<BiomaRequerimento> listaBiomaRequerimento) {
		return Util.isNullOuVazio(listaBiomaRequerimento);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isExisteBiomaRequerimento(Collection<BiomaRequerimento> listaBiomaRequerimento) {
		return !Util.isNullOuVazio(listaBiomaRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarBiomaRequerimentoGeradoNaEtapa7(Requerimento req, Collection<BiomaRequerimento> listaBiomaRequerimento)  {
		ArrayList<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbientalEspecial = listarEnquadramentoAtoAmbientalEspecial(req);
		
		if(isRequerimentoEnquadradoEmAtoEspecial(listaEnquadramentoAtoAmbientalEspecial)) {
			LocalizacaoGeografica loc = localizacaoGeograficaService.buscarLocalizacaoGeograficaEmpreendimento(req.getIdeRequerimento());
			atualizarBiomaRequerimentoPorAto(listaBiomaRequerimento, listaEnquadramentoAtoAmbientalEspecial, loc);
		}
		
		Collection<Florestal> listaFlorestal = florestalService.carregarListaFlorestal(req);
		Collection<PerguntaRequerimento> listarPerguntaRequerimento = perguntaRequerimentoService.listarPerguntaRequerimentoRespondidaAbaFlorestalComLocalizacaoGeografica(req);
		
		for(BiomaRequerimento br : listaBiomaRequerimento) {
			if(br.getValArea().equals(0.0)) {
				for(Florestal f :listaFlorestal) {
					for(PerguntaRequerimento pr :listarPerguntaRequerimento) {
						
						if(pr.getIdePergunta().getCodPergunta().equals(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_15.getCod()) 
							&& f.getIdeImovel().equals(pr.getIdeImovel()) && br.getIdeLocalizacaoGeografica().equals(pr.getIdeLocalizacaoGeografica())) {
							
							br.setValArea(f.getNumAreaSuprimida().doubleValue());
						}
					}
				}
			}
		}
		
		gravarBiomaRequerimento(listaBiomaRequerimento);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void atualizarBiomaRequerimentoPorAto(Collection<BiomaRequerimento> listaBiomaRequerimento, ArrayList<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbientalEspecial, LocalizacaoGeografica loc) {
		try{
			Collection<BiomaRequerimento> listaBiomaRequerimentoEspecial = new ArrayList<BiomaRequerimento>(); 
			for(BiomaRequerimento br : listaBiomaRequerimento) {
				if(loc.equals(br.getIdeLocalizacaoGeografica())) {
					for(int i=0; i < listaEnquadramentoAtoAmbientalEspecial.size(); i++) {
						
						BiomaRequerimento biomaRequerimento;
						EnquadramentoAtoAmbiental enquadramentoAtoAmbiental = listaEnquadramentoAtoAmbientalEspecial.get(i);
						if(i==0) {
							biomaRequerimento = br;
						}
						else {
							biomaRequerimento = br.clone();
							biomaRequerimento.setIdeBiomaRequerimento(null);
						}
						
						carregarAreaVistoriaParaAtosEspecificos(biomaRequerimento,enquadramentoAtoAmbiental);
						BiomaEnquadramentoAtoAmbiental biomaEnquadramentoAtoAmbiental = new BiomaEnquadramentoAtoAmbiental();
						biomaEnquadramentoAtoAmbiental.setIdeBiomaRequerimento(biomaRequerimento);
						biomaEnquadramentoAtoAmbiental.setIdeEnquadramentoAtoAmbiental(enquadramentoAtoAmbiental);
						br.setBiomaEnquadramentoAtoAmbiental(biomaEnquadramentoAtoAmbiental);
						
						listaBiomaRequerimentoEspecial.add(biomaRequerimento);
					}
				}
			}
			for(BiomaRequerimento br : listaBiomaRequerimentoEspecial) {
				if(!Util.isNull(br.getIdeBiomaRequerimento()) && listaBiomaRequerimentoEspecial.contains(br)) {
					listaBiomaRequerimento.remove(br);
				}
				listaBiomaRequerimento.add(br);
			}			
		}
		catch(Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isRequerimentoEnquadradoEmAtoEspecial(ArrayList<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbientalEspecial) {
		return !Util.isNullOuVazio(listaEnquadramentoAtoAmbientalEspecial);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private ArrayList<EnquadramentoAtoAmbiental> listarEnquadramentoAtoAmbientalEspecial(Requerimento req) {
		try{
			Collection<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbiental = enquadramentoService.listarEnquadramentoAtoByRequerimento(req.getIdeRequerimento());
			ArrayList<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbientalEspecial = new ArrayList<EnquadramentoAtoAmbiental>();
			for(EnquadramentoAtoAmbiental eaa : listaEnquadramentoAtoAmbiental) {
				if(atosEspeciais().contains(eaa.getAtoAmbiental())) {
					listaEnquadramentoAtoAmbientalEspecial.add(eaa);
				}
			}
			return listaEnquadramentoAtoAmbientalEspecial;
		}
		catch(Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarListaBiomaRequerimentoPelaEtapa7(Requerimento req) throws Exception {
		Collection<BiomaRequerimento> listaBiomaRequerimento = new ArrayList<BiomaRequerimento>();
		
		biomaRequerimentoDAOImpl.removerPor(req.getIdeRequerimento());
		
		PerguntaRequerimento perguntaRequerimentoAba51 = perguntaRequerimentoService.consultarPerguntaRequerimentoByCodPerguntaAndIdeRequerimento(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_1.getCod(),
				req.getIdeRequerimento());

		if(perguntaRequerimentoAba51 != null && perguntaRequerimentoAba51.getIndResposta()){
			gerarBiomaRequerimentoPeloEmpreendimento(req,listaBiomaRequerimento);
			gerarBiomaRequerimentoPelaAbaFlorestal(req, listaBiomaRequerimento);
			gravarBiomaRequerimento(listaBiomaRequerimento);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gravarBiomaRequerimento(Collection<BiomaRequerimento> listaBiomaRequerimento) {
		try{
			for(BiomaRequerimento br : listaBiomaRequerimento) {
				
				BiomaEnquadramentoAtoAmbiental beaa = br.getBiomaEnquadramentoAtoAmbiental();
				br.setBiomaEnquadramentoAtoAmbiental(null);
				
				if(Util.isNull(br.getIdeBiomaRequerimento())) {
					biomaRequerimentoDAOImpl.salvar(br);
				} else if (!Util.isNullOuVazio(br.getBiomaEnquadramentoAtoAmbiental())){
					biomaRequerimentoDAOImpl.atualizar(br);
				}
				
				if(beaa != null) {
					beaa = beaa.clone();
					
					beaa.setIdeBiomaRequerimento(br);
					
					beaa.setBiomaEnquadramentoAtoAmbientalPK(
							new BiomaEnquadramentoAtoAmbientalPK(
									beaa.getIdeBiomaRequerimento().getIdeBiomaRequerimento(), 
									beaa.getIdeEnquadramentoAtoAmbiental().getIdeEnquadramentoAtoAmbiental()
					));
					
					biomaEnquadramentoAtoAmbientalDAO.salvarOuAtualizar(beaa);
				}
			}
		} catch(Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void gerarBiomaRequerimentoPeloEmpreendimento(Requerimento req,	Collection<BiomaRequerimento> listaBiomaRequerimento) {
		boolean pergunta110 = false;
		boolean pergunta116 = false;
		Collection<PerguntaRequerimento> listPR = perguntaRequerimentoService.listarPerguntaRequerimentoRespondidaAbaFlorestal(req);
		
		if(!Util.isNullOuVazio(listPR)) {
			for (PerguntaRequerimento pr : listPR) {
				if(pr.getIdePergunta().getCodPergunta().equals(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p10.getCod())) pergunta110 = true; 
				if(pr.getIdePergunta().getCodPergunta().equals(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p16.getCod())) pergunta116 = true; 
			}
		}
		
		if(pergunta110 && pergunta116) {
			LocalizacaoGeografica localizacaoGeografica = localizacaoGeograficaService.buscarLocalizacaoGeograficaEmpreendimento(req.getIdeRequerimento());
			
			if(localizacaoGeografica != null) {
				Collection<BiomaRequerimento> listaBiomaRequerimentoGerada = 
						biomaRequerimentoDAOImpl.gerarListaBiomaRequerimentoParaPonto(req.getIdeRequerimento(), localizacaoGeografica);
				
				if(!isNaoExisteBiomaRequerimento(listaBiomaRequerimentoGerada)) {
					listaBiomaRequerimento.addAll(listaBiomaRequerimentoGerada);
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void gerarBiomaRequerimentoPeloEmpreendimento(Requerimento req,	Collection<BiomaRequerimento> listaBiomaRequerimento, Collection<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbiental)  {
		Collection<AtoAmbiental> listaAtosEspeciais = atosEspeciais();
		for(EnquadramentoAtoAmbiental eaa : listaEnquadramentoAtoAmbiental) {
			if(listaAtosEspeciais.contains(eaa.getAtoAmbiental())) {
				LocalizacaoGeografica loc = localizacaoGeograficaService.buscarLocalizacaoGeograficaEmpreendimento(req.getIdeRequerimento());
				Collection<BiomaRequerimento> listaBiomaRequerimentoGerada = biomaRequerimentoDAOImpl.gerarListaBiomaRequerimentoParaPonto(req.getIdeRequerimento(), loc);
				for(BiomaRequerimento br : listaBiomaRequerimentoGerada) {
					carregarAreaVistoriaParaAtosEspecificos(br,eaa);
					BiomaEnquadramentoAtoAmbiental beaa = new BiomaEnquadramentoAtoAmbiental();
					beaa.setIdeBiomaRequerimento(br);
					beaa.setIdeEnquadramentoAtoAmbiental(eaa);
					br.setBiomaEnquadramentoAtoAmbiental(beaa);
				}
				listaBiomaRequerimento.addAll(listaBiomaRequerimentoGerada);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarAreaVistoriaParaAtosEspecificos(BiomaRequerimento biomaRequerimento, EnquadramentoAtoAmbiental eaa) {
		try{
			Requerimento req = biomaRequerimento.getIdeRequerimento();
			List<Florestal> listaFlorestal = florestalService.carregarListaFlorestal(req);
			if (eaa.getAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.ARL.getId()))) {
				BigDecimal somaArea = BigDecimal.ZERO;
				for(Florestal florestal : listaFlorestal) {
					if(!Util.isNull(florestal.getNumAreaReservaLegal())) {
						somaArea = somaArea.add(florestal.getNumAreaReservaLegal());
					}
				}
				biomaRequerimento.setValArea(somaArea.doubleValue());
			}
			else if(eaa.getAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.ARRL.getId()))) {
				BigDecimal somaArea = BigDecimal.ZERO;
				for(Florestal florestal : listaFlorestal) {
					if(!Util.isNull(florestal.getNumAreaProcessoAprovacaoReservaLegal())) {
						somaArea = somaArea.add(florestal.getNumAreaProcessoAprovacaoReservaLegal());
					}
				}
				biomaRequerimento.setValArea(somaArea.doubleValue());
			}
			else if(eaa.getAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.ANUENCIA.getId()))
					|| eaa.getAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.RVFR.getId()))) {
				biomaRequerimento.setValArea( 0.0);
			}
		}
		catch(Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<AtoAmbiental> atosEspeciais() {
		Collection<AtoAmbiental> listaAtos = Arrays.asList(new AtoAmbiental[] {
				new AtoAmbiental(AtoAmbientalEnum.ARL.getId()),
				new AtoAmbiental(AtoAmbientalEnum.ARRL.getId()),
				new AtoAmbiental(AtoAmbientalEnum.ANUENCIA.getId()),
				new AtoAmbiental(AtoAmbientalEnum.RVFR.getId())
		});
		return listaAtos;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void gerarBiomaRequerimentoPelaAbaFlorestal(Requerimento req, Collection<BiomaRequerimento> listaBiomaRequerimento) throws Exception {
		
		Collection<PerguntaRequerimento> listaPerguntaRequerimento = perguntaRequerimentoService.listarPerguntaRequerimentoRespondidaAbaFlorestalComLocalizacaoGeografica(req);
		for(PerguntaRequerimento pr : listaPerguntaRequerimento) {
			LocalizacaoGeografica loc = pr.getIdeLocalizacaoGeografica();
			Boolean incluirNoCalculoVistoria = biomaRequerimentoDAOImpl.incluirNoCalculoVistoria(req, loc);
			
			for(AtoAmbiental ato : req.getAtosAmbientais()) {
				if(incluirNoCalculoVistoria && !ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.DQC.getId())) {
					Collection<BiomaRequerimento> listaBiomaRequerimentoGerada = biomaRequerimentoDAOImpl.gerarListaBiomaRequerimento(req.getIdeRequerimento(), loc.getIdeLocalizacaoGeografica());
					if(isExisteBiomaRequerimento(listaBiomaRequerimentoGerada)) {
						for(BiomaRequerimento biomaReq:listaBiomaRequerimentoGerada) {
							if(!listaBiomaRequerimento.contains(biomaReq)) {
								listaBiomaRequerimento.add(biomaReq);
							}
						}
					}
					else {
						listaBiomaRequerimentoGerada = biomaRequerimentoDAOImpl.gerarListaBiomaRequerimentoParaPonto(req.getIdeRequerimento(), loc);
						if(isExisteBiomaRequerimento(listaBiomaRequerimentoGerada)) {
							listaBiomaRequerimento.addAll(listaBiomaRequerimentoGerada);
						}
					}
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void gerarBiomaRequerimentoPelaAbaFlorestal(Requerimento req, Collection<BiomaRequerimento> listaBiomaRequerimento, boolean existeAtoFlorestal) {
		if(existeAtoFlorestal) {
			Collection<PerguntaRequerimento> listaPerguntaRequerimento = perguntaRequerimentoService.listarPerguntaRequerimentoRespondidaAbaFlorestalComLocalizacaoGeografica(req);
			for(PerguntaRequerimento pr : listaPerguntaRequerimento) {
				LocalizacaoGeografica loc = pr.getIdeLocalizacaoGeografica();
				Boolean incluirNoCalculoVistoria = biomaRequerimentoDAOImpl.incluirNoCalculoVistoria(req, loc);
				if(incluirNoCalculoVistoria) {
					Collection<BiomaRequerimento> listaBiomaRequerimentoGerada = biomaRequerimentoDAOImpl.gerarListaBiomaRequerimento(req.getIdeRequerimento(), loc.getIdeLocalizacaoGeografica());
					if(isExisteBiomaRequerimento(listaBiomaRequerimentoGerada)) {
						listaBiomaRequerimento.addAll(listaBiomaRequerimentoGerada);
					}
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean verificarEnquadramentoEmAtoFlorestal(Collection<EnquadramentoAtoAmbiental> listaEnquadramentoAtoAmbiental) {
		boolean existeAtoFlorestal = false;
		Collection<AtoAmbiental> listaAtosEspeciais = atosEspeciais();
		Collection<AtoAmbiental> listaAtoFlorestal = atoAmbientalService.listarAtoAmbientalByTipoAtoByAtivo(new TipoAto(TipoAtoEnum.FLORESTAL.getId()));
		
		listaAtoFlorestal.remove(new AtoAmbiental(AtoAmbientalEnum.PPV_EPMF.getId()));
		listaAtoFlorestal.remove(new AtoAmbiental(AtoAmbientalEnum.PRORROGACAO_AUTORIZACAO_SUPRESSAO.getId()));
		
		for(EnquadramentoAtoAmbiental eaa : listaEnquadramentoAtoAmbiental) {
			if(listaAtoFlorestal.contains(eaa.getAtoAmbiental()) && !listaAtosEspeciais.contains(eaa.getAtoAmbiental())) {
				existeAtoFlorestal = true;
				break;
			}
		}
		return existeAtoFlorestal;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<BiomaRequerimento> somarAreasBiomaRequerimento(Collection<BiomaRequerimento> listaBiomaRequerimento) {
		
		Collection<BiomaRequerimento> listaRetorno = null;
		if(isExisteBiomaRequerimento(listaBiomaRequerimento)) {
			BiomaEnum[] biomas = BiomaEnum.values();
			listaRetorno = new ArrayList<BiomaRequerimento>();
			
			for(int index=0; index < biomas.length; index++) {
				
				BigDecimal somaTotalAreasBioma = BigDecimal.valueOf(0.0);
				
				for(BiomaRequerimento br : listaBiomaRequerimento) {
					if(br.getIdeBioma().getIdeBioma().equals(biomas[index].getId())) {
						somaTotalAreasBioma = BigDecimal.valueOf(br.getValArea()).add(somaTotalAreasBioma);
					}
				}
				
				BiomaRequerimento biomaRequerimentoAreaSomada = montarBiomaRequerimento(biomas, index, somaTotalAreasBioma);
				listaRetorno.add(biomaRequerimentoAreaSomada);
			}
		}
		return listaRetorno;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private BiomaRequerimento montarBiomaRequerimento(BiomaEnum[] biomas, int index, BigDecimal somaTotalAreasBioma) {
		
		Bioma ideBioma = new Bioma();
		ideBioma.setIdeBioma(biomas[index].getId());
		ideBioma.setNomBioma(biomas[index].getNomBioma());
		
		BiomaRequerimento biomaRequerimentoAreaSomada = new BiomaRequerimento();
		biomaRequerimentoAreaSomada.setIdeBioma(ideBioma);
		biomaRequerimentoAreaSomada.setValArea(somaTotalAreasBioma.doubleValue());
		
		return biomaRequerimentoAreaSomada;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaBiomaRequerimento(Collection<BiomaRequerimento> listaBiomaRequerimento) {
		biomaRequerimentoDAOImpl.salvarEmLote(listaBiomaRequerimento);
	}
	
	public Double calcularValorTotalAreaBioma(Collection<BiomaRequerimento> listaBiomaRequerimento) {
		BigDecimal soma = BigDecimal.ZERO;
		for(BiomaRequerimento br : listaBiomaRequerimento) {
			if(!Util.isNullOuVazio(br.getValArea())) {
				soma=soma.add(BigDecimal.valueOf(br.getValArea()));
			}
		}
		return soma.doubleValue();
	}
}