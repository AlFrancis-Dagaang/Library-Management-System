package test;

import com.app.dao.BookTransactionDAO;
import com.app.model.BookTransaction;
import com.app.util.DBConnection;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class IssueBookDAOTest {
    @Test
    public void getIssueTransactoin(){
        DBConnection db = new DBConnection();
        BookTransactionDAO dao = new BookTransactionDAO(db);
        BookTransaction transaction = null;


        try(Connection con =  db.getConnection()){
            transaction = dao.getIssueTransactionById(1);

        }catch (SQLException e){
            e.printStackTrace();
        }

        Assert.assertNotNull(transaction);
    }

}
