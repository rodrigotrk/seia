package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="pct_familia")
public class PctFamilia extends AbstractEntity {

	private static final long serialVersionUID = 764576021779999112L;
	
	@Id
	@SequenceGenerator(name = "pct_familia_generator", sequenceName = "pct_familia_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pct_familia_generator")
	@Column(name="ide_pct_familia")
	private Integer idePctFamilia;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pct", referencedColumnName="ide_pct", nullable=false)
	private PctImovelRural idePctImovelRural;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pessoa", referencedColumnName="ide_pessoa", nullable=false)
	private Pessoa idePessoa;
	
	@Column(name = "dtc_cadastro", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCadastro;
	
	@Column(name = "dtc_exclusao", nullable=true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
	
	@Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pessoa_associada", referencedColumnName="ide_pessoa", nullable=false)
	private Pessoa idePessoaAssociada;
	
	@Transient
	private Collection<PctFamilia> membrosFamiliaCollection;

	public Integer getIdePctFamilia() {
		return idePctFamilia;
	}

	public void setIdePctFamilia(Integer idePctFamilia) {
		this.idePctFamilia = idePctFamilia;
	}

	public PctImovelRural getIdePctImovelRural() {
		return idePctImovelRural;
	}

	public void setIdePctImovelRural(PctImovelRural idePct) {
		this.idePctImovelRural = idePct;
	}

	public Pessoa getIdePessoa() {
		return idePessoa;
	}

	public void setIdePessoa(Pessoa idePessoa) {
		this.idePessoa = idePessoa;
	}

	public Date getDtcCadastro() {
		return dtcCadastro;
	}

	public void setDtcCadastro(Date dtcCadastro) {
		this.dtcCadastro = dtcCadastro;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Pessoa getIdePessoaAssociada() {
		return idePessoaAssociada;
	}

	public void setIdePessoaAssociada(Pessoa idePessoaAssociada) {
		this.idePessoaAssociada = idePessoaAssociada;
	}

	public Collection<PctFamilia> getMembrosFamiliaCollection() {
		return membrosFamiliaCollection;
	}

	public void setMembrosFamiliaCollection(
			Collection<PctFamilia> membrosFamiliaCollection) {
		this.membrosFamiliaCollection = membrosFamiliaCollection;
	}
	
}
