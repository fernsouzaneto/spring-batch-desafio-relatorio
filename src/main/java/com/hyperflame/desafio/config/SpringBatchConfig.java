package com.hyperflame.desafio.config;

import com.hyperflame.desafio.CustomItemProcessor;
import com.hyperflame.desafio.CustomItemWriter;
import com.hyperflame.desafio.mapper.ClienteFieldSetMapper;
import com.hyperflame.desafio.mapper.VendasFieldSetMapper;
import com.hyperflame.desafio.mapper.VendedorFieldSetMapper;
import com.hyperflame.desafio.model.Relatorio;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class SpringBatchConfig extends DefaultBatchConfigurer {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private PropertiesConfig propertiesConfig;

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory
                .get("gerarRelatorio")
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory
                .get("step-01")
                .<String, Relatorio>chunk(2)
                .reader(customReader())
                .processor(processor())
                .writer(customItemWriter())
                .build();
    }


    @Bean
    public MultiResourceItemReader customReader() throws Exception {

        MultiResourceItemReader multiResourceItemReader = new MultiResourceItemReader();
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] arquivos = null;

        try {
            String dirEntrada = formatarDiretorio(propertiesConfig.getDirEntrada());
            String extensaoArqEntrada = propertiesConfig.getExtensaoArqEntrada();

            arquivos = patternResolver.getResources("file:" + dirEntrada + extensaoArqEntrada);

            multiResourceItemReader.setDelegate(flatFileItemReader());
            multiResourceItemReader.setResources(arquivos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return multiResourceItemReader;
    }

    @Bean
    public CustomItemWriter customItemWriter() {
        CustomItemWriter customItemWriter = new CustomItemWriter();
        customItemWriter.setDelegate(writer());
        return customItemWriter;
    }

    @Bean
    public CustomItemProcessor processor() {
        return new CustomItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<Object> writer() {
        FlatFileItemWriter<Object> itemWriter = new FlatFileItemWriter<>();
        String extensaoArqSaida = propertiesConfig.getExtensaoArqSaida();
        String dirSaida = formatarDiretorio(propertiesConfig.getDirSaida());
        String generatedName = System.currentTimeMillis() + extensaoArqSaida;

        return new FlatFileItemWriterBuilder<Object>()
                .name("flatFileItemWriter")
                .resource(new FileSystemResource(dirSaida + generatedName))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }

    @Bean
    public FlatFileItemReader flatFileItemReader() throws Exception {
        return new FlatFileItemReaderBuilder<>()
                .name("flatFileItemReader")
                .lineMapper(lineMapper())
                .build();
    }

    private LineMapper lineMapper() throws Exception {
        PatternMatchingCompositeLineMapper mapper = new PatternMatchingCompositeLineMapper();
        mapper.setTokenizers(definirPadraoDasLinhas());
        mapper.setFieldSetMappers(converterLinhasEmObj());

        return mapper;
    }

    private Map<String, FieldSetMapper> converterLinhasEmObj() throws Exception {
        Map<String, FieldSetMapper> map = new HashMap<>();
        map.put("001*", new ClienteFieldSetMapper());
        map.put("002*", new VendedorFieldSetMapper());
        map.put("003*", new VendasFieldSetMapper());

        return map;
    }

    private Map<String, LineTokenizer> definirPadraoDasLinhas() {
        Map<String, LineTokenizer> lineTokenizerMap = new HashMap<>(3);

        lineTokenizerMap.put("001*", clienteRecordTokenizer());
        lineTokenizerMap.put("002*", vendedorRecordTokenizer());
        lineTokenizerMap.put("003*", vendasRecordTokenizer());

        return lineTokenizerMap;
    }

    private DelimitedLineTokenizer clienteRecordTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("ç");
        lineTokenizer.setNames("id", "cpf", "nome", "salario");

        return lineTokenizer;
    }

    private DelimitedLineTokenizer vendedorRecordTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("ç");
        lineTokenizer.setNames("id", "cnpj", "business", "area");

        return lineTokenizer;
    }

    private DelimitedLineTokenizer vendasRecordTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("ç");
        lineTokenizer.setNames("id", "codigoVenda", "nomeVendedor", "produtos");

        return lineTokenizer;
    }

    private String formatarDiretorio(String dir) {
        return dir.replace("\\", "/");
    }
}
