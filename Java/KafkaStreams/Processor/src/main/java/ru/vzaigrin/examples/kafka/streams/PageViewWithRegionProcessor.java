package ru.ibs.kafka.advanced.streams;

import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;

public class PageViewWithRegionProcessor implements Processor<Long, PageView, String, String> {
    private KeyValueStore<Long, String> kvUserProfilesStore;
    private ProcessorContext<String, String> context;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        this.context = context;
        kvUserProfilesStore = context.getStateStore("userProfilesStore");
    }

    @Override
    public void process(final Record<Long, PageView> record) {
        Long id = record.key();
        String regionFromStore = kvUserProfilesStore.get(id);
        String region = regionFromStore == null ? "NA" : regionFromStore;
        String page = record.value().getPage().toString();
        context.forward(new Record<>(region, page, record.timestamp()));
    }

    @Override
    public void close() {
    }
}