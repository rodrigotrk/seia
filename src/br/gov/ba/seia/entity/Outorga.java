package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.util.Util;

/**
 * 
 * @author Lucas Reis
 * 
 **/
@Entity
@Table(name = "outorga")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Outorga.findAll", query = "SELECT o FROM Outorga o"),
	@NamedQuery(name = "Outorga.excluirByIdeOutorga", query = "DELETE FROM Outorga o WHERE o.ideOutorga = :ideOutorga"),
	@NamedQuery(name = "Outorga.findByIdeOutorga", query = "SELECT o FROM Outorga o WHERE o.ideOutorga = :ideOutorga"),
	@NamedQuery(name = "Outorga.findByNumProcessoOutorga", query = "SELECT o FROM Outorga o WHERE o.numProcessoOutorga = :numProcessoOutorga"),
	@NamedQuery(name = "Outorga.findByNumPortariaOutorga", query = "SELECT o FROM Outorga o WHERE o.numPortariaOutorga = :numPortariaOutorga"),
	@NamedQuery(name = "Outorga.findByDtcPublicacaoPortaria", query = "SELECT o FROM Outorga o WHERE o.dtcPublicacaoPortaria = :dtcPublicacaoPortaria"),
	@NamedQuery(name = "Outorga.findByDtcValidadeOutorga", query = "SELECT o FROM Outorga o WHERE o.dtcValidadeOutorga = :dtcValidadeOutorga"),
	@NamedQuery(name = "Outorga.findByIdeTipoSolicitacao", query = "SELECT o FROM Outorga o WHERE o.ideTipoSolicitacao = :ideTipoSolicitacao"),
	@NamedQuery(name = "Outorga.findByIdeRequerimento", query = "SELECT o FROM Outorga o WHERE o.ideRequerimento = :ideRequerimento"),
	@NamedQuery(name = "Outorga.findByIdeModalidadeOutorga", query = "SELECT o FROM Outorga o WHERE o.ideModalidadeOutorga = :ideModalidadeOutorga"), })
public class Outorga implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outorga_ide_outorga_generator")
	@SequenceGenerator(name = "outorga_ide_outorga_generator", sequenceName = "outorga_ide_outorga_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_outorga", nullable = false)
	private Integer ideOutorga;

	@Basic(optional = false)
	@Column(name = "num_processo_outorga", nullable = true, length = 50)
	private String numProcessoOutorga;

	@Basic(optional = false)
	@Column(name = "num_portaria_outorga", nullable = true, length = 50)
	private String numPortariaOutorga;

	@Basic(optional = false)
	@Column(name = "dtc_publicacao_portaria", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcPublicacaoPortaria;

	@Basic(optional = false)
	@Column(name = "dtc_validade_outorga", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidadeOutorga;

	@Column(name = "valor_volume_caminhao", precision = 10, scale = 2, nullable = true)
	private BigDecimal valorVolumeCaminhaoPipa;

	@Column(name = "qtd_transporte_caminhao", nullable = true)
	private Integer qtdTransporteCaminhaoPipa;

	@JoinColumn(name = "ide_tipo_solicitacao", referencedColumnName = "ide_tipo_solicitacao", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoSolicitacao ideTipoSolicitacao;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;

	@JoinColumn(name = "ide_modalidade_outorga", referencedColumnName = "ide_modalidade_outorga", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ModalidadeOutorga ideModalidadeOutorga;

	@JoinColumn(name = "ide_tipo_alteracao", referencedColumnName = "ide_tipo_alteracao", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoAlteracao ideTipoAlteracao;

	@JoinTable(name = "outorga_tipo_captacao", joinColumns = { @JoinColumn(name = "ide_outorga", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_tipo_captacao", nullable = false, updatable = false) })
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private Collection<TipoCaptacao> tipoCaptacaoCollection;

	@OneToMany(mappedBy = "ideOutorga", fetch = FetchType.LAZY)
	private Collection<OutorgaLocalizacaoGeografica> outorgaLocalizacaoGeograficaCollection;

	@Transient
	private TipoCaptacao tipoCaptacao;

	public Outorga() {
	}

	public Outorga(String numProcessoOutorga, String numPortariaOutorga, Date dtcPublicacaoPortaria,
			Date dtcValidadeOutorga, TipoSolicitacao ideTipoSolicitacao, ModalidadeOutorga ideModalidadeOutorga) {
		this.numProcessoOutorga = numProcessoOutorga;
		this.numPortariaOutorga = numPortariaOutorga;
		this.dtcPublicacaoPortaria = dtcPublicacaoPortaria;
		this.ideTipoSolicitacao = ideTipoSolicitacao;
		this.ideModalidadeOutorga = ideModalidadeOutorga;
		this.dtcValidadeOutorga = dtcValidadeOutorga;
	}

	public Outorga(Requerimento ideRequerimento) {
		super();
		this.ideRequerimento = ideRequerimento;
	}

	public Outorga(TipoSolicitacao ideTipoSolicitacao, Requerimento ideRequerimento, ModalidadeOutorga modalidadeOutorga) {
		this.ideTipoSolicitacao = ideTipoSolicitacao;
		this.ideRequerimento = ideRequerimento;
		this.ideModalidadeOutorga = modalidadeOutorga;
	}

	public Outorga(Integer ideOutorga) {
		this.ideOutorga = ideOutorga;
	}

	public Integer getIdeOutorga() {
		return ideOutorga;
	}

	public void setIdeOutorga(Integer ideOutorga) {
		this.ideOutorga = ideOutorga;
	}

	public String getNumProcessoOutorga() {
		return numProcessoOutorga;
	}

	public void setNumProcessoOutorga(String numProcessoOutorga) {
		this.numProcessoOutorga = numProcessoOutorga;
	}

	public String getNumPortariaOutorga() {
		return numPortariaOutorga;
	}

	public void setNumPortariaOutorga(String numPortariaOutorga) {
		this.numPortariaOutorga = numPortariaOutorga;
	}

	public Date getDtcPublicacaoPortaria() {
		return dtcPublicacaoPortaria;
	}
	
	public void setDtcPublicacaoPortaria(Date dtcPublicacaoPortaria) {
		this.dtcPublicacaoPortaria = dtcPublicacaoPortaria;
	}

	public Date getDtcValidadeOutorga() {
		return dtcValidadeOutorga;
	}
	
	public String getDtcValidadeOutorgaFormatado() {
		if(!Util.isNullOuVazio(dtcValidadeOutorga)) {
			SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");  
			return spf.format(dtcValidadeOutorga);
		} else {
			return "-";
		}
	}

	public void setDtcValidadeOutorga(Date dtcValidadeOutorga) {
		this.dtcValidadeOutorga = dtcValidadeOutorga;
	}

	public Integer getQtdTransporteCaminhaoPipa() {
		return qtdTransporteCaminhaoPipa;
	}

	public void setQtdTransporteCaminhaoPipa(Integer qtdTransporteCaminhaoPipa) {
		this.qtdTransporteCaminhaoPipa = qtdTransporteCaminhaoPipa;
	}

	public BigDecimal getValorVolumeCaminhaoPipa() {
		return valorVolumeCaminhaoPipa;
	}

	public void setValorVolumeCaminhaoPipa(BigDecimal valorVolumeCaminhaoPipa) {
		this.valorVolumeCaminhaoPipa = valorVolumeCaminhaoPipa;
	}

	public TipoSolicitacao getIdeTipoSolicitacao() {
		return ideTipoSolicitacao;
	}

	public void setIdeTipoSolicitacao(TipoSolicitacao ideTipoSolicitacao) {
		this.ideTipoSolicitacao = ideTipoSolicitacao;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public ModalidadeOutorga getIdeModalidadeOutorga() {
		return ideModalidadeOutorga;
	}

	public void setIdeModalidadeOutorga(ModalidadeOutorga ideModalidadeOutorga) {
		this.ideModalidadeOutorga = ideModalidadeOutorga;
	}


	public TipoAlteracao getIdeTipoAlteracao() {
		return ideTipoAlteracao;
	}

	public void setIdeTipoAlteracao(TipoAlteracao ideTipoAlteracao) {
		this.ideTipoAlteracao = ideTipoAlteracao;
	}

	public Collection<TipoCaptacao> getTipoCaptacaoCollection() {
		return tipoCaptacaoCollection;
	}

	public void setTipoCaptacaoCollection(Collection<TipoCaptacao> tipoCaptacaoCollection) {
		this.tipoCaptacaoCollection = tipoCaptacaoCollection;
	}

	public TipoCaptacao getTipoCaptacao() {
		return tipoCaptacao;
	}
	
	public Boolean getIntervencao() {
		if(this.ideModalidadeOutorga != null)
			return this.ideModalidadeOutorga.getIdeModalidadeOutorga().equals(ModalidadeOutorgaEnum.INTERVENCAO.getIdModalidade());
		else
			return false;
	}
	
	public Boolean getAproveitHidrico() {
		if(this.ideModalidadeOutorga != null)
			return this.ideModalidadeOutorga.getIdeModalidadeOutorga().equals(ModalidadeOutorgaEnum.POTENCIAL_HIDRELETRICO.getIdModalidade());
		else
			return false;
	}

	public void setTipoCaptacao(TipoCaptacao tipoCaptacao) {
		this.tipoCaptacao = tipoCaptacao;
	}

	public Collection<OutorgaLocalizacaoGeografica> getOutorgaLocalizacaoGeograficaCollection() {
		return Util.isLazyInitExcepOuNull(outorgaLocalizacaoGeograficaCollection) ? outorgaLocalizacaoGeograficaCollection = new ArrayList<OutorgaLocalizacaoGeografica>() : outorgaLocalizacaoGeograficaCollection;
	}

	public void setOutorgaLocalizacaoGeograficaCollection(Collection<OutorgaLocalizacaoGeografica> outorgaLocalizacaoGeograficaCollection) {
		this.outorgaLocalizacaoGeograficaCollection = outorgaLocalizacaoGeograficaCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtcPublicacaoPortaria == null) ? 0 : dtcPublicacaoPortaria.hashCode());
		result = prime * result + ((dtcValidadeOutorga == null) ? 0 : dtcValidadeOutorga.hashCode());
		result = prime * result + ((ideModalidadeOutorga == null) ? 0 : ideModalidadeOutorga.hashCode());
		result = prime * result + ideOutorga;
		result = prime * result + ((ideRequerimento == null) ? 0 : ideRequerimento.hashCode());
		result = prime * result + ((ideTipoSolicitacao == null) ? 0 : ideTipoSolicitacao.hashCode());
		result = prime * result + ((numPortariaOutorga == null) ? 0 : numPortariaOutorga.hashCode());
		result = prime * result + ((numProcessoOutorga == null) ? 0 : numProcessoOutorga.hashCode());
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
		Outorga other = (Outorga) obj;
		if (dtcPublicacaoPortaria == null) {
			if (other.dtcPublicacaoPortaria != null) {
				return false;
			}
		} else if (!dtcPublicacaoPortaria.equals(other.dtcPublicacaoPortaria)) {
			return false;
		}
		if (dtcValidadeOutorga == null) {
			if (other.dtcValidadeOutorga != null) {
				return false;
			}
		} else if (!dtcValidadeOutorga.equals(other.dtcValidadeOutorga)) {
			return false;
		}
		if (ideModalidadeOutorga == null) {
			if (other.ideModalidadeOutorga != null) {
				return false;
			}
		} else if (!ideModalidadeOutorga.equals(other.ideModalidadeOutorga)) {
			return false;
		}
		if (ideOutorga != other.ideOutorga) {
			return false;
		}
		if (ideRequerimento == null) {
			if (other.ideRequerimento != null) {
				return false;
			}
		} else if (!ideRequerimento.equals(other.ideRequerimento)) {
			return false;
		}
		if (ideTipoSolicitacao == null) {
			if (other.ideTipoSolicitacao != null) {
				return false;
			}
		} else if (!ideTipoSolicitacao.equals(other.ideTipoSolicitacao)) {
			return false;
		}
		if (numPortariaOutorga == null) {
			if (other.numPortariaOutorga != null) {
				return false;
			}
		} else if (!numPortariaOutorga.equals(other.numPortariaOutorga)) {
			return false;
		}
		if (numProcessoOutorga == null) {
			if (other.numProcessoOutorga != null) {
				return false;
			}
		} else if (!numProcessoOutorga.equals(other.numProcessoOutorga)) {
			return false;
		}
		return true;
	}

}
