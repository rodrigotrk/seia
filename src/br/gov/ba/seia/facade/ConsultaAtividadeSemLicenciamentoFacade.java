package br.gov.ba.seia.facade;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicTipoStatus;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.TipoAtividadeNaoSujeitaLicenciamento;
import br.gov.ba.seia.entity.TipoCertificado;
import br.gov.ba.seia.enumerator.EstadoEnum;
import br.gov.ba.seia.enumerator.OrgaoEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitaLicService;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitaLicStatusService;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitaLicTipoStatusService;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.PermissaoPerfilService;
import br.gov.ba.seia.service.TipoAtividadeNaoSujeitaLicenciamentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @since 07/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConsultaAtividadeSemLicenciamentoFacade {
	
	@EJB
	private TipoAtividadeNaoSujeitaLicenciamentoService tipoAtividadeService; 
	
	@EJB
	private CadastroAtividadeNaoSujeitaLicTipoStatusService tipoStatusService;
	
	@EJB
	private CadastroAtividadeNaoSujeitaLicService cadastroService;

	@EJB
	private CadastroAtividadeNaoSujeitaLicStatusService cadastroStatusService;

	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private PermissaoPerfilService permissaoPerfilService;
	
	@EJB
	private CertificadoService certificadoService;


	/**
	 * Método para obter os {@link Municipio} da <b>Bahia</b>.
	 * 
	 * @author eduardo.fernandes 
	 * @since 07/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> listarLocalidades() throws Exception {
		return (List<Municipio>) municipioService.filtrarListaMunicipiosPorEstado(new Estado(EstadoEnum.BAHIA.getId()));
	}

	/**
	 * Método que vai retornar uma lista de {@link CadastroAtividadeNaoSujeitaLicTipoStatus}. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 07/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicTipoStatus> listarTipoStatus() throws Exception {
		return tipoStatusService.listarTipoStatus();
	}

	/**
	 * Método que vai retornar uma lista de {@link TipoAtividadeNaoSujeitaLicenciamento}. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 07/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAtividadeNaoSujeitaLicenciamento> listarAtividadesSemLicenciamentoAmbiental() throws Exception {
		return tipoAtividadeService.listarTipoAtividade();
	}

	/**
	 * TODO ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a> TODO ADICIONAR TICKET
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLic> listarPorCriteriaDemanda(Map<String, Object> map) throws Exception {
		return cadastroService.consultarCadastro(map);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer consultarCount(Map<String, Object> map) throws Exception {
		return cadastroService.consultarCount(map);
	}
	
	/**
	 * TODO ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a> TODO ADICIONAR TICKET
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer count(Map<String, Object> map) throws Exception {
		return cadastroService.countConsultarCadastro(map);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCadastro(CadastroAtividadeNaoSujeitaLic cadastro) throws Exception {
		cadastroService.salvar(cadastro);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Integer> listarPessoasQuePodemSerConsultadasPeloUsuario() {
		try {
			return permissaoPerfilService.listarIdesPessoasAptas(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		} 
	}

	/**
	 * TODO ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes 
	 * @since 22/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8192">#8192</a>
	 * @param cadastro
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCertificado(CadastroAtividadeNaoSujeitaLic cadastro) throws Exception {
		Certificado certificado = null;
		if(cadastro.getTipoAtividadeNaoSujeitaLicenciamento().getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.PERFURACAO_DE_POCOS.getIde())){
			certificado = montarCertificado(cadastro, TipoCertificadoEnum.CAEPOG);
		} 
		else if(cadastro.getTipoAtividadeNaoSujeitaLicenciamento().getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.PESQUISA_MINERAL.getIde())){
			certificado = montarCertificado(cadastro, TipoCertificadoEnum.PESQUISA_MINERAL);
		}
		else if(cadastro.getTipoAtividadeNaoSujeitaLicenciamento().getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.SILOS_E_ARMAZENS.getIde())){
			certificado = montarCertificado(cadastro, TipoCertificadoEnum.SILOS_ARMAZEM);
		}
		else if(cadastro.getTipoAtividadeNaoSujeitaLicenciamento().getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.INSTALACAO_DE_TORRES.getIde())){
			certificado = montarCertificado(cadastro, TipoCertificadoEnum.TORRES_ANEMOMETRICAS);
		}
		if(!Util.isNull(certificado)){
			certificadoService.salvar(certificado);
			/*certificadoService.salvarCertificado(certificado);*/
			cadastro.setIdeCertificado(certificado);
			cadastroService.salvar(cadastro);
		}
	}

	private Certificado montarCertificado(CadastroAtividadeNaoSujeitaLic cadastro, TipoCertificadoEnum tipoCertificadoEnum) throws Exception {
		Certificado certificado = new Certificado();
		certificado.setIdeOrgao(new Orgao(OrgaoEnum.INEMA));
		certificado.setTipoCertificado(new TipoCertificado(tipoCertificadoEnum.getId()));
		certificado.setNumCertificado(gerarNumeroCertificado(certificado));
		certificado.setDtcEmissaoCertificado(obterDataDeConclusaoDoCadastro(cadastro));
		return certificado;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String gerarNumeroCertificado(Certificado certificado) throws Exception{
		return certificadoService.gerarNumeroCertificadoByTipo(certificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Date obterDataDeConclusaoDoCadastro(CadastroAtividadeNaoSujeitaLic cadastro) throws Exception {
		return cadastroStatusService.buscarStatusConcluido(cadastro).getDtcStatus();
	}
	
}