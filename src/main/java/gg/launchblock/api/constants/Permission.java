package gg.launchblock.api.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {

    // meta - not shown in public list of applicable permissions
    CONTAINER("container", null, false),

    // meta - token management - not shown in public list of applicable permissions
    CREATE_API_TOKENS("create_api_tokens", null, false),
    VIEW_API_TOKENS("view_api_tokens", null, false),
    DELETE_API_TOKENS("delete_api_tokens", null, false),

    // meta - environments - not shown in public list of applicable permissions (api tokens cannot access other environments)
    CREATE_ENVIRONMENTS("create_environments", null, false),
    VIEW_ENVIRONMENTS("view_environments", null, false),
    UPDATE_ENVIRONMENTS("update_environments", null, false),
    DELETE_ENVIRONMENTS("delete_environments", null, false),

    // generic - not shown as this is given on all tokens - view a variety of essential things
    VIEW_WORKSPACE("view_workspace", null, false),

    // GitHub entity viewing
    UPDATE_GITHUB_CONNECTION("update_github_connection", "Generate a GitHub installation token", false),
    VIEW_GITHUB_REPOSITORIES("view_github_repositories", "View github repositories within the workspace"),
    VIEW_GITHUB_BRANCHES("view_github_branches", "View github branches within a repository"),
    VIEW_GITHUB_COMMITS("view_github_commits", "View github commits within a branch in a repository"),

    // projects
    CREATE_PROJECTS("create_projects", "Create a new project within an environment"),
    UPDATE_PROJECTS("update_projects", "Update details about projects within an environment"),
    DELETE_PROJECTS("delete_projects", "Remove a project from the current environment"),

    // lifecycles (deployments)
    CREATE_LIFECYCLE("create_lifecycles", "Trigger a deployment to occur inside of a project within an environment"),
    VIEW_LIFECYCLES("view_lifecycles", "View deployments within a project in an environment"),
    ROLLBACK_LIFECYCLES("rollback_lifecycles", "Rollback a deployment to a previous one in a project"),
    REMOVE_LIFECYCLES("delete_lifecycles", "Set a lifecycle as removed"),

    // deployment container scaling
    CREATE_CONTAINERS("create_containers", "Create new containers within a deployment"),
    VIEW_CONTAINERS("view_containers", "View active containers within a deployment"),

    // scaling
    VIEW_SCALE_GOALS("view_scale_goals", "View progress towards set down scaling parameters"),

    // logs
    VIEW_LOGS("view_logs", "View the logs attached to a lifecycle"),
    CREATE_LOGS("create_logs", "Create a set of logs attached to a container within a deployment", false),

    // variables (env vars)
    CREATE_VARIABLES("create_variables", "Create environment variables on a project or environment"),
    VIEW_VARIABLES("view_variables", "View variables attached to a project or environment"),
    UPDATE_VARIABLES("update_variables", "Update the list or contents of variables on a project or environment"),
    DELETE_VARIABLES("delete_variables", "Remove variables from a project or environment"),

    // game instances within a container
    VIEW_GAME_INSTANCES("view_game_instances", "View a list of game instances within a container"),

    // matchmaker
    CREATE_MATCHMAKER_MATCH("create_matchmaker_match", "Create a matchmaker match with a list of players", false),
    VIEW_MATCHMAKER_MATCH("view_matchmaker_match", "Create a matchmaker match with a list of players", false),
    MANAGE_MATCHMAKER_MATCH("manage_matchmaker_match", "Remove players from a matchmaker match", false),

    // affect container metadata [container only]
    CREATE_GAME_INSTANCES("create_game_instances", "Create game instances within the calling container", false),
    UPDATE_GAME_INSTANCES("update_game_instances", "Update the state and metadata of games within the calling container", false),
    REMOVE_GAME_INSTANCES("remove_game_instances", "Remove game instances within the calling container", false);


    private final String identifier;
    private final String description;
    private final boolean canAddToToken;

    Permission(final String identifier, final String description) {
        this.identifier = identifier;
        this.description = description;
        this.canAddToToken = true;
    }
}
