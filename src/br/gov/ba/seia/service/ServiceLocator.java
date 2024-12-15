package br.gov.ba.seia.service;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Level;

import br.gov.ba.seia.util.Log4jUtil;

/**
 * Implementação de exemplo do Design Pattern Service Locator. Esta classe
 * implementa um cache de interfaces home.
 * 
 * 
 */
public class ServiceLocator {

	/**
	 * Contexto JNDI cuja instaciação será reduzida, já que o custo deste tipo
	 * de operação é alto.
	 */
	private InitialContext jndiContext;

	/**
	 * Este ServiceLocator será um Singleton
	 */
	private static ServiceLocator instance;

	/**
	 * Cosntrutor privado, evitando multiplas instâncias deste
	 * objeto(Singleton).
	 *
	 */
	private ServiceLocator() {
		try {
			jndiContext = new InitialContext();
		} catch (Exception exc) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exc);
		}
	}

	/**
	 * Recupera a instancia do Singleton
	 */
	public static ServiceLocator getInstance() {
		if (instance == null) {
			instance = new ServiceLocator();
		}
		return instance;
	}

	/**
	 * Método que faz o lookup generico de um EJB.
	 * Classe que representa a interface do EJB que será feito o lookup.
	 *
	 * @param <T>
	 * @param clazz
	 * @return T
	 * @throws Exception
	 */
	public <T> T buscarEJB(Class<T> clazz) throws Exception {
		/*
		 * Pega o nome completo da interface utilizando reflection, faz o lookup
		 * do EJB e o retorno do EJB.
		 */
		return (T) jndiContext.lookup(clazz.getName());
	}

	/**
	 * Recupera uma implementação da interface home. O lookup do bean é sempre
	 * feito pois é remoto.
	 */
	public EJBHome getHome(String jndiName) {
		Object ref = null;
		try {
			ref = jndiContext.lookup(jndiName);
			EJBHome home = (EJBHome) PortableRemoteObject.narrow(ref,
					EJBHome.class);
		} catch (Exception exc) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exc);
		}
		return (EJBHome) ref;
	}

	/**
	 * Recupera uma implementação da interface Local home. O lookup do bean só é
	 * feito se não houver nenhuma referência sua no cache da EJBHomeFactory
	 */
	public EJBLocalHome getLocalHome(String jndiName) {
		EJBLocalHome localHome = null;
		try {
			//localHome = (EJBLocalHome) EJBHomeFactory.getInstance().lookup(
				//	jndiName);
		} catch (Exception exc) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exc);
		}
		return localHome;
	}
}