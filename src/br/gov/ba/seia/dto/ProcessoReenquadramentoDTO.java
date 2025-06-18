package br.gov.ba.seia.dto;

import java.util.Collection;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;

public class ProcessoReenquadramentoDTO {

	private ProcessoReenquadramento processoReenquadramento;
	private StatusReenquadramento statusAtual;
	private Requerimento requerimento;
	private Pessoa requerente;
	private Empreendimento empreendimento;
	private Collection<Pessoa> listaPessoaRequerimento;
	private StatusReenquadramentoEnum statusTramitado;
	
	private boolean visualizar;
	
	public boolean isRenderedReenquadrar() {
		return new StatusReenquadramento(StatusReenquadramentoEnum.AGUARDANDO_REENQUADRAMENTO).equals(statusAtual);
	}
	
	public boolean isRenderedEnvioDocumentacao() {
		return new StatusReenquadramento(StatusReenquadramentoEnum.AGUARDANDO_ENVIO_DOCUMENTACAO).equals(statusAtual) 
			|| new StatusReenquadramento(StatusReenquadramentoEnum.PENDENCIA_ENVIO_DOCUMENTACAO).equals(statusAtual)
			|| new StatusReenquadramento(StatusReenquadramentoEnum.PENDENCIA_VALIDACAO_DOCUMENTO).equals(statusAtual);
	}
	
	public boolean isRenderedValidacaoPrevia() {
		return new StatusReenquadramento(StatusReenquadramentoEnum.EM_VALIDACAO_PREVIA).equals(statusAtual)
		|| new StatusReenquadramento(StatusReenquadramentoEnum.PENDENCIA_VALIDACAO_DOCUMENTO).equals(statusAtual);
	}
	
	public boolean isRenderedBoleto() {
		return new StatusReenquadramento(StatusReenquadramentoEnum.VALIDADO).equals(statusAtual);
	}
	
	public boolean isRenderedEnvioComprovante() {
		return new StatusReenquadramento(StatusReenquadramentoEnum.BOLETO_PAGAMENTO_LIBERADO).equals(statusAtual)
		|| new StatusReenquadramento(StatusReenquadramentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE).equals(statusAtual);
	}
	
	public boolean isRenderedValidacaoComprovante() {
		//return new StatusReenquadramento(StatusReenquadramentoEnum.COMPORVANTE_ENVIADO).equals(statusAtual);
		return false;
	}
	
	public boolean isRenderedFluxoAlternativo() {
		return new StatusReenquadramento(StatusReenquadramentoEnum.REENQUADRADO).equals(statusAtual);
	}
	
	public ProcessoReenquadramento getProcessoReenquadramento() {
		return processoReenquadramento;
	}

	public void setProcessoReenquadramento(ProcessoReenquadramento processoReenquadramento) {
		this.processoReenquadramento = processoReenquadramento;
	}

	public StatusReenquadramento getStatusAtual() {
		return statusAtual;
	}

	public void setStatusAtual(StatusReenquadramento statusAtual) {
		this.statusAtual = statusAtual;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public Collection<Pessoa> getListaPessoaRequerimento() {
		return listaPessoaRequerimento;
	}

	public void setListaPessoaRequerimento(Collection<Pessoa> listaPessoaRequerimento) {
		this.listaPessoaRequerimento = listaPessoaRequerimento;
	}

	public boolean isVisualizar() {
		return visualizar;
	}

	public void setVisualizar(boolean visualizar) {
		this.visualizar = visualizar;
	}

	public StatusReenquadramentoEnum getStatusTramitado() {
		return statusTramitado;
	}

	public void setStatusTramitado(StatusReenquadramentoEnum statusTramitado) {
		this.statusTramitado = statusTramitado;
	}

}