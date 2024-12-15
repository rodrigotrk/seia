package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntity;


@Entity
@Table(name="tipo_aproveitamento_hidreletrico")
public class TipoAproveitamentoHidreletrico extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_tipo_aproveitamento_hidreletrico")
	private Integer ideTipoAproveitamentoHidreletrico;

	@Historico(name="Tipo aproveitamento Hidreletrico")
	@Column(name="dsc_tipo_aproveitamento_hidreletrico")
	private String dscTipoAproveitamentoHidreletrico;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@OneToMany(mappedBy="ideTipoAproveitamentoHidreletrico")
	private Collection<CerhBarragemAproveitamentoHidreletrico> cerhBarragemAproveitamentoHidreletricoCollection;

	public TipoAproveitamentoHidreletrico() {
	}

	public Integer getIdeTipoAproveitamentoHidreletrico() {
		return ideTipoAproveitamentoHidreletrico;
	}

	public void setIdeTipoAproveitamentoHidreletrico(Integer ideTipoAproveitamentoHidreletrico) {
		this.ideTipoAproveitamentoHidreletrico = ideTipoAproveitamentoHidreletrico;
	}

	public String getDscTipoAproveitamentoHidreletrico() {
		return dscTipoAproveitamentoHidreletrico;
	}

	public void setDscTipoAproveitamentoHidreletrico(String dscTipoAproveitamentoHidreletrico) {
		this.dscTipoAproveitamentoHidreletrico = dscTipoAproveitamentoHidreletrico;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Collection<CerhBarragemAproveitamentoHidreletrico> getCerhBarragemAproveitamentoHidreletricoCollection() {
		return cerhBarragemAproveitamentoHidreletricoCollection;
	}

	public void setCerhBarragemAproveitamentoHidreletricoCollection(Collection<CerhBarragemAproveitamentoHidreletrico> cerhBarragemAproveitamentoHidreletricoCollection) {
		this.cerhBarragemAproveitamentoHidreletricoCollection = cerhBarragemAproveitamentoHidreletricoCollection;
	}
}