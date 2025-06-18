package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoTipologia;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.facade.AdicionarMunicipiosServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("adicionarMunicipioController")
@ViewScoped
public class AdicionarMunicipioController {

	@EJB
	private AdicionarMunicipiosServiceFacade adicionarMunicipiosServiceFacade;

	private Boolean resposta;
	private String nomMunicipio;
	private Empreendimento empreendimento;
	private LocalizacaoGeografica localizacaoGeografica;
	private Collection<Municipio> listaMunicipioEnvolvido;
	private LazyDataModel<Municipio> listaMunicipio;
	private Collection<Municipio> listaMunicipioSelecionado;

	private boolean naoSalvarEmprendimento;
	
	private String updateComponente;
	
	@PostConstruct
	private void load() {
		try{

			empreendimento = (Empreendimento) SessaoUtil.recuperarObjetoSessao("EMPREENDIMENTO_SESSAO");

			if(Util.isNullOuVazio(empreendimento)) {
				EmpreendimentoController empreendimentoController = (EmpreendimentoController) SessaoUtil.recuperarManagedBean("#{empreendimentoController}", EmpreendimentoController.class);
				empreendimento = empreendimentoController.getEmpreendimento();
			}

			if(!Util.isNull(empreendimento)) {
				listaMunicipioEnvolvido = adicionarMunicipiosServiceFacade.listarMunicipio(empreendimento.getIdeEmpreendimento());
				if(!Util.isNullOuVazio(listaMunicipioEnvolvido)) {
					resposta=true;
				}
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void onKeyUp() {
		if(nomMunicipio.length() >= 3) {
			buscarMunicipio();
			Html.atualizar("frmAdicionarMunicipio:pnlListaMunicipioSelecionado");
		}
	}

	public String getMsgConfirmarAdicaoAutomaticaMunicipio() {

		if(Util.isNullOuVazio(listaMunicipioSelecionado)) {
			return "";
		}

		StringBuilder msg = new StringBuilder();
		msg.append("<p>Caro(a) Usuário(a), além do(s) município(s) já cadastrado(s), foi identificado que o shapefile sobrepõe o(s) município(s):</p>");
		msg.append("<p><ul>");
		for(Municipio municipio : listaMunicipioSelecionado) {
			msg.append("<li> - "+municipio.getNomMunicipio()+"</li>");
		}
		msg.append("</ul></p>");
		msg.append("<p>Deseja que o sistema atualize a lista de municípios no cadastro do endereço do empreendimento?</p>");

		return msg.toString();
	}

	public void changeListener(Municipio municipio) {

		if(listaMunicipioSelecionado == null) {
			listaMunicipioSelecionado = new ArrayList<Municipio>();
		}

		if(municipio.getChecked()) {
			if(!Util.isNullOuVazio(listaMunicipioEnvolvido) && listaMunicipioEnvolvido.contains(municipio)) {
				JsfUtil.addWarnMessage("O município \"" + municipio.getNomMunicipio() + "\" já foi selecionado.");
			}
			else {
				if(!listaMunicipioSelecionado.contains(municipio)){
					listaMunicipioSelecionado.add(municipio);
				}
			}
		}
		else{
			listaMunicipioSelecionado.remove(municipio);
		}
	}


	public boolean isDisabledResposta() {
	
		boolean retorno = Util.isNullOuVazio(empreendimento);

		if(retorno) {
			load();
		} 
		else {
			
			retorno = isEmpreendimentoDeTransporteDeResiduosPerigoso();
			
			if(Util.isNullOuVazio(empreendimento)) {
				return false;
			}else if(!Util.isNullOuVazio(empreendimento.getIndBaseOperacional())) {
				if(empreendimento.getIndBaseOperacional()) {
					retorno = false;
				}else {
					return false;
				}
			}if(retorno && (Util.isNullOuVazio(empreendimento.getIndBaseOperacional()) || !empreendimento.getIndBaseOperacional())){
				retorno = true;
				this.resposta = false;
			}else{
				retorno = false;
			}
			
		}

		return retorno;
	}
	
	/**
	 * 
	 * @return <b>true</b> se houver tipologia <br> <b>false</b> se não houver tipologia 
	 * @see Melhoria #7870
	 */
	private boolean isEmpreendimentoDeTransporteDeResiduosPerigoso() {
		if(!Util.isNullOuVazio(empreendimento.getEmpreendimentoTipologiaCollection())){
			for(EmpreendimentoTipologia et : empreendimento.getEmpreendimentoTipologiaCollection()){
				if(et.getTipologiaGrupo().getIdeTipologia().isTipologiaTransportadoraResiduosPerigosos()){
					return true;
				}
			}
		}
		return false;
	}

	public boolean isRenderedAdicionarMunicipio() {
		return !Util.isNull(resposta) && resposta;
	}

	public boolean isRenderedListaMunicipioEnvolvido() {
		return !Util.isNullOuVazio(listaMunicipioEnvolvido);
	}


	public void carregarDialogAdicionarMunicipio() {
		listaMunicipio=null;
		nomMunicipio="";
		if(listaMunicipioSelecionado == null){
			listaMunicipioSelecionado = new ArrayList<Municipio>();
		}
		Html.atualizar("tabAbas:frmAdicionarMunicipio");
		Html.exibir("dlgAdicionarMunicipio");
		
	}

	public void removerDadoGeografico() {
		try{
			adicionarMunicipiosServiceFacade.removerDadoGeografico(localizacaoGeografica.getIdeLocalizacaoGeografica());
			rc().execute("dlgConfirmarAdicaoAutomaticaMunicipio.hide();");
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void salvarAutomatico() {
		try{
			removerDuplicacoes();
			if(!Util.isNullOuVazio(listaMunicipioSelecionado)) {
				adicionarMunicipiosServiceFacade.salvar(empreendimento, listaMunicipioSelecionado);
				listaMunicipioEnvolvido = adicionarMunicipiosServiceFacade.listarMunicipio(empreendimento.getIdeEmpreendimento());
				rc().execute("dlgConfirmarAdicaoAutomaticaMunicipio.hide();");
				JsfUtil.addSuccessMessage("Informações gravadas com sucesso.");
			}
			else{
				JsfUtil.addWarnMessage("Selecione um município.");

			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void salvar() {
		try{
			removerDuplicacoes();
			if(!Util.isNullOuVazio(listaMunicipioSelecionado)) {
				if(!isNaoSalvarEmprendimento()){
					adicionarMunicipiosServiceFacade.salvar(empreendimento, listaMunicipioSelecionado);
					listaMunicipioEnvolvido = adicionarMunicipiosServiceFacade.listarMunicipio(empreendimento.getIdeEmpreendimento());
				}
				rc().execute("dlgAdicionarMunicipio.hide();");
				if(!Util.isNullOuVazio(updateComponente)){
					Html.atualizar(updateComponente);
				}
				JsfUtil.addSuccessMessage("Informações gravadas com sucesso.");
			} else{
				JsfUtil.addWarnMessage("Selecione um município.");

			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void removerDuplicacoes() {
		Collection<Municipio> listaMunicipioParaRemover = new ArrayList<Municipio>();
		for(Municipio municipio : listaMunicipioSelecionado) {
			if(listaMunicipioEnvolvido.contains(municipio)) {
				listaMunicipioParaRemover.add(municipio);
			}
		}
		listaMunicipioSelecionado.removeAll(listaMunicipioParaRemover);
	}

	public void removerMunicipioGrid() {
		try {

			if (!Util.isNullOuVazio(listaMunicipioEnvolvido)) {

				for (Municipio municipio : listaMunicipioEnvolvido) {
					adicionarMunicipiosServiceFacade.remover(empreendimento,
							municipio);
				}

				if (!Util.isNullOuVazio(resposta)) {
					listaMunicipioEnvolvido.clear();
				}  
			}
			Html.atualizar("tabAbas");
		} catch (Exception e) {
			Log4jUtil.log(AceiteReenquadramentoController.class.getSimpleName(), Level.ERROR, e);
		}
	}
	
	public void remover(Municipio municipio) {

		try {
			adicionarMunicipiosServiceFacade.remover(empreendimento, municipio);
			listaMunicipioEnvolvido.remove(municipio);
			if(!Util.isNullOuVazio(listaMunicipioSelecionado)) {
				listaMunicipioSelecionado.remove(municipio);
			}
			JsfUtil.addSuccessMessage("O município removido com sucesso.");
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private RequestContext rc() {
		return RequestContext.getCurrentInstance();
	}

	public void buscarMunicipio() {

		listaMunicipio = new LazyDataModel<Municipio>() {

		private static final long serialVersionUID = -6475445635129726741L;

		@Override
		public List<Municipio> load(int first, int pageSize, String arg2, SortOrder arg3, Map<String, String> arg4) {
			try{
				Collection<Municipio> municipios = adicionarMunicipiosServiceFacade.obterMunicipiosBahiaByNomeDemanda(nomMunicipio, first, pageSize);
				List<Municipio> retorno = new ArrayList<Municipio>();

				if(!Util.isNullOuVazio(listaMunicipioSelecionado)) {
					for(Municipio m1 : listaMunicipioSelecionado) {
						if(municipios.contains(m1)) {
							municipios.remove(m1);
						}
					}
					retorno.addAll(listaMunicipioSelecionado);
				}
				retorno.addAll(municipios);

				return retorno;
			} catch(Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
			return null;
			}
		};

		try {
			Integer count = 0;
			count = adicionarMunicipiosServiceFacade.obterMunicipiosBahiaByNomeCount(nomMunicipio);
			if(!Util.isNullOuVazio(listaMunicipioSelecionado)) {
				count += listaMunicipioSelecionado.size();
			}
			listaMunicipio.setRowCount(count);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public Boolean getResposta() {
		return resposta;
	}

	public void setResposta(Boolean resposta) {
		this.resposta = resposta;
	}

	public String getNomMunicipio() {
		return nomMunicipio;
	}

	public void setNomMunicipio(String nomMunicipio) {
		this.nomMunicipio = nomMunicipio;
	}

	public Collection<Municipio> getListaMunicipioEnvolvido() {
		return listaMunicipioEnvolvido;
	}

	public void setListaMunicipioEnvolvido(
			Collection<Municipio> listaMunicipioEnvolvido) {
		this.listaMunicipioEnvolvido = listaMunicipioEnvolvido;
	}

	public Collection<Municipio> getListaMunicipioSelecionado() {
		return listaMunicipioSelecionado;
	}

	public void setListaMunicipioSelecionado(
			Collection<Municipio> listaMunicipioSelecionado) {
		this.listaMunicipioSelecionado = listaMunicipioSelecionado;
	}

	public LazyDataModel<Municipio> getListaMunicipio() {
		return listaMunicipio;
	}

	public void setListaMunicipio(LazyDataModel<Municipio> listaMunicipio) {
		this.listaMunicipio = listaMunicipio;
	}

	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}

	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}

	public boolean isNaoSalvarEmprendimento() {
		return naoSalvarEmprendimento;
	}

	public void setNaoSalvarEmprendimento(boolean naoSalvarEmprendimento) {
		this.naoSalvarEmprendimento = naoSalvarEmprendimento;
	}

	public String getUpdateComponente() {
		return updateComponente;
	}

	public void setUpdateComponente(String updateComponente) {
		this.updateComponente = updateComponente;
	}

}