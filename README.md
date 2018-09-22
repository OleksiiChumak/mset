# MSet

Mathematical sets in java.

[![Maven Central](https://img.shields.io/maven-central/v/com.ochumak/mset-core.svg)](https://maven-badges.herokuapp.com/maven-central/com.ochumak/mset-core)
[![Build Status](https://travis-ci.org/OleksiiChumak/mset.svg?branch=master)](https://travis-ci.org/OleksiiChumak/mset)
[![Coverage Status](https://sonarcloud.io/api/project_badges/measure?project=OleksiiChumak_mset&metric=coverage)](https://sonarcloud.io/dashboard?id=OleksiiChumak_mset)
[![License](http://img.shields.io/:license-apache-green.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

### Examples

Creating MSet

```java
MSet<Long> longSet = MSet.of(1L, 2L, 3L); //from array

ArrayList<String> strings = new ArrayList<>(); //from collection
strings.add("v1");
strings.add("v2");
MSet<String> stringSet = MSet.of(strings);

MSet<Integer> integerSet = IntStream.range(0, 10) //from stream
    .boxed()
    .collect(MSet.toMSet());
```

Iterating through set :
```java
for (Integer value : set) { //foreach loop
    //...
}

set.stream()    //stream
.forEach(value -> { /* ... */});


Iterator<Integer> iterator = set.iterator(); //iterator
while (iterator.hasNext()) {
    Integer value = iterator.next();
    // ... 
}
```

Some set operations:
```java
MSet.of(1, 2, 3).union(MSet.of(2, 3, 4)); //union {1,2,3,4}

MSet.of(1, 2, 3).intersection(MSet.of(2, 3, 4)); //intersection {2,3}

MSet.of(1, 2, 3).contains(1); // true
 
MSet.of(1, 2, 3).intersection(MSet.of(2, 3, 4)).contains(1); // false
```

Universal set bounded operations:
```java
MSet.of(1, 2, 3)
.complement()
.contains(4, MSet.of(1, 2, 3, 4)) //true

MSet.of(1, 2, 3)
.complement()
.contains(1, MSet.of(1, 2, 3, 4)) //false

```

## License

This project is licensed under [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0)

### Installation

Releases are available in [Maven Central](https://repo1.maven.org/maven2/com/ochumak/mset-core/)

#### Maven

Add this snippet to the pom.xml `dependencies` section:

```xml
<dependency>
  <groupId>com.ochumak</groupId>
  <artifactId>mset-core</artifactId>
  <version>1.4</version>
</dependency>
```

#### Gradle 

Add this snippet to the build.gradle `dependencies` section:

```groovy
compile 'com.ochumak:mset-core:1.4'
```

Pull requests are welcome.