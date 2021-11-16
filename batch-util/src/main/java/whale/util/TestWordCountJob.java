package whale.util;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class TestWordCountJob {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getCheckpointConfig().setCheckpointInterval(60000);
        DataStreamSource<String> localhostDataStream = env.socketTextStream("localhost", 9910);
        SingleOutputStreamOperator<Tuple2<Long, Long>> flatMap = localhostDataStream.flatMap(new FlatMapFunction<String, Tuple2<Long, Long>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<Long, Long>> collector) throws Exception {
                String[] s1 = s.split(" ");
                collector.collect(new Tuple2<Long, Long>(Long.valueOf(s1[0]), Long.valueOf(s1[1])));
            }
        });
        SingleOutputStreamOperator<Tuple2<Long, Long>> reduce = flatMap.keyBy(value -> value.f0)
                .reduce((a, b) -> {
                    return new Tuple2<>(a.f0, a.f1 + b.f1);
                });
        reduce.print();
        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}