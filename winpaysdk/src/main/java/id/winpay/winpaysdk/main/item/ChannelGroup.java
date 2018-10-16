package id.winpay.winpaysdk.main.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class ChannelGroup {
    private final String group;
    private final List<Channel> channels;

    public ChannelGroup(String group) {
        this.group = group;
        this.channels = new ArrayList<>();
    }

    public boolean isEmpty() {
        return group.isEmpty();
    }

    public String getGroup() {
        return group;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void addChannel(Channel channel) {
        this.channels.add(channel);
    }

    public void clearChannel() {
        this.channels.clear();
    }
}
