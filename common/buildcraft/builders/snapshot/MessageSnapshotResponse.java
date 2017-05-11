package buildcraft.builders.snapshot;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public class MessageSnapshotResponse implements IMessage {
    private Snapshot snapshot;

    public MessageSnapshotResponse() {
    }

    public MessageSnapshotResponse(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        new PacketBuffer(buf).writeCompoundTag(Snapshot.writeToNBT(snapshot));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            snapshot = Snapshot.readFromNBT(new PacketBuffer(buf).readCompoundTag());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public enum Handler implements IMessageHandler<MessageSnapshotResponse, IMessage> {
        INSTANCE;

        @Override
        public IMessage onMessage(MessageSnapshotResponse message, MessageContext ctx) {
            ClientSnapshots.INSTANCE.onSnapshotReceived(message.snapshot);
            return null;
        }
    }
}
