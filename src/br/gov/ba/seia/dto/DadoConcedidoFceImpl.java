package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.util.Util;

public class DadoConcedidoFceImpl extends DadoConcedidoImpl {
	
	private Fce fce;
	
	public DadoConcedidoFceImpl(Fce fce) {
		super(fce.getProcessoAto());
		this.fce = fce;
	}
	
	@Override
	public String getStatus() {
		if(fce.isIndConcluido() == null || !fce.isIndConcluido()) {
			return Util.getString("analise_tecnica_lbl_status_fce_nao_concluido");
		}
		
		return Util.getString("analise_tecnica_lbl_status_fce_concluido");
	}
	
	@Override
	public String getDescricao() {
		return fce.getIdeDocumentoObrigatorio().getNomDocumentoObrigatorio();
	}
	
	@Override
	public boolean isRenderedFce() {
		return true;
	}
	
	@Override
	public boolean isRenderedDadoConcedido() {
		return false;
	}
	
	/**
	 * Esta regra foi feita para não exibir o FCE de ASV para o técnico;
	 * O que deve ser exibido é o Dado Concedido de ASV que é gerado a partir dos dados do requerimento e do FCE.
	 */
	@Override
	public boolean isVisivel() {
		return !fce.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.FCE_ASV.getId());
	}
	
	public Fce getFce() {
		return fce;
	}
	
	public void setFce(Fce fce) {
		this.fce = fce;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fce == null) ? 0 : fce.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		DadoConcedidoFceImpl other = (DadoConcedidoFceImpl) obj;
		if(fce == null) {
			if(other.fce != null) return false;
		} else if(!fce.equals(other.fce)) return false;
		return true;
	}
}
