### 依赖与许可

- Minecraft `1.6.4-MITE`
- FishModLoader `3.4.2`
- ManyLib `2.3.1`
- ModMenu `10.1.1`
- Java `17+`
- 许可证：MIT License


# 更新日志

---

## 版本历史

### v1.1.0（样式扩展、配置分页与构建修复）

#### 新增功能

- **方块/实体独立样式**：方块选中框与实体碰撞箱可以分别配置颜色、线宽、透明度、动画和填充
- **预设主题**：新增 `Custom`、`Classic Black`、`Red / Blue`、`Rainbow`、`Low Alpha`、`Bold` 预设
- **填充模式**：新增 `Outline Only`、`Fill Only`、`Outline + Fill`
- **可见性控制**：描边与填充可分别设置为正常遮挡或透墙可见
- **动画模式**：新增 `Animated`、`Static Gradient`、`Breathing Alpha`、`Off`
- **配置分页**：配置界面拆分为 `Common`、`Gradient`、`Solid`、`Hotkey` 四个分页按钮
- **多语言补充**：新增俄语语言文件，并扩展中英文配置文本
- **文档与许可**：README 增加中文说明，项目添加 MIT 许可证

#### 修复与兼容性

- **方块禁用行为修复**：关闭自定义方块高亮时不再吞掉原版选中框
- **渲染状态保护**：方块与实体渲染路径都使用 OpenGL 属性栈和 `try/finally` 恢复状态

### v1.0.2（实体碰撞箱模式与颜色模式更新）

#### 新增功能

- **实体碰撞箱渲染模式**：新增 `Modern` 和 `Old` 两种模式
- **现代碰撞箱渲染**：支持在实体渲染完成后再绘制碰撞箱线框，避免旧版前置渲染带来的遮挡观感问题
- **颜色模式切换**：新增 `Gradient` 与 `Solid` 模式，并提供独立的单颜色设置

#### 实现调整

- **共享描边逻辑**：方块选中框与实体碰撞箱共用同一套描边绘制与颜色计算逻辑
- **配置项扩展**：新增 `entityHitboxRenderMode`、`colorMode` 与 `solidColor` 等配置项，并补充中英文配置文本

### v1.0.0（初始版本）

#### 新增功能

- **方块选中框渐变渲染**：将原版单色选中框替换为彩色渐变效果
- **实体碰撞箱渲染**：为 F3+B 显示的实体边界框添加渐变效果
- **渐变色动画**：实现动态渐变循环动画
- **RGB 插值**：支持 RGB 模式的颜色过渡
- **HSV 插值**：支持 HSV 模式的颜色过渡（更鲜艳的彩虹效果）
- **配置系统**：集成 ManyLib 提供可视化配置界面
- **ModMenu 支持**：支持从 ModMenu 打开配置
- **热键支持**：默认 M+B 快捷键打开配置

#### 配置项

- `enableBlockOutline`：启用/禁用方块选中框
- `enableEntityOutline`：启用/禁用实体碰撞箱
- `gradientStartColor`：渐变起始颜色
- `gradientEndColor`：渐变结束颜色
- `gradientMode`：渐变插值模式（RGB/HSV）
- `cycleSeconds`：循环周期
- `lineWidth`：线宽
- `alpha`：透明度

---
