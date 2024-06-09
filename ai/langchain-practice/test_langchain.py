# test_langchain.py

from openai_utils import get_google_gemini_model


def text_generation():
    llm = get_google_gemini_model()
    # Assuming the model has a method called 'generate' for text generation
    response = llm.invoke("네이버에 대해 알려줘")
    print(response)


if __name__ == "__main__":
    text_generation()
