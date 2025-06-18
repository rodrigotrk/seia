package br.gov.ba.seia.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.ba.seia.entity.GeoBahia;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.util.Util;

public class FiltroImovelRuralRelatorioDTO {
	
	private Integer ideProprietario;
	private Integer ideProcurador;
	private Pessoa responsavelTecnico;
	private Pessoa cadastrante;
	private String nomImovelRural;
	private List<Integer> listMunicipio;
	private List<Integer> listStatusCadastro;
	private Date dtaInicioPeriodoPrimeiraFinalizacao;
	private Date dtaFimPeriodoPrimeiraFinalizacao;
	private Date dtaInicioPeriodoPrimeiraSincronizacao;
	private Date dtaFimPeriodoPrimeiraSincronizacao;
	private Date dtaInicioPeriodoReservaLegal;
	private Date dtaFimPeriodoReservaLegal;
	private String numCertificado;
	private List<Integer> listGestorFinanceiro;
	private List<Integer> listContratoConvenio;
	
	private Integer tipoFiltroSelecionado;	
	private GeoBahia idBiomaSelecionado;
	private GeoBahia idRPGASelecionado;
	private GeoBahia idUnidadeSelecionado;
	private GeoBahia idTerritorioSelecionado;
	private GeoBahia idBaciaSelecionado;
	// 1 - <= 4 / 2 - > 4
	private Integer modulosFiscais;	
	private Integer tipoArl;	
	// 3 - Termo de Compromisso / 2 - Certificado / 4 - Aviso
	// 1 - Preenchido / 2 - Não preenchido
	private Integer questionario;
	private Integer documentoFinal;
	private boolean cadastroBndes;
	private boolean cadastroCda;
	private boolean cadastroIncra;
	private boolean moduloImovelRural;
	private boolean moduloAssentamento;
	private boolean moduloPCT;
	private boolean numeroCar;	
	private boolean praCadastrado;
	private boolean outroPassivoAmbiental;
	private boolean rlAprovada;
	private boolean sobreposicaoRLAPP;
	private boolean rlMenorQueVintePorCento;
	private Boolean statusPendente;
	private boolean limiteBloqueado;
	
	public FiltroImovelRuralRelatorioDTO(){
		this.tipoFiltroSelecionado = 0;
		this.modulosFiscais = null;
		this.tipoArl = null;
		this.documentoFinal = null;
		this.questionario = null;
		this.ideProprietario = null;
		this.ideProcurador = null;
		this.nomImovelRural = null;
		this.listMunicipio = new ArrayList<Integer>();
		this.listStatusCadastro = new ArrayList<Integer>();
		this.dtaInicioPeriodoPrimeiraFinalizacao = null;
		this.dtaFimPeriodoPrimeiraFinalizacao = null;
		this.dtaInicioPeriodoPrimeiraSincronizacao = null;
		this.dtaFimPeriodoPrimeiraSincronizacao = null;
		this.dtaInicioPeriodoReservaLegal = null;
		this.dtaFimPeriodoReservaLegal = null;
		this.numCertificado = null;
		this.listGestorFinanceiro = new ArrayList<Integer>();
		this.listContratoConvenio = new ArrayList<Integer>();
		this.statusPendente = null;
	}
	
	public Integer getIdeProprietario() {
		return ideProprietario;
	}
	public void setIdeProprietario(Integer ideProprietario) {
		this.ideProprietario = ideProprietario;
	}
	public Integer getIdeProcurador() {
		return ideProcurador;
	}
	public void setIdeProcurador(Integer ideProcurador) {
		this.ideProcurador = ideProcurador;
	}
	public String getNomImovelRural() {
		return nomImovelRural;
	}
	public void setNomImovelRural(String nomImovelRural) {
		this.nomImovelRural = nomImovelRural;
	}
	public List<Integer> getListMunicipio() {
		return listMunicipio;
	}
	public void setListMunicipio(List<Integer> listMunicipio) {
		this.listMunicipio = listMunicipio;
	}
	public List<Integer> getListStatusCadastro() {
		return listStatusCadastro;
	}
	public void setListStatusCadastro(List<Integer> listStatusCadastro) {
		this.listStatusCadastro = listStatusCadastro;
	}
	public Date getDtaInicioPeriodoPrimeiraFinalizacao() {
		return dtaInicioPeriodoPrimeiraFinalizacao;
	}
	public void setDtaInicioPeriodoPrimeiraFinalizacao(
			Date dtaInicioPeriodoPrimeiraFinalizacao) {
		this.dtaInicioPeriodoPrimeiraFinalizacao = dtaInicioPeriodoPrimeiraFinalizacao;
	}
	public Date getDtaFimPeriodoPrimeiraFinalizacao() {
		return dtaFimPeriodoPrimeiraFinalizacao;
	}
	public void setDtaFimPeriodoPrimeiraFinalizacao(
			Date dtaFimPeriodoPrimeiraFinalizacao) {
		this.dtaFimPeriodoPrimeiraFinalizacao = dtaFimPeriodoPrimeiraFinalizacao;
	}
	public Date getDtaInicioPeriodoPrimeiraSincronizacao() {
		return dtaInicioPeriodoPrimeiraSincronizacao;
	}
	public void setDtaInicioPeriodoPrimeiraSincronizacao(
			Date dtaInicioPeriodoPrimeiraSincronizacao) {
		this.dtaInicioPeriodoPrimeiraSincronizacao = dtaInicioPeriodoPrimeiraSincronizacao;
	}
	public Date getDtaFimPeriodoPrimeiraSincronizacao() {
		return dtaFimPeriodoPrimeiraSincronizacao;
	}
	public void setDtaFimPeriodoPrimeiraSincronizacao(
			Date dtaFimPeriodoPrimeiraSincronizacao) {
		this.dtaFimPeriodoPrimeiraSincronizacao = dtaFimPeriodoPrimeiraSincronizacao;
	}
	public Date getDtaInicioPeriodoReservaLegal() {
		return dtaInicioPeriodoReservaLegal;
	}
	public void setDtaInicioPeriodoReservaLegal(
			Date dtaInicioPeriodoReservaLegal) {
		this.dtaInicioPeriodoReservaLegal = dtaInicioPeriodoReservaLegal;
	}
	public Date getDtaFimPeriodoReservaLegal() {
		return dtaFimPeriodoReservaLegal;
	}
	public void setDtaFimPeriodoReservaLegal(
			Date dtaFimPeriodoReservaLegal) {
		this.dtaFimPeriodoReservaLegal = dtaFimPeriodoReservaLegal;
	}
	public String getNumCertificado() {
		return numCertificado;
	}
	public void setNumCertificado(String numCertificado) {
		this.numCertificado = numCertificado;
	}
	public List<Integer> getListGestorFinanceiro() {
		return listGestorFinanceiro;
	}
	public void setListGestorFinanceiro(List<Integer> listGestorFinanceiro) {
		this.listGestorFinanceiro = listGestorFinanceiro;
	}
	public List<Integer> getListContratoConvenio() {
		return listContratoConvenio;
	}
	public void setListContratoConvenio(List<Integer> listContratoConvenio) {
		this.listContratoConvenio = listContratoConvenio;
	}
	
	public Integer getTipoFiltroSelecionado() {
		return tipoFiltroSelecionado;
	}

	public void setTipoFiltroSelecionado(Integer tipoFiltroSelecionado) {
		this.tipoFiltroSelecionado = tipoFiltroSelecionado;
	}

	public GeoBahia getIdBiomaSelecionado() {
		if(Util.isNull(this.idBiomaSelecionado)){
			return new GeoBahia();
		}
		return idBiomaSelecionado;
	}

	public void setIdBiomaSelecionado(GeoBahia idBiomaSelecionado) {
		this.idBiomaSelecionado = idBiomaSelecionado;
	}

	public GeoBahia getIdRPGASelecionado() {
		if(Util.isNull(this.idRPGASelecionado)){
			return new GeoBahia();
		}
		return idRPGASelecionado;
	}

	public void setIdRPGASelecionado(GeoBahia idRPGASelecionado) {
		this.idRPGASelecionado = idRPGASelecionado;
	}

	public GeoBahia getIdUnidadeSelecionado() {
		if(Util.isNull(this.idUnidadeSelecionado)){
			return new GeoBahia();
		}
		return idUnidadeSelecionado;
	}

	public void setIdUnidadeSelecionado(GeoBahia idUnidadeSelecionado) {
		this.idUnidadeSelecionado = idUnidadeSelecionado;
	}

	public GeoBahia getIdTerritorioSelecionado() {
		if(Util.isNull(this.idTerritorioSelecionado)){
			return new GeoBahia();
		}
		return idTerritorioSelecionado;
	}

	public void setIdTerritorioSelecionado(GeoBahia idTerritorioSelecionado) {
		this.idTerritorioSelecionado = idTerritorioSelecionado;
	}

	public GeoBahia getIdBaciaSelecionado() {
		if(Util.isNull(this.idBaciaSelecionado)){
			return new GeoBahia();
		}
		return idBaciaSelecionado;
	}

	public void setIdBaciaSelecionado(GeoBahia idBaciaSelecionado) {
		this.idBaciaSelecionado = idBaciaSelecionado;
	}

	public Integer getModulosFiscais() {
		return modulosFiscais;
	}

	public void setModulosFiscais(Integer modulosFiscais) {
		this.modulosFiscais = modulosFiscais;
	}
	
	public Integer getTipoArl() {
		return tipoArl;
	}

	public void setTipoArl(Integer tipoArl) {
		this.tipoArl = tipoArl;
	}

	public Integer getDocumentoFinal() {
		return documentoFinal;
	}

	public void setDocumentoFinal(Integer documentoFinal) {
		this.documentoFinal = documentoFinal;
	}
	
	public Integer getQuestionario() {
		return questionario;
	}

	public void setQuestionario(Integer questionario) {
		this.questionario = questionario;
	}

	public boolean isCadastroCda() {
		return cadastroCda;
	}

	public void setCadastroCda(boolean cadastroCda) {
		this.cadastroCda = cadastroCda;
	}
	
	public boolean isCadastroBndes() {
		return cadastroBndes;
	}

	public void setCadastroBndes(boolean cadastroBndes) {
		this.cadastroBndes = cadastroBndes;
	}

	public boolean isNumeroCar() {
		return numeroCar;
	}

	public void setNumeroCar(boolean numeroCar) {
		this.numeroCar = numeroCar;
	}

	public boolean isPraCadastrado() {
		return praCadastrado;
	}

	public void setPraCadastrado(boolean praCadastrado) {
		this.praCadastrado = praCadastrado;
	}

	public boolean isOutroPassivoAmbiental() {
		return outroPassivoAmbiental;
	}

	public void setOutroPassivoAmbiental(boolean outroPassivoAmbiental) {
		this.outroPassivoAmbiental = outroPassivoAmbiental;
	}

	public boolean isRlAprovada() {
		return rlAprovada;
	}

	public void setRlAprovada(boolean rlAprovada) {
		this.rlAprovada = rlAprovada;
	}

	public boolean isSobreposicaoRLAPP() {
		return sobreposicaoRLAPP;
	}

	public void setSobreposicaoRLAPP(boolean sobreposicaoRLAPP) {
		this.sobreposicaoRLAPP = sobreposicaoRLAPP;
	}

	public boolean isRlMenorQueVintePorCento() {
		return rlMenorQueVintePorCento;
	}

	public void setRlMenorQueVintePorCento(boolean rlMenorQueVintePorCento) {
		this.rlMenorQueVintePorCento = rlMenorQueVintePorCento;
	}

	public boolean isCadastroIncra() {
		return cadastroIncra;
	}

	public void setCadastroIncra(boolean cadastroIncra) {
		this.cadastroIncra = cadastroIncra;
	}

	public Boolean getStatusPendente() {
		return statusPendente;
	}

	public void setStatusPendente(Boolean statusPendente) {
		this.statusPendente = statusPendente;
	}

	public Pessoa getResponsavelTecnico() {
		return responsavelTecnico;
	}

	public void setResponsavelTecnico(Pessoa responsavelTecnico) {
		this.responsavelTecnico = responsavelTecnico;
	}

	public boolean isLimiteBloqueado() {
		return limiteBloqueado;
	}

	public void setLimiteBloqueado(boolean limiteBloqueado) {
		this.limiteBloqueado = limiteBloqueado;
	}

	public boolean isModuloImovelRural() {
		return moduloImovelRural;
	}

	public void setModuloImovelRural(boolean moduloImovelRural) {
		this.moduloImovelRural = moduloImovelRural;
	}

	public boolean isModuloAssentamento() {
		return moduloAssentamento;
	}

	public void setModuloAssentamento(boolean moduloAssentamento) {
		this.moduloAssentamento = moduloAssentamento;
	}

	public boolean isModuloPCT() {
		return moduloPCT;
	}

	public void setModuloPCT(boolean moduloPCT) {
		this.moduloPCT = moduloPCT;
	}
}