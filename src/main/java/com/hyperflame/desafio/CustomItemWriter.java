package com.hyperflame.desafio;

import com.hyperflame.desafio.model.Cliente;
import com.hyperflame.desafio.model.Relatorio;
import com.hyperflame.desafio.model.Vendas;
import com.hyperflame.desafio.model.Vendedor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomItemWriter implements ItemWriter<Object> {
    Logger logger = LoggerFactory.getLogger(CustomItemWriter.class);
    private Relatorio relatorio = new Relatorio();
    private FlatFileItemWriter<Object> delegate;
    private int total = 0;
    private boolean completed;
    private long jobExecutionId;

    @AfterStep
    public void afterStep(StepExecution stepExecution){
        try {
            logger.info("[ItemWriter] Escrevendo dados no arquivo...");
            this.getDelegate().open(new ExecutionContext());
            this.delegate.write(montarDadosRelatorio());
            logger.info("[ItemWriter] Dados escritos com sucesso!!!");
            logger.info("[ItemWriter] ID Operacao -> " + stepExecution.getId());
            logger.info("[ItemWriter] Total processados -> " + stepExecution.getWriteCount());
        } catch (Exception e) {
            logger.error("[ItemWriter] Ocorreu um erro -> " + e.getMessage());
        }
        finally {
            delegate.close();
        }
        logger.error("[ItemWriter] STATUS -> " + stepExecution.getStatus());
    }

    @Override
    public void write(List<? extends Object> list) throws Exception {
        for (Object obj : list) {
            if (obj instanceof Vendas) {
                relatorio.getVendas().add((Vendas) obj);
            }else if (obj instanceof Cliente) {
                relatorio.getClientes().add((Cliente) obj);
            } else if (obj instanceof Vendedor) {
                relatorio.getVendedores().add((Vendedor) obj);
            }
        }
    }

    private List<String> montarDadosRelatorio() {
        String totalClientes = "TOTAL CLIENTES -> " + relatorio.consultaTotalClientes();
        String totalVendedores = "TOTAL VENDEDORES -> " + relatorio.consultaTotalVendedores();
        String IdVendaMaisCara = "ID VENDA MAIS CARA -> " + relatorio.consultaIdVendaMaisCara();
        String piorVendedor = "PIOR VENDEDOR -> " + relatorio.consultaPiorVendedor();
        return Arrays.asList(totalClientes, totalVendedores, IdVendaMaisCara, piorVendedor);
    }
}
