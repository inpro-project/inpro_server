package com.example.demo.src.report;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.report.model.PostBlockRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ReportService {

    private final ReportProvider reportProvider;
    private final ReportDao reportDao;

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

}
