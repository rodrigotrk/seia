package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@XmlRootElement
@Table( name = "fce_canal_trecho")
@NamedQueries(
		@NamedQuery(name = "FceCanalTrecho.removeByIdFceCanalTrecho", query = "DELETE FROM FceCanalTrecho a WHERE a.idFceCanalTrecho = :idFceCanalTrecho")
)
public class FceCanalTrecho implements BaseEntity, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="fce_canal_trecho_seq")    
	@SequenceGenerator(name="fce_canal_trecho_seq", sequenceName="fce_canal_trecho_seq", allocationSize=1)
	@Column( name = "ide_fce_canal_trecho")
	private Integer idFceCanalTrecho;
	
	@JoinColumn(name = "ide_localizacao_geografica_inicio", referencedColumnName = "ide_localizacao_geografica", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private LocalizacaoGeografica localizacaoGeograficaInicio;
	
	@JoinColumn(name = "ide_localizacao_geografica_fim", referencedColumnName = "ide_localizacao_geografica", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private LocalizacaoGeografica localizacaoGeograficaFim;
	
	@Column( name = "val_comprimento")
	private Double comprimento;
	
	@JoinColumn(name="ide_tipo_canal",referencedColumnName = "ide_tipo_canal", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoCanal tipoCanal;
	
	@ManyToMany
	@JoinTable(name = "fce_canal_trecho_tipo_revestimento",  
		joinColumns = { @JoinColumn(name = "ide_fce_canal_trecho", referencedColumnName = "ide_fce_canal_trecho", nullable = false, updatable = false) }, 
		inverseJoinColumns = { @JoinColumn(name = "ide_tipo_revestimento", referencedColumnName = "ide_tipo_revestimento", nullable = false, updatable = false) })
	private List<TipoRevestimento> tiposRevestimentos;
	
	@JoinColumn(name="ide_fce_canal",referencedColumnName = "ide_fce_canal", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private FceCanal canal;
	
	@OneToMany(mappedBy = "fceCanalTrecho", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FceCanalTrechoSecaoGeometrica> fceCanalTrechoSecaoGeometrica;
	
	public FceCanalTrecho() {
		this.tiposRevestimentos = new ArrayList<TipoRevestimento>();
		this.fceCanalTrechoSecaoGeometrica = new ArrayList<FceCanalTrechoSecaoGeometrica>();
	}
	
	@Override
	public Long getId() {
		return new Long(idFceCanalTrecho);
	}

	public Integer getIdFceCanalTrecho() {
		return idFceCanalTrecho;
	}

	public void setIdFceCanalTrecho(Integer idFceCanalTrecho) {
		this.idFceCanalTrecho = idFceCanalTrecho;
	}

	public LocalizacaoGeografica getLocalizacaoGeograficaInicio() {
		return localizacaoGeograficaInicio;
	}

	public void setLocalizacaoGeograficaInicio(
			LocalizacaoGeografica localizacaoGeograficaInicio) {
		this.localizacaoGeograficaInicio = localizacaoGeograficaInicio;
	}

	public LocalizacaoGeografica getLocalizacaoGeograficaFim() {
		return localizacaoGeograficaFim;
	}

	public void setLocalizacaoGeograficaFim(
			LocalizacaoGeografica localizacaoGeograficaFim) {
		this.localizacaoGeograficaFim = localizacaoGeograficaFim;
	}

	public Double getComprimento() {
		return comprimento;
	}

	public void setComprimento(Double comprimento) {
		this.comprimento = comprimento;
	}

	public TipoCanal getTipoCanal() {
		return tipoCanal;
	}

	public void setTipoCanal(TipoCanal tipoCanal) {
		this.tipoCanal = tipoCanal;
	}

	public List<TipoRevestimento> getTiposRevestimentos() {
		return tiposRevestimentos;
	}

	public void setTiposRevestimentos(List<TipoRevestimento> tiposRevestimentos) {
		this.tiposRevestimentos = tiposRevestimentos;
	}

	public FceCanal getCanal() {
		return canal;
	}

	public void setCanal(FceCanal canal) {
		this.canal = canal;
	}

	public List<FceCanalTrechoSecaoGeometrica> getFceCanalTrechoSecaoGeometrica() {
		return fceCanalTrechoSecaoGeometrica;
	}

	public void setFceCanalTrechoSecaoGeometrica(
			List<FceCanalTrechoSecaoGeometrica> fceCanalTrechoSecaoGeometrica) {
		this.fceCanalTrechoSecaoGeometrica = fceCanalTrechoSecaoGeometrica;
	}
	
	@Override
	public boolean equals(Object object) {
        if (!(object instanceof FceCanalTrecho)) {
            return false;
        }
        FceCanalTrecho other = (FceCanalTrecho) object;
        if ((this.getIdFceCanalTrecho() == null && other.getIdFceCanalTrecho() != null) || (this.getIdFceCanalTrecho() != null && !this.getIdFceCanalTrecho().equals(other.getIdFceCanalTrecho()))) {
            return false;
        }
        if(!this.getLocalizacaoGeograficaInicio().equals(other.getLocalizacaoGeograficaInicio()) 
        		|| !this.getLocalizacaoGeograficaFim().equals(other.getLocalizacaoGeograficaFim())){
        	return false;
        }
        return true;
	}

}
