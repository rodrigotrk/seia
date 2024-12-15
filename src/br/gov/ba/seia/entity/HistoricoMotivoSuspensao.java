package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author danilo.santos
 */
@Entity
@Table(name = "historico_motivo_suspensao")
public class HistoricoMotivoSuspensao implements Serializable, Cloneable{
	private static final long serialVersionUID = -2232043090451106298L;
	
	@EmbeddedId
	private HistoricoMotivoSuspensaoPK historicoMotivoSuspensaoPK;
	
    @JoinColumn(name="ide_suspensao_cadastro", referencedColumnName="ide_suspensao_cadastro", insertable = false, updatable = false)
    @OneToOne(fetch=FetchType.EAGER)
    private HistoricoSuspensaoCadastro ideSuspensaoCadastro;
   
    @JoinColumn(name="ide_motivo_suspensao", referencedColumnName="ide_motivo_suspensao", insertable = false, updatable = false)
    @OneToOne(fetch=FetchType.EAGER)
    private MotivoSuspensaoCadastro ideMotivoSuspensaoCadastro;
   
    public HistoricoMotivoSuspensao() {
    	
    }
    
    public HistoricoMotivoSuspensao(HistoricoSuspensaoCadastro ideSuspensaoCadastro, MotivoSuspensaoCadastro ideMotivoSuspensaoCadastro) {
		this.ideSuspensaoCadastro = ideSuspensaoCadastro;
		this.ideMotivoSuspensaoCadastro = ideMotivoSuspensaoCadastro;
		this.historicoMotivoSuspensaoPK = new HistoricoMotivoSuspensaoPK(ideSuspensaoCadastro.getIdeSuspensaoCadastro(), ideMotivoSuspensaoCadastro.getIdeMotivoSuspensaoCadastro());
	}
    
	public HistoricoMotivoSuspensao(HistoricoMotivoSuspensaoPK historicoMotivoSuspensaoPK) {
		this.historicoMotivoSuspensaoPK = historicoMotivoSuspensaoPK;
	}

	public HistoricoMotivoSuspensaoPK getHistoricoMotivoSuspensaoPK() {
		return historicoMotivoSuspensaoPK;
	}

	public void setHistoricoMotivoSuspensaoPK(HistoricoMotivoSuspensaoPK historicoMotivoSuspensaoPK) {
		this.historicoMotivoSuspensaoPK = historicoMotivoSuspensaoPK;
	}

	public HistoricoSuspensaoCadastro getIdeSuspensaoCadastro() {
		return ideSuspensaoCadastro;
	}

	public void setIdeSuspensaoCadastro(HistoricoSuspensaoCadastro ideSuspensaoCadastro) {
		this.ideSuspensaoCadastro = ideSuspensaoCadastro;
	}

	public MotivoSuspensaoCadastro getIdeMotivoSuspensaoCadastro() {
		return ideMotivoSuspensaoCadastro;
	}

	public void setIdeMotivoSuspensaoCadastro(MotivoSuspensaoCadastro ideMotivoSuspensaoCadastro) {
		this.ideMotivoSuspensaoCadastro = ideMotivoSuspensaoCadastro;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((historicoMotivoSuspensaoPK == null) ? 0 : historicoMotivoSuspensaoPK.hashCode());
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
		HistoricoMotivoSuspensao other = (HistoricoMotivoSuspensao) obj;
		if (historicoMotivoSuspensaoPK == null) {
			if (other.historicoMotivoSuspensaoPK != null)
				return false;
		} else if (!historicoMotivoSuspensaoPK.equals(other.historicoMotivoSuspensaoPK))
			return false;
		return true;
	}
    
	@Override
	public HistoricoMotivoSuspensao clone() throws CloneNotSupportedException {
		return (HistoricoMotivoSuspensao) super.clone();
	}
}