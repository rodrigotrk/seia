package br.gov.ba.seia.facade;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.CerhController;
import br.gov.ba.seia.dao.CerhDAOImpl;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhStatus;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.util.StringUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 25/04/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhFinalizarFacade {

	@EJB
	private CerhDAOImpl cerhDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh getNumeroCadastroCerh(Integer ideCerh) throws Exception {
		return cerhDAO.buscarCerhPor(ideCerh);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void armazenarHistorico(CerhController ctrl) {
		try {
			ctrl.prepararArmazenamentoHistorico();
		} 
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarCERH(Cerh cerh, CerhStatusEnum tipStatus) throws Exception {
		if(tipStatus.equals(CerhStatusEnum.CADASTRO_COMPLETO)) {
			
			if(Util.isNullOuVazio(cerh.getNumCadastro()) && Util.isNullOuVazio(cerh.getIdeCerhPai())) {
				cerh.setNumCadastro(gerarNumeroCadastro());
			}
			
		}
		tramitar(cerh, tipStatus);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitar(Cerh cerh, CerhStatusEnum statusEnum) throws Exception {
		cerh.setIdeCerhStatus(new CerhStatus(statusEnum));
		cerhDAO.salvar(cerh);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarNumeroCadastro() throws Exception {
		StringBuilder numeroCERH = new StringBuilder();

		String ultimoNumero = cerhDAO.getUltimoNumeroCadastro();

		if (Util.isNullOuVazio(ultimoNumero)) {
			ultimoNumero = String.valueOf(1);
		}
		else {
			ultimoNumero = String.valueOf(Integer.parseInt(ultimoNumero) + 1);
		}

		numeroCERH
				.append(new SimpleDateFormat("yyyy").format(new Date()))
				.append('.');

		numeroCERH
				.append("001")
				.append('.');

		numeroCERH
				.append(StringUtil.lPad(ultimoNumero, '0', 6))
				.append('/');

		numeroCERH
				.append("INEMA/CERH");

		return numeroCERH.toString();
	}

}
