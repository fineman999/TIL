from langchain.prompts import ChatPromptTemplate
from langchain.schema.runnable import RunnableMap
import pdf_process
import openai_utils


def create_template():
    template = """
    Answer the Question as based only on the following context:
    {context}
    Question: {question}
    """
    return ChatPromptTemplate.from_template(template)


def run_prompt(pdf_path: str = "./[이슈리포트 2022-2호] 혁신성장 정책금융 동향.pdf",
               prompt=None,
               gemini=None):
    texts = pdf_process.split_pdf_text(pdf_path)
    docsearch = pdf_process.generate_text_embeddings(texts)
    retriever = pdf_process.perform_document(docsearch)
    chain = RunnableMap({"context": lambda x: retriever.get_relevant_documents(x["question"]),
                         "question": lambda x: x["question"]}) | prompt | gemini
    invoke = chain.invoke({"question": "혁신성장 정책금융 동향에 대해 설명해줘"})
    print(invoke)
    return invoke.content


def main():
    prompt = create_template()
    gemini = openai_utils.get_google_gemini_model()
    answer = run_prompt(prompt=prompt, gemini=gemini)
    print("-----------------")
    print(answer)


if __name__ == "__main__":
    main()
