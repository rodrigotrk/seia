package br.gov.ba.seia.dto;

import java.util.Date;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Requerimento;

public class ConsultaPublicaDTO {
	
	private Requerimento requerimento;
	private Date dtcAberturaRequerimento;
	private String requerente;
	private String empreendimento;
	private String statusRequerimento;
	private Processo processo;
	
	public ConsultaPublicaDTO(Requerimento requerimento, Date dtcAberturaRequerimento, String requerente, String empreendimento, String statusRequerimento, Processo processo) {
		this.requerimento = requerimento;
		this.dtcAberturaRequerimento = dtcAberturaRequerimento;
		this.requerente = requerente;
		this.empreendimento = empreendimento;
		this.statusRequerimento = statusRequerimento;
		this.processo = processo;
	}
	
	public Date getDtcAberturaRequerimento() {
		return dtcAberturaRequerimento;
	}
	
	public void setDtcAberturaRequerimento(Date dtcAberturaRequerimento) {
		this.dtcAberturaRequerimento = dtcAberturaRequerimento;
	}
	
	public String getRequerente() {
		return requerente;
	}
	
	public void setRequerente(String requerente) {
		this.requerente = requerente;
	}
	
	public String getEmpreendimento() {
		return empreendimento;
	}
	
	public void setEmpreendimento(String empreendimento) {
		this.empreendimento = empreendimento;
	}
	
	public String getStatusRequerimento() {
		return statusRequerimento;
	}
	
	public void setStatusRequerimento(String statusRequerimento) {
		this.statusRequerimento = statusRequerimento;
	}
	
	public Processo getProcesso() {
		return processo;
	}
	
	public void setProcesso(Processo processo) {
		this.processo = processo;
	}
	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}
}