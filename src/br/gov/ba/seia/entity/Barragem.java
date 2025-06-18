package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.util.Util;


@Entity
@Table(name="barragem")
public class Barragem extends AbstractEntityHist implements Comparable<Barragem> {
	private static final long serialVersionUID = 1L;
	
	private static final Integer OUTROS = 361;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "barragem_seq")
	@SequenceGenerator(name = "barragem_seq", sequenceName = "barragem_seq", allocationSize = 1)
	@Column(name="ide_barragem")
	private Integer ideBarragem;

	@Historico(name="Nome da barragem")
	@Column(name="nom_barragem")
	private String nomBarragem;
	
	@Column(name="ind_origem_usuario")
	private Boolean indOrigemUsuario;
	
	@OneToMany(mappedBy="ideBarragem")
	private Collection<CerhBarragemCaracterizacao> cerhBarragemCaracterizacaoCollection;

	@OneToMany(mappedBy="ideBarragem")
	private Collection<CerhCaptacaoCaracterizacao> cerhCaptacaoCaracterizacaoCollection;
	/**
	 * Construtor
	 */
	public Barragem() {
	}
	/**
	 * Construtor com parametros
	 * @param integer
	 * @param string
	 */
	public Barragem(Integer integer, String string) {
		this.ideBarragem = integer;
		this.nomBarragem = string;
	}

	public Integer getIdeBarragem() {
		return ideBarragem;
	}

	public void setIdeBarragem(Integer ideBarragem) {
		this.ideBarragem = ideBarragem;
	}

	public String getNomBarragem() {
		return nomBarragem;
	}

	public void setNomBarragem(String nomBarragem) {
		this.nomBarragem = nomBarragem;
	}

	public Collection<CerhBarragemCaracterizacao> getCerhBarragemCaracterizacaoCollection() {
		return cerhBarragemCaracterizacaoCollection;
	}

	public void setCerhBarragemCaracterizacaoCollection(Collection<CerhBarragemCaracterizacao> cerhBarragemCaracterizacaoCollection) {
		this.cerhBarragemCaracterizacaoCollection = cerhBarragemCaracterizacaoCollection;
	}

	public Collection<CerhCaptacaoCaracterizacao> getCerhCaptacaoCaracterizacaoCollection() {
		return cerhCaptacaoCaracterizacaoCollection;
	}

	public void setCerhCaptacaoCaracterizacaoCollection(Collection<CerhCaptacaoCaracterizacao> cerhCaptacaoCaracterizacaoCollection) {
		this.cerhCaptacaoCaracterizacaoCollection = cerhCaptacaoCaracterizacaoCollection;
	}

	public boolean isOutros() {
		return this.ideBarragem.equals(OUTROS);
	}

	public Boolean getIndOrigemUsuario() {
		return indOrigemUsuario;
	}
	
	public void setIndOrigemUsuario(Boolean indOrigemUsuario) {
		this.indOrigemUsuario = indOrigemUsuario;
	}
	
	@Override
	public int compareTo(Barragem o) {
		return Util.substituirCaracterEspecial(this.nomBarragem).toLowerCase().compareTo(Util.substituirCaracterEspecial(o.nomBarragem).toLowerCase());
	}
	
}