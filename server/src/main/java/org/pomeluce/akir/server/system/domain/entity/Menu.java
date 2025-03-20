package org.pomeluce.akir.server.system.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.pomeluce.akir.common.core.domain.BaseEntity;

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
    private @Id long menuId;
    private String code;
    private String label;
    private String show;
    private String disabled;
    private long parentId;
    private String target;

    public Menu() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return menuId == menu.menuId && parentId == menu.parentId && Objects.equals(code, menu.code) && Objects.equals(label, menu.label) && Objects.equals(show, menu.show) && Objects.equals(disabled, menu.disabled) && Objects.equals(target, menu.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, code, label, show, disabled, parentId, target);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuId=" + menuId +
                ", code='" + code + '\'' +
                ", label='" + label + '\'' +
                ", show='" + show + '\'' +
                ", disabled='" + disabled + '\'' +
                ", parentId=" + parentId +
                ", target='" + target + '\'' +
                "} " + super.toString();
    }
}
