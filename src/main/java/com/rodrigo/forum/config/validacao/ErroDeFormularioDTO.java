package com.rodrigo.forum.config.validacao;

public class ErroDeFormularioDTO {

    private String campo;
    private String error;

    public ErroDeFormularioDTO(String campo, String error) {
        this.campo = campo;
        this.error = error;
    }

    public String getCampo() {
        return campo;
    }

    public String getError() {
        return error;
    }
}
