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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

/**
 * The persistent class for the outorga_mineracao database table.
 * 
 */
@Entity
@Table(name = "outorga_mineracao")
@NamedQuery(name = "OutorgaMineracao.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM OutorgaMineracao o WHERE o.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class OutorgaMineracao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "OUTORGA_MINERACAO_IDEOUTORGAMINERACAO_GENERATOR", sequenceName = "OUTORGA_MINERACAO_IDE_OUTORGA_MINERACAO_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OUTORGA_MINERACAO_IDEOUTORGAMINERACAO_GENERATOR")
	@Column(name = "ide_outorga_mineracao")
	private Integer ideOutorgaMineracao;

	@Length(max=20, message="O Número da portaria excede o tamanho máximo de caracteres permitido.")
	@Column(name = "num_portaria_outorga", nullable = false)
	private String numPortariaOutorga;

	@Length(max=31, message="O Número do processo excede o tamanho máximo de caracteres permitido.")
	@Column(name = "num_processo_outorga", nullable = false)
	private String numProcessoOutorga;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", referencedColumnName = "ide_fce_licenciamento_mineral", nullable = true)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_pesquisa_mineral", referencedColumnName = "ide_fce_pesquisa_mineral", nullable = true)
	private FcePesquisaMineral fcePesquisaMineral;

	@Transient
	private boolean confirmado;

	@Transient
	private boolean edicao;

	public OutorgaMineracao() {
	}

	public OutorgaMineracao(Object object, String numProcessoTramite) {
		if(object instanceof FceLicenciamentoMineral){
			this.fceLicenciamentoMineral = (FceLicenciamentoMineral) object;
		}
		else if(object instanceof FcePesquisaMineral){
			this.fcePesquisaMineral = (FcePesquisaMineral) object;
		}
		this.numProcessoOutorga = numProcessoTramite;
	}

	public Integer getIdeOutorgaMineracao() {
		return this.ideOutorgaMineracao;
	}

	public void setIdeOutorgaMineracao(Integer ideOutorgaMineracao) {
		this.ideOutorgaMineracao = ideOutorgaMineracao;
	}

	public String getNumPortariaOutorga() {
		return this.numPortariaOutorga;
	}

	public void setNumPortariaOutorga(String numPortariaOutorga) {
		this.numPortariaOutorga = numPortariaOutorga;
	}

	public String getNumProcessoOutorga() {
		return this.numProcessoOutorga;
	}

	public void setNumProcessoOutorga(String numProcessoOutorga) {
		this.numProcessoOutorga = numProcessoOutorga;
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return this.fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public FcePesquisaMineral getFcePesquisaMineral() {
		return this.fcePesquisaMineral;
	}

	public void setFcePesquisaMineral(FcePesquisaMineral fcePesquisaMineral) {
		this.fcePesquisaMineral = fcePesquisaMineral;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao() {
		edicao = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideOutorgaMineracao == null) ? 0 : ideOutorgaMineracao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		OutorgaMineracao other = (OutorgaMineracao) obj;
		if(ideOutorgaMineracao == null){
			if(other.ideOutorgaMineracao != null)
				return false;
		}
		else if(!ideOutorgaMineracao.equals(other.ideOutorgaMineracao))
			return false;
		return true;
	}

}