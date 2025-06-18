package br.gov.ba.seia.controller.abstracts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * Abstração
 *
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 16/04/2015
 */
public abstract class BaseFceOutorgaAquiculturaTanqueRedeController extends BaseFceOutorgaAquiculturaController {

	private static final boolean BARRAGEM = true;
	private static final boolean RIO = false;

	protected abstract void separarIntervencao();

	/**
	 * Carregar aba
	 */
	@Override
	public void carregarAba() {
		super.listarAquiculturaTipoAtividade();
	}

	/**
	 * Carregar dados requerimento
	 */
	@Override
	public void carregarDadosRequerimento() {
		listarOutorgaLocalizacaoGeograficaFromRequerimento();
		super.listarTipologiaDoRequerimento();
	}

	/**
	 * Listar outorga localização geografica no requerimento
	 */
	protected void listarOutorgaLocalizacaoGeograficaFromRequerimento() {
		try {
			super.listaOutorgaLocalizacaoGeografica = super.fceOutorgaServiceFacade
					.listarOutorgaLocalizacaoGeograficaTipoIntervencaoAquicultura(super.requerimento);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(
					Util.getString("msg_generica_erro_ao_carregar") + " a lista de Outorga Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * 
	 * @param aquiculturaEnum
	 * @return
	 * @throws Exception
	 */
	protected FceAquicultura buscarFceAquiculturaTanqueRedeBy(TipoAquiculturaEnum aquiculturaEnum) throws Exception {
		return super.aquiculturaServiceFacade.buscarFceAquiculturaRequerenteBy(super.requerimento, aquiculturaEnum);
	}

	/**
	 * 
	 * @param aquiculturaEnum
	 * @return
	 */
	protected boolean existeFceAquicultura(TipoAquiculturaEnum aquiculturaEnum) {
		return !Util.isNullOuVazio(super.fce) && !Util.isNull(buscarFceAquiculturaBy(super.fce, aquiculturaEnum));
	}

	/**
	 * listar
	 * 
	 * @return barragem
	 */
	protected List<OutorgaLocalizacaoGeografica> separarIntervencaoToBarragem() {
		return separarIntervencao(BARRAGEM);
	}

	/**
	 * 
	 * @return RIO
	 */
	protected List<OutorgaLocalizacaoGeografica> separarIntervencaoToRio() {
		return separarIntervencao(RIO);
	}

	/**
	 * 
	 * @param tipoIntervencaoSolicitado
	 * @return lista outorga localização geografica
	 */
	private List<OutorgaLocalizacaoGeografica> separarIntervencao(boolean tipoIntervencaoSolicitado) {
		List<OutorgaLocalizacaoGeografica> listaOutorgaLocGeo = new ArrayList<OutorgaLocalizacaoGeografica>();
		for (OutorgaLocalizacaoGeografica olg : super.listaOutorgaLocalizacaoGeografica) {
			try {
				Boolean resposta = super.aquiculturaServiceFacade
						.isIntervencaoBarragem(super.requerimento.getIdeRequerimento(), olg);
				if (!Util.isNull(resposta) && (tipoIntervencaoSolicitado == resposta)) {
					listaOutorgaLocGeo.add(olg);
				}
			} catch (Exception e) {
				JsfUtil.addErrorMessage(
						Util.getString("msg_generica_erro_ao_carregar") + " a pergunta do requerimento.");
				Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
		return listaOutorgaLocGeo;
	}

	/**
	 * Validar aba aquicultura
	 * @param fceAquicultura
	 * @return
	 */
	public boolean validarAba(FceAquicultura fceAquicultura) {
		boolean valido = true;
		if (!super.validarListaFceOutorgaLocalizacaoGeografica("poligonal")) {
			valido = false;
		}
		if (!super.validarTipoAtividade()) {
			valido = false;
		}
		if (!super.validarDadosSobreCultivo(fceAquicultura)) {
			valido = false;
		}
		return valido;
	}
	/**
	 * exluir lista fce aquicultura especeis
	 * @param fceAquicultura
	 * @throws Exception
	 */
	public void excluirListaFceAquiculturaEspeceis(FceAquicultura fceAquicultura) throws Exception {
		super.excluirListFceAquiculturaEspecie(fceAquicultura);
	}
}