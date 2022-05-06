package com.hyperflame.desafio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class Vendas implements Serializable {
    private String id;
    private String codigoVenda;
    private String nomeVendedor;
    private List<Produto> produtos;
    private Double totalVendido;

    public void calcularTotalVendido() {
        this.totalVendido = 0D;
        for (Produto produto : this.produtos) {
            totalVendido += produto.getPreco() * produto.getQuantidade();
        }
    }
}
