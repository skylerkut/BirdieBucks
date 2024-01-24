package com.example.as1.Controllers.RecycleViews;

public class GroupScrollCard {

    String groupName;
    int numMembers;

    public GroupScrollCard(String groupName, int numMembers) {
        this.groupName = groupName;
        this.numMembers = numMembers;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(int numMembers) {
        this.numMembers = numMembers;
    }


}
