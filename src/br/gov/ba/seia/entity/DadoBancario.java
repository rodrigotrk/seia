package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author luis
 */
@Entity
@Table(name = "dado_bancario")
@NamedQueries({@NamedQuery(name = "DadoBancario.findAll", query = "SELECT d FROM DadoBancario d")})
public class DadoBancario implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_dado_bancario")
    private Integer ideDadoBancario;
    
	@Basic(optional = false)
    @Column(name = "nom_cedente")
    private String nomCedente;
    
	@Basic(optional = false)
    @Column(name = "num_cnpj")
    private String numCnpj;
    
	@Basic(optional = false)
    @Column(name = "convenio")
    private String convenio;
    
	@Basic(optional = false)
    @Column(name = "num_agencia")
    private Integer numAgencia;
    
	@Basic(optional = false)
    @Column(name = "num_digito_agencia")
    private String numDigitoAgencia;
    
	@Basic(optional = false)
    @Column(name = "num_conta")
    private Integer numConta;
    
	@Basic(optional = false)
    @Column(name = "num_digito_conta")
    private String numDigitoConta;
    
	@Basic(optional = false)
    @Column(name = "num_carteira")
    private String numCarteira;
    
	@Column(name = "local_pagamento")
    private String localPagamento;
    
	@Basic(optional = false)
    @Column(name = "dtc_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
	@Column(name = "dsc_instrucao_1")
    private String dscInstrucao1;
    
	@Column(name = "dsc_instrucao_2")
    private String dscInstrucao2;
    
	@Column(name = "dsc_instrucao_3")
    private String dscInstrucao3;
    
	@Column(name = "dsc_instrucao_4")
    private String dscInstrucao4;
    
	@Column(name = "dsc_instrucao_5")
    private String dscInstrucao5;
    
	@Column(name = "dsc_instrucao_6")
    private String dscInstrucao6;
    
	@Basic(optional = false)
    @Column(name = "ind_ativo")
    private boolean indAtivo;
    
	@JoinColumn(name = "ide_banco", referencedColumnName = "ide_banco")
    @ManyToOne(optional = false)
    private Banco ideBanco;
	
	public String retornarConvenio() {
		return getLeftPad(this.getConvenio(), 9)+"0014"+getRightPad("", 7);
	}
	
	private String getLeftPad(String texto, int tamanho){
		if(StringUtils.isEmpty(texto)){
			texto = StringUtils.EMPTY;
		}
		
		return StringUtils.right(StringUtils.leftPad(texto, tamanho, '0'), tamanho);
	}
	
	private String getRightPad(String texto, int tamanho){
		if(StringUtils.isEmpty(texto)){
			texto = StringUtils.EMPTY;
		}
		
		return StringUtils.left(StringUtils.rightPad(texto, tamanho, ' '), tamanho);
	}

    public DadoBancario() {}

    public DadoBancario(Integer ideDadoBancario) {
        this.ideDadoBancario = ideDadoBancario;
    }

    public DadoBancario(Integer ideDadoBancario, String nomCedente, String numCnpj, String convenio, Integer numAgencia, String numDigitoAgencia, Integer numConta, String numDigitoConta, String numCarteira, Date dtcCriacao, boolean indAtivo) {
        this.ideDadoBancario = ideDadoBancario;
        this.nomCedente = nomCedente;
        this.numCnpj = numCnpj;
        this.convenio = convenio;
        this.numAgencia = numAgencia;
        this.numDigitoAgencia = numDigitoAgencia;
        this.numConta = numConta;
        this.numDigitoConta = numDigitoConta;
        this.numCarteira = numCarteira;
        this.dtcCriacao = dtcCriacao;
        this.indAtivo = indAtivo;
    }

    public Integer getIdeDadoBancario() {
        return ideDadoBancario;
    }

    public void setIdeDadoBancario(Integer ideDadoBancario) {
        this.ideDadoBancario = ideDadoBancario;
    }

    public String getNomCedente() {
        return nomCedente;
    }

    public void setNomCedente(String nomCedente) {
        this.nomCedente = nomCedente;
    }

    public String getNumCnpj() {
        return numCnpj;
    }

    public void setNumCnpj(String numCnpj) {
        this.numCnpj = numCnpj;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public Integer getNumAgencia() {
        return numAgencia;
    }

    public void setNumAgencia(Integer numAgencia) {
        this.numAgencia = numAgencia;
    }

    public String getNumDigitoAgencia() {
        return numDigitoAgencia;
    }

    public void setNumDigitoAgencia(String numDigitoAgencia) {
        this.numDigitoAgencia = numDigitoAgencia;
    }

    public Integer getNumConta() {
        return numConta;
    }

    public void setNumConta(Integer numConta) {
        this.numConta = numConta;
    }

    public String getNumDigitoConta() {
        return numDigitoConta;
    }

    public void setNumDigitoConta(String numDigitoConta) {
        this.numDigitoConta = numDigitoConta;
    }

    public String getNumCarteira() {
        return numCarteira;
    }

    public void setNumCarteira(String numCarteira) {
        this.numCarteira = numCarteira;
    }

    public String getLocalPagamento() {
        return localPagamento;
    }

    public void setLocalPagamento(String localPagamento) {
        this.localPagamento = localPagamento;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public String getDscInstrucao1() {
        return dscInstrucao1;
    }

    public void setDscInstrucao1(String dscInstrucao1) {
        this.dscInstrucao1 = dscInstrucao1;
    }

    public String getDscInstrucao2() {
        return dscInstrucao2;
    }

    public void setDscInstrucao2(String dscInstrucao2) {
        this.dscInstrucao2 = dscInstrucao2;
    }

    public String getDscInstrucao3() {
        return dscInstrucao3;
    }

    public void setDscInstrucao3(String dscInstrucao3) {
        this.dscInstrucao3 = dscInstrucao3;
    }

    public String getDscInstrucao4() {
        return dscInstrucao4;
    }

    public void setDscInstrucao4(String dscInstrucao4) {
        this.dscInstrucao4 = dscInstrucao4;
    }

    public String getDscInstrucao5() {
        return dscInstrucao5;
    }

    public void setDscInstrucao5(String dscInstrucao5) {
        this.dscInstrucao5 = dscInstrucao5;
    }

    public String getDscInstrucao6() {
        return dscInstrucao6;
    }

    public void setDscInstrucao6(String dscInstrucao6) {
        this.dscInstrucao6 = dscInstrucao6;
    }

    public boolean getIndAtivo() {
        return indAtivo;
    }

    public void setIndAtivo(boolean indAtivo) {
        this.indAtivo = indAtivo;
    }

    public Banco getIdeBanco() {
        return ideBanco;
    }

    public void setIdeBanco(Banco ideBanco) {
        this.ideBanco = ideBanco;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideDadoBancario != null ? ideDadoBancario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof DadoBancario)) {
            return false;
        }
        DadoBancario other = (DadoBancario) object;
        if ((this.ideDadoBancario == null && other.ideDadoBancario != null) || (this.ideDadoBancario != null && !this.ideDadoBancario.equals(other.ideDadoBancario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.DadoBancario[ ideDadoBancario=" + ideDadoBancario + " ]";
    }
    
}
