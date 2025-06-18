package br.gov.ba.seia.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AssentadoIncra;
import br.gov.ba.seia.entity.AssentadoIncraImovelRural;
import br.gov.ba.seia.entity.AssociacaoAssentadoImovelRuralIncra;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.XLSUtil;
import br.gov.ba.seia.validators.CpfValidator;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AssentadoIncraService {

	@Inject
	private IDAO<AssentadoIncra> assentadoIncraDAO;

	private final int NOME_ASSENTADO = 0;
	private final int CPF = 1;
	private final int CODIGO_SIPRA = 2;
	private final int NOME_MAE = 4;
	private final int DATA_NASCIMENTO = 3;
	private final int LATITUDE = 5;
	private final int LONGITUDE = 6;

	public List<AssentadoIncra> listAssentadosIncraDoImovelRuralPorAssociacao(
			List<AssociacaoAssentadoImovelRuralIncra> listAssociacaoAssentadoImovelRuralIncra) {
		List<AssentadoIncra> listAssentados = new ArrayList<AssentadoIncra>();
		for (AssociacaoAssentadoImovelRuralIncra aAI : listAssociacaoAssentadoImovelRuralIncra) {
			listAssentados.add(aAI.getIdeAssentadoIncraImovelRural().getIdeAssentadoIncra());
		}
		return listAssentados;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AssentadoIncra> listarAssentadosIncraPorImovelRural(
			List<AssentadoIncraImovelRural> listAssentadoIncraImovelRural) {
		List<AssentadoIncra> listAssentados = new ArrayList<AssentadoIncra>();
		for (AssentadoIncraImovelRural assentadoImovelRural : listAssentadoIncraImovelRural) {
			listAssentados.add(assentadoImovelRural.getIdeAssentadoIncra());
		}
		return listAssentados;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(AssentadoIncra assentadoIncra)  {
		assentadoIncraDAO.salvarOuAtualizar(assentadoIncra);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AssentadoIncra carregarAssentadoIncraPorPessoaFisica(PessoaFisica idePessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AssentadoIncra.class);
		criteria.add(Restrictions.eq("idePessoaFisica.idePessoaFisica", idePessoaFisica.getIdePessoaFisica()));
		return assentadoIncraDAO.buscarPorCriteria(criteria);
	}

	public List<AssentadoIncraImovelRural> carregarAssentadosDaPlanilha(String pathNomeArquivo) throws Exception {
		List<Object[]> listValoresPorLinha = XLSUtil.getTodosValoresPorLinha(pathNomeArquivo, 0);
		isCamposValidos(listValoresPorLinha);
		return carregarCampos(listValoresPorLinha);
	}

	private List<AssentadoIncraImovelRural> carregarCampos(List<Object[]> listValoresPorLinha) {
		List<AssentadoIncraImovelRural> listAssentados = new ArrayList<AssentadoIncraImovelRural>();
		for (Object[] object : listValoresPorLinha) {
			AssentadoIncra assentado = new AssentadoIncra();
			assentado.setIdePessoaFisica(new PessoaFisica(String.valueOf(object[NOME_ASSENTADO]), String
					.valueOf(object[CPF])));
			assentado.getIdePessoaFisica().setNomMae(String.valueOf(object[NOME_MAE]));
			try {
				assentado.getIdePessoaFisica().setDtcNascimento(new SimpleDateFormat("dd/MM/yyyy").parse(object[DATA_NASCIMENTO].toString()));
			} catch (ParseException e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			
			assentado.setCodSipra(String.valueOf(object[CODIGO_SIPRA]));
			AssentadoIncraImovelRural assentadoIncraImovelRural = new AssentadoIncraImovelRural();
			if(object.length > LONGITUDE) {
				if(!Util.isNull(object[LATITUDE]) && !Util.isNull(object[LONGITUDE])) {
					assentadoIncraImovelRural.setLatitude(Double.valueOf(object[LATITUDE].toString().replace(" ", "").replace(",", ".")));
					assentadoIncraImovelRural.setLongitude(Double.valueOf(object[LONGITUDE].toString().replace(" ", "").replace(",", ".")));
				}
			}
			assentadoIncraImovelRural.setIdeAssentadoIncra(assentado);
			listAssentados.add(assentadoIncraImovelRural);
		}
		return listAssentados;
	}

	private void isCamposValidos(List<Object[]> listValoresPorLinha) throws Exception {
		validaCamposObrigatorios(listValoresPorLinha);
		validaCpf(listValoresPorLinha);
	}

	private void validaCpf(List<Object[]> listValoresPorLinha) throws Exception {
		for (Object campo[] : listValoresPorLinha) {
			campo[CPF] = Util.replaceString(campo[CPF].toString(), new String[] { "/", ".", "-" });
			if(!CpfValidator.validaCPF(campo[CPF].toString())) {
				throw new SeiaException("Cpf inválido [linha: " + (listValoresPorLinha.indexOf(campo)+1) + " ]");
			}
		}
	}

	private void validaCamposObrigatorios(List<Object[]> listValoresPorLinha) throws Exception {
		for (Object campo[] : listValoresPorLinha) {
			if (Util.isNullOuVazio(campo[NOME_ASSENTADO]) || Util.isNullOuVazio(campo[CPF])
					|| Util.isNullOuVazio(campo[CODIGO_SIPRA]) || Util.isNullOuVazio(campo[DATA_NASCIMENTO]) || Util.isNullOuVazio(campo[NOME_MAE])) {
				throw new SeiaException("Campo obrigatório sem preencher [linha: " + listValoresPorLinha.indexOf(campo)
						+ " ]");
			}
		}
	}

}
