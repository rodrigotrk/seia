package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;

@Entity
@Table(name="cerh_captacao_outros_usos")
public class CerhCaptacaoOutrosUsos extends AbstractEntityHist implements CerhDadosFinalidadeInterface  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_captacao_outros_usos_seq")
	@SequenceGenerator(name = "cerh_captacao_outros_usos_seq", sequenceName = "cerh_captacao_outros_usos_seq", allocationSize = 1)
	@Column(name="ide_cerh_captacao_outros_usos")
	private Integer ideCerhCaptacaoOutrosUsos;

	@ManyToOne
	@JoinColumn(name="ide_cerh_captacao_caracterizacao_finalidade")
	private CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade;

	@Historico
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ide_cerh_outros_usos")
	private CerhOutrosUsos ideCerhOutrosUsos;

	public CerhCaptacaoOutrosUsos() {
		
	}

	public CerhCaptacaoOutrosUsos(CerhCaptacaoCaracterizacaoFinalidade cerhFinalidadeOutros) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = cerhFinalidadeOutros;
	}

	public Integer getIdeCerhCaptacaoOutrosUsos() {
		return ideCerhCaptacaoOutrosUsos;
	}

	public void setIdeCerhCaptacaoOutrosUsos(Integer ideCerhCaptacaoOutrosUsos) {
		this.ideCerhCaptacaoOutrosUsos = ideCerhCaptacaoOutrosUsos;
	}

	public CerhCaptacaoCaracterizacaoFinalidade getIdeCerhCaptacaoCaracterizacaoFinalidade() {
		return ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public void setIdeCerhCaptacaoCaracterizacaoFinalidade(
			CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public CerhOutrosUsos getIdeCerhOutrosUsos() {
		return ideCerhOutrosUsos;
	}

	public void setIdeCerhOutrosUsos(CerhOutrosUsos ideCerhOutrosUsos) {
		this.ideCerhOutrosUsos = ideCerhOutrosUsos;
	}

	@Override
	public Integer getIde() {
		return this.ideCerhCaptacaoOutrosUsos;
	}
}