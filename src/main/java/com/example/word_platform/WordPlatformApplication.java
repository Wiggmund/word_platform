package com.example.word_platform;

import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.repository.UserRepo;
import com.example.word_platform.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WordPlatformApplication {

  public static void main(String[] args) {
    SpringApplication.run(WordPlatformApplication.class, args);
  }

  @Bean
  public CommandLineRunner run(
      UserService userService,
      UserRepo userRepo
  ) {
    return args -> {
      userRepo.deleteAll();
      userRepo.flush();
//      userService.createUser(new UserCreateDto("Sem", "sem@gmail.com"));
    };
  }
}
