
# Nectar API User Service

NectarAPI is a microservices-based, integrated meter device management (MDM) and head-end system (HES) tool for prepaid, STSEd2 meters. It is developed to support high availability for small, medium and large utilities and is intended to be deployed on kubernetes or similar orchestrators. NectarAPI allows utilities to generate and decode IEC62055-41 tokens using its internal virtual HSM or a Prism HSM via the Prism Thrift API. In addition, it allows for subscriber, meter and utility management and multiple STS configurations can be managed using the NectarAPI. NectarAPI uses an API-first approach and exposes feature-rich, REST API endpoints that allow for token generation/decoding, subscribers/users/utility management, logging e.t.c. NectarAPI's virtual HSM is IEC62055-41:2018 (STS6) compliant and supports DES (DKGA02) and KDF-HMAC-SHA-256 (DKGA04) as well as STA (EA07) and MISTY1 (EA11).

The user-service is one of the micro-services required to run the NectarAPI. This service manages the following on a NectarAPI deployment:

1. Users - These are entities able to log into the NectarAPI and create subscribers, meters, STS configurations and utilities.
2. Meters - The user service manages electricity, water and gas meters and their assignment to subscribers.
3. Subscribers - These are individuals assigned meters by utilities. 
4. Utilities - These are the vending entities. Multiple utilities with different payment and STS configurations can be assigned to a single user.

# Built with

NectarAPI user-service is built using Springboot version 3, OpenJDK (Java) version 17, Redis and Gradle8 for builds. 

# Getting Started

To run the user service, first run the [nectar-db](https://github.com/NectarAPI/nectar-db) service and ensure that the credentials and port match those defined in this service's `/src/main/resources/application.yml` configurations. 

Then use gradle to deploy and run the service

`./gradlew build -x test && ./gradlew bootRun`


You should have output similar to the following:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.1)

2024-02-08T07:54:17.110Z  INFO 1 --- [           main] k.c.n.user.NectarUserServiceApplication  : Starting NectarUserServiceApplication using Java 17.0.10 with PID 1 (/etc/user-service/user-service.jar started by root in /etc/user-service)
2024-02-08T07:54:17.129Z  INFO 1 --- [           main] k.c.n.user.NectarUserServiceApplication  : No active profile set, falling back to 1 default profile: "default"
2024-02-08T07:54:19.652Z  INFO 1 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2024-02-08T07:54:19.771Z  INFO 1 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 87 ms. Found 15 JPA repository interfaces.
2024-02-08T07:54:21.326Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8082 (http)
2024-02-08T07:54:21.337Z  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-02-08T07:54:21.337Z  INFO 1 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.17]
2024-02-08T07:54:21.379Z  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-02-08T07:54:21.380Z  INFO 1 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 3958 ms
2024-02-08T07:54:21.573Z  INFO 1 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2024-02-08T07:54:21.636Z  INFO 1 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.4.1.Final
2024-02-08T07:54:21.730Z  INFO 1 --- [           main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2024-02-08T07:54:22.014Z  INFO 1 --- [           main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2024-02-08T07:54:22.039Z  INFO 1 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2024-02-08T07:54:22.306Z  INFO 1 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@745926c3
2024-02-08T07:54:22.308Z  INFO 1 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2024-02-08T07:54:22.532Z  WARN 1 --- [           main] org.hibernate.orm.deprecation            : HHH90000025: PostgreSQLDialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
2024-02-08T07:54:25.566Z  INFO 1 --- [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2024-02-08T07:54:25.580Z  INFO 1 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2024-02-08T07:54:26.166Z  INFO 1 --- [           main] o.s.d.j.r.query.QueryEnhancerFactory     : Hibernate is in classpath; If applicable, HQL parser will be used.
2024-02-08T07:54:28.230Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8082 (http) with context path ''
2024-02-08T07:54:28.259Z  INFO 1 --- [           main] k.c.n.user.NectarUserServiceApplication  : Started NectarUserServiceApplication in 12.861 seconds (process running for 16.977)
2024-02-08T08:04:15.884Z  INFO 1 --- [nio-8082-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2024-02-08T08:04:15.884Z  INFO 1 --- [nio-8082-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2024-02-08T08:04:15.886Z  INFO 1 --- [nio-8082-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms



```

# Usage

While the `user-service` may be run independent of other NectarAPI micro-services, it is recommended that the nectar-deploy script be used to launch the tokens-service as part of NectarAPI. REST API access may then be available via the [api-gateway](https://github.com/NectarAPI/api-gateway).

# Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions are greatly appreciated.

If you have suggestions for adding or removing projects, feel free to open an issue to discuss it, or directly create a pull request after you edit the README.md file with necessary changes.

Please make sure you check your spelling and grammar.

Please create individual PRs for each suggestion.


# Creating A Pull Request
To create a PR, please use the following steps:
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

# License 

Distributed under the  AGPL-3.0 License. See LICENSE for more information
