package br.gov.ba.ws.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.entity.Usuario;


/**
 * The persistent class for the usuario_dispositivo database table.
 * 
 */
@Entity
@Table(name="usuario_dispositivo")
@NamedQueries({
	@NamedQuery(name = "UsuarioDispositivo.excluirByIdeUsuarioAndCodDispositivo", query = "DELETE FROM UsuarioDispositivo u WHERE u.ideUsuarioDispositivo.usuario.idePessoaFisica= :ideUsuario and u.ideUsuarioDispositivo.codDispositivo = :codDispositivo"),
	@NamedQuery(name = "UsuarioDispositivo.excluirByCodDispositivo", query = "DELETE FROM UsuarioDispositivo u WHERE u.ideUsuarioDispositivo.codDispositivo = :codDispositivo"),
})
public class UsuarioDispositivo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USUARIO_DISPOSITIVO_SEQ_GENERATOR", sequenceName="USUARIO_DISPOSITIVO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USUARIO_DISPOSITIVO_SEQ_GENERATOR")
	@Column(name="ide_usuario_dispositivo")
	private Integer ideUsuarioDispositivo;

	@NotNull
	@JoinColumn(name="ide_pessoa_fisica",referencedColumnName = "ide_pessoa_fisica", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	@NotNull
	@JoinColumn(name="ide_tipo_dispositivo",referencedColumnName = "ide_tipo_dispositivo", nullable = false)
	@OneToOne(fetch = FetchType.EAGER)
	private TipoDispositivo ideTipoDispositivo;

	@NotNull
	@Column(name="cod_dispositivo")
	private String codDispositivo;

	
	public UsuarioDispositivo() {
		}
	
	public UsuarioDispositivo(Usuario usuario,TipoDispositivo ideTipoDispositivo, String codDispositivo) {
		this.usuario = usuario;
		this.ideTipoDispositivo = ideTipoDispositivo;
		this.codDispositivo = codDispositivo;
	}

	public Integer getIdeUsuarioDispositivo() {
		return ideUsuarioDispositivo;
	}

	public void setIdeUsuarioDispositivo(Integer ideUsuarioDispositivo) {
		this.ideUsuarioDispositivo = ideUsuarioDispositivo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public TipoDispositivo getIdeTipoDispositivo() {
		return ideTipoDispositivo;
	}

	public void setIdeTipoDispositivo(TipoDispositivo ideTipoDispositivo) {
		this.ideTipoDispositivo = ideTipoDispositivo;
	}

	public String getCodDispositivo() {
		return codDispositivo;
	}

	public void setCodDispositivo(String codDispositivo) {
		this.codDispositivo = codDispositivo;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codDispositivo == null) ? 0 : codDispositivo.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		UsuarioDispositivo other = (UsuarioDispositivo) obj;
		if (codDispositivo == null) {
			if (other.codDispositivo != null)
				return false;
		} else if (!codDispositivo.equals(other.codDispositivo))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

	
}