package com.hyperflame.desafio.mapper;

import com.hyperflame.desafio.model.Produto;
import com.hyperflame.desafio.model.Vendas;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class VendasFieldSetMapper implements FieldSetMapper<Vendas> {

    @Override
    public Vendas mapFieldSet(FieldSet fieldSet) throws BindException {
        return getVendas(fieldSet);
    }

    private Vendas getVendas(FieldSet fieldSet) {
        Vendas vendas = new Vendas();
        vendas.setId(fieldSet.readString(0));
        vendas.setCodigoVenda(fieldSet.readString(1));
        vendas.setProdutos(convertStrFieldSetToProdutos(fieldSet.readString(2)));
        vendas.setNomeVendedor(fieldSet.readString(3));
        vendas.calcularTotalVendido();
        return vendas;
    }

    private List<Produto> convertStrFieldSetToProdutos(String str){
        List<Produto> produtos = new ArrayList<>();
        if(str == null){
            return produtos;
        }
        var arrVendas = str.replace("[","")
                .replace("]","")
                .split(",");
        for (String item : arrVendas){
            var arrItemInfo = item.split("-");
            Produto produto = new Produto();
            produto.setId(arrItemInfo[0]);
            produto.setQuantidade(Integer.parseInt(arrItemInfo[1]));
            produto.setPreco(Double.parseDouble(arrItemInfo[2]));
            produtos.add(produto);
        }

        return produtos;
    }
}
