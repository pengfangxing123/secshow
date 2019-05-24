package cn.com.jrj.vtmatch.basicmatch.matchcore;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * mysql 代码生成器
 * </p>
 *
 * @author jobob
 * @since 2018-09-12
 */
public class MysqlGenerator {

    /**
     * RUN THIS
     */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        // 是否覆盖同名文件，默认是false
        gc.setFileOverride(true);
        gc.setOutputDir(projectPath + "/match-core/src/main/java");
        gc.setAuthor("jobob");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://172.16.198.3:3306/ycmatch?useUnicode=true&characterEncoding=utf8&useSSL=false");
        // dsc.setSchemaName("public")
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("JRJ_ycmatch_Test_W");
        dsc.setPassword("1b6r3yDkhQTlcEat10w0");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("");
        pc.setParent("");
        pc.setMapper("cn.com.jrj.vtmatch.basicmatch.matchcore.dao");
        pc.setController("cn.com.jrj.vtmatch.basicmatch.matchcore.web.controller.match");
        pc.setService("cn.com.jrj.vtmatch.basicmatch.matchcore.service");
        pc.setServiceImpl("cn.com.jrj.vtmatch.basicmatch.matchcore.service.impl");
        pc.setEntity("cn.com.jrj.vtmatch.basicmatch.matchcore.entity.match");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/match-core/src/main/java/cn/com/jrj/vtmatch/basicmatch/matchcore/dao/"  + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("cn.com.jrj.vtmatch.basicmatch.matchcore.entity.base.BaseEntity");
        strategy.setEntityLombokModel(true);
        //strategy.setSuperControllerClass("com.baomidou.mybatisplus.samples.generator.common.BaseController")
        strategy.setInclude("win_send_sms_record");
//        strategy.setInclude("win_fragment","win_match_award","win_match_join","win_match_basic",
//                "win_match_rule","win_match_team_basic","win_match_team_join",
//                "win_match_team_member_stat_rank","win_match_team_stat_rank","win_match_user_stat_rank",
//                "win_send_red_detail","win_stat_config",
//                "win_trading","win_user","win_user_account","win_user_account_stat");
        strategy.setSuperEntityColumns("id", "gmt_create", "gmt_modified");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix("win_");
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
