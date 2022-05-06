package com.hyperflame.desafio.mapper;

import com.hyperflame.desafio.model.Cliente;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

@NoArgsConstructor
public class ClienteFieldSetMapper implements FieldSetMapper<Cliente> {

    @Override
    public Cliente mapFieldSet(FieldSet fieldSet) throws BindException {
        return getCliente(fieldSet);
    }

    private Cliente getCliente(FieldSet fieldSet) {
        Cliente cliente = new Cliente();
        cliente.setId(fieldSet.readString(0));
        cliente.setNome(fieldSet.readString(1));
        cliente.setCpf(fieldSet.readString(2));
        cliente.setSalario(fieldSet.readString(3));
        return cliente;
    }
}
