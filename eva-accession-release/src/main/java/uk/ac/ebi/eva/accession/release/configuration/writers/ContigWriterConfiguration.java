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
package uk.ac.ebi.eva.accession.release.configuration.writers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.ac.ebi.eva.accession.release.io.ContigWriter;
import uk.ac.ebi.eva.accession.release.parameters.InputParameters;

import java.io.File;

import static uk.ac.ebi.eva.accession.release.configuration.BeanNames.ACTIVE_CONTIG_WRITER;
import static uk.ac.ebi.eva.accession.release.configuration.BeanNames.MERGED_CONTIG_WRITER;
import static uk.ac.ebi.eva.accession.release.io.ContigWriter.getActiveContigsFilePath;
import static uk.ac.ebi.eva.accession.release.io.ContigWriter.getMergedContigsFilePath;

@Configuration
public class ContigWriterConfiguration {

    @Bean(ACTIVE_CONTIG_WRITER)
    public ContigWriter activeContigWriter(InputParameters inputParameters) {
        return new ContigWriter(new File(getActiveContigsFilePath(inputParameters.getOutputFolder(),
                                                                  inputParameters.getAssemblyAccession())));
    }

    @Bean(MERGED_CONTIG_WRITER)
    public ContigWriter mergedContigWriter(InputParameters inputParameters) {
        return new ContigWriter(new File(getMergedContigsFilePath(inputParameters.getOutputFolder(),
                                                                  inputParameters.getAssemblyAccession())));
    }
}
