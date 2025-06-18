package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Tipologia;

public class TransferenciaAmbientalDTO {

	private Boolean  visivel;
	private Processo processo;
	private AtoAmbiental atoAmbiental;
	private Tipologia    tipologia;
	private Pessoa Cedente;
	private Pessoa	Receptor;
		
	public TransferenciaAmbientalDTO(){}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public AtoAmbiental getAtoAmbiental() {
		return atoAmbiental;
	}

	public void setAtoAmbiental(AtoAmbiental atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	public Pessoa getCedente() {
		return Cedente;
	}

	public void setCedente(Pessoa cedente) {
		Cedente = cedente;
	}

	public Pessoa getReceptor() {
		return Receptor;
	}

	public void setReceptor(Pessoa receptor) {
		Receptor = receptor;
	}

	public Tipologia getTipologia() {
		
		if(tipologia==null){
			Tipologia t =  new Tipologia();
			t.setDesTipologia(" --- ");
			return t;
		}
		
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public Boolean getVisivel() {
		return visivel;
	}

	public void setVisivel(Boolean visivel) {
		this.visivel = visivel;
	}	
}
