package com.jrosadob.batchtasklet.steps;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ResourceLoader;

import com.jrosadob.batchtasklet.entities.Person;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReaderStep implements Tasklet {
    private final ResourceLoader resourceLoader;

    public ReaderStep(final ResourceLoader resourceLoader) {
        super();
        this.resourceLoader = resourceLoader;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("> Start ReaderStep ");
        Reader reader = new FileReader(resourceLoader.getResource("file:src/main/resources/target/persons.csv").getFile());
        CSVParser parser = new CSVParserBuilder()
                                .withSeparator(',')
                                .build();
        CSVReader csvReader = new CSVReaderBuilder(reader)
                                .withCSVParser(parser)
                                .withSkipLines(1)   // Ignorar la primera línea como encabezado
                                .build();

        List<Person> personList = new ArrayList<>();
        String[] linea;
        while ((linea = csvReader.readNext()) != null) {
            Person person = new Person();
            person.setPersonName(linea[0]);
            person.setPersonLastName(linea[1]);
            person.setAge(Integer.parseInt(linea[2]));

            personList.add(person);
        }

        csvReader.close();
        reader.close();

        chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext()
                    .put("personList", personList);
        
        log.info("> End ReaderStep");
        return RepeatStatus.FINISHED;
    }
}
