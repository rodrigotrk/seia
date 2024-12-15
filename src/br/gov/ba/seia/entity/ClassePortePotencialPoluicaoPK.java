package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author luis
 */
@Embeddable
public class ClassePortePotencialPoluicaoPK implements Serializable {

	private static final long serialVersionUID = -1572962097331811108L;
	
	@Basic(optional = false)
    @Column(name = "ide_classe")
    private int ideClasse;
    
	@Basic(optional = false)
    @Column(name = "ide_porte")
    private int idePorte;
    
	@Basic(optional = false)
    @Column(name = "ide_potencial_poluicao")
    private int idePotencialPoluicao;

    public ClassePortePotencialPoluicaoPK() {
    }

    public ClassePortePotencialPoluicaoPK(int ideClasse, int idePorte, int idePotencialPoluicao) {
        this.ideClasse = ideClasse;
        this.idePorte = idePorte;
        this.idePotencialPoluicao = idePotencialPoluicao;
    }

    public int getIdeClasse() {
        return ideClasse;
    }

    public void setIdeClasse(int ideClasse) {
        this.ideClasse = ideClasse;
    }

    public int getIdePorte() {
        return idePorte;
    }

    public void setIdePorte(int idePorte) {
        this.idePorte = idePorte;
    }

    public int getIdePotencialPoluicao() {
        return idePotencialPoluicao;
    }

    public void setIdePotencialPoluicao(int idePotencialPoluicao) {
        this.idePotencialPoluicao = idePotencialPoluicao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideClasse;
        hash += (int) idePorte;
        hash += (int) idePotencialPoluicao;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ClassePortePotencialPoluicaoPK)) {
            return false;
        }
        ClassePortePotencialPoluicaoPK other = (ClassePortePotencialPoluicaoPK) object;
        if (this.ideClasse != other.ideClasse) {
            return false;
        }
        if (this.idePorte != other.idePorte) {
            return false;
        }
        if (this.idePotencialPoluicao != other.idePotencialPoluicao) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ClassePortePotencialPoluicaoPK[ ideClasse=" + ideClasse + ", idePorte=" + idePorte + ", idePotencialPoluicao=" + idePotencialPoluicao + " ]";
    }
    
}
