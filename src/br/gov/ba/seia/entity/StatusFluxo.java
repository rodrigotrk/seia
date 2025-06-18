package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

/**
 * 
 * @author alex.santos
 */
@Entity
@Table(name = "status_fluxo", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_status_fluxo" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "StatusFluxo.findAll", query = "SELECT s FROM StatusFluxo s"),
		@NamedQuery(name = "StatusFluxo.findByIdeStatusFluxo", query = "SELECT s FROM StatusFluxo s WHERE s.ideStatusFluxo = :ideStatusFluxo"),
		@NamedQuery(name = "StatusFluxo.findByDscStatusFluxo", query = "SELECT s FROM StatusFluxo s WHERE s.dscStatusFluxo = :dscStatusFluxo"),
		@NamedQuery(name = "StatusFluxo.findByIndExcluido", query = "SELECT s FROM StatusFluxo s WHERE s.indExcluido = :indExcluido"),
		@NamedQuery(name = "StatusFluxo.findByDtcCriacao", query = "SELECT s FROM StatusFluxo s WHERE s.dtcCriacao = :dtcCriacao"),
		@NamedQuery(name = "StatusFluxo.findByDtcExclusao", query = "SELECT s FROM StatusFluxo s WHERE s.dtcExclusao = :dtcExclusao") })
public class StatusFluxo implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "STATUS_FLUXO_IDESTATUSFLUXO_GENERATOR", sequenceName = "STATUS_FLUXO_IDE_STATUS_FLUXO_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATUS_FLUXO_IDESTATUSFLUXO_GENERATOR")
	@Column(name = "ide_status_fluxo", unique = true, nullable = false)
	private Integer ideStatusFluxo;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "dsc_status_fluxo", nullable = false, length = 50)
	private String dscStatusFluxo;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;
	@Basic(optional = false)
	@NotNull
	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideStatusFluxo", fetch = FetchType.LAZY)
	private Collection<ControleTramitacao> controleTramitacaoCollection;

	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "dsc_status_fluxo_externo", nullable = false, length = 50)
	private String dscStatusFluxoExterno;

	public StatusFluxo() {
	}

	public StatusFluxo(Integer ideStatusFluxo) {
		this.ideStatusFluxo = ideStatusFluxo;
	}

	public StatusFluxo(Integer ideStatusFluxo, String dscStatusFluxo, boolean indExcluido, Date dtcCriacao) {
		this.ideStatusFluxo = ideStatusFluxo;
		this.dscStatusFluxo = dscStatusFluxo;
		this.indExcluido = indExcluido;
		this.dtcCriacao = dtcCriacao;
	}

	public Integer getIdeStatusFluxo() {
		return ideStatusFluxo;
	}

	public void setIdeStatusFluxo(Integer ideStatusFluxo) {
		this.ideStatusFluxo = ideStatusFluxo;
	}

	public String getDscStatusFluxo() {
		return dscStatusFluxo;
	}

	public void setDscStatusFluxo(String dscStatusFluxo) {
		this.dscStatusFluxo = dscStatusFluxo;
	}

	public boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
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

	@XmlTransient
	public Collection<ControleTramitacao> getControleTramitacaoCollection() {
		return controleTramitacaoCollection;
	}

	public void setControleTramitacaoCollection(Collection<ControleTramitacao> controleTramitacaoCollection) {
		this.controleTramitacaoCollection = controleTramitacaoCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideStatusFluxo != null ? ideStatusFluxo.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof StatusFluxo)) {
			return false;
		}
		StatusFluxo other = (StatusFluxo) object;
		if ((this.ideStatusFluxo == null && other.ideStatusFluxo != null)
				|| (this.ideStatusFluxo != null && !this.ideStatusFluxo.equals(other.ideStatusFluxo))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideStatusFluxo);
	}

	@Override
	public Long getId() {
		return new Long(this.ideStatusFluxo);
	}

	public String getDscStatusFluxoExternoOuInterno() {
		if (Util.isNullOuVazio(dscStatusFluxoExterno))
			return dscStatusFluxo;
		else
			return dscStatusFluxoExterno;
	}
	
	@JSON(include = false)
	public String getDscStatusFluxoExterno() {
		if (Util.isNullOuVazio(dscStatusFluxoExterno))
			return null;
		return dscStatusFluxoExterno;
	}

	public void setDscStatusFluxoExterno(String dscStatusFluxoExterno) {
		this.dscStatusFluxoExterno = dscStatusFluxoExterno;
	}
	
	@JSON(include = false)
   	public boolean isFormado() {
   		return StatusFluxoEnum.FORMADO.getStatus() == this.ideStatusFluxo;
   	}
	
	@JSON(include = false)
   	public boolean isEncaminhadoArea() {
   		return StatusFluxoEnum.ENCAMINHADO_PARA_AREA.getStatus() == this.ideStatusFluxo;
   	}
	
	@JSON(include = false)
   	public boolean isEncaminhadoGestor() {
   		return StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus() == this.ideStatusFluxo;
   	}
	
	@JSON(include = false)
   	public boolean isAprovacaoNotificacao() {
   		return StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO.getStatus() == this.ideStatusFluxo;
   	}
	
	@JSON(include = false)
   	public boolean isNotificacaoRespondida() {
   		return StatusFluxoEnum.NOTIFICACAO_RESPONDIDA.getStatus() == this.ideStatusFluxo;
   	}
	
	
	@JSON(include = false)
   	public boolean isAnalise() {
   		return StatusFluxoEnum.ANALISE_TECNICA.getStatus() == this.ideStatusFluxo;
   	}
	@JSON(include = false)
   	public boolean isConcluido() {
   		return StatusFluxoEnum.CONCLUIDO.getStatus() == this.ideStatusFluxo;
   	}
	@JSON(include = false)
   	public boolean isArquivado() {
   		return StatusFluxoEnum.ARQUIVADO.getStatus() == this.ideStatusFluxo;
   	}
	@JSON(include = false)
   	public boolean isNotificacaoExpedida() {
   		return StatusFluxoEnum.NOTIFICACAO_EXPEDIDA.getStatus() == this.ideStatusFluxo;
   	}

}
