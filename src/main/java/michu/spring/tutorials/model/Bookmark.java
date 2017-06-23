package michu.spring.tutorials.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@RequiredArgsConstructor
public class Bookmark {
	
	@JsonIgnore
	@ManyToOne
	@NonNull
	private Account account;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NonNull
	public String uri;
	@NonNull
	public String description;
	
	
	

}
