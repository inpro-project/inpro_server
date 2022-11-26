package com.example.demo.src.disc;

import com.example.demo.config.BaseException;
import com.example.demo.src.disc.model.PostDiscReq;
import com.example.demo.src.disc.model.PostUserDiscRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.Constant.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DiscService {

    private final DiscProvider discProvider;
    private final DiscDao discDao;

    private final double weights [] = {WEIGHT1, WEIGHT2, WEIGHT3};

    private final double percentWeights [] = {PERCENTWEIGHT1, PERCENTWEIGHT2};

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

    // disc 유형별 비중(퍼센트) 계산
    public double[] calDiscPercent(PostDiscReq postDiscReq) throws BaseException {
        double dWeight = 0;
        double iWeight = 0;
        double sWeight = 0;
        double cWeight = 0;

        // 적합인 경우 가중치 조정
        for(int i = 0; i < postDiscReq.getGoodList().size(); i++){
            if (postDiscReq.getGoodList().get(i).getName().equals("id")) {
               iWeight += percentWeights[0];
               dWeight += percentWeights[1];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("di")) {
                dWeight += percentWeights[0];
                iWeight += percentWeights[1];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("cs")) {
                cWeight += percentWeights[0];
                sWeight += percentWeights[1];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("sc")) {
                sWeight += percentWeights[0];
                cWeight += percentWeights[1];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("is")) {
                iWeight += percentWeights[0];
                sWeight += percentWeights[1];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("si")) {
                sWeight += percentWeights[0];
                iWeight += percentWeights[1];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("dc")) {
                dWeight += percentWeights[0];
                cWeight += percentWeights[1];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("cd")) {
                cWeight += percentWeights[0];
                dWeight += percentWeights[1];
            } else if (postDiscReq.getGoodList().get(i).getName().equals("i")) {
                iWeight += 1;
            } else if (postDiscReq.getGoodList().get(i).getName().equals("d")) {
                dWeight += 1;
            } else if (postDiscReq.getGoodList().get(i).getName().equals("c")) {
                cWeight += 1;
            } else if (postDiscReq.getGoodList().get(i).getName().equals("s")) {
                sWeight += 1;
            }
        }

        // 부적합인 경우 가중치 조정
        for(int j = 0; j < postDiscReq.getBadList().size(); j++) {
            if(postDiscReq.getBadList().get(j).getName().equals("id")){
                cWeight += percentWeights[0];
                sWeight += percentWeights[1];
            } else if(postDiscReq.getBadList().get(j).getName().equals("di")){
                sWeight += percentWeights[0];
                cWeight += percentWeights[1];
            } else if(postDiscReq.getBadList().get(j).getName().equals("cs")){
                iWeight += percentWeights[0];
                dWeight += percentWeights[1];
            } else if(postDiscReq.getBadList().get(j).getName().equals("sc")){
                dWeight += percentWeights[0];
                iWeight += percentWeights[1];
            } else if(postDiscReq.getBadList().get(j).getName().equals("is")){
                cWeight += percentWeights[0];
                dWeight += percentWeights[1];
            } else if(postDiscReq.getBadList().get(j).getName().equals("si")){
                dWeight += percentWeights[0];
                cWeight += percentWeights[1];
            } else if(postDiscReq.getBadList().get(j).getName().equals("dc")){
                sWeight += percentWeights[0];
                iWeight += percentWeights[1];
            } else if(postDiscReq.getBadList().get(j).getName().equals("cd")){
                iWeight += percentWeights[0];
                sWeight += percentWeights[1];
            } else if(postDiscReq.getBadList().get(j).getName().equals("i")){
                cWeight += 1;
            } else if(postDiscReq.getBadList().get(j).getName().equals("d")){
                sWeight += 1;
            } else if(postDiscReq.getBadList().get(j).getName().equals("c")){
                iWeight += 1;
            } else if(postDiscReq.getBadList().get(j).getName().equals("s")){
                dWeight += 1;
            }
        }

        // 유형별 비중 계산
        double totalWeight = dWeight + iWeight + sWeight + cWeight;
        double dPercent = (dWeight/totalWeight) * 100;
        double iPercent = (iWeight/totalWeight) * 100;
        double sPercent = (sWeight/totalWeight) * 100;
        double cPercent = (cWeight/totalWeight) * 100;

        double[] discPercent = {dPercent, iPercent, sPercent, cPercent};
        return discPercent;
    }

    // user disc 결과 저장 - x, y 좌표
    public PostUserDiscRes createUserDisc(int userIdx, String isRepDisc, PostDiscReq postDiscReq) throws BaseException {
        try {
            // x, y 좌표 계산
            double[] xy = calGoodList(postDiscReq);
            double[] xy2 = calBadList(xy, postDiscReq);

            // disc 유형별 비중 계산
            double[] discPercent = calDiscPercent(postDiscReq);

            int userDiscIdx = discDao.createUserDisc(userIdx, xy2, isRepDisc, discPercent);
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

    public void updateUserDiscName(int userIdx, int userDiscIdx, String name) throws BaseException {
        // userDiscIdx 유효성 검사
        if(discProvider.checkUserDiscIdx(userIdx, userDiscIdx) == 0){
            throw new BaseException(USERDISC_INVALID_USERDISCIDX);
        }
        try {
            // Query string으로 전달된 이름이 null이면 임의로 순차적인 이름으로 설정
            if(name == null){
                int count = discProvider.getUserDiscCount(userIdx);
                name = "user disc(" + count + ")";
            }

            int result = discDao.updateUserDiscName(userDiscIdx, name);
            if(result == 0){
                throw new BaseException(FAIL_USERDISCNAME);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
