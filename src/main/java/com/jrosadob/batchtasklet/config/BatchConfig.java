package com.jrosadob.batchtasklet.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import com.jrosadob.batchtasklet.services.interfaces.PersonService;
import com.jrosadob.batchtasklet.steps.DecompressStep;
import com.jrosadob.batchtasklet.steps.ProcessorStep;
import com.jrosadob.batchtasklet.steps.ReaderStep;
import com.jrosadob.batchtasklet.steps.WriterStep;


@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private final ResourceLoader resourceLoader;
    private final PersonService personService;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    public BatchConfig(final ResourceLoader resourceLoader,
                       final PersonService personService,
                       final JobBuilderFactory jobBuilderFactory,
                       final StepBuilderFactory stepBuilderFactory) {
        super();
        this.resourceLoader = resourceLoader;
        this.personService = personService;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }
    //#region Steps
    @Bean
    @JobScope
    public DecompressStep decompressStep() {
        return new DecompressStep(this.resourceLoader);
    }
    @Bean
    @JobScope
    public ReaderStep readerStep() {
        return new ReaderStep(this.resourceLoader);
    }
    @Bean
    @JobScope
    public ProcessorStep processorStep() {
        return new ProcessorStep();
    }
    @Bean
    @JobScope
    public WriterStep writerStep() {
        return new WriterStep(this.personService);
    }
    //#endregion

    //#region Build tasklet Steps
    @Bean
    public Step decompressStepBuild(){
        return stepBuilderFactory.get("DecompressStep")
                .tasklet(decompressStep())
                .build();
    }

    @Bean
    public Step readerStepBuild() {
        return stepBuilderFactory.get("ReaderStep")
                .tasklet(readerStep())
                .build();
    }

    @Bean
    public Step processorStepBuild(){
        return stepBuilderFactory.get("ProcessorStep")
                .tasklet(processorStep())
                .build();
    }

    @Bean
    public Step writerStepBuild() {
        return stepBuilderFactory.get("WriterStep")
                .tasklet(writerStep())
                .build();
    }
    //#endregion

    //#region Job
    @Bean
    public Job batchTaskletJob() {
        return jobBuilderFactory.get("batchTaskletJob")
                .start(decompressStepBuild())
                .next(readerStepBuild())
                .next(processorStepBuild())
                .next(writerStepBuild())
                .build();
    }
    //#endregion
}
