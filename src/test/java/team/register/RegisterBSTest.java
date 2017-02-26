package team.register;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import team.login.LoginBS;
import team.model.User;
import team.model.enums.UserType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RegisterBSTest {
    @Before
    public void setUp() {
        try {
            Files.deleteIfExists(Paths.get("./database.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RegisterBS.createUserTable();
    }

    @Test
    public void testNullValuesFail() throws Exception {
        /*Test no Username*/
        try {
            User user= RegisterBS.addUser(null, "pass", UserType.USER, "u@gmail.com");
            Assert.assertNull(user);
            user = RegisterBS.addUser("user", null, UserType.USER, "u@gmail.com");
            Assert.assertNull(user);
            user = RegisterBS.addUser("user", "pass", null, "u@gmail.com");
            Assert.assertNull(user);
        } catch (Exception e) {
        }


    }
    @Test
    public void testAddUser() throws Exception {
        /*Test add One user*/
        User user = new User("user","pass",UserType.USER,"u@gmail.com");
        user = RegisterBS.addUser(user.getUsername(), user.getPassword(),
                user.getUserType(), user.getEmail());
        Assert.assertEquals(user.getUsername(), "user");
        Assert.assertEquals(user.getPassword(), "pass");
        Assert.assertEquals(user.getUserType(), UserType.USER);
        Assert.assertEquals(user.getEmail(), "u@gmail.com");

        RegisterBS.addUser(user.getUsername(), user.getPassword(),
                user.getUserType(), user.getEmail());


        /*Test if user can login*/
        user = LoginBS.login("user", "pass");
        if (user == null) {
            Assert.fail();
        }
    }

    @Test
    public void testAddUsers() throws Exception {
        /*Test add multiple users*/
        for (int i = 0; i < 10; i++) {
            User user = new User("user" + i, "pass", UserType.USER, "u@gmail.com");
            user = RegisterBS.addUser(user.getUsername(), user.getPassword(),
                    user.getUserType(), user.getEmail());
            Assert.assertEquals(user.getUsername(), "user" + i);
            Assert.assertEquals(user.getPassword(), "pass");
            Assert.assertEquals(user.getUserType(), UserType.USER);
            Assert.assertEquals(user.getEmail(), "u@gmail.com");


        /*Test if users can login*/
            user = LoginBS.login("user" + i, "pass");
            if (user  == null) {
                Assert.fail();
            }
        }
    }

}