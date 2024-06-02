# 베이스 이미지로 최신 nginx 이미지를 사용합니다.
FROM nginx:latest

# Nginx 설정 파일을 컨테이너 내부로 복사합니다.
# (옵션) 필요에 따라 사용자 정의 설정 파일을 복사할 수 있습니다.
COPY ./my-nginx.conf /etc/nginx/nginx.conf

# mkdir cert
RUN mkdir -p /etc/nginx/cert

# copy cert
COPY ./cert /etc/nginx/cert



# Nginx가 수신 대기할 포트를 지정합니다.
EXPOSE 8080

# 기본 명령은 nginx를 실행하여 포그라운드에서 유지하는 것입니다.
CMD ["nginx", "-g", "daemon off;"]
