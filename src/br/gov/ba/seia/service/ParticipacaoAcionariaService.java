package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.ParticipacaoAcionariaDAOImpl;
import br.gov.ba.seia.entity.ParticipacaoAcionaria;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.enumerator.NaturezaJuridicaEnum;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ParticipacaoAcionariaService {

	@Inject
	private ParticipacaoAcionariaDAOImpl participacaoAcionariaDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarParticipacaoAcionaria(ParticipacaoAcionaria participacaoAcionaria)  {
		participacaoAcionariaDAOImpl.salvarParticipacaoAcionaria(participacaoAcionaria);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<ParticipacaoAcionaria> buscaParticipacaoAcionariaPorPessoaJuridica(PessoaJuridica pessoajuridica) {
		return participacaoAcionariaDAOImpl.buscaParticipacaoAcionariaPorPessoaJuridica(pessoajuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirParticipacaoAcionaria(ParticipacaoAcionaria participacaoAcionaria) {
		participacaoAcionariaDAOImpl.excluirParticipacaoAcionaria(participacaoAcionaria);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Boolean validateParticipacaoAcionaria(ParticipacaoAcionaria participacaoAcionaria, PessoaJuridica pessoaJuridica, Boolean editMode) {

		Boolean retornoValidate = Boolean.TRUE;
		
		BigDecimal totalParticipacoesAcionarias = null;
		
		totalParticipacoesAcionarias = calcularTotalParticipacaoAcionariaConsiderandoValorAtual(participacaoAcionaria, pessoaJuridica, editMode);
		
		if ((totalParticipacoesAcionarias.compareTo(new BigDecimal(100)) == 1)) {
			retornoValidate = Boolean.FALSE;
		} 
		
		return retornoValidate;
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Boolean validateParticipacaoAcionariaRequerimentoUnico(PessoaJuridica pessoaJuridica) {
		
		Boolean retornoValidate = Boolean.TRUE;
		
		BigDecimal totalParticipacoesAcionarias = calcularTotalParticipacaoAcionaria(pessoaJuridica);
		
		if (!pessoaJuridica.getIdeNaturezaJuridica().getIdeNaturezaJuridica().equals(NaturezaJuridicaEnum.SOCIEDADE_ANONIMA.getId()) 
				&& (totalParticipacoesAcionarias.compareTo(new BigDecimal(0)) < 1)) {
			
			retornoValidate = Boolean.FALSE;
		}
		
		return retornoValidate;
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public BigDecimal calcularTotalParticipacaoAcionariaConsiderandoValorAtual(ParticipacaoAcionaria participacaoAcionaria, PessoaJuridica pessoaJuridica, Boolean editMode) {
		BigDecimal totalParticipacoesAcionarias = new BigDecimal(0);
		try {
			if(editMode) {
				List<ParticipacaoAcionaria> listaParticipacaoAcionaria = (List<ParticipacaoAcionaria>)this.buscaParticipacaoAcionariaPorPessoaJuridica(pessoaJuridica);
				
				for (ParticipacaoAcionaria participacaoAcionaria2 : listaParticipacaoAcionaria) {
					
					if(participacaoAcionaria2.getIdeParticipacaoAcionaria().compareTo(participacaoAcionaria.getIdeParticipacaoAcionaria()) != 0) {
						totalParticipacoesAcionarias = participacaoAcionaria2.getPrcParticipacaoAcionaria().add(totalParticipacoesAcionarias);
					}
				}
				
				if (!Util.isNull(participacaoAcionaria.getPrcParticipacaoAcionaria())) {
					totalParticipacoesAcionarias = totalParticipacoesAcionarias.add(participacaoAcionaria.getPrcParticipacaoAcionaria());
				}
			} else {
				List<ParticipacaoAcionaria> listaParticipacaoAcionaria = (List<ParticipacaoAcionaria>)this.buscaParticipacaoAcionariaPorPessoaJuridica(pessoaJuridica);
				
				for (ParticipacaoAcionaria participacaoAcionaria2 : listaParticipacaoAcionaria) {
					totalParticipacoesAcionarias = participacaoAcionaria2.getPrcParticipacaoAcionaria().add(totalParticipacoesAcionarias);
				}
				
				if (!Util.isNull(participacaoAcionaria.getPrcParticipacaoAcionaria())) {
					totalParticipacoesAcionarias = totalParticipacoesAcionarias.add(participacaoAcionaria.getPrcParticipacaoAcionaria());
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return totalParticipacoesAcionarias;
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public BigDecimal calcularTotalParticipacaoAcionaria(PessoaJuridica pessoaJuridica) {
		BigDecimal totalParticipacoesAcionarias = new BigDecimal(0);
		try {
			 List<ParticipacaoAcionaria> listaParticipacaoAcionaria = (List<ParticipacaoAcionaria>)this.buscaParticipacaoAcionariaPorPessoaJuridica(pessoaJuridica);
			for (ParticipacaoAcionaria participacaoAcionaria : listaParticipacaoAcionaria) {
				totalParticipacoesAcionarias = participacaoAcionaria.getPrcParticipacaoAcionaria().add(totalParticipacoesAcionarias);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return totalParticipacoesAcionarias;
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ParticipacaoAcionaria buscarParticipacaoAcionariaPorID(Integer ideParticipacaoAcionaria) {
		return participacaoAcionariaDAOImpl.buscarParticipacaoAcionariaPorID(ideParticipacaoAcionaria);
	}
	
	public boolean isParticipanteAcionarioInativado(int idePessoaFisica, int idePessoaJuridica) {
		return participacaoAcionariaDAOImpl.isParticipanteAcionarioInativado(idePessoaFisica, idePessoaJuridica);

	}
}
