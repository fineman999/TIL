from langchain_core.prompts import ChatPromptTemplate
from langchain_openai import ChatOpenAI
from config import settings

def test_prompt_template():


    llm = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY)

    messages = ChatPromptTemplate.from_messages([("system", "이 시스템은 여행 전문가입니다."), ("user", "{user_input}")])
    chain = messages | llm
    invoke = chain.invoke({"user_input": "오사카 여행지 추천해주세요."})
    print(invoke)


if __name__ == "__main__":
    test_prompt_template()
