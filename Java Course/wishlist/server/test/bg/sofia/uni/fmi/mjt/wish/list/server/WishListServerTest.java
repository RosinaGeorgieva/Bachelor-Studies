package bg.sofia.uni.fmi.mjt.wish.list.server;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WishListServerTest { // moje bi testovete da sa na drugite klasowe; da mockvam tuk?
    private static final int port = 7777;
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SUCCESSFULLY_REGISTERED_MSG = "[ Username rosi successfully registered ]" + LINE_SEPARATOR;

    private static final String REGISTER_VALID_USER_1 = "3 register rosi 12";
    private static final String REGISTER_VALID_USER_2 = "3 register Gabi jhg3";
    private static final String POST_WISH_WITH_VALID_USER_1 = "3 post-wish rosi kniga";
    private static final String POST_WISH_WITH_INVALID_USER = "3 post-wish niki topka";

    private static final String WISH_ALREADY_SUBMITTED_MESSAGE_FORMAT = "[ The same gift for student Gabi was already submitted ]" + LINE_SEPARATOR;
    private static final String WISH_SUCCESSFULLY_SUBMITTED_MESSAGE = "[ Gift kniga for student rosi submitted successfully ]" + LINE_SEPARATOR;
    private static final String LOGOUT = "3 logout" + LINE_SEPARATOR;
    private static final String SUCCESSFUL_LOGOUT_MSG = "[ Successfully logged out ]" + LINE_SEPARATOR;

    private static final String NOT_LOGGED_IN_MSG = "[ You are not logged in ]" + LINE_SEPARATOR;
    private static final String NO_SUCH_REGISTERED_MSG = "[ Student with username niki is not registered ]" + LINE_SEPARATOR;
    private static final String NO_STUDENTS_IN_LIST_MESSAGE_FORMAT = "[ There are no students present in the wish list ]" + LINE_SEPARATOR;

    private static final String REGISTER_VALID_USER_3 = "3 register dANI jhg3";
    private static final String POST_WISH_WITH_ANOTHER_VALID_USER = "3 post-wish Gabi kote";

    private static final String REGISTER_VALID_USER_4 = "3 register Ivan 12";
    private static final String REGISTER_VALID_USER_5 = "3 register Desi 32";
    private static final String LOGIN_USER_4 = "3 login Ivan 12";
    private static final String POST_WISH_FOR_USER_4 = "3 post-wish Ivan kuche";
    private static final String POST_ANOTHER_WISH_FOR_USER_4 = "3 post-wish Ivan papagal";
    private static final String WISH_LIST_OF_USER_4 = "[ Ivan: [kuche, papagal] ]" + LINE_SEPARATOR;
    private static final String VALID_GET_WISH = "3 get-wish";

    private static Server server = new WishListServer(port);

    @BeforeClass
    public static void setUp() throws InterruptedException {
        ServerThread serverThread = new ServerThread(server);
        serverThread.start();
        Thread.sleep(500);
        server.processRequest(REGISTER_VALID_USER_4);
        server.processRequest(LOGOUT);
    }

    @After
    public void cleanUp() {
        server.processRequest(LOGIN_USER_4);
        String response;
        do {
            response = server.processRequest(VALID_GET_WISH);
        } while (!response.equals(NO_STUDENTS_IN_LIST_MESSAGE_FORMAT));
        server.processRequest(LOGOUT);
    }

    @Test
    public void testProcessRequestPostWishValid() {
        assertEquals(SUCCESSFULLY_REGISTERED_MSG, server.processRequest(REGISTER_VALID_USER_1));
        assertEquals(WISH_SUCCESSFULLY_SUBMITTED_MESSAGE, server.processRequest(POST_WISH_WITH_VALID_USER_1));
        assertEquals(SUCCESSFUL_LOGOUT_MSG, server.processRequest(LOGOUT));
    }

    @Test
    public void testProcessRequestPostWishNotLoggedIn() {
        assertEquals(NOT_LOGGED_IN_MSG, server.processRequest(POST_WISH_WITH_VALID_USER_1));
    }

    @Test
    public void testProcessRequestPostWishUnregisteredUser() {
        server.processRequest(REGISTER_VALID_USER_2);
        assertEquals(NO_SUCH_REGISTERED_MSG, server.processRequest(POST_WISH_WITH_INVALID_USER)); //0
    }

    @Test
    public void testProcessRequestPostAlreadyExistingWish() {
        server.processRequest(REGISTER_VALID_USER_3);
        server.processRequest(POST_WISH_WITH_ANOTHER_VALID_USER);//da mu promenq imeto na tozi post
        assertEquals(WISH_ALREADY_SUBMITTED_MESSAGE_FORMAT, server.processRequest(POST_WISH_WITH_ANOTHER_VALID_USER));
    }

    @Test
    public void testProcessRequestGetWish() {
        server.processRequest(REGISTER_VALID_USER_5);

        server.processRequest(POST_WISH_FOR_USER_4);
        server.processRequest(POST_ANOTHER_WISH_FOR_USER_4);

        assertEquals(WISH_LIST_OF_USER_4, server.processRequest(VALID_GET_WISH));
    }
}
