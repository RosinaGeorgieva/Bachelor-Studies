package bg.sofia.uni.fmi.mjt.wish.list.auth.server;

import bg.sofia.uni.fmi.mjt.wish.list.server.Server;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WishListAuthenticationServerTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String INVALID_USERNAME_MSG = "[ Username Ko$io is invalid, select a valid one ]" + LINE_SEPARATOR;
    private static final String USERNAME_ALREADY_TAKEN_MSG = "[ Username Gabi is already taken, select another one ]" +LINE_SEPARATOR;
    private static final String SUCCESSFULLY_REGISTERED_MSG = "[ Username Zdravko successfully registered ]" + LINE_SEPARATOR;
    private static final String SUCCESSFUL_LOGIN_MSG = "[ User Ivan successfully logged in ]" + LINE_SEPARATOR;
    private static final String ALREADY_LOGGED_IN_MSG = "[ You are already logged in]" + LINE_SEPARATOR;
    private static final String UNSUCCESSFUL_LOGIN_MSG = "[ Invalid username/password combination ]" + LINE_SEPARATOR;
    private static final String SUCCESSFUL_LOGOUT_MSG = "[ Successfully logged out ]" + LINE_SEPARATOR;
    private static final String NOT_LOGGED_IN_MSG = "[ You are not logged in ]" + LINE_SEPARATOR;
    private static final String NO_SUCH_REGISTERED_MSG = "[ Student with username dancho is not registered ]" + LINE_SEPARATOR;
    private static final String TOO_FEW_ARGUMENTS = "[ Too few arguments provided. ]" + LINE_SEPARATOR;
    private static final String UNKNOWN_COMMAND_MSG = "[ Unknown command ]" + LINE_SEPARATOR;
    private static final String OK = "-*&OK&*-" + LINE_SEPARATOR;
    private static final int SERVER_PORT = 7778;

    private static final String REGISTER_VALID_USER_1 = "3 register Zdravko Abcd1234";
    private static final String REGISTER_VALID_USER_2 = "3 register Gabi jhg3";
    private static final String REGISTER_VALID_USER_3 = "3 register Ivan 12";
    private static final String REGISTER_VALID_USER_4 = "3 register Desi 32";
    private static final String REGISTER_VALID_USER_5 = "3 register Hristo 56";
    private static final String REGISTER_VALID_USER_6 = "3 register Kaloyan parola5";
    private static final String REGISTER_VALID_USER_7 = "3 register 100yo parola6";
    private static final String REGISTER_VALID_USER_8 = "3 register ludi.jabi jaba1";
    private static final String REGISTER_VALID_USER_9 = "3 register rosi 12";
    private static final String REGISTER_VALID_USER_10 = "3 register Vanya 14554sss";

    private static final String REGISTER_WITH_EXISTING_USERNAME = "3 register Gabi 15";
    private static final String REGISTER_WITH_INVALID_USERNAME = "3 register Ko$io Abcd1234";

    private static final String LOGIN_VALID_USER = "3 login Ivan 12";
    private static final String LOGIN_INVALID_COMBINATION = "3 login Desi nevalidnaparola";
    private static final String LOGIN_AFTER_REGISTER = "3 login Hristo 56";
    private static final String LOGIN_ALREADY_LOGGED_IN = "3 login Kaloyan parola5";

    private static final String LOGOUT = "3 logout";

    private static final String VALID_WISH_RETRIEVAL = "3 get-wish";
    private static final String WISH_SUBMITTION = "3 post-wish rosi diploma";
    private static final String WISH_SUBMITTION_WITH_UNEXISTING_USER = "3 post-wish dancho kuche";

    private static final String NOT_ENOUGH_ARGUMENTS_COMMAND = "3 login rosi";
    private static final String UNKNOWN_COMMAND = "3 command";

    private static final Server server = new WishListAuthenticationServer(SERVER_PORT);

    @Test
    public void testProcessRequestRegisterValidUser() {
        assertEquals(SUCCESSFULLY_REGISTERED_MSG, server.processRequest(REGISTER_VALID_USER_1));
        server.processRequest(LOGOUT);
    }

    @Test
    public void testProcessRequestRegisterExistingUsername() {
        server.processRequest(REGISTER_VALID_USER_2);

        assertEquals(USERNAME_ALREADY_TAKEN_MSG, server.processRequest(REGISTER_WITH_EXISTING_USERNAME));
    }

    @Test
    public void testProcessRequestRegisterInvalidUsername() {
        assertEquals(INVALID_USERNAME_MSG, server.processRequest(REGISTER_WITH_INVALID_USERNAME));
    }

    @Test
    public void testProcessRequestLoginValidUser() {
        server.processRequest(REGISTER_VALID_USER_3);
        server.processRequest(LOGOUT);

        assertEquals(SUCCESSFUL_LOGIN_MSG, server.processRequest(LOGIN_VALID_USER));
        server.processRequest(LOGOUT);
    }

    @Test
    public void testProcessRequestLoginInvalidCombination() {
        server.processRequest(REGISTER_VALID_USER_4);
        server.processRequest(LOGOUT);

        assertEquals(UNSUCCESSFUL_LOGIN_MSG, server.processRequest(LOGIN_INVALID_COMBINATION));
    }

    @Test
    public void testProcessRequestLoginAfterRegister() {
        server.processRequest(REGISTER_VALID_USER_5);

        assertEquals(ALREADY_LOGGED_IN_MSG, server.processRequest(LOGIN_AFTER_REGISTER));
    }

    @Test
    public void testProcessRequestLoginAlreadyLoggedIn() {
        server.processRequest(REGISTER_VALID_USER_6);

        assertEquals(ALREADY_LOGGED_IN_MSG, server.processRequest(LOGIN_ALREADY_LOGGED_IN));
        server.processRequest(LOGOUT);
    }
//po skoro da napravq metoda v server-a da se kazva respondTo
    @Test
    public void testProcessRequestLogoutValid() {
        server.processRequest(REGISTER_VALID_USER_7);

        assertEquals(SUCCESSFUL_LOGOUT_MSG, server.processRequest(LOGOUT));
    }

    @Test
    public void testProcessRequestLogoutNotLoggedIn() {
        server.processRequest(LOGOUT);
        assertEquals(NOT_LOGGED_IN_MSG, server.processRequest(LOGOUT));
    }

    @Test
    public void testProcessRequestAllowWishListRetrievalValid() {
        server.processRequest(REGISTER_VALID_USER_8);

        assertEquals(OK, server.processRequest(VALID_WISH_RETRIEVAL));
        server.processRequest(LOGOUT);
    }

    @Test
    public void testProcessRequestAllowWishListRetrievalInvalid() {
        assertEquals(NOT_LOGGED_IN_MSG, server.processRequest(VALID_WISH_RETRIEVAL));
    }

    @Test
    public void testProcessRequestWithNotEnoughArguments() {
        assertEquals(TOO_FEW_ARGUMENTS, server.processRequest(NOT_ENOUGH_ARGUMENTS_COMMAND));
    }

    @Test
    public void testProcessRequestAllowWishListSubmittionValid() {
        server.processRequest(REGISTER_VALID_USER_9);

        assertEquals(OK, server.processRequest(WISH_SUBMITTION));
        server.processRequest(LOGOUT);
    }

    @Test
    public void testProcessRequestAllowWishListSubmittionNotLoggedIn() {
        assertEquals(NOT_LOGGED_IN_MSG, server.processRequest(WISH_SUBMITTION));
    }

    @Test
    public void testProcessRequestAllowWishLishlistSubmittionNoSuchUser() {
        server.processRequest(REGISTER_VALID_USER_10);

        assertEquals(NO_SUCH_REGISTERED_MSG, server.processRequest(WISH_SUBMITTION_WITH_UNEXISTING_USER));
        server.processRequest(LOGOUT);
    }

    @Test
    public void testProcessRequestUnknownCommand() {
        assertEquals(UNKNOWN_COMMAND_MSG, server.processRequest(UNKNOWN_COMMAND));
    }
}
