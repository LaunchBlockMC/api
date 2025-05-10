package gg.launchblock.api.user.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ActorType {
    USER("user:"),
    APPLICATION("application:"),
    CONTAINER("container:"),
    API_TOKEN("api_token:");

    private final String prefix;
}
