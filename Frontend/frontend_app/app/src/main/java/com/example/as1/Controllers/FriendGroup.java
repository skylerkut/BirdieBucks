package com.example.as1.Controllers;

import java.util.ArrayList;
import java.util.List;

public class FriendGroup {
        private int id;
        private String groupName;

        private List<User> groupMembers;
        private long groupLeaderID;


        public String getGroupName(){
            return groupName;
        }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroupMembers(List<User> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public long getGroupLeaderID() {
        return groupLeaderID;
    }

    public void setGroupLeaderID(long groupLeaderID) {
        this.groupLeaderID = groupLeaderID;
    }

        public List<User> getGroupMembers() {
           return groupMembers;
        }

        public void setGroupName(String groupName){
            this.groupName = groupName;
        }

        public void addUser(User user){
            groupMembers.add(user);
        }

        public void removeUser(User user){
            groupMembers.remove(user);
        }

    }

