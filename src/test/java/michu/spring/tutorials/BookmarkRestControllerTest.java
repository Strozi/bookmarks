package michu.spring.tutorials;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.activemq.transport.tcp.ExceededMaximumConnectionsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import michu.spring.tutorials.model.Account;
import michu.spring.tutorials.model.AccountRepository;
import michu.spring.tutorials.model.Bookmark;
import michu.spring.tutorials.model.BookmarkRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTutorialBuildRestApplication.class)
@WebAppConfiguration
public class BookmarkRestControllerTest {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	

	private MockMvc mockMvc;
	
	private String userName = "bdussault";
	
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	private Account account;
	
	private List<Bookmark> bookmarkList = new ArrayList<>();
	
	@Autowired
	private BookmarkRepository bookmarkRepository;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired 
	void setConverters(HttpMessageConverter<?>[] converters){
		
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny()
				.orElse(null);
		
		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}
	
	@Before
	public void setup() throws Exception{
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		this.bookmarkRepository.deleteAllInBatch();
		this.accountRepository.deleteAllInBatch();
		
		this.account = accountRepository.save(new Account(userName, "password"));
		this.bookmarkList.add(bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/1/" + userName, "A description")));
		this.bookmarkList.add(bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/2/" + userName, "A description")));
	
	}
	
	
	@Test
	public void userNotFound() throws Exception{
		
		mockMvc.perform(post("/george/bookmarks")
				.content(this.json(new Bookmark()))
				.contentType(contentType))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void readSingleBookmark() throws Exception{
		
		mockMvc.perform(get("/" + userName + "/bookmarks/" + this.bookmarkList.get(0).getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is(this.bookmarkList.get(0).getId().intValue())))
				.andExpect(jsonPath("$.uri", is("http://bookmark.com/1/" + userName)))
				.andExpect(jsonPath("$.description", is("A description")));
		
	}
	
	
	
	
	protected String json(Object o ) throws IOException{		
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
