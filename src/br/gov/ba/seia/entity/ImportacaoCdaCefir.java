package br.gov.ba.seia.entity;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.util.Util;

/**
 *
 * @author carlos.duarte
 */
@Entity
@Table(name = "importacao_cda_cefir")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportacaoCdaCefir.findAll", query = "SELECT i FROM ImportacaoCdaCefir i"),
    @NamedQuery(name = "ImportacaoCdaCefir.findByIdeImportacaoCdaCefir", query = "SELECT i FROM ImportacaoCdaCefir i WHERE i.ideImportacaoCdaCefir = :ideImportacaoCdaCefir")})  

public class ImportacaoCdaCefir implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMPORTACAO_CDA_CEFIR_SEQ") 
    @SequenceGenerator(name="IMPORTACAO_CDA_CEFIR_SEQ", sequenceName="IMPORTACAO_CDA_CEFIR_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_importacao_cda_cefir", nullable = false)
	private Integer ideImportacaoCdaCefir;
		
	@JoinColumn(name = "ide_usuario_importacao", referencedColumnName = "ide_pessoa_fisica")
    @ManyToOne(optional = false)
    private Usuario ideUsuarioImportacao;
	
	@Column(name = "dtc_importacao", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcImportacao;
	
	@Transient
	private String caminhoPlanilhaEntrada;	
	@Transient
	private String caminhoDbf;
	@Transient
	private String caminhoShp;
	@Transient
	private String caminhoShx;
	@Transient
	private String caminhoPlanilhaSaida;
	
	public ImportacaoCdaCefir() {
		
	}
	
	public ImportacaoCdaCefir(Integer ideImportacaoCdaCefir) {
		this.ideImportacaoCdaCefir = ideImportacaoCdaCefir;
	}

	public Integer getIdeImportacaoCdaCefir() {
		return ideImportacaoCdaCefir;
	}

	public void setIdeImportacaoCdaCefir(Integer ideImportacaoCdaCefir) {
		this.ideImportacaoCdaCefir = ideImportacaoCdaCefir;
	}

	public Usuario getIdeUsuarioImportacao() {
		return ideUsuarioImportacao;
	}

	public void setIdeUsuarioImportacao(Usuario ideUsuarioImportacao) {
		this.ideUsuarioImportacao = ideUsuarioImportacao;
	}

	public Date getDtcImportacao() {
		return dtcImportacao;
	}

	public void setDtcImportacao(Date dtcImportacao) {
		this.dtcImportacao = dtcImportacao;
	}

	public String getCaminhoPlanilhaEntrada() {
		if(Util.isNull(caminhoPlanilhaEntrada) && !Util.isNull(this.ideImportacaoCdaCefir)){
			caminhoPlanilhaEntrada = DiretorioArquivoEnum.IMPORTACAOCDACEFIR.toString()+this.ideImportacaoCdaCefir.toString()+File.separator+this.ideImportacaoCdaCefir.toString()+"_PLANILHA_ENTRADA.xls";
		}
		return caminhoPlanilhaEntrada;
	}

	public void setCaminhoPlanilhaEntrada(String caminhoPlanilhaEntrada) {
		this.caminhoPlanilhaEntrada = caminhoPlanilhaEntrada;
	}

	public String getCaminhoDbf() {
		if(Util.isNull(caminhoDbf) && !Util.isNull(this.ideImportacaoCdaCefir)){
			caminhoDbf = DiretorioArquivoEnum.IMPORTACAOCDACEFIR.toString()+this.ideImportacaoCdaCefir.toString()+File.separator+this.ideImportacaoCdaCefir.toString()+"_SHAPE.dbf";
		}
		return caminhoDbf;
	}

	public void setCaminhoDbf(String caminhoDbf) {
		this.caminhoDbf = caminhoDbf;
	}

	public String getCaminhoShp() {
		if(Util.isNull(caminhoShp) && !Util.isNull(this.ideImportacaoCdaCefir)){
			caminhoShp = DiretorioArquivoEnum.IMPORTACAOCDACEFIR.toString()+this.ideImportacaoCdaCefir.toString()+File.separator+this.ideImportacaoCdaCefir.toString()+"_SHAPE.shp";
		}
		return caminhoShp;
	}

	public void setCaminhoShp(String caminhoShp) {
		this.caminhoShp = caminhoShp;
	}

	public String getCaminhoShx() {
		if(Util.isNull(caminhoShx) && !Util.isNull(this.ideImportacaoCdaCefir)){
			caminhoShx = DiretorioArquivoEnum.IMPORTACAOCDACEFIR.toString()+this.ideImportacaoCdaCefir.toString()+File.separator+this.ideImportacaoCdaCefir.toString()+"_SHAPE.shx";
		}
		return caminhoShx;
	}

	public void setCaminhoShx(String caminhoShx) {
		this.caminhoShx = caminhoShx;
	}
	
	public String getCaminhoPlanilhaSaida() {
		if(Util.isNull(caminhoPlanilhaSaida) && !Util.isNull(this.ideImportacaoCdaCefir)){
			caminhoPlanilhaSaida = DiretorioArquivoEnum.IMPORTACAOCDACEFIR.toString()+this.ideImportacaoCdaCefir.toString()+File.separator+this.ideImportacaoCdaCefir.toString()+"_PLANILHA_SAIDA.xls";
		}
		return caminhoPlanilhaSaida;
	}

	public void setCaminhoPlanilhaSaida(String caminhoPlanilhaSaida) {
		this.caminhoPlanilhaSaida = caminhoPlanilhaSaida;
	}

	@Override
    public boolean equals(Object object) {
        if (!(object instanceof ImportacaoCdaCefir)) {
            return false;
        }
        ImportacaoCdaCefir other = (ImportacaoCdaCefir) object;
        if ((this.ideImportacaoCdaCefir == null && other.ideImportacaoCdaCefir != null) || (this.ideImportacaoCdaCefir != null && !this.ideImportacaoCdaCefir.equals(other.ideImportacaoCdaCefir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ImportacaoCdaCefir[ ideImportacaoCdaCefir=" + ideImportacaoCdaCefir + " ]";
    }

}
