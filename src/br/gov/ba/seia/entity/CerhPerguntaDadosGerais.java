package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;

@Entity
@Table(name="cerh_pergunta_dados_gerais")
public class CerhPerguntaDadosGerais extends AbstractEntityHist  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_pergunta_dados_gerais_seq")
	@SequenceGenerator(name = "cerh_pergunta_dados_gerais_seq", sequenceName = "cerh_pergunta_dados_gerais_seq", allocationSize = 1)
	@Column(name="ide_cerh_pergunta_dados_gerais")
	private Integer ideCerhPerguntaDadosGerais;

	@Column(name="cod_pergunta")
	private String codPergunta;

	@Historico(nameKey="Pergunta")
	@Column(name="dsc_pergunta")
	private String dscPergunta;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_criacao")
	private Date dtcCriacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_exclusao")
	private Date dtcExclusao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@OneToMany(mappedBy="ideCerhPerguntaDadosGerais")
	private Collection<CerhRespostaDadosGerais> cerhRespostaDadosGeraisCollection;

	public CerhPerguntaDadosGerais() {
	}

	public Integer getIdeCerhPerguntaDadosGerais() {
		return ideCerhPerguntaDadosGerais;
	}

	public void setIdeCerhPerguntaDadosGerais(Integer ideCerhPerguntaDadosGerais) {
		this.ideCerhPerguntaDadosGerais = ideCerhPerguntaDadosGerais;
	}

	public String getCodPergunta() {
		return codPergunta;
	}

	public void setCodPergunta(String codPergunta) {
		this.codPergunta = codPergunta;
	}

	public String getDscPergunta() {
		return dscPergunta;
	}

	public void setDscPergunta(String dscPergunta) {
		this.dscPergunta = dscPergunta;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Collection<CerhRespostaDadosGerais> getCerhRespostaDadosGeraisCollection() {
		return cerhRespostaDadosGeraisCollection;
	}

	public void setCerhRespostaDadosGeraisCollection(Collection<CerhRespostaDadosGerais> cerhRespostaDadosGeraisCollection) {
		this.cerhRespostaDadosGeraisCollection = cerhRespostaDadosGeraisCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCerhPerguntaDadosGerais == null) ? 0
						: ideCerhPerguntaDadosGerais.hashCode());
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
		CerhPerguntaDadosGerais other = (CerhPerguntaDadosGerais) obj;
		if (ideCerhPerguntaDadosGerais == null) {
			if (other.ideCerhPerguntaDadosGerais != null)
				return false;
		} else if (!ideCerhPerguntaDadosGerais
				.equals(other.ideCerhPerguntaDadosGerais))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideCerhPerguntaDadosGerais);
	}

}