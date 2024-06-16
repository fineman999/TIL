from langchain_community.document_loaders import WebBaseLoader, UnstructuredURLLoader, PyPDFLoader, Docx2txtLoader
from langchain_community.document_loaders.csv_loader import CSVLoader


def test_web_base_loader():
    loader = WebBaseLoader("https://n.news.naver.com/article/092/0002307222?sid=105")

    data = loader.load()
    print(data[0].page_content)


def test_unstructured_loader():

    urls = [
        "https://n.news.naver.com/article/092/0002307222?sid=105",
        "https://n.news.naver.com/article/052/0001944792?sid=105",
    ]

    loader = UnstructuredURLLoader(urls=urls)
    data = loader.load()
    print(data[0].page_content)


def test_pdf_document_loader():
    pdf_loader = PyPDFLoader("../05_vetorstores/[공고문]2024년청년전세임대1순위입주자수시모집.pdf")
    pages = pdf_loader.load_and_split() # pdf 문서를 읽어서 페이지별로 나누어 반환
    print(pages[1].page_content)


def test_word_document_loader():
    docx_loader = Docx2txtLoader("sample.docx")
    pages = docx_loader.load() # word 문서를 읽어서 페이지별로 나누어 반환


def test_csv_loader():
    csv_loader = CSVLoader(
        file_path="sample.csv",
        csv_args={"delimiter": ",", "quotechar": '"'},
    )
    data = csv_loader.load()
    print(data[0].page_content)


if __name__ == "__main__":
    test_pdf_document_loader()