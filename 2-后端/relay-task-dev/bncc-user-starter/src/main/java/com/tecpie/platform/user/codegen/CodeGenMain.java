package com.tecpie.platform.user.codegen;

import com.tecpie.codegen.CodeGeneratorParam;
import com.tecpie.codegen.TecpieCodeGenerator;

/**
 * 代码生成类，使用时根据注释修改相应内容
 *
 * @author tecpie
 */
public class CodeGenMain {

    public static void main(String[] args) {
        CodeGeneratorParam param = new CodeGeneratorParam();

        // 数据库连接配置
        param.setDbUrl("jdbc:mysql://localhost:3308/platform?useSSL=false&allowPublicKeyRetrieval=true&useTimezone=true&serverTimezone=GMT&useUnicode=true&characterEncoding=UTF-8");
        param.setDbUsername("root");
        param.setDbPassword("qwer123$");

        // 作者信息
        param.setAuthor("tecpie");

        /// 微服务项目 - 当项目划分多个子项目时填写
        // param.setBaseApiDirOffset("demo-project-api");
        // param.setBaseServiceDirOffset("demo-project-service");

        // 业务模块生成的基础包路径
        param.setPackageName("com.tecpie.platform.user.business.system");

        // 表名转换为类名时去除前缀
        param.setTablePrefix("_t");

        /**
         * 1、一次性生成多个业务模块代码时, 可以多次调用 put 方法，key 会作为模块名
         * 2、value 为二维数组，其中可填写两个数组，每个数组中可传入多个元素
         *    第一个数组 - 模块主表，会生成完成的模块代码
         *    第二个数组 - 模块从表/关系表，会生成必要的模块代码 (例如不会对该表生成增删改查的 http 接口)
         */
        param.getTableCollection().put("organization", new String[][]{
                {"sys_user_unit"}
        });
        /// 可以一次性生成多个业务模块代码
        // param.getTableCollection().put("demo_module2", new String[][]{});

        // 执行代码生成
        TecpieCodeGenerator.generator(param);
    }
}