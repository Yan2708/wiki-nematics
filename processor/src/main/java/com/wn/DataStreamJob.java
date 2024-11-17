package com.wn;

import com.wn.model.ArticleChange;
import com.wn.model.ArticleCount;
import com.wn.utils.Deserializer;
import com.wn.utils.ModifArticleWindowFunction;
import com.wn.utils.MongoDBSink;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class DataStreamJob {
	private static final Logger LOG = LoggerFactory.getLogger(DataStreamJob.class);

	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		KafkaSource<ArticleChange> source = KafkaSource.<ArticleChange>builder()
				.setBootstrapServers("broker:19092")
				.setTopics("wikipedia.changes")
				.setGroupId("flink-consumer-group")
				.setStartingOffsets(OffsetsInitializer.earliest())
				.setValueOnlyDeserializer(new Deserializer<>(ArticleChange.class))
				.build();

		WatermarkStrategy<ArticleChange> watermarkStrategy = WatermarkStrategy
		.<ArticleChange>forBoundedOutOfOrderness(Duration.ofSeconds(5))
		.withTimestampAssigner((event, timestamp) -> event.getTimestamp());

		DataStream<ArticleChange> stream = env.fromSource(
				source,
				watermarkStrategy,
				"Kafka Source"
		);


		DataStream<ArticleCount> count = stream
				.keyBy(ArticleChange::getTitle)
				.window(SlidingEventTimeWindows.of(Duration.ofMinutes(10), Duration.ofMinutes(1)))
				.apply(new ModifArticleWindowFunction());

		MongoDBSink<ArticleCount> mongoDBSink = new MongoDBSink<>(
				"mongodb://root:rootpass@mongodb:27017",
				"wikipedia",
				"ArticlesCount"
		);



		stream
			.keyBy(ArticleChange::getTitle) // Grouping by article title
			.window(SlidingEventTimeWindows.of(Duration.ofMinutes(10), Duration.ofMinutes(1)))
			.apply(new ModifArticleWindowFunction())
			.map(articleChange -> {
                // Log or print the key and its associated element
                String logMessage = String.format("Key: %s, ArticleChange: %s", articleChange.getTitle(), articleChange);
                LOG.info(logMessage); // Use logging
                return logMessage;     // Return message for printing
            })
			.print();

//		stream.map(record -> {
//			LOG.info("Received record: {}", record);
//			return record;
//		}).print();

		count.sinkTo(mongoDBSink);


		env.execute("Simple Kafka Consumer");
	}
}
