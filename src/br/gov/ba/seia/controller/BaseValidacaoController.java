package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public class BaseValidacaoController {

	@EJB
	protected AtoAmbientalService atoAmbientalService;

	@EJB
	protected RequerimentoUnicoService requerimentoUnicoService;
	
	@EJB
	protected EmpreendimentoService empreendimentoService;

	@EJB
	protected AreaService areaService;
	
	protected Collection<Area> areas;
	
	protected Collection<AtoAmbiental> atosAmbientais;
	
	protected EmpreendimentoRequerimento empreendimentoRequerimento;
	

	protected boolean isTipologiaDoEmpreendimentoXorY() throws Exception {
		return requerimentoUnicoService.isTipologiaDoEmpreendimentoXouY(this.empreendimentoRequerimento.getIdeEmpreendimento());
	}
	
	protected void consultarAreas(Requerimento requerimento) {
	
		try {
			
			if(Util.isNull(this.areas)) {
				this.areas = new ArrayList<Area>();
			}
			
			if (this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.ARLS.getId()))) {
				this.areas = this.areaService.listarAreasByIds(AreaEnum.SELIC.getId());
				requerimento.setIdeArea(this.areas.iterator().next());
				return;
			}
			else if (this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.TLA.getId()))) {
				this.areas = this.areaService.listarAreasByIds(AreaEnum.DIREG.getId());
				requerimento.setIdeArea(this.areas.iterator().next());
				return;
			}
			else {
				this.areas = this.areaService.listarPorRequerimento(requerimento, this.isAtoRloRlu());
			}
			
			if(!Util.isNull(this.empreendimentoRequerimento) && isTipologiaDoEmpreendimentoXorY()) {
				List<Area> areasPorCoordenacoes = (List<Area>) areaService.listarCoordenacoesAnexo(this.isAtoRloRlu());
				for(Area area : areasPorCoordenacoes){
					if(!areas.contains(area)) {
						this.areas.add(area);
					}
				}
			}
			
			Porte porte = null;
			
			if(!Util.isNull(this.empreendimentoRequerimento)) {
				porte = this.empreendimentoRequerimento.getIdePorte();
			}
			
			if (!Util.isNullOuVazio(porte) && !Util.isLazyInitExcepOuNull(porte) && (porte.isPequeno() || porte.isMedio())) {
				Area coged = this.areaService.carregarGet(AreaEnum.COGED.getId());
				if(!areas.contains(coged)){
					this.areas.add(coged);
				}
			}
			
			if(isOutorga() || isAA()) {
				Area nout = this.areaService.carregarAreaByCriteria(AreaEnum.NOUT.getId());
				if(!areas.contains(nout)){
					this.areas.add(nout);
				}
			}
			
			Collection<AtoAmbiental> listaAtosRequerimento = retornarListaAtosRequerimento(requerimento);
			
			if(isRequerimentoEnquadradoEmAtoFlorestal(listaAtosRequerimento) || isRequerimentoEnquadradoDeclaratorio(listaAtosRequerimento)) {
				Area coasp = this.areaService.carregarAreaByCriteria(AreaEnum.COASP.getId());
				if(!this.areas.contains(coasp)) {
					this.areas.add(coasp);
				}
			}else {
				List<Tipologia> listTipologias = new ArrayList<Tipologia>();
				listTipologias.add(new Tipologia(TipologiaEnum.BOVINOS_BUBALINOS_MUARES_EQUINOS));
				listTipologias.add(new Tipologia(TipologiaEnum.AVES_PEQUENOS_MAMIFEROS));
				listTipologias.add(new Tipologia(TipologiaEnum.CAPRINO_OVINOS));
				listTipologias.add(new Tipologia(TipologiaEnum.SUINOS));
				listTipologias.add(new Tipologia(TipologiaEnum.CRECHE_SUINOS));
				if(!Util.isNullOuVazio(empreendimentoRequerimento)) {
					empreendimentoRequerimento = empreendimentoService.buscarEmpreendimentoRequerimentoComTipologia(empreendimentoRequerimento.getIdeRequerimento());
					if(isTipoAtoLicenca(listaAtosRequerimento) && temTipologiasEspecificas(empreendimentoRequerimento,listTipologias)) {
						Area coasp = this.areaService.carregarAreaByCriteria(AreaEnum.COASP.getId());
						if(!this.areas.contains(coasp)) {
							this.areas.add(coasp);
						}
					}
				}
			}
			
			List<Tipologia> listTipologiasCofaq = new ArrayList<Tipologia>();
			listTipologiasCofaq.add(new Tipologia(TipologiaEnum.CRIADOUROS_COMERCIAS));
			listTipologiasCofaq.add(new Tipologia(TipologiaEnum.CRIADOUROS_CIENTIFICOS_CRAS_CETAS_MANTENEDORES));
			listTipologiasCofaq.add(new Tipologia(TipologiaEnum.ZOOLOGICOS));
			listTipologiasCofaq.add(new Tipologia(TipologiaEnum.CRIADOUROS_COMERCIAS_ABELHAS));
			listTipologiasCofaq.add(new Tipologia(TipologiaEnum.ABATEDOUROS_FRIGORIFICOS));
			
			if(!Util.isNullOuVazio(empreendimentoRequerimento)) {
				empreendimentoRequerimento = empreendimentoService.buscarEmpreendimentoRequerimentoComTipologia(empreendimentoRequerimento.getIdeRequerimento());
				if(temTipologiasEspecificas(empreendimentoRequerimento,listTipologiasCofaq)) {
					Area cofaq = this.areaService.carregarAreaByCriteria(AreaEnum.COFAQ.getId());
					if(!this.areas.contains(cofaq)) {
						this.areas.add(cofaq);
					}
				}
			}
			
			
			if(isUnicoAtoAmbiental() && isAnuencia()) {
				Area coges = this.areaService.carregarAreaByCriteria(AreaEnum.COGES.getId());
				this.areas.add(coges);
			}
			
			if(isRequerimentoEnquadradoSemTipologia(listaAtosRequerimento)) {
				Collection<Area> listaArea = areaService.listarAreaParaRequerimentoEnquadradoSemTipologia();
				for(Area area : listaArea) {
					if(!this.areas.contains(area)) {
						this.areas.add(area);
					}
				}
			}
			
			Collections.sort((List<Area>) this.areas);

		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível carregar a lista de áreas - " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	private Collection<AtoAmbiental> retornarListaAtosRequerimento(
			Requerimento requerimento) {
		Collection<AtoAmbiental> listaAtosRequerimento = atoAmbientalService.filtrarListaAtoAmbientalPorEnquadramentoRequerimento(requerimento.getIdeRequerimento());
		return listaAtosRequerimento;
	}
	
	private boolean isRequerimentoEnquadradoSemTipologia(Collection<AtoAmbiental> atos)  {
		
		List<AtoAmbiental> listaAtosValidacao = atoAmbientalService.listarAtosParaRequerimentosEnquadradosSemTipologia();
		
		for(AtoAmbiental atoValidacao : listaAtosValidacao) {
			if(atos.contains(atoValidacao)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isRequerimentoEnquadradoEmAtoFlorestal(Collection<AtoAmbiental> atos) {
		return existeUmOuMaisAtosDoTipoNaLista(atos,TipoAtoEnum.FLORESTAL.getId());
	}

	private boolean isTipoAtoLicenca(Collection<AtoAmbiental> atos) {
		return existeUmOuMaisAtosDoTipoNaLista(atos,TipoAtoEnum.LICENCA.getId());
	}
	
	private boolean isRequerimentoEnquadradoDeclaratorio(Collection<AtoAmbiental> atos) {
		return existeUmOuMaisAtosDoTipoNaLista(atos,TipoAtoEnum.DECLARATORIO.getId());
	}

	private boolean existeUmOuMaisAtosDoTipoNaLista(Collection<AtoAmbiental> atos, Integer ideTipoAto) {
		if(!Util.isNullOuVazio(atos)) {
			for (AtoAmbiental atoAmbiental : atos) {
				if(atoAmbiental.getIdeTipoAto().getIdeTipoAto().equals(ideTipoAto)) {
					return true;
				}
			}
		}
		return false;
	}	
	
	private boolean temTipologiasEspecificas(EmpreendimentoRequerimento empreendimentoRequerimento, List<Tipologia> listTipologias) {
		
		if(!Util.isNullOuVazio(empreendimentoRequerimento)){
			if(!Util.isNullOuVazio(empreendimentoRequerimento.getIdeEmpreendimento())) {
				for(Tipologia empTipologia : empreendimentoRequerimento.getIdeEmpreendimento().getTipologias()) {
					if(listTipologias.contains(empTipologia)) {
						return true;
					}
				}
			}
		} 
		
		
		return false;

	}
	
	private boolean isAA() {
		return this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.AUTORIZACAO_AMBIENTAL.getId()));
	}
	
	private boolean isOutorga() {
		return this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.ROUT.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.OUTP.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.DOUT.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.DRDH.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.AOUT.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.PPV_OUT.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.COUT.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.PERFURACAO_POCO.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.TRAVESSIA_DE_DUTO.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.LANCAMENTO_DE_EFLUENTES_PARA_OS_DIVERSOS_FINS.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.EXTRAÇÃO_EXPLOTAÇÃO_MINERAL_EM_LEITO_PESQUISA_E_LAVRA_LIMPEZA_DESASSOREAMENTO_E_DRAGAGEM.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.DRENAGEM_DE_AGUAS_PLUVIAIS_COM_DESAGUE_EM_MANANCIAL.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.CONSTRUCAO_DE_PONTE.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.CONSTRUCAO_DE_PIER_DIQUE_CAIS.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.BARRAGEM.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.CANALIZACAO_E_RETIFICACAO.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.CAPTACAO_ABASTECIMENTO_HUMANO.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.CAPTACAO_ABASTECIMENTO_INDUSTRIAL.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.CAPTACAO_AQUICULTURA.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.CAPTACAO_DESSEDENTACAO_E_CRIACAO_ANIMAL.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.IRRIGACAO.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.PULVERIZACAO.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.OUTORGA.getId()));
	}
	
	@SuppressWarnings("unused")
	private boolean isALRSouTLA() {
		return this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.ARLS.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.TLA.getId()));
	}
	
	private boolean isAtoRloRlu() {
		return this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.RLO.getId()))
				|| this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.RLU.getId()));
	}
	
	private boolean isUnicoAtoAmbiental(){
		return atosAmbientais.size() == 1;
	}
	
	private boolean isAnuencia(){
		return this.atosAmbientais.contains(new AtoAmbiental(AtoAmbientalEnum.ANUENCIA.getId()));
	}
	
	protected boolean isDirucSecundaria(){
		return !isUnicoAtoAmbiental() && isAnuencia();
	}

	public Collection<Area> getAreas() {
		return areas;
	}

	public void setAreas(Collection<Area> areas) {
		this.areas = areas;
	}

	public Collection<AtoAmbiental> getAtosAmbientais() {
		return atosAmbientais;
	}

	public void setAtosAmbientais(Collection<AtoAmbiental> atosAmbientais) {
		this.atosAmbientais = atosAmbientais;
	}
	
	
	
}
