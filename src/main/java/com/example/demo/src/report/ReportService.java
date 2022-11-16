package com.example.demo.src.report;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.config.BaseException;
import com.example.demo.src.report.model.PostBlockRes;
import com.example.demo.src.report.model.PostReportReq;
import com.example.demo.src.report.model.PostReportRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ReportService {

    private final ReportProvider reportProvider;
    private final ReportDao reportDao;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void updateBlock(int blockIdx) throws BaseException {
        try {
            int result = reportDao.updateBlock(blockIdx);
            if(result == 0){
                throw new BaseException(FAIL_BLOCK);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostBlockRes createBlock(int userIdx, int blockedUserIdx) throws BaseException {
        // blockedUserIdx 유효성 검사
        if(reportProvider.checkUserIdx(blockedUserIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 중복 차단 유효성 검사
        if(reportProvider.checkPreBlock(userIdx, blockedUserIdx) == 1){
            throw new BaseException(BLOCK_INVALID_BLOCKEDUSERIDX);
        }

        try {
            // 이전에 차단을 했던 유저의 경우
            int preBlockIdx = reportProvider.checkBlockHist(userIdx, blockedUserIdx);
            if(preBlockIdx != 0){
                // active 업데이트
                updateBlock(preBlockIdx);
                return new PostBlockRes(preBlockIdx);
            }

            int blockIdx = reportDao.createBlock(userIdx, blockedUserIdx);
            return new PostBlockRes(blockIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteBlock(int userIdx, int blockedUserIdx) throws BaseException {
        // blockedUserIdx 유효성 검사
        if(reportProvider.checkUserIdx(blockedUserIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 차단하지 않은 유저에 대해 차단 취소 요청을 할 경우
        if(reportProvider.checkPreBlock(userIdx, blockedUserIdx) == 0){
            throw new BaseException(UNBLOCK_INVALID_BLOCKEDUSERIDX);
        }

        try {
            int result = reportDao.deleteBlock(userIdx, blockedUserIdx);
            if(result == 0){
                throw new BaseException(FAIL_UNBLOCK);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostReportRes createReport(int userIdx, int reportedUserIdx, PostReportReq postReportReq) throws BaseException {
        // reportedUserIdx 유효성 검사
        if(reportProvider.checkUserIdx(reportedUserIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }
        try {
            int reportIdx = reportDao.createReport(userIdx, reportedUserIdx, postReportReq);
            return new PostReportRes(reportIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createReportImgs(int reportIdx, List<MultipartFile> multipartFile) throws BaseException {
        try {
            for(int i = 0; i < multipartFile.size(); i++){
                String originalFileName = multipartFile.get(i).getOriginalFilename();
                String reportImgUrl = getReportImgUrl(multipartFile.get(i));
                int result = reportDao.createReportImgs(reportIdx, originalFileName, reportImgUrl);
                if(result == 0){
                    throw new BaseException(FAIL_IMG);
                }
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getReportImgUrl(MultipartFile multipartFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String originalFilename = multipartFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(index + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;
        String key = "reportImg/" + storeFileName;

        amazonS3.putObject(bucket, key, multipartFile.getInputStream(), objectMetadata);
        String reportImgUrl = amazonS3.getUrl(bucket, key).toString();

        return reportImgUrl;
    }

    public void createReportFiles(int reportIdx, List<MultipartFile> multipartFile) throws BaseException {
        try {
            for(int i = 0; i < multipartFile.size(); i++){
                String originalFileName = multipartFile.get(i).getOriginalFilename();
                String reportFileUrl = getReportFileUrl(multipartFile.get(i));
                int result = reportDao.createReportFiles(reportIdx, originalFileName, reportFileUrl);
                if(result == 0){
                    throw new BaseException(FAIL_FILE);
                }
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getReportFileUrl(MultipartFile multipartFile) throws IOException, BaseException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String originalFilename = multipartFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(index + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;
        String key = "reportFile/" + storeFileName;

        amazonS3.putObject(bucket, key, multipartFile.getInputStream(), objectMetadata);
        String reportFileUrl = amazonS3.getUrl(bucket, key).toString();

        return reportFileUrl;
    }

}
