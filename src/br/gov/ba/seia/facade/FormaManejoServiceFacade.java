package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.gov.ba.seia.entity.ParametroReferencia;
import br.gov.ba.seia.entity.PorteCompetencia;
import br.gov.ba.seia.entity.PorteTipologia;
import br.gov.ba.seia.entity.TipologiaGrupoArea;
import br.gov.ba.seia.entity.TipologiaTipoAto;
import br.gov.ba.seia.entity.UnidadeMedidaTipologiaGrupo;
import br.gov.ba.seia.entity.VwFormaManejo;
import br.gov.ba.seia.service.ParametroReferenciaService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.PorteCompetenciaService;
import br.gov.ba.seia.service.PorteTipologiaService;
import br.gov.ba.seia.service.TipologiaGrupoAreaService;
import br.gov.ba.seia.service.TipologiaTipoAtoService;
import br.gov.ba.seia.service.UnidadeMedidaTipologiaGrupoService;

/**
 * @author mario.junior
 */
@Stateless
public class FormaManejoServiceFacade extends PessoaService {
	@EJB
	private PorteTipologiaService porteTipologiaService;
	@EJB
	private UnidadeMedidaTipologiaGrupoService unidadeMedidaTipologiaGrupoService;
	@EJB
	private ParametroReferenciaService parametroReferenciaService;
	@EJB
	private PorteCompetenciaService porteCompetenciaService;
	@EJB
	private TipologiaTipoAtoService tipologiaTipoAtoService;
	@EJB
	private TipologiaGrupoAreaService tipologiaGrupoAreaService;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarFormaManejo(VwFormaManejo vwFormaManejo) throws Exception {
		
		//deletar paramentro referencia e unidade tipologia grupo
		Collection<UnidadeMedidaTipologiaGrupo> unidadeMedidaTipologiaGrupo = this.unidadeMedidaTipologiaGrupoService.recuperarUnidadeMedidaTipologiaGrupo(vwFormaManejo.getIdeTipologiaGrupo());
		Collection<ParametroReferencia> parametroReferencia = this.parametroReferenciaService.recuperarParametroReferencia(unidadeMedidaTipologiaGrupo);
		
		this.parametroReferenciaService.deletarParametroReferencias(parametroReferencia);
		this.unidadeMedidaTipologiaGrupoService.deletarUnidadeMedidaTipologiaGrupos(unidadeMedidaTipologiaGrupo);
		
		//deletar porte tipologia
		Collection<PorteTipologia> porteTipologia = this.porteTipologiaService.recuperarPorteTipologia(vwFormaManejo.getIdeTipologiaGrupo());
		this.porteTipologiaService.deletarPorteTipologias(porteTipologia);
		
		
		//deletar porte compentencia
		Collection<PorteCompetencia> porteCompetencia = this.porteCompetenciaService.recuperarPorteCompetencia(vwFormaManejo.getIdeTipologiaGrupo());
		this.porteCompetenciaService.deletarPorteCompetencia(porteCompetencia);
		
		//deletar tipologia tipo ato
		Collection<TipologiaTipoAto> tipologiaTipoAto = this.tipologiaTipoAtoService.recuperarTipologiaTipoAto(vwFormaManejo.getIdeTipologiaGrupo());
		this.tipologiaTipoAtoService.deletarTipologiaTipoAto(tipologiaTipoAto);
		
		//deletar tipologia grupo area
		Collection<TipologiaGrupoArea> tipologiaGrupoArea = this.tipologiaGrupoAreaService.recuperarTipologiaGrupoArea(vwFormaManejo.getIdeTipologiaGrupo());
		this.tipologiaGrupoAreaService.deletarTipologiaGrupoArea(tipologiaGrupoArea);
		
		//deletar tipologia grupo 
		//TipologiaGrupo tipologiaGrupo = this.tipologiaGrupoService.carregarTipologiaGrupo(vwFormaManejo.getIdeTipologiaGrupo());
		//this.tipologiaGrupoService.deletarTipologiaGrupo(tipologiaGrupo);
		//this.tipologiaGrupoService.deletarTipologiaGrupoPorNamedQuery(tipologiaGrupo);
		
	}

		
	

	
}