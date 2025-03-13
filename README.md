# Quarkus Java Template

![Template Service](https://i.imgur.com/wTCpksY.png)

---

## 📅 Planned Features

- [x] Pricing log annotation
- [x] Permission requirement annotation
- [x] User storage in request context
- [ ] Automated spreading of events to other apps via kafka

---

## ▶️ Running locally

Running locally allows you to quickly test your changes, using the Quarkus framework we can

1. Copy `.env-example` into a file called `.env` and fill in all of the environment vars
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
2. Database - we use MongoDB by default, as it's simple and has good support - other CDI supported technologies include
   MySQL, Postgres and Cassandra.

---

## Help & Contacts

Questions can be asked in the **#engineering** Pumble channel for help and support.

- Product Manager: Billy Robinson
- Engineering Manager: Billy Robinson

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

---

## 🖥️ Deployment

Deployment is automatically handled by [Railway](https://railway.app/) on push to the `stg` or `prod` branches, see
configuration in `railyway.json`

All database migrations and steps should be automated when being added, if there's not a way to automate these, see the
`docs/deploy_guides` folder for information on deploying.
