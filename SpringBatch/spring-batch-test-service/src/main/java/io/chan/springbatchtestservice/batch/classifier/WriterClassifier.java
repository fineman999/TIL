package io.chan.springbatchtestservice.batch.classifier;

import io.chan.springbatchtestservice.batch.domain.ApiRequestVO;
import java.util.Map;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

/**
 * 입력된 객체를 분류하고 그에 따른 결과를 반환하는 역할을 한다. C와 T는 제네릭 타입을 나타냅니다. C는 classify 메소드의 입력 타입을, T는 classify
 * 메소드의 반환 타입을 나타냅니다. 이를 통해 Classifier 인터페이스를 구현하는 클래스에서는 어떤 타입의 객체를 분류하고, 어떤 타입의 결과를 반환할 것인지를 유연하게
 * 정의할 수 있습니다
 */
public class WriterClassifier<C, T> implements Classifier<C, T> {

  // ProductVO의 type에 따라 ItemProcessor<ProductVO, ApiRequestVO> 타입의 객체를 반환할 수 있습니다.
  private final Map<String, ItemWriter<ApiRequestVO>> writerMap;

  public WriterClassifier(final Map<String, ItemWriter<ApiRequestVO>> writerMap) {
    this.writerMap = writerMap;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T classify(final C classifiable) {
    if (classifiable instanceof final ApiRequestVO apiRequestVO) {
      return (T) writerMap.get(apiRequestVO.productVO().getType());
    }
    throw new IllegalArgumentException("Unknown type: " + classifiable);
  }
}
