package br.gov.ba.ws;

import java.util.HashSet;
import java.util.Set;

//@ApplicationPath("/app")
/**
 * Classe Para gerenciar as classes de serviços do Web Service.
 * @author jorge.ramos
 *
 */
public class SseApl extends javax.ws.rs.core.Application {
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(ServicoLogin.class);
        classes.add(ServicoRequerimento.class);
        classes.add(ServicoRequerente.class);
        classes.add(ServicoProcesso.class);
        classes.add(ServicoNotificacao.class);
        classes.add(ServicoEmpreendimento.class);
        classes.add(ServicoSeguranca.class);
        classes.add(ServicoPauta.class);
        classes.add(ServicoMunicipio.class);
        classes.add(ServicoAlerta.class);
        return classes;
    }
}