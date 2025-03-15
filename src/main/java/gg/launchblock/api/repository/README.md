```java
package gg.launchblock.api.repository;

import gg.launchblock.onboarder.entities.WorkspaceEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkspaceRepository implements ReactivePanacheMongoRepositoryBase<WorkspaceEntity, String> {
}
```