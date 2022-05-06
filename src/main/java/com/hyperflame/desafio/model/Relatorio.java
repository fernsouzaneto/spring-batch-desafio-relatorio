package com.hyperflame.desafio.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
@AllArgsConstructor
public class Relatorio implements Serializable {
    private List<Cliente> clientes;
    private List<Vendedor> vendedores;
    private List<Vendas> vendas;

    public Relatorio(){
        this.clientes = new ArrayList<>();
        this.vendedores = new ArrayList<>();
        this.vendas = new ArrayList<>();
    }

    public int consultaTotalClientes(){
        return  this.clientes.size();
    }

    public int consultaTotalVendedores(){
        return  this.vendedores.size();
    }

    public String consultaIdVendaMaisCara(){
        Optional<Produto> produtoMaisCaro = this.vendas.stream()
                .map(Vendas::getProdutos)
                .flatMap(Collection::stream)
                .max(Comparator.comparing(Produto::getPreco));

        if(produtoMaisCaro.isPresent())
        for (Vendas venda : this.vendas) {
            List<Produto> produtos = venda.getProdutos();
            if(produtos.stream().anyMatch(p -> Objects.equals(p, produtoMaisCaro.get()))){
                return venda.getCodigoVenda();
            }
        }

        return "-1";
    }

    public String consultaPiorVendedor(){
        Optional<Vendas> piorVenda =  this.vendas.stream()
                .min(Comparator.comparing(Vendas::getTotalVendido));
        return piorVenda.map(Vendas::getNomeVendedor).orElse("");
    }
}
