```java
package gg.launchblock.template.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ExampleMapper {

    WorkspaceDetailsModel toWorkspaceModel(WorkspaceEntity workspaceEntity);

}
```