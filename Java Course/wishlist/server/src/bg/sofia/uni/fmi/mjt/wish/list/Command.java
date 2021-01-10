package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.Serializable;

public record Command(String commandName) implements Serializable {
    private static final long serialVersionUID = 1;

    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    private static final String POST_WISH = "post-wish";

    public boolean requiresUser() { //typo ime, da, znam; shte go opravq
        return commandName.equals(REGISTER) || commandName.equals(LOGIN) || commandName.equals(POST_WISH);
    }
}
