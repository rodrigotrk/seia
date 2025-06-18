package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralAbastecimento;
import br.gov.ba.seia.entity.ImovelRuralPrad;
import br.gov.ba.seia.entity.ImovelRuralTac;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.PctImovelRural;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ReservaLegalTramite;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.entity.SupressaoVegetacao;
import br.gov.ba.seia.entity.TipoVinculoImovel;

public class ImovelRuralDTO {
	
	private ImovelRural imovelRural;
	private PessoaImovel pessoaImovel;
	private PessoaImovel pessoaQueSalvouOuEditouImovel;
	private PessoaImovel pessoaImovelSolicitante;
	private ReservaLegal reservaLegal;
	private Empreendimento empreendimento;
	private ReservaLegalTramite reservaTramite;
	private ImovelRuralAbastecimento imovelRuralAbastecimento;
	private SupressaoVegetacao supressaoVegetacao;
	private ImovelRuralTac imovelRuralTac;
	private ImovelRuralPrad imovelRuralPrad;
	//CAMPOS INSERIDOS PARA DIMINUIR O NUMERO DE PARAMETROS DO METODO "ImovelRuralService.listarPorCriteriaDemanda(*)" QUE CONSULTA OS IMOVEIS RURAIS CADASTRADOS.
	private Municipio municipio;
	private Pessoa proprietarioOuProcurador;
	private Pessoa solicitante;
	private Pessoa requerente;
	private String imoveisAValidar;	
	private Boolean vinculaRequerenteDeProcurador;
	private TipoVinculoImovel tipoVinculoRequerenteDeProcurador;
	private StatusReservaLegal statusReservaLegal;
	private String numCertificado;
	private String pontosPesquisa;
	private Boolean bloqueioAsv;
	private Boolean imovelCDA;
	private Boolean imovelBNDES;
	private Double valArea;
	private Boolean stsCertificado;  //Redmine 8381 -  danilos.santos
	private Boolean indSuspensao;
	private Boolean indBloqueioLimite;
	private Boolean indRestaurado;
	private String numSicar;
	private PctImovelRural pctImovelRural;
	private String tipoCadastro;
    private String numeroMatricula;
    private Pessoa responsavelTecnico;
    private Pessoa procurador;
 
	
	private String urlVoltar = "/paginas/manter-imoveis/cefir/listaImoveisRural.xhtml";
	
	

	public ImovelRuralDTO () {
		imovelRural = null;
		pessoaImovel = null;
		reservaLegal = null;
		empreendimento = null;
		reservaTramite = null;
		imovelRuralAbastecimento = null;
		supressaoVegetacao = null;
		imovelRuralTac = null;
		imovelRuralPrad = null;
		pessoaQueSalvouOuEditouImovel = null;
		proprietarioOuProcurador = null;
		municipio = null;
		solicitante = null;
		imoveisAValidar = null;
		statusReservaLegal = null;
		numCertificado = null;
		bloqueioAsv = null;
		stsCertificado = null;
		numSicar=null;
		pctImovelRural = null;
		tipoCadastro = null;
		responsavelTecnico = null;
		numeroMatricula =null;
		procurador =null;
		indBloqueioLimite = null;
	}	
	
	
	public PessoaImovel getPessoaQueSalvouOuEditouImovel() {
		return pessoaQueSalvouOuEditouImovel;
	}
	
	
	public void setPessoaQueSalvouOuEditouImovel(PessoaImovel pessoaQueSalvouOuEditouImovel) {
		this.pessoaQueSalvouOuEditouImovel = pessoaQueSalvouOuEditouImovel;
	}
	
	public ImovelRural getImovelRural() {
		return imovelRural;
	}
	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}
	public PessoaImovel getPessoaImovel() {
		return pessoaImovel;
	}
	public void setPessoaImovel(PessoaImovel pessoaImovel) {
		this.pessoaImovel = pessoaImovel;
	}
	public ReservaLegal getReservaLegal() {
		return reservaLegal;
	}
	public void setReservaLegal(ReservaLegal reservaLegal) {
		this.reservaLegal = reservaLegal;
	}


	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}


	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}


	public ReservaLegalTramite getReservaTramite() {
		return reservaTramite;
	}


	public void setReservaTramite(ReservaLegalTramite reservaTramite) {
		this.reservaTramite = reservaTramite;
	}


	public Municipio getMunicipio() {
		return municipio;
	}


	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}


	public Pessoa getProprietarioOuProcurador() {
		return proprietarioOuProcurador;
	}


	public void setProprietarioOuProcurador(Pessoa proprietarioOuProcurador) {
		this.proprietarioOuProcurador = proprietarioOuProcurador;
	}


	public Pessoa getSolicitante() {
		return solicitante;
	}


	public void setSolicitante(Pessoa solicitante) {
		this.solicitante = solicitante;
	}


	public String getImoveisAValidar() {
		return imoveisAValidar;
	}


	public void setImoveisAValidar(String imoveisAValidar) {
		this.imoveisAValidar = imoveisAValidar;
	}


	public ImovelRuralAbastecimento getImovelRuralAbastecimento() {
		return imovelRuralAbastecimento;
	}


	public void setImovelRuralAbastecimento(ImovelRuralAbastecimento imovelRuralAbastecimento) {
		this.imovelRuralAbastecimento = imovelRuralAbastecimento;
	}


	public SupressaoVegetacao getSupressaoVegetacao() {
		return supressaoVegetacao;
	}

	public void setSupressaoVegetacao(SupressaoVegetacao supressaoVegetacao) {
		this.supressaoVegetacao = supressaoVegetacao;
	}
	
	public void setImovelRuralTac(ImovelRuralTac imovelRuralTac) {
		this.imovelRuralTac = imovelRuralTac;
	}
	
	public ImovelRuralTac getImovelRuralTac() {
		return imovelRuralTac;
	}
	
	public void setImovelRuralPrad(ImovelRuralPrad imovelRuralPrad) {
		this.imovelRuralPrad = imovelRuralPrad;
	}
	
	public ImovelRuralPrad getImovelRuralPrad() {
		return imovelRuralPrad;
	}


	public Boolean getVinculaRequerenteDeProcurador() {
		return vinculaRequerenteDeProcurador;
	}


	public void setVinculaRequerenteDeProcurador(
			Boolean vinculaRequerenteDeProcurador) {
		this.vinculaRequerenteDeProcurador = vinculaRequerenteDeProcurador;
	}


	public TipoVinculoImovel getTipoVinculoRequerenteDeProcurador() {
		return tipoVinculoRequerenteDeProcurador;
	}


	public void setTipoVinculoRequerenteDeProcurador(
			TipoVinculoImovel tipoVinculoRequerenteDeProcurador) {
		this.tipoVinculoRequerenteDeProcurador = tipoVinculoRequerenteDeProcurador;
	}


	public Pessoa getRequerente() {
		return requerente;
	}


	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}


	public PessoaImovel getPessoaImovelSolicitante() {
		return pessoaImovelSolicitante;
	}


	public void setPessoaImovelSolicitante(PessoaImovel pessoaImovelSolicitante) {
		this.pessoaImovelSolicitante = pessoaImovelSolicitante;
	}
	
	
	public String getUrlVoltar() {
		return urlVoltar;
	}
	
	public void setUrlVoltar(String urlVoltar) {
		this.urlVoltar = urlVoltar;
	}
	
	public StatusReservaLegal getStatusReservaLegal() {
		return statusReservaLegal;
	}

	public void setStatusReservaLegal(StatusReservaLegal statusReservaLegal) {
		this.statusReservaLegal = statusReservaLegal;
	}
	
	public Boolean getBloqueioAsv() {
		return bloqueioAsv;
	}
	
	public void setBloqueioAsv(Boolean bloqueioAsv) {
		this.bloqueioAsv = bloqueioAsv;
	}
	
	public String getNumCertificado() {
		return numCertificado;
	}

	public void setNumCertificado(String numCertificado) {
		this.numCertificado = numCertificado;
	}
	
	public String getPontosPesquisa() {
		return pontosPesquisa;
	}
	
	public void setPontosPesquisa(String pontosPesquisa) {
		this.pontosPesquisa = pontosPesquisa;
	}


	public Double getValArea() {
		return valArea;
	}


	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}


	public Boolean getImovelCDA() {
		return imovelCDA;
	}


	public void setImovelCDA(Boolean imovelCDA) {
		this.imovelCDA = imovelCDA;
	}
	
	public Boolean getImovelBNDES() {
		return imovelBNDES;
	}


	public void setImovelBNDES(Boolean imovelBNDES) {
		this.imovelBNDES = imovelBNDES;
	}


	public Boolean getStsCertificado() {
		return stsCertificado;
	}

	public void setStsCertificado(Boolean stsCertificado) {
		this.stsCertificado = stsCertificado;

	}
	
	public Boolean getIndSuspensao() {
		return indSuspensao;
	}


	public void setIndSuspensao(Boolean indSuspensao) {
		this.indSuspensao = indSuspensao;
	}


	public String getNumSicar() {
		return numSicar;
	}


	public void setNumSicar(String numSicar) {
		this.numSicar = numSicar;
	}
	
	
	public PctImovelRural getPctImovelRural() {
		return pctImovelRural;
	}


	public void setPctImovelRural(PctImovelRural pctImovelRural) {
		this.pctImovelRural = pctImovelRural;
	}


	public String getTipoCadastro() {
		return tipoCadastro;
	}


	public void setTipoCadastro(String tipoCadastro) {
		this.tipoCadastro = tipoCadastro;
	}


	public String getNumeroMatricula() {
		return numeroMatricula;
	}


	public void setNumeroMatricula(String numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}


	public Pessoa getResponsavelTecnico() {
		return responsavelTecnico;
	}


	public void setResponsavelTecnico(Pessoa responsavelTecnico) {
		this.responsavelTecnico = responsavelTecnico;
	}


	public Pessoa getProcurador() {
		return procurador;
	}


	public void setProcurador(Pessoa procurador) {
		this.procurador = procurador;
	}
	
	public Boolean getIndBloqueioLimite() {
		return indBloqueioLimite;
	}


	public void setIndBloqueioLimite(Boolean indBloqueioLimite) {
		this.indBloqueioLimite = indBloqueioLimite;
	}


	public Boolean getIndRestaurado() {
		return indRestaurado;
	}


	public void setIndRestaurado(Boolean indRestaurado) {
		this.indRestaurado = indRestaurado;
	}


}
