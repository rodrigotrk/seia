package br.gov.ba.seia.enumerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Level;

import br.gov.ba.seia.util.Log4jUtil;

public enum ConfigEnum {
	
	RECATCHA_URL("recaptcha.url"),
	RECAPTCHA_PRIVATE_KEY("recaptcha.private-key"),
	RECAPTCHA_PUBLIC_KEY("recaptcha.public-key"),
	SMTP_SERVER("service.smtp"),
	BANCO_CENTRAL_SERVER("service.banco-central"),
	GEOBAHIA_SERVER("service.geobahia"),
	NOVO_GEOBAHIA_SERVER("service.novo_geobahia"),
	GEOSEIA_SERVER("service.geoseia"),
	SEIA_API_SERVER("service.seia-api"),
	SINAFLOR_SERVER("service.sinaflor"),
	SINAFLOR_CHAVE_ACESSO("service.sinaflor-chave-acesso"),
	SINAFLOR_CODIGO_ACESSO("service.sinaflor-codigo-acesso"),
	CEP_INEMA_SERVER("service.cep.inema"),
	CEP_VIACEP_SERVER("service.cep.viacep");
	
	private String value;
	
	ConfigEnum(String key) {
        try{
        	Properties props = new Properties();
        	InputStream in = getClass().getClassLoader().getResourceAsStream("seia.properties");
            props.load(in);
            in.close();
            value=props.getProperty(key);
            Log4jUtil.log(this.getClass().getSimpleName(), Level.INFO, value);
        }
        catch(IOException e){
        	Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
        }
	}	
	
	@Override
	public String toString(){
		return this.value;
	}
}
