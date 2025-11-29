观前提醒：作者对工程一窍不通，知之甚少，如看到大粪代码请勿高血压（不是）

插件具体介绍(Paper 1.21.5)

本插件复刻了 Dream 的猎人游戏玩法，且加入了更多功能，奖励机制和配置。有一些配置是专门为小游戏服务器打造的。

## 高度自定义化

### 自定义游戏结束条件
- 获得某进度
- 获得某方块
- 通关
- 更多内容敬请期待...

### 自定义物品/功能禁用
- 床/重生锚 爆炸
- 射出药水箭
- 是否向所有玩家展示某玩家取得进度的消息

### 自定义任何玩家生命数量

## 奖励机制

见 [奖励公式](https://www.luogu.me/paste/m1dfbqy2)

## 更多配置

- 在游戏开始前，提前加载出生点周围区块
- 开启白名单，其他人加入需发送申请
- “房主”（游戏主持人）设定，方便游玩

## 小功能：自动重启（需要外部.py，后果自负！）

### 使用效果

在游戏结束后，自动 **删除原版维度存档，Multiverse 三维度设置（为了刷新出生点）**，并重启服务器，以达到 24*7 不间断游戏的效果。

### 使用方式

下载.py，将其移动到服务器文件夹下即可。注意 py 文件名称要与 config.yml中一致。
需要安装库：psutil pyyaml

当然您也可以用您自己的python脚本！

## 其他内容

### 前置插件（均非必须安装）：
- PlaceholderAPI
- MultiverseCore
- Vault

### 权限：
- manhunt.cmd（是否能够执行/manhunt指令）

### Placeholders（需要PlaceholderAPI插件）
- manhunt_runnerscount 跑者数量
- manhunt_allow_玩家名 此玩家是否被房主允许加入房间，是返回true，否返回false
- manhunt_loop_player_name_数字 在一个包含所有玩家的列表中，找到编号为此数字的玩家名。保证列表有序性。
- manhunt_loop_runner_name_数字 同上，但是在跑者列表中找。
- manhunt_runner_玩家名 此玩家是否是跑者，是返回true，不是返回false
- manhunt_tracking 当以玩家身份尝试获取此占位符的值时，如该玩家是猎人，会返回该玩家追踪的玩家，否则返回“无”。
- manhunt_playersinroom 返回当前服务器玩家数量。
- manhunt_fangzhu 返回房主名字。
- manhunt_endmethod 返回游戏结束方式
- manhunt_time 返回目前游戏时间（游戏已开始的时间或游戏结束后距离服务器关闭的时间）
- manhunt_disabled_info 返回被禁用的玩法（如 “禁 床炸 重生锚炸”）
