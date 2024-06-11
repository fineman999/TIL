from config import settings
from langchain_openai import ChatOpenAI


def test_llm_invoke():
    # model
    llm = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY)
    # chain 실행
    chain = llm.invoke("지구의 자전 주기는?")
    print(chain)
    # 결과
    # content='지구의 자전 주기는 약 24시간입니다. 이것은 하루의 시간을 결정하고, 지구를 동서방향으로 회전시키는데 중요한 역할을 합니다.
    # 이러한 자전은 지구의 자전축을 따라 일어나며, 남북극을 중심으로 발생합니다.'
    # response_metadata={'
    # token_usage': {'
    #   completion_tokens': 100,
    #   'prompt_tokens': 16,
    #   'total_tokens': 116
#   },
#       'model_name': 'gpt-3.5-turbo',
#       'system_fingerprint': None,
#       'finish_reason': 'stop',
#       'logprobs': None
#       }
#       id='run-7349387a-a9b1-4600-9a64-e87881ea1cec-0'
#       usage_metadata={'input_tokens': 16, 'output_tokens': 100, 'total_tokens': 116}


if __name__ == "__main__":
    test_llm_invoke()
