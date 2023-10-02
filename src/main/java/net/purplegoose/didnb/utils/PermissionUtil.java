package net.purplegoose.didnb.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class PermissionUtil {

    private PermissionUtil() {
        // static use only
    }

    private static boolean isMemberAdmin(Member member) {
        return member.hasPermission(Permission.ADMINISTRATOR);
    }

    private static boolean isMemberOwner(Member member) {
        return member.isOwner();
    }

    private static boolean doMemberHasDefaultAdminRole(List<Role> roles) {
        Role tempRole = roles.stream()
                .filter(role -> role.getName().equalsIgnoreCase("Bot Admin"))
                .findFirst()
                .orElse(null);
        return tempRole != null;
    }

    private static boolean doMemberHasCustomAdminRole(List<Role> roles, String guildAdminRoleID) {
        Role tempRole = roles.stream()
                .filter(role -> role.getId().equals(guildAdminRoleID))
                .findFirst()
                .orElse(null);
        return tempRole != null;
    }

    public static boolean isUserPermitted(Member member, String guildAdminRoleID) {
        if (isMemberOwner(member) || isMemberAdmin(member)) {
            return true;
        }

        List<Role> roles = member.getRoles();
        if (guildAdminRoleID == null) {
            return doMemberHasDefaultAdminRole(roles);
        }

        return doMemberHasCustomAdminRole(roles, guildAdminRoleID);
    }
}
