package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhStatus;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoCertificado;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.util.Util;


/**
 * @author eduardo.fernandes 
 * @since 25/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8447">#8447</a>
 *
 */
public class CertificadoDTO {

	private Certificado certificado;

	private String numero;
	private String nomRequerente;
	private String numCpfCnpj;
	private String endereco;
	private boolean podeImprimir;
	
	public CertificadoDTO(Certificado certificado){
		this.certificado = certificado;
		this.podeImprimir = true;
	}
	
	public CertificadoDTO(Certificado certificado, Requerimento requerimento) {
		this(certificado);
		this.numero = requerimento.getNumRequerimento();
		this.nomRequerente = requerimento.getRequerente().getNomeRazao();
		this.numCpfCnpj = requerimento.getRequerente().getCpfCnpjFormatado();
		
		if (!Util.isNullOuVazio(requerimento.getUltimoEmpreendimento())) {
			if(!Util.isNullOuVazio(requerimento.getUltimoEmpreendimento().getEndereco())) {
				this.endereco = requerimento.getUltimoEmpreendimento().getEndereco().getEnderecoCompleto();				
			}			
		}
	}
	
	public CertificadoDTO(Certificado certificado, CadastroAtividadeNaoSujeitaLic cadastro) {
		this(certificado);
		this.numero = cadastro.getNumCadastro();
		this.nomRequerente = cadastro.getNomRequerente();
		this.numCpfCnpj = cadastro.getNumCpfcnpjRequerente();
		this.endereco = cadastro.getIdeEmpreendimento().getEndereco().getEnderecoCompleto();
	}
	
	public CertificadoDTO(Certificado certificado, Cerh cerh) {
		this(certificado);
		this.numero = cerh.getNumCadastro();
		this.nomRequerente = cerh.getIdePessoaRequerente().getNomeRazao();
		this.numCpfCnpj = cerh.getIdePessoaRequerente().getCpfCnpjFormatado();
		this.endereco = cerh.getIdeEmpreendimento().getEndereco().getEnderecoCompleto();
		this.podeImprimir = new CerhStatus(CerhStatusEnum.CADASTRO_COMPLETO).equals(cerh.getIdeCerhStatus());
	}

	public CertificadoDTO(Certificado certificado, Requerimento requerimento, Imovel imovel) {
		this(certificado);
		this.numero = requerimento.getNumRequerimento();
		this.nomRequerente = imovel.getImovelRural().getIdeRequerenteCadastro().getNomeRazao();
		this.numCpfCnpj = imovel.getImovelRural().getIdeRequerenteCadastro().getCpfCnpjFormatado();
		
		if(!Util.isNullOuVazio(imovel.getIdeEndereco())){
			this.endereco = imovel.getIdeEndereco().getEnderecoCompleto();
		}
	}

	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getNomRequerente() {
		return nomRequerente;
	}
	
	public void setNomRequerente(String nomRequerente) {
		this.nomRequerente = nomRequerente;
	}
	
	public String getNumCpfCnpj() {
		return numCpfCnpj;
	}
	
	public void setNumCpfCnpj(String numCpfCnpj) {
		this.numCpfCnpj = numCpfCnpj;
	}
	
	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Certificado getCertificado() {
		return certificado;
	}
	
	public boolean isExisteCertificado(){
		return !Util.isNull(certificado) && !Util.isNull(certificado.getIdeCertificado()); 
	}
	
	public boolean isCertificadoDeRequerimento(){
		return isExisteCertificado() && (Util.isNull(certificado.getTipoCertificado()) || isDla());
	}

	public boolean isExisteTipoCertificado(){
		return !Util.isNull(certificado.getTipoCertificado());
	}
	
	public boolean isCertificadoDeCadastro(){
		return isExisteCertificado() 
				&& isExisteTipoCertificado()
				&& (certificado.getTipoCertificado().equals(new TipoCertificado(TipoCertificadoEnum.CAEPOG.getId()))
				|| certificado.getTipoCertificado().equals(new TipoCertificado(TipoCertificadoEnum.PESQUISA_MINERAL.getId()))
				|| certificado.getTipoCertificado().equals(new TipoCertificado(TipoCertificadoEnum.SILOS_ARMAZEM.getId()))
				|| certificado.getTipoCertificado().equals(new TipoCertificado(TipoCertificadoEnum.TORRES_ANEMOMETRICAS.getId())));
	}
	
	public boolean isCertificadoDeImovelRural(){
		return isExisteCertificado() 
				&& isExisteTipoCertificado()
				&& (certificado.getTipoCertificado().equals(new TipoCertificado(TipoCertificadoEnum.CEFIR.getId()))
				|| certificado.getTipoCertificado().equals(new TipoCertificado(TipoCertificadoEnum.AVISO_BNDES.getId()))
				|| certificado.getTipoCertificado().equals(new TipoCertificado(TipoCertificadoEnum.TERMO_DE_COMPROMISSO.getId())));
	}
	
	public boolean isCertificadoDeCerh(){
		return isExisteCertificado() 
				&& isExisteTipoCertificado()
				&& (certificado.getTipoCertificado().equals(new TipoCertificado(TipoCertificadoEnum.CERH.getId())));
	}

	public boolean isDla(){
		return isExisteTipoCertificado()
				&& certificado.getTipoCertificado().equals(new TipoCertificado(TipoCertificadoEnum.DLA.getId()));
	}
	
	public boolean isLac() {
		return certificado.getIdeAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.LAC.getId()));
	}

	public boolean isRfp() {
		return certificado.getIdeAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.REGISTRO_FLORESTA_PRODUCAO.getId()));
	}

	public boolean isRcfp() {
		return certificado.getIdeAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.CORTE_FLORESTA_PRODUCAO.getId()));
	}

	public boolean isDiap() {
		return certificado.getIdeAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.DIAP.getId()));
	}
	
	public boolean isApe() {
		return certificado.getIdeAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.APE.getId()));
	}
	
	public boolean isCrf() {
		return isExisteTipoCertificado()
				&& certificado.getTipoCertificado().equals(new TipoCertificado(TipoCertificadoEnum.CRF.getId()));
	}

	public boolean isPodeImprimir() {
		return podeImprimir;
	}
	
	public boolean isInexigibilidade(){
		return certificado.getIdeAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.INEXIGIBILIDADE.getId()));
	}
	
	public boolean isRlac() {
		return certificado.getIdeAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.RLAC.getId()));
	}
	
	public boolean isDtrp() {
		return certificado.getIdeAtoAmbiental().equals(new AtoAmbiental(AtoAmbientalEnum.DTRP.getId()));
	}
}