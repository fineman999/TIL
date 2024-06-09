from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain_community.vectorstores import Chroma
from langchain_community.document_loaders import PyPDFLoader
from langchain_huggingface import HuggingFaceEmbeddings


def split_pdf_text(pdf_path: str) -> list:
    pdf_loader = PyPDFLoader(pdf_path)
    pages = pdf_loader.load_and_split()

    text_splitter = RecursiveCharacterTextSplitter(chunk_size=500, chunk_overlap=50)
    return text_splitter.split_documents(pages)


def generate_text_embeddings(texts: list) -> Chroma:
    model_name = "jhgan/ko-sbert-nli"
    model_kwargs = {"device": "cpu"}
    encode_kwargs = {"normalize_embeddings": True}
    hf = HuggingFaceEmbeddings(
        model_name=model_name,
        model_kwargs=model_kwargs,
        encode_kwargs=encode_kwargs
    )
    return Chroma.from_documents(texts, hf)


def perform_document_search(docsearch: Chroma, prompt: str = "혁신성장 정책금용에 대해서 설명해줘") -> list:
    retriever = docsearch.as_retriever(search_type="mmr", search_kwargs={'k': 3, "fetch_k": 10})
    return retriever.get_relevant_documents(prompt)


def perform_document(docsearch: Chroma):
    return docsearch.as_retriever(search_type="mmr", search_kwargs={'k': 3, "fetch_k": 10})


def main(pdf_path: str = "/[이슈리포트 2022-2호] 혁신성장 정책금융 동향.pdf"):
    texts = split_pdf_text(pdf_path)
    docsearch = generate_text_embeddings(texts)

    prompt = "혁신성장 정책금용에 대해서 설명해줘"
    results = perform_document_search(docsearch, prompt)
    for result in results:
        print(result)


if __name__ == "__main__":
    main()
