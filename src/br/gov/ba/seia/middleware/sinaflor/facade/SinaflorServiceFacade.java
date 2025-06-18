package br.gov.ba.seia.middleware.sinaflor.facade;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.ProcessoSinaflorDAOImpl;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoSinaflor;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.middleware.sinaflor.model.EmpreendimentoSaida;
import br.gov.ba.seia.middleware.sinaflor.model.PessoaSaida;
import br.gov.ba.seia.middleware.sinaflor.model.TokenSaida;
import br.gov.ba.seia.middleware.sinaflor.service.SinaflorService;
import br.gov.ba.seia.service.VwConsultaProcessoService;
import br.gov.ba.seia.util.Util;
/**
 * Classe de serviço Sinaflor Facede
 * @author 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SinaflorServiceFacade {
	
	@EJB
	private SinaflorService sinaflorService;
	@EJB
	private VwConsultaProcessoService vwConsultaProcessoService;
	@EJB
	private ProcessoSinaflorDAOImpl processoSinaflorDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void jobSinaflor() {
		try {
			Collection<VwConsultaProcesso> listaProcessoSincronizacaoSinaflor = listarProcessoSincronizacaoSinaflor();
			if(!Util.isNullOuVazio(listaProcessoSincronizacaoSinaflor)) {
				for(VwConsultaProcesso processoSEIA : listaProcessoSincronizacaoSinaflor) {
					sincronizarCadastro(processoSEIA);
				}
			}
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean sincronizarCadastro(VwConsultaProcesso processoSEIA) throws Exception {
		
		ProcessoSinaflor processoSinaflor = new ProcessoSinaflor();
		processoSinaflor.setDtcSincronizacao(new Date());
		processoSinaflor.setIdeProcesso(new Processo(processoSEIA.getIdeProcesso()));
		
		List<String> log = processoSinaflor.iniciarLog();
		
		try {
			
			sinaflorService.autenticar();
			
			Pessoa pessoaSEIA =  sinaflorService.carregarPessoaSEIA(processoSEIA);
			PessoaSaida pessoaCadastradaSinaflor = sinaflorService.validarPessoaSINAFLOR(pessoaSEIA.getCpfCnpj(), log);
			
			TokenSaida token = sinaflorService.gerarTokenSINAFLOR(log);
			processoSinaflor.setToken(token.getHash());
			
			EmpreendimentoSaida empreendimentoCadastrado = sinaflorService.cadastrarEmpreendimento(token, pessoaCadastradaSinaflor, pessoaSEIA, processoSEIA, log);
			
			sinaflorService.cadastrarProjeto(token, empreendimentoCadastrado, processoSEIA, log);
			
			processoSinaflor.setIndConcluido(true);
			
		} catch (Exception e) {
			log.add(e.getMessage());
			System.out.println(" sinaflor inicio stack trace ");
			e.printStackTrace();
			System.out.println(" sinaflor fim stack trace ");
			System.out.println("--------");
			System.out.println(" sinaflor inicio log ");
			System.out.println(log);
			System.out.println(" sinaflor fim log");
			
			processoSinaflor.setIndConcluido(false);
			
		} finally {
			processoSinaflor.finalizarLog();
			sinaflorService.finalizar();
			processoSinaflorDAO.salvar(processoSinaflor);
		}
		
		return processoSinaflor.getIndConcluido();
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<VwConsultaProcesso> listarProcessoSincronizacaoSinaflor() throws Exception {
		return vwConsultaProcessoService.listarProcessoSincronizacaoSinaflor();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoSinaflor> listarProcessoSinaflor(Map<String, Object> params, Integer first, Integer pageSize) throws Exception {
		return processoSinaflorDAO.listarProcessoSinaflor(params, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer listarProcessoSinaflorCount(Map<String, Object> params) throws Exception {
		return processoSinaflorDAO.listarProcessoSinaflorCount(params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso buscarVwConsultaProcessoPorIdeProcesso(Integer ideProcesso) throws Exception {
		return vwConsultaProcessoService.buscarVwConsultaProcessoPorIdeProcesso(ideProcesso, false);
	}

}