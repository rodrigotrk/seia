package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * The persistent class for the licenca_objeto_alteracao database table.
 * 
 */
@Entity
@Table(name = "licenca_objeto_alteracao")
@NamedQueries({ @NamedQuery(name = "LicencaObjetoAlteracao.removedByIdeLicenca", query = "DELETE FROM LicencaObjetoAlteracao l WHERE l.ideLicenca = :ideLicenca") })
public class LicencaObjetoAlteracao implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LicencaObjetoAlteracaoPK id;

	@NotNull
	@JoinColumn(name = "ide_licenca", referencedColumnName = "ide_licenca", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Licenca ideLicenca;

	@NotNull
	@JoinColumn(name = "ide_objeto_alteracao", referencedColumnName = "ide_objeto_alteracao", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ObjetoAlteracao ideObjetoAlteracao;

	public LicencaObjetoAlteracao() {
	}

	public LicencaObjetoAlteracao(Licenca licenca, ObjetoAlteracao obj) {
		this.ideLicenca = licenca;
		this.ideObjetoAlteracao = obj;
		this.id = new LicencaObjetoAlteracaoPK(licenca.getIdeLicenca(), obj.getIdeObjetoAlteracao());

	}

	public LicencaObjetoAlteracaoPK getId() {
		return this.id;
	}

	public void setId(LicencaObjetoAlteracaoPK id) {
		this.id = id;
	}

	public ObjetoAlteracao getIdeObjetoAlteracao() {
		return ideObjetoAlteracao;
	}

	public void setIdeObjetoAlteracao(ObjetoAlteracao ideObjetoAlteracao) {
		this.ideObjetoAlteracao = ideObjetoAlteracao;
	}

	public Licenca getIdeLicenca() {
		return ideLicenca;
	}

	public void setIdeLicenca(Licenca ideLicenca) {
		this.ideLicenca = ideLicenca;
	}

}