package game.world.utils;

import com.google.protobuf.MessageLite;

import java.lang.reflect.Method;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/17 9:39
 */
public class BasicProto {
    private final String SET = "set";
    private final String GET = "get";
    public void parseProto(MessageLite proto){
        Class<? extends BasicProto> c = getClass();
        Class<? extends MessageLite> aClass = proto.getClass();
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (name.startsWith(SET) && parameterTypes.length == 1){
                name = name.substring(3);
                try {
                    Method m = aClass.getMethod(GET + name);
                    if (m.getReturnType().equals(parameterTypes[0])){
                        method.invoke(this, m.invoke(proto));
                    }
                } catch (Exception e) {}
            }
        }
    }

    public void parseObject(MessageLite.Builder builder){
        Class<? extends BasicProto> c = getClass();
        Class<? extends MessageLite.Builder> aClass = builder.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (name.startsWith(SET) && parameterTypes.length == 1){
                name = name.substring(3);
                try {
                    Method m = c.getMethod(GET + name);
                    if (m.getReturnType().equals(parameterTypes[0])){
                        method.invoke(builder, m.invoke(this));
                    }
                } catch (Exception e) {}
            }
        }
    }
}
