package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoAtividadeInexigivelEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "atividade_inexigivel")
@XmlRootElement
public class AtividadeInexigivel extends AbstractEntity implements Cloneable {
	
	private static final long serialVersionUID = -4303169296439000132L;
	
	@Id
	@SequenceGenerator(name = "ATIVIDADE_INEXIGIVEL_SEQ", sequenceName = "ATIVIDADE_INEXIGIVEL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATIVIDADE_INEXIGIVEL_SEQ")
	@Basic(optional = false)
	@Column(name = "ide_atividade_inexigivel")
	private Integer ideAtividadeInexigivel;
	
	@Column(name = "nom_atividade_inexigivel", nullable = false)
	private String nomAtividadeInexigivel;
	
	@Column(name = "dtc_criacao", nullable = false)
	private Date dtcCriacao;
	
	@Column(name = "dtc_exclusao")
	private Date dtcExclusao;
	
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;
	
	@JoinColumn(name = "ide_tipo_atividade_inexigivel", referencedColumnName = "ide_tipo_atividade_inexigivel")
	@ManyToOne(optional = false)
	private TipoAtividadeInexigivel tipoAtividadeInexigivel;
	
	@Column(name = "permite_local_realizacao", nullable = false)
	private Boolean permiteLocalRealizacao;
	
	@Column(name = "permite_endereco", nullable = false)
	private Boolean permiteEndereco;
	
	@Column(name = "permite_projeto", nullable = false)
	private Boolean permiteProjeto;
	
	@Column(name = "PERMITE_QTD_BUEIROS", nullable = false)
	private Boolean permiteQtdBueiros;
	
	@Column(name = "PERMITE_UNIDADE", nullable = false)
	private Boolean permiteUnidade;
	
	@Column(name = "PERMITE_ABASTECIMENTO", nullable = false)
	private Boolean permiteAbastecimento;
	
	@Column(name = "PERMITE_PONTE", nullable = false)
	private Boolean permitePonte;
	
	@Column(name = "PERMITE_LUZ_PARA_TODOS", nullable = false)
	private Boolean permiteLuzParaTodos;
	
	@Column(name = "PERMITE_MUNICIPIO_EMERGENCIAL", nullable = false)
	private Boolean permiteMunicipioEmergencial;
	
	public AtividadeInexigivel() {
	}
	
	public AtividadeInexigivel(String string) {
		this.nomAtividadeInexigivel = string;
	}
	
	public Integer getIdeAtividadeInexigivel() {
		return ideAtividadeInexigivel;
	}
	
	public String getNomAtividadeInexigivel() {
		return nomAtividadeInexigivel;
	}
	
	public Date getDtcCriacao() {
		return dtcCriacao;
	}
	
	public Date getDtcExclusao() {
		return dtcExclusao;
	}
	
	public Boolean getIndAtivo() {
		return indAtivo;
	}
	
	public TipoAtividadeInexigivel getTipoAtividadeInexigivel() {
		return tipoAtividadeInexigivel;
	}
	
	public void setIdeAtividadeInexigivel(Integer ideAtividadeInexigivel) {
		this.ideAtividadeInexigivel = ideAtividadeInexigivel;
	}
	
	public void setNomAtividadeInexigivel(String nomAtividadeInexigivel) {
		this.nomAtividadeInexigivel = nomAtividadeInexigivel;
	}
	
	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}
	
	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}
	
	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	public void setTipoAtividadeInexigivel(TipoAtividadeInexigivel tipoAtividadeInexigivel) {
		this.tipoAtividadeInexigivel = tipoAtividadeInexigivel;
	}
	
	public Boolean getPermiteLocalRealizacao() {
		return permiteLocalRealizacao;
	}
	
	public void setPermiteLocalRealizacao(Boolean permiteLocalRealizacao) {
		this.permiteLocalRealizacao = permiteLocalRealizacao;
	}
	
	public boolean isPermiteInformarRioIntervencao() {
		if(getTipoAtividadeInexigivel().getIdeTipoAtividadeInexigivel().equals(TipoAtividadeInexigivelEnum.OUTORGA.getIdeTipoAtividadeInexigivel())) {
			return true;
		}
		
		return false;
	}
	
	public boolean isPermiteSupressaoVegetacao() {
		if(getTipoAtividadeInexigivel().getIdeTipoAtividadeInexigivel().equals(TipoAtividadeInexigivelEnum.LICENCA.getIdeTipoAtividadeInexigivel())) {
			return true;
		}
		
		return false;
	}
	
	public Boolean getPermiteEndereco() {
		return permiteEndereco;
	}
	
	public Boolean getPermiteProjeto() {
		return permiteProjeto;
	}
	
	public void setPermiteEndereco(Boolean permiteEndereco) {
		this.permiteEndereco = permiteEndereco;
	}
	
	public void setPermiteProjeto(Boolean permiteProjeto) {
		this.permiteProjeto = permiteProjeto;
	}
	
	public Boolean getPermiteQtdBueiros() {
		return permiteQtdBueiros;
	}
	
	public void setPermiteQtdBueiros(Boolean permiteQtdBueiros) {
		this.permiteQtdBueiros = permiteQtdBueiros;
	}
	
	public Boolean getPermiteUnidade() {
		return permiteUnidade;
	}
	
	public void setPermiteUnidade(Boolean permiteUnidade) {
		this.permiteUnidade = permiteUnidade;
	}
	
	public Boolean getPermiteAbastecimento() {
		return permiteAbastecimento;
	}
	
	public void setPermiteAbastecimento(Boolean permiteAbastecimento) {
		this.permiteAbastecimento = permiteAbastecimento;
	}
	
	public Boolean getPermitePonte() {
		return permitePonte;
	}
	
	public void setPermitePonte(Boolean permitePonte) {
		this.permitePonte = permitePonte;
	}
	
	public boolean isTipoAtividadeLicenca() {
		if(!Util.isNull(getTipoAtividadeInexigivel())
				&& TipoAtividadeInexigivelEnum.LICENCA.getIdeTipoAtividadeInexigivel().equals(getTipoAtividadeInexigivel().getIdeTipoAtividadeInexigivel())) {
			return true;
		}
		
		return false;
	}
	
	public boolean isTipoAtividadeOutorga() {
		if(!Util.isNull(getTipoAtividadeInexigivel())
				&& TipoAtividadeInexigivelEnum.OUTORGA.getIdeTipoAtividadeInexigivel().equals(getTipoAtividadeInexigivel().getIdeTipoAtividadeInexigivel())) {
			return true;
		}
		
		return false;
	}
	
	public Boolean getPermiteLuzParaTodos() {
		return permiteLuzParaTodos;
	}
	
	public void setPermiteLuzParaTodos(Boolean permiteLuzParaTodos) {
		this.permiteLuzParaTodos = permiteLuzParaTodos;
	}
	
	public Boolean getPermiteMunicipioEmergencial() {
		return permiteMunicipioEmergencial;
	}
	
	public void setPermiteMunicipioEmergencial(Boolean permiteMunicipioEmergencial) {
		this.permiteMunicipioEmergencial = permiteMunicipioEmergencial;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtcCriacao == null) ? 0 : dtcCriacao.hashCode());
		result = prime * result + ((dtcExclusao == null) ? 0 : dtcExclusao.hashCode());
		result = prime * result + ((ideAtividadeInexigivel == null) ? 0 : ideAtividadeInexigivel.hashCode());
		result = prime * result + ((indAtivo == null) ? 0 : indAtivo.hashCode());
		result = prime * result + ((nomAtividadeInexigivel == null) ? 0 : nomAtividadeInexigivel.hashCode());
		result = prime * result + ((permiteAbastecimento == null) ? 0 : permiteAbastecimento.hashCode());
		result = prime * result + ((permiteEndereco == null) ? 0 : permiteEndereco.hashCode());
		result = prime * result + ((permiteLocalRealizacao == null) ? 0 : permiteLocalRealizacao.hashCode());
		result = prime * result + ((permiteLuzParaTodos == null) ? 0 : permiteLuzParaTodos.hashCode());
		result = prime * result + ((permitePonte == null) ? 0 : permitePonte.hashCode());
		result = prime * result + ((permiteProjeto == null) ? 0 : permiteProjeto.hashCode());
		result = prime * result + ((permiteQtdBueiros == null) ? 0 : permiteQtdBueiros.hashCode());
		result = prime * result + ((permiteUnidade == null) ? 0 : permiteUnidade.hashCode());
		result = prime * result + ((tipoAtividadeInexigivel == null) ? 0 : tipoAtividadeInexigivel.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		AtividadeInexigivel other = (AtividadeInexigivel) obj;
		if(dtcCriacao == null) {
			if(other.dtcCriacao != null) return false;
		} else if(!dtcCriacao.equals(other.dtcCriacao)) return false;
		if(dtcExclusao == null) {
			if(other.dtcExclusao != null) return false;
		} else if(!dtcExclusao.equals(other.dtcExclusao)) return false;
		if(ideAtividadeInexigivel == null) {
			if(other.ideAtividadeInexigivel != null) return false;
		} else if(!ideAtividadeInexigivel.equals(other.ideAtividadeInexigivel)) return false;
		if(indAtivo == null) {
			if(other.indAtivo != null) return false;
		} else if(!indAtivo.equals(other.indAtivo)) return false;
		if(nomAtividadeInexigivel == null) {
			if(other.nomAtividadeInexigivel != null) return false;
		} else if(!nomAtividadeInexigivel.equals(other.nomAtividadeInexigivel)) return false;
		if(permiteAbastecimento == null) {
			if(other.permiteAbastecimento != null) return false;
		} else if(!permiteAbastecimento.equals(other.permiteAbastecimento)) return false;
		if(permiteEndereco == null) {
			if(other.permiteEndereco != null) return false;
		} else if(!permiteEndereco.equals(other.permiteEndereco)) return false;
		if(permiteLocalRealizacao == null) {
			if(other.permiteLocalRealizacao != null) return false;
		} else if(!permiteLocalRealizacao.equals(other.permiteLocalRealizacao)) return false;
		if(permiteLuzParaTodos == null) {
			if(other.permiteLuzParaTodos != null) return false;
		} else if(!permiteLuzParaTodos.equals(other.permiteLuzParaTodos)) return false;
		if(permitePonte == null) {
			if(other.permitePonte != null) return false;
		} else if(!permitePonte.equals(other.permitePonte)) return false;
		if(permiteProjeto == null) {
			if(other.permiteProjeto != null) return false;
		} else if(!permiteProjeto.equals(other.permiteProjeto)) return false;
		if(permiteQtdBueiros == null) {
			if(other.permiteQtdBueiros != null) return false;
		} else if(!permiteQtdBueiros.equals(other.permiteQtdBueiros)) return false;
		if(permiteUnidade == null) {
			if(other.permiteUnidade != null) return false;
		} else if(!permiteUnidade.equals(other.permiteUnidade)) return false;
		if(tipoAtividadeInexigivel == null) {
			if(other.tipoAtividadeInexigivel != null) return false;
		} else if(!tipoAtividadeInexigivel.equals(other.tipoAtividadeInexigivel)) return false;
		return true;
	}

}
