package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "outorga_localizacao_geografica_finalidade")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "OutorgaLocalizacaoGeograficaFinalidade.findAll", query = "SELECT o FROM OutorgaLocalizacaoGeograficaFinalidade o"),
	@NamedQuery(name = "OutorgaLocalizacaoGeograficaFinalidade.removeByideOutargaLocGeo", query = "DELETE FROM OutorgaLocalizacaoGeograficaFinalidade o WHERE o.ideOutorgaLocalizacaoGeografica = :ideOutorgaLocalizacaoGeografica") })
public class OutorgaLocalizacaoGeograficaFinalidade implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outorga_localizacao_geografica_finalidade_ide_outorga_localizacao_geografica_finalidade_generator")
	@SequenceGenerator(name = "outorga_localizacao_geografica_finalidade_ide_outorga_localizacao_geografica_finalidade_generator", sequenceName = "outorga_localizacao_geografica_finalidade_ide_outorga_localizacao_geografica_finalidade_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_outorga_localizacao_geografica_finalidade", nullable = false)
	private Integer ideOutorgaLocalizacaoGeograficaFinalidade;

	@JoinColumn(name = "ide_outorga_localizacao_geografica", referencedColumnName = "ide_outorga_localizacao_geografica", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica;

	@JoinColumn(name = "ide_tipo_finalidade_uso_agua", referencedColumnName = "ide_tipo_finalidade_uso_agua", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;

	@Column(name = "ind_captacao")
	private Boolean indCaptacao;
	
	@Column(name = "ind_excluido")
	private Boolean indExcluido;
	
	
	public OutorgaLocalizacaoGeograficaFinalidade() {

	}

	public OutorgaLocalizacaoGeograficaFinalidade(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua){
		this.ideTipoFinalidadeUsoAgua=tipoFinalidadeUsoAgua;
	}

	public OutorgaLocalizacaoGeograficaFinalidade(OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica,
			TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua, Boolean indCaptacao) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
		this.indCaptacao = indCaptacao;
	}

	public OutorgaLocalizacaoGeograficaFinalidade(OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica,
			TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public OutorgaLocalizacaoGeografica getIdeOutorgaLocalizacaoGeografica() {
		return ideOutorgaLocalizacaoGeografica;
	}

	public void setIdeOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}

	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public Boolean getIndCaptacao() {
		return indCaptacao;
	}

	public void setIndCaptacao(Boolean indCaptacao) {
		this.indCaptacao = indCaptacao;
	}

	public Integer getIdeOutorgaLocalizacaoGeograficaFinalidade() {
		return ideOutorgaLocalizacaoGeograficaFinalidade;
	}

	public void setIdeOutorgaLocalizacaoGeograficaFinalidade(Integer ideOutorgaLocalizacaoGeograficaFinalidade) {
		this.ideOutorgaLocalizacaoGeograficaFinalidade = ideOutorgaLocalizacaoGeograficaFinalidade;
	}
	
	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime
				* result)
				+ ((ideOutorgaLocalizacaoGeograficaFinalidade == null) ? 0 : ideOutorgaLocalizacaoGeograficaFinalidade
						.hashCode());
		result = (prime * result) + ((ideTipoFinalidadeUsoAgua == null) ? 0 : ideTipoFinalidadeUsoAgua.hashCode());
		result = (prime * result) + ((indCaptacao == null) ? 0 : indCaptacao.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		OutorgaLocalizacaoGeograficaFinalidade other = (OutorgaLocalizacaoGeograficaFinalidade) obj;
		if (ideOutorgaLocalizacaoGeograficaFinalidade == null) {
			if (other.ideOutorgaLocalizacaoGeograficaFinalidade != null) {
				return false;
			}
		} else if (!ideOutorgaLocalizacaoGeograficaFinalidade.equals(other.ideOutorgaLocalizacaoGeograficaFinalidade)) {
			return false;
		}
		if (ideTipoFinalidadeUsoAgua == null) {
			if (other.ideTipoFinalidadeUsoAgua != null) {
				return false;
			}
		} else if (!ideTipoFinalidadeUsoAgua.equals(other.ideTipoFinalidadeUsoAgua)) {
			return false;
		}
		if (indCaptacao == null) {
			if (other.indCaptacao != null) {
				return false;
			}
		} else if (!indCaptacao.equals(other.indCaptacao)) {
			return false;
		}
		return true;
	}

}