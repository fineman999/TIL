# 1. ElasticSearch
## 1. 실행
```shell
docker-copomse up
```
- 인증서 및 비밀번호 생성이 완료되면 설정 컨테이너가 의도적으로 종료

## 2. 테스트
```shell
docker ps
```
- docker ps에서 컨테이너 이름 확인

```shell
mkdir -p ./tmp                                                                                                                                                                                                                                       
docker cp docker-es01-1:/usr/share/elasticsearch/config/certs/ca/ca.crt ./tmp/
```

- 인증서 복사

```shell
curl --cacert /tmp/ca.crt -u elastic:changeme https://localhost:9200
{
  "name" : "es01",
  "cluster_name" : "docker-cluster",
  "cluster_uuid" : "r89hmPq4TciTj35Q6EVgwQ",
  "version" : {
    "number" : "8.7.1",
    "build_flavor" : "default",
    "build_type" : "docker",
    "build_hash" : "f229ed3f893a515d590d0f39b05f68913e2d9b53",
    "build_date" : "2023-04-27T04:33:42.127815583Z",
    "build_snapshot" : false,
    "lucene_version" : "9.5.0",
    "minimum_wire_compatibility_version" : "7.17.0",
    "minimum_index_compatibility_version" : "7.0.0"
  },
  "tagline" : "You Know, for Search"
}

```

# 2. Kibana

- `environment` 섹션에서 ELASTICSEARCH_HOSTS=https://es01:9200을 지정하고 있음을 확인하세요. 
- 컨테이너를 지정할 수 있습니다. 
- Docker 기본 네트워킹을 활용하고 있으므로 ES01 Elasticsearch 컨테이너의 이름을 여기에 지정합니다. 
- docker-compose.yml 파일 시작 부분에 지정된 “elastic” 네트워크를 사용하는 모든 컨테이너는 다른 컨테이너 이름을 올바르게 확인하고 서로 통신할 수 있습니다.

- 아이디: elastic
- 비밀번호: .env에 설정한 비밀번호


# 3. **metricbeat.yml**
- Metricbeat는 시작하기 전에 ES01 및 Kibana 노드가 정상인지에 따라 달라집니다. 
- 여기서 주목할만한 구성은 metricbeat.yml 파일에 있습니다. 
- Elasticsearch, Kibana, Logstash 및 Docker를 포함하여 메트릭 수집을 위한 4개의 모듈을 활성화했습니다. 
- 즉, Metricbeat가 작동 중인지 확인한 후 Kibana로 이동하여 “스택 모니터링”으로 이동하여 상황이 어떻게 보이는지 확인할 수 있습니다.
- [링크](http://localhost:5601/app/monitoring)