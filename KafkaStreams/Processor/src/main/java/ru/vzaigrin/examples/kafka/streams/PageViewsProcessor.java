package ru.vzaigrin.examples.kafka.streams;

import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;

public class PageViewsProcessor  implements Processor<Long, PageView, String, PageViewWithRegion> {
    private KeyValueStore<Long, String> userProfilesStore;
    private KeyValueStore<String, Long> pageViewStore;
    private ProcessorContext<String, PageViewWithRegion> context;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        this.context = context;
        userProfilesStore = context.getStateStore("userProfilesStore");
        pageViewStore = context.getStateStore("pageViewStore");
    }

    @Override
    public void process(final Record<Long, PageView> record) {
        Long id = record.key();
        String region = userProfilesStore.get(id);
        Long count = pageViewStore.get(region);

        if (count == null) count = 1L;
        else count = count + 1;
        pageViewStore.put(region, count);

        PageViewWithRegion pageViewWithRegion = new PageViewWithRegion();
        pageViewWithRegion.setRegion(region);
        pageViewWithRegion.setPages(count);

        long timestamp = System.currentTimeMillis();
        context.forward(new Record<>(region, pageViewWithRegion, timestamp));
    }

    @Override
    public void close() {
    }
}