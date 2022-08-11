package ru.yandex.practicum.filmorate;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;


/** Функция автоматической настройки не всегда отвечает задачам приложения.
 * В некоторых случаях, например при тестировании, может понадобиться отключить
 * автоконфигурацию подключения к базе данных.
 * Чтобы объяснить Spring Boot, что его помощь не требуется,
 * добавьте в аннотацию @SpringBootApplication параметр exclude со значением DataSourceAutoConfiguration.class. */
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)

/** Для реализации альтернативного способа объявим тестовый класс ManualJdbcConnectService
 *   и отметим его аннотацией @Component: */
@Component
public class ManualJdbcConnectService {

    /** Внутри этого класса необходимо описать КОНСТАНТЫ с адресом и параметрами подключения. Например, вот так: */
    public static final String JDBC_URL="jdbc:mysql://cat.world:3306/allcats";
    public static final String JDBC_USERNAME="iamacat";
    public static final String JDBC_PASSWORD="iamapet";
    public static final String JDBC_DRIVER="org.mysql.jdbc.Driver";

    /** Затем нужно вызвать метод, например JdbcTemplate getTemplate().
      * А внутри него создать объект DataSource — точнее, его Spring-имплементацию.: */
    public JdbcTemplate getTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(JDBC_DRIVER);
        dataSource.setUrl(JDBC_URL);
        dataSource.setUsername(JDBC_USERNAME);
        dataSource.setPassword(JDBC_PASSWORD);
/** В конце необходимо подключить новый JdbcTemplate к источнику данных. */
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

}
