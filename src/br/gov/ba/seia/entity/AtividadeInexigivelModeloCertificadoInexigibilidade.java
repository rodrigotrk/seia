package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "atividade_inexigivel_modelo_certificado_inexigibilidade")
public class AtividadeInexigivelModeloCertificadoInexigibilidade extends AbstractEntity {
	
	private static final long serialVersionUID = -3500788214603403404L;
	
	@Id
	@SequenceGenerator(name = "ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO_INEXIGIBILIDADE_SEQ", sequenceName = "ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO_INEXIGIBILIDADE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATIVIDADE_INEXIGIVEL_MODELO_CERTIFICADO_INEXIGIBILIDADE_SEQ")
	@Column(name = "ide_atividade_inexigivel_modelo_certificado")
	private Integer ideModeloCertificadoInexigibilidade;
	
	@JoinColumn(name = "ide_modelo_certificado_inexigibilidade", referencedColumnName = "ide_modelo_certificado_inexigibilidade")
	@ManyToOne(optional = false)
	private ModeloCertificadoInexigibilidade modeloCertificadoInexigibilidade;
	
	@JoinColumn(name = "ide_atividade_inexigivel", referencedColumnName = "ide_atividade_inexigivel")
	@ManyToOne(optional = false)
	private AtividadeInexigivel atividadeInexigivel;
	
	public Integer getIdeModeloCertificadoInexigibilidade() {
		return ideModeloCertificadoInexigibilidade;
	}
	
	public void setIdeModeloCertificadoInexigibilidade(Integer ideModeloCertificadoInexigibilidade) {
		this.ideModeloCertificadoInexigibilidade = ideModeloCertificadoInexigibilidade;
	}
	
	public ModeloCertificadoInexigibilidade getModeloCertificadoInexigibilidade() {
		return modeloCertificadoInexigibilidade;
	}
	
	public void setModeloCertificadoInexigibilidade(ModeloCertificadoInexigibilidade modeloCertificadoInexigibilidade) {
		this.modeloCertificadoInexigibilidade = modeloCertificadoInexigibilidade;
	}
	
	public AtividadeInexigivel getAtividadeInexigivel() {
		return atividadeInexigivel;
	}
	
	public void setAtividadeInexigivel(AtividadeInexigivel atividadeInexigivel) {
		this.atividadeInexigivel = atividadeInexigivel;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((atividadeInexigivel == null) ? 0 : atividadeInexigivel.hashCode());
		result = prime * result + ((ideModeloCertificadoInexigibilidade == null) ? 0 : ideModeloCertificadoInexigibilidade.hashCode());
		result = prime * result + ((modeloCertificadoInexigibilidade == null) ? 0 : modeloCertificadoInexigibilidade.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!super.equals(obj)) return false;
		if(getClass() != obj.getClass()) return false;
		AtividadeInexigivelModeloCertificadoInexigibilidade other = (AtividadeInexigivelModeloCertificadoInexigibilidade) obj;
		if(atividadeInexigivel == null) {
			if(other.atividadeInexigivel != null) return false;
		} else if(!atividadeInexigivel.equals(other.atividadeInexigivel)) return false;
		if(ideModeloCertificadoInexigibilidade == null) {
			if(other.ideModeloCertificadoInexigibilidade != null) return false;
		} else if(!ideModeloCertificadoInexigibilidade.equals(other.ideModeloCertificadoInexigibilidade)) return false;
		if(modeloCertificadoInexigibilidade == null) {
			if(other.modeloCertificadoInexigibilidade != null) return false;
		} else if(!modeloCertificadoInexigibilidade.equals(other.modeloCertificadoInexigibilidade)) return false;
		return true;
	}
}
