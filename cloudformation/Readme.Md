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
export AWS_PROFILE=home

# 프로필 전환 확인
>> aws sts get-caller-identity
```