package br.gov.ba.seia.dto;


public class ColunasExibirRelatorioQuantitativoImoveisRuraisDTO {
	
	private boolean imoveisRurais;
	private boolean reservaLegal;
	private boolean app;
	private boolean areaProdutiva;
	private boolean vegetacaoNativa;
	private boolean usoAgua;
	private boolean outrosPassivos;
	private boolean rppn;
	private boolean selecaoTodos;
	

	public ColunasExibirRelatorioQuantitativoImoveisRuraisDTO(){
		
	}
	
	public ColunasExibirRelatorioQuantitativoImoveisRuraisDTO(boolean selecionarTodos){
		this.imoveisRurais = selecionarTodos;
		this.reservaLegal = selecionarTodos;
		this.app = selecionarTodos;
		this.areaProdutiva = selecionarTodos;
		this.vegetacaoNativa = selecionarTodos;
		this.usoAgua = selecionarTodos;
		this.outrosPassivos = selecionarTodos;
		this.rppn = selecionarTodos;
		this.selecaoTodos = selecionarTodos;
	}
	
	public boolean isAlgumTemaSelecionado() {
		return imoveisRurais || reservaLegal || app ||	areaProdutiva || vegetacaoNativa || usoAgua ||outrosPassivos || rppn;
	}
	
	public boolean isSelecaoTodos() {
		return selecaoTodos;
	}

	public void setSelecaoTodos(boolean selecaoTodos) {
		this.selecaoTodos = selecaoTodos;
	}
	
	public boolean isImoveisRurais() {
		return imoveisRurais;
	}

	public void setImoveisRurais(boolean imoveisRurais) {
		this.imoveisRurais = imoveisRurais;
	}

	public boolean isReservaLegal() {
		return reservaLegal;
	}

	public void setReservaLegal(boolean reservaLegal) {
		this.reservaLegal = reservaLegal;
	}

	public boolean isApp() {
		return app;
	}

	public void setApp(boolean app) {
		this.app = app;
	}

	public boolean isAreaProdutiva() {
		return areaProdutiva;
	}

	public void setAreaProdutiva(boolean areaProdutiva) {
		this.areaProdutiva = areaProdutiva;
	}

	public boolean isVegetacaoNativa() {
		return vegetacaoNativa;
	}

	public void setVegetacaoNativa(boolean vegetacaoNativa) {
		this.vegetacaoNativa = vegetacaoNativa;
	}

	public boolean isUsoAgua() {
		return usoAgua;
	}

	public void setUsoAgua(boolean usoAgua) {
		this.usoAgua = usoAgua;
	}

	public boolean isOutrosPassivos() {
		return outrosPassivos;
	}

	public void setOutrosPassivos(boolean outrosPassivos) {
		this.outrosPassivos = outrosPassivos;
	}

	public boolean isRppn() {
		return rppn;
	}

	public void setRppn(boolean rppn) {
		this.rppn = rppn;
	}
}	
