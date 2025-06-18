package br.gov.ba.seia.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.Session;


public class BancoUtil {
	private static final String URL_DATA_SOURCE = "java:/seiaDs";

	public static Connection obterConexao() throws Exception {
		Context lContexto = null;
		DataSource lDataSource = null;
		Connection lConnection = null;

		try {
	        lContexto = new InitialContext();
	        lDataSource = (javax.sql.DataSource) lContexto.lookup(URL_DATA_SOURCE);
	        lConnection = lDataSource.getConnection();	        
        }
		catch (NamingException lExcecao) {
			throw new Exception("Não foi possível encontrar o nome da conexão do banco.", lExcecao);
        }
		catch (SQLException lExcecao) {
			throw new Exception("Ocorreu um erro de sql.", lExcecao);
        }
		return lConnection;
	}
	
	public static DataSource getDataSource() throws Exception {
		Context lContexto = null;
		DataSource lDataSource = null;

		try {
	        lContexto = new InitialContext();
	        lDataSource = (javax.sql.DataSource) lContexto.lookup(URL_DATA_SOURCE);	        	        
        }
		catch (NamingException lExcecao) {
			throw new Exception("Não foi possível encontrar o nome da conexão do banco.", lExcecao);
        }		
		return lDataSource;
	}
	
	public static Session getSession() throws Exception{
		Session session = null;
		try {
			session = (Session)getDataSource();
		} catch (SQLException exception) {
			throw new Exception("Ocorreu um erro de sql.", exception);
		}
		return session;
	}
	
	public static EntityManager getEntityManager() throws Exception{
		EntityManager entityManager = null;
		try {
			entityManager = (EntityManager)getDataSource();
		} catch (SQLException exception) {
			throw new Exception("Ocorreu um erro de sql.", exception);
		}
		return entityManager;
	}
}