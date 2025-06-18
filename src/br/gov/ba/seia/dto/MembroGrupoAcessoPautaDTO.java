package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.Funcionario;

public class MembroGrupoAcessoPautaDTO {
	
	private Funcionario funcionario;	
	private String dscPerfil;
	private String dscIndSubstituto;
	
	public MembroGrupoAcessoPautaDTO(){
		
	}
	
	public MembroGrupoAcessoPautaDTO(Funcionario funcionario,String dscPerfil,String dscIndSubstituto) {
		this.setFuncionario(funcionario);	
		this.dscPerfil=dscPerfil;
		this.dscIndSubstituto=dscIndSubstituto;
	}
	
	
	public String getDscPerfil() {
		return dscPerfil;
	}
	
	public void setDscPerfil(String dscPerfil) {
		this.dscPerfil = dscPerfil;
	}
	
	public String getDscIndSubstituto() {
		return dscIndSubstituto;
	}
	
	public void setDscIndSubstituto(String dscIndSubstituto) {
		this.dscIndSubstituto = dscIndSubstituto;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

}
