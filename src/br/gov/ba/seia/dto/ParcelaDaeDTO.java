package br.gov.ba.seia.dto;

import java.text.DecimalFormat;
import java.util.Date;

import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.SituacaoDae;
import br.gov.ba.seia.util.FormaterUtil;
import br.gov.ba.seia.util.Util;


public class ParcelaDaeDTO implements Comparable<ParcelaDaeDTO> {

	private Integer numParcela;
	
	private Double valorAcrescimo;
	
	private Double valorDae;
	
	private SituacaoDae situacaoDae;
	
	private Dae dae;
	
	private Pessoa pessoa;
	
	private EnderecoPessoa enderecoPessoa;
	
	private Date vencimento;
	
	private String urlDae;
	
	private Integer ideMemoriaCalculoDaeParcela;
	
	private boolean indParcelaUnica;
	
	private Date dataDaeVencido;
	private Date dataDaeAnteriorPago;
	
	/* Booleans para visualizaçãod de icons*/
	private Boolean gerarDae;
	private Boolean boleto;
	private Boolean calendario;
	private Boolean pago;
	private Boolean vencido;
	/* *** */

	public Integer getNumParcela() {
		return numParcela;
	}

	public void setNumParcela(Integer numParcela) {
		this.numParcela = numParcela;
	}

	public Double getValorAcrescimo() {
		return valorAcrescimo;
	}

	public void setValorAcrescimo(Double valorAcrescimo) {
		this.valorAcrescimo = valorAcrescimo;
	}

	public Double getValorDae() {
		return valorDae;
	}

	public void setValorDae(Double valorDae) {
		this.valorDae = valorDae;
	}

	public SituacaoDae getSituacaoDae() {
		return situacaoDae;
	}

	public void setSituacaoDae(SituacaoDae situacaoDae) {
		this.situacaoDae = situacaoDae;
	}

	public Dae getDae() {
		return dae;
	}

	public void setDae(Dae dae) {
		this.dae = dae;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public EnderecoPessoa getEnderecoPessoa() {
		return enderecoPessoa;
	}

	public void setEnderecoPessoa(EnderecoPessoa enderecoPessoa) {
		this.enderecoPessoa = enderecoPessoa;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}
	
	public String getUrlDae() {
		return urlDae;
	}

	public void setUrlDae(String urlDae) {
		this.urlDae = urlDae;
	}

	public String getVencimentoFormatado() {
		if (!Util.isNullOuVazio(this.vencimento)) {
			return FormaterUtil.getDataFormatoPtBr(this.vencimento);
		}
		return null;
	}
	
	public String getValorDaeFormatado() {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		return df.format(valorDae);
	}

	public Boolean getGerarDae() {
		return gerarDae;
	}

	public void setGerarDae(Boolean gerarDae) {
		this.gerarDae = gerarDae;
	}

	public Boolean getBoleto() {
		return boleto;
	}

	public void setBoleto(Boolean boleto) {
		this.boleto = boleto;
	}

	public Boolean getCalendario() {
		return calendario;
	}

	public void setCalendario(Boolean calendario) {
		this.calendario = calendario;
	}

	public Boolean getPago() {
		return pago;
	}

	public void setPago(Boolean pago) {
		this.pago = pago;
	}

	public Integer getIdeMemoriaCalculoDaeParcela() {
		return ideMemoriaCalculoDaeParcela;
	}

	public void setIdeMemoriaCalculoDaeParcela(Integer ideMemoriaCalculoDaeParcela) {
		this.ideMemoriaCalculoDaeParcela = ideMemoriaCalculoDaeParcela;
	}

	public boolean isIndParcelaUnica() {
		return indParcelaUnica;
	}

	public void setIndParcelaUnica(boolean indParcelaUnica) {
		this.indParcelaUnica = indParcelaUnica;
	}

	@Override
	public int compareTo(ParcelaDaeDTO o) {
		return this.getIdeMemoriaCalculoDaeParcela().compareTo(o.getIdeMemoriaCalculoDaeParcela());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideMemoriaCalculoDaeParcela == null) ? 0
						: ideMemoriaCalculoDaeParcela.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParcelaDaeDTO other = (ParcelaDaeDTO) obj;
		if (ideMemoriaCalculoDaeParcela == null) {
			if (other.ideMemoriaCalculoDaeParcela != null)
				return false;
		} else if (!ideMemoriaCalculoDaeParcela
				.equals(other.ideMemoriaCalculoDaeParcela))
			return false;
		return true;
	}

	public Date getDataDaeVencido() {
		return dataDaeVencido;
	}

	public void setDataDaeVencido(Date dataDaeVencido) {
		this.dataDaeVencido = dataDaeVencido;
	}

	public Date getDataDaeAnteriorPago() {
		return dataDaeAnteriorPago;
	}

	public void setDataDaeAnteriorPago(Date dataDaeAnteriorPago) {
		this.dataDaeAnteriorPago = dataDaeAnteriorPago;
	}

	public Boolean getVencido() {
		return vencido;
	}

	public void setVencido(Boolean vencido) {
		this.vencido = vencido;
	}
	
}
