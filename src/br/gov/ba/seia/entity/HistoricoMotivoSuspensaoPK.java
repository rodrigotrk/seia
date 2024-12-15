package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author danilo.santos
 */
@Embeddable
public class HistoricoMotivoSuspensaoPK implements Serializable {

	private static final long serialVersionUID = 2227860717206995905L;

	@Column(name="ide_suspensao_cadastro", nullable=false)
	private Integer ideSuspensaoCadastro;

	@Column(name="ide_motivo_suspensao", nullable=false)
	private Integer ideMotivoSuspensaoCadastro;

	public HistoricoMotivoSuspensaoPK() {
	
	}

	public HistoricoMotivoSuspensaoPK(Integer ideSuspensaoCadastro, Integer ideMotivoSuspensaoCadastro) {
		this.ideSuspensaoCadastro = ideSuspensaoCadastro;
		this.ideMotivoSuspensaoCadastro = ideMotivoSuspensaoCadastro;
	}

	public Integer getIdeSuspensaoCadastro() {
		return ideSuspensaoCadastro;
	}

	public void setIdeSuspensaoCadastro(Integer ideSuspensaoCadastro) {
		this.ideSuspensaoCadastro = ideSuspensaoCadastro;
	}

	public Integer getIdeEnquadramentoAtoAmbiental() {
		return ideMotivoSuspensaoCadastro;
	}

	public void setIdeEnquadramentoAtoAmbiental(Integer ideMotivoSuspensaoCadastro) {
		this.ideMotivoSuspensaoCadastro = ideMotivoSuspensaoCadastro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideSuspensaoCadastro == null) ? 0 : ideSuspensaoCadastro.hashCode());
		result = prime * result + ((ideMotivoSuspensaoCadastro == null) ? 0 : ideMotivoSuspensaoCadastro.hashCode());
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
		HistoricoMotivoSuspensaoPK other = (HistoricoMotivoSuspensaoPK) obj;
		if (ideSuspensaoCadastro == null) {
			if (other.ideSuspensaoCadastro != null)
				return false;
		} else if (!ideSuspensaoCadastro.equals(other.ideSuspensaoCadastro))
			return false;
		if (ideMotivoSuspensaoCadastro == null) {
			if (other.ideMotivoSuspensaoCadastro != null)
				return false;
		} else if (!ideMotivoSuspensaoCadastro.equals(other.ideMotivoSuspensaoCadastro))
			return false;
		return true;
	}
}