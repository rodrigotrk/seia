/**
 * 
 */
package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import br.gov.ba.seia.dao.ImovelRuralDAOImpl;
import br.gov.ba.seia.dao.ValidacaoGeoSeiaDAOIf;
import br.gov.ba.seia.entity.AssentadoIncraImovelRural;
import br.gov.ba.seia.entity.GeoJsonSicar;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.VegetacaoNativa;
import br.gov.ba.seia.enumerator.SistemaCoordenadaEnum;
import br.gov.ba.seia.enumerator.StatusCadastroImovelRuralEnum;
import br.gov.ba.seia.enumerator.TemaGeoseiaEnum;
import br.gov.ba.seia.exception.AreaDeclaradaInvalidaException;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.exception.CodigoIbgeMunicipioInvalidoException;
import br.gov.ba.seia.exception.ImovelSuspensoException;
import br.gov.ba.seia.exception.LocalizacaoGeograficaException;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.exception.SobreposicaoAreasException;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author carlos.duarte
 * 
 * Class responsável por validações geográficas no SEIA
 * 
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ValidacaoGeoSeiaService {
	
	private final int IMOVEL_CREDITO_IDE = 0;
	private final int IMOVEL_CREDITO_DENOMINACAO = 1;
	private final int IMOVEL_CREDITO_STATUS = 2;
	private final int IMOVEL_CREDITO_IDE_LOC = 3;
	private final int IMOVEL_CREDITO_VAL_AREA_RL = 4;
	private final int IMOVEL_CREDITO_NUM_SICAR = 5;
	
	private final int IMOVEL_DEBITO_IDE = 0;
	private final int IMOVEL_DEBITO_DENOMINACAO = 1;
	private final int IMOVEL_DEBITO_STATUS = 2;
	private final int IMOVEL_DEBITO_IDE_LOC_IMOVEL = 3;
	private final int IMOVEL_DEBITO_IDE_LOC_RL = 4;
	
	@Inject
	private ValidacaoGeoSeiaDAOIf validarGeoSeiaDAO;
	
	@Inject
	private ImovelRuralDAOImpl imovelRuralImplDao;
	
	@Inject
	private VegetacaoNativaService vegetacaoNativaService;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	
	@Inject
	private ImovelRuralService imovelRuralService;
	
	@Inject
	private IDAO<ImovelRural> imovelRuralDAO;
	
	@Inject
	private ImovelSuspensaoService imovelSuspensaoService;
	
	@Inject
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	public String buscarGeometriaShape(Integer ideLocalizacaoGeografica) throws Exception {
		return validarGeoSeiaDAO.buscarGeometriaShape(ideLocalizacaoGeografica);
	}
	
	public void validarAreaDeclarada(Double areaDeclarada, final Integer ideLocalizacao) throws Exception {
		Double areaShape = validarGeoSeiaDAO.buscarAreaDeclaradaEGeorreferenciada(ideLocalizacao)[1];
		if(Util.isNullOuVazio(areaShape)) {
			throw new CampoObrigatorioException("Erro interno na validação da área declarada. Contate o administrador do sistema.");
		}
		Double percentual = (areaShape/areaDeclarada)*100;
		//Arredonda o percentual para apenas uma casa decimal
		percentual = Util.converterDoubleUmaCasa(percentual);
		
		if(percentual < 90 || percentual > 110) {
			throw new AreaDeclaradaInvalidaException(areaDeclarada, areaShape);
		}
	}
	
	public void validarAreaDeclaradaShapeTemporario(Double areaDeclarada, final String geometria)throws Exception {
		Double areaShape = validarGeoSeiaDAO.buscarAreaGeorreferenciadaShapeTemporario(geometria);
		if(Util.isNullOuVazio(areaShape)) {
			throw new CampoObrigatorioException("Erro interno na validação da área declarada. Contate o administrador do sistema.");
		}
		Double percentual = (areaShape/areaDeclarada)*100;
		//Arredonda o percentual para apenas uma casa decimal
		percentual = Util.converterDoubleUmaCasa(percentual);
		
		if(percentual < 90 || percentual > 110) {
			throw new AreaDeclaradaInvalidaException(areaDeclarada, areaShape);
		}
	}
	
	public void validarCodigoIbgeMunicipioShape(String geometria, Integer pCodIbegeMunicipio) throws Exception{
		List<Integer> listaCodigoIbgeMunicipio = validarGeoSeiaDAO.buscarCodigoIbgeMunicipioShape(geometria);
		for (Integer codigoIbgeMuncipio : listaCodigoIbgeMunicipio) {
			if(codigoIbgeMuncipio.equals(pCodIbegeMunicipio))
				return;
		}
		
		throw new CodigoIbgeMunicipioInvalidoException("Município da localização geográfica do imóvel não confere com o endereço cadastrado.");
	}
	
	public Boolean validarAreaSupressaoRequerimento(Integer ideLocalizacao, Double area) throws Exception {
		Boolean validado = validarGeoSeiaDAO.validaAreaComAreaDaLocalizacaoGeografica(ideLocalizacao, area);
		if(Util.isNullOuVazio(validado)) {
			throw new Exception("O sistema não conseguiu calcular a área, contate o administrador do sistema.");
		}
		return validado;	
	}
	
	public void validarSobreposicaoTemaShapeTemporario(final String geometria, final int tipo, Integer ideLocalizacao) throws Exception {
		List<Integer> localizacoes = validarGeoSeiaDAO.listarLocalizacaoGeograficaComSobreposicao(null, geometria, tipo);
		if(Util.isNullOuVazio(localizacoes)) {
			return;
		}
		
		Double totalAreaSobreposta = 0.0;
		for (Integer localizacao : localizacoes) {
			if(!localizacao.equals(ideLocalizacao)) {
				totalAreaSobreposta += validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(null, geometria, localizacao, null)[2];
			}
		}
		
		if(totalAreaSobreposta == 0.0) {
			return;
		}
		
		Double areaReal = validarGeoSeiaDAO.buscarAreaGeorreferenciadaShapeTemporario(geometria);
		Double percentual = (totalAreaSobreposta/areaReal)*100;
		//Arrendondando o percentual para apenas uma casa decimal
		percentual = Util.converterDoubleUmaCasa(percentual);
		
		if(percentual > 100) {
			throw new SobreposicaoAreasException(Double.valueOf(100));
		}
		
		if(percentual > 10) {
			throw new SobreposicaoAreasException(percentual);
		}
	}
	
	public Boolean isLocalizacaoGeograficaIgual(final String geometriaA, final Integer ideLocalizacaoB) throws Exception {
		Double[] areas = validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(null, geometriaA, ideLocalizacaoB, null);
		Double areaTotalA = areas[0];
		Double areaTotalB = areas[1];
		Double areaSobreposicao = areas[2];
		
		if(areaTotalA > 0 && areaTotalB > 0 && areaSobreposicao > 0){
			Double percentualA = (areaSobreposicao/areaTotalA)*100;
			Double percentualB = (areaSobreposicao/areaTotalB)*100;
			//Arredonda o percentual para apenas uma casa decimal
			percentualA = Util.converterDoubleUmaCasa(percentualA);
			percentualB = Util.converterDoubleUmaCasa(percentualB);
			
			if(percentualA >= 90 && percentualB >= 90) {
				return true;
			}
		}
		
		return false;
	}
	
	public void validarLocalizacaoGeografica(Integer ideLocalizacaoA, String geometriaA, Integer ideLocalizacaoB, String geometriaB) throws Exception {
		if(!isLocalizacaoGeograficaContida(ideLocalizacaoA, geometriaA, ideLocalizacaoB, geometriaB)) {
			throw new LocalizacaoGeograficaException();
		}
	}
	
	public Boolean isLocalizacaoGeograficaContida(Integer ideLocalizacaoA, String geometriaA, Integer ideLocalizacaoB, String geometriaB) throws Exception {
		Double[] areas = validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(ideLocalizacaoA, geometriaA, ideLocalizacaoB, geometriaB);
		if(Util.isNullOuVazio(areas)) {
			throw new CampoObrigatorioException("Erro interno na validação de localização geográfica. Contate o administrador do sistema.");
		}
		if(areas[0] > 0 && areas[2] > 0) {
			Double percentual = (areas[2]/areas[0])*100;
			//Arrendondando o percentual para apenas uma casa decimal
			percentual = Util.converterDoubleUmaCasa(percentual);
			
			if(percentual >= 90) {
				return true;
			}
		}
		return false;
	}
		
	public Boolean isRlMenor20PorCento(ImovelRural pImovelRural) throws Exception {
		String geometriaIm = null;
		String geometriaRl = null;
		Double areaGeoImovel = 0.0;
		Double areaGeoRl = 0.0;
		
		//Geometria limite do imóvel
		if(pImovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
			geometriaIm = buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), 1, null);
		} else {
			geometriaIm = buscarGeometriaShape(pImovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		}
	
		//Geometria da Reserva Legal
		if(localizacaoGeograficaService.existeTheGeom(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica()) 
				&& pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
			geometriaRl = buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), 2, null);
		} else {
			if(!Util.isNullOuVazio(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica())){
				
				geometriaRl = buscarGeometriaShape(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
		}
		
		areaGeoImovel = validarGeoSeiaDAO.buscarAreaGeorreferenciadaShapeTemporario(geometriaIm);				
		areaGeoRl = validarGeoSeiaDAO.buscarAreaGeorreferenciadaShapeTemporario(geometriaRl);	
		
		Double percentual = (areaGeoRl/areaGeoImovel)*100;
		//Arredonda o percentual para apenas uma casa decimal
		percentual = Util.converterDoubleUmaCasa(percentual);
		
		if(percentual < 19.5) {
			return true;
		}
		
		return false;
	}
	
	public Boolean validaAprovacaoRlProcessoCerberus(String numeroProcesso, Integer ideLocalizacao) throws Exception {		
		return validarGeoSeiaDAO.validaShapeRlCerberus(numeroProcesso, ideLocalizacao);
	}
	
	public Boolean isValidaCompensacaoReservaLegal(Collection<PessoaImovel> lProprietarios, ImovelRural pImovelRural) throws Exception {
		String geometriaRl = null;
		Double percentual = 0.0;
		Double[] areas = new Double[3];
		Double areaTotalCompensada = 0.0;
		int qtdImoveis = 0;
		boolean numSicarCompensacaoValidado = false;
		
		percentual = retornaPercentualRlNoImovel(pImovelRural);
		
		if(percentual == 100) {
			throw new CampoObrigatorioException("Tipo de reserva legal (regularizada ou pretendida) não corresponde ao shape importado.");
		}
		
		List<ImovelRural> listaImoveisProprietarios = retornaListaImoveisDosProprietarios(lProprietarios, pImovelRural);
		
		if(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
			geometriaRl = buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.RESERVA_LEGAL.getId(), null);
		} else {
			geometriaRl = buscarGeometriaShape(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		}
		
		List<Object[]> listaImoveisCreditoRl = validarGeoSeiaDAO.listarImoveisCreditoRl(pImovelRural.getIdeImovelRural(), geometriaRl, false);
		
		for (Object[] imovelCredito : listaImoveisCreditoRl) {
			if(!Util.isNullOuVazio(imovelCredito[IMOVEL_CREDITO_IDE_LOC])) {
				if(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					areas = validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(null, geometriaRl, (Integer) imovelCredito[IMOVEL_CREDITO_IDE_LOC], null);
				} else {
					areas[0] = validarGeoSeiaDAO.buscarAreaGeorreferenciadaShapeTemporario(geometriaRl);
					areas[2] = ((BigDecimal)imovelCredito[IMOVEL_CREDITO_VAL_AREA_RL]).doubleValue();
				}
				if(Util.isNullOuVazio(areas)) {
					throw new CampoObrigatorioException("Erro interno na validação de compensação de Reserva Legal, contate o administrador do sistema.");
				}
				if (areas[0] > 0 && areas[2] > 0) {
					qtdImoveis++;
					if(validaCompensacaoRl(imovelCredito, pImovelRural, listaImoveisProprietarios, qtdImoveis)){
						percentual += (areas[2]/areas[0])*100;
						String numSicarCompensacao = pImovelRural.getReservaLegal().getNumSicarCompensacao().replace(".", "");
						String numSicarImovelCredito = (String)imovelCredito[IMOVEL_CREDITO_NUM_SICAR];
						numSicarImovelCredito = numSicarImovelCredito.replace(".", "");
						
						if (!Util.isNullOuVazio(imovelCredito[IMOVEL_CREDITO_NUM_SICAR]) && numSicarCompensacao.equalsIgnoreCase(numSicarImovelCredito)) {
							numSicarCompensacaoValidado = true;
						}
						areaTotalCompensada += areas[2];
					}
				}
			}
		}		
		//Arredonda o percentual para apenas uma casa decimal
		percentual = Util.converterDoubleUmaCasa(percentual);
		if(percentual < 90) {
			return false;
		}

		if(!numSicarCompensacaoValidado) {
			throw new CampoObrigatorioException("O número CAR informado não corresponde a um imóvel onde está localizada a Reserva Legal compensada.");
		}
		
		if(!validaSobreposicaoReservaLegalComVegetacaoNativa(geometriaRl, areaTotalCompensada, listaImoveisCreditoRl)) {
			throw new CampoObrigatorioException(Util.getString("cefir_msg_A027"));
		}
		
		return true;
	}

	private List<ImovelRural> retornaListaImoveisDosProprietarios(Collection<PessoaImovel> lProprietarios, ImovelRural pImovelRural) throws Exception {
		List<ImovelRural> lImoveisProprietarios = new ArrayList<ImovelRural>();
		
		for (PessoaImovel pessoaImovel : lProprietarios) {
			
			if(!pessoaImovel.getIndExcluido()){
				List<ImovelRural> lImoveisPessoaImovel = imovelRuralImplDao.listarImoveisPorProprietarioCompensacao(pessoaImovel.getIdePessoa()); 
				
				for(ImovelRural imovelRural : lImoveisPessoaImovel) {
					
					if(!imovelRural.getIdeImovelRural().equals(pImovelRural.getIdeImovelRural()) 
							&& imovelRural.isCadastrado() && !lImoveisProprietarios.contains(imovelRural)) {
						
						lImoveisProprietarios.add(imovelRural); 
					}
				}
			}
		}
		
		return lImoveisProprietarios;
	}

	private Double retornaPercentualRlNoImovel(ImovelRural pImovelRural) throws Exception {
		String geometriaIm = null;
		String geometriaRl = null;
		Double percentual = 0.0;
		Double[] areas;
		
		//Se a geometria do limite do imóvel não estiver carregada, obtem através do arquivo shape temporário ou diretamente do banco
		if(pImovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
			geometriaIm = buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), null);
		} else {
			geometriaIm = buscarGeometriaShape(pImovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		}
		
		//Obtem a geometria da reserva legal através do arquivo shape temporário ou diretamente do banco
		if(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
			geometriaRl = buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.RESERVA_LEGAL.getId(), null);					
		} else {
			geometriaRl = buscarGeometriaShape(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		}
		
		areas = validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(null, geometriaRl, null, geometriaIm);
		
		if(areas[0] > 0 && areas[2] > 0) {
			percentual = (areas[2]/areas[0])*100;
			//Arredonda o percentual para apenas uma casa decimal
			percentual = Util.converterDoubleUmaCasa(percentual);
		}
		return percentual;
	}

	private boolean validaCompensacaoRl(Object[] imovelCredito, ImovelRural proprioImovel, List<ImovelRural> listaImoveisProprietarios, int qtdImoveis) throws Exception {
		ImovelRural imovelCreditoRlValidar = null;
		//regra removida de acordo com o redmine de ticket #10742
		/*if(qtdImoveis > 1) {
			if(!proprioImovel.getReservaLegal().getIndAverbada() && (!Util.isNullOuVazio(proprioImovel.getIndReservaLegal()) && !proprioImovel.getIndReservaLegal())) {
				if(!validarGeoSeiaDAO.isExcecaoRegraCompensacaoRlMaisDeUmImovel(proprioImovel.getIdeImovelRural())) {
					throw new ReservaCompensadaEmMaisDeUmImovelException("A reserva legal está sendo compensada em mais de um imóvel além do próprio imóvel.");
				}
			}
		}*/
		if(proprioImovel.houveAlteracaoProprietario()) {
			imovelCreditoRlValidar = imovelRuralImplDao.carregarImovelPorId((Integer) imovelCredito[IMOVEL_CREDITO_IDE]);
		} else {
			for (ImovelRural imovelRural : listaImoveisProprietarios) {
				if(imovelRural.getIdeImovelRural().equals((Integer) imovelCredito[IMOVEL_CREDITO_IDE])) {
					imovelCreditoRlValidar = imovelRural;
					break;
				}
			}
		}
		
		if(!Util.isNullOuVazio(imovelCreditoRlValidar)) {
			if (imovelCreditoRlValidar.getReservaLegal().getIdeTipoArl().equals(1)) {
				throw new CampoObrigatorioException("A reserva legal está sendo compensada em um imóvel que possui reserva do tipo em condomínio");
			}
			if (imovelCreditoRlValidar.getReservaLegal().getIdeTipoArl().equals(4)) {
				throw new CampoObrigatorioException("A reserva legal está sendo compensada em um imóvel que possui reserva do tipo em compensação por servidão ambiental");
			}
			return true;
		} else {
			return false;
		}
	}
	
	public String buscarGeometriaShapeTemporario(Integer ideImovel, Integer tipo, String identificador) throws Exception {
		String[] retorno = validarGeoSeiaDAO.buscarGeometriaShapeTemporarioCEFIR(ideImovel, tipo, identificador);
		if (retorno != null) {
			if (retorno.length > 0){
				if(retorno[0].equalsIgnoreCase("ERRO")){
					throw new Exception(retorno[2]+" ["+retorno[1]+"]");
				} else {
					return retorno[0];
				}
			}
		}
		return "";
	}
	
	public String buscarGeometriaShapeTemporario(LocalizacaoGeografica ideLocalizacao) throws Exception {
		String[] retorno = validarGeoSeiaDAO.buscarGeometriaShapeTemporario(ideLocalizacao);
		if (retorno != null) {
			if (retorno.length > 0){
				if(retorno[0].equalsIgnoreCase("ERRO")){
					throw new Exception(retorno[2]+" ["+retorno[1]+"]");
				} else {
					return retorno[0];
				}
			}
		}
		return "";
	}
	
	public boolean validaPercentualSobreposicao(String geometriaA, String geometriaB) throws Exception{
		return validarGeoSeiaDAO.validaPercentualSobreposicao(geometriaA, geometriaB);
	}

	public void validarAssentadoLote(Integer ideImovel, List<AssentadoIncraImovelRural> listAssentadosPlanilha, String geometriaImovel) throws Exception {
		List<String> listaDePontos = new ArrayList<String>();
		
		for (AssentadoIncraImovelRural assentadoIncraImovelRural : listAssentadosPlanilha) {
			if(!Util.isNull(assentadoIncraImovelRural.getLongitude()) && !Util.isNull(assentadoIncraImovelRural.getLatitude())) {
				listaDePontos.add(assentadoIncraImovelRural.getLongitude() + " " + assentadoIncraImovelRural.getLatitude());
			}
		}
		if(!Util.isNullOuVazio(listaDePontos)) {
			List<String> listResultado = validarGeoSeiaDAO.validarAssentadoLote(ideImovel, listaDePontos.toString().replace("[", "").replace("]", ""), geometriaImovel);
			if(Util.isNullOuVazio(listResultado)) {
				throw new Exception("ERRO: Contate o administrador do sistema.");
			}
			
			if(!listResultado.get(0).equalsIgnoreCase("OK")) {
				throw new Exception(listResultado.get(1));
			}
		}
	}
	
	public GeoJsonSicar buscarGeoJsonSicar(List<Integer> localizacoes) throws Exception {
		return validarGeoSeiaDAO.buscarGeoJsonSicar(localizacoes);
						
	}
	
	/**
	 * Método que informa a área do Shape através do seu theGeom ({@link String})
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 02/02/2015
	 * @param theGeom
	 * @return {@link Double}
	 * @throws Exception
	 */
	public Double retonarAreaDoShapeByGeometria(String theGeom) throws Exception {
		return validarGeoSeiaDAO.buscarAreaGeorreferenciadaShapeTemporario(theGeom);
	}
	
	public boolean validaTipoGeometriaPoligono(Integer ideLocalizacao, String geometria) throws Exception{
		String retorno = validarGeoSeiaDAO.validaTipoGeometria(ideLocalizacao, geometria);
		if (retorno != null) {
			if(retorno.equalsIgnoreCase("POLYGON")){
				return true;				
			}
		}
		return false;
	}
	
	public boolean validaTipoGeometriaLinha(Integer ideLocalizacao, String geometria) throws Exception{
		String retorno = validarGeoSeiaDAO.validaTipoGeometria(ideLocalizacao, geometria);
		if (retorno != null) {
			if(retorno.equalsIgnoreCase("LINE")){
				return true;				
			}
		}
		return false;
	}
	
	public boolean validaTipoGeometriaPonto(Integer ideLocalizacao, String geometria) throws Exception{
		String retorno = validarGeoSeiaDAO.validaTipoGeometria(ideLocalizacao, geometria);
		if (retorno != null) {
			if(retorno.equalsIgnoreCase("POINT")){
				return true;				
			}
		}
		return false;
	}
	
	public void validarTalhoesDeclaradosGeometria(Integer talhoesDeclarada, final String geometria) throws Exception {
		Integer talhoesShape = validarGeoSeiaDAO.buscarQuantidadeTalhoesDeclarados(geometria);
		if(Util.isNullOuVazio(talhoesShape)) {
			throw new CampoObrigatorioException("Erro interno na validação dos talhões da atividade desenvolvida, contate o administrador do sistema.");
		}
		
		if(!talhoesDeclarada.equals(talhoesShape)) {
			throw new CampoObrigatorioException("A quantidade de talhões informado da Atividade Desenvolvida ("+talhoesDeclarada + ") não confere com a quantidade de talhões do shapefile importado ("+talhoesShape+").");
		}
	}
	
	/**
	 * Retorno ide_imovel_rural, des_denominacao, ide_status_imovel, ide_loc_imovel, area_intersecao e numero_car_imovel
	 * @param pImovelRural
	 * @return
	 */
	public List<Object[]> obterImoveisCredito(ImovelRural pImovelRural, Boolean proprioImovel) {
		String geometriaRl;
		try {
			if(pImovelRural.possuiReservaLegal() && pImovelRural.getReservaLegal().possuiLocalizacao()) {
				geometriaRl = buscarGeometriaShape(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				return validarGeoSeiaDAO.listarImoveisCreditoRl(pImovelRural.getIdeImovelRural(), geometriaRl, proprioImovel);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return null;
	}
	
	/**
	 * Método que verifica se {@link LocalizacaoGeografica} está contida em outra {@link LocalizacaoGeografica}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 03/11/2015
	 * @param locGeoPrincipal = {@link LocalizacaoGeografica} Maior, ex: Empreendimento, Imóvel Rural, etc.
	 * @param locGeoContida = {@link LocalizacaoGeografica} Menor, ex: Área de Cultivo, APP, etc.
	 * @param porcentagemContida = Pocentagem que deve ser indicada de acordo com cada regra.
	 */
	public boolean verificarSobreposicaoCompleta(LocalizacaoGeografica locGeoPrincipal, LocalizacaoGeografica locGeoContida, Double porcentagemContida) throws Exception {
		String geometriaPrincipal = null;
		String geometriaContida = null;
		Double percentual = 0.0;
		Double[] areas;		

		geometriaPrincipal = buscarGeometriaShape(locGeoPrincipal.getIdeLocalizacaoGeografica());
		geometriaContida = buscarGeometriaShape(locGeoContida.getIdeLocalizacaoGeografica()); 

		areas = validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(null, geometriaContida, null, geometriaPrincipal);
		
		// areas[2] área de Interseção
		if(areas[2] == 0){ // Caso o resultado seja 0, significa que não há interseção entre as geometrias.
			return false;
		} 
		else {
			// areas[0] área da Geometria Contida
			if(areas[0] != null) {
				percentual = (areas[2]/areas[0])*100;
				//Arredonda o percentual para apenas uma casa decimal
				percentual = Util.converterDoubleUmaCasa(percentual);
				return percentual.equals(porcentagemContida);
			} 
			else {
				System.out.println("A porcetagem contida é de:" + percentual + "mas foi solicitado que ela estivesse " + porcentagemContida + " contida.");
				return false;
			}
		}
	}
	
	public Double retornaPercentualSobreposicao(LocalizacaoGeografica locGeoPrincipal, LocalizacaoGeografica locGeoContida) throws Exception {
		String geometriaPrincipal = null;
		String geometriaContida = null;
		Double percentual = 0.0;
		Double[] areas;
		
		geometriaPrincipal = buscarGeometriaShape(locGeoPrincipal.getIdeLocalizacaoGeografica());
		geometriaContida = buscarGeometriaShape(locGeoContida.getIdeLocalizacaoGeografica()); 
		
		areas = validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(null, geometriaContida, null, geometriaPrincipal);
		
		// areas[2] área de Interseção
		if(areas[2] == 0){ // Caso o resultado seja 0, significa que não há interseção entre as geometrias.
			percentual = 0.0;
			
		} else if(areas[0] != null) {
			percentual = (areas[2]/areas[0])*100; // areas[0] área da Geometria Contida
		}
		
		return percentual;
	}
	
	/**
	 * Método que vai verificar se a {@link LocalizacaoGeografica} está dentro
	 * do Estado.
	 * 
	 * @author eduardo.fernandes
	 * @since 19/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param localizacaoGeografica
	 * @return
	 */
	public boolean isLocalizacaoGeograficaSomenteNaBahia(LocalizacaoGeografica localizacaoGeografica) {
		try{
			return validarGeoSeiaDAO.isLocalizacaoGeograficaDentroDaBahia(localizacaoGeografica);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações da Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isExisteIntersecao(LocalizacaoGeografica locGeoA, LocalizacaoGeografica locGeoB) throws Exception {
		String theGeomA = buscarGeometriaShape(locGeoA.getIdeLocalizacaoGeografica());
		String theGeomB = buscarGeometriaShape(locGeoB.getIdeLocalizacaoGeografica());
		Double[] areas = validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(null, theGeomA, null, theGeomB);

		// areas[2] área de Interseção
		return areas[2] != null && Util.converterDoubleQuatroCasas(areas[2]) > 0.0;
	}

	/**
	 * @author carlos.duarte
	 * 
	 * Método que valida se há a sobreposição 
	 * da área da geometria informada por parâmetro com outros imóveis ignorando o id do imóvel passado 
	 * por parâmetro que está sendo validado, seguindo o padrão de validação de sobreposição de imóveis do CAR.
	 * 
	 * @param geometria
	 * @param ideImovelRural
	 * @throws SobreposicaoAreasException
	 * @throws Exception
	 */
	public void validaSobreposicaoImovel(String geometria, ImovelRural imovelRural) throws SobreposicaoAreasException, Exception {
		if(!validarGeoSeiaDAO.validaSobreposicaoImovel(geometria, imovelRural.getIdeImovelRural())) {
			if(imovelSuspensaoService.listaImoveisSobrepostos(geometria, imovelRural)){
				throw new  ImovelSuspensoException("Caro usuário, o limite do seu imóvel rural sobrepõe uma área suspensa para novos cadastros por decisão judicial. Para maiores informações, por favor, entre em contato com o Service Desk do SEIA, por meio do email atendimento.seia@inema.ba.gov.br");
			}else{
				throw new SobreposicaoAreasException(geometria);
			}
		}
	}

	/**
	 * @author carlos.duarte
	 * 
	 * Método que valida se a área da reserva legal a ser compensada está localizada em área de vegetação nátiva 
	 * RN042
	 * 	 * 
	 * @param listaImoveisCreditoRl 
	 * @param pImovelRural
	 * @throws Exception
	 */
	public boolean validaSobreposicaoReservaLegalComVegetacaoNativa(String geometriaRl, Double areaTotalCompensada, List<Object[]> listaImoveisCreditoRl) throws Exception {
		List<Integer> listaLocVnImoveisCredito = listarLocalizacoesVnImoveisCredito(listaImoveisCreditoRl);
		Double areaTotalSobrepostaEmVn = 0.0;
		if (!Util.isNullOuVazio(listaLocVnImoveisCredito)) {
			for (Integer ideLocalizacaoVn : listaLocVnImoveisCredito) {
				areaTotalSobrepostaEmVn += validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(null, geometriaRl, ideLocalizacaoVn, null)[2];
			}
			if(areaTotalSobrepostaEmVn > 0 && ((areaTotalSobrepostaEmVn / areaTotalCompensada) * 100) >= 90) {
				return true;
			}
		}
		
		
		return false;
	}
	
	public List<Integer> listarLocalizacoesVnImoveisCredito(List<Object[]> listaImoveisCreditoRl) throws Exception {
		List<Integer> listaLocalizacoesVnImoveisCredito = new ArrayList<Integer>();
		
		for (Object[] imovelCredito : listaImoveisCreditoRl) {
			VegetacaoNativa vegetacao = vegetacaoNativaService.listarVegetacaoNativaByImovelRural(new ImovelRural((Integer)imovelCredito[IMOVEL_CREDITO_IDE]));
			if(!Util.isNullOuVazio(vegetacao) && !Util.isNullOuVazio(vegetacao.getIdeLocalizacaoGeografica())) {
				listaLocalizacoesVnImoveisCredito.add(vegetacao.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
		}
		return listaLocalizacoesVnImoveisCredito;
	}
	
	/**
	 * Método responsável validar se as Reservas legais compensadas no imóvel estão cadastradas sobre a área da vegetação nativa.
	 * @return boolean
	 * @throws Exception
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 04/05/2016
	*/
	public void validaSobreposicaoVegetacaoNativaComRlsCompensadas(ImovelRural pImovelRural, String geometriaVn) throws Exception {		
		List<Object[]> listaImoveisDebitoRl = validarGeoSeiaDAO.listarImoveisDebitoRl(pImovelRural.getIdeImovelRural(), null);
		for (Object[] imovelDebito : listaImoveisDebitoRl) {
			Double[] areas = new Double[3];
			Double areaTotalCompensada = 0.0;
			Double areaTotalSobrepostaEmVn = 0.0;
			
			String geometriaRl = buscarGeometriaShape((Integer) imovelDebito[IMOVEL_DEBITO_IDE_LOC_RL]);
			List<Object[]> listaImoveisCreditoRl = validarGeoSeiaDAO.listarImoveisCreditoRl((Integer) imovelDebito[IMOVEL_DEBITO_IDE], geometriaRl, false);
			List<Integer> listaLocVnImoveisCredito = listarLocalizacoesVnImoveisCredito(listaImoveisCreditoRl);
			
			for (Object[] imovelCredito : listaImoveisCreditoRl) {
				if(!Util.isNullOuVazio(imovelCredito[IMOVEL_CREDITO_IDE_LOC])) {
					areas = validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(null, geometriaRl, (Integer) imovelCredito[IMOVEL_CREDITO_IDE_LOC], null);
					
					if(Util.isNullOuVazio(areas)) {
						throw new CampoObrigatorioException("Erro interno na validação de compensação de Reserva Legal, contate o administrador do sistema.");
					}
					
					if (areas[2] > 0) {						
						areaTotalCompensada += areas[2];
					}
				}
			}
			
			if (!Util.isNullOuVazio(listaLocVnImoveisCredito)) {
				for (Integer ideLocalizacaoVn : listaLocVnImoveisCredito) {
					if(Util.isNullOuVazio(pImovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()) 
							|| !pImovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().equals(ideLocalizacaoVn)) {
						areaTotalSobrepostaEmVn += validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(null, geometriaRl, ideLocalizacaoVn, null)[2];
					}
				}
			}
			areaTotalSobrepostaEmVn += validarGeoSeiaDAO.buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(null, geometriaRl, null, geometriaVn)[2];
			
			if(areaTotalSobrepostaEmVn > 0 && ((areaTotalSobrepostaEmVn / areaTotalCompensada) * 100) <= 90) {
				throw new CampoObrigatorioException(Util.getString("cefir_msg_A018"));
			}
		}
	}
	
	public boolean verificaSobreposicaoShapeUsandoContains(LocalizacaoGeografica locGeoPrincipal, LocalizacaoGeografica locGeoContida) throws Exception {
		String geometriaPrincipal = buscarGeometriaShape(locGeoPrincipal.getIdeLocalizacaoGeografica());
		String geometriaContida = buscarGeometriaShape(locGeoContida.getIdeLocalizacaoGeografica());
		
		if (Util.isNullOuVazio(geometriaPrincipal)) {
			throw new Exception("Erro ao validar Localização Geográfica. A geometria principal não existe.");
		}
		
		if (Util.isNullOuVazio(geometriaContida)) {
			throw new Exception("Erro ao validar Localização Geográfica. A geometria contida não existe.");
		}
		
		return validarGeoSeiaDAO.verificaSobreposicaoShapeUsandoContains(geometriaPrincipal, geometriaContida);
	}
	
	public boolean verificaPontoNoShapeUsandoContains(LocalizacaoGeografica locGeoPrincipal, LocalizacaoGeografica locGeoPontoContida) throws Exception {
		String geometriaPrincipal = null;
		String geometriaContida = null;
		int geograficaSIRGAS2000 = 4674;
		
		String coordGeoNumerica = ("POINT("+locGeoPontoContida.getLatitudeInicial()+" "+locGeoPontoContida.getLongitudeInicial()+")").replaceAll(",", ".");
		int srid = Integer.valueOf(locGeoPontoContida.getIdeSistemaCoordenada().getSrid());
		int sridShape = Integer.valueOf(locGeoPrincipal.getIdeSistemaCoordenada().getSrid());
		
		geometriaPrincipal = buscarGeometriaShape(locGeoPrincipal.getIdeLocalizacaoGeografica());
		
		if (isCoordenadaUTM(locGeoPontoContida) && srid != geograficaSIRGAS2000 && sridShape == geograficaSIRGAS2000) {
			coordGeoNumerica = ("POINT("+locGeoPontoContida.getLatitudeInicial()+" "+locGeoPontoContida.getLongitudeInicial()+")").replaceAll(",", ".");
			geometriaContida =  validarGeoSeiaDAO.converterPonto(coordGeoNumerica, srid, geograficaSIRGAS2000);
		} else {
			geometriaContida = buscarGeometriaShape(locGeoPontoContida.getIdeLocalizacaoGeografica());
		}

		return validarGeoSeiaDAO.verificaSobreposicaoShapeUsandoContains(geometriaPrincipal, geometriaContida);
	}
	
	public boolean verificaPontoNoShapeUsandoContainsTheGeom(String locGeoPrincipal, LocalizacaoGeografica locGeoShapePrincipal, LocalizacaoGeografica locGeoPontoContida) throws Exception {
		String geometriaPrincipal = locGeoPrincipal;
		String geometriaContida = null;
		int geograficaSIRGAS2000 = 4674;
		
		String coordGeoNumerica = ("POINT("+locGeoPontoContida.getLatitudeInicial()+" "+locGeoPontoContida.getLongitudeInicial()+")").replaceAll(",", ".");
		int srid = Integer.valueOf(locGeoPontoContida.getIdeSistemaCoordenada().getSrid());
		int sridShape = Integer.valueOf(locGeoShapePrincipal.getIdeSistemaCoordenada().getSrid());
		
		if (isCoordenadaUTM(locGeoPontoContida) && srid != geograficaSIRGAS2000 && sridShape == geograficaSIRGAS2000) {
			coordGeoNumerica = ("POINT("+locGeoPontoContida.getLatitudeInicial()+" "+locGeoPontoContida.getLongitudeInicial()+")").replaceAll(",", ".");
			geometriaContida =  validarGeoSeiaDAO.converterPonto(coordGeoNumerica, srid, geograficaSIRGAS2000);
		} else {
			geometriaContida = buscarGeometriaShape(locGeoPontoContida.getIdeLocalizacaoGeografica());
		}

		return validarGeoSeiaDAO.verificaSobreposicaoShapeUsandoContains(geometriaPrincipal, geometriaContida);
	}
	
	public boolean verificaSobreposicaoShapeUsandoContainsTheGeom(String theGeomPrincipal, String theGeomContida){
		return validarGeoSeiaDAO.verificaSobreposicaoShapeUsandoContains(theGeomPrincipal, theGeomContida);
	}
	
	private boolean isCoordenadaUTM(LocalizacaoGeografica lg) {
		return 
			Arrays.asList(new Integer[] {
				SistemaCoordenadaEnum.UTM_23_SAD69.getId(),
				SistemaCoordenadaEnum.UTM_23_SIRGAS_2000.getId(),
				SistemaCoordenadaEnum.UTM_24_SAD69.getId(),
				SistemaCoordenadaEnum.UTM_24_SIRGAS_2000.getId()
			}
		).contains(lg.getIdeSistemaCoordenada().getIdeSistemaCoordenada());
	}
	
	/**
	 * 
	 * Método que retorna os ide_localizacao_geografica sobrepostos pela theGeom. 
	 *
	 * @author ivanildo.souza
	 *
	 * @param theGeom
	 * @return Collection
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Integer> getCollectionIdeLocalizacaoGeograficaSobrepostaBy(String theGeom, Integer ideImovel) {
		return validarGeoSeiaDAO.getCollectionIdeLocalizacaoGeograficaSobrepostaBy(theGeom, ideImovel);
	}
	
	/**
	 * Método para verificar se o the_geom da primeira {@link LocalizacaoGeografica} intercepta o the_geom da segunda.
	 * 
	 * @author eduardo.fernandes 
	 * @since 23/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8434">#8434</a>
	 * @param locGeoA
	 * @param locGeoB
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isTheGeomIgual(LocalizacaoGeografica locGeoA, LocalizacaoGeografica locGeoB) throws Exception{
		return validarGeoSeiaDAO.isTheGeomIgual(locGeoA, locGeoB);
	}
	
	/**
	 * Método para verificar se a sobreposicao do limite do imóvel deve ser ignorada ou não
	 * 
	 * @author danilo.santos 
	 * @since 11/10/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/9665">#9665</a>
	 * @return boolean
	 */
	private boolean executarValidacaoSobreposicaoLimiteImovel(String theGeom, ImovelRural imovel) throws Exception{
		boolean retorno = true;
		
		//O imóvel rural não marcou a opção "Deseja completar suas informações no Cadastro Estadual Florestal de Imóveis Rurais?"
		if(!imovel.getIndDesejaCompletarInformacoes()) {
			return false;
		} else {
			Collection<Integer> listaIdeLocalizacaoGeografica = localizacaoGeograficaServiceFacade.getCollectionLocalizacaoGeograficaSobrepostaBy(theGeom, imovel.getIdeImovelRural());
			
			if(listaIdeLocalizacaoGeografica.size() > 0){
				List<ImovelRural> listaImoveis = new ArrayList<ImovelRural>();
				
				for (ImovelRural imovelRural : imovelRuralService.listarImoveisSobrepostosPorId(listaIdeLocalizacaoGeografica, imovel)) {
					if(!imovelRural.isRegistrado()) {
						listaImoveis.add(imovelRural);
					}
				}
				
 				for (ImovelRural imovelRural : listaImoveis) {
					if(!imovelRural.getIndDesejaCompletarInformacoes()){
						retorno = false;
					} else {
						return true;
					}
				}
			}
		}
		
		return retorno;
	}
		
	public double getAreaShapeTemporario(String geometria) throws Exception {
		return validarGeoSeiaDAO.buscarAreaGeorreferenciadaShapeTemporario(geometria);
	}
	
	public void validarReservaAreaDeclaradaShapeTemporario(Double areaDeclarada, Double areaShape)throws Exception { 
		Double percentual = (areaShape/areaDeclarada)*100;
		//Arredonda o percentual para apenas uma casa decimal
		percentual = Util.converterDoubleUmaCasa(percentual);
		
		if(percentual < 100 || percentual > 110) {
			throw new AreaDeclaradaInvalidaException(areaDeclarada, areaShape);
		}
	}
	
	public void validarValorAreaDeclaradaShapeTemporario(Double areaDeclarada, Double areaShape)throws Exception { 
		Double percentual = (areaShape/areaDeclarada)*100;
		//Arredonda o percentual para apenas uma casa decimal
		percentual = Util.converterDoubleUmaCasa(percentual);
		
		if(percentual < 90 || percentual > 110) {
			throw new AreaDeclaradaInvalidaException(areaDeclarada, areaShape);
		}
	}
	
	public void validaSobreposicaoImovelIncra(String geometria, ImovelRural imovelRural) throws SobreposicaoAreasException, Exception {
		if(!validarGeoSeiaDAO.validaSobreposicaoImovelIncra(geometria, imovelRural.getIdeImovelRural())) {
			if(imovelSuspensaoService.listaImoveisSobrepostos(geometria, imovelRural)){
				throw new  ImovelSuspensoException("Caro usuário, o limite do seu imóvel rural sobrepõe uma área suspensa para novos cadastros por decisão judicial. Para maiores informações, por favor, entre em contato com o Service Desk do SEIA, por meio do email atendimento.seia@inema.ba.gov.br");
			}else{
				throw new SobreposicaoAreasException(geometria);
			}
		}
	}
	
	public boolean validaSobreposicaoImovelTerritorio(String geometria, Integer ideImovelRural) throws Exception {
		return validarGeoSeiaDAO.validaSobreposicaoImovel(geometria, ideImovelRural);
	}
	
	public boolean validaSobreposicaoImovelTerritorioPCT(String geometria, Integer ideImovelRural) throws Exception {
		return validarGeoSeiaDAO.validaSobreposicaoImovelPCT(geometria, ideImovelRural);
	}
	
	public void validarRlComunidadePCT(final String geometriaRl, final int tipo, ImovelRural imovelRural) throws Exception,SeiaException {
		List<Integer> localizacoes = validarGeoSeiaDAO.listarLocalizacaoGeograficaComSobreposicao(null, geometriaRl, tipo);
		
		if(!Util.isNullOuVazio(localizacoes)) {
			for(Integer loc: localizacoes) {
				if(!loc.equals(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()) && validaPercentualSobreposicao(buscarGeometriaShape(loc), geometriaRl)) {
					throw new SeiaException("O shapefile da Reserva Legal sobrepõe a área da comunidade de outro PCT.");
				}
			}
		}
	}
	
	public void validarComunidadeRlPCT(final String geometriaIm, final int tipo, ImovelRural imovelRural) throws Exception,SeiaException {
		List<Integer> localizacoes = validarGeoSeiaDAO.listarLocalizacaoGeograficaComSobreposicao(null, geometriaIm, tipo);
		
		if(!Util.isNullOuVazio(localizacoes)) {
			for(Integer loc: localizacoes) {
				if(!Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica()) && !loc.equals(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()) && validaPercentualSobreposicao(buscarGeometriaShape(loc), geometriaIm)) {
					throw new SeiaException("O shapefile da comunidade sobrepõe a área da Reserva legal de outro PCT.");
				}
			}
		}
	}
}
