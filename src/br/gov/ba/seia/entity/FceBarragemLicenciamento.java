/**
 * 
 */
package br.gov.ba.seia.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.SimpleEntityImpl;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name="fce_barragem_licenciamento")
@XmlRootElement
public class FceBarragemLicenciamento extends SimpleEntityImpl implements Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ide_fce_barragem_licenciamento_seq", sequenceName="ide_fce_barragem_licenciamento_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ide_fce_barragem_licenciamento_seq")
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_fce_barragem_licenciamento")
	private Integer ideFceBarragemLicenciamento;
	
	@JoinColumn(name="ide_fce_barragem",referencedColumnName = "ide_fce_barragem")
	@ManyToOne(fetch=FetchType.LAZY)
	private FceBarragem fceBarragem;
	
	@JoinTable(name = "fce_barragem_obras_infra", joinColumns = { @JoinColumn(name = "ide_fce_barragem_licenciamento", referencedColumnName = "ide_fce_barragem_licenciamento") }, inverseJoinColumns = { @JoinColumn(name = "ide_obras_infra_complementares", referencedColumnName = "ide_obras_infra_complementares") })
	@ManyToMany
	private List<ObrasInfraComplementares> obrasInfraComplementares;
	
	@JoinTable(name = "fce_barragem_material_utilizado", joinColumns = { @JoinColumn(name = "ide_fce_barragem_licenciamento", referencedColumnName = "ide_fce_barragem_licenciamento") }, inverseJoinColumns = { @JoinColumn(name = "ide_material_utilizado", referencedColumnName = "ide_material_utilizado") })
	@ManyToMany
	private List<MaterialUtilizado> materialUtilizados;
	
	@JoinColumn(name="ide_poligonal_app",referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(fetch=FetchType.LAZY)
	private LocalizacaoGeografica poligonalApp;
	
	@JoinColumn(name="ide_poligonal_area_suprimida",referencedColumnName = "ide_localizacao_geografica", nullable=true)
	@ManyToOne(fetch=FetchType.LAZY)
	private LocalizacaoGeografica poligonalAreaSuprimida;
	
	@OneToMany(mappedBy = "fceBarragemLicenciamento")
	private List<FceBarragLicencLocaGeo> barragemLicenciamentoLocalizacaoGeo;
	
	public FceBarragemLicenciamento() {
		this.materialUtilizados = new ArrayList<MaterialUtilizado>();
		this.obrasInfraComplementares = new ArrayList<ObrasInfraComplementares>();
	}
	
	/**
	 * @return the ideFceBarragemLicenciamento
	 */
	public Integer getIdeFceBarragemLicenciamento() {
		return ideFceBarragemLicenciamento;
	}

	/**
	 * @param ideFceBarragemLicenciamento the ideFceBarragemLicenciamento to set
	 */
	public void setIdeFceBarragemLicenciamento(Integer ideFceBarragemLicenciamento) {
		this.ideFceBarragemLicenciamento = ideFceBarragemLicenciamento;
	}

	/**
	 * @return the fceBarragem
	 */
	public FceBarragem getFceBarragem() {
		return fceBarragem;
	}

	/**
	 * @param fceBarragem the fceBarragem to set
	 */
	public void setFceBarragem(FceBarragem fceBarragem) {
		this.fceBarragem = fceBarragem;
	}
	
	/**
	 * @return the obrasInfraComplementares
	 */
	public List<ObrasInfraComplementares> getObrasInfraComplementares() {
		return obrasInfraComplementares;
	}

	/**
	 * @param obrasInfraComplementares the obrasInfraComplementares to set
	 */
	public void setObrasInfraComplementares(
			List<ObrasInfraComplementares> obrasInfraComplementares) {
		this.obrasInfraComplementares = obrasInfraComplementares;
	}

	/**
	 * @return the materialUtilizados
	 */
	public List<MaterialUtilizado> getMaterialUtilizados() {
		return materialUtilizados;
	}

	/**
	 * @param materialUtilizados the materialUtilizados to set
	 */
	public void setMaterialUtilizados(List<MaterialUtilizado> materialUtilizados) {
		this.materialUtilizados = materialUtilizados;
	}

	@Override
	public Long getId() {
		return new Long(this.getIdeFceBarragemLicenciamento());
	}

	/**
	 * @return the poligonalApp
	 */
	public LocalizacaoGeografica getPoligonalApp() {
		return poligonalApp;
	}

	/**
	 * @param poligonalApp the poligonalApp to set
	 */
	public void setPoligonalApp(LocalizacaoGeografica poligonalApp) {
		this.poligonalApp = poligonalApp;
	}

	/**
	 * @return the poligonalAreaSuprimida
	 */
	public LocalizacaoGeografica getPoligonalAreaSuprimida() {
		return poligonalAreaSuprimida;
	}

	/**
	 * @param poligonalAreaSuprimida the poligonalAreaSuprimida to set
	 */
	public void setPoligonalAreaSuprimida(
			LocalizacaoGeografica poligonalAreaSuprimida) {
		this.poligonalAreaSuprimida = poligonalAreaSuprimida;
	}

	/**
	 * @return the barragemLicenciamentoLocalizacaoGeo
	 */
	public List<FceBarragLicencLocaGeo> getBarragemLicenciamentoLocalizacaoGeo() {
		return barragemLicenciamentoLocalizacaoGeo;
	}

	/**
	 * @param barragemLicenciamentoLocalizacaoGeo the barragemLicenciamentoLocalizacaoGeo to set
	 */
	public void setBarragemLicenciamentoLocalizacaoGeo(
			List<FceBarragLicencLocaGeo> barragemLicenciamentoLocalizacaoGeo) {
		this.barragemLicenciamentoLocalizacaoGeo = barragemLicenciamentoLocalizacaoGeo;
	}

	 @Override
     public FceBarragemLicenciamento clone() throws CloneNotSupportedException {
          return (FceBarragemLicenciamento)super.clone();
     }
}
