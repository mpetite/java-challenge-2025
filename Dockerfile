FROM ubuntu:latest
LABEL authors="matias.e.petite"

ENTRYPOINT ["top", "-b"]