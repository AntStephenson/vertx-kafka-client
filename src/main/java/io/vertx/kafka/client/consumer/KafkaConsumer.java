/*
 * Copyright 2016 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vertx.kafka.client.consumer;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.ReadStream;
import io.vertx.kafka.client.common.KafkaClientOptions;
import io.vertx.kafka.client.common.PartitionInfo;
import io.vertx.kafka.client.common.TopicPartition;
import io.vertx.kafka.client.consumer.impl.KafkaConsumerImpl;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.Deserializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Vert.x Kafka consumer.
 * <p>
 * You receive Kafka records by providing a {@link KafkaConsumer#handler(Handler)}. As messages arrive the handler
 * will be called with the records.
 * <p>
 * The {@link #pause()} and {@link #resume()} provides global control over reading the records from the consumer.
 * <p>
 * The {@link #pause(Set)} and {@link #resume(Set)} provides finer grained control over reading records
 * for specific Topic/Partition, these are Kafka's specific operations.
 */
@VertxGen
public interface KafkaConsumer<K, V> extends ReadStream<KafkaConsumerRecord<K, V>> {

  /**
   * Create a new KafkaConsumer instance from a native {@link Consumer}.
   *
   * @param vertx Vert.x instance to use
   * @param consumer the Kafka consumer to wrap
   * @return  an instance of the KafkaConsumer
   */
  @GenIgnore
  static <K, V> KafkaConsumer<K, V> create(Vertx vertx, Consumer<K, V> consumer) {
    KafkaReadStream<K, V> stream = KafkaReadStream.create(vertx, consumer);
    return new KafkaConsumerImpl<>(stream);
  }

  /**
   * Create a new KafkaConsumer instance
   *
   * @param vertx Vert.x instance to use
   * @param config Kafka consumer configuration
   * @return  an instance of the KafkaConsumer
   */
  static <K, V> KafkaConsumer<K, V> create(Vertx vertx, Map<String, String> config) {
    KafkaReadStream<K, V> stream = KafkaReadStream.create(vertx, new HashMap<>(config));
    return new KafkaConsumerImpl<>(stream).registerCloseHook();
  }

  /**
   * Create a new KafkaConsumer instance
   *
   * @param vertx Vert.x instance to use
   * @param config  Kafka consumer configuration
   * @param keyType class type for the key deserialization
   * @param valueType class type for the value deserialization
   * @return  an instance of the KafkaConsumer
   */
  static <K, V> KafkaConsumer<K, V> create(Vertx vertx, Map<String, String> config,
                                           Class<K> keyType, Class<V> valueType) {
    KafkaReadStream<K, V> stream = KafkaReadStream.create(vertx, new HashMap<>(config), keyType, valueType);
    return new KafkaConsumerImpl<>(stream).registerCloseHook();
  }

  /**
   * Create a new KafkaConsumer instance
   *
   * @param vertx Vert.x instance to use
   * @param config  Kafka consumer configuration
   * @param keyDeserializer key deserializer
   * @param valueDeserializer value deserializer
   * @return  an instance of the KafkaConsumer
   */
  @GenIgnore
  static <K, V> KafkaConsumer<K, V> create(Vertx vertx, Map<String, String> config,
                                           Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
    KafkaReadStream<K, V> stream = KafkaReadStream.create(vertx, new HashMap<>(config), keyDeserializer, valueDeserializer);
    return new KafkaConsumerImpl<>(stream).registerCloseHook();
  }

  /**
   * Create a new KafkaConsumer instance
   *
   * @param vertx Vert.x instance to use
   * @param options Kafka consumer options
   * @return  an instance of the KafkaConsumer
   */
  static <K, V> KafkaConsumer<K, V> create(Vertx vertx, KafkaClientOptions options) {
    KafkaReadStream<K, V> stream = KafkaReadStream.create(vertx, options);
    return new KafkaConsumerImpl<>(stream).registerCloseHook();
  }

  /**
   * Create a new KafkaConsumer instance
   *
   * @param vertx Vert.x instance to use
   * @param options  Kafka consumer options
   * @param keyType class type for the key deserialization
   * @param valueType class type for the value deserialization
   * @return  an instance of the KafkaConsumer
   */
  static <K, V> KafkaConsumer<K, V> create(Vertx vertx, KafkaClientOptions options,
                                           Class<K> keyType, Class<V> valueType) {
    KafkaReadStream<K, V> stream = KafkaReadStream.create(vertx, options, keyType, valueType);
    return new KafkaConsumerImpl<>(stream).registerCloseHook();
  }

  /**
   * Create a new KafkaConsumer instance
   *
   * @param vertx Vert.x instance to use
   * @param options  Kafka consumer options
   * @param keyDeserializer key deserializer
   * @param valueDeserializer value deserializer
   * @return  an instance of the KafkaConsumer
   */
  @GenIgnore
  static <K, V> KafkaConsumer<K, V> create(Vertx vertx, KafkaClientOptions options,
                                           Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
    KafkaReadStream<K, V> stream = KafkaReadStream.create(vertx, options, keyDeserializer, valueDeserializer);
    return new KafkaConsumerImpl<>(stream).registerCloseHook();
  }

  /**
   * Create a new KafkaConsumer instance
   *
   * @param vertx Vert.x instance to use
   * @param config Kafka consumer configuration
   * @return  an instance of the KafkaConsumer
   */
  @GenIgnore
  static <K, V> KafkaConsumer<K, V> create(Vertx vertx, Properties config) {
    KafkaReadStream<K, V> stream = KafkaReadStream.create(vertx, config);
    return new KafkaConsumerImpl<>(stream).registerCloseHook();
  }

  /**
   * Create a new KafkaConsumer instance
   *
   * @param vertx Vert.x instance to use
   * @param config Kafka consumer configuration
   * @param keyType class type for the key deserialization
   * @param valueType class type for the value deserialization
   * @return  an instance of the KafkaConsumer
   */
  @GenIgnore
  static <K, V> KafkaConsumer<K, V> create(Vertx vertx, Properties config,
                                           Class<K> keyType, Class<V> valueType) {
    KafkaReadStream<K, V> stream = KafkaReadStream.create(vertx, config, keyType, valueType);
    return new KafkaConsumerImpl<>(stream).registerCloseHook();
  }

  /**
   * Create a new KafkaConsumer instance
   *
   * @param vertx Vert.x instance to use
   * @param config Kafka consumer configuration
   * @param keyDeserializer key deserializer
   * @param valueDeserializer value deserializer
   * @return  an instance of the KafkaConsumer
   */
  @GenIgnore
  static <K, V> KafkaConsumer<K, V> create(Vertx vertx, Properties config,
                                           Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
    KafkaReadStream<K, V> stream = KafkaReadStream.create(vertx, config, keyDeserializer, valueDeserializer);
    return new KafkaConsumerImpl<>(stream).registerCloseHook();
  }

  @Fluent
  @Override
  KafkaConsumer<K, V> exceptionHandler(Handler<Throwable> handler);

  @Fluent
  @Override
  KafkaConsumer<K, V> handler(Handler<KafkaConsumerRecord<K, V>> handler);

  @Fluent
  @Override
  KafkaConsumer<K, V> pause();

  @Fluent
  @Override
  KafkaConsumer<K, V> resume();

  @Override
  KafkaConsumer<K, V> fetch(long amount);

  @Fluent
  @Override
  KafkaConsumer<K, V> endHandler(Handler<Void> endHandler);

  /**
   * Returns the current demand.
   *
   * <ul>
   *   <i>If the stream is in <i>flowing</i> mode will return {@link Long#MAX_VALUE}.</i>
   *   <li>If the stream is in <i>fetch</i> mode, will return the current number of elements still to be delivered or 0 if paused.</li>
   * </ul>
   *
   * @return current demand
   */
  long demand();

  /**
   * Subscribe to the given topic to get dynamically assigned partitions.
   * <p>
   * Due to internal buffering of messages, when changing the subscribed topic
   * the old topic may remain in effect
   * (as observed by the {@linkplain #handler(Handler)} record handler})
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will only see messages
   * consistent with the new topic.
   *
   * @param topic  topic to subscribe to
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> subscribe(String topic);

  /**
   * Subscribe to the given list of topics to get dynamically assigned partitions.
   * <p>
   * Due to internal buffering of messages, when changing the subscribed topics
   * the old set of topics may remain in effect
   * (as observed by the {@linkplain #handler(Handler)} record handler})
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will only see messages
   * consistent with the new set of topics.
   *
   * @param topics  topics to subscribe to
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> subscribe(Set<String> topics);

  /**
   * Subscribe to all topics matching specified pattern to get dynamically assigned partitions.
   * <p>
   * Due to internal buffering of messages, when changing the subscribed topics
   * the old set of topics may remain in effect
   * (as observed by the {@linkplain #handler(Handler)} record handler})
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will only see messages
   * consistent with the new set of topics.
   *
   * @param pattern  Pattern to subscribe to
   * @return a {@code Future} completed with the operation result
   */
  @GenIgnore(GenIgnore.PERMITTED_TYPE)
  Future<Void> subscribe(Pattern pattern);

  /**
   * Manually assign a partition to this consumer.
   * <p>
   * Due to internal buffering of messages, when reassigning
   * the old partition may remain in effect
   * (as observed by the {@linkplain #handler(Handler)} record handler)}
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will only see messages
   * consistent with the new partition.
   *
   * @param topicPartition  partition which want assigned
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> assign(TopicPartition topicPartition);

  /**
   * Manually assign a list of partition to this consumer.
   * <p>
   * Due to internal buffering of messages, when reassigning
   * the old set of partitions may remain in effect
   * (as observed by the {@linkplain #handler(Handler)} record handler)}
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will only see messages
   * consistent with the new set of partitions.
   *
   * @param topicPartitions  partitions which want assigned
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> assign(Set<TopicPartition> topicPartitions);

  /**
   * Get the set of partitions currently assigned to this consumer.
   *
   * @return a future notified on operation completed
   */
  Future<Set<TopicPartition>> assignment();

  /**
   * Get metadata about partitions for all topics that the user is authorized to view.
   *
   * @return a future notified on operation completed
   */
  @GenIgnore
  Future<Map<String, List<PartitionInfo>>> listTopics();

  /**
   * Unsubscribe from topics currently subscribed with subscribe.
   *
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> unsubscribe();

  /**
   * Get the current subscription.
   *
   * @return a future notified  on operation completed
   */
  Future<Set<String>> subscription();

  /**
   * Suspend fetching from the requested partition.
   * <p>
   * Due to internal buffering of messages,
   * the {@linkplain #handler(Handler) record handler} will
   * continue to observe messages from the given {@code topicPartition}
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will not see messages
   * from the given {@code topicPartition}.
   *
   * @param topicPartition topic partition from which suspend fetching
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> pause(TopicPartition topicPartition);

  /**
   * Suspend fetching from the requested partitions.
   * <p>
   * Due to internal buffering of messages,
   * the {@linkplain #handler(Handler) record handler} will
   * continue to observe messages from the given {@code topicPartitions}
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will not see messages
   * from the given {@code topicPartitions}.
   *
   * @param topicPartitions topic partition from which suspend fetching
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> pause(Set<TopicPartition> topicPartitions);

  /**
   * Get the set of partitions that were previously paused by a call to pause(Set).
   *
   * @return a future notified on operation completed
   */
  Future<Set<TopicPartition>> paused();

  /**
   * Resume specified partition which have been paused with pause.
   *
   * @param topicPartition topic partition from which resume fetching
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> resume(TopicPartition topicPartition);

  /**
   * Resume specified partitions which have been paused with pause.
   *
   * @param topicPartitions topic partition from which resume fetching
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> resume(Set<TopicPartition> topicPartitions);

  /**
   * Set the handler called when topic partitions are revoked to the consumer
   *
   * @param handler handler called on revoked topic partitions
   * @return  current KafkaConsumer instance
   */
  @Fluent
  KafkaConsumer<K, V> partitionsRevokedHandler(Handler<Set<TopicPartition>> handler);

  /**
   * Set the handler called when topic partitions are assigned to the consumer
   *
   * @param handler handler called on assigned topic partitions
   * @return  current KafkaConsumer instance
   */
  @Fluent
  KafkaConsumer<K, V> partitionsAssignedHandler(Handler<Set<TopicPartition>> handler);

  /**
   * Overrides the fetch offsets that the consumer will use on the next poll.
   * <p>
   * Due to internal buffering of messages,
   * the {@linkplain #handler(Handler) record handler} will
   * continue to observe messages fetched with respect to the old offset
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will only see messages
   * consistent with the new offset.
   *
   * @param topicPartition  topic partition for which seek
   * @param offset  offset to seek inside the topic partition
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> seek(TopicPartition topicPartition, long offset);


  /**
   * Overrides the fetch offsets that the consumer will use on the next poll.
   * <p>
   * Due to internal buffering of messages,
   * the {@linkplain #handler(Handler) record handler} will
   * continue to observe messages fetched with respect to the old offset
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will only see messages
   * consistent with the new offset.
   *
   * @param topicPartition  topic partition for which seek
   * @param offsetAndMetadata  offset to seek inside the topic partition
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> seek(TopicPartition topicPartition, OffsetAndMetadata offsetAndMetadata);

  /**
   * Seek to the first offset for each of the given partition.
   * <p>
   * Due to internal buffering of messages,
   * the {@linkplain #handler(Handler) record handler} will
   * continue to observe messages fetched with respect to the old offset
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will only see messages
   * consistent with the new offset.
   *
   * @param topicPartition topic partition for which seek
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> seekToBeginning(TopicPartition topicPartition);

  /**
   * Seek to the first offset for each of the given partitions.
   * <p>
   * Due to internal buffering of messages,
   * the {@linkplain #handler(Handler) record handler} will
   * continue to observe messages fetched with respect to the old offset
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will only see messages
   * consistent with the new offset.
   *
   * @param topicPartitions topic partition for which seek
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> seekToBeginning(Set<TopicPartition> topicPartitions);

  /**
   * Seek to the last offset for each of the given partition.
   * <p>
   * Due to internal buffering of messages,
   * the {@linkplain #handler(Handler) record handler} will
   * continue to observe messages fetched with respect to the old offset
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will only see messages
   * consistent with the new offset.
   *
   * @param topicPartition topic partition for which seek
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> seekToEnd(TopicPartition topicPartition);

  /**
   * Seek to the last offset for each of the given partitions.
   * <p>
   * Due to internal buffering of messages,
   * the {@linkplain #handler(Handler) record handler} will
   * continue to observe messages fetched with respect to the old offset
   * until some time <em>after</em> the given {@code completionHandler}
   * is called. In contrast, the once the given {@code completionHandler}
   * is called the {@link #batchHandler(Handler)} will only see messages
   * consistent with the new offset.
   *
   * @param topicPartitions topic partition for which seek
   * @return a {@code Future} completed with the operation result
   */
  Future<Void> seekToEnd(Set<TopicPartition> topicPartitions);


  /**
   * Commit current offsets for all the subscribed list of topics and partition.
   */
  Future<Void> commit();

  /**
   * Commit the specified offsets for the specified list of topics and partitions to Kafka.
   *
   * @param offsets offsets list to commit
   */
  @GenIgnore
  Future<Map<TopicPartition, OffsetAndMetadata>> commit(Map<TopicPartition, OffsetAndMetadata> offsets);

  /**
   * Get the last committed offset for the given partition (whether the commit happened by this process or another).
   *
   * @param topicPartition  topic partition for getting last committed offset
   * @return a future notified on operation completed
   */
  Future<OffsetAndMetadata> committed(TopicPartition topicPartition);

  /**
   * Get metadata about the partitions for a given topic.
   *
   * @param topic topic partition for which getting partitions info
   * @return a future notified on operation completed
   */
  Future<List<PartitionInfo>> partitionsFor(String topic);

  /**
   * Set the handler to be used when batches of messages are fetched
   * from the Kafka server. Batch handlers need to take care not to block
   * the event loop when dealing with large batches. It is better to process
   * records individually using the {@link #handler(Handler) record handler}.
   * @param handler handler called when batches of messages are fetched
   * @return current KafkaConsumer instance
   */
  @Fluent
  KafkaConsumer<K, V> batchHandler(Handler<KafkaConsumerRecords<K, V>> handler);

  /**
   * Close the consumer
   */
  Future<Void> close();

  /**
   * Get the offset of the next record that will be fetched (if a record with that offset exists).
   *
   * @param partition The partition to get the position for
   * @return a future notified on operation completed
   */
  Future<Long> position(TopicPartition partition);

  /**
   * Look up the offsets for the given partitions by timestamp. Note: the result might be empty in case
   * for the given timestamp no offset can be found -- e.g., when the timestamp refers to the future
   * @param topicPartitionTimestamps A map with pairs of (TopicPartition, Timestamp).
   * @return a future notified on operation completed
   */
  @GenIgnore
  Future<Map<TopicPartition, OffsetAndTimestamp>> offsetsForTimes(Map<TopicPartition, Long> topicPartitionTimestamps);

  /**
   * Look up the offset for the given partition by timestamp. Note: the result might be null in case
   * for the given timestamp no offset can be found -- e.g., when the timestamp refers to the future
   * @param topicPartition TopicPartition to query.
   * @param timestamp Timestamp to be used in the query.
   * @return a future notified on operation completed
   */
  Future<OffsetAndTimestamp> offsetsForTimes(TopicPartition topicPartition, Long timestamp);

  /**
   * Get the first offset for the given partitions.
   * @param topicPartitions the partitions to get the earliest offsets.
   * @return a future notified on operation completed
   */
  @GenIgnore
  Future<Map<TopicPartition, Long>> beginningOffsets(Set<TopicPartition> topicPartitions);

  /**
   * Get the first offset for the given partitions.
   * @param topicPartition the partition to get the earliest offset.
   * @return a future notified on operation completed
   */
  Future<Long> beginningOffsets(TopicPartition topicPartition);

  /**
   * Get the last offset for the given partitions. The last offset of a partition is the offset
   * of the upcoming message, i.e. the offset of the last available message + 1.
   * @param topicPartitions the partitions to get the end offsets.
   * @return a future notified on operation completed
   */
  @GenIgnore
  Future<Map<TopicPartition, Long>> endOffsets(Set<TopicPartition> topicPartitions);

  /**
   * Get the last offset for the given partition. The last offset of a partition is the offset
   * of the upcoming message, i.e. the offset of the last available message + 1.
   * @param topicPartition the partition to get the end offset.
   * @return a future notified on operation completed
   */
  Future<Long> endOffsets(TopicPartition topicPartition);

  /**
   * @return  underlying the {@link KafkaReadStream} instance
   */
  @GenIgnore
  KafkaReadStream<K, V> asStream();

  /**
   * @return the underlying consumer
   */
  @GenIgnore
  Consumer<K, V> unwrap();

  /**
   * Sets the poll timeout for the underlying native Kafka Consumer. Defaults to 1000ms.
   * Setting timeout to a lower value results in a more 'responsive' client, because it will block for a shorter period
   * if no data is available in the assigned partition and therefore allows subsequent actions to be executed with a shorter
   * delay. At the same time, the client will poll more frequently and thus will potentially create a higher load on the Kafka Broker.
   *
   * @param timeout The time, spent waiting in poll if data is not available in the buffer.
   * If 0, returns immediately with any records that are available currently in the native Kafka consumer's buffer,
   * else returns empty. Must not be negative.
   */
  @Fluent
  @GenIgnore(GenIgnore.PERMITTED_TYPE)
  KafkaConsumer<K, V> pollTimeout(Duration timeout);

  /**
   * Executes a poll for getting messages from Kafka.
   *
   * @param timeout The maximum time to block (must not be greater than {@link Long#MAX_VALUE} milliseconds)
   * @return a future notified on operation completed
   */
  @GenIgnore(GenIgnore.PERMITTED_TYPE)
  Future<KafkaConsumerRecords<K, V>> poll(Duration timeout);

}
