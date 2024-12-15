package br.gov.ba.seia.facade;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.dao.MovimentacaoFinanceiraDaoImpl;
import br.gov.ba.seia.dao.MovimentacaoFinanceiraTccaProdutoDaoImpl;
import br.gov.ba.seia.dao.ProdutoExecucaoDaoImpl;
import br.gov.ba.seia.dao.ProdutoSaldoDaoImpl;
import br.gov.ba.seia.dao.ProjetoAcaoDaoImpl;
import br.gov.ba.seia.dao.ProjetoAcaoProdutoDaoImpl;
import br.gov.ba.seia.dao.ProjetoEmpresaExecutoraDaoImpl;
import br.gov.ba.seia.dao.ProjetoStatusDaoImpl;
import br.gov.ba.seia.dao.ProjetoUnidadeConservacaoDaoImpl;
import br.gov.ba.seia.dao.TccaDaoImpl;
import br.gov.ba.seia.dao.TccaDocumentoApensadoDaoImpl;
import br.gov.ba.seia.dao.TccaHistoricoReajusteValorDaoImpl;
import br.gov.ba.seia.dao.TccaHistoricoRenovacaoPrazoValidadeDaoImpl;
import br.gov.ba.seia.dao.TccaProjetoDaoImpl;
import br.gov.ba.seia.dao.TccaSaldoDaoImpl;
import br.gov.ba.seia.dao.TccaStatusDaoImpl;
import br.gov.ba.seia.dao.TipoOrigemDestinoDaoImpl;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.EmpreendimentoTipologia;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.MovimentacaoFinanceira;
import br.gov.ba.seia.entity.MovimentacaoFinanceiraTccaProduto;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProdutoExecucao;
import br.gov.ba.seia.entity.ProdutoSaldo;
import br.gov.ba.seia.entity.ProjetoAcao;
import br.gov.ba.seia.entity.ProjetoAcaoProduto;
import br.gov.ba.seia.entity.ProjetoEmpresaExecutora;
import br.gov.ba.seia.entity.ProjetoStatus;
import br.gov.ba.seia.entity.ProjetoUnidadeConservacao;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.entity.TccaDocumentoApensado;
import br.gov.ba.seia.entity.TccaHistoricoReajusteValor;
import br.gov.ba.seia.entity.TccaHistoricoRenovacaoPrazoValidade;
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.entity.TccaSaldo;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoOrigemDestino;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.UnidadeConservacao;
import br.gov.ba.seia.entity.VwConsultaEmpreendimento;
import br.gov.ba.seia.enumerator.TccaProjetoTipoStatusEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.service.EmpreendimentoRequerimentoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.LicencaService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.RequerimentoPessoaService;
import br.gov.ba.seia.service.TccaService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BaseTccaFacade {
	
	/**
	 * SERVICE's
	 */
	
	@EJB
	private TccaService tccaService;
	
	@EJB
	private EnderecoPessoaService enderecoPessoaService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private EmpreendimentoRequerimentoService empreendimentoRequerimentoService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private RequerimentoPessoaService requerimentoPessoaService;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private TipologiaService tipologiaService;
	
	@EJB
	private LicencaService licencaService;
	
	/**
	 * DAO's
	 */
	
	@EJB
	private ProjetoStatusDaoImpl projetoStatusDaoImpl;
	
	@EJB
	private ProjetoAcaoDaoImpl projetoAcaoDaoImpl;
	
	@EJB
	private ProjetoAcaoProdutoDaoImpl projetoAcaoProdutoDaoImpl;
	
	@EJB
	private ProjetoUnidadeConservacaoDaoImpl projetoUnidadeConservacaoDaoImpl;
	
	@EJB
	private ProdutoSaldoDaoImpl produtoSaldoDaoImpl;

	@EJB
	private ProdutoExecucaoDaoImpl produtoExecucaoDaoImpl;
	
	@EJB
	private TccaDaoImpl tccaDaoImpl;
	
	@EJB
	private TccaDocumentoApensadoDaoImpl tccaDocumentoApensadoDaoImpl;
	
	@EJB
	private TccaStatusDaoImpl tccaStatusDaoImpl;
	
	@EJB
	private TccaProjetoDaoImpl tccaProjetoDaoImpl;
	
	@EJB
	private TccaSaldoDaoImpl tccaSaldoDaoImpl;
	
	@EJB
	private TccaHistoricoRenovacaoPrazoValidadeDaoImpl tccaHistoricoRenovacaoPrazoValidadeDaoImpl;

	@EJB
	private TccaHistoricoReajusteValorDaoImpl tccaHistoricoReajusteValorDaoImpl;
	
	@EJB
	private ProjetoEmpresaExecutoraDaoImpl projetoEmpresaExecutoraDaoImpl;
	
	@EJB
	private TipoOrigemDestinoDaoImpl tipoOrigemDestinoDaoImpl;
	
	@EJB
	private MovimentacaoFinanceiraDaoImpl movimentacaoFinanceiraDaoImpl;
	
	@EJB
	private MovimentacaoFinanceiraTccaProdutoDaoImpl movimentacaoFinanceiraTccaProdutoDaoImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Telefone> buscarTelefonesPorPessoa(Pessoa pessoa) {
		try {
			return telefoneService.buscarTelefonesPorPessoa(pessoa);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> buscarRepresentantesLegaisPorPessoa(PessoaJuridica pessoaJuridica) {
		try {
			return representanteLegalService.listarRepresentanteLegalPorPessoaJuridica(pessoaJuridica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EnderecoPessoa buscarEnderecoPorPessoa(Pessoa pessoa) {
		try {
			return enderecoPessoaService.buscarEnderecoPorPessoa(pessoa);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<VwConsultaEmpreendimento> listarEmpreendimentoPorRequerente(Municipio mun, Pessoa requerente, String nome, int first, int pageSize) {
		try {
			return empreendimentoService.listarPorCriteriaDemanda(mun, requerente, nome, first, pageSize);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countEmpreendimentoPorRequerente(Municipio mun, Pessoa requerente, String nome) {
		try {
			return empreendimentoService.count(mun, requerente, nome);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento buscarEmpreendimentoPorIdComMunicipio(Integer ideEmpreendimento) {
		try {
			return empreendimentoService.carregarPorIdComMunicipio(ideEmpreendimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<EmpreendimentoTipologia> listarEmpreendimentoTipologia(Empreendimento empreendimento) {
		try {
			return empreendimentoService.buscarEmpreendimentoTipologia(empreendimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> listarTipologias() {
		try {
			return tipologiaService.filtrarListaTipologias(new Tipologia(new Tipologia()));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TreeNode montarArvoreTipologia(Tipologia pTipologiaSelecionada) {
		try {
			return empreendimentoService.montarArvoreTipologia(pTipologiaSelecionada);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmpreendimentoTipologia(EmpreendimentoTipologia pEmpreendimentoTipologia) {
		try {
			empreendimentoService.salvarEmpreendimentoTipologia(pEmpreendimentoTipologia);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirEmpreendimentoTipologia(EmpreendimentoTipologia et) {
		try {
			empreendimentoService.excluirEmpreendimentoTipologia(et);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo buscarProcessoPorNumero(String numProcesso) {
		try {
			RequerimentoPessoa rp = null;
			
			Processo p = processoService.buscarProcessoPeloNumero(new Processo(numProcesso)); 
			
			if(!Util.isNullOuVazio(p) && !Util.isNullOuVazio(p.getIdeRequerimento())
					&& !Util.isNullOuVazio(p.getIdeRequerimento().getRequerimentoPessoaCollection())) {
				
				for (RequerimentoPessoa reqPess : p.getIdeRequerimento().getRequerimentoPessoaCollection()) {
					if(reqPess.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {
						rp = reqPess;
					}
				}
			
				if(Util.isNullOuVazio(rp)) {
					rp = requerimentoPessoaService.obterPorRequerimentoETipoPessoa(p.getIdeRequerimento(), new TipoPessoaRequerimento(1));
				}
				
				p.getIdeRequerimento().setRequerente(rp.getPessoa());
				
				//BUSCA O EMPREENDIMENTO
				EmpreendimentoRequerimento er = empreendimentoRequerimentoService.buscarEmpreendimentoRequerimento(p.getIdeRequerimento());
				p.getIdeRequerimento().setUltimoEmpreendimento(er.getIdeEmpreendimento());
			}
			
			return p;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTCCA(Tcca tcca) {
		try {
			tccaService.salvarTCCA(tcca);
		} catch (Exception e) {
			if(!e.getMessage().equals("Este número de TCCA já existe, por favor insira outro.")) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			} else {
				JsfUtil.addErrorMessage("Houve um erro ao salvar.");
			}
			
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTccaProjeto(TccaProjeto tccaProjeto) {
		try {
			tccaProjetoDaoImpl.salvar(tccaProjeto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTCCADocumentoApensado(TccaDocumentoApensado tda) {
		try {
			tccaDocumentoApensadoDaoImpl.salvar(tda);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TccaDocumentoApensado> listarDocumentosApensados(Tcca tcca) {
		try {
			return tccaDocumentoApensadoDaoImpl.listarPorTCCA(tcca);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countConsultaTcca(Pessoa requerenteFiltro, Empreendimento empreendimentoFiltro, 
			String numLicencaFiltro, String numProcessoFiltro, String numTCCAFiltro) {
		
		try {
			return tccaDaoImpl.countConsultaTcca(requerenteFiltro, empreendimentoFiltro, numLicencaFiltro, numProcessoFiltro, numTCCAFiltro);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tcca> listarTcca(Pessoa requerenteFiltro, Empreendimento empreendimentoFiltro,
			String numLicencaFiltro, String numProcessoFiltro, String numTCCAFiltro, int first, int pageSize) {
		
		try {
			
			List<Tcca> tccas = tccaDaoImpl.consultarTcca(requerenteFiltro, empreendimentoFiltro, numLicencaFiltro, numProcessoFiltro, numTCCAFiltro, first, pageSize);
			
			for (Tcca t : tccas) {
				t.setUltimoStatus(tccaStatusDaoImpl.buscarUltimoPorTcca(t));
			}
			
			return tccas;

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTcca(Tcca tcca) {
		try {
			tccaDaoImpl.excluir(tcca);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarNovoStatusDoTcca(Tcca tcca, TccaProjetoTipoStatusEnum statusEnum) {
		try {
			tccaStatusDaoImpl.salvar(tcca, statusEnum);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProdutoExecucao> listarProdutoExecucaoPorTcca(Tcca tcca){
		try {
			return produtoExecucaoDaoImpl.listarPorTcca(tcca);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TccaProjeto> listarTccaProjetoPorTcca(Tcca tcca, Integer first, Integer pageSize) {
		
		try {
			return tccaProjetoDaoImpl.listarPorTcca(tcca, first, pageSize);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProjetoAcao(ProjetoAcao projetoAcao) {
		
		try {
			projetoAcaoDaoImpl.salvar(projetoAcao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProjetoAcaoProduto(ProjetoAcaoProduto projetoAcaoProduto) {
	
		try {
			projetoAcaoProdutoDaoImpl.salvar(projetoAcaoProduto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvarProdutoSaldo(ProdutoSaldo produtoSaldo) {		
		
		try {
			 if (produtoSaldo != null) {
				produtoSaldoDaoImpl.salvar(produtoSaldo);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProjetoUnidadeConservacao(ProjetoUnidadeConservacao projetoUnidadeConservacao) {
		try {
			projetoUnidadeConservacaoDaoImpl.salvar(projetoUnidadeConservacao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTccaRenovacao(TccaHistoricoRenovacaoPrazoValidade tccaHistoricoRenovacaoPrazoValidade) {
		try {
			tccaHistoricoRenovacaoPrazoValidadeDaoImpl.salvar(tccaHistoricoRenovacaoPrazoValidade);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTccaHistoricoReajusteValor(TccaHistoricoReajusteValor tccaHistoricoReajusteValor) {
		try {
			tccaHistoricoReajusteValorDaoImpl.salvar(tccaHistoricoReajusteValor);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Houve um erro ao salvar.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProdutoSaldo buscarUltimoProdutoSaldoPorProjetoAcaoProduto(ProjetoAcaoProduto pap) {
		try {
			return produtoSaldoDaoImpl.buscarUltimoPorProjetoAcaoProduto(pap);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProdutoSaldo> listarUltimosProdutoSaldoPorTccaProjeto(TccaProjeto projeto) {
		try {
			return produtoSaldoDaoImpl.listarUltimosPorTccaProjeto(projeto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProdutoSaldo> listarProdutoSaldoPorProjetoAcaoProduto(ProjetoAcaoProduto pap) {
		try {
			return produtoSaldoDaoImpl.listarPorProjetoAcaoProduto(pap);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TccaSaldo buscarUltimoTccaSaldoPorTcca(Tcca tcca) {
		try {
			return tccaSaldoDaoImpl.buscarUltimoPorTcca(tcca);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeConservacao> listarUnidadesConservacao() {
		try {
			return licencaService.obterListaUnidadeConservacao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProjetoUnidadeConservacao(ProjetoUnidadeConservacao puc) {
		try {
			ProjetoUnidadeConservacao pucExcluir = projetoUnidadeConservacaoDaoImpl.buscarPorProjetoEUnidadeConservacao(puc);
			
			if(!Util.isNullOuVazio(pucExcluir)) projetoUnidadeConservacaoDaoImpl.excluir(pucExcluir);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProjetoAcao(ProjetoAcao pa) {
		try {
			ProjetoAcao paExcluir = projetoAcaoDaoImpl.buscarPorProjetoEAcao(pa);
			
			if(!Util.isNullOuVazio(paExcluir)) projetoAcaoDaoImpl.excluir(paExcluir);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProjetoAcaoProduto(ProjetoAcaoProduto pap) {
		try {
			ProjetoAcaoProduto papExcluir = projetoAcaoProdutoDaoImpl.buscarPorAcaoEProduto(pap);
			
			if(!Util.isNullOuVazio(papExcluir)) projetoAcaoProdutoDaoImpl.excluir(papExcluir);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProjetoAcaoProdutoPorProjetoAcao(ProjetoAcao acao) {
		try {
			if(!Util.isNullOuVazio(acao)) {
				List<ProjetoAcaoProduto> listExcluir = projetoAcaoProdutoDaoImpl.listarPorProjetoAcao(acao);
				
				if(!Util.isNullOuVazio(listExcluir)) {
					for (ProjetoAcaoProduto pap : listExcluir) {
						projetoAcaoProdutoDaoImpl.excluir(pap);
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int countConsultaProjeto(Tcca tcca) {
		try {
			return tccaProjetoDaoImpl.countConsultarProjetosPorTcca(tcca);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarNovoStatusDoProjeto(TccaProjeto projeto, TccaProjetoTipoStatusEnum statusEnum) {
		try {
			projetoStatusDaoImpl.salvar(projeto, statusEnum);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProjetoUnidadeConservacao> listarProjetoUnidadeConservacaoPorProjeto(TccaProjeto projeto) {
		try {
			return projetoUnidadeConservacaoDaoImpl.listarPorProjeto(projeto); 
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProjetoAcao> listarProjetoAcaoPorTccaProjeto(TccaProjeto projeto) {
		try {
			return projetoAcaoDaoImpl.listarPorProjeto(projeto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProjetoAcaoProduto> listarProjetoAcaoProdutoPorProjetoAcao(ProjetoAcao acao) {
		try {
			return projetoAcaoProdutoDaoImpl.listarPorProjetoAcao(acao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProjeto(TccaProjeto projeto) {
		try {
			tccaProjetoDaoImpl.excluir(projeto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProdutoExecucao buscarProdutoExecucaoPorProduto(ProjetoAcaoProduto pap) {
		try {
			return produtoExecucaoDaoImpl.buscarPorProduto(pap);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProdutoExecucao buscarProdutoExecucaoPorID(ProdutoExecucao pe) {
		try {
			return produtoExecucaoDaoImpl.buscarPorID(pe.getIdeProdutoExecucao());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProdutoExecucao(ProdutoExecucao produtoExecucao) {
		try {
			produtoExecucaoDaoImpl.salvar(produtoExecucao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProjetoEmpresaExecutora(ProjetoEmpresaExecutora projetoEmpresaExecutora) {
		try {
			projetoEmpresaExecutoraDaoImpl.salvar(projetoEmpresaExecutora);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProjetoEmpresaExecutora> listarProjetoEmpresaExecutoraPorProjeto(TccaProjeto projeto) {
		try {
			return projetoEmpresaExecutoraDaoImpl.listarPorProjeto(projeto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProjetoEmpresaExecutora buscarProjetoEmpresaExecutoraAtivaPorProjeto(TccaProjeto projeto) {
		try {
			return projetoEmpresaExecutoraDaoImpl.buscarAtivaPorProjeto(projeto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoOrigemDestino> listarTodosTipoOrigemDestino() {
		try {
			return tipoOrigemDestinoDaoImpl.listarTodos();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tcca> listarTCCAPorNumero(String numTCCA) {
		try {
			
			List<Tcca> tccas = tccaDaoImpl.listarPorNumeroTCCA(numTCCA);
			
			for (Tcca t : tccas) {
				t.setUltimoStatus(tccaStatusDaoImpl.buscarUltimoPorTcca(t));
			}
			
			return tccas;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarMovimentacaoFinanceira(MovimentacaoFinanceira mov) {
		try {
			if (mov != null) {
				movimentacaoFinanceiraDaoImpl.salvar(mov);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MovimentacaoFinanceira buscarMovimentacaoFinanceiraPorID(MovimentacaoFinanceira movF) {
		try {
			return movimentacaoFinanceiraDaoImpl.buscarPorID(movF);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MovimentacaoFinanceira> listarMovimentacaoFinanceiraPorProdutoExecucao(ProdutoExecucao pe) {
		try {
			return movimentacaoFinanceiraDaoImpl.listarPorProdutoExecucao(pe);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTccaSaldo(TccaSaldo saldo) {
		try {
			if(saldo != null) {
				tccaSaldoDaoImpl.salvar(saldo);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarMovimentacaoFinanceiraTccaProduto(MovimentacaoFinanceiraTccaProduto mftp) {
		try {
			if(mftp != null) {
				movimentacaoFinanceiraTccaProdutoDaoImpl.salvar(mftp);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarMovimentacoesESaldos(
		MovimentacaoFinanceira movimentacaoFinanceira, MovimentacaoFinanceiraTccaProduto movimentacaoFinanceiraTccaProduto, TccaSaldo tccaSaldo, TccaSaldo tccaSaldoDestino,
			ProdutoSaldo produtoSaldo, ProdutoSaldo produtoDestinoSaldo) {
		
		try {
			salvarMovimentacaoFinanceira(movimentacaoFinanceira);
			salvarMovimentacaoFinanceiraTccaProduto(movimentacaoFinanceiraTccaProduto);
			salvarTccaSaldo(tccaSaldo);
			salvarTccaSaldo(tccaSaldoDestino);
			salvarProdutoSaldo(produtoSaldo);
			salvarProdutoSaldo(produtoDestinoSaldo);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProjetoStatus buscarUltimoProjetoStatusPorTccaProjeto(TccaProjeto tccaProjeto) {
		try {
			return projetoStatusDaoImpl.buscarUltimoPorProjeto(tccaProjeto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MovimentacaoFinanceiraTccaProduto> listarMovimentacaoFinanceiraTccaProdutoPorProjeto(TccaProjeto proj)  {
		try {
			return movimentacaoFinanceiraTccaProdutoDaoImpl.listarPorTccaProjeto(proj); 
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MovimentacaoFinanceiraTccaProduto> listarMovimentacaoFinanceiraTccaProdutoPorTcca(Tcca tcca)  {
		try {
			return movimentacaoFinanceiraTccaProdutoDaoImpl.listarPorTcca(tcca); 
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MovimentacaoFinanceiraTccaProduto> listarMovimentacaoFinanceiraTccaProdutoPorProjetoAcaoProduto(ProjetoAcaoProduto pap)  {
		try {
			return movimentacaoFinanceiraTccaProdutoDaoImpl.listarPorProjetoAcaoProduto(pap);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TccaHistoricoReajusteValor> listarTccaHistoricoReajusteValorPorTcca(Tcca tcca)  {
		try {
			return tccaHistoricoReajusteValorDaoImpl.listarPorTcca(tcca); 
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}