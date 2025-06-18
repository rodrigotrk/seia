/**
 * 
 */
package br.gov.ba.seia.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name = "cerh_dae_tipo_uso")
public class CerhDaeTipoUso extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8525329707710212618L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_dae_tipo_uso_seq")
	@SequenceGenerator(name = "cerh_dae_tipo_uso_seq", sequenceName = "cerh_dae_tipo_uso_seq", allocationSize = 1)
	@Column(name = "ide_cerh_dae_tipo_uso")
	private Integer ideCerhDaeTipoUso;
	
	@ManyToOne
	@JoinColumn(name = "ide_dae")
	private Dae dae;
	
	@ManyToOne
	@JoinColumn(name = "ide_tipo_uso_recurso_hidrico")
	private TipoUsoRecursoHidrico tipoUsoRecursoHidrico;

	@Column(name = "val_valor_original")
	private BigDecimal valorOriginal;

	@Column(name = "val_acrescimo")
	private BigDecimal valorAcrescimo;

	@ManyToOne
	@JoinColumn(name = "ide_cerh_localizacao_geografica")
	private CerhLocalizacaoGeografica cerhLocalizacaoGeografica;
	
	public CerhDaeTipoUso() {
		super();
	}

	public CerhDaeTipoUso(TipoUsoRecursoHidrico tipoUsoRecursoHidrico,
			BigDecimal valorOriginal,CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		this.tipoUsoRecursoHidrico = tipoUsoRecursoHidrico;
		this.valorOriginal = valorOriginal;
		this.valorAcrescimo = new BigDecimal(0);
		this.cerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
	}

	public Integer getIdeCerhDaeTipoUso() {
		return ideCerhDaeTipoUso;
	}

	public void setIdeCerhDaeTipoUso(Integer ideCerhDaeTipoUso) {
		this.ideCerhDaeTipoUso = ideCerhDaeTipoUso;
	}

	/**
	 * @return the cerhDae
	 */
	public Dae getDae() {
		return dae;
	}

	/**
	 * @param cerhDae the cerhDae to set
	 */
	public void setDae(Dae dae) {
		this.dae = dae;
	}

	/**
	 * @return the tipoUsoRecursoHidrico
	 */
	public TipoUsoRecursoHidrico getTipoUsoRecursoHidrico() {
		return tipoUsoRecursoHidrico;
	}

	public void setTipoUsoRecursoHidrico(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		this.tipoUsoRecursoHidrico = tipoUsoRecursoHidrico;
	}

	public BigDecimal getValorOriginal() {
		return valorOriginal;
	}

	public void setValorOriginal(BigDecimal valorOriginal) {
		this.valorOriginal = valorOriginal;
	}

	public BigDecimal getValorAcrescimo() {
		return valorAcrescimo;
	}

	public void setValorAcrescimo(BigDecimal valorAcrescimo) {
		this.valorAcrescimo = valorAcrescimo;
	}

	public CerhLocalizacaoGeografica getCerhLocalizacaoGeografica() {
		return cerhLocalizacaoGeografica;
	}

	/**
	 * @param cerhLocalizacaoGeografica the cerhLocalizacaoGeografica to set
	 */
	public void setCerhLocalizacaoGeografica(
			CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		this.cerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
	}

}
