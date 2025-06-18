package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dto.UsuarioExternoDTO;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;



@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaFisicaJuridicaReativarService {
	
	@Inject
	IDAO<PessoaFisica> pessoaFisicaDAO;
	@EJB
	private PessoaService pessoaService;
	@Inject
	IDAO<Pessoa> pessoaDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarPessoa(UsuarioExternoDTO usuarioReativar){
		
		Pessoa pessoa = pessoaService.carregarGet(usuarioReativar.getPessoaFisica().getIdePessoaFisica());
		
		pessoa.setIndExcluido(false);
		pessoa.setDtcExclusao(null);
		pessoaDAO.atualizar(pessoa);
	}
	
	public Collection<PessoaFisica> filtrarListaPessoasFisicasExcluidas(PessoaFisica pPessoaFisica) {		
					
		StringBuilder sql = new StringBuilder("SELECT pf.nom_pessoa, " +
				" pf.num_cpf, pf.ide_pessoa_fisica " +
				" FROM pessoa p " +
				" INNER JOIN pessoa_fisica pf ON pf.ide_pessoa_fisica = p.ide_pessoa " +
				" WHERE p.ind_excluido= TRUE ");
		
		if(!Util.isNullOuVazio(pPessoaFisica.getNomPessoa())) sql.append(" AND pf.nom_pessoa like '%" + pPessoaFisica.getNomPessoa() + "%'");
		if(!Util.isNullOuVazio(pPessoaFisica.getNumCpf())) sql.append(" AND pf.num_cpf = '" + pPessoaFisica.getNumCpf() + "'");
		
		sql.append(" ORDER BY pf.nom_pessoa");
				
				
		List<PessoaFisica> retorno = new ArrayList<PessoaFisica>();
		
		try {
			
			List<PessoaFisica>  lista = pessoaFisicaDAO.buscarPorNativeQuery(sql.toString(), null);
			
			for (Object object : lista.toArray()) {
				Object[] resultElement = (Object[]) object;
				PessoaFisica pessoaFisica = new PessoaFisica();
				int i = 0;
				
				pessoaFisica.setNomPessoa((String) resultElement[i]);
				pessoaFisica.setNumCpf((String) resultElement[++i]);
				pessoaFisica.setIdePessoaFisica((Integer) resultElement[++i]); 
				
				retorno.add(pessoaFisica);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
			return retorno;
		}

}
