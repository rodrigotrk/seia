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
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.DeclaracaoParcialDae;
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
public class DeclaracaoParcialDaeService {

	@Inject
	CertificadoDAOImpl certificadoDAOImpl;
	
	@Inject
	AtoAmbientalService atoAmbientalService;
	
	@Inject
	private CertificadoUtil certificadoUtil;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DeclaracaoParcialDae certificado) {
		this.gerarEValidarToken(certificado);
		salvarCertificado(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCertificado(DeclaracaoParcialDae certificado) {
		this.certificadoDAOImpl.salvar(certificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarCertificado(RequerimentoUnicoDTO requerimentoUnicoDTO) {
		Requerimento requerimento = requerimentoUnicoDTO.getRequerimentoUnico().getRequerimento();
		List<DeclaracaoParcialDae> certificadosDoRequerimento = this.carregarByIdRequerimento(requerimento.getIdeRequerimento());
		for (AtoAmbiental atoAmbiental : requerimentoUnicoDTO.getRequerimentoUnico().getAtosAmbientais()) {
			if(atoAmbiental.getIndDeclaratorio()){
				DeclaracaoParcialDae declaracaoParcialDae = certificadoUtil.gerarDeclaracaoParcialDae(atoAmbiental, requerimento);
				if(!certificadosDoRequerimento.contains(declaracaoParcialDae)){
					String numeroCertificado = this.gerarNumeroCertificado(declaracaoParcialDae);
					declaracaoParcialDae.setNumDeclaracaoParcialDae(numeroCertificado);
					this.salvar(declaracaoParcialDae);
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarCertificadoNovoReq(Requerimento requerimento)  {
		List<DeclaracaoParcialDae> certificadosDoRequerimento = this.carregarByIdRequerimento(requerimento.getIdeRequerimento());
		requerimento.setAtosAmbientais(atoAmbientalService.listarAtoAmbientalPorEnquadramentoRequerimento(requerimento.getIdeRequerimento()));
		
		for (AtoAmbiental atoAmbiental : requerimento.getAtosAmbientais()) {
			
			if(atoAmbiental.getIndDeclaratorio()){
				DeclaracaoParcialDae certificado = certificadoUtil.gerarDeclaracaoParcialDae(atoAmbiental, requerimento);
				
				if(!certificadosDoRequerimento.contains(certificado)){
					String numeroCertificado = this.gerarNumeroCertificado(certificado);
					certificado.setNumDeclaracaoParcialDae(numeroCertificado);
					this.salvar(certificado);
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarEValidarToken(DeclaracaoParcialDae certificado) {
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
	public void atualizar(DeclaracaoParcialDae certificado)  {
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
	public String gerarNumeroCertificado(DeclaracaoParcialDae declaracao)  {
		Integer ideAtoAmbiental = declaracao.getIdeAtoAmbiental().getIdeAtoAmbiental();
		String ultimoNumeroCertificado = obterUltimoNumeroCertificado(ideAtoAmbiental);
		return this.gerarNovoNumeroCertificado(declaracao, ultimoNumeroCertificado);
	}
	


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String obterUltimoNumeroCertificado(Integer ideAmbiental) {
		Certificado ultimoCertificado = this.certificadoDAOImpl.obterUltimoCertificado(ideAmbiental);
		return Util.isNull(ultimoCertificado) ? null : ultimoCertificado.getNumCertificado();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String obterUltimoNumeroCertificadoByTipo(Integer ideCertificado)  {
		Certificado ultimoCertificado = this.certificadoDAOImpl.obterUltimoCertificadoByTipo(ideCertificado);
		return Util.isNull(ultimoCertificado) ? null : ultimoCertificado.getNumCertificado();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String gerarNovoNumeroCertificado(DeclaracaoParcialDae declaracao, String ultimoNumero) {
		return CertificadoUtil.obterProximoNumeroCertificado(declaracao, ultimoNumero,true);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoParcialDae> carregarByIdRequerimento(Integer ideRequerimento) {
		return this.certificadoDAOImpl.carregarDeclaracaoByIdRequerimento(ideRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Certificado> carregarByIdeImovelAndTipo(Integer ideImovel, Integer ideTipo) {
		return this.certificadoDAOImpl.carregarByIdeImovelAndTipo(ideImovel, ideTipo);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoParcialDae carregarByToken(String token) {
		return this.certificadoDAOImpl.carregarByTokenDeclaracao(token);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoParcialDae carregaDeclaracaoByide(Integer ideCertificado){
		return certificadoDAOImpl.carregarDeclaracaoByIde(ideCertificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoParcialDae carregarInformacoesRequerimentoByCertificado(Integer ideCertificado)  {
		return this.certificadoDAOImpl.carregarInformacoesRequerimentoByDeclaracao(ideCertificado);
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
	public boolean exists(Integer ideRequerimento) {
		List<DeclaracaoParcialDae> listCertificados = carregarByIdRequerimento(ideRequerimento);
		return !Util.isNull(listCertificados) && !listCertificados.isEmpty();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado obterUltimoCertificadoPorRequerimento(Integer ideRequerimento) {
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
	public Certificado carregarByIdRequerimentoAndAtoDTRP(Integer requerimento, AtoAmbientalEnum atoEnum) {
		return this.certificadoDAOImpl.carregarByIdRequerimentoAndAto(requerimento, atoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean hasToken(Integer ideRequerimento, AtoAmbientalEnum atoEnum) {
		return this.certificadoDAOImpl.hasToken(ideRequerimento, atoEnum);
	}
	
	//#9330
	public String ObterProximaSequencePorTipoCertificado(TipoCertificado tipoCertificado) {
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
				
			default:
				break;
			}
			
			boolean existeCertificado = this.certificadoDAOImpl.existsCertificadoAnoPorTipo(tipoCertifEnum);
			if(existeCertificado){
				return this.certificadoDAOImpl.obterProximaSequencePorTipoCertificado(sequence);
			}else{
				return this.certificadoDAOImpl.resetSequencePorTipoCertificado(sequence);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DeclaracaoParcialDae carregarByIdRequerimentoAndAto(Integer requerimento, AtoAmbientalEnum atoEnum) {
		return this.certificadoDAOImpl.carregarByIdRequerimentoAndAtoDeclaracao(requerimento, atoEnum);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DeclaracaoParcialDae carregarCertificado(Integer ideRequerimento, AtoAmbientalEnum atoEnum) {
		return this.carregarByIdRequerimentoAndAto(ideRequerimento,atoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTokenCertificado(DeclaracaoParcialDae certificado) {
		this.gerarEValidarToken(certificado);
		this.atualizar(certificado);
	}
	
}