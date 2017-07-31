# Masterleague4s
A scala client and crawler for the masterleague API

[![Build Status](https://travis-ci.org/martijnhoekstra/masterleague4s.svg?branch=master)](https://travis-ci.org/martijnhoekstra/masterleague4s)

[![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](https://gitter.im/masterleague4s/Lobby)

## Status
This project is currently in pre-release, where it serves to scratch a personal itch. Creating issues on this repo with feature
requests is a good way to see if we cn also make this project scratch your itch. Creating issues with nags, annoyances and other
bugbears is also encouraged.

The current pre-release is version 0.0.3, released for scala 2.12.x only. Scala 2.11.11 is the last 2.11 release, and with that, 2.11 is effectively EOL. If you do want a 2.11 release for some reason, open an issue and we can talk about it.

## Installing

Artifacts are published to Sonatype. Add the following line to your `build.sbt`:

```
libraryDepenencies += "com.heroestools" %% "masterleague4s" % "0.0.3"
```

For Maven

```
<dependency>
    <groupId>com.heroestools</groupId>
    <artifactId>masterleague4s_2.12</artifactId>
    <version>0.0.3</version>
</dependency>
```

For Ivy

```
<dependency org="com.heroestools" name="masterleague4s_2.12" rev="0.0.3" />
```

And for the rest, well, you get the idea.

If you want to live on the bleeding edge there are snapshot releases published to sonatype snapshots. You'll need to add a resolver for that if you haven't already:

```
resolvers += 
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
 ```
 
 The latest snapshot release is 
 
 ```
libraryDepenencies += "com.heroestools" %% "masterleague4s" % "0.0.4-SNAPSHOT"
```
 
## Documentation

There is [Scaladoc](https://oss.sonatype.org/service/local/repositories/releases/archive/com/heroestools/masterleague4s_2.12/0.0.3/masterleague4s_2.12-0.0.3-javadoc.jar/!/masterleague4s/index.html).
The "main" entrypoint for the API is [the API object in the api package](https://oss.sonatype.org/service/local/repositories/releases/archive/com/heroestools/masterleague4s_2.12/0.0.3/masterleague4s_2.12-0.0.3-javadoc.jar/!/masterleague4s/api/Api$.html),
which can give you streaming results in an fs2 stream, or fully collected results in a `Map[Long -> Entry]`

At the moment, the documentation is only types and method names, no explanation. In the very early days of this library the API
is too volatile and succeptable to changes to warrent a bigger investment in that. When it stabalizes, there should be more
comprehensive doc.

## Java users

On a best-effort basis we expose an API that is hopefully Java friendly. You can find this API as [the Api object in the javaapi package](https://oss.sonatype.org/service/local/repositories/releases/archive/com/heroestools/masterleague4s_2.12/0.0.3/masterleague4s_2.12-0.0.3-javadoc.jar/!/masterleague4s/javaapi/Api$.html)

## License

This project is released, for now, under the GPLv3. That means (informally, non-legalese, non-legally-binding language):

* You can use this project for *whatever you want*
* You have access to the source code for of this project, which you can also use for *whatever you want*

under the conditions that
* *If* you further distribute this software, you must do so under the same license terms
* *If* you distribute *other* software that uses this library, you must also do so under the same license terms, for the entire thing
* If you just make other software, but you're not distributing it, power to you! Have fun with it. There are no further conditions attached.
