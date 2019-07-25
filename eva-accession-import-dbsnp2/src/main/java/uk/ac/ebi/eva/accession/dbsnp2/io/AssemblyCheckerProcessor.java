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
package uk.ac.ebi.eva.accession.dbsnp2.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import uk.ac.ebi.eva.accession.core.io.FastaSynonymSequenceReader;
import uk.ac.ebi.eva.accession.dbsnp2.model.DbsnpClusteredVariant;

import java.util.HashSet;
import java.util.Set;

/**
 * Processor to check if a clustered variant maps to the genome.
 */
public class AssemblyCheckerProcessor implements ItemProcessor<DbsnpClusteredVariant, DbsnpClusteredVariant> {

    private static Logger logger = LoggerFactory.getLogger(AssemblyCheckerProcessor.class);

    private FastaSynonymSequenceReader fastaReader;

    private Set<String> processedContigs;

    public AssemblyCheckerProcessor(FastaSynonymSequenceReader fastaReader) {
        this.fastaReader = fastaReader;
        this.processedContigs = new HashSet<>();
    }

    @Override
    public DbsnpClusteredVariant process(DbsnpClusteredVariant dbsnpClusteredVariant) {
        String referenceAllele = dbsnpClusteredVariant.getReferenceAllele();
        if (referenceAllele.isEmpty()) {
            dbsnpClusteredVariant.setAssemblyMatch(true);
            return dbsnpClusteredVariant;
        }
        String contig = dbsnpClusteredVariant.getContig();
        long start = dbsnpClusteredVariant.getStart();
        boolean matches = false;
        try {
            long end = start + (referenceAllele.length() - 1);
            String sequence = fastaReader.getSequenceToUpperCase(contig, start, end);
            matches = sequence.equals(referenceAllele.toUpperCase());
        } catch (IllegalArgumentException ex) {
            if (!processedContigs.contains(contig)) {
                processedContigs.add(contig);
                logger.warn(ex.getMessage());
            }
        } finally {
            dbsnpClusteredVariant.setAssemblyMatch(matches);
        }
        return dbsnpClusteredVariant;
    }
}
