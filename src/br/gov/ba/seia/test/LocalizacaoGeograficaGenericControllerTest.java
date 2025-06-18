package br.gov.ba.seia.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.gov.ba.seia.controller.LocalizacaoGeograficaGenericController;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.GeoReferenciavel;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.service.ParamPersistDadoGeoService;

@RunWith(MockitoJUnitRunner.class)
public class LocalizacaoGeograficaGenericControllerTest {

	@InjectMocks
    private LocalizacaoGeograficaGenericController localizacaoGeograficaGenericController;

    @Mock
    private GeoReferenciavel geoReferenciavel;

    @Mock
    private ArquivoProcesso arquivoProcesso;

    @Mock
    private MotivoNotificacao motivoNotificacao;

    @Mock
    private ParamPersistDadoGeoService paramPersistDadoGeoService;
    
    @Mock
    private LocalizacaoGeografica localizacaoGeograficaSelecionada = new LocalizacaoGeografica();

    @Before
    public void setUp() {
    	paramPersistDadoGeoService = mock(ParamPersistDadoGeoService.class);
    }
    
    @Test
    public void testPersistirShapesWithImovelAndNotNullConditions() throws Exception {

        Imovel imovel = mock(Imovel.class);
        Endereco endereco = mock(Endereco.class);
        Logradouro logradouro = mock(Logradouro.class);
        Municipio municipio = mock(Municipio.class);

        when(imovel.getIdeEndereco()).thenReturn(endereco);
        when(endereco.getIdeLogradouro()).thenReturn(logradouro);
        when(logradouro.getIdeMunicipio()).thenReturn(municipio);
        when(municipio.getCoordGeobahiaMunicipio()).thenReturn(imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());

        localizacaoGeograficaGenericController.persistirShapes(1);

        verify(paramPersistDadoGeoService).salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, true, imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), false, 1);
        verify(paramPersistDadoGeoService).salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), false, 1);
    }

    @Test
    public void testPersistirShapesWithEmpreendimentoAndNotNullEnderecoParaValidacao() throws Exception {
    	 Empreendimento empreendimento = mock(Empreendimento.class);
         Endereco endereco = mock(Endereco.class);
         Logradouro logradouro = mock(Logradouro.class);
         Bairro bairro = mock(Bairro.class);
         Municipio municipio = mock(Municipio.class);
         
         when(empreendimento.getEnderecoParaValidacao()).thenReturn(endereco);
         when(endereco.getIdeLogradouro()).thenReturn(logradouro);
         when(logradouro.getIdeBairro()).thenReturn(bairro);
         when(bairro.getIdeMunicipio()).thenReturn(municipio);

         localizacaoGeograficaGenericController.persistirShapes(1);
         
         verify(paramPersistDadoGeoService).salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, true, empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeBairro().getIdeMunicipio().getCoordGeobahiaMunicipio(), false, 0);
         verify(paramPersistDadoGeoService).salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeBairro().getIdeMunicipio().getCoordGeobahiaMunicipio(), false, 1);
         
         
    }

    @Test
    public void testPersistirShapesWithEmpreendimentoAndNullEnderecoParaValidacao() throws Exception {
        Empreendimento empreendimento = mock(Empreendimento.class);

        localizacaoGeograficaGenericController.persistirShapes(1);

        verify(paramPersistDadoGeoService).salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, true, empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), false, 1);
        verify(paramPersistDadoGeoService).salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), false, 0);
    }

    @Test
    public void testPersistirShapesWithEmpreendimentoAndNotNullConditions() throws Exception {
        Empreendimento empreendimento = mock(Empreendimento.class);
        Endereco endereco = mock(Endereco.class);
        Logradouro logradouro = mock(Logradouro.class);
        Bairro bairro = mock(Bairro.class);
        Municipio municipio = mock(Municipio.class);

        when(empreendimento.getEnderecoParaValidacao()).thenReturn(endereco);
        when(endereco.getIdeLogradouro()).thenReturn(logradouro);
        when(logradouro.getIdeBairro()).thenReturn(bairro);
        when(bairro.getIdeMunicipio()).thenReturn(municipio);
        when(municipio.getCoordGeobahiaMunicipio()).thenReturn(municipio.getCoordGeobahiaMunicipio());

        localizacaoGeograficaGenericController.persistirShapes(1);

        verify(paramPersistDadoGeoService).salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, true, municipio.getCoordGeobahiaMunicipio(), false, 1);
        verify(paramPersistDadoGeoService).salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, municipio.getCoordGeobahiaMunicipio(), true, 0);
    }

    @Test
    public void testPersistirShapesWithEmpreendimentoAndNullConditions() throws Exception {
        Empreendimento empreendimento = mock(Empreendimento.class);

        when(empreendimento.getEnderecoParaValidacao()).thenReturn(null);

        localizacaoGeograficaGenericController.persistirShapes(1);

        verify(paramPersistDadoGeoService).salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, true, empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), false, 1);
        verify(paramPersistDadoGeoService).salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), true, 0);
    }
    
    
    //somente novo método
    @Test
    public void deveRetornarFalsoParaMotivoNotificacaoRealocacao() {
    	ArquivoProcesso arq = (ArquivoProcesso) geoReferenciavel;
        Mockito.when(arq.getMotivoNotificacao()).thenReturn(motivoNotificacao);
        Mockito.when(motivoNotificacao.getIdeMotivoNotificacao()).thenReturn(16);
        boolean resultado = localizacaoGeograficaGenericController.validarMunicipio();
        assertFalse(resultado);
    }

    @Test
    public void deveRetornarVerdadeiroParaOutroMotivoNotificacao() {
    	ArquivoProcesso arq = (ArquivoProcesso) geoReferenciavel;
        Mockito.when(arq.getMotivoNotificacao()).thenReturn(motivoNotificacao);
        Mockito.when(motivoNotificacao.getIdeMotivoNotificacao()).thenReturn(1);
        boolean resultado = localizacaoGeograficaGenericController.validarMunicipio();
        assertTrue(resultado);
    }

    @Test
    public void deveRetornarVerdadeiroParaGeoReferenciavelNulo() {
        boolean resultado = localizacaoGeograficaGenericController.validarMunicipio();
        assertTrue(resultado);
    }
    
}
