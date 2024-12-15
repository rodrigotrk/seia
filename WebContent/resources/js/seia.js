function mascararNumeroLR(pCampo, pEvento, pQtdeDigInt, pQtdeDigFrac, pSimbAgrup, pSimbFrac, pQtdeDigAgrup) {

	var lCode = 0;

	if (pEvento.keyCode)
		lCode = pEvento.keyCode;
	else if (pEvento.which)
		lCode = pEvento.which;
	else if (pEvento.charCode)
		lCode = pEvento.charCode;

	if (lCode == 9)
		return;

	valorTmp = pCampo.value.replace(/\D/g, '');
	tamTotal = pQtdeDigInt + pQtdeDigFrac;

	tamValor = valorTmp.length;
	if (tamValor > tamTotal)
		valorTmp = valorTmp.substring(tamValor - tamTotal);
	tamValor = valorTmp.length;

	posInsDig = tamValor - pQtdeDigFrac;
	qtdeAgrup = parseInt(posInsDig / pQtdeDigAgrup);
	if (posInsDig % pQtdeDigAgrup == 0)
		qtdeAgrup--;

	if (pQtdeDigFrac > 0 && tamValor > pQtdeDigFrac) {
		valorTmp = valorTmp.substring(0, posInsDig) + pSimbFrac
				+ valorTmp.substring(posInsDig);
	}
	for ( var i = 0; i < qtdeAgrup; i++) {
		posInsDig = posInsDig - pQtdeDigAgrup;
		valorTmp = valorTmp.substring(0, posInsDig) + pSimbAgrup
				+ valorTmp.substring(posInsDig);
	}
	pCampo.value = valorTmp;
}

/*function abrirDialog(idDialog) {
	var top = (screen.height - $('#'+idDialog).css('height'))/2;
	var left = (screen.width - $('#'+idDialog).css('width'))/2;
	
	$('#'+idDialog).css({'top': top, 'left': left});
	var dialog = $('#'+idDialog);
	dialog.show();	
}*/

function mascararBigDecimal(pCampo, pEvento, pQtdeDigInt, pQtdeDigFrac) {
	return mascararNumeroLR(pCampo, pEvento, pQtdeDigInt, pQtdeDigFrac, '', '.', 1);
}

// REMOVE OS NÚMEROS - PODE TUDO MENOS NUMEROS
function SomenteLetras(obj) {
	var nat = "";
	nat = obj;
	nat = nat.replace(/[0-9]+/gi, "");
	$('input[id*="naturalidade"]').val(nat);
}

function maxLengthTextArea(obj, max) {
	var nat = "";
	var num = "";
	nat = obj;
	num = nat.value.length;
	if(num>max){
		//trava o textarea
		obj.value = obj.value.substring(0,max);
	}
}

/*
 * Modificado por 
 * @alexandreQueiroz
 * 
 * Permite somente Letras minusculas, numeros, pontos e underline
 * @param obj   que contém o texto
 * @param event que contém o KeyCode 
 */

//Para o chorme
function mascaraLogin(obj,event) {

   var navegador = navigator.userAgent.toLowerCase();

   if ($.browser.webkit){
		
		 var tecla = (event.keyCode ? event.keyCode : event.which); 
		 
	     if (((tecla >= 97) && (tecla <= 122))|| 
	     	 ((tecla >= 48) && (tecla <= 57)) ||
	 	      (tecla == 46)                           ||
	 	      (tecla == 95)||(tecla == 86)
	 	     )      {return true;}
	         else   {return false;}
   }
} 

function mascaraFirefox(obj,event) {
		
	 var navegador = navigator.userAgent.toLowerCase();
	
	if ((navegador.indexOf('firefox') > -1)) {
		
	   var tecla = event.keyCode ? event.keyCode : event.which;	    
	   
	 if(tecla != 37 && tecla != 39){
		var nat = "";
		nat = obj;
			
		nat.value = nat.value.replace(/[^a-z0-9._]+/gi,"");
		nat.value = nat.value.toLowerCase();
	  }
	 }
	 else {
	
		
	}
}

/*
* Retorna a posição do cursor no elemento text dado:
*
* @param Object el Elemento tipo INPUT type TEXT
* @return int posição do cursor na string do elemento
*/
function getPosCursor(el) {
   
   var pos = 0;
   
   if (typeof(el.selectionStart) != "undefined") {
       pos = el.selectionStart;
   } else if (document.selection) {
       var r = document.selection.createRange();
       var rd = r.duplicate();
       pos = 0 - rd.moveStart("character",-1e5);
   }
  
}

/**
* Posiciona o cursor na posição indicada sobre o elemento text dado:
*
* @param Object el  Elemento tipo INPUT type TEXT
* @param int    pos Posição na string do elemento
*/
function setPosCursor(el, pos) {
 
 if (el.setSelectionRange) {
   el.focus();
   el.setSelectionRange(pos, pos);
 } else if (el.createTextRange) {
   var r = el.createTextRange();
   r.collapse(true);
   r.moveEnd('character', pos);
   r.moveStart('character', pos);
   r.select();
 }
}

var minLatitude = 8;
var maxLatitude = 18;
var minLongitude = 37;
var maxLongitude = 47;

function verificaLatitude(obj) {
	if (obj.value == '') {
		alertaLatLong(obj, minLatitude, 'Latitude');
	}
	else {
	var valor = 0;
	valor = parseFloat(obj.value);
	if (valor < minLatitude) {
		alertaLatLong(obj, minLatitude, 'Latitude');
	}
		else if (valor > maxLatitude) {
		alertaLatLong(obj, maxLatitude, 'Latitude');
	}
	}
	return true;
}

function verificaLongitude(obj) {
	if (obj.value == '') {
		alertaLatLong(obj, minLongitude, 'Longitude');
	}
	else{
		var valor = 0;
		valor = parseFloat(obj.value);
		if (valor < minLongitude) {
			alertaLatLong(obj, minLongitude, 'Longitude');
		}
		else if (valor > maxLongitude) {
			alertaLatLong(obj, maxLongitude, 'Longitude');
		}	
	}
	return true;
}

// //VERIFICA SE OS VALORES DE MINUTOS E SEGUNDOS EST�O ENTRE -1 E 60
function verificaMinutoSegundo(obj) {
	if (obj.value == '') {
		obj.value = 0;
		document.getElementById(obj.id).blur;
//		$("#" + obj.id).blur();
		return false;
	}
	var valor = obj.value;
	if (valor < 0) {
		obj.value = 0;
		document.getElementById(obj.id).blur;
//		$("#" + obj.id).blur();
		return false;
	}
	if (valor > 59) {
		obj.value = 59;
		document.getElementById(obj.id).blur;
//		$("#" + obj.id).blur();
		return false;
	}
	return true;
}
// ALERTA PARA LATITUDE OU LONGITUDE - INFORME O VALOR LIMITE E O TIPO DE ALERTA
// QUE DESEJA
function alertaLatLong(obj, valLimite, tipo) {
	if (tipo == 'Latitude') {
		var alerta = 'Para Latitude, o campo Grau(%B0) deve possuir um valor de '
				+ -maxLatitude + ' a ' + -minLatitude + '.';
		alert(unescape(alerta));
	}
	else if (tipo == 'Longitude') {
			var alerta = 'Para Longitude, o campo Grau(%B0) deve possuir um valor de '
					+ -maxLongitude + ' a ' + -minLongitude + '.';
			alert(unescape(alerta));
	}
	else {
			alert('Tipo desconhecido!');
			return false;
		}
	obj.value = parseFloat(valLimite);
	document.getElementById(obj.id).blur;
//	$("#" + obj.id).blur();
	return false;
}

function alertaValorAzimute(obj) {
	if (obj.value > 360) {
		var alerta = 'A valor máximo do Azimute deve ser 360°';
		obj.value = 360;
		alert(alerta);
	} else if (obj.value < 0) {
		var alerta = 'A valor mínimo do Azimute deve ser 0°';
		obj.value = 0;
		alert(alerta);
	}
}

// SOMENTE NUMEROS - MAIS NADA
function SomenteNumero(campo) {
	var digits = "0123456789";
	var campo_temp;
	for ( var i = 0; i < campo.value.length; i++) {
		campo_temp = campo.value.substring(i, i + 1);
		if (digits.indexOf(campo_temp) == -1) {
			campo.value = campo.value.substring(0, i);
		}
	}
}

//SOMENTE NUMEROS e PONTO
function SomenteDecimal(campo) {
	var digits = "0123456789.";
	var campo_temp;
	for ( var i = 0; i < campo.value.length; i++) {
		campo_temp = campo.value.substring(i, i + 1);
		if (digits.indexOf(campo_temp) == -1) {
			campo.value = campo.value.substring(0, i);
		}
	}
}

//SOMENTE NUMEROS - MAIS NADA
function soNumero(campo) {
	campo.value = campo.value.replace('-','').replace('.','').replace('/','');
	alert(campo.value);
}

//EXCETO NUMEROS - MAIS NADA - PARA QUALQUER CAMPO
function ExcetoNumero(campo) {
	var digits = "0123456789";
	var campo_temp;
	for ( var i = 0; i < campo.value.length; i++) {
		campo_temp = campo.value.substring(i, i + 1);
		if (digits.indexOf(campo_temp) != -1) {
			campo.value = campo.value.substring(0, i);
		}
	}
}

function RemovMenosFracGrau() {
	var obj = $('input[id*="latitudeFracaoGrau"]');
	RemoveMenos(obj.attr('id'), obj.val());
	obj = $('input[id*="longitudeFracaoGrau"]');
	RemoveMenos(obj.attr('id'), obj.val());
}

function RemoveMenos(id, value) {
	var str = "";
	if (value != null) {
		str = value;
		if (str.indexOf("-", "") != -1) {
			str = str.substring(str.indexOf("-", "") + 1, str.length);
			$('input[id*="' + id + '"]').val(str);
		}
	}
} 

// Formata o campo valor
function formataValor(campo) {
	campo.value = filtraCampoValor(campo);
	vr = campo.value;
	tam = vr.length;
	if (tam <= 2) {
		campo.value = vr;
	}
	if ((tam > 2) && (tam <= 5)) {
		campo.value = vr.substr(0, tam - 2) + '.' + vr.substr(tam - 2, tam);
	}
	if ((tam >= 6) && (tam <= 8)) {
		campo.value = vr.substr(0, tam - 5) + ',' + vr.substr(tam - 5, 3) + '.'
				+ vr.substr(tam - 2, tam);
	}
	if ((tam >= 9) && (tam <= 11)) {
		campo.value = vr.substr(0, tam - 8) + '.' + vr.substr(tam - 8, 3) + ','
				+ vr.substr(tam - 5, 3) + '.' + vr.substr(tam - 2, tam);
	}
	if ((tam >= 12) && (tam <= 14)) {
		campo.value = vr.substr(0, tam - 11) + ',' + vr.substr(tam - 11, 3)
				+ '.' + vr.substr(tam - 8, 3) + ',' + vr.substr(tam - 5, 3)
				+ ',' + vr.substr(tam - 2, tam);
	}
	if ((tam >= 15) && (tam <= 18)) {
		campo.value = vr.substr(0, tam - 14) + ',' + vr.substr(tam - 14, 3)
				+ '.' + vr.substr(tam - 11, 3) + ',' + vr.substr(tam - 8, 3)
				+ '.' + vr.substr(tam - 5, 3) + '.' + vr.substr(tam - 2, tam);
	}
}
// limpa todos os caracteres especiais do campo solicitado
function filtraCampoValor(campo) {
	var s = "";
	vr = campo.value;
	tam = vr.length;
	for ( var i = 0; i < tam; i++) {
		if (vr.substring(i, i + 1) >= "0" && vr.substring(i, i + 1) <= "9") {
			s = s + vr.substring(i, i + 1);
		}
	}
	campo.value = s;
	return campo.value;
}

function blurOnEnter(event, object) {
	if (event.keyCode == 13) {
		object.blur();
		return true;
	}
}

function maskIt(component, e, mascara) {
	// Cancela se o evento for Backspace
	var ea = e;
	if (!e)
		ea = window.event;
	if (ea.keyCode)
		code = ea.keyCode;
	else if (ea.which)
		code = ea.which;

	// Variaveis da função
    var txt = component.value.replace(/[^\d]+/gi, '').reverse();
	var mask = mascara.reverse();
	var ret = "";
	txt = removeLastZeros(txt);
	// Loop na mascara para aplicar os caracteres
	for ( var x = 0, y = 0, z = mask.length; x < z && y < txt.length;) {
		if (mask.charAt(x) != '#' && mask.charAt(x) != '9') {
			ret += mask.charAt(x);
			x++;
		} else {
			ret += txt.charAt(y);
			y++;
			x++;
		}
	}
	component.value = ret.reverse();
	addZero(component);
}

function maskItQuatro(component, e, mascara) {
	// Cancela se o evento for Backspace
	var ea = e;
	if (!e)
		ea = window.event;
	if (ea.keyCode)
		code = ea.keyCode;
	else if (ea.which)
		code = ea.which;

	// Variaveis da função
    var txt = component.value.replace(/[^\d]+/gi, '').reverse();
	var mask = mascara.reverse();
	var ret = "";
	txt = removeLastZeros(txt);
	// Loop na mascara para aplicar os caracteres
	for ( var x = 0, y = 0, z = mask.length; x < z && y < txt.length;) {
		if (mask.charAt(x) != '#' && mask.charAt(x) != '9') {
			ret += mask.charAt(x);
			x++;
		} else {
			ret += txt.charAt(y);
			y++;
			x++;
		}
	}
	component.value = ret.reverse();
	addZeroQuatro(component);
}

function validaTeclado(component, evt, mascara) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    //se for backspace sempre permite a ação do botão
    if(charCode == 8) {
    	return true;
    }
    //Verifica se o valor do caractere nao corresponde a um numero
    //Caso nao corresponda retorna false
    if (charCode > 31 && (charCode < 48 || charCode > 57))
       return false;
    //verifica o tamanho do campo com a mascara
    //primeiro remove os caracteres especiais da mascara (fica apenas
    //o 9 e o #
    var maskClear = mascara.replace(/[^#9]+/gi, '');
    var txt = component.value.replace(/[^\d]+/gi, '');
    if(txt.length >= maskClear.length) {
	return false;
    }
    //caso não haja problema, aceita
    return true;
 }

function addZero(component) {
	var value = component.value;
	if(value.length > 2) {
		return;
	}

	switch (value.length) {
	case 0:
		component.value = '0,00';
		break;
	case 1:
		component.value = '0,0' + value;
		break;
	case 2:
		component.value = '0,' + value;
		break;
	}
}

function addZeroQuatro(component) {
	var value = component.value;
	if(value.length > 4) {
		return;
	}
	console.log(value.length);
	switch (value.length) {
	case 0:
		component.value = '0,0000';
		break;
	case 1:
		component.value = '0,000' + value;
		break;
	case 2:
		component.value = '0,00' + value;
		break;
	case 3:
		component.value = '0,0' + value;
		break;
	case 4:
		component.value = '0,' + value;
		break;
	}
}

function removeLastZeros(valueReverse) {
	var returnNotReverse = "";
	var encontrouDifZero = false;
	for(var x = (valueReverse.length - 1); x >= 0; x--) {

		if(valueReverse.charAt(x) == "0" && !encontrouDifZero) {
			continue;
		}
		encontrouDifZero = true;
		returnNotReverse += valueReverse.charAt(x);
	}
	return returnNotReverse.reverse();
}

function setFieldPosition(field) {
	field.selectionStart = field.value.length;
	field.selectionEnd = field.value.length;
}

String.prototype.reverse = function() {
	return this.split('').reverse().join('');
};

function validaMensagensFinalizacao(args, perguntaRlMenorQueVinteExibida, msgRlMenorQueVinteExibida, msgSobreposicaoRlAppExibida, msgSobreposicaoRlApExibida) {
	if(args.openModal && args.isRlMenorQueVintePorCento && !perguntaRlMenorQueVinteExibida) {
		confirmDialogPerguntaRlmenorQueVinte.show();
	} else if(args.openModal && args.isRlMenorQueVintePorCento && !msgRlMenorQueVinteExibida) {
		confirmDialogRlmenorQueVinte.show();
	} else if(args.openModal && args.existeSobreposicaoRlApp && !msgSobreposicaoRlAppExibida) {
		confirmDialogSobreposicaoRlApp.show();
	} else if (args.openModal && args.existeSobreposicaoRlAp && !msgSobreposicaoRlApExibida) {
		confirmDialogSobreposicaoRlAp.show();
	} else if(args.openModal) {
		confirmDialogConfirmaAceite.show();
	}
}


function verificarFiltros() {
	var statusProcesso = $('#frmRelatorioQuantitativo\\:tfStatusFluxo').val();
	var categoriaAto = $('#frmRelatorioQuantitativo\\:tfCategoriaAto').val();
	var atoAmbiental = $('#frmRelatorioQuantitativo\\:tfAto').val();
	var diretoria = $('#frmRelatorioQuantitativo\\:tfDiretoria').val();
	var area = $('#frmRelatorioQuantitativo\\:tfArea').val();
	var municipio = $('#frmRelatorioQuantitativo\\:tfMunicipio').val();
	var periodoFormacaoDE = $('#frmRelatorioQuantitativo\\:tfPeriodoDeFormacaoDE_input').val();
	var periodoFormacaoATE = $('#frmRelatorioQuantitativo\\:tfPeriodoDeFormacaoATE_input').val();
	var periodoStatusDE = $('#frmRelatorioQuantitativo\\:tfPeriodoStatusDE_input').val();
	var periodoStatusATE = $('#frmRelatorioQuantitativo\\:tfPeriodoStatusATE_input').val();

	if(statusProcesso=="Todos" && categoriaAto=="Todos" && atoAmbiental=="Todos" 
	&& diretoria=="Todos" && area=="Todos" && municipio=="" && periodoFormacaoDE=="" 
	&& periodoFormacaoATE==""){
		//alert('Informe pelo menos um filtro.');
		exibirMensagemEscolhaDeFiltro();
		return false;
	}
	
	if((periodoFormacaoDE=="" && periodoFormacaoATE!="") || (periodoFormacaoDE!="" && periodoFormacaoATE=="")){
		//alert('Informe o intervalo de formação completo.');
		exibirMensagemPeriodoDeFormacao();
		return false;		
	}
	
	if((periodoStatusDE=="" && periodoStatusATE!="") || (periodoStatusDE!="" && periodoStatusATE=="")) {
		//alert('Informe o intervalo do período do status de forma completa.');
		exibirMensagemPeriodoDoStatus();
		return false;
	}
	
	return true;
}

function exibeMensagemImoveisPendentes(args) {
	if(args.existeImoveisPendentes) {
		dlgAvisoImoveisPendentes.show();
	} else {
		window.self.location.href = "/paginas/manter-imoveis/cefir/listaImoveisRural.xhtml";
	} 	
}

function aumentaZindex_loc_geografica_doc(){
	$('#_loc_geografica_doc').css('z-index',1112);
	$('#confirmaExclusaoShapeFileLocGeo').css('top','32%');
	$('#confirmaExclusaoShapeFileLocGeo').css('left','40%');
}

function mascararNumeroAndAddZeros(pCampo, pEvento, pQtdeDigInt, pQtdeDigFrac, pSimbAgrup, pSimbFrac, pQtdeDigAgrup) {

	var lCode = 0;

	if (pEvento.keyCode)
		lCode = pEvento.keyCode;
	else if (pEvento.which)
		lCode = pEvento.which;
	else if (pEvento.charCode)
		lCode = pEvento.charCode;

	if (lCode == 9)
		return;

	valueSemVirgula = pCampo.value.replace(/\D/g, '');
	
	if(valueSemVirgula.length <= pQtdeDigFrac){
        pCampo.value = addZeros(pQtdeDigFrac, valueSemVirgula);
	}

	valorTmp = pCampo.value.replace(/\D/g, '');
	tamTotal = pQtdeDigInt + pQtdeDigFrac;

	tamValor = valorTmp.length;
	if (tamValor > tamTotal)
		valorTmp = valorTmp.substring(tamValor - tamTotal);
	tamValor = valorTmp.length;

	posInsDig = tamValor - pQtdeDigFrac;
	qtdeAgrup = parseInt(posInsDig / pQtdeDigAgrup);
	if (posInsDig % pQtdeDigAgrup == 0)
		qtdeAgrup--;

	if (pQtdeDigFrac > 0 && tamValor > pQtdeDigFrac) {
		valorTmp = valorTmp.substring(0, posInsDig) + pSimbFrac
				+ valorTmp.substring(posInsDig);
	}
	for ( var i = 0; i < qtdeAgrup; i++) {
		posInsDig = posInsDig - pQtdeDigAgrup;
		valorTmp = valorTmp.substring(0, posInsDig) + pSimbAgrup
				+ valorTmp.substring(posInsDig);
	}
	removerEouAdicionarZeroAntesDaVirgula(valorTmp, pCampo, pQtdeDigFrac);
}

function addZeros(pQtdeDigFrac, valueSemVirgula) {
    var qtdZeros = 0;
    var zeros = '';
	
    qtdZeros = pQtdeDigFrac - (valueSemVirgula.length - 1);

	for(var i = 0; i < qtdZeros; i++){
	    zeros = zeros + '0';
	}	    
	return valueSemVirgula = zeros + valueSemVirgula;
}

function removerEouAdicionarZeroAntesDaVirgula(valorTmp, pCampo, pQtdeDigFrac){
	var zeros = '';
	if(pQtdeDigFrac > 0){
		zeros = valorTmp.substring(0,valorTmp.indexOf(','));
	} else {
		if(pCampo.value.indexOf('.') == 0){
			pCampo.value.replace('.','');
		} 
		zeros = pCampo.value.toString();
	}
	var isZero = true;
	var count = 0;
	while(isZero){
		if(zeros.charAt(count)=='0'){
			count++;
		}
		else{
			isZero=false;
		}		
	}	
    valorTmp = valorTmp.substring(count,valorTmp.lenght);	    
    if(valorTmp.indexOf(',') == 0){
    	return pCampo.value = '0'+valorTmp;
    } else {
    	return pCampo.value = valorTmp;
    }
}

function handleLoginRequest(xhr, status, args, widgetvar) {
	if(args.flagValidate) {
		widgetvar.hide();  
	} 
}

function limparForm(form) {
	$('.ui-messages-error').remove();
	form.reset();
}

function limparMsgErro() {
	$('.ui-messages-error').remove();
}

function mascaraMutuario(o) {
	obj = o;
	setTimeout('execmascara()',1);
}

function execmascara() {
	obj.value=cpfCnpj(obj.value);
}

function cpfCnpj(v) {
	//Remove tudo o que nao e digito
	v=v.replace(/\D/g,"");

	if (v.length < 14) { //CPF

		//Coloca um ponto entre o terceiro e o quarto digitos
		v=v.replace(/(\d{3})(\d)/,"$1.$2");
		
		//Coloca um ponto entre o terceiro e o quarto digitos
		//de novo (para o segundo bloco de numeros)
		v=v.replace(/(\d{3})(\d)/,"$1.$2");
		
		//Coloca um hifen entre o terceiro e o quarto digitos
		v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2");

	} else { //CNPJ

		//Coloca ponto entre o segundo e o terceiro digitos
		v=v.replace(/^(\d{2})(\d)/,"$1.$2");
		
		//Coloca ponto entre o quinto e o sexto digitos
		v=v.replace(/^(\d{2})\.(\d{3})(\d)/,"$1.$2.$3");
		
		//Coloca uma barra entre o oitavo e o nono digitos
		v=v.replace(/\.(\d{3})(\d)/,".$1/$2");
		
		//Coloca um hifen depois do bloco de quatro digitos
		v=v.replace(/(\d{4})(\d)/,"$1-$2");

	}

	return v;

}

function validaTela(xhr, status, args, pWidgetVarComponente) {
	if(!args.validationFailed || args.flagValidate) {
		pWidgetVarComponente.hide();
	}
}

/**
 * Mascara para campos Decimais 
 * <br>campo  = this
 * <br>qtdDigitos = Valor inteiro que indica onde deve-se adicionar o sinal para formatacao
 * <br>simboloSeparador = Simbolo que deve ser utilizado para fazer a formatacao
 * <br> ------------ Exemplos ---------------
 * <br> maskDecimal(this, 2, '.') -> 12.00000
 * <br> maskDecimal(this, 3, ',') -> 325,4500
 */
function maskDecimal(campo, qtdDigitos, simboloSeparador){
	campo.value = filtraCampoValor(campo);
	vr = campo.value;
	
	// Remove o 0 no ínicio da String
	vr = vr.replace(/^0+/, '')
	
	// Caso o tamanho da String seja inferior ao valor utilizado para fazer a separação, não modifca-se a String.
	if (vr.length <= qtdDigitos) {
		campo.value = vr;
	}
	// Caso o tamanho da String seja superior ao valor utilizado para fazer a separação, adiciona-se o elemento separador da String.
	if (vr.length > qtdDigitos) {
	    campo.value = vr.substr(0, qtdDigitos) + simboloSeparador + vr.substr(qtdDigitos, vr.length);
	} 

}

// Variação: 37º - 47º
function longitudeGrau(campo){
	maskDecimal(campo, 2, '.');
}

// Variação: 8º - 18º
function latitudeGrau(campo){
    campo.value = filtraCampoValor(campo);
    vr = campo.value;
	// Os valores podem ser iniciados com 8 ou 9 e portanto a máscara deve se adaptar.
    if(vr.charAt(0) == 8 || vr.charAt(0) == 9){
        if(vr.length > 1){
            campo.value = vr.substr(0, 1) + '.' + vr.substr(1, vr.length);       
        }
	}
	else {
	    maskDecimal(campo, 2, '.');
	}
}

function latLongFormat(number, digits) {
	var numberString = number.value.toString(); //Converte o valor do número para um texto a partir do componente
	var dot = numberString.indexOf("."); // Obtém a posição do ponto
	var length = numberString.length; // Obtém o tamanho to texto do número
	var positionDifference = (length - dot) - 1; //Subtrai o tamanho do texto do número pela posição do ponto e por mais uma posição que representa o próprio ponto
		
	if(positionDifference>digits){
		numberString = numberString.substring(0, numberString.length - (positionDifference - digits)); //Remove a quantidade de números no final do texto para que fique a quantidade de dígitos requerida.
	}
	
	number.value = numberString; //
}