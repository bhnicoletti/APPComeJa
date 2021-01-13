package br.com.nicoletti.comeja.model;

/**
 * Created by Nicoletti on 08/09/2016.
 */

public class Dispositivo {
    private Long idDispositivo;
    private String tokenDispositivo;
    private Long idEmpresa;

    public Long getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(Long idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public String getTokenDispositivo() {
        return tokenDispositivo;
    }

    public void setTokenDispositivo(String tokenDispositivo) {
        this.tokenDispositivo = tokenDispositivo;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Dispositivo() {
    }
}
