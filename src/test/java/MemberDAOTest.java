import com.app.dao.MemberDAO;
import com.app.model.Member;
import com.app.util.DBConnection;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class MemberDAOTest {

     @Test
     public void testCreatedMember(){
          DBConnection db = new DBConnection();
          MemberDAO dao = new MemberDAO(db);
          Date date = new Date();
          Member member = new Member("Naruto", "Konoha", 608208888, "Student" );
          member = dao.addMember(member);

          Assert.assertNotNull(member);
     }
}
