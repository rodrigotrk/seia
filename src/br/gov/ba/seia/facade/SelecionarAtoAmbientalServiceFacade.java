package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.primefaces.model.TreeNode;

import br.gov.ba.seia.dao.AtoAmbientalDAOImpl;
import br.gov.ba.seia.dao.TipoAtoDAOImpl;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ObjetivoAtividadeManejo;
import br.gov.ba.seia.entity.TipoAtividadeFauna;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.service.FaunaService;
import br.gov.ba.seia.service.SelecionarAtoAmbientalService;
import br.gov.ba.seia.service.TipologiaService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SelecionarAtoAmbientalServiceFacade {
	
	@EJB
	private AtoAmbientalDAOImpl atoAmbientalDAOImpl;
	
	@EJB
	private TipoAtoDAOImpl tipoAtoDAOImpl;
	
	@EJB
	private TipologiaService tipologiaService;
	
	@EJB
	private FaunaService faunaService;
	
	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;
	
	@EJB
	private SelecionarAtoAmbientalService selecionarAtoAmbientalService;  
	
	public Collection<AtoAmbiental> listarAtoAmbientalPorTipoAto(TipoAto tipoAto) {
		return atoAmbientalDAOImpl.listarPorTipoAto(tipoAto);
	}
	
	public Collection<Tipologia> listarTipologiaPorAtoAmbiental(AtoAmbiental atoAmbiental) throws Exception {
		return tipologiaService.listarPorAtoAmbiental(atoAmbiental);
	}
	
	public Collection<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAgua(AtoAmbiental atoAmbiental, Tipologia tipologia) throws Exception {
		return tipoFinalidadeUsoAguaService.listarTipoFinalidadeUsoAgua(atoAmbiental, tipologia);
	}
	
	public Collection<ObjetivoAtividadeManejo> listarObjetivoAtividadeManejo()  {
		return faunaService.listarObjetivoAtividadeManejoParaReenquadramento();
	}
	
	public Collection<TipoAtividadeFauna> listarTipoAtividadeFauna() {
		return faunaService.listarTipoAtividadeFaunaParaReenquadramento();
	}
	
	public Collection<TipoAto> listarTipoAto() {
		return tipoAtoDAOImpl.listarTodos();
	}
	
	public Collection<TipoAto> listarTipoAtoComAtoAmbiental()  {
		return tipoAtoDAOImpl.listarTiposAtoComAtoAmbiental();
	}

	public TreeNode montarArvoreTipologia(Tipologia tipologia) {
		return selecionarAtoAmbientalService.montarArvoreTipologia(tipologia);
	}
	
	public void adicionaGrupoTipologia(boolean fecharDialog,TreeNode root, TreeNode selectedNode,Collection<Tipologia> listaTipologiaEmpreendimento) {
		selecionarAtoAmbientalService.adicionaGrupoTipologia(fecharDialog, root, selectedNode,listaTipologiaEmpreendimento);
	}	
	
}