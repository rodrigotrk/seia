package br.gov.ba.seia.dto;
import java.util.ArrayList;
import java.util.List;

public class StatusNotificacao {
	// essa classe deve virar uma entidade e ter seu próprio service, etc
	// etc....

	private Integer ideStatus;
	private String dscStatus;

	public StatusNotificacao(Integer ideStatus, String dscStatus) {
		this.ideStatus = ideStatus;
		this.dscStatus = dscStatus;

	}
	public StatusNotificacao() {

	}
	
	public StatusNotificacao(Integer ideStatus) {
		this.ideStatus = ideStatus;
		this.dscStatus = null;

	}

	public Integer getIdeStatus() {
		return ideStatus;
	}

	public void setIdeStatus(Integer ideStatus) {
		this.ideStatus = ideStatus;
	}

	public String getDscStatus() {
		return dscStatus;
	}

	public void setDscStatus(String dscStatus) {
		this.dscStatus = dscStatus;
	}

	public static List<StatusNotificacao> listarTodos() {
		List<StatusNotificacao> lista = new ArrayList<StatusNotificacao>();
		lista.add(new StatusNotificacao(1, "Emitida"));
		lista.add(new StatusNotificacao(2, "Para Revisão"));
		lista.add(new StatusNotificacao(3, "Cancelada"));
		lista.add(new StatusNotificacao(4, "Aguardando Aprovação"));

		return lista;
	}

	@Override
	public String toString() {
		return String.valueOf(ideStatus);
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideStatus != null ? ideStatus.hashCode() : 0);
        return hash;
    }

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StatusNotificacao)) {
            return false;
        }
		StatusNotificacao other = (StatusNotificacao) obj;
        if ((this.ideStatus == null && other.getIdeStatus() != null) || (this.ideStatus != null && !this.ideStatus.equals(other.ideStatus))) {
            return false;
        }
        return true;
	}
	
	

}
