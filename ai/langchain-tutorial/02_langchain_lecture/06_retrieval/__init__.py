from config import settings
import tiktoken
from langchain_community.document_loaders import PyPDFLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_chroma import Chroma
from langchain_openai import ChatOpenAI
from langchain.callbacks.streaming_stdout import StreamingStdOutCallbackHandler
from langchain_core.prompts import ChatPromptTemplate

tokenizer = tiktoken.get_encoding("cl100k_base")


def openai_chat():
    params = {
        "temperature": 0.7,  # 생성된 텍스트의 다양성 조정
        "max_tokens": 10,  # 생성할 최대 토큰 수
        "streaming": True,
        "callbacks": [StreamingStdOutCallbackHandler()]  # 스트리밍으로 답변을 받기 위한 콜백
    }

    kwargs = {
        "frequency_penalty": 0.5,  # 높은 값은 반복을 줄이고, 낮은 값은 반복을 증가시킴(0.0 ~ 1.0)
        "presence_penalty": 0.5,  # 높은 값은 중복을 줄이고, 낮은 값은 중복을 증가시킴(0.0 ~ 1.0)
        "stop": "\n",  # 생성된 텍스트 중지 문자
    }

    llm = ChatOpenAI(
        model="gpt-3.5-turbo",
        **params, openai_api_key=settings.API_KEY, model_kwargs=kwargs)
    return llm


def tiktoken_len(text):
    tokens = tokenizer.encode(text)
    return len(tokens)


def retrieve_pdf_text(file_path):
    pdf_loader = PyPDFLoader(file_path)
    pages = pdf_loader.load_and_split()
    return pages


def run_recursive_character_text_splitter(split_page):
    text_splitter = RecursiveCharacterTextSplitter(
        chunk_size=500,  # 텍스트를 나눌 단위
        chunk_overlap=100,  # 나누어진 텍스트의 중복
        length_function=tiktoken_len,  # 텍스트의 길이를 계산할 함수
    )

    return text_splitter.split_documents(split_page)


def get_embeddings():
    model_name = "jhgan/ko-sbert-nli"
    model_kwargs = {"device": "cpu"}
    encode_kwargs = {"normalize_embeddings": True}
    hf = HuggingFaceEmbeddings(
        model_name=model_name,
        model_kwargs=model_kwargs,
        encode_kwargs=encode_kwargs
    )
    return hf


def save_chroma_db(hf, split_text):
    db = Chroma.from_documents(split_text, hf, persist_directory="chroma_db")
    return db


def run_query(llm, vector_db, query):
    # MMR - 다양성 고려 (lambda_mult = 0.5)
    # fetch_k: 총 50개의 연관성있는 문서를 뽑은 다음
    # k: 최대한 다양하게 조합을 한 다음 5개의 문서를 LLM에게 전달
    retriever = vector_db.as_retriever(
        search_type='mmr',
        search_kwargs={'k': 5, 'fetch_k': 50}
    )
    # Prompt
    template = '''Answer the question based only on the following context:
    {context}
    
    Question: {question}
    '''

    prompt = ChatPromptTemplate.from_template(template)

    # 가장 유사한 문서 5개를 찾아서 출력
    docs = retriever.invoke(query)
    print("-" * 50)
    print(docs)
    print("-" * 50)

    chain = prompt | llm

    response = chain.invoke({
        "context": format_docs(docs),
        "question": query
    })
    print(response)


def format_docs(docs):
    return '\n\n'.join([d.page_content for d in docs])


if __name__ == "__main__":
    file_path = "[공고문]2024년청년전세임대1순위입주자수시모집.pdf"
    pages = retrieve_pdf_text(file_path)
    text_splitter = run_recursive_character_text_splitter(pages)
    hf = get_embeddings()
    vectorstore = save_chroma_db(hf, text_splitter)
    llm = openai_chat()
    run_query(llm, vectorstore, "청년은 몇살부터 몇살까지야?")
