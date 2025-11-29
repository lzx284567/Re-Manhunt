package rbtree.manhunt;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;

public class Restart {
    static public void restart(){
        try {
            Bukkit.getConsoleSender().sendMessage("正在尝试运行 "+Config.getRestartBatPosition()+"...");
            ProcessBuilder pb = new ProcessBuilder();
            File serverFolder = Bukkit.getServer().getWorldContainer();
            Manhunt.getManhunt().getLogger().info(serverFolder.getAbsolutePath());
            String os = System.getProperty("os.name").toLowerCase();
            String[] command;
            if (os.contains("win")) {
                command = new String[] {
                        "cmd.exe", "/c", "start", "cmd.exe", "/c",
                        "python", Config.getRestartBatPosition()
                };
            } else {
                command = new String[] {
                        "xterm", "-e", "python3", Config.getRestartBatPosition()
                };
            }
            Manhunt.getManhunt().getLogger().info(Arrays.toString(command));
            pb.directory(serverFolder);
            pb.command(command);

            Process process = pb.start();
            Thread outputThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Manhunt.getManhunt().getLogger().info("[Python Info] " + line);
                    }
                } catch (IOException e) {
                    Manhunt.getManhunt().getLogger().severe(e.getMessage());
                }
            });
            outputThread.start();
            Thread errorThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Manhunt.getManhunt().getLogger().severe("[Python Error] " + line);
                    }
                } catch (IOException e) {
                    Manhunt.getManhunt().getLogger().info(e.getMessage());
                }
            });
            errorThread.start();

            Bukkit.getScheduler().runTaskLater(Manhunt.getManhunt(), () -> Bukkit.getServer().shutdown(), 50);

        } catch (IOException e){
            Manhunt.getManhunt().getLogger().log(Level.SEVERE,"无法打开重启脚本，请检查config.yml中填写的脚本位置是否正确！");
            Manhunt.getManhunt().getLogger().log(Level.SEVERE,"注意：脚本应该在服务器文件夹内！");
            Manhunt.getManhunt().getLogger().log(Level.SEVERE,"示例：如脚本位置为 服务器文件夹/restart/脚本名称.py");
            Manhunt.getManhunt().getLogger().log(Level.SEVERE,"则脚本名称应填：restart/脚本名称.py");
        }

    }
}
