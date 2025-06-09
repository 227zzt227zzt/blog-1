package com.zzt.blog.config;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.zzt.blog.enums.ArticleStatus;
import com.zzt.blog.enums.CommentStatus;
import com.zzt.blog.enums.UserStatus;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 227
 */
@Configuration
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
public class MybatisEnumConfig {

    private final SqlSessionFactory sqlSessionFactory;

    public MybatisEnumConfig(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @PostConstruct
    public void registerEnums() {
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();

        // 创建Set并添加枚举类
        Set<Class<?>> enumClasses = new HashSet<>();
        enumClasses.add(ArticleStatus.class);
        enumClasses.add(CommentStatus.class);
        enumClasses.add(UserStatus.class);

        enumClasses.forEach(enumClass -> {
            if (hasEnumValueField(enumClass)) {
                typeHandlerRegistry.register(enumClass, MybatisEnumTypeHandler.class);
            }
        });
    }

    private boolean hasEnumValueField(Class<?> enumClass) {
        return Arrays.stream(enumClass.getDeclaredFields())
                .anyMatch(field -> field.isAnnotationPresent(EnumValue.class));
    }
}