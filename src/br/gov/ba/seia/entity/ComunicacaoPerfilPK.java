package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ComunicacaoPerfilPK  implements Serializable {
	
	private static final long serialVersionUID = 1L;


	

	@Column(name="ide_comunicacao", nullable=false)
	private Integer ideComunicacao;
	
	@Column(name="ide_perfil", nullable=false)
	private Integer idePerfil;
	
	public ComunicacaoPerfilPK() {
		
	}
	
	public ComunicacaoPerfilPK(Integer ideComunicacao, Integer idePerfil) {
		super();
		this.ideComunicacao = ideComunicacao;
		this.idePerfil = idePerfil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideComunicacao == null) ? 0 : ideComunicacao.hashCode());
		result = prime * result + ((idePerfil == null) ? 0 : idePerfil.hashCode());
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
		ComunicacaoPerfilPK other = (ComunicacaoPerfilPK) obj;
		if (ideComunicacao == null) {
			if (other.ideComunicacao != null)
				return false;
		} else if (!ideComunicacao.equals(other.ideComunicacao)) {
			return false;
		}
		if (idePerfil == null) {
			if (other.idePerfil != null)
				return false;
		} else if (!idePerfil.equals(other.idePerfil)) {
			return false;
		}
		return true;
	}

	public Integer getIdeComunicacao() {
		return ideComunicacao;
	}

	public void setIdeComunicacao(Integer ideComunicacao) {
		this.ideComunicacao = ideComunicacao;
	}

	public Integer getIdePerfil() {
		return idePerfil;
	}

	public void setIdePerfil(Integer idePerfil) {
		this.idePerfil = idePerfil;
	}
	
	
}
