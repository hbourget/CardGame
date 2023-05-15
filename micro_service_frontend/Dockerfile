FROM node:20 as build
WORKDIR /app
COPY . .
ENV NG_CLI_ANALYTICS=ci
RUN npm install && npm run build

FROM nginx:1.23.4
WORKDIR /usr/share/nginx/html
COPY --from=build /app/dist/micro_service_frontend .
COPY nginx.conf /etc/nginx/conf.d/default.conf
# EXPOSE 80
# CMD ["nginx", "-g", "daemon off;"]
