package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author carlos.sousa
 */
@Embeddable
public class RelGrupoPerfilFuncionalidadePK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_funcionalidade", nullable = false)
    private int ideFuncionalidade;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_acao", nullable = false)
    private int ideAcao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_perfil", nullable = false)
    private int idePerfil;

    public RelGrupoPerfilFuncionalidadePK() {
    }

    public RelGrupoPerfilFuncionalidadePK(int ideFuncionalidade, int ideAcao, int idePerfil) {
        this.ideFuncionalidade = ideFuncionalidade;
        this.ideAcao = ideAcao;
        this.idePerfil = idePerfil;
    }

    public int getIdeFuncionalidade() {
        return ideFuncionalidade;
    }

    public void setIdeFuncionalidade(int ideFuncionalidade) {
        this.ideFuncionalidade = ideFuncionalidade;
    }

    public int getIdeAcao() {
        return ideAcao;
    }

    public void setIdeAcao(int ideAcao) {
        this.ideAcao = ideAcao;
    }

    public int getIdePerfil() {
        return idePerfil;
    }

    public void setIdePerfil(int idePerfil) {
        this.idePerfil = idePerfil;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideFuncionalidade;
        hash += (int) ideAcao;
        hash += (int) idePerfil;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof RelGrupoPerfilFuncionalidadePK)) {
            return false;
        }
        RelGrupoPerfilFuncionalidadePK other = (RelGrupoPerfilFuncionalidadePK) object;
        if (this.ideFuncionalidade != other.ideFuncionalidade) {
            return false;
        }
        if (this.ideAcao != other.ideAcao) {
            return false;
        }
        if (this.idePerfil != other.idePerfil) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RelGrupoPerfilFuncionalidadePK[ ideFuncionalidade=" + ideFuncionalidade + ", ideAcao=" + ideAcao + ", idePerfil=" + idePerfil + " ]";
    }
    
}
