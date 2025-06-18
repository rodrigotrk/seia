package br.gov.ba.seia.middleware.seia.model;
/**
 * Classe modelo para tipo de boleto de pagamento
 * @author 
 *
 */
public class TipoBoletoPagamento {

	private Integer ideTipoBoletoPagamento;

	private String nomTipoBoletoPagamento;

	private Boolean indRequerimento;

	private Boolean indProcesso;

	private Boolean indAtivo;

	public TipoBoletoPagamento() {
		super();
	}

	public Integer getIdeTipoBoletoPagamento() {
		return ideTipoBoletoPagamento;
	}

	public void setIdeTipoBoletoPagamento(Integer ideTipoBoletoPagamento) {
		this.ideTipoBoletoPagamento = ideTipoBoletoPagamento;
	}

	public String getNomTipoBoletoPagamento() {
		return nomTipoBoletoPagamento;
	}

	public void setNomTipoBoletoPagamento(String nomTipoBoletoPagamento) {
		this.nomTipoBoletoPagamento = nomTipoBoletoPagamento;
	}

	public Boolean getIndRequerimento() {
		return indRequerimento;
	}

	public void setIndRequerimento(Boolean indRequerimento) {
		this.indRequerimento = indRequerimento;
	}

	public Boolean getIndProcesso() {
		return indProcesso;
	}

	public void setIndProcesso(Boolean indProcesso) {
		this.indProcesso = indProcesso;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

}