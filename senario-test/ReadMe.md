# 1. 셋업
```bash
docker-compose up -d influxdb grafana
```

# 2. 실행 
### 2.1 grafana 접속
- http://localhost:3000
- id: admin
- pw: admin
### 2.2 Configuration 추가
- Configuration -> Data Sources -> Add data source
- Name: influxdb
- HTTP URL: http://influxdb:8086
- Database: k6
- Save & Test
### 2.3 Dashboard 추가
- New -> Import
- ID: 2587
- Load

# 3. 테스트 실행
- scripts/k6.js 실행 결과를 influxdb에 저장하고, grafana로 시각화
```bash
k6 run --out influxdb=http://localhost:8086/k6 scripts/k6.js
```

# 4. 결과 확인