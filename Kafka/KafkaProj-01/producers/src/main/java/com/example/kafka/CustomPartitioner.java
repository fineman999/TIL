package com.example.kafka;

import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.internals.StickyPartitionCache;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomPartitioner implements Partitioner {
  private static final Logger logger = LoggerFactory.getLogger(CustomPartitioner.class.getName());
  private final StickyPartitionCache stickyPartitionCache = new StickyPartitionCache();
  private String specialKeyName;

  /** properties에 등록한 정보들 */
  @Override
  public void configure(final Map<String, ?> configs) {
    specialKeyName = configs.get("custom.special.key").toString();
  }

  @Override
  public int partition(
      final String topic,
      final Object key,
      final byte[] keyBytes,
      final Object vale,
      final byte[] valueBytes,
      final Cluster cluster) {
    final List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
    int numPartitions = partitionInfos.size();
    int numSpecialPartitions = numPartitions / 2; // 2개의 파티션으로 나누기

    int partitionIndex = 0;

    if (keyBytes == null) {
      //            return stickyPartitionCache.partition(topic, cluster);
      throw new InvalidRecordException("key is null");
    }

    if (key.equals(specialKeyName)) {
      partitionIndex =
          Utils.toPositive(Utils.murmur2(valueBytes) % numSpecialPartitions); // 0 또는 1 반환

    } else {
      partitionIndex =
          Utils.toPositive(Utils.murmur2(keyBytes) % (numPartitions - numSpecialPartitions))
              + numSpecialPartitions; // 2 또는 3 반환
    }
    logger.info("key: {}, partitionIndex: {}", key, partitionIndex);
    return partitionIndex;
  }

  @Override
  public void close() {}
}
