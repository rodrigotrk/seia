package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "outorga_localizacao_geografica_imovel")
@XmlRootElement
@NamedQuery(name = "OutorgaLocalizacaoGeograficaImovel.removeByideOLGI", query = "delete FROM OutorgaLocalizacaoGeograficaImovel olgi WHERE olgi.ideOutorgaLocalizacaoGeografica = :ideOutorgaLocalizacaoGeografica")
public class OutorgaLocalizacaoGeograficaImovel implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected OutorgaLocalizacaoGeograficaImovelPK outorgaLocalizacaoGeograficaImovelPK;

	@JoinColumn(name = "ide_outorga_localizacao_geografica", referencedColumnName = "ide_outorga_localizacao_geografica", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica;

	@JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Imovel ideImovel;

	public OutorgaLocalizacaoGeograficaImovel() {

	}

	public OutorgaLocalizacaoGeograficaImovel(OutorgaLocalizacaoGeograficaImovelPK outorgaLocalizacaoGeograficaImovelPK) {
		this.outorgaLocalizacaoGeograficaImovelPK = outorgaLocalizacaoGeograficaImovelPK;
	}

	public OutorgaLocalizacaoGeograficaImovel(OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica,
			Imovel ideImovel) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
		this.ideImovel = ideImovel;
	}

	public OutorgaLocalizacaoGeografica getIdeOutorgaLocalizacaoGeografica() {
		return ideOutorgaLocalizacaoGeografica;
	}

	public void setIdeOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}

	public Imovel getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(Imovel ideImovel) {
		this.ideImovel = ideImovel;
	}

	public OutorgaLocalizacaoGeograficaImovelPK getOutorgaLocalizacaoGeograficaImovelPK() {
		return outorgaLocalizacaoGeograficaImovelPK;
	}

	public void setOutorgaLocalizacaoGeograficaImovelPK(
			OutorgaLocalizacaoGeograficaImovelPK outorgaLocalizacaoGeograficaImovelPK) {
		this.outorgaLocalizacaoGeograficaImovelPK = outorgaLocalizacaoGeograficaImovelPK;
	}

}
