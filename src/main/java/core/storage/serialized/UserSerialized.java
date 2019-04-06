package core.storage.serialized;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;

public class UserSerialized {

    private final String userId;

    public UserSerialized(User user) {
        this.userId = user.getId();
    }

    public User get(JDA jda) {
        return jda.getUserById(this.userId);
    }

}
