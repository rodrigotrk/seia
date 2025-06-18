package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "processo_sinaflor")
public class ProcessoSinaflor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "processo_sinaflor_seq")
	@SequenceGenerator(name = "processo_sinaflor_seq", sequenceName = "processo_sinaflor_seq", allocationSize = 1)
	@Column(name = "ide_processo_sinaflor", nullable = false)
	private Integer ideProcessoSinaflor;

	@Column(name = "token", nullable = false)
	private String token;

	@Column(name = "dtc_sincronizacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcSincronizacao;

	@Column(name = "ind_concluido")
	private Boolean indConcluido;

	@Column(name = "dsc_log")
	private String dscLog;

	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo")
	@ManyToOne(fetch = FetchType.EAGER)
	private Processo ideProcesso;
	
	@Transient
	private List<String> log;

	public ProcessoSinaflor() {

	}

	public Integer getIdeProcessoSinaflor() {
		return ideProcessoSinaflor;
	}

	public void setIdeProcessoSinaflor(Integer ideProcessoSinaflor) {
		this.ideProcessoSinaflor = ideProcessoSinaflor;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getDtcSincronizacao() {
		return dtcSincronizacao;
	}

	public void setDtcSincronizacao(Date dtcSincronizacao) {
		this.dtcSincronizacao = dtcSincronizacao;
	}

	public Boolean getIndConcluido() {
		return indConcluido;
	}

	public void setIndConcluido(Boolean indConcluido) {
		this.indConcluido = indConcluido;
	}

	public String getDscLog() {
		return dscLog;
	}

	public void setDscLog(String dscLog) {
		this.dscLog = dscLog;
	}

	public Processo getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}
	
	public List<String> getLog() {
		return log;
	}
	
	public List<String> iniciarLog() {
		log = new ArrayList<String>();
		return log;
	}
	
	public void finalizarLog() {
		StringBuilder log = new StringBuilder();
		for(String str : this.log) {
			log.append(str);
			log.append("\n\n");
		}
		dscLog = log.toString(); 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideProcessoSinaflor == null) ? 0 : ideProcessoSinaflor.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
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
		ProcessoSinaflor other = (ProcessoSinaflor) obj;
		if (ideProcessoSinaflor == null) {
			if (other.ideProcessoSinaflor != null)
				return false;
		} else if (!ideProcessoSinaflor.equals(other.ideProcessoSinaflor))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}



}
