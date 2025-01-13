# AWS Configure 설정
```bash
# 인증정보 설정 (프로필 사용 X, default 인증정보 설정)
>> aws configure

# profile 등록
>> aws configure --profile <PROFILE_NAME>

# profile 리스트 확인
>> aws configure list

# 해당 프로필로 전환
>> export AWS_PROFILE="<PROFILE_NAME>"
export AWS_PROFILE=chanwork01

# 프로필 전환 확인
>> aws sts get-caller-identity
```

# AWS Bastion Server로 EC2 접속
```bash
# Bastion Server로 접속
>> ssh -i <PEM_FILE> ec2-user@<BASTION_PUBLIC_IP>

# chmod 400 <PEM_FILE>
>> chmod 400 weplat-work-ap2-key.pem

# Bastion Server에서 EC2로 접속
>> ssh -i <PEM_FILE> ec2-user@<EC2_PRIVATE_IP>
ex ) ssh -i weplat-work-ap2-key.pem ec2-user@10.0.2.170
```

# AWS EC2 Start init Script
```bash

#!/bin/bash
sudo yum -y update
sudo amazon-linux-extras install docker
sudo service docker start
sudo usermod -a -G docker ec2-user
sudo apt install ruby
sudo apt install wget
cd ~ && wget https://aws-codedeploy-ap-northeast-2.s3.ap-northeast-2.amazonaws.com/latest/install
sudo chmod +x ./install
sudo ./install auto
sudo service codedeploy-agent start
```