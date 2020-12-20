FROM arm32v7/openjdk:11-jre

# Install gradle
ENV GRADLE_HOME=/opt/gradle/gradle-6.4.1
ENV PATH=${GRADLE_HOME}/bin:${PATH}

RUN wget https://services.gradle.org/distributions/gradle-6.4.1-bin.zip -P /tmp &&\
    unzip -d /opt/gradle /tmp/gradle-*.zip &&\
    rm -rf /tmp/*

WORKDIR /usr/src/app

COPY settings.gradle.kts ./

COPY build.gradle.kts ./

RUN gradle build || return 0

COPY . .

CMD [ "gradle", "run" ]
