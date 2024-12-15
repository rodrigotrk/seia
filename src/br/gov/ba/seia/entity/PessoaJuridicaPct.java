package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="pessoa_juridica_pct")
public class PessoaJuridicaPct extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoa_juridica_pct_seq")
	@SequenceGenerator(name = "pessoa_juridica_pct_seq", sequenceName = "pessoa_juridica_pct_seq", allocationSize = 1)
	@Column(name="ide_pessoa_juridica_pct", nullable=false)
	private Integer idePessoaJuridicaPct;
	
	@ManyToOne
	@JoinColumn(name="ide_pct", referencedColumnName="ide_pct", nullable=false)
	private PctImovelRural idePctImovelRural;
	
	@ManyToOne
	@JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica", nullable = false)
	private PessoaJuridica idePessoaJuridica;
	
	@ManyToOne
	@JoinColumn(name = "ide_tipo_pessoa_juridica_pct", referencedColumnName = "ide_tipo_pessoa_juridica_pct", nullable = false)
	private TipoPessoaJuridicaPct ideTipoPessoaJuridicaPct;
	
	@Column(name = "dtc_cadastro", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCadastro;
	
	@Column(name = "dtc_exclusao", nullable=true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
	
	@Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
	
	@Transient
	private PessoaFisica idePessoaFisicaRepresentanteLegal;
	
	@Transient
	private Telefone ideTelefone;
	
	@Transient
	private String numCpf;
	
	public PessoaJuridicaPct() {}
	
	public PessoaJuridicaPct clone() throws CloneNotSupportedException {
		return (PessoaJuridicaPct) super.clone();
		
	}
	
	public PessoaJuridicaPct(Integer idePessoaJuridicaPct) {
		this.idePessoaJuridicaPct = idePessoaJuridicaPct;
	}
	
	public Integer getIdePessoaJuridicaPct() {
		return idePessoaJuridicaPct;
	}

	public void setIdePessoaJuridicaPct(Integer idePessoaJuridicaPct) {
		this.idePessoaJuridicaPct = idePessoaJuridicaPct;
	}

	public PctImovelRural getIdePctImovelRural() {
		return idePctImovelRural;
	}

	public void setIdePctImovelRural(PctImovelRural pctImovelRural) {
		this.idePctImovelRural = pctImovelRural;
	}

	public PessoaJuridica getIdePessoaJuridica() {
		return idePessoaJuridica;
	}

	public void setIdePessoaJuridica(PessoaJuridica idePessoaJuridica) {
		this.idePessoaJuridica = idePessoaJuridica;
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

	public PessoaFisica getIdePessoaFisicaRepresentanteLegal() {
		return idePessoaFisicaRepresentanteLegal;
	}

	public void setIdePessoaFisicaRepresentanteLegal(
			PessoaFisica idePessoaFisicaRepresentanteLegal) {
		this.idePessoaFisicaRepresentanteLegal = idePessoaFisicaRepresentanteLegal;
	}

	public Telefone getIdeTelefone() {
		return ideTelefone;
	}

	public void setIdeTelefone(Telefone ideTelefone) {
		this.ideTelefone = ideTelefone;
	}

	public TipoPessoaJuridicaPct getIdeTipoPessoaJuridicaPct() {
		return ideTipoPessoaJuridicaPct;
	}

	public void setIdeTipoPessoaJuridicaPct(TipoPessoaJuridicaPct ideTipoPessoaJuridicaPct) {
		this.ideTipoPessoaJuridicaPct = ideTipoPessoaJuridicaPct;
	}

	public String getNumCpf() {
		return numCpf;
	}

	public void setNumCpf(String numCpf) {
		this.numCpf = numCpf;
	}
}
