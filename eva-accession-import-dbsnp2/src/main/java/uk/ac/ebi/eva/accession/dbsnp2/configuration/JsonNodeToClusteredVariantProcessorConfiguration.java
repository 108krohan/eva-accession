package uk.ac.ebi.eva.accession.dbsnp2.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.eva.accession.core.ClusteredVariant;
import uk.ac.ebi.eva.accession.core.contig.ContigMapping;
import uk.ac.ebi.eva.accession.core.io.FastaSynonymSequenceReader;
import uk.ac.ebi.eva.accession.dbsnp2.io.AssemblyCheckerProcessor;
import uk.ac.ebi.eva.accession.dbsnp2.io.JsonNodeToClusteredVariantProcessor;
import uk.ac.ebi.eva.accession.dbsnp2.parameters.InputParameters;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static uk.ac.ebi.eva.accession.dbsnp2.configuration.BeanNames.DBSNP_JSON_VARIANT_PROCESSOR;

/**
 * Configuration to convert a dbSNP JSON line to a clustered variant object,
 * and check if variant maps to the genome.
 */
@Configuration
public class JsonNodeToClusteredVariantProcessorConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(JsonNodeToClusteredVariantProcessor.class);

    @Bean(name = DBSNP_JSON_VARIANT_PROCESSOR)
    @StepScope
    public ItemProcessor<JsonNode, ClusteredVariant> dbsnpJsonVariantProcessor(
            InputParameters parameters,
            JsonNodeToClusteredVariantProcessor jsonNodeToClusteredVariantProcessor,
            AssemblyCheckerProcessor assemblyCheckerProcessor) {
        logger.info("Injecting dbsnpJsonVariantProcessor with parameters: {}", parameters);
        CompositeItemProcessor<JsonNode, ClusteredVariant> compositeProcessor =
            new CompositeItemProcessor<>();
        compositeProcessor.setDelegates(Arrays.asList(jsonNodeToClusteredVariantProcessor,
                                                      assemblyCheckerProcessor));
        return compositeProcessor;
    }

    @Bean
    JsonNodeToClusteredVariantProcessor jsonNodeToClusteredVariantProcessor() {
        return new JsonNodeToClusteredVariantProcessor();
    }

    @Bean
    AssemblyCheckerProcessor assemblyCheckerProcessor(FastaSynonymSequenceReader fastaSynonymSequenceReader) {
        return new AssemblyCheckerProcessor(fastaSynonymSequenceReader);
    }

    @Bean
    FastaSynonymSequenceReader fastaSynonymSequenceReader(ContigMapping contigMapping, InputParameters parameters)
            throws IOException {
        Path referenceFastaFile = Paths.get(parameters.getFasta());
        return new FastaSynonymSequenceReader(contigMapping, referenceFastaFile);
    }

    @Bean
    ContigMapping contigMapping(InputParameters parameters) throws Exception {
        return new ContigMapping(parameters.getAssemblyReportUrl());
    }
}