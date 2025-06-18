//Carregar automaticamente funcoes javascript junto com as paginas do sistema
$(document).ready(function() {
	limitarTextArea();
	aplicarMascaraParaCamposSomenteInteiros();
	aplicarMascaraParaCamposNumericos(4, '.campoNumericoComDecimal-4');
	aplicarMascaraParaCamposNumericos(3, '.campoNumericoComDecimal-3');
	aplicarMascaraParaCamposNumericos(2, '.campoNumericoComDecimal-2');
	bloquearEnter();
	valorMaximoCampo(31,".valorMaximo-31");
	valorMaximoCampo(30,".valorMaximo-30");
	valorMaximoCampo(28,".valorMaximo-28");
	valorMaximoCampo(24,".valorMaximo-24");
	valorMaximoCampo(100,".valorMaximo-100");
});

function abrirModal(dialog) {
	$(dialog).modal({
		escapeClose : false,
		clickClose : false,
		showClose : false
	});
}

function fecharModal(dialog) {
	$.modal.close();
}

function bloquearEnter() {
	$(document).delegate('.notSubmit','keypress', function(event) {
		if(event.keyCode == 13) {
			return false;
		}
	});
}

function limitarTextArea() {
	
	var targetClass = '.limitedTextArea';
	
	$(document).delegate(targetClass, 'keyup blur', function() {
		var listaTextArea = $(targetClass);
		for (i in listaTextArea) {
			var textArea = listaTextArea[i];
			var texto = $(textArea).val();
			var classes = $(textArea).attr('class');
			var strClasseInicio = classes.indexOf("maxlength") + "maxlength".length;
			var strClasseFim = classes.indexOf(" ", strClasseInicio);
			var maxlength = parseInt(classes.substring(strClasseInicio, strClasseFim));
			if (texto.length > maxlength) {
				$(textArea).val(texto.substring(0, maxlength));
			}
		}
	});
}

function aplicarMascaraParaCamposSomenteInteiros() {

	var targetClass = '.inputTextOnlyInteger';

	$(document).delegate(targetClass, 'keyup blur', function() {

		var listaInputText = $(targetClass);

		for (i in listaInputText) {
			var inputText = $(listaInputText[i]);
			var valor = $(inputText).val();
			$(inputText).val(valor.toString().replace(/[^\d]+/gi, ''));
		}
	});
}

function aplicarMascaraParaCamposNumericos(numCasasDecimais, targetClass) {

	$(document).delegate(targetClass, 'keyup blur', function() {
		
		var inputText = $(this);
		var maxlength = inputText.attr('maxlength');
		var valor = inputText.val();
		
		if(maxlength != undefined) {
			valor = toMask(numCasasDecimais, valor);
			if(valor.length > maxlength) {
				valor = valor.substring(0,maxlength);
			}
		}
		
		inputText.val(toMask(numCasasDecimais, valor));

		/*var listaInputText = $(targetClass);

		for (i in listaInputText) {
			var inputText = $(listaInputText[i]);
			var valor = $(inputText).val();
			$(inputText).val(toMask(numCasasDecimais, valor));
		}*/
	});
}



function valorMaximoCampo(valorMaximo, targetClass) {

	$(document).delegate(targetClass, 'keyup blur', function() {

		var listaInputText = $(targetClass);

		for (i in listaInputText) {
			var inputText = $(listaInputText[i]);
			var valor = $(inputText).text();
			valor = valor.replace(',','.');
			
			if (valor > valorMaximo){
				$(inputText).text(valorMaximo);				
			}
			
		}
	});
}

function toMask(numCasasDecimais, valor) {
	valor = valor.toString().replace(/[^\d]+/gi, '');
	valor = colocarVirgula(numCasasDecimais, valor);
	valor = separarMilhar(valor);
	return valor;
}

function colocarVirgula(numCasasDecimais, valor) {

	if (valor.length == numCasasDecimais) {
		return "0," + valor;
	} else if (valor.length < numCasasDecimais) {
		var numCasasQueFaltam = numCasasDecimais - valor.length;
		var mask = "0,";

		for (i = 0; i < numCasasQueFaltam; i++) {
			mask = mask + "0";
		}
		return mask + valor;
	} else {
		var posicaoDaVirgula = valor.length - numCasasDecimais;
		var inteiro = valor.substring(0, posicaoDaVirgula);
		var decimal = valor.substring(posicaoDaVirgula, valor.length);

		return retornarValorInteiro(inteiro) + "," + decimal;
	}
}

function retornarValorInteiro(valor) {
	while (valor.charAt(0) == "0" && valor.length > 1) {
		valor = valor.substring(1, valor.length);
	}
	return valor;
}

function separarMilhar(valor) {

	var inteiro = valor.substring(0, valor.indexOf(','));
	var decimal = valor.substring(valor.indexOf(',') + 1, valor.length);

	var novoInteiro = "";
	var cont = 0;
	for (i = inteiro.length; i >= 1; i--) {
		cont++;
		if (cont == 4) {
			novoInteiro = inteiro.charAt(i - 1) + "." + novoInteiro;
			cont = 1;
		} else {
			novoInteiro = inteiro.charAt(i - 1) + novoInteiro;
		}
	}
	return novoInteiro + "," + decimal;
}




