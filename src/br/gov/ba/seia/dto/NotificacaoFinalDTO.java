package br.gov.ba.seia.dto;

import java.util.Collection;
import java.util.List;

import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.Classe;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.FinalidadeReenquadramento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Legislacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoMotivoNotificacao;
import br.gov.ba.seia.entity.NotificacaoParcial;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PotencialPoluicao;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ReenquadramentoPotencialPoluicao;
import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.entity.ReenquadramentoTipologia;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.FinalidadeReenquadramentoEnum;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.util.Util;

public class NotificacaoFinalDTO implements Cloneable {
	
	private Boolean usuarioLider;
	private Boolean salvandoNotificacao;
	private boolean acessoFeitoPorPermissaoConcedida;
	
	private boolean alterarAtoAutorizativo;
	private boolean incluirNovoAtoAutorizativo;
	private boolean alterarPotencialPoluidor;
	private boolean alterarClasseEmpreendimento;
	private boolean alterarTipologia;
	private boolean corrigirPorte;

	private Processo processo;
	private VwConsultaProcesso vwProcesso;
	private NotificacaoParcial notificacaoParcial;
	private Legislacao legislacao;
	private Pauta pautaCriacao;	
	private TipoNotificacaoEnum tipoNotificacaoEnum;

	private Boolean usuarioCoordenador;
	private Boolean notificacaoComArquivosApensados;
	private String observacao;
	private String email;
	private Notificacao notificacaoFinal;
	private Collection<Imovel> listaImovel;
	private Collection<Imovel> listaImovelFlorestal;
	private NotificacaoMotivoNotificacao notificacaoMotivoNotificacaoSelecionado;
	private Collection<NotificacaoMotivoNotificacao> listaNotificacaoMotivoNotificacao;
	private Collection<NotificacaoMotivoNotificacao> listaNotificacaoMotivoNotificacaoSelecionado; 
	private Collection<NotificacaoMotivoNotificacao> listaNotificacaoMotivoNotificacaoEnvioShape;
	private Collection<NotificacaoMotivoNotificacao> listaNotificacaoMotivoNotificacaoEnvioShapeSelecionado;
	private Collection<ArquivoProcesso> listaDeDocumentosDaNotificacao;
	private ArquivoProcesso documentoNotificacaoSelecionado;
	
	private Collection<Classe> listaClasseParaReenquadramento;
	private Collection<ProcessoAto> listaProcessoAtoParaReenquadramento;
	private Collection<PotencialPoluicao> listaPotencialPoluicaoParaReenquadramento;
	private Collection<RequerimentoTipologia> listaRequerimentoTipologiaParaReenquadramento;
	private Collection<EmpreendimentoRequerimento> listaEmpreendimentoRequerimentoParaReenquadramento;
	
	private ReenquadramentoProcesso reenquadramentoProcesso;
	private ReenquadramentoProcessoAto reenquadramentoProcessoAtoSelecionado;
	private Collection<ReenquadramentoProcessoAto> listaAlteracaoReenquadramentoProcessoAto;
	private Collection<ReenquadramentoProcessoAto> listaInclusaoReenquadramentoProcessoAto;
	
	private Collection<ReenquadramentoPotencialPoluicao> listaReenquadramentoPotencialPoluicao; 
	private Collection<ReenquadramentoTipologia> listaReenquadramentoTipologia; 
	
	private String mensagemFinalidadeReenquadramentoVazia;
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Notificacao getNotificacaoFinal() {
		return notificacaoFinal;
	}

	public void setNotificacaoFinal(Notificacao notificacaoFinal) {
		this.notificacaoFinal = notificacaoFinal;
	}

	public Boolean getNotificacaoComArquivosApensados() {
		return notificacaoComArquivosApensados;
	}

	public void setNotificacaoComArquivosApensados(
			Boolean notificacaoComArquivosApensados) {
		this.notificacaoComArquivosApensados = notificacaoComArquivosApensados;
	}

	public Collection<ArquivoProcesso> getListaDeDocumentosDaNotificacao() {
		return listaDeDocumentosDaNotificacao;
	}

	public void setListaDeDocumentosDaNotificacao(
			List<ArquivoProcesso> listaDeDocumentosDaNotificacao) {
		this.listaDeDocumentosDaNotificacao = listaDeDocumentosDaNotificacao;
	}

	public Boolean getUsuarioCoordenador() {
		return usuarioCoordenador;
	}

	public void setUsuarioCoordenador(Boolean usuarioCoordenador) {
		this.usuarioCoordenador = usuarioCoordenador;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getUsuarioLider() {
		return usuarioLider;
	}

	public void setUsuarioLider(Boolean usuarioLider) {
		this.usuarioLider = usuarioLider;
	}

	public Boolean getSalvandoNotificacao() {
		return salvandoNotificacao;
	}

	public void setSalvandoNotificacao(Boolean salvandoNotificacao) {
		this.salvandoNotificacao = salvandoNotificacao;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public VwConsultaProcesso getVwProcesso() {
		return vwProcesso;
	}

	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public NotificacaoParcial getNotificacaoParcial() {
		return notificacaoParcial;
	}

	public void setNotificacaoParcial(NotificacaoParcial notificacaoParcial) {
		this.notificacaoParcial = notificacaoParcial;
	}

	public Legislacao getLegislacao() {
		return legislacao;
	}

	public void setLegislacao(Legislacao legislacao) {
		this.legislacao = legislacao;
	}

	public Pauta getPautaCriacao() {
		return pautaCriacao;
	}

	public void setPautaCriacao(Pauta pautaCriacao) {
		this.pautaCriacao = pautaCriacao;
	}

	public boolean isAcessoFeitoPorPermissaoConcedida() {
		return acessoFeitoPorPermissaoConcedida;
	}

	public void setAcessoFeitoPorPermissaoConcedida(
			boolean acessoFeitoPorPermissaoConcedida) {
		this.acessoFeitoPorPermissaoConcedida = acessoFeitoPorPermissaoConcedida;
	}
		
	public ArquivoProcesso getDocumentoNotificacaoSelecionado() {
		return documentoNotificacaoSelecionado;
	}

	public void setDocumentoNotificacaoSelecionado(
			ArquivoProcesso documentoNotificacaoSelecionado) {
		this.documentoNotificacaoSelecionado = documentoNotificacaoSelecionado;
	}

	public TipoNotificacaoEnum getTipoNotificacaoEnum() {
		return tipoNotificacaoEnum;
	}

	public void setTipoNotificacaoEnum(TipoNotificacaoEnum tipoNotificacaoEnum) {
		this.tipoNotificacaoEnum = tipoNotificacaoEnum;
	}

	public Collection<Imovel> getListaImovel() {
		return listaImovel;
	}

	public void setListaImovel(Collection<Imovel> listaImovel) {
		this.listaImovel = listaImovel;
	}
	
	public Collection<NotificacaoMotivoNotificacao> getListaNotificacaoMotivoNotificacao() {
		return listaNotificacaoMotivoNotificacao;
	}

	public void setListaNotificacaoMotivoNotificacao(
			Collection<NotificacaoMotivoNotificacao> listaNotificacaoMotivoNotificacao) {
		this.listaNotificacaoMotivoNotificacao = listaNotificacaoMotivoNotificacao;
	}

	public Collection<NotificacaoMotivoNotificacao> getListaNotificacaoMotivoNotificacaoSelecionado() {
		return listaNotificacaoMotivoNotificacaoSelecionado;
	}

	public void setListaNotificacaoMotivoNotificacaoSelecionado(
			Collection<NotificacaoMotivoNotificacao> listaNotificacaoMotivoNotificacaoSelecionado) {
		this.listaNotificacaoMotivoNotificacaoSelecionado = listaNotificacaoMotivoNotificacaoSelecionado;
	}

	public Collection<NotificacaoMotivoNotificacao> getListaNotificacaoMotivoNotificacaoEnvioShape() {
		return listaNotificacaoMotivoNotificacaoEnvioShape;
	}

	public void setListaNotificacaoMotivoNotificacaoEnvioShape(
			Collection<NotificacaoMotivoNotificacao> listaNotificacaoMotivoNotificacaoEnvioShape) {
		this.listaNotificacaoMotivoNotificacaoEnvioShape = listaNotificacaoMotivoNotificacaoEnvioShape;
	}

	public Collection<NotificacaoMotivoNotificacao> getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado() {
		return listaNotificacaoMotivoNotificacaoEnvioShapeSelecionado;
	}

	public void setListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado(
			Collection<NotificacaoMotivoNotificacao> listaNotificacaoMotivoNotificacaoEnvioShapeSelecionado) {
		this.listaNotificacaoMotivoNotificacaoEnvioShapeSelecionado = listaNotificacaoMotivoNotificacaoEnvioShapeSelecionado;
	}

	public NotificacaoMotivoNotificacao getNotificacaoMotivoNotificacaoSelecionado() {
		return notificacaoMotivoNotificacaoSelecionado;
	}

	public void setNotificacaoMotivoNotificacaoSelecionado(
			NotificacaoMotivoNotificacao notificacaoMotivoNotificacaoSelecionado) {
		this.notificacaoMotivoNotificacaoSelecionado = notificacaoMotivoNotificacaoSelecionado;
	}

	public Collection<Imovel> getListaImovelFlorestal() {
		return listaImovelFlorestal;
	}

	public void setListaImovelFlorestal(Collection<Imovel> listaImovelFlorestal) {
		this.listaImovelFlorestal = listaImovelFlorestal;
	}
	
	public boolean isIncluirNovoAtoAutorizativo() {
		return incluirNovoAtoAutorizativo;
	}
	
	public void setIncluirNovoAtoAutorizativo(boolean incluirNovoAtoAutorizativo) {
		this.incluirNovoAtoAutorizativo = incluirNovoAtoAutorizativo;
	}
	
	public boolean isAlterarAtoAutorizativo() {
		return alterarAtoAutorizativo;
	}

	public void setAlterarAtoAutorizativo(boolean alterarAtoAutorizativo) {
		this.alterarAtoAutorizativo = alterarAtoAutorizativo;
	}

	public boolean isAlterarPotencialPoluidor() {
		return alterarPotencialPoluidor;
	}

	public void setAlterarPotencialPoluidor(boolean alterarPotencialPoluidor) {
		this.alterarPotencialPoluidor = alterarPotencialPoluidor;
	}

	public boolean isAlterarClasseEmpreendimento() {
		return alterarClasseEmpreendimento;
	}

	public void setAlterarClasseEmpreendimento(boolean alterarClasseEmpreendimento) {
		this.alterarClasseEmpreendimento = alterarClasseEmpreendimento;
	}

	public boolean isAlterarTipologia() {
		return alterarTipologia;
	}

	public void setAlterarTipologia(boolean alterarTipologia) {
		this.alterarTipologia = alterarTipologia;
	}

	public boolean isCorrigirPorte() {
		return corrigirPorte;
	}

	public void setCorrigirPorte(boolean corrigirPorte) {
		this.corrigirPorte = corrigirPorte;
	}
	
	public NotificacaoFinalDTO clone() throws CloneNotSupportedException {
		
		NotificacaoFinalDTO clone = (NotificacaoFinalDTO) super.clone();
		
		if(!Util.isNull(notificacaoMotivoNotificacaoSelecionado)) {
			clone.setNotificacaoMotivoNotificacaoSelecionado(getNotificacaoMotivoNotificacaoSelecionado().clone());
		}
		
		clone.setListaNotificacaoMotivoNotificacao(Util.deepCloneList(getListaNotificacaoMotivoNotificacao()));
		
		clone.setListaNotificacaoMotivoNotificacaoEnvioShape(Util.deepCloneList(getListaNotificacaoMotivoNotificacaoEnvioShape()));
		for(NotificacaoMotivoNotificacao nmn : clone.getListaNotificacaoMotivoNotificacaoEnvioShape()) {
			if(!Util.isNullOuVazio(nmn.getMotivoNotificacaoImovelCollection())) {
				nmn.setMotivoNotificacaoImovelCollection(Util.deepCloneList(nmn.getMotivoNotificacaoImovelCollection()));
			}
		}
		
		clone.setListaNotificacaoMotivoNotificacaoSelecionado(Util.deepCloneList(getListaNotificacaoMotivoNotificacaoSelecionado()));
		for(NotificacaoMotivoNotificacao nmn : clone.getListaNotificacaoMotivoNotificacaoSelecionado()) {
			if(!Util.isNullOuVazio(nmn.getMotivoNotificacaoImovelCollection())) {
				nmn.setMotivoNotificacaoImovelCollection(Util.deepCloneList(nmn.getMotivoNotificacaoImovelCollection()));
			}
		}
		
		clone.setListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado(Util.deepCloneList(getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado()));
		for(NotificacaoMotivoNotificacao nmn : clone.getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado()) {
			if(!Util.isNullOuVazio(nmn.getMotivoNotificacaoImovelCollection())) {
				nmn.setMotivoNotificacaoImovelCollection(Util.deepCloneList(nmn.getMotivoNotificacaoImovelCollection()));
			}
		}
		
		return clone;
	}
	
	public void carregarFinalidadeReenquadramento(Collection<FinalidadeReenquadramento> listaFinalidadeReenquadramento) {
		if(!Util.isNullOuVazio(listaFinalidadeReenquadramento)) {
			if(listaFinalidadeReenquadramento.contains(new FinalidadeReenquadramento(FinalidadeReenquadramentoEnum.ALTERACAO_ATOS_AUTORIZATIVOS))) {
				alterarAtoAutorizativo = true;
			}
			if(listaFinalidadeReenquadramento.contains(new FinalidadeReenquadramento(FinalidadeReenquadramentoEnum.INCLUSAO_NOVOS_ATOS_AUTORIZATIVOS))) {
				incluirNovoAtoAutorizativo = true;
			}
			if(listaFinalidadeReenquadramento.contains(new FinalidadeReenquadramento(FinalidadeReenquadramentoEnum.ALTERACAO_CLASSE_EMPREENDIMENTO))) {
				alterarClasseEmpreendimento = true;
			}
			if(listaFinalidadeReenquadramento.contains(new FinalidadeReenquadramento(FinalidadeReenquadramentoEnum.ALTERACAO_POTENCIAL_POLUIDOR_ATIVIDADE))) {
				alterarPotencialPoluidor = true;
			}
			if(listaFinalidadeReenquadramento.contains(new FinalidadeReenquadramento(FinalidadeReenquadramentoEnum.ALTERACAO_TIPOLOGIA))) {
				alterarTipologia = true;
			}
			if(listaFinalidadeReenquadramento.contains(new FinalidadeReenquadramento(FinalidadeReenquadramentoEnum.CORRECAO_PORTE_EMPREENDIMENTO))) {
				corrigirPorte = true;
			}
		}
	}

	public Collection<ProcessoAto> getListaProcessoAtoParaReenquadramento() {
		return listaProcessoAtoParaReenquadramento;
	}

	public void setListaProcessoAtoParaReenquadramento(Collection<ProcessoAto> listaProcessoAtoParaReenquadramento) {
		this.listaProcessoAtoParaReenquadramento = listaProcessoAtoParaReenquadramento;
	}

	public ReenquadramentoProcessoAto getReenquadramentoProcessoAtoSelecionado() {
		return reenquadramentoProcessoAtoSelecionado;
	}

	public void setReenquadramentoProcessoAtoSelecionado(ReenquadramentoProcessoAto reenquadramentoProcessoAtoSelecionado) {
		this.reenquadramentoProcessoAtoSelecionado = reenquadramentoProcessoAtoSelecionado;
	}

	public Collection<ReenquadramentoProcessoAto> getListaAlteracaoReenquadramentoProcessoAto() {
		return listaAlteracaoReenquadramentoProcessoAto;
	}

	public void setListaAlteracaoReenquadramentoProcessoAto(Collection<ReenquadramentoProcessoAto> listaAlteracaoReenquadramentoProcessoAto) {
		this.listaAlteracaoReenquadramentoProcessoAto = listaAlteracaoReenquadramentoProcessoAto;
	}

	public Collection<ReenquadramentoProcessoAto> getListaInclusaoReenquadramentoProcessoAto() {
		return listaInclusaoReenquadramentoProcessoAto;
	}

	public void setListaInclusaoReenquadramentoProcessoAto(Collection<ReenquadramentoProcessoAto> listaInclusaoReenquadramentoProcessoAto) {
		this.listaInclusaoReenquadramentoProcessoAto = listaInclusaoReenquadramentoProcessoAto;
	}

	public Collection<RequerimentoTipologia> getListaRequerimentoTipologiaParaReenquadramento() {
		return listaRequerimentoTipologiaParaReenquadramento;
	}

	public void setListaRequerimentoTipologiaParaReenquadramento(
			Collection<RequerimentoTipologia> listaRequerimentoTipologiaParaReenquadramento) {
		this.listaRequerimentoTipologiaParaReenquadramento = listaRequerimentoTipologiaParaReenquadramento;
	}

	public Collection<ReenquadramentoPotencialPoluicao> getListaReenquadramentoPotencialPoluicao() {
		return listaReenquadramentoPotencialPoluicao;
	}

	public void setListaReenquadramentoPotencialPoluicao(Collection<ReenquadramentoPotencialPoluicao> listaReenquadramentoPotencialPoluicao) {
		this.listaReenquadramentoPotencialPoluicao = listaReenquadramentoPotencialPoluicao;
	}

	public Collection<EmpreendimentoRequerimento> getListaEmpreendimentoRequerimentoParaReenquadramento() {
		return listaEmpreendimentoRequerimentoParaReenquadramento;
	}

	public void setListaEmpreendimentoRequerimentoParaReenquadramento(
			Collection<EmpreendimentoRequerimento> listaEmpreendimentoRequerimentoParaReenquadramento) {
		this.listaEmpreendimentoRequerimentoParaReenquadramento = listaEmpreendimentoRequerimentoParaReenquadramento;
	}

	public Collection<Classe> getListaClasseParaReenquadramento() {
		return listaClasseParaReenquadramento;
	}

	public void setListaClasseParaReenquadramento(Collection<Classe> listaClasseParaReenquadramento) {
		this.listaClasseParaReenquadramento = listaClasseParaReenquadramento;
	}

	public Collection<ReenquadramentoTipologia> getListaReenquadramentoTipologia() {
		return listaReenquadramentoTipologia;
	}

	public void setListaReenquadramentoTipologia(Collection<ReenquadramentoTipologia> listaReenquadramentoTipologia) {
		this.listaReenquadramentoTipologia = listaReenquadramentoTipologia;
	}

	public ReenquadramentoProcesso getReenquadramentoProcesso() {
		return reenquadramentoProcesso;
	}

	public void setReenquadramentoProcesso(ReenquadramentoProcesso reenquadramentoProcesso) {
		this.reenquadramentoProcesso = reenquadramentoProcesso;
	}

	public Collection<PotencialPoluicao> getListaPotencialPoluicaoParaReenquadramento() {
		return listaPotencialPoluicaoParaReenquadramento;
	}

	public void setListaPotencialPoluicaoParaReenquadramento(Collection<PotencialPoluicao> listaPotencialPoluicaoParaReenquadramento) {
		this.listaPotencialPoluicaoParaReenquadramento = listaPotencialPoluicaoParaReenquadramento;
	}

	public String getMensagemFinalidadeReenquadramentoVazia() {
		return mensagemFinalidadeReenquadramentoVazia;
	}

	public void setMensagemFinalidadeReenquadramentoVazia(
			String mensagemFinalidadeReenquadramentoVazia) {
		this.mensagemFinalidadeReenquadramentoVazia = mensagemFinalidadeReenquadramentoVazia;
	}
}