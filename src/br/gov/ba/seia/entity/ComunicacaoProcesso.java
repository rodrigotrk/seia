package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.interfaces.ComunicacaoRequerenteInterface;

@Entity
@Table(name = "comunicacao_processo")
public class ComunicacaoProcesso implements Serializable, ComunicacaoRequerenteInterface {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="comunicacao_processo_ide_comunicacao_processo_seq")
	@SequenceGenerator(name="comunicacao_processo_ide_comunicacao_processo_seq", sequenceName="comunicacao_processo_ide_comunicacao_processo_seq", allocationSize=1)
	@Basic(optional = false)
	@Column(name = "ide_comunicacao_processo", nullable = false)
	private Integer ideComunicacaoProcesso;
	@Basic(optional = false)
	@Column(name = "dtc_comunicacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcComunicacao;
	@Basic(optional = false)
	@Column(name = "des_mensagem", nullable = false)
	private String desMensagem;
	@JoinColumn(name="ide_processo", referencedColumnName="ide_processo",nullable=false)
	@OneToOne(fetch=FetchType.LAZY,optional=false)
	private Processo ideProcesso;	
	
	public ComunicacaoProcesso(){
		
	}
	
	public ComunicacaoProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}
	
	public ComunicacaoProcesso(Date dtcComunicacao, String desMensagem,Processo ideProcesso) {
		this.dtcComunicacao = dtcComunicacao;
		this.desMensagem = desMensagem;
		this.ideProcesso = ideProcesso;
	}
	
	public Integer getIdeComunicacaoProcesso() {
		return ideComunicacaoProcesso;
	}
	public void setIdeComunicacaoProcesso(Integer ideComunicacaoProcesso) {
		this.ideComunicacaoProcesso = ideComunicacaoProcesso;
	}
	public Date getDtcComunicacao() {
		return dtcComunicacao;
	}
	
	public String getDtcComunicacaoFormatado(){
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formatter.format(dtcComunicacao);
	}
	public void setDtcComunicacao(Date dtcComunicacao) {
		this.dtcComunicacao = dtcComunicacao;
	}
	public String getDesMensagem() {
		return desMensagem;
	}
	public void setDesMensagem(String desMensagem) {
		this.desMensagem = desMensagem;
	}
	public Processo getIdeProcesso() {
		return ideProcesso;
	}
	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}
	
	public String getDesMensagemResumo(){
		String resumo=desMensagem.replace("<br />", "\n");
		if(resumo.length()>99){
			resumo = resumo.substring(0,99);
			while(resumo.charAt(resumo.length() - 1) != ' '){
				resumo = resumo.substring(0, resumo.length()-2);
			}
			return resumo.substring(0,resumo.length()-1) + "...";
		}
		else{
			return desMensagem;
		}
	}
}
