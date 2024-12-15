package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.util.StringUtil;
import br.gov.ba.seia.util.Util;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "localizacao_geografica")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "LocalizacaoGeografica.findAll", query = "SELECT l FROM LocalizacaoGeografica l"),
	@NamedQuery(name = "LocalizacaoGeografica.findByIdeLocalizacaoGeografica", query = "SELECT l FROM LocalizacaoGeografica l WHERE l.ideLocalizacaoGeografica = :ideLocalizacaoGeografica"),
	@NamedQuery(name = "LocalizacaoGeografica.findByDtcCriacao", query = "SELECT l FROM LocalizacaoGeografica l WHERE l.dtcCriacao = :dtcCriacao"),
	@NamedQuery(name = "LocalizacaoGeografica.findByIndExcluido", query = "SELECT l FROM LocalizacaoGeografica l WHERE l.indExcluido = :indExcluido"),
	@NamedQuery(name = "LocalizacaoGeografica.findByDtcExclusao", query = "SELECT l FROM LocalizacaoGeografica l WHERE l.dtcExclusao = :dtcExclusao"),
	@NamedQuery(name = "LocalizacaoGeografica.findByFonteCoordenada", query = "SELECT l FROM LocalizacaoGeografica l WHERE l.fonteCoordenada = :fonteCoordenada"),
	@NamedQuery(name = "LocalizacaoGeografica.deleteByIde", query = "DELETE FROM LocalizacaoGeografica l WHERE l.ideLocalizacaoGeografica = :ideLocalizacaoGeografica"),
	@NamedQuery(name = "LocalizacaoGeografica.updateReqUnicoByIdeLocGeo", query = "UPDATE RequerimentoUnico set ideLocalizacaoGeografica = null WHERE ideLocalizacaoGeografica = :ideLocalizacaoGeografica"),
	@NamedQuery(name = "LocalizacaoGeografica.atualizarLocGeoReqImovel", query = "UPDATE RequerimentoImovel set ideLocalizacaoGeografica = null WHERE ideLocalizacaoGeografica = :ideLocalizacaoGeografica"),
	@NamedQuery(name = "LocalizacaoGeografica.deletarPerguntaReqImovel", query = "DELETE FROM PerguntaRequerimento p WHERE p.ideLocalizacaoGeografica = :ideLocalizacaoGeografica"),
	@NamedQuery(name = "LocalizacaoGeografica.findByDesLocalizacaoGeografica", query = "SELECT l FROM LocalizacaoGeografica l WHERE l.desLocalizacaoGeografica = :desLocalizacaoGeografica")})
public class LocalizacaoGeografica extends AbstractEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOCALIZACAO_GEOGRAFICA_IDE_LOCALIZACAO_GEOGRAFICA_seq")
	@SequenceGenerator(name="LOCALIZACAO_GEOGRAFICA_IDE_LOCALIZACAO_GEOGRAFICA_seq", sequenceName="LOCALIZACAO_GEOGRAFICA_IDE_LOCALIZACAO_GEOGRAFICA_seq", allocationSize=1)
	@Column(name = "ide_localizacao_geografica")
	private Integer ideLocalizacaoGeografica;

	@Basic(optional = false)
	@NotNull
	@Column(name = "dtc_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao = new Date();

	@Historico(key=true)
	@Basic(optional = false)
	@Column(name = "ind_excluido")
	private boolean indExcluido;

	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;

	@Column(name = "fonte_coordenada")
	private Integer fonteCoordenada;

	@Column(name = "des_localizacao_geografica")
	private String desLocalizacaoGeografica;

	@Column(name = "dtc_atualizacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcAtualizacao;

	@JoinColumn(name = "ide_sistema_coordenada", referencedColumnName = "ide_sistema_coordenada")
	@ManyToOne(optional = false)
	private SistemaCoordenada ideSistemaCoordenada;

	@JoinColumn(name = "ide_classificacao_secao", referencedColumnName = "ide_classificacao_secao")
	@ManyToOne
	private ClassificacaoSecaoGeometrica ideClassificacaoSecao;

	@Historico
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideLocalizacaoGeografica", fetch=FetchType.LAZY)
	private Collection<DadoGeografico> dadoGeograficoCollection;

	@OneToMany(mappedBy = "ideLocalizacaoGeografica")
	private Collection<ParamPersistDadoGeo> paramPersistDadoGeoCollection;
	
	@OneToMany(mappedBy = "ideLocalizacaoGeografica", fetch=FetchType.LAZY)
	private Collection<OutorgaLocalizacaoGeografica> outorgaLocalizacaoGeograficaCollection;

	@Transient
	private BigDecimal vlrArea;
	
	@Transient
	private int linha=1;

	@Transient
	private Boolean localizacaoExcluida;
	
	@Transient
	private Boolean novosArquivosShapeImportados;
	
	@Transient
	private List<String> listArquivosShape;

	@Transient
	private String nomTipoIntervencao;
	
	@Transient
	private Boolean possuiLocGeografica;

	@Transient
	private String pontoLatitude;
	
	@Transient
	private String pontoLongitude;

	@Transient
	private String latitudeInicial;
	
	@Transient
	private String longitudeInicial;
	
	@Transient
	private String baciaHidrografica;
	
	@Transient
	private String subBacia;
	
	@Transient
	private String rpga;
	
	@Transient
	private Notificacao notificacao;
	
	@Transient
	private String sistemaAquifero;
	
	@Transient
	private Integer ideDocumentoObrigatorio;
	
	@Transient
	private String theGeom;
	
	public LocalizacaoGeografica() {
		localizacaoExcluida = false;
		novosArquivosShapeImportados = false;
	}
	
	public LocalizacaoGeografica(Collection<DadoGeografico> dadoGeograficoCollection) {
		localizacaoExcluida = false;
		novosArquivosShapeImportados = false;
		this.dadoGeograficoCollection = dadoGeograficoCollection;
	}
	public LocalizacaoGeografica(ClassificacaoSecaoGeometrica ideClassificacaoSecao) {
		localizacaoExcluida = false;
		novosArquivosShapeImportados = false;
		this.ideClassificacaoSecao = ideClassificacaoSecao;
	}

	public LocalizacaoGeografica(Integer ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
		localizacaoExcluida = false;
		novosArquivosShapeImportados = false;
	}

	public LocalizacaoGeografica(Integer ideLocalizacaoGeografica, Date dtcCriacao, boolean indExcluido) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
		localizacaoExcluida = false;
		novosArquivosShapeImportados = false;
	}

	public LocalizacaoGeografica(SistemaCoordenada sistemaCoordenada) {
		this.ideSistemaCoordenada = sistemaCoordenada;
		localizacaoExcluida = false;
		novosArquivosShapeImportados = false;
	}

	public LocalizacaoGeografica(Integer ideLocalizacaoGeografica, String nomTipoIntervencao) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
		this.nomTipoIntervencao = nomTipoIntervencao;
		localizacaoExcluida = false;
		novosArquivosShapeImportados = false;
	}
	
	public String getLatitudeInicialPad() {
		if(latitudeInicial.contains("º")){
			return latitudeInicial;	
		}
		
		return StringUtil.rlPad(latitudeInicial,'0', 10);
	}

	public String getLongitudeInicialPad() {
		if(longitudeInicial.contains("º")){
			return longitudeInicial;
		}
		return StringUtil.rlPad(longitudeInicial,'0', 10);
	}
	
	public BigDecimal getVlrArea() {
		if(!Util.isNullOuVazio(vlrArea) && vlrArea.equals(new BigDecimal(0))) {
			return null;
		}
		return vlrArea;
	}
	
	public String getNomeSistemaCoordenadas(){
		if(Util.isNullOuVazio(this.ideSistemaCoordenada)){
			return "";
		}else{
			switch (this.ideSistemaCoordenada.getIdeSistemaCoordenada()) {
			case 1:
				return "Geográfica SAD69";
			case 2:
				return "UTM23 SAD69";
			case 3:
				return "UTM24 SAD69";
			case 4:
				return "Geográfica SIRGAS2000";
			case 5:
				return "UTM23 SIRGAS2000";
			case 6:
				return "UTM24 SIRGAS2000";
			default:
				return "Sistema Inválido";
			}
		}
	}

	public Boolean getIdeLocalizacaoGeograficaNotNull() {
		if(!Util.isNullOuVazio(this.ideLocalizacaoGeografica)) {
			return true;
		} else {
			return false;
		}
	}

	public String getIdeClassificacaoSecaoToString(){
		if(!Util.isNullOuVazio(this.ideClassificacaoSecao)){
			if(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue() == this.ideClassificacaoSecao.getIdeClassificacaoSecao()){
				return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getNomeClassificSec();
			}else if(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue() == this.ideClassificacaoSecao.getIdeClassificacaoSecao()){
				return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getNomeClassificSec();
			}else{
				return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getNomeClassificSec();
			}
		}else{
			return "";
		}
	}
	
	public Boolean getPossuiLocGeografica() {
		Boolean retorno = false;

		if(!Util.isNullOuVazio(dadoGeograficoCollection)){
			for (DadoGeografico dg : dadoGeograficoCollection) {
				if(!Util.isNullOuVazio(dg.getCoordGeoNumerica())  ){
					retorno = true;
				}
			}
		}
		return retorno;
	}
	
	public Integer getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}
	
	public void defineIdeLocalizacaoGeograficaComoNula() {
		this.ideLocalizacaoGeografica = null;
	}
	
	public void setIdeLocalizacaoGeografica(Integer ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}
	
	public Date getDtcCriacao() {
		return dtcCriacao;
	}
	
	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}
	
	public boolean getIndExcluido() {
		return indExcluido;
	}
	
	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}
	
	public Date getDtcExclusao() {
		return dtcExclusao;
	}
	
	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}
	
	public Integer getFonteCoordenada() {
		return fonteCoordenada;
	}
	
	public void setFonteCoordenada(Integer fonteCoordenada) {
		this.fonteCoordenada = fonteCoordenada;
	}
	
	public String getDesLocalizacaoGeografica() {
		return desLocalizacaoGeografica;
	}
	
	public void setDesLocalizacaoGeografica(String desLocalizacaoGeografica) {
		this.desLocalizacaoGeografica = desLocalizacaoGeografica;
	}
	
	@XmlTransient
	public Collection<DadoGeografico> getDadoGeograficoCollection() {
		return dadoGeograficoCollection;
	}
	
	public void setDadoGeograficoCollection(Collection<DadoGeografico> dadoGeograficoCollection) {
		this.dadoGeograficoCollection = dadoGeograficoCollection;
	}
	
	public SistemaCoordenada getIdeSistemaCoordenada() {
		return ideSistemaCoordenada;
	}
	
	public void setIdeSistemaCoordenada(SistemaCoordenada ideSistemaCoordenada) {
		this.ideSistemaCoordenada = ideSistemaCoordenada;
	}
	
	public ClassificacaoSecaoGeometrica getIdeClassificacaoSecao() {
		return ideClassificacaoSecao;
	}
	
	public void setIdeClassificacaoSecao(ClassificacaoSecaoGeometrica ideClassificacaoSecao) {
		this.ideClassificacaoSecao = ideClassificacaoSecao;
	}

	public Collection<ParamPersistDadoGeo> getParamPersistDadoGeoCollection() {
		return paramPersistDadoGeoCollection;
	}
	
	public void setParamPersistDadoGeoCollection(Collection<ParamPersistDadoGeo> paramPersistDadoGeoCollection) {
		this.paramPersistDadoGeoCollection = paramPersistDadoGeoCollection;
	}
	
	public void setVlrArea(BigDecimal vlrArea) {
		this.vlrArea = vlrArea;
	}
	
	public Boolean getLocalizacaoExcluida() {
		return localizacaoExcluida;
	}
	
	public void setLocalizacaoExcluida(Boolean localizacaoExcluida) {
		this.localizacaoExcluida = localizacaoExcluida;
	}
	
	public Boolean getNovosArquivosShapeImportados() {
		return novosArquivosShapeImportados;
	}
	
	public void setNovosArquivosShapeImportados(Boolean novosArquivosShapeImportados) {
		this.novosArquivosShapeImportados = novosArquivosShapeImportados;
	}
	
	public List<String> getListArquivosShape() {
		return listArquivosShape;
	}
	
	public void setListArquivosShape(List<String> listArquivosShape) {
		this.listArquivosShape = listArquivosShape;
	}
	
	public int getLinha() {
		return linha;
	}
	
	public void setLinha(int linha) {
		this.linha = linha;
	}
	
	public String getNomTipoIntervencao() {
		return nomTipoIntervencao;
	}
	
	public void setNomTipoIntervencao(String nomTipoIntervencao) {
		this.nomTipoIntervencao = nomTipoIntervencao;
	}
	
	public void setPossuiLocGeografica(Boolean possuiLocGeografica) {
		this.possuiLocGeografica = possuiLocGeografica;
	}
	
	public String getPontoLatitude() {
		return pontoLatitude;
	}
	
	public void setPontoLatitude(String pontoLatitude) {
		this.pontoLatitude = pontoLatitude;
	}
	
	public String getPontoLongitude() {
		return pontoLongitude;
	}
	
	public void setPontoLongitude(String pontoLongitude) {
		this.pontoLongitude = pontoLongitude;
	}
	
	public String getLatitudeInicial() {
		return latitudeInicial;
	}
	
	public void setLatitudeInicial(String latitudeInicial) {
		this.latitudeInicial = latitudeInicial;
	}
	
	public String getLongitudeInicial() {
		return longitudeInicial;
	}
	
	public void setLongitudeInicial(String longitudeInicial) {
		this.longitudeInicial = longitudeInicial;
	}
	
	public String getBaciaHidrografica() {
		return baciaHidrografica;
	}
	
	public void setBaciaHidrografica(String baciaHidrografica) {
		this.baciaHidrografica = baciaHidrografica;
	}
	
	public String getSubBacia() {
		return subBacia;
	}
	
	public void setSubBacia(String subBacia) {
		this.subBacia = subBacia;
	}
	
	public String getRpga() {
		return rpga;
	}
	
	public void setRpga(String rpga) {
		this.rpga = rpga;
	}
	
	public Collection<OutorgaLocalizacaoGeografica> getOutorgaLocalizacaoGeograficaCollection() {
		return outorgaLocalizacaoGeograficaCollection;
	}
	
	public void setOutorgaLocalizacaoGeograficaCollection(Collection<OutorgaLocalizacaoGeografica> outorgaLocalizacaoGeograficaCollection) {
		this.outorgaLocalizacaoGeograficaCollection = outorgaLocalizacaoGeograficaCollection;
	}
	
	public Notificacao getNotificacao() {
		return notificacao;
	}
	
	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}
	
	public String getSistemaAquifero() {
		return sistemaAquifero;
	}
	
	public void setSistemaAquifero(String sistemaAquifero) {
		this.sistemaAquifero = sistemaAquifero;
	}
	
	public Integer getIdeDocumentoObrigatorio() {
		return ideDocumentoObrigatorio;
	}
	
	public void setIdeDocumentoObrigatorio(Integer ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}
	
	public String getTheGeom() {
		return theGeom;
	}

	public void setTheGeom(String theGeom) {
		this.theGeom = theGeom;
	}

	public Date getDtcAtualizacao() {
		return dtcAtualizacao;
	}
	
	public void setDtcAtualizacao(Date dtcAtualizacao) {
		this.dtcAtualizacao = dtcAtualizacao;
	}
	
	@Override
	public LocalizacaoGeografica clone() throws CloneNotSupportedException {
		LocalizacaoGeografica clone = (LocalizacaoGeografica) super.clone();
		clone.setDadoGeograficoCollection(Util.deepCloneList(dadoGeograficoCollection));
		clone.setParamPersistDadoGeoCollection(Util.deepCloneList(paramPersistDadoGeoCollection));
		clone.setOutorgaLocalizacaoGeograficaCollection(Util.deepCloneList(outorgaLocalizacaoGeograficaCollection));
		return clone;
	}
	
}
