package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "modelo_certificado_inexigibilidade")
public class ModeloCertificadoInexigibilidade extends AbstractEntity {
	
	private static final long serialVersionUID = -7179565180595482838L;
	
	@Id
	@Column(name = "ide_modelo_certificado_inexigibilidade")
	private Integer ideModeloCertificadoInexigibilidade;
	
	@Column(name = "codigo_modelo_certificado_inexigibilidade")
	@NotNull
	private String codigo;
	
	@Column(name = "dsc_modelo_certificado_inexigibilidade")
	@NotNull
	@Size(max = 255)
	private String descricao;
	
	@Column(name = "ind_ativo")
	@NotNull
	private Boolean indAtivo;
	
	@Column(name = "caminho_arquivo_modelo_certificado_inexigibilidade")
	@NotNull
	@Size(max = 255)
	private String caminhoArquivoModeloCertificadoInexigibilidade;
	
	public Integer getIdeModeloCertificadoInexigibilidade() {
		return ideModeloCertificadoInexigibilidade;
	}
	
	public void setIdeModeloCertificadoInexigibilidade(Integer ideModeloCertificadoInexigibilidade) {
		this.ideModeloCertificadoInexigibilidade = ideModeloCertificadoInexigibilidade;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Boolean getIndAtivo() {
		return indAtivo;
	}
	
	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	public String getCaminhoArquivoModeloCertificadoInexigibilidade() {
		return caminhoArquivoModeloCertificadoInexigibilidade;
	}
	
	public void setCaminhoArquivoModeloCertificadoInexigibilidade(String caminhoArquivoModeloCertificadoInexigibilidade) {
		this.caminhoArquivoModeloCertificadoInexigibilidade = caminhoArquivoModeloCertificadoInexigibilidade;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((ideModeloCertificadoInexigibilidade == null) ? 0 : ideModeloCertificadoInexigibilidade.hashCode());
		result = prime * result + ((indAtivo == null) ? 0 : indAtivo.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		ModeloCertificadoInexigibilidade other = (ModeloCertificadoInexigibilidade) obj;
		if(codigo == null) {
			if(other.codigo != null) return false;
		} else if(!codigo.equals(other.codigo)) return false;
		if(descricao == null) {
			if(other.descricao != null) return false;
		} else if(!descricao.equals(other.descricao)) return false;
		if(ideModeloCertificadoInexigibilidade == null) {
			if(other.ideModeloCertificadoInexigibilidade != null) return false;
		} else if(!ideModeloCertificadoInexigibilidade.equals(other.ideModeloCertificadoInexigibilidade)) return false;
		if(indAtivo == null) {
			if(other.indAtivo != null) return false;
		} else if(!indAtivo.equals(other.indAtivo)) return false;
		return true;
	}
}
