package br.gov.ba.seia.entity;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "fce_linha_energia_localizacao_geografica")
@NamedQuery(name = "FceLinhaEnergiaLocalizacaoGeografica.removeFceLinhaEnergiaLocalizacaoGeografica", query = "DELETE FROM FceLinhaEnergiaLocalizacaoGeografica f WHERE f.ideFceLinhaEnergia = :ideFceLinhaEnergia and f.ideLocalizacaoGeografica = :ideLocalizacaoGeografica")
public class FceLinhaEnergiaLocalizacaoGeografica extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_fce_linha_energia_localizacao_geografica")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ide_fce_linha_energia_localizacao_geografica_seq")
	@SequenceGenerator(name = "ide_fce_linha_energia_localizacao_geografica_seq", sequenceName = "ide_fce_linha_energia_localizacao_geografica_seq", allocationSize = 1)
	private Integer ideFceLinhaEnergiaLocalizacaoGeografica;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_fce_linha_energia", referencedColumnName = "ide_fce_linha_energia", nullable = false)
	private FceLinhaEnergia ideFceLinhaEnergia;
	
	@NotNull
	@Column(name = "ind_preferencial")
	private Boolean indPreferencial;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = false)
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@NotNull
	@Column(name = "ind_objeto_concedido")
	private Boolean indObjetoConcedido;

	@Transient
	private List<Municipio> listaMunicipio;
	
	@Transient
	private String numeroNotificacao;
	
	public FceLinhaEnergiaLocalizacaoGeografica() {
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}
	
	public FceLinhaEnergiaLocalizacaoGeografica(Boolean indPreferencial) {
		this();
		this.indPreferencial = indPreferencial;
	}

	public FceLinhaEnergiaLocalizacaoGeografica(FceLinhaEnergia fceLinhaEnergia) {
		this();
		this.ideFceLinhaEnergia = fceLinhaEnergia;
		this.indPreferencial = false;
		this.indObjetoConcedido = false;
	}

	public Integer getIdeFceLinhaEnergiaLocalizacaoGeografica() {
		return ideFceLinhaEnergiaLocalizacaoGeografica;
	}

	public void setIdeFceLinhaEnergiaLocalizacaoGeografica(Integer ideFceLinhaEnergiaLocalizacaoGeografica) {
		this.ideFceLinhaEnergiaLocalizacaoGeografica = ideFceLinhaEnergiaLocalizacaoGeografica;
	}

	public FceLinhaEnergia getIdeFceLinhaEnergia() {
		return ideFceLinhaEnergia;
	}

	public void setIdeFceLinhaEnergia(FceLinhaEnergia ideFceLinhaEnergia) {
		this.ideFceLinhaEnergia = ideFceLinhaEnergia;
	}

	public Boolean getIndPreferencial() {
		return indPreferencial;
	}

	public void setIndPreferencial(Boolean indPreferencial) {
		this.indPreferencial = indPreferencial;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public Boolean getIndObjetoConcedido() {
		return indObjetoConcedido;
	}

	public void setIndObjetoConcedido(Boolean indObjetoConcedido) {
		this.indObjetoConcedido = indObjetoConcedido;
	}

	public List<Municipio> getListaMunicipio() {
		return listaMunicipio;
	}

	public void setListaMunicipio(List<Municipio> listaMunicipio) {
		this.listaMunicipio = listaMunicipio;
	}

	public String getNumeroNotificacao() {
		return numeroNotificacao;
	}

	public void setNumeroNotificacao(String numeroNotificacao) {
		this.numeroNotificacao = numeroNotificacao;
	}
}
