package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.controller.requestDto.modify.ModifyRecordRequestDto;
import com.DateBuzz.Backend.controller.requestDto.RecordRequestDto;
import com.DateBuzz.Backend.controller.responseDto.RecordResponseDto;
import com.DateBuzz.Backend.controller.responseDto.Response;
import com.DateBuzz.Backend.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class RecordController {

    private final RecordService recordService;

    @GetMapping("")
    private Response<Page<RecordResponseDto>> list( Pageable pageable){
        return Response.success(recordService.getList(pageable));
    }

    @GetMapping("/login")
    private Response<Page<RecordResponseDto>> listLogin( Pageable pageable, Authentication authentication){
        return Response.success(recordService.getListLogin(pageable, authentication.getName()));
    }

    @PostMapping("")
    private Response<Void> records(@RequestBody RecordRequestDto requestDto, Authentication authentication){
        recordService.writes(requestDto, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{recordId}")
    private Response<RecordResponseDto> getDetail(@PathVariable Long recordId){
        return Response.success(recordService.getrecord(recordId));
    }

    @GetMapping("/login/{recordId}")
    private Response<RecordResponseDto> getDetailLogin(@PathVariable Long recordId, Authentication authentication){
        return Response.success(recordService.getrecordLogin(recordId, authentication.getName()));
    }

    @DeleteMapping("/{recordId}")
    private Response<Long> delete(@PathVariable Long recordId, Authentication authentication){
        return Response.success(recordService.deleteArticle(recordId, authentication.getName()));
    }
}
