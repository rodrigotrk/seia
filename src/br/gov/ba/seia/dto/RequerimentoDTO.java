package br.gov.ba.seia.dto;

import java.io.Serializable;
import java.util.Date;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Lac;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

public class RequerimentoDTO implements Serializable {

	private static final long serialVersionUID = -559305069996133815L;
	
	private Requerimento requerimento;
	private Processo processo;
	private Pessoa pessoa;
	private Empreendimento empreendimento;
	private StatusRequerimento statusRequerimento;
	private Lac lac;
	private Pessoa pessoaTramitacao;
	private boolean indTla;
	private int indiceTela;
	
	private boolean visualizar = false;
	private boolean modoEnquadramento = false;
	private String urlVoltar = "/paginas/novo-requerimento/consulta.xhtml";
	
	private Boolean indDla;
	
	private Municipio municipio;
	
	//O processo deste requerimento está em trâmite de reenquadramento ou foi reenquadrado.
	private boolean processoReenquadrado; 
	
	private ProcessoReenquadramentoDTO processoReenquadramentoDTO;
	
	public RequerimentoDTO() {}

	public RequerimentoDTO(Object[] resultElement) {

		this.requerimento = new Requerimento((Integer) resultElement[0], (String) resultElement[2],
				(Date) resultElement[1]);
		this.requerimento.setIdeOrgao(new Orgao((Integer) resultElement[4], (Integer) resultElement[5]));

		this.empreendimento = new Empreendimento((Integer) resultElement[9], (String) resultElement[10],
				(Boolean) resultElement[11]);

		String nomPessoaFisica = (String) resultElement[8];
		String nomPessoaJuridica = (String) resultElement[7];
		Integer idePessoa = (Integer) resultElement[6];
		if (!Util.isNullOuVazio(nomPessoaFisica))
			this.pessoa = new Pessoa(idePessoa,
					new PessoaFisica(idePessoa, nomPessoaFisica, (String) resultElement[20]));
		else
			this.pessoa = new Pessoa(idePessoa, new PessoaJuridica(idePessoa, nomPessoaJuridica,
					(String) resultElement[20]));

		this.requerimento.setDesEmail((String) resultElement[28]);
		this.requerimento.setNomContato((String) resultElement[29]);
		this.requerimento.setNumTelefone((String) resultElement[30]);

		this.statusRequerimento = (StatusRequerimento) resultElement[12];

		Integer idePessoaTramitacao = (Integer) resultElement[14];
		String nomPessoaFisicaTramitacao = (String) resultElement[15];
		String nomPessoaJuridicaTramitacao = (String) resultElement[16];

		if (!Util.isNullOuVazio(nomPessoaFisicaTramitacao))
			this.pessoaTramitacao = new Pessoa(idePessoaTramitacao, new PessoaFisica(idePessoa,
					nomPessoaFisicaTramitacao));
		else
			this.pessoaTramitacao = new Pessoa(idePessoaTramitacao, new PessoaJuridica(idePessoa,
					nomPessoaJuridicaTramitacao));

		this.lac = new Lac((Integer) resultElement[13]);
		this.lac.setIdeDocumentoObrigatorio(new DocumentoObrigatorio((Integer) resultElement[17]));

		// verificar se precisa do porte this.requerimento.setIdePorte(new
		// Porte((Integer) resultElement[18],(String) resultElement[19]));

		Municipio municipio = null;
		if (!Util.isNull(resultElement[21])) {
			municipio = new Municipio((Integer) resultElement[21], (String) resultElement[22],
					(Boolean) resultElement[23]);
		} else {
			municipio = new Municipio((Integer) resultElement[24], (String) resultElement[25],
					(Boolean) resultElement[26]);
		}

		this.empreendimento.setMunicipio(municipio);

		this.requerimento.setIndEstadoEmergencia((Boolean) resultElement[27]);
		
		this.empreendimento.setIndCessionario((Boolean) resultElement[31]);
		this.indDla = (Boolean) resultElement[32];
		this.requerimento.setIdeEnderecoContato(new Endereco((Integer) resultElement[33]));

	}

	public boolean isPermitidoCancelar() {
		return !isFormado() && !isCompleto() && !isCancelado() && !isDeclaracaoEmitida() && !isIncompleto() && !ContextoUtil.getContexto().isUsuarioExterno();
	}

	public boolean isEnquadramento() {
		return StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento()
				|| StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}
	
	public boolean isPendenciaEnquadramento() {
		return StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}
	
	public boolean isEnvioDocumentacao() {
		return StatusRequerimentoEnum.ENQUADRADO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento()
				|| StatusRequerimentoEnum.PENDENCIA_VALIDACAO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento()
				|| StatusRequerimentoEnum.PENDENCIA_ENVIO_DOCUMENTACAO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}
	
	public boolean isValidacaoPrevia() {
		return StatusRequerimentoEnum.EM_VALIDACAO_PREVIA.getStatus() == this.statusRequerimento.getIdeStatusRequerimento()
				|| StatusRequerimentoEnum.PENDENCIA_VALIDACAO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}
	
	public boolean isBoletoLiberado() {
		return StatusRequerimentoEnum.VALIDADO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}
	
	public boolean isEnvioComprovante() {
		return StatusRequerimentoEnum.PAGAMENTO_LIBERADO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento()
				|| StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}
	
	public boolean isValidacaoComprovante() {
		return StatusRequerimentoEnum.COMPROVANTE_ENVIADO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}
	
	public boolean isCancelado() {
		return StatusRequerimentoEnum.CANCELADO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}
	
	public boolean isCompleto() {
		return StatusRequerimentoEnum.REQUERIMENTO_CONCLUIDO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}
	
	public boolean isFormado() {
		return StatusRequerimentoEnum.FORMADO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}
	
	public boolean isIncompleto() {
		return StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}
	
	public boolean isDeclaracaoEmitida() {
		return StatusRequerimentoEnum.DECLARACAO_EMITIDA.getStatus() == this.statusRequerimento.getIdeStatusRequerimento();
	}

	public boolean isReenquadramento() {
		return !Util.isNullOuVazio(processoReenquadramentoDTO);
	}
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/
	
	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public StatusRequerimento getStatusRequerimento() {
		return statusRequerimento;
	}

	public void setStatusRequerimento(StatusRequerimento statusRequerimento) {
		this.statusRequerimento = statusRequerimento;
	}

	public Lac getLac() {
		return lac;
	}

	public void setLac(Lac lac) {
		this.lac = lac;
	}

	public Pessoa getPessoaTramitacao() {
		return pessoaTramitacao;
	}

	public void setPessoaTramitacao(Pessoa pessoaTramitacao) {
		this.pessoaTramitacao = pessoaTramitacao;
	}

	public String getUrlVoltar() {
		return urlVoltar;
	}

	public void setUrlVoltar(String urlVoltar) {
		this.urlVoltar = urlVoltar;
	}

	public boolean isVisualizar() {
		return visualizar;
	}

	public void setVisualizar(boolean visualizar) {
		this.visualizar = visualizar;
	}

	public boolean isModoEnquadramento() {
		return modoEnquadramento;
	}

	public void setModoEnquadramento(boolean modoEnquadramento) {
		this.modoEnquadramento = modoEnquadramento;
	}

	public Boolean getIndDla() {
		return indDla;
	}

	public void setIndDla(Boolean indDla) {
		this.indDla = indDla;
	}
	
	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public boolean isIndTla() {
		return indTla;
	}

	public void setIndTla(boolean indTla) {
		this.indTla = indTla;
	}
	
	public int getIndiceTela() {
		return indiceTela;
	}

	public void setIndiceTela(int indiceTela) {
		this.indiceTela = indiceTela;
	}
	
	public boolean isProcessoReenquadrado() {
		return processoReenquadrado;
	}
	
	public void setProcessoReenquadrado(boolean processoReenquadrado){
		this.processoReenquadrado = processoReenquadrado;
	}

	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}

	public void setProcessoReenquadramentoDTO(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
}
