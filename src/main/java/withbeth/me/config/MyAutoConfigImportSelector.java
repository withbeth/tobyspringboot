package withbeth.me.config;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyAutoConfigImportSelector implements DeferredImportSelector  {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
            "withbeth.me.config.autoconfig.DispatcherServletConfig",
            "withbeth.me.config.autoconfig.TomcatWebServerConfig"
        };
    }
}
