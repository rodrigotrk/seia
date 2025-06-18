package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "especie_supressao_aut_destino_socio_economico")
public class EspecieSupressaoAutDestinoSocioEconomico implements Serializable,
		BaseEntity {

	private static final long serialVersionUID = 6984065489636905246L;

	@Id
	@SequenceGenerator(name = "ESPECIE_SUPRESSAO_AUT_DESTINO_SOCIO_ECONOMICO_GENERATOR", sequenceName = "especie_supressao_aut_destino_socio_economico_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESPECIE_SUPRESSAO_AUT_DESTINO_SOCIO_ECONOMICO_GENERATOR")
	@Column(name = "ide_especie_supressao_aut_destino_socio_economico")
	private Integer ideEspecieSupressaoAutDestinoSocioEconomico;

	@JoinColumn(name = "ide_especie_supressao_autorizacao", referencedColumnName = "ide_especie_supressao_autorizacao", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private EspecieSupressaoAutorizacao ideEspecieSupressaoAutorizacao;

	@JoinColumn(name = "ide_destino_socioeconomico", referencedColumnName = "ide_destino_socioeconomico", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private DestinoSocioeconomico ideDestinoSocioeconomico;

	public Integer getIdeEspecieSupressaoAutDestinoSocioEconomico() {
		return ideEspecieSupressaoAutDestinoSocioEconomico;
	}

	public void setIdeEspecieSupressaoAutDestinoSocioEconomico(
			Integer ideEspecieSupressaoAutDestinoSocioEconomico) {
		this.ideEspecieSupressaoAutDestinoSocioEconomico = ideEspecieSupressaoAutDestinoSocioEconomico;
	}

	public EspecieSupressaoAutorizacao getIdeEspecieSupressaoAutorizacao() {
		return ideEspecieSupressaoAutorizacao;
	}

	public void setIdeEspecieSupressaoAutorizacao(
			EspecieSupressaoAutorizacao ideEspecieSupressaoAutorizacao) {
		this.ideEspecieSupressaoAutorizacao = ideEspecieSupressaoAutorizacao;
	}

	public DestinoSocioeconomico getIdeDestinoSocioeconomico() {
		return ideDestinoSocioeconomico;
	}

	public void setIdeDestinoSocioeconomico(
			DestinoSocioeconomico ideDestinoSocioeconomico) {
		this.ideDestinoSocioeconomico = ideDestinoSocioeconomico;
	}

	@Override
	public Long getId() {
		return new Long(this.ideEspecieSupressaoAutDestinoSocioEconomico);
	}
}
