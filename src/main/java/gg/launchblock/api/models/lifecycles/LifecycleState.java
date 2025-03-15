package gg.launchblock.api.models.lifecycles;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LifecycleState {
    QUEUE(LifecycleStage.QUEUE),
    BUILDING(LifecycleStage.BUILD),
    BUILD_FAILED(LifecycleStage.FAILED),
    DEPLOYING(LifecycleStage.DEPLOY),
    DEPLOY_FAILED(LifecycleStage.FAILED),
    DEPLOYED(LifecycleStage.DEPLOY),
    ROLLED_BACK(LifecycleStage.REMOVED),
    REPLACED(LifecycleStage.REMOVED);

    private final LifecycleStage stage;

}
