package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Tabela que armazena a reserva da água
 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
 */
@Entity
@Table(name="reserva_agua")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name  = "ReservaAgua.excluirByIdeFceReservaAgua", query = "DELETE FROM ReservaAgua ra WHERE ra.ideFceReservaAgua.ideFceReservaAgua = :ideFceReservaAgua"),
	@NamedQuery(name  = "ReservaAgua.excluirByIdeFceOutorgaLocalizacaoGeografica", query = "DELETE FROM ReservaAgua ra WHERE ra.ideFceOutorgaLocalizacaoGeografica.ideFceOutorgaLocalizacaoGeografica = :ideFceOutorgaLocalizacaoGeografica"),
})
public class ReservaAgua implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ReservaAguaPK reservaAguaPK;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_status_reserva_agua", nullable=false)
	private Date dtcStatusReservaAgua;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_reserva_agua", referencedColumnName="ide_fce_reserva_agua", nullable = false, insertable = false, updatable = false)
	private FceReservaAgua ideFceReservaAgua;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="ide_status_reserva_agua", referencedColumnName="ide_status_reserva_agua", nullable = false, insertable = false, updatable = false)
	private StatusReservaAgua ideStatusReservaAgua;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_outorga_localizacao_geografica", referencedColumnName="ide_fce_outorga_localizacao_geografica", nullable=false, insertable = false, updatable = false)
	private FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pessoa_status_reserva_agua", referencedColumnName="ide_pessoa_fisica", nullable=false)
	private Funcionario idePessoaStatusReservaAgua;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="ide_motivo_reserva_agua", referencedColumnName="ide_motivo_reserva_agua", nullable=true)
	private MotivoReservaAgua ideMotivoReservaAgua;
	
	@Column(name="dsc_status_reserva_agua", length=100, nullable=true)
	private String dscStatusReservaAgua;

	public ReservaAgua(){
	}
	
	public ReservaAgua(FceReservaAgua ideFceReservaAgua, StatusReservaAgua ideStatusReservaAgua, FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica, Funcionario funcionario) {
		this.reservaAguaPK = new ReservaAguaPK(ideFceReservaAgua, ideStatusReservaAgua, ideFceOutorgaLocalizacaoGeografica);
		this.ideFceReservaAgua = ideFceReservaAgua;
		this.ideStatusReservaAgua = ideStatusReservaAgua;
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
		this.idePessoaStatusReservaAgua = funcionario;
		this.dtcStatusReservaAgua = new Date();
	}
	
	public ReservaAguaPK getReservaAguaPK() {
		return reservaAguaPK;
	}

	public void setReservaAguaPK(ReservaAguaPK reservaAguaPK) {
		this.reservaAguaPK = reservaAguaPK;
	}

	public Date getDtcStatusReservaAgua() {
		return dtcStatusReservaAgua;
	}

	public void setDtcStatusReservaAgua(Date dtcStatusReservaAgua) {
		this.dtcStatusReservaAgua = dtcStatusReservaAgua;
	}

	public Funcionario getIdePessoaStatusReservaAgua() {
		return this.idePessoaStatusReservaAgua;
	}

	public void setIdePessoaStatusReservaAgua(Funcionario idePessoaStatusReservaAgua) {
		this.idePessoaStatusReservaAgua = idePessoaStatusReservaAgua;
	}

	public FceReservaAgua getIdeFceReservaAgua() {
		return ideFceReservaAgua;
	}

	public void setIdeFceReservaAgua(FceReservaAgua ideFceReservaAgua) {
		this.ideFceReservaAgua = ideFceReservaAgua;
	}

	public MotivoReservaAgua getIdeMotivoReservaAgua() {
		return ideMotivoReservaAgua;
	}

	public void setIdeMotivoReservaAgua(MotivoReservaAgua ideMotivoReservaAgua) {
		this.ideMotivoReservaAgua = ideMotivoReservaAgua;
	}

	public StatusReservaAgua getIdeStatusReservaAgua() {
		return ideStatusReservaAgua;
	}

	public void setIdeStatusReservaAgua(StatusReservaAgua ideStatusReservaAgua) {
		this.ideStatusReservaAgua = ideStatusReservaAgua;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((reservaAguaPK == null) ? 0 : reservaAguaPK.hashCode());
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
		ReservaAgua other = (ReservaAgua) obj;
		if (reservaAguaPK == null) {
			if (other.reservaAguaPK != null)
				return false;
		} else if (!reservaAguaPK.equals(other.reservaAguaPK))
			return false;
		return true;
	}

	public FceOutorgaLocalizacaoGeografica getIdeFceOutorgaLocalizacaoGeografica() {
		return ideFceOutorgaLocalizacaoGeografica;
	}

	public void setIdeFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}

	public String getDscStatusReservaAgua() {
		return dscStatusReservaAgua;
	}

	public void setDscStatusReservaAgua(String dscStatusReservaAgua) {
		this.dscStatusReservaAgua = dscStatusReservaAgua;
	}
	
}