package com.example.demo.src.disc;

import com.example.demo.config.BaseException;
import com.example.demo.src.disc.model.PostDiscReq;
import com.example.demo.src.disc.model.PostSearchDiscRes;
import com.example.demo.src.disc.model.PostUserDiscRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.Constant.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DiscService {

    private final DiscDao discDao;

    private final double weights [] = {WEIGHT1, WEIGHT2, WEIGHT3};

    // 적합 - 가중치 증가
    public double[] calGoodList(PostDiscReq postDiscReq) {
        double x = 0.0;
        double y = 0.0;

        for(int i = 0; i < postDiscReq.getGoodList().size(); i++) {
            if (postDiscReq.getGoodList().get(i).getName().equals("id")) {
                x += weights[0];
                y += weights[2];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("di")) {
                x += -weights[0];
                y += weights[2];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("cs")) {
                x += -weights[0];
                y += -weights[2];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("sc")) {
                x += weights[0];
                y += -weights[2];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("is")) {
                x += weights[2];
                y += weights[0];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("si")) {
                x += weights[2];
                y += -weights[0];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("dc")) {
                x += -weights[2];
                y += weights[0];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("cd")) {
                x += -weights[2];
                y += -weights[0];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("i")) {
                x += weights[1];
                y += weights[1];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("d")) {
                x += -weights[1];
                y += weights[1];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("c")) {
                x += -weights[1];
                y += -weights[1];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("s")) {
                x += weights[1];
                y += -weights[1];
            }
        }
        double[] xy = {x, y};
        return xy;
    }

    // 부적합 - 가중치 감소
    public double[] calBadList(double[] xy, PostDiscReq postDiscReq) {
        double x = xy[0];
        double y = xy[1];

        for(int j = 0; j < postDiscReq.getBadList().size(); j++) {
            if(postDiscReq.getBadList().get(j).getName().equals("id")){
                x -= weights[0];
                y -= weights[2];
            } else if(postDiscReq.getBadList().get(j).getName().equals("di")){
                x -= -weights[0];
                y -= weights[2];
            } else if(postDiscReq.getBadList().get(j).getName().equals("cs")){
                x -= -weights[0];
                y -= -weights[2];
            } else if(postDiscReq.getBadList().get(j).getName().equals("sc")){
                x -= weights[0];
                y -= -weights[2];
            } else if(postDiscReq.getBadList().get(j).getName().equals("is")){
                x -= weights[2];
                y -= weights[0];
            } else if(postDiscReq.getBadList().get(j).getName().equals("si")){
                x -= weights[2];
                y -= -weights[0];
            } else if(postDiscReq.getBadList().get(j).getName().equals("dc")){
                x -= -weights[2];
                y -= weights[0];
            } else if(postDiscReq.getBadList().get(j).getName().equals("cd")){
                x -= -weights[2];
                y -= -weights[0];
            } else if(postDiscReq.getBadList().get(j).getName().equals("i")){
                x -= weights[1];
                y -= weights[1];
            } else if(postDiscReq.getBadList().get(j).getName().equals("d")){
                x -= -weights[1];
                y -= weights[1];
            } else if(postDiscReq.getBadList().get(j).getName().equals("c")){
                x -= -weights[1];
                y -= -weights[1];
            } else if(postDiscReq.getBadList().get(j).getName().equals("s")){
                x -= weights[1];
                y -= -weights[1];
            }
        }
        double[] xy2 = {x, y};
        return xy2;
    }

    // user disc 결과 저장 - x, y 좌표
    public PostUserDiscRes createUserDisc(int userIdx, String isRepDisc, PostDiscReq postDiscReq) throws BaseException {
        try {
            // x, y 좌표 계산
            double[] xy = calGoodList(postDiscReq);
            double[] xy2 = calBadList(xy, postDiscReq);

            int userDiscIdx = discDao.createUserDisc(userIdx, xy2[0], xy2[1], isRepDisc);
            createUserDiscTestAsGood(userDiscIdx, postDiscReq);
            createUserDiscTestAsBad(userDiscIdx, postDiscReq);
            return new PostUserDiscRes(userDiscIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // User disc 결과 중 적합에 대한 정보 저장
    public void createUserDiscTestAsGood(int userDiscIdx, PostDiscReq postDiscReq) throws BaseException {
        try {
            for(int i = 0; i < postDiscReq.getGoodList().size(); i++){
                discDao.createUserDiscTestAsGood(userDiscIdx, postDiscReq.getGoodList().get(i).getDiscFeatureIdx());
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // User disc 결과 중 부적합에 대한 정보 저장
    public void createUserDiscTestAsBad(int userDiscIdx, PostDiscReq postDiscReq) throws BaseException {
        try {
            for(int i = 0; i < postDiscReq.getBadList().size(); i++){
                discDao.createUserDiscTestAsBad(userDiscIdx, postDiscReq.getBadList().get(i).getDiscFeatureIdx());
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // Search disc 결과 저장 - x, y 좌표
    public PostSearchDiscRes createSearchDisc(int userIdx, String isRepDisc, PostDiscReq postDiscReq) throws BaseException {
        try {
            // x, y 좌표 계산
            double[] xy = calGoodList(postDiscReq);
            double[] xy2 = calBadList(xy, postDiscReq);

            int searchDiscIdx = discDao.createSearchDisc(userIdx, xy2[0], xy2[1], isRepDisc);
            createSearchDiscTestAsGood(searchDiscIdx, postDiscReq);
            createSearchDiscTestAsBad(searchDiscIdx, postDiscReq);
            return new PostSearchDiscRes(searchDiscIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // Search disc 결과 중 적합에 대한 정보 저장
    public void createSearchDiscTestAsGood(int searchDiscIdx, PostDiscReq postDiscReq) throws BaseException {
        try {
            for(int i = 0; i < postDiscReq.getGoodList().size(); i++){
                discDao.createSearchDiscTestAsGood(searchDiscIdx, postDiscReq.getGoodList().get(i).getDiscFeatureIdx());
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // Search disc 결과 중 부적합에 대한 정보 저장
    public void createSearchDiscTestAsBad(int searchDiscIdx, PostDiscReq postDiscReq) throws BaseException {
        try {
            for(int i = 0; i < postDiscReq.getBadList().size(); i++){
                discDao.createSearchDiscTestAsBad(searchDiscIdx, postDiscReq.getBadList().get(i).getDiscFeatureIdx());
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
