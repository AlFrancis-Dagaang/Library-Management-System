import com.app.dao.MemberDAO;
import com.app.dto.BookDTO;
import com.app.model.Book;
import com.app.model.Member;
import com.app.util.DBConnection;
import com.app.util.MapperUtil;
import org.junit.Assert;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.Date;

public class MemberDAOTest {

     @Test
     public void testBook(){
          Book book = new Book("Rick Riordan", "Percy Jackson", 2012, false, "General Book");

          ModelMapper mapper = MapperUtil.getModelMapper();
          BookDTO dto =mapper.map(book, BookDTO.class);

          Assert.assertEquals(book.getYearOfPublication(), dto.getYearOfPublication());
     }
}
