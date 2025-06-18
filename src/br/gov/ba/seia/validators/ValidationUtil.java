package br.gov.ba.seia.validators;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidationUtil {
	
	private ValidationUtil(){
	}
	
	public static final boolean isDate(String texto){
		
		if(ValidationUtil.isEmpty(texto))
			return false;
		
		Pattern padrao = Pattern.compile("(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[12][0-9]{3}");
		DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Matcher pesquisa = padrao.matcher(texto);
		
		if (pesquisa.matches()){
			try{
				formato.setLenient(false);
				formato.parse(texto);
				return true;
			}
			catch(ParseException pe){
				return false;
			}
		}
		else{		
			return false;
		}
	}
	
	public static final boolean isTime(String texto){
		Pattern padrao = Pattern.compile("(0[0-9]|1[0-9]|2[0123]):([012345][0-9])");
		Matcher pesquisa = padrao.matcher(texto);
		return pesquisa.matches();
	}
	
	public static final boolean isNumber(String texto){
		if(ValidationUtil.isEmpty(texto))
			return false;
		
		Pattern padrao = Pattern.compile("[0-9]+");
		Matcher pesquisa = padrao.matcher(texto);		
		return pesquisa.matches();
	}
	
	public static final boolean validateEmail(String texto){
		
		Pattern padrao = Pattern.compile("[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.[a-z]{2,}(\\.[a-zA-Z].){0,1}");
		Matcher pesquisa = padrao.matcher(texto);
		
		return pesquisa.matches();
	}
	
	public static final boolean validateCPF(String texto){
		try {
            int d1,d4,xx,nCount,resto,digito1,digito2;
            String Check;
            String Separadores = "/-.";
            d1 = 0; d4 = 0; xx = 1;
            
            // Verifica se todos os caracteres sao iguais
            boolean todosIguais = true;
            char primeiroChar = texto.charAt(0);
            for (int i=1;i<texto.toCharArray().length;i++){
            	char charAtual=texto.toCharArray()[i];
            	if (charAtual!=primeiroChar){
            		todosIguais=false;
            		break;
            	}
            }
            if (todosIguais)
            	return false;
            
            for (nCount = 0; nCount < texto.length() -2; nCount++){
                String s_aux = texto.substring(nCount, nCount+1);
                if (Separadores.indexOf(s_aux) == -1) {
                    d1 = d1 + ( 11 - xx ) * Integer.valueOf (s_aux).intValue();
                    d4 = d4 + ( 12 - xx ) * Integer.valueOf (s_aux).intValue();
                    xx++;
                };
            };
            resto = (d1 % 11);
            if (resto < 2){
                digito1 = 0;
            }else{
                digito1 = 11 - resto;
            }

            d4 = d4 + 2 * digito1;
            resto = (d4 % 11);
            if (resto < 2){
                digito2 = 0;
            }else{
                digito2 = 11 - resto;
            }

            Check = String.valueOf(digito1) + String.valueOf(digito2);

            String s_aux2 = texto.substring (texto.length()-2, texto.length());
         
            if (s_aux2.compareTo (Check) != 0){
                return false;
            }
            return true;
        }
        catch (Exception e){
            return false;
        }
	}
	
	public static final boolean validateCNPJ(String texto){
		if(texto.length()!=14)
			return false;
		int i=5;
		int j=0;
		int soma = 0;
		while(j < texto.length()-2){
			try{
				soma += Integer.valueOf(texto.substring(j, j+1)).intValue() * i;
			}catch(NumberFormatException e){
				return false;
			}
			if(i==2)
				i=9;
			else
				i--;
			j++;			
		}

		int result = soma%11;
		int digito1;
		if(result<2)
			digito1 = 0;
		else
			digito1 = 11 - result;
		
		if(digito1 != Integer.valueOf(texto.substring(12, 13)).intValue())
			return false;
		
		i=6;
		j=0;
		soma = 0;
		while(j < texto.length()-1){
			try{
				soma += Integer.valueOf(texto.substring(j, j+1)).intValue() * i;
			}catch(NumberFormatException e){
				return false;
			}
			if(i==2)
				i=9;
			else
				i--;
			j++;			
		}

		result = soma%11;
		int digito2;
		if(result<2)
			digito2 = 0;
		else
			digito2 = 11 - result;
		
		if(digito2 != Integer.valueOf(texto.substring(13, 14)).intValue())
			return false;
		
		return true;
	}
	
	public static final <T> boolean isEmpty(T texto,boolean espacoComoConteudo){
		if (espacoComoConteudo){
			if (texto != null && !texto.toString().equals(""))
				return false;
			
		}
		else{
			if (texto != null && !texto.toString().trim().equals(""))
				return false;
		}

		return true;
	}
	
	public static final <T> boolean isEmpty(T texto){
		return isEmpty(texto , false);
	}
	
	
	public static Boolean validaSegurancaSenha(String senha){
		
		Matcher m = Pattern.compile("[!@#$%¨&*?/]{1,}").matcher(senha);
		Matcher m2 = Pattern.compile("[A-Z]{1,}").matcher(senha);
		Matcher m3 = Pattern.compile("[0-9]{1,}").matcher(senha);
		if(m.find() && m2.find() && m3.find()){
			return true;
		}
		return false;
		
	}

}
