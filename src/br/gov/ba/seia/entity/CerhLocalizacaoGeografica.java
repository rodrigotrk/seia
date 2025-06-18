package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;


@Entity
@Table(name="cerh_localizacao_geografica")
public class CerhLocalizacaoGeografica extends AbstractEntityHist implements Cloneable{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "cerh_localizacao_geografica_seq", sequenceName = "cerh_localizacao_geografica_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_localizacao_geografica_seq")
	@Column(name="ide_cerh_localizacao_geografica")
	private Integer ideCerhLocalizacaoGeografica;

	@Historico(name="Coordenada")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_processo")
	private CerhProcesso ideCerhProcesso;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_tipo_uso")
	private CerhTipoUso ideCerhTipoUso;

	@Historico
	@OneToOne(mappedBy="ideCerhLocalizacaoGeografica")
	private CerhBarragemCaracterizacao ideCerhBarragemCaracterizacao;

	@Historico
	@OneToOne(mappedBy="ideCerhLocalizacaoGeografica")
	private CerhCaptacaoCaracterizacao ideCerhCaptacaoCaracterizacao;

	@Historico
	@OneToOne(mappedBy="ideCerhLocalizacaoGeografica")
	private CerhIntervencaoCaracterizacao ideCerhIntervencaoCaracterizacao;
	
	@Historico
	@OneToOne(mappedBy="ideCerhLocalizacaoGeografica")
	private CerhLancamentoEfluenteCaracterizacao ideCerhLancamentoEfluenteCaracterizacao;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_geo_rpga")
	private GeoRpga gid;
	
	@Transient
	private String nomeMunicipio;
	
	@Transient
	private CerhLocalizacaoGeografica cerhLocalizacaoGeograficaSelecionado;
	
	public CerhLocalizacaoGeografica() {
		ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}

	public CerhLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.ideLocalizacaoGeografica = localizacaoGeografica;
	}

	public CerhLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica, CerhTipoUso cerhTipoUso) {
		this(localizacaoGeografica);
		this.ideCerhTipoUso = cerhTipoUso;
	}
	
	public CerhLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica, CerhProcesso cerhProcesso) {
		this(localizacaoGeografica);
		this.ideCerhProcesso = cerhProcesso;
	}

	public CerhLocalizacaoGeografica(Integer ideCerhLocalizacaoGeografica) {
		this.ideCerhLocalizacaoGeografica = ideCerhLocalizacaoGeografica;
	}

	public Integer getIdeCerhLocalizacaoGeografica() {
		return ideCerhLocalizacaoGeografica;
	}

	public void setIdeCerhLocalizacaoGeografica(Integer ideCerhLocalizacaoGeografica) {
		this.ideCerhLocalizacaoGeografica = ideCerhLocalizacaoGeografica;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public CerhProcesso getIdeCerhProcesso() {
		return ideCerhProcesso;
	}

	public void setIdeCerhProcesso(CerhProcesso ideCerhProcesso) {
		this.ideCerhProcesso = ideCerhProcesso;
	}

	public CerhTipoUso getIdeCerhTipoUso() {
		return ideCerhTipoUso;
	}

	public void setIdeCerhTipoUso(CerhTipoUso ideCerhTipoUso) {
		this.ideCerhTipoUso = ideCerhTipoUso;
	}

	public CerhBarragemCaracterizacao getIdeCerhBarragemCaracterizacao() {
		return ideCerhBarragemCaracterizacao;
	}

	public void setIdeCerhBarragemCaracterizacao(CerhBarragemCaracterizacao ideCerhBarragemCaracterizacao) {
		this.ideCerhBarragemCaracterizacao = ideCerhBarragemCaracterizacao;
	}

	public CerhCaptacaoCaracterizacao getIdeCerhCaptacaoCaracterizacao() {
		return ideCerhCaptacaoCaracterizacao;
	}

	public void setIdeCerhCaptacaoCaracterizacao(CerhCaptacaoCaracterizacao ideCerhCaptacaoCaracterizacao) {
		this.ideCerhCaptacaoCaracterizacao = ideCerhCaptacaoCaracterizacao;
	}

	public CerhIntervencaoCaracterizacao getIdeCerhIntervencaoCaracterizacao() {
		return ideCerhIntervencaoCaracterizacao;
	}

	public void setIdeCerhIntervencaoCaracterizacao(CerhIntervencaoCaracterizacao ideCerhIntervencaoCaracterizacao) {
		this.ideCerhIntervencaoCaracterizacao = ideCerhIntervencaoCaracterizacao;
	}

	public CerhLancamentoEfluenteCaracterizacao getIdeCerhLancamentoEfluenteCaracterizacao() {
		return ideCerhLancamentoEfluenteCaracterizacao;
	}

	public void setIdeCerhLancamentoEfluenteCaracterizacao(CerhLancamentoEfluenteCaracterizacao cerhLancamentoEfluenteCaracterizacao) {
		this.ideCerhLancamentoEfluenteCaracterizacao = cerhLancamentoEfluenteCaracterizacao;
	}
	
	public CerhLocalizacaoGeografica getCerhLocalizacaoGeograficaSelecionado() {
		return cerhLocalizacaoGeograficaSelecionado;
	}

	public void setCerhLocalizacaoGeograficaSelecionado(CerhLocalizacaoGeografica cerhLocalizacaoGeograficaSelecionado) {
		this.cerhLocalizacaoGeograficaSelecionado = cerhLocalizacaoGeograficaSelecionado;
	}
	
	public String getNomeMunicipio() {
		return nomeMunicipio;
	}

	public void setNomeMunicipio(String nomeMunicipio) {
		this.nomeMunicipio = nomeMunicipio;
	}

	public CerhLocalizacaoGeografica clone() throws CloneNotSupportedException {
		return (CerhLocalizacaoGeografica) super.clone();
	}

	public GeoRpga getGid() {
		return gid;
	}

	public void setGid(GeoRpga gid) {
		this.gid = gid;
	}

}