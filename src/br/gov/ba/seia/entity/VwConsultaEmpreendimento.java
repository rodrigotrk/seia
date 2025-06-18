package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "vw_consulta_empreendimento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwConsultaEmpreendimento.findAll", query = "SELECT v FROM VwConsultaEmpreendimento v"),
    @NamedQuery(name = "VwConsultaEmpreendimento.findByIdeEmpreendimento", query = "SELECT v FROM VwConsultaEmpreendimento v WHERE v.ideEmpreendimento = :ideEmpreendimento"),
    @NamedQuery(name = "VwConsultaEmpreendimento.findByNomEmpreendimento", query = "SELECT v FROM VwConsultaEmpreendimento v WHERE v.nomEmpreendimento = :nomEmpreendimento"),
    @NamedQuery(name = "VwConsultaEmpreendimento.findByIdePessoa", query = "SELECT v FROM VwConsultaEmpreendimento v WHERE v.idePessoa = :idePessoa"),
    @NamedQuery(name = "VwConsultaEmpreendimento.findByNomPessoa", query = "SELECT v FROM VwConsultaEmpreendimento v WHERE v.nomPessoa = :nomPessoa"),
    @NamedQuery(name = "VwConsultaEmpreendimento.findByNomRazaoSocial", query = "SELECT v FROM VwConsultaEmpreendimento v WHERE v.nomRazaoSocial = :nomRazaoSocial"),
    @NamedQuery(name = "VwConsultaEmpreendimento.findByIdeMunicipio", query = "SELECT v FROM VwConsultaEmpreendimento v WHERE v.ideMunicipio = :ideMunicipio"),
    @NamedQuery(name = "VwConsultaEmpreendimento.findByNomMunicipio", query = "SELECT v FROM VwConsultaEmpreendimento v WHERE v.nomMunicipio = :nomMunicipio")})
public class VwConsultaEmpreendimento implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @Column(name = "ide_empreendimento")
    private Integer ideEmpreendimento;
    
	@Size(max = 200)
    @Column(name = "nom_empreendimento", length = 200)
    private String nomEmpreendimento;
    
	@Column(name = "ide_pessoa")
    private Integer idePessoa;
    
	@Size(max = 200)
    @Column(name = "nom_pessoa", length = 200)
    private String nomPessoa;
    
	@Size(max = 200)
    @Column(name = "nom_razao_social", length = 200)
    private String nomRazaoSocial;
    
	@Column(name = "ide_municipio")
    private Integer ideMunicipio;
    
	@Size(max = 60)
    @Column(name = "nom_municipio", length = 60)
    private String nomMunicipio;
    
    @Transient
    private Empreendimento empreendimento;
    
    public Empreendimento getEmpreendimentoCarregado() {
    	empreendimento = new Empreendimento(ideEmpreendimento);
    	empreendimento.setNomEmpreendimento(nomEmpreendimento);
    	empreendimento.setIdePessoa(new Pessoa(idePessoa));
    	empreendimento.setMunicipio(new Municipio(ideMunicipio));
    	
		return empreendimento;
    }

    public VwConsultaEmpreendimento() {}

    public Integer getIdeEmpreendimento() {
        return ideEmpreendimento;
    }
    
    public void setIdeEmpreendimento(Integer ideEmpreendimento) {
        this.ideEmpreendimento = ideEmpreendimento;
    }

    public String getNomEmpreendimento() {
        return nomEmpreendimento;
    }
    
    public void setNomEmpreendimento(String nomEmpreendimento) {
        this.nomEmpreendimento = nomEmpreendimento;
    }

    public Integer getIdePessoa() {
        return idePessoa;
    }
    
    public void setIdePessoa(Integer idePessoa) {
        this.idePessoa = idePessoa;
    }

    public String getNomPessoa() {
        return nomPessoa;
    }
    
    public void setNomPessoa(String nomPessoa) {
        this.nomPessoa = nomPessoa;
    }

    public String getNomRazaoSocial() {
        return nomRazaoSocial;
    }
    
    public void setNomRazaoSocial(String nomRazaoSocial) {
        this.nomRazaoSocial = nomRazaoSocial;
    }

    public Integer getIdeMunicipio() {
        return ideMunicipio;
    }
    
    public void setIdeMunicipio(Integer ideMunicipio) {
        this.ideMunicipio = ideMunicipio;
    }

    public String getNomMunicipio() {
        return nomMunicipio;
    }
    
    public void setNomMunicipio(String nomMunicipio) {
        this.nomMunicipio = nomMunicipio;
    }

	public Empreendimento getEmpreendimento() {
		this.empreendimento = new Empreendimento(this.ideEmpreendimento);
		return this.empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}
}