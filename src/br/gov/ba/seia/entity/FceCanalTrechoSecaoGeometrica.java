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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@Table( name = "fce_canal_trecho_secao_geometrica")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "FceCanalTrechoSecaoGeometrica.removeByIdTrechoSecaoGeometrica", query = "DELETE FROM FceCanalTrechoSecaoGeometrica a WHERE a.ideFceCanalTrechoSecaoGeometrica = :ideFceCanalTrechoSecaoGeometrica")
})
public class FceCanalTrechoSecaoGeometrica implements BaseEntity, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="fce_canal_trecho_secao_geometrica_seq")    
	@SequenceGenerator(name="fce_canal_trecho_secao_geometrica_seq", sequenceName="fce_canal_trecho_secao_geometrica_seq", allocationSize=1)
	@Column(name = "ide_fce_canal_trecho_secao_geometrica")
	private Integer ideFceCanalTrechoSecaoGeometrica;
	
	@Column(name = "val_base_maior")
	private double base_maior;
	
	@Column(name = "val_base_menor")
	private double base_menor;
	
	@Column(name = "val_altura")
	private double altura;
	
	@Column(name = "val_largura")
	private double largura;
	
	@Column(name = "val_diametro")
	private double diametro;

	@JoinColumn(name = "ide_fce_canal_trecho", referencedColumnName = "ide_fce_canal_trecho", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private FceCanalTrecho fceCanalTrecho;
	
	@JoinColumn(name = "ide_secao_geometrica", referencedColumnName = "ide_secao_geometrica", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private SecaoGeometrica secaoGeometrica;
	
	@Override
	public Long getId() {
		return new Long(ideFceCanalTrechoSecaoGeometrica);
	}

	public Integer getIdeFceCanalTrechoSecaoGeometrica() {
		return ideFceCanalTrechoSecaoGeometrica;
	}

	public void setIdeFceCanalTrechoSecaoGeometrica(
			Integer ideFceCanalTrechoSecaoGeometrica) {
		this.ideFceCanalTrechoSecaoGeometrica = ideFceCanalTrechoSecaoGeometrica;
	}

	public double getBase_maior() {
		return base_maior;
	}

	public void setBase_maior(double base_maior) {
		this.base_maior = base_maior;
	}

	public double getBase_menor() {
		return base_menor;
	}

	public void setBase_menor(double base_menor) {
		this.base_menor = base_menor;
	}

	public double getAltura() {
		return altura;
	}

	public void setAltura(double altura) {
		this.altura = altura;
	}

	public double getLargura() {
		return largura;
	}

	public void setLargura(double largura) {
		this.largura = largura;
	}

	public double getDiametro() {
		return diametro;
	}

	public void setDiametro(double diametro) {
		this.diametro = diametro;
	}

	public FceCanalTrecho getFceCanalTrecho() {
		return fceCanalTrecho;
	}

	public void setFceCanalTrecho(FceCanalTrecho fceCanalTrecho) {
		this.fceCanalTrecho = fceCanalTrecho;
	}

	public SecaoGeometrica getSecaoGeometrica() {
		return secaoGeometrica;
	}

	public void setSecaoGeometrica(SecaoGeometrica secaoGeometrica) {
		this.secaoGeometrica = secaoGeometrica;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FceCanalTrechoSecaoGeometrica other = (FceCanalTrechoSecaoGeometrica) obj;
//		if (ideFceCanalTrechoSecaoGeometrica == null && other.ideFceCanalTrechoSecaoGeometrica == null) {
			if (!this.getSecaoGeometrica().equals(other.getSecaoGeometrica())) {
				return false;
			}
//		}
		return true;
	}
}
