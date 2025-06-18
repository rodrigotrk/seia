package br.gov.ba.seia.entity;

import java.io.Serializable;

import br.gov.ba.seia.interfaces.BaseEntity;

public class GeoBahia implements Serializable, BaseEntity {


	private static final long serialVersionUID = -1354118927939852562L;
	private Integer gid;
    private String nome;
    
    public GeoBahia() {
    }

    public GeoBahia(Integer gid) {
        this.setGid(gid);
    }

	public GeoBahia(Integer gid, String nome) {
        this.setGid(gid);
        this.setNome(nome);
    }

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
    public String toString() {
    	return String.valueOf(gid);
    }

	@Override
	public Long getId() {
		return new Long(this.gid);
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gid == null) ? 0 : gid.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeoBahia other = (GeoBahia) obj;
		if (gid == null) {
			if (other.gid != null)
				return false;
		} else if (!gid.equals(other.gid))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
}
