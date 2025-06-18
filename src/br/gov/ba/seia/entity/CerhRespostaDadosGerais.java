package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;

@Entity
@Table(name="cerh_resposta_dados_gerais")
public class CerhRespostaDadosGerais extends AbstractEntityHist implements  Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_resposta_dados_gerais_seq")
	@SequenceGenerator(name = "cerh_resposta_dados_gerais_seq", sequenceName = "cerh_resposta_dados_gerais_seq", allocationSize = 1)
	@Column(name="ide_cerh_resposta_dados_gerais")
	private Integer ideCerhRespostaDadosGerais;

	@Historico(nameMethod="getIdeCerhPerguntaDadosGerais")
	@Column(name="ind_resposta")
	private Boolean indResposta;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_cerh")
	private Cerh ideCerh;

	@Historico(key=true)
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_cerh_pergunta_dados_gerais")
	private CerhPerguntaDadosGerais ideCerhPerguntaDadosGerais;

	@OneToMany(mappedBy="ideCerhRespostaDadosGerais")
	private Collection<CerhTipoUso> cerhTipoUsoCollection;

	public CerhRespostaDadosGerais() {
	}

	public Integer getIdeCerhRespostaDadosGerais() {
		return ideCerhRespostaDadosGerais;
	}

	public void setIdeCerhRespostaDadosGerais(Integer ideCerhRespostaDadosGerais) {
		this.ideCerhRespostaDadosGerais = ideCerhRespostaDadosGerais;
	}

	public Boolean getIndResposta() {
		return indResposta;
	}

	public void setIndResposta(Boolean indResposta) {
		this.indResposta = indResposta;
	}

	public Cerh getIdeCerh() {
		return ideCerh;
	}

	public void setIdeCerh(Cerh ideCerh) {
		this.ideCerh = ideCerh;
	}

	public CerhPerguntaDadosGerais getIdeCerhPerguntaDadosGerais() {
		return ideCerhPerguntaDadosGerais;
	}

	public void setIdeCerhPerguntaDadosGerais(CerhPerguntaDadosGerais ideCerhPerguntaDadosGerais) {
		this.ideCerhPerguntaDadosGerais = ideCerhPerguntaDadosGerais;
	}

	public Collection<CerhTipoUso> getCerhTipoUsoCollection() {
		return cerhTipoUsoCollection;
	}

	public void setCerhTipoUsoCollection(Collection<CerhTipoUso> cerhTipoUsoCollection) {
		this.cerhTipoUsoCollection = cerhTipoUsoCollection;
	}
	
	public CerhRespostaDadosGerais clone() throws CloneNotSupportedException {
		return (CerhRespostaDadosGerais) super.clone();
	}

}