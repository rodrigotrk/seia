package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "requerimento_tipologia")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "RequerimentoTipologia.findAll", query = "SELECT r FROM RequerimentoTipologia r"),
		@NamedQuery(name = "RequerimentoTipologia.findByIdeRequerimentoTipologia", query = "SELECT r FROM RequerimentoTipologia r WHERE r.ideRequerimentoTipologia = :ideRequerimentoTipologia"),
		@NamedQuery(name = "RequerimentoTipologia.findByIdeRequerimento", query = "SELECT r FROM RequerimentoTipologia r WHERE r.ideRequerimento.ideRequerimento = :ideRequerimento"),
		@NamedQuery(name = "RequerimentoTipologia.findByValAtividade", query = "SELECT r FROM RequerimentoTipologia r WHERE r.valAtividade = :valAtividade") })
public class RequerimentoTipologia implements Serializable, BaseEntity, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "REQUERIMENTO_TIPOLOGIA_IDEREQUERIMENTOTIPOLOGIA_GENERATOR", sequenceName = "REQUERIMENTO_TIPOLOGIA_IDE_REQUERIMENTO_TIPOLOGIA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUERIMENTO_TIPOLOGIA_IDEREQUERIMENTOTIPOLOGIA_GENERATOR")
	@Column(name = "ide_requerimento_tipologia")
	private Integer ideRequerimentoTipologia;
	
	@Basic(optional = false)
	@Column(name = "val_atividade", nullable = true, precision = 14, scale = 3)
	private BigDecimal valAtividade;
	
	@Basic(optional = false)
	@Column(name = "ind_tipologia_principal", nullable = false)
	private boolean indTipologiaPrincipal;
	
	@Basic(optional = false)
	@Column(name = "ind_acao_tipologia", nullable = false)
	private Integer indAcaoTipologia;
	
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;
	
	@JoinColumn(name = "ide_unidade_medida_tipologia_grupo", referencedColumnName = "ide_unidade_medida_tipologia_grupo", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private UnidadeMedidaTipologiaGrupo ideUnidadeMedidaTipologiaGrupo;
	
	@Transient
	private Boolean preenchidoUsuario;
	@Transient
	private boolean checkboxPrincipalAtivo;
	@Transient
	private Porte porte;
	@Transient
	private String valAtividadeString;

	public RequerimentoTipologia() {
		preenchidoUsuario = false;
	}

	public RequerimentoTipologia(Integer ideRequerimentoTipologia) {
		this.ideRequerimentoTipologia = ideRequerimentoTipologia;
	}

	public Integer getIdeRequerimentoTipologia() {
		return ideRequerimentoTipologia;
	}

	public void setIdeRequerimentoTipologia(Integer ideRequerimentoTipologia) {
		this.ideRequerimentoTipologia = ideRequerimentoTipologia;
	}

	public BigDecimal getValAtividade() {
		return valAtividade;
	}

	public void setValAtividade(BigDecimal valAtividade) {
		this.valAtividade = valAtividade;
	}

	public UnidadeMedidaTipologiaGrupo getIdeUnidadeMedidaTipologiaGrupo() {
		return ideUnidadeMedidaTipologiaGrupo;
	}

	public void setIdeUnidadeMedidaTipologiaGrupo(UnidadeMedidaTipologiaGrupo ideUnidadeMedidaTipologiaGrupo) {
		this.ideUnidadeMedidaTipologiaGrupo = ideUnidadeMedidaTipologiaGrupo;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public boolean isIndTipologiaPrincipal() {
		return indTipologiaPrincipal;
	}

	public void setIndTipologiaPrincipal(boolean indTipologiaPrincipal) {
		this.indTipologiaPrincipal = indTipologiaPrincipal;
	}

	public Boolean getPreenchidoUsuario() {
		return preenchidoUsuario;
	}

	public void setPreenchidoUsuario(Boolean preenchidoUsuario) {
		this.preenchidoUsuario = preenchidoUsuario;
	}

	public boolean isCheckboxPrincipalAtivo() {
		return checkboxPrincipalAtivo;
	}

	public void setCheckboxPrincipalAtivo(boolean checkboxPrincipalAtivo) {
		this.checkboxPrincipalAtivo = checkboxPrincipalAtivo;
	}

	public Porte getPorte() {
		return porte;
	}

	public void setPorte(Porte porte) {
		this.porte = porte;
	}

	public String getValAtividadeString() {
		return valAtividadeString;
	}

	public void setValAtividadeString(String valAtividadeString) {
		this.valAtividadeString = valAtividadeString;
	}

	public Integer getIndAcaoTipologia() {
		return indAcaoTipologia;
	}

	public void setIndAcaoTipologia(Integer indAcaoTipologia) {
		this.indAcaoTipologia = indAcaoTipologia;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideRequerimentoTipologia != null ? ideRequerimentoTipologia.hashCode() : 0);
		return hash;
	}
	
	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof RequerimentoTipologia)) {
			return false;
		}
		RequerimentoTipologia other = (RequerimentoTipologia) object;
		if ((this.ideRequerimentoTipologia == null && other.ideRequerimentoTipologia != null)
				|| (this.ideRequerimentoTipologia != null && !this.ideRequerimentoTipologia
				.equals(other.ideRequerimentoTipologia))) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return String.valueOf(ideRequerimentoTipologia);
	}
	
	@Override
	public RequerimentoTipologia clone() throws CloneNotSupportedException {
		return (RequerimentoTipologia) super.clone();
	}

	@Override
	public Long getId() {
		return Long.valueOf(ideRequerimentoTipologia);
	}
}
