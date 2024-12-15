package br.gov.ba.seia.dto;

import java.util.HashMap;

import java.util.Map;

import br.gov.ba.seia.util.Util;


public class ColunasExibirImovelRuralRelatorioDTO {
	
	private boolean nomImovelRural;
	private boolean nomMunicipio;
	private boolean nomStatusCadastro;
	private boolean dtaFinalizacao;
	private boolean dtaSincronizacao;
	private boolean dtaPrimeiraFinalizacao;
	private boolean nomTipoDocumento;
	private boolean valArea;
	private boolean qtdModuloFiscal;
	private boolean nomStatusReservaLegal;
	private boolean numContratoConvenio;
	private boolean nomRequerente;
	private boolean numCpfCnpjRequerente;
	private boolean proprietario;
	private boolean responsavelTecnico;
	private boolean nomeCadastrante;
	private boolean cpfCadastrante;
	

	public boolean isNomeCadastrante() {
		return nomeCadastrante;
	}

	public void setNomeCadastrante(boolean nomeCadastrante) {
		this.nomeCadastrante = nomeCadastrante;
	}

	public boolean isCpfCadastrante() {
		return cpfCadastrante;
	}

	public void setCpfCadastrante(boolean cpfCadastrante) {
		this.cpfCadastrante = cpfCadastrante;
	}
	
	public boolean isResponsavelTecnico() {
		return responsavelTecnico;
	}

	public void setResponsavelTecnico(boolean responsavelTecnico) {
		this.responsavelTecnico = responsavelTecnico;
	}

	private Map<String, String> mapColunas;
	
	public ColunasExibirImovelRuralRelatorioDTO(){
		preparaMapColunas();
	}

	public boolean isNomImovelRural() {
		return nomImovelRural;
	}

	public void setNomImovelRural(boolean nomImovelRural) {
		this.nomImovelRural = nomImovelRural;
	}

	public boolean isNomMunicipio() {
		return nomMunicipio;
	}

	public void setNomMunicipio(boolean nomMunicipio) {
		this.nomMunicipio = nomMunicipio;
	}

	public boolean isNomStatusCadastro() {
		return nomStatusCadastro;
	}

	public void setNomStatusCadastro(boolean nomStatusCadastro) {
		this.nomStatusCadastro = nomStatusCadastro;
	}

	public boolean isDtaFinalizacao() {
		return dtaFinalizacao;
	}

	public void setDtaFinalizacao(boolean dtaFinalizacao) {
		this.dtaFinalizacao = dtaFinalizacao;
	}
	
	public boolean isDtaSincronizacao() {
		return dtaSincronizacao;
	}

	public void setDtaSincronizacao(boolean dtaSincronizacao) {
		this.dtaSincronizacao = dtaSincronizacao;
	}

	public boolean isDtaPrimeiraFinalizacao() {
		return dtaPrimeiraFinalizacao;
	}

	public void setDtaPrimeiraFinalizacao(boolean dtaPrimeiraFinalizacao) {
		this.dtaPrimeiraFinalizacao = dtaPrimeiraFinalizacao;
	}
	
	public boolean isNomTipoDocumento() {
		return nomTipoDocumento;
	}

	public void setNomTipoDocumento(boolean nomTipoDocumento) {
		this.nomTipoDocumento = nomTipoDocumento;
	}

	public boolean isValArea() {
		return valArea;
	}

	public void setValArea(boolean valArea) {
		this.valArea = valArea;
	}

	public boolean isQtdModuloFiscal() {
		return qtdModuloFiscal;
	}

	public void setQtdModuloFiscal(boolean qtdModuloFiscal) {
		this.qtdModuloFiscal = qtdModuloFiscal;
	}

	public boolean isNomStatusReservaLegal() {
		return nomStatusReservaLegal;
	}

	public void setNomStatusReservaLegal(boolean nomStatusReservaLegal) {
		this.nomStatusReservaLegal = nomStatusReservaLegal;
	}

	public boolean isNumContratoConvenio() {
		return numContratoConvenio;
	}

	public void setNumContratoConvenio(boolean numContratoConvenio) {
		this.numContratoConvenio = numContratoConvenio;
	}

	public boolean isNomRequerente() {
		return nomRequerente;
	}

	public void setNomRequerente(boolean nomRequerente) {
		this.nomRequerente = nomRequerente;
	}

	public boolean isNumCpfCnpjRequerente() {
		return numCpfCnpjRequerente;
	}

	public void setNumCpfCnpjRequerente(boolean numCpfCnpjRequerente) {
		this.numCpfCnpjRequerente = numCpfCnpjRequerente;
	}
	
	public boolean isProprietario() {
		return proprietario;
	}

	public void setProprietario(boolean proprietario) {
		this.proprietario = proprietario;
	}
	
	
	public void preparaMapColunas() {
		this.mapColunas = new HashMap<String, String>();
		
		if (nomImovelRural) {
			mapColunas.put(Util.getString("cefir_lbl_nome_imovel_rural"), "nomImovelRural");
		}
		if (nomMunicipio) {
			mapColunas.put(Util.getString("consulta_empree_municipio"), "nomMunicipio");
		}
		if (nomStatusCadastro) {
			mapColunas.put(Util.getString("cefir_lbl_status_cadastro"), "nomStatusCadastro");
		}
		if (dtaPrimeiraFinalizacao) {
			mapColunas.put(Util.getString("cefir_lbl_dtc_primeira_finalizacao"), "dtaPrimeiraFinalizacao");
		}
		if (nomRequerente) {
			mapColunas.put(Util.getString("cefir_lbl_nome_requerente"), "nomRequerente");
		}
		if (valArea) {
			mapColunas.put(Util.getString("cefir_msg_relatorio_area_cartorio"), "valArea");
		}
		if (qtdModuloFiscal) {
			mapColunas.put(Util.getString("cefir_lbl_quantidade_modulos_fiscais"), "qtdModuloFiscal");
		}
		if (nomStatusReservaLegal) {
			mapColunas.put(Util.getString("cefir_lbl_status_reserva_legal"), "nomStatusReservaLegal");
		}
		if (numContratoConvenio) {
			mapColunas.put(Util.getString("cefir_lbl_relatorio_contrato_convenio"), "numContratoConvenio");
		}
		if (nomTipoDocumento) {
			mapColunas.put(Util.getString("cefir_lbl_tipo_documento"), "nomTipoDocumento");
		}
		if (numCpfCnpjRequerente) {
			mapColunas.put(Util.getString("cefir_lbl_cpf_cnpj_requerente"), "numCpfCnpjRequerente");
		}
		if (dtaFinalizacao) {
			mapColunas.put(Util.getString("cefir_lbl_data_finalizacao"), "dtaFinalizacao");
		}
		if (dtaSincronizacao) {
			mapColunas.put(Util.getString("cefir_lbl_data_sincronizacao"), "dtaSincronizacao");
		}
		if(proprietario){
			mapColunas.put(Util.getString("cefir_lbl_proprietario"), "proprietario");
		}
		if(responsavelTecnico) {
			mapColunas.put(Util.getString("cefir_lbl_responsavel_tecnico"), "responsavelTecnico");
		}
		
		if(nomeCadastrante) {
			mapColunas.put(Util.getString("cefir_lbl_nome_cadastrante"), "nomeCadastrante");
		}
		
		if(cpfCadastrante) {
			mapColunas.put(Util.getString("cefir_lbl_cpf_cadastrante"), "cpfCadastrante");
		}
		
	}

	public Map<String, String> getMapColunas() {
		return mapColunas;
	}
	
	public boolean getLimiteExibicaoAtingido() {
		if(!Util.isNull(mapColunas) && mapColunas.size() == 6) {
			return true;
		}
		return false;
	}
	
	public int getQtdColunasExibir() {
		if(!Util.isNullOuVazio(this.mapColunas))
			return mapColunas.size();
		else
			return 0;
	}


}
