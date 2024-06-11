from langchain_core.prompts import PromptTemplate
from langchain_openai import ChatOpenAI
from langchain_core.output_parsers import StrOutputParser
from config import settings


def test_template():
    # name과 age라는 두 개의 변수를 사용하는 프롬프트 템플릿을 정의
    # PromptTemplate 인스턴스 생성
    prompt_template = PromptTemplate.from_template("안녕하세요, 제 이름은 {name}이고, 나이는 {age}살입니다.")

    # PromptTemplate 인스턴스를 사용하여 변수를 채워넣은 프롬프트 생성
    filled_prompt = prompt_template.format(name="홍길동", age=20)

    print(filled_prompt)
    # 출력: 안녕하세요, 제 이름은 홍길동이고, 나이는 20살입니다.
    return prompt_template


# 프롬프트 템플릿 간의 결합
def combining_between_prompt_template(prompt_template1):
    combined_prompt = (
            prompt_template1
            + PromptTemplate.from_template("\n\n 아버지를 아버지라 부르지 못하고")
            + "\n\n{language}로 번역해주세요."
    )
    print(combined_prompt)
    # 출력: input_variables=['age', 'language', 'name'] template='안녕하세요, 제 이름은 {name}이고, 나이는 {age}살입니다.\n\n 아버지를 아버지라 부르지 못하고\n\n{language}로 번역해주세요.'
    return combined_prompt


def run_chain(combined_template):
    llm = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY)
    output_parser = StrOutputParser()
    chain = combined_template | llm | output_parser
    invoke = chain.invoke({"name": "홍길동", "age": 20, "language": "영어"})
    print(invoke)
    # 출력: I cannot call my father "father."


if __name__ == "__main__":
    prompt_template = test_template()
    template = combining_between_prompt_template(prompt_template)
    run_chain(template)
