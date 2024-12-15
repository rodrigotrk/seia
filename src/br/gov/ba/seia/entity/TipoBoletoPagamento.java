package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * Entidade da tabela tipo_boleto_pagamento
 * 
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 03/12/2013
 */
@Entity
@Table(name = "tipo_boleto_pagamento")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "TipoBoletoPagamento.findAll", query = "SELECT t FROM TipoBoletoPagamento t order by t.nomTipoBoletoPagamento"),
	@NamedQuery(name = "TipoBoletoPagamento.findById", query = "SELECT t FROM TipoBoletoPagamento t WHERE t.ideTipoBoletoPagamento = :ideTipoBoletoPagamento"),
	@NamedQuery(name = "TipoBoletoPagamento.findAtivos", query = "SELECT t FROM TipoBoletoPagamento t WHERE t.indAtivo = TRUE") })
public class TipoBoletoPagamento extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_boleto_pagamento_ide_tipo_boleto_pagamento_seq")
	@SequenceGenerator(name = "tipo_boleto_pagamento_ide_tipo_boleto_pagamento_seq", sequenceName = "tipo_boleto_pagamento_ide_tipo_boleto_pagamento_seq", allocationSize = 1)
	@Column(name = "ide_tipo_boleto_pagamento", nullable = false)
	private Integer ideTipoBoletoPagamento;

	@Basic(optional = false)
	@Column(name = "nom_tipo_boleto_pagamento", nullable = false, length = 50)
	private String nomTipoBoletoPagamento;

	@Basic(optional = false)
	@Column(name = "ind_requerimento", nullable = false)
	private Boolean indRequerimento;

	@Basic(optional = false)
	@Column(name = "ind_processo", nullable = false)
	private Boolean indProcesso;

	@Basic(optional = false)
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;

	public TipoBoletoPagamento() {
		super();
	}
	
	/**
	 * Construtor por ID do Tipo Boleto.
	 * 
	 * @param ideTipoBoletoPagamento
	 */
	public TipoBoletoPagamento(Integer ideTipoBoletoPagamento) {
		this.ideTipoBoletoPagamento = ideTipoBoletoPagamento;
	}

	public Integer getIdeTipoBoletoPagamento() {
		return ideTipoBoletoPagamento;
	}

	public void setIdeTipoBoletoPagamento(Integer ideTipoBoletoPagamento) {
		this.ideTipoBoletoPagamento = ideTipoBoletoPagamento;
	}

	public String getNomTipoBoletoPagamento() {
		return nomTipoBoletoPagamento;
	}

	public void setNomTipoBoletoPagamento(String nomTipoBoletoPagamento) {
		this.nomTipoBoletoPagamento = nomTipoBoletoPagamento;
	}

	public Boolean getIndRequerimento() {
		return indRequerimento;
	}

	public void setIndRequerimento(Boolean indRequerimento) {
		this.indRequerimento = indRequerimento;
	}

	public Boolean getIndProcesso() {
		return indProcesso;
	}

	public void setIndProcesso(Boolean indProcesso) {
		this.indProcesso = indProcesso;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

}