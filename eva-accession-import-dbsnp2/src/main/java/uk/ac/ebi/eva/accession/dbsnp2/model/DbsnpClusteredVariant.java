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
package uk.ac.ebi.eva.accession.dbsnp2.model;

import uk.ac.ebi.eva.accession.core.IClusteredVariant;
import uk.ac.ebi.eva.accession.core.persistence.DbsnpClusteredVariantEntity;

public class DbsnpClusteredVariant extends DbsnpClusteredVariantEntity {

    private String referenceAllele;

    private boolean assemblyMatch;

    public DbsnpClusteredVariant(long accession, String hashedMessage,
                                 IClusteredVariant clusteredVariant,
                                 String referenceAllele) {
        super(accession, hashedMessage, clusteredVariant);
        this.referenceAllele = referenceAllele;
    }

    public void setReferenceAllele(String referenceAllele) {
        this.referenceAllele = referenceAllele;
    }

    public String getReferenceAllele() {
        return referenceAllele;
    }

    public void setAssemblyMatch(boolean assemblyMatch) {
        this.assemblyMatch = assemblyMatch;
    }

    public boolean getAssemblyMatch() {
        return assemblyMatch;
    }
}
