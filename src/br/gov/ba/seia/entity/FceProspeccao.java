package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;



import br.gov.ba.seia.dto.AquiculturaAtividadeDTO;

@Entity
@Table(name = "fce_prospeccao")
@NamedQueries({
	@NamedQuery(name = "FceProspeccao.remover", query = "DELETE FROM FceProspeccao f WHERE f.ideFceProspeccao = :ideFceProspeccao"),
	@NamedQuery(name = "FceProspeccao.removerByFcePesquisaMineralProspeccao", query = "DELETE FROM FceProspeccao f WHERE f.ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao = :ideFcePesquisaMineralProspeccao")
})

public class FceProspeccao implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "fce_prospeccao_ide_fce_prospeccao_seq", sequenceName = "fce_prospeccao_ide_fce_prospeccao_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_prospeccao_ide_fce_prospeccao_seq")
	@Column(name = "ide_fce_prospeccao", nullable = false, insertable = false, updatable = false)
	private Integer ideFceProspeccao;

	@Length(max=20, message="O campo permite no máximo 20 caracteres")
	@Column(name="seq_prospeccao")
	private String seqProspeccao;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)										
	@JoinColumn(name = "ide_fce_pesquisa_mineral_prospeccao")
	private FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao;
	
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	public FceProspeccao() {
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}

	public Integer getIdeFceProspeccao() {
		return ideFceProspeccao;
	}

	public void setIdeFceProspeccao(Integer ideFceProspeccao) {
		this.ideFceProspeccao = ideFceProspeccao;
	}

	public String getSeqProspeccao() {
		return seqProspeccao;
	}

	public void setSeqProspeccao(String seqProspeccao) {
		this.seqProspeccao = seqProspeccao;
	}

	public FcePesquisaMineralProspeccao getIdeFcePesquisaMineralProspeccao() {
		return ideFcePesquisaMineralProspeccao;
	}

	public void setIdeFcePesquisaMineralProspeccao(	FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao) {
		this.ideFcePesquisaMineralProspeccao = ideFcePesquisaMineralProspeccao;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFcePesquisaMineralProspeccao == null) ? 0 : ideFcePesquisaMineralProspeccao.hashCode());
		result = prime * result + ((ideFceProspeccao == null) ? 0 : ideFceProspeccao.hashCode());
		result = prime * result + ((ideLocalizacaoGeografica == null) ? 0 	: ideLocalizacaoGeografica.hashCode());
		result = prime * result	+ ((seqProspeccao == null) ? 0 : seqProspeccao.hashCode());
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
		FceProspeccao other = (FceProspeccao) obj;
		if (ideFcePesquisaMineralProspeccao == null) {
			if (other.ideFcePesquisaMineralProspeccao != null)
				return false;
		} else if (!ideFcePesquisaMineralProspeccao	.equals(other.ideFcePesquisaMineralProspeccao))
			return false;
		if (ideFceProspeccao == null) {
			if (other.ideFceProspeccao != null)
				return false;
		} else if (!ideFceProspeccao.equals(other.ideFceProspeccao))
			return false;
		if (ideLocalizacaoGeografica == null) {
			if (other.ideLocalizacaoGeografica != null)
				return false;
		} else if (!ideLocalizacaoGeografica.equals(other.ideLocalizacaoGeografica))
			return false;
		if (seqProspeccao == null) {
			if (other.seqProspeccao != null)
				return false;
		} else if (!seqProspeccao.equals(other.seqProspeccao))
			return false;
		return true;
	}

	public FceProspeccao clone() throws CloneNotSupportedException{
		return (FceProspeccao) super.clone();
	}
	
}

