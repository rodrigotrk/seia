package br.gov.ba.seia.dto;

import br.gov.ba.seia.enumerator.TipoPessoaEnum;


public class TipoPessoaDTO {
	
	private TipoPessoaEnum fisica;
	private TipoPessoaEnum juridica;
	private boolean isUsuarioConvenio;
	
	private TipoPessoaEnum tipoPessoaEnum;
	private boolean desabilitar;
	
	public TipoPessoaDTO() {
		fisica = TipoPessoaEnum.FISICA;
		juridica = TipoPessoaEnum.JURIDICA;
	}

	public TipoPessoaEnum getTipoPessoaEnum() {
		return tipoPessoaEnum;
	}
	
	public void setTipoPessoaEnum(TipoPessoaEnum tipoPessoaEnum) {
		this.tipoPessoaEnum = tipoPessoaEnum;
	}
	
	public boolean isDesabilitar() {
		return desabilitar;
	}
	
	public void setDesabilitar(boolean desabilitar) {
		this.desabilitar = desabilitar;
	}

	public TipoPessoaEnum getFisica() {
		return fisica;
	}

	public void setFisica(TipoPessoaEnum fisica) {
		this.fisica = fisica;
	}

	public TipoPessoaEnum getJuridica() {
		return juridica;
	}

	public void setJuridica(TipoPessoaEnum juridica) {
		this.juridica = juridica;
	}

	public boolean isUsuarioConvenio() {
		return isUsuarioConvenio;
	}

	public void setUsuarioConvenio(boolean isUsuarioConvenio) {
		this.isUsuarioConvenio = isUsuarioConvenio;
	}

}
