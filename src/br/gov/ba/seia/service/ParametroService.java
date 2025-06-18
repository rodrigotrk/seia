package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.enumerator.ParametroEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ParametroService {

	@Inject
	private IDAO<Parametro> parametroDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Parametro obterPorEnum(ParametroEnum parametroEnum){
		return parametroDAO.carregarGet(parametroEnum.getIdeParametro());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Parametro obterPorId(int ideParametro){
		return parametroDAO.carregarGet(ideParametro);
	}
	
	public Integer obterValorInt(Parametro pParametro) throws Exception {
		
		try {
			return Integer.parseInt(pParametro.getDscValor());
		} catch(NumberFormatException e) {
			throw new Exception("Erro ao obter o valor do parametro como Integer");
		}
	}
	
	public Integer obterValorInt(ParametroEnum parametroEnum) throws Exception {
		
		try {
			Parametro pParametro = this.obterPorEnum(parametroEnum);
			return Integer.parseInt(pParametro.getDscValor());
		} catch(NumberFormatException e) {
			throw new Exception("Erro ao obter o valor do parametro como Integer");
		}
	}
	
	public Double obterValorDouble(Parametro pParametro) throws Exception {
		
		try {
			return Double.parseDouble(pParametro.getDscValor());
		} catch(NumberFormatException e) {
			throw new Exception("Erro ao obter o valor do parametro como Double.");
		}
	}
}