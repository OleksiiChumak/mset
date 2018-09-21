#!/usr/bin/env bash

function generateSigningFile()
{
    echo Generating signing file
    echo $SIGN_KEY | base64 -d > secring.gpg
}

BUILD_ARGS="build -s --no-daemon jacocoTestReport sonarqube -Dsonar.projectKey=OleksiiChumak_mset -Dsonar.organization=oleksiichumak-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$SONAR_TOKEN"
MAVEN_CENTRAL_ARGS="-Psigning.keyId=$SING_KEY_ID -Psigning.password=$SIGN_PASSWORD -Psigning.secretKeyRingFile=secring.gpg -PossrhUsername=$OSSRH_USERNAME -PossrhPassword=$OSSRH_PASSWORD"

if [[ $MAVEN_ACTION == 'PUBLISH' &&  $TRAVIS_BRANCH  == 'master' ]]; then
    echo
    echo Jars will be published to maven repository
    echo
    generateSigningFile
    BUILD_ARGS+=" uploadArchives "
    BUILD_ARGS+=MAVEN_CENTRAL_ARGS
elif [[ $MAVEN_ACTION == 'RELEASE' &&  $TRAVIS_BRANCH  == 'master' ]]; then
    echo
    echo Jars will be released
    echo
    generateSigningFile
    BUILD_ARGS+=" release -Prelease.useAutomaticVersion=true "
    BUILD_ARGS+=MAVEN_CENTRAL_ARGS
else
    echo
    echo "Set MAVEN_ACTION={PUBLISH,RELEASE} to perform action maven repository. MAVEN_ACTION works only on master branch"
    echo
    BUILD_ARGS+=" -x signArchives"
fi

./gradlew $BUILD_ARGS