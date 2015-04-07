package game.world.net;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import game.world.protobuf.ResultPro;
import game.world.utils.ErrorsUtil;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/3/6 10:29
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Packet {
    private short cmd;

    private MessageLite ret;

    private MessageLite body;

    public Packet(short cmd){
        this.cmd = cmd;
    }

    public MessageLite getRet(){
        if (ret == null)
            ret = ResultPro.Result.getDefaultInstance();
        try {
            ResultPro.Result.Builder builder = ResultPro.Result.parseFrom(ret.toByteArray()).toBuilder();
            if (!builder.hasMsg()){
                builder.setMsg(ErrorsUtil.getErrorDesc(builder.getCode(), Locale.SIMPLIFIED_CHINESE));
                return builder.build();
            }
        } catch (InvalidProtocolBufferException e) {
            log.error("获取ret异常!", e);
        }
        return ret;
    }

    public int calcSize(){
        int size = 2; //cmd
        if (getRet() != null){
            size += getRet().toByteArray().length;
        }
        if(getBody() != null) {
            size += getBody().toByteArray().length;
        }
        return size;
    }

    public void write(ByteBuf byteBuf){
        byteBuf.writeShort(calcSize()); //输出总长度
        byteBuf.writeShort(cmd); //命令

        if(getRet() != null) {
            byteBuf.writeBytes(getRet().toByteArray());
        }

        if(getBody() != null) {
            byteBuf.writeBytes(getBody().toByteArray());
        }
    }
}
