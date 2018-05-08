package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class ListUser {

    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
