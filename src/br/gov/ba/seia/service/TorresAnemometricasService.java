/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.service
 * @autor: diegoraian em 29 de set de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.TorresAnemometricasDAOImpl;
import br.gov.ba.seia.entity.LocalizacaoAtividadeTorre;
import br.gov.ba.seia.entity.TorreAnemometrica;
import br.gov.ba.seia.entity.TorreAnemometricaLocalizacaoAtividadeTorre;
import br.gov.ba.seia.util.Util;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: TorresAnemometricasService.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.service
 * @autor: diegoraian em 29 de set de 2017
 * Objetivo: 	
	
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TorresAnemometricasService {
	
	private static final String  ZONA_DE_AMORTECIMENTO = "Zona de Amortecimento";
	
	@Inject 
	private TorresAnemometricasDAOImpl dao;
	
	@Inject
	private IDAO<TorreAnemometricaLocalizacaoAtividadeTorre> idao ;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTorreAnemometrica(TorreAnemometrica torreAnemometrica) throws Exception{
		
		if(!Util.isNullOuVazio(torreAnemometrica.getListaLocalizacaoAtividadeTorres())){
			
			for(LocalizacaoAtividadeTorre locTorre : torreAnemometrica.getListaLocalizacaoAtividadeTorres()){
				
				torreAnemometrica.setIndPossuiCefir(torreAnemometrica.getIdeCadastroAtividadeNaoSugeitaLic().getIndPossuiCefir());
			
				if(locTorre.getNomLocalizacaoAtividadeTorre().equals(ZONA_DE_AMORTECIMENTO)){
					torreAnemometrica.setIndConservacaoAmortecimento(Boolean.TRUE);
					break;
				}else{
					torreAnemometrica.setIndConservacaoAmortecimento(Boolean.FALSE);
				}
			}
		}else{
			
			torreAnemometrica.setIndPossuiCefir(torreAnemometrica.getIdeCadastroAtividadeNaoSugeitaLic().getIndPossuiCefir());
			torreAnemometrica.setIndConservacaoAmortecimento(Boolean.FALSE);
		}
		
		dao.salvarOuAtualizar(torreAnemometrica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TorreAnemometrica> carregarTorresPorCadastroAtividade(Integer ideCadastroAtividadeNaoSujeitaLic) throws Exception{
		return dao.carregarPorCadastroAtividade(ideCadastroAtividadeNaoSujeitaLic);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTorreAnemometrica(TorreAnemometrica torreAnemometrica) throws Exception{
		torreAnemometrica.setIndExcluido(Boolean.TRUE);
		dao.atualizar(torreAnemometrica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTrorreAnemometrica(TorreAnemometrica torreAnemometrica) throws Exception{
		dao.atualizar(torreAnemometrica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTorreAnemometricaLocalizacaoAtividadeTorre(TorreAnemometricaLocalizacaoAtividadeTorre torreAtividadeTorre) throws Exception{
		idao.salvarOuAtualizar(torreAtividadeTorre);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTorreAnemometricaLocalizacaoAtividadeTorre(TorreAnemometricaLocalizacaoAtividadeTorre torreAtividadeTorre) throws Exception{
		idao.atualizar(torreAtividadeTorre);
	}
}
