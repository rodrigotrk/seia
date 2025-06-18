package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;


@PersistenceUnits({@PersistenceUnit(unitName="seiapu")})  
//@PersistenceContext(unitName="seiapu")
//@PersistenceUnit(unitName="seiapu")
@Entity
@Table(name = "processo_externo")
@XmlRootElement
public class ProcessoExterno implements Serializable {
	
	private static final long serialVersionUID = -3873653765458098881L;

//	@Id
////	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="processo_externo_ide_processo_externo_seq")    
////	@SequenceGenerator(name="processo_externo_ide_processo_externo_seq", sequenceName="processo_externo_ide_processo_externo_seq", allocationSize=1)
//	@Basic(optional = false)
//	@Column(name = "ide_processo_externo")
//	private Integer ideProcessoExterno;
    @Column(name = "base")
	private String base;
	
    @Column(name = "sistema")
	private String sistema;
	
    @Column(name = "documento_cpf_cnpj")
	private String documentoCpfCnpj;
	
    @Id
    @Column(name = "processo")
	private String processo;
	
    @Column(name = "dtc_formacao")
    @Temporal(TemporalType.TIMESTAMP)
	private Date dtcFormacao;
	
    @Column(name = "deferimento")
	private String deferimento;
	
    @Column(name = "status")
	private String status;
	
    @Column(name = "tipo")
	private String tipo;

//	public Integer getIdeProcessoExterno() {
//		return ideProcessoExterno;
//	}
//
//	public void setIdeProcessoExterno(Integer ideProcessoExterno) {
//		this.ideProcessoExterno = ideProcessoExterno;
//	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getProcesso() {
		return processo;
	}

	public String getProcessoFormatado() {
		if(!Util.isNullOuVazio(processo)){
			String prefixo = processo.substring(0,4);
			String sufixo = processo.substring(4);
			return prefixo +"-"+sufixo;
		}
		return processo;
	}
	
	public void setProcesso(String processo) {
		this.processo = processo;
	}

	public Date getDtcFormacao() {
		return dtcFormacao;
	}

	public void setDtcFormacao(Date dtcFormacao) {
		this.dtcFormacao = dtcFormacao;
	}

	public String getDeferimento() {
		return deferimento;
	}

	public void setDeferimento(String deferimento) {
		this.deferimento = deferimento;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDocumentoCpfCnpj() {
		return documentoCpfCnpj;
	}

	public void setDocumentoCpfCnpj(String documentoCpfCnpj) {
		this.documentoCpfCnpj = documentoCpfCnpj;
	}

}
