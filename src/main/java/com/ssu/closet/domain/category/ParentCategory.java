package com.ssu.closet.domain.category;

import com.ssu.closet.domain.item.Item;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParentCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 500)
    private String name;

    @OneToMany(mappedBy = "parentCategory", cascade={CascadeType.ALL})
    private List <Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "parentCategory", cascade={CascadeType.ALL})
    private List <ChildCategory> childCategories = new ArrayList<>();
}

