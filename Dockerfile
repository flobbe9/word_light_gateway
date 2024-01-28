FROM gradle:jdk17-alpine

WORKDIR /workdir

COPY . ./

ENTRYPOINT gradle bootRun