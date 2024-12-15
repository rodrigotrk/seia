package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;

import br.gov.ba.seia.entity.GeoJsonSicar;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.exception.CampoObrigatorioException;

/**
 * @author carlos.duarte
 *
 *Interface DAO responsável por acessar as funções de validações geográficas do SEIA.
 *O parâmetro Tipo usado nos métodos são representados por:
 *1 - IMÓVEL
 *2 - RESERVA LEGAL
 *3 - APP
 *4 - ÁREA PRODUTIVA
 *5 - VEGETAÇÃO NATIVA
 */
public interface ValidacaoGeoSeiaDAOIf {
	
	public String buscarGeometriaShape(Integer ideLocalizacao) ;	
	
	/**
	 * @author carlos.duarte
	 *
	 *Interface do método que retorna em um vetor a área declarada no cadastro do SEIA e 
	 *a área geométrica calculada a partir do Shape.
	 *RETORNO: vetor[0] = área declarada / vetor[1] = área geométrica calculada a partir do Shape
	 *
	 */
	public Double[] buscarAreaDeclaradaEGeorreferenciada(Integer ideLocalizacao) ;
	
	public Double buscarAreaGeorreferenciadaShapeTemporario(String geometria) ;
	
	public List<Integer> buscarCodigoIbgeMunicipioShape(String geometria) ;
	
	/**
	 * @author micael.coutinho
	 * 
	 * Método que retorna um boolean validando a área informada como parametro e 
	 * a área geométrica calculada a partir do Shape da localizacao geografica do parametro.
	 * 
	 * @param ideLocalizacao
	 * @param area
	 * @return Boolean - verdade se diferença entre as áreas for menor que 10%
	 * @
	 */
	public Boolean validaAreaComAreaDaLocalizacaoGeografica(Integer ideLocalizacao, Double area) ;
	
	/**
	 * @author kelson.borges
	 *
	 * Método que retorna uma lista de todas as localizações geográficas que intersectam a localização geográfica
	 * informada a partir do seu identificador ou da geometria em formato PostGIS
	 * 
	 * RETORNO: Lista de identificadores de localização geográfica
	 */
	public List<Integer> listarLocalizacaoGeograficaComSobreposicao(Integer ideLocalizacao, String geometria, Integer tipo) ;
	
	/**
	 * @author kelson.borges
	 *
	 * Método que retorna um vetor contendo as áreas georreferenciadas totais e a área de sobreposição de duas 
	 * localizações geográficas informadas a partir dos seus identificadores e/ou das suas geometrias em formato PostGIS
	 * 
	 * RETORNO: 
	 * 	vetor[0] = área total da localização geográfica A
	 * 	vetor[1] = área total da localização geográfica B
	 * 	vetor[2] = área sobreposta (comum as duas localizações geográficas)
	 *
	 */	
	public Double[] buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(Integer ideLocalizacaoA, String geometriaA, Integer ideLocalizacaoB, String geometriaB) ;
		
	/**
	 * @author carlos.duarte
	 *
	 *Método que retorna em um vetor com a área total de uma localização informada e a 
	 *área sobreposta dessa localização a partir do shape do processo cerberus.
	 *RETORNO: vetor[0] = área total da localização informada / vetor[1] = área sobreposta da localização informada a partir do shape do processo cerberus
	 *
	 */
	public Boolean validaShapeRlCerberus(String numeroProcesso, Integer ideLocalizacao) ;
	
	/**
	 * @author carlos.duarte
	 *
	 *Método que retorna a geometria do shape passado como string.
	 * @throws CampoObrigatorioException 
	 *
	 */
	public String[] buscarGeometriaShapeTemporarioCEFIR(Integer ideImovel, Integer tipo, String identificador) throws CampoObrigatorioException ;
	
	public boolean validaPercentualSobreposicao(String geometriaA, String geometriaB) throws Exception ;

	public List<String> validarAssentadoLote(Integer ideImovel, String listaDePontos, String geometriaImovel) throws Exception;

	public GeoJsonSicar buscarGeoJsonSicar(List<Integer> localizacoes) ;
	
	public String validaTipoGeometria(Integer ideLocalizacao, String geometria) ;
	
	public List<Object[]> listarImoveisCreditoRl(Integer ideImovelRural, String geometriaRl, Boolean proprioImovel) ;
	
	public List<Object[]> listarImoveisDebitoRl(Integer ideImovelRural, String geometriaIm) ;

	public Integer buscarQuantidadeTalhoesDeclarados(String geometria) throws Exception;
	
		
	/**
	 * @author carlos.duarte
	 * 
	 * Método que retorna um boolean validando a sobreposição 
	 * da área da geometria informada por parâmetro com outros imóveis ignorando o id do imóvel passado 
	 * por parâmetro que está sendo validado, seguindo o padrão de validação de sobreposição de imóveis do CAR.
	 * 
	 * @param geometria
	 * @param ideImovelRural
	 * @return Boolean - TRUE se não houver sobreposição e FALSE se houver sobreposição e não poder proseguir com o cadastro.
	 * @
	 */
	public boolean validaSobreposicaoImovel(String geometria, Integer ideImovelRural) ;
	
	public boolean isExcecaoRegraCompensacaoRlMaisDeUmImovel(Integer ideImovelRural) ;
	
	public boolean verificaSobreposicaoShapeUsandoContains(String geometriaA, String geometriaB);
	
	public Collection<Integer> getCollectionIdeLocalizacaoGeograficaSobrepostaBy(String theGeom, Integer ideImovel) ;
	
	public boolean validaSobreposicaoImovelPCT(String geometria, Integer ideImovelRural) throws Exception;
	
	public String converterPonto(String coordGeoNumerica, Integer sridCoord, Integer sridNovo)throws Exception;
	
	
	/**
	 * Método que verifica se aquela {@link LocalizacaoGeografica} está 100%
	 * interceptando o Estado da Bahia.
	 * 
	 * @author eduardo.fernandes
	 * @since 19/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param locGeo
	 * @return
	 * @
	 */
	public boolean isLocalizacaoGeograficaDentroDaBahia(LocalizacaoGeografica locGeo) ;
	
	/**
	 * Método para verificar se o the_geom da primeira {@link LocalizacaoGeografica} intercepta o the_geom da segunda.
	 * 
	 * @author eduardo.fernandes 
	 * @since 23/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8434">#8434</a>
	 * @param locGeoA
	 * @param locGeoB
	 * @return
	 */
	public boolean isTheGeomIgual(LocalizacaoGeografica locGeoA, LocalizacaoGeografica locGeoB) ;

	public String[] buscarGeometriaShapeTemporario(LocalizacaoGeografica localizacaoGeografica) throws Exception ;
	
	public boolean validaSobreposicaoImovelIncra(String geometria, Integer ideImovelRural) ;
	
	public boolean validaPercentualSobreposicaoPCT(String geometriaA, String geometriaB) throws Exception ;
	
}