package yuki.mirai.demo;

import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import yuki.mirai.demo.pojo.set.Config;
import yuki.mirai.demo.pojo.set.ConfigVariable;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(DemoApplication.class, args);

        Yaml yml = new Yaml(new Constructor(Config.class));
        Set.CONFIG = yml.load(FileUtils.openInputStream(new File("config.yml")));
        Set.CONFIG_VARIABLE = new ConfigVariable();
        if (OnStartLogin()){
            RunVariable.BOT.getLogger().info("config.yml 存在账号密码,使用内账号密码登录");
        }else {
            System.out.println("config.yml不存在默认账号密码,请通过WebUI登录");
        }


        HandlerSetter.setHandler();
    }

    private static Boolean OnStartLogin() {
        if (Set.CONFIG.Bot.QQ != null && Set.CONFIG.Bot.Password != null) {
            RunVariable.BOT = BotFactory.INSTANCE.newBot(Set.CONFIG.Bot.QQ,Set.CONFIG.Bot.Password,new BotConfiguration(){
                {
                    setWorkingDir(new File(Set.CONFIG.Bot.Workspace));
                    setCacheDir(new File(Set.CONFIG.Bot.CacheDir));
                    setHeartbeatStrategy(HeartbeatStrategy.STAT_HB);
                    setProtocol(MiraiProtocol.ANDROID_PHONE);
                    fileBasedDeviceInfo("DeviceInfo.json");
                    enableContactCache();
                    setLoginCacheEnabled(true);
                }
            });
            RunVariable.BOT.login();
            return true;
        }else {
            return false;
        }

    }

}
