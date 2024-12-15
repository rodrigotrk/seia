package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;

import br.gov.ba.seia.dao.CertificadoDAOImpl;
import br.gov.ba.seia.dto.RequerimentoUnicoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.CaepogCertificado;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoCertificado;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.TokenUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CertificadoService {

	@Inject
	CertificadoDAOImpl certificadoDAOImpl;
	
	@Inject
	AtoAmbientalService atoAmbientalService;
	
	@Inject
	private CertificadoUtil certificadoUtil;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CaepogCertificado getCertificadoByideCaepog(Caepog caepog) {
		return	certificadoDAOImpl.getCertificadoByideCaepog(caepog);	
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Certificado certificado)  {
		this.gerarEValidarToken(certificado);
		salvarCertificado(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCertificado(Certificado certificado)  {
		this.certificadoDAOImpl.salvar(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaepogCertificado(CaepogCertificado caepogCertificado)  {
		this.certificadoDAOImpl.salvarCaepogCertificado(caepogCertificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarComprovante(Requerimento requerimento,TipoCertificadoEnum tipoComprovante)  {
		if(!this.exists(requerimento.getIdeRequerimento())){
			Certificado certificado = CertificadoUtil.gerarCertificadoWithoutNumero(requerimento,tipoComprovante);
			this.salvar(certificado);
		}		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarCertificado(RequerimentoUnicoDTO requerimentoUnicoDTO) {
		Requerimento requerimento = requerimentoUnicoDTO.getRequerimentoUnico().getRequerimento();
		List<Certificado> certificadosDoRequerimento = this.carregarByIdRequerimento(requerimento.getIdeRequerimento());
		for (AtoAmbiental atoAmbiental : requerimentoUnicoDTO.getRequerimentoUnico().getAtosAmbientais()) {
			if(atoAmbiental.getIndDeclaratorio()){
				Certificado certificado = certificadoUtil.gerarCertificado(atoAmbiental, requerimento);
				if(!certificadosDoRequerimento.contains(certificado)){
					String numeroCertificado = this.gerarNumeroCertificado(certificado);
					certificado.setNumCertificado(numeroCertificado);
					this.salvar(certificado);
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarCertificadoNovoReq(Requerimento requerimento) {
		List<Certificado> certificadosDoRequerimento = this.carregarByIdRequerimento(requerimento.getIdeRequerimento());
		requerimento.setAtosAmbientais(atoAmbientalService.listarAtoAmbientalPorEnquadramentoRequerimento(requerimento.getIdeRequerimento()));
		
		for (AtoAmbiental atoAmbiental : requerimento.getAtosAmbientais()) {
			
			if(atoAmbiental.getIndDeclaratorio()){
				Certificado certificado = certificadoUtil.gerarCertificado(atoAmbiental, requerimento);
				
				if(!certificadosDoRequerimento.contains(certificado)){
					String numeroCertificado = this.gerarNumeroCertificado(certificado);
					certificado.setNumCertificado(numeroCertificado);
					this.salvar(certificado);
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarEValidarToken(Certificado certificado) {
		try {

			boolean tokenValido = false;
			while (!tokenValido) {
				String token = TokenUtil.gerarCodigoAcesso();

				tokenValido = validarToken(token);
				if (tokenValido) {
					certificado.setNumToken(token);
				}
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean validarToken(String token)  {
		return this.certificadoDAOImpl.validarToken(token);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Certificado certificado)  {
		this.certificadoDAOImpl.atualizar(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarById(Integer ideCertificado)  {
		return this.certificadoDAOImpl.carregarById(ideCertificado);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarByIdRequerimentoAndAtoLac(Integer requerimento)  {
		return this.certificadoDAOImpl.carregarByIdRequerimentoAndAto(requerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarNumeroCertificado(Certificado certificado)  {
		Integer ideAtoAmbiental = certificado.getIdeAtoAmbiental().getIdeAtoAmbiental();
		String ultimoNumeroCertificado = obterUltimoNumeroCertificado(ideAtoAmbiental);
		return this.gerarNovoNumeroCertificado(certificado, ultimoNumeroCertificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarNumeroCertificadoByTipo(Certificado certificado)  {
		return this.gerarNovoNumeroCertificado(certificado, StringUtils.EMPTY);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String obterUltimoNumeroCertificado(Integer ideAmbiental)  {
		Certificado ultimoCertificado = this.certificadoDAOImpl.obterUltimoCertificado(ideAmbiental);
		return Util.isNull(ultimoCertificado) ? null : ultimoCertificado.getNumCertificado();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String obterUltimoNumeroCertificadoByTipo(Integer ideCertificado)  {
		Certificado ultimoCertificado = this.certificadoDAOImpl.obterUltimoCertificadoByTipo(ideCertificado);
		return  Util.isNull(ultimoCertificado) ? null : ultimoCertificado.getNumCertificado();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String gerarNovoNumeroCertificado(Certificado certificado, String ultimoNumero) {
		return CertificadoUtil.obterProximoNumeroCertificado(certificado, ultimoNumero,false);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Certificado> carregarByIdRequerimento(Integer ideRequerimento)  {
		return this.certificadoDAOImpl.carregarByIdRequerimento(ideRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Certificado> carregarByIdeImovelAndTipo(Integer ideImovel, Integer ideTipo)  {
		return this.certificadoDAOImpl.carregarByIdeImovelAndTipo(ideImovel, ideTipo);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarByToken(String token)  {
		return this.certificadoDAOImpl.carregarByToken(token);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarCertificadoByide(Integer ideCertificado) {
		return certificadoDAOImpl.carregarCertificadoByIde(ideCertificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarCertificadoByNumCertificado(String numCertificado) {
		return certificadoDAOImpl.carregarCertificadoByNumCertificado(numCertificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarInformacoesRequerimentoByCertificado(Integer ideCertificado)  {
		return this.certificadoDAOImpl.carregarInformacoesRequerimentoByCertificado(ideCertificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarInformacoesCerhByCertificado(Integer ideCertificado)  {
		return this.certificadoDAOImpl.carregarInformacoesCerhByCertificado(ideCertificado);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasToken(Integer ideRequerimento)  {
		return this.certificadoDAOImpl.hasToken(ideRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean exists(Integer ideRequerimento)  {
		List<Certificado> listCertificados = carregarByIdRequerimento(ideRequerimento);
		return !Util.isNull(listCertificados) && !listCertificados.isEmpty();
	}
	
	
	public void limparNumeroTokenCertificadoDoImovel(ImovelRural imovelRural)  {
	    List<Certificado> certificado = null;  
	    if(imovelRural.isTermoCompromisso()) {
		certificado = carregarByIdeImovelAndTipo(imovelRural.getIdeImovelRural(), TipoCertificadoEnum.TERMO_DE_COMPROMISSO.getId());
	    }else if(imovelRural.getStsCertificado()) {
		certificado = carregarByIdeImovelAndTipo(imovelRural.getIdeImovelRural(), TipoCertificadoEnum.CEFIR.getId());
	    }
	    
	    if(!Util.isNullOuVazio(certificado)) {
        	    Certificado c = certificado.get(0);
        	    c.setNumToken(null);
        	    atualizar(c);
	    }
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado obterUltimoCertificadoPorRequerimento(Integer ideRequerimento)  {
	    return certificadoDAOImpl.obterUltimoCertificadoPorRequerimento(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado obterUltimoCertificadoPorTipo(TipoCertificadoEnum tipoEnum)  {
		return certificadoDAOImpl.obterUltimoCertificadoPorTipo(tipoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado buscarCertificadoCadastro(String numCertificado) {
		return certificadoDAOImpl.buscarCertificadoParaCadastro(numCertificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarByIdRequerimentoAndAtoDTRP(Integer requerimento, AtoAmbientalEnum atoEnum)  {
		return this.certificadoDAOImpl.carregarByIdRequerimentoAndAto(requerimento, atoEnum);
	}
	
	public boolean hasToken(Integer ideRequerimento, AtoAmbientalEnum atoEnum)  {
		return this.certificadoDAOImpl.hasToken(ideRequerimento, atoEnum);
	}
	
	//#9330
	public String obterProximaSequencePorTipoCertificado(TipoCertificado tipoCertificado, Boolean isDeclaracao)  {
		if (tipoCertificado == null	|| tipoCertificado.getIdeTipoCertificado() == null) {
			return "";
		} else {
			
			String sequence = StringUtils.EMPTY;	
			TipoCertificadoEnum tipoCertifEnum = null;
			switch (tipoCertificado.getIdeTipoCertificado()) {
			case 2:
				sequence = "tipo_certificado_cefir_seq";
				tipoCertifEnum = TipoCertificadoEnum.getEnum(2);
				break;
			case 3:
				sequence = "tipo_certificado_tc_seq";
				tipoCertifEnum = TipoCertificadoEnum.getEnum(3);
				break;
				
			case 4:
				sequence = "tipo_certificado_avbndes_seq";
				tipoCertifEnum = TipoCertificadoEnum.getEnum(4);
				break;

			case 7:
				sequence = "tipo_certificado_cerh_seq";
				tipoCertifEnum = TipoCertificadoEnum.getEnum(7);
				break;
			
			case 8:
				sequence = "tipo_certificado_csa_seq";
				tipoCertifEnum = TipoCertificadoEnum.getEnum(7);
				break;
				
			case 9:	
				sequence = "tipo_certificado_cta_seq";
				tipoCertifEnum = TipoCertificadoEnum.getEnum(9);
				break;
				
			case 10:
				sequence = "tipo_certificado_crf_seq";
				tipoCertifEnum = TipoCertificadoEnum.getEnum(8);
				break;				
				
			default:
				break;
			}
			
			boolean existeCertificado = false;
			
			if(isDeclaracao){
				existeCertificado = this.certificadoDAOImpl.existsDeclaracaoAnoPorTipo(tipoCertifEnum);
			}else{
				existeCertificado = this.certificadoDAOImpl.existsCertificadoAnoPorTipo(tipoCertifEnum);
			}

			if(existeCertificado){
				return this.certificadoDAOImpl.obterProximaSequencePorTipoCertificado(sequence);
			}else{
				return this.certificadoDAOImpl.resetSequencePorTipoCertificado(sequence);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarByIdRequerimentoAndAto(Integer requerimento, AtoAmbientalEnum atoEnum)  {
		return this.certificadoDAOImpl.carregarByIdRequerimentoAndAto(requerimento, atoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarNumeroCertificadoInexigibilidade(Certificado certificado)  {
		Integer ideAtoAmbiental = certificado.getIdeAtoAmbiental().getIdeAtoAmbiental();
		String ultimoNumeroCertificado = obterUltimoNumeroCertificado(ideAtoAmbiental);
		return CertificadoUtil.obterProximoNumeroCertificadoInexigibilidade(certificado, ultimoNumeroCertificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Certificado> listarPorNumeroCertificado(String token){
		return certificadoDAOImpl.listarPorNumeroCertificado(token);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel carregarImovelByCertificado(Integer ideCertificado) throws Exception {
		return certificadoDAOImpl.carregarImovelByCertificado(ideCertificado);
	}
}