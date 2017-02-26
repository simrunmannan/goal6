package team.login;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import team.model.User;
import team.model.enums.UserType;
import team.register.RegisterBS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoginBSTest {
    @Before
    public void setUp() {
        try {
            Files.deleteIfExists(Paths.get("./database.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RegisterBS.createUserTable();
        RegisterBS.addUser("user1", "pass1", UserType.USER, "email1");
    }

    @Test
    public void testLogin() throws Exception {
        /*Test empty*/
        Assert.assertNull(LoginBS.login("",""));
        Assert.assertNull(LoginBS.login(null,null));

        /*Test mismatch*/
        Assert.assertNull(LoginBS.login("user1", "pass2"));

        /*Test success*/
        User user = LoginBS.login("user1", "pass1");
        if (user == null) {
            Assert.fail();
        }
        Assert.assertEquals(user.getUsername(), "user1");
        Assert.assertEquals(user.getPassword(), "pass1");
        Assert.assertEquals(user.getUserType(), UserType.USER);
        Assert.assertEquals(user.getEmail(), "email1");
    }

}