package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.TccaDaoImpl;
import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TccaService {
	
	@Inject
	private TccaDaoImpl tccaDaoImpl;
	
	private String erroTccaDuplicado = "Este número de TCCA já existe, por favor insira outro.";
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTCCA(Tcca tcca) {
		try {
			
			List<Tcca> listTcca = tccaDaoImpl.listarPorNumeroTCCA(tcca.getNumTcca());
			
			if(!Util.isNullOuVazio(listTcca)) {
				
				for (Tcca t : listTcca) {
					
					if(tcca.getNumTcca().equals(t.getNumTcca())) {
						
						if(Util.isNullOuVazio(tcca) || !tcca.getIdeTcca().equals(t.getIdeTcca())) {
							JsfUtil.addWarnMessage(erroTccaDuplicado);
							throw new Exception(erroTccaDuplicado);
						}
					}
				}
			}
			
			tccaDaoImpl.salvar(tcca);
		} catch (Exception e) {
			if(!e.getMessage().equals(erroTccaDuplicado)) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}