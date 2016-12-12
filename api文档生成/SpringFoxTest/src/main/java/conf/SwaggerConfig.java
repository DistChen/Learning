package conf;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



import static com.google.common.collect.Lists.newArrayList;


@Configuration
//@EnableWebMvc//非springboot项目需要添加此注释
@EnableSwagger2//开启swagger2.0
@ComponentScan(basePackages ={"com.yzy.controller"})
/**
 *
 * @author yzy
 */
public class SwaggerConfig {

    /**
     * Every Docket bean is picked up by the swagger-mvc framework - allowing for multiple
     * swagger groups i.e. same code base multiple swagger resource listings.
     */

    @Bean

    public Docket customDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("");
    }

    //配置全内容
//    @Bean
//    public Docket petApi() {
//        return new Docket(DocumentationType.SWAGGER_2)//<3>
//                .select()//<4>
//                .apis(RequestHandlerSelectors.any())//<5>
//                .paths(PathSelectors.any())//<6>
//                .build()//<7>
//                .pathMapping("")//<8>
////                .directModelSubstitute(LocalDate.class,
////                        String.class)//<9>
//                .genericModelSubstitutes(ResponseEntity.class)
////                .alternateTypeRules(
////                        newRule(typeResolver.resolve(DeferredResult.class,
////                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
////                                typeResolver.resolve(WildcardType.class)))//<10>
//                .useDefaultResponseMessages(false)//<11>
//                .globalResponseMessage(RequestMethod.POST,//<12>
//                        newArrayList(new ResponseMessageBuilder()
//                                .code(500)
//                                .message("500 message")
//                                .responseModel(new ModelRef("Error"))//<13>
//                                .build()))
//                //.securitySchemes(newArrayList(apiKey()))//<14>
//                //.securityContexts(newArrayList(securityContext()))//<15>
//                .enableUrlTemplating(true)//<21>
//                .globalOperationParameters(//<22>
//                        newArrayList(new ParameterBuilder()
//                                .name("someGlobalParameter")
//                                .description("Description of someGlobalParameter")
//                                .modelRef(new ModelRef("string"))
//                                .parameterType("query")
//                                .required(true)
//                                .build()))
//                .tags(new Tag("Pet Service", "All apis relating to pets")) // <25>
//                //.additionalModels(typeResolver.resolve(AdditionalModel.class)) //<26>
//                ;
//    }
}