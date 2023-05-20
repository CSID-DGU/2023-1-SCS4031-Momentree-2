package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.controller.responseDto.RecordResponseDto;
import com.DateBuzz.Backend.controller.responseDto.Response;
import com.DateBuzz.Backend.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final RecordService recordService;

    @GetMapping("/myPage/myRecord")
    public Response<Page<RecordResponseDto>> getMyRecord(Pageable pageable, Authentication authentication){
        Page<RecordResponseDto> myRecord = recordService.getMyRecord(pageable, authentication.getName());
        return Response.success(myRecord);
    }
}
