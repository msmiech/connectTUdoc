package at.connectTUdoc.backend.service.impl;

import at.connectTUdoc.backend.exception.FileStorageException;
import at.connectTUdoc.backend.exception.MyFileNotFoundException;
import at.connectTUdoc.backend.property.FileStorageProperties;
import at.connectTUdoc.backend.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


/**
 * Based on https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Konnte das Verzeichnis nicht anlegen wo die Dateien gespeichert werden.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        // Normalize file name
        var path = file.getOriginalFilename();
        if (path == null) {
            return null;
        }
        String fileName = StringUtils.cleanPath(path);

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Entschuldigung! Der Dateiname enthält ungültige zeichen " + fileName);
            }


            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            //String folderPath = file.getResource().getFile().getParentFile().getName();
            if (!Files.exists(targetLocation)) {
                Files.createDirectories(targetLocation);
            }
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Konnte die Datei \"" + fileName + "\" nicht speichern. Bitte versuchen sie es später wieder!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("Datei nicht gefunden: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("Datei nicht gefunden: " + fileName, ex);
        }
    }
}
