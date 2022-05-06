package com.hyperflame.desafio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "config.batch")
@Configuration
@Data
public class PropertiesConfig {
    private String dirEntrada;
    private String dirSaida;
    private String extensaoArqEntrada;
    private String extensaoArqSaida;
}

