package com.jrosadob.batchtasklet.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1")
public class BatchController {
    private final JobLauncher jobLauncher;
    private final Job job;
    public BatchController(final JobLauncher jobLauncher, final Job job) {
        super();
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam(name = "file") MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        try{
            Path path = Paths.get("src" + File.separator + "main" + File.separator + "resources" + File.separator + "files" + File.separator + fileName);

            Files.createDirectories(path.getParent());
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            //File archivo = path.toAbsolutePath().toFile();

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("nombre", fileName)
                    .addDate("fecha", new Date())
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);

            Map<String, String> response = new HashMap<>();
            response.put("archivo", fileName);

            return ResponseEntity.ok(response);

        }catch(Exception exception){
            log.error("Error al iniciar el proceso batch. Error {}", exception.getMessage());
            throw new RuntimeException();
        }
    }
}

