package com.iris.sdmx.sdmxDataModelCodesDownloadRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iris.sdmx.sdmxDataModelCodesDownloadEntity.SdmxFilingDocumentsDownload;

@Repository
public interface SdmxFillingDocumentDownloadRepo extends JpaRepository<SdmxFilingDocumentsDownload, Long> {

}
