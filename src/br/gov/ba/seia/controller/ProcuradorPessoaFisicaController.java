package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.inject.Named;

import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaFisicaHistorico;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.PessoaFisicaHistoricoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorPessoaFisicaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import java.util.Collection;
import org.apache.log4j.Level;

@Named("procuradorPessoaFisicaController")
@ViewScoped
public class ProcuradorPessoaFisicaController {
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;
	
	@EJB
	private PessoaFisicaHistoricoService pessoaFisicaHistoricoService;
	
	private PessoaFisica pessoaSelecionada;
	private PessoaFisica pessoaPesquisa;
	private ProcuradorPessoaFisica procuradorPessoaFisica;
	private PessoaFisica donoDoProcurador;
	private List<ProcuradorPessoaFisica> listaProcuradorPessoaFisica;
	
	private boolean showExpandirFormCadastro;
	private boolean renderedFormPessoaFisicaProcurador;
	private boolean disabledFormPessoaFisicaProcurador;
	private Boolean disabledCpf = Boolean.FALSE;
	
	private DataModel<ProcuradorPessoaFisica> modelProcuradorPessoaFisica;
	
	public void consultarPessoaFisicaPorCpf() {
		try {
			pessoaSelecionada = pessoaFisicaService.filtrarPessoaFisicaByCpf(pessoaPesquisa);
			if (!Util.isNull(pessoaSelecionada)) {
				disabledFormPessoaFisicaProcurador = true;
				renderedFormPessoaFisicaProcurador = true;
			}
			else {
				MensagemUtil.alerta(BUNDLE.getString("procuradorMsgErroCpfNaoCadastrado"));
			}
			
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public Boolean getDisabledCpf() {
	    return disabledCpf;
	}

	public void setDisabledCpf(Boolean disabledCpf) {
	    this.disabledCpf = disabledCpf;
	}

	@PostConstruct
	public void init() {
		tratarDonoDoProcurador();
		pessoaSelecionada = new PessoaFisica();
		pessoaPesquisa = new PessoaFisica();		
		listaProcuradorPessoaFisica = new ArrayList<ProcuradorPessoaFisica>();
		procuradorPessoaFisica = new ProcuradorPessoaFisica();
		showExpandirFormCadastro = true;
	}
	
	private void tratarDonoDoProcurador() {
		Usuario usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
		
		if (usuarioLogado.isUsuarioExterno() 
				|| (ContextoUtil.getContexto().getCadastroInCompleto() != null && ContextoUtil.getContexto().getCadastroInCompleto())) {
			
			if (ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().equals(ContextoUtil.getContexto().getPessoaFisica()) || Util.isNull(ContextoUtil.getContexto().getPessoaFisica())) {
				donoDoProcurador = usuarioLogado.getPessoaFisica();
			} else {
				donoDoProcurador = ContextoUtil.getContexto().getPessoaFisica();
			}
		} else {
			donoDoProcurador = ContextoUtil.getContexto().getPessoaFisica();
		}
		if (Util.isNull(donoDoProcurador)) {
			donoDoProcurador = new PessoaFisica();
		}
	}
	
	/**
	 * Verifica se é permitido adicionar ou excluir procuradores com base nas regras de negócio.
	 * Regras:
	 * 1. Somente no primeiro cadastro é permitido incluir procuradores
	 * 2. No primeiro cadastro, só é permitido incluir a si mesmo como procurador
	 * 3. Após o primeiro cadastro, não é permitido incluir ou excluir procuradores
	 */
	private boolean isPermitidoManipularProcuradores() {
	    try {
	        // Verificar se o usuário logado é o próprio ou um procurador
	        PessoaFisica usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
	        boolean isProprioUsuario = donoDoProcurador.equals(usuarioLogado);
	        
	        // Se for o próprio usuário, SEMPRE pode manipular procuradores
	        if (isProprioUsuario) {
	            return true;
	        }
	        
	        // Se chegou aqui, é um procurador ou outro tipo de usuário
	        // Verificar se a pessoa já está salva no banco de dados (tem ID)
	        boolean pessoaJaExiste = !Util.isNullOuVazio(donoDoProcurador.getId());
	        
	        if (pessoaJaExiste) {
	            // Buscar todos os procuradores existentes
	            Collection<ProcuradorPessoaFisica> procuradoresExistentes = listarProcuradoresPessoaFisica();
	            
	            // Se já tem procuradores, não é o primeiro cadastro
	            // Procurador não pode manipular após o primeiro cadastro
	            if (procuradoresExistentes != null && !procuradoresExistentes.isEmpty()) {
	                return false;
	            }
	        }
	        
	        // Se chegou aqui, é o primeiro cadastro ou não é um procurador
	        return true;
	    } catch (Exception e) {
	        Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
	        return false; // Em caso de erro, não permite manipulação por segurança
	    }
	}

	/**
	 * Verifica se o procurador que está sendo adicionado é o próprio usuário logado
	 */
	private boolean isProcuradorUsuarioLogado() {
	    // Obter o usuário logado
	    PessoaFisica usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
	    
	    // Verificar se o procurador selecionado é o próprio usuário logado
	    return pessoaSelecionada != null && pessoaSelecionada.equals(usuarioLogado);
	}

	/**
	 * Lista os procuradores da pessoa física atual
	 */
	private Collection<ProcuradorPessoaFisica> listarProcuradoresPessoaFisica() {
	    try {
	        // Verificar se a pessoa tem ID (já está salva)
	        if (Util.isNullOuVazio(donoDoProcurador.getId())) {
	            return new ArrayList<ProcuradorPessoaFisica>(); // Com tipo explícito para Java 6
	        }
	        
	        // Criar um objeto ProcuradorPessoaFisica para usar como filtro
	        ProcuradorPessoaFisica filtro = new ProcuradorPessoaFisica();
	        filtro.setIdePessoaFisica(donoDoProcurador);
	        
	        // Chamar o serviço para listar os procuradores
	        Collection<ProcuradorPessoaFisica> procuradores = procuradorPessoaFisicaService.listarProcuradorPessoaFisica(filtro);
	        return procuradores != null ? procuradores : new ArrayList<ProcuradorPessoaFisica>();
	    } catch (Exception e) {
	        Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
	        return new ArrayList<ProcuradorPessoaFisica>();  // Com tipo explícito para Java 6
	    }
	}

	/**
	 * Método para salvar um procurador
	 */
	/**
	 * Método para salvar um procurador
	 */
	public void salvarPessoa() {
	    try {
	        procuradorPessoaFisica.setIdeProcurador(pessoaSelecionada);
	        procuradorPessoaFisica.setIdePessoaFisica(donoDoProcurador);
	        
	        // Verificar se o usuário logado é o próprio usando o método robusto
	        boolean isProprioUsuario = isUsuarioProprietario();
	        
	        
	        // Se NÃO for o próprio usuário, aplicar validações específicas
	        if (!isProprioUsuario) {
	            // Verificar se é permitido adicionar procuradores
	            if (!isPermitidoManipularProcuradores()) {
	                MensagemUtil.alerta("Não é permitido incluir outros procuradores no primeiro cadastro.");
	                return;
	            }
	            
	            // Verificar se o procurador é o próprio usuário logado
	            if (!isProcuradorUsuarioLogado()) {
	                MensagemUtil.alerta("No primeiro cadastro de pessoa física, o procurador só pode adicionar a si mesmo como procurador.");
	                return;
	            }
	        }

	        // Continuar com o processo de salvamento...
	        if (validarProcurador()) {
	            pessoaSelecionada.setNumCpf(pessoaPesquisa.getNumCpf());
	            pessoaSelecionada.setTipSexo(null);

	            showExpandirFormCadastro = true;

	            String msgSucesso = BUNDLE.getString("geral_msg_alteracao_sucesso");

	            if (Util.isNull(procuradorPessoaFisica.getIdeProcuradorPessoaFisica())) {
	                msgSucesso = BUNDLE.getString("geral_msg_inclusao_sucesso");
	            }

	            if (!procuradorAlreadyProcuradorPF()) {
	                procuradorPessoaFisicaService.salvarProcuradorPessoaFisica(procuradorPessoaFisica);
	                pessoaFisicaHistoricoService.salvarHistoricoPessoaFisica(preencherHistoricoPessoaFisica());
	                loadListaProcuradorPessoaFisica();
	                if (Util.isNull(procuradorPessoaFisica.getIdeProcuradorPessoaFisica())) {
	                    MensagemUtil.sucesso("Procurador incluído com sucesso.");
	                } else {
	                    MensagemUtil.sucesso("Procurador alterado com sucesso.");
	                }
	            }
	            else {
	                MensagemUtil.alerta("Procurador(a) já cadastrado(a) para essa Pessoa Física");
	            }
	        }
	        else {
	            MensagemUtil.alerta(BUNDLE.getString("procuradorMsgErroProcuradorDeleMesmo"));
	        }
	        Html.atualizar("paneltabviewpf");
	    }
	    catch (Exception e) {
	        throw Util.capturarException(e, Util.SEIA_EXCEPTION);
	    }
	}

	// Método auxiliar para verificar se existem procuradores cadastrados
	private boolean existemProcuradoresCadastrados() {
	    try {
	        // Criar um objeto ProcuradorPessoaFisica para usar como filtro
	        ProcuradorPessoaFisica filtro = new ProcuradorPessoaFisica();
	        filtro.setIdePessoaFisica(donoDoProcurador);
	        
	        // Chamar o serviço para listar os procuradores
	        Collection<ProcuradorPessoaFisica> procuradores = procuradorPessoaFisicaService.listarProcuradorPessoaFisica(filtro);
	        
	        // Verificar se existem procuradores
	        return procuradores != null && !procuradores.isEmpty();
	    } catch (Exception e) {
	        Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
	        return false;  // Em caso de erro, assume que não existem procuradores
	    }
	}
	
	public PessoaFisicaHistorico preencherHistoricoPessoaFisica() {
		PessoaFisicaHistorico pessoaFisicaHistorico = new PessoaFisicaHistorico();
		
		pessoaFisicaHistorico.setIdePessoaFisica(procuradorPessoaFisica.getIdePessoaFisica());
		pessoaFisicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaFisicaHistorico.setIdeProcuradorPessoaFisica(procuradorPessoaFisica);
		pessoaFisicaHistorico.setStatusProcurador(true);
		pessoaFisicaHistorico.setDtcHistorico(new Date());
		
		return pessoaFisicaHistorico;
	}

	private boolean procuradorAlreadyProcuradorPF() {
		try {
			Boolean procuradorAlreadyProcuradorPF = null;
			if(Util.isNullOuVazio(procuradorPessoaFisica.getIdeProcurador().getIdePessoaFisica())){
				return false;
			}
			procuradorAlreadyProcuradorPF = procuradorPessoaFisicaService.procuradorAlreadyProcuradorPF(procuradorPessoaFisica);
			return procuradorAlreadyProcuradorPF;
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION); 
		}
	}

	public void limparObjetos() {
		this.pessoaSelecionada = new PessoaFisica();
		this.procuradorPessoaFisica = new ProcuradorPessoaFisica();
		pessoaPesquisa = new PessoaFisica();
		renderedFormPessoaFisicaProcurador=false;
		disabledFormPessoaFisicaProcurador=false;
	}

	public String limparObjetosPessoaFisicaAction() {
		this.pessoaSelecionada = new PessoaFisica();
		this.procuradorPessoaFisica = new ProcuradorPessoaFisica();
		disabledFormPessoaFisicaProcurador = false;
		renderedFormPessoaFisicaProcurador= false;
		pessoaPesquisa = new PessoaFisica();
		return "";
	}

	public PessoaFisica getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(PessoaFisica pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}

	private Boolean validarProcurador() {
		Boolean retorno = Boolean.TRUE;
		
		try {
			if (!Util.isNull(procuradorPessoaFisica) 
					&& !Util.isNull(procuradorPessoaFisica.getIdePessoaFisica()) 
					&& !Util.isNull(procuradorPessoaFisica.getIdeProcurador()) 
					&& !Util.isNull(procuradorPessoaFisica.getIdeProcurador().getIdePessoaFisica())
					&& !Util.isNull(procuradorPessoaFisica.getIdePessoaFisica().getIdePessoaFisica()) 
					&& procuradorPessoaFisica.getIdeProcurador().getIdePessoaFisica().intValue() == procuradorPessoaFisica.getIdePessoaFisica().getIdePessoaFisica().intValue()) {
				
				retorno = Boolean.FALSE;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao relacionar Procurador com Pessoa Física.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return retorno;
	}

	public void loadListaProcuradorPessoaFisica() {
		try {
			procuradorPessoaFisica.setIdePessoaFisica(donoDoProcurador);
			listaProcuradorPessoaFisica = (List<ProcuradorPessoaFisica>) procuradorPessoaFisicaService.listarProcuradorPessoaFisica(procuradorPessoaFisica);
		}
		catch (Exception e) {
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	/**
	 * Método para excluir um procurador
	 */
	public void excluirProcuradorPessoaFisica() {
	    try {
	        // Verificar se o usuário logado é o próprio usando o método robusto
	        boolean isProprioUsuario = isUsuarioProprietario();
	        
	        
	        // Se NÃO for o próprio usuário, aplicar validações específicas
	        if (!isProprioUsuario) {
	            // Verificar se é permitido excluir procuradores
	            if (!isPermitidoManipularProcuradores()) {
	                MensagemUtil.alerta("Não é permitido excluir procuradores após o primeiro cadastro.");
	                return;
	            }
	            
	            // Verificar se o procurador a ser excluído é o próprio usuário logado
	            PessoaFisica usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
	            boolean isExcluindoProprioUsuario = procuradorPessoaFisica.getIdeProcurador().equals(usuarioLogado);
	            
	            if (!isExcluindoProprioUsuario) {
	                MensagemUtil.alerta("No primeiro cadastro, o procurador só pode excluir a si mesmo como procurador.");
	                return;
	            }
	        }

	        ProcuradorPessoaFisica procuradorTemp = procuradorPessoaFisica;
	        procuradorPessoaFisicaService.excluirProcuradorPessoaFisica(procuradorTemp);
	        pessoaFisicaHistoricoService.salvarHistoricoPessoaFisica(preencherHistoricoPessoaFisica());
	        loadListaProcuradorPessoaFisica();
	        MensagemUtil.sucesso("Procurador excluído com sucesso.");
	    } catch (Exception e) {
	        throw Util.capturarException(e, Util.SEIA_EXCEPTION);
	    }
	}
	
	/**
	 * Método para verificar se o usuário logado é o próprio dono
	 */
	private boolean isUsuarioProprietario() {
	    try {
	        // Obter o usuário logado
	        PessoaFisica usuarioLogado = null;
	        if (ContextoUtil.getContexto() != null && ContextoUtil.getContexto().getUsuarioLogado() != null) {
	            usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
	        }
	        
	        // Verificar se o donoDoProcurador é o próprio usuário logado
	        if (usuarioLogado != null && donoDoProcurador != null) {
	            // Verificar por ID para garantir que a comparação funcione
	            if (usuarioLogado.getId() != null && donoDoProcurador.getId() != null) {
	                return usuarioLogado.getId().equals(donoDoProcurador.getId());
	            }
	            // Se os IDs não estiverem disponíveis, tentar comparar por CPF
	            if (usuarioLogado.getNumCpf() != null && donoDoProcurador.getNumCpf() != null) {
	                return usuarioLogado.getNumCpf().equals(donoDoProcurador.getNumCpf());
	            }
	        }
	        
	        return false;
	    } catch (Exception e) {
	        Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
	        return false;
	    }
	}
	
	public PessoaFisicaHistorico preencherHistoricoPessoaFisicaExclusaoProcurador() {
		PessoaFisicaHistorico pessoaFisicaHistorico = new PessoaFisicaHistorico();
		
		pessoaFisicaHistorico = pessoaFisicaHistoricoService.selecionarPorIdeProcuradorPessoaFisica(procuradorPessoaFisica);
		
		pessoaFisicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
		pessoaFisicaHistorico.setStatusProcurador(false);
		pessoaFisicaHistorico.setDtcHistorico(new Date());
		
		return pessoaFisicaHistorico;
	}
	

	public PessoaFisica getPessoaPesquisa() {
		return pessoaPesquisa;
	}

	public void setPessoaPesquisa(PessoaFisica pessoaPesquisa) {
		this.pessoaPesquisa = pessoaPesquisa;
	}

	public List<ProcuradorPessoaFisica> getListaProcuradorPessoaFisica() {
		return listaProcuradorPessoaFisica;
	}

	public void setListaProcuradorPessoaFisica(List<ProcuradorPessoaFisica> listaProcuradorPessoaFisica) {
		this.listaProcuradorPessoaFisica = listaProcuradorPessoaFisica;
	}

	public DataModel<ProcuradorPessoaFisica> getModelProcuradorPessoaFisica() {
		loadListaProcuradorPessoaFisica();
		if (!Util.isNull(listaProcuradorPessoaFisica) && !listaProcuradorPessoaFisica.isEmpty()) {
			modelProcuradorPessoaFisica = Util.castListToDataModel(listaProcuradorPessoaFisica);
		} else {
			modelProcuradorPessoaFisica = null;
		}
		return modelProcuradorPessoaFisica;
	}

	public void setModelProcuradorPessoaFisica(DataModel<ProcuradorPessoaFisica> modelProcuradorPessoaFisica) {
		this.modelProcuradorPessoaFisica = modelProcuradorPessoaFisica;
	}

	public ProcuradorPessoaFisica getProcuradorPessoaFisica() {
		return procuradorPessoaFisica;
	}

	public void setProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica) {
		this.procuradorPessoaFisica = procuradorPessoaFisica;
	}

	public PessoaFisica getDonoDoProcurador() {
		return donoDoProcurador;
	}

	public void setDonoDoProcurador(PessoaFisica donoDoProcurador) {
		this.donoDoProcurador = donoDoProcurador;
	}

	public boolean getShowExpandirFormCadastro() {
		return showExpandirFormCadastro;
	}

	public void setShowExpandirFormCadastro(boolean showExpandirFormCadastro) {
		this.showExpandirFormCadastro = showExpandirFormCadastro;
	}

	public void setaPessoaFisicaUsuario() {
		pessoaSelecionada = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
	}

	public boolean isRenderedFormPessoaFisicaProcurador() {
		return renderedFormPessoaFisicaProcurador;
	}

	public void setRenderedFormPessoaFisicaProcurador(boolean renderedFormPessoaFisicaProcurador) {
		this.renderedFormPessoaFisicaProcurador = renderedFormPessoaFisicaProcurador;
	}

	public boolean isDisabledFormPessoaFisicaProcurador() {
		return disabledFormPessoaFisicaProcurador;
	}

	public void setDisabledFormPessoaFisicaProcurador(boolean disabledFormPessoaFisicaProcurador) {
		this.disabledFormPessoaFisicaProcurador = disabledFormPessoaFisicaProcurador;
	}
	
	
}
