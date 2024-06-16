import tiktoken
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_chroma import Chroma
from langchain_community.vectorstores import FAISS
from langchain_community.document_loaders import PyPDFLoader

tokenizer = tiktoken.get_encoding("cl100k_base")


# 주어진 텍스트가 몇 개의 토큰으로 구성되어 있는지 계산
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


def run_embeddings():
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


def run_query(vector_db, query):
    query_embedding = vector_db.similarity_search(query)
    print(query_embedding[0].page_content)
    length = tiktoken_len(query_embedding[0].page_content)
    print(length)
    return query_embedding


def load_chroma_db_and_query(hf, query):
    db = Chroma(persist_directory="chroma_db", embedding_function=hf)
    docs = db.similarity_search(query)
    print(docs[0].page_content)
    return db


def similarity_search(db, query):
    #  k: 가장 유사한 상위 3개의 문서를 반환
    score = db.similarity_search_with_score(query, k=3)
    # print("가장 유사한 문서: \n\n {}\n\n".format(score[0][0].page_content))
    # print("문서 유사도: \n\n {}\n\n".format(score[0][1]))

    for i, (doc, sim) in enumerate(score):
        print(f"가장 유사한 문서 {i + 1}: \n\n {doc.page_content}\n\n")
        print(f"문서 유사도: {sim}\n\n")
        print(f"메타 데이터: {doc.metadata}\n\n")
        print("-" * 50)


def create_faiss(docs, hf):
    db = FAISS.from_documents(docs, hf)
    return db


def save_faiss_index(db):
    db.save_local("faiss_index")


def load_faiss_index(hf):
    db = FAISS.load_local("faiss_index", hf, allow_dangerous_deserialization=True)
    return db


def max_marginal_relevance_search(db, query):
    # k=3와 fetch_k=10을 설정하여, 상위 10개의 유사한 문서 중에서 서로 다른 정보를 제공하는 3개의 문서를 선택합니다.
    # lambda_mult는 문서의 중요도와 유사도 사이의 균형을 조정합니다.
    score = db.max_marginal_relevance_search(query, k=3, fetch_k=10, lambda_mult=0.3)
    print(score)


if __name__ == "__main__":
    # pages = retrieve_pdf_text("[공고문]2024년청년전세임대1순위입주자수시모집.pdf")
    # texts = run_recursive_character_text_splitter(pages)
    hf = run_embeddings()
    # db = create_faiss(texts, hf)
    db = load_faiss_index(hf)
    query = "청년은 몇살부터 몇살까지인가요?"
    max_marginal_relevance_search(db, query)

    # save_faiss_index(db)

    ## Chroma DB ##
    # query = "입주 대상자는 누구야?"
    # db = load_chroma_db_and_query(hf, query)
    # similarity_search(db, query)
    # query_embedding = run_query(db, query)
