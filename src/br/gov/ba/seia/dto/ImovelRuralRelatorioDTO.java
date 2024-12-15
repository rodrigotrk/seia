package br.gov.ba.seia.dto;

import static org.junit.Assert.fail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.gov.ba.seia.enumerator.StatusCadastroImovelRuralEnum;
import br.gov.ba.seia.util.Util;


public class ImovelRuralRelatorioDTO implements Serializable {

	private static final long serialVersionUID = -559305069996133815L;
	
	private Integer ideImovelRural;
	private String nomImovelRural;
	private String nomMunicipio;
	private String nomStatusCadastro;
	private Date dtaFinalizacao;
	private Date dtaSincronizacao;
	private Date dtaPrimeiraFinalizacao;
	private Date dtaAprovacaoReservaLegal;	
	private String nomTipoDocumento;
	private Double valArea;
	private Double qtdModuloFiscal;
	private String nomStatusReservaLegal;
	private String numContratoConvenio;
	private String nomRequerente;
	private String numCpfCnpjRequerente;
	private String urlReciboInscricao;
	private boolean stsCertificado;
	private boolean isAvisoBndes;
	private String nomeResponsavelTecnico;
	private String cpfCadastrante;
	private String nomeCadastrante;
	
	

	public String getNomeResponsavelTecnico() {
		return nomeResponsavelTecnico;
	}

	public void setNomeResponsavelTecnico(String nomeResponsavelTecnico) {
		this.nomeResponsavelTecnico = nomeResponsavelTecnico;
	}

	public String getCpfCadastrante() {
		return cpfCadastrante;
	}

	public void setCpfCadastrante(String cpfCadastrante) {
		this.cpfCadastrante = cpfCadastrante;
	}

	public String getNomeCadastrante() {
		return nomeCadastrante;
	}

	public void setNomeCadastrante(String nomeCadastrante) {
		this.nomeCadastrante = nomeCadastrante;
	}

	public ImovelRuralRelatorioDTO(){
		this.ideImovelRural = null;
		this.nomImovelRural = null;
		this.nomMunicipio = null;
		this.nomStatusCadastro = null;
		this.dtaFinalizacao = null;
		this.dtaSincronizacao = null;
		this.dtaPrimeiraFinalizacao = null;
		this.dtaAprovacaoReservaLegal = null;
		this.nomTipoDocumento = null;
		this.valArea = null;
		this.qtdModuloFiscal = null;
		this.nomStatusReservaLegal = null;
		this.numContratoConvenio = null;
		this.nomRequerente = null;
		this.numCpfCnpjRequerente = null;
		this.urlReciboInscricao = null;
	}
	
	public ImovelRuralRelatorioDTO(Object[] resultElement, boolean consultaSimples) {
		this.ideImovelRural = (Integer) resultElement[0];
		if(!Util.isNull(resultElement[1])) {
			this.valArea = ((BigDecimal) resultElement[1]).doubleValue();
		}

		if(!consultaSimples){
			if(!Util.isNull(resultElement[3])) {
				this.nomMunicipio = (String) resultElement[3];
			}
			if(!Util.isNull(resultElement[4])) {
				this.nomStatusCadastro = (String) resultElement[4];
			}
			if(!Util.isNull(resultElement[5])) {
				this.dtaFinalizacao = (Date) resultElement[5];
			}
			if(!Util.isNull(resultElement[6])) {
				this.dtaAprovacaoReservaLegal = (Date) resultElement[6];
			}
			if(!Util.isNull(resultElement[7])) {
				this.nomTipoDocumento = (String) resultElement[7];
			}
			
			this.nomImovelRural = (String) resultElement[8];

			if(!Util.isNull(resultElement[9])) {
				this.qtdModuloFiscal = ((BigDecimal) resultElement[9]).doubleValue();
			}
			if(!Util.isNull(resultElement[10])) {
				this.nomStatusReservaLegal = (String) resultElement[10];
			}
			if(!Util.isNull(resultElement[11])) {
				this.numContratoConvenio = (String) resultElement[11];
			}
			if(!Util.isNull(resultElement[12])) {
				this.nomRequerente = (String) resultElement[12];
			}
			if(!Util.isNull(resultElement[13])) {
				this.numCpfCnpjRequerente = (String) resultElement[13];
			}
			if(!Util.isNull(resultElement[14])) {
				this.stsCertificado = (Boolean) resultElement[14];
			}
			if(!Util.isNull(resultElement[15])) {
				this.dtaPrimeiraFinalizacao = (Date) resultElement[15];			
			}
			if(!Util.isNull(resultElement[16])) {
				this.urlReciboInscricao = (String) resultElement[16];
			}
			if(!Util.isNull(resultElement[17])) {
				this.isAvisoBndes = (Boolean) resultElement[17];			
			}
			if(!Util.isNull(resultElement[18])) {
				this.dtaSincronizacao = (Date) resultElement[18];			
			}
			if(!Util.isNull(resultElement[19])) {
				this.nomeResponsavelTecnico = (String) resultElement[19];			
			}
			
			if(!Util.isNull(resultElement[20])) {
				this.nomeCadastrante = (String) resultElement[20];
			}
			
			if(!Util.isNull(resultElement[21])) {
				this.cpfCadastrante = (String) resultElement[21];
			}
			
		}
		
	}
	
	public Integer getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(Integer ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public String getNomImovelRural() {
		return nomImovelRural;
	}
	public void setNomImovelRural(String nomImovelRural) {
		this.nomImovelRural = nomImovelRural;
	}
	public String getNomMunicipio() {
		return nomMunicipio;
	}
	public void setNomMunicipio(String nomMunicipio) {
		this.nomMunicipio = nomMunicipio;
	}
	public String getNomStatusCadastro() {
		return nomStatusCadastro;
	}
	public void setNomStatusCadastro(String nomStatusCadastro) {
		this.nomStatusCadastro = nomStatusCadastro;
	}
	public Date getDtaFinalizacao() {
		return dtaFinalizacao;
	}
	public void setDtaFinalizacao(Date dtaFinalizacao) {
		this.dtaFinalizacao = dtaFinalizacao;
	}
	public Date getDtaSincronizacao() {
		return dtaSincronizacao;
	}
	public void setDtaSincronizacao(Date dtaSincronizacao) {
		this.dtaSincronizacao = dtaSincronizacao;
	}
	public Date getDtaPrimeiraFinalizacao() {
		return dtaPrimeiraFinalizacao;
	}
	public void setDtaPrimeiraFinalizacao(Date dtaPrimeiraFinalizacao) {
		this.dtaPrimeiraFinalizacao = dtaPrimeiraFinalizacao;
	}
	public String getNomTipoDocumento() {
		return nomTipoDocumento;
	}
	public void setNomTipoDocumento(String nomTipoDocumento) {
		this.nomTipoDocumento = nomTipoDocumento;
	}
	public Double getValArea() {
		return valArea;
	}
	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}
	public Double getQtdModuloFiscal() {
		return qtdModuloFiscal;
	}
	public void setQtdModuloFiscal(Double qtdModuloFiscal) {
		this.qtdModuloFiscal = qtdModuloFiscal;
	}
	public String getNomStatusReservaLegal() {
		return nomStatusReservaLegal;
	}
	public void setNomStatusReservaLegal(String nomStatusReservaLegal) {
		this.nomStatusReservaLegal = nomStatusReservaLegal;
	}
	public String getNumContratoConvenio() {
		return numContratoConvenio;
	}
	public void setNumContratoConvenio(String numContratoConvenio) {
		this.numContratoConvenio = numContratoConvenio;
	}
	public String getNomRequerente() {
		return nomRequerente;
	}
	public void setNomRequerente(String nomRequerente) {
		this.nomRequerente = nomRequerente;
	}
	public String getNumCpfCnpjRequerente() {
		return numCpfCnpjRequerente;
	}
	public void setNumCpfCnpjRequerente(String numCpfCnpjRequerente) {
		this.numCpfCnpjRequerente = numCpfCnpjRequerente;
	}

	public boolean isStsCertificado() {
		return stsCertificado && isCadastrado();
	}

	public void setStsCertificado(boolean stsCertificado) {
		this.stsCertificado = stsCertificado;
	}

	public boolean isAvisoBndes() {
		return isAvisoBndes;
	}

	public void setAvisoBndes(boolean isAvisoBndes) {
		this.isAvisoBndes = isAvisoBndes;
	}

	public boolean isTermoCompromisso() {
		return !stsCertificado && isCadastrado();
	}

	private boolean isCadastrado() {
		return nomStatusCadastro.equalsIgnoreCase(StatusCadastroImovelRuralEnum.CADASTRADO.name());
	}
	
	public boolean isCnpj(){
		return numCpfCnpjRequerente.length() == 14;
	}
	
	public boolean isCpf(){
		return numCpfCnpjRequerente.length() == 11;
	}

	public String getUrlReciboInscricao() {
		return urlReciboInscricao;
	}

	public void setUrlReciboInscricao(String urlReciboInscricao) {
		this.urlReciboInscricao = urlReciboInscricao;
	}
}
