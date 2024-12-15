package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntityLocalizacaoGeografica;

@Entity
@Table(name = "declaracao_inexigibilidade_info_abastecimento")
@XmlRootElement
public class DeclaracaoInexigibilidadeInfoAbastecimento extends AbstractEntityLocalizacaoGeografica {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ide_declaracao_inexigibilidade_info_abastecimento")
	private Integer ideDeclaracaoInexigibilidadeInfoAbastecimento;
	
	@Column(name = "val_vazao")
	private BigDecimal valVazao;
	
	@Column(name = "nom_sistema")
	private String nomSistema;
	
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(optional = false)
	private LocalizacaoGeografica localizacaoGeografica;
	
	@JoinColumn(name = "ide_endereco", referencedColumnName = "ide_endereco")
	@ManyToOne(optional = false)
	private Endereco endereco;
	
	@JoinColumn(name = "ide_declaracao_inexigibilidade", referencedColumnName = "ide_declaracao_inexigibilidade")
	@ManyToOne(optional = false)
	private DeclaracaoInexigibilidade declaracaoInexigibilidade;
	
	public DeclaracaoInexigibilidadeInfoAbastecimento() {
		this.localizacaoGeografica = new LocalizacaoGeografica();
	}
	
	public Integer getIdeDeclaracaoInexigibilidadeInfoAbastecimento() {
		return ideDeclaracaoInexigibilidadeInfoAbastecimento;
	}
	
	public BigDecimal getValVazao() {
		return valVazao;
	}
	
	public String getNomSistema() {
		return nomSistema;
	}
	
	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}
	
	public DeclaracaoInexigibilidade getDeclaracaoInexigibilidade() {
		return declaracaoInexigibilidade;
	}
	
	public void setIdeDeclaracaoInexigibilidadeInfoAbastecimento(Integer ideDeclaracaoInexigibilidadeInfoAbastecimento) {
		this.ideDeclaracaoInexigibilidadeInfoAbastecimento = ideDeclaracaoInexigibilidadeInfoAbastecimento;
	}
	
	public void setValVazao(BigDecimal valVazao) {
		this.valVazao = valVazao;
	}
	
	public void setNomSistema(String nomSistema) {
		this.nomSistema = nomSistema;
	}
	
	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public void setDeclaracaoInexigibilidade(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		this.declaracaoInexigibilidade = declaracaoInexigibilidade;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((declaracaoInexigibilidade == null) ? 0 : declaracaoInexigibilidade.hashCode());
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((ideDeclaracaoInexigibilidadeInfoAbastecimento == null) ? 0 : ideDeclaracaoInexigibilidadeInfoAbastecimento.hashCode());
		result = prime * result + ((localizacaoGeografica == null) ? 0 : localizacaoGeografica.hashCode());
		result = prime * result + ((nomSistema == null) ? 0 : nomSistema.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		DeclaracaoInexigibilidadeInfoAbastecimento other = (DeclaracaoInexigibilidadeInfoAbastecimento) obj;
		if(declaracaoInexigibilidade == null) {
			if(other.declaracaoInexigibilidade != null) return false;
		} else if(!declaracaoInexigibilidade.equals(other.declaracaoInexigibilidade)) return false;
		if(endereco == null) {
			if(other.endereco != null) return false;
		} else if(!endereco.equals(other.endereco)) return false;
		if(ideDeclaracaoInexigibilidadeInfoAbastecimento == null) {
			if(other.ideDeclaracaoInexigibilidadeInfoAbastecimento != null) return false;
		} else if(!ideDeclaracaoInexigibilidadeInfoAbastecimento.equals(other.ideDeclaracaoInexigibilidadeInfoAbastecimento)) return false;
		if(localizacaoGeografica == null) {
			if(other.localizacaoGeografica != null) return false;
		} else if(!localizacaoGeografica.equals(other.localizacaoGeografica)) return false;
		if(nomSistema == null) {
			if(other.nomSistema != null) return false;
		} else if(!nomSistema.equals(other.nomSistema)) return false;
		return true;
	}
	
	@Override
	public List<LocalizacaoGeografica> getLocalizacoesGeograficas() {
		return Arrays.asList(localizacaoGeografica);
	}
	
	@Override
	public String toString() {
		return String.valueOf(ideDeclaracaoInexigibilidadeInfoAbastecimento);
	}
}
