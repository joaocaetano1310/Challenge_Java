package com.futurevet.api.entity.enums;

public enum EspeciePet {
    CAO("Cão"),
    GATO("Gato"),
    COELHO("Coelho"),
    OUTRO("Outro");

    private final String descricao;

    EspeciePet(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
