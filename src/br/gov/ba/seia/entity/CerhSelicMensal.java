package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name = "cerh_selic_mensal")
public class CerhSelicMensal extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_selic_mensal_seq")
	@SequenceGenerator(name = "cerh_selic_mensal_seq", sequenceName = "cerh_selic_mensal_seq", allocationSize = 1)
	@Column(name = "ide_cerh_selic_mensal")
	private Integer ideCerhSelicMensal;
	
	@Column(name = "num_mes")
	private Integer numMes;

	@Column(name = "num_ano")
	private Integer numAno;

	@Column(name = "val_taxa")
	private BigDecimal valTaxa;
	
	@ManyToOne
	@JoinColumn(name="ide_usuario_cadastro")
	private Usuario usuarioCadastro;
	
	@Column(name = "dtc_alteracao")
    @Temporal(TemporalType.TIMESTAMP)
	private Date dtcAlteracao;
	
	public Integer getIdeCerhSelicMensal() {
		return ideCerhSelicMensal;
	}

	public void setIdeCerhSelicMensal(Integer ideCerhSelicMensal) {
		this.ideCerhSelicMensal = ideCerhSelicMensal;
	}

	public Integer getNumMes() {
		return numMes;
	}

	public void setNumMes(Integer numMes) {
		this.numMes = numMes;
	}

	public Integer getNumAno() {
		return numAno;
	}

	public void setNumAno(Integer numAno) {
		this.numAno = numAno;
	}

	public BigDecimal getValTaxa() {
		return valTaxa;
	}

	public void setValTaxa(BigDecimal valTaxa) {
		this.valTaxa = valTaxa;
	}

	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public Date getDtcAlteracao() {
		return dtcAlteracao;
	}

	public void setDtcAlteracao(Date dtcAlteracao) {
		this.dtcAlteracao = dtcAlteracao;
	}

}
