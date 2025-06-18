package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;

/**
 * 
 * @author eduardo.fernandes
 * 
 */

@Entity
@Table(name = "tipo_solicitacao")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoSolicitacao.findAll", query = "SELECT t FROM TipoSolicitacao t"),
		@NamedQuery(name = "TipoSolicitacao.findByIdeTipoSolicitacao", query = "SELECT t FROM TipoSolicitacao t WHERE t.ideTipoSolicitacao = :ideTipoSolicitacao"),
		@NamedQuery(name = "TipoSolicitacao.findByNomTipoSolicitacao", query = "SELECT t FROM TipoSolicitacao t WHERE t.nomTipoSolicitacao = :nomTipoSolicitacao") })
public class TipoSolicitacao extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_solicitacao", nullable = false)
	private Integer ideTipoSolicitacao;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "nom_tipo_solicitacao", nullable = false, length = 40)
	private String nomTipoSolicitacao;

	public TipoSolicitacao(Integer ideTipoSolicitacao, String nomTipoSolicitacao) {
		this.ideTipoSolicitacao = ideTipoSolicitacao;
		this.nomTipoSolicitacao = nomTipoSolicitacao;
	}

	public TipoSolicitacao() {
	}

	public TipoSolicitacao(Integer ideTipoSolicitacao) {
		this.ideTipoSolicitacao = ideTipoSolicitacao;
	}

	public Integer getIdeTipoSolicitacao() {
		return ideTipoSolicitacao;
	}

	public void setIdeTipoSolicitacao(Integer ideTipoSolicitacao) {
		this.ideTipoSolicitacao = ideTipoSolicitacao;
	}

	public String getNomTipoSolicitacao() {
		return nomTipoSolicitacao;
	}

	public void setNomTipoSolicitacao(String nomTipoSolicitacao) {
		this.nomTipoSolicitacao = nomTipoSolicitacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTipoSolicitacao == null) ? 0 : ideTipoSolicitacao.hashCode());
		result = prime * result + ((nomTipoSolicitacao == null) ? 0 : nomTipoSolicitacao.hashCode());
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
		TipoSolicitacao other = (TipoSolicitacao) obj;
		if (ideTipoSolicitacao == null) {
			if (other.ideTipoSolicitacao != null)
				return false;
		} else if (!ideTipoSolicitacao.equals(other.ideTipoSolicitacao))
			return false;
		if (nomTipoSolicitacao == null) {
			if (other.nomTipoSolicitacao != null)
				return false;
		} else if (!nomTipoSolicitacao.equals(other.nomTipoSolicitacao))
			return false;
		return true;
	}
	
	public boolean isAlteracaoLicenca() {
		return TipoSolicitacaoEnum.ALTERACAO_LICENCA.getId().equals(ideTipoSolicitacao);
	}
	
	public boolean isRenovacaoLicenca() {
		return TipoSolicitacaoEnum.RENOVACAO_LICENCA.getId().equals(ideTipoSolicitacao);
	}
	
	public boolean isTla() {
		return TipoSolicitacaoEnum.TRANSFERENCIA_LICENCA_AMBIENTAL.getId().equals(ideTipoSolicitacao);
	}
}