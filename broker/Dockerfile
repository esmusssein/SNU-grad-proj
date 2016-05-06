FROM node:4.4.3
ADD . /usr/app
EXPOSE 1883
WORKDIR /usr/app
RUN rm -r node_modules
RUN npm install
ENTRYPOINT node index.js