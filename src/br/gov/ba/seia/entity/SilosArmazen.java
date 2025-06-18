package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


/**
 * The persistent class for the silos_armazens database table.
 * 
 */
@Entity
@Table(name="silos_armazens")
@NamedQuery(name="SilosArmazen.findAll", query="SELECT s FROM SilosArmazen s")
public class SilosArmazen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SILOS_ARMAZENS_IDESILOSARMAZENS_GENERATOR", sequenceName="SILOS_ARMAZENS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SILOS_ARMAZENS_IDESILOSARMAZENS_GENERATOR")
	@Column(name="ide_silos_armazens")
	private Integer ideSilosArmazens;
	
	@ManyToOne
	@JoinColumn(name="ide_cadastro_atividade_nao_sujeita_lic")
	private CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSujeitaLic;
	
	@ManyToOne
	@JoinColumn(name="ide_empreendimento")
	private Empreendimento ideEmpreendimento;

	@Column(name="ind_aceite_declaracao_final")
	private Boolean indAceiteDeclaracaoFinal;

	@Column(name="ind_aceite_empreendimento_area_protegida")
	private Boolean indAceiteEmpreendimentoAreaProtegida;

	@Column(name="ind_aceite_instrucoes")
	private Boolean indAceiteInstrucoes;

	@Column(name="ind_empreendimento_caldeira")
	private Boolean indEmpreendimentoCaldeira;

	@Column(name="ind_existe_comunidade")
	private Boolean indExisteComunidade;

	@Column(name="ind_industrializacao")
	private Boolean indIndustrializacao;

	@Column(name="ind_queima_combustivel")
	private Boolean indQueimaCombustivel;

	@Column(name="val_area_total_construida")
	private BigDecimal valAreaTotalConstruida;

	@Column(name="val_area_total_terreno")
	private BigDecimal valAreaTotalTerreno;

	//bi-directional many-to-one association to CaracterizacaoAmbientalSilosArmazen
	@OneToMany(mappedBy="silosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<CaracterizacaoAmbientalSilosArmazen> caracterizacaoAmbientalSilosArmazens;

	//bi-directional many-to-one association to SilosArmazensImovelRural
	@OneToMany(mappedBy="silosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<SilosArmazensImovel> silosArmazensImovelRurals;

	//bi-directional many-to-one association to SilosArmazensOperacaoDesenvolvida
	@OneToMany(mappedBy="silosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<SilosArmazensOperacaoDesenvolvida> silosArmazensOperacaoDesenvolvidas;

	//bi-directional many-to-one association to SilosArmazensResponsavelTecnico
	@OneToMany(mappedBy="silosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<SilosArmazensResponsavelTecnico> silosArmazensResponsavelTecnicos;

	//bi-directional many-to-one association to SilosArmazensTipoCombustivel
	@OneToMany(mappedBy="silosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<SilosArmazensTipoCombustivel> silosArmazensTipoCombustivels;

	//bi-directional many-to-one association to SilosArmazensUnidadeArmazenadora
	@OneToMany(mappedBy="silosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<SilosArmazensUnidadeArmazenadora> silosArmazensUnidadeArmazenadoras;

	//bi-directional many-to-one association to SistemaSegurancaSilosArmazen
	@OneToMany(mappedBy="silosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<SistemaSegurancaSilosArmazen> sistemaSegurancaSilosArmazens;

	public SilosArmazen() {
		this.silosArmazensUnidadeArmazenadoras = new ArrayList<SilosArmazensUnidadeArmazenadora>();
	}

	public Integer getIdeSilosArmazens() {
		return this.ideSilosArmazens;
	}

	public void setIdeSilosArmazens(Integer ideSilosArmazens) {
		this.ideSilosArmazens = ideSilosArmazens;
	}

	public CadastroAtividadeNaoSujeitaLic getIdeCadastroAtividadeNaoSujeitaLic() {
		return ideCadastroAtividadeNaoSujeitaLic;
	}

	public void setIdeCadastroAtividadeNaoSujeitaLic(
			CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSujeitaLic) {
		this.ideCadastroAtividadeNaoSujeitaLic = ideCadastroAtividadeNaoSujeitaLic;
	}

	
	
	public Empreendimento getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public Boolean getIndAceiteDeclaracaoFinal() {
		return this.indAceiteDeclaracaoFinal;
	}

	public void setIndAceiteDeclaracaoFinal(Boolean indAceiteDeclaracaoFinal) {
		this.indAceiteDeclaracaoFinal = indAceiteDeclaracaoFinal;
	}

	public Boolean getIndAceiteEmpreendimentoAreaProtegida() {
		return this.indAceiteEmpreendimentoAreaProtegida;
	}

	public void setIndAceiteEmpreendimentoAreaProtegida(Boolean indAceiteEmpreendimentoAreaProtegida) {
		this.indAceiteEmpreendimentoAreaProtegida = indAceiteEmpreendimentoAreaProtegida;
	}

	public Boolean getIndAceiteInstrucoes() {
		return this.indAceiteInstrucoes;
	}

	public void setIndAceiteInstrucoes(Boolean indAceiteInstrucoes) {
		this.indAceiteInstrucoes = indAceiteInstrucoes;
	}

	public Boolean getIndEmpreendimentoCaldeira() {
		return this.indEmpreendimentoCaldeira;
	}

	public void setIndEmpreendimentoCaldeira(Boolean indEmpreendimentoCaldeira) {
		this.indEmpreendimentoCaldeira = indEmpreendimentoCaldeira;
	}

	public Boolean getIndExisteComunidade() {
		return this.indExisteComunidade;
	}

	public void setIndExisteComunidade(Boolean indExisteComunidade) {
		this.indExisteComunidade = indExisteComunidade;
	}

	public Boolean getIndIndustrializacao() {
		return this.indIndustrializacao;
	}

	public void setIndIndustrializacao(Boolean indIndustrializacao) {
		this.indIndustrializacao = indIndustrializacao;
	}

	public Boolean getIndQueimaCombustivel() {
		return this.indQueimaCombustivel;
	}

	public void setIndQueimaCombustivel(Boolean indQueimaCombustivel) {
		this.indQueimaCombustivel = indQueimaCombustivel;
	}

	public BigDecimal getValAreaTotalConstruida() {
		return this.valAreaTotalConstruida;
	}

	public void setValAreaTotalConstruida(BigDecimal valAreaTotalConstruida) {
		this.valAreaTotalConstruida = valAreaTotalConstruida;
	}

	public BigDecimal getValAreaTotalTerreno() {
		return this.valAreaTotalTerreno;
	}

	public void setValAreaTotalTerreno(BigDecimal valAreaTotalTerreno) {
		this.valAreaTotalTerreno = valAreaTotalTerreno;
	}

	public List<CaracterizacaoAmbientalSilosArmazen> getCaracterizacaoAmbientalSilosArmazens() {
		return this.caracterizacaoAmbientalSilosArmazens;
	}

	public void setCaracterizacaoAmbientalSilosArmazens(List<CaracterizacaoAmbientalSilosArmazen> caracterizacaoAmbientalSilosArmazens) {
		this.caracterizacaoAmbientalSilosArmazens = caracterizacaoAmbientalSilosArmazens;
	}

	public List<SilosArmazensImovel> getSilosArmazensImovelRurals() {
		return this.silosArmazensImovelRurals;
	}

	public void setSilosArmazensImovelRurals(List<SilosArmazensImovel> silosArmazensImovelRurals) {
		this.silosArmazensImovelRurals = silosArmazensImovelRurals;
	}


	public List<SilosArmazensOperacaoDesenvolvida> getSilosArmazensOperacaoDesenvolvidas() {
		return this.silosArmazensOperacaoDesenvolvidas;
	}

	public void setSilosArmazensOperacaoDesenvolvidas(List<SilosArmazensOperacaoDesenvolvida> silosArmazensOperacaoDesenvolvidas) {
		this.silosArmazensOperacaoDesenvolvidas = silosArmazensOperacaoDesenvolvidas;
	}

	public List<SilosArmazensResponsavelTecnico> getSilosArmazensResponsavelTecnicos() {
		return this.silosArmazensResponsavelTecnicos;
	}

	public void setSilosArmazensResponsavelTecnicos(List<SilosArmazensResponsavelTecnico> silosArmazensResponsavelTecnicos) {
		this.silosArmazensResponsavelTecnicos = silosArmazensResponsavelTecnicos;
	}

	public List<SilosArmazensTipoCombustivel> getSilosArmazensTipoCombustivels() {
		return this.silosArmazensTipoCombustivels;
	}

	public void setSilosArmazensTipoCombustivels(List<SilosArmazensTipoCombustivel> silosArmazensTipoCombustivels) {
		this.silosArmazensTipoCombustivels = silosArmazensTipoCombustivels;
	}

	public List<SilosArmazensUnidadeArmazenadora> getSilosArmazensUnidadeArmazenadoras() {
		return this.silosArmazensUnidadeArmazenadoras;
	}

	public void setSilosArmazensUnidadeArmazenadoras(List<SilosArmazensUnidadeArmazenadora> silosArmazensUnidadeArmazenadoras) {
		this.silosArmazensUnidadeArmazenadoras = silosArmazensUnidadeArmazenadoras;
	}

	public List<SistemaSegurancaSilosArmazen> getSistemaSegurancaSilosArmazens() {
		return this.sistemaSegurancaSilosArmazens;
	}

	public void setSistemaSegurancaSilosArmazens(List<SistemaSegurancaSilosArmazen> sistemaSegurancaSilosArmazens) {
		this.sistemaSegurancaSilosArmazens = sistemaSegurancaSilosArmazens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSilosArmazens == null) ? 0 : ideSilosArmazens.hashCode());
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
		SilosArmazen other = (SilosArmazen) obj;
		if (ideSilosArmazens == null) {
			if (other.ideSilosArmazens != null)
				return false;
		} else if (!ideSilosArmazens.equals(other.ideSilosArmazens))
			return false;
		return true;
	}

	
}