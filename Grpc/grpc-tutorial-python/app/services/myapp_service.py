from concurrent import futures
import grpc
from app.proto import myapp_pb2, MyAppServicer


class MyAppService(MyAppServicer):
    def SayHello(self, request, context):
        return myapp_pb2.HelloReply(message=f'Hello, {request.name}!')

