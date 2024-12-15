package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "rel_grupo_perfil_funcionalidade")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "RelGrupoPerfilFuncionalidade.findAll", query = "SELECT r FROM RelGrupoPerfilFuncionalidade r"),
		@NamedQuery(name = "RelGrupoPerfilFuncionalidade.findByIdeFuncionalidade", query = "SELECT r FROM RelGrupoPerfilFuncionalidade r WHERE r.relGrupoPerfilFuncionalidadePK.ideFuncionalidade = :ideFuncionalidade"),
		@NamedQuery(name = "RelGrupoPerfilFuncionalidade.findByIdeAcao", query = "SELECT r FROM RelGrupoPerfilFuncionalidade r WHERE r.relGrupoPerfilFuncionalidadePK.ideAcao = :ideAcao"),
		@NamedQuery(name = "RelGrupoPerfilFuncionalidade.findByIdePerfil", query = "SELECT r FROM RelGrupoPerfilFuncionalidade r WHERE r.relGrupoPerfilFuncionalidadePK.idePerfil = :idePerfil"),
		@NamedQuery(name = "RelGrupoPerfilFuncionalidade.removerRelGrupoPerfilFuncionalidade", query = "delete from RelGrupoPerfilFuncionalidade relGrupoPerfilFuncionalidade where relGrupoPerfilFuncionalidade.perfil.idePerfil = :idePerfil and relGrupoPerfilFuncionalidade.funcionalidade.ideFuncionalidade = :ideFuncionalidade and relGrupoPerfilFuncionalidade.acao.ideAcao = :ideAcao") })
public class RelGrupoPerfilFuncionalidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected RelGrupoPerfilFuncionalidadePK relGrupoPerfilFuncionalidadePK;

	@JoinColumn(name = "ide_perfil", referencedColumnName = "ide_perfil", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Perfil perfil;

	@JoinColumn(name = "ide_funcionalidade", referencedColumnName = "ide_funcionalidade", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Funcionalidade funcionalidade;

	@JoinColumn(name = "ide_acao", referencedColumnName = "ide_acao", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Acao acao;

	public RelGrupoPerfilFuncionalidade() {}

	public RelGrupoPerfilFuncionalidade(Perfil perfil, Funcionalidade funcionalidade, Acao acao) {

		this.perfil = perfil;
		this.funcionalidade = funcionalidade;
		this.acao = acao;
	}

	public RelGrupoPerfilFuncionalidade(RelGrupoPerfilFuncionalidadePK relGrupoPerfilFuncionalidadePK) {

		this.relGrupoPerfilFuncionalidadePK = relGrupoPerfilFuncionalidadePK;
	}

	public RelGrupoPerfilFuncionalidade(int ideFuncionalidade, int ideAcao, int idePerfil) {

		this.relGrupoPerfilFuncionalidadePK = new RelGrupoPerfilFuncionalidadePK(ideFuncionalidade, ideAcao, idePerfil);
	}

	public RelGrupoPerfilFuncionalidadePK getRelGrupoPerfilFuncionalidadePK() {

		return relGrupoPerfilFuncionalidadePK;
	}

	public void setRelGrupoPerfilFuncionalidadePK(RelGrupoPerfilFuncionalidadePK relGrupoPerfilFuncionalidadePK) {

		this.relGrupoPerfilFuncionalidadePK = relGrupoPerfilFuncionalidadePK;
	}

	public Perfil getPerfil() {

		return perfil;
	}

	public void setPerfil(Perfil perfil) {

		this.perfil = perfil;
	}

	public Funcionalidade getFuncionalidade() {

		return funcionalidade;
	}

	public void setFuncionalidade(Funcionalidade funcionalidade) {

		this.funcionalidade = funcionalidade;
	}

	public Acao getAcao() {

		return acao;
	}

	public void setAcao(Acao acao) {

		this.acao = acao;
	}

	@Override
	public int hashCode() {

		int hash = 0;
		hash += (relGrupoPerfilFuncionalidadePK != null ? relGrupoPerfilFuncionalidadePK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {

		
		if (!(object instanceof RelGrupoPerfilFuncionalidade)) {
			return false;
		}
		RelGrupoPerfilFuncionalidade other = (RelGrupoPerfilFuncionalidade) object;
		if ((this.relGrupoPerfilFuncionalidadePK == null && other.relGrupoPerfilFuncionalidadePK != null)
				|| (this.relGrupoPerfilFuncionalidadePK != null && !this.relGrupoPerfilFuncionalidadePK.equals(other.relGrupoPerfilFuncionalidadePK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {

		return String.valueOf(relGrupoPerfilFuncionalidadePK);
	}
}