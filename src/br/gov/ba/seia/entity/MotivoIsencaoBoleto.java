package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "motivo_isencao_boleto")
@NamedQueries({@NamedQuery(name = "MotivoIsencaoBoleto.findAll", query = "SELECT m FROM MotivoIsencaoBoleto m")})
public class MotivoIsencaoBoleto extends AbstractEntity {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_motivo_isencao_boleto")
    private Integer ideMotivoIsencaoBoleto;
    
	@Basic(optional = false)
    @Column(name = "dsc_motivo_isencao_boleto")
    private String dscMotivoIsencaoBoleto;
    
	@Basic(optional = false)
    @Column(name = "ind_ativo")
    private boolean indAtivo;
	
	@Basic(optional = false)
	@Column(name = "ind_requerimento")
	private boolean indRequerimento;
	
	@Basic(optional = false)
	@Column(name = "ind_reenquadramento")
	private boolean indReenquadramento;
    
	@OneToMany(mappedBy = "ideMotivoIsencaoBoleto")
    private Collection<BoletoPagamentoRequerimento> boletoPagamentoRequerimentoCollection;

    public MotivoIsencaoBoleto() {}

    public MotivoIsencaoBoleto(Integer ideMotivoIsencaoBoleto) {
        this.ideMotivoIsencaoBoleto = ideMotivoIsencaoBoleto;
    }

    public MotivoIsencaoBoleto(Integer ideMotivoIsencaoBoleto, String dscMotivoIsencaoBoleto, boolean indAtivo) {
        this.ideMotivoIsencaoBoleto = ideMotivoIsencaoBoleto;
        this.dscMotivoIsencaoBoleto = dscMotivoIsencaoBoleto;
        this.indAtivo = indAtivo;
    }

    public Integer getIdeMotivoIsencaoBoleto() {
        return ideMotivoIsencaoBoleto;
    }

    public void setIdeMotivoIsencaoBoleto(Integer ideMotivoIsencaoBoleto) {
        this.ideMotivoIsencaoBoleto = ideMotivoIsencaoBoleto;
    }

    public String getDscMotivoIsencaoBoleto() {
        return dscMotivoIsencaoBoleto;
    }

    public void setDscMotivoIsencaoBoleto(String dscMotivoIsencaoBoleto) {
        this.dscMotivoIsencaoBoleto = dscMotivoIsencaoBoleto;
    }

    public boolean getIndAtivo() {
        return indAtivo;
    }

    public void setIndAtivo(boolean indAtivo) {
        this.indAtivo = indAtivo;
    }

    public Collection<BoletoPagamentoRequerimento> getBoletoPagamentoRequerimentoCollection() {
        return boletoPagamentoRequerimentoCollection;
    }

    public void setBoletoPagamentoRequerimentoCollection(Collection<BoletoPagamentoRequerimento> boletoPagamentoRequerimentoCollection) {
        this.boletoPagamentoRequerimentoCollection = boletoPagamentoRequerimentoCollection;
    }

	public boolean isIndRequerimento() {
		return indRequerimento;
	}

	public void setIndRequerimento(boolean indRequerimento) {
		this.indRequerimento = indRequerimento;
	}

	public boolean isIndReenquadramento() {
		return indReenquadramento;
	}

	public void setIndReenquadramento(boolean indReenquadramento) {
		this.indReenquadramento = indReenquadramento;
	}
}
