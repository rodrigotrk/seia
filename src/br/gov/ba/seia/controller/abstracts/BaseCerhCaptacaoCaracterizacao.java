package br.gov.ba.seia.controller.abstracts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.CerhCaracterizacaoCaptacaoLancamentoController;
import br.gov.ba.seia.dto.BigDecimalDTO;
import br.gov.ba.seia.dto.CerhCaptacaoDadosIrrigacaoDTO;
import br.gov.ba.seia.dto.CerhCaracterizacaoDTO;
import br.gov.ba.seia.dto.TipoFinalidadeUsoAguaOrder;
import br.gov.ba.seia.entity.CerhCaptacaoAbastecimentoIndustrial;
import br.gov.ba.seia.entity.CerhCaptacaoAbastecimentoPublico;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacao;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhCaptacaoDadosIrrigacao;
import br.gov.ba.seia.entity.CerhCaptacaoDadosMineracao;
import br.gov.ba.seia.entity.CerhCaptacaoOutrosUsos;
import br.gov.ba.seia.entity.CerhCaptacaoTransposicao;
import br.gov.ba.seia.entity.CerhCaptacaoVazaoSazonalidade;
import br.gov.ba.seia.entity.CerhFinalidadeTransposicao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhOutrosUsos;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.enumerator.MesEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.CerhAbasCaptacoesFacade;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoCaptacaoLancamentoInterface;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoFinalidadeInterface;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;
import br.gov.ba.seia.interfaces.CerhVazaoSazonalidadeInterface;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemLacFceUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @since 20/04/2017
 *
 */
public abstract class BaseCerhCaptacaoCaracterizacao extends CerhCaracterizacaoCaptacaoLancamentoController {

	@EJB
	private CerhAbasCaptacoesFacade facade;

	private CerhCaptacaoDadosMineracao cerhCaptacaoDadosMineracao;

	public abstract void fecharDialogDadosMineracao();

	private CerhCaptacaoAbastecimentoIndustrial cerhCaptacaoAbastecimentoIndustrial;

	public abstract void fecharDialogDadosAbsIndustrial();

	private Collection<UnidadeMedida> unidadeMedidaCollection;

	private Collection<CerhFinalidadeTransposicao> selectCerhFinalidadeTransposicaoCollection;
	private Collection<BigDecimalDTO> proporcaoAguaPolpaCollection;

	private CerhCaptacaoDadosIrrigacaoDTO cerhCaptacaoDadosIrrigacaoDTO;

	/**
	 * Preparar CERH finalidade uso da agua
	 */
	@Override
	public void prepararCerhFinalidadeUsoAgua(CerhFinalidadeUsoAguaInterface finalidadeIf) {
		CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade = (CerhCaptacaoCaracterizacaoFinalidade) finalidadeIf;
		if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAbastecimentoIndustrial()) {
			caracterizacaoFinalidade.getCerhCaptacaoAbastecimentoIndustrialCollection().iterator();
			listarUnidadeMedida(getTipologiaEnum());
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAbastecimentoPublico()) {
			super.listarTipoPrestadorServico();
			Integer tipoPerda = 0;
			if (!Util.isNull(
					caracterizacaoFinalidade.getIdeCerhCaptacaoAbastecimentoPublico().getIndIncertoDesconhecido())
					&& caracterizacaoFinalidade.getIdeCerhCaptacaoAbastecimentoPublico().getIndIncertoDesconhecido()) {
				tipoPerda = 3;
			} else if (!Util.isNull(
					caracterizacaoFinalidade.getIdeCerhCaptacaoAbastecimentoPublico().getIndPerdaDistribuicao())) {
				if (!caracterizacaoFinalidade.getIdeCerhCaptacaoAbastecimentoPublico().getIndPerdaDistribuicao()) {
					tipoPerda = 2;
				} else if (caracterizacaoFinalidade.getIdeCerhCaptacaoAbastecimentoPublico()
						.getIndPerdaDistribuicao()) {
					tipoPerda = 1;
				}
			}
			caracterizacaoFinalidade.getIdeCerhCaptacaoAbastecimentoPublico().setTipoPerda(tipoPerda);
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeIrrigacao()) {
			cerhCaptacaoDadosIrrigacaoDTO = new CerhCaptacaoDadosIrrigacaoDTO();
			listarCulturaIrrigada();
			listarMetodoIrrigacao();
			for (CerhCaptacaoDadosIrrigacao dadosIrrigacao : caracterizacaoFinalidade
					.getCerhCaptacaoDadosIrrigacaoCollection()) {
				dadosIrrigacao.setConfirmado(true);
			}

			listarCulturaIrrigada();
			listarMetodoIrrigacao();
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos()) {
			listarCerhOutrosUsos();
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracao()) {
			caracterizacaoFinalidade.getCerhCaptacaoDadosMineracaoCollection().iterator();
			listarUnidadeMedida(getTipologiaEnum());
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracaoAreia()) {
			montarListaProporcaoAguaPolpa();
		}

	}

	/**
	 * Obter CERH finalidade
	 */
	@Override
	public void obterCerhFinalidade(CerhFinalidadeUsoAguaInterface finalidadeUsoAguaInterface) throws Exception {
		CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade = (CerhCaptacaoCaracterizacaoFinalidade) finalidadeUsoAguaInterface;
		if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAbastecimentoIndustrial()) {
			caracterizacaoFinalidade.setCerhCaptacaoAbastecimentoIndustrialCollection(
					getFacade().listarCaptacaoAbsIndustrial(caracterizacaoFinalidade));
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAbastecimentoPublico()) {
			caracterizacaoFinalidade.setIdeCerhCaptacaoAbastecimentoPublico(
					getFacade().listarCaptacaoAbsPublico(caracterizacaoFinalidade));
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeIrrigacao()) {
			caracterizacaoFinalidade.setCerhCaptacaoDadosIrrigacaoCollection(
					getFacade().listarCaptacaoIrrigacao(caracterizacaoFinalidade));
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracao()) {
			caracterizacaoFinalidade.setCerhCaptacaoDadosMineracaoCollection(
					getFacade().listarCaptacaoMineracao(caracterizacaoFinalidade));
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos()) {
			caracterizacaoFinalidade
					.setIdeCerhCaptacaoOutrosUso(getFacade().listarCaptacaoOutrosUsos(caracterizacaoFinalidade));
		}
	}

	/**
	 * Limpar Associativas
	 */
	@Override
	public void limparAssociativas() {
		Collection<CerhCaptacaoCaracterizacaoFinalidade> banco = getFacade()
				.listarCerhCaptacaoCaracterizacaoFinalidade(getCerhCaracterizacao().getIdeCerhCaptacaoCaracterizacao());
		Collection<CerhCaptacaoCaracterizacaoFinalidade> memoria = getCerhCaracterizacao()
				.getCerhCaptacaoCaracterizacaoFinalidadeCollection();

		banco.removeAll(memoria);

		for (CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade : getFacade()
				.listarCerhCaptacaoCaracterizacaoFinalidade(
						getCerhCaracterizacao().getIdeCerhCaptacaoCaracterizacao())) {
			if (!getCerhCaracterizacao().getCerhCaptacaoCaracterizacaoFinalidadeCollection()
					.contains(cerhCaptacaoCaracterizacaoFinalidade)) {
				getFacade().excluirCaracterizacaoFinalidade(carregarFinalidade(cerhCaptacaoCaracterizacaoFinalidade));
			}

			getFacade().excluirCaracterizacaoFinalidade(cerhCaptacaoCaracterizacaoFinalidade);
		}

		for (CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade : getCerhCaracterizacao()
				.getCerhCaptacaoCaracterizacaoFinalidadeCollection()) {
			if (cerhCaptacaoCaracterizacaoFinalidade.getCerhCaptacaoDadosIrrigacaoCollection() != null) {
				for (CerhCaptacaoDadosIrrigacao dadosIrrigacao : cerhCaptacaoCaracterizacaoFinalidade
						.getCerhCaptacaoDadosIrrigacaoCollection()) {
					dadosIrrigacao.setIdeCerhCaptacaoCaracterizacaoFinalidade(cerhCaptacaoCaracterizacaoFinalidade);
				}
			}
		}

	}

	/**
	 * 
	 * @param finalidade
	 * @return
	 */
	private CerhCaptacaoCaracterizacaoFinalidade carregarFinalidade(CerhCaptacaoCaracterizacaoFinalidade finalidade) {
		return getFacade().carregarFinalidade(finalidade);
	}

	public boolean validarDialog() {
		boolean valido = true;

		if (!super.validarFinalidades()) {
			valido = false;
		}
		if (!super.validarVazaoSazonalidade()) {
			valido = false;
		}
		return valido;
	}

	/**
	 * Lista Cultura Irrigada
	 */
	public void listarCulturaIrrigada() {
		try {
			if (Util.isNullOuVazio(cerhCaptacaoDadosIrrigacaoDTO.getListaTipologiaAtividade())) {
				cerhCaptacaoDadosIrrigacaoDTO
						.setListaTipologiaAtividade(getFacade().listarTipologiaAtividadeIrrigacao());
				cerhCaptacaoDadosIrrigacaoDTO.clonarListaTipoAtividade();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de cultura irrigada.");
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Lista Metodo de Irrigação
	 */
	public void listarMetodoIrrigacao() {
		try {
			if (Util.isNullOuVazio(cerhCaptacaoDadosIrrigacaoDTO.getMetodoIrrigacaoCollection())) {
				cerhCaptacaoDadosIrrigacaoDTO.setMetodoIrrigacaoCollection(getFacade().listarMetodoIrrigacao());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(
					Util.getString("msg_generica_erro_ao_carregar") + " a lista de métodos de irrigação.");
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Lista Unidade de Medida p tipologia
	 * 
	 * @param tipologiaEnum
	 */
	public void listarUnidadeMedida(TipologiaEnum tipologiaEnum) {
		try {
			if (Util.isNullOuVazio(unidadeMedidaCollection)) {
				unidadeMedidaCollection = getFacade().listarUnidadeMedida(tipologiaEnum);
			}
		} catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar") + " a lista de "
					+ Util.getString("geral_lbl_unidade") + "s.");
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * 
	 * @return se existe outro tipo de cultura irrigada
	 */
	protected boolean existeOutraCulturaIrrigada() {
		if (!Util.isNullOuVazio(getCerhFinalidadeIrrigacao())
				&& !Util.isNullOuVazio(getCerhFinalidadeIrrigacao().getCerhCaptacaoDadosIrrigacaoCollection())) {
			for (CerhCaptacaoDadosIrrigacao dadosIrrigacao : getCerhFinalidadeIrrigacao()
					.getCerhCaptacaoDadosIrrigacaoCollection()) {
				if (dadosIrrigacao.getIdeTipologiaAtividade().isOutros()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void changeCerhCaptacaoCaracterizacaoFinalidadeCollection(ValueChangeEvent event) {
		Collection<TipoFinalidadeUsoAgua> collNew = (Collection<TipoFinalidadeUsoAgua>) event.getNewValue();
		Collection<TipoFinalidadeUsoAgua> collOld = (Collection<TipoFinalidadeUsoAgua>) event.getOldValue();

		if (!Util.isNullOuVazio(collNew)) {

			if (Util.isNull(getCerhCaracterizacao().getCerhCaptacaoCaracterizacaoFinalidadeCollection())) {
				getCerhCaracterizacao().setCerhCaptacaoCaracterizacaoFinalidadeCollection(
						new ArrayList<CerhCaptacaoCaracterizacaoFinalidade>());
			}

			if (!Util.isNull(collOld) && collOld.size() > collNew.size()) {

				List<TipoFinalidadeUsoAgua> removidos = new ArrayList<TipoFinalidadeUsoAgua>();

				for (TipoFinalidadeUsoAgua velho : collOld) {
					if (!collNew.contains(velho)) {
						removidos.add(velho);
					}
				}

				List<CerhCaptacaoCaracterizacaoFinalidade> lista = (List<CerhCaptacaoCaracterizacaoFinalidade>) getCerhCaracterizacao()
						.getCerhCaptacaoCaracterizacaoFinalidadeCollection();

				for (int x = lista.size(); x > 0; x--) {
					for (int y = 0; y < removidos.size(); y++) {
						if (lista.get(x - 1).getIdeTipoFinalidadeUsoAgua().getId().equals(removidos.get(y).getId())) {
							lista.remove(x - 1);
						}
					}

				}

			} else {
				for (TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua : collNew) {
					CerhCaptacaoCaracterizacaoFinalidade cerhFinalidade = new CerhCaptacaoCaracterizacaoFinalidade(
							getCerhCaracterizacao(), tipoFinalidadeUsoAgua);

					if (!isContem(cerhFinalidade,
							getCerhCaracterizacao().getCerhCaptacaoCaracterizacaoFinalidadeCollection())) {
						getCerhCaracterizacao().getCerhCaptacaoCaracterizacaoFinalidadeCollection().add(cerhFinalidade);
						prepararInformacaoUsoFinalidade(cerhFinalidade);
					}
				}
			}
			getCerhCaracterizacao().setDscObservacao(getObservacao());
		}
		/*
		 * else {
		 * getCerhCaracterizacao().setCerhCaptacaoCaracterizacaoFinalidadeCollection(
		 * null); }
		 */
	}

	/**
	 * 
	 * @param finalidade
	 * @param finalidades
	 * @return
	 */
	private boolean isContem(CerhCaptacaoCaracterizacaoFinalidade finalidade,
			Collection<CerhCaptacaoCaracterizacaoFinalidade> finalidades) {
		for (CerhCaptacaoCaracterizacaoFinalidade f : finalidades) {
			if (f.getIdeTipoFinalidadeUsoAgua().getId().equals(finalidade.getIdeTipoFinalidadeUsoAgua().getId())) {
				return true;
			}
		}

		return false;
	}
	/**
	 * 
	 * @param caracterizacaoFinalidade
	 */
	public void prepararInformacaoUsoFinalidade(CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade) {
		if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAbastecimentoIndustrial()) {
			getCerhFinalidadeAbastecimentoIndustrial().setCerhCaptacaoAbastecimentoIndustrialCollection(
					new ArrayList<CerhCaptacaoAbastecimentoIndustrial>());
			listarUnidadeMedida(getTipologiaEnum());
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAbastecimentoPublico()) {
			getCerhFinalidadeAbastecimentoPublico().setIdeCerhCaptacaoAbastecimentoPublico(
					new CerhCaptacaoAbastecimentoPublico(getCerhFinalidadeAbastecimentoPublico()));
			super.listarTipoPrestadorServico();
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeIrrigacao()) {
			getCerhFinalidadeIrrigacao()
					.setCerhCaptacaoDadosIrrigacaoCollection(new ArrayList<CerhCaptacaoDadosIrrigacao>());
			cerhCaptacaoDadosIrrigacaoDTO = new CerhCaptacaoDadosIrrigacaoDTO();
			listarCulturaIrrigada();
			listarMetodoIrrigacao();
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracao()) {
			getCerhFinalidadeMineracao()
					.setCerhCaptacaoDadosMineracaoCollection(new ArrayList<CerhCaptacaoDadosMineracao>());
			listarUnidadeMedida(getTipologiaEnum());
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos()) {
			getCerhFinalidadeOutrosUsos()
					.setIdeCerhCaptacaoOutrosUso(new CerhCaptacaoOutrosUsos(getCerhFinalidadeOutrosUsos()));
			listarCerhOutrosUsos();
		} else if (caracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracaoAreia()) {
			montarListaProporcaoAguaPolpa();
		}
	}
	/**
	 * 
	 */
	private void montarListaProporcaoAguaPolpa() {
		if (Util.isNull(proporcaoAguaPolpaCollection)) {
			proporcaoAguaPolpaCollection = new ArrayList<BigDecimalDTO>();
			proporcaoAguaPolpaCollection.add(new BigDecimalDTO("1", BigDecimal.valueOf(1.0).setScale(1)));
			proporcaoAguaPolpaCollection.add(new BigDecimalDTO("1,5", BigDecimal.valueOf(1.5).setScale(1)));
			proporcaoAguaPolpaCollection.add(new BigDecimalDTO("2", BigDecimal.valueOf(2.0).setScale(1)));
			proporcaoAguaPolpaCollection.add(new BigDecimalDTO("2,5", BigDecimal.valueOf(2.5).setScale(1)));
			proporcaoAguaPolpaCollection.add(new BigDecimalDTO("3", BigDecimal.valueOf(3.0).setScale(1)));
			proporcaoAguaPolpaCollection.add(new BigDecimalDTO("3,5", BigDecimal.valueOf(3.5).setScale(1)));
			proporcaoAguaPolpaCollection.add(new BigDecimalDTO("4", BigDecimal.valueOf(4.0).setScale(1)));
		}
	}

	/*
	 * XXX - FINALIDADE IRRIGAÇÃO
	 */
	public void adicionarCultura(ActionEvent event) {
		TipologiaAtividade tipologiaAtividade = (TipologiaAtividade) event.getComponent().getAttributes()
				.get("culturaIrrigada");
		if (Util.isNullOuVazio(getCerhFinalidadeIrrigacao().getCerhCaptacaoDadosIrrigacaoCollection())) {
			getCerhFinalidadeIrrigacao()
					.setCerhCaptacaoDadosIrrigacaoCollection(new ArrayList<CerhCaptacaoDadosIrrigacao>());
		}
		getCerhFinalidadeIrrigacao().getCerhCaptacaoDadosIrrigacaoCollection()
				.add(new CerhCaptacaoDadosIrrigacao(tipologiaAtividade, getCerhFinalidadeIrrigacao()));
		if (tipologiaAtividade.isOutros()) {
			JsfUtil.addWarnMessage(Util.getString("fce_pulverizacao_outros"));
		}
	}
	
	/**
	 * 
	 * @param event
	 */
	public void confirmarCultura(ActionEvent event) {
		CerhCaptacaoDadosIrrigacao dadosIrrigacao = (CerhCaptacaoDadosIrrigacao) event.getComponent().getAttributes()
				.get("cerhDadoIrrigacao");
		if (validarCultura(dadosIrrigacao)) {
			dadosIrrigacao.setConfirmado(true);
		}
	}
	/**
	 * 
	 * @param dadosIrrigacao
	 * @return
	 */
	private boolean validarCultura(CerhCaptacaoDadosIrrigacao dadosIrrigacao) {
		boolean valido = true;
		if (Util.isNullOuVazio(dadosIrrigacao.getIdeMetodoIrrigacao())) {
			MensagemUtil.msg0003("O método de irrigação");
			valido = false;
		}
		if (Util.isNullOuVazio(dadosIrrigacao.getValAreaIrrigada())) {
			MensagemUtil.msg0003("A área irrigada");
			valido = false;
		}
		return valido;
	}
	/**
	 * 
	 * @param event
	 */
	public void editarCultura(ActionEvent event) {
		CerhCaptacaoDadosIrrigacao dadosIrrigacao = (CerhCaptacaoDadosIrrigacao) event.getComponent().getAttributes()
				.get("cerhDadoIrrigacao");
		dadosIrrigacao.setConfirmado(false);
	}
	/**
	 * Excluir Cultura
	 */
	public void excluirCultura() {
		try {
			CerhCaptacaoDadosIrrigacao dadosIrrigacao = cerhCaptacaoDadosIrrigacaoDTO.getCerhCaptacaoDadosIrrigacao();
			super.excluirDadosFinalidade(dadosIrrigacao);
			getCerhFinalidadeIrrigacao().getCerhCaptacaoDadosIrrigacaoCollection().remove(dadosIrrigacao);
			MensagemUtil.sucesso("Cultura Irrigada excluída com sucesso!");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
	}
	
	/**
	 * Pesquisar Cultura
	 * @param event
	 */
	public void pesquisarCultura(AjaxBehaviorEvent event) {

		String nomeCultura = (String) event.getComponent().getAttributes().get("nomeCultura");

		cerhCaptacaoDadosIrrigacaoDTO.getListaTipologiaAtividadeFULL();

		List<TipologiaAtividade> listaTemp = new ArrayList<TipologiaAtividade>();
		listaTemp.addAll(cerhCaptacaoDadosIrrigacaoDTO.getListaTipologiaAtividadeFULL());

		cerhCaptacaoDadosIrrigacaoDTO.getListaTipologiaAtividade().clear();

		for (TipologiaAtividade temp : listaTemp) {
			if (temp.getDscTipologiaAtividade().toLowerCase().indexOf(nomeCultura.toLowerCase()) != -1) {
				cerhCaptacaoDadosIrrigacaoDTO.getListaTipologiaAtividade().add(temp);
			}
		}
		if (cerhCaptacaoDadosIrrigacaoDTO.getListaTipologiaAtividade().isEmpty() && !existeOutraCulturaIrrigada()) {
			cerhCaptacaoDadosIrrigacaoDTO.getListaTipologiaAtividade().add(new TipologiaAtividade("Outros"));
		}

		atualizarTabelaCultura(event);

	}
	/**
	 * Atualiza Tabela Cultura
	 * @param event
	 */
	private void atualizarTabelaCultura(AjaxBehaviorEvent event) {
		String id = event.getComponent().getId();
		String update = event.getComponent().getClientId().replace(id, "");
		if (getTipoUsoRecursoHidricoEnum().equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL)) {
			Html.atualizar(update.concat("dataTableCultura"));
		} else {
			Html.atualizar(update.concat("dataTableCulturaSubterranea"));
		}
	}
	/**
	 * Validar Irrigação
	 * @param cerhCaptacaoDadosIrrigacaoCollection
	 * @return
	 */
	protected boolean validarIrrigacao(Collection<CerhCaptacaoDadosIrrigacao> cerhCaptacaoDadosIrrigacaoCollection) {
		boolean valido = true;
		if (Util.isNullOuVazio(cerhCaptacaoDadosIrrigacaoCollection)) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma cultura irrigada.");
			valido = false;
		} else {
			for (CerhCaptacaoDadosIrrigacao dadosIrrigacao : cerhCaptacaoDadosIrrigacaoCollection) {
				if (!dadosIrrigacao.isConfirmado()) {
					JsfUtil.addErrorMessage(
							Util.getString("msg_generica_necessario_confirmar_inf_0040") + " a cultura irrigada.");
					valido = false;
					break;
				}
			}
		}
		return valido;
	}

	/*
	 * XXX - FINALIDADE ABASTECIMENTO INDUSTRIAL
	 */
	public void prepararDadosAbastecimentoIndustrial() {
		cerhCaptacaoAbastecimentoIndustrial = new CerhCaptacaoAbastecimentoIndustrial(
				getCerhFinalidadeAbastecimentoIndustrial());
	}
	/**
	 * Incluir Dados Abastecimento Industrial
	 */
	public void incluirDadosAbastecimentoIndustrial() {
		if (validarDadoAbsIndustrial(cerhCaptacaoAbastecimentoIndustrial)) {
			getCerhFinalidadeAbastecimentoIndustrial().getCerhCaptacaoAbastecimentoIndustrialCollection()
					.add(cerhCaptacaoAbastecimentoIndustrial);
			fecharDialogDadosAbsIndustrial();
			MensagemUtil.msg0001();
		}
	}
	/**
	 * Validar Dados de abastecimento Industrial
	 * @param cerhCaptacaoAbastecimentoIndustrial
	 * @return
	 */
	private boolean validarDadoAbsIndustrial(CerhCaptacaoAbastecimentoIndustrial cerhCaptacaoAbastecimentoIndustrial) {
		boolean valido = true;
		if (super.isUsuarioExterno()) {
			if (Util.isNullOuVazio(cerhCaptacaoAbastecimentoIndustrial.getNomProduto())) {
				MensagemUtil.msg0003("O " + Util.getString("geral_lbl_produto"));
				valido = false;
			}
			if (Util.isNullOuVazio(cerhCaptacaoAbastecimentoIndustrial.getValConsumoAgua())) {
				MensagemUtil.msg0003("O " + Util.getString("cerh_finalidade_dados_consumo_agua"));
				valido = false;
			}
			if (Util.isNullOuVazio(cerhCaptacaoAbastecimentoIndustrial.getValProducaoDiaria())) {
				MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade_dados_abs_industrial_producao_diaria"));
				valido = false;
			}
			if (Util.isNullOuVazio(cerhCaptacaoAbastecimentoIndustrial.getValProducaoAnual())) {
				MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade_dados_abs_industrial_producao_anual"));
				valido = false;
			}
			if (Util.isNullOuVazio(cerhCaptacaoAbastecimentoIndustrial.getIdeUnidadeMedida())) {
				MensagemUtil.msg0003("A " + Util.getString("geral_lbl_unidade"));
				valido = false;
			}
		} else {
			if (Util.isNullOuVazio(cerhCaptacaoAbastecimentoIndustrial.getNomProduto())
					&& Util.isNullOuVazio(cerhCaptacaoAbastecimentoIndustrial.getValConsumoAgua())
					&& Util.isNullOuVazio(cerhCaptacaoAbastecimentoIndustrial.getValProducaoDiaria())
					&& Util.isNullOuVazio(cerhCaptacaoAbastecimentoIndustrial.getValProducaoAnual())
					&& Util.isNullOuVazio(cerhCaptacaoAbastecimentoIndustrial.getIdeUnidadeMedida())) {
				JsfUtil.addErrorMessage("Favor informar ao menos um dos campos.");
				valido = false;
			}
		}
		return valido;
	}
	
	/**
	 * Excluir dados abastecimento industrial
	 */
	public void excluirDadoAbsIndustrial() {
		try {
			super.excluirDadosFinalidade(cerhCaptacaoAbastecimentoIndustrial);
			getCerhFinalidadeAbastecimentoIndustrial().getCerhCaptacaoAbastecimentoIndustrialCollection()
					.remove(cerhCaptacaoAbastecimentoIndustrial);
			MensagemUtil.erro(Util.getString("messagem_017", "Dado de abastecimento industrial"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Validar abastecimento industrial
	 * @param cerhCaptacaoAbastecimentoIndustrialCollection
	 * @return
	 */
	protected boolean validarAbastecimentoIndustrial(
			Collection<CerhCaptacaoAbastecimentoIndustrial> cerhCaptacaoAbastecimentoIndustrialCollection) {
		if (super.isUsuarioExterno() && Util.isNullOuVazio(cerhCaptacaoAbastecimentoIndustrialCollection)) {
			JsfUtil.addErrorMessage(
					Util.getString("msg_generica_selecione_um") + " um " + Util.getString("cerh_finalidade_dados_de")
							+ " " + Util.getString("cerh_finalidade_abs_industrial") + ".");
			return false;
		}
		return true;
	}

	/*
	 * XXX - FINALIDADE ABASTECIMENTO PÚBLICO
	 */
	public void changeFinalidadeAbsPublicoPerdaDistribuica(ValueChangeEvent event) {
		Integer sim = 1;
		Integer nao = 2;
		if ((Integer) event.getNewValue() == sim) {
			getCerhFinalidadeAbastecimentoPublico().getIdeCerhCaptacaoAbastecimentoPublico()
					.setIndPerdaDistribuicao(true);
			getCerhFinalidadeAbastecimentoPublico().getIdeCerhCaptacaoAbastecimentoPublico()
					.setIndIncertoDesconhecido(null);
		} else if ((Integer) event.getNewValue() == nao) {
			getCerhFinalidadeAbastecimentoPublico().getIdeCerhCaptacaoAbastecimentoPublico()
					.setIndPerdaDistribuicao(false);
			getCerhFinalidadeAbastecimentoPublico().getIdeCerhCaptacaoAbastecimentoPublico()
					.setIndIncertoDesconhecido(null);
		} else {
			getCerhFinalidadeAbastecimentoPublico().getIdeCerhCaptacaoAbastecimentoPublico()
					.setIndPerdaDistribuicao(null);
			getCerhFinalidadeAbastecimentoPublico().getIdeCerhCaptacaoAbastecimentoPublico()
					.setIndIncertoDesconhecido(true);
		}
	}
	/**
	 * Validar abastecimento publico
	 * @param ideCerhCaptacaoAbastecimentoPublico
	 * @return
	 */
	protected boolean validarAbastecimentoPublico(
			CerhCaptacaoAbastecimentoPublico ideCerhCaptacaoAbastecimentoPublico) {
		boolean valido = true;
		if (Util.isNull(ideCerhCaptacaoAbastecimentoPublico.getIdeCerhTipoPrestadorServico())) {
			MensagemUtil.msg0003("O " + Util.getString("cerh_finalidade_dados_abs_publico_prestador_servico"));
			valido = false;
		}
		if (Util.isNullOuVazio(ideCerhCaptacaoAbastecimentoPublico.getIndPerdaDistribuicao())
				&& Util.isNullOuVazio(ideCerhCaptacaoAbastecimentoPublico.getIndIncertoDesconhecido())) {
			MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade_dados_abs_publico_perda_distribuicao"));
			valido = false;
		} else if (!Util.isNullOuVazio(ideCerhCaptacaoAbastecimentoPublico.getIndPerdaDistribuicao())
				&& ideCerhCaptacaoAbastecimentoPublico.getIndPerdaDistribuicao()
				&& Util.isNullOuVazio(ideCerhCaptacaoAbastecimentoPublico.getValIndicePerdaDistribuicao())) {
			MensagemUtil.msg0003("O " + Util.getString("cerh_finalidade_dados_abs_publico_valor_indice_perda"));
			valido = false;
		} else if (!Util.isNullOuVazio(ideCerhCaptacaoAbastecimentoPublico.getValIndicePerdaDistribuicao())
				&& ideCerhCaptacaoAbastecimentoPublico.getValIndicePerdaDistribuicao() > 100) {
			JsfUtil.addErrorMessage("Não pode ser superior à 100%.");
			valido = false;
		}

		return valido;
	}

	/*
	 * XXX - FINALIDADE MINERAÇÃO
	 */
	public void prepararDadosMineracao() {
		cerhCaptacaoDadosMineracao = new CerhCaptacaoDadosMineracao(getCerhFinalidadeMineracao());
	}
	
	/**
	 * Incluir dados mineração
	 */
	public void incluirDadosMineracao() {
		if (validarDadoMineracao(cerhCaptacaoDadosMineracao)) {
			getCerhFinalidadeMineracao().getCerhCaptacaoDadosMineracaoCollection().add(cerhCaptacaoDadosMineracao);
			fecharDialogDadosMineracao();
			MensagemUtil.msg0001();
		}
	}
	/**
	 * Validar dados mineração
	 * @param cerhCaptacaoDadosMineracao
	 * @return
	 */
	private boolean validarDadoMineracao(CerhCaptacaoDadosMineracao cerhCaptacaoDadosMineracao) {
		boolean valido = true;
		if (super.isUsuarioExterno()) {
			if (Util.isNullOuVazio(cerhCaptacaoDadosMineracao.getNomProduto())) {
				MensagemUtil.msg0003("O " + Util.getString("geral_lbl_produto"));
				valido = false;
			}
			if (Util.isNullOuVazio(cerhCaptacaoDadosMineracao.getValConsumoAgua())) {
				MensagemUtil.msg0003("O " + Util.getString("cerh_finalidade_dados_consumo_agua"));
				valido = false;
			}
			if (Util.isNullOuVazio(cerhCaptacaoDadosMineracao.getValProducaoMaximaMensal())) {
				MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade_dados_mineracao_consumo_mensal"));
				valido = false;
			}
			if (Util.isNullOuVazio(cerhCaptacaoDadosMineracao.getValProducaoMaximaAnual())) {
				MensagemUtil.msg0003("A " + Util.getString("cerh_finalidade_dados_mineracao_consumo_anual"));
				valido = false;
			}
			if (Util.isNullOuVazio(cerhCaptacaoDadosMineracao.getIdeUnidadeMedida())) {
				MensagemUtil.msg0003("A " + Util.getString("geral_lbl_unidade"));
				valido = false;
			}
		} else {
			if (Util.isNullOuVazio(cerhCaptacaoDadosMineracao.getNomProduto())
					&& Util.isNullOuVazio(cerhCaptacaoDadosMineracao.getValConsumoAgua())
					&& Util.isNullOuVazio(cerhCaptacaoDadosMineracao.getValProducaoMaximaMensal())
					&& Util.isNullOuVazio(cerhCaptacaoDadosMineracao.getValProducaoMaximaAnual())
					&& Util.isNullOuVazio(cerhCaptacaoDadosMineracao.getIdeUnidadeMedida())) {
				JsfUtil.addErrorMessage("Favor informar ao menos um dos campos.");
				valido = false;
			}
		}
		return valido;
	}
	/**
	 * Excluir dados mineração
	 */
	public void excluirDadoMineracao() {
		try {
			super.excluirDadosFinalidade(cerhCaptacaoDadosMineracao);
			getCerhFinalidadeMineracao().getCerhCaptacaoDadosMineracaoCollection().remove(cerhCaptacaoDadosMineracao);
			MensagemUtil.erro(Util.getString("messagem_017", "Dado de mineração"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Validar Mineração
	 * @param cerhCaptacaoDadosMineracaoCollection
	 * @return
	 */
	protected boolean validarMineracao(Collection<CerhCaptacaoDadosMineracao> cerhCaptacaoDadosMineracaoCollection) {
		if (super.isUsuarioExterno() && Util.isNullOuVazio(cerhCaptacaoDadosMineracaoCollection)) {
			JsfUtil.addErrorMessage(
					Util.getString("msg_generica_selecione_um") + " um " + Util.getString("cerh_finalidade_dados_de")
							+ " " + Util.getString("cerh_finalidade_mineracao") + ".");
			return false;
		}
		return true;
	}

	/*
	 * XXX - OUTROS USOS
	 */
	public void changeFinalidadeOutrosUsos(ValueChangeEvent event) {
		CerhOutrosUsos newOutrosUsos = (CerhOutrosUsos) event.getNewValue();
		CerhOutrosUsos oldOutrosUsos = (CerhOutrosUsos) event.getOldValue();

		CerhCaptacaoCaracterizacaoFinalidade cerhFinalidadeOutros = getCerhFinalidadeOutrosUsos();

		if (!Util.isNull(newOutrosUsos)) {
			if (Util.isNull(cerhFinalidadeOutros.getIdeCerhCaptacaoOutrosUso())) {
				cerhFinalidadeOutros.setIdeCerhCaptacaoOutrosUso(new CerhCaptacaoOutrosUsos(cerhFinalidadeOutros));
			}
			if (!newOutrosUsos.equals(oldOutrosUsos) && newOutrosUsos.isOutros()) {
				MensagemLacFceUtil.exibirInformacao001();
			}
		} else {
			cerhFinalidadeOutros.setIdeCerhCaptacaoOutrosUso(null);
		}
	}
	/**
	 * Validar outros usos
	 * @param ideCerhCaptacaoOutrosUso
	 * @return
	 */
	protected boolean validarOutrosUsos(CerhCaptacaoOutrosUsos ideCerhCaptacaoOutrosUso) {
		if (Util.isNull(ideCerhCaptacaoOutrosUso.getIdeCerhOutrosUsos())) {
			MensagemUtil.msg0003("Outros Usos");
			return false;
		}
		return true;
	}
	
	/**
	 * Excluir outros usos
	 */
	@Override
	public boolean existeOutros() {
		if (!Util.isNull(getCerhCaracterizacao())
				&& !Util.isNullOuVazio(getCerhCaracterizacao().getCerhCaptacaoCaracterizacaoFinalidadeCollection())) {
			for (CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade : getCerhCaracterizacao()
					.getCerhCaptacaoCaracterizacaoFinalidadeCollection()) {
				if (cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos()) {
					if (cerhCaptacaoCaracterizacaoFinalidade.getIdeCerhCaptacaoOutrosUso().getIdeCerhOutrosUsos()
							.isOutros()) {
						return true;
					}
				} else if (cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeIrrigacao()) {
					for (CerhCaptacaoDadosIrrigacao cerhCaptacaoDadosIrrigacao : cerhCaptacaoCaracterizacaoFinalidade
							.getCerhCaptacaoDadosIrrigacaoCollection()) {
						if (cerhCaptacaoDadosIrrigacao.getIdeTipologiaAtividade().isOutros()
								|| cerhCaptacaoDadosIrrigacao.getIdeMetodoIrrigacao().isOutros()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
/**
 * Validar se existe outros usos alem dos já vinculados ao processo
 */
	@Override
	public boolean isExisteOutrosUsoAlemDoProcesso() {
		if (!Util.isNullOuVazio(super.cerhDTO.getAbaDadoGerais().getListaTipoUsoRecursoHidricoCaptacaoSelecionado())) {
			if (super.cerhDTO.getAbaDadoGerais().getListaTipoUsoRecursoHidricoCaptacaoSelecionado()
					.contains(new TipoUsoRecursoHidrico(getTipoUsoRecursoHidricoEnum()))) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Validar Finalidade Selecionada
	 */
	@Override
	public boolean isFinalidadeSelecionada() {
		return !Util.isNullOuVazio(getCerhCaracterizacao().getCerhCaptacaoCaracterizacaoFinalidadeCollection());
	}
	/**
	 * Validar se a finalidade necessita de informações de uso
	 */
	@Override
	public boolean isFinalidadeNecessitaInformacoesDeUso(CerhFinalidadeUsoAguaInterface cerhCaracterizacaoFinalidade) {
		CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade = (CerhCaptacaoCaracterizacaoFinalidade) cerhCaracterizacaoFinalidade;
		return cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAbastecimentoIndustrial()
				|| cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeAbastecimentoPublico()
				|| cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeIrrigacao()
				|| cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracao()
				|| cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos();
	}
	
	/**
	 * Validar caracterização na finalidade do CERH
	 */
	@Override
	public boolean validarCerhCaraceterizacaoFinalidade() {
		boolean valido = true;
		for (CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade : getCerhCaracterizacao()
				.getCerhCaptacaoCaracterizacaoFinalidadeCollection()) {
			if (cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua()
					.isFinalidadeAbastecimentoIndustrial()) {
				if (!validarAbastecimentoIndustrial(getCerhFinalidadeAbastecimentoIndustrial()
						.getCerhCaptacaoAbastecimentoIndustrialCollection())) {
					valido = false;
				}
			} else if (cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua()
					.isFinalidadeAbastecimentoPublico()) {
				if (!validarAbastecimentoPublico(
						getCerhFinalidadeAbastecimentoPublico().getIdeCerhCaptacaoAbastecimentoPublico())) {
					valido = false;
				}
			} else if (cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeIrrigacao()) {
				if (!validarIrrigacao(getCerhFinalidadeIrrigacao().getCerhCaptacaoDadosIrrigacaoCollection())) {
					valido = false;
				}
			} else if (cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeMineracao()) {
				if (!validarMineracao(getCerhFinalidadeMineracao().getCerhCaptacaoDadosMineracaoCollection())) {
					valido = false;
				}
			} else if (cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos()) {
				if (!validarOutrosUsos(getCerhFinalidadeOutrosUsos().getIdeCerhCaptacaoOutrosUso())) {
					valido = false;
				}
			}
		}
		return valido;
	}
/**
 * Finalidade Abastecimento Publico
 * @return tipo de finalidade Abastecimento público
 */
	public CerhCaptacaoCaracterizacaoFinalidade getCerhFinalidadeAbastecimentoPublico() {
		return (CerhCaptacaoCaracterizacaoFinalidade) super.getCerhFinalidade(
				TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_PUBLICO);
	}
	/**
	 * Finalidade Irrigação
	 * @return tipo de finalidade Irrigação
	 */
	public CerhCaptacaoCaracterizacaoFinalidade getCerhFinalidadeIrrigacao() {
		return (CerhCaptacaoCaracterizacaoFinalidade) super.getCerhFinalidade(TipoFinalidadeUsoAguaEnum.IRRIGACAO);
	}
	/**
	 * Finalidade Abastecimento Industrial
	 * @return tipo de finalidade Abastecimento Industrial
	 */
	public CerhCaptacaoCaracterizacaoFinalidade getCerhFinalidadeAbastecimentoIndustrial() {
		return (CerhCaptacaoCaracterizacaoFinalidade) super.getCerhFinalidade(
				TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL);
	}
	/**
	 * Finalidade Mineração
	 * @return tipo de finalidade Mineração
	 */
	public CerhCaptacaoCaracterizacaoFinalidade getCerhFinalidadeMineracao() {
		return (CerhCaptacaoCaracterizacaoFinalidade) super.getCerhFinalidade(TipoFinalidadeUsoAguaEnum.MINERACAO);
	}
	/**
	 * 
	 */
	@Override
	public CerhCaptacaoCaracterizacao getCerhCaracterizacao(CerhCaracterizacaoDTO dto) {
		return dto.getCerhLocalizacaoGeografica().getIdeCerhCaptacaoCaracterizacao();
	}
	/**
	 * 
	 */
	@Override
	public CerhCaptacaoCaracterizacaoFinalidade getCerhFinalidadeOutrosUsos() {
		return (CerhCaptacaoCaracterizacaoFinalidade) super.getCerhFinalidadeOutrosUsos();
	}
	/**
	 * 
	 */
	@Override
	public CerhCaptacaoCaracterizacao getCerhCaracterizacao() {
		if (Util.isNull(super.cerhCaracterizacao)) {
			super.cerhCaracterizacao = new CerhCaptacaoCaracterizacao(
					dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica());
			dto.getCaracterizacaoDTO().getCerhLocalizacaoGeografica()
					.setIdeCerhCaptacaoCaracterizacao((CerhCaptacaoCaracterizacao) super.cerhCaracterizacao);
		}
		return (CerhCaptacaoCaracterizacao) super.cerhCaracterizacao;
	}
	/**
	 * 
	 */
	@Override
	public CerhAbasCaptacoesFacade getFacade() {
		return facade;
	}
	/**
	 * 
	 */
	@Override
	public CerhVazaoSazonalidadeInterface getCerhVazaoSazonalidade(MesEnum mes) {
		return new CerhCaptacaoVazaoSazonalidade(getCerhCaracterizacao(), mes);
	}
	/**
	 * 
	 */
	@Override
	public CerhCaracterizacaoCaptacaoLancamentoInterface getCerhCaracterizacaoVazaoSazonalidade() {
		return getCerhCaracterizacao();
	}
	/**
	 * 
	 */
	@Override
	public CerhCaracterizacaoFinalidadeInterface getCerhCaracterizacaoFinalidade() {
		return getCerhCaracterizacao();
	}
	/**Obter Caracterização CERH
	 * 
	 */
	@Override
	public CerhCaptacaoCaracterizacao obterCaracterizacaoMontada(CerhLocalizacaoGeografica clg) throws Exception {
		CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao = null;
		if(!Util.isNullOuVazio(clg.getIdeCerhCaptacaoCaracterizacao())) {
			cerhCaptacaoCaracterizacao = clg.getIdeCerhCaptacaoCaracterizacao();
		}else {
			cerhCaptacaoCaracterizacao = getFacade().carregarCerhCaptacaoCaracterizacao(clg);
		}
		
		if (!Util.isNull(cerhCaptacaoCaracterizacao)) {
			cerhCaptacaoCaracterizacao.setIdeCerhLocalizacaoGeografica(clg);

			if (cerhCaptacaoCaracterizacao.getIdeCerhSituacaoTipoUso() != null) {
				cerhCaptacaoCaracterizacao.setIdeCerhSituacaoTipoUso(getFacade().carregarCerhSituacaoTipoUso(
						cerhCaptacaoCaracterizacao.getIdeCerhSituacaoTipoUso().getIdeCerhSituacaoTipoUso()));
			}

			cerhCaptacaoCaracterizacao
					.setCerhCaptacaoVazaoSazonalidadeCollection(getFacade().listarCerhCaptacaoVazaoSazonalidade(
							cerhCaptacaoCaracterizacao.getIdeCerhCaptacaoCaracterizacao()));
			cerhCaptacaoCaracterizacao.setCerhCaptacaoCaracterizacaoFinalidadeCollection(
					getFacade().listarCerhCaptacaoCaracterizacaoFinalidade(
							cerhCaptacaoCaracterizacao.getIdeCerhCaptacaoCaracterizacao()));

			if (!Util.isNullOuVazio(cerhCaptacaoCaracterizacao.getCerhCaptacaoCaracterizacaoFinalidadeCollection())) {
				for (CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade : cerhCaptacaoCaracterizacao
						.getCerhCaptacaoCaracterizacaoFinalidadeCollection()) {
					obterCerhFinalidade(caracterizacaoFinalidade);
				}
			}
		}

		return cerhCaptacaoCaracterizacao;

	}
	
	@Override
	public CerhCaptacaoCaracterizacao obterCaracterizacaoMontadaHistorico(CerhLocalizacaoGeografica clg) throws Exception {
		CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao = getFacade().carregarCerhCaptacaoCaracterizacao(clg);
		if (!Util.isNull(cerhCaptacaoCaracterizacao)) {
			cerhCaptacaoCaracterizacao.setIdeCerhLocalizacaoGeografica(clg);

			if (cerhCaptacaoCaracterizacao.getIdeCerhSituacaoTipoUso() != null) {
				cerhCaptacaoCaracterizacao.setIdeCerhSituacaoTipoUso(getFacade().carregarCerhSituacaoTipoUso(
						cerhCaptacaoCaracterizacao.getIdeCerhSituacaoTipoUso().getIdeCerhSituacaoTipoUso()));
			}

			cerhCaptacaoCaracterizacao
					.setCerhCaptacaoVazaoSazonalidadeCollection(getFacade().listarCerhCaptacaoVazaoSazonalidade(
							cerhCaptacaoCaracterizacao.getIdeCerhCaptacaoCaracterizacao()));
			cerhCaptacaoCaracterizacao.setCerhCaptacaoCaracterizacaoFinalidadeCollection(
					getFacade().listarCerhCaptacaoCaracterizacaoFinalidade(
							cerhCaptacaoCaracterizacao.getIdeCerhCaptacaoCaracterizacao()));

			if (!Util.isNullOuVazio(cerhCaptacaoCaracterizacao.getCerhCaptacaoCaracterizacaoFinalidadeCollection())) {
				for (CerhCaptacaoCaracterizacaoFinalidade caracterizacaoFinalidade : cerhCaptacaoCaracterizacao
						.getCerhCaptacaoCaracterizacaoFinalidadeCollection()) {
					obterCerhFinalidade(caracterizacaoFinalidade);
				}
			}
		}

		return cerhCaptacaoCaracterizacao;

	}
	/**
	 * Excluir caracterização cerh
	 */
	@Override
	public void excluirCaracterizacao(CerhCaracterizacaoInterface caracterizacaoInterface) {
		getFacade().excluirCaracterizacao((CerhCaptacaoCaracterizacao) caracterizacaoInterface);
	}
	/**
	 * Consultar caracterização cerh
	 */
	@Override
	public void prepararConsultar(CerhCaracterizacaoDTO caracterizacaoDTO) {
		caracterizacaoDTO.getCerhLocalizacaoGeografica().setIdeCerhCaptacaoCaracterizacao(
				getFacade().carregarIdeCerhCaptacaoCaracterizacao(caracterizacaoDTO.getCerhLocalizacaoGeografica()));
	}
	
	/**
	 * Gravar historico cerh
	 */
	@Override
	public void armazenarHistorico() throws Exception {

		for (CerhCaracterizacaoDTO dto : getDto().getListaCaracterizacaoDTO()) {
			CerhCaptacaoCaracterizacao copia = null;
			try {
					copia = (CerhCaptacaoCaracterizacao) dto.getCerhLocalizacaoGeografica()
							.getIdeCerhCaptacaoCaracterizacao().clone();
			} catch (CloneNotSupportedException e) {
				Log4jUtil.log(this.getClass().getSimpleName(), Level.ERROR, e);
			}
			prepararDialogIncluirCaracterizacaoHistorico(dto, true);
			CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao = (CerhCaptacaoCaracterizacao) cerhCaracterizacao;

			limparIdeCerhCaptacaoCaracterizacao(cerhCaptacaoCaracterizacao);

			CerhProcesso cerhProcessoAntigo = cerhCaptacaoCaracterizacao.getIdeCerhLocalizacaoGeografica()
					.getIdeCerhProcesso();
			if (!Util.isNull(cerhProcessoAntigo)) {

				for (CerhProcesso cerhProcessoNovo : cerhDTO.getAbaDadoGerais().getNovoCerh()
						.getCerhProcessoCollection()) {
					if (cerhProcessoAntigo.equals(cerhProcessoNovo.getCerhProcessoPai())) {

						CerhLocalizacaoGeografica cloneCerhLocalizacaoGeografica = cerhCaptacaoCaracterizacao
								.getIdeCerhLocalizacaoGeografica().clone();
						cloneCerhLocalizacaoGeografica.setIdeObjetoPai(cloneCerhLocalizacaoGeografica.getId());
						cloneCerhLocalizacaoGeografica.setIdeCerhLocalizacaoGeografica(null);
						cloneCerhLocalizacaoGeografica.setIdeCerhProcesso(cerhProcessoNovo);

						for (CerhTipoUso cerhTipoUso : cerhProcessoNovo.getCerhTipoUsoCollection()) {
							if (cerhTipoUso.getIdeCerhProcesso().getIdeCerhProcesso()
									.equals(cerhProcessoNovo.getIdeCerhProcesso())) {
								cloneCerhLocalizacaoGeografica.setIdeCerhTipoUso(cerhTipoUso);
							}
						}

						cloneCerhLocalizacaoGeografica
								.setIdeLocalizacaoGeografica(getFacade().duplicarLocalizacaoGeografica(
										cloneCerhLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
						cerhCaptacaoCaracterizacao.setIdeCerhLocalizacaoGeografica(cloneCerhLocalizacaoGeografica);
					}
				}
			} else {
				for (CerhTipoUso ctu : cerhDTO.getAbaDadoGerais().getNovoCerh().getCerhTipoUsoCollection()) {
					if (ctu.getCerhTipoUsoPai()
							.equals(cerhCaptacaoCaracterizacao.getIdeCerhLocalizacaoGeografica().getIdeCerhTipoUso())) {

						CerhLocalizacaoGeografica cloneCerhLocalizacaoGeografica = cerhCaptacaoCaracterizacao
								.getIdeCerhLocalizacaoGeografica().clone();
						cloneCerhLocalizacaoGeografica.setIdeObjetoPai(cloneCerhLocalizacaoGeografica.getId());
						cloneCerhLocalizacaoGeografica.getIdeCerhCaptacaoCaracterizacao()
								.setIdeCerhCaptacaoCaracterizacao(copia.getIdeCerhCaptacaoCaracterizacao());
						cloneCerhLocalizacaoGeografica.setIdeCerhLocalizacaoGeografica(null);
						cloneCerhLocalizacaoGeografica.setIdeCerhTipoUso(ctu);

						LocalizacaoGeografica loc = cloneCerhLocalizacaoGeografica.getIdeLocalizacaoGeografica()
								.clone();
						
						cloneCerhLocalizacaoGeografica
								.setIdeLocalizacaoGeografica(getFacade().duplicarLocalizacaoGeografica(loc));
						
						cerhCaptacaoCaracterizacao.setIdeCerhLocalizacaoGeografica(cloneCerhLocalizacaoGeografica);
					}
				}
			}
			//limparIdeCerhCaptacaoCaracterizacao(cerhCaptacaoCaracterizacao);
			salvarCaracterizacaoHistorico();
		}
	}

	private void limparIdeCerhCaptacaoCaracterizacao(CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao) {
		cerhCaptacaoCaracterizacao.setIdeObjetoPai(cerhCaptacaoCaracterizacao.getId());
		cerhCaptacaoCaracterizacao.setIdeCerhCaptacaoCaracterizacao(null);

		if (!Util.isNullOuVazio(cerhCaptacaoCaracterizacao.getCerhCaptacaoVazaoSazonalidadeCollection())) {
			for (CerhCaptacaoVazaoSazonalidade ccvs : cerhCaptacaoCaracterizacao
					.getCerhCaptacaoVazaoSazonalidadeCollection()) {
				ccvs.setIdeObjetoPai(ccvs.getId());
				ccvs.setIdeCerhCaptacaoVazaoSazonalidade(null);
				ccvs.setIdeCerhCaptacaoCaracterizacao(cerhCaptacaoCaracterizacao);
			}
		}

		if (!Util.isNullOuVazio(cerhCaptacaoCaracterizacao.getCerhCaptacaoCaracterizacaoFinalidadeCollection())) {
			for (CerhCaptacaoCaracterizacaoFinalidade cccf : cerhCaptacaoCaracterizacao
					.getCerhCaptacaoCaracterizacaoFinalidadeCollection()) {

				cccf.setIdeObjetoPai(cccf.getId());
				cccf.setIdeCerhCaptacaoCaracterizacaoFinalidade(null);
				cccf.setIdeCerhCaptacaoCaracterizacao(cerhCaptacaoCaracterizacao);

				if (!Util.isNullOuVazio(cccf.getCerhCaptacaoDadosIrrigacaoCollection())) {
					for (CerhCaptacaoDadosIrrigacao ccdi : cccf.getCerhCaptacaoDadosIrrigacaoCollection()) {
						ccdi.setIdeObjetoPai(ccdi.getId());
						ccdi.setIdeCerhCaptacaoDadosIrrigacao(null);
						ccdi.setIdeCerhCaptacaoCaracterizacaoFinalidade(cccf);
					}
				}

				if (!Util.isNullOuVazio(cccf.getCerhCaptacaoDadosMineracaoCollection())) {
					for (CerhCaptacaoDadosMineracao ccdm : cccf.getCerhCaptacaoDadosMineracaoCollection()) {
						ccdm.setIdeObjetoPai(ccdm.getId());
						ccdm.setIdeCerhCaptacaoDadosMineracao(null);
						ccdm.setIdeCerhCaptacaoCaracterizacaoFinalidade(cccf);
					}
				}

				if (!Util.isNull(cccf.getIdeCerhCaptacaoMineracaoExtracaoAreia())) {
					cccf.getIdeCerhCaptacaoMineracaoExtracaoAreia()
							.setIdeObjetoPai(cccf.getIdeCerhCaptacaoMineracaoExtracaoAreia().getId());
					cccf.getIdeCerhCaptacaoMineracaoExtracaoAreia().setIdeCerhCaptacaoMineracaoExtracaoAreia(null);
					cccf.getIdeCerhCaptacaoMineracaoExtracaoAreia()
							.setIdeCerhCaptacaoCaracterizacaoFinalidade(cccf);
				}

				if (!Util.isNull(cccf.getIdeCerhCaptacaoOutrosUso())) {
					cccf.getIdeCerhCaptacaoOutrosUso().setIdeObjetoPai(cccf.getIdeCerhCaptacaoOutrosUso().getId());
					cccf.getIdeCerhCaptacaoOutrosUso().setIdeCerhCaptacaoOutrosUsos(null);
					cccf.getIdeCerhCaptacaoOutrosUso().setIdeCerhCaptacaoCaracterizacaoFinalidade(cccf);
				}

				if (!Util.isNull(cccf.getIdeCerhCaptacaoTermoeletrica())) {
					cccf.getIdeCerhCaptacaoTermoeletrica()
							.setIdeObjetoPai(cccf.getIdeCerhCaptacaoTermoeletrica().getId());
					cccf.getIdeCerhCaptacaoTermoeletrica().setIdeCerhCaptacaoTermoeletrica(null);
					cccf.getIdeCerhCaptacaoTermoeletrica().setIdeCerhCaptacaoCaracterizacaoFinalidade(cccf);
				}

				if (!Util.isNullOuVazio(cccf.getCerhCaptacaoTransposicaoCollection())) {
					for (CerhCaptacaoTransposicao cct : cccf.getCerhCaptacaoTransposicaoCollection()) {
						cct.setIdeObjetoPai(cct.getId());
						cct.setIdeCerhCaptacaoTransposicao(null);
						cct.setIdeCerhCaptacaoCaracterizacaoFinalidade(cccf);
					}
				}

				if (!Util.isNullOuVazio(cccf.getCerhCaptacaoAbastecimentoIndustrialCollection())) {
					for (CerhCaptacaoAbastecimentoIndustrial ccai : cccf
							.getCerhCaptacaoAbastecimentoIndustrialCollection()) {
						ccai.setIdeObjetoPai(ccai.getId());
						ccai.setIdeCerhCaptacaoAbastecimentoIndustrial(null);
						ccai.setIdeCerhCaptacaoCaracterizacaoFinalidade(cccf);
					}
				}

				if (!Util.isNull(cccf.getIdeCerhCaptacaoAbastecimentoPublico())) {
					cccf.getIdeCerhCaptacaoAbastecimentoPublico().setIdeCerhCaptacaoAbastecimentoPublico(null);
					cccf.getIdeCerhCaptacaoAbastecimentoPublico().setIdeCerhCaptacaoCaracterizacaoFinalidade(cccf);
				}
			}
		}
	}

	/**
	 *
	 * RN 00293 - CERH Campo Observação
	 *
	 * O campo "Observação" ficará visível para o usuário interno e oculto para o
	 * usuário externo, sempre que a coordenada estiver vinculada a um processo com
	 * Situação da regularização "Outorgado" ou "Dispensado. Preenchimento não
	 * obrigatório.
	 *
	 * Nas telas de Caracterização da Captação Superficial e da Captação
	 * Subterrânea, o campo "Observação", caso o usuário tenha selecionado mais de
	 * uma finalidade, o campo "Observação" será preenchido de forma automática
	 * (podendo ser alterado pelo usuário) com o seguinte texto: "Junto a esta
	 * finalidade existe também [Nome das finalidades selecionadas, separados por
	 * vírgula]. Será excluído do texto a "finalidade principal", que será exportada
	 * para o CNARH40, e será definida com base na seguinte ordem de prioridade:
	 * 
	 * 1. Abastecimento público 2. Irrigação 3. Abastecimento industrial 4.
	 * Abastecimento humano 5. Dessedentação animal 6. Transposição 7. Mineração 8.
	 * Pulverização agrícola 9. Infraestrutura 10. Mineração – Extração de Areia/
	 * Cascalho em leito de rio 11. Termoelétrica 12. Aquicultura em viveiros
	 * escavados, raceway ou similares 13. Lazer e Turismo
	 *
	 * 
	 * @author eduardo.fernandes
	 * @since 02/04/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
	 * @return
	 */
	public String getObservacao() {
		String obs = "";
		if (!Util.isNullOuVazio(getCerhCaracterizacao())
				&& getCerhCaracterizacao().getCerhCaptacaoCaracterizacaoFinalidadeCollection().size() > 1) {
			List<TipoFinalidadeUsoAguaOrder> listaOrdenada = getListaFinalidadeOrdenada();
			if (listaOrdenada.size() > 1) {
				obs = "Junto a esta finalidade existe também ";
				String finalidade = "";
				for (int i = 1; i < listaOrdenada.size(); i++) {
					finalidade = finalidade
							.concat(listaOrdenada.get(i).getTipoFinalidadeUsoAgua().getNomTipoFinalidadeUsoAgua());
					if (i == listaOrdenada.size() - 1) {
						finalidade = finalidade.concat(".");
					} else {
						finalidade = finalidade.concat(", ");
					}
				}
				obs = obs.concat(finalidade);
			}
		}
		return obs;
	}
	/**
	 * Listar tipo de finalidades uso da agua ordenada
	 * @return lista
	 */
	private List<TipoFinalidadeUsoAguaOrder> getListaFinalidadeOrdenada() {
		List<TipoFinalidadeUsoAguaOrder> lista = new ArrayList<TipoFinalidadeUsoAguaOrder>();
		for (CerhFinalidadeUsoAguaInterface tipoFinalidadeUsoAgua : getCerhCaracterizacao()
				.getCerhCaptacaoCaracterizacaoFinalidadeCollection()) {
			Integer ideTipoFinalidadeUsoAgua = tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()
					.getIdeTipoFinalidadeUsoAgua();
			if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_PUBLICO.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(0, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.IRRIGACAO.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(1, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(2, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_HUMANO.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(3, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.DESSEDENTACAO_ANIMAL.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(4, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.TRANSPOSICAO.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(5, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.MINERACAO.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(5, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.PULVERIZACAO_AGRICOLA.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(6, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.INFRAESTRUTURA.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(6, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.MINERACAO_EXTRACAO_AREIA.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(7, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.TERMOELETRICA.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(8, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua
					.equals(TipoFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(9, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.LAZER_TURISMO.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(10, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			} else if (ideTipoFinalidadeUsoAgua.equals(TipoFinalidadeUsoAguaEnum.COMERCIO_SERVICOS.getId())) {
				lista.add(new TipoFinalidadeUsoAguaOrder(11, tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
			}
		}
		Collections.sort(lista);
		return lista;
	}

	public Collection<BigDecimalDTO> getProporcaoAguaPolpaCollection() {
		return proporcaoAguaPolpaCollection;
	}

	public Collection<UnidadeMedida> getUnidadeMedidaCollection() {
		return unidadeMedidaCollection;
	}

	public CerhCaptacaoDadosMineracao getCerhCaptacaoDadosMineracao() {
		return cerhCaptacaoDadosMineracao;
	}

	public void setCerhCaptacaoDadosMineracao(CerhCaptacaoDadosMineracao cerhCaptacaoDadosMineracao) {
		this.cerhCaptacaoDadosMineracao = cerhCaptacaoDadosMineracao;
	}

	public CerhCaptacaoAbastecimentoIndustrial getCerhCaptacaoAbastecimentoIndustrial() {
		return cerhCaptacaoAbastecimentoIndustrial;
	}

	public void setCerhCaptacaoAbastecimentoIndustrial(
			CerhCaptacaoAbastecimentoIndustrial cerhCaptacaoAbastecimentoIndustrial) {
		this.cerhCaptacaoAbastecimentoIndustrial = cerhCaptacaoAbastecimentoIndustrial;
	}

	public Collection<CerhFinalidadeTransposicao> getSelectCerhFinalidadeTransposicaoCollection() {
		return selectCerhFinalidadeTransposicaoCollection;
	}

	public void setSelectCerhFinalidadeTransposicaoCollection(
			Collection<CerhFinalidadeTransposicao> selectCerhFinalidadeTransposicaoCollection) {
		this.selectCerhFinalidadeTransposicaoCollection = selectCerhFinalidadeTransposicaoCollection;
	}

	public CerhCaptacaoDadosIrrigacaoDTO getCerhCaptacaoDadosIrrigacaoDTO() {
		return cerhCaptacaoDadosIrrigacaoDTO;
	}
}
