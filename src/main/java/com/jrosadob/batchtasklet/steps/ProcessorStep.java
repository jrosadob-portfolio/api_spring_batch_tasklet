package com.jrosadob.batchtasklet.steps;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.jrosadob.batchtasklet.entities.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessorStep implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("> Start ProcessorStep ");

        List<Person> personList;
        var tmpPersonList =  chunkContext.getStepContext()
                                                    .getStepExecution()
                                                    .getJobExecution()
                                                    .getExecutionContext()
                                                    .get("personList");

        personList = convertToPersonList(tmpPersonList);

        List<Person> toPersonList = personList.stream().map(person -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            person.setInsertionDate(formatter.format(LocalDateTime.now()));
            return person;
        }).collect(Collectors.toList());

        chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext()
                    .put("personList", toPersonList);

        log.info("> End ProcessorStep");
        return null;
    }

    public List<Person> convertToPersonList(Object objectPerson) {
        List<Person> personList = new ArrayList<>();

        if (objectPerson instanceof List<?>) {
            List<?> tmpList = (List<?>) objectPerson;
            for (Object o : tmpList) {
                if (o instanceof Person) {
                    personList.add((Person) o);
                } else {
                    throw new ClassCastException("Not all items in list are of type Person");
                }
            }
        } else {
            throw new ClassCastException("tmpPersonList is not of type List<Person>");
        }

        return personList;
    }
    
}
