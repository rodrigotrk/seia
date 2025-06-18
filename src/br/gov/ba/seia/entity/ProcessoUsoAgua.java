package br.gov.ba.seia.entity;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Basic;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "processo_uso_agua")
@XmlRootElement
@NamedQueries({
@NamedQuery(name = "ProcessoUsoAgua.findAll", query = "SELECT a FROM ProcessoUsoAgua a where a.indExcluido=false")})
public class ProcessoUsoAgua implements Serializable {

	private static final long serialVersionUID = -9166387459387681606L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "processo_uso_agua_ide_processo_uso_agua_seq")
	@SequenceGenerator(name = "processo_uso_agua_ide_processo_uso_agua_seq", sequenceName = "processo_uso_agua_ide_processo_uso_agua_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_processo_uso_agua", nullable = false)
	private Integer ideProcessoUsoAgua;

	@JoinColumn(name = "ide_imovel_rural_uso_agua", referencedColumnName = "ide_imovel_rural_uso_agua", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private ImovelRuralUsoAgua ideImovelRuralUsoAgua;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "num_processo", nullable = false, length = 40)
	private String numeroProcesso;
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
	
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;

	public Integer getIdeProcessoUsoAgua() {
		return ideProcessoUsoAgua;
	}

	public void setIdeProcessoUsoAgua(Integer ideProcessoUsoAgua) {
		this.ideProcessoUsoAgua = ideProcessoUsoAgua;
	}

	public ImovelRuralUsoAgua getIdeImovelRuralUsoAgua() {
		return ideImovelRuralUsoAgua;
	}

	public void setIdeImovelRuralUsoAgua(
			ImovelRuralUsoAgua ideImovelRuralUsoAgua) {
		this.ideImovelRuralUsoAgua = ideImovelRuralUsoAgua;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideProcessoUsoAgua != null ? ideProcessoUsoAgua.hashCode() : 0);
        return hash;
    }
	
	@Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ProcessoUsoAgua)) {
            return false;
        }
        ProcessoUsoAgua other = (ProcessoUsoAgua) object;
        if ((this.ideProcessoUsoAgua == null && other.ideProcessoUsoAgua != null) || (this.ideProcessoUsoAgua != null && !this.ideProcessoUsoAgua.equals(other.ideProcessoUsoAgua))) {
            return false;
        }
        return true;
    }


}
