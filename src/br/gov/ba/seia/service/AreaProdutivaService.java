package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.dao.AreaProdutivaDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.AreaProdutiva;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividade;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadeAgricultura;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadeAnimal;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadePiscicultura;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.TemaGeoseiaEnum;
import br.gov.ba.seia.enumerator.TipologiaCefirEnum;
import br.gov.ba.seia.exception.AreaDeclaradaInvalidaException;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.exception.LocalizacaoGeograficaException;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AreaProdutivaService {
	
	@Inject
	IDAO<AreaProdutiva> dao;	

	@Inject
	AreaProdutivaDAOImpl daoImpl;

	@Inject
	IDAO<Tipologia> daoTipologia;
	
	@Inject
	IDAO<AreaProdutivaTipologiaAtividade> daoAPTA;

	@Inject
	IDAO<AreaProdutivaTipologiaAtividadeAgricultura> daoAPTAAG;

	@Inject
	IDAO<AreaProdutivaTipologiaAtividadeAnimal> daoAPTAAN;
	
	@Inject
	IDAO<AreaProdutivaTipologiaAtividadePiscicultura> daoAPTAPI;
	
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private TipologiaService tipologiaService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AreaProdutiva> listarAreaProdutivaByImovelRural(ImovelRural imovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaProdutiva.class, "areaProdutiva");
		criteria.createAlias("ideTipologia", "tipologia", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tipologia.tipologiaGrupo", "tipologiaGrupo", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("indExcluido", false));
		criteria.createAlias("ideTipologiaSubgrupo", "tipologiaSubgrupo", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tipologiaSubgrupo.tipologiaGrupo", "tipologiaGrupo2", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("indExcluido", false));
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		criteria.addOrder(Order.asc("ideAreaProdutiva"));

		return dao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<Tipologia> listarTipologia() {
		return  daoTipologia.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(AreaProdutiva areaProdutiva)  {
		this.daoImpl.salvar(areaProdutiva);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(AreaProdutiva areaProdutiva)  {
		this.daoImpl.atualizar(areaProdutiva);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(AreaProdutiva areaProdutiva)  {
		this.daoImpl.salvarOuAtualizar(areaProdutiva);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(AreaProdutiva pAreaProdutiva)  {
		/**
		 * excluindo todas as apta's*/
		this.excluirAllAptaByIdeAreaProdutiva(pAreaProdutiva);	
		pAreaProdutiva.setAreaProdutivaTipologiaAtividadeCollection(null);
		this.daoImpl.excluir(pAreaProdutiva);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(AreaProdutivaTipologiaAtividade pAPTA)  {
		this.daoAPTA.salvar(pAPTA);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(AreaProdutivaTipologiaAtividade pAPTA)  {
		this.daoAPTA.salvarOuAtualizar(pAPTA);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAgricultura(AreaProdutivaTipologiaAtividadeAgricultura pAPTAAG)  {
		this.daoAPTAAG.salvar(pAPTAAG);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAnimal(AreaProdutivaTipologiaAtividadeAnimal pAPTAAN)  {
		this.daoAPTAAN.salvar(pAPTAAN);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPiscicultura(AreaProdutivaTipologiaAtividadePiscicultura pAPTAPI)  {
		this.daoAPTAPI.salvar(pAPTAPI);
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAllAtividadeAgriculturaByIdeApta(Integer pIdeAreaProdutivaTipologiaAtividade)  {
		String deleteSQL = "DELETE FROM area_produtiva_tipologia_atividade_agricultura WHERE ide_area_produtiva_tipologia_atividade = :ideAreaProdutivaTipologiaAtividade";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideAreaProdutivaTipologiaAtividade", pIdeAreaProdutivaTipologiaAtividade);
		daoAPTAAG.executarNativeQuery(deleteSQL, params);
	}	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAllAtividadeAnimalByIdeApta(Integer pIdeAreaProdutivaTipologiaAtividade)  {
		String deleteSQL = "DELETE FROM area_produtiva_tipologia_atividade_animal WHERE ide_area_produtiva_tipologia_atividade = :ideAreaProdutivaTipologiaAtividade";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideAreaProdutivaTipologiaAtividade", pIdeAreaProdutivaTipologiaAtividade);
		daoAPTAAN.executarNativeQuery(deleteSQL, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAllAtividadePisciculturaByIdeApta(Integer pIdeAreaProdutivaTipologiaAtividade)  {
		String deleteSQL = "DELETE FROM area_produtiva_tipologia_atividade_piscicultura WHERE ide_area_produtiva_tipologia_atividade = :ideAreaProdutivaTipologiaAtividade";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideAreaProdutivaTipologiaAtividade", pIdeAreaProdutivaTipologiaAtividade);
		daoAPTAAN.executarNativeQuery(deleteSQL, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAllAptaByIdeAreaProdutiva(AreaProdutiva pAreaProdutiva)  {
		Collection<AreaProdutivaTipologiaAtividade> listaApta = this.listarAptaByAreaProdutiva(pAreaProdutiva);		
		for (AreaProdutivaTipologiaAtividade areaProdutivaTipologiaAtividade : listaApta) {
			this.excluirAllAtividadeAgriculturaByIdeApta(areaProdutivaTipologiaAtividade.getIdeAreaProdutivaTipologiaAtividade());
			this.excluirAllAtividadeAnimalByIdeApta(areaProdutivaTipologiaAtividade.getIdeAreaProdutivaTipologiaAtividade());
			this.excluirAllAtividadePisciculturaByIdeApta(areaProdutivaTipologiaAtividade.getIdeAreaProdutivaTipologiaAtividade());
		}		
		String deleteSQL = "DELETE FROM area_produtiva_tipologia_atividade WHERE ide_area_produtiva = :ideAreaProdutiva";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideAreaProdutiva", pAreaProdutiva.getIdeAreaProdutiva());
		daoAPTAAG.executarNativeQuery(deleteSQL, params);
	}	

	public Collection<AreaProdutivaTipologiaAtividade> listarAptaByAreaProdutiva(AreaProdutiva areaProdutiva) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaProdutivaTipologiaAtividade.class, "areaProdutivaTipologiaAtividade");
		criteria.add(Restrictions.eq("ideAreaProdutiva", areaProdutiva));
		return daoAPTA.listarPorCriteria(criteria);
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAPTA(AreaProdutivaTipologiaAtividade pAreaProdutivaTipologiaAtividade)  {
		this.daoAPTA.remover(pAreaProdutivaTipologiaAtividade);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AreaProdutiva carregarTudo(AreaProdutiva pAreaProdutiva)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(AreaProdutiva.class, "AreaProdutiva");
		criteria.createAlias("ideTipologia", "tipologia", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tipologia.tipologiaGrupo", "tipologiaGrupo", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("indExcluido", false));
		criteria.createAlias("ideTipologiaSubgrupo", "tipologiaSubgrupo", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tipologiaSubgrupo.tipologiaGrupo", "tipologiaGrupo2", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("indExcluido", false));
		criteria.createAlias("ideDocumentoAutorizacaoManejo", "ideDocumentoAutorizacaoManejo", JoinType.LEFT_OUTER_JOIN);		
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideImovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideAreaProdutiva", pAreaProdutiva.getIdeAreaProdutiva()));
		criteria.addOrder(Order.asc("ideAreaProdutiva"));
		AreaProdutiva lAreaProdutiva = dao.buscarPorCriteria(criteria);
		Hibernate.initialize(lAreaProdutiva.getIdeTipologia().getIdeTipologiaPai());
		if(!Util.isNullOuVazio(lAreaProdutiva.getAreaProdutivaTipologiaAtividadeCollection()))
			Hibernate.initialize(lAreaProdutiva.getAreaProdutivaTipologiaAtividadeCollection());		
		return lAreaProdutiva;
	}
	
	/**
	 * Método que valida as informações das áreas produtivas cadastradas na finalização do cadastro do imóvel rural.
	 * @param imovelRural
	 * @param context
	 * @return void
	 * @throws Exception 
	 * @
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	public void validaAreaProdutivaParaFinalizacao(ImovelRural imovelRural, RequestContext context) throws Exception  {
		if (!Util.isNull(imovelRural.getIndAreaProdutiva()) && imovelRural.getIndAreaProdutiva()) {
			boolean listaVazia = true;		
			
			if(!Util.isNullOuVazio(imovelRural.getAreaProdutivaCollection())){
				for (AreaProdutiva ap : imovelRural.getAreaProdutivaCollection()) {
					if (!ap.getIndExcluido()) {
						listaVazia = false;
						if (!validouCamposObrigatoriosAreaProdutiva(ap, imovelRural.isImovelINCRA()) && !imovelRural.isImovelCDA() && !imovelRural.isImovelBNDES()) {
							throw new Exception("Existe Atividade Desenvolvida cadastrada sem tipo atividade!");
						}
						if(ap.getIdeTipologia().getIdeTipologia().equals(TipologiaCefirEnum.SILVICULTURA.getId())){
							if(Util.isNullOuVazio(ap.getIdeTipologiaSubgrupo()) || Util.isNullOuVazio(ap.getIdeTipologiaSubgrupo().getIdeTipologia())) {
								throw new Exception("Prezado usuário, favor atualizar o tipo de Silvicultura desenvolvida em seu imóvel. Retorne à aba Dados Específicos/Atividade desenvolvida e atualize seu cadastro.");
							}
						}
					}
				}			
			}
			
			if(listaVazia) {
				if(!imovelRural.isImovelCDA() && !imovelRural.isImovelINCRA() && !imovelRural.isImovelBNDES()) {
					throw new SeiaException("Por favor cadastre pelo menos uma Atividade desenvolvida.");
				}
			} else {
				validaTheGeomAreaProdutiva(imovelRural, context);
			}
		}
	}
	
	public boolean validouCamposObrigatoriosAreaProdutiva(AreaProdutiva areaProdutiva, boolean isImovelIncra) {
		if(!Util.isNullOuVazio(areaProdutiva.getIdeTipologiaSubgrupo()) && !areaProdutiva.possuiTipologiaAtividadeCadastrada() && atividadeCadastraTipologiaAtividade(areaProdutiva)) {
			if(isImovelIncra) { 
				if(areaProdutiva.getIdeTipologia().getIdeTipologia() != 6 && areaProdutiva.getIdeTipologia().getIdeTipologia() != 5 && areaProdutiva.getIdeTipologia().getIdeTipologia() != 8){
					return false;
				}
			} else {
				return false;
			}
		}		
		return true;
	}

	private boolean atividadeCadastraTipologiaAtividade(final AreaProdutiva areaProdutiva) {
		final Integer ideTipologia = areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia();
		if(ideTipologia.equals(TipologiaCefirEnum.MANEJO_FLORESTAL_SUSTENTAVEL.getId()) 
				||ideTipologia.equals(TipologiaCefirEnum.MANEJO_CABRUCA.getId()) 
				||ideTipologia.equals(TipologiaCefirEnum.SILVICULTURA_NAO_VINCULADA_PSS.getId())
				||ideTipologia.equals(TipologiaCefirEnum.SILVICULTURA_VINCULADA_PSS.getId())
				||ideTipologia.equals(TipologiaCefirEnum.SILVICULTURA_VINCULADA_PSS_AREA_ATE_200_HA.getId())
				||ideTipologia.equals(TipologiaCefirEnum.PRODUCAO_CARVAO.getId())
				||ideTipologia.equals(TipologiaCefirEnum.ALGICULTURA.getId())
				||ideTipologia.equals(TipologiaCefirEnum.MALACOCULTURA.getId())
				||ideTipologia.equals(TipologiaCefirEnum.RANICULTURA.getId())
				||ideTipologia.equals(TipologiaCefirEnum.CRIACAO_CONFINADA_SEMI_CONFINADA.getId())) {
			return false;
		}
		return true;
	}
	
	private void validaTheGeomAreaProdutiva(ImovelRural pImovelRural, RequestContext context) throws Exception  {
		try{
			String geometriaIm = null;
			String geometriaRl = null;
			String geometriaAp = null;			
			
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
			
			for (AreaProdutiva ap : pImovelRural.getAreaProdutivaCollection()) {
				if(!ap.getIndExcluido()){
					if(ap.getIdeLocalizacaoGeografica() == null){
						throw new CampoObrigatorioException("Existe Atividade desenvolvida sem localização geográfica.");
					}
					if(!localizacaoGeograficaService.existeTheGeom(ap.getIdeLocalizacaoGeografica())) {
						throw new CampoObrigatorioException("Existe Atividade desenvolvida sem shapefile importado, favor verificar.");
					}else{
						//Obtem a geometria da Atividade Desenvolvida através do arquivo shape temporário ou diretamente do banco
						if(ap.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
							geometriaAp = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId(), ap.getCodigoPersistirShape());
						} else {
							geometriaAp = validacaoGeoSeiaService.buscarGeometriaShape(ap.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						}
						
						//Aplica validação de área e validação do tema em relação ao limite do imóvel
						validacaoGeoSeiaService.validarAreaDeclaradaShapeTemporario(ap.getValArea(), geometriaAp);
						
						if(validaShapeDentroDoImovel(ap)){
							validacaoGeoSeiaService.validarLocalizacaoGeografica(null, geometriaAp, null, geometriaIm);
						}
						
												
						//Verifica se a Reserva Legal é do tipo No Próprio Imóvel (2) ou Em Compensação (3)
						if(!Util.isNullOuVazio(pImovelRural.getReservaLegal()) && !Util.isNullOuVazio(pImovelRural.getReservaLegal().getIdeTipoArl()) && (pImovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(2) || pImovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(3))){
							//Aplica validação de sobreposição entre Atividade desenvolvida e Reserva Legal
							if(validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaAp, geometriaRl)){
								if(!validaSobreposicaoRlAp(ap, pImovelRural)) {
									throw new CampoObrigatorioException("Não é possível finalizar o cadastro por ter sido detectada a sobreposição irregular da área de Reserva Legal com a Atividade Desenvolvida.");
								}else{
									context.addCallbackParam("existeSobreposicaoRlAp", true);
								}
							}
						}
						
						//Aplica validação de sobreposição entre APP e Atividade desenvolvida do tipo MANEJO SUSTENTÁVEL
						if(ap.isManejoSustentavel() && !Util.isNullOuVazio(pImovelRural.getAppCollection())) {
							for (App app : pImovelRural.getAppCollection()) {
								String geometriaApp = null;
								//Obtem a geometria da APP através do arquivo shape temporário ou diretamente do banco
								if(app.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
									geometriaApp = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.APP.getId(), app.getCodigoPersistirShape());
								} else {
									geometriaApp = validacaoGeoSeiaService.buscarGeometriaShape(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
								}
								if(!Util.isNullOuVazio(geometriaApp) && validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaApp, geometriaAp)){
									throw new CampoObrigatorioException("Não é possível finalizar o cadastro por ter sido detectada a sobreposição irregular de Área de Preservação Permanente com a Atividade Desenvolvida do tipo Manejo Sustentável.");
								}
							}
						}
						
					}
				}
			}
			
		} catch(AreaDeclaradaInvalidaException a) {
			throw new SeiaException("A área informada da Atividade Desenvolvida ("+a.getAreaDeclarada()+" ha) não confere com a área do shapefile importado ("+a.getAreaCalculada()+" ha).");
		} catch(LocalizacaoGeograficaException l) {
			throw new SeiaException("Existe geometria de Atividade Desenvolvida que não está dentro do Limite do Imóvel Rural cadastrado.");
		} catch (CampoObrigatorioException c) {
			throw new SeiaException(c.getMessage());
		} catch (Exception e) {
			throw new SeiaException("Erro na validação de geometria da Atividade Desenvolvida, contate o administrador do sistema.");
		}
	}
	
	private boolean validaShapeDentroDoImovel(AreaProdutiva ap) {
		if(ap.possuiTipologiaSubGrupoCadastrada()) {
			if(ap.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.ALGICULTURA.getId())
					|| ap.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.MALACOCULTURA.getId())
					|| (ap.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.AQUICULTURA.getId())
							&& !Util.isNullOuVazio(ap.getAreaProdutivaTipologiaAtividadeCollection())
							&& ap.getAreaProdutivaTipologiaAtividadeCollection().iterator().next().getIdeTipologiaAtividade().getIdeTipologiaAtividade().equals(
									TipologiaCefirEnum.PSICULTURA_CONTINENTAL_TANQUE_REDE_RACEWAY_SIMILAR.getId()))) {
				return false;
			}
					
		}
		return true;
	}

	private boolean validaSobreposicaoRlAp(AreaProdutiva ap, ImovelRural imovelRural) {	
		try{
			if(imovelRural.isMenorQueQuatroModulosFiscais() || ContextoUtil.getContexto().isPCT()){
				if(!validacaoGeoSeiaService.isRlMenor20PorCento(imovelRural)) {
					Tipologia tp;
					tp = ap.getIdeTipologia();					
					if(tp.getIdeTipologia().equals(TipologiaCefirEnum.PRODUTOS_AGRICULTURA.getId()) || tp.getIdeTipologia().equals(TipologiaCefirEnum.SILVICULTURA.getId()) || tp.getIdeTipologia().equals(TipologiaCefirEnum.MANEJO_CABRUCA.getId())){
						return true;
					}
				}			
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
		
		return false;
	}
	
	public boolean getRenderedPerguntaLicenca(AreaProdutiva areaProdutiva, boolean isVisualizacao, ImovelRural imovelRural) {
		if(isVisualizacao && !Util.isNullOuVazio(areaProdutiva.getLicenciada()) && !Util.isNullOuVazio(areaProdutiva.getNumProcesso())) {
			return true;			
		} else {
			return  renderedPerguntaLicencaConfinadas(areaProdutiva)
					|| renderedPerguntaLicencaAquicultura(areaProdutiva)
					|| renderedPerguntaLicencaCarcinicultura(areaProdutiva)
					|| renderedPerguntaLicencaRaniculturaAlgiculturaMalacocultura(areaProdutiva)
					|| renderedPerguntaLicencaSilvicultura(areaProdutiva, imovelRural)
					|| renderedPerguntaLicencaAgricultura(areaProdutiva, imovelRural)
					|| renderedPerguntaLicencaAnimais(areaProdutiva, imovelRural);
		}
	}
	

	private boolean renderedPerguntaLicencaConfinadas(AreaProdutiva areaProdutiva) {
			
		if (areaProdutiva.possuiTipologiaAtividadeCadastrada()){
			
			for (AreaProdutivaTipologiaAtividade tipologiaAtividade : areaProdutiva.getAreaProdutivaTipologiaAtividadeCollection()) {
				
				Integer ideTipologia = tipologiaAtividade.getIdeTipologiaAtividade().getIdeTipologiaAtividade();
				
				if (ideTipologia.equals(TipologiaCefirEnum.BOVINOS_BUBALINOS_MUARES_EQUINOS_CRIACAO_CONFINADA.getId())) {
					if ( tipologiaAtividade.getIdeAreaProdutivaTipologiaAtividadeAnimal().getQtdCabeca().intValue() >= 50) {
						return true;
					}
				} else if (ideTipologia.equals(TipologiaCefirEnum.AVES_PEQUENOS_MAMIFEROS_CRIACAO_CONFINADA.getId())) {
					if (tipologiaAtividade.getIdeAreaProdutivaTipologiaAtividadeAnimal().getQtdCabeca().intValue() >= 12000) {
						return true;
					}
				} else if (ideTipologia.equals(TipologiaCefirEnum.CAPRINOS_OVINOS_CRIACAO_CONFINADA.getId())) {
					if (tipologiaAtividade.getIdeAreaProdutivaTipologiaAtividadeAnimal().getQtdCabeca().intValue() >= 500) {
						return true;
					}
				} else if (ideTipologia.equals(TipologiaCefirEnum.SUINOS_CRIACAO_CONFINADA.getId())) {
					if (tipologiaAtividade.getIdeAreaProdutivaTipologiaAtividadeAnimal().getQtdCabeca().intValue() >= 300) {
						return true;
					}
				} else if (ideTipologia.equals(TipologiaCefirEnum.CRECHE_SUINOS_CRIACAO_CONFINADA.getId())) {
					if (tipologiaAtividade.getIdeAreaProdutivaTipologiaAtividadeAnimal().getQtdCabeca().intValue() >= 1000) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean renderedPerguntaLicencaAquicultura(AreaProdutiva areaProdutiva) {
		if (areaProdutiva.possuiTipologiaAtividadeCadastrada()) {
			for (AreaProdutivaTipologiaAtividade tipologiaAtividade : areaProdutiva.getAreaProdutivaTipologiaAtividadeCollection()) {
				
				Integer ideTipologia = tipologiaAtividade.getIdeTipologiaAtividade().getIdeTipologiaAtividade();
				
				if (ideTipologia.equals(TipologiaCefirEnum.PSICULTURA_INTENSIVA_EM_VIVEIROS_ESCAVADOS.getId())) {
					if (areaProdutiva.possuiAreaCadastrada() && areaProdutiva.getValArea().doubleValue() >= 1){
						return true;
					}
				} else if (ideTipologia.equals(TipologiaCefirEnum.PSICULTURA_CONTINENTAL_TANQUE_REDE_RACEWAY_SIMILAR.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean renderedPerguntaLicencaCarcinicultura(AreaProdutiva areaProdutiva) {
		
		if (areaProdutiva.possuiTipologiaAtividadeCadastrada()) {
			for (AreaProdutivaTipologiaAtividade tipologiaAtividade : areaProdutiva.getAreaProdutivaTipologiaAtividadeCollection()) {
				Integer ideTipologia = tipologiaAtividade.getIdeTipologiaAtividade().getIdeTipologiaAtividade();
				if (ideTipologia.equals(TipologiaCefirEnum.CARCINICULTURA_EM_VIVEIROS_ESCAVADOS.getId()) || ideTipologia.equals(TipologiaCefirEnum.CARCINICULTURA_EM_VIVEIROS_ESCAVADOS_EM_APICUNS_E_SALGADOS.getId())){
						return true;
				} 
			}
		}

		return false;
	}

	private boolean renderedPerguntaLicencaRaniculturaAlgiculturaMalacocultura(AreaProdutiva areaProdutiva) {
				
		if (areaProdutiva.possuiTipologiaSubGrupoCadastrada()){
			
			Integer ideTipologia = areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia();
			
			if (ideTipologia.equals(TipologiaCefirEnum.RANICULTURA.getId())){
				return true;
			}else if (ideTipologia.equals(TipologiaCefirEnum.ALGICULTURA.getId()) || ideTipologia.equals(TipologiaCefirEnum.MALACOCULTURA.getId())) {
				if (areaProdutiva.possuiAreaCadastrada() && areaProdutiva.getValArea().doubleValue() >= 1){
					return true;
				}
			}
		}

		return false;
	}

	private boolean renderedPerguntaLicencaSilvicultura(AreaProdutiva areaProdutiva, ImovelRural imovelRural){
		if (areaProdutiva.possuiTipologiaCadastrada() &&
				areaProdutiva.getIdeTipologia().getIdeTipologia().equals(TipologiaCefirEnum.SILVICULTURA.getId())){
			if (areaProdutiva.possuiTipologiaSubGrupoCadastrada()){
				if (areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.PRODUCAO_CARVAO.getId())){
					return true;
				}else if (areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.SILVICULTURA_VINCULADA_PSS.getId())){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean renderedPerguntaLicencaAgricultura(AreaProdutiva areaProdutiva, ImovelRural imovelRural) {
		if (areaProdutiva.possuiTipologiaCadastrada() &&
				areaProdutiva.getIdeTipologia().getIdeTipologia().equals(TipologiaCefirEnum.PRODUTOS_AGRICULTURA.getId())){
			
			if (!imovelRural.isMenorQueQuatroModulosFiscais()) {
				return true;
			}
		}
		
		return false;		
	}
		
	private boolean renderedPerguntaLicencaAnimais(AreaProdutiva areaProdutiva, ImovelRural imovelRural) {
		if (areaProdutiva.possuiTipologiaCadastrada() &&
				areaProdutiva.getIdeTipologia().getIdeTipologia().equals(TipologiaCefirEnum.CRIACAO_DE_ANIMAIS.getId())){
			
			if (areaProdutiva.possuiTipologiaSubGrupoCadastrada() &&
					areaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.PECUARIA.getId())){
				
				if (!imovelRural.isMenorQueQuatroModulosFiscais()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean validaCampoLicencaAreaProdutiva(ImovelRural imovelRural){
		boolean validou = true;
		if (!Util.isNull(imovelRural.getIndAreaProdutiva()) && imovelRural.getIndAreaProdutiva()) {
			if(!Util.isNullOuVazio(imovelRural.getAreaProdutivaCollection())){
				for (AreaProdutiva ap : imovelRural.getAreaProdutivaCollection()) {
					if (!ap.getIndExcluido()) {
						if(getRenderedPerguntaLicenca(ap, false, imovelRural) && Util.isNullOuVazio(ap.getLicenciada())){
							validou = false;
						}
					}
				}
			}
		}
		return validou;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AreaProdutiva> obterAreaSequeiroCacauSefir(Imovel imovel)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaProdutiva.class);
		criteria.add(Restrictions.eq("ideImovelRural.ideImovelRural", imovel.getIdeImovel()));
		
		return this.dao.listarPorCriteria(criteria);
	}
	
	
}
