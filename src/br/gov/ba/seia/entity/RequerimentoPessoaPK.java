package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author alex.santos
 */
@Embeddable
public class RequerimentoPessoaPK implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_requerimento")
	private int ideRequerimento;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_pessoa")
	private int idePessoa;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_pessoa_requerimento")
	private int ideTipoPessoaRequerimento;

    public RequerimentoPessoaPK() {}

    public RequerimentoPessoaPK(int ideRequerimento, int idePessoa, int ideTipoPessoaRequerimento) {
        this.ideRequerimento = ideRequerimento;
        this.idePessoa = idePessoa;
        this.ideTipoPessoaRequerimento = ideTipoPessoaRequerimento;
    }

    public int getIdeRequerimento() {
        return ideRequerimento;
    }

    public void setIdeRequerimento(int ideRequerimento) {
        this.ideRequerimento = ideRequerimento;
    }

    public int getIdePessoa() {
        return idePessoa;
    }

    public void setIdePessoa(int idePessoa) {
        this.idePessoa = idePessoa;
    }

    public int getIdeTipoPessoaRequerimento() {
        return ideTipoPessoaRequerimento;
    }

    public void setIdeTipoPessoaRequerimento(int ideTipoPessoaRequerimento) {
        this.ideTipoPessoaRequerimento = ideTipoPessoaRequerimento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideRequerimento;
        hash += (int) idePessoa;
        hash += (int) ideTipoPessoaRequerimento;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof RequerimentoPessoaPK)) {
            return false;
        }
        RequerimentoPessoaPK other = (RequerimentoPessoaPK) object;
        if (this.ideRequerimento != other.ideRequerimento) {
            return false;
        }
        if (this.idePessoa != other.idePessoa) {
            return false;
        }
        if (this.ideTipoPessoaRequerimento != other.ideTipoPessoaRequerimento) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RequerimentoPessoaPK[ ideRequerimento=" + ideRequerimento + ", idePessoa=" + idePessoa + ", ideTipoPessoaRequerimento=" + ideTipoPessoaRequerimento + " ]";
    }
    
}
