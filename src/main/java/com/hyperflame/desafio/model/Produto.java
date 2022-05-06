package com.hyperflame.desafio.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Produto implements Serializable {
    private String id;
    private Integer quantidade;
    private Double preco;
}
