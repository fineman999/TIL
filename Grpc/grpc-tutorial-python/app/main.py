# app/main.py

from concurrent import futures
import grpc, signal
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


def serve():
    server = create_server()

    def handle_sigterm(*args):
        print("Received shutdown signal")
        # Graceful shutdown
        all_rpcs_done_event = server.stop(5)  # 5 seconds grace period
        all_rpcs_done_event.wait(5)  # Wait for the grace period to complete
        print("Server shut down gracefully")

    signal.signal(signal.SIGTERM, handle_sigterm)
    signal.signal(signal.SIGINT, handle_sigterm)

    server.start()
    print(f"gRPC server is running on port {settings.GRPC_PORT}")
    try:
        server.wait_for_termination()
    except KeyboardInterrupt:
        print("KeyboardInterrupt received, shutting down server")


def start_server():
    serve()
