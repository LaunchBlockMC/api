package gg.launchblock.api.models.lifecycles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LifecycleStateLogModel {

    private LifecycleState state;
    private Instant timestamp;

}
