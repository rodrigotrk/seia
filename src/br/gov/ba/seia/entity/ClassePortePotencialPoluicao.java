package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author luis
 */
@Entity
@Table(name = "classe_porte_potencial_poluicao")
@NamedQueries({
    @NamedQuery(name = "ClassePortePotencialPoluicao.findAll", query = "SELECT c FROM ClassePortePotencialPoluicao c")})
public class ClassePortePotencialPoluicao implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@EmbeddedId
    protected ClassePortePotencialPoluicaoPK classePortePotencialPoluicaoPK;
    
	@Column(name = "ind_ativo")
    private Boolean indAtivo;
    
	@JoinColumn(name = "ide_potencial_poluicao", referencedColumnName = "ide_potencial_poluicao", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PotencialPoluicao potencialPoluicao;
    
	@JoinColumn(name = "ide_porte", referencedColumnName = "ide_porte", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Porte porte;
    
	@JoinColumn(name = "ide_classe", referencedColumnName = "ide_classe", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Classe classe;

    public ClassePortePotencialPoluicao() {
    }

    public ClassePortePotencialPoluicao(ClassePortePotencialPoluicaoPK classePortePotencialPoluicaoPK) {
        this.classePortePotencialPoluicaoPK = classePortePotencialPoluicaoPK;
    }

    public ClassePortePotencialPoluicao(int ideClasse, int idePorte, int idePotencialPoluicao) {
        this.classePortePotencialPoluicaoPK = new ClassePortePotencialPoluicaoPK(ideClasse, idePorte, idePotencialPoluicao);
    }

    public ClassePortePotencialPoluicaoPK getClassePortePotencialPoluicaoPK() {
        return classePortePotencialPoluicaoPK;
    }

    public void setClassePortePotencialPoluicaoPK(ClassePortePotencialPoluicaoPK classePortePotencialPoluicaoPK) {
        this.classePortePotencialPoluicaoPK = classePortePotencialPoluicaoPK;
    }

    public Boolean getIndAtivo() {
        return indAtivo;
    }

    public void setIndAtivo(Boolean indAtivo) {
        this.indAtivo = indAtivo;
    }

    public PotencialPoluicao getPotencialPoluicao() {
        return potencialPoluicao;
    }

    public void setPotencialPoluicao(PotencialPoluicao potencialPoluicao) {
        this.potencialPoluicao = potencialPoluicao;
    }

    public Porte getPorte() {
        return porte;
    }

    public void setPorte(Porte porte) {
        this.porte = porte;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (classePortePotencialPoluicaoPK != null ? classePortePotencialPoluicaoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ClassePortePotencialPoluicao)) {
            return false;
        }
        ClassePortePotencialPoluicao other = (ClassePortePotencialPoluicao) object;
        if ((this.classePortePotencialPoluicaoPK == null && other.classePortePotencialPoluicaoPK != null) || (this.classePortePotencialPoluicaoPK != null && !this.classePortePotencialPoluicaoPK.equals(other.classePortePotencialPoluicaoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ClassePortePotencialPoluicao[ classePortePotencialPoluicaoPK=" + classePortePotencialPoluicaoPK + " ]";
    }
    
}
