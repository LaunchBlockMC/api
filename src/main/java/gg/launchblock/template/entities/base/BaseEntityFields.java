package gg.launchblock.template.entities.base;

import gg.launchblock.template.user.base.LaunchBlockActor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;

import java.time.Instant;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BaseEntityFields {

    private LaunchBlockActor createdByClient;
    private Instant createdTimestamp;
    private LaunchBlockActor mostRecentUpdateClient;
    private Instant updateTimestamp;

    public BaseEntityFields(final LaunchBlockActor user) {
        this.createdByClient = user;
        this.createdTimestamp = Instant.now();
        this.mostRecentUpdateClient = user;
        this.updateTimestamp = Instant.now();
    }

    @Contract(mutates = "this")
    public BaseEntityFields update(final LaunchBlockActor user) {
        this.updateTimestamp = Instant.now();
        this.mostRecentUpdateClient = user;
        return this;
    }

}