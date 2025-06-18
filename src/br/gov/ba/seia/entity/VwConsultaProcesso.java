package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.util.Util;

public class VwConsultaProcesso implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer ideProcesso;
	private String numProcesso;
	private String nomMunicipio;
	private Date dtcFormacao;
	private Integer ideEmpreendimento;
	private String nomEmpreendimento;
	private Boolean indUnidadeConservacao;
	private String desEmail;
	private Integer ideTipoPessoaRequerimento;
	private Integer ideRequerimento;
	private String numRequerimento;
	private Date dtcCriacaoRequerimento;
	private Integer idePorte;
	private String nomPorte;
	private String nomLogradouro;
	private Integer idePessoaRequerente;
	private String nomRequerente;
	private String numCpfcnpjRequerente;
	private Date dtcTramitacao;
	private Integer ideFuncionario;
	private Integer idePerfil;
	private Integer ideArea;
	private Integer ideAreaTramitacao;
	private Integer idePautaTramitacao;
	private String nomAreaTramitacao;
	private Integer ideMunicipio;
	private String idesAtosAmbientais;
	private Integer tipoNotificacaoEmAprovacao;
	private Boolean indEstadoEmergencia;
	private Funcionario funcionarioLiderEquipe;
	private GrupoProcesso grupoProcesso;
	private Area area;
	private StatusFluxo statusFluxo;
	private String nomStatus;
	private Long qtdDiasFormado;
	private Lac lac;
	private ProcessoReenquadramento processoReenquadramento;
	private String desEmailRequerimento;
	
	private boolean podeEncaminhar;
	private boolean podeFormarEquipe;
	private boolean indLiderEquipe;
	private boolean podeNotificar;
	private boolean podeAprovarNotificacao;
	private boolean podeApensarDocumento;
	private boolean podeExibirAnaliseTecnicaImovelRural;
	private boolean podeReabrirProcesso;
	private boolean podeAprovarAnaliseTecnica;
	private boolean podeAtualizarPassivo;
	private boolean renderedNotificacaoPrazo;
	private boolean renderedNotificacaoComunicacao;
	private boolean renderedReservaAgua;
	private boolean renderedLacPosto;
	private boolean renderedLacErb;
	private boolean renderedLacTransportadora;
	private boolean renderedDqc;
	private boolean renderedRfp;
	private boolean renderedRcfp;
	private boolean renderedRlac;
	private boolean renderedAPE;
	private boolean statusAguardandoAprovacao;
	
	private Collection<ProcessoAto> listProcessoAto;
	private Collection<ProcessoAto> listProcessoAtoReequadramento;
	private Collection<RequerimentoTipologia> requerimentoTipologiaList;
	private Collection<EmpreendimentoRequerimento> empreendimentoRequerimentoList;
	private Collection<ControleTramitacao> controleTramitacaoAllList;
	private Collection<Tipologia> tipologiaList;
	private Collection<ControleFluxo> controleFluxoList;
	private Collection<ComprovantePagamento> comprovantePagamentoList;
	private Collection<ComprovantePagamento> comprovantePagamentoComplementarList;
	private Collection<ComprovantePagamentoDae> comprovantePagamentoDaeList;
	private Collection<ArquivoProcesso> arquivoProcessoList;
	private Collection<ArquivoProcesso> arquivoProcessoNotificacaoList;
	private Collection<Notificacao> notificacaoEnviadaList;
	private Collection<Notificacao> notificacaoReprovadaList;
	private Collection<NotificacaoParcial> notificacaoParcialList;
	private Collection<PerguntaRequerimento> perguntas;
	private Collection<DocumentoIdentificacaoRequerimento> documentoIdentificacaoRequerimentoList;
	private Collection<DocumentoObrigatorioRequerimento> documentoObrigatorioRequerimentoList;
	private Collection<DocumentoRepresentacaoRequerimento> documentoRepresentacaoRequerimentoList;
	private Collection<DocumentoObrigatorioReenquadramento> documentoObrigatorioReenquadramentoList;
	private Collection<ProcessoReenquadramentoHistAto> processoReenquadramentoHistAtoList;
	private Collection<DocumentoRequerimentoEmpreendimento> documentoRequerimentoEmpreendimentoList;
	
	public VwConsultaProcesso() {
		listProcessoAto = new ArrayList<ProcessoAto>();
		listProcessoAtoReequadramento = new ArrayList<ProcessoAto>();
		controleTramitacaoAllList = new ArrayList<ControleTramitacao>();
		tipologiaList = new ArrayList<Tipologia>();
		controleFluxoList = new ArrayList<ControleFluxo>();
		comprovantePagamentoList = new ArrayList<ComprovantePagamento>();
		comprovantePagamentoComplementarList = new ArrayList<ComprovantePagamento>();
		comprovantePagamentoDaeList = new ArrayList<ComprovantePagamentoDae>();
		arquivoProcessoList = new ArrayList<ArquivoProcesso>();
		arquivoProcessoNotificacaoList = new ArrayList<ArquivoProcesso>();
		notificacaoEnviadaList = new ArrayList<Notificacao>();
		notificacaoReprovadaList = new ArrayList<Notificacao>();
		notificacaoParcialList = new ArrayList<NotificacaoParcial>();
		documentoIdentificacaoRequerimentoList = new ArrayList<DocumentoIdentificacaoRequerimento>();
		documentoObrigatorioRequerimentoList = new ArrayList<DocumentoObrigatorioRequerimento>();
		documentoRepresentacaoRequerimentoList = new ArrayList<DocumentoRepresentacaoRequerimento>();
		processoReenquadramentoHistAtoList = new ArrayList<ProcessoReenquadramentoHistAto>();
		documentoRequerimentoEmpreendimentoList = new ArrayList<DocumentoRequerimentoEmpreendimento>();
		
	}
	
	public boolean verificarStatus(StatusFluxoEnum statusFluxoEnum) {
		return new StatusFluxo(statusFluxoEnum.getStatus()).equals(statusFluxo);
	}

	public VwConsultaProcesso(Integer ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	public Integer getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Integer ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public String getNumProcessoFormatado() {
		return numProcesso.substring(0, 15).concat(" " + numProcesso.substring(15));
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public String getNomMunicipio() {
		return nomMunicipio;
	}

	public void setNomMunicipio(String nomMunicipio) {
		this.nomMunicipio = nomMunicipio;
	}

	public Date getDtcFormacao() {
		return dtcFormacao;
	}

	public void setDtcFormacao(Date dtcFormacao) {
		this.dtcFormacao = dtcFormacao;
	}

	public String getNomEmpreendimento() {
		return nomEmpreendimento;
	}

	public void setNomEmpreendimento(String nomEmpreendimento) {
		this.nomEmpreendimento = nomEmpreendimento;
	}

	public String getDesEmail() {
		return desEmail;
	}

	public void setDesEmail(String desEmail) {
		this.desEmail = desEmail;
	}

	public Integer getIdeTipoPessoaRequerimento() {
		return ideTipoPessoaRequerimento;
	}

	public void setIdeTipoPessoaRequerimento(Integer ideTipoPessoaRequerimento) {
		this.ideTipoPessoaRequerimento = ideTipoPessoaRequerimento;
	}

	public Integer getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Integer ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public String getNumRequerimento() {
		return numRequerimento;
	}

	public void setNumRequerimento(String numRequerimento) {
		this.numRequerimento = numRequerimento;
	}

	public Date getDtcCriacaoRequerimento() {
		return dtcCriacaoRequerimento;
	}

	public void setDtcCriacaoRequerimento(Date dtcCriacaoRequerimento) {
		this.dtcCriacaoRequerimento = dtcCriacaoRequerimento;
	}

	public Integer getIdePorte() {
		return idePorte;
	}

	public void setIdePorte(Integer idePorte) {
		this.idePorte = idePorte;
	}

	public Integer getIdePessoaRequerente() {
		return idePessoaRequerente;
	}

	public void setIdePessoaRequerente(Integer idePessoaRequerente) {
		this.idePessoaRequerente = idePessoaRequerente;
	}

	public String getNomRequerente() {
		return nomRequerente;
	}

	public void setNomRequerente(String nomRequerente) {
		this.nomRequerente = nomRequerente;
	}

	public String getNumCpfcnpjRequerente() {
		return numCpfcnpjRequerente;
	}

	public String getNumCpfcnpjRequerenteFormatado() {
		String cpfCnpj = this.numCpfcnpjRequerente;

		// CNPJ
		if (cpfCnpj != null && cpfCnpj.length() == 14) {
			cpfCnpj = cpfCnpj.substring(0, 2) + "." + cpfCnpj.substring(2, 5)
					+ "." + cpfCnpj.substring(5, 8) + "/"
					+ cpfCnpj.substring(8, 12) + "-"
					+ cpfCnpj.substring(12, 14);
		}
		// CPF
		else if (cpfCnpj != null && cpfCnpj.length() == 11) {
			cpfCnpj = cpfCnpj.substring(0, 3) + "." + cpfCnpj.substring(3, 6)
					+ "." + cpfCnpj.substring(6, 9) + "-"
					+ cpfCnpj.substring(9, 11);
		}

		return cpfCnpj;
	}

	public void setNumCpfcnpjRequerente(String numCpfcnpjRequerente) {
		this.numCpfcnpjRequerente = numCpfcnpjRequerente;
	}


	public Collection<ControleTramitacao> getControleTramitacaoAllList() {
		return controleTramitacaoAllList;
	}

	public void setControleTramitacaoAllList(
			Collection<ControleTramitacao> controleTramitacaoAllList) {
		this.controleTramitacaoAllList = controleTramitacaoAllList;
	}

	public Collection<ControleFluxo> getControleFluxoList() {
		return controleFluxoList;
	}

	public void setControleFluxoList(Collection<ControleFluxo> controleFluxoList) {
		this.controleFluxoList = controleFluxoList;
	}

	public Collection<DocumentoObrigatorioRequerimento> getDocumentoObrigatorioRequerimentoList() {
		return documentoObrigatorioRequerimentoList;
	}

	public void setDocumentoObrigatorioRequerimentoList(Collection<DocumentoObrigatorioRequerimento> documentoObrigatorioRequerimentoList) {
		this.documentoObrigatorioRequerimentoList = documentoObrigatorioRequerimentoList;
	}

	public Collection<ArquivoProcesso> getArquivoProcessoList() {
		return arquivoProcessoList;
	}

	public void setArquivoProcessoList(
			Collection<ArquivoProcesso> arquivoProcessoList) {
		this.arquivoProcessoList = arquivoProcessoList;
	}

	public Funcionario getFuncionarioLiderEquipe() {
		return funcionarioLiderEquipe;
	}

	public void setFuncionarioLiderEquipe(Funcionario funcionarioLiderEquipe) {
		this.funcionarioLiderEquipe = funcionarioLiderEquipe;
	}

	public GrupoProcesso getGrupoProcesso() {
		return grupoProcesso;
	}

	public void setGrupoProcesso(GrupoProcesso grupoProcesso) {
		this.grupoProcesso = grupoProcesso;
	}

	public Area getArea() {
		if (Util.isNull(area)) {
			area = new Area();
			area.setNomArea(nomAreaTramitacao);
			area.setIdeArea(ideAreaTramitacao);
		}
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public StatusFluxo getStatusFluxo() {
		return statusFluxo;
	}

	public void setStatusFluxo(StatusFluxo statusFluxo) {
		this.statusFluxo = statusFluxo;
	}

	public String getNomStatus() {
		return statusFluxo.getDscStatusFluxo();
	}
	
	public Long getQtdDiasFormado() {
		return qtdDiasFormado;
	}

	public void setQtdDiasFormado(Long qtdDiasFormado) {
		this.qtdDiasFormado = qtdDiasFormado;
	}

	public String getNomPorte() {
		return nomPorte;
	}

	public void setNomPorte(String nomPorte) {
		this.nomPorte = nomPorte;
	}

	public String getNomLogradouro() {
		return nomLogradouro;
	}

	public void setNomLogradouro(String nomLogradouro) {
		this.nomLogradouro = nomLogradouro;
	}

	public Collection<Notificacao> getNotificacaoEnviadaList() {
		return notificacaoEnviadaList;
	}

	public void setNotificacaoEnviadaList(
			Collection<Notificacao> notificacaoEnviadaList) {
		this.notificacaoEnviadaList = notificacaoEnviadaList;
	}

	public Collection<Notificacao> getNotificacaoReprovadaList() {
		return notificacaoReprovadaList;
	}

	public void setNotificacaoReprovadaList(
			Collection<Notificacao> notificacaoReprovadaList) {
		this.notificacaoReprovadaList = notificacaoReprovadaList;
	}

	public Collection<NotificacaoParcial> getNotificacaoParcialList() {
		return notificacaoParcialList;
	}

	public void setNotificacaoParcialList(
			Collection<NotificacaoParcial> notificacaoParcialList) {
		this.notificacaoParcialList = notificacaoParcialList;
	}

	public Integer getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Integer ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public Integer getIdeFuncionario() {
		return ideFuncionario;
	}

	public void setIdeFuncionario(Integer ideFuncionario) {
		this.ideFuncionario = ideFuncionario;
	}

	public Integer getIdePerfil() {
		return idePerfil;
	}

	public void setIdePerfil(Integer idePerfil) {
		this.idePerfil = idePerfil;
	}

	public Integer getIdeArea() {
		return ideArea;
	}

	public void setIdeArea(Integer ideArea) {
		this.ideArea = ideArea;
	}

	public Integer getIdeAreaTramitacao() {
		return ideAreaTramitacao;
	}

	public void setIdeAreaTramitacao(Integer ideAreaTramitacao) {
		this.ideAreaTramitacao = ideAreaTramitacao;
	}

	public Integer getIdePautaTramitacao() {
		return idePautaTramitacao;
	}

	public void setIdePautaTramitacao(Integer idePautaTramitacao) {
		this.idePautaTramitacao = idePautaTramitacao;
	}


	public String getNomAreaTramitacao() {
		return nomAreaTramitacao;
	}

	public void setNomAreaTramitacao(String nomAreaTramitacao) {
		this.nomAreaTramitacao = nomAreaTramitacao;
	}

	public boolean isPodeEncaminhar() {
		return podeEncaminhar;
	}

	public void setPodeEncaminhar(boolean podeEncaminhar) {
		this.podeEncaminhar = podeEncaminhar;
	}

	public boolean isPodeNotificar() {
		return podeNotificar;
	}

	public void setPodeNotificar(boolean podeNotificar) {
		this.podeNotificar = podeNotificar;
	}

	public boolean isPodeAprovarNotificacao() {
		return podeAprovarNotificacao;
	}

	public void setPodeAprovarNotificacao(boolean podeAprovarNotificacao) {
		this.podeAprovarNotificacao = podeAprovarNotificacao;
	}

	public boolean isPodeApensarDocumento() {
		return podeApensarDocumento;
	}

	public void setPodeApensarDocumento(boolean podeApensarDocumento) {
		this.podeApensarDocumento = podeApensarDocumento;
	}

	public Integer getIdeMunicipio() {
		return ideMunicipio;
	}

	public void setIdeMunicipio(Integer ideMunicipio) {
		this.ideMunicipio = ideMunicipio;
	}

	public String getIdesAtosAmbientais() {
		return idesAtosAmbientais;
	}

	public void setIdesAtosAmbientais(String idesAtosAmbientais) {
		this.idesAtosAmbientais = idesAtosAmbientais;
	}

	public Collection<ComprovantePagamento> getComprovantePagamentoList() {
		return comprovantePagamentoList;
	}

	public void setComprovantePagamentoList(
			Collection<ComprovantePagamento> comprovantePagamentoList) {
		this.comprovantePagamentoList = comprovantePagamentoList;
	}

	public Collection<ComprovantePagamentoDae> getComprovantePagamentoDaeList() {
		return comprovantePagamentoDaeList;
	}

	public void setComprovantePagamentoDaeList(
			Collection<ComprovantePagamentoDae> comprovantePagamentoDaeList) {
		this.comprovantePagamentoDaeList = comprovantePagamentoDaeList;
	}

	public String toString() {
		return String.valueOf(this.getIdeProcesso());
	}

	public Date getDtcTramitacao() {
		return dtcTramitacao;
	}

	public void setDtcTramitacao(Date dtcTramitacao) {
		this.dtcTramitacao = dtcTramitacao;
	}

	public Integer getTipoNotificacaoEmAprovacao() {
		return tipoNotificacaoEmAprovacao;
	}

	public void setTipoNotificacaoEmAprovacao(Integer tipoNotificacaoEmAprovacao) {
		this.tipoNotificacaoEmAprovacao = tipoNotificacaoEmAprovacao;
	}

	public Collection<Tipologia> getTipologiaList() {
		return tipologiaList;
	}

	public void setTipologiaList(Collection<Tipologia> tipologiaList) {
		this.tipologiaList = tipologiaList;
	}

	public Collection<PerguntaRequerimento> getPerguntas() {
		return perguntas;
	}

	public void setPerguntas(Collection<PerguntaRequerimento> perguntas) {
		this.perguntas = perguntas;
	}

	public Boolean getIndEstadoEmergencia() {
		return indEstadoEmergencia;
	}

	public void setIndEstadoEmergencia(Boolean indEstadoEmergencia) {
		this.indEstadoEmergencia = indEstadoEmergencia;
	}

	public Collection<ArquivoProcesso> getArquivoProcessoNotificacaoList() {
		return arquivoProcessoNotificacaoList;
	}

	public void setArquivoProcessoNotificacaoList(
			Collection<ArquivoProcesso> arquivoProcessoNotificacaoList) {
		this.arquivoProcessoNotificacaoList = arquivoProcessoNotificacaoList;
	}

	public Collection<ProcessoAto> getListProcessoAto() {
		return listProcessoAto;
	}

	public void setListProcessoAto(Collection<ProcessoAto> listProcessoAto) {
		this.listProcessoAto = listProcessoAto;
	}
	public Collection<ProcessoAto> getListProcessoAtoReequadramento() {
		return listProcessoAtoReequadramento;
	}
	
	public void setListProcessoAtoReequadramento(Collection<ProcessoAto> listProcessoAtoReequadramento) {
		this.listProcessoAtoReequadramento = listProcessoAtoReequadramento;
	}

	public Boolean getIndUnidadeConservacao() {
		return indUnidadeConservacao;
	}

	public void setIndUnidadeConservacao(Boolean indUnidadeConservacao) {
		this.indUnidadeConservacao = indUnidadeConservacao;
	}

	public boolean isPodeReabrirProcesso() {
		return podeReabrirProcesso;
	}
	
	public void setPodeReabrirProcesso(boolean podeReabrirProcesso) {
		this.podeReabrirProcesso = podeReabrirProcesso;
	}

	public boolean isPodeAtualizarPassivo() {
		return podeAtualizarPassivo;
	}

	public void setPodeAtualizarPassivo(boolean podeAtualizarPassivo) {
		this.podeAtualizarPassivo = podeAtualizarPassivo;
	}

	public boolean isRenderedNotificacaoPrazo() {
		return renderedNotificacaoPrazo;
	}

	public void setRenderedNotificacaoPrazo(boolean renderedNotificacaoPrazo) {
		this.renderedNotificacaoPrazo = renderedNotificacaoPrazo;
	}

	public boolean isRenderedNotificacaoComunicacao() {
		return renderedNotificacaoComunicacao;
	}

	public void setRenderedNotificacaoComunicacao(
			boolean renderedNotificacaoComunicacao) {
		this.renderedNotificacaoComunicacao = renderedNotificacaoComunicacao;
	}

	public boolean isStatusAguardandoAprovacao() {
		return statusAguardandoAprovacao;
	}

	public void setStatusAguardandoAprovacao(boolean statusAguardandoAprovacao) {
		this.statusAguardandoAprovacao = statusAguardandoAprovacao;
	}

	public boolean isIndLiderEquipe() {
		return indLiderEquipe;
	}

	public void setIndLiderEquipe(boolean indLiderEquipe) {
		this.indLiderEquipe = indLiderEquipe;
	}

	public boolean isPodeExibirAnaliseTecnicaImovelRural() {
		return podeExibirAnaliseTecnicaImovelRural;
	}

	public void setPodeExibirAnaliseTecnicaImovelRural(
			boolean podeExibirAnaliseTecnicaImovelRural) {
		this.podeExibirAnaliseTecnicaImovelRural = podeExibirAnaliseTecnicaImovelRural;
	}

	public boolean isPodeAprovarAnaliseTecnica() {
		return podeAprovarAnaliseTecnica;
	}

	public void setPodeAprovarAnaliseTecnina(boolean podeAprovarAnaliseTecnica) {
		this.podeAprovarAnaliseTecnica = podeAprovarAnaliseTecnica;
	}

	public boolean isPodeFormarEquipe() {
		return podeFormarEquipe;
	}

	public void setPodeFormarEquipe(boolean podeFormarEquipe) {
		this.podeFormarEquipe = podeFormarEquipe;
	}

	public boolean isRenderedReservaAgua() {
		return renderedReservaAgua;
	}

	public void setRenderedReservaAgua(boolean renderedReservaAgua) {
		this.renderedReservaAgua = renderedReservaAgua;
	}

	public Lac getLac() {
		return lac;
	}

	public void setLac(Lac lac) {
		this.lac = lac;
	}

	public boolean isRenderedLacPosto() {
		return renderedLacPosto;
	}

	public void setRenderedLacPosto(boolean renderedLacPosto) {
		this.renderedLacPosto = renderedLacPosto;
	}

	public boolean isRenderedLacErb() {
		return renderedLacErb;
	}

	public void setRenderedLacErb(boolean renderedLacErb) {
		this.renderedLacErb = renderedLacErb;
	}

	public boolean isRenderedLacTransportadora() {
		return renderedLacTransportadora;
	}

	public void setRenderedLacTransportadora(boolean renderedLacTransportadora) {
		this.renderedLacTransportadora = renderedLacTransportadora;
	}

	public boolean isRenderedDqc() {
		return renderedDqc;
	}

	public void setRenderedDqc(boolean renderedDqc) {
		this.renderedDqc = renderedDqc;
	}

	public boolean isRenderedRfp() {
		return renderedRfp;
	}

	public void setRenderedRfp(boolean renderedRfp) {
		this.renderedRfp = renderedRfp;
	}

	public boolean isRenderedRcfp() {
		return renderedRcfp;
	}

	public void setRenderedRcfp(boolean renderedRcfp) {
		this.renderedRcfp = renderedRcfp;
	}

	public boolean isRenderedRlac() {
		return renderedRlac;
	}

	public void setRenderedRlac(boolean renderedRlac) {
		this.renderedRlac = renderedRlac;
	}

	public Collection<DocumentoIdentificacaoRequerimento> getDocumentoIdentificacaoRequerimentoList() {
		return documentoIdentificacaoRequerimentoList;
	}

	public void setDocumentoIdentificacaoRequerimentoList(
			Collection<DocumentoIdentificacaoRequerimento> documentoIdentificacaoRequerimentoList) {
		this.documentoIdentificacaoRequerimentoList = documentoIdentificacaoRequerimentoList;
	}

	public Collection<DocumentoRepresentacaoRequerimento> getDocumentoRepresentacaoRequerimentoList() {
		return documentoRepresentacaoRequerimentoList;
	}

	public void setDocumentoRepresentacaoRequerimentoList(
			Collection<DocumentoRepresentacaoRequerimento> documentoRepresentacaoRequerimentoList) {
		this.documentoRepresentacaoRequerimentoList = documentoRepresentacaoRequerimentoList;
	}
	public boolean isRenderedAPE() {
		return renderedAPE;
	}

	public void setRenderedAPE(boolean renderedAPE) {
		this.renderedAPE = renderedAPE;
	}

	public Collection<RequerimentoTipologia> getRequerimentoTipologiaList() {
		return requerimentoTipologiaList;
	}

	public void setRequerimentoTipologiaList(Collection<RequerimentoTipologia> requerimentoTipologiaList) {
		this.requerimentoTipologiaList = requerimentoTipologiaList;
	}

	public Collection<EmpreendimentoRequerimento> getEmpreendimentoRequerimentoList() {
		return empreendimentoRequerimentoList;
	}

	public void setEmpreendimentoRequerimentoList(Collection<EmpreendimentoRequerimento> empreendimentoRequerimentoList) {
		this.empreendimentoRequerimentoList = empreendimentoRequerimentoList;
	}

	public ProcessoReenquadramento getProcessoReenquadramento() {
		return processoReenquadramento;
	}

	public void setProcessoReenquadramento(
			ProcessoReenquadramento processoReenquadramento) {
		this.processoReenquadramento = processoReenquadramento;
	}

	public Collection<DocumentoObrigatorioReenquadramento> getDocumentoObrigatorioReenquadramentoList() {
		return documentoObrigatorioReenquadramentoList;
	}

	public void setDocumentoObrigatorioReenquadramentoList(
			Collection<DocumentoObrigatorioReenquadramento> documentoObrigatorioReenquadramentoList) {
		this.documentoObrigatorioReenquadramentoList = documentoObrigatorioReenquadramentoList;
	}

	public Collection<ProcessoReenquadramentoHistAto> getProcessoReenquadramentoHistAtoList() {
		return processoReenquadramentoHistAtoList;
	}

	public void setProcessoReenquadramentoHistAtoList(
			Collection<ProcessoReenquadramentoHistAto> processoReenquadramentoHistAtoList) {
		this.processoReenquadramentoHistAtoList = processoReenquadramentoHistAtoList;
	}

	public Collection<DocumentoRequerimentoEmpreendimento> getDocumentoRequerimentoEmpreendimentoList() {
		return documentoRequerimentoEmpreendimentoList;
	}

	public void setDocumentoRequerimentoEmpreendimentoList(
			Collection<DocumentoRequerimentoEmpreendimento> documentoRequerimentoEmpreendimentoList) {
		this.documentoRequerimentoEmpreendimentoList = documentoRequerimentoEmpreendimentoList;
	}

	public Collection<ComprovantePagamento> getComprovantePagamentoComplementarList() {
		return comprovantePagamentoComplementarList;
	}

	public void setComprovantePagamentoComplementarList(
			Collection<ComprovantePagamento> comprovantePagamentoComplementarList) {
		this.comprovantePagamentoComplementarList = comprovantePagamentoComplementarList;
	}

	public String getDesEmailRequerimento() {
		return desEmailRequerimento;
	}

	public void setDesEmailRequerimento(String desEmailRequerimento) {
		this.desEmailRequerimento = desEmailRequerimento;
	}
	
}