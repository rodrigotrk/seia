package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

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
import br.gov.ba.seia.service.AcaoService;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.FuncionalidadeAcaoPessoaFisicaPautaService;
import br.gov.ba.seia.service.FuncionalidadeAcaoPessoaFisicaService;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("controleAcessoPautaPermissoesController")
@ViewScoped
@SuppressWarnings("all")
public class ControleAcessoPautaPermissoesController {
	
	private List<Acao> permissoesPautaArea;
	private List<Acao> permissoesPautaGestor;
	private List<Acao> listaDePermissoesPautaArea;
	private List<Acao> listaDePermissoesPautaGestor;
	private Funcionario funcionarioSelecionado;
	private PessoaFisica gestor;
	private Pauta pautaArea;
	private Pauta pautaGestor;
	private boolean funcionarioSubstituto;
	
	@EJB
	private PautaService pautaService;	
	@EJB
	private AcaoService acaoService;	
	@EJB
	private FuncionalidadeAcaoPessoaFisicaService funcionalidadeAcaoPessoaFisicaService;
	@EJB
	private FuncionalidadeAcaoPessoaFisicaPautaService funcionalidadeAcaoPessoaFisicaPautaService;
	@EJB
	private AreaService areaService;
	
	public void carregaTela(){
		try{
			inicializarVariaveis();
			carregarPermissoes();
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	private void inicializarVariaveis() throws Exception {
		gestor = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
		carregarListaDePermissoesPautaArea();
		carregarListaDePermissoesPautaGestor();
		carregarPautaArea();
		carregarPautaGestor();
	}
	
	private void carregarPermissoes() throws Exception {
		permissoesPautaArea = acaoService.listarPermissoesPorFuncionalidadePorFuncionarioPorGestor(new Funcionalidade(17), funcionarioSelecionado, pautaArea);
		permissoesPautaGestor = acaoService.listarPermissoesPorFuncionalidadePorFuncionarioPorGestor(new Funcionalidade(18), funcionarioSelecionado, pautaGestor);
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
	
	@SuppressWarnings("unused")
	public void carregarListaDePermissoesPautaArea() throws Exception {
		//carregando conjunto de ações
		Acao apensarDocumento = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.APENSAR_DOCUMENTO.getId()));
		Acao encaminharProcesso = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.ENCAMINHAR_PROCESSO.getId()));
		Acao formarEquipe = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.FORMAR_EQUIPE.getId()));
		Acao adicionarTecnico = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.ADICIONAR_TECNICO.getId()));
		Acao removerTecnico = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.REMOVER_TECNICO.getId()));
		Acao distribuir = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.DISTRIBUIR.getId()));
		Acao visualizarEquipe = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.VISUALIZAR_EQUIPE.getId()));
		Acao definirCronograma = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.DEFINIR_CRONOGRAMA.getId()));
		
		List<Acao> permissoesPautaAreaItens = new ArrayList<Acao>();
		
		//permissoes da pauta da area
		permissoesPautaAreaItens.add(apensarDocumento);
		permissoesPautaAreaItens.add(encaminharProcesso);
		permissoesPautaAreaItens.add(formarEquipe);
		//permissoesPautaAreaItens.add(adicionarTecnico);
		//.add(removerTecnico);
		//permissoesPautaAreaItens.add(distribuir);
		permissoesPautaAreaItens.add(visualizarEquipe);
		permissoesPautaAreaItens.add(definirCronograma);
		
		setListaDePermissoesPautaArea(permissoesPautaAreaItens);
	}
	
	@SuppressWarnings("unused")
	public void carregarListaDePermissoesPautaGestor() throws Exception {
		//carregando conjunto de ações
		Acao apensarDocumento = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.APENSAR_DOCUMENTO.getId()));
		Acao encaminharProcesso = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.ENCAMINHAR_PROCESSO.getId()));
		Acao formarEquipe = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.FORMAR_EQUIPE.getId()));
		Acao adicionarTecnico = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.ADICIONAR_TECNICO.getId()));
		Acao removerTecnico = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.REMOVER_TECNICO.getId()));
		Acao distribuir = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.DISTRIBUIR.getId()));
		Acao visualizarEquipe = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.VISUALIZAR_EQUIPE.getId()));
		Acao definirCronograma = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.DEFINIR_CRONOGRAMA.getId()));			
		Acao notificar = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.NOTIFICAR.getId()));		
		Acao acoesDaNotificacao = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.ACOES_DA_NOTIFICACAO.getId()));		
		Acao aprovarNotificacao = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.APROVAR_NOTIFICACAO.getId()));
		Acao enviarNotificacaoParaRevisao = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.ENVIAR_NOTIFICACAO_PARA_REVISAO.getId()));
		Acao editarNotificacao = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.EDITAR_NOTIFICACAO.getId()));
		Acao cancelarNotificacao = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.CANCELAR_NOTIFICACAO.getId()));			
		Acao concluirProcesso = acaoService.carregaGet(new Acao(AcaoControlePermissaoAcessoPautaEnum.CONCLUIR_PROCESSO.getId()));
		
		List<Acao> permissoesPautaGestorItens = new ArrayList<Acao>();
		
		//permissoes da pauta do gestor
		permissoesPautaGestorItens.add(apensarDocumento);
		permissoesPautaGestorItens.add(encaminharProcesso);
		permissoesPautaGestorItens.add(formarEquipe);
		permissoesPautaGestorItens.add(adicionarTecnico);
		permissoesPautaGestorItens.add(removerTecnico);
		permissoesPautaGestorItens.add(distribuir);
		permissoesPautaGestorItens.add(visualizarEquipe);
		permissoesPautaGestorItens.add(definirCronograma);			
		permissoesPautaGestorItens.add(notificar);
		permissoesPautaGestorItens.add(acoesDaNotificacao);
		if(areaService.obterPessoaFisicaCoordenadorPorIdeArea(2).getIdePessoaFisica().equals(gestor.getIdePessoaFisica()) 
				|| areaService.obterPessoaFisicaCoordenadorPorIdeArea(56).getIdePessoaFisica().equals(gestor.getIdePessoaFisica()) )
			permissoesPautaGestorItens.add(concluirProcesso);
		//permissoesPautaGestorItens.add(aprovarNotificacao);
		//permissoesPautaGestorItens.add(enviarNotificacaoParaRevisao);
		//permissoesPautaGestorItens.add(editarNotificacao);
		//permissoesPautaGestorItens.add(cancelarNotificacao);
		setListaDePermissoesPautaGestor(permissoesPautaGestorItens);
		
	}
	
	public void salvarPermissoesSubstituto(){
		try{
			if(funcionarioSubstituto){
				inicializarVariaveis();
				gravarListaDePermissoes(listaDePermissoesPautaArea, FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DA_AREA.getId(), funcionarioSelecionado, pautaArea);
				gravarListaDePermissoes(listaDePermissoesPautaGestor, FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DO_GESTOR.getId(), funcionarioSelecionado, pautaGestor);
				JsfUtil.addSuccessMessage("O funcionário como substituto selecionado foi adicionado ao grupo de acesso.");
			}
			else{
				JsfUtil.addSuccessMessage("O funcionário selecionado foi adicionado ao grupo de acesso.");
			}
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}
	
	private void gravarListaDePermissoes(List<Acao> listaDeAcoes, Integer funcionalidade, Funcionario funcionario, Pauta pauta) throws Exception{
		
		for(Acao acao : listaDeAcoes){
			
			FuncionalidadeAcaoPessoaFisica funcionalidadeAcaoPessoaFisica =	null;
			
			funcionalidadeAcaoPessoaFisica = 
					funcionalidadeAcaoPessoaFisicaService.buscarFuncionalidadeAcaoPessoaFisicaPorFuncionalidadePorAcaoPorFuncionario(
							new Funcionalidade(funcionalidade), acao, funcionario);
			
			if(funcionalidadeAcaoPessoaFisica == null) {
				
				funcionalidadeAcaoPessoaFisica = new FuncionalidadeAcaoPessoaFisica(new Funcionalidade(funcionalidade),funcionario,acao);
				funcionalidadeAcaoPessoaFisicaService.salvar(funcionalidadeAcaoPessoaFisica);
				
				salvaFAPFP(funcionalidadeAcaoPessoaFisica, pauta);
			} else {
				Collection<FuncionalidadeAcaoPessoaFisicaPauta> fapfp = funcionalidadeAcaoPessoaFisicaService.carregar(funcionalidadeAcaoPessoaFisica);
				
				boolean existeFAPFP = false;
				
				if(!Util.isNullOuVazio(fapfp)) {
					
					for (FuncionalidadeAcaoPessoaFisicaPauta f : fapfp) {
						if(!Util.isNullOuVazio(f.getIdePauta()) && !Util.isNullOuVazio(pauta)) {
							if(f.getIdePauta().getIdePauta().equals(pauta.getIdePauta())) {
								existeFAPFP = true;
							}
						}
					}
				}
				
				if(!existeFAPFP) {
					salvaFAPFP(funcionalidadeAcaoPessoaFisica, pauta);
				}
			}
		}
	}

	private void salvaFAPFP(FuncionalidadeAcaoPessoaFisica funcionalidadeAcaoPessoaFisica, Pauta pauta) throws Exception {
		FuncionalidadeAcaoPessoaFisicaPautaPK funcionalidadeAcaoPessoaFisicaPautaPK = 
				new FuncionalidadeAcaoPessoaFisicaPautaPK(funcionalidadeAcaoPessoaFisica.getIdeFuncionalidadeAcaoPessoaFisica(), pauta.getIdePauta());
		
		FuncionalidadeAcaoPessoaFisicaPauta funcionalidadeAcaoPessoaFisicaPauta = 
				new FuncionalidadeAcaoPessoaFisicaPauta(funcionalidadeAcaoPessoaFisicaPautaPK, false);
		
		funcionalidadeAcaoPessoaFisicaPautaService.salvar(funcionalidadeAcaoPessoaFisicaPauta);
	}
	
	public void salvarPermissoes(){
		
		try{
			removerPermissoesNaoMarcadas(FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DA_AREA);
			removerPermissoesNaoMarcadas(FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DO_GESTOR);
			gravarListaDePermissoes(permissoesPautaArea, FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DA_AREA.getId(), funcionarioSelecionado, pautaArea);
			gravarListaDePermissoes(permissoesPautaGestor, FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DO_GESTOR.getId(), funcionarioSelecionado, pautaGestor);
			JsfUtil.addSuccessMessage("Permissões salvas com sucesso!");
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addSuccessMessage("Ocorreu um erro ao tentar salvar as permissões. Por favor, tente novamente.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		
	}

	private void removerPermissoesNaoMarcadas(FuncionalidadeControlePermissaoAcessoPautaEnum pautaEnum) throws Exception {
		
		List<Acao> permissoesParaRemover = new ArrayList<Acao>();
		List<Acao> permissoes = pautaEnum.equals(FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DA_AREA) ? listaDePermissoesPautaArea : listaDePermissoesPautaGestor;
		List<Acao> permissoesSelecionadas = pautaEnum.equals(FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DA_AREA) ? permissoesPautaArea : permissoesPautaGestor;
		
		for(Acao acao : permissoes) {
			if(!permissoesSelecionadas.contains(acao)) {
				permissoesParaRemover.add(acao);
			}
		}
		
		funcionalidadeAcaoPessoaFisicaService.removerTudo(
			funcionarioSelecionado, pautaEnum.equals(FuncionalidadeControlePermissaoAcessoPautaEnum.PAUTA_DA_AREA) ? pautaArea : pautaGestor, permissoesParaRemover);
	}

	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}
	
	public void setFuncionarioSelecionado(Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}

	public List<Acao> getPermissoesPautaArea() {
		return permissoesPautaArea;
	}

	public void setPermissoesPautaArea(List<Acao> permissoesPautaArea) {
		this.permissoesPautaArea = permissoesPautaArea;
	}

	public List<Acao> getPermissoesPautaGestor() {
		return permissoesPautaGestor;
	}

	public void setPermissoesPautaGestor(List<Acao> permissoesPautaGestor) {
		this.permissoesPautaGestor = permissoesPautaGestor;
	}

	public boolean isFuncionarioSubstituto() {
		return funcionarioSubstituto;
	}

	public void setFuncionarioSubstituto(boolean funcionarioSubstituto) {
		this.funcionarioSubstituto = funcionarioSubstituto;
	}

	public List<Acao> getListaDePermissoesPautaArea() {
		return listaDePermissoesPautaArea;
	}

	public void setListaDePermissoesPautaArea(
			List<Acao> listaDePermissoesPautaArea) {
		this.listaDePermissoesPautaArea = listaDePermissoesPautaArea;
	}

	public List<Acao> getListaDePermissoesPautaGestor() {
		return listaDePermissoesPautaGestor;
	}

	public void setListaDePermissoesPautaGestor(
			List<Acao> listaDePermissoesPautaGestor) {
		this.listaDePermissoesPautaGestor = listaDePermissoesPautaGestor;
	}
}
