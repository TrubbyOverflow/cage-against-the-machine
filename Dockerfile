FROM gradle:6.6.1

WORKDIR /usr/src/app

COPY settings.gradle.kts ./

COPY build.gradle.kts ./

RUN gradle build || return 0

COPY . .

ARG TELEGRAM_TOKEN

ENV TELEGRAM_TOKEN=$TELEGRAM_TOKEN

CMD [ "gradle", "run" ]
