package com.example.demo.src.disc;

import com.example.demo.config.BaseException;
import com.example.demo.src.disc.model.GetDiscTestRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiscProvider {

    private final DiscDao discDao;

    public List<GetDiscTestRes> getDiscTest() throws BaseException {
        try {
            List<GetDiscTestRes> getDiscTestRes = discDao.getDiscTest();
            return getDiscTestRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
