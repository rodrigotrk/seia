package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="tipo_projeto")
public class TipoProjeto extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_tipo_projeto")
	private Integer ideTipoProjeto;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_criacao")
	private Date dtcCriacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_exclusao")
	private Date dtcExclusao;

	@Column(name="ind_excluido")
	private Boolean indExcluido;

	@Column(name="nom_tipo_projeto")
	private String nomTipoProjeto;

	@OneToMany(mappedBy="ideTipoProjeto")
	private Collection<ContratoConvenio> contratoConvenioCollection;

	public TipoProjeto() {
	}

	public Integer getIdeTipoProjeto() {
		return ideTipoProjeto;
	}

	public void setIdeTipoProjeto(Integer ideTipoProjeto) {
		this.ideTipoProjeto = ideTipoProjeto;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getNomTipoProjeto() {
		return nomTipoProjeto;
	}

	public void setNomTipoProjeto(String nomTipoProjeto) {
		this.nomTipoProjeto = nomTipoProjeto;
	}

	public Collection<ContratoConvenio> getContratoConvenioCollection() {
		return contratoConvenioCollection;
	}

	public void setContratoConvenioCollection(Collection<ContratoConvenio> contratoConvenioCollection) {
		this.contratoConvenioCollection = contratoConvenioCollection;
	}
}