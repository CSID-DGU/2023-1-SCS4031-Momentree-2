package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.controller.requestDto.modify.ModifyRecordRequestDto;
import com.DateBuzz.Backend.controller.responseDto.Response;
import com.DateBuzz.Backend.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class RecordModifyController {

    private RecordService recordService;

    @PatchMapping("/{record_id}")
    public Response<Void> modifyRecord(@PathVariable Long record_id, ModifyRecordRequestDto requestDto, Authentication authentication){
        recordService.modifyRecord(record_id, requestDto, authentication.getName());
        return Response.success();
    }
}
