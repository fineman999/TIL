# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# NO CHECKED-IN PROTOBUF GENCODE
# source: app/proto/myapp.proto
# Protobuf Python Version: 5.27.1
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import runtime_version as _runtime_version
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
_runtime_version.ValidateProtobufRuntimeVersion(
    _runtime_version.Domain.PUBLIC,
    5,
    27,
    1,
    '',
    'app/proto/myapp.proto'
)
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x15\x61pp/proto/myapp.proto\x12\x05myapp\"\"\n\x0cHelloRequest\x12\x12\n\x04name\x18\x01 \x01(\tR\x04name\"&\n\nHelloReply\x12\x18\n\x07message\x18\x01 \x01(\tR\x07message2;\n\x05MyApp\x12\x32\n\x08SayHello\x12\x13.myapp.HelloRequest\x1a\x11.myapp.HelloReplyBK\n\tcom.myappB\nMyappProtoP\x01\xa2\x02\x03MXX\xaa\x02\x05Myapp\xca\x02\x05Myapp\xe2\x02\x11Myapp\\GPBMetadata\xea\x02\x05Myappb\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'app.proto.myapp_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  _globals['DESCRIPTOR']._loaded_options = None
  _globals['DESCRIPTOR']._serialized_options = b'\n\tcom.myappB\nMyappProtoP\001\242\002\003MXX\252\002\005Myapp\312\002\005Myapp\342\002\021Myapp\\GPBMetadata\352\002\005Myapp'
  _globals['_HELLOREQUEST']._serialized_start=32
  _globals['_HELLOREQUEST']._serialized_end=66
  _globals['_HELLOREPLY']._serialized_start=68
  _globals['_HELLOREPLY']._serialized_end=106
  _globals['_MYAPP']._serialized_start=108
  _globals['_MYAPP']._serialized_end=167
# @@protoc_insertion_point(module_scope)
