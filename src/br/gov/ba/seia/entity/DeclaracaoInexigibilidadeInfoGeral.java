package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "declaracao_inexigibilidade_info_geral")
@XmlRootElement
public class DeclaracaoInexigibilidadeInfoGeral extends AbstractEntity {
	
	private static final long serialVersionUID = 2250247263989875103L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_declaracao_inexigibilidade_info_geral", nullable = false)
	private Integer ideDeclaracaoInexigibilidadeInfoGeral;
	
	@Column(name = "ind_luz_para_todos")
	private Boolean indLuzParaTodos;
	
	@Column(name = "ind_asv_atos_autorizativos")
	private Boolean indAsvAtosAutorizativos;
	
	@JoinColumn(name = "ide_declaracao_inexigibilidade", referencedColumnName = "ide_declaracao_inexigibilidade", nullable = false)
	@ManyToOne(optional = false)
	private DeclaracaoInexigibilidade declaracaoInexigibilidade;
	
	@JoinColumn(name = "ide_tipo_local_atividade_inexigivel", referencedColumnName = "ide_tipo_local_atividade_inexigivel", nullable = true)
	@ManyToOne(optional = false)
	private TipoLocalAtividadeInexigivel tipoLocalAtividadeInexigivel;
	
	@JoinColumn(name = "ide_tipo_rio_intervencao", referencedColumnName = "ide_tipo_rio_intervencao", nullable = true)
	@ManyToOne(optional = false)
	private TipoRioIntervencao tipoRioIntervencao;
	
	@Column(name = "ind_sistema_simplificado_abastecimento")
	private Boolean indSistemaSimplificadoAbastecimento;
	
	@JoinColumn(name = "ide_endereco", referencedColumnName = "ide_endereco")
	@ManyToOne(optional = false)
	private Endereco endereco;
	
	public Integer getIdeDeclaracaoInexigibilidadeInfoGeral() {
		return ideDeclaracaoInexigibilidadeInfoGeral;
	}
	
	public Boolean getIndLuzParaTodos() {
		return indLuzParaTodos;
	}
	
	public Boolean getIndAsvAtosAutorizativos() {
		return indAsvAtosAutorizativos;
	}
	
	public DeclaracaoInexigibilidade getDeclaracaoInexigibilidade() {
		return declaracaoInexigibilidade;
	}
	
	public TipoLocalAtividadeInexigivel getTipoLocalAtividadeInexigivel() {
		return tipoLocalAtividadeInexigivel;
	}
	
	public TipoRioIntervencao getTipoRioIntervencao() {
		return tipoRioIntervencao;
	}
	
	public Boolean getIndSistemaSimplificadoAbastecimento() {
		return indSistemaSimplificadoAbastecimento;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}
	
	public void setIdeDeclaracaoInexigibilidadeInfoGeral(Integer ideDeclaracaoInexigibilidadeInfoGeral) {
		this.ideDeclaracaoInexigibilidadeInfoGeral = ideDeclaracaoInexigibilidadeInfoGeral;
	}
	
	public void setIndLuzParaTodos(Boolean indLuzParaTodos) {
		this.indLuzParaTodos = indLuzParaTodos;
	}
	
	public void setIndAsvAtosAutorizativos(Boolean indAsvAtosAutorizativos) {
		this.indAsvAtosAutorizativos = indAsvAtosAutorizativos;
	}
	
	public void setDeclaracaoInexigibilidade(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		this.declaracaoInexigibilidade = declaracaoInexigibilidade;
	}
	
	public void setTipoLocalAtividadeInexigivel(TipoLocalAtividadeInexigivel tipoLocalAtividadeInexigivel) {
		this.tipoLocalAtividadeInexigivel = tipoLocalAtividadeInexigivel;
	}
	
	public void setTipoRioIntervencao(TipoRioIntervencao tipoRioIntervencao) {
		this.tipoRioIntervencao = tipoRioIntervencao;
	}
	
	public void setIndSistemaSimplificadoAbastecimento(Boolean indSistemaSimplificadoAbastecimento) {
		this.indSistemaSimplificadoAbastecimento = indSistemaSimplificadoAbastecimento;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((declaracaoInexigibilidade == null) ? 0 : declaracaoInexigibilidade.hashCode());
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((ideDeclaracaoInexigibilidadeInfoGeral == null) ? 0 : ideDeclaracaoInexigibilidadeInfoGeral.hashCode());
		result = prime * result + ((indAsvAtosAutorizativos == null) ? 0 : indAsvAtosAutorizativos.hashCode());
		result = prime * result + ((indLuzParaTodos == null) ? 0 : indLuzParaTodos.hashCode());
		result = prime * result + ((indSistemaSimplificadoAbastecimento == null) ? 0 : indSistemaSimplificadoAbastecimento.hashCode());
		result = prime * result + ((tipoLocalAtividadeInexigivel == null) ? 0 : tipoLocalAtividadeInexigivel.hashCode());
		result = prime * result + ((tipoRioIntervencao == null) ? 0 : tipoRioIntervencao.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		DeclaracaoInexigibilidadeInfoGeral other = (DeclaracaoInexigibilidadeInfoGeral) obj;
		if(declaracaoInexigibilidade == null) {
			if(other.declaracaoInexigibilidade != null) return false;
		} else if(!declaracaoInexigibilidade.equals(other.declaracaoInexigibilidade)) return false;
		if(endereco == null) {
			if(other.endereco != null) return false;
		} else if(!endereco.equals(other.endereco)) return false;
		if(ideDeclaracaoInexigibilidadeInfoGeral == null) {
			if(other.ideDeclaracaoInexigibilidadeInfoGeral != null) return false;
		} else if(!ideDeclaracaoInexigibilidadeInfoGeral.equals(other.ideDeclaracaoInexigibilidadeInfoGeral)) return false;
		if(indAsvAtosAutorizativos == null) {
			if(other.indAsvAtosAutorizativos != null) return false;
		} else if(!indAsvAtosAutorizativos.equals(other.indAsvAtosAutorizativos)) return false;
		if(indLuzParaTodos == null) {
			if(other.indLuzParaTodos != null) return false;
		} else if(!indLuzParaTodos.equals(other.indLuzParaTodos)) return false;
		if(indSistemaSimplificadoAbastecimento == null) {
			if(other.indSistemaSimplificadoAbastecimento != null) return false;
		} else if(!indSistemaSimplificadoAbastecimento.equals(other.indSistemaSimplificadoAbastecimento)) return false;
		if(tipoLocalAtividadeInexigivel == null) {
			if(other.tipoLocalAtividadeInexigivel != null) return false;
		} else if(!tipoLocalAtividadeInexigivel.equals(other.tipoLocalAtividadeInexigivel)) return false;
		if(tipoRioIntervencao == null) {
			if(other.tipoRioIntervencao != null) return false;
		} else if(!tipoRioIntervencao.equals(other.tipoRioIntervencao)) return false;
		return true;
	}
}
