package br.gov.ba.seia.entity;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * @author micael.coutinho
 */
@Entity
@Table(name = "dado_geografico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DadoGeografico.findAll", query = "SELECT d FROM DadoGeografico d"),
    @NamedQuery(name = "DadoGeografico.findByIdeDadoGeografico", query = "SELECT d FROM DadoGeografico d WHERE d.ideDadoGeografico = :ideDadoGeografico"),
    @NamedQuery(name = "DadoGeografico.findByCoordGeoNumerica", query = "SELECT d FROM DadoGeografico d WHERE d.coordGeoNumerica = :coordGeoNumerica"),
    @NamedQuery(name = "DadoGeografico.removerById", query = "delete from DadoGeografico d WHERE d.ideDadoGeografico = :ideDadoGeografico"),
    @NamedQuery(name = "DadoGeografico.removerByIdLocalizacaoGeo", query = "delete from DadoGeografico d WHERE d.ideLocalizacaoGeografica.ideLocalizacaoGeografica = :ideLocalizacaoGeografica"),
    @NamedQuery(name = "DadoGeografico.findByIdeLocalizacaoGeografica", query = "SELECT d FROM DadoGeografico d WHERE d.ideLocalizacaoGeografica.ideLocalizacaoGeografica = :ideLocalizacaoGeografica")})

public class DadoGeografico extends AbstractEntity implements Cloneable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DADO_GEOGRAFICO_IDE_DADO_GEOGRAFICO_seq")
	@SequenceGenerator(name = "DADO_GEOGRAFICO_IDE_DADO_GEOGRAFICO_seq", sequenceName = "DADO_GEOGRAFICO_IDE_DADO_GEOGRAFICO_seq", allocationSize = 1)
	@Column(name = "ide_dado_geografico")
	private Integer ideDadoGeografico;
	
	@Size(max = 50)
	@Historico(name="Ponto", key=true)
	@Column(name = "coord_geo_numerica")
	private String coordGeoNumerica;
	
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

    public DadoGeografico() {
    }

    public DadoGeografico(Integer ideDadoGeografico) {
        this.ideDadoGeografico = ideDadoGeografico;
    }
    
    public DadoGeografico(LocalizacaoGeografica localizacaoGeografica) {
    	this.ideLocalizacaoGeografica = localizacaoGeografica;
    }

    public Integer getIdeDadoGeografico() {
        return ideDadoGeografico;
    }

    public void setIdeDadoGeografico(Integer ideDadoGeografico) {
        this.ideDadoGeografico = ideDadoGeografico;
    }

    public String getCoordGeoNumerica() {
        return coordGeoNumerica;
    }

    public void setCoordGeoNumerica(String coordGeoNumerica) {
        this.coordGeoNumerica = coordGeoNumerica;
    }

    public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
        return ideLocalizacaoGeografica;
    }

    public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
        this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
    }
  
    @Override
    public DadoGeografico clone() throws CloneNotSupportedException {
    	return (DadoGeografico) super.clone();
    }
}