# graphql 전송하기
## Mutation
### Mutation 연습하기 1

```graphql
mutation CreateMusicMutation {
	createMusic(artist: "볼빨간사춘기",genre:"힙합"){
		id
	}
}
```
- `CreateMusicMutation`는 Operation name 에 해당하는 부분
- 생략할 수 있는 부분이지만, 공식문서에 따르면 실제 서비스 환경에서 요청을 식별하고 디버깅하는데 도움을 주기 때문에 Operation name 을 부여하는 것을 권장
- 두번째 라인의 `createMusic` 이름을 통해 실제로 스키마에 정의된 필드 및 컨트롤러 메서드와 매핑

### Create - Response
```json
{
  "data": {
	"createMusic": {
	  "id": 1
	}
  }
}
```
- 위 request 단점은 하드코딩된 값을 사용하면, 정적인 인자를 전달하는 문제점이 있다.
- 이를 해결하기 위해 변수(Variables)를 사용할 수 있다.
```graphql
mutation CreateMusicMutation($artist: String!, $genre: String!) {
	createMusic(artist: $artist, genre: $genre){
		id
	}
}
```
```json
{
  "artist": "볼빨간사춘기",
  "genre": "힙합"
}
```
- 위와 같이 변수를 사용하면, 동적으로 값을 전달할 수 있다.


## Query Operation
```graphql
query GetMusicQuery($id: ID!) {
    getMusicById(id: $id) {
        id
        artist
        genre
        createdDate
    }
}
```

```json
{
  "id": 1
}
```

## Enum 활용
```graphql
enum Genre {
    POP
    DANCE
    HIPHOP
    BALLAD
    ROCK
}
type Music {
    id : ID! # '!' means required
    artist : String
    genre : Genre
    createdDate : String
}
```
## Input Type 활용
```graphql
input MusicInput {
    artist : String
    genre : Genre
}
type Mutation {
    createMusic(musicInputDto: MusicInput) : Music
    updateMusic(id : ID!, artist : String, genre : Genre) : Music
    deleteMusic(id : ID!) : Boolean
}
```

### Mutation 연습하기 2
```graphql
mutation CreateMusic($musicInputDto: MusicInput!) {
    createMusic(musicInputDto: $musicInputDto) {
        artist
        genre
    }
}

```
```json
{
  "musicInputDto": {
    "artist": "에픽하이",
    "genre": "HIPHOP"
  }
}
```

## Custom Scalar Type
GraphQL은 어떤 언어에 의존하지 않으며, 기본적으로 `ID`, `String`, `Int`, `Float`, `Boolean` 타입을 제공한다.
