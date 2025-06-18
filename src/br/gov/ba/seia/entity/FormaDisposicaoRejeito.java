package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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

import br.gov.ba.seia.enumerator.TipoEstruturaEnum;
import br.gov.ba.seia.util.Util;

/**
 * The persistent class for the forma_disposicao_rejeitos database table.
 *
 */
@Entity
@Table(name = "forma_disposicao_rejeitos")
@NamedQueries({
	@NamedQuery(name = "FormaDisposicaoRejeito.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FormaDisposicaoRejeito f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral"),	
	@NamedQuery(name = "FormaDisposicaoRejeito.removeByIdeFormaDisposicaoRejeito", query = "DELETE FROM FormaDisposicaoRejeito f WHERE f.ideFormaDisposicaoRejeito = :ideFormaDisposicaoRejeito")	
})
public class FormaDisposicaoRejeito implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "FORMA_DISPOSICAO_REJEITOS_IDEFORMADISPOSICAOREJEITO_GENERATOR", sequenceName = "FORMA_DISPOSICAO_REJEITO_IDE_FORMA_DISPOSICAO_REJEITO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORMA_DISPOSICAO_REJEITOS_IDEFORMADISPOSICAOREJEITO_GENERATOR")
	@Column(name = "ide_forma_disposicao_rejeito")
	private Integer ideFormaDisposicaoRejeito;

	@Column(name = "area_ocupada")
	private BigDecimal areaOcupada;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_residuo_gerado")
	private TipoResiduoGerado tipoResiduoGerado;

	@Column(name = "ind_sistema_impermeabilizacao")
	private Boolean indSistemaImpermeabilizacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_classificacao_rejeito_dnpm")
	private ClassificacaoRejeitoDnpm classificacaoRejeitoDnpm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral")
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_estrutura")
	private TipoEstrutura tipoEstrutura;

	public FormaDisposicaoRejeito() {
	}

	public FormaDisposicaoRejeito(FceLicenciamentoMineral ideFceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public Integer getIdeFormaDisposicaoRejeito() {
		return this.ideFormaDisposicaoRejeito;
	}

	public void setIdeFormaDisposicaoRejeito(Integer ideFormaDisposicaoRejeito) {
		this.ideFormaDisposicaoRejeito = ideFormaDisposicaoRejeito;
	}

	public BigDecimal getAreaOcupada() {
		return this.areaOcupada;
	}

	public void setAreaOcupada(BigDecimal areaOcupada) {
		this.areaOcupada = areaOcupada;
	}

	public Boolean getIndSistemaImpermeabilizacao() {
		return this.indSistemaImpermeabilizacao;
	}

	public void setIndSistemaImpermeabilizacao(Boolean indSistemaImpermeabilizacao) {
		this.indSistemaImpermeabilizacao = indSistemaImpermeabilizacao;
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return this.fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public TipoEstrutura getTipoEstrutura() {
		return this.tipoEstrutura;
	}

	public void setTipoEstrutura(TipoEstrutura tipoEstrutura) {
		this.tipoEstrutura = tipoEstrutura;
	}

	public TipoResiduoGerado getTipoResiduoGerado() {
		return tipoResiduoGerado;
	}

	public void setTipoResiduoGerado(TipoResiduoGerado tipoResiduoGerado) {
		this.tipoResiduoGerado = tipoResiduoGerado;
	}

	public boolean isEmpilhamentoDrenado() {
		return !Util.isNullOuVazio(tipoEstrutura) && tipoEstrutura.equals(new TipoEstrutura(TipoEstruturaEnum.EMPILHAMENTO_DRENADO));
	}

	public boolean isCavaExaurida() {
		return !Util.isNullOuVazio(tipoEstrutura) && tipoEstrutura.equals(new TipoEstrutura(TipoEstruturaEnum.CAVA_EXAURIDA));
	}

	public boolean isNaoSeAplica() {
		return !Util.isNullOuVazio(tipoEstrutura) && tipoEstrutura.equals(new TipoEstrutura(TipoEstruturaEnum.NAO_SE_APLICA));
	}

	public boolean isOutros() {
		return !Util.isNullOuVazio(tipoEstrutura) && tipoEstrutura.equals(new TipoEstrutura(TipoEstruturaEnum.OUTROS));
	}

	public ClassificacaoRejeitoDnpm getClassificacaoRejeitoDnpm() {
		return classificacaoRejeitoDnpm;
	}

	public void setClassificacaoRejeitoDnpm(ClassificacaoRejeitoDnpm classificacaoRejeitoDnpm) {
		this.classificacaoRejeitoDnpm = classificacaoRejeitoDnpm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classificacaoRejeitoDnpm == null) ? 0 : classificacaoRejeitoDnpm.hashCode());
		result = prime * result + ((fceLicenciamentoMineral == null) ? 0 : fceLicenciamentoMineral.hashCode());
		result = prime * result + ((ideFormaDisposicaoRejeito == null) ? 0 : ideFormaDisposicaoRejeito.hashCode());
		result = prime * result + ((tipoEstrutura == null) ? 0 : tipoEstrutura.hashCode());
		result = prime * result + ((tipoResiduoGerado == null) ? 0 : tipoResiduoGerado.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		FormaDisposicaoRejeito other = (FormaDisposicaoRejeito) obj;
		if(classificacaoRejeitoDnpm == null){
			if(other.classificacaoRejeitoDnpm != null) {
				return false;
			}
		}
		else if(!classificacaoRejeitoDnpm.equals(other.classificacaoRejeitoDnpm)) {
			return false;
		}
		if(fceLicenciamentoMineral == null){
			if(other.fceLicenciamentoMineral != null) {
				return false;
			}
		}
		else if(!fceLicenciamentoMineral.equals(other.fceLicenciamentoMineral)) {
			return false;
		}
		if(ideFormaDisposicaoRejeito == null){
			if(other.ideFormaDisposicaoRejeito != null) {
				return false;
			}
		}
		else if(!ideFormaDisposicaoRejeito.equals(other.ideFormaDisposicaoRejeito)) {
			return false;
		}
		if(tipoEstrutura == null){
			if(other.tipoEstrutura != null) {
				return false;
			}
		}
		else if(!tipoEstrutura.equals(other.tipoEstrutura)) {
			return false;
		}
		if(tipoResiduoGerado == null){
			if(other.tipoResiduoGerado != null) {
				return false;
			}
		}
		else if(!tipoResiduoGerado.equals(other.tipoResiduoGerado)) {
			return false;
		}
		return true;
	}

}