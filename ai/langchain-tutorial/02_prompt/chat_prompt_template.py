from langchain_core.prompts import ChatPromptTemplate, SystemMessagePromptTemplate, HumanMessagePromptTemplate
from langchain_core.output_parsers import StrOutputParser
from langchain_openai import ChatOpenAI
from config import settings

"""
1. Message 유형
SystemMessage: 시스템의 기능을 설명합니다.
HumanMessage: 사용자의 질문을 나타냅니다.
AIMessage: AI 모델의 응답을 제공합니다.
FunctionMessage: 특정 함수 호출의 결과를 나타냅니다.
ToolMessage: 도구 호출의 결과를 나타냅니다.
"""


def test_prompt_template():
    chat_prompt = ChatPromptTemplate.from_messages([("system", "이 시스템은 천문학 질문에 답변할 수 있습니다."), ("user", "{user_input}")])

    messages = chat_prompt.format_messages(user_input="태양계에서 가장 큰 행성은 무엇인가요?")
    print(messages)
    # 출력: [SystemMessage(content='이 시스템은 천문학 질문에 답변할 수 있습니다.'), HumanMessage(content='태양계에서 가장 큰 행성은 무엇인가요?')]
    return chat_prompt


def run_prompt_template(chat_prompt):
    llm = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY)
    output_parser = StrOutputParser()
    chain = chat_prompt | llm | output_parser
    invoke = chain.invoke({"user_input": "태양계에서 가장 큰 행성은 무엇인가요?"})
    print(invoke)
    # 출력: 태양계에서 가장 큰 행성은 목성입니다. 목성은 태양계에서 가장 큰 질량과 부피를 가지고 있으며, 지름이 가장 큰 행성이기도 합니다.


def message_prompt_template():
    chat_prompt = ChatPromptTemplate.from_messages(
        [SystemMessagePromptTemplate.from_template("이 시스템은 천문학 질문에 답변할 수 있습니다."),
         HumanMessagePromptTemplate.from_template("{user_input}"), ])

    messages = chat_prompt.format_messages(user_input="태양계에서 가장 큰 행성은 무엇인가요?")
    print(messages)
    # 출력: [SystemMessage(content='이 시스템은 천문학 질문에 답변할 수 있습니다.'), HumanMessage(content='태양계에서 가장 큰 행성은 무엇인가요?')]


if __name__ == "__main__":
    # template = test_prompt_template()
    # run_prompt_template(template)
    message_prompt_template()
