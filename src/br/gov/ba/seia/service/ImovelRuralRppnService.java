package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.ImovelRuralDAOImpl;
import br.gov.ba.seia.dao.ImovelRuralRppnDAOImpl;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralRppn;
import br.gov.ba.seia.enumerator.TemaGeoseiaEnum;
import br.gov.ba.seia.exception.AreaDeclaradaInvalidaException;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.exception.LocalizacaoGeograficaException;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralRppnService {

	@Inject
	ImovelRuralRppnDAOImpl daoImpl;
	
	@Inject
	ImovelRuralDAOImpl imovelRuralImplDao;

	@Inject
	IDAO<ImovelRuralRppn> dao;
	
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRuralRppn buscarImoveRuralRppnByImovelRural(ImovelRural imovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRuralRppn.class);
		criteria.createAlias("ideImovelRural", "imovelRural", JoinType.INNER_JOIN);
		criteria.createAlias("ideLocalizacaoGeografica", "locGeo", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("locGeo.dadoGeograficoCollection", "dadoGeo", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		return dao.buscarPorCriteria(criteria);
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ImovelRuralRppn imovelRuralRppn)  {
		this.daoImpl.salvar(imovelRuralRppn);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ImovelRuralRppn imovelRuralRppn)  {
		this.daoImpl.atualizar(imovelRuralRppn);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ImovelRuralRppn imovelRuralRppn)  {
		this.daoImpl.remover(imovelRuralRppn);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirImovelRuralRppnPorImovelRural(ImovelRural ideImovelRural)  {
		daoImpl.excluirImovelRuralRppnPorImovelRural(ideImovelRural);		
	}

	public void validaRppnParaFinalizacao(ImovelRural imovelRural) throws Exception  {
		if(!Util.isNull(imovelRural.getIndRppn()) && imovelRural.getIndRppn()) {
			if(Util.isNullOuVazio(imovelRural.getIdeImovelRuralRppn())){
				throw new Exception("Por favor complete as informações da sua RPPN.");
			}
			validaTheGeomRppn(imovelRural);
		}
	}
	
	private void validaTheGeomRppn(ImovelRural pImovelRural) throws Exception  {
		try{
			String geometriaIm = null;
			String geometriaRppn = null;

			if(pImovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
				geometriaIm = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), null);
			} else {
				geometriaIm = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
						
			if(pImovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica() == null) {
				if(pImovelRural.isImovelBNDES() && pImovelRural.isImovelCDA()) {
					throw new CampoObrigatorioException("Não existe localização geográfica da RPPN.");
				}
			}
			if(!localizacaoGeograficaService.existeTheGeom(pImovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica())){
				if(pImovelRural.isImovelBNDES() && pImovelRural.isImovelCDA()) {
					throw new CampoObrigatorioException("Favor importar o shapefile da RPPN.");
				}
			}else{
				//Obtem a geometria da RPPN através do arquivo shape temporário ou diretamente do banco
				
				if(pImovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					geometriaRppn = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.RPPN.getId(), null);
				} else {
					geometriaRppn = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}
				
				//Aplica validação de área e validação do tema em relação ao limite do imóvel
				validacaoGeoSeiaService.validarAreaDeclaradaShapeTemporario(pImovelRural.getIdeImovelRuralRppn().getValArea(), geometriaRppn);
				validacaoGeoSeiaService.validarLocalizacaoGeografica(null, geometriaRppn, null, geometriaIm);
			}
			
		} catch(AreaDeclaradaInvalidaException a) {
			throw new Exception("A área informada da RPPN ("+a.getAreaDeclarada()+" ha) não confere com a área do shapefile importado ("+a.getAreaCalculada()+" ha).");
		} catch(LocalizacaoGeograficaException l) {
			throw new Exception("A geometria da RPPN não está dentro do Limite do Imóvel Rural cadastrado.");
		} catch (CampoObrigatorioException c) {
			throw new Exception(c.getMessage());
		} catch (Exception e) {
			throw new Exception("Erro na validação de geometria da RPPN, contate o administrador do sistema.");
		}
	}
}
