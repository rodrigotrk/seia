package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;

@Entity
@Table(name="cerh_tipo_uso")
public class CerhTipoUso extends AbstractEntityHist implements Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_tipo_uso_seq")
	@SequenceGenerator(name = "cerh_tipo_uso_seq", sequenceName = "cerh_tipo_uso_seq", allocationSize = 1)
	@Column(name="ide_cerh_tipo_uso")
	private Integer ideCerhTipoUso;

	@ManyToOne
	@JoinColumn(name="ide_cerh")
	private Cerh ideCerh;

	@ManyToOne
	@JoinColumn(name="ide_cerh_processo")
	private CerhProcesso ideCerhProcesso;

	@ManyToOne
	@JoinColumn(name="ide_cerh_resposta_dados_gerais")
	private CerhRespostaDadosGerais ideCerhRespostaDadosGerais;
	
	@ManyToOne
	@JoinColumn(name="ide_tipo_uso_recurso_hidrico")
	@Historico (nameMethod ="getIdeTipoUsoRecursoHidrico", key=true)  
	private TipoUsoRecursoHidrico ideTipoUsoRecursoHidrico;
	
	@OneToMany(mappedBy="ideCerhTipoUso", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Collection<CerhLocalizacaoGeografica> cerhLocalizacaoGeograficaCollection;
	
	@Transient
	private CerhTipoUso cerhTipoUsoPai;

	public CerhTipoUso() {
	}
	
	public CerhTipoUso(Integer ideCerhTipoUso) {
		this.ideCerhTipoUso = ideCerhTipoUso;
	}
	
	public CerhTipoUso(TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		this.ideTipoUsoRecursoHidrico = new TipoUsoRecursoHidrico(tipoUsoRecursoHidricoEnum);
	}
	
	public CerhTipoUso(Cerh cerh, TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		this(tipoUsoRecursoHidricoEnum);
		this.ideCerh = cerh;
	}
	
	public CerhTipoUso(CerhProcesso cerhProcesso, TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		this(cerhProcesso.getIdeCerh(), tipoUsoRecursoHidricoEnum);
		this.ideCerhProcesso = cerhProcesso;
	}

	public Integer getIdeCerhTipoUso() {
		return ideCerhTipoUso;
	}

	public void setIdeCerhTipoUso(Integer ideCerhTipoUso) {
		this.ideCerhTipoUso = ideCerhTipoUso;
	}

	public Cerh getIdeCerh() {
		return ideCerh;
	}

	public void setIdeCerh(Cerh ideCerh) {
		this.ideCerh = ideCerh;
	}

	public CerhProcesso getIdeCerhProcesso() {
		return ideCerhProcesso;
	}

	public void setIdeCerhProcesso(CerhProcesso ideCerhProcesso) {
		this.ideCerhProcesso = ideCerhProcesso;
	}

	public CerhRespostaDadosGerais getIdeCerhRespostaDadosGerais() {
		return ideCerhRespostaDadosGerais;
	}

	public void setIdeCerhRespostaDadosGerais(
			CerhRespostaDadosGerais ideCerhRespostaDadosGerais) {
		this.ideCerhRespostaDadosGerais = ideCerhRespostaDadosGerais;
	}

	public TipoUsoRecursoHidrico getIdeTipoUsoRecursoHidrico() {
		return ideTipoUsoRecursoHidrico;
	}

	public void setIdeTipoUsoRecursoHidrico(TipoUsoRecursoHidrico ideTipoUsoRecursoHidrico) {
		this.ideTipoUsoRecursoHidrico = ideTipoUsoRecursoHidrico;
	}

	public Collection<CerhLocalizacaoGeografica> getCerhLocalizacaoGeograficaCollection() {
		return cerhLocalizacaoGeograficaCollection;
	}

	public void setCerhLocalizacaoGeograficaCollection(Collection<CerhLocalizacaoGeografica> cerhLocalizacaoGeograficaCollection) {
		this.cerhLocalizacaoGeograficaCollection = cerhLocalizacaoGeograficaCollection;
	}
	
	public CerhTipoUso getCerhTipoUsoPai() {
		return cerhTipoUsoPai;
	}
	
	public void setCerhTipoUsoPai(CerhTipoUso cerhTipoUsoPai) {
		this.cerhTipoUsoPai = cerhTipoUsoPai;
	}
	
	public CerhTipoUso clone() throws CloneNotSupportedException {
		return (CerhTipoUso) super.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ideTipoUsoRecursoHidrico == null) ? 0 : ideTipoUsoRecursoHidrico.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CerhTipoUso other = (CerhTipoUso) obj;
		if (ideTipoUsoRecursoHidrico == null) {
			if (other.ideTipoUsoRecursoHidrico != null)
				return false;
		} else if (!ideTipoUsoRecursoHidrico.equals(other.ideTipoUsoRecursoHidrico))
			return false;
		return true;
	}

}