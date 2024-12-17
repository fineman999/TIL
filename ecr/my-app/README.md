## 1.1 AWS ECR 도커 로그아웃
```bash
docker logout <ECR URI>

ex) docker logout 183295419955.dkr.ecr.ap-northeast-2.amazonaws.com/weplat2/namespace
```

## 2. AWS Docker 빌드 및 태그
```bash
docker build -t <ECR URI>:<tag> .

```

##  3. AWS ECR 도커 인증하기
```bash
aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <ECR URI>

Login Succeeded
```


## 3. 이미지를 ECR에 푸시
```bash
docker push <ECR URI>:<tag>
```

## 4. ECR에 이미지가 잘 올라갔는지 확인
```bash
aws ecr describe-images --repository-name <repository-name>

ex) aws ecr describe-images --repository-name weplat2/namespace
```

## 5. ECR에 있는 이미지를 EC2에 로그인하고, aws configure 설정

## 6.ECR에 업로드한 이미지를 EC2에서 다운로드
```bash
docker pull <ECR URI>:<tag>
```

## 7. 다운로드한 이미지를 실행
```bash
docker run -d -p 80:80 <ECR URI>:<tag>

ex) docker run -d -p 3001:3000 --name dev-container -e NODE_ENV=dev --restart always my-dev-image
```
