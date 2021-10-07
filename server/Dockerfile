FROM gradle:6.9.1-jdk11
MAINTAINER  Reza Nojavan <reza.nojavan@email.com>
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

ENTRYPOINT ["gradle", "bootRun"]