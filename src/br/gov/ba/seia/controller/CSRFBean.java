package br.gov.ba.seia.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.gov.ba.seia.util.security.CSRFTokenUtil;

@ManagedBean
@RequestScoped
public class CSRFBean {

	private String csrfToken;

    public String getCsrfToken() {
        if (csrfToken == null) {
            csrfToken = CSRFTokenUtil.generateToken();
            CSRFTokenUtil.storeToken(csrfToken);
        }
        return csrfToken;
    }
}
