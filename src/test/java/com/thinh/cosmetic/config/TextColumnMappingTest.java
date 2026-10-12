package com.thinh.cosmetic.config;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.type.BasicType;
import org.hibernate.type.descriptor.java.StringJavaType;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TextColumnMappingTest {
    @Test
    void allLongTextFieldsUseTextAndJdbcStringsWithTheStandardPostgresDialect() throws Exception {
        try (var registry = new StandardServiceRegistryBuilder()
                .applySetting(JdbcSettings.DIALECT, PostgreSQLDialect.class.getName())
                .applySetting(JdbcSettings.ALLOW_METADATA_ON_BOOT, false)
                .build()) {
            var sources = new MetadataSources(registry);
            var scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
            var entities = scanner.findCandidateComponents("com.thinh.cosmetic.domain.entity");
            assertThat(entities).isNotEmpty();
            for (var entity : entities) {
                var entityClass = Class.forName(entity.getBeanClassName());
                sources.addAnnotatedClass(entityClass);
                for (var field : entityClass.getDeclaredFields()) {
                    assertThat(field.isAnnotationPresent(Lob.class))
                            .as("%s.%s must not use a JDBC LOB", entityClass.getSimpleName(), field.getName())
                            .isFalse();
                }
            }
            var metadata = sources.buildMetadata();
            var fields = Map.of(
                    "account.AuditLogEntity", new String[]{"content"},
                    "catalog.BrandEntity", new String[]{"description"},
                    "catalog.ProductEntity", new String[]{"description", "mainIngredients", "uses"},
                    "returns.ReturnRequestEntity", new String[]{"description"},
                    "review.ReviewEntity", new String[]{"content"});
            String content = "Mỹ phẩm dưỡng ẩm — tiếng Việt. ".repeat(2000);
            for (var entry : fields.entrySet()) {
                var entity = metadata.getEntityBinding("com.thinh.cosmetic.domain.entity." + entry.getKey());
                for (var field : entry.getValue()) {
                    var property = entity.getProperty(field);
                    assertThat(property.getColumns().getFirst().getSqlType(metadata)).isEqualTo("text");
                    var jdbcType = ((BasicType<?>) property.getType()).getJdbcType();
                    var statement = mock(PreparedStatement.class);
                    var resultSet = mock(ResultSet.class);
                    when(resultSet.getString(1)).thenReturn(content);
                    jdbcType.getBinder(StringJavaType.INSTANCE).bind(statement, content, 1, null);
                    assertThat(jdbcType.getExtractor(StringJavaType.INSTANCE).extract(resultSet, 1, null))
                            .isEqualTo(content);
                    verify(statement).setString(1, content);
                    verify(resultSet).getString(1);
                    verify(resultSet).wasNull();
                    verifyNoMoreInteractions(statement, resultSet);
                }
            }
        }
    }
}
