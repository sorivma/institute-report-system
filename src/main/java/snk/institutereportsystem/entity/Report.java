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

    private long reportId;

    private long userId;
    /**
     * Поле темы отчёта
     */
    private long themeId;
    /**
     * Поле текста отчёта
     */
    private String text;
    /**
     * Поле статуса отчёта
     */
    private Status status;

    @Override
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", userId=" + userId +
                ", themeId=" + themeId +
                ", text='" + text + '\'' +
                ", status=" + status +
                '}';
    }
}
