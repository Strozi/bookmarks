package michu.spring.tutorials.model;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>{

	//SELECT b from Bookmark b WHERE b.account.username = :username
	Collection<Bookmark> findByAccountUsername(String username);
	
}
