package com.example.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Catalogue{
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Content> purchasedContent = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    private User user;
}
