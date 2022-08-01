package com.example.user.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CatalogueResponse {

    List<ContentResponse> ownedContent = new ArrayList<>();

}
