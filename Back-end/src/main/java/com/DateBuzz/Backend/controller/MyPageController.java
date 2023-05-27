package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.controller.responseDto.RecordResponseDto;
import com.DateBuzz.Backend.controller.responseDto.Response;
import com.DateBuzz.Backend.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final RecordService recordService;

    @GetMapping("/myPage/myRecord")
    public Response<Page<RecordResponseDto>> getMyRecord(@PageableDefault(size = 5, sort = "recordedID", direction = Sort.Direction.ASC) Pageable pageable, Authentication authentication){
        Page<RecordResponseDto> myRecord = recordService.getMyRecord(pageable, authentication.getName());
        return Response.success(myRecord);
    }

    @GetMapping("/myPage/myBookmark")
    public Response<Page<RecordResponseDto>> getMyBookmarkRecord(@PageableDefault(size = 5, sort = "recordedID", direction = Sort.Direction.ASC) Pageable pageable, Authentication authentication){
        Page<RecordResponseDto> myRecord = recordService.getMyBookmarkRecord(pageable, authentication.getName());
        return Response.success(myRecord);
    }
}
