package br.gov.ba.seia.entity;

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


@Entity
@Table(name = "cerh_cod_receita_tipo_uso")
public class CerhCodReceitaTpUso extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_cod_receita_tipo_uso_seq")
	@SequenceGenerator(name = "cerh_cod_receita_tipo_uso_seq", sequenceName = "cerh_cod_receita_tipo_uso_seq", allocationSize = 1)
	@Column(name = "ide_cerh_cod_receita_tipo_uso")
	private Integer ideCerhCodreceitaTpUso;
	
	@ManyToOne
	@JoinColumn(name="ide_sefaz_codigo_receita")
	private SefazCodigoReceita sefazCodigoReceita;
	
	@ManyToOne
	@JoinColumn(name="ide_tipo_uso_recurso_hidrico")
	private TipoUsoRecursoHidrico tipoUsoRecursoHidrico;
	

	/**
	 * @return the ideCerhCodreceitaTpUso
	 */
	public Integer getIdeCerhCodreceitaTpUso() {
		return ideCerhCodreceitaTpUso;
	}

	/**
	 * @param ideCerhCodreceitaTpUso the ideCerhCodreceitaTpUso to set
	 */
	public void setIdeCerhCodreceitaTpUso(Integer ideCerhCodreceitaTpUso) {
		this.ideCerhCodreceitaTpUso = ideCerhCodreceitaTpUso;
	}

	/**
	 * @return the tipoUsoRecursoHidrico
	 */
	public TipoUsoRecursoHidrico getTipoUsoRecursoHidrico() {
		return tipoUsoRecursoHidrico;
	}

	public SefazCodigoReceita getSefazCodigoReceita() {
		return sefazCodigoReceita;
	}

	public void setSefazCodigoReceita(SefazCodigoReceita sefazCodigoReceita) {
		this.sefazCodigoReceita = sefazCodigoReceita;
	}

	/**
	 * @param tipoUsoRecursoHidrico the tipoUsoRecursoHidrico to set
	 */
	public void setTipoUsoRecursoHidrico(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		this.tipoUsoRecursoHidrico = tipoUsoRecursoHidrico;
	}

}
