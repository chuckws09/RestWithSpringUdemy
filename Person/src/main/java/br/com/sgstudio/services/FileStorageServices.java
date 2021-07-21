package br.com.sgstudio.services;

import br.com.sgstudio.config.FileStorageConfig;
import br.com.sgstudio.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServices {

    private final Path fileStorageLocation;

    public FileStorageServices(FileStorageConfig config) {
        this.fileStorageLocation = Paths.get(config.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStorageLocation);
        }
        catch (Exception e)
        {
            throw new FileStorageException("Could not create the directory!", e);
        }
    }

    public String storeFile(MultipartFile file)
    {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains(".."))
            {
                throw new FileStorageException("Sorry! Your file contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e)
        {
            throw new FileStorageException("Could not store file " + fileName, e);
        }
    }

    public Resource loadFileAsResource(String fileName)
    {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists())
            {
                return resource;
            }
            else
            {
                throw new FileStorageException("Could not find file " + fileName);
            }
        }
        catch (Exception e)
        {
            throw new FileStorageException("Could not find file " + fileName, e);
        }
    }

}
