from langchain_text_splitters import CharacterTextSplitter, RecursiveCharacterTextSplitter, Language
import tiktoken


def test_character_text_splitter():
    with open('state_of_the_union.txt', 'r') as file:
        text = file.read()

    text_splitter = CharacterTextSplitter(
        separator='\n\n',  # 구분자
        chunk_size=1000,  # 텍스트를 나눌 단위
        chunk_overlap=100,  # 나누어진 텍스트의 중복
        length_function=len,  # 텍스트의 길이를 계산할 함수
    )

    texts = text_splitter.split_text(text)
    # print(texts[0])
    # print("-" * 50)
    # print(texts[1])
    # print("-" * 50)
    # print(texts[2])

    char_list = []
    for text in texts:
        char_list.append(len(text))
    print(char_list)


def test_recursive_character_text_splitter():
    with open('state_of_the_union.txt', 'r') as file:
        text = file.read()

    text_splitter = RecursiveCharacterTextSplitter(
        chunk_size=1000,  # 텍스트를 나눌 단위
        chunk_overlap=100,  # 나누어진 텍스트의 중복
        length_function=len,  # 텍스트의 길이를 계산할 함수
    )

    texts = text_splitter.split_text(text)
    print(texts[0])
    print("-" * 50)
    print(texts[1])
    print("-" * 50)
    print(texts[2])

    char_list = []
    for text in texts:
        char_list.append(len(text))
    print(char_list)


def test_language():
    PYTHON_CODE = """
    def hello_world():
        print("Hello, World!")
        
    hello_world()
    """

    python_splitter = RecursiveCharacterTextSplitter.from_language(
        language=Language.PYTHON,
        chunk_size=50,
        chunk_overlap=0
    )

    texts = python_splitter.create_documents([PYTHON_CODE])
    print(texts)


# cl100k_base: gpt 모델들에 대해서 인코딩할때 사용되는 토큰
tokenizer = tiktoken.get_encoding("cl100k_base")


def tiktoken_len(text):
    tokens = tokenizer.encode(text)
    return len(tokens)


def test_tiktoken():
    with open('state_of_the_union.txt', 'r') as file:
        text = file.read()
    text_splitter = RecursiveCharacterTextSplitter.from_tiktoken_encoder(
        chunk_size=1000,
        chunk_overlap=200,
        encoding_name='cl100k_base'
    )
    texts = text_splitter.split_text(text)

    token_list = []
    for text in texts:
        token_list.append(tiktoken_len(text))
    print(token_list)


if __name__ == "__main__":
    test_tiktoken()
