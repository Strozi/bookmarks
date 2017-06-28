package michu.spring.tutorials.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>{

	
	//will create JPA query fo the form
	//select a from Account a where a.username = :username
	Optional<Account> findByUsername(String username);
	
	
}
