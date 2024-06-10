import logging
from .config import settings
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)
logger.info("Application package initialized.")

logger.info(f"API_KEY: {settings.API_KEY}")
logger.info(f"GRPC_PORT: {settings.GRPC_PORT}")
