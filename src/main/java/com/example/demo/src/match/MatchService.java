package com.example.demo.src.match;

import com.example.demo.config.BaseException;
import com.example.demo.src.match.model.PostUserLikeRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.FAIL_USERLIKE;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MatchService {

    private final MatchDao matchDao;

    public PostUserLikeRes updateUserLike(int userLikeIdx) throws BaseException {
        try {
            int result = matchDao.updateUserLike(userLikeIdx);
            if(result == 0){
                throw new BaseException(FAIL_USERLIKE);
            }
            return new PostUserLikeRes(userLikeIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUserLikeRes createUserLike(int likerIdx, int likingIdx) throws BaseException {
        try {
            int userLikeIdx = matchDao.createUserLike(likerIdx, likingIdx);
            return new PostUserLikeRes(userLikeIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
