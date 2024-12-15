package br.gov.ba.ws;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.log4j.Level;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.ReportUtil;
import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotifications;

public class GCMNotification {
	
	// Put your Google API Server Key here			 ​
	private static final String GOOGLE_SERVER_KEY = "AIzaSyAV5gYcfrCkpgzV9JHbojmRwe6nfHtjzPI"; //SEIA
	//private static final String GOOGLE_SERVER_KEY = "AIzaSyCtjhSa7Y92TETyZH-Yszahb7dJtki4cu8"; TESTE
	static final String MESSAGE_KEY_T = "title";	
	static final String MESSAGE_KEY_S = "subject";
	static final String MESSAGE_KEY_ID = "id";
	static final String MESSAGE_KEY_TIPO = "tipo";
	static final String MESSAGE_KEY_ID_ALERTA = "ide_alerta";
	
	@Inject
	ReportUtil util;
	
	public GCMNotification() {
		super();
	}
	/**
	 * Enviar alerta android
	 * @param id
	 * @param titulo
	 * @param corpo
	 * @param tipoAlerta
	 * @param ideAlerta
	 * @return
	 */
	public boolean EnviarAlertaAndroid(String id ,String titulo, String corpo, Integer tipoAlerta, Integer ideAlerta) {
		
			try {
				Sender sender = new Sender(GOOGLE_SERVER_KEY);
				
				Message message = new Message.Builder().addData(MESSAGE_KEY_T, titulo).addData(MESSAGE_KEY_TIPO, tipoAlerta.toString()).addData(MESSAGE_KEY_S, corpo).addData(MESSAGE_KEY_ID,id).addData(MESSAGE_KEY_ID_ALERTA, ideAlerta.toString()).build();
				
				Result result = null;
				
				result = sender.send(message, id, 1);

				return  true;
											
			} catch (IOException ioe) {
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, ioe);
				
				return  false;
				
			} catch (Exception e) {
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				
				return  false;
			}
	}
	/**
	 * 
	 * @param msg
	 * @param id
	 * @param tipoAlerta
	 * @param ideAlerta
	 */
	public void enviarAlertaIos(String msg, String id, Integer tipoAlerta, Integer ideAlerta){
		try {
			  PushNotificationPayload payload = PushNotificationPayload.complex();
			  payload.addAlert(msg);
			  payload.addCustomDictionary("tipoAlerta", tipoAlerta);
		      payload.addCustomDictionary("ideAlerta", ideAlerta);
		      PushedNotifications push = Push.payload(payload, util.getRealPath("/resources") + "SEIA_DEV.p12", "123.4", false, id);
		      System.out.println("IOS" + push.getSuccessfulNotifications().size() + " " + id); 
		} catch (CommunicationException e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} catch (KeystoreException e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}

	}
}
