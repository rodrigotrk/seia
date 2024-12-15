package br.gov.ba.seia.historico.suspensao;

import java.util.Date;
import java.util.List;

import br.gov.ba.seia.entity.MotivoSuspensaoCadastro;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.interfaces.AgrupadorInterface;
/**
 * Classe do filtro do historico de suspensão
 * @author 
 *
 */
public class FiltroHistoricoSuspensao implements AgrupadorInterface<FiltroHistoricoSuspensao> {
	
	private Date dataInicio;
	
	private Date dataFim;
		
	private Date dataSuspensao;
	
	private Usuario usuario;
	
	private String motivo; 
	
	private String  observacao;
	
	private String  notificacaoOuAuto;
	
	private Integer situacao;
	
	private Integer ideSuspensaoCadastro;

	private PessoaFisica pessoa;
	
	private List <MotivoSuspensaoCadastro> listaMotivos;
	
	@Override
	public FiltroHistoricoSuspensao getObjetoAgrupado() {
		return this;
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getNotificacaoOuAuto() {
		return notificacaoOuAuto;
	}

	public void setNotificacaoOuAuto(String notificacaoOuAuto) {
		this.notificacaoOuAuto = notificacaoOuAuto;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public Date getDataSuspensao() {
		return dataSuspensao;
	}

	public void setDataSuspensao(Date dataSuspensao) {
		this.dataSuspensao = dataSuspensao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List <MotivoSuspensaoCadastro> getListaMotivos() {
		return listaMotivos;
	}

	public void setListaMotivos(List <MotivoSuspensaoCadastro> listaMotivos) {
		this.listaMotivos = listaMotivos;
	}

	public Integer getIdeSuspensaoCadastro() {
		return ideSuspensaoCadastro;
	}

	public void setIdeSuspensaoCadastro(Integer ideSuspensaoCadastro) {
		this.ideSuspensaoCadastro = ideSuspensaoCadastro;
	}
	
	public PessoaFisica getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaFisica pessoa) {
		this.pessoa = pessoa;
	}
}
