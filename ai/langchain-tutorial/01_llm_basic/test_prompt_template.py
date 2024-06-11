from config import settings
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser
def test_prompt_template():

    # prompt + model + output parser
    # 이는 모델에게 천문학 분야의 전문가로서 행동하라고 지시
    prompt = ChatPromptTemplate.from_template(
        "You are an expert in astronomy. Answer the question. <Question>: {input}")

    llm = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY)

    # 모델의 출력을 문자열 형태로 파싱
    output_parser = StrOutputParser()

    # LCEL chain 실행
    chain = prompt | llm | output_parser

    invoke = chain.invoke({"input": "지구의 자전 주기는?"})
    print(invoke)
    # 결과
    # 지구의 자전 주기는 약 24시간입니다. 이것은 하루 동안 지구가 한 번 자전하는 시간을 의미합니다.


if __name__ == "__main__":
    test_prompt_template()

