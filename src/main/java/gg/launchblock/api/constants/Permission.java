package gg.launchblock.api.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {

    // meta - not shown in public list of applicable permissions
    CONTAINER("container", null, false),

    // meta - token management - not shown in public list of applicable permissions
    VIEW_API_TOKENS("view_api_tokens", null, false),
    CREATE_API_TOKENS("create_api_tokens", null, false),
    DELETE_API_TOKENS("delete_api_tokens", null, false),

    // meta - environments - not shown in public list of applicable permissions (api tokens cannot access other environments)
    CREATE_ENVIRONMENTS("create_environments", null, false),
    UPDATE_ENVIRONMENTS("update_environments", null, false),
    VIEW_ENVIRONMENTS("view_environments", null, false),
    DELETE_ENVIRONMENTS("delete_environments", null, false),

    // generic - not shown as this is given on all tokens - view a variety of essential things
    VIEW_WORKSPACE("view_workspace", null, false),

    // GitHub entity viewing
    VIEW_GITHUB_REPOSITORIES("view_github_repositories", "View github repositories within the workspace"),
    VIEW_GITHUB_BRANCHES("view_github_branches", "View github branches within a repository"),
    VIEW_GITHUB_COMMITS("view_github_commits", "View github commits within a branch in a repository"),
    UPDATE_GITHUB_CONNECTION("update_github_connection", "Generate a GitHub installation token", false),

    // projects
    CREATE_PROJECTS("create_projects", "Create a new project within an environment"),
    UPDATE_PROJECTS("update_projects", "Update details about projects within an environment"),
    DELETE_PROJECTS("delete_projects", "Remove a project from the current environment"),

    // lifecycles (deployments)
    CREATE_LIFECYCLE("create_lifecycles", "Trigger a deployment to occur inside of a project within an environment"),
    VIEW_LIFECYCLES("view_lifecycles", "View deployments within a project in an environment"),
    ROLLBACK_LIFECYCLES("rollback_lifecycles", "Rollback a deployment to a previous one in a project"),

    // deployment container scaling
    VIEW_CONTAINERS("view_containers", "View active containers within a deployment"),
    CREATE_CONTAINERS("create_containers", "Create new containers within a deployment"),

    // game instances within a container
    VIEW_GAME_INSTANCES("view_game_instances", "View a list of game instances within a container"),

    // affect container metadata [container only]
    CREATE_GAME_INSTANCES("create_game_instances", "Create game instances within the calling container", false),
    REMOVE_GAME_INSTANCES("remove_game_instances", "Remove game instances within the calling container", false),
    UPDATE_GAME_INSTANCES("update_game_instances", "Update the state and metadata of games within the calling container", false);


    private final String identifier;
    private final String description;
    private final boolean canAddToToken;

    Permission(final String identifier, final String description) {
        this.identifier = identifier;
        this.description = description;
        this.canAddToToken = true;
    }
}
