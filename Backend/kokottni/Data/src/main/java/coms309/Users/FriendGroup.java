package coms309.Users;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name = "friendgroup")
public class FriendGroup {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String groupName;

    @Column(name = "groupLeaderID")
    private long groupLeaderID;



    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "friendGroup" )
    private List<User> groupMembers = new ArrayList<>();


    public FriendGroup(UUID id, String groupName, long groupLeaderID){
        this.id = id;
        this.groupName = groupName;
        this.groupLeaderID = groupLeaderID;
    }

    public FriendGroup() {
        this.groupName = "defaultGroup";
        this.groupLeaderID = 0;
    }

    public FriendGroup(String groupName, long groupLeaderID){
        this.groupName = groupName;
        this.groupLeaderID = groupLeaderID;
    }

    public String getGroupName(){
        return groupName;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public UUID getId(){
        return id;
    }
    public List<User> getGroupMembers() {
//        List<String> groupMembersNames = new ArrayList<>();
//        for(User user : groupMembers){
//            groupMembersNames.add(user.getName());
//        }
//
//        return groupMembersNames;
        return groupMembers;
    }


    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public void setGroupLeader(User user){this.groupLeaderID = user.getId();}

    public long getGroupLeader(){return groupLeaderID;}

    public void addUser(User user){
        groupMembers.add(user);
    }

    public void removeUser(User user){
        groupMembers.remove(user);
    }

    //TODO
    //getAllMessages(){
    //return message log from repo
    //}

    public boolean findUser(User user){return groupMembers.contains(user);}

}