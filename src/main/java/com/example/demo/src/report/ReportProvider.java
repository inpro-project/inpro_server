package com.example.demo.src.report;

import com.example.demo.config.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportProvider {

    private final ReportDao reportDao;

    public int checkUserIdx(int userIdx) throws BaseException {
        try {
            return reportDao.checkUserIdx(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPreBlock(int userIdx, int blockedUserIdx) throws BaseException {
        try {
            return reportDao.checkPreBlock(userIdx, blockedUserIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkBlockHist(int userIdx, int blockedUserIdx) throws BaseException {
        try {
            return reportDao.checkBlockHist(userIdx, blockedUserIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
