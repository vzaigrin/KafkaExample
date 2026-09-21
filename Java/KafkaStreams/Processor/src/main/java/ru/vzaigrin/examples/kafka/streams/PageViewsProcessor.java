package ru.vzaigrin.examples.kafka.streams;

import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;

public class PageViewsProcessor  implements Processor<String, String, String, PageViewWithRegion> {
    private KeyValueStore<String, Long> kvPageViewStore;
    private ProcessorContext<String, PageViewWithRegion> context;

    @Override
    public void init(ProcessorContext<String, PageViewWithRegion> context) {
        this.context = context;
        kvPageViewStore = context.getStateStore("pageViewStore");
    }

    @Override
    public void process(final Record<String, String> record) {
        String region = record.key();
        Long preCount = kvPageViewStore.get(region);
        long count = preCount == null ? 1L : preCount + 1L;
        kvPageViewStore.put(region, count);

        PageViewWithRegion pageViewWithRegion = new PageViewWithRegion();
        pageViewWithRegion.setRegion(region);
        pageViewWithRegion.setPages(count);

        context.forward(new Record<>(region, pageViewWithRegion, record.timestamp()));
    }

    @Override
    public void close() {
    }
}