package coms309.Users;

public class GroupLeader extends User{

    public GroupLeader(Long id, double money, String name, String email, String dob, String username, String password){
        super(id, money, name, email, dob, username, password);
        this.setPrivilege('g');
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (other.getId() == null) {
            return other.getId() == null;
        } else return this.getId().equals(other.getId());
    }
}
