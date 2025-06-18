package br.gov.ba.seia.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.service.CertificadoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LacServiceFacade {

	@EJB
	CertificadoService certificadoService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCertificadoLac(Certificado certificado) {
		String numeroCertificao = this.certificadoService.gerarNumeroCertificado(certificado);
		certificado.setNumCertificado(numeroCertificao);
		this.certificadoService.salvar(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTokenCertificadoLac(Certificado certificado) throws Exception {
		this.certificadoService.gerarEValidarToken(certificado);
		this.certificadoService.atualizar(certificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Certificado carregarCertificado(Integer ideRequerimento) throws Exception {
		return this.certificadoService.carregarByIdRequerimentoAndAtoLac(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasCertificado(Integer ideRequerimento) throws Exception {
		return this.certificadoService.exists(ideRequerimento);
	}

	public boolean hasToken(Integer ideRequerimento) throws Exception {
		return this.certificadoService.hasToken(ideRequerimento);
	}
	
}
