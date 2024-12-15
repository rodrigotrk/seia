package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author luis
 */
@Embeddable
public class PostoCombustivelProdutosComercializadosPK implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_posto_combustivel")
    private int idePostoCombustivel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_produto")
    private int ideProduto;

    public PostoCombustivelProdutosComercializadosPK() {
    }

    public PostoCombustivelProdutosComercializadosPK(int idePostoCombustivel, int ideProduto) {
        this.idePostoCombustivel = idePostoCombustivel;
        this.ideProduto = ideProduto;
    }

    public int getIdePostoCombustivel() {
        return idePostoCombustivel;
    }

    public void setIdePostoCombustivel(int idePostoCombustivel) {
        this.idePostoCombustivel = idePostoCombustivel;
    }

    public int getideProduto() {
        return ideProduto;
    }

    public void setideProduto(int ideProduto) {
        this.ideProduto = ideProduto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idePostoCombustivel;
        hash += (int) ideProduto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof PostoCombustivelProdutosComercializadosPK)) {
            return false;
        }
        PostoCombustivelProdutosComercializadosPK other = (PostoCombustivelProdutosComercializadosPK) object;
        if (this.idePostoCombustivel != other.idePostoCombustivel) {
            return false;
        }
        if (this.ideProduto != other.ideProduto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PostoCombustivelProdutosComercializadosPK[ idePostoCombustivel=" + idePostoCombustivel + ", ideProduto=" + ideProduto + " ]";
    }
    
}
