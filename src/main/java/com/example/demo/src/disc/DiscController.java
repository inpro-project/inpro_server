package com.example.demo.src.disc;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.disc.model.GetDiscTestRes;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@ApiResponses({
        @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
        @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다.")
})
@RestController
@AllArgsConstructor
@RequestMapping("/app")
public class DiscController {

   private final DiscProvider discProvider;

    /**
     * 업무 성향 테스트지 조회 API
     * [GET] /app/discs
     * @return BaseResponse<List<GetDiscTestRes>>
     */
    @ApiOperation(value = "업무 성향 테스트지 조회 API")
    @ResponseBody
    @GetMapping("/discs")
    public BaseResponse<List<GetDiscTestRes>> getDiscTest(){
        try {
            List<GetDiscTestRes> getDiscTestRes = discProvider.getDiscTest();
            return new BaseResponse<>(getDiscTestRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
