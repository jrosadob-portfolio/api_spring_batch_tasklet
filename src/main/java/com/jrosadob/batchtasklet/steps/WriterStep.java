package com.jrosadob.batchtasklet.steps;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.jrosadob.batchtasklet.entities.Person;
import com.jrosadob.batchtasklet.services.interfaces.PersonService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WriterStep implements Tasklet {
    private final PersonService personService;

    public WriterStep(final PersonService personService) {
        super();
        this.personService = personService;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("> Start WriterStep ");

        List<Person> personList;
        var tmpPersonList = chunkContext.getStepContext()
                                        .getStepExecution()
                                        .getJobExecution()
                                        .getExecutionContext()
                                        .get("personList");
        
        personList = convertToPersonList(tmpPersonList);

        personList.forEach( person -> {
            if(person != null){
                log.info(person.toString());
            }
        });

        personService.saveAll(personList);

        log.info("> End WriterStep");
        return RepeatStatus.FINISHED;
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
