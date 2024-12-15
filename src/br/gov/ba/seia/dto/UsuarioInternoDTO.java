package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.util.Util;

public class UsuarioInternoDTO {

	private PessoaFisica pessoaFisica;
	private Funcionario funcionario;
	private Usuario usuario;

	public UsuarioInternoDTO() {
	}

	public UsuarioInternoDTO(PessoaFisica pPessoaFisica, Funcionario pFuncionario, Usuario pUsuario) {
		this.pessoaFisica = pPessoaFisica;
		this.funcionario = pFuncionario;
		this.usuario = pUsuario;
	}

	public PessoaFisica getPessoaFisica() {
		if (Util.isNull(pessoaFisica))
			pessoaFisica = new PessoaFisica();
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		if (!Util.isNullOuVazio(pessoaFisica)) {
			setFuncionario(pessoaFisica.getFuncionario());
			setUsuario(pessoaFisica.getUsuario());
		}
		this.pessoaFisica = pessoaFisica;
	}

	public Funcionario getFuncionario() {
		if (Util.isNull(funcionario))
			funcionario = new Funcionario();
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Usuario getUsuario() {
		if (Util.isNull(usuario))
			usuario = new Usuario();
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}