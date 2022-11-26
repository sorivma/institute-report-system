package snk.institutereportsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Класс пользователя со свойствами <b>id</b> <b>name</b> <b>surname</b> <b>patronymic</b> <b>password</b> <b>role</b>
 */
@Getter
@Setter
@AllArgsConstructor

public class User{
    /**
     * id пользователя
     */
    private Long id;
    /**
     * Имя пользователя
     */
    @NonNull
    private String name;
    /**
     * Фамилия пользователя
     */
    @NonNull
    private String surname;
    /**
     * Отчество пользователя
     */
    @NonNull
    private String patronymic;
    /**
     * Пароль пользователя
     */
    @NonNull
    private String password;
    /**
     * Должность пользователя
     */
    @NonNull
    private Role role;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
