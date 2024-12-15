package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;

/**
 * @author eduardo.fernandes
 * @since 16/01/2017
 * @see <a href="http://10.105.12.26/redmine/issues/8263">#8263</a>
 */
@Entity
@Table(name="ato_declaratorio")
@NamedQuery(name="AtoDeclaratorio.findAll", query="SELECT a FROM AtoDeclaratorio a")
public class AtoDeclaratorio extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="ATO_DECLARATORIO_GENERATOR", sequenceName="ATO_DECLARATORIO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ATO_DECLARATORIO_GENERATOR")
	@Column(name="ide_ato_declaratorio")
	private Integer ideAtoDeclaratorio;
	
	@Column(name="dtc_criacao")
	private Date dtcCriacao;
	
	@Column(name="ind_concluido")
	private Boolean indConcluido;
	
	@JoinColumn(name = "ide_documento_obrigatorio", referencedColumnName = "ide_documento_obrigatorio")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private DocumentoObrigatorio ideDocumentoObrigatorio;
	
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;
	
	@OneToOne(mappedBy = "atoDeclaratorio", fetch = FetchType.LAZY)
	private DeclaracaoTransporte declaracaoTransporte;
	
	@Transient
	private boolean desabilitaTudo;
	
	public AtoDeclaratorio() {}
	
	public AtoDeclaratorio(Requerimento req) {
		this.ideRequerimento = req;
	}
	
	public AtoDeclaratorio(Requerimento requerimento, DocumentoObrigatorioEnum docObrigatorioEnum) {
		this.ideRequerimento = requerimento;
		this.ideDocumentoObrigatorio = new DocumentoObrigatorio(docObrigatorioEnum.getId());
		this.dtcCriacao = new Date();
		this.indConcluido = false;
	}
	

	
	public Integer getIdeAtoDeclaratorio() {
		return this.ideAtoDeclaratorio;
	}
	
	public void setIdeAtoDeclaratorio(Integer ideAtoDeclaratorio) {
		this.ideAtoDeclaratorio = ideAtoDeclaratorio;
	}
	
	public Date getDtcCriacao() {
		return this.dtcCriacao;
	}
	
	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}
	
	public DocumentoObrigatorio getIdeDocumentoObrigatorio() {
		return this.ideDocumentoObrigatorio;
	}
	
	public void setIdeDocumentoObrigatorio(DocumentoObrigatorio ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}
	
	public Requerimento getIdeRequerimento() {
		return this.ideRequerimento;
	}
	
	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}
	
	public Boolean getIndConcluido() {
		return this.indConcluido;
	}
	
	public void setIndConcluido(Boolean indConcluido) {
		this.indConcluido = indConcluido;
	}
	
	public boolean isDesabilitaTudo() {
		return desabilitaTudo;
	}
	
	public void setDesabilitaTudo(boolean desabilitaTudo) {
		this.desabilitaTudo = desabilitaTudo;
	}
	
	public DeclaracaoTransporte getDeclaracaoTransporte() {
		return declaracaoTransporte;
	}
	
	public void setDeclaracaoTransporte(DeclaracaoTransporte declaracaoTransporte) {
		this.declaracaoTransporte = declaracaoTransporte;
	}
}