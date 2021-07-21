package br.com.sgstudio.converter;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public final class YAML2HTTPConverter extends AbstractJackson2HttpMessageConverter {
    public YAML2HTTPConverter() {
        super(new YAMLMapper(), MediaType.parseMediaType("application/x-yaml"));
    }
}
