package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.enumerator.StatusBoletoEnum;

/**
 * Entidade da tabela status_boleto_pagamento
 * 
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 21/11/2013
 */
@Entity
@Table(name = "status_boleto_pagamento")
@XmlRootElement
public class StatusBoletoPagamento implements Serializable{

	private static final long serialVersionUID = 1755132213553667551L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_boleto_pagamento_ide_status_boleto_pagamento_seq")
	@SequenceGenerator(name = "status_boleto_pagamento_ide_status_boleto_pagamento_seq", sequenceName = "status_boleto_pagamento_ide_status_boleto_pagamento_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_status_boleto_pagamento", nullable = false)
	private Integer ideStatusBoletoPagamento;

	@Basic(optional = false)
	@Column(name = "nom_status_boleto_pagamento", nullable = false, length = 50)
	private String nomStatusBoletoPagamento;

	@Basic(optional = false)
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;
	
	public StatusBoletoPagamento() {
		super();
	}
	
	/**
	 * Construtor por ID do status.
	 * 
	 * @param ID do {@link StatusBoletoEnum}
	 */
	public StatusBoletoPagamento(Integer id) {
		this.ideStatusBoletoPagamento = id;
	}
	
	/**
	 * Construtor por nome do status.
	 * 
	 * @param ID do {@link StatusBoletoEnum}
	 */
	public StatusBoletoPagamento(String nomStatusBoletoPagamento) {
		this.nomStatusBoletoPagamento = nomStatusBoletoPagamento;
	}

	/**
	 * @return the ideStatusBoletoPagamento
	 */
	public Integer getIdeStatusBoletoPagamento() {
	
		return ideStatusBoletoPagamento;
	}

	/**
	 * @param ideStatusBoletoPagamento the ideStatusBoletoPagamento to set
	 */
	public void setIdeStatusBoletoPagamento(Integer ideStatusBoletoPagamento) {
	
		this.ideStatusBoletoPagamento = ideStatusBoletoPagamento;
	}

	/**
	 * @return the nomStatusBoletoPagamento
	 */
	public String getNomStatusBoletoPagamento() {
	
		return nomStatusBoletoPagamento;
	}

	/**
	 * @param nomStatusBoletoPagamento the nomStatusBoletoPagamento to set
	 */
	public void setNomStatusBoletoPagamento(String nomStatusBoletoPagamento) {
	
		this.nomStatusBoletoPagamento = nomStatusBoletoPagamento;
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