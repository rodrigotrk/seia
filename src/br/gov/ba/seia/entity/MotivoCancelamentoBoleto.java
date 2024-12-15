package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidade da tabela motivo_cancelamento_boleto
 * 
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 21/11/2013
 */
@Entity
@Table(name = "motivo_cancelamento_boleto", uniqueConstraints = { @UniqueConstraint(columnNames = { "nom_motivo_cancelamento_boleto" }) })
@XmlRootElement
public class MotivoCancelamentoBoleto {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "motivo_cancelamento_boleto_ide_motivo_cancelamento_boleto_seq")
	@SequenceGenerator(name = "motivo_cancelamento_boleto_ide_motivo_cancelamento_boleto_seq", sequenceName = "motivo_cancelamento_boleto_ide_motivo_cancelamento_boleto_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_motivo_cancelamento_boleto", nullable = false)
	private Integer ideMotivoCancelamentoBoleto;

	@Basic(optional = false)
	@Column(name = "nom_motivo_cancelamento_boleto", nullable = false, length = 50)
	private String nomMotivoCancelamentoBoleto;

	@Basic(optional = false)
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;

	/**
	 * @return the ideMotivoCancelamentoBoleto
	 */
	public Integer getIdeMotivoCancelamentoBoleto() {
	
		return ideMotivoCancelamentoBoleto;
	}

	/**
	 * @param ideMotivoCancelamentoBoleto the ideMotivoCancelamentoBoleto to set
	 */
	public void setIdeMotivoCancelamentoBoleto(Integer ideMotivoCancelamentoBoleto) {
	
		this.ideMotivoCancelamentoBoleto = ideMotivoCancelamentoBoleto;
	}

	/**
	 * @return the nomMotivoCancelamentoBoleto
	 */
	public String getNomMotivoCancelamentoBoleto() {
	
		return nomMotivoCancelamentoBoleto;
	}

	/**
	 * @param nomMotivoCancelamentoBoleto the nomMotivoCancelamentoBoleto to set
	 */
	public void setNomMotivoCancelamentoBoleto(String nomMotivoCancelamentoBoleto) {
	
		this.nomMotivoCancelamentoBoleto = nomMotivoCancelamentoBoleto;
	}

	/**
	 * @return the indAtivo
	 */
	public Boolean getIndAtivo() {
	
		return indAtivo;
	}

	/**
	 * @param indAtivo the indAtivo to set
	 */
	public void setIndAtivo(Boolean indAtivo) {
	
		this.indAtivo = indAtivo;
	}
}