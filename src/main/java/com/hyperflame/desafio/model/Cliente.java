package com.hyperflame.desafio.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Cliente implements Serializable {
    private String id;
    private String cpf;
    private String nome;
    private String salario;
}
