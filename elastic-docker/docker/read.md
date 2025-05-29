# 0. 링크
- [Elastic Stack 및 Docker Compose 시작](https://www.elastic.co/kr/blog/getting-started-with-the-elastic-stack-and-docker-compose)

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

# 4. filebeat

- 먼저 'filebeat_ingest_data' 폴더를 컨테이너에 매핑하도록 바인드 마운트를 설정합니다. 
- 호스트에 이 폴더가 없으면 컨테이너가 회전할 때 생성됩니다. 
- 사용자 정의 로그에 대해 Elastic Observability 내의 로그 스트림 뷰어를 테스트하려는 경우 .log 확장자를 /filebeat_ingest_data/로 확장하면 로그가 기본 Filebeat Datastream으로 읽혀집니다.
- 이 외에도 /var/lib/docker/containers 및 /var/run에도 매핑합니다. 
- /kr/docker.sock 이는 filebeat.autodiscover 섹션 및 과 결합됩니다. 
- 힌트 기반 자동 검색을 사용하면 Filebeat가 모든 컨테이너에 대한 로그를 가져올 수 있습니다. 
- 이러한 로그는 위에서 언급한 로그 스트림 뷰어에서도 찾을 수 있습니다.

# 5. logstash
- Logstash 구성은 Filebeat 구성과 매우 유사합니다. 
- 이번에도 바인드 마운트를 사용하고 호스트의 /logstash_ingest_data/ 폴더를 Logstash 컨테이너에 매핑합니다. 
- 여기에서 다양한 입력 플러그인 및 필터 플러그인 중 일부를 logstash.yml 파일을 수정하여 테스트해 볼 수 있습니다. 
- 그런 다음 데이터를 /logstash_ingest_data/ 폴더에 놓습니다. logstash.yml 파일을 수정한 후 Logstash 컨테이너를 다시 시작해야 할 수도 있습니다. 
- 참고로 Logstash 출력 인덱스 이름은 "logstash-%{+YYYY.MM.dd}"입니다. 
- 데이터를 보려면 아래와 같이 'logstash-*' 패턴에 대한 데이터 보기를 생성해야 합니다.



# 6. kibana 콘솔에서 쿼리 작성하기
- [링크](https://esbook.kimjmin.net/04-data/4.2-crud)
- Elasticsearch에서는 단일 도큐먼트별로 고유한 URL을 갖습니다. 도큐먼트에 접근하는 URL은
- `http://<호스트>:<포트>/<인덱스>/_doc/<도큐먼트 id>`

- 입력은 PUT과 POST로 할 수 있다.
- POST같은 경우 ID를 자동 생성한다.
- POST {index}/_update/{id}를 통해 업데이트 가능하다.

# 7. Relevancy
- BM25 알고리즘에서 **희소성(IDF)**이 높고, **텀 빈도(TF)**가 높으며, **필드 길이(Field Length)**가 짧은 경우, 검색 결과의 relevancy(정확도), 즉 스코어는 매우 높게 나타남
1. 높은 IDF (희소성): 검색어가 전체 인덱스에서 드물게 등장하면(예: "쥬라기"가 "공원"보다 적은 도큐먼트에 등장), 해당 텀은 더 중요한 정보를 담고 있다고 간주되어 스코어가 높아집니다.
2. 높은 TF (텀 빈도): 검색어가 특정 도큐먼트 내에서 자주 등장하면(예: "쥬라기 공원"이 10번 등장), 그 도큐먼트가 검색어와 더 관련성이 높다고 판단되어 스코어가 올라갑니다.
3. 짧은 Field Length: 검색어가 포함된 필드(예: 제목)가 짧을수록 텀의 비중이 크다고 간주되어 스코어가 더 높아집니다(예: 짧은 제목에 "lazy"가 포함된 경우).

# 8. 검색
- match 쿼리는 텍스트 검색에 특화된 쿼리로, 검색어와 도큐먼트의 relevancy를 계산하여 스코어를 부여합니다.
- must, must_not, should는 논리적 조건을 정의하는 bool 쿼리의 구성 요소이며, match는 이들 내부에서 실제 텍스트 매칭을 수행합니다.
- filter와 달리 match는 스코어를 계산하며, 풀 텍스트 검색에서 유연한 매칭과 정렬에 사용됩니다.
- **색인(indexing)**은 데이터를 검색 가능하도록 역 인덱스 구조로 변환하여 저장하는 과정

# 9. 데이터 색인과 텍스트 분석
- 텍스트 분석: 문자열 필드가 저장될 때 데이터에서 검색어 토큰을 저장하기 위해 여러 단계를 거치는 과정
  - 캐릭터 필터: 텍스트 데이터가 입력되면 가장 먼저 필요에 따라 전체 문장에서 특정 문자를 대치하거나 제거하는데 이 과정을 담당하는 기능
  - 토크나이저: 문장에 속한 단어들을 텀 단위로 하나씩 분리 해 내는 처리 과정을 거치는데 이 과정을 담당하는 기능
  - 토큰필터: 분리된 텀 들을 하나씩 가공하는 과정을 거치는데 이 과정을 담당하는 기능