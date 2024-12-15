package br.gov.ba.seia.validators;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;



/**
 * 
 * @author rubem.filho
 *
 */
public class CnpjValidator implements Validator {
	
	@Override
	public void validate(FacesContext facesContext, UIComponent arg1, Object valorTela) throws ValidatorException {
	    try {
	    	validaCNPJ(String.valueOf(valorTela));	    	
		}catch (Exception e) {
			
			if (!Util.isNull(valorTela) && !Util.isEmptyString(valorTela.toString()) ){
				JsfUtil.addErrorMessage("CNPJ Inválido.");
			}
		}
     }
		 
	public static boolean validarCNPJ(String cnpj){
		try {
			validaCNPJ(cnpj);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	 /**
	  * Valida CNPJ do usuário.
	  *
	  * @param cnpj String valor com 14 dígitos
	  */
	public static void validaCNPJ(String cnpj) throws FormatoCNPJException {
	      
		 if(cnpj == null || cnpj.length() != 14){
		      throw new FormatoCNPJException();
		 }             
		 
		 try {
		        Long.parseLong(cnpj);
		       } catch (NumberFormatException e) { // CNPJ não possui somente números
		               throw new FormatoCNPJException();
		     }
		 
		 int soma = 0;
		 String cnpj_calc = cnpj.substring(0, 12);
		 
		 char chr_cnpj[] = cnpj.toCharArray();
		 
		 for(int i = 0; i < 4; i++)
		      if(chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
		                    soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
		 
		 for(int i = 0; i < 8; i++)
		      if(chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9)
		                    soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
		 
		 int dig = 11 - soma % 11;
		 cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();
	     soma = 0;
	     
	     for(int i = 0; i < 5; i++)
		       if(chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
		                   soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
		 
		 for(int i = 0; i < 8; i++)
		       if(chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9)
		                   soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
		 
		  dig = 11 - soma % 11;
		  cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();
		 
		  if(!cnpj.equals(cnpj_calc) || cnpj.equals("00000000000000"))
			  throw new FormatoCNPJException();
	   }
 
}

class FormatoCNPJException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public FormatoCNPJException(){
		super();
	}
}

