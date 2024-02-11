package ru.skypro.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс, представляющий точку входа в приложение.
 * Использует аннотацию @SpringBootApplication для автоматической конфигурации и запуска приложения.
 */
@SpringBootApplication
public class HomeworkApplication {

  /**
   * Метод main, который запускает приложение.
   *
   * @param args Аргументы командной строки.
   */
  public static void main(String[] args) {
    SpringApplication.run(HomeworkApplication.class, args);
  }
}
