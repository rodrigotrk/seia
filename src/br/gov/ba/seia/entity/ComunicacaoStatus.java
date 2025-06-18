package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * 		
 * @author antoniony.lima
 *
 */
@Entity
@Table(name = "COMUNICACAO_STATUS")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "ComunicacaoStatus.findAll", query = "SELECT A FROM ComunicacaoStatus A") })
public class ComunicacaoStatus extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "IDE_COMUNICACAO_STATUS")
	private Integer ideComunicacao;
	
	@Basic(optional = false)
	@Column(name = "dsc_status", length = 20)
	private String dscStatus;

	
	public String getDscStatus() {
		return dscStatus;
	}

	public void setDscStatus(String dscStatus) {
		this.dscStatus = dscStatus;
	}

	public Integer getIdeComunicacao() {
		return ideComunicacao;
	}

	public void setIdeComunicacao(Integer ideComunicacao) {
		this.ideComunicacao = ideComunicacao;
	}

}
