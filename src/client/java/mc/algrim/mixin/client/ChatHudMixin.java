package mc.algrim.mixin.client;

import mc.algrim.fabric.chat.AlgrimChatClient;
import mc.algrim.fabric.chat.ChatProcessor;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(ChatHud.class)
public class ChatHudMixin {
    @ModifyVariable(
        method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V",
        at = @At("HEAD"),
        argsOnly = true
    )
    private Text onClientChat(Text message) {
        try {
            return ChatProcessor.INSTANCE.execute(message);
        } catch (Exception e) {
            AlgrimChatClient.getLogger().error("There was an error applying style", e);
            return message;
        }

    }
}
