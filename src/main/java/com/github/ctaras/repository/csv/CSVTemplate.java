package com.github.ctaras.repository.csv;

import com.github.ctaras.repository.DataStruct;
import com.github.ctaras.util.Log;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

abstract class CSVTemplate {
    protected final CSVConfig config;
    final DataStruct dt;
    final ReadWriteLock lock;

    CSVTemplate(CSVConfig config, DataStruct dt) {
        this.config = config;
        this.dt = dt;

        lock = new ReentrantReadWriteLock();
    }

    boolean appendData(String tablePath, String row, Logger logger, String message) {
        boolean done = true;
        lock.writeLock().lock();
        try {
            String line = config.lineSeparator + row;
            Files.write(Paths.get(tablePath), line.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            Log.e(logger, () -> message, e);
            done = false;
        } finally {
            lock.writeLock().unlock();
        }

        return done;
    }

    boolean updateData(Path filePath, String id, String row, Logger logger, String message) {
        boolean done = true;

        List<String> fileContent;

        lock.writeLock().lock();

        try {
            fileContent = new ArrayList<>(Files.readAllLines(filePath, StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).startsWith(id)) {
                    fileContent.set(i, row);
                    break;
                }
            }

            Files.write(filePath, fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.e(logger, () -> message, e);
            done = false;
        } finally {
            lock.writeLock().unlock();
        }

        return done;
    }

    boolean deleteData(Path filePath, String id, Logger logger, String message) {
        boolean done = true;

        List<String> fileContent;

        lock.writeLock().lock();

        try {
            fileContent = new ArrayList<>(Files.readAllLines(filePath, StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).startsWith(id)) {
                    fileContent.remove(i);
                    break;
                }
            }

            Files.write(filePath, fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.e(logger, () -> message, e);
            done = false;
        } finally {
            lock.writeLock().unlock();
        }

        return done;
    }
}
