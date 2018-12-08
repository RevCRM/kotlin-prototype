FROM openjdk:11-jre-slim

# Copy and extract artifact
COPY  revcrm-server/build/distributions/revcrm-server-1.0.tar /app/
WORKDIR /app
RUN tar -xvf revcrm-server-1.0.tar

# Run it
WORKDIR /app/revcrm-server-1.0
CMD bin/revcrm-server
EXPOSE 8800
