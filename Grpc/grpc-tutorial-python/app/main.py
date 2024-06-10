# app/main.py

from concurrent import futures
import grpc
from app.proto import myapp_pb2_grpc, myapp_pb2
from app.services import MyAppService
from app.config import settings
from grpc_reflection.v1alpha import reflection


def create_server():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    myapp_pb2_grpc.add_MyAppServicer_to_server(MyAppService(), server)

    # Enable reflection
    SERVICE_NAMES = (
        myapp_pb2.DESCRIPTOR.services_by_name['MyApp'].full_name,
        reflection.SERVICE_NAME,
    )
    reflection.enable_server_reflection(SERVICE_NAMES, server)

    server.add_insecure_port(f'[::]:{settings.GRPC_PORT}')

    return server


def start_server():
    server = create_server()
    server.start()
    print("gRPC server is running on port 50051")
    server.wait_for_termination()
