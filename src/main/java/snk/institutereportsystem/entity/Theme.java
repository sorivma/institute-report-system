package snk.institutereportsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс темы отчёта со свойствами <b>id</b> <b>name</b>
 */
@Getter
@Setter
@AllArgsConstructor
public class Theme {
    /**
     * id отчёта
     */
    private Long id;
    /**
     * название темы
     */
    private String name;

    @Override
    public String toString() {
        return getName();
    }
}
