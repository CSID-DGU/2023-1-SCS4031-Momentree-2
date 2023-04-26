package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.controller.requestDto.RecordRequestDto;
import com.DateBuzz.Backend.controller.responseDto.RecordResponseDto;
import com.DateBuzz.Backend.controller.responseDto.Response;
import com.DateBuzz.Backend.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/community")
    private Response<Page<RecordResponseDto>> list(Pageable pageable){
        return Response.success(recordService.getList(pageable));
    }

    @PostMapping("/community")
    private Response<Void> records(@RequestBody RecordRequestDto requestDto) throws Exception {
        recordService.writes(requestDto);
        return Response.success();
    }
}
