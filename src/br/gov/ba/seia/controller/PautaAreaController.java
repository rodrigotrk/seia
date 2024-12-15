package br.gov.ba.seia.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.enumerator.AcaoControlePermissaoAcessoPautaEnum;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.OperacaoProcessoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("pautaAreaController")
@ViewScoped
public class PautaAreaController extends PautaCompartilhadaController{

	@EJB
	private FuncionarioService funcionarioService;

	private String paginaDialog;
	private boolean selic;
	private boolean acaoFluxoAlternativo;
	
	@Override
	protected void init() {
		super.init();
		try{
			super.listarCategoria();
			verificarPautaSelic();
			operacaoProcessoEnum = OperacaoProcessoEnum.DISTRIBUIR;
			consultarProcesso();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void verificarPautaSelic() {
		PessoaFisica pf = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
		selic = funcionarioService.validarCoordenadorSelic(pf) 
				|| Util.isNull(pauta) == false && new Area(AreaEnum.SELIC.getId()).equals(pauta.getIdeArea());
	}
	
	public void limparTela(){
		super.limpar();
	}
	
	@Override
	public void consultarProcesso() {
		try {
			if (validate()) {
				consultar();
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	/**
	 * Metodo para pegar os parametros para a consulta.
	 * @return
	 */
	@Override
	protected Map<String, Object> getParametros() {
		Map<String, Object> params = super.getParametros();
		
		List<Integer> statusValidos = retornarStatusValidos();
		params.put("statusFluxo", statusValidos);
		
		return params;
	}

	private List<Integer> retornarStatusValidos() {
		Integer[] statusValidos = new Integer[] {
			StatusFluxoEnum.FORMADO.getStatus(),
			StatusFluxoEnum.ENCAMINHADO_PARA_AREA.getStatus(),
			StatusFluxoEnum.NOTIFICACAO_NAO_RESPONDIDA.getStatus(),
			StatusFluxoEnum.NOTIFICACAO_RESPONDIDA.getStatus()
		};
		return Arrays.asList(statusValidos);
	}
	
	public boolean isDisabledCronograma() {
		
		if(selic) {
			return true;
		}
		
		if(funcionarioResponsavel) {
			return false;
		}
		
		return !temAcessoConcedidoPeloGestor(AcaoControlePermissaoAcessoPautaEnum.DEFINIR_CRONOGRAMA.getId());
	}
	
	public boolean isDisabledFormarEquipe() {

		if(selic) {
			return true;
		}
		
		if(funcionarioResponsavel) {
			return false;
		}
		
		return !temAcessoConcedidoPeloGestor(AcaoControlePermissaoAcessoPautaEnum.FORMAR_EQUIPE.getId());
	}
	
	public boolean isDisabledEncaminhar() {
		if(funcionarioResponsavel) {
			return false;
		}
		return !temAcessoConcedidoPeloGestor(AcaoControlePermissaoAcessoPautaEnum.ENCAMINHAR_PROCESSO.getId());
	}

	public boolean isAcaoFluxoAlternativo() {
		return acaoFluxoAlternativo;
	}

	public void setAcaoFluxoAlternativo(boolean acaoFluxoAlternativo) {
		this.acaoFluxoAlternativo = acaoFluxoAlternativo;
	}

	public String getPaginaDialog() {
		return paginaDialog;
	}

	public void setPaginaDialog(String paginaDialog) {
		this.paginaDialog = paginaDialog;
	}

	public boolean isSelic() {
		return selic;
	}

	public void setSelic(boolean selic) {
		this.selic = selic;
	}
}