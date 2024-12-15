package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.util.ContextoUtil;


/**
 * Entidade que representa a tabela <i>reserva_legal_bloqueio</i>.
 * @author eduardo.fernandes
 * @since 17/07/2015
 */
@Entity
@Table(name="reserva_legal_bloqueio")
@NamedQuery(name="ReservaLegalBloqueio.findAll", query="SELECT r FROM ReservaLegalBloqueio r")
public class ReservaLegalBloqueio implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * Identificador do bloqueio da reserva legal.
	 * @author eduardo.fernandes
	 * @since 17/07/2015
	 */
	@Id
	@SequenceGenerator(name="RESERVA_LEGAL_BLOQUEIO_IDERESERVALEGALBLOQUEIO_GENERATOR", sequenceName="RESERVA_LEGAL_BLOQUEIO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RESERVA_LEGAL_BLOQUEIO_IDERESERVALEGALBLOQUEIO_GENERATOR")
	@Column(name="ide_reserva_legal_bloqueio", nullable=false)
	private Integer ideReservaLegalBloqueio;

	@Column(name="dtc_criacao", nullable=false)
	private Date dtcCriacao;

	/**
	 * Identificador se a reserva legal está em análise ou não.
	 * @author eduardo.fernandes
	 * @since 17/07/2015
	 */
	@Column(name="ind_bloqueado", nullable=false)
	private Boolean indBloqueado;

	/**
	 * Identificador da reserva legal que estará em análise pelo técnico.
	 * @author eduardo.fernandes
	 * @since 17/07/2015
	 */
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_reserva_legal", referencedColumnName="ide_reserva_legal", nullable=false)
	private ReservaLegal reservaLegal;

	/**
	 * Identificador do usuário (técnico) que estará analisando a reserva legal.
	 * @author eduardo.fernandes
	 * @since 17/07/2015
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_usuario", referencedColumnName="ide_pessoa_fisica", nullable=false)
	private Usuario usuario;

	public ReservaLegalBloqueio(){

	}

	public ReservaLegalBloqueio(ReservaLegal reservaLegal, Boolean indBloqueado) {
		this.usuario = ContextoUtil.getContexto().getUsuarioLogado();
		this.dtcCriacao = new Date();
		this.reservaLegal = reservaLegal;
		this.indBloqueado = indBloqueado;
	}

	public Integer getIdeReservaLegalBloqueio() {
		return this.ideReservaLegalBloqueio;
	}

	public void setIdeReservaLegalBloqueio(Integer ideReservaLegalBloqueio) {
		this.ideReservaLegalBloqueio = ideReservaLegalBloqueio;
	}

	public Date getDtcCriacao() {
		return this.dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Boolean getIndBloqueado() {
		return this.indBloqueado;
	}

	public void setIndBloqueado(Boolean indBloqueado) {
		this.indBloqueado = indBloqueado;
	}

	public ReservaLegal getReservaLegal() {
		return this.reservaLegal;
	}

	public void setReservaLegal(ReservaLegal reservaLegal) {
		this.reservaLegal = reservaLegal;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideReservaLegalBloqueio == null) ? 0
						: ideReservaLegalBloqueio.hashCode());
		result = prime * result
				+ ((reservaLegal == null) ? 0 : reservaLegal.hashCode());
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
		ReservaLegalBloqueio other = (ReservaLegalBloqueio) obj;
		if (ideReservaLegalBloqueio == null) {
			if (other.ideReservaLegalBloqueio != null) {
				return false;
			}
		} else if (!ideReservaLegalBloqueio
				.equals(other.ideReservaLegalBloqueio)) {
			return false;
		}
		if (reservaLegal == null) {
			if (other.reservaLegal != null) {
				return false;
			}
		} else if (!reservaLegal.equals(other.reservaLegal)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.cefir.entity.ReservaLegalBloqueio [ideReservaLegalBloqueio=" + ideReservaLegalBloqueio + "]";
	}

	@Override
	public ReservaLegalBloqueio clone() throws CloneNotSupportedException {
		return (ReservaLegalBloqueio) super.clone();
	}
}