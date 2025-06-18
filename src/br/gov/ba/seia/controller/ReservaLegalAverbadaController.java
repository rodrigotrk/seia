package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ReservaLegalAverbada;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("reservaLegalAverbadaController")
@ViewScoped
public class ReservaLegalAverbadaController extends SeiaControllerAb {

	private Imovel imovelSelecionado;
	private ReservaLegal reservaLegalSelecionada = new ReservaLegal();
	private ReservaLegalAverbada reservaLegalAverbadaSelecionada = new ReservaLegalAverbada();
	private Boolean indAprovacao = false;	
	private List<String> listaArquivoReservaAverbada;
		
	@EJB
	private ImovelRuralFacade imovelRuralServiceFacade;
	
		
	@PostConstruct
	public void init() {		
	}

		
	public String carregarReservaLegalAverbada(){
		try{
			if (!Util.isNullOuVazio(imovelSelecionado.getImovelRural().getReservaLegal())){
				imovelSelecionado.getImovelRural().setReservaLegal(imovelRuralServiceFacade.carregarTudo(imovelSelecionado.getImovelRural().getReservaLegal()));
				this.reservaLegalAverbadaSelecionada = imovelRuralServiceFacade.carregarReservaLegalAverbada(imovelSelecionado.getImovelRural().getReservaLegal());
				if(!Util.isNullOuVazio(reservaLegalAverbadaSelecionada)) {
					listaArquivoReservaAverbada = new ArrayList<String>();
					this.listaArquivoReservaAverbada.add(reservaLegalAverbadaSelecionada.getIdeDocumentoAverbacao().getNomDocumentoObrigatorio());					
				}
			}
		} catch (Exception e) {			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}	
		
		return null;
	}
	
	public void salvarValidacaoDocumentoAverbacao(){
		try{
			if(!Util.isNullOuVazio(imovelSelecionado.getImovelRural().getReservaLegal())){				
				imovelSelecionado.getImovelRural().getReservaLegal().setDtcAprovacao(new Date());				
				imovelSelecionado.getImovelRural().getReservaLegal().setIdeUsuarioAprovacao(new Usuario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
				if(indAprovacao){
					imovelSelecionado.getImovelRural().getReservaLegal().setIdeStatus(new StatusReservaLegal(2));
					imovelRuralServiceFacade.atualizarReservaLegal(imovelSelecionado.getImovelRural().getReservaLegal());
					JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S007"));
					RequestContext context = RequestContext.getCurrentInstance();
					context.addCallbackParam("closeModal", true);
					imovelSelecionado = null;
				}else{
					if(!Util.isNullOuVazio(imovelSelecionado.getImovelRural().getReservaLegal().getObservacao())){
						imovelSelecionado.getImovelRural().getReservaLegal().setIdeStatus(new StatusReservaLegal(6));
						imovelRuralServiceFacade.atualizarReservaLegal(imovelSelecionado.getImovelRural().getReservaLegal());
						JsfUtil.addSuccessMessage("Status da reserva legal alterado com sucesso!");
						RequestContext context = RequestContext.getCurrentInstance();
						context.addCallbackParam("closeModal", true);
						imovelSelecionado = null;
					}else{
						JsfUtil.addErrorMessage("O preenchimento do campo Observações é obrigatório!");
					}
				}
			}
			
			
		} catch (Exception e) {			
			JsfUtil.addErrorMessage(e.getMessage());
		}		
	}
	
	public StreamedContent getArquivoBaixarReservaAverbada() {
		StreamedContent arquivoBaixar = null;
		String dscCaminhoArquivo = this.reservaLegalAverbadaSelecionada.getIdeDocumentoAverbacao().getDscCaminhoArquivo();
		if(!Util.isNullOuVazio(dscCaminhoArquivo)) {
			try {
				arquivoBaixar  = imovelRuralServiceFacade.getContentFile(dscCaminhoArquivo);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
			return arquivoBaixar;
	}
			
	public Imovel getImovelSelecionado() {
		return imovelSelecionado;
	}

	public void setImovelSelecionado(Imovel imovelSelecionado) {
		this.imovelSelecionado = imovelSelecionado;
	}
	
	public ReservaLegal getReservaLegalSelecionada() {
		return reservaLegalSelecionada;
	}

	public void setReservaLegalSelecionada(ReservaLegal reservaLegalSelecionada) {
		this.reservaLegalSelecionada = reservaLegalSelecionada;
	}
	
	public ReservaLegalAverbada getReservaLegalAverbadaSelecionado() {
		return reservaLegalAverbadaSelecionada;
	}

	public void setReservaLegalAverbadaSelecionado(ReservaLegalAverbada reservaLegalAverbadaSelecionada) {
		this.reservaLegalAverbadaSelecionada = reservaLegalAverbadaSelecionada;
	}
	
	public Boolean getIndAprovacao() {
		return indAprovacao;
	}

	public void setIndAprovacao(Boolean indAprovacao) {
		this.indAprovacao = indAprovacao;
	}	
	
	public List<String> getListaArquivoReservaAverbada() {
		return listaArquivoReservaAverbada;
	}

	public void setListaArquivoReservaAverbada(List<String> listaArquivoReservaAverbada) {
		this.listaArquivoReservaAverbada = listaArquivoReservaAverbada;
	}
	
}