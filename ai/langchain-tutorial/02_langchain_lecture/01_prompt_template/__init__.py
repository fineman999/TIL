from langchain_openai import OpenAI, ChatOpenAI, OpenAIEmbeddings
from config import settings
from langchain_core.prompts import ChatPromptTemplate, PromptTemplate, SystemMessagePromptTemplate, \
    HumanMessagePromptTemplate, FewShotPromptTemplate
from langchain_core.example_selectors import SemanticSimilarityExampleSelector
from langchain_chroma import Chroma
from langchain_core.output_parsers import CommaSeparatedListOutputParser

cooker_template = """
    너는 요리사야. 내가 가진 재료들을 갖고 만들 수 있는 요리를 추천하고, 그 요리의 레시피를 제시해줘.
    내가 가진 재료는 아래와 같아.
    
    <재료>
    {ingredients}
    """


def test_prompt_template_init():
    # 모델 인스턴스를 생성할 때 설정한
    llm = OpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY, max_tokens=1000)

    # 모델 호출
    question = "태양계에서 가장 큰 행성은 무엇인가요?"
    invoke = llm.invoke(input=question)
    print(invoke)


def test_prompt_template():
    # 프롬프트 템플릿을 통해 매개변수 삽입 가능한 문자열로 변환
    string_prompt = PromptTemplate.from_template("tell me a joke about {subject}")

    # 매개변수 삽입한 결과를 string_prompt_value에 할당
    string_prompt_value = string_prompt.format(subject="football")

    # string_prompt_value 출력
    print(string_prompt_value)
    # 출력: tell me a joke about football


def test_prompt_template_cook():
    template = """
    너는 요리사야. 내가 가진 재료들을 갖고 만들 수 있는 요리를 추천하고, 그 요리의 레시피를 제시해줘.
    내가 가진 재료는 아래와 같아.
    
    <재료>
    {ingredients}
    """

    prompt_template = ChatPromptTemplate.from_template(template)

    ingredients = """
    '양파', '계란', '사과', '빵'
    """

    llm = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY, max_tokens=1000, temperature=0)

    chain = prompt_template | llm

    invoke = chain.invoke({"ingredients": ingredients})
    print(invoke.content)


def test_prompt_template_chat():
    chatgpt = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY, max_tokens=1000, temperature=0)

    # ChatGPT에게 역할 부여
    system_message_prompt = SystemMessagePromptTemplate.from_template(cooker_template)

    # 사용자가 입력할 매개변수 template을 선언한다.
    human_template = "{ingredients}"
    human_template = HumanMessagePromptTemplate.from_template(human_template)

    # ChatPromptTemplate에 system message와 human message를 연결한다.
    template = ChatPromptTemplate.from_messages([system_message_prompt, human_template])

    # ChatPromptTemplate을 ChatGPT에게 전달한다.
    chain = template | chatgpt

    # 사용자가 입력한 매개변수를 전달한다.
    ingredients = """
    '양파', '계란', '사과', '빵'
    """

    # ChatGPT에게 요리를 요청한다.
    invoke = chain.invoke({"ingredients": ingredients})

    # 요리 결과를 출력한다.
    print(invoke.content)


def test_few_shot():
    examples = [
        {
            "question": "아이유로 삼행시 만들어줘",
            "answer":
                """
아: 아이유는
이: 이쁘고
유: 유명하다
            """
        },
        {
            "question": "김민수로 삼행시 만들어줘",
            "answer":
                """
김: 김치는 맛있다.
민: 민달팽이도 좋아하는 김치!
수: 수박도 맛있지만 김치가 최고!
            """
        }
    ]

    example_prompt = PromptTemplate.from_template("Question:\n{question}\nAnswer:\n{answer}")

    # print(example_prompt.format(**examples[0])) # **examples[0]은 examples[0]의 key와 value를 unpacking하여 전달

    prompt = FewShotPromptTemplate(
        examples=examples,
        example_prompt=example_prompt,
        suffix="Question:\n{question}\nAnswer:",
        input_variables=["question"],
    )

    # print(prompt.format(question="호날두로 삼행시 만들어줘"))

    llm = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY, max_tokens=1000, temperature=0)

    chain = prompt | llm

    invoke = chain.invoke({"question": "호날두로 삼행시 만들어줘"})
    print(invoke.content)
    # 출력:
    # 호: 호날두는
    # 날: 날렵한 발로
    # 두: 두둥실 뛰어다니네.


def test_semantic_similarity():
    example_prompt = PromptTemplate.from_template("Input:\n{input}\nOutput:\n{output}")
    examples = [
        {"input": "행복", "output": "슬픔"},
        {"input": "사랑", "output": "이별"},
        {"input": "평화", "output": "전쟁"},
        {"input": "불안", "output": "안정"},
        {"input": "긴 기차", "output": "짧은 기차"},
        {"input": "큰 공", "output": "작은 공"}
    ]

    example_selector = SemanticSimilarityExampleSelector.from_examples(
        # 여기에는 선택 가능한 예시 목록이 있습니다.
        examples,
        # 여기에는 의미적 유사성을 측정하는 데 사용되는 임베딩을 생성하는 임베딩 클래스가 있습니다.
        OpenAIEmbeddings(openai_api_key=settings.API_KEY),
        # 여기에는 임베딩을 저장하고 유사성 검색을 수행하는 데 사용되는 VectorStore 클래스가 있습니다.
        Chroma,
        # 이것은 생성할 예시의 수입니다.
        k=1,
    )

    similar_prompt = FewShotPromptTemplate(
        example_selector=example_selector,
        example_prompt=example_prompt,
        prefix="주어진 입력에 대해 반대의 의미를 가진 단어를 찾아주세요.",
        suffix="Input:\n{input}\nOutput:",
        input_variables=["input"],
    )

    llm = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY, max_tokens=1000, temperature=0)

    chain = similar_prompt | llm

    invoke = chain.invoke({"input": "무서운"})
    print(invoke.content)
    # 출력: 평화로운

    invoke = chain.invoke({"input": "작은 비행기"})
    print(invoke.content)
    # 출력: 큰 비행기


def test_output_parser():
    parser = CommaSeparatedListOutputParser()

    format_instructions = parser.get_format_instructions()
    print(format_instructions)
    # 출력: Your response should be a list of comma separated values, eg: `foo, bar, baz` or `foo,bar,baz`

    prompt = PromptTemplate(template="{주제} 5개를 추천해줘. \n{format_instructions}",
                            input_variables=["주제"],  # 가변적으로 사용할 변수를 선언 - 사용자가 입력할 변수
                            partial_variables={"format_instructions": format_instructions}
                            # 고정적으로 instruction을 사용할 변수를 선언
                            )
    model = ChatOpenAI(model="gpt-3.5-turbo", openai_api_key=settings.API_KEY, temperature=0)

    chain = prompt | model | parser

    response = chain.invoke({"주제": "영화"})
    print(response)
    # output_parse로 인해 쉼표로 구분된 리스트로 출력됨
    # 출력: ['인셉션', '라라랜드', '쇼생크 탈출', '어바웃 타임', '쇼생크 탈출']


if __name__ == "__main__":
    test_output_parser()
