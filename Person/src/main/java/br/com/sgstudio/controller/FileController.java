package br.com.sgstudio.controller;

import br.com.sgstudio.data.vo.UploadFileRespondeVO;
import br.com.sgstudio.services.FileStorageServices;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"FileEndpoint"})
@RestController
@RequestMapping("/api/file/v1")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageServices fileStorageServices;

    @PostMapping("/uploadFile")
    public UploadFileRespondeVO uploadFile(@RequestParam("file") MultipartFile file)
    {
        String fileName = fileStorageServices.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/v1/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileRespondeVO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultiFiles")
    public List<UploadFileRespondeVO> uploadMultiFiles(@RequestParam("files") MultipartFile[] files)
    {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName, HttpServletRequest request)
    {
        Resource resource = fileStorageServices.loadFileAsResource(fileName);

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }
        catch(Exception e)
        {
            logger.info("Could not find the file " + fileName);
        }

        if (contentType == null)
        {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
