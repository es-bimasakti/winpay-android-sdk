package id.winpay.winpaysdk.main.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.winpay.winpaysdk.main.item.Channel;
import id.winpay.winpaysdk.main.item.ChannelGroup;
import id.winpay.winpaysdk.util.Var;

/**
 * Created by Dian Cahyono on 08/10/18.
 */
public final class ToolbarMessage extends SuperMessage {

    public ToolbarMessage() {
        super();
    }

    public ToolbarMessage(Map<String, Object> map) {
        super(map);
    }

    public void setGrouping(int group) {
        setItem("group", group);
    }

    public List<ChannelGroup> getListChannels() {
        ChannelGroup group = new ChannelGroup("WINPAY");
        insertToGroup(group, Var.toArrayList(getDataAsMap().get("products")));

        List<ChannelGroup> channelGroups = new ArrayList<>();
        channelGroups.add(group);

        return channelGroups;
    }

    public List<ChannelGroup> getGroupChannels() {
        List<ChannelGroup> channelGroups = new ArrayList<>();
        Map products = Var.toHashMap(getDataAsMap().get("products"));
        for (Object i : products.keySet()) {
            Object o = products.get(i);
            if (!(o instanceof List)) {
                continue;
            }

            ChannelGroup group = new ChannelGroup(Var.toString(i, ""));
            insertToGroup(group, (List) o);
            if (!group.isEmpty()) {
                channelGroups.add(group);
            }
        }

        return channelGroups;
    }

    private void insertToGroup(ChannelGroup group, List channels) {
        for (Object c : channels) {
            if (!(c instanceof Map)) {
                continue;
            }

            Map map = (Map) c;
            String pc = Var.toString(map.get("payment_code"), "");

            Channel channel = new Channel(Var.toBoolean(map.get("is_direct"), false));
            channel.setPayment_code(pc);
            channel.setPayment_logo(Var.toString(map.get("payment_logo"), ""));
            channel.setPayment_name(Var.toString(map.get("payment_name"), ""));
            channel.setPayment_url(Var.toString(map.get("payment_url_v2"), ""));
            channel.setDescription(Var.toString(map.get("payment_description"), ""));

            group.addChannel(channel);
        }
    }

}
