import unittest
from app.proto import myapp_pb2, myapp_pb2_grpc
from app.services.myapp_service import MyAppService


class TestMyAppService(unittest.TestCase):
    def setUp(self):
        self.service = MyAppService()

    def test_say_hello(self):
        request = myapp_pb2.HelloRequest(name="TestUser")
        response = self.service.SayHello(request, None)
        self.assertEqual(response.message, "Hello, TestUser!")


if __name__ == '__main__':
    unittest.main()
