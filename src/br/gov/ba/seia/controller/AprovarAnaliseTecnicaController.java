package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.AprovacaoAnaliseAtoDTO;
import br.gov.ba.seia.dto.DadoConcedidoFceImpl;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.DadoConcedidoInterface;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceUtil;


@Named("aprovarAnaliseTecnicaController")
@ViewScoped
public class AprovarAnaliseTecnicaController extends SuperProcessoAnaliseTecnicaController{
	
	private Funcionario gestor;
	private Collection<AprovacaoAnaliseAtoDTO> listaAprovacaoAnaliseAtoDTO;
	
	@Override
	public void load(VwConsultaProcesso vwProcesso) {
		try{
			super.load(vwProcesso);

			listaAprovacaoAnaliseAtoDTO = super.analiseTecnicaServiceFacade.listarAprovacaoAnaliseAtoDTO(vwProcesso.getIdeProcesso());
			
			carregarGestor();
			
			rc().addPartialUpdateTarget("pnlAprovarAnaliseTecnica");
			rc().execute("dlgAprovarAnaliseTecnica.show();");
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	
	}
	
	public void abrirDadoConcedido(DadoConcedidoInterface dadoConcedido) {
		super.abrirDadoConcedido(dadoConcedido, true);
	}

	private void carregarGestor() {
		gestor = new Funcionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
	}
	
	public boolean isRenderedEnviarSalvar() {
		boolean aguardaAprovacao = vwProcesso.getStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.AGUARDANDO_APROVACAO_ANALISE_TECNICA.getStatus());
		return !super.analiseTecnicaServiceFacade.isAnaliseAprovadaPeloGestor(vwProcesso.getIdeProcesso()) || aguardaAprovacao;
	}
	
	public boolean isDesabilitarCampos() {
		boolean aguardaAprovacao = vwProcesso.getStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.AGUARDANDO_APROVACAO_ANALISE_TECNICA.getStatus());
		return super.analiseTecnicaServiceFacade.isAnaliseAprovadaPeloGestor(vwProcesso.getIdeProcesso()) && !aguardaAprovacao;
	}

	public void visualizarFce(DadoConcedidoInterface dadoConcedidoDTO) {
		try {
			DadoConcedidoFceImpl dadoConcedido = (DadoConcedidoFceImpl) dadoConcedidoDTO;
			Fce fceTovisualizar = dadoConcedido.getFce().clone();
			fceTovisualizar.setIdeDadoOrigem(null);
			FceUtil.visualizarFce(fceTovisualizar);
		}
		catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void visualizarFceTecnico(DadoConcedidoInterface dadoConcedidoDTO) {
		DadoConcedidoFceImpl dadoConcedido = (DadoConcedidoFceImpl) dadoConcedidoDTO;
		FceUtil.visualizarFce(dadoConcedido);
	}
	
	public String getMensagemConfirmacao() {
		
		if(Util.isNull(super.analiseTecnica)) {
			return "";
		}
		else if(!Util.isNull(super.analiseTecnica.getIndAprovado()) && super.analiseTecnica.getIndAprovado()) {
			return Util.getString("analise_tecnica_msg_aprovacao");
		}
		
		return Util.getString("analise_tecnica_msg_revisao");
	}
	
	public void confirmarEnvio() {
		rc().addPartialUpdateTarget("formDlgConfirmarFimAnaliseTecnicaGestor");
		rc().execute("dlgConfirmarFimAnaliseTecnicaGestor.show();");
	}
	
	public void salvar() {
		try {
			super.analiseTecnicaServiceFacade.salvarAprovacaoAnaliseTecnica(super.analiseTecnica, listaAprovacaoAnaliseAtoDTO, super.vwProcesso.getIdeProcesso(), gestor.getIdePessoaFisica());
			JsfUtil.addSuccessMessage("Informações salvas com sucesso.");
			rc().execute("atualizarPauta();");
			rc().execute("dlgAprovarAnaliseTecnica.hide();");
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void enviar() {
		try {
			if(isDecisaoParaRevisao() && isObservacaoNaoPreenchido()){
				JsfUtil.addErrorMessage(Util.getString("analise_tecnica_msg_obrigatoriedade_observacao"));
				rc().execute("dlgConfirmarFimAnaliseTecnicaGestor.hide();");
			} 
			else {
				super.analiseTecnicaServiceFacade.enviarAprovacaoAnaliseTecnica(super.analiseTecnica, listaAprovacaoAnaliseAtoDTO, super.vwProcesso.getIdeProcesso(), gestor.getIdePessoaFisica());
				JsfUtil.addSuccessMessage("Dados enviados com sucesso.");
				rc().execute("atualizarPauta();");
				rc().execute("dlgConfirmarFimAnaliseTecnicaGestor.hide();");
				rc().execute("dlgAprovarAnaliseTecnica.hide();");
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 16/03/2016
	 */
	public boolean isObservacaoNaoPreenchido() {
		return Util.isNullOuVazio(super.analiseTecnica.getObservacao());
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 16/03/2016
	 */
	public boolean isDecisaoParaRevisao() {
		return !super.analiseTecnica.getIndAprovado();
	}

	public Collection<AprovacaoAnaliseAtoDTO> getListaAprovacaoAnaliseAtoDTO() {
		return listaAprovacaoAnaliseAtoDTO;
	}

	public void setListaAprovacaoAnaliseAtoDTO(Collection<AprovacaoAnaliseAtoDTO> listaAprovacaoAnaliseAtoDTO) {
		this.listaAprovacaoAnaliseAtoDTO = listaAprovacaoAnaliseAtoDTO;
	}
	
}