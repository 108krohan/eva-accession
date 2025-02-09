/*
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.accession.release.io;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;

/**
 * Writes the list of contigs to a flat file
 */
public class ContigWriter implements ItemStreamWriter<String> {

    private static final String FILE_EXTENSION = ".txt";

    private static final String ACTIVE_FILE_PREFIX = "/active_contigs_";

    private static final String MERGED_FILE_PREFIX = "/merged_contigs_";

    private final File output;

    private PrintWriter printWriter;

    public ContigWriter(File output) {
        this.output = output;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            printWriter = new PrintWriter(new FileWriter(this.output));
        } catch (IOException e) {
            throw new ItemStreamException(e);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {
        printWriter.close();
    }

    @Override
    public void write(List<? extends String> contigs) {
        for (String contig : contigs) {
            printWriter.println(contig);
        }
    }

    public static String getActiveContigsFilePath(String referenceAssembly) {
        return ACTIVE_FILE_PREFIX + referenceAssembly + FILE_EXTENSION;
    }

    public static String getActiveContigsFilePath(File outputFolder, String referenceAssembly) {
        return outputFolder + getActiveContigsFilePath(referenceAssembly);
    }

    public static String getActiveContigsFilePath(String outputFolder, String referenceAssembly) {
        return Paths.get(outputFolder) + getActiveContigsFilePath(referenceAssembly);
    }

    public static String getMergedContigsFilePath(String referenceAssembly) {
        return MERGED_FILE_PREFIX + referenceAssembly + FILE_EXTENSION;
    }

    public static String getMergedContigsFilePath(File outputFolder, String referenceAssembly) {
        return outputFolder + getMergedContigsFilePath(referenceAssembly);
    }

    public static String getMergedContigsFilePath(String outputFolder, String referenceAssembly) {
        return Paths.get(outputFolder) + getMergedContigsFilePath(referenceAssembly);
    }
}
