package soloProject.soloProject.s3upload.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import soloProject.soloProject.s3upload.util.CommonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public List<String> uploadFileV1(MultipartFile[] multipartFiles) {
        validateFileExists(multipartFiles);

        List<String> response = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles) {
            String fileName = CommonUtils.buildFileName(multipartFile.getOriginalFilename());

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());

            try (InputStream inputStream = multipartFile.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new RuntimeException("업로드 실패");
            }

            String string = amazonS3Client.getUrl(bucketName, fileName).toString();
            response.add(string);
        }

        return response;
    }

    private void validateFileExists(MultipartFile[] multipartFile) {
        for(MultipartFile f : multipartFile)
        if (f.isEmpty()) {
            throw new RuntimeException("파일 없음");
        }
    }

    public byte[] downloadFileV1(String resourcePath) {
        validateFileExistsAtUrl(resourcePath);

        S3Object s3Object = amazonS3Client.getObject(bucketName, resourcePath);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("다운 받기 에러");
        }
    }

    private void validateFileExistsAtUrl(String resourcePath) {
        if (!amazonS3Client.doesObjectExist(bucketName, resourcePath)) {
            throw new RuntimeException("경로 없음");
        }
    }
}