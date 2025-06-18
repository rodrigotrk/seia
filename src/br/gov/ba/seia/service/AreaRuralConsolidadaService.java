package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AreaRuralConsolidada;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.enumerator.TemaGeoseiaEnum;
import br.gov.ba.seia.exception.AreaDeclaradaInvalidaException;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.exception.LocalizacaoGeograficaException;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AreaRuralConsolidadaService {
	
	@Inject
	IDAO<AreaRuralConsolidada> dao;
	
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	public AreaRuralConsolidada listarAreaRuralConsolidadaByImovelRural(ImovelRural imovelRural){
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaRuralConsolidada.class, "areaRuralConsolidada");
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		criteria.addOrder(Order.asc("ideAreaRuralConsolidada"));
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(AreaRuralConsolidada areaRuralConsolidada) {
		this.dao.remover(areaRuralConsolidada);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(AreaRuralConsolidada areaRuralConsolidada)  {
		this.dao.salvarOuAtualizar(areaRuralConsolidada);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AreaRuralConsolidada carregarTudo(AreaRuralConsolidada areaRuralConsolidada) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaRuralConsolidada.class, "AreaRuralConsolidada");
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideImovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideAreaRuralConsolidada", areaRuralConsolidada.getIdeAreaRuralConsolidada()));
		criteria.addOrder(Order.asc("ideAreaRuralConsolidada"));
		return dao.buscarPorCriteria(criteria);
	}

	public void validaAreaRuralConsolidadaParaFinalizacao(ImovelRural imovelRural) throws Exception {
		if(!Util.isNull(imovelRural.getIndAreaRuralConsolidada()) && imovelRural.getIndAreaRuralConsolidada()) {
			if(Util.isNullOuVazio(imovelRural.getIdeAreaRuralConsolidada())) {
				throw new SeiaException("Por favor cadastre a Área rural consolidada do imóvel");
			}
			validaTheGeomAreaRuralConsolidada(imovelRural);
		}
	}

	private void validaTheGeomAreaRuralConsolidada(ImovelRural pImovelRural) throws Exception {
		try{
			String geometriaIm = null;
			String geometriaArc = null;
			
			if(pImovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
				geometriaIm = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), null);
			} else {
				geometriaIm = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
			
			if(pImovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica() == null) {
				throw new CampoObrigatorioException("Não existe localização geográfica da Área Rural Consolidada.");
			}

			if(!localizacaoGeograficaService.existeTheGeom(pImovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica())){
				throw new CampoObrigatorioException("Favor importar o shapefile da Área Rural Consolidada.");
				//Obtem a geometria da Área Rural Consolidada através do arquivo shape temporário ou diretamente do banco
			}
				
			if(pImovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
				geometriaArc = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.AREA_RURAL_CONSOLIDADA.getId(), null);
			} else {
				geometriaArc = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
			
			//Aplica validação de área e validação do tema em relação ao limite do imóvel
			validacaoGeoSeiaService.validarAreaDeclaradaShapeTemporario(pImovelRural.getIdeAreaRuralConsolidada().getValArea(), geometriaArc);
			validacaoGeoSeiaService.validarLocalizacaoGeografica(null, geometriaArc, null, geometriaIm);
			
		}catch(AreaDeclaradaInvalidaException a) {
			throw new SeiaException("A área informada da Área Rural Consolidada ("+a.getAreaDeclarada()+" ha) não confere com a área do shapefile importado ("+a.getAreaCalculada()+" ha).");
		}catch(LocalizacaoGeograficaException l) {
			throw new SeiaException("A geometria da Área Rural Consolidada não está dentro do Limite do Imóvel Rural cadastrado.");
		} catch (CampoObrigatorioException c) {
			throw new SeiaException(c.getMessage());
		}catch (Exception e) {
			throw new SeiaException("Erro na validação de geometria da Área Rural Consolidada, contate o administrador do sistema.");
		}
	}
	


}
