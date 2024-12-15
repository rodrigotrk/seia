package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "comunicacao_perfil")
@NamedQueries({ @NamedQuery(name = "ComunicacaoPerfil.findByComunicacao", query = "SELECT A FROM ComunicacaoPerfil A WHERE A.comunicacaoPerfilPK.ideComunicacao = :ideComunicacao") })
public class ComunicacaoPerfil implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ComunicacaoPerfilPK comunicacaoPerfilPK;
	
	@JoinColumn(name="ide_comunicacao", referencedColumnName="ide_comunicacao", insertable = false, updatable = false)
	@ManyToOne
	private Comunicacao ideComunicacao;
	
	@JoinColumn(name="ide_perfil", referencedColumnName="ide_perfil", insertable = false, updatable = false)
	@ManyToOne
	private Perfil idePerfil;

	public ComunicacaoPerfil() {
		
	}
	
	public ComunicacaoPerfil(Perfil idePerfil) {
		
		this.idePerfil = idePerfil;
	}

	public void gerarPK() {
		comunicacaoPerfilPK = new ComunicacaoPerfilPK(
			ideComunicacao.getId(),
			idePerfil.getIdePerfil()
		);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideComunicacao == null) ? 0 : ideComunicacao.hashCode());
		result = prime * result + ((comunicacaoPerfilPK == null) ? 0
				: comunicacaoPerfilPK.hashCode());
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
		ComunicacaoPerfil other = (ComunicacaoPerfil) obj;
		if (ideComunicacao == null) {
			if (other.ideComunicacao != null)
				return false;
		} else if (!ideComunicacao.equals(other.ideComunicacao)) {
			return false;
		}
		if (comunicacaoPerfilPK == null) {
			if (other.comunicacaoPerfilPK != null)
				return false;
		} else if (!comunicacaoPerfilPK
				.equals(other.comunicacaoPerfilPK)) {
			return false;
		}
		return true;
	}

	public ComunicacaoPerfilPK getComunicacaoPerfilPK() {
		return comunicacaoPerfilPK;
	}

	public void setComunicacaoPerfilPK(ComunicacaoPerfilPK comunicacaoPerfilPK) {
		this.comunicacaoPerfilPK = comunicacaoPerfilPK;
	}

	public Comunicacao getIdeComunicacao() {
		return ideComunicacao;
	}

	public void setIdeComunicacao(Comunicacao ideComunicacao) {
		this.ideComunicacao = ideComunicacao;
	}

	public Perfil getIdePerfil() {
		return idePerfil;
	}

	public void setIdePerfil(Perfil idePerfil) {
		this.idePerfil = idePerfil;
	}

	

}