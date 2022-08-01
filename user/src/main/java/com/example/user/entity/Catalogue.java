package com.example.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Catalogue extends Base{
//    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL)
    @OneToMany
    @ToString.Exclude
    private List<Content> purchasedContent = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    private User user;
}
