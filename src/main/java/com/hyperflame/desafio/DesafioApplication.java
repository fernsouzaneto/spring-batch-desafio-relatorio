package com.hyperflame.desafio;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableBatchProcessing
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableScheduling
public class DesafioApplication {

	@Autowired
	private Job job;

	@Autowired
	private JobLauncher jobLauncher;

	public static void main(String[] args) {
		SpringApplication.run(DesafioApplication.class, args);
	}

	//roda a cada o job a cada 1min
	@Scheduled(cron = "0 */1 * * * ?")
	public void perform() throws Exception
	{
		JobParameters params = new JobParametersBuilder()
				.addString("Gerar-Relatorio-Auto", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(job, params);
	}

}
