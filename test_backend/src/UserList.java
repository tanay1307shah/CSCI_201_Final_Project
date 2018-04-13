import java.util.ArrayList;
import java.util.List;

public class UserList {
    private List<User> userList;
    private static UserList userListInstance = new UserList();

    public static UserList getUserListInstance() {
        return userListInstance;
    }

    UserList(){
        userList = new ArrayList<>();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void addUserToDB(User newUser){
        userList.add(newUser);
    }
}
