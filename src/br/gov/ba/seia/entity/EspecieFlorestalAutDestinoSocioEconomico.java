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

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "especie_florestal_aut_destino_socio_economico")
public class EspecieFlorestalAutDestinoSocioEconomico extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "especie_florestal_aut_destino_socio_economico_seq")
	@SequenceGenerator(name = "especie_florestal_aut_destino_socio_economico_seq", sequenceName = "especie_florestal_aut_destino_socio_economico_seq", allocationSize = 1)
	@Column(name = "ide_especie_florestal_aut_destino_socio_economico")
	private Integer ideEspecieFlorestalAutDestinoSocioEconomico;
	
	@JoinColumn(name = "ide_especie_florestal_autorizacao", referencedColumnName = "ide_especie_florestal_autorizacao", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private EspecieFlorestalAutorizacao ideEspecieFlorestalAutorizacao;
	
	@JoinColumn(name = "ide_destino_socioeconomico", referencedColumnName = "ide_destino_socioeconomico", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private DestinoSocioeconomico ideDestinoSocioeconomico;
	
	public EspecieFlorestalAutDestinoSocioEconomico() {}
	
	public EspecieFlorestalAutDestinoSocioEconomico(EspecieFlorestalAutorizacao ideEspecieFlorestalAutorizacao, DestinoSocioeconomico ideDestinoSocioeconomico) {
		this.ideEspecieFlorestalAutorizacao = ideEspecieFlorestalAutorizacao;
		this.ideDestinoSocioeconomico = ideDestinoSocioeconomico;
	}
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/
	
	public Integer getIdeEspecieFlorestalAutDestinoSocioEconomico() {
		return ideEspecieFlorestalAutDestinoSocioEconomico;
	}
	
	public void setIdeEspecieFlorestalAutDestinoSocioEconomico(Integer ideEspecieFlorestalAutDestinoSocioEconomico) {
		this.ideEspecieFlorestalAutDestinoSocioEconomico = ideEspecieFlorestalAutDestinoSocioEconomico;
	}
	
	public EspecieFlorestalAutorizacao getIdeEspecieFlorestalAutorizacao() {
		return ideEspecieFlorestalAutorizacao;
	}
	
	public void setIdeEspecieFlorestalAutorizacao(EspecieFlorestalAutorizacao ideEspecieFlorestalAutorizacao) {
		this.ideEspecieFlorestalAutorizacao = ideEspecieFlorestalAutorizacao;
	}
	
	public DestinoSocioeconomico getIdeDestinoSocioeconomico() {
		return ideDestinoSocioeconomico;
	}
	
	public void setIdeDestinoSocioeconomico(DestinoSocioeconomico ideDestinoSocioeconomico) {
		this.ideDestinoSocioeconomico = ideDestinoSocioeconomico;
	}
}