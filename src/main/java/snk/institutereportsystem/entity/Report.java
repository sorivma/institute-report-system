package snk.institutereportsystem.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс отчёта со свойствами <b>theme</b> <b>text</b> <b>status</b>
 */
@Getter
@Setter
@AllArgsConstructor
public class Report {
    /**
     * Поле темы отчёта
     */
    private Theme theme;
    /**
     * Поле текста отчёта
     */
    private String text;
    /**
     * Поле статуса отчёта
     */
    private Status status;
}
