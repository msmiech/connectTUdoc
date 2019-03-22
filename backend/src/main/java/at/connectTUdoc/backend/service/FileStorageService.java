package at.connectTUdoc.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Based on https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/
 */
public interface FileStorageService {

    /**
     * Stores the multipart file to the system storage
     *
     * @param file - the multipartfile  to store
     * @return the path to the file
     */
    String storeFile(MultipartFile file);

    /**
     * Loads a file from the storage
     *
     * @param fileName - the path of the filename
     * @return the resource of the file
     */
    Resource loadFileAsResource(String fileName);
}
