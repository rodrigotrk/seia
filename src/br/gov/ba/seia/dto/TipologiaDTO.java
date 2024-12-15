package br.gov.ba.seia.dto;

import java.util.ArrayList;
import java.util.List;

import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.interfaces.BaseEntity;

public class TipologiaDTO implements BaseEntity {

    private boolean checked;
    private Tipologia tipologia;
    private List<TipologiaDTO> tipologiasFilhas;
    private List<TipologiaDTO> tipologiasSelected;
    
    public void marcar() {
        checked = true;
    }

    public void desmarcar() {
        checked = false;
    }
    
    public TipologiaDTO() {
		super();
	}

    public TipologiaDTO(Tipologia tipologia){
        this.tipologia = tipologia;
        tipologiasFilhas = new ArrayList<TipologiaDTO>();
        tipologiasSelected = new ArrayList<TipologiaDTO>();
    }
    
    /*********************
	/*					 *
	//XXX: GETS AND SETS *
	/* 					 *
	/*********************/
    
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public List<TipologiaDTO> getTipologiasFilhas() {
		return tipologiasFilhas;
	}

	public void setTipologiasFilhas(List<TipologiaDTO> tipologiasFilhas) {
		this.tipologiasFilhas = tipologiasFilhas;
	}

	public List<TipologiaDTO> getTipologiasSelected() {
		return tipologiasSelected;
	}

	public void setTipologiasSelected(List<TipologiaDTO> tipologiasSelected) {
		this.tipologiasSelected = tipologiasSelected;
	}

	@Override
	public Long getId() {
		return Long.valueOf(tipologia.getId());
	}
}