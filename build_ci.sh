#!/usr/bin/env bash

BUILD_ARGS=build -s --no-daemon jacocoTestReport sonarqube -Dsonar.projectKey=OleksiiChumak_mset -Dsonar.organization=oleksiichumak-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$SONAR_TOKEN

if [[ $PUBSLISH_TO_MAVEN == 'true' &&  $TRAVIS_BRANCH  == 'master' ]]
then
    echo Jars will be published to maven repository
    echo $SIGN_KEY | base64 -d > secring.gpg
    BUILD_ARGS=$BUILD_ARGS uploadArchives -Psigning.keyId=$SING_KEY_ID -Psigning.password=$SIGN_PASSWORD -Psigning.secretKeyRingFile=secring.gpg -PossrhUsername=$OSSRH_USERNAME -PossrhPassword=$OSSRH_PASSWORD
else
    echo Set PUBSLISH_TO_MAVEN=true to publish jars. Publish works only on master brunch
    BUILD_ARGS=$BUILD_ARGS -x singing
fi

echo $BUILD_ARGS
#./gradlew ${BUILD_ARGS}