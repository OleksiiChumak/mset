# MSet

Mathematical sets in java.

[![Build Status](https://travis-ci.org/OleksiiChumak/mset.svg?branch=master)](https://travis-ci.org/OleksiiChumak/mset)
[![Coverage Status](https://sonarcloud.io/api/project_badges/measure?project=OleksiiChumak_mset&metric=coverage)](https://sonarcloud.io/dashboard?id=OleksiiChumak_mset)

## License

This project is licensed under [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0)

### Installation

Snapshots are available in [Snapshot Repository](https://s3.eu-central-1.amazonaws.com/mset-artifacts/snapshot/)

#### Gradle

Add this snippets to build.gradle 

`repositories` section:  

```groovy
 maven {
    url 'https://s3.eu-central-1.amazonaws.com/mset-artifacts/snapshot/'
}
```

`dependencies` section:

```groovy
implementation 'org.mset:mset-core:1.0-SNAPSHOT'
```

Pull requests are welcome.