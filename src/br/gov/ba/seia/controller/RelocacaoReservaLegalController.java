package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.dto.DadoConcedidoImpl;
import br.gov.ba.seia.dto.RelocacaoReservaLegalDTO;
import br.gov.ba.seia.entity.FormaRealocacaoRl;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.TipoArl;
import br.gov.ba.seia.enumerator.FormaRealocacaoRlEnum;
import br.gov.ba.seia.enumerator.TipoArlEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.DadoConcedidoInterface;
import br.gov.ba.seia.service.facade.RelocacaoReservaLegalServiceFacade;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("relocacaoReservaLegalController")
@ViewScoped
public class RelocacaoReservaLegalController {
	
	@EJB
	private RelocacaoReservaLegalServiceFacade relocacaoReservaLegalServiceFacade;
	
	private Collection<RelocacaoReservaLegalDTO> listaRelocacaoReservaLegalDTO;
	private Collection<TipoArl> listaTipoArl;
	private Collection<FormaRealocacaoRl> listaFormaRealocacaoRl;
	private boolean somenteVisualizacao;
	private DadoConcedidoImpl dadoConcedido;
	
	@PostConstruct
	private void init() {
		
	}
	
	public void load(DadoConcedidoInterface dadoConcedidoDTO, boolean somenteVisualizacao) {
		try{
			this.somenteVisualizacao = somenteVisualizacao;
			dadoConcedido = (DadoConcedidoImpl) dadoConcedidoDTO;
			listaRelocacaoReservaLegalDTO = relocacaoReservaLegalServiceFacade.carregar(dadoConcedido.getProcessoAto());
			listaTipoArl = relocacaoReservaLegalServiceFacade.listarTipoArl();
			listaFormaRealocacaoRl = relocacaoReservaLegalServiceFacade.listarTodosFormaRealocacaoRls();
			RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlRelocacaoReservaLegal");
			RequestContext.getCurrentInstance().execute("dlgRelocacaoReservaLegal.show();");
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isRenderedBtnSalvar() {
		return somenteVisualizacao == false;
	}
	
	public boolean isDisabled(RelocacaoReservaLegalDTO relocacaoReservaLegalDTO, ReservaLegal rl) {
		if(somenteVisualizacao) {
			return true;
		}
		else if(Util.isNull(relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada())) {
			return false;
		}
		
		return !relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().equals(rl);
	}
	
	public void add(RelocacaoReservaLegalDTO relocacaoReservaLegalDTO, ReservaLegal rl) {
		if(!Util.isNull(rl) && rl.isIndConcedido()) {
			relocacaoReservaLegalDTO.setReservaLegalPoligonalSelecionada(rl);
			formaRealocacaotoTipoArl(relocacaoReservaLegalDTO);
		}
		else {
			relocacaoReservaLegalDTO.setReservaLegalPoligonalSelecionada(null);
			rl.setIdeFormaRealocacaoRl(null);
		}
	}
	
	public void change(RelocacaoReservaLegalDTO relocacaoReservaLegalDTO, ReservaLegal rl) {
		if(rl.isIndConcedido()) {
			relocacaoReservaLegalDTO.setReservaLegalPoligonalSelecionada(rl);
			
			formaRealocacaotoTipoArl(relocacaoReservaLegalDTO);
		}
	}

	private void formaRealocacaotoTipoArl(
			RelocacaoReservaLegalDTO relocacaoReservaLegalDTO) {
//		relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().setIdeTipoArl(new TipoArl());
		
		if(!Util.isNull(relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().getIdeFormaRealocacaoRl())) {
			if(relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().getIdeFormaRealocacaoRl().getIdeFormaRealocacaoRl().equals(FormaRealocacaoRlEnum.ECOND.getId())){
				relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().setIdeTipoArl(new TipoArl(TipoArlEnum.ECOND.getId()));
				relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().getIdeFormaRealocacaoRl().setIdeFormaRealocacaoRl(FormaRealocacaoRlEnum.ECOND.getId());
			}else if(relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().getIdeFormaRealocacaoRl().getIdeFormaRealocacaoRl().equals(FormaRealocacaoRlEnum.NPI.getId())){
				relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().setIdeTipoArl(new TipoArl(TipoArlEnum.NPI.getId()));
				relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().getIdeFormaRealocacaoRl().setIdeFormaRealocacaoRl(FormaRealocacaoRlEnum.NPI.getId());
			}else if(relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().getIdeFormaRealocacaoRl().getIdeFormaRealocacaoRl().equals(FormaRealocacaoRlEnum.ECSF.getId())){
				relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().getIdeFormaRealocacaoRl().setIdeFormaRealocacaoRl(FormaRealocacaoRlEnum.ECSF.getId());
			}else if(relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().getIdeFormaRealocacaoRl().getIdeFormaRealocacaoRl().equals(FormaRealocacaoRlEnum.ECIP.getId())){
				relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().getIdeFormaRealocacaoRl().setIdeFormaRealocacaoRl(FormaRealocacaoRlEnum.ECIP.getId());
			} else if(relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().getIdeFormaRealocacaoRl().getIdeFormaRealocacaoRl().equals(FormaRealocacaoRlEnum.CDAUC.getId())){
				relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().setIdeTipoArl(new TipoArl(TipoArlEnum.CDAUC.getId()));
				relocacaoReservaLegalDTO.getReservaLegalPoligonalSelecionada().getIdeFormaRealocacaoRl().setIdeFormaRealocacaoRl(FormaRealocacaoRlEnum.CDAUC.getId()); 
			}
		}
	}
	
	public void salvar() {
		try {
			relocacaoReservaLegalServiceFacade.salvar(listaRelocacaoReservaLegalDTO);
			dadoConcedido.setConcluido(true);
			RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlAnaliseTecnica");
			JsfUtil.addSuccessMessage(Util.getString("relocacao_reverva_legal_msg_sucesso"));
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("relocacao_reverva_legal_msg_erro"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public String criarUrlShape(LocalizacaoGeografica lg) {
		return Util.criarUrlShape(lg);
	}
	
	public boolean isRenderedPoligonalRequerimento(RelocacaoReservaLegalDTO relocacaoReservaLegalDTO) {
		return relocacaoReservaLegalDTO.getListaPoligonalRequerimento().isEmpty() == false;
	}
	
	public boolean isRenderedPoligonalNotificacao(RelocacaoReservaLegalDTO relocacaoReservaLegalDTO) {
		return relocacaoReservaLegalDTO.getListaPoligonalNotificacao().isEmpty() == false;
	}
	
	public Collection<RelocacaoReservaLegalDTO> getListaRelocacaoReservaLegalDTO() {
		return listaRelocacaoReservaLegalDTO;
	}

	public void setListaRelocacaoReservaLegalDTO(
			Collection<RelocacaoReservaLegalDTO> listaRelocacaoReservaLegalDTO) {
		this.listaRelocacaoReservaLegalDTO = listaRelocacaoReservaLegalDTO;
	}

	public Collection<TipoArl> getListaTipoArl() {
		return listaTipoArl;
	}

	public void setListaTipoArl(Collection<TipoArl> listaTipoArl) {
		this.listaTipoArl = listaTipoArl;
	}

	public Collection<FormaRealocacaoRl> getListaFormaRealocacaoRl() {
		return listaFormaRealocacaoRl;
	}

	public void setListaFormaRealocacaoRl(
			Collection<FormaRealocacaoRl> listaFormaRealocacaoRl) {
		this.listaFormaRealocacaoRl = listaFormaRealocacaoRl;
	}

}
