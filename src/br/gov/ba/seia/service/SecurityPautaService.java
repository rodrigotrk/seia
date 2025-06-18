package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.FuncionalidadeAcaoPessoaFisica;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SecurityPautaService {	
	@EJB
	private FuncionalidadeAcaoPessoaFisicaService funcionalidadeAcaoPessoaFisicaService;

	@SuppressWarnings("unchecked")
	public boolean temAcessoConcedidoPeloGestor(int funcionalidade, int acao, Pauta pautaGestor) {
		List<FuncionalidadeAcaoPessoaFisica> permissoes = null;
		String codigoPermissao = "permissaoGestor"+pautaGestor.getIdePauta();
		try{			
			permissoes = (List<FuncionalidadeAcaoPessoaFisica>) SessaoUtil.recuperarObjetoSessao(codigoPermissao);			
		}
		catch(Exception e){	
			Log4jUtil.log(SecurityPautaService.class.getName(), Level.ERROR, e);
		}
		
		try{
			if(permissoes==null){
				Integer ideFuncionarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
				permissoes = funcionalidadeAcaoPessoaFisicaService.listarPermissoes(new PessoaFisica(ideFuncionarioLogado), pautaGestor);
				SessaoUtil.adicionarObjetoSessao(codigoPermissao, permissoes);
			}			
		}
		catch(Exception e){
			return false;			
		}
		for(FuncionalidadeAcaoPessoaFisica fapf : permissoes){
			if(fapf.getIdeFuncionalidade().getIdeFuncionalidade()==funcionalidade
			    && fapf.getIdeAcao().getIdeAcao()==acao){
				return true;
			}
		}
		return false;
	}	
}