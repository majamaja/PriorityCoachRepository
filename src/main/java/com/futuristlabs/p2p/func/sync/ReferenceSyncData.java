package com.futuristlabs.p2p.func.sync;

import com.futuristlabs.p2p.func.lifeupgrade.LifeUpgradeCategory;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;
import java.util.UUID;

public class ReferenceSyncData {

    public UpdatedReferenceSyncData updated = new UpdatedReferenceSyncData();
    public DeletedReferenceSyncData deleted = new DeletedReferenceSyncData();

    @JsonIgnore
    public boolean isEmpty() {
        return updated.isEmpty() && deleted.isEmpty();
    }

    public static abstract class AbstractReferenceSyncData<LIFE_UPGRADE_CATEGORIES> {
        public List<LIFE_UPGRADE_CATEGORIES> lifeUpgradeCategories;

        public boolean isEmpty() {
            return (lifeUpgradeCategories == null || lifeUpgradeCategories.isEmpty());
        }
    }

    public static class UpdatedReferenceSyncData extends AbstractReferenceSyncData<LifeUpgradeCategory> {

    }

    public static class DeletedReferenceSyncData extends AbstractReferenceSyncData<UUID> {

    }

}
