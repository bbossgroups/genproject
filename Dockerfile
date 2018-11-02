FROM frolvlad/alpine-oraclejdk8
VOLUME /tmp
ADD build/dist/genproject /tmp/genproject
