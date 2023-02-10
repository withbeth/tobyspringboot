package withbeth.me.helloboot.factorybean;

import org.springframework.beans.factory.FactoryBean;

import java.security.MessageDigest;

public class MessageDigestFactoryBean implements FactoryBean<MessageDigest> {

    @Override
    public MessageDigest getObject() throws Exception {
        return MessageDigest.getInstance("MD5");
    }

    @Override
    public Class<?> getObjectType() {
        return MessageDigest.class;
    }
}
