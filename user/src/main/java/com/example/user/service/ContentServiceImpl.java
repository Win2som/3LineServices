package com.example.user.service;


import com.example.user.dto.CreateContentRequest;
import com.example.user.entity.Content;
import com.example.user.entity.User;
import com.example.user.exception.ResourceAlreadyExistException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService{

    private final ContentRepository contentRepository;

    @Override
    public ResponseEntity<String> createContent(CreateContentRequest contentRequest) throws ResourceAlreadyExistException {

        //check that the person trying to create content is a content creator
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       Optional<Content> optionalContent = Optional.ofNullable(contentRepository.findByTitle(contentRequest.getTitle()));


       if(optionalContent.isPresent()){
           throw new ResourceAlreadyExistException("A content with this title already exist");
       }
        Content content = Content.builder()
                .title(contentRequest.getTitle())
                .body(contentRequest.getBody())
                .price(contentRequest.getPrice())
                .user(currentUser)
                .build();
       content.setCreatedTime(LocalDateTime.now());
       content.setModifiedTime(LocalDateTime.now());

       contentRepository.save(content);

       return new ResponseEntity<>("Content created", HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Content> getContent(String title) {
        log.info(title);
        log.info(title);
        log.info(title);
        log.info("conttent contenet");
        log.info("conttent contenet");
        Content content = contentRepository.findByTitle(title);
        if(content == null){
            try {
                throw new ResourceNotFoundException("Content by "+title+ " does not exist");
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return new ResponseEntity<>(content, HttpStatus.OK);
    }
}
