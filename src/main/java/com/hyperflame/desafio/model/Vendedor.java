package com.hyperflame.desafio.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Vendedor implements Serializable {
    private String id;
    private String cnpj;
    private String business;
    private String area;
}
