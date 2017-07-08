# Masterleague4s
A scala client and crawler for the masterleague API

[![Build Status](https://travis-ci.org/martijnhoekstra/masterleague4s.svg?branch=master)](https://travis-ci.org/martijnhoekstra/masterleague4s)

## Status
This project is currently in pre-release, where it serves to scratch a personal itch. Creating issues on this repo with feature
requests is a good way to see if we cn also make this project scratch your itch. Creating issues with nags, annoyances and other bugbears
is also encouraged.

The current (and first) pre-release is version 0.0.2, released for scala 2.12.x only.

## Installing

Artifacts are published to Sonatype. Add the following line to your `build.sbt`:

```
libraryDepenencies += "com.heroestools" %% "mlapi" % "0.0.2"
```

For Maven

```
<dependency>
    <groupId>com.heroestools</groupId>
    <artifactId>mlapi_2.12</artifactId>
    <version>0.0.2</version>
</dependency>
```

For Ivy

```
<dependency org="com.heroestools" name="mlapi_2.12" rev="0.0.2" />
```

And for the rest, well, you get the idea.

## Documentation

There is [Scaladoc](https://oss.sonatype.org/service/local/repositories/releases/archive/com/heroestools/mlapi_2.12/0.0.2/mlapi_2.12-0.0.2-javadoc.jar/!/masterleagueapi/index.html).
The "main" entrypoint for the API is [the API object in the api package](https://oss.sonatype.org/service/local/repositories/releases/archive/com/heroestools/mlapi_2.12/0.0.2/mlapi_2.12-0.0.2-javadoc.jar/!/masterleagueapi/api/Api$.html),
which can give you streaming results in an fs2 stream, or fully collected results in a `Map[Long -> Entry]`

At the moment, the documentation is only types and method names, no explanation. In the very early days of this library the API
is too volatile and succeptable to changes to warrent a bigger investment in that. When it stabalizes, there should be more
comprehensive doc.


## Java users

On a best-effort basis we expose an API that is hopefully Java friendly. You can find this API as [the Api object in the javaapi package](https://oss.sonatype.org/service/local/repositories/releases/archive/com/heroestools/mlapi_2.12/0.0.2/mlapi_2.12-0.0.2-javadoc.jar/!/masterleagueapi/javaapi/Api$.html)
