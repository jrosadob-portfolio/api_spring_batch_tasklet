package com.jrosadob.batchtasklet.steps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecompressStep implements Tasklet {
    private final ResourceLoader resourceLoader;

    public DecompressStep(final ResourceLoader resourceLoader) {
        super();
        this.resourceLoader = resourceLoader;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("> Start ProcessorStep ");
        Resource resource = resourceLoader.getResource("file:src/main/resources/zip/persons.zip");
        String filePath = resource.getFile().getAbsolutePath();
        ZipFile zipFile = new ZipFile(filePath);
        File targetDir = new File(resource.getFile().getParent(), "target");
        
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File entryDestination = new File(targetDir, entry.getName());
            if (entry.isDirectory()) {
                entryDestination.mkdirs();
            } else {
                entryDestination.getParentFile().mkdirs();
                InputStream in = zipFile.getInputStream(entry);
                FileOutputStream out = new FileOutputStream(entryDestination);
                byte[] buffer = new byte[1024];
                int len;

                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }

                out.close();
                in.close();
            }
        }
        zipFile.close();
        log.info("> End ProcessorStep");
        return RepeatStatus.FINISHED;
    }
    
}
