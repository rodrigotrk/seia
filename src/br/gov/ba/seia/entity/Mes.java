package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.MesEnum;

@Entity
@Table(name = "mes")
public class Mes extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "ide_mes", nullable = false)
    private Integer ideMes;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "cod_mes", nullable = false, length = 3)
    private String codMes;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "nom_mes", nullable = false, length = 10)
    private String nomMes;
    
    @OneToMany(mappedBy="ideMes")
	private Collection<CerhCaptacaoVazaoSazonalidade> cerhCaptacaoVazaoSazonalidadeCollection;

	@OneToMany(mappedBy="ideMes")
	private Collection<CerhLancamentoEfluenteSazonalidade> cerhLancamentoEfluenteSazonalidadeCollection;

    public Mes() {
    }

    public Mes(Integer ideMes) {
        this.ideMes = ideMes;
    }

    public Mes(Integer ideMes, String nomMes) {
		this.ideMes = ideMes;
		this.nomMes = nomMes;
	}

	public Mes(Integer ideMes, String codMes, String nomMes) {
        this.ideMes = ideMes;
        this.nomMes = nomMes;
        this.codMes = codMes;
    }

	public Mes(MesEnum mesEnum) {
		this(mesEnum.getValue(), mesEnum.toString());
	}

	public Integer getIdeMes() {
        return ideMes;
    }

    public void setIdeMes(Integer ideMes) {
        this.ideMes = ideMes;
    }

    public String getCodMes() {
        return codMes;
    }

    public void setCodMes(String codMes) {
        this.codMes = codMes;
    }

    public String getNomMes() {
        return nomMes;
    }

    public void setNomMes(String nomMes) {
        this.nomMes = nomMes;
    }
    
    public Collection<CerhCaptacaoVazaoSazonalidade> getCerhCaptacaoVazaoSazonalidadeCollection() {
		return cerhCaptacaoVazaoSazonalidadeCollection;
	}

	public void setCerhCaptacaoVazaoSazonalidadeCollection(Collection<CerhCaptacaoVazaoSazonalidade> cerhCaptacaoVazaoSazonalidadeCollection) {
		this.cerhCaptacaoVazaoSazonalidadeCollection = cerhCaptacaoVazaoSazonalidadeCollection;
	}

	public Collection<CerhLancamentoEfluenteSazonalidade> getCerhLancamentoEfluenteSazonalidadeCollection() {
		return cerhLancamentoEfluenteSazonalidadeCollection;
	}

	public void setCerhLancamentoEfluenteSazonalidadeCollection(Collection<CerhLancamentoEfluenteSazonalidade> cerhLancamentoEfluenteSazonalidadeCollection) {
		this.cerhLancamentoEfluenteSazonalidadeCollection = cerhLancamentoEfluenteSazonalidadeCollection;
	}
}
