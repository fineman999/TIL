from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts import ChatPromptTemplate
from langchain_openai import ChatOpenAI

from config import settings


def chain1():
    prompt1 = ChatPromptTemplate.from_template("translates {korean_word} to English.")

    llm = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY)

    chain = prompt1 | llm | StrOutputParser()

    context = chain.invoke({"korean_word": "미래"})
    print(context)
    # Output: Future
    return chain


def chain2(chain1):
    prompt2 = ChatPromptTemplate.from_template(
        "explain {english_word} using oxford dictionary to me in Korean."
    )

    llm = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY)

    chain = (
        {"english_word": chain1}
        | prompt2
        | llm
        | StrOutputParser()
    )

    context = chain.invoke({"korean_word": "미래"})
    print(context)
    # Output: 미래란 시간, 공간 또는 상황으로서, 현재의 시점에서는 아직 일어나지 않았거나 결정되지 않은 것을 말한다. 미래는 미래에 대한 예측, 계획 또는 희망을 나타낼 때 사용된다.


if __name__ == "__main__":
    runnable_serializable = chain1()
    chain2(runnable_serializable)
