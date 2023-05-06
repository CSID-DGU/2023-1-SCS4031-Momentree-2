package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.controller.requestDto.RecordRequestDto;
import com.DateBuzz.Backend.controller.responseDto.RecordResponseDto;
import com.DateBuzz.Backend.controller.responseDto.Response;
import com.DateBuzz.Backend.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/community")
    private Response<Page<RecordResponseDto>> list(@PageableDefault(size=3) Pageable pageable){
        return Response.success(recordService.getList(pageable));
    }

    @PostMapping("/community")
    private Response<Void> records(@RequestBody RecordRequestDto requestDto, Authentication authentication){
        recordService.writes(requestDto, authentication.getName());
        return Response.success();
    }

    @GetMapping("/community/{recordId}")
    private Response<RecordResponseDto> getDetail(@PathVariable Long recordId){
        return Response.success(recordService.getrecord(recordId));
    }
}
