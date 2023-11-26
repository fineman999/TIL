package io.chan.springcoresecurity.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role_hierarchy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"roleHierarchy", "parentName"})
public class RoleHierarchy {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "child_name")
    private String childName;

    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_name", referencedColumnName = "child_name")
    private RoleHierarchy parentName;

    @OneToMany(mappedBy = "parentName", cascade={CascadeType.ALL})
    private Set<RoleHierarchy> roleHierarchy = new HashSet<>();
    @Builder
    public RoleHierarchy(Long id, String childName, RoleHierarchy parentName) {
        this.id = id;
        this.childName = childName;
        this.parentName = parentName;
    }

    public void setParentName(RoleHierarchy parentRoleHierarchy) {
        this.parentName = parentRoleHierarchy;
    }
}
