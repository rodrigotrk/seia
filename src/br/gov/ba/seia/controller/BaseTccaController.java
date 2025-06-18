package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import br.gov.ba.seia.entity.ProdutoExecucao;
import br.gov.ba.seia.entity.ProdutoSaldo;
import br.gov.ba.seia.entity.ProjetoAcao;
import br.gov.ba.seia.entity.ProjetoAcaoProduto;
import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.entity.UnidadeConservacao;
import br.gov.ba.seia.facade.BaseTccaFacade;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;

public abstract class BaseTccaController {
	
	@EJB
	protected BaseTccaFacade facade;
	
	protected Tcca tccaDestinoRecurso;
	protected TccaProjeto projetoDestinoRecurso;
	protected ProjetoAcao acaoDestinoRecurso;
	protected ProjetoAcaoProduto produtoDestinoRecurso;
	
	public TccaProjeto calcularValoresExecutadosNoProjeto(TccaProjeto projeto) {
		
		if(!Util.isNullOuVazio(projeto.getProjetoAcaoCollection())) {
			for (ProjetoAcao acao : projeto.getProjetoAcaoCollection()) {
				for (ProjetoAcaoProduto prod : acao.getProjetoAcaoProdutoCollection()) {
					
					ProdutoExecucao pe = facade.buscarProdutoExecucaoPorProduto(prod);
					
					if(!Util.isNullOuVazio(pe)) {
						prod.setValorPrevisto((pe.getValPrevisto()));
						prod.setValorContratado((pe.getValContratado()));
						prod.setValorExecutado((pe.getValExecutado()));
					} else {
						prod.setValorPrevisto(BigDecimal.ZERO);
						prod.setValorContratado(BigDecimal.ZERO);
						prod.setValorExecutado(BigDecimal.ZERO);
					}
				}
			}
			
			projeto.setValorPrevisto(BigDecimal.ZERO);
			projeto.setValorContratado(BigDecimal.ZERO);
			projeto.setValorExecutado(BigDecimal.ZERO);
			
			for (ProjetoAcao acao : projeto.getProjetoAcaoCollection()) {
				acao.setValorPrevisto(BigDecimal.ZERO);
				acao.setValorContratado(BigDecimal.ZERO);
				acao.setValorExecutado(BigDecimal.ZERO);
				
				for (ProjetoAcaoProduto prod : acao.getProjetoAcaoProdutoCollection()) {
					if(!Util.isNullOuVazio(prod.getValorPrevisto())) {
						acao.setValorPrevisto(acao.getValorPrevisto().add(prod.getValorPrevisto()));
					}
					if(!Util.isNullOuVazio(prod.getValorContratado())) {
						acao.setValorContratado(acao.getValorContratado().add(prod.getValorContratado()));
					}
					if(!Util.isNullOuVazio(prod.getValorExecutado())) {
						acao.setValorExecutado(acao.getValorExecutado().add(prod.getValorExecutado()));
					}
				}
				
				projeto.setValorPrevisto(projeto.getValorPrevisto().add(acao.getValorPrevisto()));
				projeto.setValorContratado(projeto.getValorContratado().add(acao.getValorContratado()));
				projeto.setValorExecutado(projeto.getValorExecutado().add(acao.getValorExecutado()));
			}
		}
		
		return projeto;
	}
	
	public TccaProjeto calcularSaldosDoProjeto(TccaProjeto projeto) {
		
		projeto.setProjetoAcaoCollection(facade.listarProjetoAcaoPorTccaProjeto(projeto));
		
		for (ProjetoAcao acao : projeto.getProjetoAcaoCollection()) {
			acao.setProjetoAcaoProdutoCollection(facade.listarProjetoAcaoProdutoPorProjetoAcao(acao));
			
			for (ProjetoAcaoProduto produto : acao.getProjetoAcaoProdutoCollection()) {
				
				ProdutoSaldo saldo = facade.buscarUltimoProdutoSaldoPorProjetoAcaoProduto(produto);
				
				if(!Util.isNullOuVazio(saldo)) {
					
					//PROJETO
					if(projeto.getValorSaldo() == null) projeto.setValorSaldo(BigDecimal.ZERO);
					if(saldo.getValSaldoProduto() != null) projeto.setValorSaldo(projeto.getValorSaldo().add(saldo.getValSaldoProduto()));
					
					if(projeto.getValorRemanejado() == null) projeto.setValorRemanejado(BigDecimal.ZERO);
					if(saldo.getValSaldoRemanejado() != null) projeto.setValorRemanejado(projeto.getValorRemanejado().add(saldo.getValSaldoRemanejado()));
					
					if(projeto.getValorSuplementado() == null) projeto.setValorSuplementado(BigDecimal.ZERO);
					if(saldo.getValSaldoSuplementado() != null) projeto.setValorSuplementado(projeto.getValorSuplementado().add(saldo.getValSaldoSuplementado()));
					
					//AÇÃO
					if(acao.getValorSaldo() == null) acao.setValorSaldo(BigDecimal.ZERO);
					if(saldo.getValSaldoProduto() != null) acao.setValorSaldo(acao.getValorSaldo().add(saldo.getValSaldoProduto()));
					
					if(acao.getValorRemanejado() == null) acao.setValorRemanejado(BigDecimal.ZERO);
					if(saldo.getValSaldoRemanejado() != null) acao.setValorRemanejado(acao.getValorRemanejado().add(saldo.getValSaldoRemanejado()));
					
					if(acao.getValorSuplementado() == null) acao.setValorSuplementado(BigDecimal.ZERO);
					if(saldo.getValSaldoSuplementado() != null) acao.setValorSuplementado(acao.getValorSuplementado().add(saldo.getValSaldoSuplementado()));
				}
				
				produto.setUltimoProdutoSaldo(saldo);
			}
		}
		
		return projeto;
	}

	public void buscarTccaPorNumero(Tcca tcca) {

		if(!Util.isNullOuVazio(tcca) && tccaDestinoRecurso != null && !Util.isNullOuVazio(tccaDestinoRecurso.getNumTcca())) {
			
			if(tcca.getNumTcca().equals(tccaDestinoRecurso.getNumTcca())) {
				JsfUtil.addWarnMessage("O número do TCCA de destino é igual ao número do TCCA de origem. Verifique o número do TCCA e tente novamente.");
				tccaDestinoRecurso = new Tcca();
			} else {
				
				List<Tcca> listTcca = facade.listarTCCAPorNumero(tccaDestinoRecurso.getNumTcca());
				
				if(!Util.isNullOuVazio(listTcca)) {
					
					if(listTcca.size() > 1) {
						JsfUtil.addWarnMessage("Foram encontrados mais de um TCCA para esse número, favor digitar o número do TCCA corretamente.");
						tccaDestinoRecurso = new Tcca();
					} else {
						tccaDestinoRecurso = listTcca.get(0);
						tccaDestinoRecurso.setUltimoSaldo(facade.buscarUltimoTccaSaldoPorTcca(tccaDestinoRecurso));
						
						if(tccaDestinoRecurso.getUltimoStatus() == null
							|| !tccaDestinoRecurso.isStatusVigente() && !tccaDestinoRecurso.isStatusEmExecucao() && !tccaDestinoRecurso.isStatusCancelado()) {
							
							JsfUtil.addWarnMessage("Este TCCA não se encontra nos status \"Vigente\", \"Em Execução\" ou \"Cancelado\".");
							tccaDestinoRecurso = new Tcca();
						}
					}
				} else {
					JsfUtil.addWarnMessage("Não foi encontrado nenhum TCCA para esse número, favor digitar o número do TCCA corretamente.");
					tccaDestinoRecurso = new Tcca();
				}
			}
		}
	}
	
	public void changeProjetoDestinoRecurso() {
		acaoDestinoRecurso = null;
		produtoDestinoRecurso = null;
	}
	
	public void changeAcaoDestinoRecurso() {
		produtoDestinoRecurso = null;
	}
	
	public String isValorNegativo(BigDecimal valor) {
		if(valor.compareTo(BigDecimal.ZERO) == -1) {
			return "red";
		} else {
			return "black";
		}
	}
	
	public String isValorNegativoInvertido(BigDecimal valor) {
		if(valor.compareTo(BigDecimal.ZERO) == 1) {
			return "red";
		} else {
			return "black";
		}
	}

	public boolean validarFinalizacaoProjeto(TccaProjeto projeto, List<UnidadeConservacao> listUnidadesConservacaoSelecionadas) {
		if(Util.isNullOuVazio(projeto.getNomProjeto())) {
			return false;
		}
		
		if(Util.isNullOuVazio(projeto.getProjetoUnidadeConservacaoCollection()) 
				&& Util.isNullOuVazio(listUnidadesConservacaoSelecionadas)) {
			
			return false;
		}
		
		if(Util.isNullOuVazio(projeto.getProjetoAcaoCollection())) {
			return false;
		} else {
			for (ProjetoAcao pa : projeto.getProjetoAcaoCollection()) {
				if(Util.isNullOuVazio(pa.getProjetoAcaoProdutoCollection())) {
					return false;
				}
			}
		}
		
		return true;
	}

	/**
	 * RN 305
	 */
	public List<TccaProjeto> listaProjetosValidosParaMovimentacao(Tcca tcca, TccaProjeto proj) {
		
		List<TccaProjeto> listaValidos = new ArrayList<TccaProjeto>();
		
		if(!Util.isNullOuVazio(tcca) && !Util.isNullOuVazio(tcca.getTccaProjetoCollection())) {
			
			for (TccaProjeto tp : tcca.getTccaProjetoCollection()) {
				
				if(tp.isStatusPrevisto() || tp.isStatusEmExecucao() || tp.isStatusConcluido() || tp.isStatusCancelado()) {
					
					if(Util.isNullOuVazio(proj)) {
						listaValidos.add(tp);
					} else if(!tp.getIdeTccaProjeto().equals(proj.getIdeTccaProjeto())) {
						listaValidos.add(tp);
					}
				}
			}
		}
		
		
		return listaValidos;
	}
	

	
	public Tcca getTccaDestinoRecurso() {
		return tccaDestinoRecurso;
	}

	public void setTccaDestinoRecurso(Tcca tccaDestinoRecurso) {
		this.tccaDestinoRecurso = tccaDestinoRecurso;
	}

	public BaseTccaFacade getFacade() {
		return facade;
	}

	public void setFacade(BaseTccaFacade facade) {
		this.facade = facade;
	}

	public TccaProjeto getProjetoDestinoRecurso() {
		return projetoDestinoRecurso;
	}

	public void setProjetoDestinoRecurso(TccaProjeto projetoDestinoRecurso) {
		this.projetoDestinoRecurso = projetoDestinoRecurso;
	}

	public ProjetoAcao getAcaoDestinoRecurso() {
		return acaoDestinoRecurso;
	}

	public void setAcaoDestinoRecurso(ProjetoAcao acaoDestinoRecurso) {
		this.acaoDestinoRecurso = acaoDestinoRecurso;
	}

	public ProjetoAcaoProduto getProdutoDestinoRecurso() {
		return produtoDestinoRecurso;
	}

	public void setProdutoDestinoRecurso(ProjetoAcaoProduto produtoDestinoRecurso) {
		this.produtoDestinoRecurso = produtoDestinoRecurso;
	}
}