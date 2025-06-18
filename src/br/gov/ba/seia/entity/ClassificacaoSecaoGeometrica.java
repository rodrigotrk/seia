package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "classificacao_secao_geometrica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClassificacaoSecaoGeometrica.findAll", query = "SELECT c FROM ClassificacaoSecaoGeometrica c"),
    @NamedQuery(name = "ClassificacaoSecaoGeometrica.findByIdeClassificacaoSecao", query = "SELECT c FROM ClassificacaoSecaoGeometrica c WHERE c.ideClassificacaoSecao = :ideClassificacaoSecao"),
    @NamedQuery(name = "ClassificacaoSecaoGeometrica.findByNomClassificacaoSecao", query = "SELECT c FROM ClassificacaoSecaoGeometrica c WHERE c.nomClassificacaoSecao = :nomClassificacaoSecao")})
public class ClassificacaoSecaoGeometrica extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_classificacao_secao")
	private Integer ideClassificacaoSecao;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 25)
	@Column(name = "nom_classificacao_secao")
	private String nomClassificacaoSecao;
	
	@OneToMany(mappedBy = "ideClassificacaoSecao", fetch = FetchType.LAZY)
	private Collection<LocalizacaoGeografica> localizacaoGeograficaCollection;

    public ClassificacaoSecaoGeometrica() {
    }

    public ClassificacaoSecaoGeometrica(Integer ideClassificacaoSecao) {
        this.ideClassificacaoSecao = ideClassificacaoSecao;
    }

    public ClassificacaoSecaoGeometrica(Integer ideClassificacaoSecao, String nomClassificacaoSecao) {
        this.ideClassificacaoSecao = ideClassificacaoSecao;
        this.nomClassificacaoSecao = nomClassificacaoSecao;
    }

    public Integer getIdeClassificacaoSecao() {
        return ideClassificacaoSecao;
    }

    public void setIdeClassificacaoSecao(Integer ideClassificacaoSecao) {
        this.ideClassificacaoSecao = ideClassificacaoSecao;
    }

    public String getNomClassificacaoSecao() {
        return nomClassificacaoSecao;
    }

    public void setNomClassificacaoSecao(String nomClassificacaoSecao) {
        this.nomClassificacaoSecao = nomClassificacaoSecao;
    }

    @XmlTransient
    public Collection<LocalizacaoGeografica> getLocalizacaoGeograficaCollection() {
        return localizacaoGeograficaCollection;
    }

    public void setLocalizacaoGeograficaCollection(Collection<LocalizacaoGeografica> localizacaoGeograficaCollection) {
        this.localizacaoGeograficaCollection = localizacaoGeograficaCollection;
    }
    
    public Boolean isPonto() {
    	return this.ideClassificacaoSecao.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId());
    }
    
    public Boolean isShapeFile() {
    	return this.ideClassificacaoSecao.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId());
    }
    
    public Boolean isDesenho() {
    	return this.ideClassificacaoSecao.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_DESENHO.getId());
    }
}
