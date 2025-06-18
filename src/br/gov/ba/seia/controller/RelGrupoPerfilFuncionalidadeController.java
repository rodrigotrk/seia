package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.entity.RelGrupoPerfilFuncionalidade;
import br.gov.ba.seia.entity.Secao;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AcaoService;
import br.gov.ba.seia.service.FuncionalidadeAcaoService;
import br.gov.ba.seia.service.FuncionalidadeService;
import br.gov.ba.seia.service.PerfilService;
import br.gov.ba.seia.service.RelGrupoPerfilFuncionalidadeService;
import br.gov.ba.seia.service.SecaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("relGrupoPerfilFuncionalidadeController")
@ViewScoped
public class RelGrupoPerfilFuncionalidadeController extends SeiaControllerAb {

	@EJB
	private RelGrupoPerfilFuncionalidadeService permissaoService;
	@EJB
	private PerfilService perfilService;
	@EJB
	SecaoService secaoService;
	@EJB
	private FuncionalidadeService funcionalidadeService;
	@EJB
	private AcaoService acaoService;
	@EJB
	private FuncionalidadeAcaoService funcionalidadeAcaoService;

	private Perfil perfil;

	private Secao secao;

	private Funcionalidade funcionalidade;

	private Acao acao;

	private RelGrupoPerfilFuncionalidade permissao;

	private DataModel<RelGrupoPerfilFuncionalidade> modelPermissoes;

	private Collection<Perfil> perfis;

	private boolean exibeComboAcao;

	private boolean exibeComboAcaoPopup;

	private List<Funcionalidade> listaFuncionalidades;

	private List<Acao> listaAcoes;

	private List<Acao> listaAcoesPopup;
	
	private boolean exibeComboFuncionalidade;

	// private DualListModel<Funcionalidade> funcionalidades; 
	// private DualListModel<Acao> acoes;
	// private UIForm formularioASerLimpoPermissao;
	// private UIForm formularioASerLimpoPermissoes;

	public RelGrupoPerfilFuncionalidadeController() {}

	@PostConstruct
	public void init() {

		this.funcionalidade = new Funcionalidade();
		this.perfil = new Perfil();
		this.acao = new Acao();
		this.secao = new Secao();
		this.exibeComboAcao = false;
		this.exibeComboFuncionalidade = false;
	}

	/**
	 * Obtem a lista do combo de funcionalidades dentro do popup.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return
	 */
	public List<Funcionalidade> carregaFuncionalidades() {

		try {
			
			exibeComboFuncionalidade = false;
			List<Funcionalidade> lista = new ArrayList<Funcionalidade>();
			lista.add(new Funcionalidade(-1, "Selecione..."));
			
			if (!Util.isNullOuVazio(permissao) && 
					!Util.isNullOuVazio(funcionalidade) && 
					!Util.isNullOuVazio(funcionalidade.getIdeSecao()) && 
					funcionalidade.getIdeSecao().getIdeSecao() != -1) {
				
				lista.addAll(funcionalidadeAcaoService.obterFuncionalidadesPorSecao(this.funcionalidade.getIdeSecao()));
				if(lista.size() > 1) exibeComboFuncionalidade = true;
				
			} else {
				
				lista.addAll(funcionalidadeAcaoService.obterFuncionalidades());
				if(lista.size() > 1) exibeComboFuncionalidade = true;
			}
			
			return lista;

		} catch (Exception e) {
			this.addMensagemErro("Não foi possível carregar a lista de funcionalidades.", e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
	}

	/**
	 * Obtem a lista do combo de acoes se o combo de funcionalidade tiver sido preenchido.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return
	 */
	public void carregaAcoes() {

		try {

			if (Util.isNullOuVazio(funcionalidade) || Util.isNullOuVazio(funcionalidade.getIdeFuncionalidade()) || funcionalidade.getIdeFuncionalidade().equals(-1)) {
				listaAcoes = null;
				exibeComboAcao = false;
				acao = null;
			} else {
				listaAcoes = funcionalidadeAcaoService.obterAcoes(funcionalidade);
				exibeComboAcao = true;
				acao = null;
			}

		} catch (Exception e) {
			this.addMensagemErro("Não foi possível carregar a lista de ações.", e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			listaAcoes = null;
			exibeComboAcao = false;
			acao = null;
		}
	}

	/**
	 * Obtem a lista do combo de acoes se o combo de funcionalidade dentro do popup tiver sido preenchido.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return
	 */
	public List<Acao> getListaAcoesPopup() {

		try {

			if (Util.isNullOuVazio(permissao) || Util.isNullOuVazio(permissao.getFuncionalidade()) || Util.isNullOuVazio(permissao.getFuncionalidade().getIdeFuncionalidade()) 
					|| permissao.getFuncionalidade().getIdeFuncionalidade().equals(-1)) {
				
				listaAcoesPopup = null;
				exibeComboAcaoPopup = false;
				permissao.setAcao(null);
			} else {
				listaAcoesPopup = funcionalidadeAcaoService.obterAcoes(permissao.getFuncionalidade());
				exibeComboAcaoPopup = true;
				permissao.setAcao(null);
			}

		} catch (Exception e) {
			this.addMensagemErro("Não foi possível carregar a lista de ações.", e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			listaAcoesPopup = null;
			exibeComboAcaoPopup = false;
			permissao.setAcao(null);
		}

		return listaAcoesPopup;
	}

	/**
	 * Metodo utilizado no botao de pesquisa.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	public void pesquisarPermissao() {

		try {
			if(funcionalidade.getIdeFuncionalidade().equals(-1)) {
				modelPermissoes = Util.castListToDataModel(permissaoService.filtrarListaPermissoes(new RelGrupoPerfilFuncionalidade(perfil, null, acao)));
			} else {
				modelPermissoes = Util.castListToDataModel(permissaoService.filtrarListaPermissoes(new RelGrupoPerfilFuncionalidade(perfil, funcionalidade, acao)));
			}
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	/**
	 * Metodo chamado no botao incluir permissao, para preparar os objetos.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	public void novaPermissao() {

		setPermissao(new RelGrupoPerfilFuncionalidade());
		getPermissao().setAcao(new Acao());
		getPermissao().setPerfil(new Perfil());
		getPermissao().setFuncionalidade(new Funcionalidade());

		// limparTelaPermissao();
	}

	/**
	 * Metodo chamado no popup de incluir uma nova permissao.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	public void salvarPermissao() {

		try {
			
			if(Util.isNullOuVazio(permissao.getFuncionalidade()) 
					|| Util.isNullOuVazio(permissao.getFuncionalidade().getIdeFuncionalidade()) 
					|| permissao.getFuncionalidade().getIdeFuncionalidade().equals(-1)) {
				
				this.addMensagemErro("O campo Funcionalidade é de preenchimento obrigatório.", StringUtils.EMPTY);
				getRequestContext().addCallbackParam("validationFailed", true);
			} else {
				try {
					permissaoService.salvarPermissao(permissao);
					this.setPermissao(new RelGrupoPerfilFuncionalidade());
					this.setModelPermissoes(null);
					JsfUtil.addSuccessMessage("Permissão cadastrada com sucesso");
				} catch (AppExceptionError e) {
					this.addMensagemErro(e.getMessage(), StringUtils.EMPTY);
					getRequestContext().addCallbackParam("validationFailed", true);
				}
			}
		} catch (Exception e) {
			this.addMensagemErro("Ocorreu um erro ao tentar salvar a permissão!", e.getMessage());
			getRequestContext().addCallbackParam("validationFailed", true);
		}
	}

	/**
	 * Metodo utilizado no dialog de exclusao acionado na grid.
	 * 
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	public void excluirPermissao() {

		try {

			permissaoService.excluirPermissao(getPermissao());
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}

		this.setPermissao(new RelGrupoPerfilFuncionalidade());
		this.setModelPermissoes(null);
	}
	
	/**
	 * Obtem a lista do combo de seção.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return
	 */
	public Collection<Secao> getColSecoes() {
		
		Collection<Secao> c = new ArrayList<Secao>();
		c.add(new Secao(-1, "Selecione..."));
		c.addAll(secaoService.filtrarListaSecoes(new Secao()));
		
		return c;
    }
	
	/*
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	/**
	 * @return the modelPermissoes
	 */
	public DataModel<RelGrupoPerfilFuncionalidade> getModelPermissoes() {

		if (Util.isNull(modelPermissoes)) {

			try {

				modelPermissoes = Util.castListToDataModel(permissaoService.filtrarListaPermissoes(new RelGrupoPerfilFuncionalidade(getPerfil(), getFuncionalidade(), getAcao())));
			} catch (Exception exception) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}

		return modelPermissoes;
	}

	/**
	 * @param modelPermissoes the modelPermissoes to set
	 */
	public void setModelPermissoes(DataModel<RelGrupoPerfilFuncionalidade> modelPermissoes) {
	
		this.modelPermissoes = modelPermissoes;
	}

	/**
	 * @return the perfis
	 */
	public Collection<Perfil> getPerfis() {

		if (Util.isNullOuVazio(this.perfis)) return perfilService.filtrarListaPerfis(new Perfil());
		else return this.perfis;
	}

	/**
	 * @param perfis the perfis to set
	 */
	public void setPerfis(Collection<Perfil> perfis) {
	
		this.perfis = perfis;
	}
	
	/**
	 * @return the perfil
	 */
	public Perfil getPerfil() {

		if (Util.isNullOuVazio(perfil)) perfil = new Perfil();
		return perfil;
	}

	/**
	 * @param perfil the perfil to set
	 */
	public void setPerfil(Perfil perfil) {
	
		this.perfil = perfil;
	}

	/**
	 * @return the funcionalidade
	 */
	public Funcionalidade getFuncionalidade() {

		if (Util.isNull(funcionalidade)) funcionalidade = new Funcionalidade();
		return funcionalidade;
	}

	/**
	 * @param funcionalidade the funcionalidade to set
	 */
	public void setFuncionalidade(Funcionalidade funcionalidade) {
	
		this.funcionalidade = funcionalidade;
	}

	/**
	 * @return the acao
	 */
	public Acao getAcao() {

		if (Util.isNull(acao)) acao = new Acao();
		return acao;
	}

	/**
	 * @param acao the acao to set
	 */
	public void setAcao(Acao acao) {
	
		this.acao = acao;
	}

	/**
	 * @return the permissao
	 */
	public RelGrupoPerfilFuncionalidade getPermissao() {

		if (Util.isNull(permissao)) permissao = new RelGrupoPerfilFuncionalidade();
		return permissao;
	}

	/**
	 * @param permissao the permissao to set
	 */
	public void setPermissao(RelGrupoPerfilFuncionalidade permissao) {
	
		this.permissao = permissao;
	}

	/**
	 * @return the secao
	 */
	public Secao getSecao() {
	
		return secao;
	}

	/**
	 * @param secao the secao to set
	 */
	public void setSecao(Secao secao) {
	
		this.secao = secao;
	}

	/**
	 * @return the exibeComboAcao
	 */
	public boolean isExibeComboAcao() {

		return exibeComboAcao;
	}

	/**
	 * @param exibeComboAcao the exibeComboAcao to set
	 */
	public void setExibeComboAcao(boolean exibeComboAcao) {

		this.exibeComboAcao = exibeComboAcao;
	}

	/**
	 * @return the listaFuncionalidades
	 */
	public List<Funcionalidade> getListaFuncionalidades() {
		
		this.listaFuncionalidades = carregaFuncionalidades();
		
		return listaFuncionalidades;
	}

	/**
	 * @param listaFuncionalidades the listaFuncionalidades to set
	 */
	public void setListaFuncionalidades(List<Funcionalidade> listaFuncionalidades) {

		this.listaFuncionalidades = listaFuncionalidades;
	}

	/**
	 * @return the listaAcoes
	 */
	public List<Acao> getListaAcoes() {

		return listaAcoes;
	}

	/**
	 * @param listaAcoes the listaAcoes to set
	 */
	public void setListaAcoes(List<Acao> listaAcoes) {

		this.listaAcoes = listaAcoes;
	}

	/**
	 * @return the exibeComboAcaoPopup
	 */
	public boolean isExibeComboAcaoPopup() {

		return exibeComboAcaoPopup;
	}

	/**
	 * @param exibeComboAcaoPopup the exibeComboAcaoPopup to set
	 */
	public void setExibeComboAcaoPopup(boolean exibeComboAcaoPopup) {

		this.exibeComboAcaoPopup = exibeComboAcaoPopup;
	}
	
	/**
	 * @param listaAcoesPopup the listaAcoesPopup to set
	 */
	public void setListaAcoesPopup(List<Acao> listaAcoesPopup) {
		
		this.listaAcoesPopup = listaAcoesPopup;
	}

	/**
	 * @return the exibeComboFuncionalidade
	 */
	public boolean isExibeComboFuncionalidade() {
	
		return exibeComboFuncionalidade;
	}

	/**
	 * @param exibeComboFuncionalidade the exibeComboFuncionalidade to set
	 */
	public void setExibeComboFuncionalidade(boolean exibeComboFuncionalidade) {
	
		this.exibeComboFuncionalidade = exibeComboFuncionalidade;
	}
}

/* --- REMOCAO DA FUNCIONALIDADE DE INCLUIR MULTIPLAS PERMISSOES NO TICKET #4932 ---


	public void novasPermissoes() {

		setPermissao(new RelGrupoPerfilFuncionalidade());
		getPermissao().setAcao(new Acao());
		getPermissao().setPerfil(new Perfil());
		getPermissao().setFuncionalidade(new Funcionalidade());

		limparTelaPermissoes();
	}

	public void limparTelaPermissao() {

		limparComponentesFormulario(formularioASerLimpoPermissao);
	}

	public void limparTelaPermissoes() {

		limparComponentesFormulario(formularioASerLimpoPermissoes);
	}

	public void salvarPermissoes() {

		try {

			if (funcionalidades.getTarget().isEmpty()) {

				funcionalidadeService.excluirFuncionalidadePorSecaoPerfil(secao, perfil);
			} else {

				Collection<RelGrupoPerfilFuncionalidade> lPermissoes = new ArrayList<RelGrupoPerfilFuncionalidade>();

				funcionalidadeService.excluirFuncionalidadePorSecaoPerfil(secao, perfil);

				for (Funcionalidade lFuncionalidade : funcionalidades.getTarget()) {

					for (Acao lAcao : acoes.getTarget()) {
						lPermissoes.add(new RelGrupoPerfilFuncionalidade(perfil, lFuncionalidade, lAcao));
					}

				}

				permissaoService.salvarPermissoes(lPermissoes);
			}
		} catch (Exception exception) {

			this.addMensagemErro("Não foi possível realizar a operação, pois já existe(m) essa(s) permissão(ões) para o(s) perfil(is) de usuário escolhido(s).", exception.getMessage());
			getRequestContext().addCallbackParam("validationFailed", true);
		}

		this.setModelPermissoes(null);
		this.setPerfis(null);
		this.setFuncionalidades(null);
		this.setAcoes(null);
	}

	public Collection<Funcionalidade> getColFuncionalidades() {

		Collection<Funcionalidade> collFuncionalidades = new ArrayList<Funcionalidade>();

		try {
			if (this.funcionalidade != null && this.funcionalidade.getIdeSecao() != null) {
				collFuncionalidades = funcionalidadeService.filtrarFuncionalidadePorSecao(this.funcionalidade.getIdeSecao());
			} else {
				collFuncionalidades = funcionalidadeService.listarFuncionalidades();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}

		return collFuncionalidades;
	}

	public Collection<Acao> getColAcoes() {

		return acaoService.filtrarLista(new Acao());
	}

	public DualListModel<Funcionalidade> getFuncionalidades() {

		List<Funcionalidade> fs = null;
		List<Funcionalidade> fps = null;
		try {
			fs = (List<Funcionalidade>) getColFuncionalidades();
			fps = (List<Funcionalidade>) funcionalidadeService.filtrarFuncionalidadePorSecaoPerfil(secao, perfil);

			for (Funcionalidade fp : fps) {
				for (Funcionalidade f : fs) {
					if (fp.getIdeFuncionalidade() == f.getIdeFuncionalidade()) {
						fs.remove(fp);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.getMessage();

		}

		funcionalidades = Util.castListToDualListModel(fs, fps);
		return funcionalidades;
	}

	public void setFuncionalidades(DualListModel<Funcionalidade> funcionalidades) {

		this.funcionalidades = funcionalidades;
	}

	public DualListModel<Acao> getAcoes() {

		List<Acao> acs = null;
		List<Acao> acspf = null;

		acs = (List<Acao>) getColAcoes();
		acspf = (List<Acao>) acaoService.filtrarLista(new Acao());

		for (Acao ac : acs) {
			for (Acao acpf : acspf) {
				if (ac.getIdeAcao() == acpf.getIdeAcao()) {
					acs.remove(acpf);
					break;
				}
			}
		}

		acoes = Util.castListToDualListModel(getColAcoes(), new ArrayList<Acao>());
		return acoes;
	}

	public void setAcoes(DualListModel<Acao> acoes) {

		this.acoes = acoes;
	}

	public UIForm getFormularioASerLimpoPermissao() {

		return formularioASerLimpoPermissao;
	}

	public void setFormularioASerLimpoPermissao(UIForm formularioASerLimpoPermissao) {

		this.formularioASerLimpoPermissao = formularioASerLimpoPermissao;
	}

	public UIForm getFormularioASerLimpoPermissoes() {

		return formularioASerLimpoPermissoes;
	}

	public void setFormularioASerLimpoPermissoes(UIForm formularioASerLimpoPermissoes) {

		this.formularioASerLimpoPermissoes = formularioASerLimpoPermissoes;
	}

	public Collection<Secao> getColSecao() {

		return secaoService.filtrarListaSecoes(new Secao());
	}
*/