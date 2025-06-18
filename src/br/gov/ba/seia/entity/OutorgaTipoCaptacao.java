package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * @author Lucas Reis
 **/
@Entity
@Table(name = "outorga_tipo_captacao")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "OutorgaTipoCaptacao.selectDistinctByIdeOutorga", query = "SELECT DISTINCT otc FROM OutorgaTipoCaptacao otc WHERE otc.ideOutorga = :ideOutorga"),
		@NamedQuery(name = "OutorgaTipoCaptacao.deleteByIdeOutorga", query = "DELETE FROM OutorgaTipoCaptacao otc WHERE otc.ideOutorga = :ideOutorga"),
		@NamedQuery(name = "OutorgaTipoCaptacao.deleteByIdeOutorgaTipoCaptacao", query = "DELETE FROM OutorgaTipoCaptacao otc WHERE otc.ideOutorgaTipoCaptacao = :ideOutorgaTipoCaptacao") })
public class OutorgaTipoCaptacao implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outorga_tipo_captacao_ide_outorga_tipo_captacao_seq")
	@SequenceGenerator(name = "outorga_tipo_captacao_ide_outorga_tipo_captacao_seq", sequenceName = "outorga_tipo_captacao_ide_outorga_tipo_captacao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_outorga_tipo_captacao", nullable = false)
	private Integer ideOutorgaTipoCaptacao;

	@JoinColumn(name = "ide_outorga", referencedColumnName = "ide_outorga", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Outorga ideOutorga;

	@JoinColumn(name = "ide_tipo_captacao", referencedColumnName = "ide_tipo_captacao", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoCaptacao ideTipoCaptacao;

	@ManyToMany(mappedBy = "tipoCaptacaoCollection", fetch = FetchType.LAZY)
	private Collection<Outorga> outorgaCollection;

	public OutorgaTipoCaptacao(Outorga ideOutorga, TipoCaptacao ideTipoCaptacao) {
		this.ideOutorga = ideOutorga;
		this.ideTipoCaptacao = ideTipoCaptacao;
	}

	public OutorgaTipoCaptacao() {

	}

	public Integer getIdeOutorgaTipoCaptacao() {
		return ideOutorgaTipoCaptacao;
	}

	public void setIdeOutorgaTipoCaptacao(Integer ideOutorgaTipoCaptacao) {
		this.ideOutorgaTipoCaptacao = ideOutorgaTipoCaptacao;
	}

	public Outorga getIdeOutorga() {
		return ideOutorga;
	}

	public void setIdeOutorga(Outorga ideOutorga) {
		this.ideOutorga = ideOutorga;
	}

	public TipoCaptacao getIdeTipoCaptacao() {
		return ideTipoCaptacao;
	}

	public void setIdeTipoCaptacao(TipoCaptacao ideTipoCaptacao) {
		this.ideTipoCaptacao = ideTipoCaptacao;
	}

	@Override
	public Long getId() {
		return new Long(this.ideOutorgaTipoCaptacao);
	}

	public Collection<Outorga> getOutorgaCollection() {
		return outorgaCollection;
	}

	public void setOutorgaCollection(Collection<Outorga> outorgaCollection) {
		this.outorgaCollection = outorgaCollection;
	}

}
