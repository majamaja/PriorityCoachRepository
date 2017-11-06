package com.futuristlabs.utils.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;

@Component
public class VelocityTemplates {

    private final VelocityEngine ve;

    public VelocityTemplates() {
        ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
    }

    public String merge(String templatePath, Map<String, Object> context) {
        final Template template = ve.getTemplate(templatePath);
        final VelocityContext velocityContext = new VelocityContext(context);

        return merge(template, velocityContext);
    }

    private String merge(Template template, VelocityContext velocityContext) {
        final StringWriter writer = new StringWriter();
        template.merge(velocityContext, writer);
        return writer.toString();
    }
}
