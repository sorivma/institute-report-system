package snk.institutereportsystem.entity;

/**
 * enum класс статуса отчёта
 */
public enum Status {
    /**
     * Работа в процессе
     */
    InWORK,
    /**
     * Отчёт не проверен
     */
    UNCHECKED,
    /**
     * Отчёт отправлен на доработку
     */
    DECLINED,
    /**
     * Отчёт утверждён
     */
    APPROVED,
}
