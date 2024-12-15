package br.gov.ba.seia.dto;

import java.util.List;

import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.TelaAcaoEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

public class CerhDTO {

	private CerhAbaDadoGeraisDTO abaDadoGerais;

	private List<CerhAbaDTO> abas;

	private TelaAcaoEnum telaAcaoEnum;

	private Integer ideCerh;

	private Boolean pendente;

	private Pessoa requerente;

	private Pessoa solicitante;
	
	private CerhProcesso cerhProcesso;

	public CerhDTO(Integer ideCerh, String acao) {
		this.ideCerh = ideCerh;
		this.telaAcaoEnum = (acao == null) ? TelaAcaoEnum.CADASTRAR : TelaAcaoEnum.getEnum(acao);

		this.requerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa();
		this.solicitante = ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante().getPessoa();
	}

	public CerhDTO(Integer ideCerh, TelaAcaoEnum telaAcaoEnum) {
		this.ideCerh = ideCerh;
		this.telaAcaoEnum = telaAcaoEnum;
	}

	public boolean isValido() {
		if(isCadastrar()) {
			return !Util.isNull(requerente) && !Util.isNull(solicitante);
		}
		return true;
	}

	public CerhAbaDTO getAba(TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum){
		return getAba(new TipoUsoRecursoHidrico(tipoUsoRecursoHidricoEnum));
	}

	public CerhAbaDTO getAba(TipoUsoRecursoHidrico tipoUsoRecursoHidrico){
		if(!Util.isNull(abas)){
			for (CerhAbaDTO cerhAbaDTO : abas) {
				if(tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico().equals(cerhAbaDTO.getTipoUsoRecursoHidricoEnum().getId())){
					return cerhAbaDTO;
				}
			}
		}
		return null;
	}

	public boolean isEditar() {
		return telaAcaoEnum.isEditar();
	}

	public boolean isVisualizar() {
		return telaAcaoEnum.isVisualizar();
	}

	public boolean isCadastrar() {
		return telaAcaoEnum.isCadastrar();
	}

	public CerhAbaDadoGeraisDTO getAbaDadoGerais() {
		return abaDadoGerais;
	}

	public void setAbaDadoGerais(CerhAbaDadoGeraisDTO abaDadoGerais) {
		this.abaDadoGerais = abaDadoGerais;
	}

	public Integer getIdeCerh() {
		return ideCerh;
	}

	public void setIdeCerh(Integer ideCerh) {
		this.ideCerh = ideCerh;
	}

	public Boolean getPendente() {
		return pendente;
	}

	public void setPendente(Boolean pendente) {
		this.pendente = pendente;
	}

	public TelaAcaoEnum getTelaAcaoEnum() {
		return telaAcaoEnum;
	}

	public void setTelaAcaoEnum(TelaAcaoEnum telaAcaoEnum) {
		this.telaAcaoEnum = telaAcaoEnum;
	}

	public List<CerhAbaDTO> getAbas() {
		return abas;
	}

	public void setAbas(List<CerhAbaDTO> abas) {
		this.abas = abas;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public Pessoa getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Pessoa solicitante) {
		this.solicitante = solicitante;
	}

	public CerhProcesso getCerhProcesso() {
		return cerhProcesso;
	}

	public void setCerhProcesso(CerhProcesso cerhProcesso) {
		this.cerhProcesso = cerhProcesso;
	}

	
}
