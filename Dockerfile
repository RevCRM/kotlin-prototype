
### Build Image ###
FROM node:8-slim AS build

# Add code
ADD . /opt/revcrm/
WORKDIR /opt/revcrm

# Build & Test
# --unsafe-perm required to make postinstall lerna bootstrap work
RUN npm install --unsafe-perm
RUN npm run build
RUN npm test

# Prune for production
# RUN npm prune --production

### Run Image ###
FROM node:8-alpine

COPY --from=build /opt/revcrm /opt/revcrm
WORKDIR /opt/revcrm

# Run the image as a non-root user
RUN adduser -D nodeapp
USER nodeapp

# Run the app.  CMD is required to run on Heroku
# $PORT is set by Heroku			
# CMD gunicorn --bind 0.0.0.0:$PORT wsgi
CMD npm start

# Expose is NOT supported by Heroku
EXPOSE 3000 		
