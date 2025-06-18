package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "vw_forma_manejo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwFormaManejo.findAll", query = "SELECT v FROM VwFormaManejo v"),
    @NamedQuery(name = "VwFormaManejo.findById", query = "SELECT v FROM VwFormaManejo v WHERE v.id = :id"),
    @NamedQuery(name = "VwFormaManejo.findByIdeTipologiaGrupo", query = "SELECT v FROM VwFormaManejo v WHERE v.ideTipologiaGrupo = :ideTipologiaGrupo"),
    @NamedQuery(name = "VwFormaManejo.findByIdeTipologia", query = "SELECT v FROM VwFormaManejo v WHERE v.ideTipologia = :ideTipologia"),
    @NamedQuery(name = "VwFormaManejo.findByNomTipoManejo", query = "SELECT v FROM VwFormaManejo v WHERE v.nomTipoManejo = :nomTipoManejo"),
    @NamedQuery(name = "VwFormaManejo.findByNomPotencialPoluicao", query = "SELECT v FROM VwFormaManejo v WHERE v.nomPotencialPoluicao = :nomPotencialPoluicao"),
    @NamedQuery(name = "VwFormaManejo.findByFGetUnidadesMedidas", query = "SELECT v FROM VwFormaManejo v WHERE v.nomUnidades = :nomUnidades"),
    @NamedQuery(name = "VwFormaManejo.findByFGetAreas", query = "SELECT v FROM VwFormaManejo v WHERE v.nomAreas = :nomAreas")})
public class VwFormaManejo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private BigInteger id;
    @Column(name = "ide_tipologia_grupo")
    private Integer ideTipologiaGrupo;
    @Column(name = "ide_tipologia")
    private Integer ideTipologia;
    @Size(max = 100)
    @Column(name = "nom_tipo_manejo", length = 100)
    private String nomTipoManejo;
    @Size(max = 25)
    @Column(name = "nom_potencial_poluicao", length = 25)
    private String nomPotencialPoluicao;
    @Size(max = 2147483647)
    @Column(name = "nomUnidades", length = 2147483647)
    private String nomUnidades;
    @Size(max = 2147483647)
    @Column(name = "nomAreas", length = 2147483647)
    private String nomAreas;

    public VwFormaManejo() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Integer getIdeTipologiaGrupo() {
        return ideTipologiaGrupo;
    }

    public void setIdeTipologiaGrupo(Integer ideTipologiaGrupo) {
        this.ideTipologiaGrupo = ideTipologiaGrupo;
    }

    public Integer getIdeTipologia() {
        return ideTipologia;
    }

    public void setIdeTipologia(Integer ideTipologia) {
        this.ideTipologia = ideTipologia;
    }

    public String getNomTipoManejo() {
        return nomTipoManejo;
    }

    public void setNomTipoManejo(String nomTipoManejo) {
        this.nomTipoManejo = nomTipoManejo;
    }

    public String getNomPotencialPoluicao() {
        return nomPotencialPoluicao;
    }

    public void setNomPotencialPoluicao(String nomPotencialPoluicao) {
        this.nomPotencialPoluicao = nomPotencialPoluicao;
    }

    public String getNomUnidades() {
        return nomUnidades;
    }

    public void setNomUnidades(String nomUnidades) {
        this.nomUnidades = nomUnidades;
    }

    public String getNomAreas() {
        return nomAreas;
    }

    public void setNomAreas(String nomAreas) {
        this.nomAreas = nomAreas;
    }
    
}
