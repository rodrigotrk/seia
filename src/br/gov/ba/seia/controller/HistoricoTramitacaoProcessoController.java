package br.gov.ba.seia.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ComunicacaoProcesso;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ComunicacaoProcessoService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("historicoTramitacaoProcessoController")
@ViewScoped
public class HistoricoTramitacaoProcessoController {

	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private ComunicacaoProcessoService comunicacaoProcessoService;

	private Notificacao notificacaoFinal;
	private List<ComunicacaoProcesso> lComunicacaoProcesso;
	private ComunicacaoProcesso comunicacaoProcessoSelecionada;
	private VwConsultaProcesso vwProcesso;

	public void visualizarComunicacao(ComunicacaoProcesso comunicacaoProcesso) {
		comunicacaoProcessoSelecionada = comunicacaoProcesso;
		String replace = comunicacaoProcessoSelecionada.getDesMensagem().replace("\n", "<br />");
		comunicacaoProcessoSelecionada.setDesMensagem(replace);
	}
	
	public String getUserDestino(ControleTramitacao controleTramitacao) {
		controleTramitacao.getIdePauta().getIdeTipoPauta().getIdeTipoPauta();
		if (controleTramitacao.getIdePauta().getIdeTipoPauta()
				.getIdeTipoPauta() == TipoPautaEnum.PAUTA_AREA.getTipo()) {
			if (Util.isNullOuVazio(controleTramitacao.getIndAreaSecundaria()) || !controleTramitacao.getIndAreaSecundaria()) {
				return getNomeResponsavelArea(controleTramitacao.getIdeArea()) + "\n" + siglaArea(controleTramitacao.getIdePauta().getIdeArea());
			} else {
				return getNomeResponsavelArea(controleTramitacao.getIdePauta().getIdeArea())+ "\n"+ siglaArea(controleTramitacao.getIdePauta().getIdeArea());
			}
		} else {
			return getNomeFuncionarioPauta(controleTramitacao.getIdePauta());
		}
	}

	private String getNomeFuncionarioPauta(Pauta pauta) {
		try {
			return getNomePessoa(pauta.getIdePessoaFisica().getIdePessoaFisica());
		} catch (Exception e) {
			if (Util.isNullOuVazio(pauta.getIdePessoaFisica()))
				return " ";
			else if (Util.isNullOuVazio(pauta.getIdePessoaFisica().getIdePessoaFisica()))
				return " ";
			else
				return " Desconhecido ";
		}
	}

	private String getNomeResponsavelArea(Area area) {
		try {
			return getNomePessoa(area.getIdePessoaFisica().getIdePessoaFisica());
		} catch (Exception e) {
			if (Util.isNullOuVazio(area.getIdePessoaFisica()))
				return " ";
			else if (Util.isNullOuVazio(area.getIdePessoaFisica().getIdePessoaFisica()))
				return " ";
			else
				return " Desconhecido ";

		}
	}

	private String getNomePessoa(Integer idePessoa) {
		try {
			return pessoaFisicaService.carregarPessoaFisicaGet(idePessoa).getNomPessoa();
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);// log
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private String siglaArea(Area area) {
		String nomArea = area.getNomArea();
		if (nomArea.indexOf("-") > 0) {
			return "[" + nomArea.substring(0, nomArea.indexOf("-")) + "]";
		} else {
			return "[" + nomArea + "]";
		}
	}

	public VwConsultaProcesso getVwProcesso() {
		if (Util.isNullOuVazio(vwProcesso)) {
			vwProcesso = new VwConsultaProcesso();
		} else {
			try {
				for (ControleTramitacao ct : vwProcesso
						.getControleTramitacaoAllList()) {
					if (ct.getIdePessoaFisica() != null) {
						PessoaFisica pf = pessoaFisicaService
								.buscarPessoaFisica(ct.getIdePessoaFisica());
						ct.setIdePessoaFisica(pf);
					}
				}
			} catch (Exception e) {
			}
		}
		return vwProcesso;
	}

	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
		Exception erro = null;
		try {
			lComunicacaoProcesso = comunicacaoProcessoService.listarTodosPorProcesso(vwProcesso.getIdeProcesso());
		} catch (Exception e) {
			erro = null;
			lComunicacaoProcesso = null;
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);// log
		} finally {
			if (erro != null)
				throw Util.capturarException(erro, Util.SEIA_EXCEPTION);
		}

	}

	public ComunicacaoProcesso getComunicacaoProcessoSelecionada() {
		return comunicacaoProcessoSelecionada;
	}

	public void setComunicacaoProcessoSelecionada(
			ComunicacaoProcesso comunicacaoProcessoSelecionada) {
		this.comunicacaoProcessoSelecionada = comunicacaoProcessoSelecionada;
	}

	public Notificacao getNotificacaoFinal() {
		return notificacaoFinal;
	}

	public void setNotificacaoFinal(Notificacao notificacaoFinal) {
		this.notificacaoFinal = notificacaoFinal;
	}

	public List<ComunicacaoProcesso> getlComunicacaoProcesso() {
		return lComunicacaoProcesso;
	}

	public void setlComunicacaoProcesso(
			List<ComunicacaoProcesso> lComunicacaoProcesso) {
		this.lComunicacaoProcesso = lComunicacaoProcesso;
	}
}