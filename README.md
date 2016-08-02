# beam-interactive-java

A Java client designed to interface with the Beam Interactive API.

## Usage

To use this client in a project, you may add it to your pom.xml with the
following:

```xml
<repositories>
  <repository>
    <id>beam-releases</id>
    <url>https://maven.beam.pro/content/repositories/releases/</url>
  </repository>
  <repository>
    <id>beam-snapshots</id>
    <url>https://maven.beam.pro/content/repositories/snapshots/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>pro.beam</groupId>
    <artifactId>interactive</artifactId>
    <version>1.5.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```

then head to the [example sources](https://github.com/WatchBeam/beam-interactive-java/tree/master/src/example/java/pro/beam/example) and browse for yourself.

## Compilation

We use Maven to handle our dependencies.

- Install [Maven 3](https://maven.apache.org/download.cgi)
- Checkout this repo and: `mvn clean install`

## Questions

Someone is always available in our
[Gitter room](https://gitter.im/MCProHosting/beam-dev), and many resources can
be found on our [developer site](https://dev.beam.pro).

## License

MIT.
