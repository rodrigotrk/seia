package br.gov.ba.seia.dto;

import java.util.ArrayList;
import java.util.List;

import br.gov.ba.seia.entity.EspecieSupressaoAutorizacao;

/**
 * 
 * @author eduardo.fernandes 
 * @since 06/03/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
 *
 */
public class DadoConcedidoDTO {
	
	private Integer ideImovel;
	private String nomImovel;
	private String latitudeInicial;
	private String longitudeInicial;
	private DadoConcedidoPoligonalDTO poligonalConcedida;
	private List<DadoConcedidoPoligonalDTO> listaPoligonalDTO;
	private List<DadoConcedidoPoligonalDTO> listaPoligonalNotificacaoDTO;
	private List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao;
	
	public DadoConcedidoDTO(Integer ideImovel, String nome) {
		this.ideImovel = ideImovel;
		this.nomImovel = nome;
		this.listaPoligonalDTO = new ArrayList<DadoConcedidoPoligonalDTO>();
	}

	public String getNomImovel() {
		return nomImovel;
	}

	public List<DadoConcedidoPoligonalDTO> getListaPoligonalDTO() {
		return listaPoligonalDTO;
	}
	
	public void setListaPoligonalDTO(List<DadoConcedidoPoligonalDTO> lista) {
		this.listaPoligonalDTO = lista;
	}

	public List<DadoConcedidoPoligonalDTO> getListaPoligonalNotificacaoDTO() {
		return listaPoligonalNotificacaoDTO;
	}

	public void setListaPoligonalNotificacaoDTO(List<DadoConcedidoPoligonalDTO> listaPoligonalNotificacaoDTO) {
		this.listaPoligonalNotificacaoDTO = listaPoligonalNotificacaoDTO;
	}

	public DadoConcedidoPoligonalDTO getPoligonalConcedida() {
		return poligonalConcedida;
	}

	public void setPoligonalConcedida(DadoConcedidoPoligonalDTO poligonalConcedida) {
		this.poligonalConcedida = poligonalConcedida;
	}

	public Integer getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(Integer ideImovel) {
		this.ideImovel = ideImovel;
	}

	public String getLatitudeInicial() {
		return latitudeInicial;
	}

	public void setLatitudeInicial(String latitudeInicial) {
		this.latitudeInicial = latitudeInicial;
	}

	public String getLongitudeInicial() {
		return longitudeInicial;
	}

	public void setLongitudeInicial(String longitudeInicial) {
		this.longitudeInicial = longitudeInicial;
	}

	public List<EspecieSupressaoAutorizacao> getListaEspecieSupressaoAutorizacao() {
		return listaEspecieSupressaoAutorizacao;
	}

	public void setListaEspecieSupressaoAutorizacao(
			List<EspecieSupressaoAutorizacao> listaEspecieSupressaoAutorizacao) {
		this.listaEspecieSupressaoAutorizacao = listaEspecieSupressaoAutorizacao;
	}
	
}
