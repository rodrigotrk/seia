package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.util.Util;

public class UsuarioExternoDTO {

	private PessoaFisica pessoaFisica;
	private Usuario usuario;

	public UsuarioExternoDTO() {
		pessoaFisica = new PessoaFisica();
		usuario = new Usuario();
	}
	
	public UsuarioExternoDTO(PessoaFisica pPessoaFisica, Usuario pUsuario) {

		this.pessoaFisica = pPessoaFisica;
		this.usuario = pUsuario;
	}

	public PessoaFisica getPessoaFisica() {

		if (Util.isNull(pessoaFisica)) pessoaFisica = new PessoaFisica();
		return pessoaFisica;
	}
	public void setPessoaFisica(PessoaFisica pessoaFisica) {

		//Preencher tela de usu�rio externo
		if (!Util.isNullOuVazio(pessoaFisica)) {

			setUsuario(pessoaFisica.getUsuario());
		}

		this.pessoaFisica = pessoaFisica;
	}

	public Usuario getUsuario() {

		if (Util.isNull(usuario)) usuario = new Usuario();
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}