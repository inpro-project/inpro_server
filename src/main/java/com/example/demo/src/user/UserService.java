package com.example.demo.src.user;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserProvider userProvider;
    private final UserDao userDao;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public void modifyUser(int userIdx, MultipartFile multipartFile, PatchUserReq patchUserReq) throws BaseException {
        try{
            String userImgUrl = getStoreFileUrl(multipartFile);
            int result = userDao.modifyUser(userIdx, userImgUrl, patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getStoreFileUrl(MultipartFile multipartFile) throws IOException, BaseException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String originalFilename = multipartFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(index + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;
        String key = "profileImg/" + storeFileName;

        amazonS3.putObject(bucket, key, multipartFile.getInputStream(), objectMetadata);
        String storeFileUrl = amazonS3.getUrl(bucket, key).toString();

        return storeFileUrl;
    }

    public PostPortfolioRes createPortfolio(int userIdx, int portfolioCategoryIdx, PostPortfolioReq postPortfolioReq) throws BaseException {
        try {
            int portfolioIdx = userDao.createPortfolio(userIdx, portfolioCategoryIdx, postPortfolioReq);
            return new PostPortfolioRes(portfolioIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updatePortfolio(int userIdx, int portfolioIdx, PatchPortfolioReq patchPortfolioReq) throws BaseException {
        // 포트폴리오 인덱스 유효성 검사
        if(userProvider.checkPortfolioIdx(userIdx, portfolioIdx) != 1){
            throw new BaseException(PORTFOLIO_INVALID_PORTFOLIOIDX);
        }

        try {
            int result = userDao.updatePortfolio(portfolioIdx, patchPortfolioReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_PORTFOLIO);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deletePortfolio(int userIdx, int portfolioIdx) throws BaseException {
        // 포트폴리오 인덱스 유효성 검사
        if(userProvider.checkPortfolioIdx(userIdx, portfolioIdx) != 1){
            throw new BaseException(PORTFOLIO_INVALID_PORTFOLIOIDX);
        }

        try {
            int result = userDao.deletePortfolio(portfolioIdx);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_PORTFOLIO);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUserTagRes createUserTags(int userIdx, String name) throws BaseException {
        try {
            int userTagIdx = userDao.createUserTags(userIdx, name);
            return new PostUserTagRes(userTagIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
