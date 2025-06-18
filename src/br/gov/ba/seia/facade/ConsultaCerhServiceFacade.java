package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.CerhDAOImpl;
import br.gov.ba.seia.dao.CerhTipoStatusDAOImpl;
import br.gov.ba.seia.dao.ContratoConvenioDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhStatus;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.GeoBahia;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.TipoCertificado;
import br.gov.ba.seia.enumerator.EstadoEnum;
import br.gov.ba.seia.enumerator.OrgaoEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.enumerator.TipoRelatorioEnum;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.ImpressoraService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConsultaCerhServiceFacade {

	@Inject
	private IDAO<GeoBahia> daoGeobahia;

	@Inject
	private ImpressoraService impressoraService;

	@EJB
	private CerhDAOImpl cerhDAOImpl;

	@EJB
	private CerhTipoStatusDAOImpl cerhTipoStatusDAOImpl;

	@EJB
	private ContratoConvenioDAOImpl contratoConvenioDAOImpl;

	@EJB
	private MunicipioService municipioService;

	@EJB
	private CertificadoService certificadoService;


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Cerh> listarCerh(Integer first, Integer pageSize, Map<String, Object> params) throws Exception {
		return cerhDAOImpl.listarCerh(first, pageSize, params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer listarCerhCount(Map<String, Object> params) throws Exception {
		return cerhDAOImpl.listarCerhCount(params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Municipio> listarMunicipioBahia() throws Exception {
		return municipioService.filtrarListaMunicipiosPorEstado(new Estado(EstadoEnum.BAHIA.getId()));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhStatus> listarCerhTipoStatus() throws Exception {
		return cerhTipoStatusDAOImpl.listaCerhTipoStatus();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ContratoConvenio> listarContratoConvenio(PessoaJuridica pj) throws Exception {
		return contratoConvenioDAOImpl.listaContratoConvenio(pj);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ContratoConvenio> listarContratoConvenio() throws Exception {
		return contratoConvenioDAOImpl.listaContratoConvenio();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<GeoBahia> listarRPGA() throws Exception {
		List<GeoBahia> listGeobahia = new ArrayList<GeoBahia>();
		String sql = new String("SELECT gid, nom_rpga FROM geo_rpga ORDER BY nom_rpga");
		List<GeoBahia> buscarPorNativeQuery = daoGeobahia.buscarPorNativeQuery(sql, null);
		for (Object geoBahia : buscarPorNativeQuery) {
			Object[] obj = ((Object[])geoBahia);
			GeoBahia objGeoBahia = new GeoBahia();
			objGeoBahia.setGid(Integer.parseInt(obj[0].toString()));
			objGeoBahia.setNome(obj[1].toString());

			listGeobahia.add(objGeoBahia);
		}
		return listGeobahia;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCertificado(Cerh cerh) throws Exception {
		Certificado certificado = montarCertificado(cerh, TipoCertificadoEnum.CERH);
		if(!Util.isNull(certificado)){
			certificadoService.salvar(certificado);
			cerh.setIdeCertificado(certificado);


			/*A pessoa fisicaCadastro nunca deveria ser uma pessoa jurida,
			 * mas o sistema permite, e por conta disso esse if é necessario.
			 * retirar quando a tela de identificar papel for corrigido
			 * */

			if(cerh.getIdePessoaFisicaCadastro()==null){
				cerh.setIdePessoaFisicaCadastro(new PessoaFisica(cerh.getIdePessoaRequerente().getIdePessoa()));
			}

			cerhDAOImpl.salvar(cerh); // vai sobrescrever?
		}
	}

	private Certificado montarCertificado(Cerh cerh, TipoCertificadoEnum tipoCertificadoEnum) throws Exception {
		Certificado certificado = new Certificado();
		certificado.setIdeOrgao(new Orgao(OrgaoEnum.INEMA));
		certificado.setTipoCertificado(new TipoCertificado(tipoCertificadoEnum.getId()));
		certificado.setNumCertificado(gerarNumeroCertificado(certificado));
		certificado.setDtcEmissaoCertificado(new Date()); // data da impressao ou do status?
		return certificado;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String gerarNumeroCertificado(Certificado certificado) throws Exception{
		return certificadoService.gerarNumeroCertificadoByTipo(certificado);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh getNumeroAtualCerh(Cerh cerh) throws Exception {
		return cerhDAOImpl.getNumeroAtualCerh(cerh);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent resumoQuantitativoCerh(Map<String, Object> parametros, TipoRelatorioEnum tipoRelatorioEnum){
		return impressoraService.resumoQuantitativoCerh(parametros, tipoRelatorioEnum);
	}

	public StreamedContent resumoCerh(Cerh cerh) {
		return impressoraService.resumoCerh(cerh);
	}

	public StreamedContent imprimirCertificado(Cerh cerh) {
		return impressoraService.imprimirCertificado(cerh);
	}
	
	public Collection<ContratoConvenio> listarContratoConvenioPorFiltro(ContratoConvenio contratoConvenio, Integer first, Integer pageSize) throws Exception{
		return contratoConvenioDAOImpl.listarContratoConvenioPorFiltro(contratoConvenio,first,pageSize);
	}
	
	public int listarContratoConvenioPorFiltroCount(ContratoConvenio contratoConvenio) throws Exception{
		return contratoConvenioDAOImpl.listarContratoConvenioPorFiltroCount(contratoConvenio);
	}
}
