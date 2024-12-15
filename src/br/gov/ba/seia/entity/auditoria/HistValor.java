package br.gov.ba.seia.entity.auditoria;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Classe historico do CEFIR.
 * @author 
 *
 */
@Entity
@Table(name = "HIST_VALOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistValor.findAll", query = "SELECT v FROM HistValor v inner join v.ideHistorico h "),
    @NamedQuery(name = "HistValor.findByTabela", query = "SELECT v FROM HistValor v inner join v.ideHistorico h where v.ideCampo.ideTabela.ideTabela = :ideTabela "),
    @NamedQuery(name = "HistValor.findByCampo", query = "SELECT v FROM HistValor v inner join v.ideHistorico h where v.ideCampo.ideCampo = :ideCampoTabela "),
    @NamedQuery(name = "HistValor.findByTabelaAndCampo", query = "SELECT v FROM HistValor v inner join v.ideHistorico h where v.ideCampo.ideCampo = :ideCampoTabela and v.ideCampo.ideTabela.ideTabela = :ideTabela ")
})
public class HistValor implements Serializable{
	private static final long serialVersionUID = 5342534285908643021L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "valor_ide_valor_seq")
	@SequenceGenerator(name = "valor_ide_valor_seq", sequenceName = "valor_ide_valor_seq", allocationSize = 1)
	@NotNull
	@Column(name = "ide_valor")
	private Long ideValor;
	
	@Basic(optional = false)
	@NotNull
    @Column(name = "val_valor")
	private String valor;
	
	@Basic(optional = false)
	@NotNull
    @Column(name = "ide_registro")
	private Long ideRegistro;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ide_campo")
	private HistCampo ideCampo;
	
	@ManyToOne
	@JoinColumn(name = "ide_historico")
	private HistHistorico ideHistorico;

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public HistCampo getIdeCampo() {
		return ideCampo;
	}

	public void setIdeCampo(HistCampo ideCampo) {
		this.ideCampo = ideCampo;
	}

	public HistHistorico getIdeHistorico() {
		return ideHistorico;
	}

	public void setIdeHistorico(HistHistorico ideHistorico) {
		this.ideHistorico = ideHistorico;
	}

	public Long getIdeValor() {
		return ideValor;
	}

	public void setIdeValor(Long ideValor) {
		this.ideValor = ideValor;
	}
	
	@Override
	public String toString() {
		return this.ideCampo+"";
	}

	public Long getIdeRegistro() {
		return ideRegistro;
	}

	public void setIdeRegistro(Long ideRegistro) {
		this.ideRegistro = ideRegistro;
	}
}
