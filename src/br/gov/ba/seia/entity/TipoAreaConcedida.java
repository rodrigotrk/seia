package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.TipoAreaConcedidaEnum;

@Entity
@Table(name="tipo_area_concedida")
@NamedQuery(name="TipoAreaConcedida.findAll", query="SELECT t FROM TipoAreaConcedida t")
public class TipoAreaConcedida implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_AREA_CONCEDIDA_IDETIPOAREACONCEDIDA_GENERATOR", sequenceName="TIPO_AREA_CONCEDIDA_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_AREA_CONCEDIDA_IDETIPOAREACONCEDIDA_GENERATOR")
	@Column(name="ide_tipo_area_concedida")
	private Integer ideTipoAreaConcedida;

	@Column(name="des_tipo_area_concedida")
	private String desTipoAreaConcedida;

	@OneToMany(mappedBy="ideTipoAreaConcedida")
	private List<ProcessoAtoConcedido> processoAtoConcedidos;

	public TipoAreaConcedida() {
	}
	
	public TipoAreaConcedida(TipoAreaConcedidaEnum atividade) {
		this.ideTipoAreaConcedida = atividade.getIde();
	}

	public Integer getIdeTipoAreaConcedida() {
		return this.ideTipoAreaConcedida;
	}

	public void setIdeTipoAreaConcedida(Integer ideTipoAreaConcedida) {
		this.ideTipoAreaConcedida = ideTipoAreaConcedida;
	}

	public String getDesTipoAreaConcedida() {
		return this.desTipoAreaConcedida;
	}

	public void setDesTipoAreaConcedida(String desTipoAreaConcedida) {
		this.desTipoAreaConcedida = desTipoAreaConcedida;
	}

	public List<ProcessoAtoConcedido> getProcessoAtoConcedidos() {
		return this.processoAtoConcedidos;
	}

	public void setProcessoAtoConcedidos(List<ProcessoAtoConcedido> processoAtoConcedidos) {
		this.processoAtoConcedidos = processoAtoConcedidos;
	}

	public ProcessoAtoConcedido addProcessoAtoConcedido(ProcessoAtoConcedido processoAtoConcedido) {
		getProcessoAtoConcedidos().add(processoAtoConcedido);
		processoAtoConcedido.setIdeTipoAreaConcedida(this);

		return processoAtoConcedido;
	}

	public ProcessoAtoConcedido removeProcessoAtoConcedido(ProcessoAtoConcedido processoAtoConcedido) {
		getProcessoAtoConcedidos().remove(processoAtoConcedido);
		processoAtoConcedido.setIdeTipoAreaConcedida(null);

		return processoAtoConcedido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoAreaConcedida == null) ? 0 : ideTipoAreaConcedida
						.hashCode());
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
		TipoAreaConcedida other = (TipoAreaConcedida) obj;
		if (ideTipoAreaConcedida == null) {
			if (other.ideTipoAreaConcedida != null)
				return false;
		} else if (!ideTipoAreaConcedida.equals(other.ideTipoAreaConcedida))
			return false;
		return true;
	}

}