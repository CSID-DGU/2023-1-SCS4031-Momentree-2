package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.controller.responseDto.Response;
import com.DateBuzz.Backend.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    @PostMapping("community/{recordId}/bookmark")
    public Response<Void> bookmarkRecord(@PathVariable Long recordId, Authentication authentication){
        bookmarkService.bookmark(recordId, authentication.getName());
        return Response.success();
    }
}
