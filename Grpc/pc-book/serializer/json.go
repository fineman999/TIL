package serializer

import (
	"google.golang.org/protobuf/encoding/protojson"
	"google.golang.org/protobuf/proto"
)

func ProtobufToJSON(message proto.Message) (string, error) {
	marshaller := protojson.MarshalOptions{
		UseEnumNumbers:  false, // true이면, enum을 숫자로 출력한다.
		Indent:          " ",   // 들여쓰기
		UseProtoNames:   true,  // true이면, 필드 이름을 proto에서 정의한 이름으로 출력한다.
		EmitUnpopulated: true,  // true이면, 값이 없는 필드도 출력한다.
	}
	bytes, err := marshaller.Marshal(message)
	return string(bytes), err
}
