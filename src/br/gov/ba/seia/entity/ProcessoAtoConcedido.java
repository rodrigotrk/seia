package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.dto.DadoConcedidoDTO;

@Entity
@Table(name="processo_ato_concedido")
@NamedQueries({
 @NamedQuery (name="ProcessoAtoConcedido.findAll", query="SELECT p FROM ProcessoAtoConcedido p"),
 @NamedQuery (name="ProcessoAtoConcedido.excluirByIdeProcessoAto", query="DELETE FROM ProcessoAtoConcedido p WHERE p.ideProcessoAto.ideProcessoAto = :ideProcessoAto")
})
public class ProcessoAtoConcedido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROCESSO_ATO_CONCEDIDO_SEQ", sequenceName="PROCESSO_ATO_CONCEDIDO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROCESSO_ATO_CONCEDIDO_SEQ")
	@Column(name="ide_processo_ato_concedido")
	private Integer ideProcessoAtoConcedido;

	@JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Imovel ideImovel;

	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@JoinColumn(name = "ide_processo_ato", referencedColumnName = "ide_processo_ato", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcessoAto ideProcessoAto;

	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Tipologia ideTipologia;

	@Column(name="val_atividade", length = 10, precision = 4)
	private BigDecimal valAtividade;

	@JoinColumn(name = "ide_tipo_area_concedida", referencedColumnName = "ide_tipo_area_concedida", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoAreaConcedida ideTipoAreaConcedida;
	
	@OneToMany(mappedBy = "ideProcessoAtoConcedido", fetch = FetchType.LAZY)
    private Collection<EspecieSupressaoAutorizacao> especieSupressaoAutorizacaoCollection;
	
	public ProcessoAtoConcedido() {
	}

	public ProcessoAtoConcedido(DadoConcedidoDTO dto) {
		this.ideImovel = new Imovel(dto.getIdeImovel());
		this.ideLocalizacaoGeografica = dto.getPoligonalConcedida().getLocalizacaoGeografica();
		this.valAtividade = new BigDecimal(dto.getPoligonalConcedida().getValArea()).setScale(4,BigDecimal.ROUND_UP);
	}

	public ProcessoAtoConcedido(Imovel ideImovel, LocalizacaoGeografica ideLocalizacaoGeografica, ProcessoAto ideProcessoAto, Tipologia ideTipologia, 
			BigDecimal valAtividade, TipoAreaConcedida ideTipoAreaConcedida) {

		this.ideImovel = ideImovel;
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
		this.ideProcessoAto = ideProcessoAto;
		this.ideTipologia = ideTipologia;
		this.valAtividade = valAtividade;
		this.ideTipoAreaConcedida = ideTipoAreaConcedida;
	}

	public Integer getIdeProcessoAtoConcedido() {
		return ideProcessoAtoConcedido;
	}

	public void setIdeProcessoAtoConcedido(Integer ideProcessoAtoConcedido) {
		this.ideProcessoAtoConcedido = ideProcessoAtoConcedido;
	}

	public Imovel getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(Imovel ideImovel) {
		this.ideImovel = ideImovel;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public ProcessoAto getIdeProcessoAto() {
		return ideProcessoAto;
	}

	public void setIdeProcessoAto(ProcessoAto ideProcessoAto) {
		this.ideProcessoAto = ideProcessoAto;
	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public BigDecimal getValAtividade() {
		return valAtividade;
	}

	public void setValAtividade(BigDecimal valAtividade) {
		this.valAtividade = valAtividade;
	}

	public TipoAreaConcedida getIdeTipoAreaConcedida() {
		return ideTipoAreaConcedida;
	}

	public void setIdeTipoAreaConcedida(TipoAreaConcedida ideTipoAreaConcedida) {
		this.ideTipoAreaConcedida = ideTipoAreaConcedida;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideProcessoAtoConcedido == null) ? 0
						: ideProcessoAtoConcedido.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessoAtoConcedido other = (ProcessoAtoConcedido) obj;
		if (ideProcessoAtoConcedido == null) {
			if (other.ideProcessoAtoConcedido != null)
				return false;
		} else if (!ideProcessoAtoConcedido
				.equals(other.ideProcessoAtoConcedido))
			return false;
		return true;
	}

	public Collection<EspecieSupressaoAutorizacao> getEspecieSupressaoAutorizacaoCollection() {
		return especieSupressaoAutorizacaoCollection;
	}

	public void setEspecieSupressaoAutorizacaoCollection(Collection<EspecieSupressaoAutorizacao> especieSupressaoAutorizacaoCollection) {
		this.especieSupressaoAutorizacaoCollection = especieSupressaoAutorizacaoCollection;
	}
}