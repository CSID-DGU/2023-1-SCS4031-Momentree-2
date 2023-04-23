package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.controller.requestDto.RecordRequestDto;
import com.DateBuzz.Backend.controller.responseDto.RecordResponseDto;
import com.DateBuzz.Backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/community")
    private Page<RecordResponseDto> list(Pageable pageable){
        return postService.getList(pageable);
    }

    @PostMapping("/community")
    private void records(@RequestBody RecordRequestDto requestDto){
        postService.writes(requestDto);
    }
}
