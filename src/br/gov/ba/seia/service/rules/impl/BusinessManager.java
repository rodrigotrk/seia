package br.gov.ba.seia.service.rules.impl;

import org.apache.log4j.Level;
import org.hibernate.Session;

import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.service.rules.intf.RuleIntf;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public abstract class BusinessManager<T> implements RuleIntf<T> {

	protected Session session;
	private Usuario usuario;

	public BusinessManager() {
	}
	
	public static BusinessManager<?> loadBusinessRules(Session session, String beanName) {
		BusinessManager<?> businessRule = null;
		String radicalName = "br.gov.ba.seia.entity";
		String newPackage = "br.gov.ba.seia.service.rules.impl.business";
		String serviceClassName = beanName;

		String businessClassName = serviceClassName.replaceFirst(radicalName, newPackage) + "Business";

		try {
			businessRule = (BusinessManager<?>) Class.forName(businessClassName).newInstance();
			System.out.println("Business rule loaded in: " + businessClassName);
			businessRule.setSession(session);
			businessRule.setUsuario(ContextoUtil.getContexto().getUsuarioLogado());
		} catch (ClassNotFoundException e) {
			System.out.println("Rule: " + businessClassName + " not found.");
			//Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		} catch (InstantiationException e) {
			Log4jUtil.log(BusinessManager.class.getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		} catch (IllegalAccessException e) {
			Log4jUtil.log(BusinessManager.class.getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		} finally {
			// do some thing here!
		}
		return businessRule;

	}

	public Session getSession() {
		return this.session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
