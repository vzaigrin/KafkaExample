package ru.vzaigrin.examples.kafka.streams;

import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.processor.api.Processor;

public class userProfilesProcessor implements Processor<Long, UserProfile, Long, String> {
    private KeyValueStore<Long, String> userProfilesStore;

    @Override
    public void init(final ProcessorContext<Long, String> context) {
        userProfilesStore = context.getStateStore("userProfilesStore");
    }

    @Override
    public void process(final Record<Long, UserProfile> record) {
        String region = record.value().getRegion().toString();
        userProfilesStore.put(record.key(), region);
    }

    @Override
    public void close() {
    }
}
