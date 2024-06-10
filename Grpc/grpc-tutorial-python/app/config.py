import os
from dotenv import load_dotenv

load_dotenv()


class ConfigSettings:
    _instance = None

    API_KEY: str = os.getenv("API_KEY")
    GRPC_PORT: int = os.getenv("GRPC_PORT")

    @classmethod
    def get_instance(cls):
        if cls._instance is None:
            cls._instance = cls()
        return cls._instance


settings = ConfigSettings.get_instance()
