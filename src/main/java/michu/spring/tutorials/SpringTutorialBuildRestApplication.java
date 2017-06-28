package michu.spring.tutorials;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import michu.spring.tutorials.model.Account;
import michu.spring.tutorials.model.AccountRepository;
import michu.spring.tutorials.model.Bookmark;
import michu.spring.tutorials.model.BookmarkRepository;

// a good place to stick of odds and ends, like @Bean definitions
@SpringBootApplication
public class SpringTutorialBuildRestApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringTutorialBuildRestApplication.class, args);		
	}
	
	
	//execute some piece of code exactly before the application startup completes
	// this interface provides access to application arguments as string array
	@Bean
	CommandLineRunner init(AccountRepository accountRepository, BookmarkRepository bookmarkRepository){
		
		return (evt) -> Arrays.asList(
				"jhoeller,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","))
				.forEach(
							a -> {
							
								Account account = accountRepository.save(new Account(a, "password"));
								bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/1/" + a, "A description"));
								bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/2/" + a, "A description"));
							});		
	}
	

}
