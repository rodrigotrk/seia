package br.gov.ba.seia.dto.identificarPapel;

import br.gov.ba.seia.enumerator.PapelSolicitacaoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

public class IdentificarPapelSolicitacaoDTO implements BaseEntity{

	private int id;
	private boolean value;
	private String nome;
	private PapelSolicitacaoEnum papelSolicitacaoEnum;

	public IdentificarPapelSolicitacaoDTO() {
	}

	public IdentificarPapelSolicitacaoDTO(PapelSolicitacaoEnum papelSolicitacaoEnum) {
		this.papelSolicitacaoEnum = papelSolicitacaoEnum;
		this.id = papelSolicitacaoEnum.getId();
		this.nome = papelSolicitacaoEnum.getNome();
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public PapelSolicitacaoEnum getPapelSolicitacaoEnum() {
		return papelSolicitacaoEnum;
	}

	public void setPapelSolicitacaoEnum(PapelSolicitacaoEnum papelSolicitacaoEnum) {
		this.papelSolicitacaoEnum = papelSolicitacaoEnum;
	}

	public String getNome() {
		return nome;
	}

	public String getDica(){
		return papelSolicitacaoEnum.getDica();
	}

	@Override
	public Long getId() {
		return Long.valueOf(id);
	}




}
