package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.dto.MembroGrupoAcessoPautaDTO;
import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.FuncionalidadeAcaoPessoaFisica;
import br.gov.ba.seia.entity.FuncionalidadeAcaoPessoaFisicaPauta;
import br.gov.ba.seia.entity.FuncionalidadeAcaoPessoaFisicaPautaPK;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.enumerator.AcaoControlePermissaoAcessoPautaEnum;
import br.gov.ba.seia.enumerator.FuncionalidadeControlePermissaoAcessoPautaEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.FuncionalidadeAcaoPessoaFisicaPautaService;
import br.gov.ba.seia.service.FuncionalidadeAcaoPessoaFisicaService;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.UsuarioService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("controleAcessoPautaController")
@ViewScoped
@SuppressWarnings("all")
public class ControleAcessoPautaController {
	
	private Area areaSelecionada;
	private Collection<Area> listaArea;
	private Collection<Funcionario> listaFuncionario;
	private Funcionario funcionarioSelecionado;
	private Boolean indSubstituto;
	private List<MembroGrupoAcessoPautaDTO> grupoDeAcesso;
	private PessoaFisica gestor;
	private Pauta pautaArea;
	private Pauta pautaGestor;
	
	@EJB
	private AreaService areaService;
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private FuncionarioService funcionarioService;
	@EJB
	private FuncionalidadeAcaoPessoaFisicaService funcionalidadeAcaoPessoaFisicaService;
	@EJB
	private FuncionalidadeAcaoPessoaFisicaPautaService funcionalidadeAcaoPessoaFisicaPautaService;
	@EJB
	private UsuarioService usuarioService;
	@EJB
	private PautaService pautaService;
	
	@PostConstruct
	public void init(){
		try{
			limparTela();
			carregarListaArea();
			gestor = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
			carregarPautaArea();
			carregarPautaGestor();
			carregarGrupoAcessoPauta();
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log			
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	private void carregarPautaArea() throws Exception {
		Area area = areaService.buscarAreaPorPessoaFisica(gestor.getIdePessoaFisica());
		pautaArea = pautaService.obtemPautaArea(area);
	}

	private void carregarPautaGestor() throws Exception {
		pautaGestor = pautaService.obtemPautaPorIdeFuncionario(gestor.getIdePessoaFisica(),TipoPautaEnum.PAUTA_COORDENADOR_AREA.getTipo());
		if(pautaGestor==null){
			pautaGestor = pautaService.obtemPautaPorIdeFuncionario(gestor.getIdePessoaFisica(),TipoPautaEnum.PAUTA_DIRETOR_AREA.getTipo());
		}
	}
	
	private void carregarGrupoAcessoPauta() throws Exception {
		List<FuncionalidadeAcaoPessoaFisica> grupo = funcionalidadeAcaoPessoaFisicaService.listarPessoaFisicaGrupoAcessoPautaPorCoordenador(gestor);
		grupoDeAcesso = new ArrayList<MembroGrupoAcessoPautaDTO>();
		for(FuncionalidadeAcaoPessoaFisica fapf : grupo){
			String perfil=fapf.getIdePessoaFisica().getPessoaFisica().getUsuario().getIdePerfil().getDscPerfil();
			String indSubstituto="Não";
			for (Iterator iterator = fapf.getFuncionalidadeAcaoPessoaFisicaPautaCollection().iterator(); iterator.hasNext();) {
				FuncionalidadeAcaoPessoaFisicaPauta fapfPauta = (FuncionalidadeAcaoPessoaFisicaPauta) iterator.next();
				if(fapfPauta.getIndSubstituto() && fapfPauta.getIdePauta().equals(pautaGestor)){
					indSubstituto="Sim";
					break;
				}
			}
			MembroGrupoAcessoPautaDTO membro = new MembroGrupoAcessoPautaDTO(fapf.getIdePessoaFisica(),perfil,indSubstituto);
			grupoDeAcesso.add(membro);
		}		
	}
	
	public void limparTela() {
		areaSelecionada = null;
		funcionarioSelecionado = new Funcionario();
		indSubstituto = null;	
		grupoDeAcesso = new ArrayList<MembroGrupoAcessoPautaDTO>();
	}
	
	private boolean verificarExistenciaDeSubstituto() {
		for(MembroGrupoAcessoPautaDTO membro : grupoDeAcesso){
			if(membro.getDscIndSubstituto().equals("Sim")){
				return true;
			}
		}
		return false;		
	}
	
	public void adicionarNovoMebroAoGrupo() {
		if((indSubstituto && verificarExistenciaDeSubstituto())==true){
			JsfUtil.addErrorMessage("Não foi possível adicionar o funcionário, pois já existe substituto.");
			RequestContext.getCurrentInstance().addCallbackParam("membroSalvo",false);
		}
		else{
			try{
				FuncionalidadeAcaoPessoaFisica funcionalidadeAcaoPessoaFisica;
				funcionalidadeAcaoPessoaFisica = 
						funcionalidadeAcaoPessoaFisicaService.buscarFuncionalidadeAcaoPessoaFisicaPorFuncionalidadePorAcaoPorFuncionario(
								new Funcionalidade(FuncionalidadeControlePermissaoAcessoPautaEnum.PERMISSAO_DE_ACESSO_A_PAUTA.getId()),
								new Acao(AcaoControlePermissaoAcessoPautaEnum.ACESSAR_PAUTA_GESTOR.getId()), funcionarioSelecionado
						);
				
				if(funcionalidadeAcaoPessoaFisica == null){
					funcionalidadeAcaoPessoaFisica = new FuncionalidadeAcaoPessoaFisica(
							new Funcionalidade(FuncionalidadeControlePermissaoAcessoPautaEnum.PERMISSAO_DE_ACESSO_A_PAUTA.getId()),
							funcionarioSelecionado,
							new Acao(AcaoControlePermissaoAcessoPautaEnum.ACESSAR_PAUTA_GESTOR.getId())
					);
					funcionalidadeAcaoPessoaFisicaService.salvar(funcionalidadeAcaoPessoaFisica);
				}
				
				FuncionalidadeAcaoPessoaFisicaPautaPK funcionalidadeAcaoPessoaFisicaPautaPK = 
						new FuncionalidadeAcaoPessoaFisicaPautaPK(
								funcionalidadeAcaoPessoaFisica.getIdeFuncionalidadeAcaoPessoaFisica(),
								pautaGestor.getIdePauta()
						);
				FuncionalidadeAcaoPessoaFisicaPauta funcionalidadeAcaoPessoaFisicaPauta = 
						new FuncionalidadeAcaoPessoaFisicaPauta(
								funcionalidadeAcaoPessoaFisicaPautaPK, 
								indSubstituto
						);
				
				funcionalidadeAcaoPessoaFisicaPautaService.salvar(funcionalidadeAcaoPessoaFisicaPauta);
				
				limparTela();
				carregarGrupoAcessoPauta();
				
				RequestContext.getCurrentInstance().addCallbackParam("membroSalvo",true);
				
			}
			catch(Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				JsfUtil.addErrorMessage("Não foi possível adicionar o funcionário selecionado no grupo de acesso.");
				RequestContext.getCurrentInstance().addCallbackParam("membroSalvo",false);
			}
		}
	}
	
	public void removerMebroDoGrupo(Funcionario funcionario) {
		try{
			
			List<Acao> acoesPautaArea = Arrays.asList(new Acao[] {
				new Acao(AcaoControlePermissaoAcessoPautaEnum.APENSAR_DOCUMENTO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.ENCAMINHAR_PROCESSO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.FORMAR_EQUIPE.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.ADICIONAR_TECNICO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.REMOVER_TECNICO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.DISTRIBUIR.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.VISUALIZAR_EQUIPE.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.DEFINIR_CRONOGRAMA.getId())
			});
			
			List<Acao> acoesPautaGestor = Arrays.asList(new Acao[] {
				new Acao(AcaoControlePermissaoAcessoPautaEnum.APENSAR_DOCUMENTO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.ENCAMINHAR_PROCESSO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.FORMAR_EQUIPE.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.ADICIONAR_TECNICO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.REMOVER_TECNICO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.DISTRIBUIR.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.VISUALIZAR_EQUIPE.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.DEFINIR_CRONOGRAMA.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.NOTIFICAR.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.ACOES_DA_NOTIFICACAO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.APROVAR_NOTIFICACAO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.ENVIAR_NOTIFICACAO_PARA_REVISAO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.EDITAR_NOTIFICACAO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.CANCELAR_NOTIFICACAO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.CONCLUIR_PROCESSO.getId()),
				new Acao(AcaoControlePermissaoAcessoPautaEnum.ACESSAR_PAUTA_GESTOR.getId())
			});
			
			funcionalidadeAcaoPessoaFisicaService.removerTudo(funcionario, pautaArea, acoesPautaArea);
			funcionalidadeAcaoPessoaFisicaService.removerTudo(funcionario, pautaGestor,acoesPautaGestor);
			carregarGrupoAcessoPauta();
			JsfUtil.addSuccessMessage("O funcionário foi removido do grupo de acesso!");
			
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("O funcionário não pôde ser removido do grupo de acesso. Por favor, tente novamente.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void carregarListaArea() {
		if(Util.isNullOuVazio(listaArea)) {
			listaArea =  areaService.filtrarListaAreas(new Area());
		}
	}	
	
	public void changeListenerArea(AjaxBehaviorEvent evt) {
		try {
			if(areaSelecionada!=null) {
				listaFuncionario = funcionarioService.listarFuncionarioPorArea(areaSelecionada);
			}
			else {
				listaFuncionario = null;
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public Area getAreaSelecionada() {
		return areaSelecionada;
	}

	public void setAreaSelecionada(Area areaSelecionada) {
		this.areaSelecionada = areaSelecionada;
	}

	public Boolean getIndSubstituto() {
		return indSubstituto;
	}

	public void setIndSubstituto(Boolean indSubstituto) {
		this.indSubstituto = indSubstituto;
	}

	public List<MembroGrupoAcessoPautaDTO> getGrupoDeAcesso() {
		return grupoDeAcesso;
	}

	public void setGrupoDeAcesso(List<MembroGrupoAcessoPautaDTO> grupoDeAcesso) {
		this.grupoDeAcesso = grupoDeAcesso;
	}
	
	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	public void setFuncionarioSelecionado(Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}

	public Collection<Area> getListaArea() {
		return listaArea;
	}

	public void setListaArea(Collection<Area> listaArea) {
		this.listaArea = listaArea;
	}

	public Collection<Funcionario> getListaFuncionario() {
		return listaFuncionario;
	}

	public void setListaFuncionario(Collection<Funcionario> listaFuncionario) {
		this.listaFuncionario = listaFuncionario;
	}
}
