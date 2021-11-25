package cn.maple.mongodb.datasource.properties;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 多数据源属性
 */
@Data
@SuppressWarnings("all")
@Component
@NacosPropertySource(dataId = "mongodb.yml", autoRefreshed = true, groupId = "DEFAULT_GROUP")
@NacosConfigurationProperties(prefix = "mongodb", dataId = "mongodb.yml", autoRefreshed = true)
public class GXMongoDynamicDataSourceProperties {
    protected Map<String, GXMongoDataSourceProperties> datasource = new LinkedHashMap<>();
}
