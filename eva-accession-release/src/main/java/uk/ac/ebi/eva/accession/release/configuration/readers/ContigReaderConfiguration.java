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
package uk.ac.ebi.eva.accession.release.configuration.readers;

import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.ac.ebi.eva.accession.core.configuration.DbsnpDataSource;
import uk.ac.ebi.eva.accession.release.io.ContigMongoReader;
import uk.ac.ebi.eva.accession.release.parameters.InputParameters;

import static uk.ac.ebi.eva.accession.release.configuration.BeanNames.ACTIVE_CONTIG_READER;
import static uk.ac.ebi.eva.accession.release.configuration.BeanNames.MERGED_CONTIG_READER;

@Configuration
@EnableConfigurationProperties({DbsnpDataSource.class})
public class ContigReaderConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ContigReaderConfiguration.class);

    @Bean(name = ACTIVE_CONTIG_READER)
    @StepScope
    ItemStreamReader<String> activeContigReader(InputParameters parameters, MongoClient mongoClient,
                                          MongoProperties mongoProperties) throws Exception {
        logger.info("Injecting ContigMongoReader (active contigs) parameters: {}, {}",
                    parameters.getAssemblyAccession(), mongoProperties.getDatabase());
        return ContigMongoReader.activeContigReader(parameters.getAssemblyAccession(), mongoClient,
                                                    mongoProperties.getDatabase());
    }

    @Bean(name = MERGED_CONTIG_READER)
    @StepScope
    ItemStreamReader<String> mergedContigReader(InputParameters parameters, MongoClient mongoClient,
                                          MongoProperties mongoProperties) throws Exception {
        logger.info("Injecting ContigMongoReader (merged contigs) parameters: {}, {}",
                    parameters.getAssemblyAccession(), mongoProperties.getDatabase());
        return ContigMongoReader.mergedContigReader(parameters.getAssemblyAccession(), mongoClient,
                                                    mongoProperties.getDatabase());
    }
}
