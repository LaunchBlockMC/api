# Public RESTful API

![Public RESTful API](https://i.imgur.com/sW6Z0uN.png)

A public RESTful API to interact with the LaunchBlock services. This allows you to make requests to grab information
from
our services about various resources within environments within your workspace, or make operations to affect them.

All endpoints are ran under `https://api.launchblock.gg/`, you must provide an `Authorization` header!

---

## 🪙 Obtaining an API token

If you are running locally (or outside of LaunchBlock running containers) you must generate an API token. You can
generate this on [your canvas page](https://canvas.launchblock.gg).

*On your canvas page, within an environment: Click Settings > Tokens > New token: Give this a name and an expiry time.*

![New Token](https://i.imgur.com/wkm5bbG.png)

If you're running within a LaunchBlock container: you can use the `LB_API_TOKEN` environment variable that's
automatically injected into each container (this also gives access to some extra endpoints, check out the docs!).

---

## 📚 Documentation

We have extensive documentation about the API, you can find this at https://api.launchblock.gg.

If you have further questions around our API, join our [Discord Server](https://discord.gg/LaunchBlock)!

---

## ▶️ Running locally

Running locally allows you to mock responses and add any additional features you'd like to be exposed.

1. Copy `.env-example` into a file called `.env` and fill in all the environment vars
2. Run `./gradlew quarkusDev` from the gradle quarkus menu to run in dev mode

--- 

## 🚀 Testing

### Running Tests

To run unit tests, execute:

```bash
./gradlew test
```

For integration tests:

```bash
./gradlew integrationTest
````

---

## 🧩 Configuration Options

### Custom Configuration

Users can define custom configuration properties in the `application.properties` file. This allows for flexibility in
application settings without modifying the source code. For example, you can add your own properties like:

```properties
# Custom configuration
myapp.custom.property1=value1
myapp.custom.property2=value2
```

You can then access these properties in your code using the @ConfigProperty annotation:

```java
import io.smallrye.config.ConfigMapping;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class MyService {

    @ConfigProperty(name = "myapp.custom.property1")
    String property1;

    // Use property1 in your service logic
}
```

### Profile Management

Quarkus supports multiple profiles to manage environment-specific configurations. You can create profile-specific
configuration files named application-{profile}.properties. For example:

- `application-dev.properties` for development settings
- `application-test.properties` for testing settings
- `application-prod.properties` for production settings

To activate a profile, set the quarkus.profile property either in your application.properties or as an environment
variable:

```properties
# Activate the dev profile
quarkus.profile=dev
```

---

## 🛠️ Tech Stack

1. Caching - we use the `quarkus-caching` library which allows either memory based cache (default) or synced cache on
   redis/other technologies.
2. Database - we use MongoDB to store API tokens, and it's simple and has good support - other CDI supported
   technologies include
   MySQL, Postgres and Cassandra - and we're passively looking at alternatives as we develop this.

---

## 📁 Structure

- Annotations: Annotations that make development quicker or automate implementation of certain parts of the application
  to reduce boilerplate.
- Clients: Rest clients that communicate with other applications or third party dependencies
- Constants: Constant values that are used around the application to avoid magic strings
- Endpoints: Endpoints that can be accessed by other applications or users
- Entities: Entities that are stored in a database or extensions to them
- Exception: Exception handling and mapping to help with debugging prod application
- Filter: Filters that are handled before or after requests are made
- Interceptor: Interceptors that are triggered before a method is ran when attached by annotation.
- Models: Models that are used for requests or responses to get data in or out of the application
- Repositories: Database repositories that allow entities to be stored in a database
- User: Utilities and objects related to handling users and the requests they make