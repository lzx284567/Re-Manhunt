package rbtree.manhunt;

import org.bukkit.plugin.Plugin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ParseLangFile {
    public static JsonObject loadLanguage() {
        Plugin manhunt = Manhunt.getManhunt();
        try (InputStream inputStream = ParseLangFile.class.getClassLoader().getResourceAsStream("lang/zh_cn.json")) {
            if (inputStream == null) {
                manhunt.getLogger().severe("找不到语言文件！");
                return null;
            }

            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            return JsonParser.parseReader(reader).getAsJsonObject();

        } catch (Exception e) {
            manhunt.getLogger().severe("加载语言文件时出错: " + e.getMessage());
            return null;
        }
    }
}