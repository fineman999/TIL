from langchain_openai import OpenAIEmbeddings
from langchain_huggingface import HuggingFaceEmbeddings
from config import settings
from numpy import dot
from numpy.linalg import norm
import numpy as np

def test_openai_embeddings():
    embeddings_model = OpenAIEmbeddings(openai_api_key=settings.API_KEY)
    embeddings = embeddings_model.embed_documents(
        [
            '안녕하세요!',
            '어! 오랜만이에요',
            '이름이 어떻게 되세요?',
            '날씨가 추워요',
            'Hello LLM!'
        ]
    )

    print(len(embeddings), len(embeddings[0]))  # 5, 1536
    # 여기서 1536은 OpenAI의 GPT-3.5-turbo 모델의 임베딩 차원 수입니다. 1536차원을 하나의 행으로 표현한 것입니다.

    embedded_query_q = embeddings_model.embed_query("이 대화에서 언급된 이름은 무엇입니까?")
    embedded_query_a = embeddings_model.embed_query("이 대화에서 언급된 이름은 홍길동입니다.")
    print(len(embedded_query_q), len(embedded_query_a))  # 1536, 1536

    # 코사인 유사도 계산
    print("코사인 유사도")
    print(cos_sim(embedded_query_q, embedded_query_a))  # 0.99999994
    print(cos_sim(embedded_query_q, embeddings[1]))
    print(cos_sim(embedded_query_q, embeddings[3]))
    """
    0.901215141684054
    0.8093448872642331
    0.7737857048011447
    """


def cos_sim(A, B):
    return dot(A, B) / (norm(A) * norm(B))



def hugging_face_embeddings():
    model_name = "BAAI/bge-small-en"
    model_kwargs = {"device": "cpu"}
    encode_kwargs = {"normalize_embeddings": True}
    hf = HuggingFaceEmbeddings(
        model_name=model_name,
        model_kwargs=model_kwargs,
        encode_kwargs=encode_kwargs
    )

    embeddings = hf.embed_documents(
        [
            "Today is a monday",
            "Weathers are good",
            "I am hungry",
            "I am sleepy",
            "I am happy"
        ]
    )

    print(len(embeddings), len(embeddings[0]))  # 5, 768

    embedded_query_q = hf.embed_query("What is the weather today?")
    embedded_query_a = hf.embed_query("The weather is good today.")
    print(len(embedded_query_q), len(embedded_query_a))  # 768, 768

    print("코사인 유사도")
    print(cos_sim(embedded_query_q, embedded_query_a))  # 0.99999994
    print(cos_sim(embedded_query_q, embeddings[1]))
    print(cos_sim(embedded_query_q, embeddings[3]))

def test_hugging_sentences_en():
    model_name = "BAAI/bge-small-en"
    model_kwargs = {"device": "cpu"}
    encode_kwargs = {"normalize_embeddings": True}
    hf = HuggingFaceEmbeddings(
        model_name=model_name,
        model_kwargs=model_kwargs,
        encode_kwargs=encode_kwargs
    )


    sentences = [
        "안녕하세요",
        "제 이름은 홍길동입니다.",
        "이름이 무엇인가요?",
        "랭체인은 유용합니다.",
        "홍길동 아버지의 이름은 홍상직입니다."
    ]
    embeddings = hf.embed_documents(
        sentences
    )

    BGE_query_q_2 = hf.embed_query("홍길동은 아버지를 아버지라 부르지 못하였습니다. 홍길동 아버지의 이름은 무엇입니까?")
    BGE_query_a_2 = hf.embed_query("홍길동의 아버지는 엄했습니다.")

    print("질문: 홍길동은 아버지를 아버지라 부르지 못하였습니다. 홍길동 아버지의 이름은 무엇입니까? ", "-"*100)
    print("홍길동의 아버지는 엄했습니다. \t\t 문장 유사도: ", round(cos_sim(BGE_query_q_2, BGE_query_a_2), 2))
    print(sentences[1] + "\t\t 문장 유사도: ", round(cos_sim(BGE_query_q_2, embeddings[1]), 2))
    print(sentences[4] + "\t\t 문장 유사도: ", round(cos_sim(BGE_query_q_2, embeddings[4]), 2))
    print(sentences[3] + "\t\t 문장 유사도: ", round(cos_sim(BGE_query_q_2, embeddings[3]), 2))
"""
질문: 홍길동은 아버지를 아버지라 부르지 못하였습니다. 홍길동 아버지의 이름은 무엇입니까?  ----------------------------------------------------------------------------------------------------
홍길동의 아버지는 엄했습니다. 		 문장 유사도:  0.96
제 이름은 홍길동입니다.		 문장 유사도:  0.92
홍길동 아버지의 이름은 홍상직입니다.		 문장 유사도:  0.95
랭체인은 유용합니다.		 문장 유사도:  0.89

-> 결과: 홍길동의 아버지는 엄했습니다. 문장이 가장 유사하게 나왔습니다.
분석: 한국어를 잘 번역하지 못하여 적절한 문장 유사도가 높지 않게 나왔습니다.
"""

def test_hugging_sentences_ko():

    hf = HuggingFaceEmbeddings(
        model_name='jhgan/ko-sbert-nli',
        model_kwargs={'device':'cpu'},
        encode_kwargs={'normalize_embeddings':True},
    )

    sentences = [
        "안녕하세요",
        "제 이름은 홍길동입니다.",
        "이름이 무엇인가요?",
        "랭체인은 유용합니다.",
        "홍길동 아버지의 이름은 홍상직입니다."
    ]
    embeddings = hf.embed_documents(
        sentences
    )

    BGE_query_q_2 = hf.embed_query("홍길동은 아버지를 아버지라 부르지 못하였습니다. 홍길동 아버지의 이름은 무엇입니까?")
    BGE_query_a_2 = hf.embed_query("홍길동의 아버지는 엄했습니다.")

    print("질문: 홍길동은 아버지를 아버지라 부르지 못하였습니다. 홍길동 아버지의 이름은 무엇입니까? ", "-"*100)
    print("홍길동의 아버지는 엄했습니다. \t\t 문장 유사도: ", round(cos_sim(BGE_query_q_2, BGE_query_a_2), 2))
    print(sentences[1] + "\t\t 문장 유사도: ", round(cos_sim(BGE_query_q_2, embeddings[1]), 2))
    print(sentences[4] + "\t\t 문장 유사도: ", round(cos_sim(BGE_query_q_2, embeddings[4]), 2))
    print(sentences[3] + "\t\t 문장 유사도: ", round(cos_sim(BGE_query_q_2, embeddings[3]), 2))
"""
질문: 홍길동은 아버지를 아버지라 부르지 못하였습니다. 홍길동 아버지의 이름은 무엇입니까?  ----------------------------------------------------------------------------------------------------
홍길동의 아버지는 엄했습니다. 		 문장 유사도:  0.47
제 이름은 홍길동입니다.		 문장 유사도:  0.54
홍길동 아버지의 이름은 홍상직입니다.		 문장 유사도:  0.61
랭체인은 유용합니다.		 문장 유사도:  0.03

-> 결과: 홍길동 아버지의 이름은 홍상직입니다. 문장이 가장 유사하게 나왔습니다.
분석: 한국어를 잘 번역하여 적절한 문장 유사도가 높게 나왔습니다.
"""
if __name__ == "__main__":

    test_hugging_sentences_en()
    print("-"*100)
    test_hugging_sentences_ko()
