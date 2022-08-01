package com.example.user.service;


import com.example.user.dto.CreateContentRequest;
import com.example.user.entity.Catalogue;
import com.example.user.entity.Content;
import com.example.user.entity.User;
import com.example.user.exception.ResourceAlreadyExistException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.repository.CatalogueRepository;
import com.example.user.repository.ContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ContentServiceImpl implements ContentService{

    private final ContentRepository contentRepository;
    private final CatalogueRepository catalogueRepository;

    public ContentServiceImpl(ContentRepository contentRepository, CatalogueRepository catalogueRepository) {
        this.contentRepository = contentRepository;
        this.catalogueRepository = catalogueRepository;
    }

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

    @Override
    public ResponseEntity<Catalogue> getCatalogue(String title) throws ResourceNotFoundException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Catalogue> catalogue = Optional.ofNullable(catalogueRepository.findByUserId(currentUser.getId()));

        if(catalogue.isEmpty()){
            throw new ResourceNotFoundException("User does not have a catalogue");
        }
        return new ResponseEntity<>(catalogue.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> addCatalogue(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Catalogue> catalogue = Optional.ofNullable(catalogueRepository.findByUserId(currentUser.getId()));


        if(catalogue.isPresent()){
            Optional<Content> content = contentRepository.findById(id);
            content.ifPresent(value -> catalogue.get().getPurchasedContent().add(value));
            catalogueRepository.save(catalogue.get());
        }

        List<Content> purchasedContents = new ArrayList<>();
        Optional<Content> content = contentRepository.findById(id);
        content.ifPresent(purchasedContents::add);
        Catalogue newCatalogue = Catalogue.builder()
                .purchasedContent(purchasedContents)
                .user(currentUser)
                .build();
        catalogueRepository.save(newCatalogue);
        return null;
    }
}
