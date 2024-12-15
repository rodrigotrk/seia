function atualizarNomeCientificoItalico(){
	selecioneEspecie();
	tabEspecie();
}

function tabEspecie() {
	
	var listaTag = $('.tabEspecie a');

	for(i=0; i < listaTag.length; i++) {
		var tag = listaTag[i];
		var especie = tag.text;
		var naoTemSpan = $(tag).find('span').length == 0;
		if(especie != 'Outros' && naoTemSpan){
			if(especie.indexOf('-')==-1) {
				$(tag).text('');
				$(tag).append('<span style="font-style: italic;">'+especie+'</span>');
			}
			else{
				var nomeCientifico = especie.substring(0, especie.indexOf('-'));
				var nomePopular = especie.substring(especie.indexOf('-')+1,especie.length);
				$(tag).text('');	
				$(tag).append('<span style="font-style: italic;">'+nomeCientifico+'</span>&nbsp;-&nbsp;<span>'+nomePopular+'</span>');
			}
		}
	}	
}

function selecioneEspecie() {
	
	var listaTag = $('.selecioneEspecie ul li');

	for(i=0; i < listaTag.length; i++) {
		var tag = listaTag[i];
		var especie = tag.textContent;
		var naoTemSpan = $(tag).find('span').length == 0;
		if(especie != 'Outros' && naoTemSpan){
			if(especie.indexOf('-')==-1) {
				$(tag).text('');
				$(tag).append('<span style="font-style: italic;">'+especie+'</span>');
			}
			else{
				var nomeCientifico = especie.substring(0, especie.indexOf('-'));
				var nomePopular = especie.substring(especie.indexOf('-')+1,especie.length);
				$(tag).text('');
				$(tag).append('<span style="font-style: italic;">'+nomeCientifico+'</span>&nbsp;-&nbsp;<span>'+nomePopular+'</span>');
			}	
		}
	}	
}

function checkEspecie() {
	
	var listaTag = $('.selecioneEspecie tbody tr');
	for(i=0; i < listaTag.length; i++) {
		var tag = $(listaTag[i]);
		var td = $(tag).children('td').last();
		var especie = $(td).text();
		var naoTemSpan = $(td).find('span').length == 0;
		if(especie != 'Outros' && naoTemSpan){
			if(especie.indexOf('-')==-1) {
				$(td).text('');
				$(td).append('<span style="font-style: italic;">'+especie+'</span>');
			}
			else{
				var nomeCientifico = especie.substring(0, especie.indexOf('-'));
				var nomePopular = especie.substring(especie.indexOf('-')+1,especie.length);
				$(td).text('');
				$(td).append('<span style="font-style: italic;">'+nomeCientifico+'</span>&nbsp;-&nbsp;<span>'+nomePopular+'</span>');
			}	
		}
	}	
}