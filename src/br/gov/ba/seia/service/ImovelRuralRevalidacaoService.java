package br.gov.ba.seia.service;


import java.util.Collection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ImovelRuralRevalidacaoDAOImpl;
import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.AreaProdutiva;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralRevalidacao;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralRevalidacaoService {
	
	private final int SECAO_LIMITE_IMOVEL = 3;
	private final int SECAO_RESERVA_LEGAL = 6;
	private final int SECAO_APP = 7;
	private final int SECAO_AREA_PRODUTIVA = 8;
	private final int SECAO_VEGETACAO_NATIVA = 9;
	private final int SECAO_AREA_RURAL_CONSOLIDADA = 10;
	private final int SECAO_RPPN = 11;
	
	private String msgSecoes = "";

	@Inject
	ImovelRuralRevalidacaoDAOImpl daoImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRuralRevalidacao filtrarById(ImovelRuralRevalidacao imovelRuralRevalidacao) {
		return daoImpl.filtrarById(imovelRuralRevalidacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(Collection<ImovelRuralRevalidacao> listImovelRuralRevalidacao)  {
		if(!Util.isNullOuVazio(listImovelRuralRevalidacao)){
			for (ImovelRuralRevalidacao imovelRuralRevalidacao : listImovelRuralRevalidacao) {
				daoImpl.salvarOuAtualizar(imovelRuralRevalidacao);			
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ImovelRuralRevalidacao> listarImovelRuralRevalidacaoByImovelRural(ImovelRural imovelRural) {		
		return daoImpl.listarImovelRuralRevalidacaoByImovelRural(imovelRural);
	}

	public void validaImovelRuralRevalidacao(ImovelRural pImovelRural) throws Exception  {
		msgSecoes = "";
		for (ImovelRuralRevalidacao imovelRuralRevalidacao : pImovelRural.getImovelRuralRevalidacaoCollection()) {
			if(!imovelRuralRevalidacao.getIndValidado()) {
				if(verificaInsercaoNovoShape(pImovelRural, imovelRuralRevalidacao)) {
					imovelRuralRevalidacao.setIndValidado(true);
					imovelRuralRevalidacao.setDtcValidacao(new Date());
				}
			}
		}
		
		if(!msgSecoes.isEmpty()) {
			throw new Exception("Prezado usuário, favor corrigir a(s) inconsistência(s) de localização geográfica na(s) seção(ões) a seguir: <br />" + msgSecoes);
		}
	}

	private boolean verificaInsercaoNovoShape(ImovelRural pImovelRural, ImovelRuralRevalidacao imovelRuralRevalidacao) {
		boolean novoShapeInserido = false;
		switch (imovelRuralRevalidacao.getIdeSecaoRevalidacao().getIdeSecaoRevalidacao()) {		
			case SECAO_LIMITE_IMOVEL:
				if(pImovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					novoShapeInserido = true;					
				} else {
					msgSecoes += "<br /> - " + imovelRuralRevalidacao.getIdeSecaoRevalidacao().getDscSecaoRevalidacao();
				}
				break;
			case SECAO_RESERVA_LEGAL:
				if(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					novoShapeInserido = true;					
				} else {
					msgSecoes += "<br /> - " + imovelRuralRevalidacao.getIdeSecaoRevalidacao().getDscSecaoRevalidacao();
				}
				break;
			case SECAO_APP:
				for (App app : pImovelRural.getAppCollection()) {
					if(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().equals(imovelRuralRevalidacao.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())) {
						if(app.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
							novoShapeInserido = true;					
						} else {
							msgSecoes += "<br /> - " + imovelRuralRevalidacao.getIdeSecaoRevalidacao().getDscSecaoRevalidacao();
						}
					}
				}
				break;
			case SECAO_AREA_PRODUTIVA:
				for (AreaProdutiva ap : pImovelRural.getAreaProdutivaCollection()) {
					if(ap.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().equals(imovelRuralRevalidacao.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())) {
						if(ap.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
							novoShapeInserido = true;					
						} else {
							msgSecoes += "<br /> - " + imovelRuralRevalidacao.getIdeSecaoRevalidacao().getDscSecaoRevalidacao();
						}
					}
				}
				break;
			case SECAO_VEGETACAO_NATIVA:
				if(pImovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					novoShapeInserido = true;					
				} else {
					msgSecoes += "<br /> - " + imovelRuralRevalidacao.getIdeSecaoRevalidacao().getDscSecaoRevalidacao();
				}
				break;
			case SECAO_AREA_RURAL_CONSOLIDADA:
				if(pImovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					novoShapeInserido = true;					
				} else {
					msgSecoes += "<br /> - " + imovelRuralRevalidacao.getIdeSecaoRevalidacao().getDscSecaoRevalidacao();
				}
				break;
			case SECAO_RPPN:
				if(pImovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					novoShapeInserido = true;					
				} else {
					msgSecoes += "<br /> - " + imovelRuralRevalidacao.getIdeSecaoRevalidacao().getDscSecaoRevalidacao();
				}
				break;
		}
		return novoShapeInserido;
	}
	
}
