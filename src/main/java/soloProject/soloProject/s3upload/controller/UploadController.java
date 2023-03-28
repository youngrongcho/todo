package soloProject.soloProject.s3upload.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import soloProject.soloProject.s3upload.service.AwsS3Service;
import soloProject.soloProject.s3upload.util.CommonUtils;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UploadController {
    private final AwsS3Service awsS3Service;

    @PostMapping("/upload")
    public List<String> uploadFile(@RequestPart(value = "file") MultipartFile[] multipartFile) {
        // resourcePath DB에 저장
        return awsS3Service.uploadFileV1(multipartFile);
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("resourcePath") String resourcePath) {
        byte[] data = awsS3Service.downloadFileV1(resourcePath);
        ByteArrayResource resource = new ByteArrayResource(data);
        HttpHeaders headers = buildHeaders(resourcePath, data);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);
    }

    private HttpHeaders buildHeaders(String resourcePath, byte[] data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(data.length);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(CommonUtils.createContentDisposition(resourcePath));
        return headers;
    }
}
