package com.example.user.controller;

import com.example.user.dto.CreateContentRequest;
import com.example.user.entity.Content;
import com.example.user.exception.ResourceAlreadyExistException;
import com.example.user.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;

    @PreAuthorize("hasAuthority('CONTENT_CREATOR')")
    @PostMapping()
    public ResponseEntity<String> createContent(@Valid @RequestBody CreateContentRequest contentRequest) throws ResourceAlreadyExistException {
        return contentService.createContent(contentRequest);
    }

    //viewAllContents
    //view content by content creator
    //edit

    @GetMapping("")
    public ResponseEntity<Content> getContent(@RequestParam(name = "title") String title) throws ResourceAlreadyExistException {
        return contentService.getContent(title);
    }
}
