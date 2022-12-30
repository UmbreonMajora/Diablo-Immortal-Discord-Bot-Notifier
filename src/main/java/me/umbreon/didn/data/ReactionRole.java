package me.umbreon.didn.data;

public class ReactionRole {

    private String messageID;
    private String guildID;
    private String reactionID;
    private String reactionType;
    private String roleID;

    public ReactionRole(String messageID, String guildID, String reactionID, String reactionType, String roleID) {
        this.messageID = messageID;
        this.guildID = guildID;
        this.reactionID = reactionID;
        this.reactionType = reactionType;
        this.roleID = roleID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getGuildID() {
        return guildID;
    }

    public void setGuildID(String guildID) {
        this.guildID = guildID;
    }

    public String getReactionID() {
        return reactionID;
    }

    public void setReactionID(String reactionID) {
        this.reactionID = reactionID;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }
}

