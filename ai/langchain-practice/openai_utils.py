import toml
import os
from langchain_google_genai import ChatGoogleGenerativeAI


def get_google_gemini_model():
    # config.toml 파일을 읽고 파싱합니다.
    config = toml.load('config.toml')

    # OPENAI API KEY를 환경변수로 설정합니다.
    os.environ['GOOGLE_API_KEY'] = config['GOOGLE_API_KEY']

    openai_api_key = os.getenv("GOOGLE_API_KEY")
    if not openai_api_key:
        raise ValueError("OPENAI_API_KEY environment variable is not set. Please set it before using this function.")

    llm = ChatGoogleGenerativeAI(model="gemini-pro", temperature=0)
    return llm
