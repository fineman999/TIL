from langchain_openai import ChatOpenAI
from config import settings
# 모델 파라미터 설정
params = {
    "temperature": 0.7, # 생성된 텍스트의 다양성 조정
    "max_tokens": 10, # 생성할 최대 토큰 수
}

kwargs = {
    "frequency_penalty": 0.5, # 높은 값은 반복을 줄이고, 낮은 값은 반복을 증가시킴(0.0 ~ 1.0)
    "presence_penalty": 0.5, # 높은 값은 중복을 줄이고, 낮은 값은 중복을 증가시킴(0.0 ~ 1.0)
    "stop": "\n", # 생성된 텍스트 중지 문자
}


def test_prompt_template():
    # 모델 인스턴스를 생성할 때 설정한
    llm = ChatOpenAI(model="gpt-3.5-turbo", **params, openai_api_key=settings.API_KEY, model_kwargs=kwargs)

    # 모델 호출
    question = "태양계에서 가장 큰 행성은 무엇인가요?"
    invoke = llm.invoke(input=question)
    print(invoke)


if __name__ == "__main__":
    test_prompt_template()