package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;

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
import br.gov.ba.seia.dao.VegetacaoNativaDAOImpl;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.TipoFinalidadeVegetacaoNativa;
import br.gov.ba.seia.entity.VegetacaoNativa;
import br.gov.ba.seia.entity.VegetacaoNativaFinalidade;
import br.gov.ba.seia.enumerator.TemaGeoseiaEnum;
import br.gov.ba.seia.exception.AreaDeclaradaInvalidaException;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.exception.LocalizacaoGeograficaException;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class VegetacaoNativaService {

	@Inject
	VegetacaoNativaDAOImpl daoImpl;

	@Inject
	IDAO<VegetacaoNativa> dao;
	@Inject
	IDAO<VegetacaoNativaFinalidade> daoVegetacaoNativaFinalidade;
	
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;

	public VegetacaoNativa listarVegetacaoNativaByImovelRural(ImovelRural imovelRural) throws Exception{
		VegetacaoNativa vegetacaoNativa = new VegetacaoNativa();
		DetachedCriteria criteria = DetachedCriteria.forClass(VegetacaoNativa.class, "vegetacaoNativa");
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		criteria.addOrder(Order.asc("ideVegetacaoNativa"));
		vegetacaoNativa = dao.buscarPorCriteria(criteria);
		if(!Util.isNullOuVazio(vegetacaoNativa)){
			vegetacaoNativa.setVegetacaoNativaFinalidadeCollection(listarVegetacaoNativaFinalidadeByVegetacaoNativa(vegetacaoNativa));
			vegetacaoNativa.setTipoFinalidadeVegetacaoNativaCollection(new ArrayList<TipoFinalidadeVegetacaoNativa>());
			for (VegetacaoNativaFinalidade vegetacaoNativaFinalidade : vegetacaoNativa.getVegetacaoNativaFinalidadeCollection()) {
				vegetacaoNativa.getTipoFinalidadeVegetacaoNativaCollection().add(vegetacaoNativaFinalidade.getIdeTipoFinalidadeVegetacaoNativa());
			}
		}
		return vegetacaoNativa;
	}
	
	public Collection<VegetacaoNativaFinalidade> listarVegetacaoNativaFinalidadeByVegetacaoNativa(VegetacaoNativa pVegetacaoNativa) throws Exception{
		Collection<VegetacaoNativaFinalidade> listVegetacaoNativaFinalidade = new ArrayList<VegetacaoNativaFinalidade>();
		DetachedCriteria criteria = DetachedCriteria.forClass(VegetacaoNativaFinalidade.class, "vegetacaoNativaFinalidade");
		criteria.add(Restrictions.eq("ideVegetacaoNativa", pVegetacaoNativa));		
		listVegetacaoNativaFinalidade = daoVegetacaoNativaFinalidade.listarPorCriteria(criteria, Order.asc("ideVegetacaoNativaFinalidade"));
		return listVegetacaoNativaFinalidade;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(VegetacaoNativa pVegetacaoNativa) throws Exception {
		this.daoImpl.salvar(pVegetacaoNativa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(VegetacaoNativa pVegetacaoNativa) throws Exception {
		this.daoImpl.salvarAllVegetacaoNativaFinalidade(pVegetacaoNativa);
		this.daoImpl.atualizar(pVegetacaoNativa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VegetacaoNativa carregarTudo(VegetacaoNativa pVegetacaoNativa) throws Exception {
		VegetacaoNativa lVegetacaoNativa = new VegetacaoNativa();
		DetachedCriteria criteria = DetachedCriteria.forClass(VegetacaoNativa.class, "VegetacaoNativa");
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideImovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideVegetacaoNativa", pVegetacaoNativa.getIdeVegetacaoNativa()));
		criteria.addOrder(Order.asc("ideVegetacaoNativa"));
		lVegetacaoNativa = dao.buscarPorCriteria(criteria);
		return lVegetacaoNativa;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(VegetacaoNativa pVegetacaoNativa) throws Exception {
		this.daoImpl.remover(pVegetacaoNativa);
	}

	public void validaVegetacaoNativaParaFinalizacao(ImovelRural imovelRural) throws Exception {
		if(!Util.isNull(imovelRural.getIndVegetacaoNativa()) && imovelRural.getIndVegetacaoNativa()) {
			if(Util.isNullOuVazio(imovelRural.getVegetacaoNativa())){
				throw new Exception("Por favor complete as informações da sua Vegetação Nativa.");
			}
			validaTheGeomVegetacaoNativa(imovelRural);			
		} else if(imovelRural.isCedeAreaParaCompensacaoRl()) {
			throw new Exception(Util.getString("cefir_msg_A018"));
		}
	}
	
	private void validaTheGeomVegetacaoNativa(ImovelRural pImovelRural) throws Exception {
		try{
			String geometriaIm = null;
			String geometriaRl = null;
			String geometriaVn = null;
			
			if(ContextoUtil.getContexto().isPCT()) {
				if(pImovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getNovosArquivosShapeImportados()) {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId(), null);
				} else {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica());
				}
			}else {
				
				if(pImovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), null);
				} else {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}
			}
			
			if(!Util.isNullOuVazio(pImovelRural.getReservaLegal()) && !Util.isNull(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica())) {
				if(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()){
					geometriaRl = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.RESERVA_LEGAL.getId(), null);
				} else {
					geometriaRl = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}
			}
			
			if(pImovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica() == null){
				throw new CampoObrigatorioException("Não existe localização geográfica da Vegetação Nativa.");
			}
			if(!localizacaoGeograficaService.existeTheGeom(pImovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica())){
				throw new CampoObrigatorioException("Favor importar o shapefile da Vegetação Nativa.");
			}
			if(pImovelRural.getVegetacaoNativa().getTipoFinalidadeVegetacaoNativaCollection().size() == 0){
				throw new CampoObrigatorioException("Deve ser informado o que você deseja fazer com a área excedente de vegetação nativa!");
			}else{
				//Se a geometria da vegetação nativa não estiver carregada, obtem através do arquivo shape temporário ou diretamente do banco
				if(Util.isNullOuVazio(geometriaVn)){
					if(pImovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
						geometriaVn = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.VEGETACAO_NATIVA.getId(), null);
					} else {
						geometriaVn = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
					}
				}
				
				//Aplica validação de área e validação do tema em relação ao limite do imóvel
				validacaoGeoSeiaService.validarAreaDeclaradaShapeTemporario(pImovelRural.getVegetacaoNativa().getValArea(), geometriaVn);
				validacaoGeoSeiaService.validarLocalizacaoGeografica(null, geometriaVn, null, geometriaIm);
				
				//Verifica se a Reserva Legal é do tipo No Próprio Imóvel (2) ou Em Compensação (3)
				if(!Util.isNullOuVazio(pImovelRural.getReservaLegal()) && !Util.isNullOuVazio(pImovelRural.getReservaLegal().getIdeTipoArl()) && (pImovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(2) || pImovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(3))){
					//Aplica validação de sobreposição entre Vegetação Nativa e Reserva Legal
					if(validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaVn, geometriaRl)){
						throw new CampoObrigatorioException(Util.getString("cefir_msg_A030"));
					}
				}
			}
			
			if(pImovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados() && pImovelRural.isCedeAreaParaCompensacaoRl()) {
				validacaoGeoSeiaService.validaSobreposicaoVegetacaoNativaComRlsCompensadas(pImovelRural, geometriaVn);
			}
			
		}catch(AreaDeclaradaInvalidaException a) {
			throw new Exception("A área informada da Vegetação Nativa ("+a.getAreaDeclarada()+" ha) não confere com a área do shapefile importado ("+a.getAreaCalculada()+" ha).");
		}catch(LocalizacaoGeograficaException l) {
			throw new Exception("A geometria da Vegetação Nativa não está dentro do Limite do Imóvel Rural cadastrado.");
		} catch (CampoObrigatorioException c) {
			throw new Exception(c.getMessage());
		} catch (Exception e) {
			throw new Exception("Erro na validação de geometria da Vegetação Nativa, contate o administrador do sistema.");
		}
	}
}
