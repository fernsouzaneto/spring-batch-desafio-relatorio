package com.hyperflame.desafio.mapper;

import com.hyperflame.desafio.model.Cliente;
import com.hyperflame.desafio.model.Vendedor;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

@NoArgsConstructor
public class VendedorFieldSetMapper implements FieldSetMapper<Vendedor> {

    @Override
    public Vendedor mapFieldSet(FieldSet fieldSet) throws BindException {
        return getVendedor(fieldSet);
    }

    private Vendedor getVendedor(FieldSet fieldSet) {
        Vendedor vendedor = new Vendedor();
        vendedor.setId(fieldSet.readString(0));
        vendedor.setCnpj(fieldSet.readString(1));
        vendedor.setBusiness(fieldSet.readString(2));
        vendedor.setArea(fieldSet.readString(3));
        return vendedor;
    }
}
