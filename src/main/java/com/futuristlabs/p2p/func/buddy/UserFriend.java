package com.futuristlabs.p2p.func.buddy;

import com.futuristlabs.p2p.func.lifeupgrade.LifeUpgradeAction;
import com.futuristlabs.p2p.func.useractions.UserActionsLog;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserFriend {

    private UUID id;
    private UUID friendId;
    private String friendName;
    private String friendEmail;
    private String friendPhone;
    private List<LifeUpgradeAction> lifeUpgradeActions;
    private List<UserActionsLog> friendActionsLog;

    public UserFriendInvitationStatus getStatus() {
        if (friendId != null) {
            return UserFriendInvitationStatus.ACCEPTED;
        }

        if ((friendEmail != null && !friendEmail.isEmpty()) || (friendPhone != null && !friendPhone.isEmpty())) {
            return UserFriendInvitationStatus.INVITED;
        }

        return UserFriendInvitationStatus.INVALID;
    }
}
