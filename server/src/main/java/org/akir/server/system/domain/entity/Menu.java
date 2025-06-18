package org.akir.server.system.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.akir.common.core.domain.BaseEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "akir_menu")
public class Menu extends BaseEntity implements Serializable {
    private static final @Serial long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long menuId;
    private String code;
    private String label;
    private Long sort;
    private Boolean show;
    private Boolean disabled;
    private Long parentId;
    private String target;

    public Menu() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;
        return Objects.equals(menuId, menu.menuId) && Objects.equals(code, menu.code) && Objects.equals(label, menu.label) && Objects.equals(sort, menu.sort) && Objects.equals(show, menu.show) && Objects.equals(disabled, menu.disabled) && Objects.equals(parentId, menu.parentId) && Objects.equals(target, menu.target);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(menuId);
        result = 31 * result + Objects.hashCode(code);
        result = 31 * result + Objects.hashCode(label);
        result = 31 * result + Objects.hashCode(sort);
        result = 31 * result + Objects.hashCode(show);
        result = 31 * result + Objects.hashCode(disabled);
        result = 31 * result + Objects.hashCode(parentId);
        result = 31 * result + Objects.hashCode(target);
        return result;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuId=" + menuId +
                ", code='" + code + '\'' +
                ", label='" + label + '\'' +
                ", sort=" + sort +
                ", show=" + show +
                ", disabled=" + disabled +
                ", parentId=" + parentId +
                ", target='" + target + '\'' +
                "} " + super.toString();
    }
}
